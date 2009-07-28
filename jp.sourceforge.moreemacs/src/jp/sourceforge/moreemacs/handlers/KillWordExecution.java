package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public final class KillWordExecution extends TextEditorExecution {

    @Override
    public void execute() throws BadLocationException {
        if (!textEditor.isEditable()) {
            return;
        }

        int current = cursor.offset();
        int next = ForwardWordExecution.getNextWordPosition(doc, current);
        String word = doc.get(current, next - current);
        Clipboard c = new Clipboard(window.getShell().getDisplay());
        c.setContents(new String[] { word }, 
                new Transfer[] { TextTransfer.getInstance() });
        doc.replace(current, next - current, "");
    }
}
