package com.neueda.minion.hipchat.cfg;

import com.google.common.base.Splitter;
import com.netflix.governator.annotations.Configuration;

import java.util.Collections;
import java.util.List;

public final class HipChatCfg {

    @Configuration("hipChat.target")
    private String target = "https://api.hipchat.com";

    @Configuration("hipChat.email")
    private String email;

    @Configuration("hipChat.token")
    private String token;

    @Configuration("hipChat.rooms")
    private String rooms;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRooms() {
        return rooms;
    }

    public List<String> getRoomsAsList() {
        if (rooms == null) {
            return Collections.emptyList();
        }
        return Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .splitToList(rooms);
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

}
