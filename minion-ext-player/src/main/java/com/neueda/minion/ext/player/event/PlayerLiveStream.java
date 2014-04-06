package com.neueda.minion.ext.player.event;

public class PlayerLiveStream extends PlayerAction {

    private final String url;
    private final String format;

    public PlayerLiveStream(String url, String format) {
        super("live");
        this.url = url;
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }

}
