package jp.sourceforge.moreemacs.handlers;


public final class UpcaseWordExecution extends ConvertWordExecution {
    @Override
    protected String convert(String word) {
        return word.toUpperCase();
    }
}

