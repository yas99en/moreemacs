package jp.sourceforge.moreemacs.utils;

import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;

public final class DocumentTransaction {
    private IDocumentExtension4 sessionManager;
    private DocumentRewriteSession session;
    
    public DocumentTransaction(IDocument doc) {
        if(doc instanceof IDocumentExtension4) {
            sessionManager = (IDocumentExtension4)doc;
        }
    }
    
    public boolean isAvailable() {
        return sessionManager != null;
    }
    
    public void begin(DocumentRewriteSessionType type) {
        if(!isAvailable()) {
            return;
        }
        if(session != null) {
            throw new IllegalStateException("session already started");
        }
        session = sessionManager.startRewriteSession(type);
    }
    
    public void end() {
        if(!isAvailable()) {
            return;
        }
        if(session == null) {
            throw new IllegalStateException("session is not started");
        }
        sessionManager.stopRewriteSession(session);
        session = null;
    }

}
