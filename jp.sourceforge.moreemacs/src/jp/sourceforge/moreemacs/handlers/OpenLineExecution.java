package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.TextUtilities;

public final class OpenLineExecution extends TextEditorExecution {

    @Override
    public void execute() throws Exception {
        if(!textEditor.isEditable()) {
            return;
        }
        String delim = TextUtilities.getDefaultLineDelimiter(doc);
        doc.replace(cursor.offset(), 0, delim);
    }

}
