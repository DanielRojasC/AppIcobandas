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
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPartesVertical extends Fragment implements View.OnFocusChangeListener, View.OnClickListener{

    static View view;
    Gson gson = new Gson();
    RequestQueue queue;
    ImageButton btnCangilon;
    ImageButton btnBanda;
    ImageButton btnPoleaMotriz;
    ImageButton btnPoleaCola;
    ImageButton btnEmpalme;
    ImageButton btnTornillo;
    ImageButton btnPuertaInspeccion;
    ImageButton btnSeguridad;
    ImageButton btnCondicionesCarga;
    AlertDialog dialogCarga;

    public static String tipoCangilon;

    static Date date= new Date();

    static SimpleDateFormat dateFormat1=new SimpleDateFormat("dd/MMM/yyyy");
    public static String fechaActual=dateFormat1.format(date);

    public static ArrayList<IdMaximaRegistro> idMaximaRegistro= new ArrayList<>();


    AlertDialog.Builder alerta;
    Dialog dialogParte;



    public FragmentPartesVertical() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_partes_vertical, container, false);
        getActivity().setTitle("Transportador Vertical");

        inicializar();

        btnCangilon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirDialogCangilon();
            }
        });

        btnBanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBanda();
            }
        });

        btnPoleaMotriz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaMotriz();
            }
        });

        btnPoleaCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaCola();
            }
        });

        btnEmpalme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogEmpalme();
            }
        });

        btnTornillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogTornillo();
            }
        });

        btnPuertaInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { abrirDialogPuertasInpeccion();
            }
        });

        btnSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogSeguridad();
            }
        });

        btnCondicionesCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogCondicionesCarga();
            }
        });

        return view;
    }



    private void inicializar() {

        queue = Volley.newRequestQueue(getContext());
        alerta = new AlertDialog.Builder(getContext());

        btnBanda = view.findViewById(R.id.btnBandaElevadora);
        btnCangilon = view.findViewById(R.id.btnCangilon);
        btnPoleaMotriz = view.findViewById(R.id.btnPoleaMotrizElevadora);
        btnPoleaCola = view.findViewById(R.id.btnPoleaColaElevadora);
        btnEmpalme = view.findViewById(R.id.btnEmpalmeElevadora);
        btnTornillo = view.findViewById(R.id.btnTornillo);
        btnPuertaInspeccion = view.findViewById(R.id.btnPuertasInspeccionElevadora);
        btnSeguridad = view.findViewById(R.id.btnSeguridadElevadora);
        btnCondicionesCarga = view.findViewById(R.id.btnCondicionesCargaElevadora);
        dialogCarga=new SpotsDialog(getContext(), "Ejecutando Registro");

    }

    private void dialogBanda() {

        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_banda_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextInputEditText txtAnchoBanda=dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
        final TextInputEditText txtEspesorTotal=dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
        final TextInputEditText txtEspCubSup=dialogParte.findViewById(R.id.txtCubSupElevadora);
        final TextInputEditText txtEspCubInf=dialogParte.findViewById(R.id.txtCubInfElevadora);
        final TextInputEditText txtEspCojin=dialogParte.findViewById(R.id.txtEspesorCojin);
        final TextInputEditText txtVelocidadBanda=dialogParte.findViewById(R.id.txtVelocidadBandaElevadora);
        final TextInputEditText txtAnchoBandaAnterior=dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
        final TextInputEditText txtEspesorTotalAnterior=dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
        final TextInputEditText txtEspCubSupAnterior=dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
        final TextInputEditText txtEspCubInfAnterior=dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
        final TextInputEditText txtEspCojinAnterior=dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
        final TextInputEditText txtVelocidadBandaAnterior=dialogParte.findViewById(R.id.txtVelocidadBandaElevadoraAnterior);
        final TextInputEditText txtCausaFallaBandaAnterior=dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);
        final TextInputEditText txtDistanciaEntrePoleas =dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
        final TextInputEditText txtTonsTransportadasBandaAnterior =dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);

        final Spinner spinnerMarcaBanda=dialogParte.findViewById(R.id.spinnerMarcaBandaElevadora);
        final Spinner spinnerNoLonas=dialogParte.findViewById(R.id.spinnerNoLonasElevadora);
        final Spinner spinnerTipoLona=dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadora);
        final Spinner spinnerTipoCubierta=dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadora);
        final Spinner spinnerTipoEmpalme= dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadora);
        final Spinner spinnerEstadoEmpalme=dialogParte.findViewById(R.id.spinnerEstadoEmpalmeElevadora);
        final Spinner spinnerResistenciaRotura=dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadora);
        final Spinner spinnerMarcaBandaAnterior=dialogParte.findViewById(R.id.spinnerMarcaBandaElevadoraAnterior);
        final Spinner spinnerNoLonasAnterior=dialogParte.findViewById(R.id.spinnerNoLonasElevadoraAnterior);
        final Spinner spinnerTipoLonaAnterior=dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadoraAnterior);
        final Spinner spinnerTipoCubiertaAnterior=dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadoraAnterior);
        final Spinner spinnerTipoEmpalmeAnterior= dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadoraAnterior);
        final Spinner spinnerResistenciaRoturaAnterior=dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadoraAnterior);



        ArrayAdapter<String> adapterMarcaBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaBanda);
        spinnerMarcaBanda.setAdapter(adapterMarcaBanda);
        spinnerMarcaBandaAnterior.setAdapter(adapterMarcaBanda);

        ArrayAdapter<String> adapterNoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noLonas);
        spinnerNoLonas.setAdapter(adapterNoLonas);
        spinnerNoLonasAnterior.setAdapter(adapterNoLonas);

        ArrayAdapter<String> adapterTipoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoLona);
        spinnerTipoLona.setAdapter(adapterTipoLonas);
        spinnerTipoLonaAnterior.setAdapter(adapterTipoLonas);

        ArrayAdapter<String> adapterTipoCubierta= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCubierta);
        spinnerTipoCubierta.setAdapter(adapterTipoCubierta);
        spinnerTipoCubiertaAnterior.setAdapter(adapterTipoCubierta);

        ArrayAdapter<String> adapterTipoEmpalme= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoEmpalme);
        spinnerTipoEmpalme.setAdapter(adapterTipoEmpalme);
        spinnerTipoEmpalmeAnterior.setAdapter(adapterTipoEmpalme);

        ArrayAdapter<String> adapterEstadoEmpalme= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoEmpalme.setAdapter(adapterEstadoEmpalme);


        ArrayAdapter<String> adapterRoturaLona=new ArrayAdapter(getContext(),R.layout.estilo_spinner,Constants.resistenciaRoturaLona);
        spinnerResistenciaRotura.setAdapter(adapterRoturaLona);
        spinnerResistenciaRoturaAnterior.setAdapter(adapterRoturaLona);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Banda");
        }

        txtAnchoBanda.setOnFocusChangeListener(this);
        txtEspesorTotal.setOnFocusChangeListener(this);
        txtEspCubSup.setOnFocusChangeListener(this);
        txtEspCubInf.setOnFocusChangeListener(this);
        txtEspCojin.setOnFocusChangeListener(this);
        txtVelocidadBanda.setOnFocusChangeListener(this);

        txtAnchoBandaAnterior.setOnFocusChangeListener(this);
        txtEspesorTotalAnterior.setOnFocusChangeListener(this);
        txtEspCubSupAnterior.setOnFocusChangeListener(this);
        txtEspCubInfAnterior.setOnFocusChangeListener(this);
        txtEspCojinAnterior.setOnFocusChangeListener(this);
        txtVelocidadBandaAnterior.setOnFocusChangeListener(this);


        Button btnEnviarRegistro=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroBandaElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("marcaBandaElevadora", spinnerMarcaBanda.getSelectedItem().toString());
                                                    params.put("anchoBandaElevadora", txtAnchoBanda.getText().toString());
                                                    params.put("noLonaBandaElevadora", spinnerNoLonas.getSelectedItem().toString());
                                                    params.put("distanciaEntrePoleasElevadora", txtDistanciaEntrePoleas.getText().toString());
                                                    params.put("tipoLonaBandaElevadora", spinnerTipoLona.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaElevadora", txtEspesorTotal.getText().toString());
                                                    params.put("espesorCojinActualElevadora", txtEspCojin.getText().toString());
                                                    params.put("espesorCubiertaSuperiorElevadora", txtEspCubSup.getText().toString());
                                                    params.put("espesorCubiertaInferiorElevadora", txtEspCubInf.getText().toString());
                                                    params.put("tipoCubiertaElevadora", spinnerTipoCubierta.getSelectedItem().toString());
                                                    params.put("tipoEmpalmeElevadora", spinnerTipoEmpalme.getSelectedItem().toString());
                                                    params.put("estadoEmpalmeElevadora", spinnerEstadoEmpalme.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaLonaElevadora", spinnerResistenciaRotura.getSelectedItem().toString());
                                                    params.put("velocidadBandaElevadora", txtVelocidadBanda.getText().toString());
                                                    params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getSelectedItem().toString());
                                                    params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                                                    params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                                    params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                                                    params.put("txtEspCubSupAnterior", txtEspCubSupAnterior.getText().toString());
                                                    params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                                                    params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                                                    params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                                    params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                                    params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                                                    params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                                                    params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());

                                                    if(!txtAnchoBanda.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                                    }
                                                    if(!txtDistanciaEntrePoleas.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                                                    }
                                                    if(!txtEspesorTotal.getText().toString().equals(""))
                                                    {
                                                        params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                                    }
                                                    if(!txtEspCojin.getText().toString().equals(""))
                                                    {
                                                        params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                                    }
                                                    if(!txtEspCubSup.getText().toString().equals(""))
                                                    {
                                                        params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                                    }
                                                    if(!txtEspCubInf.getText().toString().equals(""))
                                                    {
                                                        params.put("txtEspCubInf", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                                    }
                                                    if(!txtVelocidadBanda.getText().toString().equals(""))
                                                    {
                                                        params.put("txtVelocidadBanda", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                                                    }
                                                    if(!txtAnchoBandaAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtAnchoBandaAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                                    }
                                                    if(!txtEspesorTotalAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                                    }
                                                    if(!txtEspCubSupAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtEspCubSupAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                                    }
                                                    if(!txtEspCojinAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtEspCojinAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                                    }
                                                    if(!txtEspCubInfAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtEspCubInfAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                                    }
                                                    if(!txtTonsTransportadasBandaAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtTonsTransportadasBandaAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                                                    }

                                                    if(!txtVelocidadBandaAnterior.getText().toString().equals(""))
                                                    {
                                                        params.put("txtVelocidadBandaAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarBandaElevadora";
                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("marcaBandaElevadora", spinnerMarcaBanda.getSelectedItem().toString());
                                                    params.put("anchoBandaElevadora", txtAnchoBanda.getText().toString());
                                                    params.put("noLonaBandaElevadora", spinnerNoLonas.getSelectedItem().toString());
                                                    params.put("distanciaEntrePoleasElevadora", txtDistanciaEntrePoleas.getText().toString());
                                                    params.put("tipoLonaBandaElevadora", spinnerTipoLona.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaElevadora", txtEspesorTotal.getText().toString());
                                                    params.put("espesorCojinActualElevadora", txtEspCojin.getText().toString());
                                                    params.put("espesorCubiertaSuperiorElevadora", txtEspCubSup.getText().toString());
                                                    params.put("espesorCubiertaInferiorElevadora", txtEspCubInf.getText().toString());
                                                    params.put("tipoCubiertaElevadora", spinnerTipoCubierta.getSelectedItem().toString());
                                                    params.put("tipoEmpalmeElevadora", spinnerTipoEmpalme.getSelectedItem().toString());
                                                    params.put("estadoEmpalmeElevadora", spinnerEstadoEmpalme.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaLonaElevadora", spinnerResistenciaRotura.getSelectedItem().toString());
                                                    params.put("velocidadBandaElevadora", txtVelocidadBanda.getText().toString());
                                                    params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getSelectedItem().toString());
                                                    params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                                                    params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                                    params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                                                    params.put("anchoBandaElevadoraAnterior", txtEspCubSupAnterior.getText().toString());
                                                    params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                                                    params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                                                    params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                                    params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                                    params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                                                    params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                                                    params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());

                                    if(!txtAnchoBanda.getText().toString().equals(""))
                                    {
                                        params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                    }
                                    if(!txtDistanciaEntrePoleas.getText().toString().equals(""))
                                    {
                                        params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                                    }
                                    if(!txtEspesorTotal.getText().toString().equals(""))
                                    {
                                        params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                    }
                                    if(!txtEspCojin.getText().toString().equals(""))
                                    {
                                        params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                    }
                                    if(!txtEspCubSup.getText().toString().equals(""))
                                    {
                                        params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                    }
                                    if(!txtEspCubInf.getText().toString().equals(""))
                                    {
                                        params.put("espesorCubiertaInferiorElevadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                    }
                                    if(!txtVelocidadBanda.getText().toString().equals(""))
                                    {
                                        params.put("velocidadBandaElevadora", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                                    }
                                    if(!txtAnchoBandaAnterior.getText().toString().equals(""))
                                    {
                                        params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                    }
                                    if(!txtEspesorTotalAnterior.getText().toString().equals(""))
                                    {
                                        params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                    }
                                    if(!txtEspCubSupAnterior.getText().toString().equals(""))
                                    {
                                        params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                    }
                                    if(!txtEspCojinAnterior.getText().toString().equals(""))
                                    {
                                        params.put("espesorCojinElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                    }
                                    if(!txtEspCubInfAnterior.getText().toString().equals(""))
                                    {
                                        params.put("espesorCubiertaInferiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                    }
                                    if(!txtTonsTransportadasBandaAnterior.getText().toString().equals(""))
                                    {
                                        params.put("tonsTransportadasBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                                    }
                                    if(!txtVelocidadBandaAnterior.getText().toString().equals(""))
                                    {
                                        params.put("velocidadBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
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

        dialogParte.setCancelable(true);
        dialogParte.show();

    }


    private void abrirDialogCangilon() {

        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_cangilon_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvLongitudCangilon = dialogParte.findViewById(R.id.tvLongitudCangilon);
        TextView tvProyeccionCangilon = dialogParte.findViewById(R.id.tvProyeccioCangilon);
        TextView tvProfundidadCangilon= dialogParte.findViewById(R.id.tvProfundidadCangilon);

        TextView tvDistBordesBanda=dialogParte.findViewById(R.id.tvDistBordesBanda);
        TextView tvDistPosteriorBanda=dialogParte.findViewById(R.id.tvDistPosteriorBanda);
        TextView tvDistBordesCangilon = dialogParte.findViewById(R.id.tvDistBordesCangilon);
        TextView tvDistLabioFrontal=dialogParte.findViewById(R.id.tvDistLabioFrontal);
        TextView tvTipoVentilacion= dialogParte.findViewById(R.id.tvTipoVentilacion);


        tvLongitudCangilon.setOnClickListener(this);
        tvProfundidadCangilon.setOnClickListener(this);
        tvProyeccionCangilon.setOnClickListener(this);

        tvDistBordesBanda.setOnClickListener(this);
        tvDistBordesCangilon.setOnClickListener(this);
        tvDistPosteriorBanda.setOnClickListener(this);
        tvDistLabioFrontal.setOnClickListener(this);

        tvTipoVentilacion.setOnClickListener(this);

        final Spinner spinnerMaterialCangilon = dialogParte.findViewById(R.id.spinnerMaterialCangilon);
        final Spinner spinnerMarcaCangilon = dialogParte.findViewById(R.id.spinnerMarcaCangilon);
        final TextInputEditText spinnerReferenciaCangilon = dialogParte.findViewById(R.id.txtReferenciaCangilon);
        final Spinner spinnerNoFilasCangilon=dialogParte.findViewById(R.id.spinnerNoFilasCangilones);
        final Spinner spinnerNoAgujeros = dialogParte.findViewById(R.id.spinnerNoAgujeros);
        final Spinner spinnerTipoVentilacion = dialogParte.findViewById(R.id.spinnerTipoVentilacion);
        final Spinner spinnerTipoCangilon =dialogParte.findViewById(R.id.spinnerTipoCangilon);

        final TextInputEditText txtPesoMaterialCangilon = dialogParte.findViewById(R.id.txtPesoMaterialCangilon);
        final TextInputEditText txtPesoCangilonVacio = dialogParte.findViewById(R.id.txtPesoCangilonVacio);
        final TextInputEditText txtLongitudCangilon = dialogParte.findViewById(R.id.txtLongitudCangilon);
        final TextInputEditText txtProyeccionCangilon = dialogParte.findViewById(R.id.txtProyeccionCangilon);
        final TextInputEditText txtProfundidadCangilon = dialogParte.findViewById(R.id.txtProfundidadCangilon);
        final TextInputEditText txtCapacidadCangilon = dialogParte.findViewById(R.id.txtCapacidadCangilon);
        final TextInputEditText txtSeparacionEntreCangilones = dialogParte.findViewById(R.id.txtSeparacionCangilones);
        final TextInputEditText txtDistanciaBordesBandaEstructura = dialogParte.findViewById(R.id.txtDistBordesEstructura);
        final TextInputEditText txtDistanciaPosteriorBandaEstructura = dialogParte.findViewById(R.id.txtDistPostBandaEstructura);
        final TextInputEditText txtDistLabioFrontalCangilon = dialogParte.findViewById(R.id.txtLabioFrontalCangilonBanda);
        final TextInputEditText txtDistBordesCangilonEstructura = dialogParte.findViewById(R.id.txtDistBordesCangilonEstructura);


        ArrayAdapter<String> adapterMaterialCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialCangilon);
        spinnerMaterialCangilon.setAdapter(adapterMaterialCangilon);

        ArrayAdapter<String> adapterMarcaCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaCangilon);
        spinnerMarcaCangilon.setAdapter(adapterMarcaCangilon);

        ArrayAdapter<String> adapterReferenciaCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaCangilon);


        ArrayAdapter<String> adapterNoFilasCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noFilasCangilon);
        spinnerNoFilasCangilon.setAdapter(adapterNoFilasCangilon);

        ArrayAdapter<String> adapterNoAgujeros = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noAgujerosCangilon);
        spinnerNoAgujeros.setAdapter(adapterNoAgujeros);

        ArrayAdapter<String> adapterTipoVentilacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoVentilacion);
        spinnerTipoVentilacion.setAdapter(adapterTipoVentilacion);

        ArrayAdapter<String> adapterTipoCangilon = new ArrayAdapter(getContext(),R.layout.estilo_spinner,Constants.tipoCangilon);
        spinnerTipoCangilon.setAdapter(adapterTipoCangilon);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Cangilon");
        }


        Button btnEnviar=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);




        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroCangilon";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("longitudCangilon", txtLongitudCangilon.getText().toString());
                                                    params.put("proyeccionCangilon", txtProyeccionCangilon.getText().toString());
                                                    params.put("profundidadCangilon", txtProfundidadCangilon.getText().toString());
                                                    params.put("materialCangilon", spinnerMaterialCangilon.getSelectedItem().toString());
                                                    params.put("pesoMaterialEnCadaCangilon", txtPesoMaterialCangilon.getText().toString());
                                                    params.put("pesoCangilonVacio", txtPesoCangilonVacio.getText().toString());
                                                    params.put("marcaCangilon", spinnerMarcaCangilon.getSelectedItem().toString());
                                                    params.put("referenciaCangilon", spinnerReferenciaCangilon.getText().toString());
                                                    params.put("capacidadCangilon", txtCapacidadCangilon.getText().toString());
                                                    params.put("noFilasCangilones", spinnerNoFilasCangilon.getSelectedItem().toString());
                                                    params.put("separacionCangilones", txtSeparacionEntreCangilones.getText().toString());
                                                    params.put("noAgujeros", spinnerNoAgujeros.getSelectedItem().toString());
                                                    params.put("distanciaBordeBandaEstructura", txtDistanciaBordesBandaEstructura.getText().toString());
                                                    params.put("distanciaPosteriorBandaEstructura", txtDistanciaPosteriorBandaEstructura.getText().toString());
                                                    params.put("distanciaLaboFrontalCangilonEstructura", txtDistLabioFrontalCangilon.getText().toString());
                                                    params.put("distanciaBordesCangilonEstructura", txtDistBordesCangilonEstructura.getText().toString());
                                                    params.put("tipoVentilacion", spinnerTipoVentilacion.getSelectedItem().toString());
                                                    params.put("tipoCangilon", spinnerTipoCangilon.getSelectedItem().toString());
                                                    if(!txtLongitudCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                                                    }
                                                    if(!txtProyeccionCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                                                    }
                                                    if(!txtProfundidadCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                                                    }
                                                    if(!txtPesoMaterialCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                                                    }
                                                    if(!txtPesoCangilonVacio.getText().toString().equals(""))
                                                    {
                                                        params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                                                    }
                                                    if(!txtCapacidadCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                                                    }
                                                    if(!txtSeparacionEntreCangilones.getText().toString().equals(""))
                                                    {
                                                        params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                                                    }
                                                    if(!txtDistanciaBordesBandaEstructura.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                                                    }
                                                    if(!txtDistanciaPosteriorBandaEstructura.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                                                    }
                                                    if(!txtDistBordesCangilonEstructura.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                                                    }
                                                    if(!txtDistLabioFrontalCangilon.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarCangilon";
                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                    params.put("longitudCangilon", txtLongitudCangilon.getText().toString());
                                    params.put("proyeccionCangilon", txtProyeccionCangilon.getText().toString());
                                    params.put("profundidadCangilon", txtProfundidadCangilon.getText().toString());
                                    params.put("materialCangilon", spinnerMaterialCangilon.getSelectedItem().toString());
                                    params.put("pesoMaterialEnCadaCangilon", txtPesoMaterialCangilon.getText().toString());
                                    params.put("pesoCangilonVacio", txtPesoCangilonVacio.getText().toString());
                                    params.put("marcaCangilon", spinnerMarcaCangilon.getSelectedItem().toString());
                                    params.put("referenciaCangilon", spinnerReferenciaCangilon.getText().toString());
                                    params.put("capacidadCangilon", txtCapacidadCangilon.getText().toString());
                                    params.put("noFilasCangilones", spinnerNoFilasCangilon.getSelectedItem().toString());
                                    params.put("separacionCangilones", txtSeparacionEntreCangilones.getText().toString());
                                    params.put("noAgujeros", spinnerNoAgujeros.getSelectedItem().toString());
                                    params.put("distanciaBordeBandaEstructura", txtDistanciaBordesBandaEstructura.getText().toString());
                                    params.put("distanciaPosteriorBandaEstructura", txtDistanciaPosteriorBandaEstructura.getText().toString());
                                    params.put("distanciaLaboFrontalCangilonEstructura", txtDistLabioFrontalCangilon.getText().toString());
                                    params.put("distanciaBordesCangilonEstructura", txtDistBordesCangilonEstructura.getText().toString());
                                    params.put("tipoVentilacion", spinnerTipoVentilacion.getSelectedItem().toString());
                                    params.put("tipoCangilon", spinnerTipoCangilon.getSelectedItem().toString());

                                    if(!txtLongitudCangilon.getText().toString().equals(""))
                                    {
                                        params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                                    }
                                    if(!txtProyeccionCangilon.getText().toString().equals(""))
                                    {
                                        params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                                    }
                                    if(!txtProfundidadCangilon.getText().toString().equals(""))
                                    {
                                        params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                                    }
                                    if(!txtPesoMaterialCangilon.getText().toString().equals(""))
                                    {
                                        params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                                    }
                                    if(!txtPesoCangilonVacio.getText().toString().equals(""))
                                    {
                                        params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                                    }
                                    if(!txtCapacidadCangilon.getText().toString().equals(""))
                                    {
                                        params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                                    }
                                    if(!txtSeparacionEntreCangilones.getText().toString().equals(""))
                                    {
                                        params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                                    }
                                    if(!txtDistanciaBordesBandaEstructura.getText().toString().equals(""))
                                    {
                                        params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                                    }
                                    if(!txtDistanciaPosteriorBandaEstructura.getText().toString().equals(""))
                                    {
                                        params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                                    }
                                    if(!txtDistBordesCangilonEstructura.getText().toString().equals(""))
                                    {
                                        params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                                    }
                                    if(!txtDistLabioFrontalCangilon.getText().toString().equals(""))
                                    {
                                        params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
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




    }

    public void abrirDialogPoleaMotriz()
    {
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_motriz_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvDiametroPolea=dialogParte.findViewById(R.id.tvDiametroPoleaMotrizElevadora);
        TextView tvAnchoPolea=dialogParte.findViewById(R.id.tvAnchoPoleaMotrizElevadora);
        TextView tvLargoEje=dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizElevadora);
        TextView tvDiametroEje=dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizElevadora);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
        final Spinner spinnerBandaCentradaEnPolea=dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
        final Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizElevadora);
        final TextInputEditText txtDiametroPoleaMotriz = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
        final TextInputEditText txtAnchoPoleaMotriz = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
        final TextInputEditText txtLargoEjePoleaMotriz = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
        final TextInputEditText txtDiametroEjePoleaMotriz = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
        final TextInputEditText txtPotenciaMotor = dialogParte.findViewById(R.id.txtPotenciaMotorElevadora);
        final TextInputEditText txtRPMSalidaReductor = dialogParte.findViewById(R.id.txtRpmSalidaReductorPoleaMotriz);



        ArrayAdapter<String> adapterTipoPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        spinnerTipoPolea.setAdapter(adapterTipoPolea);

        ArrayAdapter<String> adapterBandaCentrada=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);

        ArrayAdapter<String> adapterEstadoRvto=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvto.setAdapter(adapterEstadoRvto);

        ArrayAdapter<String> adapterGuardaPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerGuardaPolea.setAdapter(adapterGuardaPolea);


        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Polea Motriz");
        }

        tvAnchoPolea.setOnClickListener(this);
        tvDiametroEje.setOnClickListener(this);
        tvDiametroPolea.setOnClickListener(this);
        tvLargoEje.setOnClickListener(this);

        Button btnEnviarInformacion=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroPoleaMotrizElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("guardaReductorPoleaMotrizElevadora", spinnerGuardaPolea.getSelectedItem().toString());
                                                    params.put("bandaCentradaEnPoleaMotrizElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaMotrizElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                    params.put("tipoPoleaMotrizElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                                    params.put("largoEjeMotrizElevadora", txtLargoEjePoleaMotriz.getText().toString());
                                                    params.put("diametroEjeMotrizElevadora", txtDiametroEjePoleaMotriz.getText().toString());
                                                    params.put("potenciaMotorMotrizElevadora", txtPotenciaMotor.getText().toString());
                                                    params.put("diametroPoleaMotrizElevadora", txtDiametroPoleaMotriz.getText().toString());
                                                    params.put("anchoPoleaMotrizElevadora", txtAnchoPoleaMotriz.getText().toString());
                                                    params.put("rpmSalidaReductorMotrizElevadora", txtRPMSalidaReductor.getText().toString());

                                                    if(!txtLargoEjePoleaMotriz.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjePoleaMotriz.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjeMotrizElevadora",  String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                                                    }
                                                    if(!txtPotenciaMotor.getText().toString().equals(""))
                                                    {
                                                        params.put("potenciaMotorMotrizElevadora",  String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                                    }
                                                    if(!txtDiametroPoleaMotriz.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroPoleaMotrizElevadora",  String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                                    }
                                                    if(!txtAnchoPoleaMotriz.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaMotrizElevadora",  String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                                    }
                                                    if(!txtRPMSalidaReductor.getText().toString().equals(""))
                                                    {
                                                        params.put("rpmSalidaReductorMotrizElevadora",  String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarRegistro";
                            StringRequest requestRegistro=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    String url=Constants.url+"actualizarPoleaMotrizElevadora";
                                    StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                            params.put("guardaReductorPoleaMotrizElevadora", spinnerGuardaPolea.getSelectedItem().toString());
                                            params.put("bandaCentradaEnPoleaMotrizElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                            params.put("estadoRevestimientoPoleaMotrizElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                            params.put("tipoPoleaMotrizElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                            params.put("largoEjeMotrizElevadora", txtLargoEjePoleaMotriz.getText().toString());
                                            params.put("diametroEjeMotrizElevadora", txtDiametroEjePoleaMotriz.getText().toString());
                                            params.put("potenciaMotorMotrizElevadora", txtPotenciaMotor.getText().toString());
                                            params.put("diametroPoleaMotrizElevadora", txtDiametroPoleaMotriz.getText().toString());
                                            params.put("anchoPoleaMotrizElevadora", txtAnchoPoleaMotriz.getText().toString());
                                            params.put("rpmSalidaReductorMotrizElevadora", txtRPMSalidaReductor.getText().toString());

                                            if(!txtLargoEjePoleaMotriz.getText().toString().equals(""))
                                            {
                                                params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                                            }
                                            if(!txtDiametroEjePoleaMotriz.getText().toString().equals(""))
                                            {
                                                params.put("diametroEjeMotrizElevadora",  String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                                            }
                                            if(!txtPotenciaMotor.getText().toString().equals(""))
                                            {
                                                params.put("potenciaMotorMotrizElevadora",  String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                            }
                                            if(!txtDiametroPoleaMotriz.getText().toString().equals(""))
                                            {
                                                params.put("diametroPoleaMotrizElevadora",  String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                            }
                                            if(!txtAnchoPoleaMotriz.getText().toString().equals(""))
                                            {
                                                params.put("anchoPoleaMotrizElevadora",  String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                            }
                                            if(!txtRPMSalidaReductor.getText().toString().equals(""))
                                            {
                                                params.put("rpmSalidaReductorMotrizElevadora",  String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestRegistro);
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

    }

    public void abrirDialogPoleaCola()
    {
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_cola_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvDiametroPolea=dialogParte.findViewById(R.id.tvDiametroPoleaMotrizElevadora);
        TextView tvAnchoPolea=dialogParte.findViewById(R.id.tvAnchoPoleaMotrizElevadora);
        TextView tvLargoEje=dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizElevadora);
        TextView tvDiametroEje=dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizElevadora);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
        final Spinner spinnerBandaCentradaEnPolea=dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);

        final TextInputEditText txtDiametroPoleaCola = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
        final TextInputEditText txtAnchoPoleaCola = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
        final TextInputEditText txtLargoEjePoleaCola = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
        final TextInputEditText txtDiametroEjePoleaCola = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
        final TextInputEditText txtLongTensorTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloElevadora);
        final TextInputEditText txtLongRecorridoContraPesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaColaElevadora);


        ArrayAdapter<String> adapterTipoPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        spinnerTipoPolea.setAdapter(adapterTipoPolea);

        ArrayAdapter<String> adapterBandaCentrada=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);

        ArrayAdapter<String> adapterEstadoRvto=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvto.setAdapter(adapterEstadoRvto);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Polea Cola");
        }


        tvAnchoPolea.setOnClickListener(this);
        tvDiametroEje.setOnClickListener(this);
        tvDiametroPolea.setOnClickListener(this);
        tvLargoEje.setOnClickListener(this);

        txtDiametroPoleaCola.setOnFocusChangeListener(this);
        txtAnchoPoleaCola.setOnFocusChangeListener(this);
        txtLargoEjePoleaCola.setOnFocusChangeListener(this);
        txtDiametroEjePoleaCola.setOnFocusChangeListener(this);



        Button btnEnviarInformacion=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroPoleaColaElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                    params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                                    params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                                                    params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                                                    params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                                                    params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                                                    params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                                                    params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());

                                                    if(!txtLargoEjePoleaCola.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjePoleaCola.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjeColaElevadora",  String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                                                    }
                                                    if(!txtLongRecorridoContraPesa.getText().toString().equals(""))
                                                    {
                                                        params.put("longitudRecorridoContrapesaPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                                                    }
                                                    if(!txtDiametroPoleaCola.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                                                    }
                                                    if(!txtAnchoPoleaCola.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                                                    }
                                                    if(!txtLongTensorTornillo.getText().toString().equals(""))
                                                    {
                                                        params.put("longitudTensorTornilloPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarRegistro";
                            StringRequest requestRegistro=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    String url=Constants.url+"actualizarPoleaColaElevadora";
                                    StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                            params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                            params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                            params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                            params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                                            params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                                            params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                                            params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                                            params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());

                                            if(!txtLargoEjePoleaCola.getText().toString().equals(""))
                                            {
                                                params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                                            }
                                            if(!txtDiametroEjePoleaCola.getText().toString().equals(""))
                                            {
                                                params.put("diametroEjePoleaColaElevadora",  String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                                            }
                                            if(!txtLongRecorridoContraPesa.getText().toString().equals(""))
                                            {
                                                params.put("longitudRecorridoContrapesaPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                                            }
                                            if(!txtDiametroPoleaCola.getText().toString().equals(""))
                                            {
                                                params.put("diametroPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                                            }
                                            if(!txtAnchoPoleaCola.getText().toString().equals(""))
                                            {
                                                params.put("anchoPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                                            }
                                            if(!txtLongTensorTornillo.getText().toString().equals(""))
                                            {
                                                params.put("longitudTensorTornilloPoleaColaElevadora",  String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestRegistro);
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

    }

    public  void abrirDialogEmpalme()
    {
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_empalme_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final Spinner spinnerEmpalmeMecanico=dialogParte.findViewById(R.id.spinnerEmpalmeMecanicoElevadora);
        final TextInputEditText txtCargaTrabajo=dialogParte.findViewById(R.id.txtCargaTrabajoBandaElevadora);
        final TextInputEditText txtTmperaturaMaterialElevadora=dialogParte.findViewById(R.id.txtTempMaterialElevadora);
        ArrayAdapter<String> adapterEmpalmeMecanico=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerEmpalmeMecanico.setAdapter(adapterEmpalmeMecanico);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Empalme");
        }

        Button btnEnviar=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroEmpalmeElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogEmpalme");


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                                                    if(!txtCargaTrabajo.getText().toString().equals(""))
                                                    {
                                                        params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                                                    }
                                                    params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                                                    if(!txtTmperaturaMaterialElevadora.getText().toString().equals(""))
                                                    {
                                                        params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                                                    }
                                                    params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                            String url=Constants.url+"actualizarEmpalmeElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogEmpalme");


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                                                    if(!txtCargaTrabajo.getText().toString().equals(""))
                                                    {
                                                        params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                                                    }
                                                    params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                                                    if(!txtTmperaturaMaterialElevadora.getText().toString().equals(""))
                                                    {
                                                        params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                                                    }
                                                    params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());
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
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
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





    }

    public void abrirDialogTornillo()
    {
        dialogParte=new Dialog(getContext());
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_tornillos_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();
        Constants.llenarLargoTornillo();
        TextView tvDiametroRosca = dialogParte.findViewById(R.id.tvDiametroTornillo);
        TextView tvLargoTornilo = dialogParte.findViewById(R.id.tvLargoTornillo);
        final Spinner spinnerDiametroRosca=dialogParte.findViewById(R.id.spinnerDiametroRosca);
        final Spinner spinnerLargoTornillo=dialogParte.findViewById(R.id.spinnerLargoTornillo);

        final Spinner spinnerMaterialTornillo=dialogParte.findViewById(R.id.spinnerMaterialTornillo);

        ArrayAdapter<String> adapterMaterialTornillos=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialTornillo);
        spinnerMaterialTornillo.setAdapter(adapterMaterialTornillos);

        ArrayAdapter<String> adapterDiametroRosca=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diametroRosca);
        spinnerDiametroRosca.setAdapter(adapterDiametroRosca);

        ArrayAdapter<String> adapterLargoTornillo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.largoTornillo);
        spinnerLargoTornillo.setAdapter(adapterLargoTornillo);




        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Tornillos");
        }

        tvDiametroRosca.setOnClickListener(this);
        tvLargoTornilo.setOnClickListener(this);

        Button btnEnviar=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);




        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();



                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroTornillosElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                                                    params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());


                                                    params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());
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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarTornillos";
                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                    params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                                    params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());

                                    params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());

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

    }

    public void abrirDialogPuertasInpeccion()
    {
        dialogParte=new Dialog(getContext());
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_puertas_inspeccion_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final TextInputEditText txtAnchoCabezaElevador=dialogParte.findViewById(R.id.txtAnchoCabezaElevador);
        final TextInputEditText txtLargoCabezaElevador=dialogParte.findViewById(R.id.txtLargoCabezaElevador);
        final TextInputEditText txtAnchoBotaElevador=dialogParte.findViewById(R.id.txtAnchoBotaElevador);
        final TextInputEditText txtLargoBotaElevador=dialogParte.findViewById(R.id.txtLargoBotaElevador);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Puertas Inspeccion");
        }

        Button btnEnviar =dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice");
                alerta.setMessage("¿Desea agregar éste registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestRegistro=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {


                                    String url = Constants.url + "maxRegistro";
                                    StringRequest requestMaxRegistro = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {

                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url = Constants.url + "registroPuertaInspeccion";
                                            StringRequest requestPuertaInspeccion=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogPuertasInspeccion");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                                                    params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                                                    params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                                                    params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                                                    if(!txtAnchoCabezaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                                                    }
                                                    if(!txtLargoCabezaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                                                    }
                                                    if (! txtAnchoBotaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                                                    }
                                                    if(!txtLargoBotaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("largoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoBotaElevador.getText().toString())));
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestPuertaInspeccion);
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarRegistro";
                            StringRequest requestRegistro=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {

                                            String url = Constants.url + "actualizarPuertaInspeccion";
                                            StringRequest requestPuertaInspeccion=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogPuertasInspeccion");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                                                    params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                                                    params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                                                    params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                                                    if(!txtAnchoCabezaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                                                    }
                                                    if(!txtLargoCabezaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                                                    }
                                                    if (! txtAnchoBotaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                                                    }
                                                    if(!txtLargoBotaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("largoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoBotaElevador.getText().toString())));
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestPuertaInspeccion);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    dialogCarga.dismiss();

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestRegistro);
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
    }

    public void abrirDialogSeguridad()
    {
        dialogParte=new Dialog(getContext());
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_seguridad_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final Spinner spinnerMonitorPeligro=dialogParte.findViewById(R.id.spinnerMonitorPeligro);
        final Spinner spinnerRodamiento=dialogParte.findViewById(R.id.spinnerRodamiento);
        final Spinner spinnerMonitorDesalineacion=dialogParte.findViewById(R.id.spinnerMonitorDesalineacion);
        final Spinner spinnerMonitorVelocidad=dialogParte.findViewById(R.id.spinnerMonitorVelocidad);
        final Spinner spinnerSensorInductivo=dialogParte.findViewById(R.id.spinnerSensorInductivo);
        final Spinner spinnerIndicadorDeNivel=dialogParte.findViewById(R.id.spinnerIndicadorDeNivel);
        final Spinner spinnerCajaDeUnion=dialogParte.findViewById(R.id.spinnerCajaDeUnion);
        final Spinner spinnerAlarmaYPantalla=dialogParte.findViewById(R.id.spinnerAlarmaYPantalla);
        final Spinner spinnerInterruptorSeguridad=dialogParte.findViewById(R.id.spinnerInterruptorDeSeguridad);

        ArrayAdapter<String> adapterMonitorPeligro=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorPeligro);
        spinnerMonitorPeligro.setAdapter(adapterMonitorPeligro);

        ArrayAdapter<String> adapterRodamiento=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.rodamiento);
        spinnerRodamiento.setAdapter(adapterRodamiento);

        ArrayAdapter<String> adapterMonitorDesalineacion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorDesalineacion);
        spinnerMonitorDesalineacion.setAdapter(adapterMonitorDesalineacion);

        ArrayAdapter<String> adapterMonitorVelocidad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorVelocidad);
        spinnerMonitorVelocidad.setAdapter(adapterMonitorVelocidad);

        ArrayAdapter<String> adapterSensorInductivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.sensorInductivo);
        spinnerSensorInductivo.setAdapter(adapterSensorInductivo);

        ArrayAdapter<String> adapterIndicadorNivel=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.indicadorDeNivel);
        spinnerIndicadorDeNivel.setAdapter(adapterIndicadorNivel);


        ArrayAdapter<String> adapterCajaUnion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.cajaUnion);
        spinnerCajaDeUnion.setAdapter(adapterCajaUnion);

        ArrayAdapter<String> adapterAlarmaPantalla=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.alarmaYPantalla);
        spinnerAlarmaYPantalla.setAdapter(adapterAlarmaPantalla);

        ArrayAdapter<String> adapterInterruptorSeguridad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.interruptorSeguridad);
        spinnerInterruptorSeguridad.setAdapter(adapterInterruptorSeguridad);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Seguridad");
        }

        Button btnEnviar=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);




        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroSeguridadElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("monitorPeligro", spinnerMonitorPeligro.getSelectedItem().toString());
                                                    params.put("rodamiento", spinnerRodamiento.getSelectedItem().toString());
                                                    params.put("monitorDesalineacion", spinnerMonitorDesalineacion.getSelectedItem().toString());
                                                    params.put("monitorVelocidad", spinnerMonitorVelocidad.getSelectedItem().toString());
                                                    params.put("sensorInductivo", spinnerSensorInductivo.getSelectedItem().toString());
                                                    params.put("indicadorNivel", spinnerIndicadorDeNivel.getSelectedItem().toString());
                                                    params.put("cajaUnion", spinnerCajaDeUnion.getSelectedItem().toString());
                                                    params.put("alarmaYPantalla", spinnerAlarmaYPantalla.getSelectedItem().toString());
                                                    params.put("interruptorSeguridad", spinnerInterruptorSeguridad.getSelectedItem().toString());

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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarRegistro";
                            StringRequest requestRegistro=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    String url=Constants.url+"actualizarSeguridadElevadora";
                                    StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                            params.put("monitorPeligro", spinnerMonitorPeligro.getSelectedItem().toString());
                                            params.put("rodamiento", spinnerRodamiento.getSelectedItem().toString());
                                            params.put("monitorDesalineacion", spinnerMonitorDesalineacion.getSelectedItem().toString());
                                            params.put("monitorVelocidad", spinnerMonitorVelocidad.getSelectedItem().toString());
                                            params.put("sensorInductivo", spinnerSensorInductivo.getSelectedItem().toString());
                                            params.put("indicadorNivel", spinnerIndicadorDeNivel.getSelectedItem().toString());
                                            params.put("cajaUnion", spinnerCajaDeUnion.getSelectedItem().toString());
                                            params.put("alarmaYPantalla", spinnerAlarmaYPantalla.getSelectedItem().toString());
                                            params.put("interruptorSeguridad", spinnerInterruptorSeguridad.getSelectedItem().toString());

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
                            }){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("fechaRegistro", fechaActual);
                                params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                return params;
                            }
                        };
                        queue.add(requestRegistro);
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




    }

    public void abrirDialogCondicionesCarga()
    {
        dialogParte=new Dialog(getContext());
        dialogParte= new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_condiciones_carga_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvTipoCarga = dialogParte.findViewById(R.id.tvTipoCarga);
        TextView tvTipoDescarga= dialogParte.findViewById(R.id.tvTipoDescarga);

        tvTipoCarga.setOnClickListener(this);
        tvTipoDescarga.setOnClickListener(this);
        Constants.llenar();
        Constants.llenarTempMin();
        Constants.llenarTempMax();

        final Spinner spinnerAtaqueQuimico=dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
        final Spinner spinnerAtaqueTemperatura=dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
        final Spinner spinnerAtaqueAceites=dialogParte.findViewById(R.id.spinnerAtaqueAceites);
        final Spinner spinnerAtaqueAbrasivo=dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
        final Spinner spinnerHorasTrabajoDia=dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
        final Spinner spinnerDiasTrabajoSemana=dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
        final Spinner spinnerAbrasividad=dialogParte.findViewById(R.id.spinnerAbrasividad);
        final Spinner spinnerPorcentajeFinos=dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
        final Spinner spinnerTipoCarga=dialogParte.findViewById(R.id.spinnerTipoCarga);
        final Spinner spinnerVariosPuntosAlimentacion=dialogParte.findViewById(R.id.spinnerVariosPuntosAlimentacion);
        final Spinner spinnerTipoDescarga=dialogParte.findViewById(R.id.spinnerTipoDescarga);
        final Spinner spinnerLluviaMaterial=dialogParte.findViewById(R.id.spinnerLluviaMaterial);
        final Spinner spinnerMaxGranulometria=dialogParte.findViewById(R.id.spinnerMaxGranulometria);


        final TextInputEditText txtMaterial= dialogParte.findViewById(R.id.txtMaterial);
        final TextInputEditText txtCapacidad= dialogParte.findViewById(R.id.txtCapacidadHorizontal);

        final TextInputEditText txtDensidad= dialogParte.findViewById(R.id.txtDensidadMaterial);
        final Spinner spinnerTempMaxMaterial= dialogParte.findViewById(R.id.spinnerTempMaxSobreBandaElevadora);
        final Spinner spinnerTempPromedioMaterial= dialogParte.findViewById(R.id.spinnerTempPromedioMaterialSobreBanda);
        final TextInputEditText txtAnchoPiernaElevador= dialogParte.findViewById(R.id.txtAnchoPiernaElevador);
        final TextInputEditText txtProfundidadPiernaElevador= dialogParte.findViewById(R.id.txtProfundidadPiernaElevador);
        final Spinner spinnerTempAmbienteMin= dialogParte.findViewById(R.id.spinnerTempAmbienteMin);
        final Spinner spinnerTempAmbienteMax= dialogParte.findViewById(R.id.spinnerTempAmbienteMax);



        ArrayAdapter<String> adapterAtaqueQuimico=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueQuimico.setAdapter(adapterAtaqueQuimico);

        ArrayAdapter<String> adapterAtaqueTemperatura=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueTemperatura.setAdapter(adapterAtaqueTemperatura);

        ArrayAdapter<String> adapterAtaqueAceites=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAceites.setAdapter(adapterAtaqueAceites);

        ArrayAdapter<String> adapterAtaqueAbrasivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAbrasivo.setAdapter(adapterAtaqueAbrasivo);

        ArrayAdapter<String> adapterSensorInductivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
        spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

        ArrayAdapter<String> adapterDiasSemana=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
        spinnerDiasTrabajoSemana.setAdapter(adapterDiasSemana);

        ArrayAdapter<String> adapterAbrasividad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
        spinnerAbrasividad.setAdapter(adapterAbrasividad);

        ArrayAdapter<String> adapterPorcentajeFinos=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
        spinnerPorcentajeFinos.setAdapter(adapterPorcentajeFinos);

        ArrayAdapter<String> adapterTipoCarga=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCarga);
        spinnerTipoCarga.setAdapter(adapterTipoCarga);

        ArrayAdapter<String> adapterVariosPuntosAlimentacion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerVariosPuntosAlimentacion.setAdapter(adapterVariosPuntosAlimentacion);

        ArrayAdapter<String> adapterTipoDescarga=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoDescarga);
        spinnerTipoDescarga.setAdapter(adapterTipoDescarga);

        ArrayAdapter<String> adapterLluviaMaterial=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerLluviaMaterial.setAdapter(adapterLluviaMaterial);

        ArrayAdapter<String> adapterTempMaterial=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
        spinnerTempMaxMaterial.setAdapter(adapterTempMaterial);
        spinnerTempPromedioMaterial.setAdapter(adapterTempMaterial);

        ArrayAdapter<String> adapterTempAmbienteMin=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMinAmbiente);
        spinnerTempAmbienteMin.setAdapter(adapterTempAmbienteMin);


        ArrayAdapter<String> adapterTempMaxAmbiente=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMaxAmbiente);
        spinnerTempAmbienteMax.setAdapter(adapterTempMaxAmbiente);

        ArrayAdapter<String> adapterMaxGranulometria=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.maxGranulometria);
        spinnerMaxGranulometria.setAdapter(adapterMaxGranulometria);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Condiciones Carga");
        }


        Button btnEnviar=dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);




        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        if(FragmentSeleccionarTransportador.bandera.equals("Nuevo"))
                        {
                            String url=Constants.url+"crearRegistro";
                            StringRequest requestCrearRegistro= new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String url=Constants.url+"maxRegistro";
                                    StringRequest requestMaxRegistro=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                            }.getType();
                                            idMaximaRegistro = gson.fromJson(response, type);

                                            String url=Constants.url+"registroCondicionesCargaElevadora";
                                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
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
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                                    params.put("materialElevadora", txtMaterial.getText().toString().toUpperCase());
                                                    params.put("ataqueQuimicoElevadora", spinnerAtaqueQuimico.getSelectedItem().toString());
                                                    params.put("ataqueTemperaturaElevadora", spinnerAtaqueTemperatura.getSelectedItem().toString());
                                                    params.put("ataqueAceitesElevadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                                    params.put("ataqueAbrasivoElevadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                                    params.put("horasTrabajoDiaElevadora", spinnerHorasTrabajoDia.getSelectedItem().toString());
                                                    params.put("capacidadElevadora", txtCapacidad.getText().toString());
                                                    params.put("diasTrabajoSemanaElevadora", spinnerDiasTrabajoSemana.getSelectedItem().toString());
                                                    params.put("porcentajeFinosElevadora", spinnerPorcentajeFinos.getSelectedItem().toString());
                                                    params.put("abrasividadElevadora", spinnerAbrasividad.getSelectedItem().toString());
                                                    params.put("maxGranulometriaElevadora", spinnerMaxGranulometria.getSelectedItem().toString());
                                                    params.put("densidadMaterialElevadora", txtDensidad.getText().toString());
                                                    params.put("tempMaxMaterialSobreBandaElevadora", spinnerTempMaxMaterial.getSelectedItem().toString());
                                                    params.put("tempPromedioMaterialSobreBandaElevadora", spinnerTempPromedioMaterial.getSelectedItem().toString());
                                                    params.put("variosPuntosDeAlimentacion", spinnerVariosPuntosAlimentacion.getSelectedItem().toString());
                                                    params.put("lluviaDeMaterial", spinnerLluviaMaterial.getSelectedItem().toString());
                                                    params.put("anchoPiernaElevador", txtAnchoPiernaElevador.getText().toString());
                                                    params.put("tempAmbienteMin", spinnerTempAmbienteMin.getSelectedItem().toString());
                                                    params.put("tempAmbienteMax", spinnerTempAmbienteMax.getSelectedItem().toString());
                                                    params.put("tipoCarga", spinnerTipoCarga.getSelectedItem().toString());
                                                    params.put("tipoDescarga", spinnerTipoDescarga.getSelectedItem().toString());
                                                    params.put("profundidadPiernaElevador", txtProfundidadPiernaElevador.getText().toString());

                                                    if(!txtDensidad.getText().toString().equals(""))
                                                    {
                                                        params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                                                    }
                                                    if(!txtAnchoPiernaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                                                    }

                                                    if(!txtProfundidadPiernaElevador.getText().toString().equals(""))
                                                    {
                                                        params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

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
                                    params.put("fechaRegistro", fechaActual);
                                    params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                    params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                    params.put("usuarioRegistro", Login.loginJsons.get(0).getNombreUsuario());

                                    return params;
                                }
                            };
                            queue.add(requestCrearRegistro);
                        }
                        else
                        {
                            String url=Constants.url+"actualizarCondicionesCargaElevadora";
                            StringRequest requestRegistroTornillos=new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
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
                            }){
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                                    params.put("materialElevadora", txtMaterial.getText().toString());
                                    params.put("ataqueQuimicoElevadora", spinnerAtaqueQuimico.getSelectedItem().toString());
                                    params.put("ataqueTemperaturaElevadora", spinnerAtaqueTemperatura.getSelectedItem().toString());
                                    params.put("ataqueAceitesElevadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                    params.put("ataqueAbrasivoElevadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                    params.put("horasTrabajoDiaElevadora", spinnerHorasTrabajoDia.getSelectedItem().toString());
                                    params.put("capacidadElevadora", txtCapacidad.getText().toString());
                                    params.put("diasTrabajoSemanaElevadora", spinnerDiasTrabajoSemana.getSelectedItem().toString());
                                    params.put("porcentajeFinosElevadora", spinnerPorcentajeFinos.getSelectedItem().toString());
                                    params.put("abrasividadElevadora", spinnerAbrasividad.getSelectedItem().toString());
                                    params.put("maxGranulometriaElevadora", spinnerMaxGranulometria.getSelectedItem().toString());
                                    params.put("densidadMaterialElevadora", txtDensidad.getText().toString());
                                    params.put("tempMaxSobreBandaElevadora", spinnerTempMaxMaterial.getSelectedItem().toString());
                                    params.put("tempPromedioMaterialSobreBandaElevadora", spinnerTempPromedioMaterial.getSelectedItem().toString());
                                    params.put("variosPuntosDeAlimentacion", spinnerVariosPuntosAlimentacion.getSelectedItem().toString());
                                    params.put("lluviaDeMaterial", spinnerLluviaMaterial.getSelectedItem().toString());
                                    params.put("anchoPiernaElevador", txtAnchoPiernaElevador.getText().toString());
                                    params.put("tempAmbienteMin", spinnerTempAmbienteMin.getSelectedItem().toString());
                                    params.put("tempAmbienteMax", spinnerTempAmbienteMax.getSelectedItem().toString());
                                    params.put("tipoCarga", spinnerTipoCarga.getSelectedItem().toString());
                                    params.put("tipoDescarga", spinnerTipoDescarga.getSelectedItem().toString());
                                    params.put("profundidadPiernaElevador", txtProfundidadPiernaElevador.getText().toString());

                                    if(!txtDensidad.getText().toString().equals(""))
                                    {
                                        params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                                    }
                                    if(!txtAnchoPiernaElevador.getText().toString().equals(""))
                                    {
                                        params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                                    }

                                    if(!txtProfundidadPiernaElevador.getText().toString().equals(""))
                                    {
                                        params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

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


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {


        switch (v.getId())
        {

            case R.id.txtDiametroPoleaMotrizElevadora:
                if(!hasFocus)
                {
                    EditText txtDiametroRosca=dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                    if(!txtDiametroRosca.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca.getText().toString());
                        if(numero<50|| numero >1524)
                        {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  60\" / 1524mm");
                        }
                    }
                }
            break;

            case R.id.txtAnchoPoleaMotrizElevadora:
            case R.id.txtDiametroEjePoleaMotrizElevadora:
                if(!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);

                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 2700) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                    if (!txtDiametroRosca1.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if (numero < 50 || numero > 2700) {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                }
                break;
            case R.id.txtAnchoBandaElevadora:
            case R.id.txtAnchoBandaElevadoraAnterior:
                if(!hasFocus)
                {
                    EditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
                    EditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
                if(!txtAnchoBanda.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtAnchoBanda.getText().toString());
                        if(numero<50|| numero >2700)
                        {
                            txtAnchoBanda.setText("");
                            txtAnchoBanda.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                    if(!txtAnchoBandaAnterior.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtAnchoBandaAnterior.getText().toString());
                        if(numero<50|| numero >2700)
                        {
                            txtAnchoBandaAnterior.setText("");
                            txtAnchoBandaAnterior.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                }
            break;

            case R.id.txtLargoEjePoleaMotrizElevadora:
                if(!hasFocus)
                {
                    EditText txtDiametroRosca=dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                    if(!txtDiametroRosca.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca.getText().toString());
                        if(numero<254|| numero >2700)
                        {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 10\" / 254mm -  106.3\" / 2700mm");
                        }
                    }
                }
            break;

            case R.id.txtEpesorTotalElevadora:
            case R.id.txtEpesorTotalElevadoraAnterior:
                if(!hasFocus)
                {
                    EditText txtDiametroRosca=dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
                    EditText txtDiametroRosca1=dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
                    if(!txtDiametroRosca.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca.getText().toString());
                        if(numero<1|| numero>40.6)
                        {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 1mm - 40.6mm");
                        }
                    }
                    if(!txtDiametroRosca1.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if(numero<1|| numero>41)
                        {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 1mm - 40.6mm");
                        }
                    }
                }
            break;

            case R.id.txtEspesorCojin:
            case R.id.txtEspesorCojinAnterior:
            case R.id.txtCubSupElevadora:
            case R.id.txtCubSupElevadoraAnterior:
            case R.id.txtCubInfElevadoraAnterior:
            case R.id.txtCubInfElevadora:
                if(!hasFocus)
                {
                    EditText txtDiametroRosca=dialogParte.findViewById(R.id.txtEspesorCojin);
                    EditText txtDiametroRosca1=dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
                    EditText txtDiametroRosca2=dialogParte.findViewById(R.id.txtCubSupElevadora);
                    EditText txtDiametroRosca3=dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
                    EditText txtDiametroRosca4=dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
                    EditText txtDiametroRosca5=dialogParte.findViewById(R.id.txtCubInfElevadora);
                    if(!txtDiametroRosca.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if(!txtDiametroRosca1.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if(!txtDiametroRosca2.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca2.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca2.setText("");
                            txtDiametroRosca2.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if(!txtDiametroRosca3.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca3.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca3.setText("");
                            txtDiametroRosca3.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if(!txtDiametroRosca4.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca4.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca4.setText("");
                            txtDiametroRosca4.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if(!txtDiametroRosca5.getText().toString().equals(""))
                    {
                        float numero=Float.parseFloat(txtDiametroRosca5.getText().toString());
                        if(numero<1|| numero>12)
                        {
                            txtDiametroRosca5.setText("");
                            txtDiametroRosca5.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                }
                break;

            case R.id.txtVelocidadBandaElevadora:
            case R.id.txtVelocidadBandaElevadoraAnterior:

                if(!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtVelocidadBandaElevadora);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtVelocidadBandaElevadoraAnterior);

                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 0.5 || numero > 8) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 0.5 m/s - 7 m/s");
                        }
                    }
                    if (!txtDiametroRosca1.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if (numero < 0.5 || numero > 8) {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 0.5 m/s - 7 m/s");
                        }
                    }
                }

                break;




        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnEnviarRegistroBandaElevadora:
             break;

            case R.id.tvLongitudCangilon:
            case R.id.tvProfundidadCangilon:
            case R.id.tvProyeccioCangilon:
                abrirDialogParte("cangilon");
                break;

            case R.id.tvDistBordesBanda:
            case R.id.tvDistPosteriorBanda:
            case R.id.tvDistLabioFrontal:
            case R.id.tvDistBordesCangilon:
                abrirDialogParte("Distancias Cangilon");
                break;

            case R.id.tvTipoVentilacion:
                abrirDialogParte("Tipo Ventilacion");
                break;

            case R.id.tvAnchoPoleaMotrizElevadora:
            case R.id.tvDiametroPoleaMotrizElevadora:
            case R.id.tvDiametroEjePoleaMotrizElevadora:
            case R.id.tvLargoEjePoleaMotrizElevadora:
                abrirDialogParte("dimensionesPoleaMotriz");
                break;

            case R.id.tvDiametroTornillo:
            case R.id.tvLargoTornillo:
                abrirDialogParte("Tornillos");
                break;

            case R.id.tvTipoCarga:
                abrirDialogParte("Tipo Carga");
                break;

            case R.id.tvTipoDescarga:
                abrirDialogParte("Tipo Descarga");
                break;
        }



    }


    public void abrirDialogParte(String parte)
    {
        Dialog dialogParte;
        ImageView imgParte;
        switch (parte)
        {
            case "cangilon":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
            break;

            case "Distancias Cangilon":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.distancias_cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
            break;

            case "Tipo Ventilacion":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.ventilacion_cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
            break;

            case "dimensionesPoleaMotriz":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.polea);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tornillos":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tornillo);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tipo Carga":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tipo_carga);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tipo Descarga":
                dialogParte=new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tipo_descarga);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;
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
                                    dialogParte.cancel();




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

    private void llenarRegistros(String parte) {
        switch (parte)
        {
            case "Tornillos":
                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            final Spinner spinnerDiametroRosca=dialogParte.findViewById(R.id.spinnerDiametroRosca);
                            final Spinner spinnerLargoTornillo=dialogParte.findViewById(R.id.spinnerLargoTornillo);

                            final Spinner spinnerMaterialTornillo=dialogParte.findViewById(R.id.spinnerMaterialTornillo);

                            ArrayAdapter<String> adapterMaterialTornillos=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialTornillo);
                            spinnerMaterialTornillo.setAdapter(adapterMaterialTornillos);

                            ArrayAdapter<String> adapterDiametroRosca=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diametroRosca);
                            spinnerDiametroRosca.setAdapter(adapterDiametroRosca);

                            ArrayAdapter<String> adapterLargoTornillo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.largoTornillo);
                            spinnerLargoTornillo.setAdapter(adapterLargoTornillo);

                            int posicion= adapterMaterialTornillos.getPosition(Login.loginJsons.get(i).getMaterialTornilloElevadora());
                            int posicion2= adapterMaterialTornillos.getPosition(Login.loginJsons.get(i).getDiametroRoscaElevadora());
                            int posicion3= adapterMaterialTornillos.getPosition(Login.loginJsons.get(i).getLargoTornilloElevadora());


                            spinnerMaterialTornillo.setSelection(posicion);
                            spinnerLargoTornillo.setSelection(posicion3);
                            spinnerDiametroRosca.setSelection(posicion2);
                            i=Login.loginJsons.size()+1;
                        }
                    }
                }
            break;

            case "Empalme":
                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            final Spinner spinnerEmpalmeMecanico=dialogParte.findViewById(R.id.spinnerEmpalmeMecanicoElevadora);
                            final TextInputEditText txtCargaTrabajo=dialogParte.findViewById(R.id.txtCargaTrabajoBandaElevadora);
                            final TextInputEditText txtTmperaturaMaterialElevadora=dialogParte.findViewById(R.id.txtTempMaterialElevadora);
                            ArrayAdapter<String> adapterEmpalmeMecanico=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);


                            txtCargaTrabajo.setText(Login.loginJsons.get(i).getCargaTrabajoBandaElevadora());
                            txtTmperaturaMaterialElevadora.setText(Login.loginJsons.get(i).getTemperaturaMaterialElevadora());

                            int posicion= adapterEmpalmeMecanico.getPosition(Login.loginJsons.get(i).getEmpalmeMecanicoElevadora());
                            spinnerEmpalmeMecanico.setSelection(posicion);
                            i=Login.loginJsons.size()+1;
                        }
                    }
                }
            break;

            case "Puertas Inspeccion":

                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            TextInputEditText txtAnchoCabezaElevador=dialogParte.findViewById(R.id.txtAnchoCabezaElevador);
                            TextInputEditText txtLargoCabezaElevador=dialogParte.findViewById(R.id.txtLargoCabezaElevador);
                            TextInputEditText txtAnchoBotaElevador=dialogParte.findViewById(R.id.txtAnchoBotaElevador);
                            TextInputEditText txtLargoBotaElevador=dialogParte.findViewById(R.id.txtLargoBotaElevador);


                            txtAnchoCabezaElevador.setText(Login.loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion());
                            txtLargoCabezaElevador.setText(Login.loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion());
                            txtAnchoBotaElevador.setText(Login.loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion());
                            txtLargoBotaElevador.setText(Login.loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion());

                            i=Login.loginJsons.size()+1;
                        }
                    }
                }
            break;


            case "Seguridad":
                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            Spinner spinnerMonitorPeligro=dialogParte.findViewById(R.id.spinnerMonitorPeligro);
                            Spinner spinnerRodamiento=dialogParte.findViewById(R.id.spinnerRodamiento);
                            Spinner spinnerMonitorDesalineacion=dialogParte.findViewById(R.id.spinnerMonitorDesalineacion);
                            Spinner spinnerMonitorVelocidad=dialogParte.findViewById(R.id.spinnerMonitorVelocidad);
                            Spinner spinnerSensorInductivo=dialogParte.findViewById(R.id.spinnerSensorInductivo);
                            Spinner spinnerIndicadorDeNivel=dialogParte.findViewById(R.id.spinnerIndicadorDeNivel);
                            Spinner spinnerCajaDeUnion=dialogParte.findViewById(R.id.spinnerCajaDeUnion);
                            Spinner spinnerAlarmaYPantalla=dialogParte.findViewById(R.id.spinnerAlarmaYPantalla);
                            Spinner spinnerInterruptorSeguridad=dialogParte.findViewById(R.id.spinnerInterruptorDeSeguridad);

                            ArrayAdapter<String> adapterMonitorPeligro=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorPeligro);
                            spinnerMonitorPeligro.setAdapter(adapterMonitorPeligro);

                            ArrayAdapter<String> adapterRodamiento=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.rodamiento);
                            spinnerRodamiento.setAdapter(adapterRodamiento);

                            ArrayAdapter<String> adapterMonitorDesalineacion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorDesalineacion);
                            spinnerMonitorDesalineacion.setAdapter(adapterMonitorDesalineacion);

                            ArrayAdapter<String> adapterMonitorVelocidad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorVelocidad);
                            spinnerMonitorVelocidad.setAdapter(adapterMonitorVelocidad);

                            ArrayAdapter<String> adapterSensorInductivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.sensorInductivo);
                            spinnerSensorInductivo.setAdapter(adapterSensorInductivo);

                            ArrayAdapter<String> adapterIndicadorNivel=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.indicadorDeNivel);
                            spinnerIndicadorDeNivel.setAdapter(adapterIndicadorNivel);


                            ArrayAdapter<String> adapterCajaUnion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.cajaUnion);
                            spinnerCajaDeUnion.setAdapter(adapterCajaUnion);

                            ArrayAdapter<String> adapterAlarmaPantalla=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.alarmaYPantalla);
                            spinnerAlarmaYPantalla.setAdapter(adapterAlarmaPantalla);

                            ArrayAdapter<String> adapterInterruptorSeguridad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.interruptorSeguridad);
                            spinnerInterruptorSeguridad.setAdapter(adapterInterruptorSeguridad);


                            int p1=adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getMonitorPeligro());
                            int p2= adapterRodamiento.getPosition(Login.loginJsons.get(i).getRodamiento());
                            int p3= adapterMonitorDesalineacion.getPosition(Login.loginJsons.get(i).getMonitorDesalineacion());
                            int p4= adapterMonitorVelocidad.getPosition(Login.loginJsons.get(i).getMonitorVelocidad());
                            int p5= adapterSensorInductivo.getPosition(Login.loginJsons.get(i).getSensorInductivo());
                            int p6= adapterIndicadorNivel.getPosition(Login.loginJsons.get(i).getIndicadorNivel());
                            int p7= adapterCajaUnion.getPosition(Login.loginJsons.get(i).getCajaUnion());
                            int p8= adapterAlarmaPantalla.getPosition(Login.loginJsons.get(i).getAlarmaYPantalla());
                            int p9= adapterInterruptorSeguridad.getPosition(Login.loginJsons.get(i).getInterruptorSeguridad());

                            spinnerMonitorPeligro.setSelection(p1);
                            spinnerRodamiento.setSelection(p2);
                            spinnerMonitorDesalineacion.setSelection(p3);
                            spinnerMonitorVelocidad.setSelection(p4);
                            spinnerSensorInductivo.setSelection(p5);
                            spinnerIndicadorDeNivel.setSelection(p6);
                            spinnerCajaDeUnion.setSelection(p7);
                            spinnerAlarmaYPantalla.setSelection(p8);
                            spinnerInterruptorSeguridad.setSelection(p9);


                            i=Login.loginJsons.size()+1;
                        }
                    }
                }


                break;

            case "Polea Motriz":
                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {
                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
                            Spinner spinnerBandaCentradaEnPolea=dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
                            Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                            Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizElevadora);
                            TextInputEditText txtDiametroPoleaMotriz = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                            TextInputEditText txtAnchoPoleaMotriz = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
                            TextInputEditText txtLargoEjePoleaMotriz = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                            TextInputEditText txtDiametroEjePoleaMotriz = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
                            TextInputEditText txtPotenciaMotor = dialogParte.findViewById(R.id.txtPotenciaMotorElevadora);
                            TextInputEditText txtRPMSalidaReductor = dialogParte.findViewById(R.id.txtRpmSalidaReductorPoleaMotriz);

                            ArrayAdapter<String> adapterTipoPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                            ArrayAdapter<String> adapterBandaCentrada=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            ArrayAdapter<String> adapterEstadoRvto=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                            ArrayAdapter<String> adapterGuardaPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

                            int p1=adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaMotrizElevadora());
                            int p2= adapterBandaCentrada.getPosition(Login.loginJsons.get(i).getBandaCentradaEnPoleaAMotrizElevadora());
                            int p3= adapterEstadoRvto.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora());
                            int p4= adapterGuardaPolea.getPosition(Login.loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora());

                            spinnerTipoPolea.setSelection(p1);
                            spinnerBandaCentradaEnPolea.setSelection(p2);
                            spinnerEstadoRvto.setSelection(p3);
                            spinnerGuardaPolea.setSelection(p4);

                            txtDiametroPoleaMotriz.setText(Login.loginJsons.get(i).getDiametroPoleaMotrizElevadora());
                            txtAnchoPoleaMotriz.setText(Login.loginJsons.get(i).getAnchoPoleaMotrizElevadora());
                            txtLargoEjePoleaMotriz.setText(Login.loginJsons.get(i).getLargoEjeMotrizElevadora());
                            txtDiametroEjePoleaMotriz.setText(Login.loginJsons.get(i).getDiametroEjeMotrizElevadora());
                            txtPotenciaMotor.setText(Login.loginJsons.get(i).getPotenciaMotorMotrizElevadora());
                            txtRPMSalidaReductor.setText(Login.loginJsons.get(i).getRpmSalidaReductorMotrizElevadora());


                            txtAnchoPoleaMotriz.setOnFocusChangeListener(this);
                            txtDiametroEjePoleaMotriz.setOnFocusChangeListener(this);
                            txtDiametroPoleaMotriz.setOnFocusChangeListener(this);
                            txtLargoEjePoleaMotriz.setOnFocusChangeListener(this);



                            i=Login.loginJsons.size()+1;
                        }
                    }


                }
            break;

            case "Polea Cola":
                for (int i=0;i<Login.loginJsons.size();i++)
                {
                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if(Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax()))
                        {
                            Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
                            Spinner spinnerBandaCentradaEnPolea=dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
                            Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);

                            final TextInputEditText txtDiametroPoleaCola = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                            final TextInputEditText txtAnchoPoleaCola = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
                            final TextInputEditText txtLargoEjePoleaCola = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                            final TextInputEditText txtDiametroEjePoleaCola = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
                            final TextInputEditText txtLongTensorTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloElevadora);
                            final TextInputEditText txtLongRecorridoContraPesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaColaElevadora);


                            ArrayAdapter<String> adapterTipoPolea=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                            spinnerTipoPolea.setAdapter(adapterTipoPolea);
                            ArrayAdapter<String> adapterBandaCentrada=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);
                            ArrayAdapter<String> adapterEstadoRvto=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                            spinnerEstadoRvto.setAdapter(adapterEstadoRvto);


                            txtDiametroPoleaCola.setText(Login.loginJsons.get(i).getDiametroPoleaColaElevadora());
                            txtAnchoPoleaCola.setText(Login.loginJsons.get(i).getAnchoPoleaColaElevadora());
                            txtLargoEjePoleaCola.setText(Login.loginJsons.get(i).getLargoEjePoleaColaElevadora());
                            txtDiametroEjePoleaCola.setText(Login.loginJsons.get(i).getDiametroEjePoleaColaElevadora());
                            txtLongTensorTornillo.setText(Login.loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora());
                            txtLongRecorridoContraPesa.setText(Login.loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora());

                            int p1=adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaColaElevadora());
                            int p2= adapterBandaCentrada.getPosition(Login.loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora());
                            int p3= adapterEstadoRvto.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora());

                            spinnerTipoPolea.setSelection(p1);
                            spinnerBandaCentradaEnPolea.setSelection(p2);
                            spinnerEstadoRvto.setSelection(p3);

                            i=Login.loginJsons.size()+1;
                        }
                    }
                }
                break;


            case "Cangilon":

                for (int i=0;i<Login.loginJsons.size();i++) {

                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if (Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax())) {
                            Spinner spinnerMaterialCangilon = dialogParte.findViewById(R.id.spinnerMaterialCangilon);
                            Spinner spinnerMarcaCangilon = dialogParte.findViewById(R.id.spinnerMarcaCangilon);
                            TextInputEditText txtReferenciaCangilon = dialogParte.findViewById(R.id.txtReferenciaCangilon);
                            Spinner spinnerNoFilasCangilon = dialogParte.findViewById(R.id.spinnerNoFilasCangilones);
                            Spinner spinnerNoAgujeros = dialogParte.findViewById(R.id.spinnerNoAgujeros);
                            Spinner spinnerTipoVentilacion = dialogParte.findViewById(R.id.spinnerTipoVentilacion);
                            Spinner spinnerTipoCangilon = dialogParte.findViewById(R.id.spinnerTipoCangilon);

                            TextInputEditText txtPesoMaterialCangilon = dialogParte.findViewById(R.id.txtPesoMaterialCangilon);
                            TextInputEditText txtPesoCangilonVacio = dialogParte.findViewById(R.id.txtPesoCangilonVacio);
                            TextInputEditText txtLongitudCangilon = dialogParte.findViewById(R.id.txtLongitudCangilon);
                            TextInputEditText txtProyeccionCangilon = dialogParte.findViewById(R.id.txtProyeccionCangilon);
                            TextInputEditText txtProfundidadCangilon = dialogParte.findViewById(R.id.txtProfundidadCangilon);
                            TextInputEditText txtCapacidadCangilon = dialogParte.findViewById(R.id.txtCapacidadCangilon);
                            TextInputEditText txtSeparacionEntreCangilones = dialogParte.findViewById(R.id.txtSeparacionCangilones);
                            TextInputEditText txtDistanciaBordesBandaEstructura = dialogParte.findViewById(R.id.txtDistBordesEstructura);
                            TextInputEditText txtDistanciaPosteriorBandaEstructura = dialogParte.findViewById(R.id.txtDistPostBandaEstructura);
                            TextInputEditText txtDistLabioFrontalCangilon = dialogParte.findViewById(R.id.txtLabioFrontalCangilonBanda);
                            TextInputEditText txtDistBordesCangilonEstructura = dialogParte.findViewById(R.id.txtDistBordesCangilonEstructura);


                            txtPesoMaterialCangilon.setText(Login.loginJsons.get(i).getPesoMaterialEnCadaCangilon());
                            txtPesoCangilonVacio.setText(Login.loginJsons.get(i).getPesoCangilonVacio());
                            txtLongitudCangilon.setText(Login.loginJsons.get(i).getLongitudCangilon());
                            txtProyeccionCangilon.setText(Login.loginJsons.get(i).getProyeccionCangilon());
                            txtProfundidadCangilon.setText(Login.loginJsons.get(i).getProfundidadCangilon());
                            txtCapacidadCangilon.setText(Login.loginJsons.get(i).getCapacidadCangilon());
                            txtSeparacionEntreCangilones.setText(Login.loginJsons.get(i).getSeparacionCangilones());
                            txtDistanciaBordesBandaEstructura.setText(Login.loginJsons.get(i).getDistanciaBordeBandaEstructura());
                            txtDistanciaPosteriorBandaEstructura.setText(Login.loginJsons.get(i).getDistanciaPosteriorBandaEstructura());
                            txtDistLabioFrontalCangilon.setText(Login.loginJsons.get(i).getDistanciaLabioFrontalCangilonEstructura());
                            txtDistBordesCangilonEstructura.setText(Login.loginJsons.get(i).getDistanciaBordesCangilonEstructura());
                            txtReferenciaCangilon.setText(Login.loginJsons.get(i).getReferenciaCangilon());

                            ArrayAdapter<String> adapterMaterialCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialCangilon);
                            spinnerMaterialCangilon.setAdapter(adapterMaterialCangilon);

                            ArrayAdapter<String> adapterMarcaCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaCangilon);
                            spinnerMarcaCangilon.setAdapter(adapterMarcaCangilon);


                            ArrayAdapter<String> adapterNoFilasCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noFilasCangilon);
                            spinnerNoFilasCangilon.setAdapter(adapterNoFilasCangilon);

                            ArrayAdapter<String> adapterNoAgujeros = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noAgujerosCangilon);
                            spinnerNoAgujeros.setAdapter(adapterNoAgujeros);

                            ArrayAdapter<String> adapterTipoVentilacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoVentilacion);
                            spinnerTipoVentilacion.setAdapter(adapterTipoVentilacion);

                            ArrayAdapter<String> adapterTipoCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCangilon);
                            spinnerTipoCangilon.setAdapter(adapterTipoCangilon);

                            int p1 = adapterMaterialCangilon.getPosition(Login.loginJsons.get(i).getMaterialCangilon());
                            int p2 = adapterMarcaCangilon.getPosition(Login.loginJsons.get(i).getMarcaCangilon());

                            int p4 = adapterNoFilasCangilon.getPosition(Login.loginJsons.get(i).getNoFilasCangilones());
                            int p5 = adapterNoAgujeros.getPosition(Login.loginJsons.get(i).getNoAgujeros());
                            int p6 = adapterTipoVentilacion.getPosition(Login.loginJsons.get(i).getTipoVentilacion());
                            int p7 = adapterTipoCangilon.getPosition(Login.loginJsons.get(i).getTipoCangilon());

                            spinnerMaterialCangilon.setSelection(p1);
                            spinnerMarcaCangilon.setSelection(p2);

                            spinnerNoFilasCangilon.setSelection(p4);
                            spinnerNoAgujeros.setSelection(p5);
                            spinnerTipoVentilacion.setSelection(p6);
                            spinnerTipoCangilon.setSelection(p7);
                            i=Login.loginJsons.size()+1;

                        }
                    }

                }

                break;

            case "Condiciones Carga":

                for (int i=0;i<Login.loginJsons.size();i++) {

                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if (Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax())) {


                            final Spinner spinnerAtaqueQuimico=dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
                            final Spinner spinnerAtaqueTemperatura=dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
                            final Spinner spinnerAtaqueAceites=dialogParte.findViewById(R.id.spinnerAtaqueAceites);
                            final Spinner spinnerAtaqueAbrasivo=dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
                            final Spinner spinnerHorasTrabajoDia=dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
                            final Spinner spinnerDiasTrabajoSemana=dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
                            final Spinner spinnerAbrasividad=dialogParte.findViewById(R.id.spinnerAbrasividad);
                            final Spinner spinnerPorcentajeFinos=dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
                            final Spinner spinnerTipoCarga=dialogParte.findViewById(R.id.spinnerTipoCarga);
                            final Spinner spinnerVariosPuntosAlimentacion=dialogParte.findViewById(R.id.spinnerVariosPuntosAlimentacion);
                            final Spinner spinnerTipoDescarga=dialogParte.findViewById(R.id.spinnerTipoDescarga);
                            final Spinner spinnerLluviaMaterial=dialogParte.findViewById(R.id.spinnerLluviaMaterial);
                            final Spinner spinnerMaxGranulometria=dialogParte.findViewById(R.id.spinnerMaxGranulometria);
                            Constants.llenar();
                            Constants.llenarTempMin();
                            Constants.llenarTempMax();

                            final TextInputEditText txtMaterial= dialogParte.findViewById(R.id.txtMaterial);
                            final TextInputEditText txtCapacidad= dialogParte.findViewById(R.id.txtCapacidadHorizontal);

                            final TextInputEditText txtDensidad= dialogParte.findViewById(R.id.txtDensidadMaterial);
                            final Spinner spinnerTempMaxMaterial= dialogParte.findViewById(R.id.spinnerTempMaxSobreBandaElevadora);
                            final Spinner spinnerTempPromedioMaterial= dialogParte.findViewById(R.id.spinnerTempPromedioMaterialSobreBanda);
                            final TextInputEditText txtAnchoPiernaElevador= dialogParte.findViewById(R.id.txtAnchoPiernaElevador);
                            final TextInputEditText txtProfundidadPiernaElevador= dialogParte.findViewById(R.id.txtProfundidadPiernaElevador);
                            final Spinner spinnerTempAmbienteMin= dialogParte.findViewById(R.id.spinnerTempAmbienteMin);
                            final Spinner spinnerTempAmbienteMax= dialogParte.findViewById(R.id.spinnerTempAmbienteMax);

                            txtMaterial.setText(Login.loginJsons.get(i).getMaterialElevadora());
                            txtCapacidad.setText(Login.loginJsons.get(i).getCapacidadElevadora());

                            txtDensidad.setText(Login.loginJsons.get(i).getDensidadMaterialElevadora());

                            txtAnchoPiernaElevador.setText(Login.loginJsons.get(i).getAnchoPiernaElevador());
                            txtProfundidadPiernaElevador.setText(Login.loginJsons.get(i).getProfundidadPiernaElevador());


                            ArrayAdapter<String> adapterAtaqueQuimico=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerAtaqueQuimico.setAdapter(adapterAtaqueQuimico);

                            ArrayAdapter<String> adapterAtaqueTemperatura=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerAtaqueTemperatura.setAdapter(adapterAtaqueTemperatura);

                            ArrayAdapter<String> adapterAtaqueAceites=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerAtaqueAceites.setAdapter(adapterAtaqueAceites);

                            ArrayAdapter<String> adapterAtaqueAbrasivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerAtaqueAbrasivo.setAdapter(adapterAtaqueAbrasivo);

                            ArrayAdapter<String> adapterSensorInductivo=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
                            spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

                            ArrayAdapter<String> adapterDiasSemana=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
                            spinnerDiasTrabajoSemana.setAdapter(adapterDiasSemana);

                            ArrayAdapter<String> adapterAbrasividad=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
                            spinnerAbrasividad.setAdapter(adapterAbrasividad);

                            ArrayAdapter<String> adapterPorcentajeFinos=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
                            spinnerPorcentajeFinos.setAdapter(adapterPorcentajeFinos);

                            ArrayAdapter<String> adapterTipoCarga=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCarga);
                            spinnerTipoCarga.setAdapter(adapterTipoCarga);

                            ArrayAdapter<String> adapterVariosPuntosAlimentacion=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerVariosPuntosAlimentacion.setAdapter(adapterVariosPuntosAlimentacion);

                            ArrayAdapter<String> adapterTipoDescarga=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoDescarga);
                            spinnerTipoDescarga.setAdapter(adapterTipoDescarga);

                            ArrayAdapter<String> adapterLluviaMaterial=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                            spinnerLluviaMaterial.setAdapter(adapterLluviaMaterial);

                            ArrayAdapter<String> adapterTempMaterial=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
                            spinnerTempMaxMaterial.setAdapter(adapterTempMaterial);
                            spinnerTempPromedioMaterial.setAdapter(adapterTempMaterial);

                            ArrayAdapter<String> adapterTempAmbienteMin=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMinAmbiente);
                            spinnerTempAmbienteMin.setAdapter(adapterTempAmbienteMin);


                            ArrayAdapter<String> adapterTempMaxAmbiente=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMaxAmbiente);
                            spinnerTempAmbienteMax.setAdapter(adapterTempMaxAmbiente);

                            ArrayAdapter<String> adapterMaxGranulometria=new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.maxGranulometria);
                            spinnerMaxGranulometria.setAdapter(adapterMaxGranulometria);



                            int p1 = adapterAtaqueQuimico.getPosition(Login.loginJsons.get(i).getAtaqueQuimicoElevadora());
                            int p2 = adapterAtaqueTemperatura.getPosition(Login.loginJsons.get(i).getGetAtaqueTemperaturaElevadora());
                            int p3 = adapterAtaqueAceites.getPosition(Login.loginJsons.get(i).getAtaqueAceitesElevadora());
                            int p4 = adapterAtaqueAbrasivo.getPosition(Login.loginJsons.get(i).getAtaqueAbrasivoElevadora());
                            int p5 = adapterSensorInductivo.getPosition(Login.loginJsons.get(i).getHorasTrabajoDiaElevadora());
                            int p6 = adapterDiasSemana.getPosition(Login.loginJsons.get(i).getDiasTrabajoSemanaElevadora());
                            int p7 = adapterAbrasividad.getPosition(Login.loginJsons.get(i).getAbrasividadElevadora());
                            int p8 = adapterPorcentajeFinos.getPosition(Login.loginJsons.get(i).getPorcentajeFinosElevadora());
                            int p9 = adapterTipoCarga.getPosition(Login.loginJsons.get(i).getTipoCarga());
                            int p10 = adapterVariosPuntosAlimentacion.getPosition(Login.loginJsons.get(i).getVariosPuntosDeAlimentacion());
                            int p11 = adapterTipoDescarga.getPosition(Login.loginJsons.get(i).getTipoDescarga());
                            int p12 = adapterLluviaMaterial.getPosition(Login.loginJsons.get(i).getLluviaDeMaterial());
                            int p13 = adapterTempMaterial.getPosition(Login.loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora());
                            int p14 = adapterTempMaterial.getPosition(Login.loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora());
                            int p15 = adapterTempAmbienteMin.getPosition(Login.loginJsons.get(i).getTempAmbienteMin());
                            int p16 = adapterTempMaxAmbiente.getPosition(Login.loginJsons.get(i).getTempAmbienteMax());
                            int p17 = adapterMaxGranulometria.getPosition(Login.loginJsons.get(i).getMaxGranulometriaElevadora());

                            spinnerAtaqueQuimico.setSelection(p1);
                            spinnerAtaqueTemperatura.setSelection(p2);
                            spinnerAtaqueAceites.setSelection(p3);
                            spinnerAtaqueAbrasivo.setSelection(p4);
                            spinnerHorasTrabajoDia.setSelection(p5);
                            spinnerDiasTrabajoSemana.setSelection(p6);
                            spinnerAbrasividad.setSelection(p7);
                            spinnerPorcentajeFinos.setSelection(p8);
                            spinnerTipoCarga.setSelection(p9);
                            spinnerVariosPuntosAlimentacion.setSelection(p10);
                            spinnerTipoDescarga.setSelection(p11);
                            spinnerLluviaMaterial.setSelection(p12);
                            spinnerTempMaxMaterial.setSelection(p13);
                            spinnerTempPromedioMaterial.setSelection(p14);
                            spinnerTempAmbienteMin.setSelection(p15);
                            spinnerTempAmbienteMax.setSelection(p16);
                            spinnerMaxGranulometria.setSelection(p17);

                            i=Login.loginJsons.size()+1;

                        }
                    }
                }

                break;

            case "Banda":

                for (int i=0;i<Login.loginJsons.size();i++) {

                    if(Login.loginJsons.get(i).getIdRegistro()!=null)
                    {

                        if (Login.loginJsons.get(i).getIdRegistro().equals(idMaximaRegistro.get(0).getMax())) {


                            TextInputEditText txtAnchoBanda=dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
                            TextInputEditText txtEspesorTotal=dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
                            TextInputEditText txtEspCubSup=dialogParte.findViewById(R.id.txtCubSupElevadora);
                            TextInputEditText txtEspCubInf=dialogParte.findViewById(R.id.txtCubInfElevadora);
                            TextInputEditText txtEspCojin=dialogParte.findViewById(R.id.txtEspesorCojin);
                            TextInputEditText txtVelocidadBanda=dialogParte.findViewById(R.id.txtVelocidadBandaElevadora);
                            TextInputEditText txtAnchoBandaAnterior=dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
                            TextInputEditText txtEspesorTotalAnterior=dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
                            TextInputEditText txtEspCubSupAnterior=dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
                            TextInputEditText txtEspCubInfAnterior=dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
                            TextInputEditText txtEspCojinAnterior=dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
                            TextInputEditText txtVelocidadBandaAnterior=dialogParte.findViewById(R.id.txtVelocidadBandaElevadoraAnterior);
                            TextInputEditText txtCausaFallaBandaAnterior=dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);
                            TextInputEditText txtDistanciaEntrePoleas =dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
                            TextInputEditText txtTonsTransportadasAnterior=dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);



                            Spinner spinnerMarcaBanda=dialogParte.findViewById(R.id.spinnerMarcaBandaElevadora);
                            Spinner spinnerNoLonas=dialogParte.findViewById(R.id.spinnerNoLonasElevadora);
                            Spinner spinnerTipoLona=dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadora);
                            Spinner spinnerTipoCubierta=dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadora);
                            Spinner spinnerTipoEmpalme= dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadora);
                            Spinner spinnerEstadoEmpalme=dialogParte.findViewById(R.id.spinnerEstadoEmpalmeElevadora);
                            Spinner spinnerResistenciaRotura=dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadora);
                            Spinner spinnerMarcaBandaAnterior=dialogParte.findViewById(R.id.spinnerMarcaBandaElevadoraAnterior);
                            Spinner spinnerNoLonasAnterior=dialogParte.findViewById(R.id.spinnerNoLonasElevadoraAnterior);
                            Spinner spinnerTipoLonaAnterior=dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadoraAnterior);
                            Spinner spinnerTipoCubiertaAnterior=dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadoraAnterior);
                            Spinner spinnerTipoEmpalmeAnterior= dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadoraAnterior);
                            Spinner spinnerResistenciaRoturaAnterior=dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadoraAnterior);



                            ArrayAdapter<String> adapterMarcaBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaBanda);
                            spinnerMarcaBanda.setAdapter(adapterMarcaBanda);
                            spinnerMarcaBandaAnterior.setAdapter(adapterMarcaBanda);

                            ArrayAdapter<String> adapterNoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noLonas);
                            spinnerNoLonas.setAdapter(adapterNoLonas);
                            spinnerNoLonasAnterior.setAdapter(adapterNoLonas);

                            ArrayAdapter<String> adapterTipoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoLona);
                            spinnerTipoLona.setAdapter(adapterTipoLonas);
                            spinnerTipoLonaAnterior.setAdapter(adapterTipoLonas);

                            ArrayAdapter<String> adapterTipoCubierta= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCubierta);
                            spinnerTipoCubierta.setAdapter(adapterTipoCubierta);
                            spinnerTipoCubiertaAnterior.setAdapter(adapterTipoCubierta);

                            ArrayAdapter<String> adapterTipoEmpalme= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoEmpalme);
                            spinnerTipoEmpalme.setAdapter(adapterTipoEmpalme);
                            spinnerTipoEmpalmeAnterior.setAdapter(adapterTipoEmpalme);

                            ArrayAdapter<String> adapterEstadoEmpalme= new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                            spinnerEstadoEmpalme.setAdapter(adapterEstadoEmpalme);


                            ArrayAdapter<String> adapterRoturaLona=new ArrayAdapter(getContext(),R.layout.estilo_spinner,Constants.resistenciaRoturaLona);
                            spinnerResistenciaRotura.setAdapter(adapterRoturaLona);
                            spinnerResistenciaRoturaAnterior.setAdapter(adapterRoturaLona);

                            int p1 = adapterMarcaBanda.getPosition(Login.loginJsons.get(i).getMarcaBandaElevadora());
                            int p2 = adapterNoLonas.getPosition(Login.loginJsons.get(i).getNoLonaBandaElevadora());
                            int p3 = adapterTipoLonas.getPosition(Login.loginJsons.get(i).getTipoLonaBandaElevadora());
                            int p4 = adapterTipoCubierta.getPosition(Login.loginJsons.get(i).getTipoCubiertaElevadora());
                            int p5 = adapterTipoEmpalme.getPosition(Login.loginJsons.get(i).getTipoEmpalmeElevadora());
                            int p6 = adapterEstadoEmpalme.getPosition(Login.loginJsons.get(i).getEstadoEmpalmeElevadora());
                            int p7 = adapterRoturaLona.getPosition(Login.loginJsons.get(i).getResistenciaRoturaLonaElevadora());


                            int p8 = adapterMarcaBanda.getPosition(Login.loginJsons.get(i).getMarcaBandaElevadoraAnterior());
                            int p9 = adapterNoLonas.getPosition(Login.loginJsons.get(i).getNoLonasBandaElevadoraAnterior());
                            int p10 = adapterTipoLonas.getPosition(Login.loginJsons.get(i).getTipoLonaBandaElevadoraAnterior());
                            int p11= adapterTipoCubierta.getPosition(Login.loginJsons.get(i).getTipoCubiertaElevadoraAnterior());
                            int p12 = adapterTipoEmpalme.getPosition(Login.loginJsons.get(i).getTipoEmpalmeElevadoraAnterior());

                            int p14 = adapterRoturaLona.getPosition(Login.loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior());


                            spinnerMarcaBanda.setSelection(p1);
                            spinnerMarcaBandaAnterior.setSelection(p8);
                            spinnerNoLonas.setSelection(p2);
                            spinnerNoLonasAnterior.setSelection(p9);
                            spinnerTipoLona.setSelection(p3);
                            spinnerTipoLonaAnterior.setSelection(p10);
                            spinnerTipoCubierta.setSelection(p4);
                            spinnerTipoCubiertaAnterior.setSelection(p11);
                            spinnerTipoEmpalme.setSelection(p5);
                            spinnerTipoEmpalmeAnterior.setSelection(p12);
                            spinnerEstadoEmpalme.setSelection(p6);

                            spinnerResistenciaRotura.setSelection(p7);
                            spinnerResistenciaRoturaAnterior.setSelection(p14);



                            txtAnchoBanda.setText(Login.loginJsons.get(i).getAnchoBandaElevadora());
                            txtEspesorTotal.setText(Login.loginJsons.get(i).getEspesorTotalBandaElevadora());
                            txtEspCubSup.setText(Login.loginJsons.get(i).getEspesorCubiertaSuperiorElevadora());
                            txtEspCubInf.setText(Login.loginJsons.get(i).getEspesorCubiertaInferiorElevadora());
                            txtEspCojin.setText(Login.loginJsons.get(i).getEspesorCojinActualElevadora());
                            txtVelocidadBanda.setText(Login.loginJsons.get(i).getVelocidadBandaElevadora());
                            txtDistanciaEntrePoleas.setText(Login.loginJsons.get(i).getDistanciaEntrePoleasElevadora());


                            txtAnchoBandaAnterior.setText(Login.loginJsons.get(i).getAnchoBandaElevadoraAnterior());
                            txtEspesorTotalAnterior.setText(Login.loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior());
                            txtEspCubSupAnterior.setText(Login.loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior());
                            txtEspCubInfAnterior.setText(Login.loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior());
                            txtEspCojinAnterior.setText(Login.loginJsons.get(i).getEspesorCojinElevadoraAnterior());
                            txtVelocidadBandaAnterior.setText(Login.loginJsons.get(i).getVelocidadBandaElevadoraAnterior());
                            txtCausaFallaBandaAnterior.setText(Login.loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior());
                            txtTonsTransportadasAnterior.setText(Login.loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior());


                            i=Login.loginJsons.size()+1;

                        }
                    }

                }

                break;

        }
    }

}
