package com.example.leopasca.guemarapp;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class pruebaNotaVoz extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String OUTPUT_FILE;
    private MediaRecorder recorder;
    String nombreNota;
    TextView txvAudio;
    public void ObtenerReferencias()
    {
        txvAudio =(TextView)findViewById(R.id.txvAudio);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_nota_voz);
        ObtenerReferencias();
        Bundle extras = getIntent().getExtras();
        nombreNota=extras.getString("Nombre");
        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/"+nombreNota+".3gpp";
        txvAudio.setText(nombreNota);
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
}
