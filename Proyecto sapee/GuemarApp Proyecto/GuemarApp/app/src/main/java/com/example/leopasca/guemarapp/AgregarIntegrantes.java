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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarIntegrantes extends AppCompatActivity {
    ListView listview;
    Integer IdGrupo =0;
    public void ObtenerReferencias()
    {
        listview =(ListView)findViewById(R.id.listAgregar);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_integrantes);
        ObtenerReferencias();
        Intent intent = getIntent();
        IdGrupo = intent.getIntExtra("IdGrupo",0);
        TraerIntegrantes traerIntegrantes = new TraerIntegrantes(getApplicationContext());
        traerIntegrantes.execute("http://leopashost.hol.es/bd/TraerIntegrantesAgregar.php?IdGrupo="+IdGrupo);
        listview.setOnItemClickListener(item);
    }
    public AdapterView.OnItemClickListener item = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String Id= (listview.getItemAtPosition(position).toString());

        }
    };
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
