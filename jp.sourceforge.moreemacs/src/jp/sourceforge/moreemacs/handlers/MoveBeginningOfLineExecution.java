package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;

public final class MoveBeginningOfLineExecution extends TextEditorExecution {
    @Override
    public void execute() throws BadLocationException {
        IRegion line = doc.getLineInformationOfOffset(cursor.offset());
        cursor.move(line.getOffset());
    }
}

