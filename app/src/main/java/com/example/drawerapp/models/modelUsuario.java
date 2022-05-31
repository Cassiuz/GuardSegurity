package com.example.drawerapp.models;

public class modelUsuario {

    String name;
    String email;
    String number;
    String direccion;


    public modelUsuario() {
    }

    public modelUsuario(String name, String email, String number, String direccion) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.direccion = direccion;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
