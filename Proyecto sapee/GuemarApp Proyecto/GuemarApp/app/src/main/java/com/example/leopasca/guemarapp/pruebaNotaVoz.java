package com.example.leopasca.guemarapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class pruebaNotaVoz extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String OUTPUT_FILE;
    private MediaRecorder recorder;
    String nombreNota;
    TextView txvAudio;
    int IdNota;
    Button btnSubir;
    public void ObtenerReferencias()
    {

        txvAudio =(TextView)findViewById(R.id.txvAudio);
        btnSubir=(Button)findViewById(R.id.btnSubir);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_nota_voz);
        ObtenerReferencias();
        Bundle extras = getIntent().getExtras();
        nombreNota=extras.getString("Nombre");
        IdNota =extras.getInt("IdNota");
        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/"+nombreNota+".3gpp";

        txvAudio.setText(nombreNota);

        btnSubir.setOnClickListener(btnSubir_click);
    }
    public void butonApretado(View view)
    {
        switch (view.getId())
        {
            case R.id.imgGrabar:
                try
                {
                    beginRecording();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.imgTerminar:
                try
                {
                    stopnRecording();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.imgPlay:
                try
                {
                    playRecording();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.imgPausa:
                try
                {
                    pauseRecording();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.imgEliminar:
                Eliminar();
            break;
        }
    }
    private void beginRecording() throws Exception
    {
     ditchMediaRecorder();
        File file = new File(OUTPUT_FILE);
        if(file.exists())
        {
            file.delete();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();

    }
    private void ditchMediaRecorder()
    {
        if(recorder!=null)
        {
            recorder.release();
        }
    }
    private void stopnRecording()
    {
    if (recorder != null)
    {
        recorder.stop();
    }
    }
    private void playRecording() throws Exception
    {
        dithMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
    private void dithMediaPlayer()
    {
        if(mediaPlayer!=null)
        {
            try {
                mediaPlayer.release();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private void pauseRecording()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }
    private void Eliminar()
    {
        Dialog dialogo = EliminarNota(IdNota);
        dialogo.show();
    }
    private Dialog EliminarNota(final int idef) {

        Log.e("GULE", "3");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Nota de voz");
        builder.setMessage("¿Esta seguro que quiere eliminar la Nota?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HttpPost post = new HttpPost();
                post.setHeader("content-type", "application/json");

                try {
                    OkHttpClient client = new  OkHttpClient();
                    String url ="http://leopashost.hol.es/bd/EliminarNota.php";
                    JSONObject dato = new JSONObject();
                    dato.put("IdNota", idef);

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
                File file = new File(OUTPUT_FILE);
                file.delete();
                Intent intentAMain = new Intent(getApplicationContext(),EdicionActivity.class);
                startActivity(intentAMain);
                finish();

            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }
    public View.OnClickListener btnSubir_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileInputStream fileInputStream=null;
            File file = new File(OUTPUT_FILE);
            byte[] bFile = new byte[(int)file.length()];
            try {
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    };
}
