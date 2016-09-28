package com.example.leopasca.guemarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegistrar;
    EditText edtUsuarioReg;
    EditText edtPassReg;
    EditText edtNombreReg;
    TextView txvIngresar;
    String UsuarioReg="",Contraseña="",Nombre ="";
    public void ObtenerReferencias()
    {
        btnRegistrar =(Button)findViewById(R.id.btnRegistrar);
        edtUsuarioReg = (EditText)findViewById(R.id.edtUsuarioReg);
        edtPassReg = (EditText)findViewById(R.id.edtPassReg);
        txvIngresar = (TextView)findViewById(R.id.txvIngresarCuenta);
        edtNombreReg = (EditText)findViewById(R.id.edtNombre);
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
          if(!edtPassReg.getText().toString().isEmpty()||!edtUsuarioReg.getText().toString().isEmpty()||!edtNombreReg.getText().toString().isEmpty())
          {
               UsuarioReg= edtUsuarioReg.getText().toString();
               Contraseña = edtPassReg.getText().toString();
               Nombre = edtNombreReg.getText().toString();
              RegistrarUsuario taskUsuarios = new RegistrarUsuario(RegisterActivity.this);
              taskUsuarios.execute("http://leopashost.hol.es/bd/CrearUsuario.php");
          }
            else
          {
              Toast.makeText(RegisterActivity.this, "Complete todos los campos requeridos", Toast.LENGTH_SHORT).show();
          }
        }
    };
    class RegistrarUsuario extends AsyncTask<String,Void, String> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public RegistrarUsuario(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }

        protected void onPreExecute(){
            this.pdia.setMessage("Cargando...");
            this.pdia.show();
        }
        @Override
        protected void onPostExecute(String Registrado) {
            super.onPostExecute(Registrado);
            if(pdia.isShowing())
            {
                pdia.dismiss();
            }
            Toast.makeText(RegisterActivity.this, "El usuario ha sido "+ Registrado + " correctamente", Toast.LENGTH_SHORT).show();
            Intent intentALog = new Intent(getApplicationContext(),LogActivity.class);
            startActivity(intentALog);
            finish();

        }


        @Override
        protected String doInBackground(String...params) {
            String url = params[0];

            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");
            try {
                OkHttpClient client = new  OkHttpClient();

                JSONObject dato = new JSONObject();
                dato.put("Email", UsuarioReg);
                dato.put("Password",Contraseña);
                dato.put("Nombre",Nombre);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                //Log.d("Response", response.body().string());
            }
            catch (IOException |JSONException e) {
                Log.d("Error", e.getMessage());
            }
            Intent intentIngresar= new Intent(getApplicationContext(),LogActivity.class);
            startActivity(intentIngresar);
            return "registrado";
        }


    }
}
