package com.niken.eventq.Model.EventParticipant;

public class EventParticipantModel {
    private Boolean status;
    private Integer code;
    private String message;
    private Integer data;
    private Datenow datenow;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Datenow getDatenow() {
        return datenow;
    }

    public void setDatenow(Datenow datenow) {
        this.datenow = datenow;
    }

}
