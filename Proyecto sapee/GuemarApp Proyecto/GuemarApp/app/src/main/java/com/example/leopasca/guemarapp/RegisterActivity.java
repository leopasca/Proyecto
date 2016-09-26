package com.example.leopasca.guemarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegistrar;
    EditText edtUsuarioReg;
    EditText edtPassReg;
    EditText edtNombre;
    TextView txvIngresar;
    public void ObtenerReferencias()
    {
        btnRegistrar =(Button)findViewById(R.id.btnRegistrar);
        edtUsuarioReg = (EditText)findViewById(R.id.edtUsuarioReg);
        edtPassReg = (EditText)findViewById(R.id.edtPassReg);
        txvIngresar = (TextView)findViewById(R.id.txvIngresarCuenta);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ObtenerReferencias();
        btnRegistrar.setOnClickListener(registrar);
        txvIngresar.setOnClickListener(ingresar);
    }
    public View.OnClickListener ingresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentIngresar= new Intent(getApplicationContext(),LogActivity.class);
            startActivity(intentIngresar);
        }
    };
    public View.OnClickListener registrar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(!edtPassReg.getText().toString().isEmpty()||!edtUsuarioReg.getText().toString().isEmpty()||!edtNombre.getText().toString().isEmpty())
          {
              HttpPost post = new HttpPost();
              post.setHeader("content-type", "application/json");
              String Usuario = edtUsuarioReg.getText().toString();
              String Contraseña = edtPassReg.getText().toString();
              String Nombre = edtNombre.getText().toString();
              try {
                  OkHttpClient client = new  OkHttpClient();
                  String url ="http://leopashost.hol.es/bd/CrearUsuario.php";
                  JSONObject dato = new JSONObject();
                  dato.put("Email", Usuario);
                  dato.put("Contraseña",Contraseña);
                  dato.put("Nombre",Nombre);
                  RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                  Request request = new Request.Builder()
                          .url(url)
                          .post(body)
                          .build();
                  Response response = client.newCall(request).execute();
                  Log.d("Response", response.body().string());
              }
              catch (IOException |JSONException e) {
                  Log.d("Error", e.getMessage());
              }
              Intent intentIngresar= new Intent(getApplicationContext(),LogActivity.class);
              startActivity(intentIngresar);
          }
            else
          {
              Toast.makeText(RegisterActivity.this, "Complete todos los campos requeridos", Toast.LENGTH_SHORT).show();
          }
        }
    };
}
