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
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
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
        RelativeLayout layout;
        String strCordenadas = "";
        ImageButton imbComentarioHoja;
        Boolean ExisteImbBoton = false;
        ImageButton imbVideo;
        EditText edtComentario;
        String urlVideo ="";
        int i = 0;
        int j =0;
        boolean abierto = false;
        private boolean ignore = false;
        ImageButton imbBoton;
        private float x, y;
        ArrayList<EditText> ListButons = new ArrayList<>();
        ArrayList<ImageButton> ListImageButons = new ArrayList<>();
        ArrayList<ImageView> ListAsteriscos = new ArrayList<>();
         Map<Integer,EditText>MapEDT = new HashMap<Integer, EditText>();
        Map<Integer,ImageButton>MapIMB = new HashMap<Integer, ImageButton>();
        Map<Integer,ImageView>MapIMG = new HashMap<Integer, ImageView>();
        List<Comentario>liscom ;
    Button Pasar;







    public void ObtenerReferencias() {
        pdfView = (PDFView) findViewById(R.id.pdfview);
        imbComentario = (ImageButton) findViewById(R.id.imbComentario);
        imbResaltador = (ImageButton) findViewById(R.id.imbResaltador);
        layout = (RelativeLayout) findViewById(R.id.LayoutRelativo);
        imbComentarioHoja = (ImageButton) findViewById(R.id.imbComentarioHoja);
        Pasar = (Button)findViewById(R.id.btnPasar);
        imbVideo =(ImageButton)findViewById(R.id.imbVideo);


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
        pdfView.fromAsset("beitza.pdf").load();
        imbComentario.setOnClickListener(imbComentario_click);
        if (ExisteImbBoton == true) {
            imbComentarioHoja.setOnClickListener(imbComentarioHoja_click);
        }
        ProgressTask task = new ProgressTask();
        task.execute("http://leopashost.hol.es/bd/ListarComentarios.php");
        imbVideo.setOnClickListener(imbVideo_click);
        Pasar.setOnClickListener(pasar);


    }
    public View.OnClickListener pasar = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent elintent = new Intent(getApplicationContext(), videoPrueba.class);
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
                        Toast.makeText(EdicionActivity.this, "Ando", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(EdicionActivity.this, "se ha eliminado", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Video");
        builder.setMessage("Ingrese una url");
        final EditText input = new EditText(EdicionActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                urlVideo = input.getText().toString();
            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pdfView.resetZoomWithAnimation();

            }
        });
        return builder.create();
    }
    private View.OnTouchListener videoGuemara = new View.OnTouchListener() {


        int ex, way;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (ignore == true) {
                return false;
            } else {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Dialog dialogourl = dialogourl();
                        dialogourl.show();
                        x = event.getX();
                        y = event.getY();
                        String IdVideo="";
                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        ImageButton imbBotonVideo = new ImageButton(getApplicationContext());
                        imbBotonVideo.setImageResource(R.mipmap.video_hoja);
                        imbBotonVideo.setX(600);
                        imbBotonVideo.setY(y);
                        imbBotonVideo.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

                        //ListImageButons.add(i,imbBoton);
                        layout.addView(imbBotonVideo);
                        HttpPost post = new HttpPost();
                        post.setHeader("content-type", "application/json");

                        try {
                            OkHttpClient client = new  OkHttpClient();
                            String url ="http://leopashost.hol.es/bd/CrearVideo.php";
                            JSONObject dato = new JSONObject();
                            dato.put("url",urlVideo);
                            dato.put("CordVideoY",y);
                            dato.put("CordVideoAsteriscoX",x);
                            dato.put("CordVideoAsteriscoY",y);
                            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String prueba = response.body().string();
                            IdVideo =  getIdComentario(prueba);

                        }
                        catch (IOException|JSONException e) {
                            Log.d("Error", e.getMessage());
                        }
                        Toast.makeText(EdicionActivity.this, "Ando", Toast.LENGTH_SHORT).show();

                        j= Integer.parseInt(IdVideo);
                        imbBotonVideo.setId(j);
                        ImageView imgAsterisco = new ImageView(getApplicationContext());
                        MapIMB.put(i,imbBoton);
                        imgAsterisco.setImageResource(R.mipmap.asteriscoideo);
                        imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
                        imgAsterisco.setX(x);
                        imgAsterisco.setY(y);
                        MapIMG.put(i,imgAsterisco);
                        layout.addView(imgAsterisco);

                        imbBotonVideo.setOnClickListener(imbComentarioHoja_click);
                        imbBotonVideo.setOnLongClickListener(imbEliminar_click);

                        Log.e("error","8");

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
            Log.e("GULE", "1");
            dialogo.show();
            Log.e("Gule", "2");
            Log.e("GULE", "3");


        }

    };
    public Dialog dialogoCrearVideo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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

}
