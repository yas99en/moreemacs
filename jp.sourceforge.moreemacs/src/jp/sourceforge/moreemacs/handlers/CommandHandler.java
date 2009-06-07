package jp.sourceforge.moreemacs.handlers;

import java.util.regex.Pattern;

import jp.sourceforge.moreemacs.MoreEmacs;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public final class CommandHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

        Execution exe = newExecution(event);
        if(!exe.init(window)) {
            return null;
        }
        try {
            exe.execute();
        } catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e);
        }

        return null;
    }
    
    private Execution newExecution(ExecutionEvent event) throws ExecutionException {
        try {
            String className = getExecutionClassName(event);
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            if(!(obj instanceof Execution)) {
                throw new ExecutionException("the class "+clazz.getName()+
                        " does not implements Execution.");
            }
            return (Execution)obj;
        } catch (ClassNotFoundException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExecutionException(e.getMessage(), e);
        }
        
    }
    
    // naming strategy will be separated to another class.
    private static final String COMMAND_PREFIX_QUOTED = Pattern.quote(
            MoreEmacs.class.getPackage().getName());
    private static final String HANDLER_PREFIX = CommandHandler.class.getPackage().getName();
    private static final String HANDLER_SUFFIX = "Execution";
    private static String getExecutionClassName(ExecutionEvent event) {
        Command command = event.getCommand();
        String className = command.getId()
        .replaceFirst(COMMAND_PREFIX_QUOTED, HANDLER_PREFIX)
        +HANDLER_SUFFIX;
        
        
        return className;
    }

}
