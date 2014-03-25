package com.neueda.minion.ext.result;

public class ExtensionResultRespond implements ExtensionResult {

    private final String response;

    public ExtensionResultRespond(String response) {
        this.response = response;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitRespond(response);
    }

}
