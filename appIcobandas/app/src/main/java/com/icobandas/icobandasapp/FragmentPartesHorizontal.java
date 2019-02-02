package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPartesHorizontal extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    View view;
    ImageButton btnBanda;
    ImageButton btnPoleaCola;
    ImageButton btnDesviador;
    ImageButton btnCondicionesCarga;
    ImageButton btnSoporteCarga;
    ImageButton btnAlineacion;
    ImageButton btnPoleaMotriz;
    ImageButton btnLimpiadorPrimario;
    ImageButton btnLimpiadorSecundarioTerciario;
    ImageButton btnPoleaAmarre;
    ImageButton btnPoleaTensora;
    ImageButton btnSeguridad;
    ImageButton btnRodilloCarga;
    ImageButton btnRodilloRetorno;

    Gson gson = new Gson();

    RequestQueue queue;


    AlertDialog.Builder alerta;
    Dialog dialogParte;
    AlertDialog dialogCarga;

    public FragmentPartesHorizontal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_partes_horizontal, container, false);
        getActivity().setTitle("Transportador horizontal o inclinado");
        inicializar();

        btnBanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogBanda();
            }
        });
        btnPoleaCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaCola();
            }
        });
        btnDesviador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogDesviador();
            }
        });
        btnCondicionesCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { abrirDialogCondicionesCarga();
            }
        });
        btnSoporteCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogSoporteCarga();
            }
        });
        btnAlineacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogAlineacion();
            }
        });
        btnPoleaMotriz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaMotriz();
            }
        });
        btnLimpiadorPrimario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { abrirDialogLimpiadorPrimario();
            }
        });
        btnLimpiadorSecundarioTerciario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { abrirDialogLimpiadorSecundarioTerciario();}});
        btnPoleaAmarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaAmarre();
            }
        });
        btnPoleaTensora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogPoleaTensora();
            }
        });
        btnSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogSeguridad();
            }
        });
        btnRodilloCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogRodilloCarga();
            }
        });

        btnRodilloRetorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogRodilloRetorno();
            }
        });


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setRetainInstance(true);


        return view;
    }




    private void inicializar() {
        queue = Volley.newRequestQueue(getContext());
        btnBanda = view.findViewById(R.id.btnBandaHorizontal);
        btnPoleaCola = view.findViewById(R.id.btnPoleaColaHorizontal);
        btnDesviador = view.findViewById(R.id.btnDesviador);
        btnCondicionesCarga = view.findViewById(R.id.btnCondicionesCargaHorizontal);
        btnSoporteCarga = view.findViewById(R.id.btnRodilloLateral);
        btnAlineacion = view.findViewById(R.id.btnRodilloAlineacion);
        btnPoleaMotriz = view.findViewById(R.id.btnPoleaMotrizHorizontal);
        btnLimpiadorPrimario = view.findViewById(R.id.btnLimpiadorPrimario);
        btnLimpiadorSecundarioTerciario = view.findViewById(R.id.btnLimpidorSecundario);
        btnPoleaAmarre = view.findViewById(R.id.btnPoleaAmarreHorizontal);
        btnPoleaTensora = view.findViewById(R.id.btnPoleaTensora);
        btnSeguridad = view.findViewById(R.id.btnSeguridadHorizontal);
        btnRodilloCarga=view.findViewById(R.id.btnRodilloCarga);
        btnRodilloRetorno=view.findViewById(R.id.btnRodilloRetorno);

        dialogCarga = new SpotsDialog(getContext(), "Ejecutando Registro");
        alerta = new AlertDialog.Builder(getContext());
    }


    public void abrirDialogBanda() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_banda_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Spinner spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontal);
        final Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasHorizontal);
        final Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontal);
        final Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontal);
        final Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontal);
        final Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeHorizontal);
        final Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontal);
        final Spinner spinnerLocTensor = dialogParte.findViewById(R.id.spinnerLocalizacionTensor);
        final Spinner spinnerBandReversible = dialogParte.findViewById(R.id.spinnerBandaReversible);
        final Spinner spinnerBandaArrastre = dialogParte.findViewById(R.id.spinnerBandaDeArrastre);


        final Spinner spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontalAnterior);
        final Spinner spinnerNoLonasAnterior = dialogParte.findViewById(R.id.spinnerNoLonasHorizontalAnterior);
        final Spinner spinnerTipoLonaAnterior = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontalAnterior);
        final Spinner spinnerTipoCubiertaAnterior = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontalAnterior);
        final Spinner spinnerTipoEmpalmeAnterior = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontalAnterior);
        final Spinner spinnerResistenciaRoturaAnterior = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontalAnterior);

        final TextInputEditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaHorizontal);
        final TextInputEditText txtEspesorTotal = dialogParte.findViewById(R.id.txtEpesorTotalHorizontal);
        final TextInputEditText txtEspCubSup = dialogParte.findViewById(R.id.txtCubSupHorizontal);
        final TextInputEditText txtEspCubInf = dialogParte.findViewById(R.id.txtCubInfHorizontal);
        final TextInputEditText txtEspCojin = dialogParte.findViewById(R.id.txtEspesorCojinHorizontal);
        final TextInputEditText txtVelocidadBanda = dialogParte.findViewById(R.id.txtVelocidadBandaHorizontal);

        final TextInputEditText txtDistanciEntrePoleas = dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
        final TextInputEditText txtInclinacion = dialogParte.findViewById(R.id.txtInclinacion);
        final TextInputEditText txtRecorridoUtilTensor = dialogParte.findViewById(R.id.txtRecorridoUtilTensor);
        final TextInputEditText txtLongitudSinfin = dialogParte.findViewById(R.id.txtLongitudSinfinBanda);
        final TextInputEditText txtTonsTransportadas = dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);

        final TextInputEditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaHorizontalAnterior);
        final TextInputEditText txtEspesorTotalAnterior = dialogParte.findViewById(R.id.txtEspesorTotalHorizontalAnterior);
        final TextInputEditText txtEspCubSupAnterior = dialogParte.findViewById(R.id.txtCubSupHorizontalAnterior);
        final TextInputEditText txtEspCubInfAnterior = dialogParte.findViewById(R.id.txtCubInfHorizontalAnterior);
        final TextInputEditText txtEspCojinAnterior = dialogParte.findViewById(R.id.txtEspesorCojinHorizontalAnterior);
        final TextInputEditText txtCausaFallo = dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);


        Button btnEnviarRegistro = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);


        ArrayAdapter<String> adapterMarcaBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaBanda);
        spinnerMarcaBanda.setAdapter(adapterMarcaBanda);
        spinnerMarcaBandaAnterior.setAdapter(adapterMarcaBanda);

        ArrayAdapter<String> adapterNoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noLonas);
        spinnerNoLonas.setAdapter(adapterNoLonas);
        spinnerNoLonasAnterior.setAdapter(adapterNoLonas);

        ArrayAdapter<String> adapterTipoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoLona);
        spinnerTipoLona.setAdapter(adapterTipoLonas);
        spinnerTipoLonaAnterior.setAdapter(adapterTipoLonas);

        ArrayAdapter<String> adapterTipoCubierta = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCubierta);
        spinnerTipoCubierta.setAdapter(adapterTipoCubierta);
        spinnerTipoCubiertaAnterior.setAdapter(adapterTipoCubierta);

        ArrayAdapter<String> adapterTipoEmpalme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoEmpalme);
        spinnerTipoEmpalme.setAdapter(adapterTipoEmpalme);
        spinnerTipoEmpalmeAnterior.setAdapter(adapterTipoEmpalme);

        ArrayAdapter<String> adapterEstadoEmpalme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoEmpalme.setAdapter(adapterEstadoEmpalme);
        spinnerResistenciaRoturaAnterior.setAdapter(adapterEstadoEmpalme);


        ArrayAdapter<String> adapterRoturaLona = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.resistenciaRoturaLona);
        spinnerResistenciaRotura.setAdapter(adapterRoturaLona);

        ArrayAdapter<String> adapterLocTensor = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.localizacionTensor);
        spinnerLocTensor.setAdapter(adapterLocTensor);

        ArrayAdapter<String> adapterBandaReversible = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandReversible.setAdapter(adapterBandaReversible);

        ArrayAdapter<String> adapterBandaArastre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandaArrastre.setAdapter(adapterBandaArastre);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Banda");
        }


        txtAnchoBanda.setOnFocusChangeListener(this);
        txtAnchoBandaAnterior.setOnFocusChangeListener(this);
        txtEspesorTotal.setOnFocusChangeListener(this);
        txtEspesorTotalAnterior.setOnFocusChangeListener(this);

        txtEspCubSup.setOnFocusChangeListener(this);
        txtEspCubSupAnterior.setOnFocusChangeListener(this);
        txtEspCojin.setOnFocusChangeListener(this);
        txtEspCojinAnterior.setOnFocusChangeListener(this);
        txtEspCubInf.setOnFocusChangeListener(this);
        txtEspCubInfAnterior.setOnFocusChangeListener(this);


        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroBandaHorizontal";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("marcaBandaTransportadora", spinnerMarcaBanda.getSelectedItem().toString());
                                                    params.put("anchoBandaTransportadora", txtAnchoBanda.getText().toString());
                                                    params.put("noLonasBandaTransportadora", spinnerNoLonas.getSelectedItem().toString());
                                                    params.put("tipoLonaBandaTransportadora", spinnerTipoLona.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaTransportadora", txtEspesorTotal.getText().toString());
                                                    params.put("espesorCubiertaSuperiorTransportadora", txtEspCubSup.getText().toString());
                                                    params.put("espesorCojinTransportadora", txtEspCojin.getText().toString());
                                                    params.put("espesorCubiertaInferiorTransportadora", txtEspCubInf.getText().toString());
                                                    params.put("tipoCubiertaTransportadora", spinnerTipoCubierta.getSelectedItem().toString());
                                                    params.put("tipoEmpalmeTransportadora", spinnerTipoEmpalme.getSelectedItem().toString());
                                                    params.put("estadoEmpalmeTransportadora", spinnerEstadoEmpalme.getSelectedItem().toString());
                                                    params.put("localizacionTensorTransportadora", spinnerLocTensor.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaLonaTransportadora", spinnerResistenciaRotura.getSelectedItem().toString());
                                                    params.put("velocidadBandaHorizontal", txtVelocidadBanda.getText().toString());
                                                    params.put("bandaReversible", spinnerBandReversible.getSelectedItem().toString());
                                                    params.put("bandaDeArrastre", spinnerBandaArrastre.getSelectedItem().toString());
                                                    params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getSelectedItem().toString());
                                                    params.put("anchoBandaHorizontalAnterior", txtAnchoBandaAnterior.getText().toString());
                                                    params.put("noLonasBandaHorizontalAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                                    params.put("tipoLonaBandaHorizontalAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                                    params.put("espesorTotalBandaHorizontalAnterior", txtEspesorTotalAnterior.getText().toString());
                                                    params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", txtEspCubSupAnterior.getText().toString());
                                                    params.put("espesorCubiertaInferiorBandaHorizontalAnterior", txtEspCubInfAnterior.getText().toString());
                                                    params.put("espesorCojinBandaHorizontalAnterior", txtEspCojinAnterior.getText().toString());
                                                    params.put("tipoEmpalmeBandaHorizontalAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                                    params.put("resistenciaRoturaLonaBandaHorizontalAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                                    params.put("tipoCubiertaBandaHorizontalAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                                    params.put("causaFallaCambioBandaHorizontal", txtCausaFallo.getText().toString());

                                                    params.put("distanciaEntrePoleasBandaHorizontal", txtDistanciEntrePoleas.getText().toString());
                                                    params.put("inclinacionBandaHorizontal", txtInclinacion.getText().toString());
                                                    params.put("recorridoUtilTensorBandaHorizontal", txtRecorridoUtilTensor.getText().toString());
                                                    params.put("longitudSinfinBandaHorizontal", txtLongitudSinfin.getText().toString());
                                                    params.put("tonsTransportadasBandaHoizontalAnterior", txtTonsTransportadas.getText().toString());


                                                    if (!txtAnchoBanda.getText().toString().equals("")) {
                                                        params.put("anchoBandaTransportadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                                    }

                                                    if (!txtEspesorTotal.getText().toString().equals("")) {
                                                        params.put("espesorTotalBandaTransportadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                                    }
                                                    if (!txtEspCojin.getText().toString().equals("")) {
                                                        params.put("espesorCojinTransportadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                                    }
                                                    if (!txtEspCubSup.getText().toString().equals("")) {
                                                        params.put("espesorCubiertaSuperiorTransportadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                                    }
                                                    if (!txtEspCubInf.getText().toString().equals("")) {
                                                        params.put("espesorCubiertaInferiorTransportadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                                    }
                                                    if (!txtVelocidadBanda.getText().toString().equals("")) {
                                                        params.put("velocidadBandaHorizontal", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));

                                                    }
                                                    if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                                        params.put("anchoBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                                    }
                                                    if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                                        params.put("espesorTotalBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                                    }
                                                    if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                                        params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                                    }
                                                    if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                                        params.put("espesorCojinBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                                    }
                                                    if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                                        params.put("espesorCubiertaInferiorBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                                    }
                                                    if (!txtDistanciEntrePoleas.getText().toString().equals("")) {
                                                        params.put("distanciaEntrePoleasBandaHorizontal", String.valueOf(Float.parseFloat(txtDistanciEntrePoleas.getText().toString())));
                                                    }
                                                    if (!txtInclinacion.getText().toString().equals("")) {
                                                        params.put("inclinacionBandaHorizontal", String.valueOf(Float.parseFloat(txtInclinacion.getText().toString())));
                                                    }
                                                    if (!txtRecorridoUtilTensor.getText().toString().equals("")) {
                                                        params.put("recorridoUtilTensorBandaHorizontal", String.valueOf(Float.parseFloat(txtRecorridoUtilTensor.getText().toString())));
                                                    }
                                                    if (!txtLongitudSinfin.getText().toString().equals("")) {
                                                        params.put("longitudSinfinBandaHorizontal", String.valueOf(Float.parseFloat(txtLongitudSinfin.getText().toString())));
                                                    }


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarBandaHorizontal";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("marcaBandaTransportadora", spinnerMarcaBanda.getSelectedItem().toString());
                                    params.put("anchoBandaTransportadora", txtAnchoBanda.getText().toString());
                                    params.put("noLonasBandaTransportadora", spinnerNoLonas.getSelectedItem().toString());
                                    params.put("tipoLonaBandaTransportadora", spinnerTipoLona.getSelectedItem().toString());
                                    params.put("espesorTotalBandaTransportadora", txtEspesorTotal.getText().toString());
                                    params.put("espesorCubiertaSuperiorTransportadora", txtEspCubSup.getText().toString());
                                    params.put("espesorCojinTransportadora", txtEspCojin.getText().toString());
                                    params.put("espesorCubiertaInferiorTransportadora", txtEspCubInf.getText().toString());
                                    params.put("tipoCubiertaTransportadora", spinnerTipoCubierta.getSelectedItem().toString());
                                    params.put("tipoEmpalmeTransportadora", spinnerTipoEmpalme.getSelectedItem().toString());
                                    params.put("estadoEmpalmeTransportadora", spinnerEstadoEmpalme.getSelectedItem().toString());
                                    params.put("localizacionTensorTransportadora", spinnerLocTensor.getSelectedItem().toString());
                                    params.put("resistenciaRoturaLonaTransportadora", spinnerResistenciaRotura.getSelectedItem().toString());
                                    params.put("velocidadBandaHorizontal", txtVelocidadBanda.getText().toString());
                                    params.put("bandaReversible", spinnerBandReversible.getSelectedItem().toString());
                                    params.put("bandaDeArrastre", spinnerBandaArrastre.getSelectedItem().toString());
                                    params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getSelectedItem().toString());
                                    params.put("anchoBandaHorizontalAnterior", txtAnchoBandaAnterior.getText().toString());
                                    params.put("noLonasBandaHorizontalAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                    params.put("tipoLonaBandaHorizontalAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                    params.put("espesorTotalBandaHorizontalAnterior", txtEspesorTotalAnterior.getText().toString());
                                    params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", txtEspCubSupAnterior.getText().toString());
                                    params.put("espesorCubiertaInferiorBandaHorizontalAnterior", txtEspCubInfAnterior.getText().toString());
                                    params.put("espesorCojinBandaHorizontalAnterior", txtEspCojinAnterior.getText().toString());
                                    params.put("tipoEmpalmeBandaHorizontalAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                    params.put("resistenciaRoturaLonaBandaHorizontalAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                    params.put("tipoCubiertaBandaHorizontalAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                    params.put("causaFallaCambioBandaHorizontal", txtCausaFallo.getText().toString());

                                    params.put("distanciaEntrePoleasBandaHorizontal", txtDistanciEntrePoleas.getText().toString());
                                    params.put("inclinacionBandaHorizontal", txtInclinacion.getText().toString());
                                    params.put("recorridoUtilTensorBandaHorizontal", txtRecorridoUtilTensor.getText().toString());
                                    params.put("longitudSinfinBandaHorizontal", txtLongitudSinfin.getText().toString());
                                    params.put("tonsTransportadasBandaHoizontalAnterior", txtTonsTransportadas.getText().toString());

                                    if (!txtAnchoBanda.getText().toString().equals("")) {
                                        params.put("anchoBandaTransportadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                    }

                                    if (!txtEspesorTotal.getText().toString().equals("")) {
                                        params.put("espesorTotalBandaTransportadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                    }
                                    if (!txtEspCojin.getText().toString().equals("")) {
                                        params.put("espesorCojinTransportadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                    }
                                    if (!txtEspCubSup.getText().toString().equals("")) {
                                        params.put("espesorCubiertaSuperiorTransportadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                    }
                                    if (!txtEspCubInf.getText().toString().equals("")) {
                                        params.put("espesorCubiertaInferiorTransportadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                    }
                                    if (!txtVelocidadBanda.getText().toString().equals("")) {
                                        params.put("velocidadBandaHorizontal", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));

                                    }
                                    if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                        params.put("anchoBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                    }
                                    if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                        params.put("espesorTotalBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                    }
                                    if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                        params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                    }
                                    if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                        params.put("espesorCojinBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                    }
                                    if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                        params.put("espesorCubiertaInferiorBandaHorizontalAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                    }
                                    if (!txtDistanciEntrePoleas.getText().toString().equals("")) {
                                        params.put("distanciaEntrePoleasBandaHorizontal", String.valueOf(Float.parseFloat(txtDistanciEntrePoleas.getText().toString())));
                                    }
                                    if (!txtInclinacion.getText().toString().equals("")) {
                                        params.put("inclinacionBandaHorizontal", String.valueOf(Float.parseFloat(txtInclinacion.getText().toString())));
                                    }
                                    if (!txtRecorridoUtilTensor.getText().toString().equals("")) {
                                        params.put("recorridoUtilTensorBandaHorizontal", String.valueOf(Float.parseFloat(txtRecorridoUtilTensor.getText().toString())));
                                    }
                                    if (!txtLongitudSinfin.getText().toString().equals("")) {
                                        params.put("longitudSinfinBandaHorizontal", String.valueOf(Float.parseFloat(txtLongitudSinfin.getText().toString())));
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });


        dialogParte.setCancelable(true);
        dialogParte.show();
    }

    public void abrirDialogPoleaCola() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_cola_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvDiametroPolea = dialogParte.findViewById(R.id.tvDiametroPoleaMotrizElevadora);
        TextView tvAnchoPolea = dialogParte.findViewById(R.id.tvAnchoPoleaMotrizElevadora);
        TextView tvLargoEje = dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizElevadora);
        TextView tvDiametroEje = dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizElevadora);
        TextView tvAnguloAmarre = dialogParte.findViewById(R.id.tvAnguloAmarrePC);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaColaHorizontal);
        final Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
        final Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicion);
        final Spinner spinnerGuardaPoleaCola = dialogParte.findViewById(R.id.spinnerGuardaPoleaColaHorizontal);
        final Spinner spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePCHorizontal);


        final TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
        final TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizHorizontal);
        final TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
        final TextInputEditText txtLongTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloHorizontal);
        final TextInputEditText txtLongContrapesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaMotrizHorizontal);
        final TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPCCola);


        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);


        spinnerTipoPolea.setAdapter(adapterTipoPolea);
        spinnerBandaCentradaEnPolea.setAdapter(adapterSiNo);
        spinnerEstadoRvto.setAdapter(adapterEstadoPartes);
        spinnerGuardaPoleaCola.setAdapter(adapterEstadoPartes);
        spinnerTipoTransicion.setAdapter(adapterTipoTransicion);
        spinnerAnguloAmarre.setAdapter(adapterAnguloAmarre);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Polea Cola");
        }


        tvAnchoPolea.setOnClickListener(this);
        tvDiametroEje.setOnClickListener(this);
        tvDiametroPolea.setOnClickListener(this);
        tvLargoEje.setOnClickListener(this);
        tvAnguloAmarre.setOnClickListener(this);


        txtDiametro.setOnFocusChangeListener(this);
        txtAncho.setOnFocusChangeListener(this);
        txtDiametroEje.setOnFocusChangeListener(this);
        txtLargoEje.setOnFocusChangeListener(this);
        txtLongTornillo.setOnFocusChangeListener(this);
        txtLongContrapesa.setOnFocusChangeListener(this);
        txtDistanciaTransicion.setOnFocusChangeListener(this);

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroPoleaColaHorizontal";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("diametroPoleaColaTransportadora", txtDiametro.getText().toString());
                                                    params.put("anchoPoleaColaTransportadora", txtAncho.getText().toString());
                                                    params.put("tipoPoleaColaTransportadora", spinnerTipoPolea.getSelectedItem().toString());
                                                    params.put("largoEjePoleaColaTransportadora", txtLargoEje.getText().toString());
                                                    params.put("diametroEjePoleaColaHorizontal", txtDiametroEje.getText().toString());
                                                    params.put("icobandasCentradaPoleaColaTransportadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                    params.put("anguloAmarrePoleaColaTransportadora", spinnerAnguloAmarre.getSelectedItem().toString());
                                                    params.put("estadoRvtoPoleaColaTransportadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                    params.put("tipoTransicionPoleaColaTransportadora", spinnerTipoTransicion.getSelectedItem().toString());
                                                    params.put("distanciaTransicionPoleaColaTransportadora", txtDistanciaTransicion.getText().toString());
                                                    params.put("longitudTensorTornilloPoleaColaTransportadora", txtLongTornillo.getText().toString());
                                                    params.put("longitudRecorridoContrapesaPoleaColaTransportadora", txtLongContrapesa.getText().toString());
                                                    params.put("guardaPoleaColaTransportadora", spinnerGuardaPoleaCola.getSelectedItem().toString());

                                                    if(!txtDiametro.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString()))) ;
                                                    }
                                                    if(!txtAncho.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString()))) ;
                                                    }
                                                    if(!txtLargoEje.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString()))) ;
                                                    }
                                                    if(!txtDiametroEje.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString()))) ;
                                                    }
                                                    if(!txtDistanciaTransicion.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString()))) ;
                                                    }
                                                    if(!txtLongTornillo.getText().toString().equals(""))
                                                    {
                                                        params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString()))) ;
                                                    }
                                                    if(!txtLongContrapesa.getText().toString().equals(""))
                                                    {
                                                        params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString()))) ;
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarPoleaColaHorizontal";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("diametroPoleaColaTransportadora", txtDiametro.getText().toString());
                                    params.put("anchoPoleaColaTransportadora", txtAncho.getText().toString());
                                    params.put("tipoPoleaColaTransportadora", spinnerTipoPolea.getSelectedItem().toString());
                                    params.put("largoEjePoleaColaTransportadora", txtLargoEje.getText().toString());
                                    params.put("diametroEjePoleaColaHorizontal", txtDiametroEje.getText().toString());
                                    params.put("icobandasCentradaPoleaColaTransportadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                    params.put("anguloAmarrePoleaColaTransportadora", spinnerAnguloAmarre.getSelectedItem().toString());
                                    params.put("estadoRvtoPoleaColaTransportadora", spinnerEstadoRvto.getSelectedItem().toString());
                                    params.put("tipoTransicionPoleaColaTransportadora", spinnerTipoTransicion.getSelectedItem().toString());
                                    params.put("distanciaTransicionPoleaColaTransportadora", txtDistanciaTransicion.getText().toString());
                                    params.put("longitudTensorTornilloPoleaColaTransportadora", txtLongTornillo.getText().toString());
                                    params.put("longitudRecorridoContrapesaPoleaColaTransportadora", txtLongContrapesa.getText().toString());
                                    params.put("guardaPoleaColaTransportadora", spinnerGuardaPoleaCola.getSelectedItem().toString());

                                    if(!txtDiametro.getText().toString().equals(""))
                                    {
                                        params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString()))) ;
                                    }
                                    if(!txtAncho.getText().toString().equals(""))
                                    {
                                        params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString()))) ;
                                    }
                                    if(!txtLargoEje.getText().toString().equals(""))
                                    {
                                        params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString()))) ;
                                    }
                                    if(!txtDiametroEje.getText().toString().equals(""))
                                    {
                                        params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString()))) ;
                                    }
                                    if(!txtDistanciaTransicion.getText().toString().equals(""))
                                    {
                                        params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString()))) ;
                                    }
                                    if(!txtLongTornillo.getText().toString().equals(""))
                                    {
                                        params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString()))) ;
                                    }
                                    if(!txtLongContrapesa.getText().toString().equals(""))
                                    {
                                        params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString()))) ;
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
    }

    public void abrirDialogDesviador() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_desviador_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerHayDesviador = dialogParte.findViewById(R.id.spinnerHayDesviador);
        final Spinner spinnerElDesviadorBascula = dialogParte.findViewById(R.id.spinnerElDesviadorBascula);
        final Spinner spinnerHayPresionUniforme = dialogParte.findViewById(R.id.spinnerHayPresionUniforme);
        final Spinner spinnerCauchoVPlow = dialogParte.findViewById(R.id.spinnerCauchoVPlow);

        final TextInputEditText txtAchoCaucho = dialogParte.findViewById(R.id.txtAnchoCauchoVPlow);
        final TextInputEditText txtEspesorCaucho = dialogParte.findViewById(R.id.txtEspesorCauchoVPlow);


        ArrayAdapter<String> adapterHayDesviador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerHayDesviador.setAdapter(adapterHayDesviador);

        ArrayAdapter<String> adapterElDesviadoBascula = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerElDesviadorBascula.setAdapter(adapterElDesviadoBascula);

        ArrayAdapter<String> adapterPresionUniforme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerHayPresionUniforme.setAdapter(adapterPresionUniforme);

        ArrayAdapter<String> adapterCauchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerCauchoVPlow.setAdapter(adapterCauchoVPlow);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Desviador");
        }

        txtAchoCaucho.setOnFocusChangeListener(this);
        txtEspesorCaucho.setOnFocusChangeListener(this);

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroDesviador";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");



                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("anchoVPlow", txtAchoCaucho.getText().toString());
                                                    params.put("espesorVPlow", txtEspesorCaucho.getText().toString());
                                                    params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                                                    params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                                                    params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                                                    params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());

                                                    if (!txtAchoCaucho.getText().toString().equals("")) {
                                                        params.put("anchoVPlow", String.valueOf(Float.parseFloat(txtAchoCaucho.getText().toString())));
                                                    }


                                                    if (!txtEspesorCaucho.getText().toString().equals("")) {
                                                        params.put("espesorVPlow", String.valueOf(Float.parseFloat(txtEspesorCaucho.getText().toString())));
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarDesviador";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("anchoVPlow", txtAchoCaucho.getText().toString());
                                    params.put("espesorVPlow", txtEspesorCaucho.getText().toString());
                                    params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                                    params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                                    params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                                    params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());

                                    if (!txtAchoCaucho.getText().toString().equals("")) {
                                        params.put("anchoVPlow", String.valueOf(Float.parseFloat(txtAchoCaucho.getText().toString())));
                                    }


                                    if (!txtEspesorCaucho.getText().toString().equals("")) {
                                        params.put("espesorVPlow", String.valueOf(Float.parseFloat(txtEspesorCaucho.getText().toString())));
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogCondicionesCarga() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_condiciones_carga_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        Constants.llenar();


        final Spinner spinnerTipoRevestimientoTolva = dialogParte.findViewById(R.id.spinnerTipoRvtoTolva);
        final Spinner spinnerEstadoRvtoTolva = dialogParte.findViewById(R.id.spinnerEstadoRvtoTolva);
        final Spinner spinnerDuracionRvto = dialogParte.findViewById(R.id.spinnerDuracionRvto);
        final Spinner spinnerDeflectores = dialogParte.findViewById(R.id.spinnerDeflectores);
        final Spinner spinnerAtaqueQuimico = dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
        final Spinner spinnerAtaqueTemperatura = dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
        final Spinner spinnerAtaqueAceites = dialogParte.findViewById(R.id.spinnerAtaqueAceites);
        final Spinner spinnerAtaqueAbrasivo = dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
        final Spinner spinnerHorasTrabajoDia = dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
        final Spinner spinnerDiasTrabajoSemana = dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
        final Spinner spinnerAbrasividad = dialogParte.findViewById(R.id.spinnerAbrasividad);
        final Spinner spinnerPorcentajeFinos = dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
        final Spinner spinnerCajaColaTolva = dialogParte.findViewById(R.id.spinnerCajaColaTolva);
        final Spinner spinnerFugaMateriales = dialogParte.findViewById(R.id.spinnerFugaDeMateriales);
        final Spinner spinnerFugaMatrialesCola = dialogParte.findViewById(R.id.spinnerFugaMaterialColaChute);
        final Spinner spinnerFugaMaterialesCostados = dialogParte.findViewById(R.id.spinnerFugaMaterialCostados);
        final Spinner spinnerFugaMaterialesParticulados = dialogParte.findViewById(R.id.spinnerFugaMaterialParticulado);
        final Spinner spinnerSistemaSujecion = dialogParte.findViewById(R.id.spinnerAbrazadera);
        final Spinner spinnerCauchoGuardaBandas = dialogParte.findViewById(R.id.spinnerCauchoGuardabandas);
        final Spinner spinnerTreSealMultiSeal = dialogParte.findViewById(R.id.spinnerTriSealMultiSeal);
        final Spinner spinnerProtectorGuardaBandas = dialogParte.findViewById(R.id.spinnerProtectorGuardaBandas);
        final Spinner spinnerCortina1 = dialogParte.findViewById(R.id.spinnerCortina1);
        final Spinner spinnerCortina2 = dialogParte.findViewById(R.id.spinnerCortina2);
        final Spinner spinnerCortina3 = dialogParte.findViewById(R.id.spinnerCortina3);
        final Spinner spinnerBoquillasAire = dialogParte.findViewById(R.id.spinnerBoquillasDeAire);
        final Spinner spinnerAlimentacionCentrada = dialogParte.findViewById(R.id.spinnerAlimentacionCentrada);
        final Spinner spinnerAtaqueImpacto = dialogParte.findViewById(R.id.spinnerAtaqueImpacto);

        final TextInputEditText txtAlturaCaida=dialogParte.findViewById(R.id.txtAlturaCaida);
        final TextInputEditText txtLongitudImpacto=dialogParte.findViewById(R.id.txtLongitudImpacto);
        final TextInputEditText txtMaterial=dialogParte.findViewById(R.id.txtMaterial);
        final TextInputEditText txtMaxGranulometria=dialogParte.findViewById(R.id.txtMaxGranulometria);
        final TextInputEditText txtTempMaxMaterialBanda=dialogParte.findViewById(R.id.txtTempMaxSobreBanda);
        final TextInputEditText txtTempPromedioBanda=dialogParte.findViewById(R.id.txtTempPromedioBanda);
        final TextInputEditText txtMaxPeso=dialogParte.findViewById(R.id.txtMaxPeso);
        final TextInputEditText txtDensidad=dialogParte.findViewById(R.id.txtDensidadMaterial);
        final TextInputEditText txtAnchoChute=dialogParte.findViewById(R.id.txtAnchoChute);
        final TextInputEditText txtLargoChute=dialogParte.findViewById(R.id.txtLargoChute);
        final TextInputEditText txtAlturaChute=dialogParte.findViewById(R.id.txtAlturaChute);
        final TextInputEditText txtEspesorGuardabandas=dialogParte.findViewById(R.id.txtEspesorGuardabandas);
        final TextInputEditText txtAnchoGuardabandas=dialogParte.findViewById(R.id.txtAnchoGuardaBandas);
        final TextInputEditText txtLargoGuardabandas=dialogParte.findViewById(R.id.txtLargoGuardaBandas);
        final TextInputEditText txtTempAmbienteMinimaHorizontal=dialogParte.findViewById(R.id.txtTempAmbienteMinimaHorizontal);
        final TextInputEditText txtTempAmbienteMaximaHorizontal=dialogParte.findViewById(R.id.txtTempAmbienteMaximaHorizontal);
        final TextInputEditText txtAnguloSobrecarga=dialogParte.findViewById(R.id.txtAnguloSobreCarga);
        final TextInputEditText txtCapacidadHorizontal=dialogParte.findViewById(R.id.txtCapacidadHorizontal);

        ArrayAdapter<String> adapterTipoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRevestimiento);
        spinnerTipoRevestimientoTolva.setAdapter(adapterTipoRvtoTolva);

        ArrayAdapter<String> adapterEstadoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvtoTolva.setAdapter(adapterEstadoRvtoTolva);

        ArrayAdapter<String> adapterDuracionRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.meses);
        spinnerDuracionRvto.setAdapter(adapterDuracionRvto);

        ArrayAdapter<String> adapterDeflectores = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerDeflectores.setAdapter(adapterDeflectores);


        ArrayAdapter<String> adapterMonitorPeligro = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueQuimico.setAdapter(adapterMonitorPeligro);
        spinnerAlimentacionCentrada.setAdapter(adapterMonitorPeligro);
        spinnerAtaqueImpacto.setAdapter(adapterMonitorPeligro);

        ArrayAdapter<String> adapterRodamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueTemperatura.setAdapter(adapterRodamiento);

        ArrayAdapter<String> adapterMonitorDesalineacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAceites.setAdapter(adapterMonitorDesalineacion);

        ArrayAdapter<String> adapterMonitorVelocidad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAbrasivo.setAdapter(adapterMonitorVelocidad);

        ArrayAdapter<String> adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
        spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

        ArrayAdapter<String> adapterIndicadorNivel = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
        spinnerDiasTrabajoSemana.setAdapter(adapterIndicadorNivel);

        ArrayAdapter<String> adapterCajaUnion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
        spinnerAbrasividad.setAdapter(adapterCajaUnion);

        ArrayAdapter<String> adapterAlarmaPantalla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
        spinnerPorcentajeFinos.setAdapter(adapterAlarmaPantalla);

        ArrayAdapter<String> adapterCajaCola = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerCajaColaTolva.setAdapter(adapterCajaCola);

        ArrayAdapter<String> adapterFugaMateriales = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.fugaDeMateriales);
        spinnerFugaMateriales.setAdapter(adapterFugaMateriales);
        spinnerFugaMatrialesCola.setAdapter(adapterMonitorPeligro);
        spinnerFugaMaterialesCostados.setAdapter(adapterMonitorPeligro);
        spinnerFugaMaterialesParticulados.setAdapter(adapterMonitorPeligro);

        spinnerSistemaSujecion.setAdapter(adapterEstadoRvtoTolva);
        spinnerCauchoGuardaBandas.setAdapter(adapterEstadoRvtoTolva);
        spinnerTreSealMultiSeal.setAdapter(adapterEstadoRvtoTolva);

        spinnerCortina1.setAdapter(adapterEstadoRvtoTolva);
        spinnerCortina2.setAdapter(adapterEstadoRvtoTolva);
        spinnerCortina3.setAdapter(adapterEstadoRvtoTolva);
        spinnerBoquillasAire.setAdapter(adapterEstadoRvtoTolva);

        ArrayAdapter<String> adapterProtectorGuardaBandas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.protectorGuardaBandas);
        spinnerProtectorGuardaBandas.setAdapter(adapterProtectorGuardaBandas);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Condicion Carga");
        }

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroCondicionCargaTransportadora";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                                                    params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                                                    params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                                                    params.put("altureCaida", txtAlturaCaida.getText().toString());
                                                    params.put("longitudImpacto", txtLongitudImpacto.getText().toString());
                                                    params.put("material", txtMaterial.getText().toString());
                                                    params.put("anguloSobreCarga", txtAnguloSobrecarga.getText().toString());
                                                    params.put("ataqueQuimicoTransportadora", spinnerAtaqueQuimico.getSelectedItem().toString());
                                                    params.put("ataqueTemperaturaTransportadora", spinnerAtaqueTemperatura.getSelectedItem().toString());
                                                    params.put("ataqueAceiteTransportadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                                    params.put("ataqueImpactoTransportadora", spinnerAtaqueImpacto.getSelectedItem().toString());
                                                    params.put("horasTrabajoPorDiaTransportadora", spinnerHorasTrabajoDia.getSelectedItem().toString());
                                                    params.put("diasTrabajPorSemanaTransportadora", spinnerDiasTrabajoSemana.getSelectedItem().toString());
                                                    params.put("alimentacionCentradaTransportadora", spinnerAlimentacionCentrada.getSelectedItem().toString());
                                                    params.put("abrasividadTransportadora", spinnerAbrasividad.getSelectedItem().toString());
                                                    params.put("porcentajeFinosTransportadora", spinnerPorcentajeFinos.getSelectedItem().toString());
                                                    params.put("maxGranulometriaTransportadora", txtMaxGranulometria.getText().toString());
                                                    params.put("maxPesoTransportadora", txtMaxPeso.getText().toString());
                                                    params.put("densidadTransportadora", txtDensidad.getText().toString());
                                                    params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getText().toString());
                                                    params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getText().toString());
                                                    params.put("cajaColaDeTolva", spinnerCajaColaTolva.getSelectedItem().toString());
                                                    params.put("fugaMateriales", spinnerFugaMateriales.getSelectedItem().toString());
                                                    params.put("fugaDeMaterialesEnLaColaDelChute", spinnerFugaMatrialesCola.getSelectedItem().toString());
                                                    params.put("fugaDeMaterialesPorLosCostados", spinnerFugaMaterialesCostados.getSelectedItem().toString());
                                                    params.put("anchoChute", txtAnchoChute.getText().toString());
                                                    params.put("largoChute", txtLargoChute.getText().toString());
                                                    params.put("alturaChute", txtAlturaChute.getText().toString());
                                                    params.put("abrazadera", spinnerSistemaSujecion.getSelectedItem().toString());
                                                    params.put("cauchoGuardabandas", spinnerCauchoGuardaBandas.getSelectedItem().toString());
                                                    params.put("fugaDeMaterialParticulaALaSalidaDelChute", spinnerFugaMaterialesParticulados.getSelectedItem().toString());
                                                    params.put("triSealMultiSeal", spinnerTreSealMultiSeal.getSelectedItem().toString());
                                                    params.put("espesorGuardaBandas", txtEspesorGuardabandas.getText().toString());
                                                    params.put("anchoGuardaBandas", txtAnchoGuardabandas.getText().toString());
                                                    params.put("largoGuardaBandas", txtLargoGuardabandas.getText().toString());
                                                    params.put("protectorGuardaBandas", spinnerProtectorGuardaBandas.getSelectedItem().toString());
                                                    params.put("cortinaAntiPolvo1", spinnerCortina1.getSelectedItem().toString());
                                                    params.put("cortinaAntiPolvo2", spinnerCortina2.getSelectedItem().toString());
                                                    params.put("cortinaAntiPolvo3", spinnerCortina3.getSelectedItem().toString());
                                                    params.put("boquillasCanonesDeAire", spinnerBoquillasAire.getSelectedItem().toString());
                                                    params.put("ataqueAbrasivoTransportadora", spinnerAtaqueAbrasivo.getSelectedItem().toString());
                                                    params.put("capacidadTransportadora", txtCapacidadHorizontal.getText().toString());
                                                    params.put("tempAmbienteMaxTransportadora", txtTempAmbienteMaximaHorizontal.getText().toString());
                                                    params.put("tempAmbienteMinTransportadora", txtTempAmbienteMinimaHorizontal.getText().toString());

                                                    if(!txtAlturaCaida.getText().equals(""))
                                                    {
                                                        params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));
                                                    }
                                                    if(!txtLongitudImpacto.getText().equals(""))
                                                    {
                                                        params.put("longitudImpacto", String.valueOf(Float.parseFloat(txtLongitudImpacto.getText().toString())));
                                                    }
                                                    if(!txtAnguloSobrecarga.getText().equals(""))
                                                    {
                                                        params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                                                    }
                                                    if(!txtMaxGranulometria.getText().equals(""))
                                                    {
                                                        params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                                                    }
                                                    if(!txtMaxPeso.getText().equals(""))
                                                    {
                                                        params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                                                    }
                                                    if(!txtDensidad.getText().equals(""))
                                                    {
                                                        params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                                                    }
                                                    if(!txtTempMaxMaterialBanda.getText().equals(""))
                                                    {
                                                        params.put("tempMaximaMaterialSobreBandaTransportadora", String.valueOf(Float.parseFloat(txtTempMaxMaterialBanda.getText().toString())));
                                                    }
                                                    if(!txtTempPromedioBanda.getText().equals(""))
                                                    {
                                                        params.put("tempPromedioMaterialSobreBandaTransportadora", String.valueOf(Float.parseFloat(txtTempPromedioBanda.getText().toString())));
                                                    }
                                                    if(!txtAnchoChute.getText().equals(""))
                                                    {
                                                        params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                                                    }
                                                    if(!txtLargoChute.getText().equals(""))
                                                    {
                                                        params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                                                    }
                                                    if(!txtAlturaChute.getText().equals(""))
                                                    {
                                                        params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                                                    }
                                                    if(!txtAnchoGuardabandas.getText().equals(""))
                                                    {
                                                        params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                                                    }
                                                    if(!txtLargoGuardabandas.getText().equals(""))
                                                    {
                                                        params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                                                    }
                                                    if(!txtCapacidadHorizontal.getText().equals(""))
                                                    {
                                                        params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                                                    }
                                                    if(!txtTempAmbienteMaximaHorizontal.getText().equals(""))
                                                    {
                                                        params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                                                    }
                                                    if(!txtTempAmbienteMinimaHorizontal.getText().equals(""))
                                                    {
                                                        params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                                                    }
                                                    if(!txtEspesorGuardabandas.getText().equals(""))
                                                    {
                                                        params.put("espesorGuardaBandas", String.valueOf(Float.parseFloat(txtEspesorGuardabandas.getText().toString())));
                                                    }




                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarCondicionCargaTransportadora";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                                    params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                                    params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                                    params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                                    params.put("altureCaida", txtAlturaCaida.getText().toString());
                                    params.put("longitudImpacto", txtLongitudImpacto.getText().toString());
                                    params.put("material", txtMaterial.getText().toString());
                                    params.put("anguloSobreCarga", txtAnguloSobrecarga.getText().toString());
                                    params.put("ataqueQuimicoTransportadora", spinnerAtaqueQuimico.getSelectedItem().toString());
                                    params.put("ataqueTemperaturaTransportadora", spinnerAtaqueTemperatura.getSelectedItem().toString());
                                    params.put("ataqueAceiteTransportadora", spinnerAtaqueAceites.getSelectedItem().toString());
                                    params.put("ataqueImpactoTransportadora", spinnerAtaqueImpacto.getSelectedItem().toString());
                                    params.put("horasTrabajoPorDiaTransportadora", spinnerHorasTrabajoDia.getSelectedItem().toString());
                                    params.put("diasTrabajPorSemanaTransportadora", spinnerDiasTrabajoSemana.getSelectedItem().toString());
                                    params.put("alimentacionCentradaTransportadora", spinnerAlimentacionCentrada.getSelectedItem().toString());
                                    params.put("abrasividadTransportadora", spinnerAbrasividad.getSelectedItem().toString());
                                    params.put("porcentajeFinosTransportadora", spinnerPorcentajeFinos.getSelectedItem().toString());
                                    params.put("maxGranulometriaTransportadora", txtMaxGranulometria.getText().toString());
                                    params.put("maxPesoTransportadora", txtMaxPeso.getText().toString());
                                    params.put("densidadTransportadora", txtDensidad.getText().toString());
                                    params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getText().toString());
                                    params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getText().toString());
                                    params.put("cajaColaDeTolva", spinnerCajaColaTolva.getSelectedItem().toString());
                                    params.put("fugaMateriales", spinnerFugaMateriales.getSelectedItem().toString());
                                    params.put("fugaDeMaterialesEnLaColaDelChute", spinnerFugaMatrialesCola.getSelectedItem().toString());
                                    params.put("fugaDeMaterialesPorLosCostados", spinnerFugaMaterialesCostados.getSelectedItem().toString());
                                    params.put("anchoChute", txtAnchoChute.getText().toString());
                                    params.put("largoChute", txtLargoChute.getText().toString());
                                    params.put("alturaChute", txtAlturaChute.getText().toString());
                                    params.put("abrazadera", spinnerSistemaSujecion.getSelectedItem().toString());
                                    params.put("cauchoGuardabandas", spinnerCauchoGuardaBandas.getSelectedItem().toString());
                                    params.put("fugaDeMaterialParticulaALaSalidaDelChute", spinnerFugaMaterialesParticulados.getSelectedItem().toString());
                                    params.put("triSealMultiSeal", spinnerTreSealMultiSeal.getSelectedItem().toString());
                                    params.put("espesorGuardaBandas", txtEspesorGuardabandas.getText().toString());
                                    params.put("anchoGuardaBandas", txtAnchoGuardabandas.getText().toString());
                                    params.put("largoGuardaBandas", txtLargoGuardabandas.getText().toString());
                                    params.put("protectorGuardaBandas", spinnerProtectorGuardaBandas.getSelectedItem().toString());
                                    params.put("cortinaAntiPolvo1", spinnerCortina1.getSelectedItem().toString());
                                    params.put("cortinaAntiPolvo2", spinnerCortina2.getSelectedItem().toString());
                                    params.put("cortinaAntiPolvo3", spinnerCortina3.getSelectedItem().toString());
                                    params.put("boquillasCanonesDeAire", spinnerBoquillasAire.getSelectedItem().toString());
                                    params.put("ataqueAbrasivoTransportadora", spinnerAtaqueAbrasivo.getSelectedItem().toString());
                                    params.put("capacidadTransportadora", txtCapacidadHorizontal.getText().toString());
                                    params.put("tempAmbienteMaxTransportadora", txtTempAmbienteMaximaHorizontal.getText().toString());
                                    params.put("tempAmbienteMinTransportadora", txtTempAmbienteMinimaHorizontal.getText().toString());
                                    if(!txtAlturaCaida.getText().equals(""))
                                    {
                                        params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));
                                    }
                                    if(!txtLongitudImpacto.getText().equals(""))
                                    {
                                        params.put("longitudImpacto", String.valueOf(Float.parseFloat(txtLongitudImpacto.getText().toString())));
                                    }
                                    if(!txtAnguloSobrecarga.getText().equals(""))
                                    {
                                        params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                                    }
                                    if(!txtMaxGranulometria.getText().equals(""))
                                    {
                                        params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                                    }
                                    if(!txtMaxPeso.getText().equals(""))
                                    {
                                        params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                                    }
                                    if(!txtDensidad.getText().equals(""))
                                    {
                                        params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                                    }
                                    if(!txtTempMaxMaterialBanda.getText().equals(""))
                                    {
                                        params.put("tempMaximaMaterialSobreBandaTransportadora", String.valueOf(Float.parseFloat(txtTempMaxMaterialBanda.getText().toString())));
                                    }
                                    if(!txtTempPromedioBanda.getText().equals(""))
                                    {
                                        params.put("tempPromedioMaterialSobreBandaTransportadora", String.valueOf(Float.parseFloat(txtTempPromedioBanda.getText().toString())));
                                    }
                                    if(!txtAnchoChute.getText().equals(""))
                                    {
                                        params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                                    }
                                    if(!txtLargoChute.getText().equals(""))
                                    {
                                        params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                                    }
                                    if(!txtAlturaChute.getText().equals(""))
                                    {
                                        params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                                    }
                                    if(!txtAnchoGuardabandas.getText().equals(""))
                                    {
                                        params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                                    }
                                    if(!txtLargoGuardabandas.getText().equals(""))
                                    {
                                        params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                                    }
                                    if(!txtCapacidadHorizontal.getText().equals(""))
                                    {
                                        params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                                    }
                                    if(!txtTempAmbienteMaximaHorizontal.getText().equals(""))
                                    {
                                        params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                                    }
                                    if(!txtTempAmbienteMinimaHorizontal.getText().equals(""))
                                    {
                                        params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                                    }
                                    if(!txtEspesorGuardabandas.getText().equals(""))
                                    {
                                        params.put("espesorGuardaBandas", String.valueOf(Float.parseFloat(txtEspesorGuardabandas.getText().toString())));
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogSoporteCarga() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_soporte_carga_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerTieneRodillosImpacto = dialogParte.findViewById(R.id.spinnerTieneRodillosImpacto);
        final Spinner spinnerCamaImpacto = dialogParte.findViewById(R.id.spinnerCamaImpacto);
        final Spinner spinnerCamaSellado = dialogParte.findViewById(R.id.spinnerCamaSellado);
        final Spinner spinnerRodillosCarga = dialogParte.findViewById(R.id.spinnerRodillosCargaHorizontal);
        final Spinner spinnerRodillosImpacto = dialogParte.findViewById(R.id.spinnerRodillosImpactoHorizontal);
        final Spinner spinnerBasculaASGCO = dialogParte.findViewById(R.id.spinnerBaculaASGCO);
        final Spinner spinnerBarrasImpacto = dialogParte.findViewById(R.id.spinnerBarraImpacto);
        final Spinner spinnerBarrasDeslizamiento = dialogParte.findViewById(R.id.spinnerBarraDeslizamiento);
        final Spinner spinnerIntegridadSoportesRodilloImpacto = dialogParte.findViewById(R.id.spinnerIntegridadSoportesRodilloImpacto);
        final Spinner spinnerIntegridadSoportesCamaImpactoSellado = dialogParte.findViewById(R.id.spinnerIntegridadSoportesCamaImpacto);
        final Spinner spinnerMaterialAtrapadoCortinas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEntreCortinas);
        final Spinner spinnerMaterialAtrapadoGuardaBandas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnGuardaBanda);
        final Spinner spinnerMaterialAtrapadoBanda = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnBanda);
        final Spinner spinnerInclinacionZonaCargue = dialogParte.findViewById(R.id.spinnerInclinacionZonaCargue);
        final Spinner spinnerTipoRodilloRC= dialogParte.findViewById(R.id.spinnerTipoRodilloRC);
        final Spinner spinnerTipoRodilloRI= dialogParte.findViewById(R.id.spinnerTipoRodilloRI);
        final Spinner spinnerBasculaPesaje= dialogParte.findViewById(R.id.spinnerBasculaPesaje);


        final Spinner spinnerAnguloAcanalmiento1artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaCola);
        final Spinner spinnerAnguloAcanalmiento2artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaCola);
        final Spinner spinnerAnguloAcanalmiento3artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaCola);
        final Spinner spinnerAnguloAcanalmiento1artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaMotriz);
        final Spinner spinnerAnguloAcanalmiento2artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaMotriz);
        final Spinner spinnerAnguloAcanalmiento3artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaMotriz);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterAcanalamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.angulosAcanalamiento);
        ArrayAdapter<String> adapterInclinacionZonaCarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.inclinacionZonaCarga);
        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);

        spinnerTieneRodillosImpacto.setAdapter(adapterSiNo);
        spinnerCamaImpacto.setAdapter(adapterSiNo);
        spinnerCamaSellado.setAdapter(adapterSiNo);
        spinnerRodillosCarga.setAdapter(adapterEstadoPartes);
        spinnerRodillosImpacto.setAdapter(adapterEstadoPartes);
        spinnerBasculaASGCO.setAdapter(adapterEstadoPartes);
        spinnerBarrasImpacto.setAdapter(adapterEstadoPartes);
        spinnerBarrasDeslizamiento.setAdapter(adapterEstadoPartes);
        spinnerIntegridadSoportesCamaImpactoSellado.setAdapter(adapterEstadoPartes);
        spinnerIntegridadSoportesRodilloImpacto.setAdapter(adapterEstadoPartes);
        spinnerMaterialAtrapadoBanda.setAdapter(adapterSiNo);
        spinnerMaterialAtrapadoCortinas.setAdapter(adapterSiNo);
        spinnerMaterialAtrapadoGuardaBandas.setAdapter(adapterSiNo);
        spinnerInclinacionZonaCargue.setAdapter(adapterInclinacionZonaCarga);
        spinnerTipoRodilloRC.setAdapter(adapterTipoRodillo);
        spinnerTipoRodilloRI.setAdapter(adapterTipoRodillo);
        spinnerBasculaPesaje.setAdapter(adapterSiNo);


        spinnerAnguloAcanalmiento1artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento2artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento3artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento1artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento2artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento3artesaPoleaMotriz.setAdapter(adapterAcanalamiento);

        final TextInputEditText txtLargoEjeRodilloCentral=dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
        final TextInputEditText txtDiametroEjeRodilloCentral=dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
        final TextInputEditText txtDiametroRodilloCentral=dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
        final TextInputEditText txtLargoTuboRodilloCentral=dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
        final TextInputEditText txtLargoEjeRodilloLateral=dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral=dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral=dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral=dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtAnchoInternoChasis=dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis=dialogParte.findViewById(R.id.txtAnchoExternoChasis);
        final TextInputEditText txtEspesorUHMV=dialogParte.findViewById(R.id.txtEspesorUHMV);
        final TextInputEditText txtAnchoBarra=dialogParte.findViewById(R.id.txtAnchoBarra);
        final TextInputEditText txtLargoBarra=dialogParte.findViewById(R.id.txtLargoBarra);


        final TextInputEditText txtDetalleRodilloCentral=dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
        final TextInputEditText txtDetalleRodilloLateral=dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);


        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Soporte Carga");
        }

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroSoporteCarga";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("tieneRodillosImpacto", spinnerTieneRodillosImpacto.getSelectedItem().toString());
                                                    params.put("camaImpacto", spinnerCamaImpacto.getSelectedItem().toString());
                                                    params.put("camaSellado", spinnerCamaSellado.getSelectedItem().toString());
                                                    params.put("basculaPesaje", spinnerBasculaPesaje.getSelectedItem().toString());
                                                    params.put("rodilloCarga", spinnerRodillosCarga.getSelectedItem().toString());
                                                    params.put("rodilloImpacto", spinnerRodillosImpacto.getSelectedItem().toString());
                                                    params.put("barraImpacto", spinnerBarrasImpacto.getSelectedItem().toString());
                                                    params.put("basculaASGCO", spinnerBasculaASGCO.getSelectedItem().toString());
                                                    params.put("barraDeslizamiento", spinnerBarrasDeslizamiento.getSelectedItem().toString());
                                                    params.put("espesorUHMV", txtEspesorUHMV.getText().toString());
                                                    params.put("anchoBarra", txtAnchoBarra.getText().toString());
                                                    params.put("largoBarra", txtLargoBarra.getText().toString());
                                                    params.put("anguloAcanalamientoArtesa1", spinnerAnguloAcanalmiento1artesaPoleaCola.getSelectedItem().toString());
                                                    params.put("anguloAcanalamientoArtesa2", spinnerAnguloAcanalmiento2artesaPoleaCola.getSelectedItem().toString());
                                                    params.put("anguloAcanalamientoArtesa3", spinnerAnguloAcanalmiento3artesaPoleaCola.getSelectedItem().toString());
                                                    params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", spinnerAnguloAcanalmiento1artesaPoleaMotriz.getSelectedItem().toString());
                                                    params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", spinnerAnguloAcanalmiento2artesaPoleaMotriz.getSelectedItem().toString());
                                                    params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", spinnerAnguloAcanalmiento3artesaPoleaMotriz.getSelectedItem().toString());
                                                    params.put("materialAtrapadoEntreCortinas", spinnerMaterialAtrapadoCortinas.getSelectedItem().toString());
                                                    params.put("materialAtrapadoEntreGuardabandas", spinnerMaterialAtrapadoGuardaBandas.getSelectedItem().toString());
                                                    params.put("materialAtrapadoEnBanda", spinnerMaterialAtrapadoBanda.getSelectedItem().toString());
                                                    params.put("integridadSoportesCamaImpacto", spinnerIntegridadSoportesCamaImpactoSellado.getSelectedItem().toString());
                                                    params.put("integridadSoportesRodilloImpacto", spinnerIntegridadSoportesRodilloImpacto.getSelectedItem().toString());
                                                    params.put("integridadSoporteCamaSellado", spinnerIntegridadSoportesCamaImpactoSellado.getSelectedItem().toString());
                                                    params.put("inclinacionZonaCargue", spinnerInclinacionZonaCargue.getSelectedItem().toString());
                                                    params.put("largoEjeRodilloCentralCarga", txtLargoEjeRodilloCentral.getText().toString());
                                                    params.put("diametroEjeRodilloCentralCarga", txtDiametroEjeRodilloCentral.getText().toString());
                                                    params.put("largoTuboRodilloCentralCarga", txtLargoTuboRodilloCentral.getText().toString());
                                                    params.put("largoEjeRodilloLateralCarga", txtLargoEjeRodilloLateral.getText().toString());
                                                    params.put("diametroEjeRodilloLateralCarga", txtDiametroEjeRodilloLateral.getText().toString());
                                                    params.put("diametroRodilloLateralCarga", txtDiametroRodilloLateral.getText().toString());
                                                    params.put("largoTuboRodilloLateralCarga", txtLargoTuboRodilloLateral.getText().toString());
                                                    params.put("tipoRodilloCarga", spinnerTipoRodilloRC.getSelectedItem().toString());
                                                    params.put("anchoInternoChasisRodilloCarga", txtAnchoInternoChasis.getText().toString());
                                                    params.put("anchoExternoChasisRodilloCarga", txtAnchoExternoChasis.getText().toString());
                                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCentral.getText().toString());
                                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloLateral.getText().toString());
                                                    params.put("tipoRodilloImpacto", spinnerTipoRodilloRI.getSelectedItem().toString());
                                                    params.put("diametroRodilloCentralCarga", txtDiametroRodilloCentral.getText().toString());

                                                    if(!txtEspesorUHMV.getText().equals(""))
                                                    {
                                                        params.put("espesorUHMV", String.valueOf(Float.parseFloat(txtEspesorUHMV.getText().toString())));
                                                    }
                                                    if(!txtAnchoBarra.getText().equals(""))
                                                    {
                                                        params.put("anchoBarra", String.valueOf(Float.parseFloat(txtAnchoBarra.getText().toString())));

                                                    }
                                                    if(!txtLargoBarra.getText().equals(""))
                                                    {
                                                        params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                                                    }
                                                    if(!txtLargoEjeRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                                                    }
                                                    if(!txtDiametroEjeRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                                                    }
                                                    if(!txtLargoTuboRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                                                    }
                                                    if(!txtLargoEjeRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtDiametroEjeRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtDiametroRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtLargoTuboRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtAnchoInternoChasis.getText().equals(""))
                                                    {
                                                        params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                                                    }
                                                    if(!txtAnchoExternoChasis.getText().equals(""))
                                                    {
                                                        params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                                                    }
                                                    if(!txtDiametroRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

                                                    }



                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarSoporteCarga";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("tieneRodillosImpacto", spinnerTieneRodillosImpacto.getSelectedItem().toString());
                                    params.put("camaImpacto", spinnerCamaImpacto.getSelectedItem().toString());
                                    params.put("camaSellado", spinnerCamaSellado.getSelectedItem().toString());
                                    params.put("basculaPesaje", spinnerBasculaPesaje.getSelectedItem().toString());
                                    params.put("rodilloCarga", spinnerRodillosCarga.getSelectedItem().toString());
                                    params.put("rodilloImpacto", spinnerRodillosImpacto.getSelectedItem().toString());
                                    params.put("barraImpacto", spinnerBarrasImpacto.getSelectedItem().toString());
                                    params.put("basculaASGCO", spinnerBasculaASGCO.getSelectedItem().toString());
                                    params.put("barraDeslizamiento", spinnerBarrasDeslizamiento.getSelectedItem().toString());
                                    params.put("espesorUHMV", txtEspesorUHMV.getText().toString());
                                    params.put("anchoBarra", txtAnchoBarra.getText().toString());
                                    params.put("largoBarra", txtLargoBarra.getText().toString());
                                    params.put("anguloAcanalamientoArtesa1", spinnerAnguloAcanalmiento1artesaPoleaCola.getSelectedItem().toString());
                                    params.put("anguloAcanalamientoArtesa2", spinnerAnguloAcanalmiento2artesaPoleaCola.getSelectedItem().toString());
                                    params.put("anguloAcanalamientoArtesa3", spinnerAnguloAcanalmiento3artesaPoleaCola.getSelectedItem().toString());
                                    params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", spinnerAnguloAcanalmiento1artesaPoleaMotriz.getSelectedItem().toString());
                                    params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", spinnerAnguloAcanalmiento2artesaPoleaMotriz.getSelectedItem().toString());
                                    params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", spinnerAnguloAcanalmiento3artesaPoleaMotriz.getSelectedItem().toString());
                                    params.put("materialAtrapadoEntreCortinas", spinnerMaterialAtrapadoCortinas.getSelectedItem().toString());
                                    params.put("materialAtrapadoEntreGuardabandas", spinnerMaterialAtrapadoGuardaBandas.getSelectedItem().toString());
                                    params.put("materialAtrapadoEnBanda", spinnerMaterialAtrapadoBanda.getSelectedItem().toString());
                                    params.put("integridadSoportesCamaImpacto", spinnerIntegridadSoportesCamaImpactoSellado.getSelectedItem().toString());
                                    params.put("integridadSoportesRodilloImpacto", spinnerIntegridadSoportesRodilloImpacto.getSelectedItem().toString());
                                    params.put("integridadSoporteCamaSellado", spinnerIntegridadSoportesCamaImpactoSellado.getSelectedItem().toString());
                                    params.put("inclinacionZonaCargue", spinnerInclinacionZonaCargue.getSelectedItem().toString());
                                    params.put("largoEjeRodilloCentralCarga", txtLargoEjeRodilloCentral.getText().toString());
                                    params.put("diametroEjeRodilloCentralCarga", txtDiametroEjeRodilloCentral.getText().toString());
                                    params.put("largoTuboRodilloCentralCarga", txtLargoTuboRodilloCentral.getText().toString());
                                    params.put("largoEjeRodilloLateralCarga", txtLargoEjeRodilloLateral.getText().toString());
                                    params.put("diametroEjeRodilloLateralCarga", txtDiametroEjeRodilloLateral.getText().toString());
                                    params.put("diametroRodilloLateralCarga", txtDiametroRodilloLateral.getText().toString());
                                    params.put("largoTuboRodilloLateralCarga", txtLargoTuboRodilloLateral.getText().toString());
                                    params.put("tipoRodilloCarga", spinnerTipoRodilloRC.getSelectedItem().toString());
                                    params.put("anchoInternoChasisRodilloCarga", txtAnchoInternoChasis.getText().toString());
                                    params.put("anchoExternoChasisRodilloCarga", txtAnchoExternoChasis.getText().toString());
                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCentral.getText().toString());
                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloLateral.getText().toString());
                                    params.put("tipoRodilloImpacto", spinnerTipoRodilloRI.getSelectedItem().toString());
                                    params.put("diametroRodilloCentralCarga", txtDiametroRodilloCentral.getText().toString());

                                    if(!txtEspesorUHMV.getText().equals(""))
                                    {
                                        params.put("espesorUHMV", String.valueOf(Float.parseFloat(txtEspesorUHMV.getText().toString())));
                                    }
                                    if(!txtAnchoBarra.getText().equals(""))
                                    {
                                        params.put("anchoBarra", String.valueOf(Float.parseFloat(txtAnchoBarra.getText().toString())));

                                    }
                                    if(!txtLargoBarra.getText().equals(""))
                                    {
                                        params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                                    }
                                    if(!txtLargoEjeRodilloCentral.getText().equals(""))
                                    {
                                        params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                                    }
                                    if(!txtDiametroEjeRodilloCentral.getText().equals(""))
                                    {
                                        params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                                    }
                                    if(!txtLargoTuboRodilloCentral.getText().equals(""))
                                    {
                                        params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                                    }
                                    if(!txtLargoEjeRodilloLateral.getText().equals(""))
                                    {
                                        params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                                    }
                                    if(!txtDiametroEjeRodilloLateral.getText().equals(""))
                                    {
                                        params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                    }
                                    if(!txtDiametroRodilloLateral.getText().equals(""))
                                    {
                                        params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                    }
                                    if(!txtLargoTuboRodilloLateral.getText().equals(""))
                                    {
                                        params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                                    }
                                    if(!txtAnchoInternoChasis.getText().equals(""))
                                    {
                                        params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                                    }
                                    if(!txtAnchoExternoChasis.getText().equals(""))
                                    {
                                        params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                                    }
                                    if(!txtDiametroRodilloCentral.getText().equals(""))
                                    {
                                        params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogAlineacion() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_alineacion_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final Spinner spinnerSistAlineacionCarga = dialogParte.findViewById(R.id.spinnerSistAlineacionCarga);
        final Spinner spinnerFuncionanSACarga = dialogParte.findViewById(R.id.spinnerFuncionanSACarga);
        Spinner spinnerSistAlineacionRetorno = dialogParte.findViewById(R.id.spinnerSistAlineacionRetorno);
        final Spinner spinnerFuncionanSARetorno = dialogParte.findViewById(R.id.spinnerFuncionanSARetorno);
        final Spinner spinnerSistAlineacionRetornoPlano = dialogParte.findViewById(R.id.spinnerSistAlineacionRetornoPlano);
        final Spinner spinnerSistAlineacionArtesaCarga = dialogParte.findViewById(R.id.spinnerSistAlineacionArtesaCarga);
        final Spinner spinnerSistAlineacionV = dialogParte.findViewById(R.id.spinnerSistAlineacionV);

        final TextInputEditText txtCantidadSACCarga = dialogParte.findViewById(R.id.txtCantidadSACCarga);
        final TextInputEditText txtCantidadSACRetorno = dialogParte.findViewById(R.id.txtCantidadSACRetorno);
        final TextInputEditText txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
        final TextInputEditText txtDetalleRodilloCargaLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);
        final TextInputEditText txtDetalleRodilloRetornoPlano = dialogParte.findViewById(R.id.txtDetalleRodilloRetornoPlano);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterEstadoParte = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

        spinnerFuncionanSACarga.setAdapter(adapterSiNo);
        spinnerFuncionanSARetorno.setAdapter(adapterSiNo);
        spinnerSistAlineacionCarga.setAdapter(adapterSiNo);
        spinnerSistAlineacionRetorno.setAdapter(adapterSiNo);

        spinnerSistAlineacionRetornoPlano.setAdapter(adapterEstadoParte);
        spinnerSistAlineacionArtesaCarga.setAdapter(adapterEstadoParte);
        spinnerSistAlineacionV.setAdapter(adapterEstadoParte);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Alineacion");
        }

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroAlineacion";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("cantidadSistemaAlineacionEnCarga", txtCantidadSACCarga.getText().toString());
                                                    params.put("sistemasAlineacionCargaFuncionando", spinnerFuncionanSACarga.getSelectedItem().toString());
                                                    params.put("sistemaAlineacionEnRetorno", spinnerFuncionanSARetorno.getSelectedItem().toString());
                                                    params.put("cantidadSistemaAlineacionEnRetorno", txtCantidadSACRetorno.getText().toString());
                                                    params.put("sistemasAlineacionRetornoFuncionando", spinnerFuncionanSARetorno.getSelectedItem().toString());
                                                    params.put("sistemaAlineacionRetornoPlano", spinnerSistAlineacionRetornoPlano.getSelectedItem().toString());
                                                    params.put("sistemaAlineacionArtesaCarga", spinnerSistAlineacionArtesaCarga.getSelectedItem().toString());
                                                    params.put("sistemaAlineacionRetornoEnV", spinnerSistAlineacionV.getSelectedItem().toString());
                                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCargaCentral.getText().toString());
                                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloCargaLateral.getText().toString());
                                                    params.put("detalleRodilloRetorno", txtDetalleRodilloRetornoPlano.getText().toString());
                                                    params.put("sistemaAlineacionCarga", spinnerSistAlineacionCarga.getSelectedItem().toString());


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarAlineacion";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("cantidadSistemaAlineacionEnCarga", txtCantidadSACCarga.getText().toString());
                                    params.put("sistemasAlineacionCargaFuncionando", spinnerFuncionanSACarga.getSelectedItem().toString());
                                    params.put("sistemaAlineacionEnRetorno", spinnerFuncionanSARetorno.getSelectedItem().toString());
                                    params.put("cantidadSistemaAlineacionEnRetorno", txtCantidadSACRetorno.getText().toString());
                                    params.put("sistemasAlineacionRetornoFuncionando", spinnerFuncionanSARetorno.getSelectedItem().toString());
                                    params.put("sistemaAlineacionRetornoPlano", spinnerSistAlineacionRetornoPlano.getSelectedItem().toString());
                                    params.put("sistemaAlineacionArtesaCarga", spinnerSistAlineacionArtesaCarga.getSelectedItem().toString());
                                    params.put("sistemaAlineacionRetornoEnV", spinnerSistAlineacionV.getSelectedItem().toString());
                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCargaCentral.getText().toString());
                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloCargaLateral.getText().toString());
                                    params.put("detalleRodilloRetorno", txtDetalleRodilloRetornoPlano.getText().toString());
                                    params.put("sistemaAlineacionCarga", spinnerSistAlineacionCarga.getSelectedItem().toString());


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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
    }

    public void abrirDialogPoleaMotriz() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_motriz_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        TextView tvDiametroPolea = dialogParte.findViewById(R.id.tvDiametroPoleaMotrizHorizontal);
        TextView tvAnchoPolea = dialogParte.findViewById(R.id.tvAnchoPoleaMotrizHorizontal);
        TextView tvLargoEje = dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizHorizontal);
        TextView tvDiametroEje = dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizHorizontal);
        TextView tvAnguloAmarre = dialogParte.findViewById(R.id.tvAnguloAmarrePMHorizontal);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizHorizontal);
        final Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
        final Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizHorizontal);
        final Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicionPoleaMotrizHorizontal);
        final Spinner spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePMHorizontal);

        final TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
        final TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizHorizontal);
        final TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
        final TextInputEditText txtHpMotor = dialogParte.findViewById(R.id.txtPotenciaMotorHorizontal);
        final TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPMotrizHorizontal);


        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);


        spinnerTipoPolea.setAdapter(adapterTipoPolea);
        spinnerBandaCentradaEnPolea.setAdapter(adapterSiNo);
        spinnerEstadoRvto.setAdapter(adapterEstadoPartes);
        spinnerGuardaPolea.setAdapter(adapterEstadoPartes);
        spinnerTipoTransicion.setAdapter(adapterTipoTransicion);
        spinnerAnguloAmarre.setAdapter(adapterAnguloAmarre);


        tvAnchoPolea.setOnClickListener(this);
        tvDiametroEje.setOnClickListener(this);
        tvDiametroPolea.setOnClickListener(this);
        tvLargoEje.setOnClickListener(this);
        tvAnguloAmarre.setOnClickListener(this);

        llenarRegistros("Polea Motriz");

        txtDiametro.setOnFocusChangeListener(this);
        txtAncho.setOnFocusChangeListener(this);
        txtDiametroEje.setOnFocusChangeListener(this);
        txtLargoEje.setOnFocusChangeListener(this);
        txtHpMotor.setOnFocusChangeListener(this);
        txtDiametro.setOnFocusChangeListener(this);

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroPoleaMotrizHorizontal";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("diametroPoleaMotrizTransportadora", txtDiametro.getText().toString());
                                                    params.put("anchoPoleaMotrizTransportadora", txtAncho.getText().toString());
                                                    params.put("tipoPoleaMotrizTransportadora", spinnerTipoPolea.getSelectedItem().toString());
                                                    params.put("largoEjePoleaMotrizTransportadora", txtLargoEje.getText().toString());
                                                    params.put("diametroEjeMotrizTransportadora", txtDiametroEje.getText().toString());
                                                    params.put("icobandasCentraEnPoleaMotrizTransportadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                    params.put("anguloAmarrePoleaMotrizTransportadora", spinnerAnguloAmarre.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaMotrizTransportadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                    params.put("tipoTransicionPoleaMotrizTransportadora", spinnerTipoTransicion.getSelectedItem().toString());
                                                    params.put("distanciaTransicionPoleaMotrizTransportadora", txtDistanciaTransicion.getText().toString());
                                                    params.put("potenciaMotorTransportadora", txtHpMotor.getText().toString());
                                                    params.put("guardaPoleaMotrizTransportadora", spinnerGuardaPolea.getSelectedItem().toString());

                                                    if(!txtDiametro.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString()))) ;
                                                    }
                                                    if(!txtAncho.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString()))) ;
                                                    }
                                                    if(!txtLargoEje.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString()))) ;
                                                    }
                                                    if(!txtDiametroEje.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString()))) ;
                                                    }
                                                    if(!txtDistanciaTransicion.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString()))) ;
                                                    }
                                                    if(!txtHpMotor.getText().toString().equals(""))
                                                    {
                                                        params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString()))) ;
                                                    }



                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarPoleaMotrizHorizontal";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("diametroPoleaMotrizTransportadora", txtDiametro.getText().toString());
                                    params.put("anchoPoleaMotrizTransportadora", txtAncho.getText().toString());
                                    params.put("tipoPoleaMotrizTransportadora", spinnerTipoPolea.getSelectedItem().toString());
                                    params.put("largoEjePoleaMotrizTransportadora", txtLargoEje.getText().toString());
                                    params.put("diametroEjeMotrizTransportadora", txtDiametroEje.getText().toString());
                                    params.put("icobandasCentraEnPoleaMotrizTransportadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                    params.put("anguloAmarrePoleaMotrizTransportadora", spinnerAnguloAmarre.getSelectedItem().toString());
                                    params.put("estadoRevestimientoPoleaMotrizTransportadora", spinnerEstadoRvto.getSelectedItem().toString());
                                    params.put("tipoTransicionPoleaMotrizTransportadora", spinnerTipoTransicion.getSelectedItem().toString());
                                    params.put("distanciaTransicionPoleaMotrizTransportadora", txtDistanciaTransicion.getText().toString());
                                    params.put("potenciaMotorTransportadora", txtHpMotor.getText().toString());
                                    params.put("guardaPoleaMotrizTransportadora", spinnerGuardaPolea.getSelectedItem().toString());

                                    if(!txtDiametro.getText().toString().equals(""))
                                    {
                                        params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString()))) ;
                                    }
                                    if(!txtAncho.getText().toString().equals(""))
                                    {
                                        params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString()))) ;
                                    }
                                    if(!txtLargoEje.getText().toString().equals(""))
                                    {
                                        params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString()))) ;
                                    }
                                    if(!txtDiametroEje.getText().toString().equals(""))
                                    {
                                        params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString()))) ;
                                    }
                                    if(!txtDistanciaTransicion.getText().toString().equals(""))
                                    {
                                        params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString()))) ;
                                    }
                                    if(!txtHpMotor.getText().toString().equals(""))
                                    {
                                        params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString()))) ;
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogLimpiadorPrimario() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_limpiador_primario_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerLadoPasarelaSentidoBanda = dialogParte.findViewById(R.id.spinnerLadoPasarelaLP);
        final Spinner spinnerMaterialAlimenticioLP = dialogParte.findViewById(R.id.spinnerMaterialAlimenticioLP);
        Spinner spinnerMaterialAcidoLP = dialogParte.findViewById(R.id.spinnerMaterialAcidoLP);
        final Spinner spinnerMaterial80y150LP = dialogParte.findViewById(R.id.spinnerMat80y15GradosLP);
        final Spinner spinnerMaterialSecoLP = dialogParte.findViewById(R.id.spinnerMaterialSecoLP);
        final Spinner spinnerMaterialHumedoLP = dialogParte.findViewById(R.id.spinnerMaterialHumedoLP);
        final Spinner spinnerMaterialAbrasivoFinoLP = dialogParte.findViewById(R.id.spinnerMaterialAbrasivoFinoLP);
        final Spinner spinnerMaterialPegajosoLP = dialogParte.findViewById(R.id.spinnerMaterialPegajosoLP);
        final Spinner spinnerMaterialAceitosoGrasosoLP = dialogParte.findViewById(R.id.spinnerMaterialAceitosoGrasosoLP);

        final Spinner spinnerMarcaLP = dialogParte.findViewById(R.id.spinnerMarcaLP);
        final Spinner spinnerReferenciaLP = dialogParte.findViewById(R.id.spinnerReferenciaLP);
        final Spinner spinnerEstadoCuchillaLP = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLP);
        final Spinner spinnerEstadoTensorLP = dialogParte.findViewById(R.id.spinnerEstadoTensorLP);
        final Spinner spinnerEstadoTuboLP = dialogParte.findViewById(R.id.spinnerEstadoTuboLP);
        final Spinner spinnerFrecRevisionCuchillaLP = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLP);
        final Spinner spinnerCuchillaEnContactoConBandaLP = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLP);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
        ArrayAdapter<String> adapterLadosPasarela = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.ladoPasarela);
        ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);
        ArrayAdapter<String> adapterReferenciaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.referenciaLimpiador);

        spinnerMaterialAlimenticioLP.setAdapter(adapterSiNo);
        spinnerMaterialAcidoLP.setAdapter(adapterSiNo);
        spinnerMaterial80y150LP.setAdapter(adapterSiNo);
        spinnerMaterialSecoLP.setAdapter(adapterSiNo);
        spinnerMaterialHumedoLP.setAdapter(adapterSiNo);
        spinnerMaterialAbrasivoFinoLP.setAdapter(adapterSiNo);
        spinnerMaterialPegajosoLP.setAdapter(adapterSiNo);
        spinnerMaterialAceitosoGrasosoLP.setAdapter(adapterSiNo);
        spinnerCuchillaEnContactoConBandaLP.setAdapter(adapterSiNo);

        spinnerEstadoCuchillaLP.setAdapter(adapterEstadoPartes);
        spinnerEstadoTuboLP.setAdapter(adapterEstadoPartes);
        spinnerEstadoTensorLP.setAdapter(adapterEstadoPartes);
        spinnerFrecRevisionCuchillaLP.setAdapter(adapterFrecRevisionCuchilla);
        spinnerLadoPasarelaSentidoBanda.setAdapter(adapterLadosPasarela);
        spinnerMarcaLP.setAdapter(adapterMarcaLimpiador);
        spinnerReferenciaLP.setAdapter(adapterReferenciaLimpiador);

        final TextInputEditText txtAnchoEstructura = dialogParte.findViewById(R.id.txtAnchoEstructuraLP);
        final TextInputEditText txtAnchoTrayectoCarga = dialogParte.findViewById(R.id.txtAnchoTrayectoCargaLP);
        final TextInputEditText txtAnchoCuchillaLP = dialogParte.findViewById(R.id.txtAnchoCuchillaLP);
        final TextInputEditText txtAltoCuchillaLP = dialogParte.findViewById(R.id.txtAltoCuchillaLP);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Limpiador Primario");
        }


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroLimpiadorPrimario";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("anchoEstructura", txtAnchoEstructura.getText().toString());
                                                    params.put("anchoTrayectoCarga", txtAnchoTrayectoCarga.getText().toString());
                                                    params.put("pasarelaRespectoAvanceBanda", spinnerLadoPasarelaSentidoBanda.getSelectedItem().toString());
                                                    params.put("materialAlimenticioTransportadora", spinnerMaterialAlimenticioLP.getSelectedItem().toString());
                                                    params.put("materialAcidoTransportadora", spinnerMaterialAceitosoGrasosoLP.getSelectedItem().toString());
                                                    params.put("materialTempEntre80y150Transportadora", spinnerMaterial80y150LP.getSelectedItem().toString());
                                                    params.put("materialSecoTransportadora", spinnerMaterialSecoLP.getSelectedItem().toString());
                                                    params.put("materialAbrasivoFinoTransportadora", spinnerMaterialAbrasivoFinoLP.getSelectedItem().toString());
                                                    params.put("materialPegajosoTransportadora", spinnerMaterialPegajosoLP.getSelectedItem().toString());
                                                    params.put("materialGrasosoAceitosoTransportadora", spinnerMaterialAceitosoGrasosoLP.getSelectedItem().toString());
                                                    params.put("marcaLimpiadorPrimario", spinnerMarcaLP.getSelectedItem().toString());
                                                    params.put("referenciaLimpiadorPrimario", spinnerReferenciaLP.getSelectedItem().toString());
                                                    params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                                                    params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                                                    params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                                                    params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                                                    params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                                                    params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                                                    params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                                                    params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());

                                                    if(!txtAnchoEstructura.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                                                    }
                                                    if(!txtAnchoTrayectoCarga.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                                                    }
                                                    if(!txtAnchoCuchillaLP.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                                                    }
                                                    if(!txtAltoCuchillaLP.getText().toString().equals(""))
                                                    {
                                                        params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
                                                    }


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarLimpiadorPrimario";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("anchoEstructura", txtAnchoEstructura.getText().toString());
                                    params.put("anchoTrayectoCarga", txtAnchoTrayectoCarga.getText().toString());
                                    params.put("pasarelaRespectoAvanceBanda", spinnerLadoPasarelaSentidoBanda.getSelectedItem().toString());
                                    params.put("materialAlimenticioTransportadora", spinnerMaterialAlimenticioLP.getSelectedItem().toString());
                                    params.put("materialAcidoTransportadora", spinnerMaterialAceitosoGrasosoLP.getSelectedItem().toString());
                                    params.put("materialTempEntre80y150Transportadora", spinnerMaterial80y150LP.getSelectedItem().toString());
                                    params.put("materialSecoTransportadora", spinnerMaterialSecoLP.getSelectedItem().toString());
                                    params.put("materialAbrasivoFinoTransportadora", spinnerMaterialAbrasivoFinoLP.getSelectedItem().toString());
                                    params.put("materialPegajosoTransportadora", spinnerMaterialPegajosoLP.getSelectedItem().toString());
                                    params.put("materialGrasosoAceitosoTransportadora", spinnerMaterialAceitosoGrasosoLP.getSelectedItem().toString());
                                    params.put("marcaLimpiadorPrimario", spinnerMarcaLP.getSelectedItem().toString());
                                    params.put("referenciaLimpiadorPrimario", spinnerReferenciaLP.getSelectedItem().toString());
                                    params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                                    params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                                    params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                                    params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                                    params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                                    params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                                    params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                                    params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());

                                    if(!txtAnchoEstructura.getText().toString().equals(""))
                                    {
                                        params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                                    }
                                    if(!txtAnchoTrayectoCarga.getText().toString().equals(""))
                                    {
                                        params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                                    }
                                    if(!txtAnchoCuchillaLP.getText().toString().equals(""))
                                    {
                                        params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                                    }
                                    if(!txtAltoCuchillaLP.getText().toString().equals(""))
                                    {
                                        params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogLimpiadorSecundarioTerciario() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_limpiador_primario_secundario_terciario);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);


        final Spinner spinnerMarcaLS = dialogParte.findViewById(R.id.spinnerMarcaLS);
        final Spinner spinnerReferenciaLS = dialogParte.findViewById(R.id.spinnerReferenciaLS);
        final Spinner spinnerEstadoCuchillaLS = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLS);
        final Spinner spinnerEstadoTensorLS = dialogParte.findViewById(R.id.spinnerEstadoTensorLS);
        final Spinner spinnerEstadoTuboLS = dialogParte.findViewById(R.id.spinnerEstadoTuboLS);
        final Spinner spinnerFrecRevisionCuchillaLS = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLS);
        final Spinner spinnerCuchillaEnContactoConBandaLS = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLS);
        final Spinner spinnerSistemaDribbleChuteLS = dialogParte.findViewById(R.id.spinnerSistemaDribbleChuteLS);

        final Spinner spinnerMarcaLT = dialogParte.findViewById(R.id.spinnerMarcaLT);
        final Spinner spinnerReferenciaLT = dialogParte.findViewById(R.id.spinnerReferenciaLT);
        final Spinner spinnerEstadoCuchillaLT = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLT);
        final Spinner spinnerEstadoTensorLT = dialogParte.findViewById(R.id.spinnerEstadoTensorLT);
        final Spinner spinnerEstadoTuboLT = dialogParte.findViewById(R.id.spinnerEstadoTuboLT);
        final Spinner spinnerFrecRevisionCuchillaLT = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLT);
        final Spinner spinnerCuchillaEnContactoConBandaLT = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLT);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
        ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);
        ArrayAdapter<String> adapterReferenciaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.referenciaLimpiador);

        spinnerMarcaLS.setAdapter(adapterMarcaLimpiador);
        spinnerMarcaLT.setAdapter(adapterMarcaLimpiador);

        spinnerReferenciaLS.setAdapter(adapterReferenciaLimpiador);
        spinnerReferenciaLT.setAdapter(adapterReferenciaLimpiador);

        spinnerEstadoTensorLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoTensorLT.setAdapter(adapterEstadoPartes);

        spinnerEstadoTuboLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoTuboLT.setAdapter(adapterEstadoPartes);

        spinnerCuchillaEnContactoConBandaLS.setAdapter(adapterSiNo);
        spinnerCuchillaEnContactoConBandaLT.setAdapter(adapterSiNo);
        spinnerSistemaDribbleChuteLS.setAdapter(adapterSiNo);


        spinnerEstadoCuchillaLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoCuchillaLT.setAdapter(adapterEstadoPartes);
        final TextInputEditText txtAnchoCuchillaLS=dialogParte.findViewById(R.id.txtAnchoCuchillaLS);
        final TextInputEditText txtAltoCuchillaLS=dialogParte.findViewById(R.id.txtAltoCuchillaLS);

        final TextInputEditText txtAnchoCuchillaLT=dialogParte.findViewById(R.id.txtAnchoCuchillaLT);
        final TextInputEditText txtAltoCuchillaLT=dialogParte.findViewById(R.id.txtAltoCuchillaLT);


        spinnerFrecRevisionCuchillaLS.setAdapter(adapterFrecRevisionCuchilla);
        spinnerFrecRevisionCuchillaLT.setAdapter(adapterFrecRevisionCuchilla);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Limpiador Secundario");
        }

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroLimpiadorSecundario";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                                                    params.put("referenciaLimpiadorSecundario", spinnerReferenciaLS.getSelectedItem().toString());
                                                    params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                                                    params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                                                    params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                                                    params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                                                    params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                                                    params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                                                    params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                                                    params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                                                    params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                                                    params.put("referenciaLimpiadorTerciario", spinnerReferenciaLT.getSelectedItem().toString());
                                                    params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                                                    params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                                                    params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                                                    params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                                                    params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                                                    params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                                                    params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());

                                                    if(!txtAnchoCuchillaLS.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                                                    }
                                                    if(!txtAltoCuchillaLS.getText().toString().equals(""))
                                                    {
                                                        params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                                                    }
                                                    if(!txtAnchoCuchillaLT.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                                                    }
                                                    if(!txtAltoCuchillaLT.getText().toString().equals(""))
                                                    {
                                                        params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
                                                    }


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarLimpiadorSecundaro";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                                    params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                                    params.put("referenciaLimpiadorSecundario", spinnerReferenciaLS.getSelectedItem().toString());
                                    params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                                    params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                                    params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                                    params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                                    params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                                    params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                                    params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                                    params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                                    params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                                    params.put("referenciaLimpiadorTerciario", spinnerReferenciaLT.getSelectedItem().toString());
                                    params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                                    params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                                    params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                                    params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                                    params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                                    params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                                    params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());

                                    if(!txtAnchoCuchillaLS.getText().toString().equals(""))
                                    {
                                        params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                                    }
                                    if(!txtAltoCuchillaLS.getText().toString().equals(""))
                                    {
                                        params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                                    }
                                    if(!txtAnchoCuchillaLT.getText().toString().equals(""))
                                    {
                                        params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                                    }
                                    if(!txtAltoCuchillaLT.getText().toString().equals(""))
                                    {
                                        params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogPoleaAmarre() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_amarre_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerTipoPAmarrePM = dialogParte.findViewById(R.id.spinnerTipoPAmarrePM);
        final Spinner spinnerIcobandasCentradaPAmarrePM = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPM);
        final Spinner spinnerEstadoRvtoPAmarrePM = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePM);

        final Spinner spinnerTipoPAmarrePC = dialogParte.findViewById(R.id.spinnerTipoPAmarrePC);
        final Spinner spinnerIcobandasCentradaPAmarrePC = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPC);
        final Spinner spinnerEstadoRvtoPAmarrePC = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePC);


        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);

        final TextInputEditText txtDiametroPAmarrePM = dialogParte.findViewById(R.id.txtDiametroPAmarrePM);
        final TextInputEditText txtAnchoPAmarrePM = dialogParte.findViewById(R.id.txtAnchoPAmarrePM);
        final TextInputEditText txtDiametroEjePAmarrePM = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePM);
        final TextInputEditText txtLargoEjePAmarrePM = dialogParte.findViewById(R.id.txtLargoEjePAmarrePM);

        final TextInputEditText txtDiametroPAmarrePC = dialogParte.findViewById(R.id.txtDiametroPAmarrePC);
        final TextInputEditText txtAnchoPAmarrePC = dialogParte.findViewById(R.id.txtAnchoPAmarrePC);
        final TextInputEditText txtDiametroEjePAmarrePC = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePC);
        final TextInputEditText txtLargoEjePAmarrePC = dialogParte.findViewById(R.id.txtLargoEjePAmarrePC);

        spinnerEstadoRvtoPAmarrePM.setAdapter(adapterEstadoPartes);
        spinnerEstadoRvtoPAmarrePC.setAdapter(adapterEstadoPartes);
        spinnerIcobandasCentradaPAmarrePC.setAdapter(adapterSiNo);
        spinnerIcobandasCentradaPAmarrePM.setAdapter(adapterSiNo);
        spinnerTipoPAmarrePM.setAdapter(adapterTipoPolea);
        spinnerTipoPAmarrePC.setAdapter(adapterTipoPolea);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Polea Amarre");
        }


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroPoleaAmarre";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("diametroPoleaAmarrePoleaMotriz", txtDiametroPAmarrePM.getText().toString());
                                                    params.put("tipoPoleaAmarrePoleaMotriz", spinnerTipoPAmarrePM.getSelectedItem().toString());
                                                    params.put("largoEjePoleaAmarrePoleaMotriz", txtLargoEjePAmarrePM.getText().toString());
                                                    params.put("diametroEjePoleaAmarrePoleaMotriz", txtDiametroEjePAmarrePM.getText().toString());
                                                    params.put("icobandasCentradaPoleaAmarrePoleaMotriz", spinnerIcobandasCentradaPAmarrePM.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", spinnerEstadoRvtoPAmarrePM.getSelectedItem().toString());
                                                    params.put("anchoPoleaAmarrePoleaMotriz", txtAnchoPAmarrePM.getText().toString());

                                                    params.put("dimetroPoleaAmarrePoleaCola", txtDiametroPAmarrePC.getText().toString());
                                                    params.put("anchoPoleaAmarrePoleaCola", txtAnchoPAmarrePC.getText().toString());
                                                    params.put("tipoPoleaAmarrePoleaCola", spinnerTipoPAmarrePC.getSelectedItem().toString());
                                                    params.put("largoEjePoleaAmarrePoleaCola", txtLargoEjePAmarrePC.getText().toString());
                                                    params.put("diametroEjePoleaAmarrePoleaCola", txtDiametroEjePAmarrePC.getText().toString());
                                                    params.put("icobandasCentradaPoleaAmarrePoleaCola", spinnerIcobandasCentradaPAmarrePC.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaAmarrePoleaCola", spinnerEstadoRvtoPAmarrePC.getSelectedItem().toString());

                                                    if(!txtDiametroPAmarrePM.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                                                    }
                                                    if(!txtLargoEjePAmarrePM.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                                                    }
                                                    if(!txtDiametroEjePAmarrePM.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                                                    }
                                                    if(!txtAnchoPAmarrePM.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                                                    }
                                                    if(!txtDiametroPAmarrePC.getText().toString().equals(""))
                                                    {
                                                        params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                                                    }
                                                    if(!txtAnchoPAmarrePC.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                                                    }
                                                    if(!txtLargoEjePAmarrePC.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjePAmarrePC.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
                                                    }



                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarPoleaAmarre";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("diametroPoleaAmarrePoleaMotriz", txtDiametroPAmarrePM.getText().toString());
                                    params.put("tipoPoleaAmarrePoleaMotriz", spinnerTipoPAmarrePM.getSelectedItem().toString());
                                    params.put("largoEjePoleaAmarrePoleaMotriz", txtLargoEjePAmarrePM.getText().toString());
                                    params.put("diametroEjePoleaAmarrePoleaMotriz", txtDiametroEjePAmarrePM.getText().toString());
                                    params.put("icobandasCentradaPoleaAmarrePoleaMotriz", spinnerIcobandasCentradaPAmarrePM.getSelectedItem().toString());
                                    params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", spinnerEstadoRvtoPAmarrePM.getSelectedItem().toString());
                                    params.put("anchoPoleaAmarrePoleaMotriz", txtAnchoPAmarrePM.getText().toString());

                                    params.put("dimetroPoleaAmarrePoleaCola", txtDiametroPAmarrePC.getText().toString());
                                    params.put("anchoPoleaAmarrePoleaCola", txtAnchoPAmarrePC.getText().toString());
                                    params.put("tipoPoleaAmarrePoleaCola", spinnerTipoPAmarrePC.getSelectedItem().toString());
                                    params.put("largoEjePoleaAmarrePoleaCola", txtLargoEjePAmarrePC.getText().toString());
                                    params.put("diametroEjePoleaAmarrePoleaCola", txtDiametroEjePAmarrePC.getText().toString());
                                    params.put("icobandasCentradaPoleaAmarrePoleaCola", spinnerIcobandasCentradaPAmarrePC.getSelectedItem().toString());
                                    params.put("estadoRevestimientoPoleaAmarrePoleaCola", spinnerEstadoRvtoPAmarrePC.getSelectedItem().toString());

                                    if(!txtDiametroPAmarrePM.getText().toString().equals(""))
                                    {
                                        params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                                    }
                                    if(!txtLargoEjePAmarrePM.getText().toString().equals(""))
                                    {
                                        params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                                    }
                                    if(!txtDiametroEjePAmarrePM.getText().toString().equals(""))
                                    {
                                        params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                                    }
                                    if(!txtAnchoPAmarrePM.getText().toString().equals(""))
                                    {
                                        params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                                    }
                                    if(!txtDiametroPAmarrePC.getText().toString().equals(""))
                                    {
                                        params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                                    }
                                    if(!txtAnchoPAmarrePC.getText().toString().equals(""))
                                    {
                                        params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                                    }
                                    if(!txtLargoEjePAmarrePC.getText().toString().equals(""))
                                    {
                                        params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                                    }
                                    if(!txtDiametroEjePAmarrePC.getText().toString().equals(""))
                                    {
                                        params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogPoleaTensora() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_tensora_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);


        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaTensoraHorizontal);
        final Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaTensoraHorizontal);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
        final Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaTensoraHorizontal);
        final Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicionPoleaTensoraHorizontal);
        final Spinner spinnerRecorrido = dialogParte.findViewById(R.id.spinnerRecorridoPoleaTensora);

        final TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaTensoraHorizontal);
        final TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaTensoraHorizontal);
        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaTensoraHorizontal);
        final TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaTensoraHorizontal);
        final TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPoleaTensora);
        final TextInputEditText txtHpMotor = dialogParte.findViewById(R.id.txtPotenciaMotorHorizontal);

        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);

        spinnerTipoPolea.setAdapter(adapterTipoPolea);
        spinnerBandaCentradaEnPolea.setAdapter(adapterSiNo);
        spinnerEstadoRvto.setAdapter(adapterEstadoPartes);
        spinnerGuardaPolea.setAdapter(adapterEstadoPartes);
        spinnerTipoTransicion.setAdapter(adapterTipoTransicion);
        spinnerRecorrido.setAdapter(adapterAnguloAmarre);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Polea Tensora");
        }

        txtDiametro.setOnFocusChangeListener(this);
        txtAncho.setOnFocusChangeListener(this);
        txtDiametroEje.setOnFocusChangeListener(this);
        txtLargoEje.setOnFocusChangeListener(this);

        Button btnEnviarInfo = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);
        btnEnviarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroPoleaTensora";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("diametroPoleaTensora", txtDiametro.getText().toString());
                                                    params.put("anchoPoleaTensora", txtAncho.getText().toString());
                                                    params.put("tipoPoleaTensora", spinnerTipoPolea.getSelectedItem().toString());
                                                    params.put("largoEjePoleaTensora", txtLargoEje.getText().toString());
                                                    params.put("tipoTransicionPoleaTensora", spinnerTipoTransicion.getSelectedItem().toString());
                                                    params.put("distanciaTransicionPoleaColaTensora", txtDistanciaTransicion.getText().toString());
                                                    params.put("potenciaMotorPoleaTensora", txtHpMotor.getText().toString());
                                                    params.put("guardaPoleaTensora", spinnerGuardaPolea.getSelectedItem().toString());
                                                    params.put("diametroEjePoleaTensora", txtDiametroEje.getText().toString());
                                                    params.put("icobandasCentradaEnPoleaTensora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                    params.put("recorridoPoleaTensora", spinnerRecorrido.getSelectedItem().toString());
                                                    params.put("estadoRevestimientoPoleaTensora", spinnerEstadoRvto.getSelectedItem().toString());

                                                    if (!txtDiametro.getText().toString().equals("")) {
                                                        params.put("diametroPoleaTensora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                                    }
                                                    if (!txtAncho.getText().toString().equals("")) {
                                                        params.put("anchoPoleaTensora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                                    }

                                                    if (!txtLargoEje.getText().toString().equals("")) {
                                                        params.put("largoEjePoleaTensora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                                    }
                                                    if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                                        params.put("distanciaTransicionPoleaColaTensora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                                    }
                                                    if (!txtHpMotor.getText().toString().equals("")) {
                                                        params.put("potenciaMotorPoleaTensora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                                                    }

                                                    if (!txtDiametroEje.getText().toString().equals("")) {
                                                        params.put("diametroEjePoleaTensora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                                    }


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarPoleaTensora";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("diametroPoleaTensora", txtDiametro.getText().toString());
                                    params.put("anchoPoleaTensora", txtAncho.getText().toString());
                                    params.put("tipoPoleaTensora", spinnerTipoPolea.getSelectedItem().toString());
                                    params.put("largoEjePoleaTensora", txtLargoEje.getText().toString());
                                    params.put("tipoTransicionPoleaTensora", spinnerTipoTransicion.getSelectedItem().toString());
                                    params.put("distanciaTransicionPoleaColaTensora", txtDistanciaTransicion.getText().toString());
                                    params.put("potenciaMotorPoleaTensora", txtHpMotor.getText().toString());
                                    params.put("guardaPoleaTensora", spinnerGuardaPolea.getSelectedItem().toString());
                                    params.put("diametroEjePoleaTensora", txtDiametroEje.getText().toString());
                                    params.put("icobandasCentradaEnPoleaTensora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                    params.put("recorridoPoleaTensora", spinnerRecorrido.getSelectedItem().toString());
                                    params.put("estadoRevestimientoPoleaTensora", spinnerEstadoRvto.getSelectedItem().toString());

                                    if (!txtDiametro.getText().toString().equals("")) {
                                        params.put("diametroPoleaTensora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                    }
                                    if (!txtAncho.getText().toString().equals("")) {
                                        params.put("anchoPoleaTensora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                    }

                                    if (!txtLargoEje.getText().toString().equals("")) {
                                        params.put("largoEjePoleaTensora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                    }
                                    if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                        params.put("distanciaTransicionPoleaColaTensora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                    }
                                    if (!txtHpMotor.getText().toString().equals("")) {
                                        params.put("potenciaMotorPoleaTensora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                                    }

                                    if (!txtDiametroEje.getText().toString().equals("")) {
                                        params.put("diametroEjePoleaTensora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
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

        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    public void abrirDialogSeguridad() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_seguridad_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);


        final Spinner spinnerPuertasInspeccion = dialogParte.findViewById(R.id.spinnerPuertasInspeccionHorizontal);
        final Spinner spinnerGuardaRodilloRetornoPlano = dialogParte.findViewById(R.id.spinnerGuardaRodilloPlano);
        final Spinner spinnerGuardaRodilloRetornoEnV = dialogParte.findViewById(R.id.spinnerGuardaRodilloRetornoV);
        final Spinner spinnerGuardaTruTrainer = dialogParte.findViewById(R.id.spinnerGuardaTruTrainer);
        final Spinner spinnerGuardaPoleaColaSeguridad = dialogParte.findViewById(R.id.spinnerGuardaPoleaColaSeguridad);
        final Spinner spinnerGuardaPoleaDeflectora = dialogParte.findViewById(R.id.spinnerGuardaPoleaDeflectora);
        final Spinner spinnerGuardaPoleaTensora = dialogParte.findViewById(R.id.spinnerGuardaPoleaTensoraSeguridad);
        final Spinner spinnerGuardaPoleaMotrizSeguridad = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizSeguridad);
        final Spinner spinnerGuardaZonaDeTransito = dialogParte.findViewById(R.id.spinnerGuardaZonaDeTransito);
        final Spinner spinnerGuardaMotores = dialogParte.findViewById(R.id.spinnerGuardaMotores);
        final Spinner spinnerGuardaCadenas = dialogParte.findViewById(R.id.spinnerGuardaCadenas);
        final Spinner spinnerGuardaCorreas = dialogParte.findViewById(R.id.spinnerGuardaCorreas);
        final Spinner spinnerInterruptoresSeguridadHorizontal = dialogParte.findViewById(R.id.spinnerInterruptoresSeguridadHorizontal);
        final Spinner spinnerSirenasDeSeguridad = dialogParte.findViewById(R.id.spinnerSirenasSeguridad);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);

        spinnerPuertasInspeccion.setAdapter(adapterSiNo);
        spinnerGuardaRodilloRetornoPlano.setAdapter(adapterSiNo);
        spinnerGuardaRodilloRetornoEnV.setAdapter(adapterSiNo);
        spinnerGuardaTruTrainer.setAdapter(adapterSiNo);
        spinnerGuardaPoleaColaSeguridad.setAdapter(adapterSiNo);
        spinnerGuardaPoleaDeflectora.setAdapter(adapterSiNo);
        spinnerGuardaPoleaTensora.setAdapter(adapterSiNo);
        spinnerGuardaPoleaMotrizSeguridad.setAdapter(adapterSiNo);
        spinnerGuardaZonaDeTransito.setAdapter(adapterSiNo);
        spinnerGuardaMotores.setAdapter(adapterSiNo);
        spinnerGuardaCadenas.setAdapter(adapterSiNo);
        spinnerGuardaCorreas.setAdapter(adapterSiNo);
        spinnerGuardaCorreas.setAdapter(adapterSiNo);
        spinnerInterruptoresSeguridadHorizontal.setAdapter(adapterSiNo);
        spinnerSirenasDeSeguridad.setAdapter(adapterSiNo);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Seguridad");
        }


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroSeguridad";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("puertasInspeccion", spinnerPuertasInspeccion.getSelectedItem().toString());
                                                    params.put("guardaRodilloRetornoPlano", spinnerGuardaRodilloRetornoPlano.getSelectedItem().toString());
                                                    params.put("guardaTruTrainer", spinnerGuardaTruTrainer.getSelectedItem().toString());
                                                    params.put("guardaPoleaDeflectora", spinnerGuardaPoleaDeflectora.getSelectedItem().toString());
                                                    params.put("guardaZonaDeTransito", spinnerGuardaZonaDeTransito.getSelectedItem().toString());
                                                    params.put("guardaMotores", spinnerGuardaMotores.getSelectedItem().toString());
                                                    params.put("guardaCadenas", spinnerGuardaCadenas.getSelectedItem().toString());
                                                    params.put("guardaCorreas", spinnerGuardaCorreas.getSelectedItem().toString());
                                                    params.put("interruptoresDeSeguridad", spinnerInterruptoresSeguridadHorizontal.getSelectedItem().toString());
                                                    params.put("sirenasDeSeguridad", spinnerSirenasDeSeguridad.getSelectedItem().toString());
                                                    params.put("guardaPoleaTensora", spinnerGuardaPoleaTensora.getSelectedItem().toString());
                                                    params.put("guardaPoleaMotrizTransportadora", spinnerGuardaPoleaMotrizSeguridad.getSelectedItem().toString());
                                                    params.put("guardaPoleaColaTransportadora", spinnerGuardaPoleaColaSeguridad.getSelectedItem().toString());
                                                    params.put("guardaRodilloRetornoV", spinnerGuardaRodilloRetornoEnV.getSelectedItem().toString());


                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarSeguridad";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("puertasInspeccion", spinnerPuertasInspeccion.getSelectedItem().toString());
                                    params.put("guardaRodilloRetornoPlano", spinnerGuardaRodilloRetornoPlano.getSelectedItem().toString());
                                    params.put("guardaTruTrainer", spinnerGuardaTruTrainer.getSelectedItem().toString());
                                    params.put("guardaPoleaDeflectora", spinnerGuardaPoleaDeflectora.getSelectedItem().toString());
                                    params.put("guardaZonaDeTransito", spinnerGuardaZonaDeTransito.getSelectedItem().toString());
                                    params.put("guardaMotores", spinnerGuardaMotores.getSelectedItem().toString());
                                    params.put("guardaCadenas", spinnerGuardaCadenas.getSelectedItem().toString());
                                    params.put("guardaCorreas", spinnerGuardaCorreas.getSelectedItem().toString());
                                    params.put("interruptoresDeSeguridad", spinnerInterruptoresSeguridadHorizontal.getSelectedItem().toString());
                                    params.put("sirenasDeSeguridad", spinnerSirenasDeSeguridad.getSelectedItem().toString());
                                    params.put("guardaPoleaTensora", spinnerGuardaPoleaTensora.getSelectedItem().toString());
                                    params.put("guardaPoleaMotrizTransportadora", spinnerGuardaPoleaMotrizSeguridad.getSelectedItem().toString());
                                    params.put("guardaPoleaColaTransportadora", spinnerGuardaPoleaColaSeguridad.getSelectedItem().toString());
                                    params.put("guardaRodilloRetornoV", spinnerGuardaRodilloRetornoEnV.getSelectedItem().toString());


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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    private void abrirDialogRodilloCarga() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_rodillo_carga);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
        final Spinner spinnerAnguloAcanalArtesaCarga = dialogParte.findViewById(R.id.spinnerAnguloAcanalArtesaCarga);

        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.tipoRodilloCarga);
        ArrayAdapter<String> adapterAnguloAcanalamiento= new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.angulosAcanalamiento);

        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
        spinnerAnguloAcanalArtesaCarga.setAdapter(adapterAnguloAcanalamiento);

        final TextInputEditText txtlargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
        final TextInputEditText txtDiametroEjeRodilloCentral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
        final TextInputEditText txtDiametroRodilloCentral= dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
        final TextInputEditText txtLargoTuboRodilloCentral= dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral= dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral= dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtDistanciaArtesasCarga= dialogParte.findViewById(R.id.txtDistanciaArtesasCarga);
        final TextInputEditText txtAnchoInternoChasis= dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis= dialogParte.findViewById(R.id.txtAnchoExternoChasis);
        final TextInputEditText txtDetalleRodilloCargaCentral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
        final TextInputEditText txtDetalleRodilloCargaLateral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Rodillo Carga");
        }


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroRodilloCarga";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("largoEjeRodilloCentralCarga", txtlargoEjeRodilloCentral.getText().toString());
                                                    params.put("diametroEjeRodilloCentralCarga", txtDiametroEjeRodilloCentral.getText().toString());
                                                    params.put("largoTuboRodilloCentralCarga", txtLargoTuboRodilloCentral.getText().toString());
                                                    params.put("largoEjeRodilloLateralCarga", txtLargoEjeRodilloLateral.getText().toString());
                                                    params.put("diametroEjeRodilloLateralCarga", txtDiametroEjeRodilloLateral.getText().toString());
                                                    params.put("diametroRodilloLateralCarga", txtDiametroRodilloLateral.getText().toString());
                                                    params.put("anchoExternoChasisRodilloCarga", txtAnchoExternoChasis.getText().toString());
                                                    params.put("anguloAcanalamientoArtesaCArga", spinnerAnguloAcanalArtesaCarga.getSelectedItem().toString());
                                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCargaCentral.getText().toString());
                                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloCargaLateral.getText().toString());
                                                    params.put("diametroRodilloCentralCarga", txtDiametroRodilloCentral.getText().toString());
                                                    params.put("largoTuboRodilloLateralCarga", txtLargoTuboRodilloLateral.getText().toString());
                                                    params.put("tipoRodilloCarga", spinnerTipoRodilloCarga.getSelectedItem().toString());
                                                    params.put("distanciaEntreArtesasCarga", txtDistanciaArtesasCarga.getText().toString());
                                                    params.put("anchoInternoChasisRodilloCarga", txtAnchoInternoChasis.getText().toString());

                                                    if(!txtlargoEjeRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjeRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                                                    }
                                                    if(!txtLargoTuboRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                                                    }
                                                    if(!txtLargoEjeRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjeRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                                                    }
                                                    if(!txtDiametroRodilloLateral.getText().equals(""))
                                                    {
                                                        params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                                                    }
                                                    if(!txtAnchoExternoChasis.getText().equals(""))
                                                    {
                                                        params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                                    }
                                                    if(!txtDiametroRodilloCentral.getText().equals(""))
                                                    {
                                                        params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                                                    }
                                                    if(!txtDistanciaArtesasCarga.getText().equals(""))
                                                    {
                                                        params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                                    }
                                                    if(!txtAnchoInternoChasis.getText().equals(""))
                                                    {
                                                        params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                                    }






                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarRodilloCarga";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("largoEjeRodilloCentralCarga", txtlargoEjeRodilloCentral.getText().toString());
                                    params.put("diametroEjeRodilloCentralCarga", txtDiametroEjeRodilloCentral.getText().toString());
                                    params.put("largoTuboRodilloCentralCarga", txtLargoTuboRodilloCentral.getText().toString());
                                    params.put("largoEjeRodilloLateralCarga", txtLargoEjeRodilloLateral.getText().toString());
                                    params.put("diametroEjeRodilloLateralCarga", txtDiametroEjeRodilloLateral.getText().toString());
                                    params.put("diametroRodilloLateralCarga", txtDiametroRodilloLateral.getText().toString());
                                    params.put("anchoExternoChasisRodilloCarga", txtAnchoExternoChasis.getText().toString());
                                    params.put("anguloAcanalamientoArtesaCArga", spinnerAnguloAcanalArtesaCarga.getSelectedItem().toString());
                                    params.put("detalleRodilloCentralCarga", txtDetalleRodilloCargaCentral.getText().toString());
                                    params.put("detalleRodilloLateralCarg", txtDetalleRodilloCargaLateral.getText().toString());
                                    params.put("diametroRodilloCentralCarga", txtDiametroRodilloCentral.getText().toString());
                                    params.put("largoTuboRodilloLateralCarga", txtLargoTuboRodilloLateral.getText().toString());
                                    params.put("tipoRodilloCarga", spinnerTipoRodilloCarga.getSelectedItem().toString());
                                    params.put("distanciaEntreArtesasCarga", txtDistanciaArtesasCarga.getText().toString());
                                    params.put("anchoInternoChasisRodilloCarga", txtAnchoInternoChasis.getText().toString());

                                    if(!txtlargoEjeRodilloCentral.getText().equals(""))
                                    {
                                        params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                                    }
                                    if(!txtDiametroEjeRodilloCentral.getText().equals(""))
                                    {
                                        params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                                    }
                                    if(!txtLargoTuboRodilloCentral.getText().equals(""))
                                    {
                                        params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                                    }
                                    if(!txtLargoEjeRodilloLateral.getText().equals(""))
                                    {
                                        params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                    }
                                    if(!txtDiametroEjeRodilloLateral.getText().equals(""))
                                    {
                                        params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                                    }
                                    if(!txtDiametroRodilloLateral.getText().equals(""))
                                    {
                                        params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                                    }
                                    if(!txtAnchoExternoChasis.getText().equals(""))
                                    {
                                        params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                    }
                                    if(!txtDiametroRodilloCentral.getText().equals(""))
                                    {
                                        params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                                    }
                                    if(!txtDistanciaArtesasCarga.getText().equals(""))
                                    {
                                        params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                    }
                                    if(!txtAnchoInternoChasis.getText().equals(""))
                                    {
                                        params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    private void abrirDialogRodilloRetorno() {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_rodillo_retorno);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        final Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
        final Spinner spinnerRodillosRetorno = dialogParte.findViewById(R.id.spinnerRodillosRetorno);

        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.tipoRodilloCarga);
        ArrayAdapter<String> adapterRodillosRetorno= new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.estadoPartes);

        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
        spinnerRodillosRetorno.setAdapter(adapterRodillosRetorno);


        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral= dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral= dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtDistanciaArtesasCarga= dialogParte.findViewById(R.id.txtDistanciaEntreRodillosRetorno);
        final TextInputEditText txtAnchoInternoChasis= dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis= dialogParte.findViewById(R.id.txtAnchoExternoChasis);
        final TextInputEditText txtDetalleRodilloCargaCentral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);


        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("Rodillo Retorno");
        }


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaHorizontal);

        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                                            String url = Constants.url + "registroRodilloRetorno";
                                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    login("dialogTornillo");
                                                    dialogParte.cancel();


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    dialogCarga.dismiss();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                    params.put("largoEjeRodilloRetorno", txtLargoEjeRodilloLateral.getText().toString());
                                                    params.put("diametroEjeRodilloRetorno", txtDiametroEjeRodilloLateral.getText().toString());
                                                    params.put("diametroRodilloRetorno", txtDiametroRodilloLateral.getText().toString());
                                                    params.put("anchoExternoChasisRetorno", txtAnchoExternoChasis.getText().toString());
                                                    params.put("detalleRodilloRetorno", txtDetalleRodilloCargaCentral.getText().toString());
                                                    params.put("largoTuboRodilloRetorno", txtLargoTuboRodilloLateral.getText().toString());
                                                    params.put("tipoRodilloRetorno", spinnerTipoRodilloCarga.getSelectedItem().toString());
                                                    params.put("distanciaEntreRodillosRetorno", txtDistanciaArtesasCarga.getText().toString());
                                                    params.put("estadoRodilloRetorno", spinnerRodillosRetorno.getSelectedItem().toString());
                                                    params.put("anchoInternoChasisRetorno", txtAnchoInternoChasis.getText().toString());

                                                    if(!txtLargoEjeRodilloLateral.getText().toString().equals(""))
                                                    {
                                                        params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                                    }
                                                    if(!txtDiametroEjeRodilloLateral.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtDiametroRodilloLateral.getText().toString().equals(""))
                                                    {
                                                        params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                                    }
                                                    if(!txtAnchoExternoChasis.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                                    }
                                                    if(!txtDetalleRodilloCargaCentral.getText().toString().equals(""))
                                                    {
                                                        params.put("detalleRodilloRetorno", String.valueOf(Float.parseFloat(txtDetalleRodilloCargaCentral.getText().toString())));
                                                    }
                                                    if(!txtLargoTuboRodilloLateral.getText().toString().equals(""))
                                                    {
                                                        params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                                                    }
                                                    if(!txtDistanciaArtesasCarga.getText().toString().equals(""))
                                                    {
                                                        params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                                    }
                                                    if(!txtAnchoInternoChasis.getText().toString().equals(""))
                                                    {
                                                        params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                                    }

                                                    return params;
                                                }
                                            };
                                            queue.add(requestRegistroTornillos);

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            dialogCarga.dismiss();
                                        }
                                    });
                                    queue.add(requestMaxRegistro);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            String url = Constants.url + "actualizarRodilloRetorno";
                            StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login("dialogTornillo");


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    dialogCarga.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                    params.put("largoEjeRodilloRetorno", txtLargoEjeRodilloLateral.getText().toString());
                                    params.put("diametroEjeRodilloRetorno", txtDiametroEjeRodilloLateral.getText().toString());
                                    params.put("diametroRodilloRetorno", txtDiametroRodilloLateral.getText().toString());
                                    params.put("anchoExternoChasisRetorno", txtAnchoExternoChasis.getText().toString());
                                    params.put("detalleRodilloRetorno", txtDetalleRodilloCargaCentral.getText().toString());
                                    params.put("largoTuboRodilloRetorno", txtLargoTuboRodilloLateral.getText().toString());
                                    params.put("tipoRodilloRetorno", spinnerTipoRodilloCarga.getSelectedItem().toString());
                                    params.put("distanciaEntreRodillosRetorno", txtDistanciaArtesasCarga.getText().toString());
                                    params.put("estadoRodilloRetorno", spinnerRodillosRetorno.getSelectedItem().toString());
                                    params.put("anchoInternoChasisRetorno", txtAnchoInternoChasis.getText().toString());

                                    if(!txtLargoEjeRodilloLateral.getText().toString().equals(""))
                                    {
                                        params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                    }
                                    if(!txtDiametroEjeRodilloLateral.getText().toString().equals(""))
                                    {
                                        params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                    }
                                    if(!txtDiametroRodilloLateral.getText().toString().equals(""))
                                    {
                                        params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                    }
                                    if(!txtAnchoExternoChasis.getText().toString().equals(""))
                                    {
                                        params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                    }

                                    if(!txtLargoTuboRodilloLateral.getText().toString().equals(""))
                                    {
                                        params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                                    }
                                    if(!txtDistanciaArtesasCarga.getText().toString().equals(""))
                                    {
                                        params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                    }
                                    if(!txtAnchoInternoChasis.getText().toString().equals(""))
                                    {
                                        params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
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


        dialogParte.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });
        dialogParte.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.txtAnchoBandaHorizontal:
            case R.id.txtAnchoBandaHorizontalAnterior:
                if (!hasFocus) {
                    EditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaHorizontal);
                    EditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaHorizontalAnterior);
                    if (!txtAnchoBanda.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtAnchoBanda.getText().toString());
                        if (numero < 50 || numero > 2700) {
                            txtAnchoBanda.setText("");
                            txtAnchoBanda.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                    if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtAnchoBandaAnterior.getText().toString());
                        if (numero < 50 || numero > 2700) {
                            txtAnchoBandaAnterior.setText("");
                            txtAnchoBandaAnterior.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
                        }
                    }
                }
                break;

            case R.id.txtEpesorTotalHorizontal:
            case R.id.txtEspesorTotalHorizontalAnterior:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtEpesorTotalHorizontal);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtEspesorTotalHorizontalAnterior);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 1 || numero > 40.6) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 1mm - 40.6mm");
                        }
                    }
                    if (!txtDiametroRosca1.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if (numero < 1 || numero > 41) {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 1mm - 40.6mm");
                        }
                    }
                }
                break;

            case R.id.txtEspesorCojinHorizontal:
            case R.id.txtEspesorCojinHorizontalAnterior:
            case R.id.txtCubSupHorizontal:
            case R.id.txtCubSupHorizontalAnterior:
            case R.id.txtCubInfHorizontal:
            case R.id.txtCubInfHorizontalAnterior:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtEspesorCojinHorizontal);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtEspesorCojinHorizontalAnterior);
                    EditText txtDiametroRosca2 = dialogParte.findViewById(R.id.txtCubSupHorizontal);
                    EditText txtDiametroRosca3 = dialogParte.findViewById(R.id.txtCubSupHorizontalAnterior);
                    EditText txtDiametroRosca4 = dialogParte.findViewById(R.id.txtCubInfHorizontal);
                    EditText txtDiametroRosca5 = dialogParte.findViewById(R.id.txtCubInfHorizontalAnterior);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if (!txtDiametroRosca1.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if (!txtDiametroRosca2.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca2.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca2.setText("");
                            txtDiametroRosca2.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if (!txtDiametroRosca3.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca3.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca3.setText("");
                            txtDiametroRosca3.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if (!txtDiametroRosca4.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca4.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca4.setText("");
                            txtDiametroRosca4.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                    if (!txtDiametroRosca5.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca5.getText().toString());
                        if (numero < 1 || numero > 12) {
                            txtDiametroRosca5.setText("");
                            txtDiametroRosca5.setError("Éste valor debe estar entre 1mm - 12mm");
                        }
                    }
                }
                break;

            case R.id.txtAnchoCauchoVPlow:
            case R.id.txtEspesorCauchoVPlow:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtAnchoCauchoVPlow);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtEspesorCauchoVPlow);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 305) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 50mm - 305mm");
                        }
                    }
                    if (!txtDiametroRosca1.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca1.getText().toString());
                        if (numero < 6.4 || numero > 25.4) {
                            txtDiametroRosca1.setText("");
                            txtDiametroRosca1.setError("Éste valor debe estar entre 6.4mm - 25.4mm");
                        }
                    }
                }
                break;

            case R.id.txtDiametroPoleaTensoraHorizontal:

                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtDiametroPoleaTensoraHorizontal);

                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 1524) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  60\" / 1524mm");
                        }
                    }

                }
                break;

            case R.id.txtAnchoPoleaTensoraHorizontal:
            case R.id.txtDiametroEjePoleaTensoraHorizontal:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtAnchoPoleaTensoraHorizontal);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtDiametroEjePoleaTensoraHorizontal);

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

            case R.id.txtLargoEjePoleaTensoraHorizontal:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtLargoEjePoleaTensoraHorizontal);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 254 || numero > 2700) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 10\" / 254mm -  106.3\" / 2700mm");
                        }
                    }
                }
            break;



            case R.id.txtDiametroPoleaMotrizHorizontal:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);

                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 1524) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  60\" / 1524mm");
                        }
                    }

                }
                break;

            case R.id.txtAnchoPoleaMotrizHorizontal:
            case R.id.txtDiametroEjePoleaMotrizHorizontal:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizHorizontal);

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

            case R.id.txtLargoEjePoleaMotrizHorizontal:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 254 || numero > 2700) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 10\" / 254mm -  106.3\" / 2700mm");
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnviarRegistroBandaHorizontal:
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

            case R.id.tvDiametroPoleaMotrizHorizontal:
            case R.id.tvAnchoPoleaMotrizHorizontal:
            case R.id.tvDiametroEjePoleaMotrizHorizontal:
            case R.id.tvLargoEjePoleaMotrizHorizontal:
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

            case R.id.tvAnguloAmarrePC:
            case R.id.tvAnguloAmarrePMHorizontal:
                abrirDialogParte("Angulo Amarre");
                break;
        }


    }


    public void abrirDialogParte(String parte) {
        Dialog dialogParte;
        ImageView imgParte;
        switch (parte) {
            case "cangilon":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Distancias Cangilon":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.distancias_cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tipo Ventilacion":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.ventilacion_cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "dimensionesPoleaMotriz":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.polea);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tornillos":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tornillo);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tipo Carga":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tipo_carga);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Tipo Descarga":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.tipo_descarga);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case "Angulo Amarre":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.angulo_amarre);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;
        }

    }

    public void llenarRegistros(String parte) {
        switch (parte) {
            case "Banda":
                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {
                        final Spinner spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontal);
                        final Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasHorizontal);
                        final Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontal);
                        final Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontal);
                        final Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontal);
                        final Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeHorizontal);
                        final Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontal);
                        final Spinner spinnerLocTensor = dialogParte.findViewById(R.id.spinnerLocalizacionTensor);
                        final Spinner spinnerBandReversible = dialogParte.findViewById(R.id.spinnerBandaReversible);
                        final Spinner spinnerBandaArrastre = dialogParte.findViewById(R.id.spinnerBandaDeArrastre);


                        final Spinner spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontalAnterior);
                        final Spinner spinnerNoLonasAnterior = dialogParte.findViewById(R.id.spinnerNoLonasHorizontalAnterior);
                        final Spinner spinnerTipoLonaAnterior = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontalAnterior);
                        final Spinner spinnerTipoCubiertaAnterior = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontalAnterior);
                        final Spinner spinnerTipoEmpalmeAnterior = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontalAnterior);
                        final Spinner spinnerResistenciaRoturaAnterior = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontalAnterior);

                        final TextInputEditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaHorizontal);
                        final TextInputEditText txtEspesorTotal = dialogParte.findViewById(R.id.txtEpesorTotalHorizontal);
                        final TextInputEditText txtEspCubSup = dialogParte.findViewById(R.id.txtCubSupHorizontal);
                        final TextInputEditText txtEspCubInf = dialogParte.findViewById(R.id.txtCubInfHorizontal);
                        final TextInputEditText txtEspCojin = dialogParte.findViewById(R.id.txtEspesorCojinHorizontal);
                        final TextInputEditText txtVelocidadBanda = dialogParte.findViewById(R.id.txtVelocidadBandaHorizontal);

                        final TextInputEditText txtDistanciEntrePoleas = dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
                        final TextInputEditText txtInclinacion = dialogParte.findViewById(R.id.txtInclinacion);
                        final TextInputEditText txtRecorridoUtilTensor = dialogParte.findViewById(R.id.txtRecorridoUtilTensor);
                        final TextInputEditText txtLongitudSinfin = dialogParte.findViewById(R.id.txtLongitudSinfinBanda);
                        final TextInputEditText txtTonsTransportadas = dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);

                        final TextInputEditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaHorizontalAnterior);
                        final TextInputEditText txtEspesorTotalAnterior = dialogParte.findViewById(R.id.txtEspesorTotalHorizontalAnterior);
                        final TextInputEditText txtEspCubSupAnterior = dialogParte.findViewById(R.id.txtCubSupHorizontalAnterior);
                        final TextInputEditText txtEspCubInfAnterior = dialogParte.findViewById(R.id.txtCubInfHorizontalAnterior);
                        final TextInputEditText txtEspCojinAnterior = dialogParte.findViewById(R.id.txtEspesorCojinHorizontalAnterior);
                        final TextInputEditText txtCausaFallo = dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);


                        ArrayAdapter<String> adapterMarcaBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaBanda);
                        spinnerMarcaBanda.setAdapter(adapterMarcaBanda);
                        spinnerMarcaBandaAnterior.setAdapter(adapterMarcaBanda);

                        ArrayAdapter<String> adapterNoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.noLonas);
                        spinnerNoLonas.setAdapter(adapterNoLonas);
                        spinnerNoLonasAnterior.setAdapter(adapterNoLonas);

                        ArrayAdapter<String> adapterTipoLonas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoLona);
                        spinnerTipoLona.setAdapter(adapterTipoLonas);
                        spinnerTipoLonaAnterior.setAdapter(adapterTipoLonas);

                        ArrayAdapter<String> adapterTipoCubierta = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCubierta);
                        spinnerTipoCubierta.setAdapter(adapterTipoCubierta);
                        spinnerTipoCubiertaAnterior.setAdapter(adapterTipoCubierta);

                        ArrayAdapter<String> adapterTipoEmpalme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoEmpalme);
                        spinnerTipoEmpalme.setAdapter(adapterTipoEmpalme);
                        spinnerTipoEmpalmeAnterior.setAdapter(adapterTipoEmpalme);

                        ArrayAdapter<String> adapterEstadoEmpalme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        spinnerEstadoEmpalme.setAdapter(adapterEstadoEmpalme);

                        ArrayAdapter<String> adapterRoturaLona = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.resistenciaRoturaLona);
                        spinnerResistenciaRotura.setAdapter(adapterRoturaLona);
                        spinnerResistenciaRoturaAnterior.setAdapter(adapterRoturaLona);

                        ArrayAdapter<String> adapterLocTensor = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.localizacionTensor);
                        spinnerLocTensor.setAdapter(adapterLocTensor);

                        ArrayAdapter<String> adapterBandaReversible = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerBandReversible.setAdapter(adapterBandaReversible);

                        ArrayAdapter<String> adapterBandaArastre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerBandaArrastre.setAdapter(adapterBandaArastre);

                        int p1 = adapterMarcaBanda.getPosition(Login.loginJsons.get(i).getMarcaBandaTransportadora());
                        int p2 = adapterMarcaBanda.getPosition(Login.loginJsons.get(i).getMarcaBandaHorizontalAnterior());
                        int p4 = adapterNoLonas.getPosition(Login.loginJsons.get(i).getNoLonasBandaTransportadora());
                        int p5 = adapterNoLonas.getPosition(Login.loginJsons.get(i).getNoLonasBandaHorizontalAnterior());
                        int p6 = adapterTipoLonas.getPosition(Login.loginJsons.get(i).getTipoLonaBandaTranportadora());
                        int p7 = adapterTipoLonas.getPosition(Login.loginJsons.get(i).getTipoLonaBandaHorizontalAnterior());
                        int p8 = adapterTipoCubierta.getPosition(Login.loginJsons.get(i).getTipoCubiertaTransportadora());
                        int p9 = adapterTipoCubierta.getPosition(Login.loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior());
                        int p10 = adapterTipoEmpalme.getPosition(Login.loginJsons.get(i).getTipoEmpalmeTransportadora());
                        int p11 = adapterTipoEmpalme.getPosition(Login.loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior());
                        int p12 = adapterRoturaLona.getPosition(Login.loginJsons.get(i).getResistenciaRoturaLonaTransportadora());
                        int p13 = adapterRoturaLona.getPosition(Login.loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior());
                        int p14 = adapterBandaReversible.getPosition(Login.loginJsons.get(i).getBandaReversible());
                        int p15 = adapterBandaArastre.getPosition(Login.loginJsons.get(i).getBandaDeArrastre());
                        int p16 = adapterEstadoEmpalme.getPosition(Login.loginJsons.get(i).getEstadoEmpalmeTransportadora());
                        int p17 = adapterLocTensor.getPosition(Login.loginJsons.get(i).getLocalizacionTensorTransportadora());

                        spinnerMarcaBanda.setSelection(p1);
                        spinnerMarcaBandaAnterior.setSelection(p2);
                        spinnerNoLonas.setSelection(p4);
                        spinnerNoLonasAnterior.setSelection(p5);
                        spinnerTipoLona.setSelection(p6);
                        spinnerTipoLonaAnterior.setSelection(p7);
                        spinnerTipoCubierta.setSelection(p8);
                        spinnerTipoCubiertaAnterior.setSelection(p9);
                        spinnerTipoEmpalme.setSelection(p10);
                        spinnerTipoEmpalmeAnterior.setSelection(p11);
                        spinnerResistenciaRotura.setSelection(p12);
                        spinnerResistenciaRoturaAnterior.setSelection(p13);
                        spinnerBandReversible.setSelection(p14);
                        spinnerBandaArrastre.setSelection(p15);
                        spinnerEstadoEmpalme.setSelection(p16);
                        spinnerLocTensor.setSelection(p17);

                        txtAnchoBandaAnterior.setText(Login.loginJsons.get(i).getAnchoBandaHorizontalAnterior());
                        txtEspesorTotalAnterior.setText(Login.loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior());
                        txtEspCubSupAnterior.setText(Login.loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior());
                        txtEspCubInfAnterior.setText(Login.loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior());
                        txtEspCojinAnterior.setText(Login.loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior());
                        txtCausaFallo.setText(Login.loginJsons.get(i).getCausaFallaCambioBandaHorizontal());

                        txtAnchoBanda.setText(Login.loginJsons.get(i).getAnchoBandaTransportadora());
                        txtEspesorTotal.setText(Login.loginJsons.get(i).getEspesorTotalBandaTransportadora());
                        txtEspCubSup.setText(Login.loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora());
                        txtEspCubInf.setText(Login.loginJsons.get(i).getEspesorCubiertaInferiorTransportadora());
                        txtEspCojin.setText(Login.loginJsons.get(i).getEspesorCojinTransportadora());
                        txtVelocidadBanda.setText(Login.loginJsons.get(i).getVelocidadBandaHorizontal());

                        txtDistanciEntrePoleas.setText(Login.loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal());
                        txtInclinacion.setText(Login.loginJsons.get(i).getInclinacionBandaHorizontal());
                        txtRecorridoUtilTensor.setText(Login.loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal());
                        txtLongitudSinfin.setText(Login.loginJsons.get(i).getLongitudSinfinBandaHorizontal());
                        txtTonsTransportadas.setText(Login.loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior());

                        i = Login.loginJsons.size() + 1;


                    }
                }
                break;

            case "Desviador":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {

                        Spinner spinnerHayDesviador = dialogParte.findViewById(R.id.spinnerHayDesviador);
                        Spinner spinnerElDesviadorBascula = dialogParte.findViewById(R.id.spinnerElDesviadorBascula);
                        Spinner spinnerHayPresionUniforme = dialogParte.findViewById(R.id.spinnerHayPresionUniforme);
                        Spinner spinnerCauchoVPlow = dialogParte.findViewById(R.id.spinnerCauchoVPlow);

                        TextInputEditText txtAchoCaucho = dialogParte.findViewById(R.id.txtAnchoCauchoVPlow);
                        TextInputEditText txtEspesorCaucho = dialogParte.findViewById(R.id.txtEspesorCauchoVPlow);


                        ArrayAdapter<String> adapterHayDesviador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerHayDesviador.setAdapter(adapterHayDesviador);

                        ArrayAdapter<String> adapterElDesviadoBascula = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerElDesviadorBascula.setAdapter(adapterElDesviadoBascula);

                        ArrayAdapter<String> adapterPresionUniforme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerHayPresionUniforme.setAdapter(adapterPresionUniforme);

                        ArrayAdapter<String> adapterCauchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        spinnerCauchoVPlow.setAdapter(adapterCauchoVPlow);

                        int p1 = adapterHayDesviador.getPosition(Login.loginJsons.get(i).getHayDesviador());
                        int p2 = adapterElDesviadoBascula.getPosition(Login.loginJsons.get(i).getElDesviadorBascula());
                        int p3 = adapterPresionUniforme.getPosition(Login.loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda());
                        int p4 = adapterCauchoVPlow.getPosition(Login.loginJsons.get(i).getCauchoVPlow());

                        spinnerHayDesviador.setSelection(p1);
                        spinnerElDesviadorBascula.setSelection(p2);
                        spinnerHayPresionUniforme.setSelection(p3);
                        spinnerCauchoVPlow.setSelection(p4);

                        txtAchoCaucho.setText(Login.loginJsons.get(i).getAnchoVPlow());
                        txtEspesorCaucho.setText(Login.loginJsons.get(i).getEspesorVPlow());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Polea Tensora":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {

                        Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaTensoraHorizontal);
                        Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaTensoraHorizontal);
                        Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                        Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaTensoraHorizontal);
                        Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicionPoleaTensoraHorizontal);
                        Spinner spinnerRecorrido = dialogParte.findViewById(R.id.spinnerRecorridoPoleaTensora);

                        TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaTensoraHorizontal);
                        TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaTensoraHorizontal);
                        TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaTensoraHorizontal);
                        TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaTensoraHorizontal);
                        TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPoleaTensora);
                        TextInputEditText txtHpMotor = dialogParte.findViewById(R.id.txtPotenciaMotorHorizontal);

                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
                        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);


                        spinnerTipoPolea.setAdapter(adapterTipoPolea);
                        spinnerBandaCentradaEnPolea.setAdapter(adapterSiNo);
                        spinnerEstadoRvto.setAdapter(adapterEstadoPartes);
                        spinnerGuardaPolea.setAdapter(adapterEstadoPartes);
                        spinnerTipoTransicion.setAdapter(adapterTipoTransicion);
                        spinnerRecorrido.setAdapter(adapterAnguloAmarre);

                        int p1 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaTensora());
                        int p2 = adapterSiNo.getPosition(Login.loginJsons.get(i).getIcobandasCentradaEnPoleaTensora());
                        int p3 = adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaTensora());
                        int p4 = adapterTipoTransicion.getPosition(Login.loginJsons.get(i).getTipoTransicionPoleaTensora());
                        int p5 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getGuardaPoleaTensora());
                        int p6 = adapterAnguloAmarre.getPosition(Login.loginJsons.get(i).getRecorridoPoleaTensora());

                        txtDiametro.setText(Login.loginJsons.get(i).getDiametroPoleaTensora());
                        txtAncho.setText(Login.loginJsons.get(i).getAnchoPoleaTensora());
                        txtDiametroEje.setText(Login.loginJsons.get(i).getDiametroEjePoleaTensora());
                        txtLargoEje.setText(Login.loginJsons.get(i).getLargoEjePoleaTensora());
                        txtDistanciaTransicion.setText(Login.loginJsons.get(i).getDistanciaTransicionPoleaColaTensora());
                        txtHpMotor.setText(Login.loginJsons.get(i).getPotenciaMotorPoleaTensora());

                        spinnerTipoPolea.setSelection(p3);
                        spinnerBandaCentradaEnPolea.setSelection(p2);
                        spinnerEstadoRvto.setSelection(p1);
                        spinnerGuardaPolea.setSelection(p5);
                        spinnerRecorrido.setSelection(p6);
                        spinnerTipoTransicion.setSelection(p4);


                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Alineacion":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {

                        Spinner spinnerSistAlineacionCarga = dialogParte.findViewById(R.id.spinnerSistAlineacionCarga);
                        Spinner spinnerFuncionanSACarga = dialogParte.findViewById(R.id.spinnerFuncionanSACarga);
                        Spinner spinnerSistAlineacionRetorno = dialogParte.findViewById(R.id.spinnerSistAlineacionRetorno);
                        Spinner spinnerFuncionanSARetorno = dialogParte.findViewById(R.id.spinnerFuncionanSARetorno);
                        Spinner spinnerSistAlineacionRetornoPlano = dialogParte.findViewById(R.id.spinnerSistAlineacionRetornoPlano);
                        Spinner spinnerSistAlineacionArtesaCarga = dialogParte.findViewById(R.id.spinnerSistAlineacionArtesaCarga);
                        Spinner spinnerSistAlineacionV = dialogParte.findViewById(R.id.spinnerSistAlineacionV);

                        TextInputEditText txtCantidadSACCarga = dialogParte.findViewById(R.id.txtCantidadSACCarga);
                        TextInputEditText txtCantidadSACRetorno = dialogParte.findViewById(R.id.txtCantidadSACRetorno);
                        TextInputEditText txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
                        TextInputEditText txtDetalleRodilloCargaLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);
                        TextInputEditText txtDetalleRodilloRetornoPlano = dialogParte.findViewById(R.id.txtDetalleRodilloRetornoPlano);

                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterEstadoParte = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

                        int p1 = adapterSiNo.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p2 = adapterSiNo.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p3 = adapterSiNo.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p4 = adapterSiNo.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p5 = adapterEstadoParte.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p6 = adapterEstadoParte.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());
                        int p7 = adapterEstadoParte.getPosition(Login.loginJsons.get(i).getSistemaAlineacionCarga());

                        spinnerFuncionanSACarga.setAdapter(adapterSiNo);
                        spinnerFuncionanSARetorno.setAdapter(adapterSiNo);
                        spinnerSistAlineacionCarga.setAdapter(adapterSiNo);
                        spinnerSistAlineacionRetorno.setAdapter(adapterSiNo);

                        spinnerSistAlineacionRetornoPlano.setAdapter(adapterEstadoParte);
                        spinnerSistAlineacionArtesaCarga.setAdapter(adapterEstadoParte);
                        spinnerSistAlineacionV.setAdapter(adapterEstadoParte);

                        spinnerFuncionanSACarga.setSelection(p1);
                        spinnerFuncionanSARetorno.setSelection(p2);
                        spinnerSistAlineacionCarga.setSelection(p3);
                        spinnerSistAlineacionRetorno.setSelection(p4);
                        spinnerSistAlineacionRetornoPlano.setSelection(p5);
                        spinnerSistAlineacionArtesaCarga.setSelection(p6);
                        spinnerSistAlineacionV.setSelection(p7);


                        txtCantidadSACCarga.setText(Login.loginJsons.get(i).getCantidadSistemaAlineacionEnCarga());
                        txtCantidadSACRetorno.setText(Login.loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno());
                        txtDetalleRodilloCargaCentral.setText(Login.loginJsons.get(i).getDetalleRodilloCentralCarga());
                        txtDetalleRodilloCargaLateral.setText(Login.loginJsons.get(i).getDetalleRodilloLateralCarg());
                        txtDetalleRodilloRetornoPlano.setText(Login.loginJsons.get(i).getDetalleRodilloRetorno());


                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Seguridad":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {

                        Spinner spinnerPuertasInspeccion = dialogParte.findViewById(R.id.spinnerPuertasInspeccionHorizontal);
                        Spinner spinnerGuardaRodilloRetornoPlano = dialogParte.findViewById(R.id.spinnerGuardaRodilloPlano);
                        Spinner spinnerGuardaRodilloRetornoEnV = dialogParte.findViewById(R.id.spinnerGuardaRodilloRetornoV);
                        Spinner spinnerGuardaTruTrainer = dialogParte.findViewById(R.id.spinnerGuardaTruTrainer);
                        Spinner spinnerGuardaPoleaColaSeguridad = dialogParte.findViewById(R.id.spinnerGuardaPoleaColaSeguridad);
                        Spinner spinnerGuardaPoleaDeflectora = dialogParte.findViewById(R.id.spinnerGuardaPoleaDeflectora);
                        Spinner spinnerGuardaPoleaTensora = dialogParte.findViewById(R.id.spinnerGuardaPoleaTensoraSeguridad);
                        Spinner spinnerGuardaPoleaMotrizSeguridad = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizSeguridad);
                        Spinner spinnerGuardaZonaDeTransito = dialogParte.findViewById(R.id.spinnerGuardaZonaDeTransito);
                        Spinner spinnerGuardaMotores = dialogParte.findViewById(R.id.spinnerGuardaMotores);
                        Spinner spinnerGuardaCadenas = dialogParte.findViewById(R.id.spinnerGuardaCadenas);
                        Spinner spinnerGuardaCorreas = dialogParte.findViewById(R.id.spinnerGuardaCorreas);
                        Spinner spinnerInterruptoresSeguridadHorizontal = dialogParte.findViewById(R.id.spinnerInterruptoresSeguridadHorizontal);
                        Spinner spinnerSirenasDeSeguridad = dialogParte.findViewById(R.id.spinnerSirenasSeguridad);


                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);

                        spinnerPuertasInspeccion.setAdapter(adapterSiNo);
                        spinnerGuardaRodilloRetornoPlano.setAdapter(adapterSiNo);
                        spinnerGuardaRodilloRetornoEnV.setAdapter(adapterSiNo);
                        spinnerGuardaTruTrainer.setAdapter(adapterSiNo);
                        spinnerGuardaPoleaColaSeguridad.setAdapter(adapterSiNo);
                        spinnerGuardaPoleaDeflectora.setAdapter(adapterSiNo);
                        spinnerGuardaPoleaTensora.setAdapter(adapterSiNo);
                        spinnerGuardaPoleaMotrizSeguridad.setAdapter(adapterSiNo);
                        spinnerGuardaZonaDeTransito.setAdapter(adapterSiNo);
                        spinnerGuardaMotores.setAdapter(adapterSiNo);
                        spinnerGuardaCadenas.setAdapter(adapterSiNo);
                        spinnerGuardaCorreas.setAdapter(adapterSiNo);

                        spinnerInterruptoresSeguridadHorizontal.setAdapter(adapterSiNo);
                        spinnerSirenasDeSeguridad.setAdapter(adapterSiNo);

                        int p1 = adapterSiNo.getPosition(Login.loginJsons.get(i).getPuertasInspeccion());
                        int p2 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaRodilloRetornoPlano());
                        int p3 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaRodilloRetornoV());
                        int p4 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaTruTrainer());
                        int p5 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaPoleaColaTransportadora());
                        int p6 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaPoleaDeflectora());
                        int p7 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaPoleaTensora());
                        int p8 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaPoleaMotrizTransportadora());
                        int p9 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaZonaDeTransito());
                        int p10 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaMotores());
                        int p11 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaCadenas());
                        int p12 = adapterSiNo.getPosition(Login.loginJsons.get(i).getGuardaCorreas());
                        int p13 = adapterSiNo.getPosition(Login.loginJsons.get(i).getInterruptoresDeSeguridad());
                        int p14 = adapterSiNo.getPosition(Login.loginJsons.get(i).getSirenasDeSeguridad());

                        spinnerPuertasInspeccion.setSelection(p1);
                        spinnerGuardaRodilloRetornoPlano.setSelection(p2);
                        spinnerGuardaRodilloRetornoEnV.setSelection(p3);
                        spinnerGuardaTruTrainer.setSelection(p4);
                        spinnerGuardaPoleaColaSeguridad.setSelection(p5);
                        spinnerGuardaPoleaDeflectora.setSelection(p6);
                        spinnerGuardaPoleaTensora.setSelection(p7);
                        spinnerGuardaPoleaMotrizSeguridad.setSelection(p8);
                        spinnerGuardaZonaDeTransito.setSelection(p9);
                        spinnerGuardaMotores.setSelection(p10);
                        spinnerGuardaCadenas.setSelection(p11);
                        spinnerGuardaCorreas.setSelection(p12);
                        spinnerInterruptoresSeguridadHorizontal.setSelection(p13);
                        spinnerSirenasDeSeguridad.setSelection(p14);

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;


            case "Polea Motriz":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizHorizontal);
                        Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
                        Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                        Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizHorizontal);
                        Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicionPoleaMotrizHorizontal);
                        Spinner spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePMHorizontal);

                        TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
                        TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
                        TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizHorizontal);
                        TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
                        TextInputEditText txtHpMotor = dialogParte.findViewById(R.id.txtPotenciaMotorHorizontal);
                        TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPMotrizHorizontal);

                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
                        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);

                        int p1 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora());
                        int p2 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getGuardaPoleaMotrizTransportadora());
                        int p3 = adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaMotrizTransportadora());
                        int p4 = adapterTipoTransicion.getPosition(Login.loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora());
                        int p5 = adapterAnguloAmarre.getPosition(Login.loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora());
                        int p6 = adapterSiNo.getPosition(Login.loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora());

                        spinnerTipoPolea.setSelection(p3);
                        spinnerBandaCentradaEnPolea.setSelection(p6);
                        spinnerEstadoRvto.setSelection(p1);
                        spinnerGuardaPolea.setSelection(p2);
                        spinnerTipoTransicion.setSelection(p4);
                        spinnerAnguloAmarre.setSelection(p5);

                        txtDiametro.setText(Login.loginJsons.get(i).getDiametroPoleaMotrizTransportadora());
                        txtAncho.setText(Login.loginJsons.get(i).getAnchoPoleaMotrizTransportadora());
                        txtDiametroEje.setText(Login.loginJsons.get(i).getDiametroEjeMotrizTransportadora());
                        txtLargoEje.setText(Login.loginJsons.get(i).getLargoEjePoleaMotrizTransportadora());
                        txtHpMotor.setText(Login.loginJsons.get(i).getPotenciaMotorTransportadora());
                        txtDistanciaTransicion.setText(Login.loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Polea Cola":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaColaHorizontal);
                        Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
                        Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                        Spinner spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicion);
                        Spinner spinnerGuardaPoleaCola = dialogParte.findViewById(R.id.spinnerGuardaPoleaColaHorizontal);
                        Spinner spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePCHorizontal);


                        final TextInputEditText txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
                        final TextInputEditText txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
                        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizHorizontal);
                        final TextInputEditText txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
                        final TextInputEditText txtLongTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloHorizontal);
                        final TextInputEditText txtLongContrapesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaMotrizHorizontal);
                        final TextInputEditText txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPCCola);

                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                        ArrayAdapter<String> adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
                        ArrayAdapter<String> adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);

                        int p1 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora());
                        int p2 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getGuardaPoleaColaTransportadora());
                        int p3 = adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaColaTransportadora());
                        int p4 = adapterTipoTransicion.getPosition(Login.loginJsons.get(i).getTipoTransicionPoleaColaTransportadora());
                        int p5 = adapterAnguloAmarre.getPosition(Login.loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora());
                        int p6 = adapterSiNo.getPosition(Login.loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora());

                        spinnerTipoPolea.setSelection(p3);
                        spinnerBandaCentradaEnPolea.setSelection(p6);
                        spinnerEstadoRvto.setSelection(p1);
                        spinnerGuardaPoleaCola.setSelection(p2);
                        spinnerTipoTransicion.setSelection(p4);
                        spinnerAnguloAmarre.setSelection(p5);

                        txtDiametro.setText(Login.loginJsons.get(i).getDiametroPoleaColaTransportadora());
                        txtAncho.setText(Login.loginJsons.get(i).getAnchoPoleaColaTransportadora());
                        txtDiametroEje.setText(Login.loginJsons.get(i).getDiametroEjePoleaColaHorizontal());
                        txtLargoEje.setText(Login.loginJsons.get(i).getLargoEjePoleaColaTransportadora());
                        txtLongTornillo.setText(Login.loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora());
                        txtDistanciaTransicion.setText(Login.loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora());
                        txtLongContrapesa.setText(Login.loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;


            case "Soporte Carga":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerTieneRodillosImpacto = dialogParte.findViewById(R.id.spinnerTieneRodillosImpacto);
                        Spinner spinnerCamaImpacto = dialogParte.findViewById(R.id.spinnerCamaImpacto);
                        Spinner spinnerCamaSellado = dialogParte.findViewById(R.id.spinnerCamaSellado);
                        Spinner spinnerRodillosCarga = dialogParte.findViewById(R.id.spinnerRodillosCargaHorizontal);
                        Spinner spinnerRodillosImpacto = dialogParte.findViewById(R.id.spinnerRodillosImpactoHorizontal);
                        Spinner spinnerBasculaASGCO = dialogParte.findViewById(R.id.spinnerBaculaASGCO);
                        Spinner spinnerBarrasImpacto = dialogParte.findViewById(R.id.spinnerBarraImpacto);
                        Spinner spinnerBarrasDeslizamiento = dialogParte.findViewById(R.id.spinnerBarraDeslizamiento);
                        Spinner spinnerIntegridadSoportesRodilloImpacto = dialogParte.findViewById(R.id.spinnerIntegridadSoportesRodilloImpacto);
                        Spinner spinnerIntegridadSoportesCamaImpactoSellado = dialogParte.findViewById(R.id.spinnerIntegridadSoportesCamaImpacto);
                        Spinner spinnerMaterialAtrapadoCortinas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEntreCortinas);
                        Spinner spinnerMaterialAtrapadoGuardaBandas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnGuardaBanda);
                        Spinner spinnerMaterialAtrapadoBanda = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnBanda);
                        Spinner spinnerInclinacionZonaCargue = dialogParte.findViewById(R.id.spinnerInclinacionZonaCargue);
                        Spinner spinnerTipoRodilloRC= dialogParte.findViewById(R.id.spinnerTipoRodilloRC);
                        Spinner spinnerTipoRodilloRI= dialogParte.findViewById(R.id.spinnerTipoRodilloRI);
                        Spinner spinnerBasculaPesaje= dialogParte.findViewById(R.id.spinnerBasculaPesaje);

                        Spinner spinnerAnguloAcanalmiento1artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaCola);
                        Spinner spinnerAnguloAcanalmiento2artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaCola);
                        Spinner spinnerAnguloAcanalmiento3artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaCola);
                        Spinner spinnerAnguloAcanalmiento1artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaMotriz);
                        Spinner spinnerAnguloAcanalmiento2artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaMotriz);
                        Spinner spinnerAnguloAcanalmiento3artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaMotriz);

                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterAcanalamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.angulosAcanalamiento);
                        ArrayAdapter<String> adapterInclinacionZonaCarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.inclinacionZonaCarga);
                        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);

                        spinnerTieneRodillosImpacto.setAdapter(adapterSiNo);
                        spinnerCamaImpacto.setAdapter(adapterSiNo);
                        spinnerCamaSellado.setAdapter(adapterSiNo);
                        spinnerRodillosCarga.setAdapter(adapterEstadoPartes);
                        spinnerRodillosImpacto.setAdapter(adapterEstadoPartes);
                        spinnerBasculaASGCO.setAdapter(adapterEstadoPartes);
                        spinnerBarrasImpacto.setAdapter(adapterEstadoPartes);
                        spinnerBarrasDeslizamiento.setAdapter(adapterEstadoPartes);
                        spinnerIntegridadSoportesCamaImpactoSellado.setAdapter(adapterEstadoPartes);
                        spinnerIntegridadSoportesRodilloImpacto.setAdapter(adapterEstadoPartes);
                        spinnerMaterialAtrapadoBanda.setAdapter(adapterSiNo);
                        spinnerMaterialAtrapadoCortinas.setAdapter(adapterSiNo);
                        spinnerMaterialAtrapadoGuardaBandas.setAdapter(adapterSiNo);
                        spinnerInclinacionZonaCargue.setAdapter(adapterInclinacionZonaCarga);
                        spinnerTipoRodilloRC.setAdapter(adapterTipoRodillo);
                        spinnerTipoRodilloRI.setAdapter(adapterTipoRodillo);
                        spinnerBasculaPesaje.setAdapter(adapterSiNo);

                        spinnerAnguloAcanalmiento1artesaPoleaCola.setAdapter(adapterAcanalamiento);
                        spinnerAnguloAcanalmiento2artesaPoleaCola.setAdapter(adapterAcanalamiento);
                        spinnerAnguloAcanalmiento3artesaPoleaCola.setAdapter(adapterAcanalamiento);
                        spinnerAnguloAcanalmiento1artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
                        spinnerAnguloAcanalmiento2artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
                        spinnerAnguloAcanalmiento3artesaPoleaMotriz.setAdapter(adapterAcanalamiento);

                        int p1 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getRodilloCarga());
                        int p2 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getRodilloImpacto());
                        int p3 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getBasculaASGCO());
                        int p4 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getBarraImpacto());
                        int p5 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getBarraDeslizamiento());
                        int p6 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getIntegridadSoporteCamaSellado());
                        int p7 = adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getIntegridadSoportesRodilloImpacto());

                        int p8 = adapterSiNo.getPosition(Login.loginJsons.get(i).getTieneRodillosImpacto());
                        int p9 = adapterSiNo.getPosition(Login.loginJsons.get(i).getCamaImpacto());
                        int p10 = adapterSiNo.getPosition(Login.loginJsons.get(i).getCamaSellado());
                        int p11 = adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAtrapadoEnBanda());
                        int p12 = adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAtrapadoEntreCortinas());
                        int p13 = adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas());


                        int p15 = adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa1());
                        int p16 = adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa2());
                        int p17= adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa3());
                        int p18 = adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz());
                        int p19 = adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz());
                        int p20 = adapterAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz());

                        int p21 = adapterTipoRodillo.getPosition(Login.loginJsons.get(i).getTipoRodilloCarga());
                        int p22 = adapterInclinacionZonaCarga.getPosition(Login.loginJsons.get(i).getInclinacionZonaCargue());
                        int p23 = adapterTipoRodillo.getPosition(Login.loginJsons.get(i).getTipoRodilloImpacto());
                        int p24 = adapterTipoRodillo.getPosition(Login.loginJsons.get(i).getBasculaPesaje());

                        spinnerRodillosCarga.setSelection(p1);
                        spinnerRodillosImpacto.setSelection(p2);
                        spinnerBasculaASGCO.setSelection(p3);
                        spinnerBarrasImpacto.setSelection(p4);
                        spinnerBarrasDeslizamiento.setSelection(p5);
                        spinnerIntegridadSoportesCamaImpactoSellado.setSelection(p6);
                        spinnerIntegridadSoportesRodilloImpacto.setSelection(p7);

                        spinnerTieneRodillosImpacto.setSelection(p8);
                        spinnerCamaImpacto.setSelection(p9);
                        spinnerCamaSellado.setSelection(p10);
                        spinnerMaterialAtrapadoBanda.setSelection(p11);
                        spinnerMaterialAtrapadoCortinas.setSelection(p12);
                        spinnerMaterialAtrapadoGuardaBandas.setSelection(p13);


                        spinnerAnguloAcanalmiento1artesaPoleaCola.setSelection(p15);
                        spinnerAnguloAcanalmiento2artesaPoleaCola.setSelection(p16);
                        spinnerAnguloAcanalmiento3artesaPoleaCola.setSelection(p17);
                        spinnerAnguloAcanalmiento1artesaPoleaMotriz.setSelection(p18);
                        spinnerAnguloAcanalmiento2artesaPoleaMotriz.setSelection(p19);
                        spinnerAnguloAcanalmiento3artesaPoleaMotriz.setSelection(p20);

                        spinnerTipoRodilloRC.setSelection(p21);

                        spinnerInclinacionZonaCargue.setSelection(p22);
                        spinnerTipoRodilloRI.setSelection(p23);
                        spinnerBasculaPesaje.setSelection(p24);

                        TextInputEditText txtLargoEjeRodilloCentral=dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
                        TextInputEditText txtDiametroEjeRodilloCentral=dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
                        TextInputEditText txtDiametroRodilloCentral=dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
                        TextInputEditText txtLargoTuboRodilloCentral=dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
                        TextInputEditText txtLargoEjeRodilloLateral=dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                        TextInputEditText txtDiametroEjeRodilloLateral=dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                        TextInputEditText txtDiametroRodilloLateral=dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                        TextInputEditText txtLargoTuboRodilloLateral=dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                        TextInputEditText txtAnchoInternoChasis=dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                        TextInputEditText txtAnchoExternoChasis=dialogParte.findViewById(R.id.txtAnchoExternoChasis);

                        TextInputEditText txtDetalleRodilloCentral=dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
                        TextInputEditText txtDetalleRodilloLateral=dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);
                        TextInputEditText txtEspesorUHMV=dialogParte.findViewById(R.id.txtEspesorUHMV);
                        final TextInputEditText txtAnchoBarra=dialogParte.findViewById(R.id.txtAnchoBarra);
                        final TextInputEditText txtLargoBarra=dialogParte.findViewById(R.id.txtLargoBarra);

                        txtLargoEjeRodilloCentral.setText(Login.loginJsons.get(i).getLargoEjeRodilloCentralCarga());
                        txtDiametroEjeRodilloCentral.setText(Login.loginJsons.get(i).getDiametroEjeRodilloCentralCarga());
                        txtDiametroRodilloCentral.setText(Login.loginJsons.get(i).getDiametroRodilloCentralCarga());
                        txtLargoTuboRodilloCentral.setText(Login.loginJsons.get(i).getLargoTuboRodilloCentralCarga());
                        txtLargoEjeRodilloLateral.setText(Login.loginJsons.get(i).getLargoEjeRodilloLateralCarga());
                        txtDiametroEjeRodilloLateral.setText(Login.loginJsons.get(i).getDiametroEjeRodilloLateralCarga());
                        txtDiametroRodilloLateral.setText(Login.loginJsons.get(i).getDiametroRodilloLateralCarga());
                        txtLargoTuboRodilloLateral.setText(Login.loginJsons.get(i).getLargoTuboRodilloLateralCarga());
                        txtAnchoInternoChasis.setText(Login.loginJsons.get(i).getAnchoInternoChasisRodilloCarga());
                        txtAnchoExternoChasis.setText(Login.loginJsons.get(i).getAnchoExternoChasisRodilloCarga());
                        txtDetalleRodilloCentral.setText(Login.loginJsons.get(i).getDetalleRodilloCentralCarga());
                        txtDetalleRodilloLateral.setText(Login.loginJsons.get(i).getDetalleRodilloLateralCarg());
                        txtEspesorUHMV.setText(Login.loginJsons.get(i).getEspesorUHMV());
                        txtAnchoBarra.setText(Login.loginJsons.get(i).getAnchoBarra());
                        txtLargoBarra.setText(Login.loginJsons.get(i).getLargoBarra());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Condicion Carga":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {

                        Constants.llenar();


                        Spinner spinnerTipoRevestimientoTolva = dialogParte.findViewById(R.id.spinnerTipoRvtoTolva);
                        Spinner spinnerEstadoRvtoTolva = dialogParte.findViewById(R.id.spinnerEstadoRvtoTolva);
                        Spinner spinnerDuracionRvto = dialogParte.findViewById(R.id.spinnerDuracionRvto);
                        Spinner spinnerDeflectores = dialogParte.findViewById(R.id.spinnerDeflectores);
                        Spinner spinnerAtaqueQuimico = dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
                        Spinner spinnerAtaqueTemperatura = dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
                        Spinner spinnerAtaqueAceites = dialogParte.findViewById(R.id.spinnerAtaqueAceites);
                        Spinner spinnerAtaqueAbrasivo = dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
                        Spinner spinnerHorasTrabajoDia = dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
                        Spinner spinnerDiasTrabajoSemana = dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
                        Spinner spinnerAbrasividad = dialogParte.findViewById(R.id.spinnerAbrasividad);
                        Spinner spinnerPorcentajeFinos = dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
                        Spinner spinnerCajaColaTolva = dialogParte.findViewById(R.id.spinnerCajaColaTolva);
                        Spinner spinnerFugaMateriales = dialogParte.findViewById(R.id.spinnerFugaDeMateriales);
                        Spinner spinnerFugaMatrialesCola = dialogParte.findViewById(R.id.spinnerFugaMaterialColaChute);
                        Spinner spinnerFugaMaterialesCostados = dialogParte.findViewById(R.id.spinnerFugaMaterialCostados);
                        Spinner spinnerFugaMaterialesParticulados = dialogParte.findViewById(R.id.spinnerFugaMaterialParticulado);
                        Spinner spinnerSistemaSujecion = dialogParte.findViewById(R.id.spinnerAbrazadera);
                        Spinner spinnerCauchoGuardaBandas = dialogParte.findViewById(R.id.spinnerCauchoGuardabandas);
                        Spinner spinnerTreSealMultiSeal = dialogParte.findViewById(R.id.spinnerTriSealMultiSeal);
                        Spinner spinnerProtectorGuardaBandas = dialogParte.findViewById(R.id.spinnerProtectorGuardaBandas);
                        Spinner spinnerCortina1 = dialogParte.findViewById(R.id.spinnerCortina1);
                        Spinner spinnerCortina2 = dialogParte.findViewById(R.id.spinnerCortina2);
                        Spinner spinnerCortina3 = dialogParte.findViewById(R.id.spinnerCortina3);
                        Spinner spinnerBoquillasAire = dialogParte.findViewById(R.id.spinnerBoquillasDeAire);
                        Spinner spinnerAlimentacionCentrada = dialogParte.findViewById(R.id.spinnerAlimentacionCentrada);
                        Spinner spinnerAtaqueImpacto = dialogParte.findViewById(R.id.spinnerAtaqueImpacto);

                        ArrayAdapter<String> adapterTipoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRevestimiento);
                        spinnerTipoRevestimientoTolva.setAdapter(adapterTipoRvtoTolva);

                        ArrayAdapter<String> adapterEstadoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        spinnerEstadoRvtoTolva.setAdapter(adapterEstadoRvtoTolva);

                        ArrayAdapter<String> adapterDuracionRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.meses);
                        spinnerDuracionRvto.setAdapter(adapterDuracionRvto);

                        ArrayAdapter<String> adapterDeflectores = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerDeflectores.setAdapter(adapterDeflectores);


                        ArrayAdapter<String> adapterMonitorPeligro = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerAtaqueQuimico.setAdapter(adapterMonitorPeligro);
                        spinnerAlimentacionCentrada.setAdapter(adapterMonitorPeligro);
                        spinnerAtaqueImpacto.setAdapter(adapterMonitorPeligro);

                        ArrayAdapter<String> adapterRodamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerAtaqueTemperatura.setAdapter(adapterRodamiento);

                        ArrayAdapter<String> adapterMonitorDesalineacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerAtaqueAceites.setAdapter(adapterMonitorDesalineacion);

                        ArrayAdapter<String> adapterMonitorVelocidad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        spinnerAtaqueAbrasivo.setAdapter(adapterMonitorVelocidad);

                        ArrayAdapter<String> adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
                        spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

                        ArrayAdapter<String> adapterIndicadorNivel = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
                        spinnerDiasTrabajoSemana.setAdapter(adapterIndicadorNivel);

                        ArrayAdapter<String> adapterCajaUnion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
                        spinnerAbrasividad.setAdapter(adapterCajaUnion);

                        ArrayAdapter<String> adapterAlarmaPantalla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
                        spinnerPorcentajeFinos.setAdapter(adapterAlarmaPantalla);

                        ArrayAdapter<String> adapterCajaCola = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        spinnerCajaColaTolva.setAdapter(adapterCajaCola);

                        ArrayAdapter<String> adapterFugaMateriales = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.fugaDeMateriales);
                        spinnerFugaMateriales.setAdapter(adapterFugaMateriales);
                        spinnerFugaMatrialesCola.setAdapter(adapterMonitorPeligro);
                        spinnerFugaMaterialesCostados.setAdapter(adapterMonitorPeligro);
                        spinnerFugaMaterialesParticulados.setAdapter(adapterMonitorPeligro);

                        spinnerSistemaSujecion.setAdapter(adapterEstadoRvtoTolva);
                        spinnerCauchoGuardaBandas.setAdapter(adapterEstadoRvtoTolva);
                        spinnerTreSealMultiSeal.setAdapter(adapterEstadoRvtoTolva);

                        spinnerCortina1.setAdapter(adapterEstadoRvtoTolva);
                        spinnerCortina2.setAdapter(adapterEstadoRvtoTolva);
                        spinnerCortina3.setAdapter(adapterEstadoRvtoTolva);
                        spinnerBoquillasAire.setAdapter(adapterEstadoRvtoTolva);

                        ArrayAdapter<String> adapterProtectorGuardaBandas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.protectorGuardaBandas);
                        spinnerProtectorGuardaBandas.setAdapter(adapterProtectorGuardaBandas);

                        spinnerTipoRevestimientoTolva.setSelection(adapterTipoRvtoTolva.getPosition(Login.loginJsons.get(i).getTipoRevestimientoTolvaCarga()));
                        spinnerEstadoRvtoTolva.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoTolvaCarga()));
                        spinnerDuracionRvto.setSelection(adapterDuracionRvto.getPosition(Login.loginJsons.get(i).getDuracionPromedioRevestimiento()));
                        spinnerDeflectores.setSelection(adapterDeflectores.getPosition(Login.loginJsons.get(i).getDeflectores()));
                        spinnerAtaqueQuimico.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getAtaqueQuimicoTransportadora()));
                        spinnerAtaqueTemperatura.setSelection(adapterRodamiento.getPosition(Login.loginJsons.get(i).getAtaqueTemperaturaTransportadora()));
                        spinnerAtaqueAceites.setSelection(adapterMonitorDesalineacion.getPosition(Login.loginJsons.get(i).getAtaqueAceiteTransportadora()));
                        spinnerAtaqueAbrasivo.setSelection(adapterMonitorVelocidad.getPosition(Login.loginJsons.get(i).getAtaqueAbrasivoTransportadora()));
                        spinnerHorasTrabajoDia.setSelection(adapterSensorInductivo.getPosition(Login.loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()));
                        spinnerDiasTrabajoSemana.setSelection(adapterIndicadorNivel.getPosition(Login.loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()));
                        spinnerAbrasividad.setSelection(adapterCajaUnion.getPosition(Login.loginJsons.get(i).getAbrasividadTransportadora()));
                        spinnerPorcentajeFinos.setSelection(adapterAlarmaPantalla.getPosition(Login.loginJsons.get(i).getPorcentajeFinosTransportadora()));
                        spinnerCajaColaTolva.setSelection(adapterCajaCola.getPosition(Login.loginJsons.get(i).getCajaColaDeTolva()));
                        spinnerFugaMateriales.setSelection(adapterFugaMateriales.getPosition(Login.loginJsons.get(i).getFugaMateriales()));
                        spinnerFugaMatrialesCola.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()));
                        spinnerFugaMaterialesCostados.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getFugaDeMaterialesPorLosCostados()));
                        spinnerFugaMaterialesParticulados.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()));
                        spinnerSistemaSujecion.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getAbrazadera()));
                        spinnerCauchoGuardaBandas.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getCauchoGuardabandas()));
                        spinnerTreSealMultiSeal.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getTriSealMultiSeal()));
                        spinnerProtectorGuardaBandas.setSelection(adapterProtectorGuardaBandas.getPosition(Login.loginJsons.get(i).getProtectorGuardaBandas()));
                        spinnerCortina1.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getCortinaAntiPolvo1()));
                        spinnerCortina2.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getCortinaAntiPolvo2()));
                        spinnerCortina3.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getCortinaAntiPolvo3()));
                        spinnerBoquillasAire.setSelection(adapterEstadoRvtoTolva.getPosition(Login.loginJsons.get(i).getBoquillasCanonesDeAire()));
                        spinnerAlimentacionCentrada.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getAlimentacionCentradaTransportadora()));
                        spinnerAtaqueImpacto.setSelection(adapterMonitorPeligro.getPosition(Login.loginJsons.get(i).getAtaqueImpactoTransportadora()));

                        TextInputEditText txtAlturaCaida=dialogParte.findViewById(R.id.txtAlturaCaida);
                        TextInputEditText txtLongitudImpacto=dialogParte.findViewById(R.id.txtLongitudImpacto);
                        TextInputEditText txtMaterial=dialogParte.findViewById(R.id.txtMaterial);
                        TextInputEditText txtMaxGranulometria=dialogParte.findViewById(R.id.txtMaxGranulometria);
                        TextInputEditText txtTempMaxMaterialBanda=dialogParte.findViewById(R.id.txtTempMaxSobreBanda);
                        TextInputEditText txtTempPromedioBanda=dialogParte.findViewById(R.id.txtTempPromedioBanda);
                        TextInputEditText txtMaxPeso=dialogParte.findViewById(R.id.txtMaxPeso);
                        TextInputEditText txtDensidad=dialogParte.findViewById(R.id.txtDensidadMaterial);
                        TextInputEditText txtAnchoChute=dialogParte.findViewById(R.id.txtAnchoChute);
                        TextInputEditText txtLargoChute=dialogParte.findViewById(R.id.txtLargoChute);
                        TextInputEditText txtAlturaChute=dialogParte.findViewById(R.id.txtAlturaChute);
                        TextInputEditText txtEspesorGuardabandas=dialogParte.findViewById(R.id.txtEspesorGuardabandas);
                        TextInputEditText txtAnchoGuardabandas=dialogParte.findViewById(R.id.txtAnchoGuardaBandas);
                        TextInputEditText txtLargoGuardabandas=dialogParte.findViewById(R.id.txtLargoGuardaBandas);
                        TextInputEditText txtTempAmbienteMinimaHorizontal=dialogParte.findViewById(R.id.txtTempAmbienteMinimaHorizontal);
                        TextInputEditText txtTempAmbienteMaximaHorizontal=dialogParte.findViewById(R.id.txtTempAmbienteMaximaHorizontal);
                        TextInputEditText txtAnguloSobrecarga=dialogParte.findViewById(R.id.txtAnguloSobreCarga);
                        TextInputEditText txtCapacidadHorizontal=dialogParte.findViewById(R.id.txtCapacidadHorizontal);


                        txtAlturaCaida.setText(Login.loginJsons.get(i).getAltureCaida());
                        txtLongitudImpacto.setText(Login.loginJsons.get(i).getLongitudImpacto());
                        txtMaterial.setText(Login.loginJsons.get(i).getMaterial());
                        txtMaxGranulometria.setText(Login.loginJsons.get(i).getMaxGranulometriaTransportadora());
                        txtTempMaxMaterialBanda.setText(Login.loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora());
                        txtTempPromedioBanda.setText(Login.loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora());
                        txtMaxPeso.setText(Login.loginJsons.get(i).getMaxPesoTransportadora());
                        txtDensidad.setText(Login.loginJsons.get(i).getDensidadTransportadora());
                        txtAnchoChute.setText(Login.loginJsons.get(i).getAnchoChute());
                        txtLargoChute.setText(Login.loginJsons.get(i).getLargoChute());
                        txtAlturaChute.setText(Login.loginJsons.get(i).getAlturaChute());
                        txtEspesorGuardabandas.setText(Login.loginJsons.get(i).getEspesorGuardaBandas());
                        txtAnchoGuardabandas.setText(Login.loginJsons.get(i).getAnchoGuardaBandas());
                        txtLargoGuardabandas.setText(Login.loginJsons.get(i).getLargoGuardaBandas());
                        txtTempAmbienteMinimaHorizontal.setText(Login.loginJsons.get(i).getTempAmbienteMinTransportadora());
                        txtTempAmbienteMaximaHorizontal.setText(Login.loginJsons.get(i).getTempAmbienteMaxTransportadora());
                        txtAnguloSobrecarga.setText(Login.loginJsons.get(i).getAnguloSobreCarga());
                        txtCapacidadHorizontal.setText(Login.loginJsons.get(i).getCapacidadTransportadora());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Limpiador Primario":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerLadoPasarelaSentidoBanda = dialogParte.findViewById(R.id.spinnerLadoPasarelaLP);
                        Spinner spinnerMaterialAlimenticioLP = dialogParte.findViewById(R.id.spinnerMaterialAlimenticioLP);
                        Spinner spinnerMaterialAcidoLP = dialogParte.findViewById(R.id.spinnerMaterialAcidoLP);
                        Spinner spinnerMaterial80y150LP = dialogParte.findViewById(R.id.spinnerMat80y15GradosLP);
                        Spinner spinnerMaterialSecoLP = dialogParte.findViewById(R.id.spinnerMaterialSecoLP);
                        Spinner spinnerMaterialHumedoLP = dialogParte.findViewById(R.id.spinnerMaterialHumedoLP);
                        Spinner spinnerMaterialAbrasivoFinoLP = dialogParte.findViewById(R.id.spinnerMaterialAbrasivoFinoLP);
                        Spinner spinnerMaterialPegajosoLP = dialogParte.findViewById(R.id.spinnerMaterialPegajosoLP);
                        Spinner spinnerMaterialAceitosoGrasosoLP = dialogParte.findViewById(R.id.spinnerMaterialAceitosoGrasosoLP);

                        Spinner spinnerMarcaLP = dialogParte.findViewById(R.id.spinnerMarcaLP);
                        Spinner spinnerReferenciaLP = dialogParte.findViewById(R.id.spinnerReferenciaLP);
                        Spinner spinnerEstadoCuchillaLP = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLP);
                        Spinner spinnerEstadoTensorLP = dialogParte.findViewById(R.id.spinnerEstadoTensorLP);
                        Spinner spinnerEstadoTuboLP = dialogParte.findViewById(R.id.spinnerEstadoTuboLP);
                        Spinner spinnerFrecRevisionCuchillaLP = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLP);
                        Spinner spinnerCuchillaEnContactoConBandaLP = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLP);


                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
                        ArrayAdapter<String> adapterLadosPasarela = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.ladoPasarela);
                        ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);
                        ArrayAdapter<String> adapterReferenciaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.referenciaLimpiador);

                        spinnerMaterialAlimenticioLP.setAdapter(adapterSiNo);
                        spinnerMaterialAcidoLP.setAdapter(adapterSiNo);
                        spinnerMaterial80y150LP.setAdapter(adapterSiNo);
                        spinnerMaterialSecoLP.setAdapter(adapterSiNo);
                        spinnerMaterialHumedoLP.setAdapter(adapterSiNo);
                        spinnerMaterialAbrasivoFinoLP.setAdapter(adapterSiNo);
                        spinnerMaterialPegajosoLP.setAdapter(adapterSiNo);
                        spinnerMaterialAceitosoGrasosoLP.setAdapter(adapterSiNo);
                        spinnerCuchillaEnContactoConBandaLP.setAdapter(adapterSiNo);

                        spinnerEstadoCuchillaLP.setAdapter(adapterEstadoPartes);
                        spinnerEstadoTuboLP.setAdapter(adapterEstadoPartes);
                        spinnerEstadoTensorLP.setAdapter(adapterEstadoPartes);
                        spinnerFrecRevisionCuchillaLP.setAdapter(adapterFrecRevisionCuchilla);
                        spinnerLadoPasarelaSentidoBanda.setAdapter(adapterLadosPasarela);
                        spinnerMarcaLP.setAdapter(adapterMarcaLimpiador);
                        spinnerReferenciaLP.setAdapter(adapterReferenciaLimpiador);

                        TextInputEditText txtAnchoEstructura = dialogParte.findViewById(R.id.txtAnchoEstructuraLP);
                        TextInputEditText txtAnchoTrayectoCarga = dialogParte.findViewById(R.id.txtAnchoTrayectoCargaLP);
                        TextInputEditText txtAnchoCuchillaLP = dialogParte.findViewById(R.id.txtAnchoCuchillaLP);
                        TextInputEditText txtAltoCuchillaLP = dialogParte.findViewById(R.id.txtAltoCuchillaLP);


                        spinnerLadoPasarelaSentidoBanda.setSelection(adapterLadosPasarela.getPosition(Login.loginJsons.get(i).getPasarelaRespectoAvanceBanda()));
                        spinnerMaterialAlimenticioLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAlimenticioTransportadora()));
                        spinnerMaterialAcidoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAcidoTransportadora()));
                        spinnerMaterial80y150LP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialTempEntre80y150Transportadora()));
                        spinnerMaterialSecoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialSecoTransportadora()));
                        spinnerMaterialHumedoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialHumedoTransportadora()));
                        spinnerMaterialAbrasivoFinoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()));
                        spinnerMaterialPegajosoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialPegajosoTransportadora()));
                        spinnerMaterialAceitosoGrasosoLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()));
                        spinnerMarcaLP.setSelection(adapterMarcaLimpiador.getPosition(Login.loginJsons.get(i).getMarcaLimpiadorPrimario()));
                        spinnerReferenciaLP.setSelection(adapterReferenciaLimpiador.getPosition(Login.loginJsons.get(i).getReferenciaLimpiadorPrimario()));
                        spinnerEstadoCuchillaLP.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()));
                        spinnerEstadoTensorLP.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTensorLimpiadorPrimario()));
                        spinnerEstadoTuboLP.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTuboLimpiadorPrimario()));
                        spinnerFrecRevisionCuchillaLP.setSelection(adapterFrecRevisionCuchilla.getPosition(Login.loginJsons.get(i).getFrecuenciaRevisionCuchilla()));
                        spinnerCuchillaEnContactoConBandaLP.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getCuchillaEnContactoConBanda()));

                        txtAnchoEstructura.setText(Login.loginJsons.get(i).getAnchoEstructura());
                        txtAnchoTrayectoCarga.setText(Login.loginJsons.get(i).getAnchoTrayectoCarga());
                        txtAnchoCuchillaLP.setText(Login.loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario());
                        txtAltoCuchillaLP.setText(Login.loginJsons.get(i).getAltoCuchillaLimpiadorPrimario());


                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;


            case "Limpiador Secundario":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerMarcaLS = dialogParte.findViewById(R.id.spinnerMarcaLS);
                        Spinner spinnerReferenciaLS = dialogParte.findViewById(R.id.spinnerReferenciaLS);
                        Spinner spinnerEstadoCuchillaLS = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLS);
                        Spinner spinnerEstadoTensorLS = dialogParte.findViewById(R.id.spinnerEstadoTensorLS);
                        Spinner spinnerEstadoTuboLS = dialogParte.findViewById(R.id.spinnerEstadoTuboLS);
                        Spinner spinnerFrecRevisionCuchillaLS = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLS);
                        Spinner spinnerCuchillaEnContactoConBandaLS = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLS);
                        Spinner spinnerSistemaDribbleChuteLS = dialogParte.findViewById(R.id.spinnerSistemaDribbleChuteLS);

                        Spinner spinnerMarcaLT = dialogParte.findViewById(R.id.spinnerMarcaLT);
                        Spinner spinnerReferenciaLT = dialogParte.findViewById(R.id.spinnerReferenciaLT);
                        Spinner spinnerEstadoCuchillaLT = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLT);
                        Spinner spinnerEstadoTensorLT = dialogParte.findViewById(R.id.spinnerEstadoTensorLT);
                        Spinner spinnerEstadoTuboLT = dialogParte.findViewById(R.id.spinnerEstadoTuboLT);
                        Spinner spinnerFrecRevisionCuchillaLT = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLT);
                        Spinner spinnerCuchillaEnContactoConBandaLT = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLT);


                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
                        ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);
                        ArrayAdapter<String> adapterReferenciaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.referenciaLimpiador);

                        spinnerMarcaLS.setAdapter(adapterMarcaLimpiador);
                        spinnerMarcaLT.setAdapter(adapterMarcaLimpiador);

                        spinnerReferenciaLS.setAdapter(adapterReferenciaLimpiador);
                        spinnerReferenciaLT.setAdapter(adapterReferenciaLimpiador);

                        spinnerEstadoTensorLS.setAdapter(adapterEstadoPartes);
                        spinnerEstadoTensorLT.setAdapter(adapterEstadoPartes);

                        spinnerEstadoTuboLS.setAdapter(adapterEstadoPartes);
                        spinnerEstadoTuboLT.setAdapter(adapterEstadoPartes);

                        spinnerCuchillaEnContactoConBandaLS.setAdapter(adapterSiNo);
                        spinnerCuchillaEnContactoConBandaLT.setAdapter(adapterSiNo);
                        spinnerSistemaDribbleChuteLS.setAdapter(adapterSiNo);


                        spinnerEstadoCuchillaLS.setAdapter(adapterEstadoPartes);
                        spinnerEstadoCuchillaLT.setAdapter(adapterEstadoPartes);


                        spinnerFrecRevisionCuchillaLS.setAdapter(adapterFrecRevisionCuchilla);
                        spinnerFrecRevisionCuchillaLT.setAdapter(adapterFrecRevisionCuchilla);

                        TextInputEditText txtAnchoCuchillaLS=dialogParte.findViewById(R.id.txtAnchoCuchillaLS);
                        TextInputEditText txtAltoCuchillaLS=dialogParte.findViewById(R.id.txtAltoCuchillaLS);

                        TextInputEditText txtAnchoCuchillaLT=dialogParte.findViewById(R.id.txtAnchoCuchillaLT);
                        TextInputEditText txtAltoCuchillaLT=dialogParte.findViewById(R.id.txtAltoCuchillaLT);



                        spinnerMarcaLS.setSelection(adapterMarcaLimpiador.getPosition(Login.loginJsons.get(i).getMarcaLimpiadorSecundario()));
                        spinnerMarcaLT.setSelection(adapterMarcaLimpiador.getPosition(Login.loginJsons.get(i).getMarcaLimpiadorTerciario()));

                        spinnerSistemaDribbleChuteLS.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getSistemaDribbleChute()));

                        spinnerReferenciaLS.setSelection(adapterReferenciaLimpiador.getPosition(Login.loginJsons.get(i).getReferenciaLimpiadorSecundario()));
                        spinnerReferenciaLT.setSelection(adapterReferenciaLimpiador.getPosition(Login.loginJsons.get(i).getReferenciaLimpiadorTerciario()));

                        spinnerEstadoCuchillaLS.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()));
                        spinnerEstadoCuchillaLT.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()));

                        spinnerEstadoTensorLS.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTensorLimpiadorSecundario()));
                        spinnerEstadoTensorLT.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTensorLimpiadorTerciario()));

                        spinnerEstadoTuboLS.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTuboLimpiadorSecundario()));
                        spinnerEstadoTuboLT.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoTuboLimpiadorTerciario()));

                        spinnerFrecRevisionCuchillaLS.setSelection(adapterFrecRevisionCuchilla.getPosition(Login.loginJsons.get(i).getFrecuenciaRevisionCuchilla1()));
                        spinnerFrecRevisionCuchillaLT.setSelection(adapterFrecRevisionCuchilla.getPosition(Login.loginJsons.get(i).getFrecuenciaRevisionCuchilla2()));

                        spinnerCuchillaEnContactoConBandaLS.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getCuchillaEnContactoConBanda1()));
                        spinnerCuchillaEnContactoConBandaLT.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getCuchillaEnContactoConBanda2()));

                        txtAnchoCuchillaLS.setText(Login.loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario());
                        txtAltoCuchillaLS.setText(Login.loginJsons.get(i).getAltoCuchillaLimpiadorSecundario());

                        txtAnchoCuchillaLT.setText(Login.loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario());
                        txtAltoCuchillaLT.setText(Login.loginJsons.get(i).getAltoCuchillaLimpiadorTerciario());


                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Polea Amarre":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerTipoPAmarrePM = dialogParte.findViewById(R.id.spinnerTipoPAmarrePM);
                        Spinner spinnerIcobandasCentradaPAmarrePM = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPM);
                        Spinner spinnerEstadoRvtoPAmarrePM = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePM);

                        Spinner spinnerTipoPAmarrePC = dialogParte.findViewById(R.id.spinnerTipoPAmarrePC);
                        Spinner spinnerIcobandasCentradaPAmarrePC = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPC);
                        Spinner spinnerEstadoRvtoPAmarrePC = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePC);


                        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);

                        TextInputEditText txtDiametroPAmarrePM = dialogParte.findViewById(R.id.txtDiametroPAmarrePM);
                        TextInputEditText txtAnchoPAmarrePM = dialogParte.findViewById(R.id.txtAnchoPAmarrePM);
                        TextInputEditText txtDiametroEjePAmarrePM = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePM);
                        TextInputEditText txtLargoEjePAmarrePM = dialogParte.findViewById(R.id.txtLargoEjePAmarrePM);

                        TextInputEditText txtDiametroPAmarrePC = dialogParte.findViewById(R.id.txtDiametroPAmarrePC);
                        TextInputEditText txtAnchoPAmarrePC = dialogParte.findViewById(R.id.txtAnchoPAmarrePC);
                        TextInputEditText txtDiametroEjePAmarrePC = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePC);
                        TextInputEditText txtLargoEjePAmarrePC = dialogParte.findViewById(R.id.txtLargoEjePAmarrePC);

                        spinnerEstadoRvtoPAmarrePM.setAdapter(adapterEstadoPartes);
                        spinnerEstadoRvtoPAmarrePC.setAdapter(adapterEstadoPartes);
                        spinnerIcobandasCentradaPAmarrePC.setAdapter(adapterSiNo);
                        spinnerIcobandasCentradaPAmarrePM.setAdapter(adapterSiNo);
                        spinnerTipoPAmarrePM.setAdapter(adapterTipoPolea);
                        spinnerTipoPAmarrePC.setAdapter(adapterTipoPolea);

                        spinnerTipoPAmarrePM.setSelection(adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()));
                        spinnerIcobandasCentradaPAmarrePM.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()));
                        spinnerEstadoRvtoPAmarrePM.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()));
                        spinnerTipoPAmarrePC.setSelection(adapterTipoPolea.getPosition(Login.loginJsons.get(i).getTipoPoleaAmarrePoleaCola()));
                        spinnerIcobandasCentradaPAmarrePC.setSelection(adapterSiNo.getPosition(Login.loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()));
                        spinnerEstadoRvtoPAmarrePC.setSelection(adapterEstadoPartes.getPosition(Login.loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()));


                        txtDiametroPAmarrePM.setText(Login.loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz());
                        txtAnchoPAmarrePM.setText(Login.loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz());
                        txtDiametroEjePAmarrePM.setText(Login.loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz());
                        txtLargoEjePAmarrePM.setText(Login.loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz());
                        txtDiametroPAmarrePC.setText(Login.loginJsons.get(i).getDimetroPoleaAmarrePoleaCola());
                        txtAnchoPAmarrePC.setText(Login.loginJsons.get(i).getAnchoPoleaAmarrePoleaCola());
                        txtDiametroEjePAmarrePC.setText(Login.loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola());
                        txtLargoEjePAmarrePC.setText(Login.loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola());

                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Rodillo Carga":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
                        Spinner spinnerAnguloAcanalArtesaCarga = dialogParte.findViewById(R.id.spinnerAnguloAcanalArtesaCarga);

                        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.tipoRodilloCarga);
                        ArrayAdapter<String> adapterAnguloAcanalamiento= new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.angulosAcanalamiento);

                        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
                        spinnerAnguloAcanalArtesaCarga.setAdapter(adapterAnguloAcanalamiento);

                        TextInputEditText txtlargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
                        TextInputEditText txtDiametroEjeRodilloCentral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
                        TextInputEditText txtDiametroRodilloCentral= dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
                        TextInputEditText txtLargoTuboRodilloCentral= dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
                        TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                        TextInputEditText txtDiametroEjeRodilloLateral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                        TextInputEditText txtDiametroRodilloLateral= dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                        TextInputEditText txtLargoTuboRodilloLateral= dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                        TextInputEditText txtDistanciaArtesasCarga= dialogParte.findViewById(R.id.txtDistanciaArtesasCarga);
                        TextInputEditText txtAnchoInternoChasis= dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                        TextInputEditText txtAnchoExternoChasis= dialogParte.findViewById(R.id.txtAnchoExternoChasis);
                        TextInputEditText txtDetalleRodilloCargaCentral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
                        TextInputEditText txtDetalleRodilloCargaLateral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);

                        spinnerAnguloAcanalArtesaCarga.setSelection(adapterAnguloAcanalamiento.getPosition(Login.loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()));
                        spinnerTipoRodilloCarga.setSelection(adapterTipoRodillo.getPosition(Login.loginJsons.get(i).getTipoRodilloCarga()));

                        txtlargoEjeRodilloCentral.setText(Login.loginJsons.get(i).getLargoEjeRodilloCentralCarga());
                        txtDiametroEjeRodilloCentral.setText(Login.loginJsons.get(i).getDiametroEjeRodilloCentralCarga());
                        txtDiametroRodilloCentral.setText(Login.loginJsons.get(i).getDiametroRodilloCentralCarga());
                        txtLargoTuboRodilloCentral.setText(Login.loginJsons.get(i).getLargoTuboRodilloCentralCarga());
                        txtLargoEjeRodilloLateral.setText(Login.loginJsons.get(i).getLargoEjeRodilloLateralCarga());
                        txtDiametroEjeRodilloLateral.setText(Login.loginJsons.get(i).getDiametroEjeRodilloLateralCarga());
                        txtLargoTuboRodilloLateral.setText(Login.loginJsons.get(i).getLargoTuboRodilloLateralCarga());
                        txtDiametroRodilloLateral.setText(Login.loginJsons.get(i).getDiametroRodilloLateralCarga());
                        txtDistanciaArtesasCarga.setText(Login.loginJsons.get(i).getDistanciaEntreArtesasCarga());
                        txtAnchoInternoChasis.setText(Login.loginJsons.get(i).getAnchoInternoChasisRodilloCarga());
                        txtAnchoExternoChasis.setText(Login.loginJsons.get(i).getAnchoExternoChasisRodilloCarga());
                        txtDetalleRodilloCargaCentral.setText(Login.loginJsons.get(i).getDetalleRodilloCentralCarga());
                        txtDetalleRodilloCargaLateral.setText(Login.loginJsons.get(i).getDetalleRodilloLateralCarg());


                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;

            case "Rodillo Retorno":

                for (int i = 0; i < Login.loginJsons.size(); i++) {
                    if (Login.loginJsons.get(i).getIdRegistro().equals(FragmentPartesVertical.idMaximaRegistro.get(0).getMax())) {


                        final Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
                        final Spinner spinnerRodillosRetorno = dialogParte.findViewById(R.id.spinnerRodillosRetorno);

                        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.tipoRodilloCarga);
                        ArrayAdapter<String> adapterRodillosRetorno= new ArrayAdapter(getContext(),R.layout.estilo_spinner, Constants.estadoPartes);

                        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
                        spinnerRodillosRetorno.setAdapter(adapterRodillosRetorno);


                        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                        final TextInputEditText txtDiametroEjeRodilloLateral= dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                        final TextInputEditText txtDiametroRodilloLateral= dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                        final TextInputEditText txtLargoTuboRodilloLateral= dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                        final TextInputEditText txtDistanciaArtesasCarga= dialogParte.findViewById(R.id.txtDistanciaEntreRodillosRetorno);
                        final TextInputEditText txtAnchoInternoChasis= dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                        final TextInputEditText txtAnchoExternoChasis= dialogParte.findViewById(R.id.txtAnchoExternoChasis);
                        final TextInputEditText txtDetalleRodilloCargaCentral= dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);

                        spinnerRodillosRetorno.setSelection(adapterRodillosRetorno.getPosition(Login.loginJsons.get(i).getEstadoRodilloRetorno()));
                        spinnerTipoRodilloCarga.setSelection(adapterTipoRodillo.getPosition(Login.loginJsons.get(i).getTipoRodilloRetorno()));


                        txtLargoEjeRodilloLateral.setText(Login.loginJsons.get(i).getLargoEjeRodilloRetorno());
                        txtDiametroEjeRodilloLateral.setText(Login.loginJsons.get(i).getDiametroEjeRodilloRetorno());
                        txtLargoTuboRodilloLateral.setText(Login.loginJsons.get(i).getLargoTuboRodilloRetorno());
                        txtDiametroRodilloLateral.setText(Login.loginJsons.get(i).getDiametroRodilloRetorno());
                        txtDistanciaArtesasCarga.setText(Login.loginJsons.get(i).getDistanciaEntreRodillosRetorno());
                        txtAnchoInternoChasis.setText(Login.loginJsons.get(i).getAnchoInternoChasisRetorno());
                        txtAnchoExternoChasis.setText(Login.loginJsons.get(i).getAnchoExternoChasisRetorno());
                        txtDetalleRodilloCargaCentral.setText(Login.loginJsons.get(i).getDetalleRodilloRetorno());



                        i = Login.loginJsons.size() + 1;

                    }
                }
                break;
        }
    }

    public void login(final String dialog) {
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
                                Toast.makeText(getContext(), "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.cancel();


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                dialogCarga.dismiss();

                            }
                        });
                        queue.add(requestCiudades);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();

                    }
                });
                queue.add(requestTransportadores);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                dialogCarga.dismiss();

            }
        });
        queue.add(requestLogin);
    }
}