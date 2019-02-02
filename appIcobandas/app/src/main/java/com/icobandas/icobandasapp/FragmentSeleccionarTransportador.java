package com.icobandas.icobandasapp;


import android.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.transition.Slide;
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
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
    Spinner spinnerPartes;
    ArrayList<String> lista = new ArrayList<>();
    public static ArrayList<String> listaClientes = new ArrayList<>();
    static ArrayList<String> vacio = new ArrayList<>();

    public static String seleccion;
    View view;

    AlertDialog.Builder alerta;
    String cliente;
    Button btnIr;
    Button btnAgregarTransportador;
    static Dialog dialogAgregarTransportador;
    RequestQueue queue;
    Gson gson = new Gson();
    ArrayList<String> listaPlantas = new ArrayList<>();
    Spinner spinnerPlantas;
    public static ArrayAdapter<String> adapterClientes;
    String codPlanta;

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


        llenarSpinner();

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
                        seleccion = spinnerTransportadores.getSelectedItem().toString().split(" - ")[2];

                        if (seleccion.equals("") || seleccion.equals("Seleccione Transportador")) {
                            TextView errorText = (TextView) spinnerTransportadores.getSelectedView();
                            errorText.setError("anything here, just to add the icon");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Debe Escoger un transportador");//
                        }
                        else if (!cliente.equals("") && !cliente.equals("Seleccione Planta") && !seleccion.equals("") && !seleccion.equals("Seleccione Transportador")) {

                            idTransportadorRegistro = spinnerTransportadores.getSelectedItem().toString().split(" - ")[0];
                            idPlantaRegistro = spinnerClientes.getSelectedItem().toString().split(" - ")[0];

                            if (seleccion.equals("B.T")) {

                                String url = Constants.url + "buscarRegistroTransportadorVertical/" + idTransportadorRegistro;
                                StringRequest request = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("\uFEFF[]"))
                                        {

                                            bandera="Nuevo";

                                        }
                                        else
                                        {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                            bandera="Actualizar";
                                        }


                                        Fragment f = new FragmentPartesHorizontal();
                                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                                        f.setExitTransition(new Slide(Gravity.LEFT));

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                .commit();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);

                            }
                            else if (seleccion.equals("B.E")) {
                                String url = Constants.url + "buscarRegistroTransportadorVertical/" + idTransportadorRegistro;
                                StringRequest request = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("\uFEFF[]"))
                                        {

                                            bandera="Nuevo";

                                        }
                                        else
                                        {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                            bandera="Actualizar";
                                        }


                                        Fragment f = new FragmentPartesVertical();
                                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                                        f.setExitTransition(new Slide(Gravity.LEFT));

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                .commit();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);


                            }
                            else if (seleccion.equals("B.DSF")) {
                                String url = Constants.url + "buscarRegistroTransportadorVertical/" + idTransportadorRegistro;
                                StringRequest request = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("\uFEFF[]"))
                                        {

                                            bandera="Nuevo";

                                        }
                                        else
                                        {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                            bandera="Actualizar";
                                        }


                                        Fragment f = new FragmentPartesPesada();
                                        f.setEnterTransition(new Slide(Gravity.RIGHT));
                                        f.setExitTransition(new Slide(Gravity.LEFT));

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                .commit();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);
                            }


                        }
                    }
                }


            }
        });

        btnAgregarTransportador.setOnClickListener(new View.OnClickListener() {
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

    }

    public static void llenarSpinner() {


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

        adapterClientes = new ArrayAdapter(MainActivity.context, android.R.layout.simple_spinner_dropdown_item, listaClientes);

        spinnerClientes.setAdapter(adapterClientes);
        spinnerClientes.setTitle("Buscar Cliente");
        spinnerClientes.setPositiveButton("Cerrar");


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

        ArrayAdapter<String> adapterTansportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, lista);

        spinnerTransportadores.setTitle("Buscar Transportador");
        spinnerTransportadores.setPositiveButton("Cerrar");
        spinnerTransportadores.setAdapter(adapterTansportadores);


    }


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
                if (spinnerPlanta.getSelectedItem().toString().equals("Seleccione Planta")) {
                    TextView errorText = (TextView) spinnerPlanta.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Debe Escoger una Planta");//

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                } else if (txtNombreTransportador.getText().toString().equals("")) {
                    txtNombreTransportador.setError("Debe ingresar un nombre para el transportador");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);

                } else if (txtDescripcionTransportador.getText().toString().equals("")) {
                    txtDescripcionTransportador.setError("Debe ingresar una descripción abreviada del transportador");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBarTranspor.setVisibility(View.INVISIBLE);
                } else {
                    final String nombreTransportador = txtNombreTransportador.getText().toString().toUpperCase();
                    final String descripcionTransportador = txtDescripcionTransportador.getText().toString().toUpperCase();
                    final String tipoTransportador = validarTipoTransportador(spinnerTipoTransportador.getSelectedItem().toString());
                    String url = Constants.url + "crearTransportador";
                    StringRequest requestCrearTransportador = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Toast.makeText(getContext(), "TRANSPORTADOR REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            recargarTodo(progressBarTranspor);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                }


            }
        });

        dialogAgregarTransportador.setCancelable(true);
        dialogAgregarTransportador.show();

    }

    private void llenarSpinnerDialog(Spinner spinnerPlanta, Spinner spinnerTipoTransportador) {

        llenarSpinner();
        ArrayAdapter<String> adapterClientes = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaClientes);

        spinnerPlanta.setAdapter(adapterClientes);


        ArrayList<String> transportadores = new ArrayList<>();

        transportadores.add("TRANSPORTADOR HORIZONTAL");
        transportadores.add("TRANSPORTADOR VERTICAL");
        transportadores.add("TRANSMISIÓN PESADA");

        ArrayAdapter<String> adapterTransportadores = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, transportadores);
        spinnerTipoTransportador.setAdapter(adapterTransportadores);
    }

    public void recargarTodo(final ProgressBar progressBarTranspor) {

        progressBarTranspor.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String url = Constants.url + "transportadores/" + Login.loginJsons.get(0).getNombreUsuario();
        StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type type = new TypeToken<List<LoginTransportadores>>() {
                }.getType();
                Login.loginTransportadores = gson.fromJson(response, type);


                llenarSpinner();


                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBarTranspor.setVisibility(View.INVISIBLE);

                dialogAgregarTransportador.cancel();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(requestTransportadores);


    }

    public String validarTipoTransportador(String s) {
        if (s.equals("TRANSPORTADOR VERTICAL")) {
            return "B.E";
        } else if (s.equals("TRANSPORTADOR HORIZONTAL")) {
            return "B.T";
        } else {
            return "B.DSF";
        }

    }


}