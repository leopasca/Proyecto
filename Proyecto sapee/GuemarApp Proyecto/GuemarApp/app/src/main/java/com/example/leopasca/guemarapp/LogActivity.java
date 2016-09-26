package com.example.leopasca.guemarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    Button btnIngresar;
    EditText edtUsuario;
    EditText edtPass;
    List<Usuario>listUs;
    String EmailIngresado ="";
    String ContraseñaIngresada="";
    TextView txvCrearCuenta;
    public void ObtenerReferencias()
    {
        btnIngresar =(Button)findViewById(R.id.btnIngresar);
        edtUsuario = (EditText)findViewById(R.id.edtUsuario);
        edtPass = (EditText)findViewById(R.id.edtPass);
        txvCrearCuenta = (TextView)findViewById(R.id.txvCrearCuenta);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ObtenerReferencias();
        btnIngresar.setOnClickListener(ingresar_click);
        txvCrearCuenta.setOnClickListener(crearCuenta);
        listUs = new ArrayList<>();


    }
    private View.OnClickListener crearCuenta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentRegistrar =new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intentRegistrar);
        }
    };
    private View.OnClickListener ingresar_click =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!edtUsuario.getText().toString().isEmpty()||! edtPass.getText().toString().isEmpty())
            {
                EmailIngresado = edtUsuario.getText().toString();
                ContraseñaIngresada = edtPass.getText().toString();
                TraerUsuarios taskUsuarios = new TraerUsuarios(getApplicationContext());
                taskUsuarios.execute("http://leopashost.hol.es/bd/TraerUsuarios.php");
            }
            else
            {
                Toast.makeText(LogActivity.this, "Ingrese los campos correspondientes", Toast.LENGTH_SHORT).show();
            }
        }
    };
    class TraerUsuarios extends AsyncTask<String,Void, List<Usuario>> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public TraerUsuarios(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }

      /*  protected void onPreExecute(){
            this.pdia.setMessage("Cargando...");
            this.pdia.show();
        }*/
        @Override
        protected void onPostExecute(List<Usuario> usuariosResult) {
            super.onPostExecute(usuariosResult);
            if (!usuariosResult.isEmpty()) {
                listUs = usuariosResult;
                //JSONArray jsonobj = new JSONArray(json);
             /*  for(int i=0;i<liscom.size();i++) {
               String json = liscom.get(i).toString();
               }*/
                Integer IdUsuario =0;
                String UsuarioBase="";
                String ContraseñaBasee="";
                String Nombre ="";
                for(Usuario usuario:listUs)
                {
                   IdUsuario  = usuario.IdUsuario;
                     UsuarioBase= usuario.Usuario;
                     ContraseñaBasee= usuario.Contraseña;
                    Nombre = usuario.Nombre;
                    if(UsuarioBase.contentEquals(EmailIngresado)&& ContraseñaIngresada.contentEquals(ContraseñaBasee))
                    {
                        if(pdia.isShowing()) {
                            pdia.dismiss();
                        }
                        Intent intentAEdicion = new Intent(getApplicationContext(),EdicionActivity.class);
                        startActivity(intentAEdicion);
                    }

                }


            }
        }
        List<Usuario> listUsuarios = new ArrayList<Usuario>();


        @Override
        protected List<Usuario> doInBackground(String...params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                //Log.d("Anda",response.body().string());
                return parse(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return new ArrayList<Usuario>();
            }
        }
        public  List<Usuario> listUsuario;

        List<Usuario>parse(String json)throws JSONException

        {
            JSONArray jsonobj = new JSONArray(json);
            listUsuario = new ArrayList<Usuario>();
            for(int i=0;i<jsonobj.length();i++)
            {

                JSONObject objUsuario = jsonobj.getJSONObject(i);
                Integer IdUsuario = objUsuario.getInt("IdUsuario");
                String Email = objUsuario.getString("Email");
                String Contraseña = objUsuario.getString("Contraseña");
                String Nombre = objUsuario.getString("Nombre");

                Usuario usuario = new Usuario(IdUsuario, Email, Contraseña,Nombre);
                listUsuario.add(usuario);

            }
            return listUsuario;
        }


    }
}
