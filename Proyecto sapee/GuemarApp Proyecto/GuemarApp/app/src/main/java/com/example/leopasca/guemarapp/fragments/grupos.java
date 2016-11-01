package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.leopasca.guemarapp.Comentario;
import com.example.leopasca.guemarapp.Grupos;
import com.example.leopasca.guemarapp.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

/**
 * Created by AMD on 31/10/2016.
 */
public class grupos extends Fragment {
    FrameLayout layout;
    Button btnCreargrupo;
    ListView listview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gruposfrag, container, false);
        return rootView;
    }
    public void ObtenerReferencias()
    {
       layout = (FrameLayout)getView().findViewById(R.id.GruposLayout);
        btnCreargrupo = (Button)getView().findViewById(R.id.btnCrearGrupo);
        listview =(ListView)getView().findViewById(R.id.listView);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
        TraerGrupos taskTraer = new TraerGrupos(getActivity());
        taskTraer.execute("http://leopashost.hol.es/bd/TraerGrupos.php");
        btnCreargrupo.setOnClickListener(crearGrupo);
    }
    public View.OnClickListener crearGrupo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    class TraerGrupos extends AsyncTask<String, Void, List<Grupos>> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog pdia;
        public Context context;
        public TraerGrupos(Context activity)
        {
            context = activity;
            pdia = new ProgressDialog(context);
        }
        protected void onPreExecute(){
            this.pdia.setMessage("Cargando Grupos");
            this.pdia.show();
        }

        @Override
        protected void onPostExecute(List<Grupos> comentResult) {
            super.onPostExecute(comentResult);
            if (pdia.isShowing())
            {
                pdia.dismiss();
            }
            ArrayList<String>Nombres = new ArrayList<String>();
            for(Grupos comen:comentResult) {
            String Nombre = comen.Nombre;
                Nombres.add(Nombre);
            }
            ArrayAdapter<String> adapter=
            new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Nombres);
            listview.setAdapter(adapter);



        }

        List<Comentario> listson = new ArrayList<Comentario>();

        @Override
        protected List<Grupos> doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                //Log.d("Anda",response.body().string());
                return parse2(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return new ArrayList<Grupos>();
            }
        }

        List<Grupos> parse2(String json) throws JSONException

        {
            List<Grupos> listgrupos;
            JSONArray jsonobj = new JSONArray(json);
            listgrupos = new ArrayList<Grupos>();
            for (int i = 0; i < jsonobj.length(); i++) {

                JSONObject objComen = jsonobj.getJSONObject(i);
                Integer IdGrupo = objComen.getInt("IdGrupo");
                String Nombre = objComen.getString("Nombre");
                Grupos grupo = new Grupos(IdGrupo, Nombre);
                listgrupos.add(grupo);

            }
            return listgrupos;
        }

    }
}
