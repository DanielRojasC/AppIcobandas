package com.icobandas.icobandasapp;


import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.CiudadesEntities;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.tooltip.Tooltip;
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
public class FragmentAgregarPlantas extends Fragment implements View.OnFocusChangeListener{

    ArrayList<String> listaClientes = new ArrayList<>();
    ArrayList<String> listaCiudades = new ArrayList<>();

    ArrayList<ClientesEntities> clientesEntitiesArrayList;
    ArrayList<String> clientesList;


    View view;
    SearchableSpinner spinnerClientes;
    EditText txtNombrePlanta;
    EditText txtDireccionPlanta;
    SearchableSpinner spinnerCiudades;
    Button btnGuardarPlanta;
    RequestQueue queue;
    ProgressBar progressBar;
    Gson gson = new Gson();
    DbHelper dbHelper;
    Cursor cursor;



    public FragmentAgregarPlantas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_agregar_plantas, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializar();
        MainActivity.txtTitulo.setText("Crear plantas");


        progressBar.setVisibility(View.INVISIBLE);

            llenarSpinnersOffline();





        btnGuardarPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 crearPlanta();
            }
        });



        return view;
    }

    private void inicializar() {
        txtDireccionPlanta=view.findViewById(R.id.txtDireccion);
        txtNombrePlanta=view.findViewById(R.id.txtNombrePlantaAgregar);
        spinnerCiudades=view.findViewById(R.id.spinnerCiudades);
        spinnerClientes=view.findViewById(R.id.spinnerNombreCliente);
        queue= Volley.newRequestQueue(getContext());
        btnGuardarPlanta=view.findViewById(R.id.btnCrearPlanta);
        progressBar=view.findViewById(R.id.progressBarAgregarPlanta);
        dbHelper= new DbHelper(getContext(),"prueba",null,1);

    }


    public void llenarSpinnersOffline()
    {
        listaClientes.clear();
        listaCiudades.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor=db.rawQuery("Select * from clientes",null);
        clientesEntitiesArrayList= new ArrayList<>();
        clientesList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            ClientesEntities clientesEntities= new ClientesEntities();
            clientesEntities.setNit(cursor.getString(0));
            clientesEntities.setNameunido(cursor.getString(1));
            clientesEntitiesArrayList.add(clientesEntities);
        }
        for (int i=1;i<=clientesEntitiesArrayList.size();i++)
        {
            clientesList.add(clientesEntitiesArrayList.get(i-1).getNit()+" - "+clientesEntitiesArrayList.get(i-1).getNameunido());
        }


        Set<String> hs = new HashSet<>();
        hs.addAll(clientesList);
        clientesList.clear();
        clientesList.addAll(hs);
        Collections.sort(clientesList,String.CASE_INSENSITIVE_ORDER);
        clientesList.add(0,"Seleccione Cliente");



        ArrayAdapter<String> adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, clientesList);
        spinnerClientes.setAdapter(adapterClientes);

        /////////////////////////////////////////////////////////////////////////////////////


        ArrayList<CiudadesEntities>ciudadesEntitiesArrayList= new ArrayList<>();
        ciudadesEntitiesArrayList.clear();


        cursor=db.rawQuery("Select * from ciudades",null);

        while (cursor.moveToNext())
        {
            CiudadesEntities ciudadesEntities= new CiudadesEntities();
            ciudadesEntities.setIdCiudad(cursor.getString(0));
            ciudadesEntities.setNombreCiudad(cursor.getString(1));
            ciudadesEntitiesArrayList.add(ciudadesEntities);

        }
        listaCiudades.clear();
        listaCiudades.add(0,"Seleccione Ciudad");
        for (int i=1; i<=ciudadesEntitiesArrayList.size(); i++)
        {
            listaCiudades.add(ciudadesEntitiesArrayList.get(i-1).getIdCiudad()+" - "+ciudadesEntitiesArrayList.get(i-1).getNombreCiudad());
        }
        ArrayAdapter<String> adapterCiudades =new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaCiudades);
        spinnerCiudades.setAdapter(adapterCiudades);
        spinnerCiudades.setTitle("Buscar Ciudad");
        spinnerCiudades.setPositiveButton("Cerrar");

    }

    public void crearPlanta()
    {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final String nitCliente=spinnerClientes.getSelectedItem().toString().split(" - ")[0];
        final String nombrePlanta= txtNombrePlanta.getText().toString().toUpperCase();
        final String direccionPlanta=txtDireccionPlanta.getText().toString();
        final String idCiudad=spinnerCiudades.getSelectedItem().toString().split(" - ")[0];

        if(nombrePlanta.equals(""))
        {
            txtNombrePlanta.setError("Debe registrar un nombre de planta");
            progressBar.setVisibility(View.INVISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
        else if(spinnerClientes.getSelectedItem().toString().equals("Seleccione Cliente"))
        {
            TextView errorText = (TextView)spinnerClientes.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Debe seleccionar un cliente");//changes the selected item text to this
            progressBar.setVisibility(View.INVISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        else if(spinnerCiudades.getSelectedItem().toString().equals("Seleccione Ciudad"))
        {
            TextView errorText = (TextView)spinnerCiudades.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Debe seleccionar la ciudad");//changes the selected item text to this
            progressBar.setVisibility(View.INVISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        else
        {
            final SQLiteDatabase db=dbHelper.getWritableDatabase();

            cursor=db.rawQuery("SELECT codplanta FROM plantas ORDER BY codplanta DESC limit 1",null);

            cursor.moveToFirst();
            String idMax=cursor.getString(0);
            int idMaxPlanta=Integer.parseInt(idMax);
            final int plantaFinal=idMaxPlanta+1;
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            db.execSQL("INSERT INTO plantas (codplanta, agenteplanta, nitplanta, nameplanta,ciudmciapl,dirmciapl,estadoRegistroPlanta) values("+plantaFinal+",'"+Login.nombreUsuario+"','"+nitCliente+"','"+nombrePlanta+"',"+idCiudad+",'"+direccionPlanta+"','Pendiente INSERTAR BD')");


            /*if(MainActivity.isOnline(getContext()))
            {
                String url=Constants.url+"crearPlanta";

                StringRequest requestCrearPlanta= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String url=Constants.url+"maxPlanta";
                        StringRequest requestPlanta=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                FragmentPartesVertical.idMaximaRegistro.clear();
                                Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                }.getType();
                                FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                MDToast.makeText(MainActivity.context, "Planta agregada correctamente", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                db.execSQL("update plantas set codplanta="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where codplanta="+plantaFinal);

                                db.execSQL("update plantas set estadoRegistroPlanta='Sincronizado' where codplanta="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                                getFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentSeleccionarTransportador()).commit();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(requestPlanta);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.INVISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("nitCliente", nitCliente);
                        params.put("NombrePlanta",nombrePlanta);
                        params.put("idUsuario",Login.nombreUsuario);
                        params.put("direccionPlanta",direccionPlanta);
                        params.put("ciudad",idCiudad);

                        return params;
                    }
                };
                requestCrearPlanta.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(requestCrearPlanta);
            }
            else
            {*/
                MDToast.makeText(getContext(),"Planta agregada correctamente", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                getFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentSeleccionarTransportador()).commit();
                progressBar.setVisibility(View.INVISIBLE);


            //}
        }



    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Tooltip tooltip;
        if(hasFocus)
        {
            switch (v.getId())
            {
                case R.id.txtNombrePlanta:
                    tooltip = new Tooltip.Builder(txtNombrePlanta).setText("Digite el nombre ÚNICO de planta o nombre abreviado del cliente + guión + ciudad.\n Ej. CEMEX CARACOLITO ó HOLCIM-NOBSA")
                            .setTextColor(Color.WHITE)
                            .setGravity(Gravity.BOTTOM)
                            .setCornerRadius(8f)
                            .setDismissOnClick(true)
                            .setCancelable(true)
                            .show();

                    break;
            }
        }
    }
}
