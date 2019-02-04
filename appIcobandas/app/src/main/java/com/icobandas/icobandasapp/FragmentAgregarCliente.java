package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.icobandas.icobandasapp.Modelos.IdMaxCliente;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.tooltip.Tooltip;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAgregarCliente extends Fragment implements View.OnFocusChangeListener{

    EditText txtNit;
    EditText txtNombreCliente;
    EditText txtNombrePlanta;
    SearchableSpinner spinnerCiudades;
    EditText txtDireccion;
    Button btnGuardarCliente;
    String ciudad;
    Gson gson = new Gson();

    RequestQueue queue;

    AlertDialog.Builder alerta;

    ProgressBar progressBar;


    ArrayList<String> listaCiudades= new ArrayList<>();
    ArrayList<IdMaxCliente> idMaxClientes= new ArrayList<>();

    View view;



    public FragmentAgregarCliente() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view= inflater.inflate(R.layout.fragment_agregar_cliente, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializar();
        getActivity().setTitle("Agregar Clientes");

        txtNit.setOnFocusChangeListener(this);
        txtNombrePlanta.setOnFocusChangeListener(this);
        txtNombreCliente.setOnFocusChangeListener(this);
        txtDireccion.setOnFocusChangeListener(this);
        progressBar.setVisibility(View.INVISIBLE);

        listas();

        spinnerCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ciudad= listaCiudades.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



               final String nitCliente=txtNit.getText().toString();
               final String nombreCliente=txtNombreCliente.getText().toString().toUpperCase();
               final String nombrePlanta=txtNombrePlanta.getText().toString().toUpperCase();
               final String ciudad=spinnerCiudades.getSelectedItem().toString().split(" - ")[0];
               final String direccion =txtDireccion.getText().toString();
               if(nitCliente.equals(""))
               {
                   txtNit.setError("Debe registrar un NIT de cliente");
                   progressBar.setVisibility(View.INVISIBLE);
                   getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
               }
               else if (nombreCliente.equals(""))
               {
                   txtNombreCliente.setError("Debe registrar un nombre de cliente");
                   progressBar.setVisibility(View.INVISIBLE);
                   getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
               }
               else if(nombrePlanta.equals(""))
               {
                   txtNombrePlanta.setError("Debe registrar al menos una planta");
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
                   String url=Constants.url+"validarNit/"+nitCliente;
                   StringRequest requestValidarNit = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           if(!response.equals("\uFEFF[]"))
                           {
                               progressBar.setVisibility(View.INVISIBLE);
                               getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                               alerta.setTitle("ICOBANDAS S.A dice:");
                               alerta.setMessage("El NIT de cliente ingresado ya se encuentra registrado");
                               alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.cancel();
                                   }
                               });

                               alerta.create();
                               alerta.show();
                           }
                           else
                           {
                               String url=Constants.url+"crearCliente";
                               StringRequest requestCrearCliente= new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                               {
                                   @Override
                                   public void onResponse(String response) {




                                       ////////////////////////////////////////////////////////

                                       String url=Constants.url+"crearPlanta";
                                       StringRequest requestCrearPlanta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                           @Override
                                           public void onResponse(String response) {


                                               String url = Constants.url + "login/" + Login.loginJsons.get(0).getNombreUsuario() + "&" + Login.loginJsons.get(0).getContraseñaUsuario();
                                               final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                   @Override
                                                   public void onResponse(String response) {

                                                       Type type = new TypeToken<List<LoginJson>>() {
                                                       }.getType();
                                                       Login.loginJsons = gson.fromJson(response, type);
                                                       progressBar.setVisibility(View.INVISIBLE);
                                                       getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                       Toast.makeText(getContext(), "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();

                                                       getFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentSeleccionarTransportador()).commit();

                                                   }
                                               }, new Response.ErrorListener() {
                                                   @Override
                                                   public void onErrorResponse(VolleyError error) {

                                                       MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

                                                   }
                                               });
                                               queue.add(requestLogin);



                                           }
                                       }, new Response.ErrorListener() {
                                           @Override
                                           public void onErrorResponse(VolleyError error) {
                                               MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                               progressBar.setVisibility(View.INVISIBLE);
                                               getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                           }
                                       }){
                                           @Override
                                           protected Map<String, String> getParams() {
                                               Map<String, String> params = new HashMap<>();
                                               params.put("nitCliente", nitCliente);
                                               params.put("NombrePlanta",nombrePlanta);
                                               params.put("idUsuario",Login.loginJsons.get(0).getNombreUsuario());
                                               params.put("direccionPlanta",direccion);
                                               params.put("ciudad",ciudad);

                                               return params;
                                           }
                                       };
                                       queue.add(requestCrearPlanta);
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
                                       params.put("nombreCliente",nombreCliente);

                                       return params;
                                   }
                               };
                               queue.add(requestCrearCliente);

                           }

                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {

                           MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                           progressBar.setVisibility(View.VISIBLE);
                           getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                       }
                   });
                   queue.add(requestValidarNit);
               }



            }
        });


        return view;
    }

    private void inicializar() {
        spinnerCiudades=view.findViewById(R.id.spinnerCiudades);
        txtDireccion=view.findViewById(R.id.txtDireccion);
        txtNit=view.findViewById(R.id.txtNit);
        txtNombreCliente=view.findViewById(R.id.txtNombreCliente);
        txtNombrePlanta=view.findViewById(R.id.txtNombrePlanta);
        btnGuardarCliente=view.findViewById(R.id.btnGuardarCliente);
        queue= Volley.newRequestQueue(getContext());
        alerta = new AlertDialog.Builder(getContext());
        progressBar= view.findViewById(R.id.progresBarAgregarCliente);

    }
    public void listas()
    {

        listaCiudades.clear();
        listaCiudades.add(0,"Seleccione Ciudad");
        for (int i=1; i<=Login.ciudadesJsons.size(); i++)
        {
            listaCiudades.add(Login.ciudadesJsons.get(i-1).getCodpoblado()+" - "+Login.ciudadesJsons.get(i-1).getUnido());
        }
        ArrayAdapter<String> adapterCiudades =new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaCiudades);
        spinnerCiudades.setAdapter(adapterCiudades);
        spinnerCiudades.setTitle("Buscar Ciudad");
        spinnerCiudades.setPositiveButton("Cerrar");


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Tooltip tooltip = null;
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
