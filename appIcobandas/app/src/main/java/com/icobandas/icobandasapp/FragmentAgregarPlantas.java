package com.icobandas.icobandasapp;


import android.content.pm.ActivityInfo;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.tooltip.Tooltip;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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


    View view;
    SearchableSpinner spinnerClientes;
    EditText txtNombrePlanta;
    EditText txtDireccionPlanta;
    SearchableSpinner spinnerCiudades;
    Button btnGuardarPlanta;
    RequestQueue queue;
    ProgressBar progressBar;
    Gson gson = new Gson();



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
        getActivity().setTitle("Agregar Plantas");

        progressBar.setVisibility(View.INVISIBLE);
        llenarSpinners();



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
    }

    public void llenarSpinners()
    {
        listaClientes.clear();

        for (int i = 1; i <= Login.loginJsons.size(); i++) {
            listaClientes.add(Login.loginJsons.get(i-1).getNit() + " - "+Login.loginJsons.get(i-1).getNameunido());

        }

        Set<String> hs = new HashSet<>();
        hs.addAll(listaClientes);
        listaClientes.clear();
        listaClientes.addAll(hs);
        listaClientes.add(0,"Seleccione Cliente");



        ArrayAdapter<String> adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaClientes);
        spinnerClientes.setAdapter(adapterClientes);

        /////////////////////////////////////////////////////////////////////////////////////


        listaCiudades.clear();
        listaCiudades.add(0,"Seleccione Ciudad");
        for (int i=1; i<=Login.ciudadesJsons.size(); i++)
        {
            listaCiudades.add(Login.ciudadesJsons.get(i-1).getCodpoblado()+" - "+Login.ciudadesJsons.get(i-1).getUnido());
        }

        ArrayAdapter<String> adapterCiudades =new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaCiudades);
        spinnerCiudades.setAdapter(adapterCiudades);

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
            String url=Constants.url+"crearPlanta";

            StringRequest requestCrearPlanta= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {






                    String url = Constants.url + "login/" + Login.loginJsons.get(0).getNombreUsuario() + "&" + Login.loginJsons.get(0).getContraseñaUsuario();
                    final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.INVISIBLE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Type type = new TypeToken<List<LoginJson>>() {
                            }.getType();
                            Login.loginJsons = gson.fromJson(response, type);
                            Toast.makeText(getContext(), "Planta agregada correctamente", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentSeleccionarTransportador()).commit();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressBar.setVisibility(View.INVISIBLE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    queue.add(requestLogin);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nitCliente", nitCliente);
                    params.put("NombrePlanta",nombrePlanta);
                    params.put("idUsuario",Login.loginJsons.get(0).getNombreUsuario());
                    params.put("direccionPlanta",direccionPlanta);
                    params.put("ciudad",idCiudad);

                    return params;
                }
            };
            queue.add(requestCrearPlanta);
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
