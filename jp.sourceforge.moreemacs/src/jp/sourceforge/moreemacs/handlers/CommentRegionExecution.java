package jp.sourceforge.moreemacs.handlers;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;


public final class CommentRegionExecution extends TextEditorExecution {
    @Override
    public void execute() throws Exception {
        Command command = getEnabledToggleCommentCommand();
        if(command == null) {
            return;
        }
        // if the selection is empty, the marked region will be new selection.
        getSelection(true);
        
        IHandlerService handlerService =
            (IHandlerService)textEditor.getSite().getService(IHandlerService.class);
        handlerService.executeCommand(command.getId(), null);

        ITextSelection selection = getSelection(false);
        textViewer.setSelectedRange(selection.getOffset()+selection.getLength(), 0);
    }
    
    private Command getEnabledToggleCommentCommand() {
        ICommandService commandService =
            (ICommandService)textEditor.getSite().getService(ICommandService.class);
        for(Command command: commandService.getDefinedCommands()) {
            if(isEnabledToggleCommentCommand(command)) {
                return command;
            }
        }
        return null;
    }
    
    private boolean isEnabledToggleCommentCommand(Command command) {
        return command.isEnabled() && command.getId().endsWith(".toggle.comment");
    }
}
