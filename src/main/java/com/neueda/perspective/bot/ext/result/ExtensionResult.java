package com.neueda.perspective.bot.ext.result;

public interface ExtensionResult {
    <T> T accept(Visitor<T> visitor);

    public static interface Visitor<T> {
        T visitProceed();

        T visitFinish(String response);
    }

}
