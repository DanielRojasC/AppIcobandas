package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.icobandas.icobandasapp.Login.ciudadesJsons;
import static com.icobandas.icobandasapp.Login.loginJsons;
import static com.icobandas.icobandasapp.Login.loginTransportadores;


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
    EditText txtObservacionRegistro;
    AlertDialog dialogCarga;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayAdapter<String> adapterTipoTransmision;
    View view;
    Button btnEnviarInformacion;
    Gson gson = new Gson();
    RequestQueue queue;
    AlertDialog.Builder alerta;
    String idRegistroUpdate;


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
        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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
                alerta.setMessage("¿Desea agregar éste registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialogCarga.show();
                        db = dbHelper.getWritableDatabase();
                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaTransmision where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(10).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaTransmision where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                db.execSQL("DELETE FROM registro where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            db.execSQL("INSERT INTO registro values (" + idRegistroInsert + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "','Pendiente INSERTAR BD', '1')");
                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate = FragmentPartesVertical.idMaximaRegistro.get(0).getMax();

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
                            params.put("observacionRegistro", txtObservacionRegistro.getText().toString());
                            params.put("estadoRegistroPesada", "Pendiente INSERTAR BD");


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

                            db.insert("bandaTransmision", null, params);
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
                                                FragmentPartesVertical.idMaximaRegistro.clear();
                                                Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                                }.getType();
                                                FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                                String url = Constants.url + "registroBandaPesada";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        dialogCarga.cancel();
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaTransmision set estadoRegistroPesada='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);

                                                        dialogCarga.dismiss();

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
                                                        params.put("observacionRegistroPesada", txtObservacionRegistro.getText().toString());


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
                                        params.put("fechaRegistro", FragmentPartesVertical.fechaActual);
                                        params.put("idTransportador", FragmentSeleccionarTransportador.idTransportadorRegistro);
                                        params.put("idPlanta", FragmentSeleccionarTransportador.idPlantaRegistro);
                                        params.put("usuarioRegistro", Login.nombreUsuario);

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {
                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                dialogCarga.dismiss();
                            }

                        } else {

                            ContentValues params = new ContentValues();
                            final SQLiteDatabase db = dbHelper.getWritableDatabase();
                            cursor=db.rawQuery("SELECT * FROM bandaTransmision where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

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
                            params.put("observacionRegistro", txtObservacionRegistro.getText().toString());

                            if (cursor.getString(10).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroPesada", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(10).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroPesada", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroPesada", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

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
                            db.update("bandaTransmision", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarBandaPesada";
                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        db.execSQL("update bandaTransmision set estadoRegistroPesada='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                        db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        FragmentSeleccionarTransportador.bandera = "Actualizar";

                                        dialogCarga.dismiss();


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
                                        params.put("observacionRegistroPesada", txtObservacionRegistro.getText().toString());



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
                                requestRegistroTornillos.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestRegistroTornillos);
                            } else {


                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";

                                dialogCarga.dismiss();
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


        return view;
    }


    private void inicializar() {
        tvAnchoBandaTransmision = view.findViewById(R.id.tvAnchoBandaTransmision);
        tvTipoTransmision = view.findViewById(R.id.tvTipoTransmision);
        tvDiametroPoleaConducida = view.findViewById(R.id.tvDiametroPoleaConducida);
        tvAnchoPoleaConducida = view.findViewById(R.id.tvAnchoPoleaConducida);
        tvAnchoPoleaMotriz = view.findViewById(R.id.tvAnchoPoleaMotrizTransmision);
        tvDistanciaCentros = view.findViewById(R.id.tvDistanciaEntreCentroTransmision);
        tvDiametroPoleaMotriz = view.findViewById(R.id.tvDiametroPoleaMotrizPesada);

        spinnerTipoTransmision = view.findViewById(R.id.spinnerTipoTransmision);
        txtDiametroPoleaConducida = view.findViewById(R.id.txtDiametroPoleaConducida);
        txtAnchoPoleaConducida = view.findViewById(R.id.txtAnchoPoleaConducidaPesada);
        txtAnchoBandaTransmision = view.findViewById(R.id.txtAnchoBandaTransmision);
        txtDiametroPoleaMotriz = view.findViewById(R.id.txtDiametroPoleaMotrizPesada);
        txtAnchoPoleaMotriz = view.findViewById(R.id.txtAnchoPoleaMotrizPesada);
        txtDistanciaEntreCentros = view.findViewById(R.id.txtDistanciaCentrosPesada);
        txtPotenciaMotor = view.findViewById(R.id.txtPotenciaMotorPesada);
        txtRpmSalidaReductor = view.findViewById(R.id.txtRpmSalidaReductorPesada);
        txtObservacionRegistro=view.findViewById(R.id.txtObservacionRegistro);
        dialogCarga = new SpotsDialog(getContext(), "Ejecutando Registro");

        btnEnviarInformacion = view.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        queue = Volley.newRequestQueue(getContext());

        dbHelper = new DbHelper(getContext(), "prueba", null, 1);

        alerta = new AlertDialog.Builder(getContext());
        adapterTipoTransmision = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoTransmision);
        spinnerTipoTransmision.setAdapter(adapterTipoTransmision);
    }


    @Override
    public void onClick(View v) {
        Dialog dialogParte;
        ImageView imgParte;
        switch (v.getId()) {
            case R.id.tvAnchoBandaTransmision:
            case R.id.tvDiametroPoleaConducida:
            case R.id.tvAnchoPoleaConducida:
            case R.id.tvAnchoPoleaMotrizTransmision:
            case R.id.tvDistanciaEntreCentroTransmision:
            case R.id.tvDiametroPoleaMotrizPesada:
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.dimensiones_dsf);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;

            case R.id.tvTipoTransmision:
                dialogParte = new Dialog(getContext());
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

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            cursor = db.rawQuery("Select * from bandaTransmision where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                    db.execSQL("UPDATE bandaTransmision set " + cursor.getColumnName(i) + "=" + "''");
                }
            }
            cursor = db.rawQuery("Select * from bandaTransmision where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
            cursor.moveToFirst();

            txtAnchoBandaTransmision.setText(cursor.getString(1));
            txtDistanciaEntreCentros.setText(cursor.getString(2));
            txtPotenciaMotor.setText(cursor.getString(3));
            txtRpmSalidaReductor.setText(cursor.getString(4));
            txtDiametroPoleaConducida.setText(cursor.getString(5));
            txtAnchoPoleaConducida.setText(cursor.getString(6));
            txtDiametroPoleaMotriz.setText(cursor.getString(7));
            txtAnchoPoleaMotriz.setText(cursor.getString(8));
            txtObservacionRegistro.setText(cursor.getString(11));
            spinnerTipoTransmision.setSelection(adapterTipoTransmision.getPosition(cursor.getString(9)));

    }
}

