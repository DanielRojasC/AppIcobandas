package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Entities.PlantasEntities;
import com.icobandas.icobandasapp.Entities.TransportadorEntities;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSeleccionarTransportador extends Fragment {

    static SearchableSpinner spinnerClientes;
    static SearchableSpinner spinnerTransportadores;
    ArrayList<String> lista = new ArrayList<>();
    public static ArrayList<String> listaClientes = new ArrayList<>();
    ArrayList<ClientesEntities> clientesEntitiesArrayList = new ArrayList<>();
    ArrayList<PlantasEntities> plantasEntitiesArrayList = new ArrayList<>();
    ArrayList<TransportadorEntities> transportadorEntitiesArrayList = new ArrayList<>();
    static ArrayList<String> vacio = new ArrayList<>();
    AppCompatImageButton btnEditarTransportador;

    public static String seleccion;
    View view;

    AlertDialog.Builder alerta;
    String cliente;
    Button btnIr;
    Button btnAgregarTransportador;
    static Dialog dialogAgregarTransportador;
    RequestQueue queue;
    Gson gson = new Gson();
    DbHelper dbHelper;
    ArrayList<String> transportadores;
    Cursor cursor;
    ArrayAdapter<String> adapterTransportadores;

    public static ArrayAdapter<String> adapterClientes;

    public static String bandera;

    public static String idTransportadorRegistro;
    public static String idPlantaRegistro;

    public FragmentSeleccionarTransportador() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seleccionar_transportador, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializar();

        llenarSpinnerOffline();


        getActivity().setTitle("Crear Registro");

        spinnerTransportadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                seleccion = lista.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEditarTransportador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerTransportadores.getSelectedItem().toString().equals("Seleccione Transportador"))
                {
                    TextView errorText = (TextView) spinnerTransportadores.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Debe Escoger un transportador");//
                }
                else
                {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    final String idTransportador=spinnerTransportadores.getSelectedItem().toString().split(" - ")[0];
                    final Cursor cursor1;
                    cursor1=db.rawQuery("Select * from transportador where idTransportador='"+idTransportador+"'", null);

                    Log.e("SENTENCIA", "Select * from transportador where idTransportador='"+idTransportador+"'");
                    final Dialog dialogEditarTransportador=new Dialog(getContext());
                    dialogEditarTransportador.setContentView(R.layout.dialog_crear_transportador);
                    dialogEditarTransportador.setCancelable(true);

                    final TextInputEditText txtNombreTransportador = dialogEditarTransportador.findViewById(R.id.txtNombreTransportador);
                    final TextInputEditText txtDescripcionTransportador = dialogEditarTransportador.findViewById(R.id.txtDescripcionTransportador);

                    final SearchableSpinner spinnerPlanta = dialogEditarTransportador.findViewById(R.id.spinnerPlanta);
                    final Spinner spinnerTipoTransportador = dialogEditarTransportador.findViewById(R.id.spinnerTipoTransportador);

                    final ProgressBar progressBarTranspor = dialogEditarTransportador.findViewById(R.id.progressBarTranspor);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                    llenarSpinnerDialog(spinnerPlanta, spinnerTipoTransportador);

                    spinnerPlanta.setEnabled(false);
                    spinnerPlanta.setTitle("Buscar Planta");
                    spinnerPlanta.setPositiveButton("Cerrar");
                    String tipoTransportador;
                    cursor1.moveToFirst();
                    if(cursor1.getString(cursor1.getColumnIndex("tipoTransportador")).equals("B.T"))
                    {
                        tipoTransportador="Transportador horizontal o inclinado";
                    }
                    else if(cursor1.getString(cursor1.getColumnIndex("tipoTransportador")).equals("B.E"))
                    {
                        tipoTransportador="Transportador vertical";
                    }
                    else
                    {
                        tipoTransportador="Transportador de transmisión pesada";
                    }
                    spinnerTipoTransportador.setSelection(adapterTransportadores.getPosition(tipoTransportador));
                    spinnerTipoTransportador.setEnabled(false);
                    String plantaTransportador = null;

                    txtNombreTransportador.setText(cursor1.getString(cursor1.getColumnIndex("nombreTransportador")));
                    for(int i=0;i<listaClientes.size();i++)
                    {
                        if(listaClientes.get(i).split(" - ")[0].equals(cursor1.getString(3)))
                        {
                            plantaTransportador=listaClientes.get(i);
                            i=listaClientes.size()+1;
                        }
                    }
                   spinnerPlanta.setSelection(adapterClientes.getPosition(plantaTransportador));
                    txtDescripcionTransportador.setText(cursor1.getString(4));


                    Button btnCrearTransportador = dialogEditarTransportador.findViewById(R.id.btnAgregarTransportador);
                    btnCrearTransportador.setText("Editar Transportador");

                    btnCrearTransportador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            String estadoTransportador;
                            if(cursor1.getString(5).equals("Sincronizado"))
                            {
                                estadoTransportador="Actualizar con DB";
                            }
                            else
                            {
                                estadoTransportador="Pendiente INSERTAR BD";
                            }
                            db.execSQL("update transportador set nombreTransportador='"+txtNombreTransportador.getText().toString()+"', caracteristicaTransportador='"+txtDescripcionTransportador.getText().toString()+"', estadoRegistroTransportador='"+estadoTransportador+"' where idTransportador='"+idTransportador+"'");
                            dialogEditarTransportador.cancel();
                            MDToast.makeText(getContext(),"Transportador editado correctamente", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            llenarSpinnerOffline();

                        }
                    });



                    dialogEditarTransportador.show();
                }
            }
        });


        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cliente = spinnerClientes.getSelectedItem().toString().split(" - ")[0];
                llenarSpinnerTransportadores(cliente);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnIr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                bandera = null;
                FragmentPartesVertical.idMaximaRegistro.clear();
                if (cliente.equals("") || cliente.equals("Seleccione Planta")) {
                    TextView errorText = (TextView) spinnerClientes.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Debe Escoger un Cliente");//
                } else {

                    if (spinnerTransportadores.getSelectedItem() == null) {
                        alerta.setTitle("ICOBANDAS S.A dice:");
                        alerta.setMessage("La planta seleccionada no tiene transportadores registrados");
                        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alerta.create();
                        alerta.show();
                    } else {
                        seleccion = spinnerTransportadores.getSelectedItem().toString();

                        if (seleccion.equals("") || seleccion.equals("Seleccione Transportador")) {
                            TextView errorText = (TextView) spinnerTransportadores.getSelectedView();
                            errorText.setError("anything here, just to add the icon");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Debe Escoger un transportador");//
                        } else if (!cliente.equals("") && !cliente.equals("Seleccione Planta") && !seleccion.equals("") && !seleccion.equals("Seleccione Transportador")) {
                            seleccion = spinnerTransportadores.getSelectedItem().toString().split(" - ")[2];

                            idTransportadorRegistro = spinnerTransportadores.getSelectedItem().toString().split(" - ")[0];
                            idPlantaRegistro = spinnerClientes.getSelectedItem().toString().split(" - ")[0];

                            if (seleccion.equals("B.T")) {

                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                cursor = db.rawQuery("SELECT * from registro where idTransportador=" + idTransportadorRegistro, null);
                                cursor.moveToFirst();

                                if (cursor.getCount() == 0) {
                                    bandera = "Nuevo";

                                } else {
                                    bandera = "Actualizar";
                                    FragmentPartesVertical.idMaximaRegistro.clear();
                                    IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                    idMaximaRegistro.setMax(cursor.getString(0));
                                    FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);

                                }

                                Fragment f = new FragmentPartesHorizontal();
                                f.setEnterTransition(new Slide(Gravity.RIGHT));
                                f.setExitTransition(new Slide(Gravity.LEFT));

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                        .commit();


                            } else if (seleccion.equals("B.E")) {

                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    cursor = db.rawQuery("SELECT * from registro where idTransportador=" + idTransportadorRegistro, null);
                                    if (cursor.getCount() == 0) {
                                        bandera = "Nuevo";

                                    } else {
                                        cursor.moveToFirst();
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                        idMaximaRegistro.setMax(cursor.getString(0));
                                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                        bandera = "Actualizar";
                                    }


                                Fragment f = new FragmentPartesVertical();
                                f.setEnterTransition(new Slide(Gravity.RIGHT));
                                f.setExitTransition(new Slide(Gravity.LEFT));

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                        .commit();


                            } else {

                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    cursor = db.rawQuery("SELECT * from registro where idTransportador=" + idTransportadorRegistro, null);
                                    if (cursor.getCount() == 0) {
                                        bandera = "Nuevo";

                                    } else {
                                        cursor.moveToFirst();
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                        idMaximaRegistro.setMax(cursor.getString(0));
                                        FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                        bandera = "Actualizar";
                                    }
                                    Fragment f = new FragmentPartesPesada();
                                    f.setEnterTransition(new Slide(Gravity.RIGHT));
                                    f.setExitTransition(new Slide(Gravity.LEFT));

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                            .commit();




                            }
                        }
                    }
                }
            }
        });

        btnAgregarTransportador.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                dialogAgregarTransportador();
            }
        });


        return view;
    }

    private void inicializar() {
        spinnerClientes = view.findViewById(R.id.spinnerClientes);
        spinnerTransportadores = view.findViewById(R.id.spinnerTransportador);
        alerta = new AlertDialog.Builder(getContext());
        btnIr = view.findViewById(R.id.btnIr);
        btnAgregarTransportador = view.findViewById(R.id.btnCrearTransportador);
        queue = Volley.newRequestQueue(getContext());
        dbHelper = new DbHelper(getContext(), "prueba", null, 1);
        btnEditarTransportador=view.findViewById(R.id.btnEliminarTransportador);
        btnEditarTransportador.setVisibility(View.INVISIBLE);

    }

    public void llenarSpinnerOffline() {
        clientesEntitiesArrayList.clear();
        plantasEntitiesArrayList.clear();
        transportadorEntitiesArrayList.clear();
        listaClientes.clear();
        ArrayList<String> listaVacia = new ArrayList<>();
        listaVacia.add(0, "");
        ArrayAdapter adapterVacio = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaVacia);
        spinnerTransportadores.setAdapter(adapterVacio);

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


        for (int i = 1; i <= plantasEntitiesArrayList.size(); i++) {
            listaClientes.add(plantasEntitiesArrayList.get(i - 1).getCodplanta() + " - " + clientesEntitiesArrayList.get(i - 1).getNameunido() + " - " + plantasEntitiesArrayList.get(i - 1).getNameplanta());
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(listaClientes);
        listaClientes.clear();
        listaClientes.addAll(hs);
        Collections.sort(listaClientes, String.CASE_INSENSITIVE_ORDER);

        listaClientes.add(0, "Seleccione Planta");


        adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaClientes);
        spinnerClientes.setAdapter(adapterClientes);
    }

    public void llenarSpinnerTransportadores(String idRegistro) {

            btnEditarTransportador.setVisibility(View.INVISIBLE);
            lista.clear();

            if (!cliente.equals("Seleccione Planta")) {
                lista.clear();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                transportadorEntitiesArrayList.clear();
                cursor = db.rawQuery("SELECT * FROM transportador where codplanta=" + cliente, null);
                while (cursor.moveToNext()) {
                    TransportadorEntities transportadorEntities = new TransportadorEntities();
                    transportadorEntities.setIdTransportador(cursor.getString(0));
                    transportadorEntities.setTipoTransportador(cursor.getString(1));
                    transportadorEntities.setNombreTransportadro(cursor.getString(2));
                    transportadorEntities.setCaracteristicaTransportador(cursor.getString(4));
                    transportadorEntitiesArrayList.add(transportadorEntities);
                }

                lista.add(0, "Seleccione Transportador");
                for (int i = 1; i <= transportadorEntitiesArrayList.size(); i++) {
                    lista.add(transportadorEntitiesArrayList.get(i - 1).getIdTransportador() + " - " + transportadorEntitiesArrayList.get(i - 1).getNombreTransportadro() + " - " + transportadorEntitiesArrayList.get(i - 1).getTipoTransportador() + " - " + transportadorEntitiesArrayList.get(i - 1).getCaracteristicaTransportador());
                }
                if(lista.size()>1)
                {
                    btnEditarTransportador.setVisibility(View.VISIBLE);
                }
            }


        ArrayAdapter<String> adapterTansportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, lista);

        spinnerTransportadores.setTitle("Buscar Transportador");
        spinnerTransportadores.setPositiveButton("Cerrar");
        spinnerTransportadores.setAdapter(adapterTansportadores);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dialogAgregarTransportador() {

        dialogAgregarTransportador = new Dialog(getContext());
        dialogAgregarTransportador.setContentView(R.layout.dialog_crear_transportador);
        final TextInputEditText txtNombreTransportador = dialogAgregarTransportador.findViewById(R.id.txtNombreTransportador);
        final TextInputEditText txtDescripcionTransportador = dialogAgregarTransportador.findViewById(R.id.txtDescripcionTransportador);

        final SearchableSpinner spinnerPlanta = dialogAgregarTransportador.findViewById(R.id.spinnerPlanta);

        spinnerPlanta.setTitle("Buscar Planta");
        spinnerPlanta.setPositiveButton("Cerrar");

        final Spinner spinnerTipoTransportador = dialogAgregarTransportador.findViewById(R.id.spinnerTipoTransportador);
        final ProgressBar progressBarTranspor = dialogAgregarTransportador.findViewById(R.id.progressBarTranspor);
        progressBarTranspor.setVisibility(View.INVISIBLE);
        llenarSpinnerDialog(spinnerPlanta, spinnerTipoTransportador);


        Button btnCrearTransportador = dialogAgregarTransportador.findViewById(R.id.btnAgregarTransportador);

        btnCrearTransportador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarTranspor.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                  if(spinnerTipoTransportador.getSelectedItem().toString().equals("Seleccione Transportador"))
                {
                    TextView errorText = (TextView) spinnerTipoTransportador.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Debe Escoger un tipo de transportador");//
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                }
                  else if (txtNombreTransportador.getText().toString().equals("")) {
                      txtNombreTransportador.setError("Debe ingresar un nombre para el transportador");
                      getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                      progressBarTranspor.setVisibility(View.INVISIBLE);

                  }
                else if (spinnerPlanta.getSelectedItem().toString().equals("Seleccione Planta"))
                {
                    TextView errorText = (TextView) spinnerPlanta.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Debe Escoger una Planta");//

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                }
                else if (txtDescripcionTransportador.getText().toString().equals(""))
                {
                    txtDescripcionTransportador.setError("Debe ingresar una descripción abreviada del transportador");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                }
                else
                {
                    final String nombreTransportador = txtNombreTransportador.getText().toString().toUpperCase();
                    final String descripcionTransportador = txtDescripcionTransportador.getText().toString().toUpperCase();
                    final String tipoTransportador = validarTipoTransportador(spinnerTipoTransportador.getSelectedItem().toString());

                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                    cursor = db.rawQuery("SELECT max(idTransportador) from transportador", null);
                    cursor.moveToFirst();

                    int idMaximaTransportador;
                    final int idInsertar;
                    if(cursor.getCount()==1 && cursor.getString(0)==null || cursor.getString(0).equals("null")  )
                    {
                        idInsertar = 1;
                    }
                    else
                    {
                        idMaximaTransportador = Integer.parseInt(cursor.getString(0));
                        idInsertar = idMaximaTransportador + 1;
                    }
                    String idPlanta = spinnerPlanta.getSelectedItem().toString().split(" - ")[0];
                    db.execSQL("INSERT INTO transportador (idTransportador, tipoTransportador, nombreTransportador, codplanta, caracteristicaTransportador, estadoRegistroTransportador) values(" + idInsertar + ",'" + tipoTransportador + "','" + nombreTransportador + "','" + idPlanta + "','" + descripcionTransportador + "','Pendiente INSERTAR BD')");
                    llenarSpinnerOffline();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



                    if (MainActivity.isOnline(getContext())) {
                        String url = Constants.url + "crearTransportador";
                        StringRequest requestCrearTransportador = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                               String url=Constants.url+"maxTransportador";
                               StringRequest request=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                   @Override
                                   public void onResponse(String response) {

                                       FragmentPartesVertical.idMaximaRegistro.clear();
                                       Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                       }.getType();
                                       FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                       db.execSQL("update transportador set idTransportador="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistroTransportador='Sincronizado' where idTransportador="+idInsertar);

                                       MDToast.makeText(getContext(), "TRANSPORTADOR REGISTRADO CORRECTAMENTE", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                       recargarTodo(progressBarTranspor);
                                       dialogAgregarTransportador.cancel();
                                   }
                               }, new Response.ErrorListener() {
                                   @Override
                                   public void onErrorResponse(VolleyError error) {

                                   }
                               });
                               queue.add(request);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBarTranspor.setVisibility(View.INVISIBLE);

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("tipoTransportador", tipoTransportador);
                                params.put("nombreTransportador", nombreTransportador);
                                params.put("idPlanta", spinnerPlanta.getSelectedItem().toString().split(" - ")[0]);
                                params.put("descripcionTransportador", descripcionTransportador);

                                return params;
                            }
                        };
                        queue.add(requestCrearTransportador);
                    } else {
                        MDToast.makeText(getContext(), "TRANSPORTADOR REGISTRADO CORRECTAMENTE", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                        recargarTodo(progressBarTranspor);
                        dialogAgregarTransportador.cancel();


                    }

                }
            }
        });

        dialogAgregarTransportador.setCancelable(true);
        dialogAgregarTransportador.show();

    }

    private void llenarSpinnerDialog(Spinner spinnerPlanta, Spinner spinnerTipoTransportador) {


            llenarSpinnerOffline();


        ArrayAdapter<String> adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaClientes);

        spinnerPlanta.setAdapter(adapterClientes);


        transportadores = new ArrayList<>();
        transportadores.add("Seleccione Transportador");
        transportadores.add("Transportador horizontal o inclinado");
        transportadores.add("Transportador vertical");
        transportadores.add("Transportador de transmisión pesada");

        adapterTransportadores = new ArrayAdapter(getContext(), R.layout.estilo_spinner, transportadores);
        spinnerTipoTransportador.setAdapter(adapterTransportadores);
    }

    public void recargarTodo(final ProgressBar progressBarTranspor) {

                llenarSpinnerOffline();

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarTranspor.setVisibility(View.INVISIBLE);

                dialogAgregarTransportador.cancel();

    }

    public String validarTipoTransportador(String s) {
        if (s.equals("Transportador vertical")) {
            return "B.E";
        } else if (s.equals("Transportador horizontal o inclinado")) {
            return "B.T";
        } else {
            return "B.DSF";
        }

    }


}