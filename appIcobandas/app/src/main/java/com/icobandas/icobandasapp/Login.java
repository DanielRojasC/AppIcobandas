package com.icobandas.icobandasapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.icobandas.icobandasapp.Modelos.CiudadesJson;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    TextInputEditText txtUsuario;
    TextInputEditText txtContraseña;
    Spinner spinnerPrueba;
    Button btnIniciarSesion;
    ProgressBar progressBarLogin;
    AlertDialog.Builder alerta;
    Editable usuario;
    Editable contra;
    RequestQueue queue;
    Gson gson = new Gson();
    DbHelper dbHelper;
    public static ArrayList<LoginJson> loginJsons = new ArrayList<>();

    public static Cursor cursor;

    public static String nombreUsuario;
    public static String rol;
    public static ArrayList<LoginTransportadores> loginTransportadores = new ArrayList<>();
    public static ArrayList<CiudadesJson> ciudadesJsons = new ArrayList<>();
    public static String nombreAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializar();


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try
                {

                    progressBarLogin.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    usuario = txtUsuario.getText();
                    contra = txtContraseña.getText();
                    if (usuario.toString().equals("")) {
                        txtUsuario.setError("Usuario no ingresado");
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLogin.setVisibility(View.INVISIBLE);
                    }
                    if (contra.toString().equals("")) {
                        txtContraseña.setError("Contraseña no ingresada");
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLogin.setVisibility(View.INVISIBLE);
                    } else if (!usuario.toString().equals("") && !contra.toString().equals("")) {

                        if (isOnline(getApplicationContext()))
                        {
                            if(usuario.toString().equals("lepe") && contra.toString().equals("lepe2019."))
                            {
                                rol="admin";
                                String url = Constants.url + "loginAdmin";
                                final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.equals("\uFEFF[]")) {
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBarLogin.setVisibility(View.INVISIBLE);
                                            alerta.setTitle("ICOBANDAS S.A dice:");
                                            alerta.setMessage("Credenciales de acesso incorrectas");
                                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    txtContraseña.setText("");
                                                    dialog.cancel();
                                                }
                                            });
                                            alerta.create();
                                            alerta.show();
                                        } else {
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                                            Type type = new TypeToken<List<LoginJson>>() {
                                            }.getType();
                                            loginJsons = gson.fromJson(response, type);

                                            String estadoAdmin=null;
                                            for (int i=0;i< loginJsons.size();i++)
                                            {
                                                if(loginJsons.get(i).getCodagente().equals("LEPE"))
                                                {
                                                    estadoAdmin=loginJsons.get(i).getEstadoagte();
                                                }
                                            }
                                            if(estadoAdmin.equals("0"))
                                            {
                                                db.execSQL("DELETE FROM usuario");
                                                db.execSQL("DELETE FROM clientes");
                                                db.execSQL("DELETE FROM plantas");
                                                db.execSQL("DELETE FROM registro");
                                                db.execSQL("DELETE FROM bandaTransmision");
                                                db.execSQL("DELETE FROM bandaTransportadora");
                                                db.execSQL("DELETE FROM bandaElevadora");

                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                progressBarLogin.setVisibility(View.INVISIBLE);
                                                alerta.setTitle("ICOBANDAS S.A dice:");
                                                alerta.setMessage("Usted no está autorizado para ingresar al sistema");
                                                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        txtContraseña.setText("");
                                                        txtUsuario.setText("");
                                                        dialog.cancel();
                                                    }
                                                });
                                                alerta.create();
                                                alerta.show();
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


                                                db.execSQL("insert into usuario values('LEPE','LEÓN PELÁEZ','cac2cdfa95fb1ee32713f5870318c8b5')");
                                                Cursor cursor1=db.rawQuery("select * from usuario",null);
                                                cursor1.moveToFirst();
                                                nombreAdmin=cursor1.getString(1);

                                                for (int i = 0; i < loginJsons.size(); i++) {
                                                    if (loginJsons.get(i).getNit() != null) {
                                                        db.execSQL("INSERT INTO clientes values('" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameunido() + "','Sincronizado')");
                                                    }
                                                    if(loginJsons.get(i).getCodplanta()!=null)
                                                    {
                                                        db.execSQL("INSERT INTO plantas values(" + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameplanta() + "',null,null,null)");

                                                    }

                                                    if (loginJsons.get(i).getIdRegistro() != null) {
                                                        db.execSQL("INSERT INTO registro values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getFechaRegistro() + "'," + loginJsons.get(i).getIdTransportador() + "," + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','Sincronizado')");
                                                    }
                                                }

                                                dbHelper.close();

                                                String url = Constants.url + "transportadoresAdmin";
                                                StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        Type type = new TypeToken<List<LoginTransportadores>>() {
                                                        }.getType();
                                                        loginTransportadores = gson.fromJson(response, type);

                                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                        db.execSQL("DELETE FROM transportador");

                                                        for (int i = 0; i < loginTransportadores.size(); i++) {
                                                            db.execSQL("INSERT INTO transportador values(" + loginTransportadores.get(i).getIdTransportador() + ",'" + loginTransportadores.get(i).getTipoTransportador() + "','" + loginTransportadores.get(i).getNombreTransportador() + "','" + loginTransportadores.get(i).getCodplanta() + "','" + loginTransportadores.get(i).getCaracteristicaTransportador() + "', 'Sincronizado')");

                                                        }
                                                        for (int i = 0; i < loginJsons.size(); i++) {
                                                            for (int x = 0; x < loginTransportadores.size(); x++) {
                                                                if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.DSF"))
                                                                {
                                                                    db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "','Sincronizado')");
                                                                }

                                                                else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E"))
                                                                {
                                                                    db.execSQL("INSERT INTO bandaElevadora values('"+loginJsons.get(i).getIdRegistro()+"', '"+loginJsons.get(i).getMarcaBandaElevadora()+"', '"+loginJsons.get(i).getAnchoBandaElevadora()+"', '"+loginJsons.get(i).getDistanciaEntrePoleasElevadora()+"', '"+loginJsons.get(i).getNoLonaBandaElevadora()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadora()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadora()+"', '"+loginJsons.get(i).getEspesorCojinActualElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorElevadora()+"', '"+loginJsons.get(i).getTipoCubiertaElevadora()+"', '"+loginJsons.get(i).getTipoEmpalmeElevadora()+"', '"+loginJsons.get(i).getEstadoEmpalmeElevadora()+"', '"+loginJsons.get(i).getResistenciaRoturaLonaElevadora()+"', '"+loginJsons.get(i).getVelocidadBandaElevadora()+"', '"+loginJsons.get(i).getMarcaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getAnchoBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getNoLonasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCojinElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getVelocidadBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getPesoMaterialEnCadaCangilon()+"', '"+loginJsons.get(i).getPesoCangilonVacio()+"', '"+loginJsons.get(i).getLongitudCangilon()+"', '"+loginJsons.get(i).getMaterialCangilon()+"', '"+loginJsons.get(i).getTipoCangilon()+"', '"+loginJsons.get(i).getProyeccionCangilon()+"','"+loginJsons.get(i).getProfundidadCangilon()+"' ,'"+loginJsons.get(i).getMarcaCangilon()+"', '"+loginJsons.get(i).getReferenciaCangilon()+"', '"+loginJsons.get(i).getCapacidadCangilon()+"', '"+loginJsons.get(i).getNoFilasCangilones()+"', '"+loginJsons.get(i).getSeparacionCangilones()+"', '"+loginJsons.get(i).getNoAgujeros()+"', '"+loginJsons.get(i).getDistanciaBordeBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaPosteriorBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura()+"', '"+loginJsons.get(i).getDistanciaBordesCangilonEstructura()+"', '"+loginJsons.get(i).getTipoVentilacion()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getLargoEjeMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getPotenciaMotorMotrizElevadora()+"', '"+loginJsons.get(i).getRpmSalidaReductorMotrizElevadora()+"', '"+loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroPoleaColaElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaColaElevadora()+"', '"+loginJsons.get(i).getTipoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLargoEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getDiametroEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora()+"', '"+loginJsons.get(i).getCargaTrabajoBandaElevadora()+"', '"+loginJsons.get(i).getTemperaturaMaterialElevadora()+"', '"+loginJsons.get(i).getEmpalmeMecanicoElevadora()+"', '"+loginJsons.get(i).getDiametroRoscaElevadora()+"', '"+loginJsons.get(i).getLargoTornilloElevadora()+"', '"+loginJsons.get(i).getMaterialTornilloElevadora()+"', '"+loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getMonitorPeligro()+"', '"+loginJsons.get(i).getRodamiento()+"', '"+loginJsons.get(i).getMonitorDesalineacion()+"', '"+loginJsons.get(i).getMonitorVelocidad()+"', '"+loginJsons.get(i).getSensorInductivo()+"', '"+loginJsons.get(i).getIndicadorNivel()+"', '"+loginJsons.get(i).getCajaUnion()+"', '"+loginJsons.get(i).getAlarmaYPantalla()+"', '"+loginJsons.get(i).getInterruptorSeguridad()+"', '"+loginJsons.get(i).getMaterialElevadora()+"', '"+loginJsons.get(i).getAtaqueQuimicoElevadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaElevadora()+"', '"+loginJsons.get(i).getAtaqueAceitesElevadora()+"', '"+loginJsons.get(i).getAtaqueAbrasivoElevadora()+"', '"+loginJsons.get(i).getCapacidadElevadora()+"', '"+loginJsons.get(i).getHorasTrabajoDiaElevadora()+"', '"+loginJsons.get(i).getDiasTrabajoSemanaElevadora()+"', '"+loginJsons.get(i).getAbrasividadElevadora()+"', '"+loginJsons.get(i).getPorcentajeFinosElevadora()+"', '"+loginJsons.get(i).getMaxGranulometriaElevadora()+"', '"+loginJsons.get(i).getDensidadMaterialElevadora()+"', '"+loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getVariosPuntosDeAlimentacion()+"', '"+loginJsons.get(i).getLluviaDeMaterial()+"', '"+loginJsons.get(i).getAnchoPiernaElevador()+"', '"+loginJsons.get(i).getProfundidadPiernaElevador()+"', '"+loginJsons.get(i).getTempAmbienteMin()+"', '"+loginJsons.get(i).getTempAmbienteMax()+"', '"+loginJsons.get(i).getTipoDescarga()+"', '"+loginJsons.get(i).getTipoCarga()+"','Sincronizado')");

                                                                }

                                                                else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T"))
                                                                {
                                                                    db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','"+loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda()+"' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "',"+
                                                                            "'"+loginJsons.get(i).getMaterial()+"', '"+loginJsons.get(i).getAnguloSobreCarga()+"', '"+loginJsons.get(i).getAtaqueQuimicoTransportadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaTransportadora()+"', '"+loginJsons.get(i).getAtaqueAceiteTransportadora()+"', '"+loginJsons.get(i).getAtaqueImpactoTransportadora()+"', '"+loginJsons.get(i).getCapacidadTransportadora()+"', '"+loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()+"', '"+loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()+"', '"+loginJsons.get(i).getAlimentacionCentradaTransportadora()+"', '"+loginJsons.get(i).getAbrasividadTransportadora()+"', '"+loginJsons.get(i).getPorcentajeFinosTransportadora()+"', '"+loginJsons.get(i).getMaxGranulometriaTransportadora()+"', '"+loginJsons.get(i).getMaxPesoTransportadora()+"', '"+loginJsons.get(i).getDensidadTransportadora()+"', '"+loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getCajaColaDeTolva()+"', '"+loginJsons.get(i).getFugaMateriales()+"', '"+loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()+"', '"+loginJsons.get(i).getFugaDeMaterialesPorLosCostados()+"', '"+loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()+"', '"+loginJsons.get(i).getAnchoChute()+"', '"+loginJsons.get(i).getLargoChute()+"', '"+loginJsons.get(i).getAlturaChute()+"', '"+loginJsons.get(i).getCauchoGuardabandas()+"', '"+loginJsons.get(i).getTriSealMultiSeal()+"', '"+loginJsons.get(i).getEspesorGuardaBandas()+"', '"+loginJsons.get(i).getAnchoGuardaBandas()+"', '"+loginJsons.get(i).getLargoGuardaBandas()+"', '"+loginJsons.get(i).getProtectorGuardaBandas()+"', '"+loginJsons.get(i).getCortinaAntiPolvo1()+"', '"+loginJsons.get(i).getCortinaAntiPolvo2()+"', '"+loginJsons.get(i).getCortinaAntiPolvo3()+"', '"+loginJsons.get(i).getBoquillasCanonesDeAire()+"', '"+loginJsons.get(i).getTempAmbienteMaxTransportadora()+"', '"+loginJsons.get(i).getTempAmbienteMinTransportadora()+"', '"+loginJsons.get(i).getTieneRodillosImpacto()+"', '"+loginJsons.get(i).getCamaImpacto()+"', '"+loginJsons.get(i).getCamaSellado()+"', '"+loginJsons.get(i).getBasculaPesaje()+"', '"+loginJsons.get(i).getRodilloCarga()+"', '"+loginJsons.get(i).getRodilloImpacto()+"', '"+loginJsons.get(i).getBasculaASGCO()+"', '"+loginJsons.get(i).getBarraImpacto()+"', '"+loginJsons.get(i).getBarraDeslizamiento()+"', '"+loginJsons.get(i).getEspesorUHMV()+"', '"+loginJsons.get(i).getAnchoBarra()+"', '"+loginJsons.get(i).getLargoBarra()+"','"+loginJsons.get(i).getAnguloAcanalamientoArtesa1()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz()+"', '"+loginJsons.get(i).getIntegridadSoportesRodilloImpacto()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreCortinas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEnBanda()+"', '"+loginJsons.get(i).getIntegridadSoportesCamaImpacto()+"', '"+loginJsons.get(i).getInclinacionZonaCargue()+"', '"+loginJsons.get(i).getSistemaAlineacionCarga()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnCarga()+"', '"+loginJsons.get(i).getSistemasAlineacionCargFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getSistemasAlineacionRetornoFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoPlano()+"', '"+loginJsons.get(i).getSistemaAlineacionArtesaCarga()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoEnV()+"', '"+loginJsons.get(i).getLargoEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroRodilloLateralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloLateralCarga()+"', '"+loginJsons.get(i).getTipoRodilloCarga()+"', '"+loginJsons.get(i).getDistanciaEntreArtesasCarga()+"', '"+loginJsons.get(i).getAnchoInternoChasisRodilloCarga()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()+"', '"+loginJsons.get(i).getDetalleRodilloCentralCarga()+"', '"+loginJsons.get(i).getDetalleRodilloLateralCarg()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getLargoEjePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizTransportadora()+"', '"+loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getPotenciaMotorTransportadora()+"', '"+loginJsons.get(i).getGuardaPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoEstructura()+"', '"+loginJsons.get(i).getAnchoTrayectoCarga()+"', '"+loginJsons.get(i).getPasarelaRespectoAvanceBanda()+"', '"+loginJsons.get(i).getMaterialAlimenticioTransportadora()+"', '"+loginJsons.get(i).getMaterialAcidoTransportadora()+"', '"+loginJsons.get(i).getMaterialTempEntre80y150Transportadora()+"', '"+loginJsons.get(i).getMaterialSecoTransportadora()+"', '"+loginJsons.get(i).getMaterialHumedoTransportadora()+"', '"+loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()+"', '"+loginJsons.get(i).getMaterialPegajosoTransportadora()+"', '"+loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()+"', '"+loginJsons.get(i).getMarcaLimpiadorPrimario()+"'           ,'"+loginJsons.get(i).getReferenciaLimpiadorPrimario()+"' ,'"+loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getAltoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTensorLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorPrimario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla()+"','"+loginJsons.get(i).getCuchillaEnContactoConBanda()+"' , '"+loginJsons.get(i).getMarcaLimpiadorSecundario()+"','"+loginJsons.get(i).getReferenciaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorSecundario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorSecundario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla1()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda1()+"', '"+loginJsons.get(i).getSistemaDribbleChute()+"','"+loginJsons.get(i).getMarcaLimpiadorTerciario()+"', '"+loginJsons.get(i).getReferenciaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getFrecuenciaRevisionCuchilla2()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda2()+"', '"+loginJsons.get(i).getEstadoRodilloRetorno()+"', '"+loginJsons.get(i).getLargoEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroRodilloRetorno()+"', '"+loginJsons.get(i).getLargoTuboRodilloRetorno()+"', '"+loginJsons.get(i).getTipoRodilloRetorno()+"', '"+loginJsons.get(i).getDistanciaEntreRodillosRetorno()+"', '"+loginJsons.get(i).getAnchoInternoChasisRetorno()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getDetalleRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDimetroPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroPoleaTensora()+"', '"+loginJsons.get(i).getAnchoPoleaTensora()+"', '"+loginJsons.get(i).getTipoPoleaTensora()+"', '"+loginJsons.get(i).getLargoEjePoleaTensora()+"', '"+loginJsons.get(i).getDiametroEjePoleaTensora()+"', '"+loginJsons.get(i).getIcobandasCentradaEnPoleaTensora()+"', '"+loginJsons.get(i).getRecorridoPoleaTensora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaTensora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaTensora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaColaTensora()+"', '"+loginJsons.get(i).getPotenciaMotorPoleaTensora()+"', '"+loginJsons.get(i).getGuardaPoleaTensora()+"', '"+loginJsons.get(i).getPuertasInspeccion()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoPlano()+"', '"+loginJsons.get(i).getGuardaTruTrainer()+"', '"+loginJsons.get(i).getGuardaPoleaDeflectora()+"', '"+loginJsons.get(i).getGuardaZonaDeTransito()+"', '"+loginJsons.get(i).getGuardaMotores()+"', '"+loginJsons.get(i).getGuardaCadenas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getInterruptoresDeSeguridad()+"', '"+loginJsons.get(i).getSirenasDeSeguridad()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoV()+"', '"+loginJsons.get(i).getDiametroRodilloCentralCarga()+"', '"+loginJsons.get(i).getTipoRodilloImpacto()+"', '"+loginJsons.get(i).getIntegridadSoporteCamaSellado()+"', '"+loginJsons.get(i).getAtaqueAbrasivoTransportadora()+"','Sincronizado')");
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
                                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                                db.execSQL("DELETE FROM ciudades");

                                                                for (int i = 0; i < ciudadesJsons.size(); i++) {
                                                                    db.execSQL("INSERT INTO ciudades values('" + ciudadesJsons.get(i).getCodpoblado() + "','" + ciudadesJsons.get(i).getUnido() + "')");
                                                                }

                                                                db.close();
                                                                nombreUsuario="LEPE";

                                                                startActivity(new Intent(Login.this, MainActivity.class));
                                                                finish();

                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                progressBarLogin.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                                        queue.add(requestCiudades);
                                                        requestCiudades.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                        MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                        progressBarLogin.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                                requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestTransportadores);
                                            }

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressBarLogin.setVisibility(View.INVISIBLE);

                                        MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    }
                                });

                                requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestLogin);
                            }
                            else
                            {
                                rol="agente";
                                String url = Constants.url + "login/" + usuario + "&" + encriptar(contra.toString());
                                final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.equals("\uFEFF[]")) {
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBarLogin.setVisibility(View.INVISIBLE);
                                            alerta.setTitle("ICOBANDAS S.A dice:");
                                            alerta.setMessage("Credenciales de acesso incorrectas");
                                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    txtContraseña.setText("");
                                                    dialog.cancel();
                                                }
                                            });
                                            alerta.create();
                                            alerta.show();
                                        } else {
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

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

                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                progressBarLogin.setVisibility(View.INVISIBLE);
                                                alerta.setTitle("ICOBANDAS S.A dice:");
                                                alerta.setMessage("Usted no está autorizado para ingresar al sistema");
                                                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        txtContraseña.setText("");
                                                        txtUsuario.setText("");
                                                        dialog.cancel();
                                                    }
                                                });
                                                alerta.create();
                                                alerta.show();
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


                                                db.execSQL("insert into usuario values('" + Login.loginJsons.get(0).getCodagente() + "','" + loginJsons.get(0).getNombreagte() + "','" + loginJsons.get(0).getPwdagente() + "')");
                                                Cursor cursor1= db.rawQuery("SELECT * FROM usuario where nombreUsuario ='"+usuario.toString().toUpperCase()+"' and contraseñaUsuario='"+encriptar(contra.toString())+"'", null);
                                                cursor1.moveToFirst();
                                                nombreAdmin=cursor1.getString(1);
                                                nombreUsuario=cursor1.getString(0);

                                                for (int i = 0; i < loginJsons.size(); i++) {
                                                    if (loginJsons.get(i).getNit() != null) {
                                                        db.execSQL("INSERT INTO clientes values('" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameunido() + "','Sincronizado')");
                                                    }

                                                    db.execSQL("INSERT INTO plantas values(" + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameplanta() + "',null,null,null)");

                                                    if (loginJsons.get(i).getIdRegistro() != null) {
                                                        db.execSQL("INSERT INTO registro values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getFechaRegistro() + "'," + loginJsons.get(i).getIdTransportador() + "," + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','Sincronizado')");
                                                    }
                                                }

                                                dbHelper.close();

                                                String url = Constants.url + "transportadores/" + usuario;
                                                StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        Type type = new TypeToken<List<LoginTransportadores>>() {
                                                        }.getType();
                                                        loginTransportadores = gson.fromJson(response, type);

                                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                        db.execSQL("DELETE FROM transportador");

                                                        for (int i = 0; i < loginTransportadores.size(); i++) {
                                                            db.execSQL("INSERT INTO transportador values(" + loginTransportadores.get(i).getIdTransportador() + ",'" + loginTransportadores.get(i).getTipoTransportador() + "','" + loginTransportadores.get(i).getNombreTransportador() + "','" + loginTransportadores.get(i).getCodplanta() + "','" + loginTransportadores.get(i).getCaracteristicaTransportador() + "', 'Sincronizado')");

                                                        }
                                                        for (int i = 0; i < loginJsons.size(); i++) {
                                                            for (int x = 0; x < loginTransportadores.size(); x++) {
                                                                if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.DSF"))
                                                                {
                                                                    db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "','Sincronizado')");
                                                                }

                                                                else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E"))
                                                                {
                                                                    db.execSQL("INSERT INTO bandaElevadora values('"+loginJsons.get(i).getIdRegistro()+"', '"+loginJsons.get(i).getMarcaBandaElevadora()+"', '"+loginJsons.get(i).getAnchoBandaElevadora()+"', '"+loginJsons.get(i).getDistanciaEntrePoleasElevadora()+"', '"+loginJsons.get(i).getNoLonaBandaElevadora()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadora()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadora()+"', '"+loginJsons.get(i).getEspesorCojinActualElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorElevadora()+"', '"+loginJsons.get(i).getTipoCubiertaElevadora()+"', '"+loginJsons.get(i).getTipoEmpalmeElevadora()+"', '"+loginJsons.get(i).getEstadoEmpalmeElevadora()+"', '"+loginJsons.get(i).getResistenciaRoturaLonaElevadora()+"', '"+loginJsons.get(i).getVelocidadBandaElevadora()+"', '"+loginJsons.get(i).getMarcaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getAnchoBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getNoLonasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCojinElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getVelocidadBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getPesoMaterialEnCadaCangilon()+"', '"+loginJsons.get(i).getPesoCangilonVacio()+"', '"+loginJsons.get(i).getLongitudCangilon()+"', '"+loginJsons.get(i).getMaterialCangilon()+"', '"+loginJsons.get(i).getTipoCangilon()+"', '"+loginJsons.get(i).getProyeccionCangilon()+"','"+loginJsons.get(i).getProfundidadCangilon()+"' ,'"+loginJsons.get(i).getMarcaCangilon()+"', '"+loginJsons.get(i).getReferenciaCangilon()+"', '"+loginJsons.get(i).getCapacidadCangilon()+"', '"+loginJsons.get(i).getNoFilasCangilones()+"', '"+loginJsons.get(i).getSeparacionCangilones()+"', '"+loginJsons.get(i).getNoAgujeros()+"', '"+loginJsons.get(i).getDistanciaBordeBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaPosteriorBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura()+"', '"+loginJsons.get(i).getDistanciaBordesCangilonEstructura()+"', '"+loginJsons.get(i).getTipoVentilacion()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getLargoEjeMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getPotenciaMotorMotrizElevadora()+"', '"+loginJsons.get(i).getRpmSalidaReductorMotrizElevadora()+"', '"+loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroPoleaColaElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaColaElevadora()+"', '"+loginJsons.get(i).getTipoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLargoEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getDiametroEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora()+"', '"+loginJsons.get(i).getCargaTrabajoBandaElevadora()+"', '"+loginJsons.get(i).getTemperaturaMaterialElevadora()+"', '"+loginJsons.get(i).getEmpalmeMecanicoElevadora()+"', '"+loginJsons.get(i).getDiametroRoscaElevadora()+"', '"+loginJsons.get(i).getLargoTornilloElevadora()+"', '"+loginJsons.get(i).getMaterialTornilloElevadora()+"', '"+loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getMonitorPeligro()+"', '"+loginJsons.get(i).getRodamiento()+"', '"+loginJsons.get(i).getMonitorDesalineacion()+"', '"+loginJsons.get(i).getMonitorVelocidad()+"', '"+loginJsons.get(i).getSensorInductivo()+"', '"+loginJsons.get(i).getIndicadorNivel()+"', '"+loginJsons.get(i).getCajaUnion()+"', '"+loginJsons.get(i).getAlarmaYPantalla()+"', '"+loginJsons.get(i).getInterruptorSeguridad()+"', '"+loginJsons.get(i).getMaterialElevadora()+"', '"+loginJsons.get(i).getAtaqueQuimicoElevadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaElevadora()+"', '"+loginJsons.get(i).getAtaqueAceitesElevadora()+"', '"+loginJsons.get(i).getAtaqueAbrasivoElevadora()+"', '"+loginJsons.get(i).getCapacidadElevadora()+"', '"+loginJsons.get(i).getHorasTrabajoDiaElevadora()+"', '"+loginJsons.get(i).getDiasTrabajoSemanaElevadora()+"', '"+loginJsons.get(i).getAbrasividadElevadora()+"', '"+loginJsons.get(i).getPorcentajeFinosElevadora()+"', '"+loginJsons.get(i).getMaxGranulometriaElevadora()+"', '"+loginJsons.get(i).getDensidadMaterialElevadora()+"', '"+loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getVariosPuntosDeAlimentacion()+"', '"+loginJsons.get(i).getLluviaDeMaterial()+"', '"+loginJsons.get(i).getAnchoPiernaElevador()+"', '"+loginJsons.get(i).getProfundidadPiernaElevador()+"', '"+loginJsons.get(i).getTempAmbienteMin()+"', '"+loginJsons.get(i).getTempAmbienteMax()+"', '"+loginJsons.get(i).getTipoDescarga()+"', '"+loginJsons.get(i).getTipoCarga()+"','Sincronizado')");

                                                                }

                                                                else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T"))
                                                                {
                                                                    db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','"+loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda()+"' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "',"+
                                                                            "'"+loginJsons.get(i).getMaterial()+"', '"+loginJsons.get(i).getAnguloSobreCarga()+"', '"+loginJsons.get(i).getAtaqueQuimicoTransportadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaTransportadora()+"', '"+loginJsons.get(i).getAtaqueAceiteTransportadora()+"', '"+loginJsons.get(i).getAtaqueImpactoTransportadora()+"', '"+loginJsons.get(i).getCapacidadTransportadora()+"', '"+loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()+"', '"+loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()+"', '"+loginJsons.get(i).getAlimentacionCentradaTransportadora()+"', '"+loginJsons.get(i).getAbrasividadTransportadora()+"', '"+loginJsons.get(i).getPorcentajeFinosTransportadora()+"', '"+loginJsons.get(i).getMaxGranulometriaTransportadora()+"', '"+loginJsons.get(i).getMaxPesoTransportadora()+"', '"+loginJsons.get(i).getDensidadTransportadora()+"', '"+loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getCajaColaDeTolva()+"', '"+loginJsons.get(i).getFugaMateriales()+"', '"+loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()+"', '"+loginJsons.get(i).getFugaDeMaterialesPorLosCostados()+"', '"+loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()+"', '"+loginJsons.get(i).getAnchoChute()+"', '"+loginJsons.get(i).getLargoChute()+"', '"+loginJsons.get(i).getAlturaChute()+"', '"+loginJsons.get(i).getCauchoGuardabandas()+"', '"+loginJsons.get(i).getTriSealMultiSeal()+"', '"+loginJsons.get(i).getEspesorGuardaBandas()+"', '"+loginJsons.get(i).getAnchoGuardaBandas()+"', '"+loginJsons.get(i).getLargoGuardaBandas()+"', '"+loginJsons.get(i).getProtectorGuardaBandas()+"', '"+loginJsons.get(i).getCortinaAntiPolvo1()+"', '"+loginJsons.get(i).getCortinaAntiPolvo2()+"', '"+loginJsons.get(i).getCortinaAntiPolvo3()+"', '"+loginJsons.get(i).getBoquillasCanonesDeAire()+"', '"+loginJsons.get(i).getTempAmbienteMaxTransportadora()+"', '"+loginJsons.get(i).getTempAmbienteMinTransportadora()+"', '"+loginJsons.get(i).getTieneRodillosImpacto()+"', '"+loginJsons.get(i).getCamaImpacto()+"', '"+loginJsons.get(i).getCamaSellado()+"', '"+loginJsons.get(i).getBasculaPesaje()+"', '"+loginJsons.get(i).getRodilloCarga()+"', '"+loginJsons.get(i).getRodilloImpacto()+"', '"+loginJsons.get(i).getBasculaASGCO()+"', '"+loginJsons.get(i).getBarraImpacto()+"', '"+loginJsons.get(i).getBarraDeslizamiento()+"', '"+loginJsons.get(i).getEspesorUHMV()+"', '"+loginJsons.get(i).getAnchoBarra()+"', '"+loginJsons.get(i).getLargoBarra()+"','"+loginJsons.get(i).getAnguloAcanalamientoArtesa1()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz()+"', '"+loginJsons.get(i).getIntegridadSoportesRodilloImpacto()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreCortinas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEnBanda()+"', '"+loginJsons.get(i).getIntegridadSoportesCamaImpacto()+"', '"+loginJsons.get(i).getInclinacionZonaCargue()+"', '"+loginJsons.get(i).getSistemaAlineacionCarga()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnCarga()+"', '"+loginJsons.get(i).getSistemasAlineacionCargFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getSistemasAlineacionRetornoFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoPlano()+"', '"+loginJsons.get(i).getSistemaAlineacionArtesaCarga()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoEnV()+"', '"+loginJsons.get(i).getLargoEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroRodilloLateralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloLateralCarga()+"', '"+loginJsons.get(i).getTipoRodilloCarga()+"', '"+loginJsons.get(i).getDistanciaEntreArtesasCarga()+"', '"+loginJsons.get(i).getAnchoInternoChasisRodilloCarga()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()+"', '"+loginJsons.get(i).getDetalleRodilloCentralCarga()+"', '"+loginJsons.get(i).getDetalleRodilloLateralCarg()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getLargoEjePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizTransportadora()+"', '"+loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getPotenciaMotorTransportadora()+"', '"+loginJsons.get(i).getGuardaPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoEstructura()+"', '"+loginJsons.get(i).getAnchoTrayectoCarga()+"', '"+loginJsons.get(i).getPasarelaRespectoAvanceBanda()+"', '"+loginJsons.get(i).getMaterialAlimenticioTransportadora()+"', '"+loginJsons.get(i).getMaterialAcidoTransportadora()+"', '"+loginJsons.get(i).getMaterialTempEntre80y150Transportadora()+"', '"+loginJsons.get(i).getMaterialSecoTransportadora()+"', '"+loginJsons.get(i).getMaterialHumedoTransportadora()+"', '"+loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()+"', '"+loginJsons.get(i).getMaterialPegajosoTransportadora()+"', '"+loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()+"', '"+loginJsons.get(i).getMarcaLimpiadorPrimario()+"'           ,'"+loginJsons.get(i).getReferenciaLimpiadorPrimario()+"' ,'"+loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getAltoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTensorLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorPrimario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla()+"','"+loginJsons.get(i).getCuchillaEnContactoConBanda()+"' , '"+loginJsons.get(i).getMarcaLimpiadorSecundario()+"','"+loginJsons.get(i).getReferenciaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorSecundario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorSecundario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla1()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda1()+"', '"+loginJsons.get(i).getSistemaDribbleChute()+"','"+loginJsons.get(i).getMarcaLimpiadorTerciario()+"', '"+loginJsons.get(i).getReferenciaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getFrecuenciaRevisionCuchilla2()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda2()+"', '"+loginJsons.get(i).getEstadoRodilloRetorno()+"', '"+loginJsons.get(i).getLargoEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroRodilloRetorno()+"', '"+loginJsons.get(i).getLargoTuboRodilloRetorno()+"', '"+loginJsons.get(i).getTipoRodilloRetorno()+"', '"+loginJsons.get(i).getDistanciaEntreRodillosRetorno()+"', '"+loginJsons.get(i).getAnchoInternoChasisRetorno()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getDetalleRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDimetroPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroPoleaTensora()+"', '"+loginJsons.get(i).getAnchoPoleaTensora()+"', '"+loginJsons.get(i).getTipoPoleaTensora()+"', '"+loginJsons.get(i).getLargoEjePoleaTensora()+"', '"+loginJsons.get(i).getDiametroEjePoleaTensora()+"', '"+loginJsons.get(i).getIcobandasCentradaEnPoleaTensora()+"', '"+loginJsons.get(i).getRecorridoPoleaTensora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaTensora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaTensora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaColaTensora()+"', '"+loginJsons.get(i).getPotenciaMotorPoleaTensora()+"', '"+loginJsons.get(i).getGuardaPoleaTensora()+"', '"+loginJsons.get(i).getPuertasInspeccion()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoPlano()+"', '"+loginJsons.get(i).getGuardaTruTrainer()+"', '"+loginJsons.get(i).getGuardaPoleaDeflectora()+"', '"+loginJsons.get(i).getGuardaZonaDeTransito()+"', '"+loginJsons.get(i).getGuardaMotores()+"', '"+loginJsons.get(i).getGuardaCadenas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getInterruptoresDeSeguridad()+"', '"+loginJsons.get(i).getSirenasDeSeguridad()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoV()+"', '"+loginJsons.get(i).getDiametroRodilloCentralCarga()+"', '"+loginJsons.get(i).getTipoRodilloImpacto()+"', '"+loginJsons.get(i).getIntegridadSoporteCamaSellado()+"', '"+loginJsons.get(i).getAtaqueAbrasivoTransportadora()+"','Sincronizado')");
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
                                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                                db.execSQL("DELETE FROM ciudades");

                                                                for (int i = 0; i < ciudadesJsons.size(); i++) {
                                                                    db.execSQL("INSERT INTO ciudades values('" + ciudadesJsons.get(i).getCodpoblado() + "','" + ciudadesJsons.get(i).getUnido() + "')");
                                                                }

                                                                db.close();
                                                                nombreUsuario=loginJsons.get(0).getCodagente();

                                                                startActivity(new Intent(Login.this, MainActivity.class));
                                                                finish();

                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                progressBarLogin.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                                        queue.add(requestCiudades);
                                                        requestCiudades.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                        MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                        progressBarLogin.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                                requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                queue.add(requestTransportadores);
                                            }

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressBarLogin.setVisibility(View.INVISIBLE);

                                        MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    }
                                });

                                requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestLogin);
                            }

                        }

                        else
                        {
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            try
                            {
                                String contraEnc=encriptar(contra.toString());
                                cursor= db.rawQuery("SELECT * FROM usuario where nombreUsuario ='"+usuario.toString().toUpperCase()+"' and contraseñaUsuario='"+contraEnc+"'", null);
                                cursor.moveToFirst();
                                if(cursor.getCount()==0)
                                {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    progressBarLogin.setVisibility(View.INVISIBLE);
                                    alerta.setTitle("ICOBANDAS S.A dice:");
                                    alerta.setMessage("Credenciales de acceso incorrectas");
                                    alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            txtContraseña.setText("");
                                            dialog.cancel();
                                        }
                                    });
                                    alerta.create();
                                    alerta.show();
                                }
                                else
                                {

                                    nombreUsuario=cursor.getString(0);

                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void inicializar() {


        txtContraseña = findViewById(R.id.txtContraseña);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        progressBarLogin = findViewById(R.id.progresBarLogin);
        alerta = new AlertDialog.Builder(this);
        queue = Volley.newRequestQueue(this);

        dbHelper = new DbHelper(getApplicationContext(), "prueba", null, 1);


    }

    public static String encriptar(String contras) {
        return new String((Hex.encodeHex(DigestUtils.md5(contras))));
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
