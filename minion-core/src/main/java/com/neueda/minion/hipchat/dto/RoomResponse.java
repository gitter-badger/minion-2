package com.neueda.minion.hipchat.dto;

import java.util.Date;

public final class RoomResponse {

    private int id;
    private String name;
    private String xmppJid;
    private String topic;
    private Date created;
    private Date lastActive;
    private boolean isArchived;
    private boolean isGuestAccessible;
    private String guestAccessUrl;
    private String privacy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmppJid() {
        return xmppJid;
    }

    public void setXmppJid(String xmppJid) {
        this.xmppJid = xmppJid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public boolean isGuestAccessible() {
        return isGuestAccessible;
    }

    public void setGuestAccessible(boolean isGuestAccessible) {
        this.isGuestAccessible = isGuestAccessible;
    }

    public String getGuestAccessUrl() {
        return guestAccessUrl;
    }

    public void setGuestAccessUrl(String guestAccessUrl) {
        this.guestAccessUrl = guestAccessUrl;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

}
