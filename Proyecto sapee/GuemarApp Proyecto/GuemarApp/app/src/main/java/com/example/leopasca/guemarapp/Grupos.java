package com.example.leopasca.guemarapp;

import java.io.Serializable;

/**
 * Created by AMD on 31/10/2016.
 */
public class Grupos implements Serializable {
    public Integer IdGrupo;
    public String Nombre;


    public Grupos(int IdGrupo, String Nombre) {
        this.IdGrupo = IdGrupo;
        this.Nombre = Nombre;
    }
}

