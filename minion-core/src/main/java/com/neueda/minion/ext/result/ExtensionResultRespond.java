package com.neueda.minion.ext.result;

public class ExtensionResultRespond implements ExtensionResult {

    private final String response;

    public ExtensionResultRespond(String response) {
        this.response = response;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRespond(response);
    }

}
