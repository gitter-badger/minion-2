package com.neueda.minion.ext;

import com.google.common.base.Joiner;

import java.util.regex.Pattern;

public final class PatternHelper {

    private PatternHelper() {
    }

    public static Pattern preambleAny(String... words) {
        return preamble(".*", words);
    }

    public static Pattern preamble(String pattern, String... words) {
        StringBuilder builder = new StringBuilder("\\s*");
        Joiner.on("\\s+")
                .skipNulls()
                .appendTo(builder, words);
        builder.append("\\s*:\\s*(")
                .append(pattern)
                .append("?)\\s*");
        return Pattern.compile(builder.toString());
    }

}
