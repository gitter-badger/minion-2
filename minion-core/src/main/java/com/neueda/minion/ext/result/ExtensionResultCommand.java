package com.neueda.minion.ext.result;

public class ExtensionResultCommand implements ExtensionResult {

    private final String event;
    private final Object data;

    public ExtensionResultCommand(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitCommand(event, data);
    }

}
