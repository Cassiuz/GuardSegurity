package com.example.drawerapp.models;

public class NavCDetalleModel {
    String nombre;
    String like;
    String idusuario;
    String rting;
    String idcat;
    String comentario;
    String idcatedetall;
    String namecate;

    public NavCDetalleModel() {
    }

    public NavCDetalleModel(String nombre, String like, String idusuario, String rting, String idcat, String comentario, String idcatedetall, String namecate) {
        this.nombre = nombre;
        this.like = like;
        this.idusuario = idusuario;
        this.rting = rting;
        this.idcat = idcat;
        this.comentario = comentario;
        this.idcatedetall = idcatedetall;
        this.namecate = namecate;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getRting() {
        return rting;
    }

    public void setRting(String rting) {
        this.rting = rting;
    }

    public String getIdcat() {
        return idcat;
    }

    public void setIdcat(String idcat) {
        this.idcat = idcat;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdcatedetall() {
        return idcatedetall;
    }

    public void setIdcatedetall(String idcatedetall) {
        this.idcatedetall = idcatedetall;
    }

    public String getNamecate() {
        return namecate;
    }

    public void setNamecate(String namecate) {
        this.namecate = namecate;
    }
}
