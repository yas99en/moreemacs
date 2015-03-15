package jp.sourceforge.moreemacs.utils;

import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterUtilsTest {
    @Test
    public void testGetWidth() {
        String ambiguousChars = "α■";

        for(int codePoint : CodePointIterator.each(ambiguousChars)) {
            assertEquals(2, CharacterUtils.getWidth(codePoint, Locale.JAPANESE));
        }
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPANESE);
            for(int codePoint : CodePointIterator.each(ambiguousChars)) {
                assertEquals(2, CharacterUtils.getWidth(codePoint));
            }
            Locale.setDefault(Locale.ENGLISH);
            for(int codePoint : CodePointIterator.each(ambiguousChars)) {
                assertEquals(1, CharacterUtils.getWidth(codePoint));
            }
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
