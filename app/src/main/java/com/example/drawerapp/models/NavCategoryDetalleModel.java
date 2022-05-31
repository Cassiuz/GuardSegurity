package com.example.drawerapp.models;

public class NavCategoryDetalleModel {

    String name;
    String type;
    String img_url;
    String price;
    String idcatd;
    String idcategoria;
    String fechadia;

    public NavCategoryDetalleModel(String name, String type, String img_url, String price, String idcatd, String idcategoria, String fechadia) {
        this.name = name;
        this.type = type;
        this.img_url = img_url;
        this.price = price;
        this.idcatd = idcatd;
        this.idcategoria = idcategoria;
        this.fechadia = fechadia;
    }

    public NavCategoryDetalleModel() {
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

    public String getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getFechadia() {
        return fechadia;
    }

    public void setFechadia(String fechadia) {
        this.fechadia = fechadia;
    }
}
