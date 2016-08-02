package com.example.leopasca.guemarapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.joanzapata.pdfview.PDFView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vudroid.pdfdroid.codec.PdfContext;
import org.vudroid.pdfdroid.codec.PdfPage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

public class EdicionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        PDFView pdfView;
        ImageButton imbComentario;
        ImageButton imbResaltador;
        ImageButton imbVoz;
        RelativeLayout layout;
        ImageButton imbComentarioHoja;
        Boolean ExisteImbBoton = false;
        ImageButton imbVideo;
        String urlVideo ="";
        int i = 0;
        int j =0;
        int k=0;
        private boolean ignore = false;
        ImageButton imbVideoHoja;
        private float x, y;
        private float xVideo, yVideo;
        private float xNota, yNota;
         Map<Integer,EditText>MapEDT = new HashMap<Integer, EditText>();
        Map<Integer,ImageButton>MapIMB = new HashMap<Integer, ImageButton>();
        Map<Integer,ImageView>MapIMG = new HashMap<Integer, ImageView>();
        Map<Integer,ImageButton>MapIMBVideo = new HashMap<Integer, ImageButton>();
        Map<Integer,ImageView>MapIMGVideo = new HashMap<Integer, ImageView>();
    Map<Integer,ImageButton>MapIMBNota = new HashMap<Integer, ImageButton>();
    Map<Integer,ImageView>MapIMGNota = new HashMap<Integer, ImageView>();
        List<Comentario>liscom ;
        List<Video>lisvid ;
         List<Nota>lisnot ;
        Button Pasar;
        private Path drawPath;
        private Paint drawPaint,canvasPaint;
        private int paintColor= 0xFFFF66;
        private Canvas drawCanvas;
        private Bitmap canvasBitmap;
        private MediaPlayer mediaPlayer;
        private String OUTPUT_FILE;





    public void ObtenerReferencias() {
        pdfView = (PDFView) findViewById(R.id.pdfview);
        imbComentario = (ImageButton) findViewById(R.id.imbComentario);
        imbResaltador = (ImageButton) findViewById(R.id.imbResaltador);
        layout = (RelativeLayout) findViewById(R.id.LayoutRelativo);
        imbComentarioHoja = (ImageButton) findViewById(R.id.imbComentarioHoja);
        Pasar = (Button)findViewById(R.id.btnPasar);
        imbVideo =(ImageButton)findViewById(R.id.imbVideo);
        imbVoz = (ImageButton)findViewById(R.id.imbVoz);
        imbVideoHoja =(ImageButton)findViewById(R.id.imbVideoHoja);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
        ObtenerReferencias();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        liscom = new ArrayList<>();
        lisvid = new ArrayList<>();
        lisnot = new ArrayList<>();
        pdfView.fromAsset("beitza.pdf").load();

        imbComentario.setOnClickListener(imbComentario_click);
        if (ExisteImbBoton == true) {
            imbComentarioHoja.setOnClickListener(imbComentarioHoja_click);
            imbComentarioHoja.setOnLongClickListener(imbEliminar_click);

        }
        //imbVideoHoja.setOnClickListener(imbVideoHoja_click);
        ProgressTask task = new ProgressTask();
        task.execute("http://leopashost.hol.es/bd/ListarComentarios.php");
        ListarVideos taskVideo = new ListarVideos();
        taskVideo.execute("http://leopashost.hol.es/bd/ListarVideos.php");
        ListarNotas listarNotas = new ListarNotas();
        listarNotas.execute("http://leopashost.hol.es/bd/ListarNotas.php");
        imbVideo.setOnClickListener(imbVideo_click);
        Pasar.setOnClickListener(pasar);
        imbVoz.setOnClickListener(imbVoz_click);


    }
    public View.OnClickListener pasar = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent elintent = new Intent(getApplicationContext(), pruebaNotaVoz.class);
            startActivity(elintent);
        }
    };

    private View.OnClickListener imbComentario_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Dialog dialogo = dialogoCrearCuadro();
            Log.e("GULE", "1");
            dialogo.show();
            Log.e("Gule", "2");
        }
    };

    private Dialog dialogoCrearCuadro() {

        Log.e("GULE", "3");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comentario");
        builder.setMessage("Presione el documento para crear un comentario");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ignore = false;
                pdfView.setOnTouchListener(comentarioGuemara);


            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private View.OnTouchListener comentarioGuemara = new View.OnTouchListener() {


        int ex, way;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (ignore == true) {
                return false;
            } else {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        x = event.getX();
                        y = event.getY();
                        String IdComentario="";
                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        ImageButton imbBoton = new ImageButton(getApplicationContext());
                        imbBoton.setImageResource(R.mipmap.comentario_hoja);
                        imbBoton.setX(600);
                        imbBoton.setY(y);
                        imbBoton.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

                        //ListImageButons.add(i,imbBoton);
                        layout.addView(imbBoton);
                        HttpPost post = new HttpPost();
                        post.setHeader("content-type", "application/json");

                        try {
                            OkHttpClient client = new  OkHttpClient();
                            String url ="http://leopashost.hol.es/bd/CrearComentario.php";
                            JSONObject dato = new JSONObject();
                            dato.put("CordComentarioY",y);
                            dato.put("CordAsteriscoX",x);
                            dato.put("CordAsteriscoY",y);
                            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String prueba = response.body().string();
                            IdComentario =  getIdComentario(prueba);

                        }
                        catch (IOException|JSONException e) {
                            Log.d("Error", e.getMessage());
                        }
                        i= Integer.parseInt(IdComentario);
                        imbBoton.setId(i);
                        ImageView imgAsterisco = new ImageView(getApplicationContext());
                        MapIMB.put(i,imbBoton);
                        imgAsterisco.setImageResource(R.mipmap.asterisco);
                        imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
                        imgAsterisco.setX(x);
                        imgAsterisco.setY(y);
                        MapIMG.put(i,imgAsterisco);
                        layout.addView(imgAsterisco);
                        //ListAsteriscos.add(i,imgAsterisco);

                        // BitmapDrawable bmpdraw
                        //bmp = Bitmap.createBitmap()
                        EditText edtComentario = new EditText(getApplicationContext());
                        Log.e("error", "3");
                        edtComentario.setY(y);
                        edtComentario.setId(i);
                        MapEDT.put(i,edtComentario);
                        edtComentario.setX(140);
                        edtComentario.setBackgroundColor(Color.parseColor("#A6A6A6"));
                        edtComentario.setVisibility(View.INVISIBLE);
                        edtComentario.setHint("Comentario");
                        Log.e("error","4");
                        edtComentario.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
                        Log.e("error","5");
                        //ListButons.add(i,edtComentario);
                        Log.e("error","6");
                        layout.addView(edtComentario);
                        Log.e("error","7");
                        imbBoton.setOnClickListener(imbComentarioHoja_click);
                        imbBoton.setOnLongClickListener(imbEliminar_click);

                        Log.e("error","8");

                        ignore = true;

                }
            }
            return true;
        }

    };
     String getIdComentario(String json)
    {
        try {

            String prueba = json.replace("[","");
            String prueba2 = prueba.replace("]","");
            JSONObject obj=new JSONObject(prueba2);
            return obj.getString("IdComentario");

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    private View.OnClickListener imbComentarioHoja_click = new View.OnClickListener() {
        public void onClick(View v) {
            final int id = v.getId();
            if(MapEDT.get(id).getVisibility() == View.INVISIBLE)
            {
                MapEDT.get(id).setVisibility(View.VISIBLE);
            }
            else
            {
                if(!MapEDT.get(id).getText().toString().trim().matches("")) {
                    //REVISAR ESTO
                    // ListButons.get(id).setBackgroundResource(R.drawable.lost_focus_border_style);
                    MapEDT.get(id).setVisibility(View.INVISIBLE);


                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        HttpPost post = new HttpPost();
                        post.setHeader("content-type", "application/json");

                    try {
                        OkHttpClient client = new  OkHttpClient();
                        String url ="http://leopashost.hol.es/bd/ActualizarComentario.php";
                        JSONObject dato = new JSONObject();
                        dato.put("Comentario", MapEDT.get(id).getText().toString());
                        dato.put("IdComentario", id);
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
                }
                else
                {
                    MapEDT.get(id).setBackgroundResource(R.drawable.focus_border_style);
                    Toast.makeText(EdicionActivity.this, "Ingrese un comentario", Toast.LENGTH_SHORT).show();
                }
            }


        }


    };
    private View.OnLongClickListener imbEliminar_click = new View.OnLongClickListener() {

        public boolean onLongClick(View v) {
            final int id = v.getId();
            Dialog dialogo = EliminarCuadro(id);
            dialogo.show();
            return true;
        }
    };

    private Dialog EliminarCuadro(final int idef) {

        Log.e("GULE", "3");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Comentario");
        builder.setMessage("¿Esta seguro que quiere eliminar el comentario?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HttpPost post = new HttpPost();
                post.setHeader("content-type", "application/json");

                try {
                    OkHttpClient client = new  OkHttpClient();
                    String url ="http://leopashost.hol.es/bd/EliminarComentario.php";
                    JSONObject dato = new JSONObject();
                    dato.put("IdComentario", idef);

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
                Toast.makeText(EdicionActivity.this, "Se ha eliminado", Toast.LENGTH_SHORT).show();
                MapEDT.get(idef).setVisibility(View.INVISIBLE);
                MapIMB.get(idef).setVisibility(View.INVISIBLE);
                MapIMG.get(idef).setVisibility(View.INVISIBLE);
                MapEDT.remove(idef);
                MapIMG.remove(idef);
                MapIMB.remove((idef));



            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edicion, menu);
        return true;
    }
    public Dialog dialogourl()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Video");
        builder1.setMessage("Ingrese una url");
        final EditText input = new EditText(EdicionActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("URL");
        builder1.setView(input);
        builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               if(!input.getText().toString().isEmpty()) {
                   if(input.getText().toString().length()>=11) {
                       urlVideo taskVideo = new urlVideo();
                       taskVideo.execute(input.getText().toString().trim().substring(input.getText().toString().trim().length() - 11));
                   }
                   else
                   {
                       Toast.makeText(EdicionActivity.this, "Ingrese una url completa", Toast.LENGTH_SHORT).show();
                   }
                   
               }
                else
               {
                   Toast.makeText(EdicionActivity.this, "Ingrese una url", Toast.LENGTH_SHORT).show();
               }
            }

        });
        builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder1.create();
    }
    private View.OnTouchListener videoGuemara = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (ignore == true) {
                return false;
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        xVideo = event.getX();
                        yVideo = event.getY();
                        Dialog dialogourl = dialogourl();
                        dialogourl.show();
                        ignore = true;

                }
            }
            return true;
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Menu) {

        } else if (id == R.id.Amigos) {

        } else if (id == R.id.Grupos) {

        } else if (id == R.id.Acerca) {

        } else if (id == R.id.Cerrar) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
   public View.OnClickListener imbVideo_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialog dialogo = dialogoCrearVideo();
            Log.e("GULE", "video");
            dialogo.show();
            Log.e("Gule", "2video");
            Log.e("GULE", "3video");


        }

    };
    public Dialog dialogoCrearVideo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Video");
        builder.setMessage("Presione el documento para crear un Video");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ignore = false;
                pdfView.setOnTouchListener(videoGuemara);


            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }
    class ProgressTask extends AsyncTask<String,Void, List<Comentario>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<Comentario> comentResult) {
            super.onPostExecute(comentResult);
            if (!comentResult.isEmpty()) {
                liscom = comentResult;
                //JSONArray jsonobj = new JSONArray(json);
             /*  for(int i=0;i<liscom.size();i++) {
               String json = liscom.get(i).toString();
               }*/
                for(Comentario comen:liscom)
                {
                    Integer idComentario = comen.IdComentario;
                    String comentario = comen.Comentario;
                    String CordComentarioY = comen.CordComentarioY.toString();
                    String CordAsteriscoX = comen.CordAsteriscoX.toString();
                    String CordAsteriscoY = comen.CordAsteriscoY.toString();
                    Float CordComentarioYfloat = Float.parseFloat(CordComentarioY);
                    Float CordAsteriscoXfloat = Float.parseFloat(CordAsteriscoX);
                    Float CordAsteriscoYfloat = Float.parseFloat(CordAsteriscoY);

                    Log.d("VALOR",idComentario.toString());
                    ImageButton imbBoton = new ImageButton(getApplicationContext());
                    imbBoton.setImageResource(R.mipmap.comentario_hoja);
                    imbBoton.setX(600);
                    imbBoton.setY(CordComentarioYfloat);
                    imbBoton.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                    layout.addView(imbBoton);
                    imbBoton.setId(idComentario);

                    ImageView imgAsterisco = new ImageView(getApplicationContext());
                    MapIMB.put(idComentario,imbBoton);
                    imgAsterisco.setImageResource(R.mipmap.asterisco);
                    imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
                    imgAsterisco.setX(CordAsteriscoXfloat);
                    imgAsterisco.setY(CordAsteriscoYfloat);
                    MapIMG.put(idComentario,imgAsterisco);
                    layout.addView(imgAsterisco);

                    EditText edtComentario = new EditText(getApplicationContext());
                    Log.e("error", "3");
                    edtComentario.setY(CordComentarioYfloat);
                    edtComentario.setId(idComentario);
                    MapEDT.put(idComentario,edtComentario);
                    edtComentario.setX(140);
                    edtComentario.setBackgroundColor(Color.parseColor("#A6A6A6"));
                    edtComentario.setVisibility(View.INVISIBLE);
                    edtComentario.setHint("Comentario");
                    Log.e("error","4");
                    edtComentario.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
                    edtComentario.setText(comentario);
                    Log.e("error","5");
                    //ListButons.add(i,edtComentario);
                    Log.e("error","6");
                    layout.addView(edtComentario);
                    Log.e("error","7");
                    imbBoton.setOnClickListener(imbComentarioHoja_click);
                    imbBoton.setOnLongClickListener(imbEliminar_click);

                }
                Log.d("Dalee",liscom.toString());


            }
        }
        List<Comentario> listson = new ArrayList<Comentario>();


        @Override
        protected List<Comentario> doInBackground(String...params) {
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
                return new ArrayList<Comentario>();
            }
        }
        public  List<Comentario> listComen;

        List<Comentario>parse(String json)throws JSONException

        {
            JSONArray jsonobj = new JSONArray(json);
            listComen = new ArrayList<Comentario>();
            for(int i=0;i<jsonobj.length();i++)
            {

                JSONObject objComen = jsonobj.getJSONObject(i);
               Integer IdComentario = objComen.getInt("IdComentario");
                String Comentario = objComen.getString("Comentario");
                Double CordComentarioY = objComen.getDouble("CordComentarioY");
                Double CordAsteriscoX = objComen.getDouble("CordAsteriscoX");
                Double CordAsteriscoY= objComen.getDouble("CordAsteriscoY");
                Comentario comen = new Comentario(IdComentario, Comentario, CordComentarioY,CordAsteriscoX,CordAsteriscoY);
                listComen.add(comen);

            }
            return listComen;
        }


    }
  class urlVideo extends AsyncTask<String,Void,String>

    {
        protected void onPostExecute(String video)
        {
            super.onPostExecute(video);

            String IdVideo="";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            ImageButton imbBotonVideo = new ImageButton(getApplicationContext());
            imbBotonVideo.setImageResource(R.mipmap.video_hoja);
            imbBotonVideo.setX(600);
            imbBotonVideo.setY(yVideo);
            imbBotonVideo.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

            //ListImageButons.add(i,imbBoton);
            layout.addView(imbBotonVideo);
            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");

            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/CrearVideo.php";
                JSONObject dato = new JSONObject();
                dato.put("url",video);
                dato.put("CordVideoY",yVideo);
                dato.put("CordVideoAsteriscoX",xVideo);
                dato.put("CordVideoAsteriscoY",yVideo);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String prueba = response.body().string();
                IdVideo =  getIdVideo(prueba);

            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }

            j= Integer.parseInt(IdVideo);
            imbBotonVideo.setId(j);
            ImageView imgAsterisco = new ImageView(getApplicationContext());
            MapIMBVideo.put(j,imbBotonVideo);
            imgAsterisco.setImageResource(R.mipmap.asteriscoideo);
            imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            imgAsterisco.setX(xVideo);
            imgAsterisco.setY(yVideo);
            MapIMGVideo.put(j,imgAsterisco);
            layout.addView(imgAsterisco);

            imbBotonVideo.setOnClickListener(imbVideoHoja_click);
            imbBotonVideo.setOnLongClickListener(imbEliminarVideo_click);


        }
        protected String doInBackground(String...params) {
            String url = params[0];
            return url;
        }
        String getIdVideo (String json)
        {
            try {

                String prueba = json.replace("[","");
                String prueba2 = prueba.replace("]","");
                JSONObject obj=new JSONObject(prueba2);
                return obj.getString("IdVideo");

            } catch (JSONException e) {
                e.printStackTrace();
                return "Error";
            }
        }

    }
    public View.OnLongClickListener imbEliminarVideo_click = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            final int id = v.getId();
            Dialog dialogo = EliminarVideo(id);
            dialogo.show();
            return true;
        }
    };
    private Dialog EliminarVideo(final int idef) {

        Log.e("GULE", "3");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Video");
        builder.setMessage("¿Esta seguro que quiere eliminar el Video?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HttpPost post = new HttpPost();
                post.setHeader("content-type", "application/json");

                try {
                    OkHttpClient client = new  OkHttpClient();
                    String url ="http://leopashost.hol.es/bd/EliminarVideo.php";
                    JSONObject dato = new JSONObject();
                    dato.put("IdVideo", idef);

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
                Toast.makeText(EdicionActivity.this, "Se ha eliminado", Toast.LENGTH_SHORT).show();
                MapIMBVideo.get(idef).setVisibility(View.INVISIBLE);
                MapIMGVideo.get(idef).setVisibility(View.INVISIBLE);
                MapIMGVideo.remove(idef);
                MapIMBVideo.remove((idef));



            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }
    public View.OnClickListener imbVideoHoja_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int id = v.getId();
            String urlVideo ="";
            HttpGet get = new HttpGet();
            get.setHeader("content-type", "application/json");
            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/TraerUrl.php?IdVideo="+id;
                JSONObject dato = new JSONObject();
                dato.put("IdVideo",id);
                Request request1 = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = client.newCall(request1).execute();
               String resp = response.body().string();
                urlVideo = getURLVideo(resp);
                Log.d("url", response.body().string());
            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }
            Intent intentAVideo = new Intent(getApplicationContext(),videoPrueba.class);
            intentAVideo.putExtra("url",urlVideo);
            startActivity(intentAVideo);
        }
    };
    String getURLVideo (String json)
    {
        try {

            String prueba = json.replace("[","");
            String prueba2 = prueba.replace("]","");
            JSONObject obj=new JSONObject(prueba2);
            return obj.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    class ListarVideos extends AsyncTask<String,Void, List<Video>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<Video> VideoResult) {
            super.onPostExecute(VideoResult);
            if (!VideoResult.isEmpty()) {
                lisvid = VideoResult;
                //JSONArray jsonobj = new JSONArray(json);
             /*  for(int i=0;i<liscom.size();i++) {
               String json = liscom.get(i).toString();
               }*/
                for(Video video:lisvid)
                {
                    Integer idVideo = video.IdVideo;
                    String url = video.url;
                    String CordVideoY = video.CordVideoY.toString();
                    String CordVideoAsteriscoX = video.CordVideoAsteriscoX.toString();
                    String CordVideoAsteriscoY = video.CordVideoAsteriscoY.toString();
                    Float CordVideoYfloat = Float.parseFloat(CordVideoY);
                    Float CordVideoAsteriscoXfloat = Float.parseFloat(CordVideoAsteriscoX);
                    Float CordVideoAsteriscoYfloat = Float.parseFloat(CordVideoAsteriscoY);

                    ImageButton imbBotonVideo = new ImageButton(getApplicationContext());
                    imbBotonVideo.setImageResource(R.mipmap.video_hoja);
                    imbBotonVideo.setX(600);
                    imbBotonVideo.setY(CordVideoYfloat);
                    imbBotonVideo.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                    layout.addView(imbBotonVideo);
                    imbBotonVideo.setId(idVideo);

                    ImageView imgAsteriscoVideo = new ImageView(getApplicationContext());
                    MapIMBVideo.put(idVideo,imbBotonVideo);
                    imgAsteriscoVideo.setImageResource(R.mipmap.asteriscoideo);
                    imgAsteriscoVideo.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
                    imgAsteriscoVideo.setX(CordVideoAsteriscoXfloat);
                    imgAsteriscoVideo.setY(CordVideoAsteriscoYfloat);
                    MapIMGVideo.put(idVideo,imgAsteriscoVideo);
                    layout.addView(imgAsteriscoVideo);


                    imbBotonVideo.setOnClickListener(imbVideoHoja_click);
                    imbBotonVideo.setOnLongClickListener(imbEliminarVideo_click);

                }
                Log.d("Dalee",liscom.toString());


            }
        }
        List<Comentario> listson = new ArrayList<Comentario>();


        @Override
        protected List<Video> doInBackground(String...params) {
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
                return new ArrayList<Video>();
            }
        }
        public  List<Video> listvid;

        List<Video>parse(String json)throws JSONException

        {
            JSONArray jsonobj = new JSONArray(json);
            listvid = new ArrayList<Video>();
            for(int i=0;i<jsonobj.length();i++)
            {

                JSONObject objComen = jsonobj.getJSONObject(i);
                Integer IdVideo = objComen.getInt("IdVideo");
                String url = objComen.getString("url");
                Double CordVideoY = objComen.getDouble("CordVideoY");
                Double CordVideoAsteriscoX = objComen.getDouble("CordVideoAsteriscoY");
                Double CordVideoAsteriscoY= objComen.getDouble("CordVideoAsteriscoY");
                Video video = new Video(IdVideo, url, CordVideoY,CordVideoAsteriscoX,CordVideoAsteriscoY);
                listvid.add(video);

            }
            return listvid;
        }


    }



    //Nota de voz
    public View.OnClickListener imbVoz_click = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            Dialog dialogo = dialogoCrearNota();

            dialogo.show();



        }

    };
    public Dialog dialogoCrearNota()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nota de voz");
        builder.setMessage("Presione el documento para crear una Nota");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ignore = false;
                pdfView.setOnTouchListener(notaGuemara);


            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }
    private View.OnTouchListener notaGuemara = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (ignore == true) {
                return false;
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        xNota   = event.getX();
                        yNota = event.getY();
                        Dialog dialogoNombreNota = dialogoNombreNota();
                        dialogoNombreNota.show();
                        ignore = true;

                }
            }
            return true;
        }

    };
    public Dialog dialogoNombreNota()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Nota de voz");
        builder1.setMessage("Ingrese el nombre de la nota");
        final EditText input = new EditText(EdicionActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Nombre");
        builder1.setView(input);
        builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!input.getText().toString().isEmpty()) {

                        TaskVoz taskVoz = new TaskVoz();
                        taskVoz.execute(input.getText().toString());


                }
                else
                {
                    Toast.makeText(EdicionActivity.this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder1.create();
    }
    class TaskVoz extends AsyncTask<String,Void,String>

    {
        protected void onPostExecute(String nombre)
        {
            super.onPostExecute(nombre);

            String IdNota="";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            ImageButton imbBotonNota = new ImageButton(getApplicationContext());
            imbBotonNota.setImageResource(R.mipmap.nota_hoja);
            imbBotonNota.setX(600);
            imbBotonNota.setY(yNota);
            imbBotonNota.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

            //ListImageButons.add(i,imbBoton);
            layout.addView(imbBotonNota);
            HttpPost post = new HttpPost();
            post.setHeader("content-type", "application/json");

            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/CrearNotaDeVoz.php";
                JSONObject dato = new JSONObject();
                dato.put("Nombre",nombre);
                dato.put("CordNotaY",yNota);
                dato.put("CordNotaAsteriscoX",xNota);
                dato.put("CordNotaAsteriscoY",yNota);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String prueba = response.body().string();
                IdNota =  getIdNota(prueba);

            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }

            k= Integer.parseInt(IdNota);
            imbBotonNota.setId(k);
            ImageView imgAsterisco = new ImageView(getApplicationContext());
            MapIMBNota.put(k,imbBotonNota);
            imgAsterisco.setImageResource(R.mipmap.asterisco_nota);
            imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            imgAsterisco.setX(xNota);
            imgAsterisco.setY(yNota);
            MapIMGNota.put(k,imgAsterisco);
            layout.addView(imgAsterisco);

            imbBotonNota.setOnClickListener(imbNotaHoja_click);
            imbBotonNota.setOnLongClickListener(imbEditaNota_click);

            Intent intentANota = new Intent(getApplicationContext(),pruebaNotaVoz.class);
            intentANota.putExtra("Nombre",nombre);
            startActivity(intentANota);
        }
        protected String doInBackground(String...params) {
            String nombreNota = params[0];
            return nombreNota;
        }
        String getIdNota (String json)
        {
            try {

                String prueba = json.replace("[","");
                String prueba2 = prueba.replace("]","");
                JSONObject obj=new JSONObject(prueba2);
                return obj.getString("IdNota");

            } catch (JSONException e) {
                e.printStackTrace();
                return "Error";
            }
        }

    }
    public View.OnClickListener imbNotaHoja_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            String Nombre ="";

            HttpGet get = new HttpGet();
            get.setHeader("content-type", "application/json");
            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/TraerNombreNota.php?IdNota="+id;
                JSONObject dato = new JSONObject();
                dato.put("IdNota",id);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                String resp = response.body().string();
                Nombre = getNombreNota(resp);
                Log.d("url", response.body().string());
            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }
            OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/"+Nombre+".3gpp";
            try
            {
                playRecording();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    private void playRecording() throws Exception
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE);
        if(mediaPlayer!=null) {
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        else
        {
            Toast.makeText(EdicionActivity.this, "No existe la nota de voz", Toast.LENGTH_SHORT).show();
        }
    }
    String getNombreNota (String json)
    {
        try {

            String prueba = json.replace("[","");
            String prueba2 = prueba.replace("]","");
            JSONObject obj=new JSONObject(prueba2);
            return obj.getString("Nombre");

        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    class ListarNotas extends AsyncTask<String,Void, List<Nota>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<Nota> notaResult) {
            super.onPostExecute(notaResult);
            if (!notaResult.isEmpty()) {
               lisnot = notaResult;
                //JSONArray jsonobj = new JSONArray(json);
             /*  for(int i=0;i<liscom.size();i++) {
               String json = liscom.get(i).toString();
               }*/
                for(Nota nota:lisnot)
                {
                    Integer idNota = nota.IdNota;
                    String Nombre = nota.Nombre;
                    String CordNotaY = nota.CordNotaY.toString();
                    String CordNotaAsteriscoX = nota.CordNotaAsteriscoX.toString();
                    String CordNotaAsteriscoY = nota.CordNotaAsteriscoY.toString();
                    Float CordNotaYfloat = Float.parseFloat(CordNotaY);
                    Float CordNotaAsteriscoXfloat = Float.parseFloat(CordNotaAsteriscoX);
                    Float CordNotaAsteriscoYfloat = Float.parseFloat(CordNotaAsteriscoY);

                    ImageButton imbBotonNota = new ImageButton(getApplicationContext());
                    imbBotonNota.setImageResource(R.mipmap.nota_hoja);
                    imbBotonNota.setX(600);
                    imbBotonNota.setY(CordNotaYfloat);
                    imbBotonNota.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                    layout.addView(imbBotonNota);
                    imbBotonNota.setId(idNota);

                    ImageView imgAsteriscoNota = new ImageView(getApplicationContext());
                    MapIMBNota.put(idNota,imbBotonNota);
                    imgAsteriscoNota.setImageResource(R.mipmap.asterisco_nota);
                    imgAsteriscoNota.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
                    imgAsteriscoNota.setX(CordNotaAsteriscoXfloat);
                    imgAsteriscoNota.setY(CordNotaAsteriscoYfloat);
                    MapIMGNota.put(idNota,imgAsteriscoNota);
                    layout.addView(imgAsteriscoNota);


                    imbBotonNota.setOnClickListener(imbNotaHoja_click);
                    imbBotonNota.setOnLongClickListener(imbEditaNota_click);

                }
                Log.d("Dalee",liscom.toString());


            }
        }
        List<Comentario> listson = new ArrayList<Comentario>();


        @Override
        protected List<Nota> doInBackground(String...params) {
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
                return new ArrayList<Nota>();
            }
        }
        public  List<Nota> listnot;

        List<Nota>parse(String json)throws JSONException

        {
            JSONArray jsonobj = new JSONArray(json);
            listnot = new ArrayList<Nota>();
            for(int i=0;i<jsonobj.length();i++)
            {

                JSONObject objComen = jsonobj.getJSONObject(i);
                Integer IdNota = objComen.getInt("IdNota");
                String Nombre = objComen.getString("Nombre");
                Double CordNotaY = objComen.getDouble("CordNotaY");
                Double CordNotaAsteriscoX = objComen.getDouble("CordNotaAsteriscoY");
                Double CordNotaAsteriscoY= objComen.getDouble("CordNotaAsteriscoY");
                Nota nota = new Nota(IdNota, Nombre, CordNotaY,CordNotaAsteriscoX,CordNotaAsteriscoY);
                listnot.add(nota);

            }
            return listnot;
        }


    }
    public View.OnLongClickListener imbEditaNota_click = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int id = v.getId();
            String Nombre ="";

            HttpGet get = new HttpGet();
            get.setHeader("content-type", "application/json");
            try {
                OkHttpClient client = new  OkHttpClient();
                String url ="http://leopashost.hol.es/bd/TraerNombreNota.php?IdNota="+id;
                JSONObject dato = new JSONObject();
                dato.put("IdNota",id);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                String resp = response.body().string();
                Nombre = getNombreNota(resp);
                Log.d("url", response.body().string());
            }
            catch (IOException|JSONException e) {
                Log.d("Error", e.getMessage());
            }
            Intent intentAPruebaNota = new Intent(getApplicationContext(),pruebaNotaVoz.class);
            intentAPruebaNota.putExtra("Nombre",Nombre);
            startActivity(intentAPruebaNota);
            return true;
        }
    };
    /*PARTE RESALTAR

    private void setupDrawing()
    {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG);


    }
   /* @Override
    protected void onSizeChanged(int w, int h, int oldv, int oldh)
    {
        super.onSizeChanged(w,h,oldv,oldh);
    }
    protected void OnDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
        canvas.drawPath(drawPath,drawPaint);
    }
    private View.OnTouchListener Resaltar = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            float touchx = event.getX();
            float touchy = event.getY();
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchx,touchy);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchx,touchy);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(touchx,touchy);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;

            }
            v.invalidate();
            return true;

        }


    };*/
}
