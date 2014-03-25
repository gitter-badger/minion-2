package com.neueda.minion.hipchat.dto;

import java.util.Date;

public final class UserResponse {

    private int id;
    private String xmppJid;
    private String name;
    private String mentionName;
    private String email;
    private String photoUrl;
    private String title;
    private String timezone;
    private Date created;
    private Date lastActive;
    private boolean isDeleted;
    private boolean isGroupAdmin;
    private boolean isGuest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXmppJid() {
        return xmppJid;
    }

    public void setXmppJid(String xmppJid) {
        this.xmppJid = xmppJid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMentionName() {
        return mentionName;
    }

    public void setMentionName(String mentionName) {
        this.mentionName = mentionName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isGroupAdmin() {
        return isGroupAdmin;
    }

    public void setGroupAdmin(boolean isGroupAdmin) {
        this.isGroupAdmin = isGroupAdmin;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

}
