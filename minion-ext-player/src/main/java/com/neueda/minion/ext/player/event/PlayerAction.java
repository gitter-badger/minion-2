package com.neueda.minion.ext.player.event;

public abstract class PlayerAction {

    private final String type;

    public PlayerAction(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
