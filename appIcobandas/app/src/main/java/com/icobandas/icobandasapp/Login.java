package com.icobandas.icobandasapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    public static ArrayList<LoginJson> loginJsons = new ArrayList<>();

    public static Cursor cursor;

    public static ArrayList<LoginTransportadores> loginTransportadores = new ArrayList<>();
    public static ArrayList<CiudadesJson> ciudadesJsons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializar();


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)
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

                    if(isOnline(getApplicationContext()))
                    {
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
                                    Type type = new TypeToken<List<LoginJson>>() {
                                    }.getType();
                                    loginJsons = gson.fromJson(response, type);



                                    String url = Constants.url + "transportadores/" + usuario;
                                    StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            Type type = new TypeToken<List<LoginTransportadores>>() {
                                            }.getType();
                                            loginTransportadores = gson.fromJson(response, type);



                                            String url = Constants.url + "ciudades";
                                            StringRequest requestCiudades = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Type type = new TypeToken<List<CiudadesJson>>() {
                                                    }.getType();
                                                    ciudadesJsons = gson.fromJson(response, type);

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
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            MDToast.makeText(Login.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBarLogin.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    queue.add(requestTransportadores);
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
                        queue.add(requestLogin);
                    }

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
