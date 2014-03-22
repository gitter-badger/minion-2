package com.neueda.perspective.bot.ext;

import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.bot.ext.result.ExtensionResultFinish;

public class ReverseExtension implements Extension {

    private static final String NAME = "reverse";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public ExtensionResult process(String message) {
        String reverse = new StringBuilder(message).reverse().toString();
        return new ExtensionResultFinish(reverse);
    }

}
