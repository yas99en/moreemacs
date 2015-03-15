package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextUtilities;

public final class NewLineExecution extends TextEditorExecution {
    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }
        
        int offset = cursor.offset();
        String delim = TextUtilities.getDefaultLineDelimiter(doc);
        doc.replace(offset, 0, delim);
        cursor.move(offset+delim.length());
    }
}

