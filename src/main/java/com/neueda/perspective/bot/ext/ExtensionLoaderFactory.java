package com.neueda.perspective.bot.ext;

import java.util.List;

public interface ExtensionLoaderFactory {
    ExtensionLoader create(List<String> extensions);
}
