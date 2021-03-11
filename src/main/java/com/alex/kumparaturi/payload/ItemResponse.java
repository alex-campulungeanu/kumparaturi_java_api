package com.alex.kumparaturi.payload;

import java.time.Instant;

public class ItemResponse {
    private Long id;
    private String name;
    private Long statusId;
    private Instant createDate;
    private String username;

    public ItemResponse() {
    }

    public ItemResponse(Long id, String name, Long statusId, Instant createDate, String username) {
        this.id = id;
        this.name = name;
        this.statusId = statusId;
        this.createDate = createDate;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

}
