package com.icobandas.icobandasapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.icobandas.icobandasapp.Adapters.AdapterRegistrosRecientes;
import com.icobandas.icobandasapp.Adapters.AdapterRegistrosRecientesOffline;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Entities.PlantasEntities;
import com.icobandas.icobandasapp.Entities.RegistrosRecientesEntities;
import com.icobandas.icobandasapp.Entities.TransportadorEntities;
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
public class FragmentRegistrosRecientes extends Fragment  {

    RecyclerView recyclerViewRecientes;
    ArrayList<LoginJson> loginJsonArrayListReferencia = new ArrayList<>();
    ArrayList<LoginJson> loginJsonArrayListFiltro = new ArrayList<>();

    TextView txtSinRegistros;
    ArrayList<String> listaClientes = new ArrayList<>();
    ArrayList<String> vacio = new ArrayList<>();
    ArrayList<String> lista = new ArrayList<>();
    View view;
    SearchableSpinner spinnerFiltroPlanta;
    SearchableSpinner spinnerFiltroTransportador;
    String cliente;
    AdapterRegistrosRecientes adapterRegistrosRecientes;
    Cursor cursor;
    DbHelper dbHelper;

    ArrayList<ClientesEntities> clientesEntitiesArrayList = new ArrayList<>();
    ArrayList<PlantasEntities> plantasEntitiesArrayList = new ArrayList<>();
    ArrayList<TransportadorEntities> transportadorEntitiesArrayList = new ArrayList<>();
    ArrayList<RegistrosRecientesEntities> registrosRecientesEntitiesArrayList = new ArrayList<>();

    AdapterRegistrosRecientesOffline adapterRegistrosRecientesOffline;

    public FragmentRegistrosRecientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registros_recientes, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Registro de Equipos");

        inicializar();

            llenarRecyclerOffline();
            llenarSinConexion();





        spinnerFiltroPlanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    ArrayList<String> arraySpinnerTransportadores = new ArrayList<>();
                    String idPlanta = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];
                    if (idPlanta.equals("Seleccione Planta")) {

                        llenarRecyclerOffline();
                    }
                    else
                    {
                        transportadorEntitiesArrayList.clear();
                        registrosRecientesEntitiesArrayList.clear();

                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registro.codplanta=" + idPlanta, null);

                        if(cursor.getCount()==0)
                        {
                            txtSinRegistros.setVisibility(View.VISIBLE);

                        }

                        else {

                            while (cursor.moveToNext()) {
                                RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                                registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                                registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                                registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                                registrosRecientesEntities.setCodplanta(cursor.getString(3));
                                registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                                registrosRecientesEntities.setNombreTransportador(cursor.getString(6));
                                registrosRecientesEntities.setNombrePlanta(cursor.getString(7));
                                registrosRecientesEntities.setNombreCliente(cursor.getString(8));
                                registrosRecientesEntities.setTipoTransportador(cursor.getString(9));

                                registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
                            }

                            txtSinRegistros.setVisibility(View.INVISIBLE);


                            /*adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
                            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
                            recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerViewRecientes.setHasFixedSize(true);*/
                        }

                        cursor = db.rawQuery("SELECT * from transportador where codplanta=" + idPlanta, null);
                        while (cursor.moveToNext()) {
                            TransportadorEntities transportadorEntities = new TransportadorEntities();
                            transportadorEntities.setIdTransportador(cursor.getString(0));
                            transportadorEntities.setTipoTransportador(cursor.getString(1));
                            transportadorEntities.setNombreTransportadro(cursor.getString(2));
                            transportadorEntities.setCaracteristicaTransportador(cursor.getString(4));
                            transportadorEntitiesArrayList.add(transportadorEntities);
                        }

                        arraySpinnerTransportadores.add(0, "Seleccione Transportador");
                        for (int i = 1; i <= transportadorEntitiesArrayList.size(); i++) {
                            arraySpinnerTransportadores.add(transportadorEntitiesArrayList.get(i - 1).getIdTransportador()+" - "+transportadorEntitiesArrayList.get(i-1).getNombreTransportadro()+ " - " + transportadorEntitiesArrayList.get(i - 1).getTipoTransportador() + " - " + transportadorEntitiesArrayList.get(i - 1).getCaracteristicaTransportador());
                        }

                        ArrayAdapter adapterTransportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arraySpinnerTransportadores);
                        spinnerFiltroTransportador.setAdapter(adapterTransportadores);


                    }


                }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFiltroTransportador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String idTransportador = spinnerFiltroTransportador.getSelectedItem().toString().split(" - ")[0];

                    loginJsonArrayListFiltro.clear();


                    if(!idTransportador.equals("") && !idTransportador.equals("Seleccione Transportador")) {
                        registrosRecientesEntitiesArrayList.clear();
                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registro.idTransportador=" + idTransportador, null);
                        cursor.moveToFirst();
                        if (cursor.getCount() == 0) {
                            txtSinRegistros.setVisibility(View.VISIBLE);
                            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
                            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);

                        }

                        else
                        {
                            RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                            registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                            registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                            registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                            registrosRecientesEntities.setCodplanta(cursor.getString(3));
                            registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                            registrosRecientesEntities.setNombreTransportador(cursor.getString(6));
                            registrosRecientesEntities.setNombrePlanta(cursor.getString(7));
                            registrosRecientesEntities.setNombreCliente(cursor.getString(8));
                            registrosRecientesEntities.setTipoTransportador(cursor.getString(9));

                            registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);

                            txtSinRegistros.setVisibility(View.INVISIBLE);

                            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
                            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
                            recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerViewRecientes.setHasFixedSize(true);
                            adapterRegistrosRecientesOffline.setMlistener(new AdapterRegistrosRecientesOffline.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void itemClick(int position, View itemView) {


                                    if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.E")) {
                                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        Fragment f = new FragmentPartesVertical();
                                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                                        f.setExitTransition(new Slide(Gravity.LEFT));

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                .commit();
                                    } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.DSF")) {
                                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        Fragment f = new FragmentPartesPesada();
                                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                                        f.setExitTransition(new Slide(Gravity.LEFT));

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                .commit();
                                    } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.T")) {
                                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                    }
                    else if(idTransportador.equals("Seleccione Transportador"))
                    {
                        ArrayList<String> arraySpinnerTransportadores = new ArrayList<>();

                        String idPlanta = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];
                        if (idPlanta.equals("Seleccione Planta")) {
                            llenarRecyclerOffline();
                        } else {

                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            transportadorEntitiesArrayList.clear();
                            registrosRecientesEntitiesArrayList.clear();

                            cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registro.codplanta=" + idPlanta, null);

                            if(cursor.getCount()==0)
                            {
                                txtSinRegistros.setVisibility(View.VISIBLE);
                                adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
                                recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
                            }

                            else
                            {
                                while (cursor.moveToNext())
                                {
                                    RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                                    registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                                    registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                                    registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                                    registrosRecientesEntities.setCodplanta(cursor.getString(3));
                                    registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                                    registrosRecientesEntities.setNombreTransportador(cursor.getString(6));
                                    registrosRecientesEntities.setNombrePlanta(cursor.getString(7));
                                    registrosRecientesEntities.setNombreCliente(cursor.getString(8));
                                    registrosRecientesEntities.setTipoTransportador(cursor.getString(9));
                                    registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
                                }
                                txtSinRegistros.setVisibility(View.INVISIBLE);

                                adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
                                recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
                                recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerViewRecientes.setHasFixedSize(true);


                            }


                        }

                        adapterRegistrosRecientesOffline.setMlistener(new AdapterRegistrosRecientesOffline.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void itemClick(int position, View itemView) {


                                if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.E")) {
                                    IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                    idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                    FragmentPartesVertical.idMaximaRegistro.clear();
                                    FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                    FragmentSeleccionarTransportador.bandera = "Actualizar";
                                    Fragment f = new FragmentPartesVertical();
                                    f.setEnterTransition(new Slide(Gravity.RIGHT));
                                    f.setExitTransition(new Slide(Gravity.LEFT));

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                            .commit();
                                } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.DSF")) {
                                    IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                    idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                    FragmentPartesVertical.idMaximaRegistro.clear();
                                    FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                    FragmentSeleccionarTransportador.bandera = "Actualizar";
                                    Fragment f = new FragmentPartesPesada();
                                    f.setEnterTransition(new Slide(Gravity.RIGHT));
                                    f.setExitTransition(new Slide(Gravity.LEFT));

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                            .commit();
                                } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.T")) {
                                    IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                    idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                                    FragmentPartesVertical.idMaximaRegistro.clear();
                                    FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                    FragmentSeleccionarTransportador.bandera = "Actualizar";
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

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void inicializar() {
        recyclerViewRecientes = view.findViewById(R.id.recyclerviewRecientes);
        txtSinRegistros = view.findViewById(R.id.txtSinRegistros);
        spinnerFiltroPlanta = view.findViewById(R.id.spinnerFiltroPlanta);
        spinnerFiltroTransportador = view.findViewById(R.id.spinnerFiltroTransportador);
        dbHelper = new DbHelper(getContext(), "prueba", null, 1);

    }


    public void llenarSinConexion() {
        clientesEntitiesArrayList.clear();
        plantasEntitiesArrayList.clear();
        transportadorEntitiesArrayList.clear();
        ArrayList<String> listaVacia = new ArrayList<>();
        listaVacia.add(0, "");
        ArrayAdapter adapterVacio = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaVacia);
        spinnerFiltroTransportador.setAdapter(adapterVacio);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("select plantas.*, clientes.* from plantas join clientes on (plantas.nitplanta=clientes.nit)", null);
        while (cursor.moveToNext()) {
            PlantasEntities plantasEntities = new PlantasEntities();

            ClientesEntities clientesEntities = new ClientesEntities();
            clientesEntities.setNit(cursor.getString(7));
            clientesEntities.setNameunido(cursor.getString(8));
            clientesEntitiesArrayList.add(clientesEntities);

            plantasEntities.setCodplanta(cursor.getString(0));
            plantasEntities.setNitplanta(cursor.getString(2));
            plantasEntities.setNameplanta(cursor.getString(3));
            plantasEntitiesArrayList.add(plantasEntities);

        }


        ArrayList<String> spinnerClientesLista = new ArrayList<>();
        for (int i = 1; i <= plantasEntitiesArrayList.size(); i++) {
            spinnerClientesLista.add(plantasEntitiesArrayList.get(i - 1).getCodplanta() + " - " + clientesEntitiesArrayList.get(i - 1).getNameunido() + " - " + plantasEntitiesArrayList.get(i - 1).getNameplanta());
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(spinnerClientesLista);
        spinnerClientesLista.clear();
        spinnerClientesLista.addAll(hs);
        Collections.sort(spinnerClientesLista, String.CASE_INSENSITIVE_ORDER);

        spinnerClientesLista.add(0, "Seleccione Planta");


        ArrayAdapter adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerClientesLista);
        spinnerFiltroPlanta.setAdapter(adapterClientes);


        db.close();

    }

    public void llenarRecyclerOffline() {

        ArrayList listaVacia = new ArrayList();
        listaVacia.add(0, "");
        ArrayAdapter adapterVacio = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaVacia);
        registrosRecientesEntitiesArrayList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        spinnerFiltroTransportador.setAdapter(adapterVacio);
        cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta)", null);

        while (cursor.moveToNext()) {
            RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
            registrosRecientesEntities.setIdRegistro(cursor.getString(0));
            registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
            registrosRecientesEntities.setIdTransportador(cursor.getString(2));
            registrosRecientesEntities.setCodplanta(cursor.getString(3));
            registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
            registrosRecientesEntities.setNombreTransportador(cursor.getString(6));
            registrosRecientesEntities.setNombrePlanta(cursor.getString(7));
            registrosRecientesEntities.setNombreCliente(cursor.getString(8));
            registrosRecientesEntities.setTipoTransportador(cursor.getString(9));
            registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
        }

        if (registrosRecientesEntitiesArrayList.size() == 0) {
            txtSinRegistros.setVisibility(View.VISIBLE);

        } else {
            txtSinRegistros.setVisibility(View.INVISIBLE);

            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList);
            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
            recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewRecientes.setHasFixedSize(true);
            adapterRegistrosRecientesOffline.setMlistener(new AdapterRegistrosRecientesOffline.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void itemClick(int position, View itemView) {


                    if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.E")) {
                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                        FragmentPartesVertical.idMaximaRegistro.clear();
                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                        Fragment f = new FragmentPartesVertical();
                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                        f.setExitTransition(new Slide(Gravity.LEFT));

                        getFragmentManager().beginTransaction()
                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                .commit();
                    } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.DSF")) {
                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                        FragmentPartesVertical.idMaximaRegistro.clear();
                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                        Fragment f = new FragmentPartesPesada();
                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                        f.setExitTransition(new Slide(Gravity.LEFT));

                        getFragmentManager().beginTransaction()
                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                .commit();
                    } else if (registrosRecientesEntitiesArrayList.get(position).getTipoTransportador().equals("B.T")) {
                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                        idMaximaRegistro.setMax(registrosRecientesEntitiesArrayList.get(position).getIdRegistro());
                        FragmentPartesVertical.idMaximaRegistro.clear();
                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                        FragmentSeleccionarTransportador.bandera = "Actualizar";
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

    }

}
