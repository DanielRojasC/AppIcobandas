package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Modelos.CiudadesJson;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPartesPesada extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {


    TextView tvTipoTransmision;
    TextView tvDiametroPoleaConducida;
    TextView tvAnchoPoleaConducida;
    TextView tvAnchoBandaTransmision;
    TextView tvDiametroPoleaMotriz;
    TextView tvAnchoPoleaMotriz;
    TextView tvDistanciaCentros;

    Spinner spinnerTipoTransmision;
    TextInputEditText txtDiametroPoleaConducida;
    TextInputEditText txtAnchoPoleaConducida;
    TextInputEditText txtAnchoBandaTransmision;
    TextInputEditText txtDiametroPoleaMotriz;
    TextInputEditText txtAnchoPoleaMotriz;
    TextInputEditText txtDistanciaEntreCentros;
    TextInputEditText txtPotenciaMotor;
    TextInputEditText txtRpmSalidaReductor;
    AlertDialog dialogCarga;

    ArrayAdapter<String> adapterTipoTransmision;



            View view;
    Button btnEnviarInformacion;
    Gson gson = new Gson();
    RequestQueue queue;
    AlertDialog.Builder alerta;


    public FragmentPartesPesada() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_partes_pesada, container, false);
        getActivity().setTitle("Banda de Transmisión Pesada");
        inicializar();
        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros();

        }
        tvAnchoBandaTransmision.setOnClickListener(this);
        tvTipoTransmision.setOnClickListener(this);
        tvDiametroPoleaConducida.setOnClickListener(this);
        tvAnchoPoleaConducida.setOnClickListener(this);
        tvAnchoPoleaMotriz.setOnClickListener(this);
        tvDistanciaCentros.setOnClickListener(this);
        tvDiametroPoleaMotriz.setOnClickListener(this);

        txtDiametroPoleaConducida.setOnFocusChangeListener(this);
        txtAnchoPoleaConducida.setOnFocusChangeListener(this);
        txtAnchoBandaTransmision.setOnFocusChangeListener(this);
        txtDiametroPoleaMotriz.setOnFocusChangeListener(this);
        txtAnchoPoleaMotriz.setOnFocusChangeListener(this);


        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("Desea agregar éste registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {
                            String url = Constants.url + "crearRegistro";
                            StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url = Constants.url + "maxRegistro";
                                    StringRequest requestMaxRegistro = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                            String url = Constants.url + "registroBandaPesada";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");



                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("tipoParteTransmision", spinnerTipoTransmision.getSelectedItem().toString());
                                                    params.put("anchoBandaTransmision", txtAnchoBandaTransmision.getText().toString());
                                                    params.put("distanciaEntreCentrosTransmision", txtDistanciaEntreCentros.getText().toString());
                                                    params.put("potenciaMotorTransmision", txtPotenciaMotor.getText().toString());
                                                    params.put("rpmSalidaReductorTransmision", txtRpmSalidaReductor.getText().toString());
                                                    params.put("diametroPoleaConducidaTransmision", txtDiametroPoleaConducida.getText().toString());
                                                    params.put("anchoPoleaConducidaTransmision", txtAnchoPoleaConducida.getText().toString());
                                                    params.put("diametroPoleaMotrizTransmision", txtDiametroPoleaMotriz.getText().toString());
                                                    params.put("anchoPoleaMotrizTransmision", txtAnchoPoleaMotriz.getText().toString());

                                                    if (!txtAnchoBandaTransmision.getText().toString().equals("")) {
                                                        params.put("anchoBandaTransmision", String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString())));
                                                    }
                                                    if (!txtDistanciaEntreCentros.getText().toString().equals("")) {
                                                        params.put("distanciaEntreCentrosTransmision", String.valueOf(Float.parseFloat(txtDistanciaEntreCentros.getText().toString())));
                                                    }
                                                    if (!txtPotenciaMotor.getText().toString().equals("")) {
                                                        params.put("potenciaMotorTransmision", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                                    }
                                                    if (!txtRpmSalidaReductor.getText().toString().equals("")) {
                                                        params.put("rpmSalidaReductorTransmision", String.valueOf(Float.parseFloat(txtRpmSalidaReductor.getText().toString())));
                                                    }
                                                    if (!txtDiametroPoleaConducida.getText().toString().equals("")) {
                                                        params.put("diametroPoleaConducidaTransmision", String.valueOf(Float.parseFloat(txtDiametroPoleaConducida.getText().toString())));
                                                    }
                                                    if (!txtAnchoPoleaConducida.getText().toString().equals("")) {
                                                        params.put("anchoPoleaConducidaTransmision", String.valueOf(Float.parseFloat(txtAnchoPoleaConducida.getText().toString())));
                                                    }
                                                    if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                                        params.put("diametroPoleaMotrizTransmision", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                                    }
                                                    if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                                        params.put("anchoPoleaMotrizTransmision", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                            dialogCarga.dismiss();

                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    dialogCarga.dismiss();

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("fechaRegistro", FragmentPartesVertical.fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        } else {
                            String url = Constants.url + "actualizarBandaPesada";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    dialogCarga.dismiss();

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("tipoParteTransmision", spinnerTipoTransmision.getSelectedItem().toString());
                                    params.put("anchoBandaTransmision", txtAnchoBandaTransmision.getText().toString());
                                    params.put("distanciaEntreCentrosTransmision", txtDistanciaEntreCentros.getText().toString());
                                    params.put("potenciaMotorTransmision", txtPotenciaMotor.getText().toString());
                                    params.put("rpmSalidaReductorTransmision", txtRpmSalidaReductor.getText().toString());
                                    params.put("diametroPoleaConducidaTransmision", txtDiametroPoleaConducida.getText().toString());
                                    params.put("anchoPoleaConducidaTransmision", txtAnchoPoleaConducida.getText().toString());
                                    params.put("diametroPoleaMotrizTransmision", txtDiametroPoleaMotriz.getText().toString());
                                    params.put("anchoPoleaMotrizTransmision", txtAnchoPoleaMotriz.getText().toString());

                                    if (!txtAnchoBandaTransmision.getText().toString().equals("")) {
                                        params.put("anchoBandaTransmision", String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString())));
                                    }
                                    if (!txtDistanciaEntreCentros.getText().toString().equals("")) {
                                        params.put("distanciaEntreCentrosTransmision", String.valueOf(Float.parseFloat(txtDistanciaEntreCentros.getText().toString())));
                                    }
                                    if (!txtPotenciaMotor.getText().toString().equals("")) {
                                        params.put("potenciaMotorTransmision", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                    }
                                    if (!txtRpmSalidaReductor.getText().toString().equals("")) {
                                        params.put("rpmSalidaReductorTransmision", String.valueOf(Float.parseFloat(txtRpmSalidaReductor.getText().toString())));
                                    }
                                    if (!txtDiametroPoleaConducida.getText().toString().equals("")) {
                                        params.put("diametroPoleaConducidaTransmision", String.valueOf(Float.parseFloat(txtDiametroPoleaConducida.getText().toString())));
                                    }
                                    if (!txtAnchoPoleaConducida.getText().toString().equals("")) {
                                        params.put("anchoPoleaConducidaTransmision", String.valueOf(Float.parseFloat(txtAnchoPoleaConducida.getText().toString())));
                                    }
                                    if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                        params.put("diametroPoleaMotrizTransmision", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                    }
                                    if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                        params.put("anchoPoleaMotrizTransmision", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                    }

                                    return params;
                                }
                            };
                            queue.add(requestRegistroTornillos);
                        }

                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alerta.create();
                alerta.show();

            }
        });



        return view;
    }



    private void inicializar() {
        tvAnchoBandaTransmision=view.findViewById(R.id.tvAnchoBandaTransmision);
        tvTipoTransmision=view.findViewById(R.id.tvTipoTransmision);
        tvDiametroPoleaConducida=view.findViewById(R.id.tvDiametroPoleaConducida);
        tvAnchoPoleaConducida=view.findViewById(R.id.tvAnchoPoleaConducida);
        tvAnchoPoleaMotriz=view.findViewById(R.id.tvAnchoPoleaMotrizTransmision);
        tvDistanciaCentros=view.findViewById(R.id.tvDistanciaEntreCentroTransmision);
        tvDiametroPoleaMotriz=view.findViewById(R.id.tvDiametroPoleaMotrizPesada);

        spinnerTipoTransmision=view.findViewById(R.id.spinnerTipoTransmision);
        txtDiametroPoleaConducida=view.findViewById(R.id.txtDiametroPoleaConducida);
        txtAnchoPoleaConducida=view.findViewById(R.id.txtAnchoPoleaConducidaPesada);
        txtAnchoBandaTransmision=view.findViewById(R.id.txtAnchoBandaTransmision);
        txtDiametroPoleaMotriz=view.findViewById(R.id.txtDiametroPoleaMotrizPesada);
        txtAnchoPoleaMotriz=view.findViewById(R.id.txtAnchoPoleaMotrizPesada);
        txtDistanciaEntreCentros=view.findViewById(R.id.txtDistanciaCentrosPesada);
        txtPotenciaMotor=view.findViewById(R.id.txtPotenciaMotorPesada);
        txtRpmSalidaReductor=view.findViewById(R.id.txtRpmSalidaReductorPesada);
        dialogCarga=new SpotsDialog(getContext(),"Ejecutando Registro");

        btnEnviarInformacion=view.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        queue=Volley.newRequestQueue(getContext());

        alerta=new AlertDialog.Builder(getContext());
        adapterTipoTransmision =new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransmision);
        spinnerTipoTransmision.setAdapter(adapterTipoTransmision);
    }


    @Override
    public void onClick(View v) {
        Dialog dialogParte;
        ImageView imgParte;
        switch (v.getId())
        {
            case R.id.tvAnchoBandaTransmision:
            case R.id.tvDiametroPoleaConducida:
            case R.id.tvAnchoPoleaConducida:
            case R.id.tvAnchoPoleaMotrizTransmision:
            case R.id.tvDistanciaEntreCentroTransmision:
            case R.id.tvDiametroPoleaMotrizPesada:
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.dimensiones_dsf);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case R.id.tvTipoTransmision:
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tipo_transmision);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
            break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {

            if (!txtDiametroPoleaConducida.getText().toString().equals("")) {
                float numero = Float.parseFloat(txtDiametroPoleaConducida.getText().toString());
                if (numero < 50 || numero > 2700) {
                    txtDiametroPoleaConducida.setText("");
                    txtDiametroPoleaConducida.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                }
            }
            if (!txtAnchoPoleaConducida.getText().toString().equals("")) {
                float numero = Float.parseFloat(txtAnchoPoleaConducida.getText().toString());
                if (numero < 50 || numero > 2700) {
                    txtAnchoPoleaConducida.setText("");
                    txtAnchoPoleaConducida.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                }
            }
            if (!txtAnchoBandaTransmision.getText().toString().equals("")) {
                float numero = Float.parseFloat(txtAnchoBandaTransmision.getText().toString());
                if (numero < 50 || numero > 2700) {
                    txtAnchoBandaTransmision.setText("");
                    txtAnchoBandaTransmision.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                }
            }
            if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                float numero = Float.parseFloat(txtDiametroPoleaMotriz.getText().toString());
                if (numero < 50 || numero > 2700) {
                    txtDiametroPoleaMotriz.setText("");
                    txtDiametroPoleaMotriz.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                }
            }
            if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                float numero = Float.parseFloat(txtAnchoPoleaMotriz.getText().toString());
                if (numero < 50 || numero > 2700) {
                    txtAnchoPoleaMotriz.setText("");
                    txtAnchoPoleaMotriz.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                }
            }
        }
    }

    private void llenarRegistros() {

        for(int i=0;i<Login.loginJsons.size();i++)
        {
            if(Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax()))
            {
                txtDiametroPoleaConducida.setText(Login.loginJsons.get(i).getDiametroPoleaConducidaTransmision());
                txtAnchoBandaTransmision.setText(Login.loginJsons.get(i).getAnchoBandaTransmision());
                txtAnchoPoleaConducida.setText(Login.loginJsons.get(i).getAnchoPoleaConducidaTransmision());
                txtAnchoPoleaMotriz.setText(Login.loginJsons.get(i).getAnchoPoleaMotrizTransmision());
                txtDistanciaEntreCentros.setText(Login.loginJsons.get(i).getDistanciaEntreCentrosTransmision());
                txtDiametroPoleaMotriz.setText(Login.loginJsons.get(i).getDiametroPoleaMotrizTransmision());
                txtPotenciaMotor.setText(Login.loginJsons.get(i).getPotenciaMotorTransmision());
                txtRpmSalidaReductor.setText(Login.loginJsons.get(i).getRpmSalidaReductorTransmision());

                int p1=adapterTipoTransmision.getPosition(Login.loginJsons.get(i).getTipoParteTransmision());

                spinnerTipoTransmision.setSelection(p1);

                i=Login.loginJsons.size()+1;

            }
        }


    }


    public  void login(final String dialog)
    {
        String url = Constants.url + "login/" + Login.loginJsons.get(0).getNombreUsuario() + "&" + Login.loginJsons.get(0).getContraseñaUsuario();
        final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Type type = new TypeToken<List<LoginJson>>() {
                }.getType();
                Login.loginJsons = gson.fromJson(response, type);


                String url = Constants.url + "transportadores/" + Login.loginJsons.get(0).getNombreUsuario();
                StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Type type = new TypeToken<List<LoginTransportadores>>() {
                        }.getType();
                        Login.loginTransportadores = gson.fromJson(response, type);

                        String url = Constants.url + "ciudades";
                        StringRequest requestCiudades = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Type type = new TypeToken<List<CiudadesJson>>() {
                                }.getType();
                                Login.ciudadesJsons = gson.fromJson(response, type);
                                MDToast.makeText(getContext(),"REGISTRO EXITOSO",MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera="Actualizar";
                                dialogCarga.dismiss();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                dialogCarga.dismiss();
                            }
                        });
                        queue.add(requestCiudades);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                        dialogCarga.dismiss();
                    }
                });
                queue.add(requestTransportadores);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                dialogCarga.dismiss();


            }
        });
        queue.add(requestLogin);
    }
}
