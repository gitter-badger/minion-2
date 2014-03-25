package com.neueda.minion.ext.result;

public class ExtensionResultProceed implements ExtensionResult {

    public ExtensionResultProceed() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProceed();
    }

}
