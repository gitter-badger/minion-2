package com.neueda.minion.ext.result;

public class ExtensionResultNotify implements ExtensionResult {

    private final String color;
    private final String text;
    private final boolean notify;

    public ExtensionResultNotify(String color, String text, boolean notify) {
        this.color = color;
        this.text = text;
        this.notify = notify;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitNotify(color, text, notify);
    }

}
