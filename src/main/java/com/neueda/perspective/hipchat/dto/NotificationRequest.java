package com.neueda.perspective.hipchat.dto;

public class NotificationRequest {

    private final String color;
    private final String message;
    private final boolean notify;
    private final String messageFormat;

    public NotificationRequest(String color, String message, boolean notify) {
        this.color = color;
        this.message = message;
        this.notify = notify;
        this.messageFormat = "text";
    }

    public String getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }

    public boolean isNotify() {
        return notify;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

}
