package com.example.leopasca.guemarapp;

import java.io.Serializable;

/**
 * Created by AMD on 01/08/2016.
 */
public class Nota implements Serializable {
    Integer IdNota;
    String Nombre;
    Double CordNotaY;
    Double CordNotaAsteriscoX;
    Double CordNotaAsteriscoY;

    public Nota(int IdNota, String Nombre, Double CordNotaY, Double CordNotaAsteriscoX, Double CordNotaAsteriscoY) {
        this.IdNota= IdNota;
        this.Nombre = Nombre;
        this.CordNotaY = CordNotaY;
        this.CordNotaAsteriscoX = CordNotaAsteriscoX;
        this.CordNotaAsteriscoY = CordNotaAsteriscoY;
    }
}
