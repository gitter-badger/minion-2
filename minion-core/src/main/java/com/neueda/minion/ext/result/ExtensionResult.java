package com.neueda.minion.ext.result;

public interface ExtensionResult {
    void accept(Visitor visitor);

    public static interface Visitor {
        void visitRespond(String response);

        void visitNotify(String color, String text, boolean notify);

        void visitCommand(String event, Object data);
    }

}
