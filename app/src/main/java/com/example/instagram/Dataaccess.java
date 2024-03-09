package com.example.instagram;

public class Dataaccess {
    public Dataaccess() {
    }

    String userimg, password, mob, gender, fullname, email, bio, img, username, userId;

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Dataaccess(String userimg, String password, String mob, String gender, String fullname, String email, String bio, String img, String username, String userId) {
        this.userimg = userimg;
        this.password = password;
        this.mob = mob;
        this.gender = gender;
        this.fullname = fullname;
        this.email = email;
        this.bio = bio;
        this.img = img;
        this.username = username;
        this.userId = userId;
    }
}