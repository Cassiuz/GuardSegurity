package com.example.drawerapp.models;

public class NavCategoryModelE {

    String name;
    String descripcion;
    String descuento;
    String img_url;
    String type;
    String idcat;
    String idUsu;
    String nameUsu;
    String Img_url_user;
    String likes;

    public NavCategoryModelE() {
    }

    public NavCategoryModelE(String name, String descripcion, String descuento, String img_url, String type, String idcat, String idUsu, String nameUsu, String Img_url_user, String likes) {
        this.name = name;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.img_url = img_url;
        this.type = type;
        this.idcat = idcat;
        this.idUsu = idUsu;
        this.nameUsu = nameUsu;
        this.Img_url_user = Img_url_user;
        this.likes = likes;
    }

    public String getImg_url_user() {
        return Img_url_user;
    }

    public void setImg_url_user(String img_url_user) {
        Img_url_user = img_url_user;
    }

    public String getNameUsu() {
        return nameUsu;
    }

    public void setNameUsu(String nameUsu) {
        this.nameUsu = nameUsu;
    }

    public String getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(String idUsu) {
        this.idUsu = idUsu;
    }

    public String getIdcat() {
        return idcat;
    }

    public void setIdcat(String idcat) {
        this.idcat = idcat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
