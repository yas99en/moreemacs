package jp.sourceforge.moreemacs.handlers;


public final class DowncaseWordExecution extends ConvertWordExecution {
    @Override
    protected String convert(String word) {
        return word.toLowerCase();
    }
}

