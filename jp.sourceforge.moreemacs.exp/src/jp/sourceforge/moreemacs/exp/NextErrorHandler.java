package jp.sourceforge.moreemacs.exp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.views.markers.MarkerSupportView;

public class NextErrorHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchSite site = HandlerUtil.getActiveSite(event);
        IWorkbenchPage page = site.getPage();

        MarkerSupportView problemView = (MarkerSupportView) page.findView("org.eclipse.ui.views.ProblemView");
        if(problemView == null) {
            return null;
        }
        @SuppressWarnings("restriction")
        IMarker[] currentMarkers = problemView.getSelectedMarkers();
        IMarker startPoint = (currentMarkers.length != 0)?currentMarkers[currentMarkers.length-1]:null;

        selectAll(problemView);
        
        @SuppressWarnings("restriction")
        IMarker[] allMarkers = problemView.getSelectedMarkers();
        goNextError(allMarkers, startPoint, page);

        return null;
    }
    
    void selectAll(IViewPart view) throws ExecutionException {
        view.getSite().getPage().activate(view);
        IHandlerService handlerService = (IHandlerService)view.getSite().getService(IHandlerService.class);
        try {
            handlerService.executeCommand("org.eclipse.ui.edit.selectAll", null);
        } catch (NotDefinedException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (NotEnabledException e) {
            throw new ExecutionException(e.getMessage(), e);
        } catch (NotHandledException e) {
            throw new ExecutionException(e.getMessage(), e);
        }
    }

    void goNextError(IMarker[] allMarkers, IMarker startPoint, IWorkbenchPage page) throws ExecutionException {
        boolean found = false;
        List<IMarker> skipped = new ArrayList<IMarker>();
        for(IMarker marker: allMarkers) {
            if(!marker.exists()) {
                continue;
            }
            if(startPoint != null && startPoint.exists()) {
                if(startPoint.equals(marker)) {
                    found = true;
                    continue;
                }
                
                if(hasSameLineNumber(startPoint, marker)) {
                    continue;
                }
                
                if(!found) {
                    skipped.add(marker);
                    continue;
                }
            }
            
            if(marker.getResource().getType() != IResource.FILE) {
                continue;
            }

            try {
                IDE.openEditor(page, marker, OpenStrategy.activateOnOpen());
            } catch (PartInitException e) {
                throw new ExecutionException(e.getMessage(), e);
            }
            return;
        }
        if(startPoint != null && startPoint.exists()) {
            skipped.add(startPoint);
        }
        
        for(IMarker marker: skipped) {
            if(marker.getResource().getType() != IResource.FILE) {
                continue;
            }

            try {
                IDE.openEditor(page, marker, OpenStrategy.activateOnOpen());
            } catch (PartInitException e) {
                throw new ExecutionException(e.getMessage(), e);
            }
            return;
        }
        
    }

    boolean hasSameLineNumber(IMarker marker1, IMarker marker2) throws ExecutionException {
        if(!marker1.getResource().equals(marker2.getResource())) {
            return false;
        }
        try {
            Integer line1 = (Integer)marker1.getAttribute(IMarker.LINE_NUMBER);
            Integer line2 = (Integer)marker2.getAttribute(IMarker.LINE_NUMBER);
            if(line1 == null) {
                return (line2 == null);
            }
            return line1.equals(line2);
        } catch (CoreException e) {
            throw new ExecutionException(e.getMessage(), e);
        }
    }
}
