package com.neueda.perspective.bot.ext;

import com.neueda.perspective.bot.ext.result.ExtensionResult;

public interface Extension {
    String name();

    void initialize();

    ExtensionResult process(String message);
}
