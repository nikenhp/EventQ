package com.niken.eventq.Model.Event;

public class Event {
    private Integer id;
    private String name;
    private String address;
    private Integer regency_id;
    private float price;
    private Integer quota;
    private Integer category_id;
    private String start_date;
    private String end_date;
    private String description;
    private String confirmation_date;
    private String created_date;
    private String photo;
    private String status;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getRegency_id() {
        return regency_id;
    }

    public float getPrice() {
        return price;
    }

    public Integer getQuota() {
        return quota;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDescription() {
        return description;
    }

    public String getConfirmation_date() {
        return confirmation_date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }
}
