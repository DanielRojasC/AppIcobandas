package com.icobandas.icobandasapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class FragmentPartesVertical extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

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
    DbHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    public static String tipoCangilon;
    int idRegistroUpdate;

    static Date date = new Date();

    static SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MMM/yyyy");
    public static String fechaActual = dateFormat1.format(date);

    public static ArrayList<IdMaximaRegistro> idMaximaRegistro = new ArrayList<>();


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
            public void onClick(View v) {
                abrirDialogPuertasInpeccion();
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
        dialogCarga = new SpotsDialog(getContext(), "Ejecutando Registro");
        dbHelper = new DbHelper(getContext(), "prueba", null, 1);


    }

    private void dialogBanda() {

        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_banda_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final TextInputEditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
        final TextInputEditText txtEspesorTotal = dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
        final TextInputEditText txtEspCubSup = dialogParte.findViewById(R.id.txtCubSupElevadora);
        final TextInputEditText txtEspCubInf = dialogParte.findViewById(R.id.txtCubInfElevadora);
        final TextInputEditText txtEspCojin = dialogParte.findViewById(R.id.txtEspesorCojin);
        final TextInputEditText txtVelocidadBanda = dialogParte.findViewById(R.id.txtVelocidadBandaElevadora);
        final TextInputEditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
        final TextInputEditText txtEspesorTotalAnterior = dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
        final TextInputEditText txtEspCubSupAnterior = dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
        final TextInputEditText txtEspCubInfAnterior = dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
        final TextInputEditText txtEspCojinAnterior = dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
        final TextInputEditText txtVelocidadBandaAnterior = dialogParte.findViewById(R.id.txtVelocidadBandaElevadoraAnterior);
        final TextInputEditText txtCausaFallaBandaAnterior = dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);
        final TextInputEditText txtDistanciaEntrePoleas = dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
        final TextInputEditText txtTonsTransportadasBandaAnterior = dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);

        final AutoCompleteTextView spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaElevadora);
        final Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasElevadora);
        final Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadora);
        final Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadora);
        final Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadora);
        final Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeElevadora);
        final Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadora);
        final AutoCompleteTextView spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaElevadoraAnterior);
        final Spinner spinnerNoLonasAnterior = dialogParte.findViewById(R.id.spinnerNoLonasElevadoraAnterior);
        final Spinner spinnerTipoLonaAnterior = dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadoraAnterior);
        final Spinner spinnerTipoCubiertaAnterior = dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadoraAnterior);
        final Spinner spinnerTipoEmpalmeAnterior = dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadoraAnterior);
        final Spinner spinnerResistenciaRoturaAnterior = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadoraAnterior);


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

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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


        Button btnEnviarRegistro = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea agregar el registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();
                        db = dbHelper.getWritableDatabase();
                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            if (cursor.getString(0) == null || cursor.getString(0).equals("null")) {
                                idRegistroInsert = 1;
                                IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                                FragmentPartesVertical.idMaximaRegistro.clear();
                                FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                idRegistroUpdate=idRegistroInsert;

                                db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "','Pendiente INSERTAR BD')");
                            } else {
                                maxRegistro = Integer.parseInt(cursor.getString(0));
                                idRegistroInsert = maxRegistro + 1;

                                IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                                idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                                FragmentPartesVertical.idMaximaRegistro.clear();
                                FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                                Log.e("DATO", "INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "')");

                                db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            }


                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("marcaBandaElevadora", spinnerMarcaBanda.getText().toString());
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
                            params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getText().toString());
                            params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                            params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                            params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                            params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                            params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", txtEspCubSupAnterior.getText().toString());
                            params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                            params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                            params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                            params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                            params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                            params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                            params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                            params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");

                            if (!txtAnchoBanda.getText().toString().equals("")) {
                                params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                            }
                            if (!txtDistanciaEntrePoleas.getText().toString().equals("")) {
                                params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                            }
                            if (!txtEspesorTotal.getText().toString().equals("")) {
                                params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                            }
                            if (!txtEspCojin.getText().toString().equals("")) {
                                params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                            }
                            if (!txtEspCubSup.getText().toString().equals("")) {
                                params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                            }
                            if (!txtEspCubInf.getText().toString().equals("")) {
                                params.put("espesorCubiertaInferiorElevadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                            }
                            if (!txtVelocidadBanda.getText().toString().equals("")) {
                                params.put("velocidadBandaElevadora", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                            }
                            if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                            }
                            if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                            }
                            if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                            }
                            if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                params.put("espesorCojinElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                            }
                            if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                params.put("espesorCubiertaInferiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                            }
                            if (!txtTonsTransportadasBandaAnterior.getText().toString().equals("")) {
                                params.put("tonsTransportadasBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                            }

                            if (!txtVelocidadBandaAnterior.getText().toString().equals("")) {
                                params.put("velocidadBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
                            }

                            db.insert("bandaElevadora", null, params);

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
                                                idMaximaRegistro = gson.fromJson(response, type);

                                                String url = Constants.url + "registroBandaElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        params.put("marcaBandaElevadora", spinnerMarcaBanda.getText().toString());
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
                                                        params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getText().toString());
                                                        params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                                                        params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                                        params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                                        params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                                                        params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", txtEspCubSupAnterior.getText().toString());
                                                        params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                                                        params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                                                        params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                                        params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                                        params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                                        params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                                                        params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                                                        params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());

                                                        if (!txtAnchoBanda.getText().toString().equals("")) {
                                                            params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                                        }
                                                        if (!txtDistanciaEntrePoleas.getText().toString().equals("")) {
                                                            params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                                                        }
                                                        if (!txtEspesorTotal.getText().toString().equals("")) {
                                                            params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                                        }
                                                        if (!txtEspCojin.getText().toString().equals("")) {
                                                            params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                                        }
                                                        if (!txtEspCubSup.getText().toString().equals("")) {
                                                            params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                                        }
                                                        if (!txtEspCubInf.getText().toString().equals("")) {
                                                            params.put("espesorCubiertaInferiorElevadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                                        }
                                                        if (!txtVelocidadBanda.getText().toString().equals("")) {
                                                            params.put("velocidadBandaElevadora", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                                                        }
                                                        if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                                            params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                                        }
                                                        if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                                            params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                                        }
                                                        if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                                            params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                                        }
                                                        if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                                            params.put("espesorCojinElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                                        }
                                                        if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                                            params.put("espesorCubiertaInferiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                                        }
                                                        if (!txtTonsTransportadasBandaAnterior.getText().toString().equals("")) {
                                                            params.put("tonsTransportadasBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                                                        }

                                                        if (!txtVelocidadBandaAnterior.getText().toString().equals("")) {
                                                            params.put("velocidadBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
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
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                                dialogCarga.cancel();
                                dialogParte.cancel();
                            }

                        } else {

                            ContentValues params = new ContentValues();

                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("marcaBandaElevadora", spinnerMarcaBanda.getText().toString());
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
                            params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getText().toString());
                            params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                            params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                            params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                            params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                            params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", txtEspCubSupAnterior.getText().toString());
                            params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                            params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                            params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                            params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                            params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                            params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                            params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                            params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtAnchoBanda.getText().toString().equals("")) {
                                params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                            }
                            if (!txtDistanciaEntrePoleas.getText().toString().equals("")) {
                                params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                            }
                            if (!txtEspesorTotal.getText().toString().equals("")) {
                                params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                            }
                            if (!txtEspCojin.getText().toString().equals("")) {
                                params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                            }
                            if (!txtEspCubSup.getText().toString().equals("")) {
                                params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                            }
                            if (!txtEspCubInf.getText().toString().equals("")) {
                                params.put("espesorCubiertaInferiorElevadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                            }
                            if (!txtVelocidadBanda.getText().toString().equals("")) {
                                params.put("velocidadBandaElevadora", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                            }
                            if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                            }
                            if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                            }
                            if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                            }
                            if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                params.put("espesorCojinElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                            }
                            if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                params.put("espesorCubiertaInferiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                            }
                            if (!txtTonsTransportadasBandaAnterior.getText().toString().equals("")) {
                                params.put("tonsTransportadasBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                            }
                            if (!txtVelocidadBandaAnterior.getText().toString().equals("")) {
                                params.put("velocidadBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
                            }

                             db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String url = Constants.url + "actualizarBandaElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                                params.put("marcaBandaElevadora", spinnerMarcaBanda.getText().toString());
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
                                                params.put("marcaBandaElevadoraAnterior", spinnerMarcaBandaAnterior.getText().toString());
                                                params.put("anchoBandaElevadoraAnterior", txtAnchoBandaAnterior.getText().toString());
                                                params.put("noLonasBandaElevadoraAnterior", spinnerNoLonasAnterior.getSelectedItem().toString());
                                                params.put("tipoLonaBandaElevadoraAnterior", spinnerTipoLonaAnterior.getSelectedItem().toString());
                                                params.put("espesorTotalBandaElevadoraAnterior", txtEspesorTotalAnterior.getText().toString());
                                                params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", txtEspCubSupAnterior.getText().toString());
                                                params.put("espesorCojinElevadoraAnterior", txtEspCojinAnterior.getText().toString());
                                                params.put("espesorCubiertaInferiorBandaElevadoraAnterior", txtEspCubInfAnterior.getText().toString());
                                                params.put("tipoCubiertaElevadoraAnterior", spinnerTipoCubiertaAnterior.getSelectedItem().toString());
                                                params.put("tipoEmpalmeElevadoraAnterior", spinnerTipoEmpalmeAnterior.getSelectedItem().toString());
                                                params.put("resistenciaRoturaBandaElevadoraAnterior", spinnerResistenciaRoturaAnterior.getSelectedItem().toString());
                                                params.put("tonsTransportadasBandaElevadoraAnterior", txtTonsTransportadasBandaAnterior.getText().toString());
                                                params.put("causaFallaCambioBandaElevadoraAnterior", txtCausaFallaBandaAnterior.getText().toString());
                                                params.put("velocidadBandaElevadoraAnterior", txtVelocidadBandaAnterior.getText().toString());

                                                if (!txtAnchoBanda.getText().toString().equals("")) {
                                                    params.put("anchoBandaElevadora", String.valueOf(Float.parseFloat(txtAnchoBanda.getText().toString())));
                                                }
                                                if (!txtDistanciaEntrePoleas.getText().toString().equals("")) {
                                                    params.put("distanciaEntrePoleasElevadora", String.valueOf(Float.parseFloat(txtDistanciaEntrePoleas.getText().toString())));
                                                }
                                                if (!txtEspesorTotal.getText().toString().equals("")) {
                                                    params.put("espesorTotalBandaElevadora", String.valueOf(Float.parseFloat(txtEspesorTotal.getText().toString())));
                                                }
                                                if (!txtEspCojin.getText().toString().equals("")) {
                                                    params.put("espesorCojinActualElevadora", String.valueOf(Float.parseFloat(txtEspCojin.getText().toString())));
                                                }
                                                if (!txtEspCubSup.getText().toString().equals("")) {
                                                    params.put("espesorCubiertaSuperiorElevadora", String.valueOf(Float.parseFloat(txtEspCubSup.getText().toString())));
                                                }
                                                if (!txtEspCubInf.getText().toString().equals("")) {
                                                    params.put("espesorCubiertaInferiorElevadora", String.valueOf(Float.parseFloat(txtEspCubInf.getText().toString())));
                                                }
                                                if (!txtVelocidadBanda.getText().toString().equals("")) {
                                                    params.put("velocidadBandaElevadora", String.valueOf(Float.parseFloat(txtVelocidadBanda.getText().toString())));
                                                }
                                                if (!txtAnchoBandaAnterior.getText().toString().equals("")) {
                                                    params.put("anchoBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtAnchoBandaAnterior.getText().toString())));
                                                }
                                                if (!txtEspesorTotalAnterior.getText().toString().equals("")) {
                                                    params.put("espesorTotalBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspesorTotalAnterior.getText().toString())));
                                                }
                                                if (!txtEspCubSupAnterior.getText().toString().equals("")) {
                                                    params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubSupAnterior.getText().toString())));
                                                }
                                                if (!txtEspCojinAnterior.getText().toString().equals("")) {
                                                    params.put("espesorCojinElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCojinAnterior.getText().toString())));
                                                }
                                                if (!txtEspCubInfAnterior.getText().toString().equals("")) {
                                                    params.put("espesorCubiertaInferiorBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtEspCubInfAnterior.getText().toString())));
                                                }
                                                if (!txtTonsTransportadasBandaAnterior.getText().toString().equals("")) {
                                                    params.put("tonsTransportadasBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtTonsTransportadasBandaAnterior.getText().toString())));
                                                }
                                                if (!txtVelocidadBandaAnterior.getText().toString().equals("")) {
                                                    params.put("velocidadBandaElevadoraAnterior", String.valueOf(Float.parseFloat(txtVelocidadBandaAnterior.getText().toString())));
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

                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";

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


    }


    private void abrirDialogCangilon() {

        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_cangilon_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvLongitudCangilon = dialogParte.findViewById(R.id.tvLongitudCangilon);
        TextView tvProyeccionCangilon = dialogParte.findViewById(R.id.tvProyeccioCangilon);
        TextView tvProfundidadCangilon = dialogParte.findViewById(R.id.tvProfundidadCangilon);
        TextView tvPesoMaterialEnCadaCangilon=dialogParte.findViewById(R.id.tvPesoMaterialEnCadaCangilon);

        TextView tvDistBordesBanda = dialogParte.findViewById(R.id.tvDistBordesBanda);
        TextView tvDistPosteriorBanda = dialogParte.findViewById(R.id.tvDistPosteriorBanda);
        TextView tvDistBordesCangilon = dialogParte.findViewById(R.id.tvDistBordesCangilon);
        TextView tvDistLabioFrontal = dialogParte.findViewById(R.id.tvDistLabioFrontal);
        TextView tvTipoVentilacion = dialogParte.findViewById(R.id.tvTipoVentilacion);


        tvLongitudCangilon.setOnClickListener(this);
        tvProfundidadCangilon.setOnClickListener(this);
        tvProyeccionCangilon.setOnClickListener(this);
        tvPesoMaterialEnCadaCangilon.setOnClickListener(this);

        tvDistBordesBanda.setOnClickListener(this);
        tvDistBordesCangilon.setOnClickListener(this);
        tvDistPosteriorBanda.setOnClickListener(this);
        tvDistLabioFrontal.setOnClickListener(this);

        tvTipoVentilacion.setOnClickListener(this);

        final Spinner spinnerMaterialCangilon = dialogParte.findViewById(R.id.spinnerMaterialCangilon);
        final Spinner spinnerMarcaCangilon = dialogParte.findViewById(R.id.spinnerMarcaCangilon);
        final TextInputEditText spinnerReferenciaCangilon = dialogParte.findViewById(R.id.txtReferenciaCangilon);
        final Spinner spinnerNoFilasCangilon = dialogParte.findViewById(R.id.spinnerNoFilasCangilones);
        final Spinner spinnerNoAgujeros = dialogParte.findViewById(R.id.spinnerNoAgujeros);
        final Spinner spinnerTipoVentilacion = dialogParte.findViewById(R.id.spinnerTipoVentilacion);
        final Spinner spinnerTipoCangilon = dialogParte.findViewById(R.id.spinnerTipoCangilon);

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

        ArrayAdapter<String> adapterTipoCangilon = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCangilon);
        spinnerTipoCangilon.setAdapter(adapterTipoCangilon);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Cangilon");
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");


                            if (!txtLongitudCangilon.getText().toString().equals("")) {

                                params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                            }
                            if (!txtProyeccionCangilon.getText().toString().equals("")) {
                                params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                            }
                            if (!txtProfundidadCangilon.getText().toString().equals("")) {
                                params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                            }
                            if (!txtPesoMaterialCangilon.getText().toString().equals("")) {
                                params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                            }
                            if (!txtPesoCangilonVacio.getText().toString().equals("")) {
                                params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                            }
                            if (!txtCapacidadCangilon.getText().toString().equals("")) {
                                params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                            }
                            if (!txtSeparacionEntreCangilones.getText().toString().equals("")) {
                                params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                            }
                            if (!txtDistanciaBordesBandaEstructura.getText().toString().equals("")) {
                                params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                            }
                            if (!txtDistanciaPosteriorBandaEstructura.getText().toString().equals("")) {
                                params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                            }
                            if (!txtDistBordesCangilonEstructura.getText().toString().equals("")) {
                                params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                            }
                            if (!txtDistLabioFrontalCangilon.getText().toString().equals("")) {
                                params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
                            }


                            db.insert("bandaElevadora", null, params);

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

                                                String url = Constants.url + "registroCangilon";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        if (!txtLongitudCangilon.getText().toString().equals("")) {
                                                            params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                                                        }
                                                        if (!txtProyeccionCangilon.getText().toString().equals("")) {
                                                            params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                                                        }
                                                        if (!txtProfundidadCangilon.getText().toString().equals("")) {
                                                            params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                                                        }
                                                        if (!txtPesoMaterialCangilon.getText().toString().equals("")) {
                                                            params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                                                        }
                                                        if (!txtPesoCangilonVacio.getText().toString().equals("")) {
                                                            params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                                                        }
                                                        if (!txtCapacidadCangilon.getText().toString().equals("")) {
                                                            params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                                                        }
                                                        if (!txtSeparacionEntreCangilones.getText().toString().equals("")) {
                                                            params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                                                        }
                                                        if (!txtDistanciaBordesBandaEstructura.getText().toString().equals("")) {
                                                            params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                                                        }
                                                        if (!txtDistanciaPosteriorBandaEstructura.getText().toString().equals("")) {
                                                            params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                                                        }
                                                        if (!txtDistBordesCangilonEstructura.getText().toString().equals("")) {
                                                            params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                                                        }
                                                        if (!txtDistLabioFrontalCangilon.getText().toString().equals("")) {
                                                            params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
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
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestCrearRegistro);
                            } else {
                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                                dialogCarga.dismiss();
                                dialogParte.dismiss();
                            }

                        } else {

                            ContentValues params = new ContentValues();
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
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtLongitudCangilon.getText().toString().equals("")) {
                                params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                            }
                            if (!txtProyeccionCangilon.getText().toString().equals("")) {
                                params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                            }
                            if (!txtProfundidadCangilon.getText().toString().equals("")) {
                                params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                            }
                            if (!txtPesoMaterialCangilon.getText().toString().equals("")) {
                                params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                            }
                            if (!txtPesoCangilonVacio.getText().toString().equals("")) {
                                params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                            }
                            if (!txtCapacidadCangilon.getText().toString().equals("")) {
                                params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                            }
                            if (!txtSeparacionEntreCangilones.getText().toString().equals("")) {
                                params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                            }
                            if (!txtDistanciaBordesBandaEstructura.getText().toString().equals("")) {
                                params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                            }
                            if (!txtDistanciaPosteriorBandaEstructura.getText().toString().equals("")) {
                                params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                            }
                            if (!txtDistBordesCangilonEstructura.getText().toString().equals("")) {
                                params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                            }
                            if (!txtDistLabioFrontalCangilon.getText().toString().equals("")) {
                                params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarCangilon";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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

                                                if (!txtLongitudCangilon.getText().toString().equals("")) {
                                                    params.put("longitudCangilon", String.valueOf(Float.parseFloat(txtLongitudCangilon.getText().toString())));
                                                }
                                                if (!txtProyeccionCangilon.getText().toString().equals("")) {
                                                    params.put("proyeccionCangilon", String.valueOf(Float.parseFloat(txtProyeccionCangilon.getText().toString())));
                                                }
                                                if (!txtProfundidadCangilon.getText().toString().equals("")) {
                                                    params.put("profundidadCangilon", String.valueOf(Float.parseFloat(txtProfundidadCangilon.getText().toString())));
                                                }
                                                if (!txtPesoMaterialCangilon.getText().toString().equals("")) {
                                                    params.put("pesoMaterialEnCadaCangilon", String.valueOf(Float.parseFloat(txtPesoMaterialCangilon.getText().toString())));
                                                }
                                                if (!txtPesoCangilonVacio.getText().toString().equals("")) {
                                                    params.put("pesoCangilonVacio", String.valueOf(Float.parseFloat(txtPesoCangilonVacio.getText().toString())));
                                                }
                                                if (!txtCapacidadCangilon.getText().toString().equals("")) {
                                                    params.put("capacidadCangilon", String.valueOf(Float.parseFloat(txtCapacidadCangilon.getText().toString())));
                                                }
                                                if (!txtSeparacionEntreCangilones.getText().toString().equals("")) {
                                                    params.put("separacionCangilones", String.valueOf(Float.parseFloat(txtSeparacionEntreCangilones.getText().toString())));
                                                }
                                                if (!txtDistanciaBordesBandaEstructura.getText().toString().equals("")) {
                                                    params.put("distanciaBordeBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaBordesBandaEstructura.getText().toString())));
                                                }
                                                if (!txtDistanciaPosteriorBandaEstructura.getText().toString().equals("")) {
                                                    params.put("distanciaPosteriorBandaEstructura", String.valueOf(Float.parseFloat(txtDistanciaPosteriorBandaEstructura.getText().toString())));
                                                }
                                                if (!txtDistBordesCangilonEstructura.getText().toString().equals("")) {
                                                    params.put("distanciaBordesCangilonEstructura", String.valueOf(Float.parseFloat(txtDistBordesCangilonEstructura.getText().toString())));
                                                }
                                                if (!txtDistLabioFrontalCangilon.getText().toString().equals("")) {
                                                    params.put("distanciaLaboFrontalCangilonEstructura", String.valueOf(Float.parseFloat(txtDistLabioFrontalCangilon.getText().toString())));
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


    }

    public void abrirDialogPoleaMotriz() {
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_motriz_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvDiametroPolea = dialogParte.findViewById(R.id.tvDiametroPoleaMotrizElevadora);
        TextView tvAnchoPolea = dialogParte.findViewById(R.id.tvAnchoPoleaMotrizElevadora);
        TextView tvLargoEje = dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizElevadora);
        TextView tvDiametroEje = dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizElevadora);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
        final Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
        final Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizElevadora);
        final TextInputEditText txtDiametroPoleaMotriz = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
        final TextInputEditText txtAnchoPoleaMotriz = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
        final TextInputEditText txtLargoEjePoleaMotriz = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
        final TextInputEditText txtDiametroEjePoleaMotriz = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
        final TextInputEditText txtPotenciaMotor = dialogParte.findViewById(R.id.txtPotenciaMotorElevadora);
        final TextInputEditText txtRPMSalidaReductor = dialogParte.findViewById(R.id.txtRpmSalidaReductorPoleaMotriz);


        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        spinnerTipoPolea.setAdapter(adapterTipoPolea);

        ArrayAdapter<String> adapterBandaCentrada = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);

        ArrayAdapter<String> adapterEstadoRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvto.setAdapter(adapterEstadoRvto);

        ArrayAdapter<String> adapterGuardaPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerGuardaPolea.setAdapter(adapterGuardaPolea);


        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Polea Motriz");
        }

        tvAnchoPolea.setOnClickListener(this);
        tvDiametroEje.setOnClickListener(this);
        tvDiametroPolea.setOnClickListener(this);
        tvLargoEje.setOnClickListener(this);

        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");


                            if (!txtLargoEjePoleaMotriz.getText().toString().equals("")) {
                                params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                            }
                            if (!txtDiametroEjePoleaMotriz.getText().toString().equals("")) {
                                params.put("diametroEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                            }
                            if (!txtPotenciaMotor.getText().toString().equals("")) {
                                params.put("potenciaMotorMotrizElevadora", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                            }
                            if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                params.put("diametroPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                            }
                            if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                params.put("anchoPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                            }
                            if (!txtRPMSalidaReductor.getText().toString().equals("")) {
                                params.put("rpmSalidaReductorMotrizElevadora", String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
                            }

                            db.insert("bandaElevadora", null, params);
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

                                                String url = Constants.url + "registroPoleaMotrizElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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

                                                        if (!txtLargoEjePoleaMotriz.getText().toString().equals("")) {
                                                            params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjePoleaMotriz.getText().toString().equals("")) {
                                                            params.put("diametroEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                                                        }
                                                        if (!txtPotenciaMotor.getText().toString().equals("")) {
                                                            params.put("potenciaMotorMotrizElevadora", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                                        }
                                                        if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                                            params.put("diametroPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                                        }
                                                        if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                                            params.put("anchoPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                                        }
                                                        if (!txtRPMSalidaReductor.getText().toString().equals("")) {
                                                            params.put("rpmSalidaReductorMotrizElevadora", String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
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
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtLargoEjePoleaMotriz.getText().toString().equals("")) {
                                params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                            }
                            if (!txtDiametroEjePoleaMotriz.getText().toString().equals("")) {
                                params.put("diametroEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                            }
                            if (!txtPotenciaMotor.getText().toString().equals("")) {
                                params.put("potenciaMotorMotrizElevadora", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                            }
                            if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                params.put("diametroPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                            }
                            if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                params.put("anchoPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                            }
                            if (!txtRPMSalidaReductor.getText().toString().equals("")) {
                                params.put("rpmSalidaReductorMotrizElevadora", String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
                            }
                            
                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                            
                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarPoleaMotrizElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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

                                                if (!txtLargoEjePoleaMotriz.getText().toString().equals("")) {
                                                    params.put("largoEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaMotriz.getText().toString())));
                                                }
                                                if (!txtDiametroEjePoleaMotriz.getText().toString().equals("")) {
                                                    params.put("diametroEjeMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaMotriz.getText().toString())));
                                                }
                                                if (!txtPotenciaMotor.getText().toString().equals("")) {
                                                    params.put("potenciaMotorMotrizElevadora", String.valueOf(Float.parseFloat(txtPotenciaMotor.getText().toString())));
                                                }
                                                if (!txtDiametroPoleaMotriz.getText().toString().equals("")) {
                                                    params.put("diametroPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaMotriz.getText().toString())));
                                                }
                                                if (!txtAnchoPoleaMotriz.getText().toString().equals("")) {
                                                    params.put("anchoPoleaMotrizElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaMotriz.getText().toString())));
                                                }
                                                if (!txtRPMSalidaReductor.getText().toString().equals("")) {
                                                    params.put("rpmSalidaReductorMotrizElevadora", String.valueOf(Float.parseFloat(txtRPMSalidaReductor.getText().toString())));
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
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("fechaRegistro", fechaActual);
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                queue.add(requestRegistro);
                            } else {

                               
                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";

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

    }

    public void abrirDialogPoleaCola() {
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_polea_cola_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvDiametroPolea = dialogParte.findViewById(R.id.tvDiametroPoleaMotrizElevadora);
        TextView tvAnchoPolea = dialogParte.findViewById(R.id.tvAnchoPoleaMotrizElevadora);
        TextView tvLargoEje = dialogParte.findViewById(R.id.tvLargoEjePoleaMotrizElevadora);
        TextView tvDiametroEje = dialogParte.findViewById(R.id.tvDiametroEjePoleaMotrizElevadora);

        final Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
        final Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
        final Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);

        final TextInputEditText txtDiametroPoleaCola = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
        final TextInputEditText txtAnchoPoleaCola = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
        final TextInputEditText txtLargoEjePoleaCola = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
        final TextInputEditText txtDiametroEjePoleaCola = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
        final TextInputEditText txtLongTensorTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloElevadora);
        final TextInputEditText txtLongRecorridoContraPesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaColaElevadora);


        ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
        spinnerTipoPolea.setAdapter(adapterTipoPolea);

        ArrayAdapter<String> adapterBandaCentrada = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);

        ArrayAdapter<String> adapterEstadoRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
        spinnerEstadoRvto.setAdapter(adapterEstadoRvto);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
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


        Button btnEnviarInformacion = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);
        btnEnviarInformacion.setOnClickListener(new View.OnClickListener() {
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();


                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                            params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                            params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                            params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                            params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                            params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                            params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                            params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");


                            if (!txtLargoEjePoleaCola.getText().toString().equals("")) {
                                params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                            }
                            if (!txtDiametroEjePoleaCola.getText().toString().equals("")) {
                                params.put("diametroEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                            }
                            if (!txtLongRecorridoContraPesa.getText().toString().equals("")) {
                                params.put("longitudRecorridoContrapesaPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                            }
                            if (!txtDiametroPoleaCola.getText().toString().equals("")) {
                                params.put("diametroPoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                            }
                            if (!txtAnchoPoleaCola.getText().toString().equals("")) {
                                params.put("anchoPoleaColaElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                            }
                            if (!txtLongTensorTornillo.getText().toString().equals("")) {
                                params.put("longitudTensorTornilloPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
                            }


                            db.insert("bandaElevadora", null, params);

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

                                                String url = Constants.url + "registroPoleaColaElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                        params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                        params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                                        params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                                                        params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                                                        params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                                                        params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                                                        params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                                                        params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());

                                                        if (!txtLargoEjePoleaCola.getText().toString().equals("")) {
                                                            params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                                                        }
                                                        if (!txtDiametroEjePoleaCola.getText().toString().equals("")) {
                                                            params.put("diametroEjeColaElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                                                        }
                                                        if (!txtLongRecorridoContraPesa.getText().toString().equals("")) {
                                                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                                                        }
                                                        if (!txtDiametroPoleaCola.getText().toString().equals("")) {
                                                            params.put("diametroPoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                                                        }
                                                        if (!txtAnchoPoleaCola.getText().toString().equals("")) {
                                                            params.put("anchoPoleaColaElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                                                        }
                                                        if (!txtLongTensorTornillo.getText().toString().equals("")) {
                                                            params.put("longitudTensorTornilloPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
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
                            params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                            params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                            params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                            params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                            params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                            params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                            params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                            params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtLargoEjePoleaCola.getText().toString().equals("")) {
                                params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                            }
                            if (!txtDiametroEjePoleaCola.getText().toString().equals("")) {
                                params.put("diametroEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                            }
                            if (!txtLongRecorridoContraPesa.getText().toString().equals("")) {
                                params.put("longitudRecorridoContrapesaPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                            }
                            if (!txtDiametroPoleaCola.getText().toString().equals("")) {
                                params.put("diametroPoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                            }
                            if (!txtAnchoPoleaCola.getText().toString().equals("")) {
                                params.put("anchoPoleaColaElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                            }
                            if (!txtLongTensorTornillo.getText().toString().equals("")) {
                                params.put("longitudTensorTornilloPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarPoleaColaElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                                params.put("bandaCentradaEnPoleaColaElevadora", spinnerBandaCentradaEnPolea.getSelectedItem().toString());
                                                params.put("estadoRevestimientoPoleaColaElevadora", spinnerEstadoRvto.getSelectedItem().toString());
                                                params.put("tipoPoleaColaElevadora", spinnerTipoPolea.getSelectedItem().toString());

                                                params.put("diametroPoleaColaElevadora", txtDiametroPoleaCola.getText().toString());
                                                params.put("anchoPoleaColaElevadora", txtAnchoPoleaCola.getText().toString());
                                                params.put("largoEjePoleaColaElevadora", txtLargoEjePoleaCola.getText().toString());
                                                params.put("diametroEjePoleaColaElevadora", txtDiametroEjePoleaCola.getText().toString());
                                                params.put("longitudTensorTornilloPoleaColaElevadora", txtLongTensorTornillo.getText().toString());
                                                params.put("longitudRecorridoContrapesaPoleaColaElevadora", txtLongRecorridoContraPesa.getText().toString());

                                                if (!txtLargoEjePoleaCola.getText().toString().equals("")) {
                                                    params.put("largoEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtLargoEjePoleaCola.getText().toString())));
                                                }
                                                if (!txtDiametroEjePoleaCola.getText().toString().equals("")) {
                                                    params.put("diametroEjePoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroEjePoleaCola.getText().toString())));
                                                }
                                                if (!txtLongRecorridoContraPesa.getText().toString().equals("")) {
                                                    params.put("longitudRecorridoContrapesaPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongRecorridoContraPesa.getText().toString())));
                                                }
                                                if (!txtDiametroPoleaCola.getText().toString().equals("")) {
                                                    params.put("diametroPoleaColaElevadora", String.valueOf(Float.parseFloat(txtDiametroPoleaCola.getText().toString())));
                                                }
                                                if (!txtAnchoPoleaCola.getText().toString().equals("")) {
                                                    params.put("anchoPoleaColaElevadora", String.valueOf(Float.parseFloat(txtAnchoPoleaCola.getText().toString())));
                                                }
                                                if (!txtLongTensorTornillo.getText().toString().equals("")) {
                                                    params.put("longitudTensorTornilloPoleaColaElevadora", String.valueOf(Float.parseFloat(txtLongTensorTornillo.getText().toString())));
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
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("fechaRegistro", fechaActual);
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                queue.add(requestRegistro);
                            } else {


                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";

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

    }

    public void abrirDialogEmpalme() {
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_empalme_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final Spinner spinnerEmpalmeMecanico = dialogParte.findViewById(R.id.spinnerEmpalmeMecanicoElevadora);
        final TextInputEditText txtCargaTrabajo = dialogParte.findViewById(R.id.txtCargaTrabajoBandaElevadora);
        final TextInputEditText txtTmperaturaMaterialElevadora = dialogParte.findViewById(R.id.txtTempMaterialElevadora);
        ArrayAdapter<String> adapterEmpalmeMecanico = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerEmpalmeMecanico.setAdapter(adapterEmpalmeMecanico);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Empalme");
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                            if (!txtCargaTrabajo.getText().toString().equals("")) {
                                params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                            }
                            params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                            if (!txtTmperaturaMaterialElevadora.getText().toString().equals("")) {
                                params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                            }
                            params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");



                            db.insert("bandaElevadora", null, params);

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

                                                String url = Constants.url + "registroEmpalmeElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                                                        if (!txtCargaTrabajo.getText().toString().equals("")) {
                                                            params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                                                        }
                                                        params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                                                        if (!txtTmperaturaMaterialElevadora.getText().toString().equals("")) {
                                                            params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                                                        }
                                                        params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());
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
                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                            if (!txtCargaTrabajo.getText().toString().equals("")) {
                                params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                            }
                            params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                            if (!txtTmperaturaMaterialElevadora.getText().toString().equals("")) {
                                params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                            }
                            params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());

                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarEmpalmeElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                                params.put("cargaTrabajoBandaElevadora", txtCargaTrabajo.getText().toString());
                                                if (!txtCargaTrabajo.getText().toString().equals("")) {
                                                    params.put("cargaTrabajoBandaElevadora", String.valueOf(Float.parseFloat(txtCargaTrabajo.getText().toString())));
                                                }
                                                params.put("temperaturaMaterialElevadora", txtTmperaturaMaterialElevadora.getText().toString());

                                                if (!txtTmperaturaMaterialElevadora.getText().toString().equals("")) {
                                                    params.put("temperaturaMaterialElevadora", String.valueOf(Float.parseFloat(txtTmperaturaMaterialElevadora.getText().toString())));
                                                }
                                                params.put("empalmeMecanicoElevadora", spinnerEmpalmeMecanico.getSelectedItem().toString());
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


    }

    public void abrirDialogTornillo() {
        dialogParte = new Dialog(getContext());
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_tornillos_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();
        TextView tvDiametroRosca = dialogParte.findViewById(R.id.tvDiametroTornillo);
        TextView tvLargoTornilo = dialogParte.findViewById(R.id.tvLargoTornillo);
        final Spinner spinnerDiametroRosca = dialogParte.findViewById(R.id.spinnerDiametroRosca);
        final Spinner spinnerLargoTornillo = dialogParte.findViewById(R.id.spinnerLargoTornillo);

        final Spinner spinnerMaterialTornillo = dialogParte.findViewById(R.id.spinnerMaterialTornillo);

        ArrayAdapter<String> adapterMaterialTornillos = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialTornillo);
        spinnerMaterialTornillo.setAdapter(adapterMaterialTornillos);

        ArrayAdapter<String> adapterDiametroRosca = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diametroRosca);
        spinnerDiametroRosca.setAdapter(adapterDiametroRosca);

        ArrayAdapter<String> adapterLargoTornillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.largoTornillo);
        spinnerLargoTornillo.setAdapter(adapterLargoTornillo);


        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Tornillos");
        }

        tvDiametroRosca.setOnClickListener(this);
        tvLargoTornilo.setOnClickListener(this);

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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                            params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());


                            params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");

                            db.insert("bandaElevadora", null, params);
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

                                                String url = Constants.url + "registroTornillosElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                                                        params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());


                                                        params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());
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
                            params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                            params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());

                            params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }
                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarTornillos";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                                params.put("diametroRoscaElevadora", spinnerDiametroRosca.getSelectedItem().toString());

                                                params.put("largoTornilloElevadora", spinnerLargoTornillo.getSelectedItem().toString());

                                                params.put("materialTornilloElevadora", spinnerMaterialTornillo.getSelectedItem().toString());

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


    }

    public void abrirDialogPuertasInpeccion() {
        dialogParte = new Dialog(getContext());
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_puertas_inspeccion_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final TextInputEditText txtAnchoCabezaElevador = dialogParte.findViewById(R.id.txtAnchoCabezaElevador);
        final TextInputEditText txtLargoCabezaElevador = dialogParte.findViewById(R.id.txtLargoCabezaElevador);
        final TextInputEditText txtAnchoBotaElevador = dialogParte.findViewById(R.id.txtAnchoBotaElevador);
        final TextInputEditText txtLargoBotaElevador = dialogParte.findViewById(R.id.txtLargoBotaElevador);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Puertas Inspeccion");
        }

        Button btnEnviar = dialogParte.findViewById(R.id.btnEnviarRegistroBandaElevadora);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("ICOBANDAS S.A dice");
                alerta.setMessage("¿Desea agregar éste registro?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogCarga.show();

                        if(FragmentSeleccionarTransportador.bandera.equals("Actualizar"))
                        {
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                            params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                            params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                            params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");

                            if (!txtAnchoCabezaElevador.getText().toString().equals("")) {
                                params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                            }
                            if (!txtLargoCabezaElevador.getText().toString().equals("")) {
                                params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                            }
                            if (!txtAnchoBotaElevador.getText().toString().equals("")) {
                                params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                            }
                            if (!txtLargoBotaElevador.getText().toString().equals("")) {
                                params.put("largoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoBotaElevador.getText().toString())));
                            }
                            db.insert("bandaElevadora", null, params);
                            FragmentSeleccionarTransportador.bandera = "Actualizar";


                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "crearRegistro";
                                StringRequest requestRegistro = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                        String url = Constants.url + "maxRegistro";
                                        StringRequest requestMaxRegistro = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {

                                                Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                                }.getType();
                                                FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                                String url = Constants.url + "registroPuertaInspeccion";
                                                StringRequest requestPuertaInspeccion = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                                                        params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                                                        params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                                                        params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                                                        params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                                                        if (!txtAnchoCabezaElevador.getText().toString().equals("")) {
                                                            params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                                                        }
                                                        if (!txtLargoCabezaElevador.getText().toString().equals("")) {
                                                            params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                                                        }
                                                        if (!txtAnchoBotaElevador.getText().toString().equals("")) {
                                                            params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                                                        }
                                                        if (!txtLargoBotaElevador.getText().toString().equals("")) {
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
                                queue.add(requestRegistro);
                            } else {


                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

                                dialogCarga.dismiss();
                                dialogParte.dismiss();
                            }
                        } else {

                            ContentValues params = new ContentValues();
                            params.put("idRegistro", idMaximaRegistro.get(0).getMax());
                            params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                            params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                            params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                            params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtAnchoCabezaElevador.getText().toString().equals("")) {
                                params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                            }
                            if (!txtLargoCabezaElevador.getText().toString().equals("")) {
                                params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                            }
                            if (!txtAnchoBotaElevador.getText().toString().equals("")) {
                                params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                            }
                            if (!txtLargoBotaElevador.getText().toString().equals("")) {
                                params.put("largoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoBotaElevador.getText().toString())));
                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarPuertaInspeccion";
                                        StringRequest requestPuertaInspeccion = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                                params.put("anchoCabezaElevadorPuertaInspeccion", txtAnchoCabezaElevador.getText().toString());
                                                params.put("largoCabezaElevadorPuertaInspeccion", txtLargoCabezaElevador.getText().toString());
                                                params.put("anchoBotaElevadorPuertaInspeccion", txtAnchoBotaElevador.getText().toString());
                                                params.put("largoBotaElevadorPuertaInspeccion", txtLargoBotaElevador.getText().toString());
                                                if (!txtAnchoCabezaElevador.getText().toString().equals("")) {
                                                    params.put("anchoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoCabezaElevador.getText().toString())));
                                                }
                                                if (!txtLargoCabezaElevador.getText().toString().equals("")) {
                                                    params.put("largoCabezaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtLargoCabezaElevador.getText().toString())));
                                                }
                                                if (!txtAnchoBotaElevador.getText().toString().equals("")) {
                                                    params.put("anchoBotaElevadorPuertaInspeccion", String.valueOf(Float.parseFloat(txtAnchoBotaElevador.getText().toString())));
                                                }
                                                if (!txtLargoBotaElevador.getText().toString().equals("")) {
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
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("fechaRegistro", fechaActual);
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                queue.add(requestRegistro);
                            } else {

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                FragmentSeleccionarTransportador.bandera = "Actualizar";

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
    }

    public void abrirDialogSeguridad() {
        dialogParte = new Dialog(getContext());
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_seguridad_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        final Spinner spinnerMonitorPeligro = dialogParte.findViewById(R.id.spinnerMonitorPeligro);
        final Spinner spinnerRodamiento = dialogParte.findViewById(R.id.spinnerRodamiento);
        final Spinner spinnerMonitorDesalineacion = dialogParte.findViewById(R.id.spinnerMonitorDesalineacion);
        final Spinner spinnerMonitorVelocidad = dialogParte.findViewById(R.id.spinnerMonitorVelocidad);
        final Spinner spinnerSensorInductivo = dialogParte.findViewById(R.id.spinnerSensorInductivo);
        final Spinner spinnerIndicadorDeNivel = dialogParte.findViewById(R.id.spinnerIndicadorDeNivel);
        final Spinner spinnerCajaDeUnion = dialogParte.findViewById(R.id.spinnerCajaDeUnion);
        final Spinner spinnerAlarmaYPantalla = dialogParte.findViewById(R.id.spinnerAlarmaYPantalla);
        final Spinner spinnerInterruptorSeguridad = dialogParte.findViewById(R.id.spinnerInterruptorDeSeguridad);

        ArrayAdapter<String> adapterMonitorPeligro = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorPeligro);
        spinnerMonitorPeligro.setAdapter(adapterMonitorPeligro);

        ArrayAdapter<String> adapterRodamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.rodamiento);
        spinnerRodamiento.setAdapter(adapterRodamiento);

        ArrayAdapter<String> adapterMonitorDesalineacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorDesalineacion);
        spinnerMonitorDesalineacion.setAdapter(adapterMonitorDesalineacion);

        ArrayAdapter<String> adapterMonitorVelocidad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorVelocidad);
        spinnerMonitorVelocidad.setAdapter(adapterMonitorVelocidad);

        ArrayAdapter<String> adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.sensorInductivo);
        spinnerSensorInductivo.setAdapter(adapterSensorInductivo);

        ArrayAdapter<String> adapterIndicadorNivel = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.indicadorDeNivel);
        spinnerIndicadorDeNivel.setAdapter(adapterIndicadorNivel);


        ArrayAdapter<String> adapterCajaUnion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.cajaUnion);
        spinnerCajaDeUnion.setAdapter(adapterCajaUnion);

        ArrayAdapter<String> adapterAlarmaPantalla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.alarmaYPantalla);
        spinnerAlarmaYPantalla.setAdapter(adapterAlarmaPantalla);

        ArrayAdapter<String> adapterInterruptorSeguridad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.interruptorSeguridad);
        spinnerInterruptorSeguridad.setAdapter(adapterInterruptorSeguridad);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Seguridad");
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            params.put("monitorPeligro", spinnerMonitorPeligro.getSelectedItem().toString());
                            params.put("rodamiento", spinnerRodamiento.getSelectedItem().toString());
                            params.put("monitorDesalineacion", spinnerMonitorDesalineacion.getSelectedItem().toString());
                            params.put("monitorVelocidad", spinnerMonitorVelocidad.getSelectedItem().toString());
                            params.put("sensorInductivo", spinnerSensorInductivo.getSelectedItem().toString());
                            params.put("indicadorNivel", spinnerIndicadorDeNivel.getSelectedItem().toString());
                            params.put("cajaUnion", spinnerCajaDeUnion.getSelectedItem().toString());
                            params.put("alarmaYPantalla", spinnerAlarmaYPantalla.getSelectedItem().toString());
                            params.put("interruptorSeguridad", spinnerInterruptorSeguridad.getSelectedItem().toString());
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");

                            db.insert("bandaElevadora", null, params);
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

                                                String url = Constants.url + "registroSeguridadElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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
                            params.put("monitorPeligro", spinnerMonitorPeligro.getSelectedItem().toString());
                            params.put("rodamiento", spinnerRodamiento.getSelectedItem().toString());
                            params.put("monitorDesalineacion", spinnerMonitorDesalineacion.getSelectedItem().toString());
                            params.put("monitorVelocidad", spinnerMonitorVelocidad.getSelectedItem().toString());
                            params.put("sensorInductivo", spinnerSensorInductivo.getSelectedItem().toString());
                            params.put("indicadorNivel", spinnerIndicadorDeNivel.getSelectedItem().toString());
                            params.put("cajaUnion", spinnerCajaDeUnion.getSelectedItem().toString());
                            params.put("alarmaYPantalla", spinnerAlarmaYPantalla.getSelectedItem().toString());
                            params.put("interruptorSeguridad", spinnerInterruptorSeguridad.getSelectedItem().toString());
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String url = Constants.url + "actualizarSeguridadElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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
                                        params.put("fechaRegistro", fechaActual);
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                queue.add(requestRegistro);
                            } else {

                                //db.execSQL("UPDATE bandaTransmision set anchoBandaTransmision='"+String.valueOf(Float.parseFloat(txtAnchoBandaTransmision.getText().toString()))+"', distanciaEntreCentrosTransmision='"+txtDistanciaEntreCentros.getText().toString()+"', potenciaMotorTransmision='"+txtPotenciaMotor.getText().toString()+"', rpmSalidaReductorTransmision='"+txtRpmSalidaReductor.getText().toString()+"', diametroPoleaConducidaTransmision='"+txtDiametroPoleaConducida.getText().toString()+"', anchoPoleaConducidaTransmision='"+txtAnchoPoleaConducida.getText().toString()+"', diametroPoleaMotrizTransmision='"+txtDiametroPoleaMotriz.getText().toString()+"', anchoPoleaMotrizTransmision='"+txtAnchoPoleaMotriz.getText().toString()+"', tipoParteTransmision='"+spinnerTipoTransmision.getSelectedItem().toString()+"' WHERE idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();

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


    }

    public void abrirDialogCondicionesCarga() {
        dialogParte = new Dialog(getContext());
        dialogParte = new Dialog(getContext());
        dialogParte.setContentView(R.layout.dialog_condiciones_carga_elevadora);
        dialogParte.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParte.setCancelable(true);
        dialogParte.show();

        TextView tvTipoCarga = dialogParte.findViewById(R.id.tvTipoCarga);
        TextView tvTipoDescarga = dialogParte.findViewById(R.id.tvTipoDescarga);

        tvTipoCarga.setOnClickListener(this);
        tvTipoDescarga.setOnClickListener(this);
        Constants.llenar();
        Constants.llenarTempMin();
        Constants.llenarTempMax();

        final Spinner spinnerAtaqueQuimico = dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
        final Spinner spinnerAtaqueTemperatura = dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
        final Spinner spinnerAtaqueAceites = dialogParte.findViewById(R.id.spinnerAtaqueAceites);
        final Spinner spinnerAtaqueAbrasivo = dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
        final Spinner spinnerHorasTrabajoDia = dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
        final Spinner spinnerDiasTrabajoSemana = dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
        final Spinner spinnerAbrasividad = dialogParte.findViewById(R.id.spinnerAbrasividad);
        final Spinner spinnerPorcentajeFinos = dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
        final Spinner spinnerTipoCarga = dialogParte.findViewById(R.id.spinnerTipoCarga);
        final Spinner spinnerVariosPuntosAlimentacion = dialogParte.findViewById(R.id.spinnerVariosPuntosAlimentacion);
        final Spinner spinnerTipoDescarga = dialogParte.findViewById(R.id.spinnerTipoDescarga);
        final Spinner spinnerLluviaMaterial = dialogParte.findViewById(R.id.spinnerLluviaMaterial);
        final Spinner spinnerMaxGranulometria = dialogParte.findViewById(R.id.spinnerMaxGranulometria);


        final TextInputEditText txtMaterial = dialogParte.findViewById(R.id.txtMaterial);
        final TextInputEditText txtCapacidad = dialogParte.findViewById(R.id.txtCapacidadHorizontal);

        final TextInputEditText txtDensidad = dialogParte.findViewById(R.id.txtDensidadMaterial);
        final Spinner spinnerTempMaxMaterial = dialogParte.findViewById(R.id.spinnerTempMaxSobreBandaElevadora);
        final Spinner spinnerTempPromedioMaterial = dialogParte.findViewById(R.id.spinnerTempPromedioMaterialSobreBanda);
        final TextInputEditText txtAnchoPiernaElevador = dialogParte.findViewById(R.id.txtAnchoPiernaElevador);
        final TextInputEditText txtProfundidadPiernaElevador = dialogParte.findViewById(R.id.txtProfundidadPiernaElevador);
        final Spinner spinnerTempAmbienteMin = dialogParte.findViewById(R.id.spinnerTempAmbienteMin);
        final Spinner spinnerTempAmbienteMax = dialogParte.findViewById(R.id.spinnerTempAmbienteMax);


        ArrayAdapter<String> adapterAtaqueQuimico = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueQuimico.setAdapter(adapterAtaqueQuimico);

        ArrayAdapter<String> adapterAtaqueTemperatura = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueTemperatura.setAdapter(adapterAtaqueTemperatura);

        ArrayAdapter<String> adapterAtaqueAceites = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAceites.setAdapter(adapterAtaqueAceites);

        ArrayAdapter<String> adapterAtaqueAbrasivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerAtaqueAbrasivo.setAdapter(adapterAtaqueAbrasivo);

        ArrayAdapter<String> adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
        spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

        ArrayAdapter<String> adapterDiasSemana = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
        spinnerDiasTrabajoSemana.setAdapter(adapterDiasSemana);

        ArrayAdapter<String> adapterAbrasividad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
        spinnerAbrasividad.setAdapter(adapterAbrasividad);

        ArrayAdapter<String> adapterPorcentajeFinos = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
        spinnerPorcentajeFinos.setAdapter(adapterPorcentajeFinos);

        ArrayAdapter<String> adapterTipoCarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCarga);
        spinnerTipoCarga.setAdapter(adapterTipoCarga);

        ArrayAdapter<String> adapterVariosPuntosAlimentacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerVariosPuntosAlimentacion.setAdapter(adapterVariosPuntosAlimentacion);

        ArrayAdapter<String> adapterTipoDescarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoDescarga);
        spinnerTipoDescarga.setAdapter(adapterTipoDescarga);

        ArrayAdapter<String> adapterLluviaMaterial = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
        spinnerLluviaMaterial.setAdapter(adapterLluviaMaterial);

        ArrayAdapter<String> adapterTempMaterial = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
        spinnerTempMaxMaterial.setAdapter(adapterTempMaterial);
        spinnerTempPromedioMaterial.setAdapter(adapterTempMaterial);

        ArrayAdapter<String> adapterTempAmbienteMin = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMinAmbiente);
        spinnerTempAmbienteMin.setAdapter(adapterTempAmbienteMin);


        ArrayAdapter<String> adapterTempMaxAmbiente = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMaxAmbiente);
        spinnerTempAmbienteMax.setAdapter(adapterTempMaxAmbiente);

        ArrayAdapter<String> adapterMaxGranulometria = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.maxGranulometria);
        spinnerMaxGranulometria.setAdapter(adapterMaxGranulometria);

        if (FragmentSeleccionarTransportador.bandera.equals("Actualizar")) {
            llenarRegistros("Condiciones Carga");
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
                            cursor=db.rawQuery("SELECT * FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax(),null);
                            cursor.moveToFirst();

                            if(cursor.getString(107).equals("Pendiente INSERTAR BD"))
                            {
                                db.execSQL("DELETE FROM bandaElevadora where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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

                            IdMaximaRegistro idMaximaRegistro = new IdMaximaRegistro();
                            idMaximaRegistro.setMax(String.valueOf(idRegistroInsert));
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            FragmentPartesVertical.idMaximaRegistro.add(idMaximaRegistro);
                            idRegistroUpdate=idRegistroInsert;


                            db.execSQL("INSERT INTO registro values (" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + ",'" + FragmentPartesVertical.fechaActual + "'," + FragmentSeleccionarTransportador.idTransportadorRegistro + "," + FragmentSeleccionarTransportador.idPlantaRegistro + ",'" + Login.nombreUsuario + "', 'Pendiente INSERTAR BD')");

                            params.clear();

                            params.put("idRegistro", FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
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
                            params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");


                            if (!txtDensidad.getText().toString().equals("")) {
                                params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                            }
                            if (!txtAnchoPiernaElevador.getText().toString().equals("")) {
                                params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                            }

                            if (!txtProfundidadPiernaElevador.getText().toString().equals("")) {
                                params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

                            }

                            db.insert("bandaElevadora", null, params);
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

                                                String url = Constants.url + "registroCondicionesCargaElevadora";
                                                StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                        db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado', idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+" where idRegistro=" + idRegistroUpdate);
                                                        db.execSQL("update registro set idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax()+", estadoRegistro='Sincronizado' where idRegistro="+idRegistroUpdate);
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

                                                        if (!txtDensidad.getText().toString().equals("")) {
                                                            params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                                                        }
                                                        if (!txtAnchoPiernaElevador.getText().toString().equals("")) {
                                                            params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                                                        }

                                                        if (!txtProfundidadPiernaElevador.getText().toString().equals("")) {
                                                            params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

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
                                        params.put("usuarioRegistro", loginJsons.get(0).getCodagente());

                                        return params;
                                    }
                                };
                                requestCrearRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                            if (cursor.getString(107).equals("Sincronizado"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else if(cursor.getString(107).equals("Actualizar con BD"))
                            {
                                params.put("estadoRegistroElevadora", "Actualizar con BD");
                                db.execSQL("Update registro set estadoRegistro='Actualizar con BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                            }
                            else
                            {
                                params.put("estadoRegistroElevadora", "Pendiente INSERTAR BD");
                                db.execSQL("update registro set estadoRegistro='Pendiente INSERTAR BD' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());

                            }

                            if (!txtDensidad.getText().toString().equals("")) {
                                params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                            }
                            if (!txtAnchoPiernaElevador.getText().toString().equals("")) {
                                params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                            }

                            if (!txtProfundidadPiernaElevador.getText().toString().equals("")) {
                                params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

                            }
                            db = dbHelper.getWritableDatabase();

                            db.update("bandaElevadora", params, "idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                            if (MainActivity.isOnline(getContext())) {
                                String url = Constants.url + "actualizarRegistro";
                                StringRequest requestCrearRegistro = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                        String url = Constants.url + "actualizarCondicionesCargaElevadora";
                                        StringRequest requestRegistroTornillos = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                db.execSQL("update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                db.execSQL("update registro set estadoRegistro='Sincronizado' where idRegistro="+FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                                MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                FragmentSeleccionarTransportador.bandera = "Actualizar";
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

                                                if (!txtDensidad.getText().toString().equals("")) {
                                                    params.put("densidadMaterialElevadora", txtDensidad.getText().toString());

                                                }
                                                if (!txtAnchoPiernaElevador.getText().toString().equals("")) {
                                                    params.put("anchoPiernaElevador", String.valueOf(Float.parseFloat(txtAnchoPiernaElevador.getText().toString())));

                                                }

                                                if (!txtProfundidadPiernaElevador.getText().toString().equals("")) {
                                                    params.put("profundidadPiernaElevador", String.valueOf(Float.parseFloat(txtProfundidadPiernaElevador.getText().toString())));

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


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {


        switch (v.getId()) {

            case R.id.txtDiametroPoleaMotrizElevadora:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 50 || numero > 1524) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 2\" / 50mm -  60\" / 1524mm");
                        }
                    }
                }
                break;

            case R.id.txtAnchoPoleaMotrizElevadora:
            case R.id.txtDiametroEjePoleaMotrizElevadora:
                if (!hasFocus) {
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
                if (!hasFocus) {
                    EditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
                    EditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
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

            case R.id.txtLargoEjePoleaMotrizElevadora:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                    if (!txtDiametroRosca.getText().toString().equals("")) {
                        float numero = Float.parseFloat(txtDiametroRosca.getText().toString());
                        if (numero < 254 || numero > 2700) {
                            txtDiametroRosca.setText("");
                            txtDiametroRosca.setError("Éste valor debe estar entre 10\" / 254mm -  106.3\" / 2700mm");
                        }
                    }
                }
                break;

            case R.id.txtEpesorTotalElevadora:
            case R.id.txtEpesorTotalElevadoraAnterior:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
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

            case R.id.txtEspesorCojin:
            case R.id.txtEspesorCojinAnterior:
            case R.id.txtCubSupElevadora:
            case R.id.txtCubSupElevadoraAnterior:
            case R.id.txtCubInfElevadoraAnterior:
            case R.id.txtCubInfElevadora:
                if (!hasFocus) {
                    EditText txtDiametroRosca = dialogParte.findViewById(R.id.txtEspesorCojin);
                    EditText txtDiametroRosca1 = dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
                    EditText txtDiametroRosca2 = dialogParte.findViewById(R.id.txtCubSupElevadora);
                    EditText txtDiametroRosca3 = dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
                    EditText txtDiametroRosca4 = dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
                    EditText txtDiametroRosca5 = dialogParte.findViewById(R.id.txtCubInfElevadora);
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

            case R.id.txtVelocidadBandaElevadora:
            case R.id.txtVelocidadBandaElevadoraAnterior:

                if (!hasFocus) {
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
        switch (v.getId()) {
            case R.id.btnEnviarRegistroBandaElevadora:
                break;

            case R.id.tvPesoMaterialEnCadaCangilon:
                abrirDialogParte("pesoEnCadaCangilon");
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

            case "pesoEnCadaCangilon":
                dialogParte = new Dialog(getContext());
                dialogParte.setContentView(R.layout.mas_detalles);
                imgParte = dialogParte.findViewById(R.id.imgMasDetalles);
                imgParte.setImageResource(R.drawable.peso_material_cada_cangilon);
                imgParte.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));
                dialogParte.setCancelable(true);
                dialogParte.show();
                break;
        }

    }

    public void login(final String dialog) {
        String url = Constants.url + "login/" + Login.loginJsons.get(0).getCodagente() + "&" + Login.loginJsons.get(0).getPwdagente();
        final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                 db = dbHelper.getWritableDatabase();

                Type type = new TypeToken<List<LoginJson>>() {
                }.getType();
                loginJsons = gson.fromJson(response, type);

                if (loginJsons.get(0).getEstadoagte().equals("0")) {
                    db.execSQL("DELETE FROM usuario");
                    db.execSQL("DELETE FROM clientes");
                    db.execSQL("DELETE FROM plantas");
                    db.execSQL("DELETE FROM registro");
                    db.execSQL("DELETE FROM bandaTransmision");
                    db.execSQL("DELETE FROM bandaTransportadora");
                    db.execSQL("DELETE FROM bandaElevadora");

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                } else {
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
                                    if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.DSF")) {
                                        db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "',null)");
                                    } else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E")) {
                                        db.execSQL("INSERT INTO bandaElevadora values('" + loginJsons.get(i).getIdRegistro() + "', '" + loginJsons.get(i).getMarcaBandaElevadora() + "', '" + loginJsons.get(i).getAnchoBandaElevadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasElevadora() + "', '" + loginJsons.get(i).getNoLonaBandaElevadora() + "', '" + loginJsons.get(i).getTipoLonaBandaElevadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaElevadora() + "', '" + loginJsons.get(i).getEspesorCojinActualElevadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorElevadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorElevadora() + "', '" + loginJsons.get(i).getTipoCubiertaElevadora() + "', '" + loginJsons.get(i).getTipoEmpalmeElevadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeElevadora() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaElevadora() + "', '" + loginJsons.get(i).getVelocidadBandaElevadora() + "', '" + loginJsons.get(i).getMarcaBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getAnchoBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getEspesorCojinElevadoraAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaElevadoraAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaElevadoraAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getVelocidadBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior() + "', '" + loginJsons.get(i).getPesoMaterialEnCadaCangilon() + "', '" + loginJsons.get(i).getPesoCangilonVacio() + "', '" + loginJsons.get(i).getLongitudCangilon() + "', '" + loginJsons.get(i).getMaterialCangilon() + "', '" + loginJsons.get(i).getTipoCangilon() + "', '" + loginJsons.get(i).getProyeccionCangilon() + "','" + loginJsons.get(i).getProfundidadCangilon() + "' ,'" + loginJsons.get(i).getMarcaCangilon() + "', '" + loginJsons.get(i).getReferenciaCangilon() + "', '" + loginJsons.get(i).getCapacidadCangilon() + "', '" + loginJsons.get(i).getNoFilasCangilones() + "', '" + loginJsons.get(i).getSeparacionCangilones() + "', '" + loginJsons.get(i).getNoAgujeros() + "', '" + loginJsons.get(i).getDistanciaBordeBandaEstructura() + "', '" + loginJsons.get(i).getDistanciaPosteriorBandaEstructura() + "', '" + loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura() + "', '" + loginJsons.get(i).getDistanciaBordesCangilonEstructura() + "', '" + loginJsons.get(i).getTipoVentilacion() + "', '" + loginJsons.get(i).getDiametroPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getAnchoPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getTipoPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getLargoEjeMotrizElevadora() + "', '" + loginJsons.get(i).getDiametroEjeMotrizElevadora() + "', '" + loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getPotenciaMotorMotrizElevadora() + "', '" + loginJsons.get(i).getRpmSalidaReductorMotrizElevadora() + "', '" + loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora() + "', '" + loginJsons.get(i).getDiametroPoleaColaElevadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaElevadora() + "', '" + loginJsons.get(i).getTipoPoleaColaElevadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaElevadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaElevadora() + "', '" + loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora() + "', '" + loginJsons.get(i).getCargaTrabajoBandaElevadora() + "', '" + loginJsons.get(i).getTemperaturaMaterialElevadora() + "', '" + loginJsons.get(i).getEmpalmeMecanicoElevadora() + "', '" + loginJsons.get(i).getDiametroRoscaElevadora() + "', '" + loginJsons.get(i).getLargoTornilloElevadora() + "', '" + loginJsons.get(i).getMaterialTornilloElevadora() + "', '" + loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion() + "', '" + loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion() + "', '" + loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion() + "', '" + loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion() + "', '" + loginJsons.get(i).getMonitorPeligro() + "', '" + loginJsons.get(i).getRodamiento() + "', '" + loginJsons.get(i).getMonitorDesalineacion() + "', '" + loginJsons.get(i).getMonitorVelocidad() + "', '" + loginJsons.get(i).getSensorInductivo() + "', '" + loginJsons.get(i).getIndicadorNivel() + "', '" + loginJsons.get(i).getCajaUnion() + "', '" + loginJsons.get(i).getAlarmaYPantalla() + "', '" + loginJsons.get(i).getInterruptorSeguridad() + "', '" + loginJsons.get(i).getMaterialElevadora() + "', '" + loginJsons.get(i).getAtaqueQuimicoElevadora() + "', '" + loginJsons.get(i).getAtaqueTemperaturaElevadora() + "', '" + loginJsons.get(i).getAtaqueAceitesElevadora() + "', '" + loginJsons.get(i).getAtaqueAbrasivoElevadora() + "', '" + loginJsons.get(i).getCapacidadElevadora() + "', '" + loginJsons.get(i).getHorasTrabajoDiaElevadora() + "', '" + loginJsons.get(i).getDiasTrabajoSemanaElevadora() + "', '" + loginJsons.get(i).getAbrasividadElevadora() + "', '" + loginJsons.get(i).getPorcentajeFinosElevadora() + "', '" + loginJsons.get(i).getMaxGranulometriaElevadora() + "', '" + loginJsons.get(i).getDensidadMaterialElevadora() + "', '" + loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora() + "', '" + loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora() + "', '" + loginJsons.get(i).getVariosPuntosDeAlimentacion() + "', '" + loginJsons.get(i).getLluviaDeMaterial() + "', '" + loginJsons.get(i).getAnchoPiernaElevador() + "', '" + loginJsons.get(i).getProfundidadPiernaElevador() + "', '" + loginJsons.get(i).getTempAmbienteMin() + "', '" + loginJsons.get(i).getTempAmbienteMax() + "', '" + loginJsons.get(i).getTipoDescarga() + "', '" + loginJsons.get(i).getTipoCarga() + "')");

                                    } else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T")) {
                                        db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','" + loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda() + "' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "'," +
                                                "'" + loginJsons.get(i).getMaterial() + "', '" + loginJsons.get(i).getAnguloSobreCarga() + "', '" + loginJsons.get(i).getAtaqueQuimicoTransportadora() + "', '" + loginJsons.get(i).getAtaqueTemperaturaTransportadora() + "', '" + loginJsons.get(i).getAtaqueAceiteTransportadora() + "', '" + loginJsons.get(i).getAtaqueImpactoTransportadora() + "', '" + loginJsons.get(i).getCapacidadTransportadora() + "', '" + loginJsons.get(i).getHorasTrabajoPorDiaTransportadora() + "', '" + loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora() + "', '" + loginJsons.get(i).getAlimentacionCentradaTransportadora() + "', '" + loginJsons.get(i).getAbrasividadTransportadora() + "', '" + loginJsons.get(i).getPorcentajeFinosTransportadora() + "', '" + loginJsons.get(i).getMaxGranulometriaTransportadora() + "', '" + loginJsons.get(i).getMaxPesoTransportadora() + "', '" + loginJsons.get(i).getDensidadTransportadora() + "', '" + loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora() + "', '" + loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora() + "', '" + loginJsons.get(i).getCajaColaDeTolva() + "', '" + loginJsons.get(i).getFugaMateriales() + "', '" + loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute() + "', '" + loginJsons.get(i).getFugaDeMaterialesPorLosCostados() + "', '" + loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute() + "', '" + loginJsons.get(i).getAnchoChute() + "', '" + loginJsons.get(i).getLargoChute() + "', '" + loginJsons.get(i).getAlturaChute() + "', '" + loginJsons.get(i).getCauchoGuardabandas() + "', '" + loginJsons.get(i).getTriSealMultiSeal() + "', '" + loginJsons.get(i).getEspesorGuardaBandas() + "', '" + loginJsons.get(i).getAnchoGuardaBandas() + "', '" + loginJsons.get(i).getLargoGuardaBandas() + "', '" + loginJsons.get(i).getProtectorGuardaBandas() + "', '" + loginJsons.get(i).getCortinaAntiPolvo1() + "', '" + loginJsons.get(i).getCortinaAntiPolvo2() + "', '" + loginJsons.get(i).getCortinaAntiPolvo3() + "', '" + loginJsons.get(i).getBoquillasCanonesDeAire() + "', '" + loginJsons.get(i).getTempAmbienteMaxTransportadora() + "', '" + loginJsons.get(i).getTempAmbienteMinTransportadora() + "', '" + loginJsons.get(i).getTieneRodillosImpacto() + "', '" + loginJsons.get(i).getCamaImpacto() + "', '" + loginJsons.get(i).getCamaSellado() + "', '" + loginJsons.get(i).getBasculaPesaje() + "', '" + loginJsons.get(i).getRodilloCarga() + "', '" + loginJsons.get(i).getRodilloImpacto() + "', '" + loginJsons.get(i).getBasculaASGCO() + "', '" + loginJsons.get(i).getBarraImpacto() + "', '" + loginJsons.get(i).getBarraDeslizamiento() + "', '" + loginJsons.get(i).getEspesorUHMV() + "', '" + loginJsons.get(i).getAnchoBarra() + "', '" + loginJsons.get(i).getLargoBarra() + "','" + loginJsons.get(i).getAnguloAcanalamientoArtesa1() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesa2() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesa3() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz() + "', '" + loginJsons.get(i).getIntegridadSoportesRodilloImpacto() + "', '" + loginJsons.get(i).getMaterialAtrapadoEntreCortinas() + "', '" + loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas() + "', '" + loginJsons.get(i).getMaterialAtrapadoEnBanda() + "', '" + loginJsons.get(i).getIntegridadSoportesCamaImpacto() + "', '" + loginJsons.get(i).getInclinacionZonaCargue() + "', '" + loginJsons.get(i).getSistemaAlineacionCarga() + "', '" + loginJsons.get(i).getCantidadSistemaAlineacionEnCarga() + "', '" + loginJsons.get(i).getSistemasAlineacionCargFuncionando() + "', '" + loginJsons.get(i).getSistemaAlineacionEnRetorno() + "', '" + loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno() + "', '" + loginJsons.get(i).getSistemasAlineacionRetornoFuncionando() + "', '" + loginJsons.get(i).getSistemaAlineacionRetornoPlano() + "', '" + loginJsons.get(i).getSistemaAlineacionArtesaCarga() + "', '" + loginJsons.get(i).getSistemaAlineacionRetornoEnV() + "', '" + loginJsons.get(i).getLargoEjeRodilloCentralCarga() + "', '" + loginJsons.get(i).getDiametroEjeRodilloCentralCarga() + "', '" + loginJsons.get(i).getLargoTuboRodilloCentralCarga() + "', '" + loginJsons.get(i).getLargoEjeRodilloLateralCarga() + "', '" + loginJsons.get(i).getDiametroEjeRodilloLateralCarga() + "', '" + loginJsons.get(i).getDiametroRodilloLateralCarga() + "', '" + loginJsons.get(i).getLargoTuboRodilloLateralCarga() + "', '" + loginJsons.get(i).getTipoRodilloCarga() + "', '" + loginJsons.get(i).getDistanciaEntreArtesasCarga() + "', '" + loginJsons.get(i).getAnchoInternoChasisRodilloCarga() + "', '" + loginJsons.get(i).getAnchoExternoChasisRetorno() + "', '" + loginJsons.get(i).getAnguloAcanalamientoArtesaCArga() + "', '" + loginJsons.get(i).getDetalleRodilloCentralCarga() + "', '" + loginJsons.get(i).getDetalleRodilloLateralCarg() + "', '" + loginJsons.get(i).getDiametroPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getDiametroEjeMotrizTransportadora() + "', '" + loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getPotenciaMotorTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaMotrizTransportadora() + "', '" + loginJsons.get(i).getAnchoEstructura() + "', '" + loginJsons.get(i).getAnchoTrayectoCarga() + "', '" + loginJsons.get(i).getPasarelaRespectoAvanceBanda() + "', '" + loginJsons.get(i).getMaterialAlimenticioTransportadora() + "', '" + loginJsons.get(i).getMaterialAcidoTransportadora() + "', '" + loginJsons.get(i).getMaterialTempEntre80y150Transportadora() + "', '" + loginJsons.get(i).getMaterialSecoTransportadora() + "', '" + loginJsons.get(i).getMaterialHumedoTransportadora() + "', '" + loginJsons.get(i).getMaterialAbrasivoFinoTransportadora() + "', '" + loginJsons.get(i).getMaterialPegajosoTransportadora() + "', '" + loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora() + "', '" + loginJsons.get(i).getMarcaLimpiadorPrimario() + "'           ,'" + loginJsons.get(i).getReferenciaLimpiadorPrimario() + "' ,'" + loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario() + "','" + loginJsons.get(i).getAltoCuchillaLimpiadorPrimario() + "','" + loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario() + "','" + loginJsons.get(i).getEstadoTensorLimpiadorPrimario() + "','" + loginJsons.get(i).getEstadoTuboLimpiadorPrimario() + "','" + loginJsons.get(i).getFrecuenciaRevisionCuchilla() + "','" + loginJsons.get(i).getCuchillaEnContactoConBanda() + "' , '" + loginJsons.get(i).getMarcaLimpiadorSecundario() + "','" + loginJsons.get(i).getReferenciaLimpiadorSecundario() + "', '" + loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario() + "', '" + loginJsons.get(i).getAltoCuchillaLimpiadorSecundario() + "', '" + loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario() + "', '" + loginJsons.get(i).getEstadoTensorLimpiadorSecundario() + "','" + loginJsons.get(i).getEstadoTuboLimpiadorSecundario() + "','" + loginJsons.get(i).getFrecuenciaRevisionCuchilla1() + "', '" + loginJsons.get(i).getCuchillaEnContactoConBanda1() + "', '" + loginJsons.get(i).getSistemaDribbleChute() + "','" + loginJsons.get(i).getMarcaLimpiadorTerciario() + "', '" + loginJsons.get(i).getReferenciaLimpiadorTerciario() + "', '" + loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario() + "', '" + loginJsons.get(i).getAltoCuchillaLimpiadorTerciario() + "', '" + loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario() + "', '" + loginJsons.get(i).getEstadoTensorLimpiadorTerciario() + "', '" + loginJsons.get(i).getEstadoTensorLimpiadorTerciario() + "', '" + loginJsons.get(i).getFrecuenciaRevisionCuchilla2() + "', '" + loginJsons.get(i).getCuchillaEnContactoConBanda2() + "', '" + loginJsons.get(i).getEstadoRodilloRetorno() + "', '" + loginJsons.get(i).getLargoEjeRodilloRetorno() + "', '" + loginJsons.get(i).getDiametroEjeRodilloRetorno() + "', '" + loginJsons.get(i).getDiametroRodilloRetorno() + "', '" + loginJsons.get(i).getLargoTuboRodilloRetorno() + "', '" + loginJsons.get(i).getTipoRodilloRetorno() + "', '" + loginJsons.get(i).getDistanciaEntreRodillosRetorno() + "', '" + loginJsons.get(i).getAnchoInternoChasisRetorno() + "', '" + loginJsons.get(i).getAnchoExternoChasisRetorno() + "', '" + loginJsons.get(i).getDetalleRodilloRetorno() + "', '" + loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz() + "', '" + loginJsons.get(i).getDimetroPoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getAnchoPoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getTipoPoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola() + "', '" + loginJsons.get(i).getDiametroPoleaTensora() + "', '" + loginJsons.get(i).getAnchoPoleaTensora() + "', '" + loginJsons.get(i).getTipoPoleaTensora() + "', '" + loginJsons.get(i).getLargoEjePoleaTensora() + "', '" + loginJsons.get(i).getDiametroEjePoleaTensora() + "', '" + loginJsons.get(i).getIcobandasCentradaEnPoleaTensora() + "', '" + loginJsons.get(i).getRecorridoPoleaTensora() + "', '" + loginJsons.get(i).getEstadoRevestimientoPoleaTensora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaTensora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTensora() + "', '" + loginJsons.get(i).getPotenciaMotorPoleaTensora() + "', '" + loginJsons.get(i).getGuardaPoleaTensora() + "', '" + loginJsons.get(i).getPuertasInspeccion() + "', '" + loginJsons.get(i).getGuardaRodilloRetornoPlano() + "', '" + loginJsons.get(i).getGuardaTruTrainer() + "', '" + loginJsons.get(i).getGuardaPoleaDeflectora() + "', '" + loginJsons.get(i).getGuardaZonaDeTransito() + "', '" + loginJsons.get(i).getGuardaMotores() + "', '" + loginJsons.get(i).getGuardaCadenas() + "', '" + loginJsons.get(i).getGuardaCorreas() + "', '" + loginJsons.get(i).getGuardaCorreas() + "', '" + loginJsons.get(i).getInterruptoresDeSeguridad() + "', '" + loginJsons.get(i).getSirenasDeSeguridad() + "', '" + loginJsons.get(i).getGuardaRodilloRetornoV() + "', '" + loginJsons.get(i).getDiametroRodilloCentralCarga() + "', '" + loginJsons.get(i).getTipoRodilloImpacto() + "', '" + loginJsons.get(i).getIntegridadSoporteCamaSellado() + "', '" + loginJsons.get(i).getAtaqueAbrasivoTransportadora() + "')");
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
                                    Login.nombreUsuario = loginJsons.get(0).getCodagente();
                                    dialogCarga.dismiss();
                                    dialogParte.cancel();
                                    MDToast.makeText(getContext(), "REGISTRO EXITOSO", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                }
                            });
                            queue.add(requestCiudades);
                            requestCiudades.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            MDToast.makeText(getContext(), error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    });
                    requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(requestLogin);
    }

    private void llenarRegistros(String parte) {
        switch (parte) {
            case "Tornillos":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }

                cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    final Spinner spinnerDiametroRosca = dialogParte.findViewById(R.id.spinnerDiametroRosca);
                    final Spinner spinnerLargoTornillo = dialogParte.findViewById(R.id.spinnerLargoTornillo);

                    final Spinner spinnerMaterialTornillo = dialogParte.findViewById(R.id.spinnerMaterialTornillo);

                    ArrayAdapter<String> adapterMaterialTornillos = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.materialTornillo);
                    spinnerMaterialTornillo.setAdapter(adapterMaterialTornillos);

                    ArrayAdapter<String> adapterDiametroRosca = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diametroRosca);
                    spinnerDiametroRosca.setAdapter(adapterDiametroRosca);

                    ArrayAdapter<String> adapterLargoTornillo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.largoTornillo);
                    spinnerLargoTornillo.setAdapter(adapterLargoTornillo);

                    int posicion = adapterMaterialTornillos.getPosition(cursor.getString(cursor.getColumnIndex("materialTornilloElevadora")));
                    int posicion2 = adapterDiametroRosca.getPosition(cursor.getString(cursor.getColumnIndex("diametroRoscaElevadora")));
                    int posicion3 = adapterLargoTornillo.getPosition(cursor.getString(cursor.getColumnIndex("largoTornilloElevadora")));
                    spinnerMaterialTornillo.setSelection(posicion);
                    spinnerLargoTornillo.setSelection(posicion3);
                    spinnerDiametroRosca.setSelection(posicion2);


                break;

            case "Empalme":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);

                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    final Spinner spinnerEmpalmeMecanico = dialogParte.findViewById(R.id.spinnerEmpalmeMecanicoElevadora);
                    final TextInputEditText txtCargaTrabajo = dialogParte.findViewById(R.id.txtCargaTrabajoBandaElevadora);
                    final TextInputEditText txtTmperaturaMaterialElevadora = dialogParte.findViewById(R.id.txtTempMaterialElevadora);
                    ArrayAdapter<String> adapterEmpalmeMecanico = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);


                    txtCargaTrabajo.setText(cursor.getString(cursor.getColumnIndex("cargaTrabajoBandaElevadora")));
                    txtTmperaturaMaterialElevadora.setText(cursor.getString(cursor.getColumnIndex("temperaturaMaterialElevadora")));
                    posicion = adapterEmpalmeMecanico.getPosition(cursor.getString(cursor.getColumnIndex("empalmeMecanicoElevadora")));
                    spinnerEmpalmeMecanico.setSelection(posicion);



                break;

            case "Puertas Inspeccion":


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    TextInputEditText txtAnchoCabezaElevador = dialogParte.findViewById(R.id.txtAnchoCabezaElevador);
                    TextInputEditText txtLargoCabezaElevador = dialogParte.findViewById(R.id.txtLargoCabezaElevador);
                    TextInputEditText txtAnchoBotaElevador = dialogParte.findViewById(R.id.txtAnchoBotaElevador);
                    TextInputEditText txtLargoBotaElevador = dialogParte.findViewById(R.id.txtLargoBotaElevador);


                    txtAnchoCabezaElevador.setText(cursor.getString(cursor.getColumnIndex("anchoCabezaElevadorPuertaInspeccion")));
                    txtLargoCabezaElevador.setText(cursor.getString(cursor.getColumnIndex("largoCabezaElevadorPuertaInspeccion")));
                    txtAnchoBotaElevador.setText(cursor.getString(cursor.getColumnIndex("anchoBotaElevadorPuertaInspeccion")));
                    txtLargoBotaElevador.setText(cursor.getString(cursor.getColumnIndex("largoBotaElevadorPuertaInspeccion")));


                break;
            case "Seguridad":


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    Spinner spinnerMonitorPeligro = dialogParte.findViewById(R.id.spinnerMonitorPeligro);
                    Spinner spinnerRodamiento = dialogParte.findViewById(R.id.spinnerRodamiento);
                    Spinner spinnerMonitorDesalineacion = dialogParte.findViewById(R.id.spinnerMonitorDesalineacion);
                    Spinner spinnerMonitorVelocidad = dialogParte.findViewById(R.id.spinnerMonitorVelocidad);
                    Spinner spinnerSensorInductivo = dialogParte.findViewById(R.id.spinnerSensorInductivo);
                    Spinner spinnerIndicadorDeNivel = dialogParte.findViewById(R.id.spinnerIndicadorDeNivel);
                    Spinner spinnerCajaDeUnion = dialogParte.findViewById(R.id.spinnerCajaDeUnion);
                    Spinner spinnerAlarmaYPantalla = dialogParte.findViewById(R.id.spinnerAlarmaYPantalla);
                    Spinner spinnerInterruptorSeguridad = dialogParte.findViewById(R.id.spinnerInterruptorDeSeguridad);

                    ArrayAdapter<String> adapterMonitorPeligro = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorPeligro);
                    spinnerMonitorPeligro.setAdapter(adapterMonitorPeligro);

                    ArrayAdapter<String> adapterRodamiento = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.rodamiento);
                    spinnerRodamiento.setAdapter(adapterRodamiento);

                    ArrayAdapter<String> adapterMonitorDesalineacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorDesalineacion);
                    spinnerMonitorDesalineacion.setAdapter(adapterMonitorDesalineacion);

                    ArrayAdapter<String> adapterMonitorVelocidad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.monitorVelocidad);
                    spinnerMonitorVelocidad.setAdapter(adapterMonitorVelocidad);

                    ArrayAdapter<String> adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.sensorInductivo);
                    spinnerSensorInductivo.setAdapter(adapterSensorInductivo);

                    ArrayAdapter<String> adapterIndicadorNivel = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.indicadorDeNivel);
                    spinnerIndicadorDeNivel.setAdapter(adapterIndicadorNivel);


                    ArrayAdapter<String> adapterCajaUnion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.cajaUnion);
                    spinnerCajaDeUnion.setAdapter(adapterCajaUnion);

                    ArrayAdapter<String> adapterAlarmaPantalla = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.alarmaYPantalla);
                    spinnerAlarmaYPantalla.setAdapter(adapterAlarmaPantalla);

                    ArrayAdapter<String> adapterInterruptorSeguridad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.interruptorSeguridad);
                    spinnerInterruptorSeguridad.setAdapter(adapterInterruptorSeguridad);


                    int p1 = adapterMonitorPeligro.getPosition(cursor.getString(cursor.getColumnIndex("monitorPeligro")));
                    int p2 = adapterRodamiento.getPosition(cursor.getString(cursor.getColumnIndex("rodamiento")));
                    int p3 = adapterMonitorDesalineacion.getPosition(cursor.getString(cursor.getColumnIndex("monitorDesalineacion")));
                    int p4 = adapterMonitorVelocidad.getPosition(cursor.getString(cursor.getColumnIndex("monitorVelocidad")));
                    int p5 = adapterSensorInductivo.getPosition(cursor.getString(cursor.getColumnIndex("sensorInductivo")));
                    int p6 = adapterIndicadorNivel.getPosition(cursor.getString(cursor.getColumnIndex("indicadorNivel")));
                    int p7 = adapterCajaUnion.getPosition(cursor.getString(cursor.getColumnIndex("cajaUnion")));
                    int p8 = adapterAlarmaPantalla.getPosition(cursor.getString(cursor.getColumnIndex("alarmaYPantalla")));
                    int p9 = adapterInterruptorSeguridad.getPosition(cursor.getString(cursor.getColumnIndex("interruptorSeguridad")));

                    spinnerMonitorPeligro.setSelection(p1);
                    spinnerRodamiento.setSelection(p2);
                    spinnerMonitorDesalineacion.setSelection(p3);
                    spinnerMonitorVelocidad.setSelection(p4);
                    spinnerSensorInductivo.setSelection(p5);
                    spinnerIndicadorDeNivel.setSelection(p6);
                    spinnerCajaDeUnion.setSelection(p7);
                    spinnerAlarmaYPantalla.setSelection(p8);
                    spinnerInterruptorSeguridad.setSelection(p9);

                break;

            case "Polea Motriz":
                
                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    Log.e("SELECT:", "SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    Spinner spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
                    Spinner spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
                    Spinner spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);
                    Spinner spinnerGuardaPolea = dialogParte.findViewById(R.id.spinnerGuardaPoleaMotrizElevadora);
                    TextInputEditText txtDiametroPoleaMotriz = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                    TextInputEditText txtAnchoPoleaMotriz = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
                    TextInputEditText txtLargoEjePoleaMotriz = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                    TextInputEditText txtDiametroEjePoleaMotriz = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
                    TextInputEditText txtPotenciaMotor = dialogParte.findViewById(R.id.txtPotenciaMotorElevadora);
                    TextInputEditText txtRPMSalidaReductor = dialogParte.findViewById(R.id.txtRpmSalidaReductorPoleaMotriz);

                    ArrayAdapter<String> adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                    ArrayAdapter<String> adapterBandaCentrada = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    ArrayAdapter<String> adapterEstadoRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    ArrayAdapter<String> adapterGuardaPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);

                    p1 = adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaMotrizElevadora")));
                    p2 = adapterBandaCentrada.getPosition(cursor.getString(cursor.getColumnIndex("bandaCentradaEnPoleaMotrizElevadora")));
                    p3 = adapterEstadoRvto.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaMotrizElevadora")));
                    p4 = adapterGuardaPolea.getPosition(cursor.getString(cursor.getColumnIndex("guardaReductorPoleaMotrizElevadora")));

                    spinnerTipoPolea.setSelection(p1);
                    spinnerBandaCentradaEnPolea.setSelection(p2);
                    spinnerEstadoRvto.setSelection(p3);
                    spinnerGuardaPolea.setSelection(p4);

                    txtDiametroPoleaMotriz.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaMotrizElevadora")));
                    txtAnchoPoleaMotriz.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaMotrizElevadora")));
                    txtLargoEjePoleaMotriz.setText(cursor.getString(cursor.getColumnIndex("largoEjeMotrizElevadora")));
                    txtDiametroEjePoleaMotriz.setText(cursor.getString(cursor.getColumnIndex("diametroEjeMotrizElevadora")));
                    txtPotenciaMotor.setText(cursor.getString(cursor.getColumnIndex("potenciaMotorMotrizElevadora")));
                    txtRPMSalidaReductor.setText(cursor.getString(cursor.getColumnIndex("rpmSalidaReductorMotrizElevadora")));


                    txtAnchoPoleaMotriz.setOnFocusChangeListener(this);
                    txtDiametroEjePoleaMotriz.setOnFocusChangeListener(this);
                    txtDiametroPoleaMotriz.setOnFocusChangeListener(this);
                    txtLargoEjePoleaMotriz.setOnFocusChangeListener(this);


                break;

            case "Polea Cola":


                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    Log.e("SELECT: ", "SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }

                     spinnerTipoPolea = dialogParte.findViewById(R.id.spinnerTipoPoleaMotrizElevadora);
                     spinnerBandaCentradaEnPolea = dialogParte.findViewById(R.id.spinnerBandaCentradaPoleaMotrizElevadora);
                     spinnerEstadoRvto = dialogParte.findViewById(R.id.spinnerEstadoRvto);

                    final TextInputEditText txtDiametroPoleaCola = dialogParte.findViewById(R.id.txtDiametroPoleaMotrizElevadora);
                    final TextInputEditText txtAnchoPoleaCola = dialogParte.findViewById(R.id.txtAnchoPoleaMotrizElevadora);
                    final TextInputEditText txtLargoEjePoleaCola = dialogParte.findViewById(R.id.txtLargoEjePoleaMotrizElevadora);
                    final TextInputEditText txtDiametroEjePoleaCola = dialogParte.findViewById(R.id.txtDiametroEjePoleaMotrizElevadora);
                    final TextInputEditText txtLongTensorTornillo = dialogParte.findViewById(R.id.txtLongitudTensorTornilloElevadora);
                    final TextInputEditText txtLongRecorridoContraPesa = dialogParte.findViewById(R.id.txtLongitudContraPesaPoleaColaElevadora);


                    adapterTipoPolea = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoPolea);
                    spinnerTipoPolea.setAdapter(adapterTipoPolea);
                    adapterBandaCentrada = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerBandaCentradaEnPolea.setAdapter(adapterBandaCentrada);
                    adapterEstadoRvto = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.estadoPartes);
                    spinnerEstadoRvto.setAdapter(adapterEstadoRvto);


                    txtDiametroPoleaCola.setText(cursor.getString(cursor.getColumnIndex("diametroPoleaColaElevadora")));
                    txtAnchoPoleaCola.setText(cursor.getString(cursor.getColumnIndex("anchoPoleaColaElevadora")));
                    txtLargoEjePoleaCola.setText(cursor.getString(cursor.getColumnIndex("largoEjePoleaColaElevadora")));
                    txtDiametroEjePoleaCola.setText(cursor.getString(cursor.getColumnIndex("diametroEjePoleaColaElevadora")));
                    txtLongTensorTornillo.setText(cursor.getString(cursor.getColumnIndex("longitudTensorTornilloPoleaColaElevadora")));
                    txtLongRecorridoContraPesa.setText(cursor.getString(cursor.getColumnIndex("longitudRecorridoContrapesaPoleaColaElevadora")));

                    p1 = adapterTipoPolea.getPosition(cursor.getString(cursor.getColumnIndex("tipoPoleaColaElevadora")));
                    p2 = adapterBandaCentrada.getPosition(cursor.getString(cursor.getColumnIndex("bandaCentradaEnPoleaColaElevadora")));
                    p3 = adapterEstadoRvto.getPosition(cursor.getString(cursor.getColumnIndex("estadoRevestimientoPoleaColaElevadora")));

                    spinnerTipoPolea.setSelection(p1);
                    spinnerBandaCentradaEnPolea.setSelection(p2);
                    spinnerEstadoRvto.setSelection(p3);

                break;


            case "Cangilon":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

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


                    txtPesoMaterialCangilon.setText(cursor.getString(cursor.getColumnIndex("pesoMaterialEnCadaCangilon")));
                    txtPesoCangilonVacio.setText(cursor.getString(cursor.getColumnIndex("pesoCangilonVacio")));
                    txtLongitudCangilon.setText(cursor.getString(cursor.getColumnIndex("longitudCangilon")));
                    txtProyeccionCangilon.setText(cursor.getString(cursor.getColumnIndex("proyeccionCangilon")));
                    txtProfundidadCangilon.setText(cursor.getString(cursor.getColumnIndex("profundidadCangilon")));
                    txtCapacidadCangilon.setText(cursor.getString(cursor.getColumnIndex("capacidadCangilon")));
                    txtSeparacionEntreCangilones.setText(cursor.getString(cursor.getColumnIndex("separacionCangilones")));
                    txtDistanciaBordesBandaEstructura.setText(cursor.getString(cursor.getColumnIndex("distanciaBordeBandaEstructura")));
                    txtDistanciaPosteriorBandaEstructura.setText(cursor.getString(cursor.getColumnIndex("distanciaPosteriorBandaEstructura")));
                    txtDistLabioFrontalCangilon.setText(cursor.getString(cursor.getColumnIndex("distanciaLaboFrontalCangilonEstructura")));
                    txtDistBordesCangilonEstructura.setText(cursor.getString(cursor.getColumnIndex("distanciaBordesCangilonEstructura")));
                    txtReferenciaCangilon.setText(cursor.getString(cursor.getColumnIndex("referenciaCangilon")));

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

                    p1 = adapterMaterialCangilon.getPosition(cursor.getString(cursor.getColumnIndex("materialCangilon")));
                    p2 = adapterMarcaCangilon.getPosition(cursor.getString(cursor.getColumnIndex("marcaCangilon")));

                    p4 = adapterNoFilasCangilon.getPosition(cursor.getString(cursor.getColumnIndex("noFilasCangilones")));
                    p5 = adapterNoAgujeros.getPosition(cursor.getString(cursor.getColumnIndex("noAgujeros")));
                    p6 = adapterTipoVentilacion.getPosition(cursor.getString(cursor.getColumnIndex("tipoVentilacion")));
                    p7 = adapterTipoCangilon.getPosition(cursor.getString(cursor.getColumnIndex("tipoCangilon")));

                    spinnerMaterialCangilon.setSelection(p1);
                    spinnerMarcaCangilon.setSelection(p2);

                    spinnerNoFilasCangilon.setSelection(p4);
                    spinnerNoAgujeros.setSelection(p5);
                    spinnerTipoVentilacion.setSelection(p6);
                    spinnerTipoCangilon.setSelection(p7);

                break;

            case "Condiciones Carga":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();


                    final Spinner spinnerAtaqueQuimico = dialogParte.findViewById(R.id.spinnerAtaqueQuimico);
                    final Spinner spinnerAtaqueTemperatura = dialogParte.findViewById(R.id.spinnerAtaqueTemperatura);
                    final Spinner spinnerAtaqueAceites = dialogParte.findViewById(R.id.spinnerAtaqueAceites);
                    final Spinner spinnerAtaqueAbrasivo = dialogParte.findViewById(R.id.spinnerAtaqueAbrasivo);
                    final Spinner spinnerHorasTrabajoDia = dialogParte.findViewById(R.id.spinnerHorasTrabajoDia);
                    final Spinner spinnerDiasTrabajoSemana = dialogParte.findViewById(R.id.spinnerDiasTrabajoSemana);
                    final Spinner spinnerAbrasividad = dialogParte.findViewById(R.id.spinnerAbrasividad);
                    final Spinner spinnerPorcentajeFinos = dialogParte.findViewById(R.id.spinnerPorcentajeFinos);
                    final Spinner spinnerTipoCarga = dialogParte.findViewById(R.id.spinnerTipoCarga);
                    final Spinner spinnerVariosPuntosAlimentacion = dialogParte.findViewById(R.id.spinnerVariosPuntosAlimentacion);
                    final Spinner spinnerTipoDescarga = dialogParte.findViewById(R.id.spinnerTipoDescarga);
                    final Spinner spinnerLluviaMaterial = dialogParte.findViewById(R.id.spinnerLluviaMaterial);
                    final Spinner spinnerMaxGranulometria = dialogParte.findViewById(R.id.spinnerMaxGranulometria);
                    Constants.llenar();
                    Constants.llenarTempMin();
                    Constants.llenarTempMax();

                    final TextInputEditText txtMaterial = dialogParte.findViewById(R.id.txtMaterial);
                    final TextInputEditText txtCapacidad = dialogParte.findViewById(R.id.txtCapacidadHorizontal);

                    final TextInputEditText txtDensidad = dialogParte.findViewById(R.id.txtDensidadMaterial);
                    final Spinner spinnerTempMaxMaterial = dialogParte.findViewById(R.id.spinnerTempMaxSobreBandaElevadora);
                    final Spinner spinnerTempPromedioMaterial = dialogParte.findViewById(R.id.spinnerTempPromedioMaterialSobreBanda);
                    final TextInputEditText txtAnchoPiernaElevador = dialogParte.findViewById(R.id.txtAnchoPiernaElevador);
                    final TextInputEditText txtProfundidadPiernaElevador = dialogParte.findViewById(R.id.txtProfundidadPiernaElevador);
                    final Spinner spinnerTempAmbienteMin = dialogParte.findViewById(R.id.spinnerTempAmbienteMin);
                    final Spinner spinnerTempAmbienteMax = dialogParte.findViewById(R.id.spinnerTempAmbienteMax);

                    txtMaterial.setText(cursor.getString(cursor.getColumnIndex("materialElevadora")));
                    txtCapacidad.setText(cursor.getString(cursor.getColumnIndex("capacidadElevadora")));

                    txtDensidad.setText(cursor.getString(cursor.getColumnIndex("densidadMaterialElevadora")));

                    txtAnchoPiernaElevador.setText(cursor.getString(cursor.getColumnIndex("anchoPiernaElevador")));
                    txtProfundidadPiernaElevador.setText(cursor.getString(cursor.getColumnIndex("profundidadPiernaElevador")));


                    ArrayAdapter<String> adapterAtaqueQuimico = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerAtaqueQuimico.setAdapter(adapterAtaqueQuimico);

                    ArrayAdapter<String> adapterAtaqueTemperatura = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerAtaqueTemperatura.setAdapter(adapterAtaqueTemperatura);

                    ArrayAdapter<String> adapterAtaqueAceites = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerAtaqueAceites.setAdapter(adapterAtaqueAceites);

                    ArrayAdapter<String> adapterAtaqueAbrasivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerAtaqueAbrasivo.setAdapter(adapterAtaqueAbrasivo);

                   adapterSensorInductivo = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.horasTrabajo);
                    spinnerHorasTrabajoDia.setAdapter(adapterSensorInductivo);

                    ArrayAdapter<String> adapterDiasSemana = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.diasSemana);
                    spinnerDiasTrabajoSemana.setAdapter(adapterDiasSemana);

                    ArrayAdapter<String> adapterAbrasividad = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.abrasividad);
                    spinnerAbrasividad.setAdapter(adapterAbrasividad);

                    ArrayAdapter<String> adapterPorcentajeFinos = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.porcentajeFinos);
                    spinnerPorcentajeFinos.setAdapter(adapterPorcentajeFinos);

                    ArrayAdapter<String> adapterTipoCarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoCarga);
                    spinnerTipoCarga.setAdapter(adapterTipoCarga);

                    ArrayAdapter<String> adapterVariosPuntosAlimentacion = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerVariosPuntosAlimentacion.setAdapter(adapterVariosPuntosAlimentacion);

                    ArrayAdapter<String> adapterTipoDescarga = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tipoDescarga);
                    spinnerTipoDescarga.setAdapter(adapterTipoDescarga);

                    ArrayAdapter<String> adapterLluviaMaterial = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.opcionSiNo);
                    spinnerLluviaMaterial.setAdapter(adapterLluviaMaterial);

                    ArrayAdapter<String> adapterTempMaterial = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.temperaturaSobreLaBanda);
                    spinnerTempMaxMaterial.setAdapter(adapterTempMaterial);
                    spinnerTempPromedioMaterial.setAdapter(adapterTempMaterial);

                    ArrayAdapter<String> adapterTempAmbienteMin = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMinAmbiente);
                    spinnerTempAmbienteMin.setAdapter(adapterTempAmbienteMin);


                    ArrayAdapter<String> adapterTempMaxAmbiente = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.tempMaxAmbiente);
                    spinnerTempAmbienteMax.setAdapter(adapterTempMaxAmbiente);

                    ArrayAdapter<String> adapterMaxGranulometria = new ArrayAdapter(getContext(), R.layout.estilo_spinner, Constants.maxGranulometria);
                    spinnerMaxGranulometria.setAdapter(adapterMaxGranulometria);


                    p1 = adapterAtaqueQuimico.getPosition(cursor.getString(cursor.getColumnIndex("ataqueQuimicoElevadora")));
                    p2 = adapterAtaqueTemperatura.getPosition(cursor.getString(cursor.getColumnIndex("ataqueTemperaturaElevadora")));
                    p3 = adapterAtaqueAceites.getPosition(cursor.getString(cursor.getColumnIndex("ataqueAceitesElevadora")));
                    p4 = adapterAtaqueAbrasivo.getPosition(cursor.getString(cursor.getColumnIndex("ataqueAbrasivoElevadora")));
                    p5 = adapterSensorInductivo.getPosition(cursor.getString(cursor.getColumnIndex("horasTrabajoDiaElevadora")));
                    p6 = adapterDiasSemana.getPosition(cursor.getString(cursor.getColumnIndex("diasTrabajoSemanaElevadora")));
                    p7 = adapterAbrasividad.getPosition(cursor.getString(cursor.getColumnIndex("abrasividadElevadora")));
                    p8 = adapterPorcentajeFinos.getPosition(cursor.getString(cursor.getColumnIndex("porcentajeFinosElevadora")));
                    p9 = adapterTipoCarga.getPosition(cursor.getString(cursor.getColumnIndex("tipoCarga")));
                    int p10 = adapterVariosPuntosAlimentacion.getPosition(cursor.getString(cursor.getColumnIndex("variosPuntosDeAlimentacion")));
                    int p11 = adapterTipoDescarga.getPosition(cursor.getString(cursor.getColumnIndex("tipoDescarga")));
                    int p12 = adapterLluviaMaterial.getPosition(cursor.getString(cursor.getColumnIndex("lluviaDeMaterial")));
                    int p13 = adapterTempMaterial.getPosition(cursor.getString(cursor.getColumnIndex("tempMaxMaterialSobreBandaElevadora")));
                    int p14 = adapterTempMaterial.getPosition(cursor.getString(cursor.getColumnIndex("tempPromedioMaterialSobreBandaElevadora")));
                    int p15 = adapterTempAmbienteMin.getPosition(cursor.getString(cursor.getColumnIndex("tempAmbienteMin")));
                    int p16 = adapterTempMaxAmbiente.getPosition(cursor.getString(cursor.getColumnIndex("tempAmbienteMax")));
                    int p17 = adapterMaxGranulometria.getPosition(cursor.getString(cursor.getColumnIndex("maxGranulometriaElevadora")));

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

                break;

            case "Banda":

                    db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) == null || cursor.getString(i).equals("null") || cursor.getString(i).equals("null")) {
                            db.execSQL("UPDATE bandaElevadora set " + cursor.getColumnName(i) + "=" + "''");
                        }
                    }
                    cursor = db.rawQuery("Select * from bandaElevadora where idRegistro=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax(), null);
                    cursor.moveToFirst();

                    TextInputEditText txtAnchoBanda = dialogParte.findViewById(R.id.txtAnchoBandaElevadora);
                    TextInputEditText txtEspesorTotal = dialogParte.findViewById(R.id.txtEpesorTotalElevadora);
                    TextInputEditText txtEspCubSup = dialogParte.findViewById(R.id.txtCubSupElevadora);
                    TextInputEditText txtEspCubInf = dialogParte.findViewById(R.id.txtCubInfElevadora);
                    TextInputEditText txtEspCojin = dialogParte.findViewById(R.id.txtEspesorCojin);
                    TextInputEditText txtVelocidadBanda = dialogParte.findViewById(R.id.txtVelocidadBandaElevadora);
                    TextInputEditText txtAnchoBandaAnterior = dialogParte.findViewById(R.id.txtAnchoBandaElevadoraAnterior);
                    TextInputEditText txtEspesorTotalAnterior = dialogParte.findViewById(R.id.txtEpesorTotalElevadoraAnterior);
                    TextInputEditText txtEspCubSupAnterior = dialogParte.findViewById(R.id.txtCubSupElevadoraAnterior);
                    TextInputEditText txtEspCubInfAnterior = dialogParte.findViewById(R.id.txtCubInfElevadoraAnterior);
                    TextInputEditText txtEspCojinAnterior = dialogParte.findViewById(R.id.txtEspesorCojinAnterior);
                    TextInputEditText txtVelocidadBandaAnterior = dialogParte.findViewById(R.id.txtVelocidadBandaElevadoraAnterior);
                    TextInputEditText txtCausaFallaBandaAnterior = dialogParte.findViewById(R.id.txtCausaFallaBandaAnterior);
                    TextInputEditText txtDistanciaEntrePoleas = dialogParte.findViewById(R.id.txtDistanciaEntrePoleas);
                    TextInputEditText txtTonsTransportadasAnterior = dialogParte.findViewById(R.id.txtTonsTransportadasBandaAnterior);


                    AutoCompleteTextView spinnerMarcaBanda = dialogParte.findViewById(R.id.spinnerMarcaBandaElevadora);
                    Spinner spinnerNoLonas = dialogParte.findViewById(R.id.spinnerNoLonasElevadora);
                    Spinner spinnerTipoLona = dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadora);
                    Spinner spinnerTipoCubierta = dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadora);
                    Spinner spinnerTipoEmpalme = dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadora);
                    Spinner spinnerEstadoEmpalme = dialogParte.findViewById(R.id.spinnerEstadoEmpalmeElevadora);
                    Spinner spinnerResistenciaRotura = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadora);
                AutoCompleteTextView spinnerMarcaBandaAnterior = dialogParte.findViewById(R.id.spinnerMarcaBandaElevadoraAnterior);
                    Spinner spinnerNoLonasAnterior = dialogParte.findViewById(R.id.spinnerNoLonasElevadoraAnterior);
                    Spinner spinnerTipoLonaAnterior = dialogParte.findViewById(R.id.spinnerTipoDeLonaElevadoraAnterior);
                    Spinner spinnerTipoCubiertaAnterior = dialogParte.findViewById(R.id.spinnerTipoCubiertaElevadoraAnterior);
                    Spinner spinnerTipoEmpalmeAnterior = dialogParte.findViewById(R.id.spinnerTipoEmpalmeElevadoraAnterior);
                    Spinner spinnerResistenciaRoturaAnterior = dialogParte.findViewById(R.id.spinnerResistenciaRoturaLonaElevadoraAnterior);


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

                    p1 = adapterMarcaBanda.getPosition(cursor.getString(cursor.getColumnIndex("marcaBandaElevadora")));
                    p2 = adapterNoLonas.getPosition(cursor.getString(cursor.getColumnIndex("noLonaBandaElevadora")));
                    p3 = adapterTipoLonas.getPosition(cursor.getString(cursor.getColumnIndex("tipoLonaBandaElevadora")));
                    p4 = adapterTipoCubierta.getPosition(cursor.getString(cursor.getColumnIndex("tipoCubiertaElevadora")));
                    p5 = adapterTipoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("tipoEmpalmeElevadora")));
                    p6 = adapterEstadoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("estadoEmpalmeElevadora")));
                    p7 = adapterRoturaLona.getPosition(cursor.getString(cursor.getColumnIndex("resistenciaRoturaLonaElevadora")));


                    p8 = adapterMarcaBanda.getPosition(cursor.getString(cursor.getColumnIndex("marcaBandaElevadoraAnterior")));
                    p9 = adapterNoLonas.getPosition(cursor.getString(cursor.getColumnIndex("noLonasBandaElevadoraAnterior")));
                    p10 = adapterTipoLonas.getPosition(cursor.getString(cursor.getColumnIndex("tipoLonaBandaElevadoraAnterior")));
                    p11 = adapterTipoCubierta.getPosition(cursor.getString(cursor.getColumnIndex("tipoCubiertaElevadoraAnterior")));
                    p12 = adapterTipoEmpalme.getPosition(cursor.getString(cursor.getColumnIndex("tipoEmpalmeElevadoraAnterior")));

                    p14 = adapterRoturaLona.getPosition(cursor.getString(cursor.getColumnIndex("resistenciaRoturaBandaElevadoraAnterior")));


                    spinnerMarcaBanda.setText(cursor.getString(cursor.getColumnIndex("marcaBandaElevadora")));
                    spinnerMarcaBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("marcaBandaElevadoraAnterior")));
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


                    txtAnchoBanda.setText(cursor.getString(cursor.getColumnIndex("anchoBandaElevadora")));
                    txtEspesorTotal.setText(cursor.getString(cursor.getColumnIndex("espesorTotalBandaElevadora")));
                    txtEspCubSup.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaSuperiorElevadora")));
                    txtEspCubInf.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaInferiorElevadora")));
                    txtEspCojin.setText(cursor.getString(cursor.getColumnIndex("espesorCojinActualElevadora")));
                    txtVelocidadBanda.setText(cursor.getString(cursor.getColumnIndex("velocidadBandaElevadora")));
                    txtDistanciaEntrePoleas.setText(cursor.getString(cursor.getColumnIndex("distanciaEntrePoleasElevadora")));
                    txtAnchoBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("anchoBandaElevadoraAnterior")));
                    txtEspesorTotalAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorTotalBandaElevadoraAnterior")));
                    txtEspCubSupAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaSuperiorBandaElevadoraAnterior")));
                    txtEspCubInfAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCubiertaInferiorBandaElevadoraAnterior")));
                    txtEspCojinAnterior.setText(cursor.getString(cursor.getColumnIndex("espesorCojinElevadoraAnterior")));
                    txtVelocidadBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("velocidadBandaElevadoraAnterior")));
                    txtCausaFallaBandaAnterior.setText(cursor.getString(cursor.getColumnIndex("causaFallaCambioBandaElevadoraAnterior")));
                    txtTonsTransportadasAnterior.setText(cursor.getString(cursor.getColumnIndex("tonsTransportadasBandaElevadoraAnterior")));
 
                break;


        }


    }
}

