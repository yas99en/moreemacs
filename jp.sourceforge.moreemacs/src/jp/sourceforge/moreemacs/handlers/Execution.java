package jp.sourceforge.moreemacs.handlers;

import org.eclipse.ui.IWorkbenchWindow;

public interface Execution {
    boolean init(IWorkbenchWindow window);
    void execute() throws Exception;
}
