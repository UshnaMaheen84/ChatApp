package com.example.admin.chatapp.ModelClasses;

public class FriendsModel {

    private String name, email, image, id, user_status;

    public FriendsModel() {
    }

    public FriendsModel(String name, String email, String image, String id, String user_status) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.id = id;
        this.user_status = user_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
