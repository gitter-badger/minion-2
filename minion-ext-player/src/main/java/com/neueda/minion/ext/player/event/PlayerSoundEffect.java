package com.neueda.minion.ext.player.event;

public class PlayerSoundEffect extends PlayerAction {

    private final String path;

    public PlayerSoundEffect(String path) {
        super("sfx");
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
