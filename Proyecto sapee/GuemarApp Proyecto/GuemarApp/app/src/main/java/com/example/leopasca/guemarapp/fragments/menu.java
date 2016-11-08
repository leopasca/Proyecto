package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leopasca.guemarapp.R;

import java.util.ArrayList;

/**
 * Created by AMD on 30/09/2016.
 */
public class menu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menufrag,container,false);
        return rootView;
    }
    TextView txvBienvenido;
    Button btn;
    RelativeLayout layout;

    public void ObtenerReferencias()
    {
        txvBienvenido = (TextView)getActivity().findViewById(R.id.txvBienvenido);
        btn = (Button)getActivity().findViewById(R.id.button2);
        layout = (RelativeLayout)getActivity().findViewById(R.id.layout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String Nombre = prefs.getString("Nombre","");
        txvBienvenido.setText("Bienvenido/a a GuemarApp " + Nombre);
        btn.setOnClickListener(btnasd);


    }
    public View.OnClickListener btnasd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout layout2 = new LinearLayout(getActivity());
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layout2.setOrientation(LinearLayout.VERTICAL);
            layout2.setLayoutParams(parms);
            layout2.setBackgroundColor(Color.parseColor("#FFFF66"));
            TextView tv = new TextView(getActivity());
            tv.setText("Hola");
            EditText edtComentario = new EditText(getActivity().getApplicationContext());
            Log.e("error", "3");
            edtComentario.setBackgroundColor(Color.parseColor("#A6A6A6"));
            edtComentario.setHint("Comentario");
            Log.e("error","4");
            edtComentario.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
            layout2.addView(tv);
            layout2.addView(edtComentario);
            layout.addView(layout2);


        }
    };


}
