package com.niken.eventq.Model.EventParticipant;

public class EventParticipant {
    private Integer id;
    private String name;
    private String address;
    private Object regencyId;
    private Integer price;
    private Integer quota;
    private Object categoryId;
    private Object startDate;
    private Object endDate;
    private String description;
    private Object confirmationDate;
    private String createdDate;
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

    public Object getRegencyId() {
        return regencyId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuota() {
        return quota;
    }

    public Object getCategoryId() {
        return categoryId;
    }

    public Object getStartDate() {
        return startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public Object getConfirmationDate() {
        return confirmationDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }
}
