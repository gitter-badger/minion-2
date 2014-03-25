package com.neueda.minion.ext;

import java.util.List;

public interface ExtensionLoaderFactory {
    ExtensionLoader create(List<String> extensions);
}
