package com.neueda.perspective.hipchat;

import com.neueda.perspective.config.AppCfg;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class HipChat {

    public static final String HIPCHAT_TOKEN = "hipChat.token";

    private final WebTarget api;

    @Inject
    public HipChat(AppCfg cfg,
                   Client client) {
        String target = cfg.getHipChat().getTarget();
        String token = System.getProperty(HIPCHAT_TOKEN);
        api = client.target(target)
                .path("v1")
                .queryParam("auth_token", token);
    }

    public void getRooms() {
        Response response = api.path("rooms/list").request().get();
        System.out.println(response.readEntity(String.class));
    }

}
