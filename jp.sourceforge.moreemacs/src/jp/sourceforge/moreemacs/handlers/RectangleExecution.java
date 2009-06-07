package jp.sourceforge.moreemacs.handlers;

import java.util.List;

abstract class RectangleExecution extends TextEditorExecution {
    private static List<String> rectangle ;
    
    protected static void setRectangle(List<String> rect) {
        rectangle = rect;
    }
    
    protected static List<String> getRectangle() {
        return rectangle;
    }
}
