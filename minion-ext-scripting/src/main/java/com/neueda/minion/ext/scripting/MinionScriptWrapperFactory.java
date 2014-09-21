package com.neueda.minion.ext.scripting;

import com.neueda.minion.ext.HipChatMessage;

public interface MinionScriptWrapperFactory {
    MinionScriptWrapper create(HipChatMessage message);
}
