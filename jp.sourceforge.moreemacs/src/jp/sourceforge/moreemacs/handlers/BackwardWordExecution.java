package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public final class BackwardWordExecution extends TextEditorExecution {

	@Override
	public void execute() throws BadLocationException {
		int current = cursor.offset();
		cursor.move(getPreviousWordPosition(doc, current));
	}
	
	public static int getPreviousWordPosition(IDocument doc, int offset) throws BadLocationException {
        CharSequence seq = new DocumentCharSequence(doc, 0, offset);
        CodePointIterator itr = new CodePointIterator(seq, seq.length());

        for(; itr.hasPrevious(); ) {
            if (Character.isLetterOrDigit(itr.previous())) {
                itr.next();
                break;
            }
        }
        for(; itr.hasPrevious(); ) {
            if (!Character.isLetterOrDigit(itr.previous())) {
                itr.next();
                break;
            }
        }
        
       return itr.index();
	}

}
