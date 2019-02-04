package com.icobandas.icobandasapp;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icobandas.icobandasapp.Adapters.AdapterRegistrosRecientes;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRegistrosRecientes extends Fragment {

    RecyclerView recyclerViewRecientes;
    ArrayList<LoginJson> loginJsonArrayListReferencia = new ArrayList<>();
    ArrayList<LoginJson> loginJsonArrayListFiltro = new ArrayList<>();

    TextView txtSinRegistros;
    ArrayList<String> listaClientes=new ArrayList<>();
    ArrayList<String> vacio=new ArrayList<>();
    ArrayList<String> lista=new ArrayList<>();
    View view;
    SearchableSpinner spinnerFiltroPlanta;
    SearchableSpinner spinnerFiltroTransportador;
    String cliente;
    AdapterRegistrosRecientes adapterRegistrosRecientes;






    public FragmentRegistrosRecientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_registros_recientes, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Registro de Equipos");

        inicializar();
        if(MainActivity.isOnline(getContext()))
        {
            if(Login.loginJsons.size()>0)
            {
                llenarTodo();
            }

        }


        spinnerFiltroPlanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(MainActivity.isOnline(getContext()))
                {
                    cliente = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];
                    if(cliente.equals("Seleccione Planta"))
                    {
                        adapterRegistrosRecientes = new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
                    }
                    else
                    {
                        loginJsonArrayListFiltro.clear();

                        for(int i=0;i<loginJsonArrayListReferencia.size();i++)
                        {
                            if(loginJsonArrayListReferencia.get(i).getCodplanta().equals(cliente))
                            {
                                loginJsonArrayListFiltro.add(loginJsonArrayListReferencia.get(i));
                            }
                        }
                        adapterRegistrosRecientes=new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
                    }
                    adapterRegistrosRecientes.setMlistener(new AdapterRegistrosRecientes.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void itemClick(final int position, final View itemView) {
                            if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.E"))
                            {
                                IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                                idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                                FragmentPartesVertical.idMaximaRegistro.clear();
                                FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                FragmentSeleccionarTransportador.bandera="Actualizar";
                                Fragment f = new FragmentPartesVertical();
                                f.setEnterTransition(new Slide(Gravity.RIGHT));
                                f.setExitTransition(new Slide(Gravity.LEFT));

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                        .commit();
                            }
                            else if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.DSF"))
                            {
                                IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                                idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                                FragmentPartesVertical.idMaximaRegistro.clear();
                                FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                FragmentSeleccionarTransportador.bandera="Actualizar";
                                Fragment f = new FragmentPartesPesada();
                                f.setEnterTransition(new Slide(Gravity.RIGHT));
                                f.setExitTransition(new Slide(Gravity.LEFT));

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                        .commit();
                            }
                            else if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.T"))
                            {
                                IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                                idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                                FragmentPartesVertical.idMaximaRegistro.clear();
                                FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                FragmentSeleccionarTransportador.bandera="Actualizar";
                                Fragment f = new FragmentPartesHorizontal();
                                f.setEnterTransition(new Slide(Gravity.RIGHT));
                                f.setExitTransition(new Slide(Gravity.LEFT));

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                        .commit();
                            }

                        }
                    });
                    llenarSpinnerTransportadores(cliente);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void inicializar() {
        recyclerViewRecientes=view.findViewById(R.id.recyclerviewRecientes);
        txtSinRegistros=view.findViewById(R.id.txtSinRegistros);
        spinnerFiltroPlanta=view.findViewById(R.id.spinnerFiltroPlanta);
        spinnerFiltroTransportador=view.findViewById(R.id.spinnerFiltroTransportador);
    }

    public void llenarSpinner() {

        listaClientes.clear();

        for (int i = 1; i <= Login.loginJsons.size(); i++) {
            listaClientes.add(Login.loginJsons.get(i - 1).getCodplanta() + " - " + Login.loginJsons.get(i - 1).getNameunido() + " - " + Login.loginJsons.get(i - 1).getNameplanta());

        }

        Set<String> hs = new HashSet<>();
        hs.addAll(listaClientes);
        listaClientes.clear();
        listaClientes.addAll(hs);
        Collections.sort(listaClientes,String.CASE_INSENSITIVE_ORDER);


        listaClientes.add(0, "Seleccione Planta");
        ArrayAdapter<String> adapterClientes = new ArrayAdapter(MainActivity.context, android.R.layout.simple_spinner_dropdown_item, listaClientes);

        spinnerFiltroPlanta.setAdapter(adapterClientes);
        spinnerFiltroPlanta.setTitle("Buscar Cliente");
        spinnerFiltroPlanta.setPositiveButton("Cerrar");

        vacio.add(0, "");
    }

    public void llenarSpinnerTransportadores(String idRegistro) {
        lista.clear();
        for (int i = 0; i < Login.loginTransportadores.size(); i++) {
            if (Login.loginTransportadores.get(i).getCodplanta().equals(idRegistro)) {
                lista.add(Login.loginTransportadores.get(i).getIdTransportador() + " - " + Login.loginTransportadores.get(i).getNombreTransportador() + " - " + Login.loginTransportadores.get(i).getTipoTransportador() + " - " + Login.loginTransportadores.get(i).getCaracteristicaTransportador());
            }
        }

        Set<String> hs1 = new HashSet<>();
        hs1.addAll(lista);
        lista.clear();
        lista.addAll(hs1);
        Collections.sort(lista, String.CASE_INSENSITIVE_ORDER);

        lista.add(0,"Seleccione Transportador");

        ArrayAdapter<String> adapterTansportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, lista);

        spinnerFiltroTransportador.setTitle("Buscar Transportador");
        spinnerFiltroTransportador.setPositiveButton("Cerrar");
        spinnerFiltroTransportador.setAdapter(adapterTansportadores);
    }

    public void llenarTodo()
    {
        loginJsonArrayListFiltro.clear();
        loginJsonArrayListReferencia.clear();
        txtSinRegistros.setVisibility(View.INVISIBLE);
        recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRecientes.setHasFixedSize(true);
        for(int i=0; i<Login.loginJsons.size();i++)
        {
            if(Login.loginJsons.get(i).getIdRegistro()!=null)
            {
                loginJsonArrayListReferencia.add(Login.loginJsons.get(i));
                loginJsonArrayListFiltro.add(Login.loginJsons.get(i));

            }
        }
        if(loginJsonArrayListReferencia.size()==0)
        {
            txtSinRegistros.setVisibility(View.VISIBLE);
        }
        else
        {
            adapterRegistrosRecientes=new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
            recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
            llenarSpinner();

        }


        spinnerFiltroTransportador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String idTransportador=spinnerFiltroTransportador.getSelectedItem().toString().split(" - ")[0];

                loginJsonArrayListFiltro.clear();

                if(!idTransportador.equals("Seleccione Transportador")|| idTransportador==null)
                {
                    loginJsonArrayListFiltro.clear();
                    for(int i=0;i<loginJsonArrayListReferencia.size();i++)
                    {
                        if(loginJsonArrayListReferencia.get(i).getIdTransportador().equals(idTransportador))
                        {
                            loginJsonArrayListFiltro.add(loginJsonArrayListReferencia.get(i));
                        }
                    }
                    adapterRegistrosRecientes=new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
                    recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
                }
                else
                {
                    if(!spinnerFiltroPlanta.getSelectedItem().toString().equals("Seleccione Planta") && spinnerFiltroTransportador.getSelectedItem().toString().equals("Seleccione Transportador"))
                    {

                        cliente = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];

                        for(int i=0;i<loginJsonArrayListReferencia.size();i++)
                        {
                            if(loginJsonArrayListReferencia.get(i).getCodplanta().equals(cliente))
                            {
                                loginJsonArrayListFiltro.add(loginJsonArrayListReferencia.get(i));
                            }
                        }
                        adapterRegistrosRecientes=new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
                    }
                    else
                    {
                        loginJsonArrayListFiltro.clear();

                        for(int i=0;i<loginJsonArrayListReferencia.size();i++)
                        {
                                loginJsonArrayListFiltro.add(loginJsonArrayListReferencia.get(i));
                        }
                        adapterRegistrosRecientes=new AdapterRegistrosRecientes(loginJsonArrayListFiltro);
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientes);
                    }


                }
                adapterRegistrosRecientes.setMlistener(new AdapterRegistrosRecientes.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void itemClick(final int position, final View itemView) {
                        if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.E"))
                        {
                            IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                            idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            FragmentSeleccionarTransportador.bandera="Actualizar";
                            Fragment f = new FragmentPartesVertical();
                            f.setEnterTransition(new Slide(Gravity.RIGHT));
                            f.setExitTransition(new Slide(Gravity.LEFT));

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                    .commit();
                        }
                        else if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.DSF"))
                        {
                            IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                            idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            FragmentSeleccionarTransportador.bandera="Actualizar";
                            Fragment f = new FragmentPartesPesada();
                            f.setEnterTransition(new Slide(Gravity.RIGHT));
                            f.setExitTransition(new Slide(Gravity.LEFT));

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                    .commit();
                        }
                        else if(loginJsonArrayListFiltro.get(position).getTipoTransportador().equals("B.T"))
                        {
                            IdMaximaRegistro idMaximaRegistro=new IdMaximaRegistro();
                            idMaximaRegistro.setMax(loginJsonArrayListFiltro.get(position).getIdRegistro());
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            FragmentSeleccionarTransportador.bandera="Actualizar";
                            Fragment f = new FragmentPartesHorizontal();
                            f.setEnterTransition(new Slide(Gravity.RIGHT));
                            f.setExitTransition(new Slide(Gravity.LEFT));

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                    .commit();
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
