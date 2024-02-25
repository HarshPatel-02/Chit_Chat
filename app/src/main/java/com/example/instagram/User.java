package com.example.instagram;

public class User {
    String unm, password;

    public User(String unm, String password) {
        this.unm = unm;
        this.password = password;
    }

    public String getUnm() {
        return unm;
    }

    public void setUnm(String unm) {
        this.unm = unm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }
}