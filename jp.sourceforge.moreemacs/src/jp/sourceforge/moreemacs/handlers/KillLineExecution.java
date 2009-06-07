package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.CodePointIterator;
import jp.sourceforge.moreemacs.utils.DocumentCharSequence;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public final class KillLineExecution extends TextEditorExecution {

	@Override
	public void execute() throws BadLocationException {
        if(!textEditor.isEditable()) {
            return;
        }

		int current = cursor.offset();
		int linePos = doc.getLineOfOffset(current);
		IRegion line = doc.getLineInformation(linePos);
		String delim = doc.getLineDelimiter(linePos);

		int length = line.getOffset() + line.getLength() - current;
		boolean allSpaces = isAllSpaces(doc, current, length);

		int cutLength = length;
		if (allSpaces && delim != null) {
			cutLength += delim.length();
		}

		String cut = doc.get(current, cutLength);
		Clipboard c = new Clipboard(window.getShell().getDisplay());
		c.setContents(
				new String[] { cut }, 
				new Transfer[] { TextTransfer.getInstance() });
		doc.replace(current, cutLength, "");
	}
	
	private boolean isAllSpaces(IDocument doc, int offset, int length) throws BadLocationException {
        CharSequence seq = new DocumentCharSequence(doc, offset, length);
        for(int codePoint : CodePointIterator.each(seq)) {
            if (!Character.isWhitespace(codePoint)) {
                return false;
            }
        }
        return true;
	}
}
