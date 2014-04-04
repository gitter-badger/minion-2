package com.neueda.minion.hipchat;

import com.neueda.minion.hipchat.cfg.HipChatCfg;
import com.neueda.minion.hipchat.dto.NotificationRequest;
import com.neueda.minion.hipchat.dto.RoomResponse;
import com.neueda.minion.hipchat.dto.UserResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class HipChat {

    private final WebTarget api;

    @Inject
    public HipChat(HipChatCfg cfg, @Named("hipChat") Client client) {
        api = client.target(cfg.getTarget())
                .path("v2")
                .queryParam("auth_token", cfg.getToken());
    }

    public RoomResponse getRoom(String name) {
        // TODO Handle 404
        return api.path("room")
                .path(name)
                .request().get(RoomResponse.class);
    }

    public UserResponse getUser(String email) {
        // TODO Handle 404
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
