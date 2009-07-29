package jp.sourceforge.moreemacs.handlers;

import org.eclipse.core.commands.Command;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;


public final class CommentRegionExecution extends TextEditorExecution {
    @Override
    public void execute() throws Exception {
        ICommandService commandService =
            (ICommandService)textEditor.getSite().getService(ICommandService.class);
        for(Command command: commandService.getDefinedCommands()) {
            if(!isEnabledToggleCommentCommand(command)) {
                continue;
            }
            
            // if the selection is empty, the marked region will be new selection.
            getSelection(true);
            
            IHandlerService handlerService =
                (IHandlerService)textEditor.getSite().getService(IHandlerService.class);
            handlerService.executeCommand(command.getId(), null);
            return;
        }
    }
    
    private boolean isEnabledToggleCommentCommand(Command command) {
        return command.isEnabled() && command.getId().endsWith(".toggle.comment");
    }
}
