package com.icobandas.icobandasapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Database.DBHelper;
import com.icobandas.icobandasapp.Modelos.CiudadesJson;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.icobandas.icobandasapp.Utilities.Utilities;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText txtUsuario;
    EditText txtContraseña;
    Spinner spinnerPrueba;
    Button btnIniciarSesion;
    ProgressBar progressBarLogin;
    AlertDialog.Builder alerta;
    Editable usuario;
    Editable contra;
    RequestQueue queue;
    Gson gson = new Gson();
    DBHelper dbHelper;
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
                                            dialog.cancel();
                                        }
                                    });
                                    alerta.create();
                                    alerta.show();
                                } else {
                                    Type type = new TypeToken<List<LoginJson>>() {
                                    }.getType();
                                    loginJsons = gson.fromJson(response, type);
                                    final SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    db.execSQL("DELETE FROM "+Utilities.TABLA_USUARIOS);
                                    db.execSQL("DELETE FROM "+Utilities.TABLA_CLIENTES);
                                    db.execSQL("DELETE FROM "+Utilities.TABLA_PLANTAS);

                                    ContentValues contentValues=new ContentValues();
                                    contentValues.put(Utilities.NOMBRE_USUARIO,loginJsons.get(0).getNombreUsuario());
                                    contentValues.put(Utilities.NOMBRE_VENDEDOR,loginJsons.get(0).getNombreVendedor());
                                    contentValues.put(Utilities.PWD_USUARIO,loginJsons.get(0).getContraseñaUsuario());
                                    db.insert(Utilities.TABLA_USUARIOS,Utilities.NOMBRE_USUARIO, contentValues);

                                    for(int i=0;i<loginJsons.size();i++)
                                    {
                                        ContentValues values=new ContentValues();
                                        values.put(Utilities.NIT_CLIENTE, loginJsons.get(i).getNit());
                                        values.put(Utilities.NOMBRE_CLIENTE, loginJsons.get(i).getNameunido());
                                        db.insert(Utilities.TABLA_CLIENTES, null, values);
                                    }

                                    for(int i=0;i<loginJsons.size();i++)
                                    {
                                        ContentValues values=new ContentValues();
                                        values.put(Utilities.ID_PLANTAS, loginJsons.get(i).getCodplanta());
                                        values.put(Utilities.NOMBRE_PLANTAS, loginJsons.get(i).getNameplanta());
                                        values.put(Utilities.NIT_CLIENTE_PLANTA, loginJsons.get(i).getNit());


                                        db.insert(Utilities.TABLA_PLANTAS,null,values);

                                    }

                                    for(int i=0;i<loginJsons.size();i++)
                                    {
                                        ContentValues values=new ContentValues();
                                        values.put(Utilities.ID_REGISTRO, loginJsons.get(i).getIdRegistro());
                                        values.put(Utilities.FECHA_REGISTRO, loginJsons.get(i).getFechaRegistro());
                                        values.put(Utilities.ID_TRANSPORTADOR_REGISTRO, loginJsons.get(i).getIdTransportador());
                                        values.put(Utilities.COD_PLANTA_REGISTRO, loginJsons.get(i).getCodplanta());
                                        values.put(Utilities.USUARIO_REGISTRO, loginJsons.get(i).getNombreUsuario());

                                        db.insert(Utilities.TABLA_REGISTRO,null,values);
                                    }

                                    db.close();


                                    String url = Constants.url + "transportadores/" + usuario;
                                    StringRequest requestTransportadores = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            Type type = new TypeToken<List<LoginTransportadores>>() {
                                            }.getType();
                                            loginTransportadores = gson.fromJson(response, type);

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                                            db.execSQL("DELETE FROM "+Utilities.TABLA_TRANSPORTADOR);

                                            for (int i=0;i<loginTransportadores.size();i++)
                                            {
                                                ContentValues values= new ContentValues();
                                                values.put(Utilities.ID_TRANSPORTADOR,loginTransportadores.get(i).getIdTransportador());
                                                values.put(Utilities.NOMBRE_TRANSPORTADOR,loginTransportadores.get(i).getNombreTransportador());
                                                values.put(Utilities.TIPO_TRANSPORTADOR,loginTransportadores.get(i).getTipoTransportador());
                                                values.put(Utilities.COD_PLANTA,loginTransportadores.get(i).getCodplanta());
                                                values.put(Utilities.CARACTERISTICA_TRANSPORTADORA,loginTransportadores.get(i).getCaracteristicaTransportador());
                                                db.insert(Utilities.TABLA_TRANSPORTADOR,null,values);
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
                                                    db.execSQL("DELETE FROM "+Utilities.TABLA_CIUDAD);

                                                    for(int i=0; i<ciudadesJsons.size();i++)
                                                    {
                                                        ContentValues values= new ContentValues();

                                                        values.put(Utilities.COD_CIUDAD, ciudadesJsons.get(i).getCodpoblado());
                                                        values.put(Utilities.NOMBRE_CIUDAD, ciudadesJsons.get(i).getUnido());
                                                        db.insert(Utilities.TABLA_CIUDAD, null, values);
                                                    }

                                                    db.close();

                                                    startActivity(new Intent(Login.this, MainActivity.class));
                                                    finish();

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    progressBarLogin.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                            queue.add(requestCiudades);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(requestLogin);
                    }
                    else
                    {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String[] parametros={usuario.toString().toUpperCase(), encriptar(contra.toString())};
                        String[] campos={Utilities.NOMBRE_USUARIO, Utilities.PWD_USUARIO, Utilities.NOMBRE_VENDEDOR};

                        try{
                            cursor=db.query(Utilities.TABLA_USUARIOS, campos, Utilities.NOMBRE_USUARIO+"=? AND "+ Utilities.PWD_USUARIO+"=?",parametros,null,null,null);
                            cursor.moveToFirst();

                            if(cursor.getString(0).equals(usuario.toString().toUpperCase()) && cursor.getString(1).equals(encriptar(contra.toString())))
                            {


                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            alerta.setTitle("ICOBANDAS S.A dice:");
                            alerta.setMessage("Credenciales de acesso incorrectas");
                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alerta.create();
                            alerta.show();

                        }
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
        dbHelper= new DBHelper(getApplicationContext(), "prueba", null, 1);
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
