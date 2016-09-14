package com.example.leopasca.guemarapp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PruebaBajarNota extends AppCompatActivity {
    Button btnBajar;
    Button btnPlay;
    String Nota ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_bajar_nota);
        btnBajar = (Button)findViewById(R.id.btnBajar);
        btnPlay =(Button)findViewById(R.id.btnPlay);
        btnBajar.setOnClickListener(bajar);

    }
    public View.OnClickListener bajar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String nombre = "pru";
            HttpGet get = new HttpGet();
            get.setHeader("content-type", "application/json");
            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://www.leopashost.hol.es/bd/BajarNota.php?Nombre="+nombre;
                JSONObject dato = new JSONObject();
                dato.put("IdVideo",nombre);
                Request request1 = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request1).execute();
                String resp = response.body().string();
                Nota = getNombre(resp);
                Log.d("url", response.body().string());
                byte[]bytes = Nota.getBytes();
                String OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/"+"pru"+".mp3";
                File outputFile = new File(OUTPUT_FILE);
                outputFile.deleteOnExit();
                FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                fileoutputstream.write(bytes);
                fileoutputstream.close();
            }
            catch (IOException |JSONException e) {
                Log.d("Error", e.getMessage());
            }
        }
    };
    String getNombre (String json)
    {
        try {

            String prueba = json.replace("[","");
            String prueba2 = prueba.replace("]","");
            JSONObject obj=new JSONObject(prueba2);
            return obj.getString("Nota");

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
