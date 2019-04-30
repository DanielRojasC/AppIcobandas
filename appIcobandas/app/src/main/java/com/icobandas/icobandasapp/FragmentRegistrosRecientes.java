package com.icobandas.icobandasapp;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.icobandas.icobandasapp.Adapters.AdapterRegistrosRecientes;
import com.icobandas.icobandasapp.Adapters.AdapterRegistrosRecientesOffline;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.AgentesEntities;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Entities.PlantasEntities;
import com.icobandas.icobandasapp.Entities.RegistrosRecientesEntities;
import com.icobandas.icobandasapp.Entities.TransportadorEntities;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRegistrosRecientes extends Fragment {

    RecyclerView recyclerViewRecientes;
    ArrayList<LoginJson> loginJsonArrayListFiltro = new ArrayList<>();
    TextView txtSinRegistros;
    View view;
    SearchableSpinner spinnerFiltroPlanta;
    SearchableSpinner spinnerFiltroTransportador;
    Cursor cursor;
    static DbHelper dbHelper;
    static RequestQueue queue;
    ArrayList<ClientesEntities> clientesEntitiesArrayList = new ArrayList<>();
    ArrayList<PlantasEntities> plantasEntitiesArrayList = new ArrayList<>();
    ArrayList<TransportadorEntities> transportadorEntitiesArrayList = new ArrayList<>();
    ArrayList<RegistrosRecientesEntities> registrosRecientesEntitiesArrayList = new ArrayList<>();
    AdapterRegistrosRecientesOffline adapterRegistrosRecientesOffline;
    FloatingActionButton botonRegistrosDesactivados;
    Dialog dialogRegistrosDesactivados;
    TextView txtFiltroPorAgente;
    SearchableSpinner spinnerAgentes;

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
        txtFiltroPorAgente=view.findViewById(R.id.txtSinRegistros2);
        spinnerAgentes=view.findViewById(R.id.spinnerFiltroAgente);
        txtFiltroPorAgente.setVisibility(View.INVISIBLE);
        spinnerAgentes.setVisibility(View.INVISIBLE);
        inicializar();

        if(Login.rol.equals("admin"))
        {
            txtFiltroPorAgente.setVisibility(View.VISIBLE);
            spinnerAgentes.setVisibility(View.VISIBLE);
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout layout;
            layout = view.findViewById(R.id.registrosRecientes);
            set.clone(layout);
            // The following breaks the connection.
            set.clear(R.id.recyclerviewRecientes, ConstraintSet.TOP);
            // Comment out line above and uncomment line below to make the connection.
            set.connect(R.id.recyclerviewRecientes, ConstraintSet.TOP, R.id.spinnerFiltroAgente, ConstraintSet.BOTTOM, 8);
            set.applyTo(layout);
            llenarSpinnerAgentes();
        }


        llenarRecyclerOffline();
        llenarSinConexion();

        botonRegistrosDesactivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogRegistrosDesactivados();
            }
        });

        spinnerFiltroPlanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                ArrayList<String> arraySpinnerTransportadores = new ArrayList<>();
                String idPlanta = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];
                if (idPlanta.equals("Seleccione Planta")) {

                    llenarRecyclerOffline();
                } else {
                    transportadorEntitiesArrayList.clear();
                    registrosRecientesEntitiesArrayList.clear();

                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta)  where registroActivado!='0' and registro.codplanta=" + idPlanta, null);

                    if (cursor.getCount() == 0) {
                        txtSinRegistros.setVisibility(View.VISIBLE);

                    } else {

                        while (cursor.moveToNext()) {
                            RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                            registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                            registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                            registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                            registrosRecientesEntities.setCodplanta(cursor.getString(3));
                            registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                            registrosRecientesEntities.setNombreTransportador(cursor.getString(7));
                            registrosRecientesEntities.setNombrePlanta(cursor.getString(8));
                            registrosRecientesEntities.setNombreCliente(cursor.getString(9));
                            registrosRecientesEntities.setTipoTransportador(cursor.getString(10));
                            registrosRecientesEntities.setRegistroActivado(cursor.getString(6));

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
                        arraySpinnerTransportadores.add(transportadorEntitiesArrayList.get(i - 1).getIdTransportador() + " - " + transportadorEntitiesArrayList.get(i - 1).getNombreTransportadro() + " - " + transportadorEntitiesArrayList.get(i - 1).getTipoTransportador() + " - " + transportadorEntitiesArrayList.get(i - 1).getCaracteristicaTransportador());
                    }

                    ArrayAdapter adapterTransportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arraySpinnerTransportadores);
                    spinnerFiltroTransportador.setAdapter(adapterTransportadores);


                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFiltroTransportador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String idTransportador = spinnerFiltroTransportador.getSelectedItem().toString().split(" - ")[0];

                loginJsonArrayListFiltro.clear();


                if (!idTransportador.equals("") && !idTransportador.equals("Seleccione Transportador")) {
                    registrosRecientesEntitiesArrayList.clear();
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registroActivado!='0' and registro.idTransportador=" + idTransportador, null);
                    cursor.moveToFirst();
                    if (cursor.getCount() == 0) {
                        txtSinRegistros.setVisibility(View.VISIBLE);
                        adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);

                    } else {
                        RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                        registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                        registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                        registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                        registrosRecientesEntities.setCodplanta(cursor.getString(3));
                        registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                        registrosRecientesEntities.setNombreTransportador(cursor.getString(7));
                        registrosRecientesEntities.setNombrePlanta(cursor.getString(8));
                        registrosRecientesEntities.setNombreCliente(cursor.getString(9));
                        registrosRecientesEntities.setTipoTransportador(cursor.getString(10));
                        registrosRecientesEntities.setRegistroActivado(cursor.getString(6));

                        registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);

                        txtSinRegistros.setVisibility(View.INVISIBLE);

                        adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
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
                } else if (idTransportador.equals("Seleccione Transportador")) {
                    ArrayList<String> arraySpinnerTransportadores = new ArrayList<>();

                    String idPlanta = spinnerFiltroPlanta.getSelectedItem().toString().split(" - ")[0];
                    if (idPlanta.equals("Seleccione Planta")) {
                        llenarRecyclerOffline();
                    } else {

                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        transportadorEntitiesArrayList.clear();
                        registrosRecientesEntitiesArrayList.clear();

                        cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registroActivado!='0' and registro.codplanta=" + idPlanta, null);

                        if (cursor.getCount() == 0) {
                            txtSinRegistros.setVisibility(View.VISIBLE);
                            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
                            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
                        } else {
                            while (cursor.moveToNext()) {
                                RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                                registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                                registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                                registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                                registrosRecientesEntities.setCodplanta(cursor.getString(3));
                                registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                                registrosRecientesEntities.setNombreTransportador(cursor.getString(7));
                                registrosRecientesEntities.setNombrePlanta(cursor.getString(8));
                                registrosRecientesEntities.setNombreCliente(cursor.getString(9));
                                registrosRecientesEntities.setTipoTransportador(cursor.getString(10));
                                registrosRecientesEntities.setRegistroActivado(cursor.getString(6));
                                registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
                            }
                            txtSinRegistros.setVisibility(View.INVISIBLE);

                            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
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

        spinnerAgentes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String codigoAgente=spinnerAgentes.getSelectedItem().toString().split(" - ")[0];
                loginJsonArrayListFiltro.clear();


                if (!codigoAgente.equals("") && !codigoAgente.equals("Seleccione Agente")) {

                    registrosRecientesEntitiesArrayList.clear();
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registroActivado!='0' and registro.usuarioRegistro='" + codigoAgente+"'", null);

                    if (cursor.getCount() == 0) {
                        txtSinRegistros.setVisibility(View.VISIBLE);
                        adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
                        recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);

                    } else {
                        while (cursor.moveToNext())
                        {
                            RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
                            registrosRecientesEntities.setIdRegistro(cursor.getString(0));
                            registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
                            registrosRecientesEntities.setIdTransportador(cursor.getString(2));
                            registrosRecientesEntities.setCodplanta(cursor.getString(3));
                            registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
                            registrosRecientesEntities.setNombreTransportador(cursor.getString(7));
                            registrosRecientesEntities.setNombrePlanta(cursor.getString(8));
                            registrosRecientesEntities.setNombreCliente(cursor.getString(9));
                            registrosRecientesEntities.setTipoTransportador(cursor.getString(10));
                            registrosRecientesEntities.setRegistroActivado(cursor.getString(6));

                            registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
                        }


                        txtSinRegistros.setVisibility(View.INVISIBLE);

                        adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
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
                else {

                    spinnerFiltroPlanta.setSelection(0);
                    llenarRecyclerOffline();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void inicializar() {
        recyclerViewRecientes = view.findViewById(R.id.recyclerviewRecientes);
        txtSinRegistros = view.findViewById(R.id.txtSinRegistros);
        spinnerFiltroPlanta = view.findViewById(R.id.spinnerFiltroPlanta);
        spinnerFiltroTransportador = view.findViewById(R.id.spinnerFiltroTransportador);
        dbHelper = new DbHelper(getContext(), "prueba", null, 1);
        queue = Volley.newRequestQueue(getContext());
        botonRegistrosDesactivados = view.findViewById(R.id.botonRegistrosDesactivados);

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
        ArrayAdapter adapterVacio = new ArrayAdapter(MainActivity.context, android.R.layout.simple_spinner_dropdown_item, listaVacia);
        registrosRecientesEntitiesArrayList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        spinnerFiltroTransportador.setAdapter(adapterVacio);
        cursor = db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registroActivado!='0'", null);

        while (cursor.moveToNext()) {
            RegistrosRecientesEntities registrosRecientesEntities = new RegistrosRecientesEntities();
            registrosRecientesEntities.setIdRegistro(cursor.getString(0));
            registrosRecientesEntities.setFechaRegistro(cursor.getString(1));
            registrosRecientesEntities.setIdTransportador(cursor.getString(2));
            registrosRecientesEntities.setCodplanta(cursor.getString(3));
            registrosRecientesEntities.setUsuarioRegistro(cursor.getString(4));
            registrosRecientesEntities.setNombreTransportador(cursor.getString(7));
            registrosRecientesEntities.setNombrePlanta(cursor.getString(8));
            registrosRecientesEntities.setNombreCliente(cursor.getString(9));
            registrosRecientesEntities.setTipoTransportador(cursor.getString(10));
            registrosRecientesEntities.setRegistroActivado(cursor.getString(6));
            registrosRecientesEntitiesArrayList.add(registrosRecientesEntities);
        }

        if (registrosRecientesEntitiesArrayList.size() == 0) {
            txtSinRegistros.setVisibility(View.VISIBLE);

        } else {
            txtSinRegistros.setVisibility(View.INVISIBLE);

            adapterRegistrosRecientesOffline = new AdapterRegistrosRecientesOffline(registrosRecientesEntitiesArrayList, getContext());
            recyclerViewRecientes.setAdapter(adapterRegistrosRecientesOffline);
            recyclerViewRecientes.setLayoutManager(new LinearLayoutManager(MainActivity.context, LinearLayoutManager.VERTICAL, false));
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

    public static void actualizarActivado(final String idRegistro, final String estadoRegistro) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update registro set registroActivado='" + estadoRegistro + "' where idRegistro='" + idRegistro + "'");
        if (MainActivity.isOnline(MainActivity.context)) {
            String url = Constants.url + "actualizarEstadoRegistro/" + idRegistro + "&" + estadoRegistro;
            StringRequest request = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(estadoRegistro.equals("0"))
                    {
                        MDToast.makeText(MainActivity.context, "Registro Desactivado", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();

                    }
                    else
                    {
                        MDToast.makeText(MainActivity.context, "Registro Activado", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();

                    }
                    Fragment fragmentRegistrosRecientes = new FragmentRegistrosRecientes();
                    MainActivity.fragmentManager.beginTransaction().detach(fragmentRegistrosRecientes).replace(R.id.contenedor, new FragmentRegistrosRecientes(), null).commit();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", idRegistro);
                    params.put("estadoRegistro", estadoRegistro);

                    return params;
                }
            };
            queue.add(request);
        } else {
            if(estadoRegistro.equals("0"))
            {
                MDToast.makeText(MainActivity.context, "Registro Desactivado", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();

            }
            else
            {
                MDToast.makeText(MainActivity.context, "Registro Activado", Toast.LENGTH_SHORT, MDToast.TYPE_INFO).show();

            }

            Fragment fragmentRegistrosRecientes = new FragmentRegistrosRecientes();

            MainActivity.fragmentManager.beginTransaction().detach(fragmentRegistrosRecientes).replace(R.id.contenedor, new FragmentRegistrosRecientes(), null).commit();


        }

    }

    public void abrirDialogRegistrosDesactivados()
    {
        ArrayList<RegistrosRecientesEntities> registrosRecientesEntities= new ArrayList<>();
        dialogRegistrosDesactivados= new Dialog(getContext());
        dialogRegistrosDesactivados.setContentView(R.layout.dialog_reactivar_registros);
        dialogRegistrosDesactivados.setCancelable(true);
        TextView txtRegistrosDesactivados=dialogRegistrosDesactivados.findViewById(R.id.txtRegistrosDesactivados);
        RecyclerView recyclerRegistrosDesactivados= dialogRegistrosDesactivados.findViewById(R.id.recyclerReactivarRegistros);
        dialogRegistrosDesactivados.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor=db.rawQuery("select distinct registro.*,transportador.nombreTransportador, plantas.nameplanta,clientes.nameunido, transportador.tipoTransportador from registro join transportador on(registro.idTransportador=transportador.idTransportador) join plantas on (plantas.codplanta=registro.codplanta)join clientes on (clientes.nit=plantas.nitplanta) where registroActivado='0'", null);
        if(cursor.getCount()==0)
        {
            txtRegistrosDesactivados.setVisibility(View.VISIBLE);
        }
        else
        {
            txtRegistrosDesactivados.setVisibility(View.INVISIBLE);

            while (cursor.moveToNext())
            {
                RegistrosRecientesEntities recientesEntities= new RegistrosRecientesEntities();
                recientesEntities.setIdRegistro(cursor.getString(0));
                recientesEntities.setFechaRegistro(cursor.getString(1));
                recientesEntities.setIdTransportador(cursor.getString(2));
                recientesEntities.setCodplanta(cursor.getString(3));
                recientesEntities.setUsuarioRegistro(cursor.getString(4));
                recientesEntities.setNombreTransportador(cursor.getString(7));
                recientesEntities.setNombrePlanta(cursor.getString(8));
                recientesEntities.setNombreCliente(cursor.getString(9));
                recientesEntities.setTipoTransportador(cursor.getString(10));
                recientesEntities.setRegistroActivado(cursor.getString(6));
                registrosRecientesEntities.add(recientesEntities);
            }
        }


        AdapterRegistrosRecientesOffline adapterRegistrosRecientesOffline= new AdapterRegistrosRecientesOffline(registrosRecientesEntities, getContext());
        recyclerRegistrosDesactivados.setAdapter(adapterRegistrosRecientesOffline);
        recyclerRegistrosDesactivados.setLayoutManager(new LinearLayoutManager(MainActivity.context, LinearLayoutManager.VERTICAL, false));
        recyclerRegistrosDesactivados.setHasFixedSize(true);

        dialogRegistrosDesactivados.show();
    }

    public void llenarSpinnerAgentes()
    {
        ArrayList<AgentesEntities> agentesEntitiesArrayList = new ArrayList<>();
        ArrayList<String> agentesArrayList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor= db.rawQuery("Select distinct * from usuario where nombreUsuario!='LEPE' and nombreUsuario!='LECA'", null);
        while (cursor.moveToNext())
        {
            AgentesEntities agentesEntities = new AgentesEntities();
            agentesEntities.setCodAgente(cursor.getString(0));
            agentesEntities.setNombreAgente(cursor.getString(1));
            agentesEntitiesArrayList.add(agentesEntities);
        }
        agentesArrayList.add(0, "Seleccione Agente");
        for (int i=1;i<agentesEntitiesArrayList.size();i++)
        {
            //if(!agentesEntitiesArrayList.get(i-1).getCodAgente().equals("LEPE") || !agentesEntitiesArrayList.get(i-1).getCodAgente().equals("LECA"))
            //{
                agentesArrayList.add(agentesEntitiesArrayList.get(i-1).getCodAgente()+" - "+agentesEntitiesArrayList.get(i-1).getNombreAgente());

            //}
        }

        ArrayAdapter<String> adapterAgentes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, agentesArrayList);
        spinnerAgentes.setAdapter(adapterAgentes);

    }

}
