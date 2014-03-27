package com.neueda.minion.hipchat;

import com.neueda.minion.config.AppCfg;
import com.neueda.minion.hipchat.dto.NotificationRequest;
import com.neueda.minion.hipchat.dto.RoomResponse;
import com.neueda.minion.hipchat.dto.UserResponse;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class HipChat {

    private static final String HIPCHAT_TOKEN = "hipChat.token";

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

    public RoomResponse getRoom(String name) {
        return api.path("room")
                .path(name)
                .request().get(RoomResponse.class);
    }

    public UserResponse getUser(String email) {
        return api.path("user")
                .path(email)
                .request().get(UserResponse.class);
    }

    public void sendNotification(String room, String color, String text, boolean notify) {
        NotificationRequest entity = new NotificationRequest(color, text, notify);
        api.path("room")
                .path(room)
                .path("notification")
                .request().post(Entity.json(entity));
    }

}
