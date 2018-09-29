package com.niken.eventq.Model.Event;

import java.util.ArrayList;

public class EventModel {
    private boolean status;
    private Integer code;
    private ArrayList<Event> data;

    public boolean isStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public ArrayList<Event> getData() {
        return data;
    }
}
