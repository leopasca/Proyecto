package com.example.leopasca.guemarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CrearGrupo extends AppCompatActivity {
    TextView txvNombre;
    public void ObtenerReferencias()
    {
        txvNombre = (TextView)findViewById(R.id.txvNombre);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        Intent intent = new Intent();
        String Nombre = intent.getStringExtra("Nombre");
        txvNombre.setText(Nombre);
    }
}
