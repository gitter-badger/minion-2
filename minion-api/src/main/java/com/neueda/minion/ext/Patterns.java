package com.neueda.minion.ext;

import com.google.common.base.Joiner;

import java.util.regex.Pattern;

public final class Patterns {

    private Patterns() {
    }

    public static Pattern sentence(String... parts) {
        StringBuilder builder = new StringBuilder("\\s*");
        Joiner.on("\\s+")
                .skipNulls()
                .appendTo(builder, parts);
        builder.append("\\s*");
        return Pattern.compile(builder.toString());
    }

    public static Pattern preamble(String... parts) {
        StringBuilder builder = new StringBuilder("\\s*");
        Joiner.on("\\s+")
                .skipNulls()
                .appendTo(builder, parts);
        builder.append("\\s*:\\s*(.*?)\\s*");
        return Pattern.compile(builder.toString());
    }

}
