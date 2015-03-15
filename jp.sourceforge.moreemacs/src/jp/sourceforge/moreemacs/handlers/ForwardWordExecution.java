package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public final class ForwardWordExecution extends TextEditorExecution {
    
    @Override
    public void execute() throws BadLocationException {
        int current = cursor.offset();
        cursor.move(getNextWordPosition(doc, current));
    }
    
    public static int getNextWordPosition(IDocument doc, int offset) throws BadLocationException {
        CharSequence seq = new DocumentCharSequence(doc, offset, doc.getLength()-offset);
        CodePointIterator itr = new CodePointIterator(seq);
        
        
        for(; itr.hasNext(); ) {
            int codePoint = itr.next();
            if (Character.isLetterOrDigit(codePoint)) {
                itr.previous();
                break;
            }
        }
        for(; itr.hasNext(); ) {
            if (!Character.isLetterOrDigit(itr.next())) {
                itr.previous();
                break;
            }
        }
        
        return offset + itr.index();
    }
}
