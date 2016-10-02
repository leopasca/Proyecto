package com.example.leopasca.guemarapp;

import java.io.Serializable;

/**
 * Created by 41587461 on 27/6/2016.
 */
public class Comentario implements Serializable {
    public Integer IdComentario;
    public String Comentario;
    public Double CordComentarioY;
    public Double CordAsteriscoX;
    public Double CordAsteriscoY;

    public Comentario(int IdComentario, String Comentario, Double CordComentarioY, Double CordAsteriscoX, Double CordAsteriscoY) {
        this.IdComentario = IdComentario;
        this.Comentario = Comentario;
        this.CordComentarioY = CordComentarioY;
        this.CordAsteriscoX = CordAsteriscoX;
        this.CordAsteriscoY = CordAsteriscoY;
    }
}