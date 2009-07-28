package jp.sourceforge.moreemacs.handlers;

import jp.sourceforge.moreemacs.utils.CodePointIterator;


public final class CapitalizeWordExecution extends ConvertWordExecution {
    @Override
    protected String convert(String word) {
        StringBuilder builder = new StringBuilder();
        for(CodePointIterator itr = new CodePointIterator(word); itr.hasNext(); ) {
            int cp = itr.next();
            
            if(!Character.isLetter(cp)) {
                builder.appendCodePoint(cp);
                continue;
            }

            builder.appendCodePoint(Character.toUpperCase(cp));
            if(itr.hasNext()) {
                builder.append(word.substring(itr.index()).toLowerCase());
            }
            break;
        }
        return builder.toString();
    }
}

