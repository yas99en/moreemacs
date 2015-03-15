package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.DocumentCharSequence;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;

public final class TransposeCharsExecution extends TextEditorExecution {
    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }

        int current = cursor.offset();
        if(current == 0) {
            // beginning of document
            return;
        }

        int linePos = doc.getLineOfOffset(current);
        IRegion line = doc.getLineInformation(linePos);
        
        DocumentCharSequence seq = new DocumentCharSequence(doc);

        if(line.getOffset() + line.getLength() == current) {
            // if end of line, adjust current position
            current = (line.getOffset() == current)
                    ? current - doc.getLineDelimiter(linePos-1).length() 
                    : seq.previousCodePointIndex(current);
            linePos = doc.getLineOfOffset(current);
            line = doc.getLineInformation(linePos);
        }
        if(current == 0) {
            // beginning of document again
            return;
        }

        int nextIndex = seq.nextCodePointIndex(current);
        String forwardChars = (line.getOffset() + line.getLength() == current) 
        ? doc.getLineDelimiter(linePos) : doc.get(current, nextIndex - current);
            
        int prevIndex = seq.previousCodePointIndex(current);
        String backwardChars = (line.getOffset() == current) 
            ? doc.getLineDelimiter(linePos-1) : doc.get(prevIndex, current-prevIndex);

        doc.replace(current-backwardChars.length(), 
                backwardChars.length() + forwardChars.length(),
                forwardChars+backwardChars);
    }
    
}
