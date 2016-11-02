package com.example.leopasca.guemarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class creargrupo extends AppCompatActivity {
    TextView txvNombre;
    ListView listview;
    Integer IdGrupo =0 ;
    String Nombre ="";
    public void ObtenerReferencias()
    {

        txvNombre = (TextView)findViewById(R.id.txvNombre);
        listview =(ListView)findViewById(R.id.listView2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        ObtenerReferencias();
        Intent intent =getIntent();
         Nombre= intent.getStringExtra("Nombre");
        txvNombre.setText(Nombre);
        TraerGrupo traerGrupo = new TraerGrupo(this);
        traerGrupo.execute("http://leopashost.hol.es/bd/TraerGrupo.php?Nombre="+Nombre);
        TraerIntegrantes traerIntegrantes = new TraerIntegrantes(this);
        traerIntegrantes.execute("http://leopashost.hol.es/bd/TraerIntegrantes.php?IdGrupo"+IdGrupo);

    }
    class TraerGrupo extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public TraerGrupo(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }
        protected void onPreExecute(){
            this.pdia.setMessage("Cargando Id");
            this.pdia.show();
        }

        @Override
        protected void onPostExecute(Integer comentResult) {
            super.onPostExecute(comentResult);
            if (pdia.isShowing())
            {
                pdia.dismiss();
            }
            IdGrupo = comentResult;

        }

        @Override
        protected Integer doInBackground(String... params) {
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
                return new Integer(IdGrupo);
            }
        }

        Integer parse2(String json) throws JSONException

        {
            Integer listgrupos=0;
            JSONArray jsonobj = new JSONArray(json);

            for (int i = 0; i < jsonobj.length(); i++) {
                JSONObject objComen = jsonobj.getJSONObject(i);
                Integer IdGrupo = objComen.getInt("IdGrupo");
                String Nombre = objComen.getString("Nombre");
                Grupos grupo = new Grupos(IdGrupo, Nombre);
                listgrupos = IdGrupo;

            }
            return listgrupos;
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
            ArrayList<String> Nombres = new ArrayList<String>();
            for(Usuario comen:comentResult) {
                String Nombre = comen.Nombre;
                Nombres.add(Nombre);
            }
            ArrayAdapter<String> adapter=
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
                String Usuario = objComen.getString("Usuario");
                String Contraseña = objComen.getString("Contraseña");
                Integer IdGrupo = objComen.getInt("IdGrupo");
                String Nombre = objComen.getString("Nombre");
                Usuario usuario = new Usuario(IdUsuario,Usuario,Contraseña,Nombre,IdGrupo);
                listInteg.add(usuario);

            }
            return listInteg;
        }

    }
}
