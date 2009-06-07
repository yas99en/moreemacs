package jp.sourceforge.moreemacs.handlers;

import java.util.ArrayList;
import java.util.List;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.ColumnUtils;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;
import jp.sourceforge.moreemacs.utils.DocumentTransaction;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;

public final class KillRectangleExecution extends RectangleExecution {

    @Override
    public void execute() throws BadLocationException {

        if(!textEditor.isEditable()) {
            return;
        }
        
        ITextSelection selection = getSelection(true);

        int start = selection.getOffset();
        int startRow = doc.getLineOfOffset(start);
        int startColumn = ColumnUtils.getColumn(doc, start, getTabStop());

        int end = start + selection.getLength();
        int endRow = doc.getLineOfOffset(end);
        int endColumn = ColumnUtils.getColumn(doc, end, getTabStop());
        
        if(startColumn > endColumn) {
            int work = startColumn;
            startColumn = endColumn;
            endColumn = work;
        }

        DocumentTransaction transaction = new DocumentTransaction(doc); 
        transaction.begin(DocumentRewriteSessionType.UNRESTRICTED_SMALL);
        try {
            List<String> rectangle = killRectangle(doc, startRow, startColumn, endRow, endColumn);
            setRectangle(rectangle);
        } finally {
            transaction.end();
        }
    }
    private List<String> killRectangle(IDocument doc,
            int startRow, int startColumn,
            int endRow, int endColumn)
            throws BadLocationException {

        List<String> rectangle = new ArrayList<String>();

        for(int i = startRow; i <= endRow; i++) {
            String str = killString(doc, i, startColumn, endColumn);
            rectangle.add(str);
        }
        return rectangle;
    }
    private String killString(IDocument doc, int row,
            int startColumn, int endColumn) throws BadLocationException {
        IRegion line = doc.getLineInformation(row);

        StringBuilder builder = new StringBuilder();
        int column = 0;
        int cutOffset = 0;
        int cutLength = 0;
        
        CharSequence seq = new DocumentCharSequence(doc, line.getOffset(), line.getLength());
        
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            if(column >= endColumn) {
                break;
            }
            int offset = line.getOffset() + itr.index();
            int codePoint = itr.next();

            int nextColumn = ColumnUtils.getNextColumn(column, codePoint, getTabStop());
            
            if(nextColumn < startColumn+1) {
                column = nextColumn;
                continue;
            }
            if(cutLength == 0) { 
                cutOffset = offset;
            }
            builder.appendCodePoint(codePoint);
            cutLength += Character.charCount(codePoint);
            column = nextColumn;
        }
        
        doc.replace(cutOffset, cutLength, "");
        cursor.move(cutOffset);

            
        for(int i = 0; i < endColumn-column; i++) {
            builder.append(' ');
        }
        
        return builder.toString();
    }

}
