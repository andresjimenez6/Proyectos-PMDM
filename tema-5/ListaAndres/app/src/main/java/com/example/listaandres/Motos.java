package com.example.listaandres;

public class Motos {
    private String marca;
    private String modelo;
    private int imagen; // el recurso de la imagen: R.drawable.algo
    public Motos(String marca, String modelo, int imagen) {
        this.marca = marca;
        this.modelo = modelo;
        this.imagen = imagen;
    }
    public String getMarca() {
        return marca;
    }
    public String getModelo() {
        return modelo;
    }
    public int getImagen() {
        return imagen;
    }
}

