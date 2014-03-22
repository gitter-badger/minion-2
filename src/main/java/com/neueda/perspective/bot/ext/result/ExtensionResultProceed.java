package com.neueda.perspective.bot.ext.result;

public class ExtensionResultProceed implements ExtensionResult {

    public ExtensionResultProceed() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProceed();
    }

}
