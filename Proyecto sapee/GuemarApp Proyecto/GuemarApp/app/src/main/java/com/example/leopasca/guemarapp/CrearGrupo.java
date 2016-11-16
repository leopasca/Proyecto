package com.example.leopasca.guemarapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrearGrupo extends AppCompatActivity {
    TextView txvNombre;
    ListView listview;
    Integer IdGrupo =0 ;
    String Nombre ="";
    Button btnEditar;
    ImageButton agregar;
    ArrayList<String> Nombres;
    ArrayAdapter<String> adapter;
    Button btnEliminarGrupo;
    public void ObtenerReferencias()
    {

        txvNombre = (TextView)findViewById(R.id.txvNombre);
        listview =(ListView)findViewById(R.id.listView2);
        btnEditar =(Button)findViewById(R.id.btnEditar);
        agregar = (ImageButton)findViewById(R.id.imbagregar);
        btnEliminarGrupo =(Button)findViewById(R.id.btnEliminarGrupo);
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
        btnEditar.setOnClickListener(editar);
        listview.setOnItemLongClickListener(listener);
        agregar.setOnClickListener(agregarUsuarios);
        btnEliminarGrupo.setOnClickListener(Eliminar);



    }
    public View.OnClickListener Eliminar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EliminarGrupo eliminar = new EliminarGrupo();
            eliminar.execute(1);
        }
    };
    class EliminarGrupo extends AsyncTask<Integer,Void,String>
    {
        @Override
        protected void onPostExecute(String listo) {
            super.onPostExecute(listo);
            Toast.makeText(CrearGrupo.this, listo, Toast.LENGTH_SHORT).show();
            finish();
        }
        protected String doInBackground(Integer...params) {
            Integer idef = params[0];
            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int IdUsuario = prefs.getInt("IdUsuario",0);

            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/EliminarGrupo.php";
                JSONObject dato = new JSONObject();
                dato.put("IdGrupo", IdGrupo);
                dato.put("IdUsuario",IdUsuario);

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d("Response", response.body().string());
            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return "Grupo Eliminado";
        }
    }
    public View.OnClickListener agregarUsuarios = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),AgregarIntegrantes.class);
            intent.putExtra("IdGrupo",IdGrupo);
            intent.putExtra("Nombre",Nombre);
            startActivity(intent);
            finish();

        }
    };
    public AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            String Id= (listview.getItemAtPosition(position).toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(CrearGrupo.this);
            builder.setTitle("Eliminar Integrante");
            builder.setMessage("¿Esta seguro que quiere eliminar este integrante?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String NombreIntegrante = listview.getItemAtPosition(position).toString();
                    EliminarIntegrante eliminar = new EliminarIntegrante(CrearGrupo.this);
                    eliminar.execute(NombreIntegrante);
                    Nombres.remove(position);
                    adapter.notifyDataSetChanged();


                }

            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                }
            });
            Dialog dialog = builder.create();
            dialog.show();
            return false;
        }
    };

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
            TraerIntegrantes traerIntegrantes = new TraerIntegrantes(context);
            traerIntegrantes.execute("http://leopashost.hol.es/bd/TraerIntegrantes.php?IdGrupo="+comentResult);
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
                Integer IdGrupo2 = objComen.getInt("IdGrupo");
                String Nombre = objComen.getString("Nombre");
                Grupos grupo = new Grupos(IdGrupo, Nombre);
                listgrupos = IdGrupo2;

            }
            return listgrupos;
        }

    }
    class EliminarIntegrante extends AsyncTask<String,Void,String>
    {
        private ProgressDialog pdia;
        public Context context;
        public EliminarIntegrante(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }
        protected void onPreExecute(){
            this.pdia.setMessage("Eliminando");
            this.pdia.show();
        }

        @Override
        protected void onPostExecute(String listo) {
            super.onPostExecute(listo);
            if (pdia.isShowing())
            {
                pdia.dismiss();
            }
            Toast.makeText(getApplicationContext(), listo, Toast.LENGTH_SHORT).show();

        }
        protected String doInBackground(String...params) {
            String idef = params[0];
            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");

            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/EliminarIntegrante.php";
                JSONObject dato = new JSONObject();
                dato.put("Nombre", idef);
                dato.put("IdGrupo",IdGrupo);

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d("Response", response.body().string());
            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return "Integrante Eliminado";
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
    public View.OnClickListener editar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),EditarGrupo.class);
            intent.putExtra("IdGrupo",IdGrupo);
            startActivity(intent);
        }
    };
}
