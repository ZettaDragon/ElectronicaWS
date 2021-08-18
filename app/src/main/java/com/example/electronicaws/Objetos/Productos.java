package com.example.electronicaws.Objetos;

import java.io.Serializable;

public class Productos implements Serializable {

    private int _ID;
    private String marca;
    private String descripcion;
    private String foto;
    private double precio;


    public Productos() {
    }

    public Productos(String marca, String descripcion, String foto, double precio) {
        this.setMarca(marca);
        this.setDescripcion(descripcion);
        this.setFoto(foto);
        this.setPrecio(precio);
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
