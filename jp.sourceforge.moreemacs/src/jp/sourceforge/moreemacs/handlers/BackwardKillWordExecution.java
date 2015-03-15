package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public final class BackwardKillWordExecution extends TextEditorExecution {

    @Override
    public void execute()throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }
        
        int current = cursor.offset();
        int previous = BackwardWordExecution.getPreviousWordPosition(doc, current);
        String word = doc.get(previous, current - previous);
        Clipboard c = new Clipboard(window.getShell().getDisplay());
        c.setContents(new String[] { word },
                new Transfer[] { TextTransfer.getInstance() });
        doc.replace(previous, current - previous, "");
        
    }
}
