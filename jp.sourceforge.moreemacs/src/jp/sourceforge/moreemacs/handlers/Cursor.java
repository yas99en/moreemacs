package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.texteditor.ITextEditor;

final class Cursor {
    private final ITextEditor textEditor;
    private final ITextViewerExtension5 textViewerEx5;
    private final StyledText styledText;
    private final ITextViewer textViewer;
    
    Cursor(ITextEditor textEditor, ITextViewer textViewer) {
        this.textEditor = textEditor;
        this.textViewer = textViewer;
        this.styledText = textViewer.getTextWidget();
        this.textViewerEx5 = (textViewer instanceof ITextViewerExtension5)
        ? (ITextViewerExtension5) textViewer: null;
    }
    
    int offset() {
        if(textViewerEx5 != null) {
            return textViewerEx5.widgetOffset2ModelOffset(
                    styledText.getCaretOffset());
        }

        ITextSelection selectoin =
            (ITextSelection) textEditor.getSelectionProvider().getSelection();
        int selectionBegin = selectoin.getOffset();
        return selectionBegin;
    }
    
    void move(int offset) {
        textViewer.setSelectedRange(offset, 0);
        textViewer.revealRange(offset, 0);

// workaround for ISourceViewer
//        sourceViewer.setRangeIndication(offset, 0, true);

// workaround for ITextViewerExtension5
//        if(textViewerEx5 != null) {
//            styledText.setCaretOffset(textViewerEx5.modelOffset2WidgetOffset(offset));
//
//            return;
//        }

// workaround for ITextEditor
//        textEditor.resetHighlightRange();
//        textEditor.setHighlightRange(offset, 0, true);
    }
}
