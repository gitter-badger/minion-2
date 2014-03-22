package com.neueda.perspective.hipchat;

import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.dto.RoomData;
import com.neueda.perspective.hipchat.dto.UserData;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class HipChat {

    public static final String HIPCHAT_TOKEN = "hipChat.token";

    private final WebTarget api;

    @Inject
    public HipChat(AppCfg cfg,
                   Client client) {
        String target = cfg.getHipChat().getTarget();
        String token = System.getProperty(HIPCHAT_TOKEN);
        api = client.target(target)
                .path("v2")
                .queryParam("auth_token", token);
    }

    public RoomData getRoom(String name) {
        return api.path("room")
                .path(name)
                .request().get(RoomData.class);
    }

    public UserData getUser(String email) {
        return api.path("user")
                .path(email)
                .request().get(UserData.class);
    }

}
