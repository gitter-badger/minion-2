package com.neueda.perspective.hipchat;

import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.dto.UserObject;
import com.neueda.perspective.hipchat.dto.UserResponse;

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
                .path("v1")
                .queryParam("auth_token", token);
    }

    public UserObject getUser(String email) {
        UserResponse response = api.path("users/show")
                .queryParam("user_id", email)
                .request().get(UserResponse.class);
        return response.getUser();
    }

}
