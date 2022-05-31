package com.example.drawerapp.models;

public class NavCategoryDetalleModelGuardado {

    String name;
    String type;
    String img_url;
    String price;
    String idcatd;

    public NavCategoryDetalleModelGuardado(String name, String type, String img_url, String price, String idcatd) {
        this.name = name;
        this.type = type;
        this.img_url = img_url;
        this.price = price;
        this.idcatd = idcatd;
    }

    public NavCategoryDetalleModelGuardado() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdcatd() {
        return idcatd;
    }

    public void setIdcatd(String idcatd) {
        this.idcatd = idcatd;
    }
}
