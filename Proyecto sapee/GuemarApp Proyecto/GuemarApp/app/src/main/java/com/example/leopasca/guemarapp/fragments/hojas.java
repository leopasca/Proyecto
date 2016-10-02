package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.leopasca.guemarapp.R;

import java.util.ArrayList;

/**
 * Created by AMD on 02/10/2016.
 */
public class hojas extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hojas,container,false);
        return rootView;
    }
    ListView listView;
    ArrayAdapter adapter;
    String[]array = {"Beitza .א","Beitza :א"};
    public void ObtenerReferencias()
    {
        listView =(ListView) getActivity().findViewById(R.id.lstview);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
        /*ArrayList<String> myStringArray1 = new ArrayList<String>();
        myStringArray1.add("Beitza .א");
        myStringArray1.add("Beitza :א");
        adapter = new ArrayAdapter(getActivity(), R.layout.hojas,R.id.lstview, myStringArray1);
        listView.setAdapter(adapter);*/
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new editar()).commit();
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String numero = (String)parent.getItemAtPosition(position);
                switch (numero)
                {
                    case "Beitza .א":
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.content_frame, new editar()).commit();
                        break;
                }

            }



        });*/

   }
}
