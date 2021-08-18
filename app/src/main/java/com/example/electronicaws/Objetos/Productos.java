package com.example.electronicaws.Objetos;

import java.io.Serializable;

public class Productos implements Serializable {
<<<<<<< HEAD
    private int _ID;
    private String marca;
    private String descripcion;
    private String fotos;

    public Productos(){}

    public Productos(String marca, String descripcion, String fotos)
    {
        this.descripcion = descripcion;
        this.marca = marca;
        this.fotos = fotos;
=======

    private int _ID;
    private String marca;
    private String descripcion;
    private String foto;

    public Productos() {
    }

    public Productos(String marca, String descripcion, String foto) {
        this.setMarca(marca);
        this.setDescripcion(descripcion);
        this.setFoto(foto);
>>>>>>> origin/main
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

<<<<<<< HEAD
    public String getFotos() {
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
=======
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
>>>>>>> origin/main
    }
}
