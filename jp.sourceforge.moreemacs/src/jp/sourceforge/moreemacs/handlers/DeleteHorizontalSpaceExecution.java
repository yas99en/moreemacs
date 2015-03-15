package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public final class DeleteHorizontalSpaceExecution extends TextEditorExecution {
    
    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }
        
        int current = cursor.offset();
        int start = skipBackwardSpaces(doc, current);
        int end = skipForwardSpaces(doc, current);
        doc.replace(start, end - start, "");
    }
    
    int skipBackwardSpaces(IDocument doc, int offset) throws BadLocationException {
        IRegion line = doc.getLineInformationOfOffset(offset);
        
        CharSequence seq = new DocumentCharSequence(doc, 
                line.getOffset(), offset-line.getOffset());
        
        int result = offset;
        for(CodePointIterator itr = new CodePointIterator(seq, seq.length()); itr.hasPrevious(); ) {
            int codePoint = itr.previous();
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            result = line.getOffset() + itr.index();
        }
        return result;
    }
    
    int skipForwardSpaces(IDocument doc, int offset) throws BadLocationException {
        IRegion line = doc.getLineInformationOfOffset(offset);
        CharSequence seq = new DocumentCharSequence(doc, 
                offset,line.getOffset()+line.getLength()-offset);
        int result = offset;
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int codePoint = itr.next();
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            result = offset + itr.index();
        }
        return result;
    }
}
