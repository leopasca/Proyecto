package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void ObtenerReferencias()
    {
        txvBienvenido = (TextView)getActivity().findViewById(R.id.txvBienvenido);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String Nombre = prefs.getString("Nombre","");
        txvBienvenido.setText("Bienvenido a GuemarApp " + Nombre);


    }
}
