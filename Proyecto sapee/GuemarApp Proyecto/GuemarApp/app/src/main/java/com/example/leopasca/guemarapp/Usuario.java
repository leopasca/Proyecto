package com.example.leopasca.guemarapp;

import java.io.Serializable;

/**
 * Created by 41587461 on 26/9/2016.
 */
public class Usuario implements Serializable {
    public Integer IdUsuario;
    public String Usuario;
    public String Contraseña;
    public String Nombre;

    public Usuario(int IdUsuario, String Usuario, String Contraseña,String Nombre) {
        this.IdUsuario = IdUsuario;
        this.Usuario = Usuario;
        this.Contraseña = Contraseña;
        this.Nombre =Nombre;
    }
}
