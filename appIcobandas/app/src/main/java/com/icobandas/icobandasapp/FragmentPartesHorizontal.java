package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Database.DbHelper;
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

import static com.icobandas.icobandasapp.FragmentPartesVertical.fechaActual;
import static com.icobandas.icobandasapp.FragmentPartesVertical.idMaximaRegistro;
import static com.icobandas.icobandasapp.Login.ciudadesJsons;
import static com.icobandas.icobandasapp.Login.loginJsons;
import static com.icobandas.icobandasapp.Login.loginTransportadores;


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
    ImageButton btnObservacionRegistro;
    SQLiteDatabase db;
    DbHelper dbHelper;

    Cursor cursor;
    
    Gson gson = new Gson();

    RequestQueue queue;
    int idRegistroUpdate;

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
        Log.e("MAXIMA: ", FragmentSeleccionarTransportador.bandera);
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
            public void onClick(View v) {
                abrirDialogCondicionesCarga();
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
            public void onClick(View v) {
                abrirDialogLimpiadorPrimario();
            }
        });
        btnLimpiadorSecundarioTerciario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogLimpiadorSecundarioTerciario();
            }
        });
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

        btnObservacionRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogObservacion();
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
        btnRodilloCarga = view.findViewById(R.id.btnRodilloCarga);
        btnRodilloRetorno = view.findViewById(R.id.btnRodilloRetorno);
        btnObservacionRegistro=view.findViewById(R.id.btnObservacionRegistroElevadora);

        dbHelper = new DbHelper(getContext(), "prueba", null, 1);

        dialogCarga = new SpotsDialog(getContext(), "Ejecutando Registro");
        alerta = new AlertDialog.Builder(getContext());
    }

    public void abrirDialogObservacion()
    {
        dialogParte = new Dialog(getContext());
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_observacion_registro);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText txtObservacionRegistro=dialogParte.findViewById(R.id.txtObservacionRegistro);

        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
        {
            llenarRegistros("observacion");
        }


        Button btnEnviar = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("observacionRegistroTransportadora", txtObservacionRegistro.getText().toString());
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera = "Actualizar";


                            if (MainActivity.isOnline(getContext())) {
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

                                                String url = Constants.url + "observacionTransportadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();


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
                                                        params.put("observacionRegistroTransportadora", txtObservacionRegistro.getText().toString());
                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();
                            }
                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("observacionRegistroTransportadora", txtObservacionRegistro.getText().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }
                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarObservacionTransportadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                                dialogCarga.cancel();
                                                dialogParte.cancel();
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
                                                params.put("observacionRegistroTransportadora", txtObservacionRegistro.getText().toString());

                                                return params;
                                            }
                                        };
                                        requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }
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
    public void abrirDialogBanda() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_banda_horizontal);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final AutoCompleteTextView spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontal);
        final Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasHorizontal);
        final Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontal);
        final Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontal);
        final Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontal);
        final Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeHorizontal);
        final Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontal);
        final Spinner spinnerLocTensor = dialogParte.findViewById(R.id.spinnerLocalizacionTensor);
        final Spinner spinnerBandReversible = dialogParte.findViewById(R.id.spinnerBandaReversible);
        final Spinner spinnerBandaArrastre = dialogParte.findViewById(R.id.spinnerBandaDeArrastre);


        final AutoCompleteTextView spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontalAnterior);
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


        ArrayAdapter<String> adapterRoturaLona = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.resistenciaRoturaLona);
        spinnerResistenciaRotura.setAdapter(adapterRoturaLona);
        spinnerResistenciaRoturaAnterior.setAdapter(adapterRoturaLona);

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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("marcaBandaTransportadora", spinnerMarcaBanda.getText().toString());
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
                            params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getText().toString());
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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


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

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                        params.put("marcaBandaTransportadora", spinnerMarcaBanda.getText().toString());
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
                                                        params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getText().toString());
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
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("marcaBandaTransportadora", spinnerMarcaBanda.getText().toString());
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
                            params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getText().toString());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

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

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarBandaHorizontal";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();

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
                                        params.put("marcaBandaTransportadora", spinnerMarcaBanda.getText().toString());
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
                                        params.put("marcaBandaHorizontalAnterior", spinnerMarcaBandaAnterior.getText().toString());
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
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }

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
        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaColaHorizontal);
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

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtDiametro.getText().toString().equals("")) {
                                params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                            }
                            if (!txtAncho.getText().toString().equals("")) {
                                params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                            }
                            if (!txtLargoEje.getText().toString().equals("")) {
                                params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                            }
                            if (!txtDiametroEje.getText().toString().equals("")) {
                                params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                            }
                            if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                            }
                            if (!txtLongTornillo.getText().toString().equals("")) {
                                params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString())));
                            }
                            if (!txtLongContrapesa.getText().toString().equals("")) {
                                params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera = "Actualizar";


                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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

                                                        if (!txtDiametro.getText().toString().equals("")) {
                                                            params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                                        }
                                                        if (!txtAncho.getText().toString().equals("")) {
                                                            params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                                        }
                                                        if (!txtLargoEje.getText().toString().equals("")) {
                                                            params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                                        }
                                                        if (!txtDiametroEje.getText().toString().equals("")) {
                                                            params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                                        }
                                                        if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                                            params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                                        }
                                                        if (!txtLongTornillo.getText().toString().equals("")) {
                                                            params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString())));
                                                        }
                                                        if (!txtLongContrapesa.getText().toString().equals("")) {
                                                            params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString())));
                                                        }

                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtDiametro.getText().toString().equals("")) {
                                params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                            }
                            if (!txtAncho.getText().toString().equals("")) {
                                params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                            }
                            if (!txtLargoEje.getText().toString().equals("")) {
                                params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                            }
                            if (!txtDiametroEje.getText().toString().equals("")) {
                                params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                            }
                            if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                            }
                            if (!txtLongTornillo.getText().toString().equals("")) {
                                params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString())));
                            }
                            if (!txtLongContrapesa.getText().toString().equals("")) {
                                params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarPoleaColaHorizontal";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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

                                        if (!txtDiametro.getText().toString().equals("")) {
                                            params.put("diametroPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                        }
                                        if (!txtAncho.getText().toString().equals("")) {
                                            params.put("anchoPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                        }
                                        if (!txtLargoEje.getText().toString().equals("")) {
                                            params.put("largoEjePoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                        }
                                        if (!txtDiametroEje.getText().toString().equals("")) {
                                            params.put("diametroEjePoleaColaHorizontal", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                        }
                                        if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                            params.put("distanciaTransicionPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                        }
                                        if (!txtLongTornillo.getText().toString().equals("")) {
                                            params.put("longitudTensorTornilloPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongTornillo.getText().toString())));
                                        }
                                        if (!txtLongContrapesa.getText().toString().equals("")) {
                                            params.put("longitudRecorridoContrapesaPoleaColaTransportadora", String.valueOf(Float.parseFloat(txtLongContrapesa.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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

        final Spinner spinnerAnchoVPlow = dialogParte.findViewById(R.id.spinnerAnchoCauchoVPlow);
        final Spinner spinnerEspesorCaucho = dialogParte.findViewById(R.id.spinnerEspesorCauchoVPlow);


        ArrayAdapter<String> adapterHayDesviador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerHayDesviador.setAdapter(adapterHayDesviador);

        ArrayAdapter<String> adapterElDesviadoBascula = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerElDesviadorBascula.setAdapter(adapterElDesviadoBascula);

        ArrayAdapter<String> adapterPresionUniforme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerHayPresionUniforme.setAdapter(adapterPresionUniforme);

        ArrayAdapter<String> adapterCauchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerCauchoVPlow.setAdapter(adapterCauchoVPlow);

        ArrayAdapter<String> adapterAnchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anchoVPlow);
        spinnerAnchoVPlow.setAdapter(adapterAnchoVPlow);

        ArrayAdapter<String> adapterEspesorVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorVPlow);
        spinnerEspesorCaucho.setAdapter(adapterEspesorVPlow);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Desviador");
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();


                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("anchoVPlow", spinnerAnchoVPlow.getSelectedItem().toString());
                            params.put("espesorVPlow", spinnerEspesorCaucho.getSelectedItem().toString());
                            params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                            params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                            params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                            params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera = "Actualizar";


                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                        params.put("anchoVPlow", spinnerAnchoVPlow.getSelectedItem().toString());
                                                        params.put("espesorVPlow", spinnerEspesorCaucho.getSelectedItem().toString());
                                                        params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                                                        params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                                                        params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                                                        params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());
                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("anchoVPlow", spinnerAnchoVPlow.getSelectedItem().toString());
                            params.put("espesorVPlow", spinnerEspesorCaucho.getSelectedItem().toString());
                            params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                            params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                            params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                            params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarDesviador";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                        params.put("anchoVPlow", spinnerAnchoVPlow.getSelectedItem().toString());
                                        params.put("espesorVPlow", spinnerEspesorCaucho.getSelectedItem().toString());
                                        params.put("cauchoVPlow", spinnerCauchoVPlow.getSelectedItem().toString());
                                        params.put("hayDesviador", spinnerHayDesviador.getSelectedItem().toString());
                                        params.put("elDesviadorBascula", spinnerElDesviadorBascula.getSelectedItem().toString());
                                        params.put("presionUniformeALoAnchoDeLaBanda", spinnerHayPresionUniforme.getSelectedItem().toString());


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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
        final Spinner spinnerEspesorGuardabandas = dialogParte.findViewById(R.id.spinnerEspesorGuardabandas);


        final TextInputEditText txtAlturaCaida = dialogParte.findViewById(R.id.txtAlturaCaida);
        final Spinner spinnerLongitudImpacto = dialogParte.findViewById(R.id.spinnerLongitudImpacto);
        final TextInputEditText txtMaterial = dialogParte.findViewById(R.id.txtMaterial);
        final TextInputEditText txtMaxGranulometria = dialogParte.findViewById(R.id.txtMaxGranulometria);
        final Spinner txtTempMaxMaterialBanda = dialogParte.findViewById(R.id.spinnerTempMaxSobreBanda);
        final Spinner txtTempPromedioBanda = dialogParte.findViewById(R.id.spinnerTempPromedioBanda);
        final TextInputEditText txtMaxPeso = dialogParte.findViewById(R.id.txtMaxPeso);
        final TextInputEditText txtDensidad = dialogParte.findViewById(R.id.txtDensidadMaterial);
        final TextInputEditText txtAnchoChute = dialogParte.findViewById(R.id.txtAnchoChute);
        final TextInputEditText txtLargoChute = dialogParte.findViewById(R.id.txtLargoChute);
        final TextInputEditText txtAlturaChute = dialogParte.findViewById(R.id.txtAlturaChute);
        final TextInputEditText txtAnchoGuardabandas = dialogParte.findViewById(R.id.txtAnchoGuardaBandas);
        final TextInputEditText txtLargoGuardabandas = dialogParte.findViewById(R.id.txtLargoGuardaBandas);
        final TextInputEditText txtTempAmbienteMinimaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMinimaHorizontal);
        final TextInputEditText txtTempAmbienteMaximaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMaximaHorizontal);
        final TextInputEditText txtAnguloSobrecarga = dialogParte.findViewById(R.id.txtAnguloSobreCarga);
        final TextInputEditText txtCapacidadHorizontal = dialogParte.findViewById(R.id.txtCapacidadHorizontal);

        ArrayAdapter<String> adapterTipoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRevestimiento);
        spinnerTipoRevestimientoTolva.setAdapter(adapterTipoRvtoTolva);

        ArrayAdapter<String> adapterEstadoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvtoTolva.setAdapter(adapterEstadoRvtoTolva);

        ArrayAdapter<String> adapterDuracionRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.meses);
        spinnerDuracionRvto.setAdapter(adapterDuracionRvto);

        ArrayAdapter<String> adapterDeflectores = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerDeflectores.setAdapter(adapterDeflectores);

        ArrayAdapter<String> adapterEspesorGuardabadas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorVPlow);
        spinnerEspesorGuardabandas.setAdapter(adapterEspesorGuardabadas);


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

        ArrayAdapter<String> adapterLongitudImpacto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.longitudImpacto);
        spinnerLongitudImpacto.setAdapter(adapterLongitudImpacto);

        ArrayAdapter<String> adapterTempSobreBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
        txtTempMaxMaterialBanda.setAdapter(adapterTempSobreBanda);
        txtTempPromedioBanda.setAdapter(adapterTempSobreBanda);

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

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();


                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                            params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                            params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                            params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                            params.put("altureCaida", txtAlturaCaida.getText().toString());
                            params.put("longitudImpacto", spinnerLongitudImpacto.getSelectedItem().toString());
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
                            params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getSelectedItem().toString());
                            params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getSelectedItem().toString());
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
                            params.put("espesorGuardaBandas", spinnerEspesorGuardabandas.getSelectedItem().toString());
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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtAlturaCaida.getText().toString().equals("")) {
                                params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));

                            }

                            if (!txtAnguloSobrecarga.getText().toString().equals("")) {
                                params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                            }
                            if (!txtMaxGranulometria.getText().toString().equals("")) {
                                params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                            }
                            if (!txtMaxPeso.getText().toString().equals("")) {
                                params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                            }
                            if (!txtDensidad.getText().toString().equals("")) {
                                params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                            }

                            if (!txtAnchoChute.getText().toString().equals("")) {
                                params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                            }
                            if (!txtLargoChute.getText().toString().equals("")) {
                                params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                            }
                            if (!txtAlturaChute.getText().toString().equals("")) {
                                params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                            }
                            if (!txtAnchoGuardabandas.getText().toString().equals("")) {
                                params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                            }
                            if (!txtLargoGuardabandas.getText().toString().equals("")) {
                                params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                            }
                            if (!txtCapacidadHorizontal.getText().toString().equals("")) {
                                params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                            }
                            if (!txtTempAmbienteMaximaHorizontal.getText().toString().equals("")) {
                                params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                            }
                            if (!txtTempAmbienteMinimaHorizontal.getText().toString().equals("")) {
                                params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            FragmentSeleccionarTransportador.bandera="Actualizar";


                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                        params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                                                        params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                                                        params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                                                        params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                                                        params.put("altureCaida", txtAlturaCaida.getText().toString());
                                                        params.put("longitudImpacto", spinnerLongitudImpacto.getSelectedItem().toString());
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
                                                        params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getSelectedItem().toString());
                                                        params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getSelectedItem().toString());
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
                                                        params.put("espesorGuardaBandas", spinnerEspesorGuardabandas.getSelectedItem().toString());
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

                                                        if (!txtAlturaCaida.getText().toString().equals("")) {
                                                            params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));
                                                        }

                                                        if (!txtAnguloSobrecarga.getText().toString().equals("")) {
                                                            params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                                                        }
                                                        if (!txtMaxGranulometria.getText().toString().equals("")) {
                                                            params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                                                        }
                                                        if (!txtMaxPeso.getText().toString().equals("")) {
                                                            params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                                                        }
                                                        if (!txtDensidad.getText().toString().equals("")) {
                                                            params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                                                        }

                                                        if (!txtAnchoChute.getText().toString().equals("")) {
                                                            params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                                                        }
                                                        if (!txtLargoChute.getText().toString().equals("")) {
                                                            params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                                                        }
                                                        if (!txtAlturaChute.getText().toString().equals("")) {
                                                            params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                                                        }
                                                        if (!txtAnchoGuardabandas.getText().toString().equals("")) {
                                                            params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                                                        }
                                                        if (!txtLargoGuardabandas.getText().toString().equals("")) {
                                                            params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                                                        }
                                                        if (!txtCapacidadHorizontal.getText().toString().equals("")) {
                                                            params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                                                        }
                                                        if (!txtTempAmbienteMaximaHorizontal.getText().toString().equals("")) {
                                                            params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                                                        }
                                                        if (!txtTempAmbienteMinimaHorizontal.getText().toString().equals("")) {
                                                            params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                                                        }
                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                            params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                            params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                            params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                            params.put("altureCaida", txtAlturaCaida.getText().toString());
                            params.put("longitudImpacto", spinnerLongitudImpacto.getSelectedItem().toString());
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
                            params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getSelectedItem().toString());
                            params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getSelectedItem().toString());
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
                            params.put("espesorGuardaBandas", spinnerEspesorGuardabandas.getSelectedItem().toString());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtAlturaCaida.getText().toString().equals("")) {
                                params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));
                            }

                            if (!txtAnguloSobrecarga.getText().toString().equals("")) {
                                params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                            }
                            if (!txtMaxGranulometria.getText().toString().equals("")) {
                                params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                            }
                            if (!txtMaxPeso.getText().toString().equals("")) {
                                params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                            }
                            if (!txtDensidad.getText().toString().equals("")) {
                                params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                            }

                            if (!txtAnchoChute.getText().toString().equals("")) {
                                params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                            }
                            if (!txtLargoChute.getText().toString().equals("")) {
                                params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                            }
                            if (!txtAlturaChute.getText().toString().equals("")) {
                                params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                            }
                            if (!txtAnchoGuardabandas.getText().toString().equals("")) {
                                params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                            }
                            if (!txtLargoGuardabandas.getText().toString().equals("")) {
                                params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                            }
                            if (!txtCapacidadHorizontal.getText().toString().equals("")) {
                                params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                            }
                            if (!txtTempAmbienteMaximaHorizontal.getText().toString().equals("")) {
                                params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                            }
                            if (!txtTempAmbienteMinimaHorizontal.getText().toString().equals("")) {
                                params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarCondicionCargaTransportadora";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                        params.put("tipoRevestimientoTolvaCarga", spinnerTipoRevestimientoTolva.getSelectedItem().toString());
                                        params.put("estadoRevestimientoTolvaCarga", spinnerEstadoRvtoTolva.getSelectedItem().toString());
                                        params.put("duracionPromedioRevestimiento", spinnerDuracionRvto.getSelectedItem().toString());
                                        params.put("deflectores", spinnerDeflectores.getSelectedItem().toString());
                                        params.put("altureCaida", txtAlturaCaida.getText().toString());
                                        params.put("longitudImpacto", spinnerLongitudImpacto.getSelectedItem().toString());
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
                                        params.put("tempMaximaMaterialSobreBandaTransportadora", txtTempMaxMaterialBanda.getSelectedItem().toString());
                                        params.put("tempPromedioMaterialSobreBandaTransportadora", txtTempPromedioBanda.getSelectedItem().toString());
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
                                        params.put("espesorGuardaBandas", spinnerEspesorGuardabandas.getSelectedItem().toString());
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

                                        if (!txtAlturaCaida.getText().toString().equals("") || !(txtAlturaCaida.getText() == null)) {
                                            params.put("altureCaida", String.valueOf(Float.parseFloat(txtAlturaCaida.getText().toString())));
                                        }

                                        if (!txtAnguloSobrecarga.getText().toString().equals("")) {
                                            params.put("anguloSobreCarga", String.valueOf(Float.parseFloat(txtAnguloSobrecarga.getText().toString())));
                                        }
                                        if (!txtMaxGranulometria.getText().toString().equals("")) {
                                            params.put("maxGranulometriaTransportadora", String.valueOf(Float.parseFloat(txtMaxGranulometria.getText().toString())));
                                        }
                                        if (!txtMaxPeso.getText().toString().equals("")) {
                                            params.put("maxPesoTransportadora", String.valueOf(Float.parseFloat(txtMaxPeso.getText().toString())));
                                        }
                                        if (!txtDensidad.getText().toString().equals("")) {
                                            params.put("densidadTransportadora", String.valueOf(Float.parseFloat(txtDensidad.getText().toString())));
                                        }

                                        if (!txtAnchoChute.getText().toString().equals("")) {
                                            params.put("anchoChute", String.valueOf(Float.parseFloat(txtAnchoChute.getText().toString())));
                                        }
                                        if (!txtLargoChute.getText().toString().equals("")) {
                                            params.put("largoChute", String.valueOf(Float.parseFloat(txtLargoChute.getText().toString())));
                                        }
                                        if (!txtAlturaChute.getText().toString().equals("")) {
                                            params.put("alturaChute", String.valueOf(Float.parseFloat(txtAlturaChute.getText().toString())));
                                        }
                                        if (!txtAnchoGuardabandas.getText().toString().equals("")) {
                                            params.put("anchoGuardaBandas", String.valueOf(Float.parseFloat(txtAnchoGuardabandas.getText().toString())));
                                        }
                                        if (!txtLargoGuardabandas.getText().toString().equals("")) {
                                            params.put("largoGuardaBandas", String.valueOf(Float.parseFloat(txtLargoGuardabandas.getText().toString())));
                                        }
                                        if (!txtCapacidadHorizontal.getText().toString().equals("")) {
                                            params.put("capacidadTransportadora", String.valueOf(Float.parseFloat(txtCapacidadHorizontal.getText().toString())));
                                        }
                                        if (!txtTempAmbienteMaximaHorizontal.getText().toString().equals("")) {
                                            params.put("tempAmbienteMaxTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMaximaHorizontal.getText().toString())));
                                        }
                                        if (!txtTempAmbienteMinimaHorizontal.getText().toString().equals("")) {
                                            params.put("tempAmbienteMinTransportadora", String.valueOf(Float.parseFloat(txtTempAmbienteMinimaHorizontal.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {

                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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

        TextView tvDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral);
        TextView tvDetalleRodilloCargaCentral1 = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral1);
        tvDetalleRodilloCargaCentral.setOnClickListener(this);
        tvDetalleRodilloCargaCentral1.setOnClickListener(this);

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
        final Spinner spinnerTipoRodilloRC = dialogParte.findViewById(R.id.spinnerTipoRodilloRC);
        final Spinner spinnerTipoRodilloRI = dialogParte.findViewById(R.id.spinnerTipoRodilloRI);
        final Spinner spinnerBasculaPesaje = dialogParte.findViewById(R.id.spinnerBasculaPesaje);
        final Spinner spinnerEspesorUHMV = dialogParte.findViewById(R.id.spinnerEspesorUHMV);
        final Spinner spinnerAnchoBarra = dialogParte.findViewById(R.id.spinnerAnchoBarra);


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
        ArrayAdapter<String> adapterEspesorUHMV = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorUHMV);
        ArrayAdapter<String> adapterAnchoBarra = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anchoBarra);


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
        spinnerEspesorUHMV.setAdapter(adapterEspesorUHMV);
        spinnerAnchoBarra.setAdapter(adapterAnchoBarra);


        spinnerAnguloAcanalmiento1artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento2artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento3artesaPoleaCola.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento1artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento2artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
        spinnerAnguloAcanalmiento3artesaPoleaMotriz.setAdapter(adapterAcanalamiento);

        final TextInputEditText txtLargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
        final TextInputEditText txtDiametroEjeRodilloCentral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
        final TextInputEditText txtDiametroRodilloCentral = dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
        final TextInputEditText txtLargoTuboRodilloCentral = dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);


        final TextInputEditText txtLargoBarra = dialogParte.findViewById(R.id.txtLargoBarra);


        final TextInputEditText txtDetalleRodilloCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
        final TextInputEditText txtDetalleRodilloLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);


        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("espesorUHMV", spinnerEspesorUHMV.getSelectedItem().toString());
                            params.put("anchoBarra", spinnerAnchoBarra.getSelectedItem().toString());
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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtLargoBarra.getText().toString().equals("")) {
                                params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                            }
                            if (!txtLargoEjeRodilloCentral.getText().toString().equals("")) {

                                params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                            }
                            if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                            }
                            if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                            }
                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                            }
                            if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                            }
                            if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();


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
                                                        params.put("tieneRodillosImpacto", spinnerTieneRodillosImpacto.getSelectedItem().toString());
                                                        params.put("camaImpacto", spinnerCamaImpacto.getSelectedItem().toString());
                                                        params.put("camaSellado", spinnerCamaSellado.getSelectedItem().toString());
                                                        params.put("basculaPesaje", spinnerBasculaPesaje.getSelectedItem().toString());
                                                        params.put("rodilloCarga", spinnerRodillosCarga.getSelectedItem().toString());
                                                        params.put("rodilloImpacto", spinnerRodillosImpacto.getSelectedItem().toString());
                                                        params.put("barraImpacto", spinnerBarrasImpacto.getSelectedItem().toString());
                                                        params.put("basculaASGCO", spinnerBasculaASGCO.getSelectedItem().toString());
                                                        params.put("barraDeslizamiento", spinnerBarrasDeslizamiento.getSelectedItem().toString());
                                                        params.put("espesorUHMV", spinnerEspesorUHMV.getSelectedItem().toString());
                                                        params.put("anchoBarra", spinnerAnchoBarra.getSelectedItem().toString());
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

                                                        if (!txtLargoBarra.getText().toString().equals("")) {
                                                            params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                                                        }
                                                        if (!txtLargoEjeRodilloCentral.toString().equals("")) {
                                                            params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                                                        }
                                                        if (!txtDiametroEjeRodilloCentral.toString().equals("")) {
                                                            params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                                                        }
                                                        if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                                            params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                                                        }
                                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                                            params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                                                        }
                                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                                                        }
                                                        if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                                            params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("tieneRodillosImpacto", spinnerTieneRodillosImpacto.getSelectedItem().toString());
                            params.put("camaImpacto", spinnerCamaImpacto.getSelectedItem().toString());
                            params.put("camaSellado", spinnerCamaSellado.getSelectedItem().toString());
                            params.put("basculaPesaje", spinnerBasculaPesaje.getSelectedItem().toString());
                            params.put("rodilloCarga", spinnerRodillosCarga.getSelectedItem().toString());
                            params.put("rodilloImpacto", spinnerRodillosImpacto.getSelectedItem().toString());
                            params.put("barraImpacto", spinnerBarrasImpacto.getSelectedItem().toString());
                            params.put("basculaASGCO", spinnerBasculaASGCO.getSelectedItem().toString());
                            params.put("barraDeslizamiento", spinnerBarrasDeslizamiento.getSelectedItem().toString());
                            params.put("espesorUHMV", spinnerEspesorUHMV.getSelectedItem().toString());
                            params.put("anchoBarra", spinnerAnchoBarra.getSelectedItem().toString());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }


                            if (!txtLargoBarra.getText().toString().equals("")) {
                                params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                            }
                            if (!txtLargoEjeRodilloCentral.getText().toString().equals("")) {

                                params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                            }
                            if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                            }
                            if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                            }
                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                            }
                            if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                            }
                            if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarSoporteCarga";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                        params.put("tieneRodillosImpacto", spinnerTieneRodillosImpacto.getSelectedItem().toString());
                                        params.put("camaImpacto", spinnerCamaImpacto.getSelectedItem().toString());
                                        params.put("camaSellado", spinnerCamaSellado.getSelectedItem().toString());
                                        params.put("basculaPesaje", spinnerBasculaPesaje.getSelectedItem().toString());
                                        params.put("rodilloCarga", spinnerRodillosCarga.getSelectedItem().toString());
                                        params.put("rodilloImpacto", spinnerRodillosImpacto.getSelectedItem().toString());
                                        params.put("barraImpacto", spinnerBarrasImpacto.getSelectedItem().toString());
                                        params.put("basculaASGCO", spinnerBasculaASGCO.getSelectedItem().toString());
                                        params.put("barraDeslizamiento", spinnerBarrasDeslizamiento.getSelectedItem().toString());
                                        params.put("espesorUHMV", spinnerEspesorUHMV.getSelectedItem().toString());
                                        params.put("anchoBarra", spinnerAnchoBarra.getSelectedItem().toString());
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


                                        if (!txtLargoBarra.getText().toString().equals("")) {
                                            params.put("largoBarra", String.valueOf(Float.parseFloat(txtLargoBarra.getText().toString())));

                                        }
                                        if (!txtLargoEjeRodilloCentral.getText().toString().equals("")) {

                                            params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloCentral.getText().toString())));

                                        }
                                        if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                            params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));

                                        }
                                        if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                            params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));

                                        }
                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));

                                        }
                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                        }
                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                        }
                                        if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                            params.put("largoTuboRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));

                                        }
                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                            params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));

                                        }
                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                            params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));

                                        }
                                        if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                            params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));

                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {

                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }

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

        TextView tvRodillo = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral);
        TextView tvRodillo1 = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral1);
        TextView tvRodillo2 = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral2);

        tvRodillo.setOnClickListener(this);
        tvRodillo1.setOnClickListener(this);
        tvRodillo2.setOnClickListener(this);

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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarAlineacion";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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
        final TextInputEditText txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaColaHorizontal);
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
        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Polea Motriz");
        }


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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtDiametro.getText().toString().equals("")) {
                                params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                            }
                            if (!txtAncho.getText().toString().equals("")) {
                                params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                            }
                            if (!txtLargoEje.getText().toString().equals("")) {
                                params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                            }
                            if (!txtDiametroEje.getText().toString().equals("")) {
                                params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                            }
                            if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                            }
                            if (!txtHpMotor.getText().toString().equals("")) {
                                params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {


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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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

                                                        if (!txtDiametro.getText().toString().equals("")) {
                                                            params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                                        }
                                                        if (!txtAncho.getText().toString().equals("")) {
                                                            params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                                        }
                                                        if (!txtLargoEje.getText().toString().equals("")) {
                                                            params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                                        }
                                                        if (!txtDiametroEje.getText().toString().equals("")) {
                                                            params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                                        }
                                                        if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                                            params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                                        }
                                                        if (!txtHpMotor.getText().toString().equals("")) {
                                                            params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {
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

                                if (!txtDiametro.getText().toString().equals("")) {
                                    params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                }
                                if (!txtAncho.getText().toString().equals("")) {
                                    params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                }
                                if (!txtLargoEje.getText().toString().equals("")) {
                                    params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                }
                                if (!txtDiametroEje.getText().toString().equals("")) {
                                    params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                }
                                if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                    params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                }
                                if (!txtHpMotor.getText().toString().equals("")) {
                                    params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                                }

                                db.insert("bandaTransportadora", null, params);
                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtDiametro.getText().toString().equals("")) {
                                params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                            }
                            if (!txtAncho.getText().toString().equals("")) {
                                params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                            }
                            if (!txtLargoEje.getText().toString().equals("")) {
                                params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                            }
                            if (!txtDiametroEje.getText().toString().equals("")) {
                                params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                            }
                            if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                            }
                            if (!txtHpMotor.getText().toString().equals("")) {
                                params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarPoleaMotrizHorizontal";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();

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

                                        if (!txtDiametro.getText().toString().equals("")) {
                                            params.put("diametroPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametro.getText().toString())));
                                        }
                                        if (!txtAncho.getText().toString().equals("")) {
                                            params.put("anchoPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtAncho.getText().toString())));
                                        }
                                        if (!txtLargoEje.getText().toString().equals("")) {
                                            params.put("largoEjePoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtLargoEje.getText().toString())));
                                        }
                                        if (!txtDiametroEje.getText().toString().equals("")) {
                                            params.put("diametroEjeMotrizTransportadora", String.valueOf(Float.parseFloat(txtDiametroEje.getText().toString())));
                                        }
                                        if (!txtDistanciaTransicion.getText().toString().equals("")) {
                                            params.put("distanciaTransicionPoleaMotrizTransportadora", String.valueOf(Float.parseFloat(txtDistanciaTransicion.getText().toString())));
                                        }
                                        if (!txtHpMotor.getText().toString().equals("")) {
                                            params.put("potenciaMotorTransportadora", String.valueOf(Float.parseFloat(txtHpMotor.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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
        final TextInputEditText txtReferenciaLP = dialogParte.findViewById(R.id.txtReferenciaLP);
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


        final TextInputEditText txtAnchoEstructura = dialogParte.findViewById(R.id.txtAnchoEstructuraLP);
        final TextInputEditText txtAnchoTrayectoCarga = dialogParte.findViewById(R.id.txtAnchoTrayectoCargaLP);
        final TextInputEditText txtAnchoCuchillaLP = dialogParte.findViewById(R.id.txtAnchoCuchillaLP);
        final TextInputEditText txtAltoCuchillaLP = dialogParte.findViewById(R.id.txtAltoCuchillaLP);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("referenciaLimpiadorPrimario", txtReferenciaLP.getText().toString());
                            params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                            params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                            params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                            params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtAnchoEstructura.getText().toString().equals("")) {
                                params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                            }
                            if (!txtAnchoTrayectoCarga.getText().toString().equals("")) {
                                params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                            }
                            if (!txtAnchoCuchillaLP.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                            }
                            if (!txtAltoCuchillaLP.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                        params.put("referenciaLimpiadorPrimario", txtReferenciaLP.getText().toString());
                                                        params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                                                        params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                                                        params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                                                        params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                                                        params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                                                        params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                                                        params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                                                        params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());

                                                        if (!txtAnchoEstructura.getText().toString().equals("")) {
                                                            params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                                                        }
                                                        if (!txtAnchoTrayectoCarga.getText().toString().equals("")) {
                                                            params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                                                        }
                                                        if (!txtAnchoCuchillaLP.getText().toString().equals("")) {
                                                            params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                                                        }
                                                        if (!txtAltoCuchillaLP.getText().toString().equals("")) {
                                                            params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);

                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }
                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            params.put("referenciaLimpiadorPrimario", txtReferenciaLP.getText().toString());
                            params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                            params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                            params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                            params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());

                            if (!txtAnchoEstructura.getText().toString().equals("")) {
                                params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                            }
                            if (!txtAnchoTrayectoCarga.getText().toString().equals("")) {
                                params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                            }
                            if (!txtAnchoCuchillaLP.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                            }
                            if (!txtAltoCuchillaLP.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
                            }

                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);


                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarLimpiadorPrimario";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                        params.put("referenciaLimpiadorPrimario", txtReferenciaLP.getText().toString());
                                        params.put("anchoCuchillaLimpiadorPrimario", txtAnchoCuchillaLP.getText().toString());
                                        params.put("altoCuchillaLimpiadorPrimario", txtAltoCuchillaLP.getText().toString());
                                        params.put("estadoCuchillaLimpiadorPrimario", spinnerEstadoCuchillaLP.getSelectedItem().toString());
                                        params.put("estadoTensorLimpiadorPrimario", spinnerEstadoTensorLP.getSelectedItem().toString());
                                        params.put("estadoTuboLimpiadorPrimario", spinnerEstadoTuboLP.getSelectedItem().toString());
                                        params.put("frecuenciaRevisionCuchilla", spinnerFrecRevisionCuchillaLP.getSelectedItem().toString());
                                        params.put("cuchillaEnContactoConBanda", spinnerCuchillaEnContactoConBandaLP.getSelectedItem().toString());
                                        params.put("materialHumedoTransportadora", spinnerMaterialHumedoLP.getSelectedItem().toString());

                                        if (!txtAnchoEstructura.getText().toString().equals("")) {
                                            params.put("anchoEstructura", String.valueOf(Float.parseFloat(txtAnchoEstructura.getText().toString())));
                                        }
                                        if (!txtAnchoTrayectoCarga.getText().toString().equals("")) {
                                            params.put("anchoTrayectoCarga", String.valueOf(Float.parseFloat(txtAnchoTrayectoCarga.getText().toString())));

                                        }
                                        if (!txtAnchoCuchillaLP.getText().toString().equals("")) {
                                            params.put("anchoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLP.getText().toString())));

                                        }
                                        if (!txtAltoCuchillaLP.getText().toString().equals("")) {
                                            params.put("altoCuchillaLimpiadorPrimario", String.valueOf(Float.parseFloat(txtAltoCuchillaLP.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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
        final TextInputEditText txtReferenciaLS = dialogParte.findViewById(R.id.txtReferenciaLS);
        final Spinner spinnerEstadoCuchillaLS = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLS);
        final Spinner spinnerEstadoTensorLS = dialogParte.findViewById(R.id.spinnerEstadoTensorLS);
        final Spinner spinnerEstadoTuboLS = dialogParte.findViewById(R.id.spinnerEstadoTuboLS);
        final Spinner spinnerFrecRevisionCuchillaLS = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLS);
        final Spinner spinnerCuchillaEnContactoConBandaLS = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLS);
        final Spinner spinnerSistemaDribbleChuteLS = dialogParte.findViewById(R.id.spinnerSistemaDribbleChuteLS);

        final Spinner spinnerMarcaLT = dialogParte.findViewById(R.id.spinnerMarcaLT);
        final TextInputEditText txtReferenciaLT = dialogParte.findViewById(R.id.txtReferenciaLT);
        final Spinner spinnerEstadoCuchillaLT = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLT);
        final Spinner spinnerEstadoTensorLT = dialogParte.findViewById(R.id.spinnerEstadoTensorLT);
        final Spinner spinnerEstadoTuboLT = dialogParte.findViewById(R.id.spinnerEstadoTuboLT);
        final Spinner spinnerFrecRevisionCuchillaLT = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLT);
        final Spinner spinnerCuchillaEnContactoConBandaLT = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLT);


        ArrayAdapter<String> adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        ArrayAdapter<String> adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
        ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);


        spinnerMarcaLS.setAdapter(adapterMarcaLimpiador);
        spinnerMarcaLT.setAdapter(adapterMarcaLimpiador);

        spinnerEstadoTensorLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoTensorLT.setAdapter(adapterEstadoPartes);

        spinnerEstadoTuboLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoTuboLT.setAdapter(adapterEstadoPartes);

        spinnerCuchillaEnContactoConBandaLS.setAdapter(adapterSiNo);
        spinnerCuchillaEnContactoConBandaLT.setAdapter(adapterSiNo);
        spinnerSistemaDribbleChuteLS.setAdapter(adapterSiNo);


        spinnerEstadoCuchillaLS.setAdapter(adapterEstadoPartes);
        spinnerEstadoCuchillaLT.setAdapter(adapterEstadoPartes);
        final TextInputEditText txtAnchoCuchillaLS = dialogParte.findViewById(R.id.txtAnchoCuchillaLS);
        final TextInputEditText txtAltoCuchillaLS = dialogParte.findViewById(R.id.txtAltoCuchillaLS);

        final TextInputEditText txtAnchoCuchillaLT = dialogParte.findViewById(R.id.txtAnchoCuchillaLT);
        final TextInputEditText txtAltoCuchillaLT = dialogParte.findViewById(R.id.txtAltoCuchillaLT);


        spinnerFrecRevisionCuchillaLS.setAdapter(adapterFrecRevisionCuchilla);
        spinnerFrecRevisionCuchillaLT.setAdapter(adapterFrecRevisionCuchilla);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                            params.put("referenciaLimpiadorSecundario", txtReferenciaLS.getText().toString());
                            params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                            params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                            params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                            params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                            params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                            params.put("referenciaLimpiadorTerciario", txtReferenciaLT.getText().toString());
                            params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                            params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                            params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtAnchoCuchillaLS.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                            }
                            if (!txtAltoCuchillaLS.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                            }
                            if (!txtAnchoCuchillaLT.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                            }
                            if (!txtAltoCuchillaLT.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                        params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                                                        params.put("referenciaLimpiadorSecundario", txtReferenciaLS.getText().toString());
                                                        params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                                                        params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                                                        params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                                                        params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                                                        params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                                                        params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                                                        params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                                                        params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                                                        params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                                                        params.put("referenciaLimpiadorTerciario", txtReferenciaLT.getText().toString());
                                                        params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                                                        params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                                                        params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                                                        params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                                                        params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                                                        params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                                                        params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());

                                                        if (!txtAnchoCuchillaLS.getText().toString().equals("")) {
                                                            params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                                                        }
                                                        if (!txtAltoCuchillaLS.getText().toString().equals("")) {
                                                            params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                                                        }
                                                        if (!txtAnchoCuchillaLT.getText().toString().equals("")) {
                                                            params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                                                        }
                                                        if (!txtAltoCuchillaLT.getText().toString().equals("")) {
                                                            params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();
                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                            params.put("referenciaLimpiadorSecundario", txtReferenciaLS.getText().toString());
                            params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                            params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                            params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                            params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                            params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                            params.put("referenciaLimpiadorTerciario", txtReferenciaLT.getText().toString());
                            params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                            params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                            params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                            params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                            params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                            params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                            params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtAnchoCuchillaLS.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                            }
                            if (!txtAltoCuchillaLS.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                            }
                            if (!txtAnchoCuchillaLT.getText().toString().equals("")) {
                                params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                            }
                            if (!txtAltoCuchillaLT.getText().toString().equals("")) {
                                params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarLimpiadorSecundaro";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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

                                        params.put("marcaLimpiadorSecundario", spinnerMarcaLS.getSelectedItem().toString());
                                        params.put("referenciaLimpiadorSecundario", txtReferenciaLS.getText().toString());
                                        params.put("anchoCuchillaLimpiadorSecundario", txtAnchoCuchillaLS.getText().toString());
                                        params.put("altoCuchillaLimpiadorSecundario", txtAltoCuchillaLS.getText().toString());
                                        params.put("estadoCuchillaLimpiadorSecundario", spinnerEstadoCuchillaLS.getSelectedItem().toString());
                                        params.put("estadoTensorLimpiadorSecundario", spinnerEstadoTensorLS.getSelectedItem().toString());
                                        params.put("estadoTuboLimpiadorSecundario", spinnerEstadoTuboLS.getSelectedItem().toString());
                                        params.put("frecuenciaRevisionCuchilla1", spinnerFrecRevisionCuchillaLS.getSelectedItem().toString());
                                        params.put("cuchillaEnContactoConBanda1", spinnerCuchillaEnContactoConBandaLS.getSelectedItem().toString());
                                        params.put("sistemaDribbleChute", spinnerSistemaDribbleChuteLS.getSelectedItem().toString());

                                        params.put("marcaLimpiadorTerciario", spinnerMarcaLT.getSelectedItem().toString());
                                        params.put("referenciaLimpiadorTerciario", txtReferenciaLT.getText().toString());
                                        params.put("anchoCuchillaLimpiadorTerciario", txtAnchoCuchillaLT.getText().toString());
                                        params.put("altoCuchillaLimpiadorTerciario", txtAltoCuchillaLT.getText().toString());
                                        params.put("estadoCuchillaLimpiadorTerciario", spinnerEstadoCuchillaLT.getSelectedItem().toString());
                                        params.put("estadoTensorLimpiadorTerciario", spinnerEstadoTensorLT.getSelectedItem().toString());
                                        params.put("estadoTuboLimpiadorTerciario", spinnerEstadoTuboLT.getSelectedItem().toString());
                                        params.put("frecuenciaRevisionCuchilla2", spinnerFrecRevisionCuchillaLT.getSelectedItem().toString());
                                        params.put("cuchillaEnContactoConBanda2", spinnerCuchillaEnContactoConBandaLT.getSelectedItem().toString());

                                        if (!txtAnchoCuchillaLS.getText().toString().equals("")) {
                                            params.put("anchoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLS.getText().toString())));
                                        }
                                        if (!txtAltoCuchillaLS.getText().toString().equals("")) {
                                            params.put("altoCuchillaLimpiadorSecundario", String.valueOf(Float.parseFloat(txtAltoCuchillaLS.getText().toString())));

                                        }
                                        if (!txtAnchoCuchillaLT.getText().toString().equals("")) {
                                            params.put("anchoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAnchoCuchillaLT.getText().toString())));

                                        }
                                        if (!txtAltoCuchillaLT.getText().toString().equals("")) {
                                            params.put("altoCuchillaLimpiadorTerciario", String.valueOf(Float.parseFloat(txtAltoCuchillaLT.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {



                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }
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

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();
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

                            if (!txtDiametroPAmarrePM.getText().toString().equals("")) {
                                params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                            }
                            if (!txtLargoEjePAmarrePM.getText().toString().equals("")) {
                                params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                            }
                            if (!txtDiametroEjePAmarrePM.getText().toString().equals("")) {
                                params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                            }
                            if (!txtAnchoPAmarrePM.getText().toString().equals("")) {
                                params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                            }
                            if (!txtDiametroPAmarrePC.getText().toString().equals("")) {
                                params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                            }
                            if (!txtAnchoPAmarrePC.getText().toString().equals("")) {
                                params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                            }
                            if (!txtLargoEjePAmarrePC.getText().toString().equals("")) {
                                params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                            }
                            if (!txtDiametroEjePAmarrePC.getText().toString().equals("")) {
                                params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
                            }
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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

                                                        if (!txtDiametroPAmarrePM.getText().toString().equals("")) {
                                                            params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                                                        }
                                                        if (!txtLargoEjePAmarrePM.getText().toString().equals("")) {
                                                            params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                                                        }
                                                        if (!txtDiametroEjePAmarrePM.getText().toString().equals("")) {
                                                            params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                                                        }
                                                        if (!txtAnchoPAmarrePM.getText().toString().equals("")) {
                                                            params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                                                        }
                                                        if (!txtDiametroPAmarrePC.getText().toString().equals("")) {
                                                            params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                                                        }
                                                        if (!txtAnchoPAmarrePC.getText().toString().equals("")) {
                                                            params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                                                        }
                                                        if (!txtLargoEjePAmarrePC.getText().toString().equals("")) {
                                                            params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjePAmarrePC.getText().toString().equals("")) {
                                                            params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtDiametroPAmarrePM.getText().toString().equals("")) {
                                params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                            }
                            if (!txtLargoEjePAmarrePM.getText().toString().equals("")) {
                                params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                            }
                            if (!txtDiametroEjePAmarrePM.getText().toString().equals("")) {
                                params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                            }
                            if (!txtAnchoPAmarrePM.getText().toString().equals("")) {
                                params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                            }
                            if (!txtDiametroPAmarrePC.getText().toString().equals("")) {
                                params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                            }
                            if (!txtAnchoPAmarrePC.getText().toString().equals("")) {
                                params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                            }
                            if (!txtLargoEjePAmarrePC.getText().toString().equals("")) {
                                params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                            }
                            if (!txtDiametroEjePAmarrePC.getText().toString().equals("")) {
                                params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
                            }


                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarPoleaAmarre";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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

                                        if (!txtDiametroPAmarrePM.getText().toString().equals("")) {
                                            params.put("diametroPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroPAmarrePM.getText().toString())));
                                        }
                                        if (!txtLargoEjePAmarrePM.getText().toString().equals("")) {
                                            params.put("largoEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePM.getText().toString())));

                                        }
                                        if (!txtDiametroEjePAmarrePM.getText().toString().equals("")) {
                                            params.put("diametroEjePoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePM.getText().toString())));

                                        }
                                        if (!txtAnchoPAmarrePM.getText().toString().equals("")) {
                                            params.put("anchoPoleaAmarrePoleaMotriz", String.valueOf(Float.parseFloat(txtAnchoPAmarrePM.getText().toString())));
                                        }
                                        if (!txtDiametroPAmarrePC.getText().toString().equals("")) {
                                            params.put("dimetroPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroPAmarrePC.getText().toString())));
                                        }
                                        if (!txtAnchoPAmarrePC.getText().toString().equals("")) {
                                            params.put("anchoPoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtAnchoPAmarrePC.getText().toString())));
                                        }
                                        if (!txtLargoEjePAmarrePC.getText().toString().equals("")) {
                                            params.put("largoEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtLargoEjePAmarrePC.getText().toString())));
                                        }
                                        if (!txtDiametroEjePAmarrePC.getText().toString().equals("")) {
                                            params.put("diametroEjePoleaAmarrePoleaCola", String.valueOf(Float.parseFloat(txtDiametroEjePAmarrePC.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();


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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


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

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";


                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

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


                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarPoleaTensora";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {
                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }

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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();


                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarSeguridad";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }


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

        TextView tvRodillo = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral);
        TextView tvRodillo1 = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral1);

        tvRodillo.setOnClickListener(this);
        tvRodillo1.setOnClickListener(this);

        final Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
        final Spinner spinnerAnguloAcanalArtesaCarga = dialogParte.findViewById(R.id.spinnerAnguloAcanalArtesaCarga);

        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);
        ArrayAdapter<String> adapterAnguloAcanalamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.angulosAcanalamiento);

        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
        spinnerAnguloAcanalArtesaCarga.setAdapter(adapterAnguloAcanalamiento);

        final TextInputEditText txtlargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
        final TextInputEditText txtDiametroEjeRodilloCentral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
        final TextInputEditText txtDiametroRodilloCentral = dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
        final TextInputEditText txtLargoTuboRodilloCentral = dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtDistanciaArtesasCarga = dialogParte.findViewById(R.id.txtDistanciaArtesasCarga);
        final TextInputEditText txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);
        final TextInputEditText txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
        final TextInputEditText txtDetalleRodilloCargaLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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
                            params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");


                            if (!txtlargoEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                            }
                            if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                            }
                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                            }
                            if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                            }
                            if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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

                                                        if (!txtlargoEjeRodilloCentral.getText().toString().equals("")) {
                                                            params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                                            params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                                                        }
                                                        if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                                            params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                                                        }
                                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                                                        }
                                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                                                        }
                                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                                        }
                                                        if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                                            params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                                                        }
                                                        if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                                            params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                                        }
                                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                                        }


                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();


                            }

                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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
                            if (cursor.getString(246).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(246).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroTransportadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroTransportadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+ idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtlargoEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                            }
                            if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                            }
                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                            }
                            if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                            }
                            if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRodilloCarga";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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

                                        if (!txtlargoEjeRodilloCentral.getText().toString().equals("")) {
                                            params.put("largoEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtlargoEjeRodilloCentral.getText().toString())));
                                        }
                                        if (!txtDiametroEjeRodilloCentral.getText().toString().equals("")) {
                                            params.put("diametroEjeRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloCentral.getText().toString())));
                                        }
                                        if (!txtLargoTuboRodilloCentral.getText().toString().equals("")) {
                                            params.put("largoTuboRodilloCentralCarga", String.valueOf(Float.parseFloat(txtLargoTuboRodilloCentral.getText().toString())));
                                        }
                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("largoEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                        }
                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroEjeRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));
                                        }
                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroRodilloLateralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));
                                        }
                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                            params.put("anchoExternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                        }
                                        if (!txtDiametroRodilloCentral.getText().toString().equals("")) {
                                            params.put("diametroRodilloCentralCarga", String.valueOf(Float.parseFloat(txtDiametroRodilloCentral.getText().toString())));
                                        }
                                        if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                            params.put("distanciaEntreArtesasCarga", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                        }
                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                            params.put("anchoInternoChasisRodilloCarga", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                        }

                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();
                            }

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

        TextView tvRodillo = dialogParte.findViewById(R.id.tvDetalleRodilloCargaCentral);
        tvRodillo.setOnClickListener(this);

        final Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
        final Spinner spinnerRodillosRetorno = dialogParte.findViewById(R.id.spinnerRodillosRetorno);

        ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);
        ArrayAdapter<String> adapterRodillosRetorno = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

        spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
        spinnerRodillosRetorno.setAdapter(adapterRodillosRetorno);


        final TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
        final TextInputEditText txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
        final TextInputEditText txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
        final TextInputEditText txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
        final TextInputEditText txtDistanciaArtesasCarga = dialogParte.findViewById(R.id.txtDistanciaEntreRodillosRetorno);
        final TextInputEditText txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
        final TextInputEditText txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);
        final TextInputEditText txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);


        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(246).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransportadora where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                FragmentSeleccionarTransportador.bandera="Nuevo";
                            }
                        }

                        if (FragmentSeleccionarTransportador.bandera.equals("Nuevo")) {

                            ContentValues params = new ContentValues();

                            db = dbHelper.getWritableDatabase();
                            cursor = db.rawQuery("SELECT max(idRegistro) from registro", null);
                            cursor.moveToFirst();
                            int maxRegistro;
                            int idRegistroInsert;
                            if (cursor.getString(0) == null) {
                                idRegistroInsert = 1;
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                            }

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD','1')");

                            params.clear();

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

                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                            }

                            if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                            }
                            if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                            }

                            db.insert("bandaTransportadora", null, params);
                            FragmentSeleccionarTransportador.bandera="Actualizar";

                            if (MainActivity.isOnline(getContext())) {
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
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado', idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+ FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
                                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                                                        dialogCarga.cancel();
                                                        dialogParte.cancel();
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

                                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                                            params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                                        }
                                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                                        }

                                                        if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                                            params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                                                        }
                                                        if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                                            params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                                        }
                                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                                            params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                                        }

                                                        return params;
                                                    }
                                                };
                                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestRegistroTornillos);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                dialogCarga.dismiss();
                                            }
                                        });
                                        requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                        params.put("usuarioRegistro", Login.loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                dialogCarga.dismiss();
                                dialogParte.dismiss();

                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
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

                            if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                            }
                            if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                            }
                            if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                            }
                            if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                            }

                            if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                            }
                            if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                            }
                            if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                            }


                            db = dbHelper.getWritableDatabase();

                            db.update("bandaTransportadora", params, "idRegistro=" + idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRodilloRetorno";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+ idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                        dialogCarga.cancel();
                                        dialogParte.dismiss();
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

                                        if (!txtLargoEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("largoEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoEjeRodilloLateral.getText().toString())));
                                        }
                                        if (!txtDiametroEjeRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroEjeRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroEjeRodilloLateral.getText().toString())));

                                        }
                                        if (!txtDiametroRodilloLateral.getText().toString().equals("")) {
                                            params.put("diametroRodilloRetorno", String.valueOf(Float.parseFloat(txtDiametroRodilloLateral.getText().toString())));

                                        }
                                        if (!txtAnchoExternoChasis.getText().toString().equals("")) {
                                            params.put("anchoExternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoExternoChasis.getText().toString())));
                                        }

                                        if (!txtLargoTuboRodilloLateral.getText().toString().equals("")) {
                                            params.put("largoTuboRodilloRetorno", String.valueOf(Float.parseFloat(txtLargoTuboRodilloLateral.getText().toString())));
                                        }
                                        if (!txtDistanciaArtesasCarga.getText().toString().equals("")) {
                                            params.put("distanciaEntreRodillosRetorno", String.valueOf(Float.parseFloat(txtDistanciaArtesasCarga.getText().toString())));
                                        }
                                        if (!txtAnchoInternoChasis.getText().toString().equals("")) {
                                            params.put("anchoInternoChasisRetorno", String.valueOf(Float.parseFloat(txtAnchoInternoChasis.getText().toString())));
                                        }


                                        return params;
                                    }
                                };
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                                dialogCarga.cancel();
                                dialogParte.dismiss();

                            }

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

            case R.id.spinnerAnchoCauchoVPlow:

                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.spinnerAnchoCauchoVPlow);

                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 305) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 50mm - 305mm");
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

                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);


                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 2700) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  106.3\" / 2700mm");
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

            case R.id.txtDiametroEjePoleaColaHorizontal:
                EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtDiametroEjePoleaColaHorizontal);

                if (!txtDiametroRosca.getText().toString().equals("")) {
                    float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                    if (numero < 1 || numero > 150) {
                        txtDiametroRosca.setText("");
                        txtDiametroRosca.setError("Éste valor debe estar entre 0.03\" / 1mm -  5.9\" / 150mm");
                    }
                }
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

            case R.id.tvDetalleRodilloCargaCentral:
            case R.id.tvDetalleRodilloCargaCentral1:
            case R.id.tvDetalleRodilloCargaCentral2:
                abrirDialogParte("Detalle Rodillo");
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

            case "Detalle Rodillo":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.detalle_rodillo);

                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;
        }

    }

    public void llenarRegistros(String parte) {
        switch (parte) {
            case "Banda":

                final AutoCompleteTextView spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontal);
                final Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasHorizontal);
                final Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaHorizontal);
                final Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaHorizontal);
                final Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeHorizontal);
                final Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeHorizontal);
                final Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaHorizontal);
                final Spinner spinnerLocTensor = dialogParte.findViewById(R.id.spinnerLocalizacionTensor);
                final Spinner spinnerBandReversible = dialogParte.findViewById(R.id.spinnerBandaReversible);
                final Spinner spinnerBandaArrastre = dialogParte.findViewById(R.id.spinnerBandaDeArrastre);


                final AutoCompleteTextView spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaHorizontalAnterior);
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

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();


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

                    int p1 = adapterMarcaBanda.getPosition(cursor.getString(cursor.getColumnIndex("marcaBandaTransportadora")));
                    int p2 = adapterMarcaBanda.getPosition(cursor.getString(cursor.getColumnIndex("marcaBandaHorizontalAnterior")));
                    int p4 = adapterNoLonas.getPosition(cursor.getString(cursor.getColumnIndex("noLonasBandaTransportadora")));
                    int p5 = adapterNoLonas.getPosition(cursor.getString(cursor.getColumnIndex("noLonasBandaHorizontalAnterior")));
                    int p6 = adapterTipoLonas.getPosition(cursor.getString(cursor.getColumnIndex("tipoLonaBandaTransportadora")));
                    int p7 = adapterTipoLonas.getPosition(cursor.getString(cursor.getColumnIndex("tipoLonaBandaHorizontalAnterior")));
                    int p8 = adapterTipoCubierta.getPosition(cursor.getString(cursor.getColumnIndex("tipoCubiertaTransportadora")));
                    int p9 = adapterTipoCubierta.getPosition(cursor.getString(cursor.getColumnIndex("tipoCubiertaBandaHorizontalAnterior")));
                    int p10 = adapterTipoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("tipoEmpalmeTransportadora")));
                    int p11 = adapterTipoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("tipoEmpalmeBandaHorizontalAnterior")));
                    int p12 = adapterRoturaLona.getPosition(cursor.getString(cursor.getColumnIndex("resistenciaRoturaLonaTransportadora")));
                    int p13 = adapterRoturaLona.getPosition(cursor.getString(cursor.getColumnIndex("resistenciaRoturaLonaBandaHorizontalAnterior")));
                    int p14 = adapterBandaReversible.getPosition(cursor.getString(cursor.getColumnIndex("bandaReversible")));
                    int p15 = adapterBandaArastre.getPosition(cursor.getString(cursor.getColumnIndex("bandaDeArrastre")));
                    int p16 = adapterEstadoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("estadoEmpalmeTransportadora")));
                    int p17 = adapterLocTensor.getPosition(cursor.getString(cursor.getColumnIndex("localizacionTensorTransportadora")));

                    spinnerMarcaBanda.setText(cursor.getString(cursor.getColumnIndex("marcaBandaTransportadora")));
                    spinnerMarcaBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("marcaBandaHorizontalAnterior")));
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

                    txtAnchoBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("anchoBandaTransportadora")));
                    txtEspesorTotalAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorTotalBandaHorizontalAnterior")));
                    txtEspCubSupAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaSuperiorBandaHorizontalAnterior")));
                    txtEspCubInfAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaInferiorBandaHorizontalAnterior")));
                    txtEspCojinAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCojinBandaHorizontalAnterior")));
                    txtCausaFallo.setText(cursor.getString(cursor.getColumnIndex("causaFallaCambioBandaHorizontal")));

                    txtAnchoBanda.setText(cursor.getString(cursor.getColumnIndex("anchoBandaTransportadora")));
                    txtEspesorTotal.setText(cursor.getString(cursor.getColumnIndex("espesorTotalBandaTransportadora")));
                    txtEspCubSup.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaSuperiorTransportadora")));
                    txtEspCubInf.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaInferiorTransportadora")));
                    txtEspCojin.setText(cursor.getString(cursor.getColumnIndex("espesorCojinTransportadora")));
                    txtVelocidadBanda.setText(cursor.getString(cursor.getColumnIndex("velocidadBandaHorizontal")));

                    txtDistanciEntrePoleas.setText(cursor.getString(cursor.getColumnIndex("distanciaEntrePoleasBandaHorizontal")));
                    txtInclinacion.setText(cursor.getString(cursor.getColumnIndex("inclinacionBandaHorizontal")));
                    txtRecorridoUtilTensor.setText(cursor.getString(cursor.getColumnIndex("recorridoUtilTensorBandaHorizontal")));
                    txtLongitudSinfin.setText(cursor.getString(cursor.getColumnIndex("longitudSinfinBandaHorizontal")));
                    txtTonsTransportadas.setText(cursor.getString(cursor.getColumnIndex("tonsTransportadasBandaHoizontalAnterior")));


                break;

            case "Desviador":

                final Spinner spinnerHayDesviador = dialogParte.findViewById(R.id.spinnerHayDesviador);
                final Spinner spinnerElDesviadorBascula = dialogParte.findViewById(R.id.spinnerElDesviadorBascula);
                final Spinner spinnerHayPresionUniforme = dialogParte.findViewById(R.id.spinnerHayPresionUniforme);
                final Spinner spinnerCauchoVPlow = dialogParte.findViewById(R.id.spinnerCauchoVPlow);

                final Spinner spinnerAnchoVPlow = dialogParte.findViewById(R.id.spinnerAnchoCauchoVPlow);
                final Spinner spinnerEspesorCaucho = dialogParte.findViewById(R.id.spinnerEspesorCauchoVPlow);


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();


                    ArrayAdapter<String> adapterHayDesviador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerHayDesviador.setAdapter(adapterHayDesviador);

                    ArrayAdapter<String> adapterElDesviadoBascula = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerElDesviadorBascula.setAdapter(adapterElDesviadoBascula);

                    ArrayAdapter<String> adapterPresionUniforme = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerHayPresionUniforme.setAdapter(adapterPresionUniforme);

                    ArrayAdapter<String> adapterCauchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    spinnerCauchoVPlow.setAdapter(adapterCauchoVPlow);

                    ArrayAdapter<String> adapterAnchoVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anchoVPlow);
                    spinnerAnchoVPlow.setAdapter(adapterAnchoVPlow);

                    ArrayAdapter<String> adapterEspesorVPlow = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorVPlow);
                    spinnerEspesorCaucho.setAdapter(adapterEspesorVPlow);


                    p1 = adapterHayDesviador.getPosition(cursor.getString(cursor.getColumnIndex("hayDesviador")));
                    p2 = adapterElDesviadoBascula.getPosition(cursor.getString(cursor.getColumnIndex("elDesviadorBascula")));
                    int p3 = adapterPresionUniforme.getPosition(cursor.getString(cursor.getColumnIndex("presionUniformeALoAnchoDeLaBanda")));
                    p4 = adapterCauchoVPlow.getPosition(cursor.getString(cursor.getColumnIndex("cauchoVPlow")));
                    p5 = adapterEspesorVPlow.getPosition(cursor.getString(cursor.getColumnIndex("espesorVPlow")));
                    p6 = adapterAnchoVPlow.getPosition(cursor.getString(cursor.getColumnIndex("anchoVPlow")));

                    spinnerHayDesviador.setSelection(p1);
                    spinnerElDesviadorBascula.setSelection(p2);
                    spinnerHayPresionUniforme.setSelection(p3);
                    spinnerCauchoVPlow.setSelection(p4);
                    spinnerEspesorCaucho.setSelection(p5);
                    spinnerAnchoVPlow.setSelection(p6);


                break;

            case "Polea Tensora":

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

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

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

                    p1 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaTensora")));
                    p2 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("icobandasCentradaEnPoleaTensora")));
                    p3 = adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaTensora")));
                    p4 = adapterTipoTransicion.getPosition(cursor.getString(cursor.getColumnIndex("tipoTransicionPoleaTensora")));
                    p5 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaTensora")));
                    p6 = adapterAnguloAmarre.getPosition(cursor.getString(cursor.getColumnIndex("recorridoPoleaTensora")));

                    txtDiametro.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaTensora")));
                    txtAncho.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaTensora")));
                    txtDiametroEje.setText(cursor.getString(cursor.getColumnIndex("diametroEjePoleaTensora")));
                    txtLargoEje.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaTensora")));
                    txtDistanciaTransicion.setText(cursor.getString(cursor.getColumnIndex("distanciaTransicionPoleaColaTensora")));
                    txtHpMotor.setText(cursor.getString(cursor.getColumnIndex("potenciaMotorPoleaTensora")));

                    spinnerTipoPolea.setSelection(p3);
                    spinnerBandaCentradaEnPolea.setSelection(p2);
                    spinnerEstadoRvto.setSelection(p1);
                    spinnerGuardaPolea.setSelection(p5);
                    spinnerRecorrido.setSelection(p6);
                    spinnerTipoTransicion.setSelection(p4);


                break;

            case "Alineacion":

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

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    
                    adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    ArrayAdapter<String> adapterEstadoParte = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

                    p1 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sistemasAlineacionCargaFuncionando")));
                    p2 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sistemasAlineacionRetornoFuncionando")));
                    p3 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sistemaAlineacionCarga")));
                    p4 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sistemaAlineacionEnRetorno")));
                    p5 = adapterEstadoParte.getPosition(cursor.getString(cursor.getColumnIndex("sistemaAlineacionRetornoPlano")));
                    p6 = adapterEstadoParte.getPosition(cursor.getString(cursor.getColumnIndex("sistemaAlineacionArtesaCarga")));
                    p7 = adapterEstadoParte.getPosition(cursor.getString(cursor.getColumnIndex("sistemaAlineacionRetornoEnV")));

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

                    txtCantidadSACCarga.setText(cursor.getString(cursor.getColumnIndex("cantidadSistemaAlineacionEnCarga")));
                    txtCantidadSACRetorno.setText(cursor.getString(cursor.getColumnIndex("cantidadSistemaAlineacionEnRetorno")));
                    txtDetalleRodilloCargaCentral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloCentralCarga")));
                    txtDetalleRodilloCargaLateral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloLateralCarg")));
                    txtDetalleRodilloRetornoPlano.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloRetorno")));


                break;

            case "Seguridad":

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


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);


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

                    p1 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("puertasInspeccion")));
                    p2 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaRodilloRetornoPlano")));
                    p3 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaRodilloRetornoV")));
                    p4 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaTruTrainer")));
                    p5 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaColaTransportadora")));
                    p6 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaDeflectora")));
                    p7 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaTensora")));
                    p8 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaMotrizTransportadora")));
                    p9 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaZonaDeTransito")));
                    p10 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaMotores")));
                    p11 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaCadenas")));
                    p12 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("guardaCorreas")));
                     p13 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("interruptoresDeSeguridad")));
                     p14 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sirenasDeSeguridad")));

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

                break;


            case "Polea Motriz":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizHorizontal);
                    spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
                    spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                    spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizHorizontal);
                    spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicionPoleaMotrizHorizontal);
                    Spinner spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePMHorizontal);

                    txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
                    txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
                    txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaColaHorizontal);
                    txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
                    txtHpMotor = dialogParte.findViewById(R.id.txtPotenciaMotorHorizontal);
                    txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPMotrizHorizontal);

                    adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                    adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
                    adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);

                     p1 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaMotrizTransportadora")));
                     p2 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaMotrizTransportadora")));
                     p3 = adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaMotrizTransportadora")));
                     p4 = adapterTipoTransicion.getPosition(cursor.getString(cursor.getColumnIndex("tipoTransicionPoleaMotrizTransportadora")));
                     p5 = adapterAnguloAmarre.getPosition(cursor.getString(cursor.getColumnIndex("anguloAmarrePoleaMotrizTransportadora")));
                     p6 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("icobandasCentraEnPoleaMotrizTransportadora")));

                    spinnerTipoPolea.setSelection(p3);
                    spinnerBandaCentradaEnPolea.setSelection(p6);
                    spinnerEstadoRvto.setSelection(p1);
                    spinnerGuardaPolea.setSelection(p2);
                    spinnerTipoTransicion.setSelection(p4);
                    spinnerAnguloAmarre.setSelection(p5);

                    txtDiametro.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaMotrizTransportadora")));
                    txtAncho.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaMotrizTransportadora")));
                    txtDiametroEje.setText(cursor.getString(cursor.getColumnIndex("diametroEjeMotrizTransportadora")));
                    txtLargoEje.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaMotrizTransportadora")));
                    txtHpMotor.setText(cursor.getString(cursor.getColumnIndex("potenciaMotorTransportadora")));
                    txtDistanciaTransicion.setText(cursor.getString(cursor.getColumnIndex("distanciaTransicionPoleaMotrizTransportadora")));


                break;

            case "Polea Cola":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaColaHorizontal);
                    spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizHorizontal);
                    spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                    spinnerTipoTransicion = dialogParte.findViewById(R.id.spinnerTipoTransicion);
                    Spinner spinnerGuardaPoleaCola = dialogParte.findViewById(R.id.spinnerGuardaPoleaColaHorizontal);
                     spinnerAnguloAmarre = dialogParte.findViewById(R.id.spinnerAnguloAmarrePCHorizontal);


                    txtDiametro = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizHorizontal);
                    txtAncho = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizHorizontal);
                    txtDiametroEje = dialogParte.findViewById(R.id.txtDiametroEjePoleaColaHorizontal);
                    txtLargoEje = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizHorizontal);
                    TextInputEditText txtLongTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloHorizontal);
                    TextInputEditText txtLongContrapesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaMotrizHorizontal);
                    txtDistanciaTransicion = dialogParte.findViewById(R.id.txtDistanciaTransicionPCCola);

                     adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                     adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                     adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                     adapterTipoTransicion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransicion);
                     adapterAnguloAmarre = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anguloAmarre);

                     p1 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoRvtoPoleaColaTransportadora")));
                     p2 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("guardaPoleaColaTransportadora")));
                     p3 = adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaColaTransportadora")));
                     p4 = adapterTipoTransicion.getPosition(cursor.getString(cursor.getColumnIndex("tipoTransicionPoleaColaTransportadora")));
                     p5 = adapterAnguloAmarre.getPosition(cursor.getString(cursor.getColumnIndex("anguloAmarrePoleaColaTransportadora")));
                     p6 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("icobandasCentradaPoleaColaTransportadora")));

                    spinnerTipoPolea.setSelection(p3);
                    spinnerBandaCentradaEnPolea.setSelection(p6);
                    spinnerEstadoRvto.setSelection(p1);
                    spinnerGuardaPoleaCola.setSelection(p2);
                    spinnerTipoTransicion.setSelection(p4);
                    spinnerAnguloAmarre.setSelection(p5);

                    txtDiametro.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaColaTransportadora")));
                    txtAncho.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaColaTransportadora")));
                    txtDiametroEje.setText(cursor.getString(cursor.getColumnIndex("diametroEjePoleaColaHorizontal")));
                    txtLargoEje.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaColaTransportadora")));
                    txtLongTornillo.setText(cursor.getString(cursor.getColumnIndex("longitudTensorTornilloPoleaColaTransportadora")));
                    txtDistanciaTransicion.setText(cursor.getString(cursor.getColumnIndex("distanciaTransicionPoleaColaTransportadora")));
                    txtLongContrapesa.setText(cursor.getString(cursor.getColumnIndex("longitudRecorridoContrapesaPoleaColaTransportadora")));


                break;


            case "Soporte Carga":

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
                Spinner spinnerTipoRodilloRC = dialogParte.findViewById(R.id.spinnerTipoRodilloRC);
                Spinner spinnerTipoRodilloRI = dialogParte.findViewById(R.id.spinnerTipoRodilloRI);
                Spinner spinnerBasculaPesaje = dialogParte.findViewById(R.id.spinnerBasculaPesaje);
                Spinner spinnerEspesorUHMV = dialogParte.findViewById(R.id.spinnerEspesorUHMV);
                Spinner spinnerAnchoBarra = dialogParte.findViewById(R.id.spinnerAnchoBarra);


                Spinner spinnerAnguloAcanalmiento1artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaCola);
                Spinner spinnerAnguloAcanalmiento2artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaCola);
                Spinner spinnerAnguloAcanalmiento3artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaCola);
                Spinner spinnerAnguloAcanalmiento1artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaMotriz);
                Spinner spinnerAnguloAcanalmiento2artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaMotriz);
                Spinner spinnerAnguloAcanalmiento3artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaMotriz);

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    spinnerTieneRodillosImpacto = dialogParte.findViewById(R.id.spinnerTieneRodillosImpacto);
                    spinnerCamaImpacto = dialogParte.findViewById(R.id.spinnerCamaImpacto);
                    spinnerCamaSellado = dialogParte.findViewById(R.id.spinnerCamaSellado);
                    spinnerRodillosCarga = dialogParte.findViewById(R.id.spinnerRodillosCargaHorizontal);
                    spinnerRodillosImpacto = dialogParte.findViewById(R.id.spinnerRodillosImpactoHorizontal);
                    spinnerBasculaASGCO = dialogParte.findViewById(R.id.spinnerBaculaASGCO);
                    spinnerBarrasImpacto = dialogParte.findViewById(R.id.spinnerBarraImpacto);
                    spinnerBarrasDeslizamiento = dialogParte.findViewById(R.id.spinnerBarraDeslizamiento);
                    spinnerIntegridadSoportesRodilloImpacto = dialogParte.findViewById(R.id.spinnerIntegridadSoportesRodilloImpacto);
                    spinnerIntegridadSoportesCamaImpactoSellado = dialogParte.findViewById(R.id.spinnerIntegridadSoportesCamaImpacto);
                    spinnerMaterialAtrapadoCortinas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEntreCortinas);
                    spinnerMaterialAtrapadoGuardaBandas = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnGuardaBanda);
                    spinnerMaterialAtrapadoBanda = dialogParte.findViewById(R.id.spinnerMaterialAtrapadoEnBanda);
                    spinnerInclinacionZonaCargue = dialogParte.findViewById(R.id.spinnerInclinacionZonaCargue);
                    spinnerTipoRodilloRC = dialogParte.findViewById(R.id.spinnerTipoRodilloRC);
                    spinnerTipoRodilloRI = dialogParte.findViewById(R.id.spinnerTipoRodilloRI);
                    spinnerBasculaPesaje = dialogParte.findViewById(R.id.spinnerBasculaPesaje);
                    spinnerEspesorUHMV = dialogParte.findViewById(R.id.spinnerEspesorUHMV);
                    spinnerAnchoBarra = dialogParte.findViewById(R.id.spinnerAnchoBarra);


                    spinnerAnguloAcanalmiento1artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaCola);
                    spinnerAnguloAcanalmiento2artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaCola);
                    spinnerAnguloAcanalmiento3artesaPoleaCola = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaCola);
                    spinnerAnguloAcanalmiento1artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal1artesaPoleaMotriz);
                    spinnerAnguloAcanalmiento2artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal2artesaPoleaMotriz);
                    spinnerAnguloAcanalmiento3artesaPoleaMotriz = dialogParte.findViewById(R.id.spinnerAnguloAcanal3artesaPoleaMotriz);


                     adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                     adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    ArrayAdapter<String> adapterAcanalamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.angulosAcanalamiento);
                    ArrayAdapter<String> adapterInclinacionZonaCarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.inclinacionZonaCarga);
                    ArrayAdapter<String> adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);
                    ArrayAdapter<String> adapterEspesorUHMV = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorUHMV);
                    ArrayAdapter<String> adapterAnchoBarra = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.anchoBarra);


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
                    spinnerEspesorUHMV.setAdapter(adapterEspesorUHMV);
                    spinnerAnchoBarra.setAdapter(adapterAnchoBarra);

                    spinnerAnguloAcanalmiento1artesaPoleaCola.setAdapter(adapterAcanalamiento);
                    spinnerAnguloAcanalmiento2artesaPoleaCola.setAdapter(adapterAcanalamiento);
                    spinnerAnguloAcanalmiento3artesaPoleaCola.setAdapter(adapterAcanalamiento);
                    spinnerAnguloAcanalmiento1artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
                    spinnerAnguloAcanalmiento2artesaPoleaMotriz.setAdapter(adapterAcanalamiento);
                    spinnerAnguloAcanalmiento3artesaPoleaMotriz.setAdapter(adapterAcanalamiento);

                    p1 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("rodilloCarga")));
                    p2 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("rodilloImpacto")));
                    p3 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("basculaASGCO")));
                    p4 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("barraImpacto")));
                    p5 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("barraDeslizamiento")));
                    p6 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("integridadSoporteCamaSellado")));
                    p7 = adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("integridadSoportesRodilloImpacto")));

                    p8 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("tieneRodillosImpacto")));
                    p9 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("camaImpacto")));
                    p10 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("camaSellado")));
                    p11 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAtrapadoEnBanda")));
                    p12 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAtrapadoEntreCortinas")));
                    p13 = adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAtrapadoEntreGuardabandas")));


                    p15 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa1")));
                    p16 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa2")));
                    p17 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa3")));
                    int p18 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa1AntesPoleaMotriz")));
                    int p19 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa2AntesPoleaMotriz")));
                    int p20 = adapterAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesa3AntesPoleaMotriz")));

                    int p21 = adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("tipoRodilloCarga")));
                    int p22 = adapterInclinacionZonaCarga.getPosition(cursor.getString(cursor.getColumnIndex("inclinacionZonaCargue")));
                    int p23 = adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("tipoRodilloImpacto")));
                    int p24 = adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("basculaPesaje")));
                    int p25 = adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("anchoBarra")));
                    int p26 = adapterEspesorUHMV.getPosition(cursor.getString(cursor.getColumnIndex("espesorUHMV")));

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
                    spinnerAnchoBarra.setSelection(p25);
                    spinnerEspesorUHMV.setSelection(p26);

                    TextInputEditText txtLargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
                    TextInputEditText txtDiametroEjeRodilloCentral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
                    TextInputEditText txtDiametroRodilloCentral = dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
                    TextInputEditText txtLargoTuboRodilloCentral = dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
                    TextInputEditText txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                    TextInputEditText txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                    TextInputEditText txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                    TextInputEditText txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                    TextInputEditText txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                    TextInputEditText txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);

                    TextInputEditText txtDetalleRodilloCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
                    TextInputEditText txtDetalleRodilloLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);


                    final TextInputEditText txtLargoBarra = dialogParte.findViewById(R.id.txtLargoBarra);

                    txtLargoEjeRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("largoEjeRodilloCentralCarga")));
                    txtDiametroEjeRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("diametroEjeRodilloCentralCarga")));
                    txtDiametroRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("diametroRodilloCentralCarga")));
                    txtLargoTuboRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("largoTuboRodilloCentralCarga")));
                    txtLargoEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoEjeRodilloLateralCarga")));
                    txtDiametroEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroEjeRodilloLateralCarga")));
                    txtDiametroRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroRodilloLateralCarga")));
                    txtLargoTuboRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoTuboRodilloLateralCarga")));
                    txtAnchoInternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoInternoChasisRodilloCarga")));
                    txtAnchoExternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoExternoChasisRodilloCarga")));
                    txtDetalleRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloCentralCarga")));
                    txtDetalleRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloLateralCarg")));

                    txtLargoBarra.setText(cursor.getString(cursor.getColumnIndex("largoBarra")));



                break;

            case "Condicion Carga":

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
                Spinner spinnerEspesorGuardabandas = dialogParte.findViewById(R.id.spinnerEspesorGuardabandas);
                Spinner txtTempMaxMaterialBanda = dialogParte.findViewById(R.id.spinnerTempMaxSobreBanda);
                Spinner txtTempPromedioBanda = dialogParte.findViewById(R.id.spinnerTempPromedioBanda);
                Spinner spinnerLongitudImpacto = dialogParte.findViewById(R.id.spinnerLongitudImpacto);
                TextInputEditText txtAlturaCaida = dialogParte.findViewById(R.id.txtAlturaCaida);
                TextInputEditText txtMaterial = dialogParte.findViewById(R.id.txtMaterial);
                TextInputEditText txtMaxGranulometria = dialogParte.findViewById(R.id.txtMaxGranulometria);

                TextInputEditText txtMaxPeso = dialogParte.findViewById(R.id.txtMaxPeso);
                TextInputEditText txtDensidad = dialogParte.findViewById(R.id.txtDensidadMaterial);
                TextInputEditText txtAnchoChute = dialogParte.findViewById(R.id.txtAnchoChute);
                TextInputEditText txtLargoChute = dialogParte.findViewById(R.id.txtLargoChute);
                TextInputEditText txtAlturaChute = dialogParte.findViewById(R.id.txtAlturaChute);
                TextInputEditText txtAnchoGuardabandas = dialogParte.findViewById(R.id.txtAnchoGuardaBandas);
                TextInputEditText txtLargoGuardabandas = dialogParte.findViewById(R.id.txtLargoGuardaBandas);
                TextInputEditText txtTempAmbienteMinimaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMinimaHorizontal);
                TextInputEditText txtTempAmbienteMaximaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMaximaHorizontal);
                TextInputEditText txtAnguloSobrecarga = dialogParte.findViewById(R.id.txtAnguloSobreCarga);
                TextInputEditText txtCapacidadHorizontal = dialogParte.findViewById(R.id.txtCapacidadHorizontal);
                Constants.llenar();

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();


                    spinnerTipoRevestimientoTolva = dialogParte.findViewById(R.id.spinnerTipoRvtoTolva);
                    spinnerEstadoRvtoTolva = dialogParte.findViewById(R.id.spinnerEstadoRvtoTolva);
                    spinnerDuracionRvto = dialogParte.findViewById(R.id.spinnerDuracionRvto);
                    spinnerDeflectores = dialogParte.findViewById(R.id.spinnerDeflectores);
                    spinnerAtaqueQuimico = dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
                    spinnerAtaqueTemperatura = dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
                    spinnerAtaqueAceites = dialogParte.findViewById(R.id.spinnerAtaqueAceites);
                    spinnerAtaqueAbrasivo = dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
                    spinnerHorasTrabajoDia = dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
                    spinnerDiasTrabajoSemana = dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
                    spinnerAbrasividad = dialogParte.findViewById(R.id.spinnerAbrasividad);
                    spinnerPorcentajeFinos = dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
                    spinnerCajaColaTolva = dialogParte.findViewById(R.id.spinnerCajaColaTolva);
                    spinnerFugaMateriales = dialogParte.findViewById(R.id.spinnerFugaDeMateriales);
                    spinnerFugaMatrialesCola = dialogParte.findViewById(R.id.spinnerFugaMaterialColaChute);
                    spinnerFugaMaterialesCostados = dialogParte.findViewById(R.id.spinnerFugaMaterialCostados);
                    spinnerFugaMaterialesParticulados = dialogParte.findViewById(R.id.spinnerFugaMaterialParticulado);
                    spinnerSistemaSujecion = dialogParte.findViewById(R.id.spinnerAbrazadera);
                    spinnerCauchoGuardaBandas = dialogParte.findViewById(R.id.spinnerCauchoGuardabandas);
                    spinnerTreSealMultiSeal = dialogParte.findViewById(R.id.spinnerTriSealMultiSeal);
                    spinnerProtectorGuardaBandas = dialogParte.findViewById(R.id.spinnerProtectorGuardaBandas);
                    spinnerCortina1 = dialogParte.findViewById(R.id.spinnerCortina1);
                    spinnerCortina2 = dialogParte.findViewById(R.id.spinnerCortina2);
                    spinnerCortina3 = dialogParte.findViewById(R.id.spinnerCortina3);
                    spinnerBoquillasAire = dialogParte.findViewById(R.id.spinnerBoquillasDeAire);
                    spinnerAlimentacionCentrada = dialogParte.findViewById(R.id.spinnerAlimentacionCentrada);
                    spinnerAtaqueImpacto = dialogParte.findViewById(R.id.spinnerAtaqueImpacto);
                    spinnerEspesorGuardabandas = dialogParte.findViewById(R.id.spinnerEspesorGuardabandas);
                    txtTempMaxMaterialBanda = dialogParte.findViewById(R.id.spinnerTempMaxSobreBanda);
                    txtTempPromedioBanda = dialogParte.findViewById(R.id.spinnerTempPromedioBanda);

                    spinnerLongitudImpacto = dialogParte.findViewById(R.id.spinnerLongitudImpacto);


                    ArrayAdapter<String> adapterTipoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRevestimiento);
                    spinnerTipoRevestimientoTolva.setAdapter(adapterTipoRvtoTolva);

                    ArrayAdapter<String> adapterEstadoRvtoTolva = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    spinnerEstadoRvtoTolva.setAdapter(adapterEstadoRvtoTolva);

                    ArrayAdapter<String> adapterDuracionRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.meses);
                    spinnerDuracionRvto.setAdapter(adapterDuracionRvto);

                    ArrayAdapter<String> adapterDeflectores = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerDeflectores.setAdapter(adapterDeflectores);

                    ArrayAdapter<String> adapterEspesorGuardabadas = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.espesorVPlow);
                    spinnerEspesorGuardabandas.setAdapter(adapterEspesorGuardabadas);


                    ArrayAdapter<String> adapterTempBanda = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
                    txtTempMaxMaterialBanda.setAdapter(adapterTempBanda);
                    txtTempPromedioBanda.setAdapter(adapterTempBanda);


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
                    ArrayAdapter<String> adapterLongitudImpacto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.longitudImpacto);
                    spinnerLongitudImpacto.setAdapter(adapterLongitudImpacto);

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

                    spinnerTipoRevestimientoTolva.setSelection(adapterTipoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("tipoRevestimientoTolvaCarga"))));
                    spinnerEstadoRvtoTolva.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoTolvaCarga"))));
                    spinnerDuracionRvto.setSelection(adapterDuracionRvto.getPosition(cursor.getString(cursor.getColumnIndex("duracionPromedioRevestimiento"))));
                    spinnerDeflectores.setSelection(adapterDeflectores.getPosition(cursor.getString(cursor.getColumnIndex("deflectores"))));
                    spinnerAtaqueQuimico.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("ataqueQuimicoTransportadora"))));
                    spinnerAtaqueTemperatura.setSelection(adapterRodamiento.getPosition(cursor.getString(cursor.getColumnIndex("ataqueTemperaturaTransportadora"))));
                    spinnerAtaqueAceites.setSelection(adapterMonitorDesalineacion.getPosition(cursor.getString(cursor.getColumnIndex("ataqueAceiteTransportadora"))));
                    spinnerAtaqueAbrasivo.setSelection(adapterMonitorVelocidad.getPosition(cursor.getString(cursor.getColumnIndex("ataqueAbrasivoTransportadora"))));
                    spinnerHorasTrabajoDia.setSelection(adapterSensorInductivo.getPosition((cursor.getString(cursor.getColumnIndex("horasTrabajoPorDiaTransportadora")))));
                    spinnerDiasTrabajoSemana.setSelection(adapterIndicadorNivel.getPosition(cursor.getString(cursor.getColumnIndex("diasTrabajPorSemanaTransportadora"))));
                    spinnerAbrasividad.setSelection(adapterCajaUnion.getPosition(cursor.getString(cursor.getColumnIndex("abrasividadTransportadora"))));
                    spinnerPorcentajeFinos.setSelection(adapterAlarmaPantalla.getPosition(cursor.getString(cursor.getColumnIndex("porcentajeFinosTransportadora"))));
                    spinnerCajaColaTolva.setSelection(adapterCajaCola.getPosition(cursor.getString(cursor.getColumnIndex("tipoRevestimientoTolvaCarga"))));
                    spinnerFugaMateriales.setSelection(adapterFugaMateriales.getPosition(cursor.getString(cursor.getColumnIndex("fugaMateriales"))));
                    spinnerFugaMatrialesCola.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("fugaDeMaterialesEnLaColaDelChute"))));
                    spinnerFugaMaterialesCostados.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("fugaDeMaterialesPorLosCostados"))));
                    spinnerFugaMaterialesParticulados.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("fugaDeMaterialParticulaALaSalidaDelChute"))));
                    spinnerSistemaSujecion.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("abrazadera"))));
                    spinnerCauchoGuardaBandas.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("cauchoGuardabandas"))));
                    spinnerTreSealMultiSeal.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("triSealMultiSeal"))));
                    spinnerProtectorGuardaBandas.setSelection(adapterProtectorGuardaBandas.getPosition(cursor.getString(cursor.getColumnIndex("protectorGuardaBandas"))));
                    spinnerCortina1.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("cortinaAntiPolvo1"))));
                    spinnerCortina2.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("cortinaAntiPolvo2"))));
                    spinnerCortina3.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("cortinaAntiPolvo3"))));
                    spinnerBoquillasAire.setSelection(adapterEstadoRvtoTolva.getPosition(cursor.getString(cursor.getColumnIndex("boquillasCanonesDeAire"))));
                    spinnerAlimentacionCentrada.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("alimentacionCentradaTransportadora"))));
                    spinnerAtaqueImpacto.setSelection(adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("ataqueImpactoTransportadora"))));
                    spinnerLongitudImpacto.setSelection(adapterLongitudImpacto.getPosition(cursor.getString(cursor.getColumnIndex("longitudImpacto"))));
                    spinnerEspesorGuardabandas.setSelection(adapterEspesorGuardabadas.getPosition(cursor.getString(cursor.getColumnIndex("espesorGuardaBandas"))));

                    txtTempMaxMaterialBanda.setSelection(adapterTempBanda.getPosition(cursor.getString(cursor.getColumnIndex("tempMaximaMaterialSobreBandaTransportadora"))));
                    txtTempPromedioBanda.setSelection(adapterTempBanda.getPosition(cursor.getString(cursor.getColumnIndex("tempPromedioMaterialSobreBandaTransportadora"))));

                    txtAlturaCaida = dialogParte.findViewById(R.id.txtAlturaCaida);
                    txtMaterial = dialogParte.findViewById(R.id.txtMaterial);
                    txtMaxGranulometria = dialogParte.findViewById(R.id.txtMaxGranulometria);

                    txtMaxPeso = dialogParte.findViewById(R.id.txtMaxPeso);
                    txtDensidad = dialogParte.findViewById(R.id.txtDensidadMaterial);
                    txtAnchoChute = dialogParte.findViewById(R.id.txtAnchoChute);
                    txtLargoChute = dialogParte.findViewById(R.id.txtLargoChute);
                    txtAlturaChute = dialogParte.findViewById(R.id.txtAlturaChute);
                    txtAnchoGuardabandas = dialogParte.findViewById(R.id.txtAnchoGuardaBandas);
                    txtLargoGuardabandas = dialogParte.findViewById(R.id.txtLargoGuardaBandas);
                    txtTempAmbienteMinimaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMinimaHorizontal);
                    txtTempAmbienteMaximaHorizontal = dialogParte.findViewById(R.id.txtTempAmbienteMaximaHorizontal);
                    txtAnguloSobrecarga = dialogParte.findViewById(R.id.txtAnguloSobreCarga);
                    txtCapacidadHorizontal = dialogParte.findViewById(R.id.txtCapacidadHorizontal);


                    txtAlturaCaida.setText(cursor.getString(cursor.getColumnIndex("altureCaida")));

                    txtMaterial.setText(cursor.getString(cursor.getColumnIndex("material")));
                    txtMaxGranulometria.setText(cursor.getString(cursor.getColumnIndex("maxGranulometriaTransportadora")));

                    txtMaxPeso.setText(cursor.getString(cursor.getColumnIndex("maxPesoTransportadora")));
                    txtDensidad.setText(cursor.getString(cursor.getColumnIndex("densidadTransportadora")));
                    txtAnchoChute.setText(cursor.getString(cursor.getColumnIndex("anchoChute")));
                    txtLargoChute.setText(cursor.getString(cursor.getColumnIndex("largoChute")));
                    txtAlturaChute.setText(cursor.getString(cursor.getColumnIndex("alturaChute")));

                    txtAnchoGuardabandas.setText(cursor.getString(cursor.getColumnIndex("anchoGuardaBandas")));
                    txtLargoGuardabandas.setText(cursor.getString(cursor.getColumnIndex("largoGuardaBandas")));
                    txtTempAmbienteMinimaHorizontal.setText(cursor.getString(cursor.getColumnIndex("tempAmbienteMinTransportadora")));
                    txtTempAmbienteMaximaHorizontal.setText(cursor.getString(cursor.getColumnIndex("tempAmbienteMaxTransportadora")));
                    txtAnguloSobrecarga.setText(cursor.getString(cursor.getColumnIndex("anguloSobreCarga")));
                    txtCapacidadHorizontal.setText(cursor.getString(cursor.getColumnIndex("capacidadTransportadora")));



                break;

            case "Limpiador Primario":

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

                Spinner spinnerEstadoCuchillaLP = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLP);
                Spinner spinnerEstadoTensorLP = dialogParte.findViewById(R.id.spinnerEstadoTensorLP);
                Spinner spinnerEstadoTuboLP = dialogParte.findViewById(R.id.spinnerEstadoTuboLP);
                Spinner spinnerFrecRevisionCuchillaLP = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLP);
                Spinner spinnerCuchillaEnContactoConBandaLP = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLP);


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    ArrayAdapter<String> adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
                    ArrayAdapter<String> adapterLadosPasarela = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.ladoPasarela);
                    ArrayAdapter<String> adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);


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


                    TextInputEditText txtAnchoEstructura = dialogParte.findViewById(R.id.txtAnchoEstructuraLP);
                    TextInputEditText txtAnchoTrayectoCarga = dialogParte.findViewById(R.id.txtAnchoTrayectoCargaLP);
                    TextInputEditText txtAnchoCuchillaLP = dialogParte.findViewById(R.id.txtAnchoCuchillaLP);
                    TextInputEditText txtAltoCuchillaLP = dialogParte.findViewById(R.id.txtAltoCuchillaLP);
                    TextInputEditText txtReferenciaLP = dialogParte.findViewById(R.id.txtReferenciaLP);


                    spinnerLadoPasarelaSentidoBanda.setSelection(adapterLadosPasarela.getPosition(cursor.getString(cursor.getColumnIndex("pasarelaRespectoAvanceBanda"))));
                    spinnerMaterialAlimenticioLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAlimenticioTransportadora"))));
                    spinnerMaterialAcidoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAcidoTransportadora"))));
                    spinnerMaterial80y150LP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialTempEntre80y150Transportadora"))));
                    spinnerMaterialSecoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialSecoTransportadora"))));
                    spinnerMaterialHumedoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialHumedoTransportadora"))));
                    spinnerMaterialAbrasivoFinoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialAbrasivoFinoTransportadora"))));
                    spinnerMaterialPegajosoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialPegajosoTransportadora"))));
                    spinnerMaterialAceitosoGrasosoLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("materialGrasosoAceitosoTransportadora"))));
                    spinnerMarcaLP.setSelection(adapterMarcaLimpiador.getPosition(cursor.getString(cursor.getColumnIndex("marcaLimpiadorPrimario"))));
                    spinnerEstadoCuchillaLP.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoCuchillaLimpiadorPrimario"))));
                    spinnerEstadoTensorLP.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTensorLimpiadorPrimario"))));
                    spinnerEstadoTuboLP.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTuboLimpiadorPrimario"))));
                    spinnerFrecRevisionCuchillaLP.setSelection(adapterFrecRevisionCuchilla.getPosition(cursor.getString(cursor.getColumnIndex("frecuenciaRevisionCuchilla"))));
                    spinnerCuchillaEnContactoConBandaLP.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("cuchillaEnContactoConBanda"))));

                    txtAnchoEstructura.setText(cursor.getString(cursor.getColumnIndex("anchoEstructura")));
                    txtAnchoTrayectoCarga.setText(cursor.getString(cursor.getColumnIndex("anchoTrayectoCarga")));
                    txtAnchoCuchillaLP.setText(cursor.getString(cursor.getColumnIndex("anchoCuchillaLimpiadorPrimario")));
                    txtAltoCuchillaLP.setText(cursor.getString(cursor.getColumnIndex("altoCuchillaLimpiadorPrimario")));
                    txtReferenciaLP.setText(cursor.getString(cursor.getColumnIndex("referenciaLimpiadorPrimario")));


                break;


            case "Limpiador Secundario":

                Spinner spinnerMarcaLS = dialogParte.findViewById(R.id.spinnerMarcaLS);

                Spinner spinnerEstadoCuchillaLS = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLS);
                Spinner spinnerEstadoTensorLS = dialogParte.findViewById(R.id.spinnerEstadoTensorLS);
                Spinner spinnerEstadoTuboLS = dialogParte.findViewById(R.id.spinnerEstadoTuboLS);
                Spinner spinnerFrecRevisionCuchillaLS = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLS);
                Spinner spinnerCuchillaEnContactoConBandaLS = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLS);
                Spinner spinnerSistemaDribbleChuteLS = dialogParte.findViewById(R.id.spinnerSistemaDribbleChuteLS);

                Spinner spinnerMarcaLT = dialogParte.findViewById(R.id.spinnerMarcaLT);

                Spinner spinnerEstadoCuchillaLT = dialogParte.findViewById(R.id.spinnerEstadoCuchillaLT);
                Spinner spinnerEstadoTensorLT = dialogParte.findViewById(R.id.spinnerEstadoTensorLT);
                Spinner spinnerEstadoTuboLT = dialogParte.findViewById(R.id.spinnerEstadoTuboLT);
                Spinner spinnerFrecRevisionCuchillaLT = dialogParte.findViewById(R.id.spinnerFrecRevisionCuchillaLT);
                Spinner spinnerCuchillaEnContactoConBandaLT = dialogParte.findViewById(R.id.spinnerCuchillaEnContactoConBandaLT);

                TextInputEditText txtAnchoCuchillaLS = dialogParte.findViewById(R.id.txtAnchoCuchillaLS);
                TextInputEditText txtAltoCuchillaLS = dialogParte.findViewById(R.id.txtAltoCuchillaLS);
                TextInputEditText txtReferenciaLS = dialogParte.findViewById(R.id.txtReferenciaLS);

                TextInputEditText txtAnchoCuchillaLT = dialogParte.findViewById(R.id.txtAnchoCuchillaLT);
                TextInputEditText txtAltoCuchillaLT = dialogParte.findViewById(R.id.txtAltoCuchillaLT);
                TextInputEditText txtReferenciaLT = dialogParte.findViewById(R.id.txtReferenciaLT);


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                     adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    adapterFrecRevisionCuchilla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.frecuenciaRevision);
                    adapterMarcaLimpiador = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.marcaLimpiador);


                    spinnerMarcaLS.setAdapter(adapterMarcaLimpiador);
                    spinnerMarcaLT.setAdapter(adapterMarcaLimpiador);


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


                    spinnerMarcaLS.setSelection(adapterMarcaLimpiador.getPosition(cursor.getString(cursor.getColumnIndex("marcaLimpiadorSecundario"))));
                    spinnerMarcaLT.setSelection(adapterMarcaLimpiador.getPosition(cursor.getString(cursor.getColumnIndex("marcaLimpiadorTerciario"))));

                    spinnerSistemaDribbleChuteLS.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("sistemaDribbleChute"))));


                    spinnerEstadoCuchillaLS.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoCuchillaLimpiadorSecundario"))));
                    spinnerEstadoCuchillaLT.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoCuchillaLimpiadorTerciario"))));

                    spinnerEstadoTensorLS.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTensorLimpiadorSecundario"))));
                    spinnerEstadoTensorLT.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTensorLimpiadorTerciario"))));

                    spinnerEstadoTuboLS.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTuboLimpiadorSecundario"))));
                    spinnerEstadoTuboLT.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoTuboLimpiadorSecundario"))));

                    spinnerFrecRevisionCuchillaLS.setSelection(adapterFrecRevisionCuchilla.getPosition(cursor.getString(cursor.getColumnIndex("frecuenciaRevisionCuchilla1"))));
                    spinnerFrecRevisionCuchillaLT.setSelection(adapterFrecRevisionCuchilla.getPosition(cursor.getString(cursor.getColumnIndex("frecuenciaRevisionCuchilla2"))));

                    spinnerCuchillaEnContactoConBandaLS.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("cuchillaEnContactoConBanda1"))));
                    spinnerCuchillaEnContactoConBandaLT.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("cuchillaEnContactoConBanda2"))));

                    txtAnchoCuchillaLS.setText(cursor.getString(cursor.getColumnIndex("anchoCuchillaLimpiadorSecundario")));
                    txtAltoCuchillaLS.setText(cursor.getString(cursor.getColumnIndex("anchoCuchillaLimpiadorTerciario")));
                    txtAltoCuchillaLS.setText(cursor.getString(cursor.getColumnIndex("altoCuchillaLimpiadorSecundario")));
                    txtReferenciaLS.setText(cursor.getString(cursor.getColumnIndex("referenciaLimpiadorSecundario")));

                    txtAnchoCuchillaLT.setText(cursor.getString(cursor.getColumnIndex("anchoCuchillaLimpiadorTerciario")));
                    txtAltoCuchillaLT.setText(cursor.getString(cursor.getColumnIndex("altoCuchillaLimpiadorTerciario")));
                    txtAltoCuchillaLT.setText(cursor.getString(cursor.getColumnIndex("altoCuchillaLimpiadorTerciario")));
                    txtReferenciaLT.setText(cursor.getString(cursor.getColumnIndex("referenciaLimpiadorTerciario")));


                break;

            case "Polea Amarre":

                Spinner spinnerTipoPAmarrePM = dialogParte.findViewById(R.id.spinnerTipoPAmarrePM);
                Spinner spinnerIcobandasCentradaPAmarrePM = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPM);
                Spinner spinnerEstadoRvtoPAmarrePM = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePM);

                Spinner spinnerTipoPAmarrePC = dialogParte.findViewById(R.id.spinnerTipoPAmarrePC);
                Spinner spinnerIcobandasCentradaPAmarrePC = dialogParte.findViewById(R.id.spinnerIcobandasCentradaPAmarraPC);
                Spinner spinnerEstadoRvtoPAmarrePC = dialogParte.findViewById(R.id.spinnerEstadoRvtoPAmarrePC);


                adapterEstadoPartes = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                adapterSiNo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
               adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);

                TextInputEditText txtDiametroPAmarrePM = dialogParte.findViewById(R.id.txtDiametroPAmarrePM);
                TextInputEditText txtAnchoPAmarrePM = dialogParte.findViewById(R.id.txtAnchoPAmarrePM);
                TextInputEditText txtDiametroEjePAmarrePM = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePM);
                TextInputEditText txtLargoEjePAmarrePM = dialogParte.findViewById(R.id.txtLargoEjePAmarrePM);

                TextInputEditText txtDiametroPAmarrePC = dialogParte.findViewById(R.id.txtDiametroPAmarrePC);
                TextInputEditText txtAnchoPAmarrePC = dialogParte.findViewById(R.id.txtAnchoPAmarrePC);
                TextInputEditText txtDiametroEjePAmarrePC = dialogParte.findViewById(R.id.txtDiametrooEjePAmarrePC);
                TextInputEditText txtLargoEjePAmarrePC = dialogParte.findViewById(R.id.txtLargoEjePAmarrePC);


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();


                    spinnerEstadoRvtoPAmarrePM.setAdapter(adapterEstadoPartes);
                    spinnerEstadoRvtoPAmarrePC.setAdapter(adapterEstadoPartes);
                    spinnerIcobandasCentradaPAmarrePC.setAdapter(adapterSiNo);
                    spinnerIcobandasCentradaPAmarrePM.setAdapter(adapterSiNo);
                    spinnerTipoPAmarrePM.setAdapter(adapterTipoPolea);
                    spinnerTipoPAmarrePC.setAdapter(adapterTipoPolea);

                    spinnerTipoPAmarrePM.setSelection(adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaAmarrePoleaMotriz"))));

                    spinnerIcobandasCentradaPAmarrePM.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("icobandasCentradaPoleaAmarrePoleaMotriz"))));
                    spinnerEstadoRvtoPAmarrePM.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaAmarrePoleaMotriz"))));
                    spinnerTipoPAmarrePC.setSelection(adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaAmarrePoleaCola"))));
                    spinnerIcobandasCentradaPAmarrePC.setSelection(adapterSiNo.getPosition(cursor.getString(cursor.getColumnIndex("icobandasCentradaPoleaAmarrePoleaCola"))));
                    spinnerEstadoRvtoPAmarrePC.setSelection(adapterEstadoPartes.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaAmarrePoleaCola"))));


                    txtDiametroPAmarrePM.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaAmarrePoleaMotriz")));
                    txtAnchoPAmarrePM.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaAmarrePoleaMotriz")));
                    txtDiametroEjePAmarrePM.setText(cursor.getString(cursor.getColumnIndex("diametroEjePoleaAmarrePoleaMotriz")));
                    txtLargoEjePAmarrePM.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaAmarrePoleaMotriz")));
                    txtDiametroPAmarrePC.setText(cursor.getString(cursor.getColumnIndex("dimetroPoleaAmarrePoleaCola")));
                    txtAnchoPAmarrePC.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaAmarrePoleaCola")));
                    txtDiametroEjePAmarrePC.setText(cursor.getString(cursor.getColumnIndex("diametroEjePoleaAmarrePoleaCola")));
                    txtLargoEjePAmarrePC.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaAmarrePoleaCola")));



                break;

            case "Rodillo Carga":

                Spinner spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);
                Spinner spinnerAnguloAcanalArtesaCarga = dialogParte.findViewById(R.id.spinnerAnguloAcanalArtesaCarga);

                 adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);
                ArrayAdapter<String> adapterAnguloAcanalamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.angulosAcanalamiento);

                spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
                spinnerAnguloAcanalArtesaCarga.setAdapter(adapterAnguloAcanalamiento);

                TextInputEditText txtlargoEjeRodilloCentral = dialogParte.findViewById(R.id.txtLargoEjeRodilloCentral);
               txtDiametroEjeRodilloCentral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloCentral);
                 txtDiametroRodilloCentral = dialogParte.findViewById(R.id.txtDiametroRodilloCentral);
                 txtLargoTuboRodilloCentral = dialogParte.findViewById(R.id.txtLargoTuboRodilloCentral);
                 txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                 txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                 txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                 txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                TextInputEditText txtDistanciaArtesasCarga = dialogParte.findViewById(R.id.txtDistanciaArtesasCarga);
                 txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                 txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);
                txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);
                txtDetalleRodilloCargaLateral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaLateral);

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    spinnerAnguloAcanalArtesaCarga.setSelection(adapterAnguloAcanalamiento.getPosition(cursor.getString(cursor.getColumnIndex("anguloAcanalamientoArtesaCArga"))));
                    spinnerTipoRodilloCarga.setSelection(adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("tipoRodilloCarga"))));


                    txtlargoEjeRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("largoEjeRodilloCentralCarga")));
                    txtDiametroEjeRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("diametroEjeRodilloCentralCarga")));
                    txtDiametroRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("diametroRodilloCentralCarga")));
                    txtLargoTuboRodilloCentral.setText(cursor.getString(cursor.getColumnIndex("largoTuboRodilloCentralCarga")));
                    txtLargoEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoEjeRodilloLateralCarga")));
                    txtDiametroEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroEjeRodilloLateralCarga")));
                    txtDiametroRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroRodilloLateralCarga")));
                    txtLargoTuboRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoTuboRodilloLateralCarga")));
                    txtAnchoInternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoInternoChasisRodilloCarga")));
                    txtAnchoExternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoExternoChasisRodilloCarga")));
                    txtDetalleRodilloCargaCentral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloCentralCarga")));
                    txtDetalleRodilloCargaLateral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloLateralCarg")));
                    txtDistanciaArtesasCarga.setText(cursor.getString(cursor.getColumnIndex("distanciaEntreArtesasCarga")));


                break;

            case "Rodillo Retorno":

                Spinner spinnerRodillosRetorno = dialogParte.findViewById(R.id.spinnerRodillosRetorno);

                txtLargoEjeRodilloLateral = dialogParte.findViewById(R.id.txtLargoEjeRodilloLateral);
                txtDiametroEjeRodilloLateral = dialogParte.findViewById(R.id.txtDiametroEjeRodilloLateral);
                txtDiametroRodilloLateral = dialogParte.findViewById(R.id.txtDiametroRodilloLateral);
                txtLargoTuboRodilloLateral = dialogParte.findViewById(R.id.txtLargoTuboRodilloLateral);
                txtDistanciaArtesasCarga = dialogParte.findViewById(R.id.txtDistanciaEntreRodillosRetorno);
                txtAnchoInternoChasis = dialogParte.findViewById(R.id.txtAnchoInternoChasis);
                txtAnchoExternoChasis = dialogParte.findViewById(R.id.txtAnchoExternoChasis);

                txtDetalleRodilloCargaCentral = dialogParte.findViewById(R.id.txtDetalleRodilloCargaCentral);

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    spinnerTipoRodilloCarga = dialogParte.findViewById(R.id.spinnerTipoRodilloCarga);

                    adapterTipoRodillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoRodilloCarga);
                    ArrayAdapter<String> adapterRodillosRetorno = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

                    spinnerTipoRodilloCarga.setAdapter(adapterTipoRodillo);
                    spinnerRodillosRetorno.setAdapter(adapterRodillosRetorno);


                    spinnerRodillosRetorno.setSelection(adapterRodillosRetorno.getPosition(cursor.getString(cursor.getColumnIndex("estadoRodilloRetorno"))));
                    spinnerTipoRodilloCarga.setSelection(adapterTipoRodillo.getPosition(cursor.getString(cursor.getColumnIndex("tipoRodilloRetorno"))));

                    txtLargoEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoEjeRodilloRetorno")));
                    txtDiametroEjeRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroEjeRodilloRetorno")));
                    txtLargoTuboRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("largoTuboRodilloRetorno")));
                    txtDiametroRodilloLateral.setText(cursor.getString(cursor.getColumnIndex("diametroRodilloRetorno")));
                    txtDistanciaArtesasCarga.setText(cursor.getString(cursor.getColumnIndex("distanciaEntreRodillosRetorno")));
                    txtAnchoInternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoInternoChasisRetorno")));
                    txtAnchoExternoChasis.setText(cursor.getString(cursor.getColumnIndex("anchoExternoChasisRetorno")));
                    txtDetalleRodilloCargaCentral.setText(cursor.getString(cursor.getColumnIndex("detalleRodilloRetorno")));


                break;

            case "observacion":

                db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                        db.execSQL("UPDATE bandaTransportadora set " + cursor.getColumnName(i) + "=" + "''");
                    }
                }
                cursor = db.rawQuery("Select * from bandaTransportadora where idRegistro=" + idMaximaRegistro.get(0).getMax(), null);
                cursor.moveToFirst();

                EditText txtObservacionRegistro=dialogParte.findViewById(R.id.txtObservacionRegistro);

                txtObservacionRegistro.setText(cursor.getString(cursor.getColumnIndex("observacionRegistroTransportadora")));

                break;


        }
    }

    public void login(final String dialog) {
        String url = Constants.url + "login/" +  Login.loginJsons.get(0).getCodagente() + "&" + Login.loginJsons.get(0).getPwdagente();
        final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                db = dbHelper.getWritableDatabase();

                Type type = new TypeToken<List<LoginJson>>() {
                }.getType();
                loginJsons = gson.fromJson(response, type);

                if(loginJsons.get(0).getEstadoagte().equals("0"))
                {
                    db.execSQL("DELETE FROM usuario");
                    db.execSQL("DELETE FROM clientes");
                    db.execSQL("DELETE FROM plantas");
                    db.execSQL("DELETE FROM registro");
                    db.execSQL("DELETE FROM bandaTransmision");
                    db.execSQL("DELETE FROM bandaTransportadora");
                    db.execSQL("DELETE FROM bandaElevadora");

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
                else
                {
                    db.execSQL("DELETE FROM usuario");
                    db.execSQL("DELETE FROM clientes");
                    db.execSQL("DELETE FROM plantas");
                    db.execSQL("DELETE FROM registro");
                    db.execSQL("DELETE FROM bandaTransmision");
                    db.execSQL("DELETE FROM bandaTransportadora");
                    db.execSQL("DELETE FROM bandaElevadora");


                    db.execSQL("insert into usuario values('" + loginJsons.get(0).getCodagente() + "','" + loginJsons.get(0).getNombreagte() + "','" + loginJsons.get(0).getPwdagente() + "')");

                    for (int i = 0; i < loginJsons.size(); i++) {
                        if (loginJsons.get(i).getNit() != null) {
                            db.execSQL("INSERT INTO clientes values('" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameunido() + "')");
                        }

                        db.execSQL("INSERT INTO plantas values(" + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameplanta() + "',null,null)");

                        if (loginJsons.get(i).getIdRegistro() != null) {
                            db.execSQL("INSERT INTO registro values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getFechaRegistro() + "'," + loginJsons.get(i).getIdTransportador() + "," + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "')");
                        }
                    }

                    dbHelper.close();

                    String url = Constants.url + "transportadores/" + Login.nombreUsuario;
                    StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Type type = new TypeToken<List<LoginTransportadores>>() {
                            }.getType();
                            loginTransportadores = gson.fromJson(response, type);

                            db = dbHelper.getWritableDatabase();
                            db.execSQL("DELETE FROM transportador");

                            for (int i = 0; i < loginTransportadores.size(); i++) {
                                db.execSQL("INSERT INTO transportador values(" + loginTransportadores.get(i).getIdTransportador() + ",'" + loginTransportadores.get(i).getTipoTransportador() + "','" + loginTransportadores.get(i).getNombreTransportador() + "','" + loginTransportadores.get(i).getCodplanta() + "','" + loginTransportadores.get(i).getCaracteristicaTransportador() + "')");

                            }
                            for (int i = 0; i < loginJsons.size(); i++) {
                                for (int x = 0; x < loginTransportadores.size(); x++) {
                                    if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.DSF"))
                                    {
                                        db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "', null)");
                                    }

                                    else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E"))
                                    {
                                        db.execSQL("INSERT INTO bandaElevadora values('"+loginJsons.get(i).getIdRegistro()+"', '"+loginJsons.get(i).getMarcaBandaElevadora()+"', '"+loginJsons.get(i).getAnchoBandaElevadora()+"', '"+loginJsons.get(i).getDistanciaEntrePoleasElevadora()+"', '"+loginJsons.get(i).getNoLonaBandaElevadora()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadora()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadora()+"', '"+loginJsons.get(i).getEspesorCojinActualElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorElevadora()+"', '"+loginJsons.get(i).getTipoCubiertaElevadora()+"', '"+loginJsons.get(i).getTipoEmpalmeElevadora()+"', '"+loginJsons.get(i).getEstadoEmpalmeElevadora()+"', '"+loginJsons.get(i).getResistenciaRoturaLonaElevadora()+"', '"+loginJsons.get(i).getVelocidadBandaElevadora()+"', '"+loginJsons.get(i).getMarcaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getAnchoBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getNoLonasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCojinElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getVelocidadBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getPesoMaterialEnCadaCangilon()+"', '"+loginJsons.get(i).getPesoCangilonVacio()+"', '"+loginJsons.get(i).getLongitudCangilon()+"', '"+loginJsons.get(i).getMaterialCangilon()+"', '"+loginJsons.get(i).getTipoCangilon()+"', '"+loginJsons.get(i).getProyeccionCangilon()+"','"+loginJsons.get(i).getProfundidadCangilon()+"' ,'"+loginJsons.get(i).getMarcaCangilon()+"', '"+loginJsons.get(i).getReferenciaCangilon()+"', '"+loginJsons.get(i).getCapacidadCangilon()+"', '"+loginJsons.get(i).getNoFilasCangilones()+"', '"+loginJsons.get(i).getSeparacionCangilones()+"', '"+loginJsons.get(i).getNoAgujeros()+"', '"+loginJsons.get(i).getDistanciaBordeBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaPosteriorBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura()+"', '"+loginJsons.get(i).getDistanciaBordesCangilonEstructura()+"', '"+loginJsons.get(i).getTipoVentilacion()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getLargoEjeMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getPotenciaMotorMotrizElevadora()+"', '"+loginJsons.get(i).getRpmSalidaReductorMotrizElevadora()+"', '"+loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroPoleaColaElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaColaElevadora()+"', '"+loginJsons.get(i).getTipoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLargoEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getDiametroEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora()+"', '"+loginJsons.get(i).getCargaTrabajoBandaElevadora()+"', '"+loginJsons.get(i).getTemperaturaMaterialElevadora()+"', '"+loginJsons.get(i).getEmpalmeMecanicoElevadora()+"', '"+loginJsons.get(i).getDiametroRoscaElevadora()+"', '"+loginJsons.get(i).getLargoTornilloElevadora()+"', '"+loginJsons.get(i).getMaterialTornilloElevadora()+"', '"+loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getMonitorPeligro()+"', '"+loginJsons.get(i).getRodamiento()+"', '"+loginJsons.get(i).getMonitorDesalineacion()+"', '"+loginJsons.get(i).getMonitorVelocidad()+"', '"+loginJsons.get(i).getSensorInductivo()+"', '"+loginJsons.get(i).getIndicadorNivel()+"', '"+loginJsons.get(i).getCajaUnion()+"', '"+loginJsons.get(i).getAlarmaYPantalla()+"', '"+loginJsons.get(i).getInterruptorSeguridad()+"', '"+loginJsons.get(i).getMaterialElevadora()+"', '"+loginJsons.get(i).getAtaqueQuimicoElevadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaElevadora()+"', '"+loginJsons.get(i).getAtaqueAceitesElevadora()+"', '"+loginJsons.get(i).getAtaqueAbrasivoElevadora()+"', '"+loginJsons.get(i).getCapacidadElevadora()+"', '"+loginJsons.get(i).getHorasTrabajoDiaElevadora()+"', '"+loginJsons.get(i).getDiasTrabajoSemanaElevadora()+"', '"+loginJsons.get(i).getAbrasividadElevadora()+"', '"+loginJsons.get(i).getPorcentajeFinosElevadora()+"', '"+loginJsons.get(i).getMaxGranulometriaElevadora()+"', '"+loginJsons.get(i).getDensidadMaterialElevadora()+"', '"+loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getVariosPuntosDeAlimentacion()+"', '"+loginJsons.get(i).getLluviaDeMaterial()+"', '"+loginJsons.get(i).getAnchoPiernaElevador()+"', '"+loginJsons.get(i).getProfundidadPiernaElevador()+"', '"+loginJsons.get(i).getTempAmbienteMin()+"', '"+loginJsons.get(i).getTempAmbienteMax()+"', '"+loginJsons.get(i).getTipoDescarga()+"', '"+loginJsons.get(i).getTipoCarga()+"')");

                                    }

                                    else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T"))
                                    {
                                        db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','"+loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda()+"' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "',"+
                                                "'"+loginJsons.get(i).getMaterial()+"', '"+loginJsons.get(i).getAnguloSobreCarga()+"', '"+loginJsons.get(i).getAtaqueQuimicoTransportadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaTransportadora()+"', '"+loginJsons.get(i).getAtaqueAceiteTransportadora()+"', '"+loginJsons.get(i).getAtaqueImpactoTransportadora()+"', '"+loginJsons.get(i).getCapacidadTransportadora()+"', '"+loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()+"', '"+loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()+"', '"+loginJsons.get(i).getAlimentacionCentradaTransportadora()+"', '"+loginJsons.get(i).getAbrasividadTransportadora()+"', '"+loginJsons.get(i).getPorcentajeFinosTransportadora()+"', '"+loginJsons.get(i).getMaxGranulometriaTransportadora()+"', '"+loginJsons.get(i).getMaxPesoTransportadora()+"', '"+loginJsons.get(i).getDensidadTransportadora()+"', '"+loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getCajaColaDeTolva()+"', '"+loginJsons.get(i).getFugaMateriales()+"', '"+loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()+"', '"+loginJsons.get(i).getFugaDeMaterialesPorLosCostados()+"', '"+loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()+"', '"+loginJsons.get(i).getAnchoChute()+"', '"+loginJsons.get(i).getLargoChute()+"', '"+loginJsons.get(i).getAlturaChute()+"', '"+loginJsons.get(i).getCauchoGuardabandas()+"', '"+loginJsons.get(i).getTriSealMultiSeal()+"', '"+loginJsons.get(i).getEspesorGuardaBandas()+"', '"+loginJsons.get(i).getAnchoGuardaBandas()+"', '"+loginJsons.get(i).getLargoGuardaBandas()+"', '"+loginJsons.get(i).getProtectorGuardaBandas()+"', '"+loginJsons.get(i).getCortinaAntiPolvo1()+"', '"+loginJsons.get(i).getCortinaAntiPolvo2()+"', '"+loginJsons.get(i).getCortinaAntiPolvo3()+"', '"+loginJsons.get(i).getBoquillasCanonesDeAire()+"', '"+loginJsons.get(i).getTempAmbienteMaxTransportadora()+"', '"+loginJsons.get(i).getTempAmbienteMinTransportadora()+"', '"+loginJsons.get(i).getTieneRodillosImpacto()+"', '"+loginJsons.get(i).getCamaImpacto()+"', '"+loginJsons.get(i).getCamaSellado()+"', '"+loginJsons.get(i).getBasculaPesaje()+"', '"+loginJsons.get(i).getRodilloCarga()+"', '"+loginJsons.get(i).getRodilloImpacto()+"', '"+loginJsons.get(i).getBasculaASGCO()+"', '"+loginJsons.get(i).getBarraImpacto()+"', '"+loginJsons.get(i).getBarraDeslizamiento()+"', '"+loginJsons.get(i).getEspesorUHMV()+"', '"+loginJsons.get(i).getAnchoBarra()+"', '"+loginJsons.get(i).getLargoBarra()+"','"+loginJsons.get(i).getAnguloAcanalamientoArtesa1()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz()+"', '"+loginJsons.get(i).getIntegridadSoportesRodilloImpacto()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreCortinas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEnBanda()+"', '"+loginJsons.get(i).getIntegridadSoportesCamaImpacto()+"', '"+loginJsons.get(i).getInclinacionZonaCargue()+"', '"+loginJsons.get(i).getSistemaAlineacionCarga()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnCarga()+"', '"+loginJsons.get(i).getSistemasAlineacionCargFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getSistemasAlineacionRetornoFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoPlano()+"', '"+loginJsons.get(i).getSistemaAlineacionArtesaCarga()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoEnV()+"', '"+loginJsons.get(i).getLargoEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroRodilloLateralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloLateralCarga()+"', '"+loginJsons.get(i).getTipoRodilloCarga()+"', '"+loginJsons.get(i).getDistanciaEntreArtesasCarga()+"', '"+loginJsons.get(i).getAnchoInternoChasisRodilloCarga()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()+"', '"+loginJsons.get(i).getDetalleRodilloCentralCarga()+"', '"+loginJsons.get(i).getDetalleRodilloLateralCarg()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getLargoEjePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizTransportadora()+"', '"+loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getPotenciaMotorTransportadora()+"', '"+loginJsons.get(i).getGuardaPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoEstructura()+"', '"+loginJsons.get(i).getAnchoTrayectoCarga()+"', '"+loginJsons.get(i).getPasarelaRespectoAvanceBanda()+"', '"+loginJsons.get(i).getMaterialAlimenticioTransportadora()+"', '"+loginJsons.get(i).getMaterialAcidoTransportadora()+"', '"+loginJsons.get(i).getMaterialTempEntre80y150Transportadora()+"', '"+loginJsons.get(i).getMaterialSecoTransportadora()+"', '"+loginJsons.get(i).getMaterialHumedoTransportadora()+"', '"+loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()+"', '"+loginJsons.get(i).getMaterialPegajosoTransportadora()+"', '"+loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()+"', '"+loginJsons.get(i).getMarcaLimpiadorPrimario()+"'           ,'"+loginJsons.get(i).getReferenciaLimpiadorPrimario()+"' ,'"+loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getAltoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTensorLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorPrimario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla()+"','"+loginJsons.get(i).getCuchillaEnContactoConBanda()+"' , '"+loginJsons.get(i).getMarcaLimpiadorSecundario()+"','"+loginJsons.get(i).getReferenciaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorSecundario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorSecundario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla1()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda1()+"', '"+loginJsons.get(i).getSistemaDribbleChute()+"','"+loginJsons.get(i).getMarcaLimpiadorTerciario()+"', '"+loginJsons.get(i).getReferenciaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getFrecuenciaRevisionCuchilla2()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda2()+"', '"+loginJsons.get(i).getEstadoRodilloRetorno()+"', '"+loginJsons.get(i).getLargoEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroRodilloRetorno()+"', '"+loginJsons.get(i).getLargoTuboRodilloRetorno()+"', '"+loginJsons.get(i).getTipoRodilloRetorno()+"', '"+loginJsons.get(i).getDistanciaEntreRodillosRetorno()+"', '"+loginJsons.get(i).getAnchoInternoChasisRetorno()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getDetalleRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDimetroPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroPoleaTensora()+"', '"+loginJsons.get(i).getAnchoPoleaTensora()+"', '"+loginJsons.get(i).getTipoPoleaTensora()+"', '"+loginJsons.get(i).getLargoEjePoleaTensora()+"', '"+loginJsons.get(i).getDiametroEjePoleaTensora()+"', '"+loginJsons.get(i).getIcobandasCentradaEnPoleaTensora()+"', '"+loginJsons.get(i).getRecorridoPoleaTensora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaTensora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaTensora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaColaTensora()+"', '"+loginJsons.get(i).getPotenciaMotorPoleaTensora()+"', '"+loginJsons.get(i).getGuardaPoleaTensora()+"', '"+loginJsons.get(i).getPuertasInspeccion()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoPlano()+"', '"+loginJsons.get(i).getGuardaTruTrainer()+"', '"+loginJsons.get(i).getGuardaPoleaDeflectora()+"', '"+loginJsons.get(i).getGuardaZonaDeTransito()+"', '"+loginJsons.get(i).getGuardaMotores()+"', '"+loginJsons.get(i).getGuardaCadenas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getInterruptoresDeSeguridad()+"', '"+loginJsons.get(i).getSirenasDeSeguridad()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoV()+"', '"+loginJsons.get(i).getDiametroRodilloCentralCarga()+"', '"+loginJsons.get(i).getTipoRodilloImpacto()+"', '"+loginJsons.get(i).getIntegridadSoporteCamaSellado()+"', '"+loginJsons.get(i).getAtaqueAbrasivoTransportadora()+"')");
                                    }
                                }
                            }
                            db.close();

                            String url = Constants.url + "ciudades";
                            StringRequest requestCiudades = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Type type = new TypeToken<List<CiudadesJson>>() {
                                    }.getType();
                                    ciudadesJsons = gson.fromJson(response, type);
                                    db = dbHelper.getWritableDatabase();
                                    db.execSQL("DELETE FROM ciudades");

                                    for (int i = 0; i < ciudadesJsons.size(); i++) {
                                        db.execSQL("INSERT INTO ciudades values('" + ciudadesJsons.get(i).getCodpoblado() + "','" + ciudadesJsons.get(i).getUnido() + "')");
                                    }

                                    db.close();
                                    Login.nombreUsuario=loginJsons.get(0).getCodagente();
                                    dialogCarga.dismiss();
                                    dialogParte.cancel();
                                    MDToast.makeText(getContext(),"REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();




                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                }
                            });
                            queue.add(requestCiudades);
                            requestCiudades.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    });
                    requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(requestTransportadores);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            }
        });

        requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(requestLogin);
    }

}