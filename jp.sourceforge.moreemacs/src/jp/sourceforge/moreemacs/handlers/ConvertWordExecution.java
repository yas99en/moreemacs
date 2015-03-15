package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.BadLocationException;

public abstract class ConvertWordExecution extends TextEditorExecution {

    @Override
    public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }
        
        int current = cursor.offset();
        int next = ForwardWordExecution.getNextWordPosition(doc, current);
        String word = doc.get(current, next-current);
        String  convertedWord = convert(word);
        doc.replace(current, next-current, convertedWord);
        cursor.move(current + convertedWord.length());
    }
    
    protected abstract String convert(String word);
}
