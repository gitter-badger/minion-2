package com.neueda.perspective.bot.ext;

import com.neueda.perspective.bot.ext.result.ExtensionResult;

public interface Extension {
    String name();

    ExtensionResult process(String message);
}
