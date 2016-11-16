package com.example.leopasca.guemarapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
 * Created by AMD on 15/11/2016.
 */
public class paginas extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.paginas,container,false);
        return rootView;
    }
    ListView listview;
    ArrayList<String> paginas = new ArrayList<String>();
    ArrayAdapter<String>adapter;
    public void ObtenerReferencias()
    {
        listview = (ListView)getActivity().findViewById(R.id.lstPaginas);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ObtenerReferencias();
       paginas.add("Beitza .ב");
        paginas.add("Beitza :ב");
        adapter=
                new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,paginas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(item);

    }
    public AdapterView.OnItemClickListener item = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String pagina= (listview.getItemAtPosition(position).toString());
            Integer pagina1 = 1;

            if(pagina=="Beitza :ב" )
            {
                pagina1 = 2;
            }
            FragmentManager fm = getFragmentManager();
            editar fragment = new editar();
            Bundle arguments = new Bundle();
            arguments.putInt("pagina", pagina1);
            fragment.setArguments(arguments);
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    };

}
