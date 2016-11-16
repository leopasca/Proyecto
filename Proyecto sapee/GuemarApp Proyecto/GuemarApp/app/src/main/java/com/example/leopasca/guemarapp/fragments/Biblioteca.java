package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.leopasca.guemarapp.R;

import java.util.ArrayList;

/**
 * Created by AMD on 02/10/2016.
 */
public class Biblioteca extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.biblioteca,container,false);
        return rootView;
    }
    ImageButton btnBeitza;
    public void ObtenerReferencias()
    {
       btnBeitza =(ImageButton)getActivity().findViewById(R.id.imbBeitza);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
        btnBeitza.setOnClickListener(editar_click);

    }
    public View.OnClickListener editar_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new paginas()).commit();
        }
    };
}
