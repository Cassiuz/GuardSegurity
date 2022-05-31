package com.example.drawerapp.models;

public class gulikcategory {

    String id;
    String idcate;
    String iduser;
    String like;
    String rting;

    public gulikcategory(){
    }

    public gulikcategory(String id, String idcate, String iduser, String like, String rting) {
        this.id = id;
        this.idcate = idcate;
        this.iduser = iduser;
        this.like = like;
        this.rting = rting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcate() {
        return idcate;
    }

    public void setIdcate(String idcate) {
        this.idcate = idcate;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getRting() {
        return rting;
    }

    public void setRting(String rting) {
        this.rting = rting;
    }
}
