package com.neueda.perspective.bot.ext.result;

public class ExtensionResultFinish implements ExtensionResult {

    private final String response;

    public ExtensionResultFinish(String response) {
        this.response = response;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFinish(response);
    }

}
