package com.example.pascalo.tester;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {
    ImageView imvImagen;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    TouchImageView view;
    float x,y;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;
    RelativeLayout layout;
    Button btnAcept;


    public void ObtenerReferencias()
    {
        layout = (RelativeLayout)findViewById(R.id.layout);
        btnAcept = (Button)findViewById(R.id.btnAceptar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObtenerReferencias();
        setContentView(R.layout.activity_main);
        view = (TouchImageView)findViewById(R.id.view) ;
        view.setImageResource(R.drawable.naturaleza);
        view.setMaxZoom(4f);
        btnAcept.setOnClickListener(acept);

    }
    public View.OnClickListener acept = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view.setOnTouchListener(tocar);
        }
    };
    public View.OnTouchListener tocar = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = event.getX();
            y = event.getY();
            ImageView imgAsterisco = new ImageView(getApplicationContext());
            imgAsterisco.setImageResource(R.mipmap.asterisco);
            imgAsterisco.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            imgAsterisco.setX(x);
            imgAsterisco.setY(y);
            layout.addView(imgAsterisco);
            return false;
        }
    };


}
