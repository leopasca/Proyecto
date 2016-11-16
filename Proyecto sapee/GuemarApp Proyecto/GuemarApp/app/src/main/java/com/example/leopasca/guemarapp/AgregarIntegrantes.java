package com.example.leopasca.guemarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarIntegrantes extends AppCompatActivity {
    ListView listview;
    Integer IdGrupo =0;
    ArrayList<String> Nombres;
    ArrayAdapter<String> adapter;
    ArrayList<String>NombresAgregar;
    Button btnListo;
    String NombreGrupo ="";
    public void ObtenerReferencias()
    {
        listview =(ListView)findViewById(R.id.listAgregar);
        btnListo = (Button) findViewById(R.id.btnListo);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_integrantes);
        ObtenerReferencias();
        Intent intent = getIntent();
        NombresAgregar = new ArrayList<String>();
        IdGrupo = intent.getIntExtra("IdGrupo",0);
        NombreGrupo =intent.getStringExtra("Nombre");
        TraerIntegrantes traerIntegrantes = new TraerIntegrantes(this);
        traerIntegrantes.execute("http://leopashost.hol.es/bd/TraerIntegrantesAgregar.php?IdGrupo="+IdGrupo);
        listview.setOnItemClickListener(item);
        btnListo.setOnClickListener(listo);
    }
    public View.OnClickListener listo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         AgregarIntegrantesGrupo agregar = new AgregarIntegrantesGrupo(AgregarIntegrantes.this);
         agregar.execute("http://leopashost.hol.es/bd/AgregarIntegrantesGrupo.php");
        }
    };
    public AdapterView.OnItemClickListener item = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String Id= (listview.getItemAtPosition(position).toString());
            NombresAgregar.add(Id);
            Toast.makeText(AgregarIntegrantes.this, "añadido", Toast.LENGTH_SHORT).show();
            Nombres.remove(position);
            adapter.notifyDataSetChanged();


        }
    };
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish()
    }
    class AgregarIntegrantesGrupo extends AsyncTask<String,Void, String> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public AgregarIntegrantesGrupo(Context activity)
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
            Toast.makeText(getApplicationContext(), Registrado, Toast.LENGTH_SHORT).show();
            Intent intentVuelta = new Intent(getApplicationContext(),CrearGrupo.class);
            intentVuelta.putExtra("Nombre",NombreGrupo);
            startActivity(intentVuelta);
            finish();

        }

        @Override
        protected String doInBackground(String...params) {
            String url = params[0];

            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");
            try {
                for (int i=0 ; i< NombresAgregar.size();i++) {
                    String NombreUsuario = NombresAgregar.get(i);
                    OkHttpClient client = new OkHttpClient();

                    JSONObject dato = new JSONObject();
                    dato.put("Nombre", NombreUsuario);
                    dato.put("IdGrupo", IdGrupo);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    //Log.d("Response", response.body().string());
                }
            }
            catch (IOException |JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return "Añadidos";
        }


    }
    class TraerIntegrantes extends AsyncTask<String, Void, List<Usuario>> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public TraerIntegrantes(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }
        @Override
        protected void onPreExecute(){
            this.pdia.setMessage("Cargando Integrantes");
            this.pdia.show();
        }

        @Override
        protected void onPostExecute(List<Usuario> comentResult) {
            super.onPostExecute(comentResult);
            if (pdia.isShowing())
            {
                pdia.dismiss();
            }
             Nombres = new ArrayList<String>();
            for(Usuario comen:comentResult) {
                String Nombre = comen.Nombre;
                Nombres.add(Nombre);
            }
             adapter=
                    new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Nombres);
            listview.setAdapter(adapter);

        }

        @Override
        protected List<Usuario> doInBackground(String... params) {
            String url = params[0];
            HttpGet get = new HttpGet();
            get.setHeader("content-type", "application/json");
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                //Log.d("Anda",response.body().string());
                return parse2(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return new ArrayList<Usuario>();
            }
        }

        List<Usuario> parse2(String json) throws JSONException

        {
            List<Usuario> listInteg;
            JSONArray jsonobj = new JSONArray(json);
            listInteg = new ArrayList<Usuario>();
            for (int i = 0; i < jsonobj.length(); i++) {

                JSONObject objComen = jsonobj.getJSONObject(i);
                Integer IdUsuario = objComen.getInt("IdUsuario");
                String Usuario = objComen.getString("Email");
                String Contraseña = objComen.getString("Password");
                String Nombre = objComen.getString("Nombre");
                Usuario usuario = new Usuario(IdUsuario,Usuario,Contraseña,Nombre);
                listInteg.add(usuario);

            }
            return listInteg;
        }

    }

}
