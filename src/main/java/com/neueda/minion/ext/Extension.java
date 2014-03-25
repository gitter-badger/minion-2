package com.neueda.minion.ext;

import com.neueda.minion.ext.result.ExtensionResult;

public interface Extension {
    String name();

    void initialize();

    ExtensionResult process(String message);
}
