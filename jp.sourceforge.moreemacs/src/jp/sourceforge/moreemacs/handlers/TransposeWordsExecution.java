package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;

public final class TransposeWordsExecution extends TextEditorExecution {
    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }

        int current = cursor.offset();
        int previousBegin = BackwardWordExecution.getPreviousWordPosition(doc, current);
        int previousEnd = ForwardWordExecution.getNextWordPosition(doc, previousBegin);
        int nextEnd = ForwardWordExecution.getNextWordPosition(doc, current);
        int nextBegin = BackwardWordExecution.getPreviousWordPosition(doc, nextEnd);
        
        if(nextBegin <= previousEnd) {
            return;
        }
        
        String previous = doc.get(previousBegin, previousEnd-previousBegin);
        String simbols = doc.get(previousEnd, nextBegin-previousEnd);
        String next = doc.get(nextBegin, nextEnd-nextBegin);

        doc.replace(previousBegin,
                nextEnd-previousBegin,
                next+simbols+previous);
        
    }
}
