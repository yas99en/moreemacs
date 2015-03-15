package jp.sourceforge.moreemacs.handlers;

import java.util.List;

final class RectangleStorage {
    private static List<String> rectangle ;
    
    private RectangleStorage() {}
    
    static void setRectangle(List<String> rect) {
        rectangle = rect;
    }
    
    static List<String> getRectangle() {
        return rectangle;
    }
}
