package com.neueda.minion.ext.result;

public class CompositeExtensionResult implements ExtensionResult {

    private final Iterable<ExtensionResult> results;

    public CompositeExtensionResult(Iterable<ExtensionResult> results) {
        this.results = results;
    }

    @Override
    public void accept(Visitor visitor) {
        for (ExtensionResult result : results) {
            result.accept(visitor);
        }
    }

}
