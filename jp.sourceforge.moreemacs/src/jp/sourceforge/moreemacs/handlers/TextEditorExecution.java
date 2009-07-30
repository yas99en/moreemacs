package jp.sourceforge.moreemacs.handlers;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

abstract class TextEditorExecution implements Execution {
    protected IWorkbenchWindow window;
    protected ITextEditor textEditor;
    protected ITextViewer textViewer;
    protected Cursor cursor;
    protected IDocument doc;
    
    @Override
    public boolean init(IWorkbenchWindow window) {
        this.window = window;

        IEditorPart editor = window.getActivePage().getActiveEditor();
        if (editor instanceof ITextEditor) {
            textEditor = (ITextEditor) editor;
        } else {
            textEditor = (ITextEditor) editor.getAdapter(ITextEditor.class);
        }
        if(textEditor == null) {
            return false;
        }
        
        doc = textEditor.getDocumentProvider().getDocument(
                textEditor.getEditorInput());

        ITextOperationTarget target =
            (ITextOperationTarget)editor.getAdapter(ITextOperationTarget.class);
        if(!(target instanceof ITextViewer)) {
            return false;
        }
        textViewer = (ITextViewer) target;
        
        cursor = new Cursor(textEditor, textViewer);
        
        return true;
    }
    
    protected ITextSelection getSelection(boolean fallbackToMark) {
        ITextSelection selection =
            (ITextSelection) textEditor.getSelectionProvider().getSelection();
        
        if(!fallbackToMark && selection.getLength() != 0) {
            return selection;
        }
        
        IEditorPart editor = window.getActivePage().getActiveEditor();
        ITextOperationTarget target =
            (ITextOperationTarget)editor.getAdapter(ITextOperationTarget.class);
        
        if(!(target instanceof ITextViewerExtension)) {
            return selection;
        }
        
        ITextViewerExtension viewerEx = (ITextViewerExtension) target;
            
        int mark = viewerEx.getMark();
        
        if(mark == -1) {
            return selection;
        }
        
        int current = selection.getOffset();
        
        int start = (mark < current) ? mark : current;
        textViewer.setSelectedRange(start, Math.abs(mark - current));
        
        return (ITextSelection) textEditor.getSelectionProvider().getSelection();
    }

    protected final int getTabStop() {
        return textViewer.getTextWidget().getTabs();
    }
}
