package jp.sourceforge.moreemacs.handlers;

import java.util.List;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.ColumnUtils;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;
import jp.sourceforge.moreemacs.utils.DocumentTransaction;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

public final class YankRectangleExecution extends TextEditorExecution {
    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }
        
        List<String> rectangle = RectangleStorage.getRectangle();
        if(rectangle == null) {
            return;
        }
        
        int current = cursor.offset();
        int row = doc.getLineOfOffset(current);
        int column = ColumnUtils.getColumn(doc, current, getTabStop());
        
        ensureLines(doc, row + rectangle.size());
        
        DocumentTransaction transaction = new DocumentTransaction(doc); 
        transaction.begin();
        try {
            yankRectangle(doc, row, column, rectangle);
        } finally {
            transaction.end();
        }
    }
    
    private void ensureLines(IDocument doc, int lines) throws BadLocationException {
        int n = lines - doc.getNumberOfLines();
        if(n <= 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        String delim = TextUtilities.getDefaultLineDelimiter(doc);
        for(int i = 0; i < n; i++) {
            builder.append(delim);
        }
        doc.replace(doc.getLength(), 0, builder.toString());
        
    }

    private void yankRectangle(IDocument doc,
            int row, int column, List<String> rectangle)
            throws BadLocationException {
        for(int i = 0; i < rectangle.size(); i++) {
            yankString(doc, row+i, column, rectangle.get(i));
        }
    }

    private void yankString(IDocument doc, int row, int column, String str)
    throws BadLocationException
    {
        IRegion line = doc.getLineInformation(row);
        int col = 0;

        CharSequence seq = new DocumentCharSequence(doc, line.getOffset(), line.getLength());
        
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int offset = line.getOffset() + itr.index();
            int codePoint = itr.next();
            if(col >= column) {
                doc.replace(offset, 0, str);
                cursor.move(offset+str.length());
                return;
            }
            col = ColumnUtils.getNextColumn(col, codePoint, getTabStop());
        }
        

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < column-col; i++) {
            builder.append(" ");
        }
        builder.append(str);
        doc.replace(line.getOffset()+line.getLength(), 0, builder.toString());
        cursor.move(line.getOffset()+line.getLength()+builder.length());
    }

}


