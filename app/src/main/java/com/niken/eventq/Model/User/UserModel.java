package com.niken.eventq.Model.User;

public class UserModel {
    private Boolean status;
    private Integer code;
    private String description;
    private String message;
    private User data;
    public User getUser() {
        return data;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }
}
