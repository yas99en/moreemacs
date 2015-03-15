package jp.sourceforge.moreemacs.utils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public final class ColumnUtils {
    private ColumnUtils() {}
    
    public static int getColumn(IDocument doc, int offset, int tabStop)
    throws BadLocationException {
        IRegion line = doc.getLineInformationOfOffset(offset);
        int column = 0;
        
        CharSequence seq = new DocumentCharSequence(doc, line.getOffset(), offset - line.getOffset());
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int codePoint = itr.next();
            column = getNextColumn(column, codePoint, tabStop);
        }
        
        return column;
    }
    
    public static int getNextColumn(int column, int codePoint, int tabStop) {
        if(codePoint == '\t') {
            return column - (column%tabStop) + tabStop;
        } else {
            return column + CharacterUtils.getWidth(codePoint);
        }
    }
}
