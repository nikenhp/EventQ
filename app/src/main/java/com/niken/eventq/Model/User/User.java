package com.niken.eventq.Model.User;

public class User {
    private int id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String gender;
    private String userRole;
    private Object birthdate;
    private String regency_id;
    private Object photo;
    private String dateCreated;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Object getGender() {
        return gender;
    }

    public String getUserRole() {
        return userRole;
    }

    public Object getBirthdate() {
        return birthdate;
    }

    public String getRegency_id() {
        return regency_id;
    }

    public Object getPhoto() {
        return photo;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
