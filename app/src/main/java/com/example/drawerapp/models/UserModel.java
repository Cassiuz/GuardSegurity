package com.example.drawerapp.models;

public class UserModel {

    String name;
    String email;
    String password;
    String rol;
    String profileImg;
    String userUid;
    String portadaImg;

    public UserModel(){
    }

    public UserModel(String name, String email, String password, String rol, String profileImg, String userUid, String portadaImg) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.profileImg = profileImg;
        this.userUid = userUid;
        this.portadaImg = portadaImg;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPortadaImg() {
        return portadaImg;
    }

    public void setPortadaImg(String portadaImg) {
        this.portadaImg = portadaImg;
    }
}
