package jp.sourceforge.moreemacs.utils;

import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterUtilsTest {
    @Test
    public void testGetWidth() {
        String str = "α■";
        for(int codePoint : CodePointIterator.each(str)) {
            assertEquals(2, CharacterUtils.getWidth(codePoint, Locale.JAPANESE));
        }
    }
}
