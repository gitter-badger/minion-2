package com.neueda.perspective.bot.ext.result;

public class ExtensionResultProceed implements ExtensionResult {

    private final String message;

    public ExtensionResultProceed(String message) {
        this.message = message;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProceed(message);
    }

}
