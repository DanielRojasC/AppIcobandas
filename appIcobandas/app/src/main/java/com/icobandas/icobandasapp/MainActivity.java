package com.icobandas.icobandasapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.icobandas.icobandasapp.Entities.BandaElevadoraEntities;
import com.icobandas.icobandasapp.Entities.BandaPesada;
import com.icobandas.icobandasapp.Entities.BandaTransportadoraEntities;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Entities.PlantasEntities;
import com.icobandas.icobandasapp.Entities.RegistroEntities;
import com.icobandas.icobandasapp.Entities.TransportadorEntities;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.icobandas.icobandasapp.Modelos.LoginTransportadores;
import com.icobandas.icobandasapp.Modelos.ResponseRegister;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.icobandas.icobandasapp.Login.loginJsons;
import static com.icobandas.icobandasapp.Login.loginTransportadores;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue queue;

    public static Context context;

    DbHelper dbHelper;
    Cursor cursor;
    ImageButton btnSync;
    Gson gson = new Gson();

    public static FragmentManager fragmentManager;
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    int sincronizacion;
    int conteo;
    ArrayList<String> registros = new ArrayList<>();
    public static TextView txtTitulo;
    AlertDialog dialogCarga;

    AlertDialog.Builder alerta;
    ResponseRegister responseRegister = new ResponseRegister();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        fragmentManager = getSupportFragmentManager();
        inicializar();
        txtTitulo = findViewById(R.id.txtTitulo);
        btnSync = toolbar.findViewById(R.id.btnSincronizar);
        alerta = new AlertDialog.Builder(this);
        dialogCarga = new SpotsDialog(this, "Sincronizando Registros...");

        context = this.getApplicationContext();
        Intent intent = new Intent(this, Service.class);
        startService(intent);

        if (!isMyServiceRunning(Service.class)) {//método que determina si el servicio ya está corriendo o no


            intent = new Intent(getApplicationContext(), com.icobandas.icobandasapp.Service.class); //serv de tipo Intent
            getApplicationContext().startService(intent); //ctx de tipo Context


        } else {
            Log.e("App", "Service already running");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.icono_menu_pequeno);


        for (int i = 0; i < toolbar.getChildCount(); i++) {
            if (toolbar.getChildAt(i) instanceof ImageButton) {
                toolbar.getChildAt(i).setScaleX(0.6f);
                toolbar.getChildAt(i).setScaleY(0.6f);
            }
        }


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.txtNombreUsuario);

        if (isOnline(getApplicationContext())) {
            if (Login.rol.equals("admin")) {
                navUsername.setText("AGENTE:\n" + Login.nombreAdmin + "\n" + Login.nombreUsuario);
            } else {
                navUsername.setText("AGENTE:\n" + Login.nombreAdmin + "\n" + Login.nombreUsuario);

            }
        } else {
            navUsername.setText("AGENTE:\n" + Login.cursor.getString(1));

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentRegistrosRecientes()).commit();


        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline(MainActivity.this))
                {
                    sincronizarTodo();
                }
                else
                {
                    alerta= new AlertDialog.Builder(MainActivity.this);
                    alerta.setTitle("ICOBANDAS S.A dice:");
                    alerta.setMessage("Debe tener conexión a internet para poder realizar la sincronización");
                    alerta.setPositiveButton("Acetptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alerta.create();
                    alerta.show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.contenedor);

            if (currentFragment instanceof FragmentRegistrosRecientes) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setTitle("ICOBANDAS S.A dice:");
                alerta.setMessage("¿Desea salir de la aplicación?");
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

            } else {
                super.onBackPressed();

            }

        }
    }

    public void inicializar() {
        queue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ingresarDatos) {


            Fragment f = new FragmentSeleccionarTransportador();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                    .commit();


        } else if (id == R.id.registrarCliente) {


            Fragment f = new FragmentAgregarCliente();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                    .commit();


        } else if (id == R.id.cerrrarSesion) {

            startActivity(new Intent(MainActivity.this, Login.class));
            finish();

        } else if (id == R.id.historialRegistros) {


            Fragment f = new FragmentRegistrosRecientes();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                    .commit();


        } else if (id == R.id.agregarPlantas) {


            Fragment f = new FragmentAgregarPlantas();


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void mostrarNotifCarga() {
        int color = Color.rgb(156, 38, 46);
        String CHANNEL_ID = "my_channel_01";

        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        mBuilder
                .setSmallIcon(R.mipmap.notificacion)
                .setContentTitle("SINCRONIZANDO DATOS")
                .setContentText("Espere...")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColorized(true)
                .setColor(color)
                .setChannelId(CHANNEL_ID)
                .setOngoing(true)
                .setProgress(100, 1, true);

        notificationManager.notify(1, mBuilder.build());

    }


    public void sincronizarTodo() {
        alerta.setTitle("ICOBANDAS S.A dice:");
        alerta.setMessage("¿Desea sincronizar todos los registros pendientes?");
        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(context, "Sincronizar", Toast.LENGTH_SHORT).show();
                notificationManager.cancelAll();
                mostrarNotifCarga();
                dialogCarga.show();
                sincronizacion = 0;
                sincronizarClientes();


                /*if (sincronizacion==1) {
                    sincronizacion =0;
                    sincronizarTransportadores();
                }
                if (sincronizacion==1) {
                    sincronizacion = 0;
                    sincronizarRegistros();
                }
                if (sincronizacion==1) {
                    sincronizacion = 0;
                    sincronizarInsercionesPesada();
                }
                if (sincronizacion==1) {
                    sincronizacion = 0;
                    sincronizarActualizacionesRegistro();
                }
                if (sincronizacion==1) {
                    sincronizacion = 1;
                    sincronizarActualizacionesPesada();
                }
                if (sincronizacion==1) {
                    sincronizacion=0;
                    sincronizarInsercionesVertical();
                }*/
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

    public void sincronizarClientes() {

        dbHelper = new DbHelper(this, "prueba", null, 1);

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("Select * from clientes where estadoRegistroCliente='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarPlantas();

        } else if (cursor.getCount() == 1) {
            try {
                cursor.moveToFirst();
                final ArrayList<ClientesEntities> clientesEntitiesArrayList = new ArrayList<>();
                ClientesEntities clientesEntities = new ClientesEntities();
                clientesEntities.setNit(cursor.getString(0));
                clientesEntities.setNameunido(cursor.getString(1));
                clientesEntitiesArrayList.add(clientesEntities);
                db.execSQL("Update clientes set estadoRegistroCliente='Sincronizado' where nit='" + cursor.getString(0) + "'");
                String url = Constants.url + "sincroClientes";

                StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        sincronizarPlantas();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                        
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        cursor.moveToFirst();
                        params.put("nitCliente", clientesEntitiesArrayList.get(0).getNit());
                        params.put("nombreCliente", clientesEntitiesArrayList.get(0).getNameunido());

                        return params;
                    }

                };
                requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(requestClientes);
                //requestClientes.wait();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }


        } else {

            try {

                final ArrayList<ClientesEntities> clientesEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    ClientesEntities clientesEntities = new ClientesEntities();
                    clientesEntities.setNit(cursor.getString(0));
                    clientesEntities.setNameunido(cursor.getString(1));
                    clientesEntitiesArrayList.add(clientesEntities);
                }

                for (int i = 0; i < clientesEntitiesArrayList.size(); i++) {

                    db.execSQL("Update clientes set estadoRegistroCliente='Sincronizado' where nit='" + clientesEntitiesArrayList.get(i).getNit() + "'");

                    String url = Constants.url + "sincroClientes";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            conteo++;
                            if (conteo == clientesEntitiesArrayList.size()) {
                                conteo = 0;
                                sincronizarPlantas();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("nitCliente", clientesEntitiesArrayList.get(finalI).getNit());
                            params.put("nombreCliente", clientesEntitiesArrayList.get(finalI).getNameunido());
                            Log.i("PARAMS", params.toString());
                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);

                    //this.wait(2000);

                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarPlantas() {
        //toast=false;
        dbHelper = new DbHelper(this, "prueba", null, 1);

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from plantas where estadoRegistroPlanta='Pendiente INSERTAR BD'", null);

        if (cursor.getCount() == 0) {
            sincronizarTransportadores();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            final String idPlanta = cursor.getString(0);
            db.execSQL("Update plantas set estadoRegistroPlanta='Sincronizado' where codplanta='" + cursor.getString(0) + "'");
            String url = Constants.url + "crearPlanta";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    responseRegister = gson.fromJson(response, responseRegister.getClass());

                    Log.e("RESPONSE", responseRegister.getResponse());

                    if (responseRegister.getResponse().equals("ok")) {
                        String url1 = Constants.url + "maxPlanta";
                        StringRequest requestPlanta = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                FragmentPartesVertical.idMaximaRegistro.clear();
                                Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                }.getType();
                                FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                                cursor = db.rawQuery("Select * from plantas where codplanta=" + idPlanta, null);
                                cursor.moveToFirst();
                                MDToast.makeText(MainActivity.context, "Planta agregada correctamente", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                db.execSQL("update plantas set codplanta=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + " where codplanta=" + idPlanta);
                                db.execSQL("update transportador set codplanta=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + " where codplanta=" + idPlanta);
                                db.execSQL("update registro set codplanta=" + FragmentPartesVertical.idMaximaRegistro.get(0).getMax() + " where codplanta=" + idPlanta);
                                sincronizarTransportadores();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestPlanta.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        queue.add(requestPlanta);

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nitCliente", cursor.getString(2));
                    params.put("NombrePlanta", cursor.getString(3));
                    params.put("idUsuario", Login.nombreUsuario);
                    params.put("direccionPlanta", cursor.getString(5));
                    params.put("ciudad", cursor.getString(4));

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);

        } else {

            try {
                final ArrayList<PlantasEntities> plantasEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    PlantasEntities plantasEntities = new PlantasEntities();
                    plantasEntities.setNitplanta(cursor.getString(2));
                    plantasEntities.setNameplanta(cursor.getString(3));
                    plantasEntities.setAgentePlanta(cursor.getString(1));
                    plantasEntities.setCiudmciapl(cursor.getString(4));
                    plantasEntities.setDirmciapl(cursor.getString(5));
                    plantasEntities.setCodplanta(cursor.getString(0));
                    plantasEntitiesArrayList.add(plantasEntities);
                }

                for (int i = 0; i < plantasEntitiesArrayList.size(); i++) {

                    db.execSQL("Update plantas set estadoRegistroPlanta='Sincronizado' where codplanta='" + plantasEntitiesArrayList.get(i).getCodplanta() + "'");
                    FragmentPartesVertical.idMaximaRegistro.clear();

                    String url = Constants.url + "crearPlanta";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            responseRegister = gson.fromJson(response, responseRegister.getClass());

                            Log.e("RESPONSE", responseRegister.getResponse());

                            if (responseRegister.getResponse().equals("ok")) {
                                String url1 = Constants.url + "maxPlanta";
                                StringRequest requestPlanta = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                        }.getType();
                                        FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                        String idPlanta = FragmentPartesVertical.idMaximaRegistro.get(0).getMax();
                                        //MDToast.makeText(MainActivity.context, "Planta agregada correctamente", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        db.execSQL("update plantas set codplanta=" + idPlanta + " where codplanta=" + plantasEntitiesArrayList.get(finalI).getCodplanta());
                                        db.execSQL("update transportador set codplanta=" + idPlanta + " where codplanta=" + plantasEntitiesArrayList.get(finalI).getCodplanta());
                                        db.execSQL("update registro set codplanta=" + idPlanta + " where codplanta=" + plantasEntitiesArrayList.get(finalI).getCodplanta());
                                        conteo++;
                                        if (conteo == plantasEntitiesArrayList.size()) {
                                            conteo = 0;
                                            sincronizarTransportadores();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                requestPlanta.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                queue.add(requestPlanta);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("nitCliente", plantasEntitiesArrayList.get(finalI).getNitplanta());
                            params.put("NombrePlanta", plantasEntitiesArrayList.get(finalI).getNameplanta());
                            params.put("idUsuario", Login.nombreUsuario);
                            params.put("direccionPlanta", plantasEntitiesArrayList.get(finalI).getDirmciapl());
                            params.put("ciudad", plantasEntitiesArrayList.get(finalI).getCiudmciapl());


                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);
                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarTransportadores() {
        //toast=false;
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("Select * from transportador where estadoRegistroTransportador='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            actulizarTransportadores();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final String tipoTransportador = cursor.getString(1);
            final String nombreTranspotador = cursor.getString(2);
            final String idPlanta = cursor.getString(3);
            final String descripcionTransportador = cursor.getString(4);
            final String idTransportador1 = cursor.getString(0);
            String url = Constants.url + "crearTransportador";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    db.execSQL("Update transportador set estadoRegistroTransportador='Sincronizado' where idTransportador='" + cursor.getString(0) + "'");

                        String url1 = Constants.url + "maxTransportador";
                        StringRequest request = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                FragmentPartesVertical.idMaximaRegistro.clear();
                                Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                }.getType();
                                FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                String idTransportador = FragmentPartesVertical.idMaximaRegistro.get(0).getMax();
                                //Log.e("SENTENCIA", "update transportador set idTransportador=" + idTransportador + " where idTransportador='" + idTransportador1 + "'");
                                db.execSQL("update transportador set idTransportador=" + idTransportador + " where idTransportador='" + idTransportador1 + "'");
                                db.execSQL("update registro set idTransportador=" + idTransportador + " where idTransportador='" + idTransportador1 + "'");
                                actulizarTransportadores();


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    request.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(request);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("tipoTransportador", tipoTransportador);
                    params.put("nombreTransportador", nombreTranspotador);
                    params.put("idPlanta", idPlanta);
                    params.put("descripcionTransportador", descripcionTransportador);

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);



        } else {

            try {
                final ArrayList<TransportadorEntities> transportadorEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    TransportadorEntities transportadorEntities = new TransportadorEntities();
                    transportadorEntities.setIdTransportador(cursor.getString(0));
                    transportadorEntities.setTipoTransportador(cursor.getString(1));
                    transportadorEntities.setNombreTransportadro(cursor.getString(2));
                    transportadorEntities.setCodplanta(cursor.getString(3));
                    transportadorEntities.setCaracteristicaTransportador(cursor.getString(4));
                    transportadorEntitiesArrayList.add(transportadorEntities);
                }

                for (int i = 0; i < transportadorEntitiesArrayList.size(); i++) {

                    db.execSQL("Update transportador set estadoRegistroTransportador='Sincronizado' where idTransportador='" + transportadorEntitiesArrayList.get(i).getIdTransportador() + "'");

                    String url = Constants.url + "crearTransportador";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                                String url1 = Constants.url + "maxTransportador";
                                StringRequest request = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                        }.getType();
                                        FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                        String idTransportador = FragmentPartesVertical.idMaximaRegistro.get(0).getMax();

                                        db.execSQL("update transportador set idTransportador=" + idTransportador + ", estadoRegistroTransportador='Sincronizado' where idTransportador=" + transportadorEntitiesArrayList.get(finalI).getIdTransportador());
                                        db.execSQL("update registro set idTransportador=" + idTransportador + " where idTransportador=" + transportadorEntitiesArrayList.get(finalI).getIdTransportador());
                                        conteo++;
                                        if (conteo == transportadorEntitiesArrayList.size()) {
                                            conteo = 0;
                                            actulizarTransportadores();

                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                            request.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            queue.add(request);




                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("tipoTransportador", transportadorEntitiesArrayList.get(finalI).getTipoTransportador());
                            params.put("nombreTransportador", transportadorEntitiesArrayList.get(finalI).getNombreTransportadro());
                            params.put("idPlanta", transportadorEntitiesArrayList.get(finalI).getCodplanta());
                            params.put("descripcionTransportador", transportadorEntitiesArrayList.get(finalI).getCaracteristicaTransportador());


                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }

    }

    public void actulizarTransportadores() {
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("Select * from transportador where estadoRegistroTransportador='Actualizar con DB'", null);
        if (cursor.getCount() == 0) {

            sincronizarRegistros();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            final String nombreTranspotador = cursor.getString(2);

            final String descripcionTransportador = cursor.getString(4);
            final String idTransportador1 = cursor.getString(0);
            db.execSQL("Update transportador set estadoRegistroTransportador='Sincronizado' where idTransportador='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarTransportador/" + cursor.getString(0);
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                        sincronizarRegistros();



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idTransportador", idTransportador1);

                    params.put("nombreTransportador", nombreTranspotador);
                    params.put("descripcionTransportador", descripcionTransportador);

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<TransportadorEntities> transportadorEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    TransportadorEntities transportadorEntities = new TransportadorEntities();
                    transportadorEntities.setIdTransportador(cursor.getString(0));
                    transportadorEntities.setTipoTransportador(cursor.getString(1));
                    transportadorEntities.setNombreTransportadro(cursor.getString(2));
                    transportadorEntities.setCodplanta(cursor.getString(3));
                    transportadorEntities.setCaracteristicaTransportador(cursor.getString(4));
                    transportadorEntitiesArrayList.add(transportadorEntities);
                }

                for (int i = 0; i < transportadorEntitiesArrayList.size(); i++) {

                    db.execSQL("Update transportador set estadoRegistroTransportador='Sincronizado' where idTransportador='" + transportadorEntitiesArrayList.get(i).getIdTransportador() + "'");

                    String url = Constants.url + "actualizarTransportador/" + transportadorEntitiesArrayList.get(i).getIdTransportador();
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (conteo == transportadorEntitiesArrayList.size()) {
                                sincronizarRegistros();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idTransportador", transportadorEntitiesArrayList.get(finalI).getIdTransportador());
                            params.put("nombreTransportador", transportadorEntitiesArrayList.get(finalI).getNombreTransportadro());
                            params.put("descripcionTransportador", transportadorEntitiesArrayList.get(finalI).getCaracteristicaTransportador());
                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);
                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarRegistros() {
        //toast=false;
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from registro where estadoRegistro='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarInsercionesPesada();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final String idRegistro = cursor.getString(0);
            db.execSQL("Update registro set estadoRegistro='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "crearRegistro";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String url1 = Constants.url + "maxRegistro";
                    StringRequest requestMaxRegistro = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            FragmentPartesVertical.idMaximaRegistro.clear();
                            Type type = new TypeToken<List<IdMaximaRegistro>>() {
                            }.getType();
                            FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);

                            int idTransportador = Integer.parseInt(FragmentPartesVertical.idMaximaRegistro.get(0).getMax());


                            db.execSQL("Update registro set idRegistro='" + idTransportador + "' where idRegistro='" + idRegistro + "'");
                            db.execSQL("update bandaTransmision set idRegistro='" + idTransportador + "' where idRegistro='" + idRegistro + "'");
                            db.execSQL("update bandaElevadora set idRegistro='" + idTransportador + "' where idRegistro='" + idRegistro + "'");
                            db.execSQL("update bandaTransportadora set idRegistro='" + idTransportador + "' where idRegistro='" + idRegistro + "'");

                            sincronizarInsercionesPesada();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();

                        }
                    });
                    requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(requestMaxRegistro);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("fechaRegistro", cursor.getString(1));
                    params.put("idTransportador", cursor.getString(2));
                    params.put("idPlanta", cursor.getString(3));
                    params.put("usuarioRegistro", cursor.getString(4));

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<RegistroEntities> registroEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    RegistroEntities registroEntities = new RegistroEntities();
                    registroEntities.setIdRegistro(cursor.getString(0));
                    registroEntities.setFechaRegistro(cursor.getString(1));
                    registroEntities.setIdTransportador(cursor.getString(2));
                    registroEntities.setCodplanta(cursor.getString(3));
                    registroEntities.setUsuarioRegistro(cursor.getString(4));
                    registroEntitiesArrayList.add(registroEntities);
                }

                for (int i = 0; i < registroEntitiesArrayList.size(); i++) {

                    db.execSQL("Update registro set estadoRegistro='Sincronizado' where idRegistro='" + registroEntitiesArrayList.get(i).getIdRegistro() + "'");

                    String url = Constants.url + "crearRegistro";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (response.equals("\ufeffok")) {
                                String url1 = Constants.url + "maxRegistro";
                                StringRequest requestMaxRegistro = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        FragmentPartesVertical.idMaximaRegistro.clear();
                                        Type type = new TypeToken<List<IdMaximaRegistro>>() {
                                        }.getType();
                                        FragmentPartesVertical.idMaximaRegistro = gson.fromJson(response, type);
                                        int idTransportador = Integer.parseInt(FragmentPartesVertical.idMaximaRegistro.get(0).getMax());
                                        //registros.add(String.valueOf(idTransportador));

                                        db.execSQL("update registro set idRegistro='" + idTransportador + "', estadoRegistro='Sincronizado' where idRegistro='" + registroEntitiesArrayList.get(finalI).getIdRegistro() + "'");
                                        Cursor cursor1 = db.rawQuery("Select * from registro", null);
                                        cursor1.moveToFirst();
                                        db.execSQL("update bandaTransmision set idRegistro='" + cursor1.getString(0) + "' where idRegistro='" + registroEntitiesArrayList.get(finalI).getIdRegistro() + "'");
                                        db.execSQL("update bandaElevadora set idRegistro='" + cursor1.getString(0) + "' where idRegistro='" + registroEntitiesArrayList.get(finalI).getIdRegistro() + "'");
                                        db.execSQL("update bandaTransportadora set idRegistro='" + cursor1.getString(0) + "' where idRegistro='" + registroEntitiesArrayList.get(finalI).getIdRegistro() + "'");

                                        conteo++;
                                        if (conteo == registroEntitiesArrayList.size()) {
                                            sincronizarInsercionesPesada();

                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();

                                    }
                                });
                                requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(requestMaxRegistro);
                            }
                            Log.e("RESPONSE", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("fechaRegistro", registroEntitiesArrayList.get(finalI).getFechaRegistro());
                            params.put("idTransportador", registroEntitiesArrayList.get(finalI).getIdTransportador());
                            params.put("idPlanta", registroEntitiesArrayList.get(finalI).getCodplanta());
                            params.put("usuarioRegistro", registroEntitiesArrayList.get(finalI).getUsuarioRegistro());


                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes).wait(2000);


                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarInsercionesPesada() {
        dbHelper = new DbHelper(this, "prueba", null, 1);
        conteo = 0;

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaTransmision where estadoRegistroPesada='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarInsercionesVertical();

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaPesada> bandaPesadaArrayList = new ArrayList<>();


            BandaPesada bandaPesada = new BandaPesada();
            bandaPesada.setIdRegistro(cursor.getString(0));
            bandaPesada.setAnchoBandaTransmision(cursor.getString(1));
            bandaPesada.setDistanciaEntreCentrosTransmision(cursor.getString(2));
            bandaPesada.setPotenciaMotorTransmision(cursor.getString(3));
            bandaPesada.setRpmSalidaReductorTransmision(cursor.getString(4));
            bandaPesada.setDiametroPoleaConducidaTransmision(cursor.getString(5));
            bandaPesada.setAnchoPoleaConducidaTransmision(cursor.getString(6));
            bandaPesada.setDiametroPoleaMotrizTransmision(cursor.getString(7));
            bandaPesada.setAnchoPoleaMotrizTransmision(cursor.getString(8));
            bandaPesada.setTipoParteTransmision(cursor.getString(9));
            bandaPesada.setObservacionRegistro(cursor.getString(11));
            bandaPesadaArrayList.add(bandaPesada);

            db.execSQL("Update bandaTransmision set estadoRegistroPesada='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "registroBandaPesada";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarInsercionesVertical();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaPesadaArrayList.get(0).getIdRegistro());
                    params.put("anchoBandaTransmision", bandaPesadaArrayList.get(0).getAnchoBandaTransmision());
                    params.put("distanciaEntreCentrosTransmision", bandaPesadaArrayList.get(0).getDistanciaEntreCentrosTransmision());
                    params.put("potenciaMotorTransmision", bandaPesadaArrayList.get(0).getPotenciaMotorTransmision());
                    params.put("rpmSalidaReductorTransmision", bandaPesadaArrayList.get(0).getRpmSalidaReductorTransmision());
                    params.put("diametroPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaConducidaTransmision());
                    params.put("anchoPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaConducidaTransmision());
                    params.put("diametroPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaMotrizTransmision());
                    params.put("anchoPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaMotrizTransmision());
                    params.put("tipoParteTransmision", bandaPesadaArrayList.get(0).getTipoParteTransmision());
                    params.put("observacionRegistroPesada", bandaPesadaArrayList.get(0).getObservacionRegistro());

                    Log.e("PARAMS", params.toString());


                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<BandaPesada> bandaPesadaArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    BandaPesada bandaPesada = new BandaPesada();
                    bandaPesada.setIdRegistro(cursor.getString(0));
                    bandaPesada.setAnchoBandaTransmision(cursor.getString(1));
                    bandaPesada.setDistanciaEntreCentrosTransmision(cursor.getString(2));
                    bandaPesada.setPotenciaMotorTransmision(cursor.getString(3));
                    bandaPesada.setRpmSalidaReductorTransmision(cursor.getString(4));
                    bandaPesada.setDiametroPoleaConducidaTransmision(cursor.getString(5));
                    bandaPesada.setAnchoPoleaConducidaTransmision(cursor.getString(6));
                    bandaPesada.setDiametroPoleaMotrizTransmision(cursor.getString(7));
                    bandaPesada.setAnchoPoleaMotrizTransmision(cursor.getString(8));
                    bandaPesada.setTipoParteTransmision(cursor.getString(9));
                    bandaPesada.setObservacionRegistro(cursor.getString(11));
                    bandaPesadaArrayList.add(bandaPesada);
                }

                for (int i = 0; i < bandaPesadaArrayList.size(); i++) {


                    String url = Constants.url + "registroBandaPesada";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransmision set  estadoRegistroPesada='Sincronizado' where idRegistro='" + bandaPesadaArrayList.get(finalI).getIdRegistro() + "'");
                            conteo++;
                            if (conteo == bandaPesadaArrayList.size()) {
                                sincronizarInsercionesVertical();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaPesadaArrayList.get(finalI).getIdRegistro());
                            params.put("anchoBandaTransmision", bandaPesadaArrayList.get(finalI).getAnchoBandaTransmision());
                            params.put("distanciaEntreCentrosTransmision", bandaPesadaArrayList.get(finalI).getDistanciaEntreCentrosTransmision());
                            params.put("potenciaMotorTransmision", bandaPesadaArrayList.get(finalI).getPotenciaMotorTransmision());
                            params.put("rpmSalidaReductorTransmision", bandaPesadaArrayList.get(finalI).getRpmSalidaReductorTransmision());
                            params.put("diametroPoleaConducidaTransmision", bandaPesadaArrayList.get(finalI).getDiametroPoleaConducidaTransmision());
                            params.put("anchoPoleaConducidaTransmision", bandaPesadaArrayList.get(finalI).getAnchoPoleaConducidaTransmision());
                            params.put("diametroPoleaMotrizTransmision", bandaPesadaArrayList.get(finalI).getDiametroPoleaMotrizTransmision());
                            params.put("anchoPoleaMotrizTransmision", bandaPesadaArrayList.get(finalI).getAnchoPoleaMotrizTransmision());
                            params.put("tipoParteTransmision", bandaPesadaArrayList.get(finalI).getTipoParteTransmision());
                            params.put("observacionRegistroPesada", bandaPesadaArrayList.get(finalI).getObservacionRegistro());


                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarInsercionesVertical() {
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaElevadora where estadoRegistroElevadora='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarInsercionesHorizontal();

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaElevadoraEntities> bandaElevadoraEntitiesArrayList = new ArrayList<>();

            BandaElevadoraEntities bandaElevadoraEntities = new BandaElevadoraEntities();
            bandaElevadoraEntities.setIdRegistro(cursor.getString(0));
            bandaElevadoraEntities.setMarcaBandaElevadora(cursor.getString(1));
            bandaElevadoraEntities.setAnchoBandaElevadora(cursor.getString(2));
            bandaElevadoraEntities.setDistanciaEntrePoleasElevadora(cursor.getString(3));
            bandaElevadoraEntities.setNoLonaBandaElevadora(cursor.getString(4));
            bandaElevadoraEntities.setTipoLonaBandaElevadora(cursor.getString(5));
            bandaElevadoraEntities.setEspesorTotalBandaElevadora(cursor.getString(6));
            bandaElevadoraEntities.setEspesorCojinActualElevadora(cursor.getString(7));
            bandaElevadoraEntities.setEspesorCubiertaSuperiorElevadora(cursor.getString(8));
            bandaElevadoraEntities.setEspesorCubiertaInferiorElevadora(cursor.getString(9));
            bandaElevadoraEntities.setTipoCubiertaElevadora(cursor.getString(10));
            bandaElevadoraEntities.setTipoEmpalmeElevadora(cursor.getString(11));
            bandaElevadoraEntities.setEstadoEmpalmeElevadora(cursor.getString(12));
            bandaElevadoraEntities.setResistenciaRoturaLonaElevadora(cursor.getString(13));
            bandaElevadoraEntities.setVelocidadBandaElevadora(cursor.getString(14));
            bandaElevadoraEntities.setMarcaBandaElevadoraAnterior(cursor.getString(15));
            bandaElevadoraEntities.setAnchoBandaElevadoraAnterior(cursor.getString(16));
            bandaElevadoraEntities.setNoLonasBandaElevadoraAnterior(cursor.getString(17));
            bandaElevadoraEntities.setTipoLonaBandaElevadoraAnterior(cursor.getString(18));
            bandaElevadoraEntities.setEspesorTotalBandaElevadoraAnterior(cursor.getString(19));
            bandaElevadoraEntities.setEspesorCubiertaSuperiorBandaElevadoraAnterior(cursor.getString(20));
            bandaElevadoraEntities.setEspesorCojinElevadoraAnterior(cursor.getString(21));
            bandaElevadoraEntities.setEspesorCubiertaInferiorBandaElevadoraAnterior(cursor.getString(22));
            bandaElevadoraEntities.setTipoCubiertaElevadoraAnterior(cursor.getString(23));
            bandaElevadoraEntities.setTipoEmpalmeElevadoraAnterior(cursor.getString(24));
            bandaElevadoraEntities.setResistenciaRoturaBandaElevadoraAnterior(cursor.getString(25));
            bandaElevadoraEntities.setTonsTransportadasBandaElevadoraAnterior(cursor.getString(26));
            bandaElevadoraEntities.setVelocidadBandaElevadoraAnterior(cursor.getString(27));
            bandaElevadoraEntities.setCausaFallaCambioBandaElevadoraAnterior(cursor.getString(28));
            bandaElevadoraEntities.setPesoMaterialEnCadaCangilon(cursor.getString(29));
            bandaElevadoraEntities.setPesoCangilonVacio(cursor.getString(30));
            bandaElevadoraEntities.setLongitudCangilon(cursor.getString(31));
            bandaElevadoraEntities.setMaterialCangilon(cursor.getString(32));
            bandaElevadoraEntities.setTipoCangilon(cursor.getString(33));
            bandaElevadoraEntities.setProyeccionCangilon(cursor.getString(34));
            bandaElevadoraEntities.setProfundidadCangilon(cursor.getString(35));
            bandaElevadoraEntities.setMarcaCangilon(cursor.getString(36));
            bandaElevadoraEntities.setReferenciaCangilon(cursor.getString(37));
            bandaElevadoraEntities.setCapacidadCangilon(cursor.getString(38));
            bandaElevadoraEntities.setNoFilasCangilones(cursor.getString(39));
            bandaElevadoraEntities.setSeparacionCangilones(cursor.getString(40));
            bandaElevadoraEntities.setNoAgujeros(cursor.getString(41));
            bandaElevadoraEntities.setDistanciaBordeBandaEstructura(cursor.getString(42));
            bandaElevadoraEntities.setDistanciaPosteriorBandaEstructura(cursor.getString(43));
            bandaElevadoraEntities.setDistanciaLaboFrontalCangilonEstructura(cursor.getString(44));
            bandaElevadoraEntities.setDistanciaBordesCangilonEstructura(cursor.getString(45));
            bandaElevadoraEntities.setTipoVentilacion(cursor.getString(46));
            bandaElevadoraEntities.setDiametroPoleaMotrizElevadora(cursor.getString(47));
            bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(48));
            bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(49));
            bandaElevadoraEntities.setTipoPoleaMotrizElevadora(cursor.getString(50));
            bandaElevadoraEntities.setLargoEjeMotrizElevadora(cursor.getString(51));
            bandaElevadoraEntities.setDiametroEjeMotrizElevadora(cursor.getString(52));
            bandaElevadoraEntities.setBandaCentradaEnPoleaMotrizElevadora(cursor.getString(53));
            bandaElevadoraEntities.setEstadoRevestimientoPoleaMotrizElevadora(cursor.getString(54));
            bandaElevadoraEntities.setPotenciaMotorMotrizElevadora(cursor.getString(55));
            bandaElevadoraEntities.setRpmSalidaReductorMotrizElevadora(cursor.getString(56));
            bandaElevadoraEntities.setGuardaReductorPoleaMotrizElevadora(cursor.getString(57));
            bandaElevadoraEntities.setDiametroPoleaColaElevadora(cursor.getString(58));
            bandaElevadoraEntities.setAnchoPoleaColaElevadora(cursor.getString(59));
            bandaElevadoraEntities.setTipoPoleaColaElevadora(cursor.getString(60));
            bandaElevadoraEntities.setLargoEjePoleaColaElevadora(cursor.getString(61));
            bandaElevadoraEntities.setDiametroEjePoleaColaElevadora(cursor.getString(62));
            bandaElevadoraEntities.setBandaCentradaEnPoleaColaElevadora(cursor.getString(63));
            bandaElevadoraEntities.setEstadoRevestimientoPoleaColaElevadora(cursor.getString(64));
            bandaElevadoraEntities.setLongitudTensorTornilloPoleaColaElevadora(cursor.getString(65));
            bandaElevadoraEntities.setLongitudRecorridoContrapesaPoleaColaElevadora(cursor.getString(66));
            bandaElevadoraEntities.setCargaTrabajoBandaElevadora(cursor.getString(67));
            bandaElevadoraEntities.setTemperaturaMaterialElevadora(cursor.getString(68));
            bandaElevadoraEntities.setEmpalmeMecanicoElevadora(cursor.getString(69));
            bandaElevadoraEntities.setDiametroRoscaElevadora(cursor.getString(70));
            bandaElevadoraEntities.setLargoTornilloElevadora(cursor.getString(71));
            bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(72));
            bandaElevadoraEntities.setAnchoCabezaElevadorPuertaInspeccion(cursor.getString(73));
            bandaElevadoraEntities.setLargoCabezaElevadorPuertaInspeccion(cursor.getString(74));
            bandaElevadoraEntities.setAnchoBotaElevadorPuertaInspeccion(cursor.getString(75));
            bandaElevadoraEntities.setLargoBotaElevadorPuertaInspeccion(cursor.getString(76));
            bandaElevadoraEntities.setMonitorPeligro(cursor.getString(77));
            bandaElevadoraEntities.setRodamiento(cursor.getString(78));
            bandaElevadoraEntities.setMonitorVelocidad(cursor.getString(79));
            bandaElevadoraEntities.setSensorInductivo(cursor.getString(80));
            bandaElevadoraEntities.setIndicadorNivel(cursor.getString(81));
            bandaElevadoraEntities.setCajaUnion(cursor.getString(82));
            bandaElevadoraEntities.setAlarmaYPantalla(cursor.getString(83));
            bandaElevadoraEntities.setInterruptorSeguridad(cursor.getString(84));
            bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(85));
            bandaElevadoraEntities.setAtaqueQuimicoElevadora(cursor.getString(86));
            bandaElevadoraEntities.setAtaqueTemperaturaElevadora(cursor.getString(87));
            bandaElevadoraEntities.setAtaqueAceitesElevadora(cursor.getString(88));
            bandaElevadoraEntities.setAtaqueAbrasivoElevadora(cursor.getString(89));
            bandaElevadoraEntities.setCapacidadElevadora(cursor.getString(90));
            bandaElevadoraEntities.setHorasTrabajoDiaElevadora(cursor.getString(91));
            bandaElevadoraEntities.setDiasTrabajoSemanaElevadora(cursor.getString(92));
            bandaElevadoraEntities.setAbrasividadElevadora(cursor.getString(93));
            bandaElevadoraEntities.setPorcentajeFinosElevadora(cursor.getString(94));
            bandaElevadoraEntities.setMaxGranulometriaElevadora(cursor.getString(95));
            bandaElevadoraEntities.setDensidadMaterialElevadora(cursor.getString(96));
            bandaElevadoraEntities.setTempMaxMaterialSobreBandaElevadora(cursor.getString(97));
            bandaElevadoraEntities.setTempPromedioMaterialSobreBandaElevadora(cursor.getString(98));
            bandaElevadoraEntities.setVariosPuntosDeAlimentacion(cursor.getString(99));
            bandaElevadoraEntities.setLluviaDeMaterial(cursor.getString(100));
            bandaElevadoraEntities.setAnchoPiernaElevador(cursor.getString(101));
            bandaElevadoraEntities.setProfundidadPiernaElevador(cursor.getString(102));
            bandaElevadoraEntities.setTempAmbienteMin(cursor.getString(103));
            bandaElevadoraEntities.setTempAmbienteMax(cursor.getString(104));
            bandaElevadoraEntities.setTipoCarga(cursor.getString(105));
            bandaElevadoraEntities.setTipoDescarga(cursor.getString(106));
            bandaElevadoraEntities.setObservacionRegistroElevadora(cursor.getString(108));

            bandaElevadoraEntitiesArrayList.add(bandaElevadoraEntities);

            db.execSQL("Update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "sincronizacionElevadora";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarInsercionesHorizontal();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaElevadoraEntitiesArrayList.get(0).getIdRegistro());
                    params.put("marcaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getMarcaBandaElevadora());
                    params.put("anchoBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoBandaElevadora());
                    params.put("distanciaEntrePoleasElevadora", bandaElevadoraEntitiesArrayList.get(0).getDistanciaEntrePoleasElevadora());
                    params.put("noLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getNoLonaBandaElevadora());
                    params.put("tipoLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoLonaBandaElevadora());
                    params.put("espesorTotalBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorTotalBandaElevadora());
                    params.put("espesorCojinActualElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCojinActualElevadora());
                    params.put("espesorCubiertaSuperiorElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaSuperiorElevadora());
                    params.put("espesorCubiertaInferiorElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaInferiorElevadora());
                    params.put("tipoCubiertaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoCubiertaElevadora());
                    params.put("tipoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoEmpalmeElevadora());
                    params.put("estadoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoEmpalmeElevadora());
                    params.put("resistenciaRoturaLonaElevadora", bandaElevadoraEntitiesArrayList.get(0).getResistenciaRoturaLonaElevadora());
                    params.put("velocidadBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getVelocidadBandaElevadora());
                    params.put("marcaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getMarcaBandaElevadoraAnterior());
                    params.put("anchoBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getAnchoBandaElevadoraAnterior());
                    params.put("noLonasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getNoLonasBandaElevadoraAnterior());
                    params.put("tipoLonaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoLonaBandaElevadoraAnterior());
                    params.put("espesorTotalBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorTotalBandaElevadoraAnterior());
                    params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaSuperiorBandaElevadoraAnterior());
                    params.put("espesorCojinElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCojinElevadoraAnterior());
                    params.put("espesorCubiertaInferiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaInferiorBandaElevadoraAnterior());
                    params.put("tipoCubiertaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoCubiertaElevadoraAnterior());
                    params.put("tipoEmpalmeElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoEmpalmeElevadoraAnterior());
                    params.put("resistenciaRoturaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getResistenciaRoturaBandaElevadoraAnterior());
                    params.put("tonsTransportadasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTonsTransportadasBandaElevadoraAnterior());
                    params.put("velocidadBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getVelocidadBandaElevadoraAnterior());
                    params.put("causaFallaCambioBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getCausaFallaCambioBandaElevadoraAnterior());
                    params.put("pesoMaterialEnCadaCangilon", bandaElevadoraEntitiesArrayList.get(0).getPesoMaterialEnCadaCangilon());
                    params.put("pesoCangilonVacio", bandaElevadoraEntitiesArrayList.get(0).getPesoCangilonVacio());
                    params.put("longitudCangilon", bandaElevadoraEntitiesArrayList.get(0).getLongitudCangilon() );
                    params.put("materialCangilon", bandaElevadoraEntitiesArrayList.get(0).getMaterialCangilon());
                    params.put("tipoCangilon", bandaElevadoraEntitiesArrayList.get(0).getTipoCangilon());
                    params.put("proyeccionCangilon", bandaElevadoraEntitiesArrayList.get(0).getProyeccionCangilon());
                    params.put("profundidadCangilon", bandaElevadoraEntitiesArrayList.get(0).getProfundidadCangilon());
                    params.put("marcaCangilon", bandaElevadoraEntitiesArrayList.get(0).getMarcaCangilon());
                    params.put("referenciaCangilon", bandaElevadoraEntitiesArrayList.get(0).getReferenciaCangilon());
                    params.put("capacidadCangilon", bandaElevadoraEntitiesArrayList.get(0).getCapacidadCangilon());
                    params.put("noFilasCangilones", bandaElevadoraEntitiesArrayList.get(0).getNoFilasCangilones());
                    params.put("separacionCangilones", bandaElevadoraEntitiesArrayList.get(0).getSeparacionCangilones());
                    params.put("noAgujeros", bandaElevadoraEntitiesArrayList.get(0).getNoAgujeros());
                    params.put("distanciaBordeBandaEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaBordeBandaEstructura());
                    params.put("distanciaPosteriorBandaEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaPosteriorBandaEstructura());
                    params.put("distanciaLaboFrontalCangilonEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaLaboFrontalCangilonEstructura());
                    params.put("distanciaBordesCangilonEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaBordesCangilonEstructura());
                    params.put("tipoVentilacion", bandaElevadoraEntitiesArrayList.get(0).getTipoVentilacion());
                    params.put("diametroPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroPoleaMotrizElevadora());
                    params.put("anchoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoPoleaMotrizElevadora());
                    params.put("tipoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoPoleaMotrizElevadora());
                    params.put("largoEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoEjeMotrizElevadora());
                    params.put("diametroEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroEjeMotrizElevadora());
                    params.put("bandaCentradaEnPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getBandaCentradaEnPoleaMotrizElevadora());
                    params.put("estadoRevestimientoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoRevestimientoPoleaMotrizElevadora());
                    params.put("potenciaMotorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getPotenciaMotorMotrizElevadora());
                    params.put("rpmSalidaReductorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getRpmSalidaReductorMotrizElevadora());
                    params.put("guardaReductorPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getGuardaReductorPoleaMotrizElevadora());
                    params.put("diametroPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroPoleaColaElevadora());
                    params.put("anchoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoPoleaColaElevadora());
                    params.put("tipoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoPoleaColaElevadora());
                    params.put("largoEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoEjePoleaColaElevadora());
                    params.put("diametroEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroEjePoleaColaElevadora());
                    params.put("bandaCentradaEnPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getBandaCentradaEnPoleaColaElevadora());
                    params.put("estadoRevestimientoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoRevestimientoPoleaColaElevadora());
                    params.put("longitudTensorTornilloPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLongitudTensorTornilloPoleaColaElevadora());
                    params.put("longitudRecorridoContrapesaPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLongitudRecorridoContrapesaPoleaColaElevadora());
                    params.put("cargaTrabajoBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getCargaTrabajoBandaElevadora());
                    params.put("temperaturaMaterialElevadora", bandaElevadoraEntitiesArrayList.get(0).getTemperaturaMaterialElevadora());
                    params.put("empalmeMecanicoElevadora", bandaElevadoraEntitiesArrayList.get(0).getEmpalmeMecanicoElevadora());
                    params.put("diametroRoscaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroRoscaElevadora());
                    params.put("largoTornilloElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoTornilloElevadora());
                    params.put("materialTornilloElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaterialTornilloElevadora());
                    params.put("anchoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getAnchoCabezaElevadorPuertaInspeccion());
                    params.put("largoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getLargoCabezaElevadorPuertaInspeccion());
                    params.put("anchoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getAnchoBotaElevadorPuertaInspeccion());
                    params.put("largoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getLargoBotaElevadorPuertaInspeccion());
                    params.put("monitorPeligro", bandaElevadoraEntitiesArrayList.get(0).getMonitorPeligro());
                    params.put("rodamiento", bandaElevadoraEntitiesArrayList.get(0).getRodamiento());
                    params.put("monitorDesalineacion", bandaElevadoraEntitiesArrayList.get(0).getMonitorDesalineacion());
                    params.put("monitorVelocidad", bandaElevadoraEntitiesArrayList.get(0).getMonitorVelocidad());
                    params.put("sensorInductivo", bandaElevadoraEntitiesArrayList.get(0).getSensorInductivo());
                    params.put("indicadorNivel", bandaElevadoraEntitiesArrayList.get(0).getIndicadorNivel());
                    params.put("cajaUnion", bandaElevadoraEntitiesArrayList.get(0).getCajaUnion());
                    params.put("alarmaYPantalla", bandaElevadoraEntitiesArrayList.get(0).getAlarmaYPantalla());
                    params.put("interruptorSeguridad", bandaElevadoraEntitiesArrayList.get(0).getInterruptorSeguridad());
                    params.put("materialElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaterialElevadora());
                    params.put("ataqueQuimicoElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueQuimicoElevadora());
                    params.put("ataqueTemperaturaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueTemperaturaElevadora());
                    params.put("ataqueAceitesElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueAceitesElevadora());
                    params.put("ataqueAbrasivoElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueAbrasivoElevadora());
                    params.put("capacidadElevadora", bandaElevadoraEntitiesArrayList.get(0).getCapacidadElevadora());
                    params.put("horasTrabajoDiaElevadora", bandaElevadoraEntitiesArrayList.get(0).getHorasTrabajoDiaElevadora());
                    params.put("diasTrabajoSemanaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiasTrabajoSemanaElevadora());
                    params.put("abrasividadElevadora", bandaElevadoraEntitiesArrayList.get(0).getAbrasividadElevadora());
                    params.put("porcentajeFinosElevadora", bandaElevadoraEntitiesArrayList.get(0).getPorcentajeFinosElevadora());
                    params.put("maxGranulometriaElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaxGranulometriaElevadora());
                    params.put("densidadMaterialElevadora", bandaElevadoraEntitiesArrayList.get(0).getDensidadMaterialElevadora());
                    params.put("tempMaxMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTempMaxMaterialSobreBandaElevadora());
                    params.put("tempPromedioMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTempPromedioMaterialSobreBandaElevadora());
                    params.put("variosPuntosDeAlimentacion", bandaElevadoraEntitiesArrayList.get(0).getVariosPuntosDeAlimentacion());
                    params.put("lluviaDeMaterial", bandaElevadoraEntitiesArrayList.get(0).getLluviaDeMaterial());
                    params.put("anchoPiernaElevador", bandaElevadoraEntitiesArrayList.get(0).getAnchoPiernaElevador());
                    params.put("profundidadPiernaElevador", bandaElevadoraEntitiesArrayList.get(0).getProfundidadPiernaElevador());
                    params.put("tempAmbienteMin", bandaElevadoraEntitiesArrayList.get(0).getTempAmbienteMin());
                    params.put("tempAmbienteMax", bandaElevadoraEntitiesArrayList.get(0).getTempAmbienteMax());
                    params.put("tipoDescarga", bandaElevadoraEntitiesArrayList.get(0).getTipoDescarga());
                    params.put("tipoCarga", bandaElevadoraEntitiesArrayList.get(0).getTipoCarga());
                    params.put("observacionRegistroElevadora", bandaElevadoraEntitiesArrayList.get(0).getObservacionRegistroElevadora());

                    for(Map.Entry entry:params.entrySet()){
                        if(entry.getValue()==null)
                        {
                            String campo=entry.getKey().toString();

                            params.put(campo, "");
                        }
                    }

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<BandaElevadoraEntities> bandaElevadoraEntitiesArrayList = new ArrayList<>();


                while (cursor.moveToNext()) {
                    BandaElevadoraEntities bandaElevadoraEntities = new BandaElevadoraEntities();
                    bandaElevadoraEntities.setIdRegistro(cursor.getString(0));
                    bandaElevadoraEntities.setMarcaBandaElevadora(cursor.getString(1));
                    bandaElevadoraEntities.setAnchoBandaElevadora(cursor.getString(2));
                    bandaElevadoraEntities.setDistanciaEntrePoleasElevadora(cursor.getString(3));
                    bandaElevadoraEntities.setNoLonaBandaElevadora(cursor.getString(4));
                    bandaElevadoraEntities.setTipoLonaBandaElevadora(cursor.getString(5));
                    bandaElevadoraEntities.setEspesorTotalBandaElevadora(cursor.getString(6));
                    bandaElevadoraEntities.setEspesorCojinActualElevadora(cursor.getString(7));
                    bandaElevadoraEntities.setEspesorCubiertaSuperiorElevadora(cursor.getString(8));
                    bandaElevadoraEntities.setEspesorCubiertaInferiorElevadora(cursor.getString(9));
                    bandaElevadoraEntities.setTipoCubiertaElevadora(cursor.getString(10));
                    bandaElevadoraEntities.setTipoEmpalmeElevadora(cursor.getString(11));
                    bandaElevadoraEntities.setEstadoEmpalmeElevadora(cursor.getString(12));
                    bandaElevadoraEntities.setResistenciaRoturaLonaElevadora(cursor.getString(13));
                    bandaElevadoraEntities.setVelocidadBandaElevadora(cursor.getString(14));
                    bandaElevadoraEntities.setMarcaBandaElevadoraAnterior(cursor.getString(15));
                    bandaElevadoraEntities.setAnchoBandaElevadoraAnterior(cursor.getString(16));
                    bandaElevadoraEntities.setNoLonasBandaElevadoraAnterior(cursor.getString(17));
                    bandaElevadoraEntities.setTipoLonaBandaElevadoraAnterior(cursor.getString(18));
                    bandaElevadoraEntities.setEspesorTotalBandaElevadoraAnterior(cursor.getString(19));
                    bandaElevadoraEntities.setEspesorCubiertaSuperiorBandaElevadoraAnterior(cursor.getString(20));
                    bandaElevadoraEntities.setEspesorCojinElevadoraAnterior(cursor.getString(21));
                    bandaElevadoraEntities.setEspesorCubiertaInferiorBandaElevadoraAnterior(cursor.getString(22));
                    bandaElevadoraEntities.setTipoCubiertaElevadoraAnterior(cursor.getString(23));
                    bandaElevadoraEntities.setTipoEmpalmeElevadoraAnterior(cursor.getString(24));
                    bandaElevadoraEntities.setResistenciaRoturaBandaElevadoraAnterior(cursor.getString(25));
                    bandaElevadoraEntities.setTonsTransportadasBandaElevadoraAnterior(cursor.getString(26));
                    bandaElevadoraEntities.setVelocidadBandaElevadoraAnterior(cursor.getString(27));
                    bandaElevadoraEntities.setCausaFallaCambioBandaElevadoraAnterior(cursor.getString(28));
                    bandaElevadoraEntities.setPesoMaterialEnCadaCangilon(cursor.getString(29));
                    bandaElevadoraEntities.setPesoCangilonVacio(cursor.getString(30));
                    bandaElevadoraEntities.setLongitudCangilon(cursor.getString(31));
                    bandaElevadoraEntities.setMaterialCangilon(cursor.getString(32));
                    bandaElevadoraEntities.setTipoCangilon(cursor.getString(33));
                    bandaElevadoraEntities.setProyeccionCangilon(cursor.getString(34));
                    bandaElevadoraEntities.setProfundidadCangilon(cursor.getString(35));
                    bandaElevadoraEntities.setMarcaCangilon(cursor.getString(36));
                    bandaElevadoraEntities.setReferenciaCangilon(cursor.getString(37));
                    bandaElevadoraEntities.setCapacidadCangilon(cursor.getString(38));
                    bandaElevadoraEntities.setNoFilasCangilones(cursor.getString(39));
                    bandaElevadoraEntities.setSeparacionCangilones(cursor.getString(40));
                    bandaElevadoraEntities.setNoAgujeros(cursor.getString(41));
                    bandaElevadoraEntities.setDistanciaBordeBandaEstructura(cursor.getString(42));
                    bandaElevadoraEntities.setDistanciaPosteriorBandaEstructura(cursor.getString(43));
                    bandaElevadoraEntities.setDistanciaLaboFrontalCangilonEstructura(cursor.getString(44));
                    bandaElevadoraEntities.setDistanciaBordesCangilonEstructura(cursor.getString(45));
                    bandaElevadoraEntities.setTipoVentilacion(cursor.getString(46));
                    bandaElevadoraEntities.setDiametroPoleaMotrizElevadora(cursor.getString(47));
                    bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(48));
                    bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(49));
                    bandaElevadoraEntities.setTipoPoleaMotrizElevadora(cursor.getString(50));
                    bandaElevadoraEntities.setLargoEjeMotrizElevadora(cursor.getString(51));
                    bandaElevadoraEntities.setDiametroEjeMotrizElevadora(cursor.getString(52));
                    bandaElevadoraEntities.setBandaCentradaEnPoleaMotrizElevadora(cursor.getString(53));
                    bandaElevadoraEntities.setEstadoRevestimientoPoleaMotrizElevadora(cursor.getString(54));
                    bandaElevadoraEntities.setPotenciaMotorMotrizElevadora(cursor.getString(55));
                    bandaElevadoraEntities.setRpmSalidaReductorMotrizElevadora(cursor.getString(56));
                    bandaElevadoraEntities.setGuardaReductorPoleaMotrizElevadora(cursor.getString(57));
                    bandaElevadoraEntities.setDiametroPoleaColaElevadora(cursor.getString(58));
                    bandaElevadoraEntities.setAnchoPoleaColaElevadora(cursor.getString(59));
                    bandaElevadoraEntities.setTipoPoleaColaElevadora(cursor.getString(60));
                    bandaElevadoraEntities.setLargoEjePoleaColaElevadora(cursor.getString(61));
                    bandaElevadoraEntities.setDiametroEjePoleaColaElevadora(cursor.getString(62));
                    bandaElevadoraEntities.setBandaCentradaEnPoleaColaElevadora(cursor.getString(63));
                    bandaElevadoraEntities.setEstadoRevestimientoPoleaColaElevadora(cursor.getString(64));
                    bandaElevadoraEntities.setLongitudTensorTornilloPoleaColaElevadora(cursor.getString(65));
                    bandaElevadoraEntities.setLongitudRecorridoContrapesaPoleaColaElevadora(cursor.getString(66));
                    bandaElevadoraEntities.setCargaTrabajoBandaElevadora(cursor.getString(67));
                    bandaElevadoraEntities.setTemperaturaMaterialElevadora(cursor.getString(68));
                    bandaElevadoraEntities.setEmpalmeMecanicoElevadora(cursor.getString(69));
                    bandaElevadoraEntities.setDiametroRoscaElevadora(cursor.getString(70));
                    bandaElevadoraEntities.setLargoTornilloElevadora(cursor.getString(71));
                    bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(72));
                    bandaElevadoraEntities.setAnchoCabezaElevadorPuertaInspeccion(cursor.getString(73));
                    bandaElevadoraEntities.setLargoCabezaElevadorPuertaInspeccion(cursor.getString(74));
                    bandaElevadoraEntities.setAnchoBotaElevadorPuertaInspeccion(cursor.getString(75));
                    bandaElevadoraEntities.setLargoBotaElevadorPuertaInspeccion(cursor.getString(76));
                    bandaElevadoraEntities.setMonitorPeligro(cursor.getString(77));
                    bandaElevadoraEntities.setRodamiento(cursor.getString(78));
                    bandaElevadoraEntities.setMonitorVelocidad(cursor.getString(79));
                    bandaElevadoraEntities.setSensorInductivo(cursor.getString(80));
                    bandaElevadoraEntities.setIndicadorNivel(cursor.getString(81));
                    bandaElevadoraEntities.setCajaUnion(cursor.getString(82));
                    bandaElevadoraEntities.setAlarmaYPantalla(cursor.getString(83));
                    bandaElevadoraEntities.setInterruptorSeguridad(cursor.getString(84));
                    bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(85));
                    bandaElevadoraEntities.setAtaqueQuimicoElevadora(cursor.getString(86));
                    bandaElevadoraEntities.setAtaqueTemperaturaElevadora(cursor.getString(87));
                    bandaElevadoraEntities.setAtaqueAceitesElevadora(cursor.getString(88));
                    bandaElevadoraEntities.setAtaqueAbrasivoElevadora(cursor.getString(89));
                    bandaElevadoraEntities.setCapacidadElevadora(cursor.getString(90));
                    bandaElevadoraEntities.setHorasTrabajoDiaElevadora(cursor.getString(91));
                    bandaElevadoraEntities.setDiasTrabajoSemanaElevadora(cursor.getString(92));
                    bandaElevadoraEntities.setAbrasividadElevadora(cursor.getString(93));
                    bandaElevadoraEntities.setPorcentajeFinosElevadora(cursor.getString(94));
                    bandaElevadoraEntities.setMaxGranulometriaElevadora(cursor.getString(95));
                    bandaElevadoraEntities.setDensidadMaterialElevadora(cursor.getString(96));
                    bandaElevadoraEntities.setTempMaxMaterialSobreBandaElevadora(cursor.getString(97));
                    bandaElevadoraEntities.setTempPromedioMaterialSobreBandaElevadora(cursor.getString(98));
                    bandaElevadoraEntities.setVariosPuntosDeAlimentacion(cursor.getString(99));
                    bandaElevadoraEntities.setLluviaDeMaterial(cursor.getString(100));
                    bandaElevadoraEntities.setAnchoPiernaElevador(cursor.getString(101));
                    bandaElevadoraEntities.setProfundidadPiernaElevador(cursor.getString(102));
                    bandaElevadoraEntities.setTempAmbienteMin(cursor.getString(103));
                    bandaElevadoraEntities.setTempAmbienteMax(cursor.getString(104));
                    bandaElevadoraEntities.setTipoCarga(cursor.getString(105));
                    bandaElevadoraEntities.setTipoDescarga(cursor.getString(106));
                    bandaElevadoraEntities.setObservacionRegistroElevadora(cursor.getString(108));
                    bandaElevadoraEntitiesArrayList.add(bandaElevadoraEntities);

                }
                conteo = 0;

                for (int i = 0; i < bandaElevadoraEntitiesArrayList.size(); i++) {


                    String url = Constants.url + "sincronizacionElevadora ";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            conteo ++;
                            db.execSQL("update bandaElevadora set  estadoRegistroElevadora='Sincronizado' where idRegistro='" + bandaElevadoraEntitiesArrayList.get(finalI).getIdRegistro() + "'");
                            if(conteo==bandaElevadoraEntitiesArrayList.size())
                            {
                                sincronizarInsercionesHorizontal();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaElevadoraEntitiesArrayList.get(finalI).getIdRegistro());
                            params.put("marcaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaBandaElevadora());
                            params.put("anchoBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBandaElevadora());
                            params.put("distanciaEntrePoleasElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaEntrePoleasElevadora());
                            params.put("noLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getNoLonaBandaElevadora());
                            params.put("tipoLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoLonaBandaElevadora());
                            params.put("espesorTotalBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorTotalBandaElevadora());
                            params.put("espesorCojinActualElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCojinActualElevadora());
                            params.put("espesorCubiertaSuperiorElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaSuperiorElevadora());
                            params.put("espesorCubiertaInferiorElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaInferiorElevadora());
                            params.put("tipoCubiertaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCubiertaElevadora());
                            params.put("tipoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoEmpalmeElevadora());
                            params.put("estadoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoEmpalmeElevadora());
                            params.put("resistenciaRoturaLonaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getResistenciaRoturaLonaElevadora());
                            params.put("velocidadBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getVelocidadBandaElevadora());
                            params.put("marcaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaBandaElevadoraAnterior());
                            params.put("anchoBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBandaElevadoraAnterior());
                            params.put("noLonasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getNoLonasBandaElevadoraAnterior());
                            params.put("tipoLonaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoLonaBandaElevadoraAnterior());
                            params.put("espesorTotalBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorTotalBandaElevadoraAnterior());
                            params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaSuperiorBandaElevadoraAnterior());
                            params.put("espesorCojinElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCojinElevadoraAnterior());
                            params.put("espesorCubiertaInferiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaInferiorBandaElevadoraAnterior());
                            params.put("tipoCubiertaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCubiertaElevadoraAnterior());
                            params.put("tipoEmpalmeElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoEmpalmeElevadoraAnterior());
                            params.put("resistenciaRoturaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getResistenciaRoturaBandaElevadoraAnterior());
                            params.put("tonsTransportadasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTonsTransportadasBandaElevadoraAnterior());
                            params.put("velocidadBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getVelocidadBandaElevadoraAnterior());
                            params.put("causaFallaCambioBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getCausaFallaCambioBandaElevadoraAnterior());
                            params.put("pesoMaterialEnCadaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getPesoMaterialEnCadaCangilon());
                            params.put("pesoCangilonVacio", bandaElevadoraEntitiesArrayList.get(finalI).getPesoCangilonVacio());
                            params.put("longitudCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudCangilon() );
                            params.put("materialCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialCangilon());
                            params.put("tipoCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCangilon());
                            params.put("proyeccionCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getProyeccionCangilon());
                            params.put("profundidadCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getProfundidadCangilon());
                            params.put("marcaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaCangilon());
                            params.put("referenciaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getReferenciaCangilon());
                            params.put("capacidadCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getCapacidadCangilon());
                            params.put("noFilasCangilones", bandaElevadoraEntitiesArrayList.get(finalI).getNoFilasCangilones());
                            params.put("separacionCangilones", bandaElevadoraEntitiesArrayList.get(finalI).getSeparacionCangilones());
                            params.put("noAgujeros", bandaElevadoraEntitiesArrayList.get(finalI).getNoAgujeros());
                            params.put("distanciaBordeBandaEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaBordeBandaEstructura());
                            params.put("distanciaPosteriorBandaEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaPosteriorBandaEstructura());
                            params.put("distanciaLaboFrontalCangilonEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaLaboFrontalCangilonEstructura());
                            params.put("distanciaBordesCangilonEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaBordesCangilonEstructura());
                            params.put("tipoVentilacion", bandaElevadoraEntitiesArrayList.get(finalI).getTipoVentilacion());
                            params.put("diametroPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroPoleaMotrizElevadora());
                            params.put("anchoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPoleaMotrizElevadora());
                            params.put("tipoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoPoleaMotrizElevadora());
                            params.put("largoEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoEjeMotrizElevadora());
                            params.put("diametroEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroEjeMotrizElevadora());
                            params.put("bandaCentradaEnPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getBandaCentradaEnPoleaMotrizElevadora());
                            params.put("estadoRevestimientoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoRevestimientoPoleaMotrizElevadora());
                            params.put("potenciaMotorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getPotenciaMotorMotrizElevadora());
                            params.put("rpmSalidaReductorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getRpmSalidaReductorMotrizElevadora());
                            params.put("guardaReductorPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getGuardaReductorPoleaMotrizElevadora());
                            params.put("diametroPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroPoleaColaElevadora());
                            params.put("anchoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPoleaColaElevadora());
                            params.put("tipoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoPoleaColaElevadora());
                            params.put("largoEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoEjePoleaColaElevadora());
                            params.put("diametroEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroEjePoleaColaElevadora());
                            params.put("bandaCentradaEnPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getBandaCentradaEnPoleaColaElevadora());
                            params.put("estadoRevestimientoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoRevestimientoPoleaColaElevadora());
                            params.put("longitudTensorTornilloPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudTensorTornilloPoleaColaElevadora());
                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudRecorridoContrapesaPoleaColaElevadora());
                            params.put("cargaTrabajoBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getCargaTrabajoBandaElevadora());
                            params.put("temperaturaMaterialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTemperaturaMaterialElevadora());
                            params.put("empalmeMecanicoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEmpalmeMecanicoElevadora());
                            params.put("diametroRoscaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroRoscaElevadora());
                            params.put("largoTornilloElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoTornilloElevadora());
                            params.put("materialTornilloElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialTornilloElevadora());
                            params.put("anchoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoCabezaElevadorPuertaInspeccion());
                            params.put("largoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getLargoCabezaElevadorPuertaInspeccion());
                            params.put("anchoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBotaElevadorPuertaInspeccion());
                            params.put("largoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getLargoBotaElevadorPuertaInspeccion());
                            params.put("monitorPeligro", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorPeligro());
                            params.put("rodamiento", bandaElevadoraEntitiesArrayList.get(finalI).getRodamiento());
                            params.put("monitorDesalineacion", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorDesalineacion());
                            params.put("monitorVelocidad", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorVelocidad());
                            params.put("sensorInductivo", bandaElevadoraEntitiesArrayList.get(finalI).getSensorInductivo());
                            params.put("indicadorNivel", bandaElevadoraEntitiesArrayList.get(finalI).getIndicadorNivel());
                            params.put("cajaUnion", bandaElevadoraEntitiesArrayList.get(finalI).getCajaUnion());
                            params.put("alarmaYPantalla", bandaElevadoraEntitiesArrayList.get(finalI).getAlarmaYPantalla());
                            params.put("interruptorSeguridad", bandaElevadoraEntitiesArrayList.get(finalI).getInterruptorSeguridad());
                            params.put("materialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialElevadora());
                            params.put("ataqueQuimicoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueQuimicoElevadora());
                            params.put("ataqueTemperaturaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueTemperaturaElevadora());
                            params.put("ataqueAceitesElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueAceitesElevadora());
                            params.put("ataqueAbrasivoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueAbrasivoElevadora());
                            params.put("capacidadElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getCapacidadElevadora());
                            params.put("horasTrabajoDiaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getHorasTrabajoDiaElevadora());
                            params.put("diasTrabajoSemanaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiasTrabajoSemanaElevadora());
                            params.put("abrasividadElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAbrasividadElevadora());
                            params.put("porcentajeFinosElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getPorcentajeFinosElevadora());
                            params.put("maxGranulometriaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaxGranulometriaElevadora());
                            params.put("densidadMaterialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDensidadMaterialElevadora());
                            params.put("tempMaxMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTempMaxMaterialSobreBandaElevadora());
                            params.put("tempPromedioMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTempPromedioMaterialSobreBandaElevadora());
                            params.put("variosPuntosDeAlimentacion", bandaElevadoraEntitiesArrayList.get(finalI).getVariosPuntosDeAlimentacion());
                            params.put("lluviaDeMaterial", bandaElevadoraEntitiesArrayList.get(finalI).getLluviaDeMaterial());
                            params.put("anchoPiernaElevador", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPiernaElevador());
                            params.put("profundidadPiernaElevador", bandaElevadoraEntitiesArrayList.get(finalI).getProfundidadPiernaElevador());
                            params.put("tempAmbienteMin", bandaElevadoraEntitiesArrayList.get(finalI).getTempAmbienteMin());
                            params.put("tempAmbienteMax", bandaElevadoraEntitiesArrayList.get(finalI).getTempAmbienteMax());
                            params.put("tipoDescarga", bandaElevadoraEntitiesArrayList.get(finalI).getTipoDescarga());
                            params.put("tipoCarga", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCarga());
                            params.put("observacionRegistroElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getObservacionRegistroElevadora());

                            for(Map.Entry entry:params.entrySet()){
                                if(entry.getValue()==null)
                                {
                                    String campo=entry.getKey().toString();

                                    params.put(campo, "");
                                }
                            }

                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarInsercionesHorizontal()
    {
        dbHelper = new DbHelper(this, "prueba", null, 1);
        conteo = 0;

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaTransportadora where estadoRegistroTransportadora='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarActualizacionesRegistro();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaTransportadoraEntities> bandaTransportadoraEntitiesArraylist = new ArrayList<>();


            BandaTransportadoraEntities bandaTransportadoraEntities= new BandaTransportadoraEntities();
            bandaTransportadoraEntities.setIdRegistro(cursor.getString(0));
            bandaTransportadoraEntities.setMarcaBandaTransportadora(cursor.getString(1));
            bandaTransportadoraEntities.setAnchoBandaTransportadora(cursor.getString(2));
            bandaTransportadoraEntities.setNoLonasBandaTransportadora(cursor.getString(3));
            bandaTransportadoraEntities.setTipoLonaBandaTransportadora(cursor.getString(4));
            bandaTransportadoraEntities.setEspesorTotalBandaTransportadora(cursor.getString(5));
            bandaTransportadoraEntities.setEspesorCubiertaSuperiorTransportadora(cursor.getString(6));
            bandaTransportadoraEntities.setEspesorCojinTransportadora(cursor.getString(7));
            bandaTransportadoraEntities.setEspesorCubiertaInferiorTransportadora(cursor.getString(8));
            bandaTransportadoraEntities.setTipoCubiertaTransportadora(cursor.getString(9));
            bandaTransportadoraEntities.setTipoEmpalmeTransportadora(cursor.getString(10));
            bandaTransportadoraEntities.setEstadoEmpalmeTransportadora(cursor.getString(11));
            bandaTransportadoraEntities.setDistanciaEntrePoleasBandaHorizontal(cursor.getString(12));
            bandaTransportadoraEntities.setInclinacionBandaHorizontal(cursor.getString(13));
            bandaTransportadoraEntities.setRecorridoUtilTensorBandaHorizontal(cursor.getString(14));
            bandaTransportadoraEntities.setLongitudSinfinBandaHorizontal(cursor.getString(15));
            bandaTransportadoraEntities.setResistenciaRoturaLonaTransportadora(cursor.getString(16));
            bandaTransportadoraEntities.setLocalizacionTensorTransportadora(cursor.getString(17));
            bandaTransportadoraEntities.setBandaReversible(cursor.getString(18));
            bandaTransportadoraEntities.setBandaDeArrastre(cursor.getString(19));
            bandaTransportadoraEntities.setVelocidadBandaHorizontal(cursor.getString(20));
            bandaTransportadoraEntities.setMarcaBandaHorizontalAnterior(cursor.getString(21));
            bandaTransportadoraEntities.setAnchoBandaHorizontalAnterior(cursor.getString(22));
            bandaTransportadoraEntities.setNoLonasBandaHorizontalAnterior(cursor.getString(23));
            bandaTransportadoraEntities.setTipoLonaBandaHorizontalAnterior(cursor.getString(24));
            bandaTransportadoraEntities.setEspesorTotalBandaHorizontalAnterior(cursor.getString(25));
            bandaTransportadoraEntities.setEspesorCubiertaSuperiorBandaHorizontalAnterior(cursor.getString(26));
            bandaTransportadoraEntities.setEspesorCubiertaInferiorBandaHorizontalAnterior(cursor.getString(27));
            bandaTransportadoraEntities.setEspesorCojinBandaHorizontalAnterior(cursor.getString(28));
            bandaTransportadoraEntities.setTipoEmpalmeBandaHorizontalAnterior(cursor.getString(29));
            bandaTransportadoraEntities.setResistenciaRoturaLonaBandaHorizontalAnterior(cursor.getString(30));
            bandaTransportadoraEntities.setTipoCubiertaBandaHorizontalAnterior(cursor.getString(31));
            bandaTransportadoraEntities.setTonsTransportadasBandaHoizontalAnterior(cursor.getString(32));
            bandaTransportadoraEntities.setCausaFallaCambioBandaHorizontal(cursor.getString(33));
            bandaTransportadoraEntities.setDiametroPoleaColaTransportadora(cursor.getString(34));
            bandaTransportadoraEntities.setAnchoPoleaColaTransportadora(cursor.getString(35));
            bandaTransportadoraEntities.setTipoPoleaColaTransportadora(cursor.getString(36));
            bandaTransportadoraEntities.setLargoEjePoleaColaTransportadora(cursor.getString(37));
            bandaTransportadoraEntities.setDiametroEjePoleaColaHorizontal(cursor.getString(38));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaColaTransportadora(cursor.getString(39));
            bandaTransportadoraEntities.setAnguloAmarrePoleaColaTransportadora(cursor.getString(40));
            bandaTransportadoraEntities.setEstadoRvtoPoleaColaTransportadora(cursor.getString(41));
            bandaTransportadoraEntities.setTipoTransicionPoleaColaTransportadora(cursor.getString(42));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTransportadora(cursor.getString(43));
            bandaTransportadoraEntities.setLongitudTensorTornilloPoleaColaTransportadora(cursor.getString(44));
            bandaTransportadoraEntities.setLongitudRecorridoContrapesaPoleaColaTransportadora(cursor.getString(45));
            bandaTransportadoraEntities.setGuardaPoleaColaTransportadora(cursor.getString(46));
            bandaTransportadoraEntities.setHayDesviador(cursor.getString(47));
            bandaTransportadoraEntities.setElDesviadorBascula(cursor.getString(48));
            bandaTransportadoraEntities.setPresionUniformeALoAnchoDeLaBanda(cursor.getString(49));
            bandaTransportadoraEntities.setCauchoVPlow(cursor.getString(50));
            bandaTransportadoraEntities.setAnchoVPlow(cursor.getString(51));
            bandaTransportadoraEntities.setEspesorVPlow(cursor.getString(52));
            bandaTransportadoraEntities.setTipoRevestimientoTolvaCarga(cursor.getString(53));
            bandaTransportadoraEntities.setEstadoRevestimientoTolvaCarga(cursor.getString(54));
            bandaTransportadoraEntities.setDuracionPromedioRevestimiento(cursor.getString(55));
            bandaTransportadoraEntities.setDeflectores(cursor.getString(56));
            bandaTransportadoraEntities.setAltureCaida(cursor.getString(57));
            bandaTransportadoraEntities.setLongitudImpacto(cursor.getString(58));
            bandaTransportadoraEntities.setMaterial(cursor.getString(59));
            bandaTransportadoraEntities.setAnguloSobreCarga(cursor.getString(60));
            bandaTransportadoraEntities.setAtaqueQuimicoTransportadora(cursor.getString(61));
            bandaTransportadoraEntities.setAtaqueTemperaturaTransportadora(cursor.getString(62));
            bandaTransportadoraEntities.setAtaqueAceiteTransportadora(cursor.getString(63));
            bandaTransportadoraEntities.setAtaqueImpactoTransportadora(cursor.getString(64));
            bandaTransportadoraEntities.setCapacidadTransportadora(cursor.getString(65));
            bandaTransportadoraEntities.setHorasTrabajoPorDiaTransportadora(cursor.getString(66));
            bandaTransportadoraEntities.setDiasTrabajPorSemanaTransportadora(cursor.getString(67));
            bandaTransportadoraEntities.setAlimentacionCentradaTransportadora(cursor.getString(68));
            bandaTransportadoraEntities.setAbrasividadTransportadora(cursor.getString(69));
            bandaTransportadoraEntities.setPorcentajeFinosTransportadora(cursor.getString(70));
            bandaTransportadoraEntities.setMaxGranulometriaTransportadora(cursor.getString(71));
            bandaTransportadoraEntities.setMaxPesoTransportadora(cursor.getString(72));
            bandaTransportadoraEntities.setDensidadTransportadora(cursor.getString(73));
            bandaTransportadoraEntities.setTempMaximaMaterialSobreBandaTransportadora(cursor.getString(74));
            bandaTransportadoraEntities.setTempPromedioMaterialSobreBandaTransportadora(cursor.getString(75));
            bandaTransportadoraEntities.setFugaDeMaterialesEnLaColaDelChute(cursor.getString(76));
            bandaTransportadoraEntities.setFugaDeMaterialesPorLosCostados(cursor.getString(77));
            bandaTransportadoraEntities.setFugaMateriales(cursor.getString(78));
            bandaTransportadoraEntities.setCajaColaDeTolva(cursor.getString(79));
            bandaTransportadoraEntities.setFugaDeMaterialParticulaALaSalidaDelChute(cursor.getString(80));
            bandaTransportadoraEntities.setAnchoChute(cursor.getString(81));
            bandaTransportadoraEntities.setLargoChute(cursor.getString(82));
            bandaTransportadoraEntities.setAlturaChute(cursor.getString(83));
            bandaTransportadoraEntities.setAbrazadera(cursor.getString(84));
            bandaTransportadoraEntities.setCauchoGuardabandas(cursor.getString(85));
            bandaTransportadoraEntities.setTriSealMultiSeal(cursor.getString(86));
            bandaTransportadoraEntities.setEspesorGuardaBandas(cursor.getString(87));
            bandaTransportadoraEntities.setAnchoGuardaBandas(cursor.getString(88));
            bandaTransportadoraEntities.setLargoGuardaBandas(cursor.getString(89));
            bandaTransportadoraEntities.setProtectorGuardaBandas(cursor.getString(90));
            bandaTransportadoraEntities.setCortinaAntiPolvo1(cursor.getString(91));
            bandaTransportadoraEntities.setCortinaAntiPolvo2(cursor.getString(92));
            bandaTransportadoraEntities.setCortinaAntiPolvo3(cursor.getString(93));
            bandaTransportadoraEntities.setBoquillasCanonesDeAire(cursor.getString(94));
            bandaTransportadoraEntities.setTempAmbienteMaxTransportadora(cursor.getString(95));
            bandaTransportadoraEntities.setTempAmbienteMinTransportadora(cursor.getString(96));
            bandaTransportadoraEntities.setTieneRodillosImpacto(cursor.getString(97));
            bandaTransportadoraEntities.setCamaImpacto(cursor.getString(98));
            bandaTransportadoraEntities.setCamaSellado(cursor.getString(99));
            bandaTransportadoraEntities.setBasculaPesaje(cursor.getString(100));
            bandaTransportadoraEntities.setRodilloCarga(cursor.getString(101));
            bandaTransportadoraEntities.setRodilloImpacto(cursor.getString(102));
            bandaTransportadoraEntities.setBasculaASGCO(cursor.getString(103));
            bandaTransportadoraEntities.setBarraImpacto(cursor.getString(104));
            bandaTransportadoraEntities.setBarraDeslizamiento(cursor.getString(105));
            bandaTransportadoraEntities.setEspesorUHMV(cursor.getString(106));
            bandaTransportadoraEntities.setAnchoBarra(cursor.getString(107));
            bandaTransportadoraEntities.setLargoBarra(cursor.getString(108));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1(cursor.getString(109));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2(cursor.getString(110));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3(cursor.getString(111));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1AntesPoleaMotriz(cursor.getString(112));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2AntesPoleaMotriz(cursor.getString(113));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3AntesPoleaMotriz(cursor.getString(114));
            bandaTransportadoraEntities.setIntegridadSoportesRodilloImpacto(cursor.getString(115));
            bandaTransportadoraEntities.setMaterialAtrapadoEntreCortinas(cursor.getString(116));
            bandaTransportadoraEntities.setMaterialAtrapadoEntreGuardabandas(cursor.getString(117));
            bandaTransportadoraEntities.setMaterialAtrapadoEnBanda(cursor.getString(118));
            bandaTransportadoraEntities.setIntegridadSoportesCamaImpacto(cursor.getString(119));
            bandaTransportadoraEntities.setInclinacionZonaCargue(cursor.getString(120));
            bandaTransportadoraEntities.setSistemaAlineacionCarga(cursor.getString(121));
            bandaTransportadoraEntities.setCantidadSistemaAlineacionEnCarga(cursor.getString(122));
            bandaTransportadoraEntities.setSistemasAlineacionCargaFuncionando(cursor.getString(123));
            bandaTransportadoraEntities.setSistemaAlineacionEnRetorno(cursor.getString(124));
            bandaTransportadoraEntities.setCantidadSistemaAlineacionEnRetorno(cursor.getString(125));
            bandaTransportadoraEntities.setSistemasAlineacionRetornoFuncionando(cursor.getString(126));
            bandaTransportadoraEntities.setSistemaAlineacionRetornoPlano(cursor.getString(127));
            bandaTransportadoraEntities.setSistemaAlineacionArtesaCarga(cursor.getString(128));
            bandaTransportadoraEntities.setSistemaAlineacionRetornoEnV(cursor.getString(129));
            bandaTransportadoraEntities.setLargoEjeRodilloCentralCarga(cursor.getString(130));
            bandaTransportadoraEntities.setDiametroEjeRodilloCentralCarga(cursor.getString(131));
            bandaTransportadoraEntities.setLargoTuboRodilloCentralCarga(cursor.getString(132));
            bandaTransportadoraEntities.setLargoEjeRodilloLateralCarga(cursor.getString(133));
            bandaTransportadoraEntities.setDiametroEjeRodilloLateralCarga(cursor.getString(134));
            bandaTransportadoraEntities.setDiametroRodilloLateralCarga(cursor.getString(135));
            bandaTransportadoraEntities.setLargoTuboRodilloLateralCarga(cursor.getString(136));
            bandaTransportadoraEntities.setTipoRodilloCarga(cursor.getString(137));
            bandaTransportadoraEntities.setDistanciaEntreArtesasCarga(cursor.getString(138));
            bandaTransportadoraEntities.setAnchoInternoChasisRodilloCarga(cursor.getString(139));
            bandaTransportadoraEntities.setAnchoExternoChasisRodilloCarga(cursor.getString(140));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesaCArga(cursor.getString(141));
            bandaTransportadoraEntities.setDetalleRodilloCentralCarga(cursor.getString(142));
            bandaTransportadoraEntities.setDetalleRodilloLateralCarg(cursor.getString(143));
            bandaTransportadoraEntities.setDiametroPoleaMotrizTransportadora(cursor.getString(144));
            bandaTransportadoraEntities.setAnchoPoleaMotrizTransportadora(cursor.getString(145));
            bandaTransportadoraEntities.setTipoPoleaMotrizTransportadora(cursor.getString(146));
            bandaTransportadoraEntities.setLargoEjePoleaMotrizTransportadora(cursor.getString(147));
            bandaTransportadoraEntities.setDiametroEjeMotrizTransportadora(cursor.getString(148));
            bandaTransportadoraEntities.setIcobandasCentraEnPoleaMotrizTransportadora(cursor.getString(149));
            bandaTransportadoraEntities.setAnguloAmarrePoleaMotrizTransportadora(cursor.getString(150));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaMotrizTransportadora(cursor.getString(151));
            bandaTransportadoraEntities.setTipoTransicionPoleaMotrizTransportadora(cursor.getString(152));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaMotrizTransportadora(cursor.getString(153));
            bandaTransportadoraEntities.setPotenciaMotorTransportadora(cursor.getString(154));
            bandaTransportadoraEntities.setGuardaPoleaMotrizTransportadora(cursor.getString(155));
            bandaTransportadoraEntities.setAnchoEstructura(cursor.getString(156));
            bandaTransportadoraEntities.setAnchoTrayectoCarga(cursor.getString(157));
            bandaTransportadoraEntities.setPasarelaRespectoAvanceBanda(cursor.getString(158));
            bandaTransportadoraEntities.setMaterialAlimenticioTransportadora(cursor.getString(159));
            bandaTransportadoraEntities.setMaterialAcidoTransportadora(cursor.getString(160));
            bandaTransportadoraEntities.setMaterialTempEntre80y150Transportadora(cursor.getString(161));
            bandaTransportadoraEntities.setMaterialSecoTransportadora(cursor.getString(162));
            bandaTransportadoraEntities.setMaterialHumedoTransportadora(cursor.getString(163));
            bandaTransportadoraEntities.setMaterialAbrasivoFinoTransportadora(cursor.getString(164));
            bandaTransportadoraEntities.setMaterialPegajosoTransportadora(cursor.getString(165));
            bandaTransportadoraEntities.setMaterialGrasosoAceitosoTransportadora(cursor.getString(166));
            bandaTransportadoraEntities.setMarcaLimpiadorPrimario(cursor.getString(167));
            bandaTransportadoraEntities.setReferenciaLimpiadorPrimario(cursor.getString(168));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorPrimario(cursor.getString(169));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorPrimario(cursor.getString(170));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorPrimario(cursor.getString(171));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorPrimario(cursor.getString(172));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorPrimario(cursor.getString(173));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla(cursor.getString(174));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda(cursor.getString(175));
            bandaTransportadoraEntities.setMarcaLimpiadorSecundario(cursor.getString(176));
            bandaTransportadoraEntities.setReferenciaLimpiadorSecundario(cursor.getString(177));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorSecundario(cursor.getString(178));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorSecundario(cursor.getString(179));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorSecundario(cursor.getString(180));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorSecundario(cursor.getString(181));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorSecundario(cursor.getString(182));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla1(cursor.getString(183));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda1(cursor.getString(184));
            bandaTransportadoraEntities.setSistemaDribbleChute(cursor.getString(185));
            bandaTransportadoraEntities.setMarcaLimpiadorTerciario(cursor.getString(186));
            bandaTransportadoraEntities.setReferenciaLimpiadorTerciario(cursor.getString(187));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorTerciario(cursor.getString(188));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorTerciario(cursor.getString(189));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorTerciario(cursor.getString(190));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorTerciario(cursor.getString(191));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorTerciario(cursor.getString(192));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla2(cursor.getString(193));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda2(cursor.getString(194));
            bandaTransportadoraEntities.setEstadoRodilloRetorno(cursor.getString(195));
            bandaTransportadoraEntities.setLargoEjeRodilloRetorno(cursor.getString(196));
            bandaTransportadoraEntities.setDiametroEjeRodilloRetorno(cursor.getString(197));
            bandaTransportadoraEntities.setDiametroRodilloRetorno(cursor.getString(198));
            bandaTransportadoraEntities.setLargoTuboRodilloRetorno(cursor.getString(199));
            bandaTransportadoraEntities.setTipoRodilloRetorno(cursor.getString(200));
            bandaTransportadoraEntities.setDistanciaEntreRodillosRetorno(cursor.getString(201));
            bandaTransportadoraEntities.setAnchoInternoChasisRetorno(cursor.getString(202));
            bandaTransportadoraEntities.setAnchoExternoChasisRetorno(cursor.getString(203));
            bandaTransportadoraEntities.setDetalleRodilloRetorno(cursor.getString(204));
            bandaTransportadoraEntities.setDiametroPoleaAmarrePoleaMotriz(cursor.getString(205));
            bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaMotriz(cursor.getString(206));
            bandaTransportadoraEntities.setTipoPoleaAmarrePoleaMotriz(cursor.getString(207));
            bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaMotriz(cursor.getString(208));
            bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaMotriz(cursor.getString(209));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaMotriz(cursor.getString(210));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaMotriz(cursor.getString(211));
            bandaTransportadoraEntities.setDimetroPoleaAmarrePoleaCola(cursor.getString(212));
            bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaCola(cursor.getString(213));
            bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaCola(cursor.getString(214));
            bandaTransportadoraEntities.setTipoPoleaAmarrePoleaCola(cursor.getString(215));
            bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaCola(cursor.getString(216));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaCola(cursor.getString(217));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaCola(cursor.getString(218));
            bandaTransportadoraEntities.setDiametroPoleaTensora(cursor.getString(219));
            bandaTransportadoraEntities.setAnchoPoleaTensora(cursor.getString(220));
            bandaTransportadoraEntities.setTipoPoleaTensora(cursor.getString(221));
            bandaTransportadoraEntities.setLargoEjePoleaTensora(cursor.getString(222));
            bandaTransportadoraEntities.setDiametroEjePoleaTensora(cursor.getString(223));
            bandaTransportadoraEntities.setIcobandasCentradaEnPoleaTensora(cursor.getString(224));
            bandaTransportadoraEntities.setRecorridoPoleaTensora(cursor.getString(225));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaTensora(cursor.getString(226));
            bandaTransportadoraEntities.setTipoTransicionPoleaTensora(cursor.getString(227));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTensora(cursor.getString(228));
            bandaTransportadoraEntities.setPotenciaMotorPoleaTensora(cursor.getString(229));
            bandaTransportadoraEntities.setGuardaPoleaTensora(cursor.getString(230));
            bandaTransportadoraEntities.setPuertasInspeccion(cursor.getString(231));
            bandaTransportadoraEntities.setGuardaRodilloRetornoPlano(cursor.getString(232));
            bandaTransportadoraEntities.setGuardaTruTrainer(cursor.getString(233));
            bandaTransportadoraEntities.setGuardaPoleaDeflectora(cursor.getString(234));
            bandaTransportadoraEntities.setGuardaZonaDeTransito(cursor.getString(235));
            bandaTransportadoraEntities.setGuardaMotores(cursor.getString(236));
            bandaTransportadoraEntities.setGuardaCadenas(cursor.getString(237));
            bandaTransportadoraEntities.setGuardaCorreas(cursor.getString(238));
            bandaTransportadoraEntities.setInterruptoresDeSeguridad(cursor.getString(239));
            bandaTransportadoraEntities.setSirenasDeSeguridad(cursor.getString(240));
            bandaTransportadoraEntities.setGuardaRodilloRetornoV(cursor.getString(241));
            bandaTransportadoraEntities.setDiametroRodilloCentralCarga(cursor.getString(242));
            bandaTransportadoraEntities.setTipoRodilloImpacto(cursor.getString(243));
            bandaTransportadoraEntities.setIntegridadSoporteCamaSellado(cursor.getString(244));
            bandaTransportadoraEntities.setAtaqueAbrasivoTransportadora(cursor.getString(245));
            bandaTransportadoraEntities.setObservacionRegistroTransportadora(cursor.getString(247));
            bandaTransportadoraEntitiesArraylist.add(bandaTransportadoraEntities);

            db.execSQL("Update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "sincronizarHorizontal";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarActualizacionesRegistro();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaTransportadoraEntitiesArraylist.get(0).getIdRegistro());
                    params.put("marcaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMarcaBandaTransportadora());
                    params.put("anchoBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBandaTransportadora());
                    params.put("noLonasBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getNoLonasBandaTransportadora());
                    params.put("tipoLonaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoLonaBandaTransportadora());
                    params.put("espesorTotalBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorTotalBandaTransportadora());
                    params.put("espesorCubiertaSuperiorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaSuperiorTransportadora());
                    params.put("espesorCojinTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCojinTransportadora());
                    params.put("espesorCubiertaInferiorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaInferiorTransportadora());
                    params.put("tipoCubiertaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoCubiertaTransportadora());
                    params.put("tipoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoEmpalmeTransportadora());
                    params.put("estadoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoEmpalmeTransportadora());
                    params.put("distanciaEntrePoleasBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntrePoleasBandaHorizontal());
                    params.put("inclinacionBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getInclinacionBandaHorizontal());
                    params.put("recorridoUtilTensorBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getRecorridoUtilTensorBandaHorizontal());
                    params.put("longitudSinfinBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getLongitudSinfinBandaHorizontal());
                    params.put("resistenciaRoturaLonaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getResistenciaRoturaLonaTransportadora());
                    params.put("localizacionTensorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLocalizacionTensorTransportadora());
                    params.put("bandaReversible", bandaTransportadoraEntitiesArraylist.get(0).getBandaReversible());
                    params.put("bandaDeArrastre", bandaTransportadoraEntitiesArraylist.get(0).getBandaDeArrastre());
                    params.put("velocidadBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getVelocidadBandaHorizontal());
                    params.put("marcaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getMarcaBandaHorizontalAnterior());
                    params.put("anchoBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBandaHorizontalAnterior());
                    params.put("noLonasBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getNoLonasBandaHorizontalAnterior());
                    params.put("tipoLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoLonaBandaHorizontalAnterior());
                    params.put("espesorTotalBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorTotalBandaHorizontalAnterior());
                    params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaSuperiorBandaHorizontalAnterior());
                    params.put("espesorCubiertaInferiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaInferiorBandaHorizontalAnterior());
                    params.put("espesorCojinBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCojinBandaHorizontalAnterior());
                    params.put("tipoEmpalmeBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoEmpalmeBandaHorizontalAnterior());
                    params.put("resistenciaRoturaLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getResistenciaRoturaLonaBandaHorizontalAnterior());
                    params.put("tipoCubiertaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoCubiertaBandaHorizontalAnterior());
                    params.put("tonsTransportadasBandaHoizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTonsTransportadasBandaHoizontalAnterior());
                    params.put("causaFallaCambioBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getCausaFallaCambioBandaHorizontal());
                    params.put("diametroPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaColaTransportadora());
                    params.put("anchoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaColaTransportadora());
                    params.put("tipoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaColaTransportadora());
                    params.put("largoEjePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaColaTransportadora());
                    params.put("diametroEjePoleaColaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaColaHorizontal());
                    params.put("icobandasCentradaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaColaTransportadora());
                    params.put("anguloAmarrePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAmarrePoleaColaTransportadora());
                    params.put("estadoRvtoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRvtoPoleaColaTransportadora());
                    params.put("tipoTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaColaTransportadora());
                    params.put("distanciaTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaColaTransportadora());
                    params.put("longitudTensorTornilloPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLongitudTensorTornilloPoleaColaTransportadora());
                    params.put("longitudRecorridoContrapesaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLongitudRecorridoContrapesaPoleaColaTransportadora());
                    params.put("guardaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaColaTransportadora());
                    params.put("hayDesviador", bandaTransportadoraEntitiesArraylist.get(0).getHayDesviador());
                    params.put("elDesviadorBascula", bandaTransportadoraEntitiesArraylist.get(0).getElDesviadorBascula());
                    params.put("presionUniformeALoAnchoDeLaBanda", bandaTransportadoraEntitiesArraylist.get(0).getPresionUniformeALoAnchoDeLaBanda());
                    params.put("cauchoVPlow", bandaTransportadoraEntitiesArraylist.get(0).getCauchoVPlow());
                    params.put("anchoVPlow", bandaTransportadoraEntitiesArraylist.get(0).getAnchoVPlow());
                    params.put("espesorVPlow", bandaTransportadoraEntitiesArraylist.get(0).getEspesorVPlow());
                    params.put("tipoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(0).getTipoRevestimientoTolvaCarga());
                    params.put("estadoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoTolvaCarga());
                    params.put("duracionPromedioRevestimiento", bandaTransportadoraEntitiesArraylist.get(0).getDuracionPromedioRevestimiento());
                    params.put("deflectores", bandaTransportadoraEntitiesArraylist.get(0).getDeflectores());
                    params.put("altureCaida", bandaTransportadoraEntitiesArraylist.get(0).getAltureCaida());
                    params.put("longitudImpacto", bandaTransportadoraEntitiesArraylist.get(0).getLongitudImpacto());
                    params.put("material", bandaTransportadoraEntitiesArraylist.get(0).getMaterial());
                    params.put("anguloSobreCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnguloSobreCarga());
                    params.put("ataqueQuimicoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueQuimicoTransportadora());
                    params.put("ataqueTemperaturaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueTemperaturaTransportadora());
                    params.put("ataqueAceiteTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueAceiteTransportadora());
                    params.put("ataqueImpactoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueImpactoTransportadora());
                    params.put("capacidadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getCapacidadTransportadora());
                    params.put("horasTrabajoPorDiaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getHorasTrabajoPorDiaTransportadora());
                    params.put("diasTrabajPorSemanaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiasTrabajPorSemanaTransportadora());
                    params.put("alimentacionCentradaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAlimentacionCentradaTransportadora());
                    params.put("abrasividadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAbrasividadTransportadora());
                    params.put("porcentajeFinosTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getPorcentajeFinosTransportadora());
                    params.put("maxGranulometriaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaxGranulometriaTransportadora());
                    params.put("maxPesoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaxPesoTransportadora());
                    params.put("densidadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDensidadTransportadora());
                    params.put("tempMaximaMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempMaximaMaterialSobreBandaTransportadora());
                    params.put("tempPromedioMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempPromedioMaterialSobreBandaTransportadora());
                    params.put("fugaDeMaterialesEnLaColaDelChute", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialesEnLaColaDelChute());
                    params.put("fugaDeMaterialesPorLosCostados", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialesPorLosCostados());
                    params.put("fugaMateriales", bandaTransportadoraEntitiesArraylist.get(0).getFugaMateriales());
                    params.put("cajaColaDeTolva", bandaTransportadoraEntitiesArraylist.get(0).getCajaColaDeTolva());
                    params.put("fugaDeMaterialParticulaALaSalidaDelChute", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialParticulaALaSalidaDelChute());
                    params.put("anchoChute", bandaTransportadoraEntitiesArraylist.get(0).getAnchoChute());
                    params.put("largoChute", bandaTransportadoraEntitiesArraylist.get(0).getLargoChute());
                    params.put("alturaChute", bandaTransportadoraEntitiesArraylist.get(0).getAlturaChute());
                    params.put("abrazadera", bandaTransportadoraEntitiesArraylist.get(0).getAbrazadera());
                    params.put("cauchoGuardabandas", bandaTransportadoraEntitiesArraylist.get(0).getCauchoGuardabandas());
                    params.put("triSealMultiSeal", bandaTransportadoraEntitiesArraylist.get(0).getTriSealMultiSeal());
                    params.put("espesorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getEspesorGuardaBandas());
                    params.put("anchoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getAnchoGuardaBandas());
                    params.put("largoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getLargoGuardaBandas());
                    params.put("protectorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getProtectorGuardaBandas());
                    params.put("cortinaAntiPolvo1", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo1());
                    params.put("cortinaAntiPolvo2", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo2());
                    params.put("cortinaAntiPolvo3", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo3());
                    params.put("boquillasCanonesDeAire", bandaTransportadoraEntitiesArraylist.get(0).getBoquillasCanonesDeAire());
                    params.put("tempAmbienteMaxTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempAmbienteMaxTransportadora());
                    params.put("tempAmbienteMinTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempAmbienteMinTransportadora());
                    params.put("tieneRodillosImpacto", bandaTransportadoraEntitiesArraylist.get(0).getTieneRodillosImpacto());
                    params.put("camaImpacto", bandaTransportadoraEntitiesArraylist.get(0).getCamaImpacto());
                    params.put("camaSellado", bandaTransportadoraEntitiesArraylist.get(0).getCamaSellado());
                    params.put("basculaPesaje", bandaTransportadoraEntitiesArraylist.get(0).getBasculaPesaje());
                    params.put("rodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getRodilloCarga());
                    params.put("rodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getRodilloImpacto());
                    params.put("basculaASGCO", bandaTransportadoraEntitiesArraylist.get(0).getBasculaASGCO());
                    params.put("barraImpacto", bandaTransportadoraEntitiesArraylist.get(0).getBarraImpacto());
                    params.put("barraDeslizamiento", bandaTransportadoraEntitiesArraylist.get(0).getBarraDeslizamiento());
                    params.put("espesorUHMV", bandaTransportadoraEntitiesArraylist.get(0).getEspesorUHMV());
                    params.put("anchoBarra", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBarra());
                    params.put("largoBarra", bandaTransportadoraEntitiesArraylist.get(0).getLargoBarra());
                    params.put("anguloAcanalamientoArtesa1", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa1());
                    params.put("anguloAcanalamientoArtesa2", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa2());
                    params.put("anguloAcanalamientoArtesa3", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa3());
                    params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa1AntesPoleaMotriz());
                    params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa2AntesPoleaMotriz());
                    params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa3AntesPoleaMotriz());
                    params.put("integridadSoportesRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoportesRodilloImpacto());
                    params.put("materialAtrapadoEntreCortinas", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEntreCortinas());
                    params.put("materialAtrapadoEntreGuardabandas", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEntreGuardabandas());
                    params.put("materialAtrapadoEnBanda", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEnBanda());
                    params.put("integridadSoportesCamaImpacto", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoportesCamaImpacto());
                    params.put("inclinacionZonaCargue", bandaTransportadoraEntitiesArraylist.get(0).getInclinacionZonaCargue());
                    params.put("sistemaAlineacionCarga", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionCarga());
                    params.put("cantidadSistemaAlineacionEnCarga", bandaTransportadoraEntitiesArraylist.get(0).getCantidadSistemaAlineacionEnCarga());
                    params.put("sistemasAlineacionCargaFuncionando", bandaTransportadoraEntitiesArraylist.get(0).getSistemasAlineacionCargaFuncionando());
                    params.put("sistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionEnRetorno());
                    params.put("cantidadSistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(0).getCantidadSistemaAlineacionEnRetorno());
                    params.put("sistemasAlineacionRetornoFuncionando", bandaTransportadoraEntitiesArraylist.get(0).getSistemasAlineacionRetornoFuncionando());
                    params.put("sistemaAlineacionRetornoPlano", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionRetornoPlano());
                    params.put("sistemaAlineacionArtesaCarga", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionArtesaCarga());
                    params.put("sistemaAlineacionRetornoEnV", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionRetornoEnV());
                    params.put("largoEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloCentralCarga());
                    params.put("diametroEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloCentralCarga());
                    params.put("largoTuboRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloCentralCarga());
                    params.put("largoEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloLateralCarga());
                    params.put("diametroEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloLateralCarga());
                    params.put("diametroRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloLateralCarga());
                    params.put("largoTuboRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloLateralCarga());
                    params.put("tipoRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloCarga());
                    params.put("distanciaEntreArtesasCarga", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntreArtesasCarga());
                    params.put("anchoInternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoInternoChasisRodilloCarga());
                    params.put("anchoExternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoExternoChasisRodilloCarga());
                    params.put("anguloAcanalamientoArtesaCArga", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesaCArga());
                    params.put("detalleRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloCentralCarga());
                    params.put("detalleRodilloLateralCarg", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloLateralCarg());
                    params.put("diametroPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaMotrizTransportadora());
                    params.put("anchoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaMotrizTransportadora());
                    params.put("tipoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaMotrizTransportadora());
                    params.put("largoEjePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaMotrizTransportadora());
                    params.put("diametroEjeMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeMotrizTransportadora());
                    params.put("icobandasCentraEnPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentraEnPoleaMotrizTransportadora());
                    params.put("anguloAmarrePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAmarrePoleaMotrizTransportadora());
                    params.put("estadoRevestimientoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaMotrizTransportadora());
                    params.put("tipoTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaMotrizTransportadora());
                    params.put("distanciaTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaMotrizTransportadora());
                    params.put("potenciaMotorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getPotenciaMotorTransportadora());
                    params.put("guardaPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaMotrizTransportadora());
                    params.put("anchoEstructura", bandaTransportadoraEntitiesArraylist.get(0).getAnchoEstructura());
                    params.put("anchoTrayectoCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoTrayectoCarga());
                    params.put("pasarelaRespectoAvanceBanda", bandaTransportadoraEntitiesArraylist.get(0).getPasarelaRespectoAvanceBanda());
                    params.put("materialAlimenticioTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAlimenticioTransportadora());
                    params.put("materialAcidoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAcidoTransportadora());
                    params.put("materialTempEntre80y150Transportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialTempEntre80y150Transportadora());
                    params.put("materialSecoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialSecoTransportadora());
                    params.put("materialHumedoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialHumedoTransportadora());
                    params.put("materialAbrasivoFinoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAbrasivoFinoTransportadora());
                    params.put("materialPegajosoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialPegajosoTransportadora());
                    params.put("materialGrasosoAceitosoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialGrasosoAceitosoTransportadora());
                    params.put("marcaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorPrimario());
                    params.put("referenciaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorPrimario());
                    params.put("anchoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorPrimario());
                    params.put("altoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorPrimario());
                    params.put("estadoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorPrimario());
                    params.put("estadoTensorLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorPrimario());
                    params.put("estadoTuboLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorPrimario());
                    params.put("frecuenciaRevisionCuchilla", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla());
                    params.put("cuchillaEnContactoConBanda", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda());
                    params.put("marcaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorSecundario());
                    params.put("referenciaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorSecundario());
                    params.put("anchoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorSecundario());
                    params.put("altoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorSecundario());
                    params.put("estadoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorSecundario());
                    params.put("estadoTensorLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorSecundario());
                    params.put("estadoTuboLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorSecundario());
                    params.put("frecuenciaRevisionCuchilla1", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla1());
                    params.put("cuchillaEnContactoConBanda1", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda1());
                    params.put("sistemaDribbleChute", bandaTransportadoraEntitiesArraylist.get(0).getSistemaDribbleChute());
                    params.put("marcaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorTerciario());
                    params.put("referenciaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorTerciario());
                    params.put("anchoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorTerciario());
                    params.put("altoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorTerciario());
                    params.put("estadoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorTerciario());
                    params.put("estadoTensorLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorTerciario());
                    params.put("estadoTuboLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorTerciario());
                    params.put("frecuenciaRevisionCuchilla2", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla2());
                    params.put("cuchillaEnContactoConBanda2", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda2());
                    params.put("estadoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRodilloRetorno());
                    params.put("largoEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloRetorno());
                    params.put("diametroEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloRetorno());
                    params.put("diametroRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloRetorno());
                    params.put("largoTuboRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloRetorno());
                    params.put("tipoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloRetorno());
                    params.put("distanciaEntreRodillosRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntreRodillosRetorno());
                    params.put("anchoInternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(0).getAnchoInternoChasisRetorno());
                    params.put("anchoExternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(0).getAnchoExternoChasisRetorno());
                    params.put("detalleRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloRetorno());
                    params.put("diametroPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaAmarrePoleaMotriz());
                    params.put("anchoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaAmarrePoleaMotriz());
                    params.put("tipoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaAmarrePoleaMotriz());
                    params.put("largoEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaAmarrePoleaMotriz());
                    params.put("diametroEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaAmarrePoleaMotriz());
                    params.put("icobandasCentradaPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaAmarrePoleaMotriz());
                    params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaAmarrePoleaMotriz());
                    params.put("dimetroPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getDimetroPoleaAmarrePoleaCola());
                    params.put("anchoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaAmarrePoleaCola());
                    params.put("largoEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaAmarrePoleaCola());
                    params.put("tipoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaAmarrePoleaCola());
                    params.put("diametroEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaAmarrePoleaCola());
                    params.put("icobandasCentradaPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaAmarrePoleaCola());
                    params.put("estadoRevestimientoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaAmarrePoleaCola());
                    params.put("diametroPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaTensora());
                    params.put("anchoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaTensora());
                    params.put("tipoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaTensora());
                    params.put("largoEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaTensora());
                    params.put("diametroEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaTensora());
                    params.put("icobandasCentradaEnPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaEnPoleaTensora());
                    params.put("recorridoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getRecorridoPoleaTensora());
                    params.put("estadoRevestimientoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaTensora());
                    params.put("tipoTransicionPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaTensora());
                    params.put("distanciaTransicionPoleaColaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaColaTensora());
                    params.put("potenciaMotorPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getPotenciaMotorPoleaTensora());
                    params.put("guardaPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaTensora());
                    params.put("puertasInspeccion", bandaTransportadoraEntitiesArraylist.get(0).getPuertasInspeccion());
                    params.put("guardaRodilloRetornoPlano", bandaTransportadoraEntitiesArraylist.get(0).getGuardaRodilloRetornoPlano());
                    params.put("guardaTruTrainer", bandaTransportadoraEntitiesArraylist.get(0).getGuardaTruTrainer());
                    params.put("guardaPoleaDeflectora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaDeflectora());
                    params.put("guardaZonaDeTransito", bandaTransportadoraEntitiesArraylist.get(0).getGuardaZonaDeTransito());
                    params.put("guardaMotores", bandaTransportadoraEntitiesArraylist.get(0).getGuardaMotores());
                    params.put("guardaCadenas", bandaTransportadoraEntitiesArraylist.get(0).getGuardaCadenas());
                    params.put("guardaCorreas", bandaTransportadoraEntitiesArraylist.get(0).getGuardaCorreas());
                    params.put("interruptoresDeSeguridad", bandaTransportadoraEntitiesArraylist.get(0).getInterruptoresDeSeguridad());
                    params.put("sirenasDeSeguridad", bandaTransportadoraEntitiesArraylist.get(0).getSirenasDeSeguridad());
                    params.put("guardaRodilloRetornoV", bandaTransportadoraEntitiesArraylist.get(0).getGuardaRodilloRetornoV());
                    params.put("diametroRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloCentralCarga());
                    params.put("tipoRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloImpacto());
                    params.put("integridadSoporteCamaSellado", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoporteCamaSellado());
                    params.put("ataqueAbrasivoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueAbrasivoTransportadora());
                    params.put("observacionRegistroTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getObservacionRegistroTransportadora());

                    Log.e("PARAMS", params.toString());

                    for(Map.Entry entry:params.entrySet()){
                        if(entry.getValue()==null)
                        {
                            String campo=entry.getKey().toString();

                            params.put(campo, "");
                        }
                    }

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<BandaTransportadoraEntities> bandaTransportadoraEntitiesArraylist = new ArrayList<>();


                while (cursor.moveToNext()) {

            
                    BandaTransportadoraEntities bandaTransportadoraEntities= new BandaTransportadoraEntities();
                    bandaTransportadoraEntities.setIdRegistro(cursor.getString(0));
                    bandaTransportadoraEntities.setMarcaBandaTransportadora(cursor.getString(1));
                    bandaTransportadoraEntities.setAnchoBandaTransportadora(cursor.getString(2));
                    bandaTransportadoraEntities.setNoLonasBandaTransportadora(cursor.getString(3));
                    bandaTransportadoraEntities.setTipoLonaBandaTransportadora(cursor.getString(4));
                    bandaTransportadoraEntities.setEspesorTotalBandaTransportadora(cursor.getString(5));
                    bandaTransportadoraEntities.setEspesorCubiertaSuperiorTransportadora(cursor.getString(6));
                    bandaTransportadoraEntities.setEspesorCojinTransportadora(cursor.getString(7));
                    bandaTransportadoraEntities.setEspesorCubiertaInferiorTransportadora(cursor.getString(8));
                    bandaTransportadoraEntities.setTipoCubiertaTransportadora(cursor.getString(9));
                    bandaTransportadoraEntities.setTipoEmpalmeTransportadora(cursor.getString(10));
                    bandaTransportadoraEntities.setEstadoEmpalmeTransportadora(cursor.getString(11));
                    bandaTransportadoraEntities.setDistanciaEntrePoleasBandaHorizontal(cursor.getString(12));
                    bandaTransportadoraEntities.setInclinacionBandaHorizontal(cursor.getString(13));
                    bandaTransportadoraEntities.setRecorridoUtilTensorBandaHorizontal(cursor.getString(14));
                    bandaTransportadoraEntities.setLongitudSinfinBandaHorizontal(cursor.getString(15));
                    bandaTransportadoraEntities.setResistenciaRoturaLonaTransportadora(cursor.getString(16));
                    bandaTransportadoraEntities.setLocalizacionTensorTransportadora(cursor.getString(17));
                    bandaTransportadoraEntities.setBandaReversible(cursor.getString(18));
                    bandaTransportadoraEntities.setBandaDeArrastre(cursor.getString(19));
                    bandaTransportadoraEntities.setVelocidadBandaHorizontal(cursor.getString(20));
                    bandaTransportadoraEntities.setMarcaBandaHorizontalAnterior(cursor.getString(21));
                    bandaTransportadoraEntities.setAnchoBandaHorizontalAnterior(cursor.getString(22));
                    bandaTransportadoraEntities.setNoLonasBandaHorizontalAnterior(cursor.getString(23));
                    bandaTransportadoraEntities.setTipoLonaBandaHorizontalAnterior(cursor.getString(24));
                    bandaTransportadoraEntities.setEspesorTotalBandaHorizontalAnterior(cursor.getString(25));
                    bandaTransportadoraEntities.setEspesorCubiertaSuperiorBandaHorizontalAnterior(cursor.getString(26));
                    bandaTransportadoraEntities.setEspesorCubiertaInferiorBandaHorizontalAnterior(cursor.getString(27));
                    bandaTransportadoraEntities.setEspesorCojinBandaHorizontalAnterior(cursor.getString(28));
                    bandaTransportadoraEntities.setTipoEmpalmeBandaHorizontalAnterior(cursor.getString(29));
                    bandaTransportadoraEntities.setResistenciaRoturaLonaBandaHorizontalAnterior(cursor.getString(30));
                    bandaTransportadoraEntities.setTipoCubiertaBandaHorizontalAnterior(cursor.getString(31));
                    bandaTransportadoraEntities.setTonsTransportadasBandaHoizontalAnterior(cursor.getString(32));
                    bandaTransportadoraEntities.setCausaFallaCambioBandaHorizontal(cursor.getString(33));
                    bandaTransportadoraEntities.setDiametroPoleaColaTransportadora(cursor.getString(34));
                    bandaTransportadoraEntities.setAnchoPoleaColaTransportadora(cursor.getString(35));
                    bandaTransportadoraEntities.setTipoPoleaColaTransportadora(cursor.getString(36));
                    bandaTransportadoraEntities.setLargoEjePoleaColaTransportadora(cursor.getString(37));
                    bandaTransportadoraEntities.setDiametroEjePoleaColaHorizontal(cursor.getString(38));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaColaTransportadora(cursor.getString(39));
                    bandaTransportadoraEntities.setAnguloAmarrePoleaColaTransportadora(cursor.getString(40));
                    bandaTransportadoraEntities.setEstadoRvtoPoleaColaTransportadora(cursor.getString(41));
                    bandaTransportadoraEntities.setTipoTransicionPoleaColaTransportadora(cursor.getString(42));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTransportadora(cursor.getString(43));
                    bandaTransportadoraEntities.setLongitudTensorTornilloPoleaColaTransportadora(cursor.getString(44));
                    bandaTransportadoraEntities.setLongitudRecorridoContrapesaPoleaColaTransportadora(cursor.getString(45));
                    bandaTransportadoraEntities.setGuardaPoleaColaTransportadora(cursor.getString(46));
                    bandaTransportadoraEntities.setHayDesviador(cursor.getString(47));
                    bandaTransportadoraEntities.setElDesviadorBascula(cursor.getString(48));
                    bandaTransportadoraEntities.setPresionUniformeALoAnchoDeLaBanda(cursor.getString(49));
                    bandaTransportadoraEntities.setCauchoVPlow(cursor.getString(50));
                    bandaTransportadoraEntities.setAnchoVPlow(cursor.getString(51));
                    bandaTransportadoraEntities.setEspesorVPlow(cursor.getString(52));
                    bandaTransportadoraEntities.setTipoRevestimientoTolvaCarga(cursor.getString(53));
                    bandaTransportadoraEntities.setEstadoRevestimientoTolvaCarga(cursor.getString(54));
                    bandaTransportadoraEntities.setDuracionPromedioRevestimiento(cursor.getString(55));
                    bandaTransportadoraEntities.setDeflectores(cursor.getString(56));
                    bandaTransportadoraEntities.setAltureCaida(cursor.getString(57));
                    bandaTransportadoraEntities.setLongitudImpacto(cursor.getString(58));
                    bandaTransportadoraEntities.setMaterial(cursor.getString(59));
                    bandaTransportadoraEntities.setAnguloSobreCarga(cursor.getString(60));
                    bandaTransportadoraEntities.setAtaqueQuimicoTransportadora(cursor.getString(61));
                    bandaTransportadoraEntities.setAtaqueTemperaturaTransportadora(cursor.getString(62));
                    bandaTransportadoraEntities.setAtaqueAceiteTransportadora(cursor.getString(63));
                    bandaTransportadoraEntities.setAtaqueImpactoTransportadora(cursor.getString(64));
                    bandaTransportadoraEntities.setCapacidadTransportadora(cursor.getString(65));
                    bandaTransportadoraEntities.setHorasTrabajoPorDiaTransportadora(cursor.getString(66));
                    bandaTransportadoraEntities.setDiasTrabajPorSemanaTransportadora(cursor.getString(67));
                    bandaTransportadoraEntities.setAlimentacionCentradaTransportadora(cursor.getString(68));
                    bandaTransportadoraEntities.setAbrasividadTransportadora(cursor.getString(69));
                    bandaTransportadoraEntities.setPorcentajeFinosTransportadora(cursor.getString(70));
                    bandaTransportadoraEntities.setMaxGranulometriaTransportadora(cursor.getString(71));
                    bandaTransportadoraEntities.setMaxPesoTransportadora(cursor.getString(72));
                    bandaTransportadoraEntities.setDensidadTransportadora(cursor.getString(73));
                    bandaTransportadoraEntities.setTempMaximaMaterialSobreBandaTransportadora(cursor.getString(74));
                    bandaTransportadoraEntities.setTempPromedioMaterialSobreBandaTransportadora(cursor.getString(75));
                    bandaTransportadoraEntities.setFugaDeMaterialesEnLaColaDelChute(cursor.getString(76));
                    bandaTransportadoraEntities.setFugaDeMaterialesPorLosCostados(cursor.getString(77));
                    bandaTransportadoraEntities.setFugaMateriales(cursor.getString(78));
                    bandaTransportadoraEntities.setCajaColaDeTolva(cursor.getString(79));
                    bandaTransportadoraEntities.setFugaDeMaterialParticulaALaSalidaDelChute(cursor.getString(80));
                    bandaTransportadoraEntities.setAnchoChute(cursor.getString(81));
                    bandaTransportadoraEntities.setLargoChute(cursor.getString(82));
                    bandaTransportadoraEntities.setAlturaChute(cursor.getString(83));
                    bandaTransportadoraEntities.setAbrazadera(cursor.getString(84));
                    bandaTransportadoraEntities.setCauchoGuardabandas(cursor.getString(85));
                    bandaTransportadoraEntities.setTriSealMultiSeal(cursor.getString(86));
                    bandaTransportadoraEntities.setEspesorGuardaBandas(cursor.getString(87));
                    bandaTransportadoraEntities.setAnchoGuardaBandas(cursor.getString(88));
                    bandaTransportadoraEntities.setLargoGuardaBandas(cursor.getString(89));
                    bandaTransportadoraEntities.setProtectorGuardaBandas(cursor.getString(90));
                    bandaTransportadoraEntities.setCortinaAntiPolvo1(cursor.getString(91));
                    bandaTransportadoraEntities.setCortinaAntiPolvo2(cursor.getString(92));
                    bandaTransportadoraEntities.setCortinaAntiPolvo3(cursor.getString(93));
                    bandaTransportadoraEntities.setBoquillasCanonesDeAire(cursor.getString(94));
                    bandaTransportadoraEntities.setTempAmbienteMaxTransportadora(cursor.getString(95));
                    bandaTransportadoraEntities.setTempAmbienteMinTransportadora(cursor.getString(96));
                    bandaTransportadoraEntities.setTieneRodillosImpacto(cursor.getString(97));
                    bandaTransportadoraEntities.setCamaImpacto(cursor.getString(98));
                    bandaTransportadoraEntities.setCamaSellado(cursor.getString(99));
                    bandaTransportadoraEntities.setBasculaPesaje(cursor.getString(100));
                    bandaTransportadoraEntities.setRodilloCarga(cursor.getString(101));
                    bandaTransportadoraEntities.setRodilloImpacto(cursor.getString(102));
                    bandaTransportadoraEntities.setBasculaASGCO(cursor.getString(103));
                    bandaTransportadoraEntities.setBarraImpacto(cursor.getString(104));
                    bandaTransportadoraEntities.setBarraDeslizamiento(cursor.getString(105));
                    bandaTransportadoraEntities.setEspesorUHMV(cursor.getString(106));
                    bandaTransportadoraEntities.setAnchoBarra(cursor.getString(107));
                    bandaTransportadoraEntities.setLargoBarra(cursor.getString(108));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1(cursor.getString(109));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2(cursor.getString(110));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3(cursor.getString(111));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1AntesPoleaMotriz(cursor.getString(112));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2AntesPoleaMotriz(cursor.getString(113));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3AntesPoleaMotriz(cursor.getString(114));
                    bandaTransportadoraEntities.setIntegridadSoportesRodilloImpacto(cursor.getString(115));
                    bandaTransportadoraEntities.setMaterialAtrapadoEntreCortinas(cursor.getString(116));
                    bandaTransportadoraEntities.setMaterialAtrapadoEntreGuardabandas(cursor.getString(117));
                    bandaTransportadoraEntities.setMaterialAtrapadoEnBanda(cursor.getString(118));
                    bandaTransportadoraEntities.setIntegridadSoportesCamaImpacto(cursor.getString(119));
                    bandaTransportadoraEntities.setInclinacionZonaCargue(cursor.getString(120));
                    bandaTransportadoraEntities.setSistemaAlineacionCarga(cursor.getString(121));
                    bandaTransportadoraEntities.setCantidadSistemaAlineacionEnCarga(cursor.getString(122));
                    bandaTransportadoraEntities.setSistemasAlineacionCargaFuncionando(cursor.getString(123));
                    bandaTransportadoraEntities.setSistemaAlineacionEnRetorno(cursor.getString(124));
                    bandaTransportadoraEntities.setCantidadSistemaAlineacionEnRetorno(cursor.getString(125));
                    bandaTransportadoraEntities.setSistemasAlineacionRetornoFuncionando(cursor.getString(126));
                    bandaTransportadoraEntities.setSistemaAlineacionRetornoPlano(cursor.getString(127));
                    bandaTransportadoraEntities.setSistemaAlineacionArtesaCarga(cursor.getString(128));
                    bandaTransportadoraEntities.setSistemaAlineacionRetornoEnV(cursor.getString(129));
                    bandaTransportadoraEntities.setLargoEjeRodilloCentralCarga(cursor.getString(130));
                    bandaTransportadoraEntities.setDiametroEjeRodilloCentralCarga(cursor.getString(131));
                    bandaTransportadoraEntities.setLargoTuboRodilloCentralCarga(cursor.getString(132));
                    bandaTransportadoraEntities.setLargoEjeRodilloLateralCarga(cursor.getString(133));
                    bandaTransportadoraEntities.setDiametroEjeRodilloLateralCarga(cursor.getString(134));
                    bandaTransportadoraEntities.setDiametroRodilloLateralCarga(cursor.getString(135));
                    bandaTransportadoraEntities.setLargoTuboRodilloLateralCarga(cursor.getString(136));
                    bandaTransportadoraEntities.setTipoRodilloCarga(cursor.getString(137));
                    bandaTransportadoraEntities.setDistanciaEntreArtesasCarga(cursor.getString(138));
                    bandaTransportadoraEntities.setAnchoInternoChasisRodilloCarga(cursor.getString(139));
                    bandaTransportadoraEntities.setAnchoExternoChasisRodilloCarga(cursor.getString(140));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesaCArga(cursor.getString(141));
                    bandaTransportadoraEntities.setDetalleRodilloCentralCarga(cursor.getString(142));
                    bandaTransportadoraEntities.setDetalleRodilloLateralCarg(cursor.getString(143));
                    bandaTransportadoraEntities.setDiametroPoleaMotrizTransportadora(cursor.getString(144));
                    bandaTransportadoraEntities.setAnchoPoleaMotrizTransportadora(cursor.getString(145));
                    bandaTransportadoraEntities.setTipoPoleaMotrizTransportadora(cursor.getString(146));
                    bandaTransportadoraEntities.setLargoEjePoleaMotrizTransportadora(cursor.getString(147));
                    bandaTransportadoraEntities.setDiametroEjeMotrizTransportadora(cursor.getString(148));
                    bandaTransportadoraEntities.setIcobandasCentraEnPoleaMotrizTransportadora(cursor.getString(149));
                    bandaTransportadoraEntities.setAnguloAmarrePoleaMotrizTransportadora(cursor.getString(150));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaMotrizTransportadora(cursor.getString(151));
                    bandaTransportadoraEntities.setTipoTransicionPoleaMotrizTransportadora(cursor.getString(152));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaMotrizTransportadora(cursor.getString(153));
                    bandaTransportadoraEntities.setPotenciaMotorTransportadora(cursor.getString(154));
                    bandaTransportadoraEntities.setGuardaPoleaMotrizTransportadora(cursor.getString(155));
                    bandaTransportadoraEntities.setAnchoEstructura(cursor.getString(156));
                    bandaTransportadoraEntities.setAnchoTrayectoCarga(cursor.getString(157));
                    bandaTransportadoraEntities.setPasarelaRespectoAvanceBanda(cursor.getString(158));
                    bandaTransportadoraEntities.setMaterialAlimenticioTransportadora(cursor.getString(159));
                    bandaTransportadoraEntities.setMaterialAcidoTransportadora(cursor.getString(160));
                    bandaTransportadoraEntities.setMaterialTempEntre80y150Transportadora(cursor.getString(161));
                    bandaTransportadoraEntities.setMaterialSecoTransportadora(cursor.getString(162));
                    bandaTransportadoraEntities.setMaterialHumedoTransportadora(cursor.getString(163));
                    bandaTransportadoraEntities.setMaterialAbrasivoFinoTransportadora(cursor.getString(164));
                    bandaTransportadoraEntities.setMaterialPegajosoTransportadora(cursor.getString(165));
                    bandaTransportadoraEntities.setMaterialGrasosoAceitosoTransportadora(cursor.getString(166));
                    bandaTransportadoraEntities.setMarcaLimpiadorPrimario(cursor.getString(167));
                    bandaTransportadoraEntities.setReferenciaLimpiadorPrimario(cursor.getString(168));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorPrimario(cursor.getString(169));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorPrimario(cursor.getString(170));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorPrimario(cursor.getString(171));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorPrimario(cursor.getString(172));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorPrimario(cursor.getString(173));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla(cursor.getString(174));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda(cursor.getString(175));
                    bandaTransportadoraEntities.setMarcaLimpiadorSecundario(cursor.getString(176));
                    bandaTransportadoraEntities.setReferenciaLimpiadorSecundario(cursor.getString(177));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorSecundario(cursor.getString(178));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorSecundario(cursor.getString(179));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorSecundario(cursor.getString(180));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorSecundario(cursor.getString(181));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorSecundario(cursor.getString(182));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla1(cursor.getString(183));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda1(cursor.getString(184));
                    bandaTransportadoraEntities.setSistemaDribbleChute(cursor.getString(185));
                    bandaTransportadoraEntities.setMarcaLimpiadorTerciario(cursor.getString(186));
                    bandaTransportadoraEntities.setReferenciaLimpiadorTerciario(cursor.getString(187));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorTerciario(cursor.getString(188));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorTerciario(cursor.getString(189));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorTerciario(cursor.getString(190));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorTerciario(cursor.getString(191));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorTerciario(cursor.getString(192));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla2(cursor.getString(193));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda2(cursor.getString(194));
                    bandaTransportadoraEntities.setEstadoRodilloRetorno(cursor.getString(195));
                    bandaTransportadoraEntities.setLargoEjeRodilloRetorno(cursor.getString(196));
                    bandaTransportadoraEntities.setDiametroEjeRodilloRetorno(cursor.getString(197));
                    bandaTransportadoraEntities.setDiametroRodilloRetorno(cursor.getString(198));
                    bandaTransportadoraEntities.setLargoTuboRodilloRetorno(cursor.getString(199));
                    bandaTransportadoraEntities.setTipoRodilloRetorno(cursor.getString(200));
                    bandaTransportadoraEntities.setDistanciaEntreRodillosRetorno(cursor.getString(201));
                    bandaTransportadoraEntities.setAnchoInternoChasisRetorno(cursor.getString(202));
                    bandaTransportadoraEntities.setAnchoExternoChasisRetorno(cursor.getString(203));
                    bandaTransportadoraEntities.setDetalleRodilloRetorno(cursor.getString(204));
                    bandaTransportadoraEntities.setDiametroPoleaAmarrePoleaMotriz(cursor.getString(205));
                    bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaMotriz(cursor.getString(206));
                    bandaTransportadoraEntities.setTipoPoleaAmarrePoleaMotriz(cursor.getString(207));
                    bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaMotriz(cursor.getString(208));
                    bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaMotriz(cursor.getString(209));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaMotriz(cursor.getString(210));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaMotriz(cursor.getString(211));
                    bandaTransportadoraEntities.setDimetroPoleaAmarrePoleaCola(cursor.getString(212));
                    bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaCola(cursor.getString(213));
                    bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaCola(cursor.getString(214));
                    bandaTransportadoraEntities.setTipoPoleaAmarrePoleaCola(cursor.getString(215));
                    bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaCola(cursor.getString(216));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaCola(cursor.getString(217));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaCola(cursor.getString(218));
                    bandaTransportadoraEntities.setDiametroPoleaTensora(cursor.getString(219));
                    bandaTransportadoraEntities.setAnchoPoleaTensora(cursor.getString(220));
                    bandaTransportadoraEntities.setTipoPoleaTensora(cursor.getString(221));
                    bandaTransportadoraEntities.setLargoEjePoleaTensora(cursor.getString(222));
                    bandaTransportadoraEntities.setDiametroEjePoleaTensora(cursor.getString(223));
                    bandaTransportadoraEntities.setIcobandasCentradaEnPoleaTensora(cursor.getString(224));
                    bandaTransportadoraEntities.setRecorridoPoleaTensora(cursor.getString(225));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaTensora(cursor.getString(226));
                    bandaTransportadoraEntities.setTipoTransicionPoleaTensora(cursor.getString(227));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTensora(cursor.getString(228));
                    bandaTransportadoraEntities.setPotenciaMotorPoleaTensora(cursor.getString(229));
                    bandaTransportadoraEntities.setGuardaPoleaTensora(cursor.getString(230));
                    bandaTransportadoraEntities.setPuertasInspeccion(cursor.getString(231));
                    bandaTransportadoraEntities.setGuardaRodilloRetornoPlano(cursor.getString(232));
                    bandaTransportadoraEntities.setGuardaTruTrainer(cursor.getString(233));
                    bandaTransportadoraEntities.setGuardaPoleaDeflectora(cursor.getString(234));
                    bandaTransportadoraEntities.setGuardaZonaDeTransito(cursor.getString(235));
                    bandaTransportadoraEntities.setGuardaMotores(cursor.getString(236));
                    bandaTransportadoraEntities.setGuardaCadenas(cursor.getString(237));
                    bandaTransportadoraEntities.setGuardaCorreas(cursor.getString(238));
                    bandaTransportadoraEntities.setInterruptoresDeSeguridad(cursor.getString(239));
                    bandaTransportadoraEntities.setSirenasDeSeguridad(cursor.getString(240));
                    bandaTransportadoraEntities.setGuardaRodilloRetornoV(cursor.getString(241));
                    bandaTransportadoraEntities.setDiametroRodilloCentralCarga(cursor.getString(242));
                    bandaTransportadoraEntities.setTipoRodilloImpacto(cursor.getString(243));
                    bandaTransportadoraEntities.setIntegridadSoporteCamaSellado(cursor.getString(244));
                    bandaTransportadoraEntities.setAtaqueAbrasivoTransportadora(cursor.getString(245));
                    bandaTransportadoraEntities.setObservacionRegistroTransportadora(cursor.getString(247));
                    bandaTransportadoraEntitiesArraylist.add(bandaTransportadoraEntities);
                }

                for (int i = 0; i < bandaTransportadoraEntitiesArraylist.size(); i++) {


                    String url = Constants.url + "sincronizarHorizontal";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransportadora set  estadoRegistroTransportadora='Sincronizado' where idRegistro='" + bandaTransportadoraEntitiesArraylist.get(finalI).getIdRegistro() + "'");
                            conteo++;
                            if (conteo == bandaTransportadoraEntitiesArraylist.size()) {
                                sincronizarActualizacionesRegistro();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaTransportadoraEntitiesArraylist.get(finalI).getIdRegistro());
                            params.put("marcaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaBandaTransportadora());
                            params.put("anchoBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBandaTransportadora());
                            params.put("noLonasBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getNoLonasBandaTransportadora());
                            params.put("tipoLonaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoLonaBandaTransportadora());
                            params.put("espesorTotalBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorTotalBandaTransportadora());
                            params.put("espesorCubiertaSuperiorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaSuperiorTransportadora());
                            params.put("espesorCojinTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCojinTransportadora());
                            params.put("espesorCubiertaInferiorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaInferiorTransportadora());
                            params.put("tipoCubiertaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoCubiertaTransportadora());
                            params.put("tipoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoEmpalmeTransportadora());
                            params.put("estadoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoEmpalmeTransportadora());
                            params.put("distanciaEntrePoleasBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntrePoleasBandaHorizontal());
                            params.put("inclinacionBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getInclinacionBandaHorizontal());
                            params.put("recorridoUtilTensorBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getRecorridoUtilTensorBandaHorizontal());
                            params.put("longitudSinfinBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudSinfinBandaHorizontal());
                            params.put("resistenciaRoturaLonaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getResistenciaRoturaLonaTransportadora());
                            params.put("localizacionTensorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLocalizacionTensorTransportadora());
                            params.put("bandaReversible", bandaTransportadoraEntitiesArraylist.get(finalI).getBandaReversible());
                            params.put("bandaDeArrastre", bandaTransportadoraEntitiesArraylist.get(finalI).getBandaDeArrastre());
                            params.put("velocidadBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getVelocidadBandaHorizontal());
                            params.put("marcaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaBandaHorizontalAnterior());
                            params.put("anchoBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBandaHorizontalAnterior());
                            params.put("noLonasBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getNoLonasBandaHorizontalAnterior());
                            params.put("tipoLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoLonaBandaHorizontalAnterior());
                            params.put("espesorTotalBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorTotalBandaHorizontalAnterior());
                            params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaSuperiorBandaHorizontalAnterior());
                            params.put("espesorCubiertaInferiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaInferiorBandaHorizontalAnterior());
                            params.put("espesorCojinBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCojinBandaHorizontalAnterior());
                            params.put("tipoEmpalmeBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoEmpalmeBandaHorizontalAnterior());
                            params.put("resistenciaRoturaLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getResistenciaRoturaLonaBandaHorizontalAnterior());
                            params.put("tipoCubiertaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoCubiertaBandaHorizontalAnterior());
                            params.put("tonsTransportadasBandaHoizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTonsTransportadasBandaHoizontalAnterior());
                            params.put("causaFallaCambioBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getCausaFallaCambioBandaHorizontal());
                            params.put("diametroPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaColaTransportadora());
                            params.put("anchoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaColaTransportadora());
                            params.put("tipoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaColaTransportadora());
                            params.put("largoEjePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaColaTransportadora());
                            params.put("diametroEjePoleaColaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaColaHorizontal());
                            params.put("icobandasCentradaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaColaTransportadora());
                            params.put("anguloAmarrePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAmarrePoleaColaTransportadora());
                            params.put("estadoRvtoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRvtoPoleaColaTransportadora());
                            params.put("tipoTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaColaTransportadora());
                            params.put("distanciaTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaColaTransportadora());
                            params.put("longitudTensorTornilloPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudTensorTornilloPoleaColaTransportadora());
                            params.put("longitudRecorridoContrapesaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudRecorridoContrapesaPoleaColaTransportadora());
                            params.put("guardaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaColaTransportadora());
                            params.put("hayDesviador", bandaTransportadoraEntitiesArraylist.get(finalI).getHayDesviador());
                            params.put("elDesviadorBascula", bandaTransportadoraEntitiesArraylist.get(finalI).getElDesviadorBascula());
                            params.put("presionUniformeALoAnchoDeLaBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getPresionUniformeALoAnchoDeLaBanda());
                            params.put("cauchoVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getCauchoVPlow());
                            params.put("anchoVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoVPlow());
                            params.put("espesorVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorVPlow());
                            params.put("tipoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRevestimientoTolvaCarga());
                            params.put("estadoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoTolvaCarga());
                            params.put("duracionPromedioRevestimiento", bandaTransportadoraEntitiesArraylist.get(finalI).getDuracionPromedioRevestimiento());
                            params.put("deflectores", bandaTransportadoraEntitiesArraylist.get(finalI).getDeflectores());
                            params.put("altureCaida", bandaTransportadoraEntitiesArraylist.get(finalI).getAltureCaida());
                            params.put("longitudImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudImpacto());
                            params.put("material", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterial());
                            params.put("anguloSobreCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloSobreCarga());
                            params.put("ataqueQuimicoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueQuimicoTransportadora());
                            params.put("ataqueTemperaturaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueTemperaturaTransportadora());
                            params.put("ataqueAceiteTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueAceiteTransportadora());
                            params.put("ataqueImpactoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueImpactoTransportadora());
                            params.put("capacidadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getCapacidadTransportadora());
                            params.put("horasTrabajoPorDiaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getHorasTrabajoPorDiaTransportadora());
                            params.put("diasTrabajPorSemanaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiasTrabajPorSemanaTransportadora());
                            params.put("alimentacionCentradaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAlimentacionCentradaTransportadora());
                            params.put("abrasividadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAbrasividadTransportadora());
                            params.put("porcentajeFinosTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getPorcentajeFinosTransportadora());
                            params.put("maxGranulometriaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaxGranulometriaTransportadora());
                            params.put("maxPesoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaxPesoTransportadora());
                            params.put("densidadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDensidadTransportadora());
                            params.put("tempMaximaMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempMaximaMaterialSobreBandaTransportadora());
                            params.put("tempPromedioMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempPromedioMaterialSobreBandaTransportadora());
                            params.put("fugaDeMaterialesEnLaColaDelChute", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialesEnLaColaDelChute());
                            params.put("fugaDeMaterialesPorLosCostados", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialesPorLosCostados());
                            params.put("fugaMateriales", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaMateriales());
                            params.put("cajaColaDeTolva", bandaTransportadoraEntitiesArraylist.get(finalI).getCajaColaDeTolva());
                            params.put("fugaDeMaterialParticulaALaSalidaDelChute", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialParticulaALaSalidaDelChute());
                            params.put("anchoChute", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoChute());
                            params.put("largoChute", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoChute());
                            params.put("alturaChute", bandaTransportadoraEntitiesArraylist.get(finalI).getAlturaChute());
                            params.put("abrazadera", bandaTransportadoraEntitiesArraylist.get(finalI).getAbrazadera());
                            params.put("cauchoGuardabandas", bandaTransportadoraEntitiesArraylist.get(finalI).getCauchoGuardabandas());
                            params.put("triSealMultiSeal", bandaTransportadoraEntitiesArraylist.get(finalI).getTriSealMultiSeal());
                            params.put("espesorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorGuardaBandas());
                            params.put("anchoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoGuardaBandas());
                            params.put("largoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoGuardaBandas());
                            params.put("protectorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getProtectorGuardaBandas());
                            params.put("cortinaAntiPolvo1", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo1());
                            params.put("cortinaAntiPolvo2", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo2());
                            params.put("cortinaAntiPolvo3", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo3());
                            params.put("boquillasCanonesDeAire", bandaTransportadoraEntitiesArraylist.get(finalI).getBoquillasCanonesDeAire());
                            params.put("tempAmbienteMaxTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempAmbienteMaxTransportadora());
                            params.put("tempAmbienteMinTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempAmbienteMinTransportadora());
                            params.put("tieneRodillosImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getTieneRodillosImpacto());
                            params.put("camaImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getCamaImpacto());
                            params.put("camaSellado", bandaTransportadoraEntitiesArraylist.get(finalI).getCamaSellado());
                            params.put("basculaPesaje", bandaTransportadoraEntitiesArraylist.get(finalI).getBasculaPesaje());
                            params.put("rodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getRodilloCarga());
                            params.put("rodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getRodilloImpacto());
                            params.put("basculaASGCO", bandaTransportadoraEntitiesArraylist.get(finalI).getBasculaASGCO());
                            params.put("barraImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getBarraImpacto());
                            params.put("barraDeslizamiento", bandaTransportadoraEntitiesArraylist.get(finalI).getBarraDeslizamiento());
                            params.put("espesorUHMV", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorUHMV());
                            params.put("anchoBarra", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBarra());
                            params.put("largoBarra", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoBarra());
                            params.put("anguloAcanalamientoArtesa1", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa1());
                            params.put("anguloAcanalamientoArtesa2", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa2());
                            params.put("anguloAcanalamientoArtesa3", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa3());
                            params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa1AntesPoleaMotriz());
                            params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa2AntesPoleaMotriz());
                            params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa3AntesPoleaMotriz());
                            params.put("integridadSoportesRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoportesRodilloImpacto());
                            params.put("materialAtrapadoEntreCortinas", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEntreCortinas());
                            params.put("materialAtrapadoEntreGuardabandas", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEntreGuardabandas());
                            params.put("materialAtrapadoEnBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEnBanda());
                            params.put("integridadSoportesCamaImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoportesCamaImpacto());
                            params.put("inclinacionZonaCargue", bandaTransportadoraEntitiesArraylist.get(finalI).getInclinacionZonaCargue());
                            params.put("sistemaAlineacionCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionCarga());
                            params.put("cantidadSistemaAlineacionEnCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getCantidadSistemaAlineacionEnCarga());
                            params.put("sistemasAlineacionCargaFuncionando", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemasAlineacionCargaFuncionando());
                            params.put("sistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionEnRetorno());
                            params.put("cantidadSistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getCantidadSistemaAlineacionEnRetorno());
                            params.put("sistemasAlineacionRetornoFuncionando", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemasAlineacionRetornoFuncionando());
                            params.put("sistemaAlineacionRetornoPlano", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionRetornoPlano());
                            params.put("sistemaAlineacionArtesaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionArtesaCarga());
                            params.put("sistemaAlineacionRetornoEnV", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionRetornoEnV());
                            params.put("largoEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloCentralCarga());
                            params.put("diametroEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloCentralCarga());
                            params.put("largoTuboRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloCentralCarga());
                            params.put("largoEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloLateralCarga());
                            params.put("diametroEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloLateralCarga());
                            params.put("diametroRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloLateralCarga());
                            params.put("largoTuboRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloLateralCarga());
                            params.put("tipoRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloCarga());
                            params.put("distanciaEntreArtesasCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntreArtesasCarga());
                            params.put("anchoInternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoInternoChasisRodilloCarga());
                            params.put("anchoExternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoExternoChasisRodilloCarga());
                            params.put("anguloAcanalamientoArtesaCArga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesaCArga());
                            params.put("detalleRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloCentralCarga());
                            params.put("detalleRodilloLateralCarg", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloLateralCarg());
                            params.put("diametroPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaMotrizTransportadora());
                            params.put("anchoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaMotrizTransportadora());
                            params.put("tipoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaMotrizTransportadora());
                            params.put("largoEjePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaMotrizTransportadora());
                            params.put("diametroEjeMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeMotrizTransportadora());
                            params.put("icobandasCentraEnPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentraEnPoleaMotrizTransportadora());
                            params.put("anguloAmarrePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAmarrePoleaMotrizTransportadora());
                            params.put("estadoRevestimientoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaMotrizTransportadora());
                            params.put("tipoTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaMotrizTransportadora());
                            params.put("distanciaTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaMotrizTransportadora());
                            params.put("potenciaMotorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getPotenciaMotorTransportadora());
                            params.put("guardaPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaMotrizTransportadora());
                            params.put("anchoEstructura", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoEstructura());
                            params.put("anchoTrayectoCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoTrayectoCarga());
                            params.put("pasarelaRespectoAvanceBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getPasarelaRespectoAvanceBanda());
                            params.put("materialAlimenticioTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAlimenticioTransportadora());
                            params.put("materialAcidoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAcidoTransportadora());
                            params.put("materialTempEntre8finalIy15finalITransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialTempEntre80y150Transportadora());
                            params.put("materialSecoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialSecoTransportadora());
                            params.put("materialHumedoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialHumedoTransportadora());
                            params.put("materialAbrasivoFinoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAbrasivoFinoTransportadora());
                            params.put("materialPegajosoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialPegajosoTransportadora());
                            params.put("materialGrasosoAceitosoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialGrasosoAceitosoTransportadora());
                            params.put("marcaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorPrimario());
                            params.put("referenciaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorPrimario());
                            params.put("anchoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorPrimario());
                            params.put("altoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorPrimario());
                            params.put("estadoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorPrimario());
                            params.put("estadoTensorLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorPrimario());
                            params.put("estadoTuboLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorPrimario());
                            params.put("frecuenciaRevisionCuchilla", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla());
                            params.put("cuchillaEnContactoConBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda());
                            params.put("marcaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorSecundario());
                            params.put("referenciaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorSecundario());
                            params.put("anchoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorSecundario());
                            params.put("altoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorSecundario());
                            params.put("estadoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorSecundario());
                            params.put("estadoTensorLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorSecundario());
                            params.put("estadoTuboLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorSecundario());
                            params.put("frecuenciaRevisionCuchilla1", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla1());
                            params.put("cuchillaEnContactoConBanda1", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda1());
                            params.put("sistemaDribbleChute", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaDribbleChute());
                            params.put("marcaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorTerciario());
                            params.put("referenciaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorTerciario());
                            params.put("anchoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorTerciario());
                            params.put("altoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorTerciario());
                            params.put("estadoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorTerciario());
                            params.put("estadoTensorLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorTerciario());
                            params.put("estadoTuboLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorTerciario());
                            params.put("frecuenciaRevisionCuchilla2", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla2());
                            params.put("cuchillaEnContactoConBanda2", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda2());
                            params.put("estadoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRodilloRetorno());
                            params.put("largoEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloRetorno());
                            params.put("diametroEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloRetorno());
                            params.put("diametroRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloRetorno());
                            params.put("largoTuboRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloRetorno());
                            params.put("tipoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloRetorno());
                            params.put("distanciaEntreRodillosRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntreRodillosRetorno());
                            params.put("anchoInternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoInternoChasisRetorno());
                            params.put("anchoExternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoExternoChasisRetorno());
                            params.put("detalleRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloRetorno());
                            params.put("diametroPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaAmarrePoleaMotriz());
                            params.put("anchoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaAmarrePoleaMotriz());
                            params.put("tipoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaAmarrePoleaMotriz());
                            params.put("largoEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaAmarrePoleaMotriz());
                            params.put("diametroEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaAmarrePoleaMotriz());
                            params.put("icobandasCentradaPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaAmarrePoleaMotriz());
                            params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaAmarrePoleaMotriz());
                            params.put("dimetroPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getDimetroPoleaAmarrePoleaCola());
                            params.put("anchoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaAmarrePoleaCola());
                            params.put("largoEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaAmarrePoleaCola());
                            params.put("tipoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaAmarrePoleaCola());
                            params.put("diametroEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaAmarrePoleaCola());
                            params.put("icobandasCentradaPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaAmarrePoleaCola());
                            params.put("estadoRevestimientoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaAmarrePoleaCola());
                            params.put("diametroPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaTensora());
                            params.put("anchoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaTensora());
                            params.put("tipoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaTensora());
                            params.put("largoEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaTensora());
                            params.put("diametroEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaTensora());
                            params.put("icobandasCentradaEnPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaEnPoleaTensora());
                            params.put("recorridoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getRecorridoPoleaTensora());
                            params.put("estadoRevestimientoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaTensora());
                            params.put("tipoTransicionPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaTensora());
                            params.put("distanciaTransicionPoleaColaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaColaTensora());
                            params.put("potenciaMotorPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getPotenciaMotorPoleaTensora());
                            params.put("guardaPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaTensora());
                            params.put("puertasInspeccion", bandaTransportadoraEntitiesArraylist.get(finalI).getPuertasInspeccion());
                            params.put("guardaRodilloRetornoPlano", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaRodilloRetornoPlano());
                            params.put("guardaTruTrainer", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaTruTrainer());
                            params.put("guardaPoleaDeflectora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaDeflectora());
                            params.put("guardaZonaDeTransito", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaZonaDeTransito());
                            params.put("guardaMotores", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaMotores());
                            params.put("guardaCadenas", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaCadenas());
                            params.put("guardaCorreas", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaCorreas());
                            params.put("interruptoresDeSeguridad", bandaTransportadoraEntitiesArraylist.get(finalI).getInterruptoresDeSeguridad());
                            params.put("sirenasDeSeguridad", bandaTransportadoraEntitiesArraylist.get(finalI).getSirenasDeSeguridad());
                            params.put("guardaRodilloRetornoV", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaRodilloRetornoV());
                            params.put("diametroRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloCentralCarga());
                            params.put("tipoRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloImpacto());
                            params.put("integridadSoporteCamaSellado", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoporteCamaSellado());
                            params.put("ataqueAbrasivoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueAbrasivoTransportadora());
                            params.put("observacionRegistroTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getObservacionRegistroTransportadora());


                            for(Map.Entry entry:params.entrySet()){
                                if(entry.getValue()==null)
                                {
                                    String campo=entry.getKey().toString();

                                    params.put(campo, "");
                                }
                            }

                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }
    

    public void sincronizarActualizacionesRegistro() {
        //toast=false;
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from registro where estadoRegistro='Actualizar con BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarActualizacionesPesada();


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final String idRegistro = cursor.getString(0);
            db.execSQL("Update registro set estadoRegistro='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarRegistro";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarActualizacionesPesada();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("fechaRegistro", cursor.getString(1));
                    params.put("idTransportador", cursor.getString(2));
                    params.put("idPlanta", cursor.getString(3));
                    params.put("usuarioRegistro", cursor.getString(4));

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<RegistroEntities> registroEntitiesArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    RegistroEntities registroEntities = new RegistroEntities();
                    registroEntities.setIdRegistro(cursor.getString(0));
                    registroEntities.setFechaRegistro(cursor.getString(1));
                    registroEntities.setIdTransportador(cursor.getString(2));
                    registroEntities.setCodplanta(cursor.getString(3));
                    registroEntities.setUsuarioRegistro(cursor.getString(4));
                    registroEntitiesArrayList.add(registroEntities);
                }

                for (int i = 0; i < registroEntitiesArrayList.size(); i++) {

                    db.execSQL("Update registro set estadoRegistro='Sincronizado' where idRegistro='" + registroEntitiesArrayList.get(i).getIdRegistro() + "'");

                    String url = Constants.url + "actualizarRegistro";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (conteo == registroEntitiesArrayList.size()) {
                                sincronizarActualizacionesPesada();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("fechaRegistro", registroEntitiesArrayList.get(finalI).getFechaRegistro());
                            params.put("idTransportador", registroEntitiesArrayList.get(finalI).getIdTransportador());
                            params.put("idPlanta", registroEntitiesArrayList.get(finalI).getCodplanta());
                            params.put("usuarioRegistro", registroEntitiesArrayList.get(finalI).getUsuarioRegistro());


                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes).wait(2000);


                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarActualizacionesPesada() {
        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaTransmision where estadoRegistroPesada='Actualizar con BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarActualizacionesElevadora();

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaPesada> bandaPesadaArrayList = new ArrayList<>();


            BandaPesada bandaPesada = new BandaPesada();
            bandaPesada.setIdRegistro(cursor.getString(0));
            bandaPesada.setAnchoBandaTransmision(cursor.getString(1));
            bandaPesada.setDistanciaEntreCentrosTransmision(cursor.getString(2));
            bandaPesada.setPotenciaMotorTransmision(cursor.getString(3));
            bandaPesada.setRpmSalidaReductorTransmision(cursor.getString(4));
            bandaPesada.setDiametroPoleaConducidaTransmision(cursor.getString(5));
            bandaPesada.setAnchoPoleaConducidaTransmision(cursor.getString(6));
            bandaPesada.setDiametroPoleaMotrizTransmision(cursor.getString(7));
            bandaPesada.setAnchoPoleaMotrizTransmision(cursor.getString(8));
            bandaPesada.setTipoParteTransmision(cursor.getString(9));
            bandaPesada.setObservacionRegistro(cursor.getString(11));
            bandaPesadaArrayList.add(bandaPesada);

            db.execSQL("Update bandaTransmision set estadoRegistroPesada='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarBandaPesada";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarActualizacionesElevadora();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaPesadaArrayList.get(0).getIdRegistro());
                    params.put("anchoBandaTransmision", bandaPesadaArrayList.get(0).getAnchoBandaTransmision());
                    params.put("distanciaEntreCentrosTransmision", bandaPesadaArrayList.get(0).getDistanciaEntreCentrosTransmision());
                    params.put("potenciaMotorTransmision", bandaPesadaArrayList.get(0).getPotenciaMotorTransmision());
                    params.put("rpmSalidaReductorTransmision", bandaPesadaArrayList.get(0).getRpmSalidaReductorTransmision());
                    params.put("diametroPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaConducidaTransmision());
                    params.put("anchoPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaConducidaTransmision());
                    params.put("diametroPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaMotrizTransmision());
                    params.put("anchoPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaMotrizTransmision());
                    params.put("tipoParteTransmision", bandaPesadaArrayList.get(0).getTipoParteTransmision());
                    params.put("observacionRegistroPesada", bandaPesadaArrayList.get(0).getObservacionRegistro());

                    Log.e("PARAMS", params.toString());


                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<BandaPesada> bandaPesadaArrayList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    BandaPesada bandaPesada = new BandaPesada();
                    bandaPesada.setIdRegistro(cursor.getString(0));
                    bandaPesada.setAnchoBandaTransmision(cursor.getString(1));
                    bandaPesada.setDistanciaEntreCentrosTransmision(cursor.getString(2));
                    bandaPesada.setPotenciaMotorTransmision(cursor.getString(3));
                    bandaPesada.setRpmSalidaReductorTransmision(cursor.getString(4));
                    bandaPesada.setDiametroPoleaConducidaTransmision(cursor.getString(5));
                    bandaPesada.setAnchoPoleaConducidaTransmision(cursor.getString(6));
                    bandaPesada.setDiametroPoleaMotrizTransmision(cursor.getString(7));
                    bandaPesada.setAnchoPoleaMotrizTransmision(cursor.getString(8));
                    bandaPesada.setTipoParteTransmision(cursor.getString(9));
                    bandaPesadaArrayList.add(bandaPesada);

                }

                conteo=0;
                for (int i = 0; i < bandaPesadaArrayList.size(); i++) {


                    String url = Constants.url + "actualizarBandaPesada";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransmision set  estadoRegistroPesada='Sincronizado' where idRegistro='" + bandaPesadaArrayList.get(finalI).getIdRegistro() + "'");
                            conteo++;
                            if(conteo==bandaPesadaArrayList.size())
                            {
                                sincronizarActualizacionesElevadora();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaPesadaArrayList.get(finalI).getIdRegistro());
                            params.put("anchoBandaTransmision", bandaPesadaArrayList.get(finalI).getAnchoBandaTransmision());
                            params.put("distanciaEntreCentrosTransmision", bandaPesadaArrayList.get(finalI).getDistanciaEntreCentrosTransmision());
                            params.put("potenciaMotorTransmision", bandaPesadaArrayList.get(finalI).getPotenciaMotorTransmision());
                            params.put("rpmSalidaReductorTransmision", bandaPesadaArrayList.get(finalI).getRpmSalidaReductorTransmision());
                            params.put("diametroPoleaConducidaTransmision", bandaPesadaArrayList.get(finalI).getDiametroPoleaConducidaTransmision());
                            params.put("anchoPoleaConducidaTransmision", bandaPesadaArrayList.get(finalI).getAnchoPoleaConducidaTransmision());
                            params.put("diametroPoleaMotrizTransmision", bandaPesadaArrayList.get(finalI).getDiametroPoleaMotrizTransmision());
                            params.put("anchoPoleaMotrizTransmision", bandaPesadaArrayList.get(finalI).getAnchoPoleaMotrizTransmision());
                            params.put("tipoParteTransmision", bandaPesadaArrayList.get(finalI).getTipoParteTransmision());
                            params.put("observacionRegistroPesada", bandaPesadaArrayList.get(finalI).getObservacionRegistro());



                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);

                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarActualizacionesElevadora()
    {

        dbHelper = new DbHelper(this, "prueba", null, 1);


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaElevadora where estadoRegistroElevadora='Actualizar con BD'", null);
        if (cursor.getCount() == 0) {

            sincronizarActualizacionesTransportadora();

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaElevadoraEntities> bandaElevadoraEntitiesArrayList = new ArrayList<>();

            BandaElevadoraEntities bandaElevadoraEntities = new BandaElevadoraEntities();
            bandaElevadoraEntities.setIdRegistro(cursor.getString(0));
            bandaElevadoraEntities.setMarcaBandaElevadora(cursor.getString(1));
            bandaElevadoraEntities.setAnchoBandaElevadora(cursor.getString(2));
            bandaElevadoraEntities.setDistanciaEntrePoleasElevadora(cursor.getString(3));
            bandaElevadoraEntities.setNoLonaBandaElevadora(cursor.getString(4));
            bandaElevadoraEntities.setTipoLonaBandaElevadora(cursor.getString(5));
            bandaElevadoraEntities.setEspesorTotalBandaElevadora(cursor.getString(6));
            bandaElevadoraEntities.setEspesorCojinActualElevadora(cursor.getString(7));
            bandaElevadoraEntities.setEspesorCubiertaSuperiorElevadora(cursor.getString(8));
            bandaElevadoraEntities.setEspesorCubiertaInferiorElevadora(cursor.getString(9));
            bandaElevadoraEntities.setTipoCubiertaElevadora(cursor.getString(10));
            bandaElevadoraEntities.setTipoEmpalmeElevadora(cursor.getString(11));
            bandaElevadoraEntities.setEstadoEmpalmeElevadora(cursor.getString(12));
            bandaElevadoraEntities.setResistenciaRoturaLonaElevadora(cursor.getString(13));
            bandaElevadoraEntities.setVelocidadBandaElevadora(cursor.getString(14));
            bandaElevadoraEntities.setMarcaBandaElevadoraAnterior(cursor.getString(15));
            bandaElevadoraEntities.setAnchoBandaElevadoraAnterior(cursor.getString(16));
            bandaElevadoraEntities.setNoLonasBandaElevadoraAnterior(cursor.getString(17));
            bandaElevadoraEntities.setTipoLonaBandaElevadoraAnterior(cursor.getString(18));
            bandaElevadoraEntities.setEspesorTotalBandaElevadoraAnterior(cursor.getString(19));
            bandaElevadoraEntities.setEspesorCubiertaSuperiorBandaElevadoraAnterior(cursor.getString(20));
            bandaElevadoraEntities.setEspesorCojinElevadoraAnterior(cursor.getString(21));
            bandaElevadoraEntities.setEspesorCubiertaInferiorBandaElevadoraAnterior(cursor.getString(22));
            bandaElevadoraEntities.setTipoCubiertaElevadoraAnterior(cursor.getString(23));
            bandaElevadoraEntities.setTipoEmpalmeElevadoraAnterior(cursor.getString(24));
            bandaElevadoraEntities.setResistenciaRoturaBandaElevadoraAnterior(cursor.getString(25));
            bandaElevadoraEntities.setTonsTransportadasBandaElevadoraAnterior(cursor.getString(26));
            bandaElevadoraEntities.setVelocidadBandaElevadoraAnterior(cursor.getString(27));
            bandaElevadoraEntities.setCausaFallaCambioBandaElevadoraAnterior(cursor.getString(28));
            bandaElevadoraEntities.setPesoMaterialEnCadaCangilon(cursor.getString(29));
            bandaElevadoraEntities.setPesoCangilonVacio(cursor.getString(30));
            bandaElevadoraEntities.setLongitudCangilon(cursor.getString(31));
            bandaElevadoraEntities.setMaterialCangilon(cursor.getString(32));
            bandaElevadoraEntities.setTipoCangilon(cursor.getString(33));
            bandaElevadoraEntities.setProyeccionCangilon(cursor.getString(34));
            bandaElevadoraEntities.setProfundidadCangilon(cursor.getString(35));
            bandaElevadoraEntities.setMarcaCangilon(cursor.getString(36));
            bandaElevadoraEntities.setReferenciaCangilon(cursor.getString(37));
            bandaElevadoraEntities.setCapacidadCangilon(cursor.getString(38));
            bandaElevadoraEntities.setNoFilasCangilones(cursor.getString(39));
            bandaElevadoraEntities.setSeparacionCangilones(cursor.getString(40));
            bandaElevadoraEntities.setNoAgujeros(cursor.getString(41));
            bandaElevadoraEntities.setDistanciaBordeBandaEstructura(cursor.getString(42));
            bandaElevadoraEntities.setDistanciaPosteriorBandaEstructura(cursor.getString(43));
            bandaElevadoraEntities.setDistanciaLaboFrontalCangilonEstructura(cursor.getString(44));
            bandaElevadoraEntities.setDistanciaBordesCangilonEstructura(cursor.getString(45));
            bandaElevadoraEntities.setTipoVentilacion(cursor.getString(46));
            bandaElevadoraEntities.setDiametroPoleaMotrizElevadora(cursor.getString(47));
            bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(48));
            bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(49));
            bandaElevadoraEntities.setTipoPoleaMotrizElevadora(cursor.getString(50));
            bandaElevadoraEntities.setLargoEjeMotrizElevadora(cursor.getString(51));
            bandaElevadoraEntities.setDiametroEjeMotrizElevadora(cursor.getString(52));
            bandaElevadoraEntities.setBandaCentradaEnPoleaMotrizElevadora(cursor.getString(53));
            bandaElevadoraEntities.setEstadoRevestimientoPoleaMotrizElevadora(cursor.getString(54));
            bandaElevadoraEntities.setPotenciaMotorMotrizElevadora(cursor.getString(55));
            bandaElevadoraEntities.setRpmSalidaReductorMotrizElevadora(cursor.getString(56));
            bandaElevadoraEntities.setGuardaReductorPoleaMotrizElevadora(cursor.getString(57));
            bandaElevadoraEntities.setDiametroPoleaColaElevadora(cursor.getString(58));
            bandaElevadoraEntities.setAnchoPoleaColaElevadora(cursor.getString(59));
            bandaElevadoraEntities.setTipoPoleaColaElevadora(cursor.getString(60));
            bandaElevadoraEntities.setLargoEjePoleaColaElevadora(cursor.getString(61));
            bandaElevadoraEntities.setDiametroEjePoleaColaElevadora(cursor.getString(62));
            bandaElevadoraEntities.setBandaCentradaEnPoleaColaElevadora(cursor.getString(63));
            bandaElevadoraEntities.setEstadoRevestimientoPoleaColaElevadora(cursor.getString(64));
            bandaElevadoraEntities.setLongitudTensorTornilloPoleaColaElevadora(cursor.getString(65));
            bandaElevadoraEntities.setLongitudRecorridoContrapesaPoleaColaElevadora(cursor.getString(66));
            bandaElevadoraEntities.setCargaTrabajoBandaElevadora(cursor.getString(67));
            bandaElevadoraEntities.setTemperaturaMaterialElevadora(cursor.getString(68));
            bandaElevadoraEntities.setEmpalmeMecanicoElevadora(cursor.getString(69));
            bandaElevadoraEntities.setDiametroRoscaElevadora(cursor.getString(70));
            bandaElevadoraEntities.setLargoTornilloElevadora(cursor.getString(71));
            bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(72));
            bandaElevadoraEntities.setAnchoCabezaElevadorPuertaInspeccion(cursor.getString(73));
            bandaElevadoraEntities.setLargoCabezaElevadorPuertaInspeccion(cursor.getString(74));
            bandaElevadoraEntities.setAnchoBotaElevadorPuertaInspeccion(cursor.getString(75));
            bandaElevadoraEntities.setLargoBotaElevadorPuertaInspeccion(cursor.getString(76));
            bandaElevadoraEntities.setMonitorPeligro(cursor.getString(77));
            bandaElevadoraEntities.setRodamiento(cursor.getString(78));
            bandaElevadoraEntities.setMonitorVelocidad(cursor.getString(79));
            bandaElevadoraEntities.setSensorInductivo(cursor.getString(80));
            bandaElevadoraEntities.setIndicadorNivel(cursor.getString(81));
            bandaElevadoraEntities.setCajaUnion(cursor.getString(82));
            bandaElevadoraEntities.setAlarmaYPantalla(cursor.getString(83));
            bandaElevadoraEntities.setInterruptorSeguridad(cursor.getString(84));
            bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(85));
            bandaElevadoraEntities.setAtaqueQuimicoElevadora(cursor.getString(86));
            bandaElevadoraEntities.setAtaqueTemperaturaElevadora(cursor.getString(87));
            bandaElevadoraEntities.setAtaqueAceitesElevadora(cursor.getString(88));
            bandaElevadoraEntities.setAtaqueAbrasivoElevadora(cursor.getString(89));
            bandaElevadoraEntities.setCapacidadElevadora(cursor.getString(90));
            bandaElevadoraEntities.setHorasTrabajoDiaElevadora(cursor.getString(91));
            bandaElevadoraEntities.setDiasTrabajoSemanaElevadora(cursor.getString(92));
            bandaElevadoraEntities.setAbrasividadElevadora(cursor.getString(93));
            bandaElevadoraEntities.setPorcentajeFinosElevadora(cursor.getString(94));
            bandaElevadoraEntities.setMaxGranulometriaElevadora(cursor.getString(95));
            bandaElevadoraEntities.setDensidadMaterialElevadora(cursor.getString(96));
            bandaElevadoraEntities.setTempMaxMaterialSobreBandaElevadora(cursor.getString(97));
            bandaElevadoraEntities.setTempPromedioMaterialSobreBandaElevadora(cursor.getString(98));
            bandaElevadoraEntities.setVariosPuntosDeAlimentacion(cursor.getString(99));
            bandaElevadoraEntities.setLluviaDeMaterial(cursor.getString(100));
            bandaElevadoraEntities.setAnchoPiernaElevador(cursor.getString(101));
            bandaElevadoraEntities.setProfundidadPiernaElevador(cursor.getString(102));
            bandaElevadoraEntities.setTempAmbienteMin(cursor.getString(103));
            bandaElevadoraEntities.setTempAmbienteMax(cursor.getString(104));
            bandaElevadoraEntities.setTipoCarga(cursor.getString(105));
            bandaElevadoraEntities.setTipoDescarga(cursor.getString(106));
            bandaElevadoraEntities.setObservacionRegistroElevadora(cursor.getString(108));

            bandaElevadoraEntitiesArrayList.add(bandaElevadoraEntities);

            db.execSQL("Update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarElevadora";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizarActualizacionesTransportadora();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaElevadoraEntitiesArrayList.get(0).getIdRegistro());
                    params.put("marcaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getMarcaBandaElevadora());
                    params.put("anchoBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoBandaElevadora());
                    params.put("distanciaEntrePoleasElevadora", bandaElevadoraEntitiesArrayList.get(0).getDistanciaEntrePoleasElevadora());
                    params.put("noLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getNoLonaBandaElevadora());
                    params.put("tipoLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoLonaBandaElevadora());
                    params.put("espesorTotalBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorTotalBandaElevadora());
                    params.put("espesorCojinActualElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCojinActualElevadora());
                    params.put("espesorCubiertaSuperiorElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaSuperiorElevadora());
                    params.put("espesorCubiertaInferiorElevadora", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaInferiorElevadora());
                    params.put("tipoCubiertaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoCubiertaElevadora());
                    params.put("tipoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoEmpalmeElevadora());
                    params.put("estadoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoEmpalmeElevadora());
                    params.put("resistenciaRoturaLonaElevadora", bandaElevadoraEntitiesArrayList.get(0).getResistenciaRoturaLonaElevadora());
                    params.put("velocidadBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getVelocidadBandaElevadora());
                    params.put("marcaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getMarcaBandaElevadoraAnterior());
                    params.put("anchoBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getAnchoBandaElevadoraAnterior());
                    params.put("noLonasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getNoLonasBandaElevadoraAnterior());
                    params.put("tipoLonaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoLonaBandaElevadoraAnterior());
                    params.put("espesorTotalBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorTotalBandaElevadoraAnterior());
                    params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaSuperiorBandaElevadoraAnterior());
                    params.put("espesorCojinElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCojinElevadoraAnterior());
                    params.put("espesorCubiertaInferiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getEspesorCubiertaInferiorBandaElevadoraAnterior());
                    params.put("tipoCubiertaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoCubiertaElevadoraAnterior());
                    params.put("tipoEmpalmeElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTipoEmpalmeElevadoraAnterior());
                    params.put("resistenciaRoturaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getResistenciaRoturaBandaElevadoraAnterior());
                    params.put("tonsTransportadasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getTonsTransportadasBandaElevadoraAnterior());
                    params.put("velocidadBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getVelocidadBandaElevadoraAnterior());
                    params.put("causaFallaCambioBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(0).getCausaFallaCambioBandaElevadoraAnterior());
                    params.put("pesoMaterialEnCadaCangilon", bandaElevadoraEntitiesArrayList.get(0).getPesoMaterialEnCadaCangilon());
                    params.put("pesoCangilonVacio", bandaElevadoraEntitiesArrayList.get(0).getPesoCangilonVacio());
                    params.put("longitudCangilon", bandaElevadoraEntitiesArrayList.get(0).getLongitudCangilon() );
                    params.put("materialCangilon", bandaElevadoraEntitiesArrayList.get(0).getMaterialCangilon());
                    params.put("tipoCangilon", bandaElevadoraEntitiesArrayList.get(0).getTipoCangilon());
                    params.put("proyeccionCangilon", bandaElevadoraEntitiesArrayList.get(0).getProyeccionCangilon());
                    params.put("profundidadCangilon", bandaElevadoraEntitiesArrayList.get(0).getProfundidadCangilon());
                    params.put("marcaCangilon", bandaElevadoraEntitiesArrayList.get(0).getMarcaCangilon());
                    params.put("referenciaCangilon", bandaElevadoraEntitiesArrayList.get(0).getReferenciaCangilon());
                    params.put("capacidadCangilon", bandaElevadoraEntitiesArrayList.get(0).getCapacidadCangilon());
                    params.put("noFilasCangilones", bandaElevadoraEntitiesArrayList.get(0).getNoFilasCangilones());
                    params.put("separacionCangilones", bandaElevadoraEntitiesArrayList.get(0).getSeparacionCangilones());
                    params.put("noAgujeros", bandaElevadoraEntitiesArrayList.get(0).getNoAgujeros());
                    params.put("distanciaBordeBandaEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaBordeBandaEstructura());
                    params.put("distanciaPosteriorBandaEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaPosteriorBandaEstructura());
                    params.put("distanciaLaboFrontalCangilonEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaLaboFrontalCangilonEstructura());
                    params.put("distanciaBordesCangilonEstructura", bandaElevadoraEntitiesArrayList.get(0).getDistanciaBordesCangilonEstructura());
                    params.put("tipoVentilacion", bandaElevadoraEntitiesArrayList.get(0).getTipoVentilacion());
                    params.put("diametroPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroPoleaMotrizElevadora());
                    params.put("anchoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoPoleaMotrizElevadora());
                    params.put("tipoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoPoleaMotrizElevadora());
                    params.put("largoEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoEjeMotrizElevadora());
                    params.put("diametroEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroEjeMotrizElevadora());
                    params.put("bandaCentradaEnPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getBandaCentradaEnPoleaMotrizElevadora());
                    params.put("estadoRevestimientoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoRevestimientoPoleaMotrizElevadora());
                    params.put("potenciaMotorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getPotenciaMotorMotrizElevadora());
                    params.put("rpmSalidaReductorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getRpmSalidaReductorMotrizElevadora());
                    params.put("guardaReductorPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(0).getGuardaReductorPoleaMotrizElevadora());
                    params.put("diametroPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroPoleaColaElevadora());
                    params.put("anchoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAnchoPoleaColaElevadora());
                    params.put("tipoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTipoPoleaColaElevadora());
                    params.put("largoEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoEjePoleaColaElevadora());
                    params.put("diametroEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroEjePoleaColaElevadora());
                    params.put("bandaCentradaEnPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getBandaCentradaEnPoleaColaElevadora());
                    params.put("estadoRevestimientoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getEstadoRevestimientoPoleaColaElevadora());
                    params.put("longitudTensorTornilloPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLongitudTensorTornilloPoleaColaElevadora());
                    params.put("longitudRecorridoContrapesaPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(0).getLongitudRecorridoContrapesaPoleaColaElevadora());
                    params.put("cargaTrabajoBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getCargaTrabajoBandaElevadora());
                    params.put("temperaturaMaterialElevadora", bandaElevadoraEntitiesArrayList.get(0).getTemperaturaMaterialElevadora());
                    params.put("empalmeMecanicoElevadora", bandaElevadoraEntitiesArrayList.get(0).getEmpalmeMecanicoElevadora());
                    params.put("diametroRoscaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiametroRoscaElevadora());
                    params.put("largoTornilloElevadora", bandaElevadoraEntitiesArrayList.get(0).getLargoTornilloElevadora());
                    params.put("materialTornilloElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaterialTornilloElevadora());
                    params.put("anchoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getAnchoCabezaElevadorPuertaInspeccion());
                    params.put("largoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getLargoCabezaElevadorPuertaInspeccion());
                    params.put("anchoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getAnchoBotaElevadorPuertaInspeccion());
                    params.put("largoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(0).getLargoBotaElevadorPuertaInspeccion());
                    params.put("monitorPeligro", bandaElevadoraEntitiesArrayList.get(0).getMonitorPeligro());
                    params.put("rodamiento", bandaElevadoraEntitiesArrayList.get(0).getRodamiento());
                    params.put("monitorDesalineacion", bandaElevadoraEntitiesArrayList.get(0).getMonitorDesalineacion());
                    params.put("monitorVelocidad", bandaElevadoraEntitiesArrayList.get(0).getMonitorVelocidad());
                    params.put("sensorInductivo", bandaElevadoraEntitiesArrayList.get(0).getSensorInductivo());
                    params.put("indicadorNivel", bandaElevadoraEntitiesArrayList.get(0).getIndicadorNivel());
                    params.put("cajaUnion", bandaElevadoraEntitiesArrayList.get(0).getCajaUnion());
                    params.put("alarmaYPantalla", bandaElevadoraEntitiesArrayList.get(0).getAlarmaYPantalla());
                    params.put("interruptorSeguridad", bandaElevadoraEntitiesArrayList.get(0).getInterruptorSeguridad());
                    params.put("materialElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaterialElevadora());
                    params.put("ataqueQuimicoElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueQuimicoElevadora());
                    params.put("ataqueTemperaturaElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueTemperaturaElevadora());
                    params.put("ataqueAceitesElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueAceitesElevadora());
                    params.put("ataqueAbrasivoElevadora", bandaElevadoraEntitiesArrayList.get(0).getAtaqueAbrasivoElevadora());
                    params.put("capacidadElevadora", bandaElevadoraEntitiesArrayList.get(0).getCapacidadElevadora());
                    params.put("horasTrabajoDiaElevadora", bandaElevadoraEntitiesArrayList.get(0).getHorasTrabajoDiaElevadora());
                    params.put("diasTrabajoSemanaElevadora", bandaElevadoraEntitiesArrayList.get(0).getDiasTrabajoSemanaElevadora());
                    params.put("abrasividadElevadora", bandaElevadoraEntitiesArrayList.get(0).getAbrasividadElevadora());
                    params.put("porcentajeFinosElevadora", bandaElevadoraEntitiesArrayList.get(0).getPorcentajeFinosElevadora());
                    params.put("maxGranulometriaElevadora", bandaElevadoraEntitiesArrayList.get(0).getMaxGranulometriaElevadora());
                    params.put("densidadMaterialElevadora", bandaElevadoraEntitiesArrayList.get(0).getDensidadMaterialElevadora());
                    params.put("tempMaxMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTempMaxMaterialSobreBandaElevadora());
                    params.put("tempPromedioMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(0).getTempPromedioMaterialSobreBandaElevadora());
                    params.put("variosPuntosDeAlimentacion", bandaElevadoraEntitiesArrayList.get(0).getVariosPuntosDeAlimentacion());
                    params.put("lluviaDeMaterial", bandaElevadoraEntitiesArrayList.get(0).getLluviaDeMaterial());
                    params.put("anchoPiernaElevador", bandaElevadoraEntitiesArrayList.get(0).getAnchoPiernaElevador());
                    params.put("profundidadPiernaElevador", bandaElevadoraEntitiesArrayList.get(0).getProfundidadPiernaElevador());
                    params.put("tempAmbienteMin", bandaElevadoraEntitiesArrayList.get(0).getTempAmbienteMin());
                    params.put("tempAmbienteMax", bandaElevadoraEntitiesArrayList.get(0).getTempAmbienteMax());
                    params.put("tipoDescarga", bandaElevadoraEntitiesArrayList.get(0).getTipoDescarga());
                    params.put("tipoCarga", bandaElevadoraEntitiesArrayList.get(0).getTipoCarga());
                    params.put("observacionRegistroElevadora", bandaElevadoraEntitiesArrayList.get(0).getObservacionRegistroElevadora());


                    for(Map.Entry entry:params.entrySet()){
                        if(entry.getValue()==null)
                        {
                            String campo=entry.getKey().toString();

                            params.put(campo, "");
                        }
                    }

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        } else {

            try {
                final ArrayList<BandaElevadoraEntities> bandaElevadoraEntitiesArrayList = new ArrayList<>();


                while (cursor.moveToNext()) {
                    BandaElevadoraEntities bandaElevadoraEntities = new BandaElevadoraEntities();
                    bandaElevadoraEntities.setIdRegistro(cursor.getString(0));
                    bandaElevadoraEntities.setMarcaBandaElevadora(cursor.getString(1));
                    bandaElevadoraEntities.setAnchoBandaElevadora(cursor.getString(2));
                    bandaElevadoraEntities.setDistanciaEntrePoleasElevadora(cursor.getString(3));
                    bandaElevadoraEntities.setNoLonaBandaElevadora(cursor.getString(4));
                    bandaElevadoraEntities.setTipoLonaBandaElevadora(cursor.getString(5));
                    bandaElevadoraEntities.setEspesorTotalBandaElevadora(cursor.getString(6));
                    bandaElevadoraEntities.setEspesorCojinActualElevadora(cursor.getString(7));
                    bandaElevadoraEntities.setEspesorCubiertaSuperiorElevadora(cursor.getString(8));
                    bandaElevadoraEntities.setEspesorCubiertaInferiorElevadora(cursor.getString(9));
                    bandaElevadoraEntities.setTipoCubiertaElevadora(cursor.getString(10));
                    bandaElevadoraEntities.setTipoEmpalmeElevadora(cursor.getString(11));
                    bandaElevadoraEntities.setEstadoEmpalmeElevadora(cursor.getString(12));
                    bandaElevadoraEntities.setResistenciaRoturaLonaElevadora(cursor.getString(13));
                    bandaElevadoraEntities.setVelocidadBandaElevadora(cursor.getString(14));
                    bandaElevadoraEntities.setMarcaBandaElevadoraAnterior(cursor.getString(15));
                    bandaElevadoraEntities.setAnchoBandaElevadoraAnterior(cursor.getString(16));
                    bandaElevadoraEntities.setNoLonasBandaElevadoraAnterior(cursor.getString(17));
                    bandaElevadoraEntities.setTipoLonaBandaElevadoraAnterior(cursor.getString(18));
                    bandaElevadoraEntities.setEspesorTotalBandaElevadoraAnterior(cursor.getString(19));
                    bandaElevadoraEntities.setEspesorCubiertaSuperiorBandaElevadoraAnterior(cursor.getString(20));
                    bandaElevadoraEntities.setEspesorCojinElevadoraAnterior(cursor.getString(21));
                    bandaElevadoraEntities.setEspesorCubiertaInferiorBandaElevadoraAnterior(cursor.getString(22));
                    bandaElevadoraEntities.setTipoCubiertaElevadoraAnterior(cursor.getString(23));
                    bandaElevadoraEntities.setTipoEmpalmeElevadoraAnterior(cursor.getString(24));
                    bandaElevadoraEntities.setResistenciaRoturaBandaElevadoraAnterior(cursor.getString(25));
                    bandaElevadoraEntities.setTonsTransportadasBandaElevadoraAnterior(cursor.getString(26));
                    bandaElevadoraEntities.setVelocidadBandaElevadoraAnterior(cursor.getString(27));
                    bandaElevadoraEntities.setCausaFallaCambioBandaElevadoraAnterior(cursor.getString(28));
                    bandaElevadoraEntities.setPesoMaterialEnCadaCangilon(cursor.getString(29));
                    bandaElevadoraEntities.setPesoCangilonVacio(cursor.getString(30));
                    bandaElevadoraEntities.setLongitudCangilon(cursor.getString(31));
                    bandaElevadoraEntities.setMaterialCangilon(cursor.getString(32));
                    bandaElevadoraEntities.setTipoCangilon(cursor.getString(33));
                    bandaElevadoraEntities.setProyeccionCangilon(cursor.getString(34));
                    bandaElevadoraEntities.setProfundidadCangilon(cursor.getString(35));
                    bandaElevadoraEntities.setMarcaCangilon(cursor.getString(36));
                    bandaElevadoraEntities.setReferenciaCangilon(cursor.getString(37));
                    bandaElevadoraEntities.setCapacidadCangilon(cursor.getString(38));
                    bandaElevadoraEntities.setNoFilasCangilones(cursor.getString(39));
                    bandaElevadoraEntities.setSeparacionCangilones(cursor.getString(40));
                    bandaElevadoraEntities.setNoAgujeros(cursor.getString(41));
                    bandaElevadoraEntities.setDistanciaBordeBandaEstructura(cursor.getString(42));
                    bandaElevadoraEntities.setDistanciaPosteriorBandaEstructura(cursor.getString(43));
                    bandaElevadoraEntities.setDistanciaLaboFrontalCangilonEstructura(cursor.getString(44));
                    bandaElevadoraEntities.setDistanciaBordesCangilonEstructura(cursor.getString(45));
                    bandaElevadoraEntities.setTipoVentilacion(cursor.getString(46));
                    bandaElevadoraEntities.setDiametroPoleaMotrizElevadora(cursor.getString(47));
                    bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(48));
                    bandaElevadoraEntities.setAnchoPoleaMotrizElevadora(cursor.getString(49));
                    bandaElevadoraEntities.setTipoPoleaMotrizElevadora(cursor.getString(50));
                    bandaElevadoraEntities.setLargoEjeMotrizElevadora(cursor.getString(51));
                    bandaElevadoraEntities.setDiametroEjeMotrizElevadora(cursor.getString(52));
                    bandaElevadoraEntities.setBandaCentradaEnPoleaMotrizElevadora(cursor.getString(53));
                    bandaElevadoraEntities.setEstadoRevestimientoPoleaMotrizElevadora(cursor.getString(54));
                    bandaElevadoraEntities.setPotenciaMotorMotrizElevadora(cursor.getString(55));
                    bandaElevadoraEntities.setRpmSalidaReductorMotrizElevadora(cursor.getString(56));
                    bandaElevadoraEntities.setGuardaReductorPoleaMotrizElevadora(cursor.getString(57));
                    bandaElevadoraEntities.setDiametroPoleaColaElevadora(cursor.getString(58));
                    bandaElevadoraEntities.setAnchoPoleaColaElevadora(cursor.getString(59));
                    bandaElevadoraEntities.setTipoPoleaColaElevadora(cursor.getString(60));
                    bandaElevadoraEntities.setLargoEjePoleaColaElevadora(cursor.getString(61));
                    bandaElevadoraEntities.setDiametroEjePoleaColaElevadora(cursor.getString(62));
                    bandaElevadoraEntities.setBandaCentradaEnPoleaColaElevadora(cursor.getString(63));
                    bandaElevadoraEntities.setEstadoRevestimientoPoleaColaElevadora(cursor.getString(64));
                    bandaElevadoraEntities.setLongitudTensorTornilloPoleaColaElevadora(cursor.getString(65));
                    bandaElevadoraEntities.setLongitudRecorridoContrapesaPoleaColaElevadora(cursor.getString(66));
                    bandaElevadoraEntities.setCargaTrabajoBandaElevadora(cursor.getString(67));
                    bandaElevadoraEntities.setTemperaturaMaterialElevadora(cursor.getString(68));
                    bandaElevadoraEntities.setEmpalmeMecanicoElevadora(cursor.getString(69));
                    bandaElevadoraEntities.setDiametroRoscaElevadora(cursor.getString(70));
                    bandaElevadoraEntities.setLargoTornilloElevadora(cursor.getString(71));
                    bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(72));
                    bandaElevadoraEntities.setAnchoCabezaElevadorPuertaInspeccion(cursor.getString(73));
                    bandaElevadoraEntities.setLargoCabezaElevadorPuertaInspeccion(cursor.getString(74));
                    bandaElevadoraEntities.setAnchoBotaElevadorPuertaInspeccion(cursor.getString(75));
                    bandaElevadoraEntities.setLargoBotaElevadorPuertaInspeccion(cursor.getString(76));
                    bandaElevadoraEntities.setMonitorPeligro(cursor.getString(77));
                    bandaElevadoraEntities.setRodamiento(cursor.getString(78));
                    bandaElevadoraEntities.setMonitorVelocidad(cursor.getString(79));
                    bandaElevadoraEntities.setSensorInductivo(cursor.getString(80));
                    bandaElevadoraEntities.setIndicadorNivel(cursor.getString(81));
                    bandaElevadoraEntities.setCajaUnion(cursor.getString(82));
                    bandaElevadoraEntities.setAlarmaYPantalla(cursor.getString(83));
                    bandaElevadoraEntities.setInterruptorSeguridad(cursor.getString(84));
                    bandaElevadoraEntities.setMaterialTornilloElevadora(cursor.getString(85));
                    bandaElevadoraEntities.setAtaqueQuimicoElevadora(cursor.getString(86));
                    bandaElevadoraEntities.setAtaqueTemperaturaElevadora(cursor.getString(87));
                    bandaElevadoraEntities.setAtaqueAceitesElevadora(cursor.getString(88));
                    bandaElevadoraEntities.setAtaqueAbrasivoElevadora(cursor.getString(89));
                    bandaElevadoraEntities.setCapacidadElevadora(cursor.getString(90));
                    bandaElevadoraEntities.setHorasTrabajoDiaElevadora(cursor.getString(91));
                    bandaElevadoraEntities.setDiasTrabajoSemanaElevadora(cursor.getString(92));
                    bandaElevadoraEntities.setAbrasividadElevadora(cursor.getString(93));
                    bandaElevadoraEntities.setPorcentajeFinosElevadora(cursor.getString(94));
                    bandaElevadoraEntities.setMaxGranulometriaElevadora(cursor.getString(95));
                    bandaElevadoraEntities.setDensidadMaterialElevadora(cursor.getString(96));
                    bandaElevadoraEntities.setTempMaxMaterialSobreBandaElevadora(cursor.getString(97));
                    bandaElevadoraEntities.setTempPromedioMaterialSobreBandaElevadora(cursor.getString(98));
                    bandaElevadoraEntities.setVariosPuntosDeAlimentacion(cursor.getString(99));
                    bandaElevadoraEntities.setLluviaDeMaterial(cursor.getString(100));
                    bandaElevadoraEntities.setAnchoPiernaElevador(cursor.getString(101));
                    bandaElevadoraEntities.setProfundidadPiernaElevador(cursor.getString(102));
                    bandaElevadoraEntities.setTempAmbienteMin(cursor.getString(103));
                    bandaElevadoraEntities.setTempAmbienteMax(cursor.getString(104));
                    bandaElevadoraEntities.setTipoCarga(cursor.getString(105));
                    bandaElevadoraEntities.setTipoDescarga(cursor.getString(106));
                    bandaElevadoraEntities.setObservacionRegistroElevadora(cursor.getString(108));
                    bandaElevadoraEntitiesArrayList.add(bandaElevadoraEntities);

                }
                conteo = 0;

                for (int i = 0; i < bandaElevadoraEntitiesArrayList.size(); i++) {


                    String url = Constants.url + "actualizarElevadora ";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            conteo ++;
                            db.execSQL("update bandaElevadora set  estadoRegistroElevadora='Sincronizado' where idRegistro='" + bandaElevadoraEntitiesArrayList.get(finalI).getIdRegistro() + "'");
                            if(conteo==bandaElevadoraEntitiesArrayList.size())
                            {
                                sincronizarActualizacionesTransportadora();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaElevadoraEntitiesArrayList.get(finalI).getIdRegistro());
                            params.put("marcaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaBandaElevadora());
                            params.put("anchoBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBandaElevadora());
                            params.put("distanciaEntrePoleasElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaEntrePoleasElevadora());
                            params.put("noLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getNoLonaBandaElevadora());
                            params.put("tipoLonaBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoLonaBandaElevadora());
                            params.put("espesorTotalBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorTotalBandaElevadora());
                            params.put("espesorCojinActualElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCojinActualElevadora());
                            params.put("espesorCubiertaSuperiorElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaSuperiorElevadora());
                            params.put("espesorCubiertaInferiorElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaInferiorElevadora());
                            params.put("tipoCubiertaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCubiertaElevadora());
                            params.put("tipoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoEmpalmeElevadora());
                            params.put("estadoEmpalmeElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoEmpalmeElevadora());
                            params.put("resistenciaRoturaLonaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getResistenciaRoturaLonaElevadora());
                            params.put("velocidadBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getVelocidadBandaElevadora());
                            params.put("marcaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaBandaElevadoraAnterior());
                            params.put("anchoBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBandaElevadoraAnterior());
                            params.put("noLonasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getNoLonasBandaElevadoraAnterior());
                            params.put("tipoLonaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoLonaBandaElevadoraAnterior());
                            params.put("espesorTotalBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorTotalBandaElevadoraAnterior());
                            params.put("espesorCubiertaSuperiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaSuperiorBandaElevadoraAnterior());
                            params.put("espesorCojinElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCojinElevadoraAnterior());
                            params.put("espesorCubiertaInferiorBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getEspesorCubiertaInferiorBandaElevadoraAnterior());
                            params.put("tipoCubiertaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCubiertaElevadoraAnterior());
                            params.put("tipoEmpalmeElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTipoEmpalmeElevadoraAnterior());
                            params.put("resistenciaRoturaBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getResistenciaRoturaBandaElevadoraAnterior());
                            params.put("tonsTransportadasBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getTonsTransportadasBandaElevadoraAnterior());
                            params.put("velocidadBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getVelocidadBandaElevadoraAnterior());
                            params.put("causaFallaCambioBandaElevadoraAnterior", bandaElevadoraEntitiesArrayList.get(finalI).getCausaFallaCambioBandaElevadoraAnterior());
                            params.put("pesoMaterialEnCadaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getPesoMaterialEnCadaCangilon());
                            params.put("pesoCangilonVacio", bandaElevadoraEntitiesArrayList.get(finalI).getPesoCangilonVacio());
                            params.put("longitudCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudCangilon() );
                            params.put("materialCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialCangilon());
                            params.put("tipoCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCangilon());
                            params.put("proyeccionCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getProyeccionCangilon());
                            params.put("profundidadCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getProfundidadCangilon());
                            params.put("marcaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getMarcaCangilon());
                            params.put("referenciaCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getReferenciaCangilon());
                            params.put("capacidadCangilon", bandaElevadoraEntitiesArrayList.get(finalI).getCapacidadCangilon());
                            params.put("noFilasCangilones", bandaElevadoraEntitiesArrayList.get(finalI).getNoFilasCangilones());
                            params.put("separacionCangilones", bandaElevadoraEntitiesArrayList.get(finalI).getSeparacionCangilones());
                            params.put("noAgujeros", bandaElevadoraEntitiesArrayList.get(finalI).getNoAgujeros());
                            params.put("distanciaBordeBandaEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaBordeBandaEstructura());
                            params.put("distanciaPosteriorBandaEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaPosteriorBandaEstructura());
                            params.put("distanciaLaboFrontalCangilonEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaLaboFrontalCangilonEstructura());
                            params.put("distanciaBordesCangilonEstructura", bandaElevadoraEntitiesArrayList.get(finalI).getDistanciaBordesCangilonEstructura());
                            params.put("tipoVentilacion", bandaElevadoraEntitiesArrayList.get(finalI).getTipoVentilacion());
                            params.put("diametroPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroPoleaMotrizElevadora());
                            params.put("anchoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPoleaMotrizElevadora());
                            params.put("tipoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoPoleaMotrizElevadora());
                            params.put("largoEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoEjeMotrizElevadora());
                            params.put("diametroEjeMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroEjeMotrizElevadora());
                            params.put("bandaCentradaEnPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getBandaCentradaEnPoleaMotrizElevadora());
                            params.put("estadoRevestimientoPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoRevestimientoPoleaMotrizElevadora());
                            params.put("potenciaMotorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getPotenciaMotorMotrizElevadora());
                            params.put("rpmSalidaReductorMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getRpmSalidaReductorMotrizElevadora());
                            params.put("guardaReductorPoleaMotrizElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getGuardaReductorPoleaMotrizElevadora());
                            params.put("diametroPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroPoleaColaElevadora());
                            params.put("anchoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPoleaColaElevadora());
                            params.put("tipoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTipoPoleaColaElevadora());
                            params.put("largoEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoEjePoleaColaElevadora());
                            params.put("diametroEjePoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroEjePoleaColaElevadora());
                            params.put("bandaCentradaEnPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getBandaCentradaEnPoleaColaElevadora());
                            params.put("estadoRevestimientoPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEstadoRevestimientoPoleaColaElevadora());
                            params.put("longitudTensorTornilloPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudTensorTornilloPoleaColaElevadora());
                            params.put("longitudRecorridoContrapesaPoleaColaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLongitudRecorridoContrapesaPoleaColaElevadora());
                            params.put("cargaTrabajoBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getCargaTrabajoBandaElevadora());
                            params.put("temperaturaMaterialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTemperaturaMaterialElevadora());
                            params.put("empalmeMecanicoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getEmpalmeMecanicoElevadora());
                            params.put("diametroRoscaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiametroRoscaElevadora());
                            params.put("largoTornilloElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getLargoTornilloElevadora());
                            params.put("materialTornilloElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialTornilloElevadora());
                            params.put("anchoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoCabezaElevadorPuertaInspeccion());
                            params.put("largoCabezaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getLargoCabezaElevadorPuertaInspeccion());
                            params.put("anchoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoBotaElevadorPuertaInspeccion());
                            params.put("largoBotaElevadorPuertaInspeccion", bandaElevadoraEntitiesArrayList.get(finalI).getLargoBotaElevadorPuertaInspeccion());
                            params.put("monitorPeligro", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorPeligro());
                            params.put("rodamiento", bandaElevadoraEntitiesArrayList.get(finalI).getRodamiento());
                            params.put("monitorDesalineacion", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorDesalineacion());
                            params.put("monitorVelocidad", bandaElevadoraEntitiesArrayList.get(finalI).getMonitorVelocidad());
                            params.put("sensorInductivo", bandaElevadoraEntitiesArrayList.get(finalI).getSensorInductivo());
                            params.put("indicadorNivel", bandaElevadoraEntitiesArrayList.get(finalI).getIndicadorNivel());
                            params.put("cajaUnion", bandaElevadoraEntitiesArrayList.get(finalI).getCajaUnion());
                            params.put("alarmaYPantalla", bandaElevadoraEntitiesArrayList.get(finalI).getAlarmaYPantalla());
                            params.put("interruptorSeguridad", bandaElevadoraEntitiesArrayList.get(finalI).getInterruptorSeguridad());
                            params.put("materialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaterialElevadora());
                            params.put("ataqueQuimicoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueQuimicoElevadora());
                            params.put("ataqueTemperaturaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueTemperaturaElevadora());
                            params.put("ataqueAceitesElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueAceitesElevadora());
                            params.put("ataqueAbrasivoElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAtaqueAbrasivoElevadora());
                            params.put("capacidadElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getCapacidadElevadora());
                            params.put("horasTrabajoDiaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getHorasTrabajoDiaElevadora());
                            params.put("diasTrabajoSemanaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDiasTrabajoSemanaElevadora());
                            params.put("abrasividadElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getAbrasividadElevadora());
                            params.put("porcentajeFinosElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getPorcentajeFinosElevadora());
                            params.put("maxGranulometriaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getMaxGranulometriaElevadora());
                            params.put("densidadMaterialElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getDensidadMaterialElevadora());
                            params.put("tempMaxMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTempMaxMaterialSobreBandaElevadora());
                            params.put("tempPromedioMaterialSobreBandaElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getTempPromedioMaterialSobreBandaElevadora());
                            params.put("variosPuntosDeAlimentacion", bandaElevadoraEntitiesArrayList.get(finalI).getVariosPuntosDeAlimentacion());
                            params.put("lluviaDeMaterial", bandaElevadoraEntitiesArrayList.get(finalI).getLluviaDeMaterial());
                            params.put("anchoPiernaElevador", bandaElevadoraEntitiesArrayList.get(finalI).getAnchoPiernaElevador());
                            params.put("profundidadPiernaElevador", bandaElevadoraEntitiesArrayList.get(finalI).getProfundidadPiernaElevador());
                            params.put("tempAmbienteMin", bandaElevadoraEntitiesArrayList.get(finalI).getTempAmbienteMin());
                            params.put("tempAmbienteMax", bandaElevadoraEntitiesArrayList.get(finalI).getTempAmbienteMax());
                            params.put("tipoDescarga", bandaElevadoraEntitiesArrayList.get(finalI).getTipoDescarga());
                            params.put("tipoCarga", bandaElevadoraEntitiesArrayList.get(finalI).getTipoCarga());
                            params.put("observacionRegistroElevadora", bandaElevadoraEntitiesArrayList.get(finalI).getObservacionRegistroElevadora());
                            for(Map.Entry entry:params.entrySet()){
                                if(entry.getValue()==null)
                                {
                                    String campo=entry.getKey().toString();

                                    params.put(campo, "");
                                }
                            }

                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }

    }


    public void sincronizarActualizacionesTransportadora()
    {
        dbHelper = new DbHelper(this, "prueba", null, 1);
        conteo = 0;

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaTransportadora where estadoRegistroTransportadora='Actualizar con BD'", null);
        if (cursor.getCount() == 0) {

            recargarBD();




        }
        else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaTransportadoraEntities> bandaTransportadoraEntitiesArraylist = new ArrayList<>();


            BandaTransportadoraEntities bandaTransportadoraEntities= new BandaTransportadoraEntities();
            bandaTransportadoraEntities.setIdRegistro(cursor.getString(0));
            bandaTransportadoraEntities.setMarcaBandaTransportadora(cursor.getString(1));
            bandaTransportadoraEntities.setAnchoBandaTransportadora(cursor.getString(2));
            bandaTransportadoraEntities.setNoLonasBandaTransportadora(cursor.getString(3));
            bandaTransportadoraEntities.setTipoLonaBandaTransportadora(cursor.getString(4));
            bandaTransportadoraEntities.setEspesorTotalBandaTransportadora(cursor.getString(5));
            bandaTransportadoraEntities.setEspesorCubiertaSuperiorTransportadora(cursor.getString(6));
            bandaTransportadoraEntities.setEspesorCojinTransportadora(cursor.getString(7));
            bandaTransportadoraEntities.setEspesorCubiertaInferiorTransportadora(cursor.getString(8));
            bandaTransportadoraEntities.setTipoCubiertaTransportadora(cursor.getString(9));
            bandaTransportadoraEntities.setTipoEmpalmeTransportadora(cursor.getString(10));
            bandaTransportadoraEntities.setEstadoEmpalmeTransportadora(cursor.getString(11));
            bandaTransportadoraEntities.setDistanciaEntrePoleasBandaHorizontal(cursor.getString(12));
            bandaTransportadoraEntities.setInclinacionBandaHorizontal(cursor.getString(13));
            bandaTransportadoraEntities.setRecorridoUtilTensorBandaHorizontal(cursor.getString(14));
            bandaTransportadoraEntities.setLongitudSinfinBandaHorizontal(cursor.getString(15));
            bandaTransportadoraEntities.setResistenciaRoturaLonaTransportadora(cursor.getString(16));
            bandaTransportadoraEntities.setLocalizacionTensorTransportadora(cursor.getString(17));
            bandaTransportadoraEntities.setBandaReversible(cursor.getString(18));
            bandaTransportadoraEntities.setBandaDeArrastre(cursor.getString(19));
            bandaTransportadoraEntities.setVelocidadBandaHorizontal(cursor.getString(20));
            bandaTransportadoraEntities.setMarcaBandaHorizontalAnterior(cursor.getString(21));
            bandaTransportadoraEntities.setAnchoBandaHorizontalAnterior(cursor.getString(22));
            bandaTransportadoraEntities.setNoLonasBandaHorizontalAnterior(cursor.getString(23));
            bandaTransportadoraEntities.setTipoLonaBandaHorizontalAnterior(cursor.getString(24));
            bandaTransportadoraEntities.setEspesorTotalBandaHorizontalAnterior(cursor.getString(25));
            bandaTransportadoraEntities.setEspesorCubiertaSuperiorBandaHorizontalAnterior(cursor.getString(26));
            bandaTransportadoraEntities.setEspesorCubiertaInferiorBandaHorizontalAnterior(cursor.getString(27));
            bandaTransportadoraEntities.setEspesorCojinBandaHorizontalAnterior(cursor.getString(28));
            bandaTransportadoraEntities.setTipoEmpalmeBandaHorizontalAnterior(cursor.getString(29));
            bandaTransportadoraEntities.setResistenciaRoturaLonaBandaHorizontalAnterior(cursor.getString(30));
            bandaTransportadoraEntities.setTipoCubiertaBandaHorizontalAnterior(cursor.getString(31));
            bandaTransportadoraEntities.setTonsTransportadasBandaHoizontalAnterior(cursor.getString(32));
            bandaTransportadoraEntities.setCausaFallaCambioBandaHorizontal(cursor.getString(33));
            bandaTransportadoraEntities.setDiametroPoleaColaTransportadora(cursor.getString(34));
            bandaTransportadoraEntities.setAnchoPoleaColaTransportadora(cursor.getString(35));
            bandaTransportadoraEntities.setTipoPoleaColaTransportadora(cursor.getString(36));
            bandaTransportadoraEntities.setLargoEjePoleaColaTransportadora(cursor.getString(37));
            bandaTransportadoraEntities.setDiametroEjePoleaColaHorizontal(cursor.getString(38));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaColaTransportadora(cursor.getString(39));
            bandaTransportadoraEntities.setAnguloAmarrePoleaColaTransportadora(cursor.getString(40));
            bandaTransportadoraEntities.setEstadoRvtoPoleaColaTransportadora(cursor.getString(41));
            bandaTransportadoraEntities.setTipoTransicionPoleaColaTransportadora(cursor.getString(42));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTransportadora(cursor.getString(43));
            bandaTransportadoraEntities.setLongitudTensorTornilloPoleaColaTransportadora(cursor.getString(44));
            bandaTransportadoraEntities.setLongitudRecorridoContrapesaPoleaColaTransportadora(cursor.getString(45));
            bandaTransportadoraEntities.setGuardaPoleaColaTransportadora(cursor.getString(46));
            bandaTransportadoraEntities.setHayDesviador(cursor.getString(47));
            bandaTransportadoraEntities.setElDesviadorBascula(cursor.getString(48));
            bandaTransportadoraEntities.setPresionUniformeALoAnchoDeLaBanda(cursor.getString(49));
            bandaTransportadoraEntities.setCauchoVPlow(cursor.getString(50));
            bandaTransportadoraEntities.setAnchoVPlow(cursor.getString(51));
            bandaTransportadoraEntities.setEspesorVPlow(cursor.getString(52));
            bandaTransportadoraEntities.setTipoRevestimientoTolvaCarga(cursor.getString(53));
            bandaTransportadoraEntities.setEstadoRevestimientoTolvaCarga(cursor.getString(54));
            bandaTransportadoraEntities.setDuracionPromedioRevestimiento(cursor.getString(55));
            bandaTransportadoraEntities.setDeflectores(cursor.getString(56));
            bandaTransportadoraEntities.setAltureCaida(cursor.getString(57));
            bandaTransportadoraEntities.setLongitudImpacto(cursor.getString(58));
            bandaTransportadoraEntities.setMaterial(cursor.getString(59));
            bandaTransportadoraEntities.setAnguloSobreCarga(cursor.getString(60));
            bandaTransportadoraEntities.setAtaqueQuimicoTransportadora(cursor.getString(61));
            bandaTransportadoraEntities.setAtaqueTemperaturaTransportadora(cursor.getString(62));
            bandaTransportadoraEntities.setAtaqueAceiteTransportadora(cursor.getString(63));
            bandaTransportadoraEntities.setAtaqueImpactoTransportadora(cursor.getString(64));
            bandaTransportadoraEntities.setCapacidadTransportadora(cursor.getString(65));
            bandaTransportadoraEntities.setHorasTrabajoPorDiaTransportadora(cursor.getString(66));
            bandaTransportadoraEntities.setDiasTrabajPorSemanaTransportadora(cursor.getString(67));
            bandaTransportadoraEntities.setAlimentacionCentradaTransportadora(cursor.getString(68));
            bandaTransportadoraEntities.setAbrasividadTransportadora(cursor.getString(69));
            bandaTransportadoraEntities.setPorcentajeFinosTransportadora(cursor.getString(70));
            bandaTransportadoraEntities.setMaxGranulometriaTransportadora(cursor.getString(71));
            bandaTransportadoraEntities.setMaxPesoTransportadora(cursor.getString(72));
            bandaTransportadoraEntities.setDensidadTransportadora(cursor.getString(73));
            bandaTransportadoraEntities.setTempMaximaMaterialSobreBandaTransportadora(cursor.getString(74));
            bandaTransportadoraEntities.setTempPromedioMaterialSobreBandaTransportadora(cursor.getString(75));
            bandaTransportadoraEntities.setFugaDeMaterialesEnLaColaDelChute(cursor.getString(76));
            bandaTransportadoraEntities.setFugaDeMaterialesPorLosCostados(cursor.getString(77));
            bandaTransportadoraEntities.setFugaMateriales(cursor.getString(78));
            bandaTransportadoraEntities.setCajaColaDeTolva(cursor.getString(79));
            bandaTransportadoraEntities.setFugaDeMaterialParticulaALaSalidaDelChute(cursor.getString(80));
            bandaTransportadoraEntities.setAnchoChute(cursor.getString(81));
            bandaTransportadoraEntities.setLargoChute(cursor.getString(82));
            bandaTransportadoraEntities.setAlturaChute(cursor.getString(83));
            bandaTransportadoraEntities.setAbrazadera(cursor.getString(84));
            bandaTransportadoraEntities.setCauchoGuardabandas(cursor.getString(85));
            bandaTransportadoraEntities.setTriSealMultiSeal(cursor.getString(86));
            bandaTransportadoraEntities.setEspesorGuardaBandas(cursor.getString(87));
            bandaTransportadoraEntities.setAnchoGuardaBandas(cursor.getString(88));
            bandaTransportadoraEntities.setLargoGuardaBandas(cursor.getString(89));
            bandaTransportadoraEntities.setProtectorGuardaBandas(cursor.getString(90));
            bandaTransportadoraEntities.setCortinaAntiPolvo1(cursor.getString(91));
            bandaTransportadoraEntities.setCortinaAntiPolvo2(cursor.getString(92));
            bandaTransportadoraEntities.setCortinaAntiPolvo3(cursor.getString(93));
            bandaTransportadoraEntities.setBoquillasCanonesDeAire(cursor.getString(94));
            bandaTransportadoraEntities.setTempAmbienteMaxTransportadora(cursor.getString(95));
            bandaTransportadoraEntities.setTempAmbienteMinTransportadora(cursor.getString(96));
            bandaTransportadoraEntities.setTieneRodillosImpacto(cursor.getString(97));
            bandaTransportadoraEntities.setCamaImpacto(cursor.getString(98));
            bandaTransportadoraEntities.setCamaSellado(cursor.getString(99));
            bandaTransportadoraEntities.setBasculaPesaje(cursor.getString(100));
            bandaTransportadoraEntities.setRodilloCarga(cursor.getString(101));
            bandaTransportadoraEntities.setRodilloImpacto(cursor.getString(102));
            bandaTransportadoraEntities.setBasculaASGCO(cursor.getString(103));
            bandaTransportadoraEntities.setBarraImpacto(cursor.getString(104));
            bandaTransportadoraEntities.setBarraDeslizamiento(cursor.getString(105));
            bandaTransportadoraEntities.setEspesorUHMV(cursor.getString(106));
            bandaTransportadoraEntities.setAnchoBarra(cursor.getString(107));
            bandaTransportadoraEntities.setLargoBarra(cursor.getString(108));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1(cursor.getString(109));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2(cursor.getString(110));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3(cursor.getString(111));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1AntesPoleaMotriz(cursor.getString(112));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2AntesPoleaMotriz(cursor.getString(113));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3AntesPoleaMotriz(cursor.getString(114));
            bandaTransportadoraEntities.setIntegridadSoportesRodilloImpacto(cursor.getString(115));
            bandaTransportadoraEntities.setMaterialAtrapadoEntreCortinas(cursor.getString(116));
            bandaTransportadoraEntities.setMaterialAtrapadoEntreGuardabandas(cursor.getString(117));
            bandaTransportadoraEntities.setMaterialAtrapadoEnBanda(cursor.getString(118));
            bandaTransportadoraEntities.setIntegridadSoportesCamaImpacto(cursor.getString(119));
            bandaTransportadoraEntities.setInclinacionZonaCargue(cursor.getString(120));
            bandaTransportadoraEntities.setSistemaAlineacionCarga(cursor.getString(121));
            bandaTransportadoraEntities.setCantidadSistemaAlineacionEnCarga(cursor.getString(122));
            bandaTransportadoraEntities.setSistemasAlineacionCargaFuncionando(cursor.getString(123));
            bandaTransportadoraEntities.setSistemaAlineacionEnRetorno(cursor.getString(124));
            bandaTransportadoraEntities.setCantidadSistemaAlineacionEnRetorno(cursor.getString(125));
            bandaTransportadoraEntities.setSistemasAlineacionRetornoFuncionando(cursor.getString(126));
            bandaTransportadoraEntities.setSistemaAlineacionRetornoPlano(cursor.getString(127));
            bandaTransportadoraEntities.setSistemaAlineacionArtesaCarga(cursor.getString(128));
            bandaTransportadoraEntities.setSistemaAlineacionRetornoEnV(cursor.getString(129));
            bandaTransportadoraEntities.setLargoEjeRodilloCentralCarga(cursor.getString(130));
            bandaTransportadoraEntities.setDiametroEjeRodilloCentralCarga(cursor.getString(131));
            bandaTransportadoraEntities.setLargoTuboRodilloCentralCarga(cursor.getString(132));
            bandaTransportadoraEntities.setLargoEjeRodilloLateralCarga(cursor.getString(133));
            bandaTransportadoraEntities.setDiametroEjeRodilloLateralCarga(cursor.getString(134));
            bandaTransportadoraEntities.setDiametroRodilloLateralCarga(cursor.getString(135));
            bandaTransportadoraEntities.setLargoTuboRodilloLateralCarga(cursor.getString(136));
            bandaTransportadoraEntities.setTipoRodilloCarga(cursor.getString(137));
            bandaTransportadoraEntities.setDistanciaEntreArtesasCarga(cursor.getString(138));
            bandaTransportadoraEntities.setAnchoInternoChasisRodilloCarga(cursor.getString(139));
            bandaTransportadoraEntities.setAnchoExternoChasisRodilloCarga(cursor.getString(140));
            bandaTransportadoraEntities.setAnguloAcanalamientoArtesaCArga(cursor.getString(141));
            bandaTransportadoraEntities.setDetalleRodilloCentralCarga(cursor.getString(142));
            bandaTransportadoraEntities.setDetalleRodilloLateralCarg(cursor.getString(143));
            bandaTransportadoraEntities.setDiametroPoleaMotrizTransportadora(cursor.getString(144));
            bandaTransportadoraEntities.setAnchoPoleaMotrizTransportadora(cursor.getString(145));
            bandaTransportadoraEntities.setTipoPoleaMotrizTransportadora(cursor.getString(146));
            bandaTransportadoraEntities.setLargoEjePoleaMotrizTransportadora(cursor.getString(147));
            bandaTransportadoraEntities.setDiametroEjeMotrizTransportadora(cursor.getString(148));
            bandaTransportadoraEntities.setIcobandasCentraEnPoleaMotrizTransportadora(cursor.getString(149));
            bandaTransportadoraEntities.setAnguloAmarrePoleaMotrizTransportadora(cursor.getString(150));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaMotrizTransportadora(cursor.getString(151));
            bandaTransportadoraEntities.setTipoTransicionPoleaMotrizTransportadora(cursor.getString(152));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaMotrizTransportadora(cursor.getString(153));
            bandaTransportadoraEntities.setPotenciaMotorTransportadora(cursor.getString(154));
            bandaTransportadoraEntities.setGuardaPoleaMotrizTransportadora(cursor.getString(155));
            bandaTransportadoraEntities.setAnchoEstructura(cursor.getString(156));
            bandaTransportadoraEntities.setAnchoTrayectoCarga(cursor.getString(157));
            bandaTransportadoraEntities.setPasarelaRespectoAvanceBanda(cursor.getString(158));
            bandaTransportadoraEntities.setMaterialAlimenticioTransportadora(cursor.getString(159));
            bandaTransportadoraEntities.setMaterialAcidoTransportadora(cursor.getString(160));
            bandaTransportadoraEntities.setMaterialTempEntre80y150Transportadora(cursor.getString(161));
            bandaTransportadoraEntities.setMaterialSecoTransportadora(cursor.getString(162));
            bandaTransportadoraEntities.setMaterialHumedoTransportadora(cursor.getString(163));
            bandaTransportadoraEntities.setMaterialAbrasivoFinoTransportadora(cursor.getString(164));
            bandaTransportadoraEntities.setMaterialPegajosoTransportadora(cursor.getString(165));
            bandaTransportadoraEntities.setMaterialGrasosoAceitosoTransportadora(cursor.getString(166));
            bandaTransportadoraEntities.setMarcaLimpiadorPrimario(cursor.getString(167));
            bandaTransportadoraEntities.setReferenciaLimpiadorPrimario(cursor.getString(168));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorPrimario(cursor.getString(169));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorPrimario(cursor.getString(170));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorPrimario(cursor.getString(171));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorPrimario(cursor.getString(172));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorPrimario(cursor.getString(173));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla(cursor.getString(174));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda(cursor.getString(175));
            bandaTransportadoraEntities.setMarcaLimpiadorSecundario(cursor.getString(176));
            bandaTransportadoraEntities.setReferenciaLimpiadorSecundario(cursor.getString(177));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorSecundario(cursor.getString(178));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorSecundario(cursor.getString(179));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorSecundario(cursor.getString(180));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorSecundario(cursor.getString(181));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorSecundario(cursor.getString(182));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla1(cursor.getString(183));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda1(cursor.getString(184));
            bandaTransportadoraEntities.setSistemaDribbleChute(cursor.getString(185));
            bandaTransportadoraEntities.setMarcaLimpiadorTerciario(cursor.getString(186));
            bandaTransportadoraEntities.setReferenciaLimpiadorTerciario(cursor.getString(187));
            bandaTransportadoraEntities.setAnchoCuchillaLimpiadorTerciario(cursor.getString(188));
            bandaTransportadoraEntities.setAltoCuchillaLimpiadorTerciario(cursor.getString(189));
            bandaTransportadoraEntities.setEstadoCuchillaLimpiadorTerciario(cursor.getString(190));
            bandaTransportadoraEntities.setEstadoTensorLimpiadorTerciario(cursor.getString(191));
            bandaTransportadoraEntities.setEstadoTuboLimpiadorTerciario(cursor.getString(192));
            bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla2(cursor.getString(193));
            bandaTransportadoraEntities.setCuchillaEnContactoConBanda2(cursor.getString(194));
            bandaTransportadoraEntities.setEstadoRodilloRetorno(cursor.getString(195));
            bandaTransportadoraEntities.setLargoEjeRodilloRetorno(cursor.getString(196));
            bandaTransportadoraEntities.setDiametroEjeRodilloRetorno(cursor.getString(197));
            bandaTransportadoraEntities.setDiametroRodilloRetorno(cursor.getString(198));
            bandaTransportadoraEntities.setLargoTuboRodilloRetorno(cursor.getString(199));
            bandaTransportadoraEntities.setTipoRodilloRetorno(cursor.getString(200));
            bandaTransportadoraEntities.setDistanciaEntreRodillosRetorno(cursor.getString(201));
            bandaTransportadoraEntities.setAnchoInternoChasisRetorno(cursor.getString(202));
            bandaTransportadoraEntities.setAnchoExternoChasisRetorno(cursor.getString(203));
            bandaTransportadoraEntities.setDetalleRodilloRetorno(cursor.getString(204));
            bandaTransportadoraEntities.setDiametroPoleaAmarrePoleaMotriz(cursor.getString(205));
            bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaMotriz(cursor.getString(206));
            bandaTransportadoraEntities.setTipoPoleaAmarrePoleaMotriz(cursor.getString(207));
            bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaMotriz(cursor.getString(208));
            bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaMotriz(cursor.getString(209));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaMotriz(cursor.getString(210));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaMotriz(cursor.getString(211));
            bandaTransportadoraEntities.setDimetroPoleaAmarrePoleaCola(cursor.getString(212));
            bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaCola(cursor.getString(213));
            bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaCola(cursor.getString(214));
            bandaTransportadoraEntities.setTipoPoleaAmarrePoleaCola(cursor.getString(215));
            bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaCola(cursor.getString(216));
            bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaCola(cursor.getString(217));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaCola(cursor.getString(218));
            bandaTransportadoraEntities.setDiametroPoleaTensora(cursor.getString(219));
            bandaTransportadoraEntities.setAnchoPoleaTensora(cursor.getString(220));
            bandaTransportadoraEntities.setTipoPoleaTensora(cursor.getString(221));
            bandaTransportadoraEntities.setLargoEjePoleaTensora(cursor.getString(222));
            bandaTransportadoraEntities.setDiametroEjePoleaTensora(cursor.getString(223));
            bandaTransportadoraEntities.setIcobandasCentradaEnPoleaTensora(cursor.getString(224));
            bandaTransportadoraEntities.setRecorridoPoleaTensora(cursor.getString(225));
            bandaTransportadoraEntities.setEstadoRevestimientoPoleaTensora(cursor.getString(226));
            bandaTransportadoraEntities.setTipoTransicionPoleaTensora(cursor.getString(227));
            bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTensora(cursor.getString(228));
            bandaTransportadoraEntities.setPotenciaMotorPoleaTensora(cursor.getString(229));
            bandaTransportadoraEntities.setGuardaPoleaTensora(cursor.getString(230));
            bandaTransportadoraEntities.setPuertasInspeccion(cursor.getString(231));
            bandaTransportadoraEntities.setGuardaRodilloRetornoPlano(cursor.getString(232));
            bandaTransportadoraEntities.setGuardaTruTrainer(cursor.getString(233));
            bandaTransportadoraEntities.setGuardaPoleaDeflectora(cursor.getString(234));
            bandaTransportadoraEntities.setGuardaZonaDeTransito(cursor.getString(235));
            bandaTransportadoraEntities.setGuardaMotores(cursor.getString(236));
            bandaTransportadoraEntities.setGuardaCadenas(cursor.getString(237));
            bandaTransportadoraEntities.setGuardaCorreas(cursor.getString(238));
            bandaTransportadoraEntities.setInterruptoresDeSeguridad(cursor.getString(239));
            bandaTransportadoraEntities.setSirenasDeSeguridad(cursor.getString(240));
            bandaTransportadoraEntities.setGuardaRodilloRetornoV(cursor.getString(241));
            bandaTransportadoraEntities.setDiametroRodilloCentralCarga(cursor.getString(242));
            bandaTransportadoraEntities.setTipoRodilloImpacto(cursor.getString(243));
            bandaTransportadoraEntities.setIntegridadSoporteCamaSellado(cursor.getString(244));
            bandaTransportadoraEntities.setAtaqueAbrasivoTransportadora(cursor.getString(245));
            bandaTransportadoraEntities.setObservacionRegistroTransportadora(cursor.getString(247));
            bandaTransportadoraEntitiesArraylist.add(bandaTransportadoraEntities);

            db.execSQL("Update bandaTransportadora set estadoRegistroTransportadora='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarHorizontal";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    recargarBD();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MDToast.makeText(MainActivity.this, "ERROR DE CONEXIÓN, INTENTE NUEVAMENTE O CONTACTE CON EL ADMINISTRADOR", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show(); dialogCarga.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idRegistro", bandaTransportadoraEntitiesArraylist.get(0).getIdRegistro());
                    params.put("marcaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMarcaBandaTransportadora());
                    params.put("anchoBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBandaTransportadora());
                    params.put("noLonasBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getNoLonasBandaTransportadora());
                    params.put("tipoLonaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoLonaBandaTransportadora());
                    params.put("espesorTotalBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorTotalBandaTransportadora());
                    params.put("espesorCubiertaSuperiorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaSuperiorTransportadora());
                    params.put("espesorCojinTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCojinTransportadora());
                    params.put("espesorCubiertaInferiorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaInferiorTransportadora());
                    params.put("tipoCubiertaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoCubiertaTransportadora());
                    params.put("tipoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoEmpalmeTransportadora());
                    params.put("estadoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoEmpalmeTransportadora());
                    params.put("distanciaEntrePoleasBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntrePoleasBandaHorizontal());
                    params.put("inclinacionBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getInclinacionBandaHorizontal());
                    params.put("recorridoUtilTensorBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getRecorridoUtilTensorBandaHorizontal());
                    params.put("longitudSinfinBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getLongitudSinfinBandaHorizontal());
                    params.put("resistenciaRoturaLonaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getResistenciaRoturaLonaTransportadora());
                    params.put("localizacionTensorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLocalizacionTensorTransportadora());
                    params.put("bandaReversible", bandaTransportadoraEntitiesArraylist.get(0).getBandaReversible());
                    params.put("bandaDeArrastre", bandaTransportadoraEntitiesArraylist.get(0).getBandaDeArrastre());
                    params.put("velocidadBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getVelocidadBandaHorizontal());
                    params.put("marcaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getMarcaBandaHorizontalAnterior());
                    params.put("anchoBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBandaHorizontalAnterior());
                    params.put("noLonasBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getNoLonasBandaHorizontalAnterior());
                    params.put("tipoLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoLonaBandaHorizontalAnterior());
                    params.put("espesorTotalBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorTotalBandaHorizontalAnterior());
                    params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaSuperiorBandaHorizontalAnterior());
                    params.put("espesorCubiertaInferiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCubiertaInferiorBandaHorizontalAnterior());
                    params.put("espesorCojinBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getEspesorCojinBandaHorizontalAnterior());
                    params.put("tipoEmpalmeBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoEmpalmeBandaHorizontalAnterior());
                    params.put("resistenciaRoturaLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getResistenciaRoturaLonaBandaHorizontalAnterior());
                    params.put("tipoCubiertaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTipoCubiertaBandaHorizontalAnterior());
                    params.put("tonsTransportadasBandaHoizontalAnterior", bandaTransportadoraEntitiesArraylist.get(0).getTonsTransportadasBandaHoizontalAnterior());
                    params.put("causaFallaCambioBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getCausaFallaCambioBandaHorizontal());
                    params.put("diametroPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaColaTransportadora());
                    params.put("anchoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaColaTransportadora());
                    params.put("tipoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaColaTransportadora());
                    params.put("largoEjePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaColaTransportadora());
                    params.put("diametroEjePoleaColaHorizontal", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaColaHorizontal());
                    params.put("icobandasCentradaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaColaTransportadora());
                    params.put("anguloAmarrePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAmarrePoleaColaTransportadora());
                    params.put("estadoRvtoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRvtoPoleaColaTransportadora());
                    params.put("tipoTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaColaTransportadora());
                    params.put("distanciaTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaColaTransportadora());
                    params.put("longitudTensorTornilloPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLongitudTensorTornilloPoleaColaTransportadora());
                    params.put("longitudRecorridoContrapesaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLongitudRecorridoContrapesaPoleaColaTransportadora());
                    params.put("guardaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaColaTransportadora());
                    params.put("hayDesviador", bandaTransportadoraEntitiesArraylist.get(0).getHayDesviador());
                    params.put("elDesviadorBascula", bandaTransportadoraEntitiesArraylist.get(0).getElDesviadorBascula());
                    params.put("presionUniformeALoAnchoDeLaBanda", bandaTransportadoraEntitiesArraylist.get(0).getPresionUniformeALoAnchoDeLaBanda());
                    params.put("cauchoVPlow", bandaTransportadoraEntitiesArraylist.get(0).getCauchoVPlow());
                    params.put("anchoVPlow", bandaTransportadoraEntitiesArraylist.get(0).getAnchoVPlow());
                    params.put("espesorVPlow", bandaTransportadoraEntitiesArraylist.get(0).getEspesorVPlow());
                    params.put("tipoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(0).getTipoRevestimientoTolvaCarga());
                    params.put("estadoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoTolvaCarga());
                    params.put("duracionPromedioRevestimiento", bandaTransportadoraEntitiesArraylist.get(0).getDuracionPromedioRevestimiento());
                    params.put("deflectores", bandaTransportadoraEntitiesArraylist.get(0).getDeflectores());
                    params.put("altureCaida", bandaTransportadoraEntitiesArraylist.get(0).getAltureCaida());
                    params.put("longitudImpacto", bandaTransportadoraEntitiesArraylist.get(0).getLongitudImpacto());
                    params.put("material", bandaTransportadoraEntitiesArraylist.get(0).getMaterial());
                    params.put("anguloSobreCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnguloSobreCarga());
                    params.put("ataqueQuimicoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueQuimicoTransportadora());
                    params.put("ataqueTemperaturaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueTemperaturaTransportadora());
                    params.put("ataqueAceiteTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueAceiteTransportadora());
                    params.put("ataqueImpactoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueImpactoTransportadora());
                    params.put("capacidadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getCapacidadTransportadora());
                    params.put("horasTrabajoPorDiaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getHorasTrabajoPorDiaTransportadora());
                    params.put("diasTrabajPorSemanaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiasTrabajPorSemanaTransportadora());
                    params.put("alimentacionCentradaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAlimentacionCentradaTransportadora());
                    params.put("abrasividadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAbrasividadTransportadora());
                    params.put("porcentajeFinosTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getPorcentajeFinosTransportadora());
                    params.put("maxGranulometriaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaxGranulometriaTransportadora());
                    params.put("maxPesoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaxPesoTransportadora());
                    params.put("densidadTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDensidadTransportadora());
                    params.put("tempMaximaMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempMaximaMaterialSobreBandaTransportadora());
                    params.put("tempPromedioMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempPromedioMaterialSobreBandaTransportadora());
                    params.put("fugaDeMaterialesEnLaColaDelChute", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialesEnLaColaDelChute());
                    params.put("fugaDeMaterialesPorLosCostados", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialesPorLosCostados());
                    params.put("fugaMateriales", bandaTransportadoraEntitiesArraylist.get(0).getFugaMateriales());
                    params.put("cajaColaDeTolva", bandaTransportadoraEntitiesArraylist.get(0).getCajaColaDeTolva());
                    params.put("fugaDeMaterialParticulaALaSalidaDelChute", bandaTransportadoraEntitiesArraylist.get(0).getFugaDeMaterialParticulaALaSalidaDelChute());
                    params.put("anchoChute", bandaTransportadoraEntitiesArraylist.get(0).getAnchoChute());
                    params.put("largoChute", bandaTransportadoraEntitiesArraylist.get(0).getLargoChute());
                    params.put("alturaChute", bandaTransportadoraEntitiesArraylist.get(0).getAlturaChute());
                    params.put("abrazadera", bandaTransportadoraEntitiesArraylist.get(0).getAbrazadera());
                    params.put("cauchoGuardabandas", bandaTransportadoraEntitiesArraylist.get(0).getCauchoGuardabandas());
                    params.put("triSealMultiSeal", bandaTransportadoraEntitiesArraylist.get(0).getTriSealMultiSeal());
                    params.put("espesorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getEspesorGuardaBandas());
                    params.put("anchoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getAnchoGuardaBandas());
                    params.put("largoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getLargoGuardaBandas());
                    params.put("protectorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(0).getProtectorGuardaBandas());
                    params.put("cortinaAntiPolvo1", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo1());
                    params.put("cortinaAntiPolvo2", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo2());
                    params.put("cortinaAntiPolvo3", bandaTransportadoraEntitiesArraylist.get(0).getCortinaAntiPolvo3());
                    params.put("boquillasCanonesDeAire", bandaTransportadoraEntitiesArraylist.get(0).getBoquillasCanonesDeAire());
                    params.put("tempAmbienteMaxTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempAmbienteMaxTransportadora());
                    params.put("tempAmbienteMinTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTempAmbienteMinTransportadora());
                    params.put("tieneRodillosImpacto", bandaTransportadoraEntitiesArraylist.get(0).getTieneRodillosImpacto());
                    params.put("camaImpacto", bandaTransportadoraEntitiesArraylist.get(0).getCamaImpacto());
                    params.put("camaSellado", bandaTransportadoraEntitiesArraylist.get(0).getCamaSellado());
                    params.put("basculaPesaje", bandaTransportadoraEntitiesArraylist.get(0).getBasculaPesaje());
                    params.put("rodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getRodilloCarga());
                    params.put("rodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getRodilloImpacto());
                    params.put("basculaASGCO", bandaTransportadoraEntitiesArraylist.get(0).getBasculaASGCO());
                    params.put("barraImpacto", bandaTransportadoraEntitiesArraylist.get(0).getBarraImpacto());
                    params.put("barraDeslizamiento", bandaTransportadoraEntitiesArraylist.get(0).getBarraDeslizamiento());
                    params.put("espesorUHMV", bandaTransportadoraEntitiesArraylist.get(0).getEspesorUHMV());
                    params.put("anchoBarra", bandaTransportadoraEntitiesArraylist.get(0).getAnchoBarra());
                    params.put("largoBarra", bandaTransportadoraEntitiesArraylist.get(0).getLargoBarra());
                    params.put("anguloAcanalamientoArtesa1", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa1());
                    params.put("anguloAcanalamientoArtesa2", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa2());
                    params.put("anguloAcanalamientoArtesa3", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa3());
                    params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa1AntesPoleaMotriz());
                    params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa2AntesPoleaMotriz());
                    params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesa3AntesPoleaMotriz());
                    params.put("integridadSoportesRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoportesRodilloImpacto());
                    params.put("materialAtrapadoEntreCortinas", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEntreCortinas());
                    params.put("materialAtrapadoEntreGuardabandas", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEntreGuardabandas());
                    params.put("materialAtrapadoEnBanda", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAtrapadoEnBanda());
                    params.put("integridadSoportesCamaImpacto", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoportesCamaImpacto());
                    params.put("inclinacionZonaCargue", bandaTransportadoraEntitiesArraylist.get(0).getInclinacionZonaCargue());
                    params.put("sistemaAlineacionCarga", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionCarga());
                    params.put("cantidadSistemaAlineacionEnCarga", bandaTransportadoraEntitiesArraylist.get(0).getCantidadSistemaAlineacionEnCarga());
                    params.put("sistemasAlineacionCargaFuncionando", bandaTransportadoraEntitiesArraylist.get(0).getSistemasAlineacionCargaFuncionando());
                    params.put("sistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionEnRetorno());
                    params.put("cantidadSistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(0).getCantidadSistemaAlineacionEnRetorno());
                    params.put("sistemasAlineacionRetornoFuncionando", bandaTransportadoraEntitiesArraylist.get(0).getSistemasAlineacionRetornoFuncionando());
                    params.put("sistemaAlineacionRetornoPlano", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionRetornoPlano());
                    params.put("sistemaAlineacionArtesaCarga", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionArtesaCarga());
                    params.put("sistemaAlineacionRetornoEnV", bandaTransportadoraEntitiesArraylist.get(0).getSistemaAlineacionRetornoEnV());
                    params.put("largoEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloCentralCarga());
                    params.put("diametroEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloCentralCarga());
                    params.put("largoTuboRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloCentralCarga());
                    params.put("largoEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloLateralCarga());
                    params.put("diametroEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloLateralCarga());
                    params.put("diametroRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloLateralCarga());
                    params.put("largoTuboRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloLateralCarga());
                    params.put("tipoRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloCarga());
                    params.put("distanciaEntreArtesasCarga", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntreArtesasCarga());
                    params.put("anchoInternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoInternoChasisRodilloCarga());
                    params.put("anchoExternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoExternoChasisRodilloCarga());
                    params.put("anguloAcanalamientoArtesaCArga", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAcanalamientoArtesaCArga());
                    params.put("detalleRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloCentralCarga());
                    params.put("detalleRodilloLateralCarg", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloLateralCarg());
                    params.put("diametroPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaMotrizTransportadora());
                    params.put("anchoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaMotrizTransportadora());
                    params.put("tipoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaMotrizTransportadora());
                    params.put("largoEjePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaMotrizTransportadora());
                    params.put("diametroEjeMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeMotrizTransportadora());
                    params.put("icobandasCentraEnPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentraEnPoleaMotrizTransportadora());
                    params.put("anguloAmarrePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAnguloAmarrePoleaMotrizTransportadora());
                    params.put("estadoRevestimientoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaMotrizTransportadora());
                    params.put("tipoTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaMotrizTransportadora());
                    params.put("distanciaTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaMotrizTransportadora());
                    params.put("potenciaMotorTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getPotenciaMotorTransportadora());
                    params.put("guardaPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaMotrizTransportadora());
                    params.put("anchoEstructura", bandaTransportadoraEntitiesArraylist.get(0).getAnchoEstructura());
                    params.put("anchoTrayectoCarga", bandaTransportadoraEntitiesArraylist.get(0).getAnchoTrayectoCarga());
                    params.put("pasarelaRespectoAvanceBanda", bandaTransportadoraEntitiesArraylist.get(0).getPasarelaRespectoAvanceBanda());
                    params.put("materialAlimenticioTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAlimenticioTransportadora());
                    params.put("materialAcidoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAcidoTransportadora());
                    params.put("materialTempEntre80y150Transportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialTempEntre80y150Transportadora());
                    params.put("materialSecoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialSecoTransportadora());
                    params.put("materialHumedoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialHumedoTransportadora());
                    params.put("materialAbrasivoFinoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialAbrasivoFinoTransportadora());
                    params.put("materialPegajosoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialPegajosoTransportadora());
                    params.put("materialGrasosoAceitosoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getMaterialGrasosoAceitosoTransportadora());
                    params.put("marcaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorPrimario());
                    params.put("referenciaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorPrimario());
                    params.put("anchoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorPrimario());
                    params.put("altoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorPrimario());
                    params.put("estadoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorPrimario());
                    params.put("estadoTensorLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorPrimario());
                    params.put("estadoTuboLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorPrimario());
                    params.put("frecuenciaRevisionCuchilla", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla());
                    params.put("cuchillaEnContactoConBanda", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda());
                    params.put("marcaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorSecundario());
                    params.put("referenciaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorSecundario());
                    params.put("anchoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorSecundario());
                    params.put("altoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorSecundario());
                    params.put("estadoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorSecundario());
                    params.put("estadoTensorLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorSecundario());
                    params.put("estadoTuboLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorSecundario());
                    params.put("frecuenciaRevisionCuchilla1", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla1());
                    params.put("cuchillaEnContactoConBanda1", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda1());
                    params.put("sistemaDribbleChute", bandaTransportadoraEntitiesArraylist.get(0).getSistemaDribbleChute());
                    params.put("marcaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getMarcaLimpiadorTerciario());
                    params.put("referenciaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getReferenciaLimpiadorTerciario());
                    params.put("anchoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getAnchoCuchillaLimpiadorTerciario());
                    params.put("altoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getAltoCuchillaLimpiadorTerciario());
                    params.put("estadoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoCuchillaLimpiadorTerciario());
                    params.put("estadoTensorLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTensorLimpiadorTerciario());
                    params.put("estadoTuboLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(0).getEstadoTuboLimpiadorTerciario());
                    params.put("frecuenciaRevisionCuchilla2", bandaTransportadoraEntitiesArraylist.get(0).getFrecuenciaRevisionCuchilla2());
                    params.put("cuchillaEnContactoConBanda2", bandaTransportadoraEntitiesArraylist.get(0).getCuchillaEnContactoConBanda2());
                    params.put("estadoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRodilloRetorno());
                    params.put("largoEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjeRodilloRetorno());
                    params.put("diametroEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjeRodilloRetorno());
                    params.put("diametroRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloRetorno());
                    params.put("largoTuboRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getLargoTuboRodilloRetorno());
                    params.put("tipoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloRetorno());
                    params.put("distanciaEntreRodillosRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaEntreRodillosRetorno());
                    params.put("anchoInternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(0).getAnchoInternoChasisRetorno());
                    params.put("anchoExternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(0).getAnchoExternoChasisRetorno());
                    params.put("detalleRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(0).getDetalleRodilloRetorno());
                    params.put("diametroPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaAmarrePoleaMotriz());
                    params.put("anchoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaAmarrePoleaMotriz());
                    params.put("tipoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaAmarrePoleaMotriz());
                    params.put("largoEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaAmarrePoleaMotriz());
                    params.put("diametroEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaAmarrePoleaMotriz());
                    params.put("icobandasCentradaPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaAmarrePoleaMotriz());
                    params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaAmarrePoleaMotriz());
                    params.put("dimetroPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getDimetroPoleaAmarrePoleaCola());
                    params.put("anchoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaAmarrePoleaCola());
                    params.put("largoEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaAmarrePoleaCola());
                    params.put("tipoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaAmarrePoleaCola());
                    params.put("diametroEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaAmarrePoleaCola());
                    params.put("icobandasCentradaPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaPoleaAmarrePoleaCola());
                    params.put("estadoRevestimientoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaAmarrePoleaCola());
                    params.put("diametroPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroPoleaTensora());
                    params.put("anchoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getAnchoPoleaTensora());
                    params.put("tipoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getTipoPoleaTensora());
                    params.put("largoEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getLargoEjePoleaTensora());
                    params.put("diametroEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDiametroEjePoleaTensora());
                    params.put("icobandasCentradaEnPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getIcobandasCentradaEnPoleaTensora());
                    params.put("recorridoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getRecorridoPoleaTensora());
                    params.put("estadoRevestimientoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getEstadoRevestimientoPoleaTensora());
                    params.put("tipoTransicionPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getTipoTransicionPoleaTensora());
                    params.put("distanciaTransicionPoleaColaTensora", bandaTransportadoraEntitiesArraylist.get(0).getDistanciaTransicionPoleaColaTensora());
                    params.put("potenciaMotorPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getPotenciaMotorPoleaTensora());
                    params.put("guardaPoleaTensora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaTensora());
                    params.put("puertasInspeccion", bandaTransportadoraEntitiesArraylist.get(0).getPuertasInspeccion());
                    params.put("guardaRodilloRetornoPlano", bandaTransportadoraEntitiesArraylist.get(0).getGuardaRodilloRetornoPlano());
                    params.put("guardaTruTrainer", bandaTransportadoraEntitiesArraylist.get(0).getGuardaTruTrainer());
                    params.put("guardaPoleaDeflectora", bandaTransportadoraEntitiesArraylist.get(0).getGuardaPoleaDeflectora());
                    params.put("guardaZonaDeTransito", bandaTransportadoraEntitiesArraylist.get(0).getGuardaZonaDeTransito());
                    params.put("guardaMotores", bandaTransportadoraEntitiesArraylist.get(0).getGuardaMotores());
                    params.put("guardaCadenas", bandaTransportadoraEntitiesArraylist.get(0).getGuardaCadenas());
                    params.put("guardaCorreas", bandaTransportadoraEntitiesArraylist.get(0).getGuardaCorreas());
                    params.put("interruptoresDeSeguridad", bandaTransportadoraEntitiesArraylist.get(0).getInterruptoresDeSeguridad());
                    params.put("sirenasDeSeguridad", bandaTransportadoraEntitiesArraylist.get(0).getSirenasDeSeguridad());
                    params.put("guardaRodilloRetornoV", bandaTransportadoraEntitiesArraylist.get(0).getGuardaRodilloRetornoV());
                    params.put("diametroRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(0).getDiametroRodilloCentralCarga());
                    params.put("tipoRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(0).getTipoRodilloImpacto());
                    params.put("integridadSoporteCamaSellado", bandaTransportadoraEntitiesArraylist.get(0).getIntegridadSoporteCamaSellado());
                    params.put("ataqueAbrasivoTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getAtaqueAbrasivoTransportadora());
                    params.put("observacionRegistroTransportadora", bandaTransportadoraEntitiesArraylist.get(0).getObservacionRegistroTransportadora());

                    Log.e("PARAMS", params.toString());

                    for(Map.Entry entry:params.entrySet()){
                        if(entry.getValue()==null)
                        {
                            String campo=entry.getKey().toString();

                            params.put(campo, "");
                        }
                    }

                    return params;
                }

            };
            requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(requestClientes);


        }
        else {

            try {
                final ArrayList<BandaTransportadoraEntities> bandaTransportadoraEntitiesArraylist = new ArrayList<>();


                while (cursor.moveToNext()) {


                    BandaTransportadoraEntities bandaTransportadoraEntities= new BandaTransportadoraEntities();
                    bandaTransportadoraEntities.setIdRegistro(cursor.getString(0));
                    bandaTransportadoraEntities.setMarcaBandaTransportadora(cursor.getString(1));
                    bandaTransportadoraEntities.setAnchoBandaTransportadora(cursor.getString(2));
                    bandaTransportadoraEntities.setNoLonasBandaTransportadora(cursor.getString(3));
                    bandaTransportadoraEntities.setTipoLonaBandaTransportadora(cursor.getString(4));
                    bandaTransportadoraEntities.setEspesorTotalBandaTransportadora(cursor.getString(5));
                    bandaTransportadoraEntities.setEspesorCubiertaSuperiorTransportadora(cursor.getString(6));
                    bandaTransportadoraEntities.setEspesorCojinTransportadora(cursor.getString(7));
                    bandaTransportadoraEntities.setEspesorCubiertaInferiorTransportadora(cursor.getString(8));
                    bandaTransportadoraEntities.setTipoCubiertaTransportadora(cursor.getString(9));
                    bandaTransportadoraEntities.setTipoEmpalmeTransportadora(cursor.getString(10));
                    bandaTransportadoraEntities.setEstadoEmpalmeTransportadora(cursor.getString(11));
                    bandaTransportadoraEntities.setDistanciaEntrePoleasBandaHorizontal(cursor.getString(12));
                    bandaTransportadoraEntities.setInclinacionBandaHorizontal(cursor.getString(13));
                    bandaTransportadoraEntities.setRecorridoUtilTensorBandaHorizontal(cursor.getString(14));
                    bandaTransportadoraEntities.setLongitudSinfinBandaHorizontal(cursor.getString(15));
                    bandaTransportadoraEntities.setResistenciaRoturaLonaTransportadora(cursor.getString(16));
                    bandaTransportadoraEntities.setLocalizacionTensorTransportadora(cursor.getString(17));
                    bandaTransportadoraEntities.setBandaReversible(cursor.getString(18));
                    bandaTransportadoraEntities.setBandaDeArrastre(cursor.getString(19));
                    bandaTransportadoraEntities.setVelocidadBandaHorizontal(cursor.getString(20));
                    bandaTransportadoraEntities.setMarcaBandaHorizontalAnterior(cursor.getString(21));
                    bandaTransportadoraEntities.setAnchoBandaHorizontalAnterior(cursor.getString(22));
                    bandaTransportadoraEntities.setNoLonasBandaHorizontalAnterior(cursor.getString(23));
                    bandaTransportadoraEntities.setTipoLonaBandaHorizontalAnterior(cursor.getString(24));
                    bandaTransportadoraEntities.setEspesorTotalBandaHorizontalAnterior(cursor.getString(25));
                    bandaTransportadoraEntities.setEspesorCubiertaSuperiorBandaHorizontalAnterior(cursor.getString(26));
                    bandaTransportadoraEntities.setEspesorCubiertaInferiorBandaHorizontalAnterior(cursor.getString(27));
                    bandaTransportadoraEntities.setEspesorCojinBandaHorizontalAnterior(cursor.getString(28));
                    bandaTransportadoraEntities.setTipoEmpalmeBandaHorizontalAnterior(cursor.getString(29));
                    bandaTransportadoraEntities.setResistenciaRoturaLonaBandaHorizontalAnterior(cursor.getString(30));
                    bandaTransportadoraEntities.setTipoCubiertaBandaHorizontalAnterior(cursor.getString(31));
                    bandaTransportadoraEntities.setTonsTransportadasBandaHoizontalAnterior(cursor.getString(32));
                    bandaTransportadoraEntities.setCausaFallaCambioBandaHorizontal(cursor.getString(33));
                    bandaTransportadoraEntities.setDiametroPoleaColaTransportadora(cursor.getString(34));
                    bandaTransportadoraEntities.setAnchoPoleaColaTransportadora(cursor.getString(35));
                    bandaTransportadoraEntities.setTipoPoleaColaTransportadora(cursor.getString(36));
                    bandaTransportadoraEntities.setLargoEjePoleaColaTransportadora(cursor.getString(37));
                    bandaTransportadoraEntities.setDiametroEjePoleaColaHorizontal(cursor.getString(38));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaColaTransportadora(cursor.getString(39));
                    bandaTransportadoraEntities.setAnguloAmarrePoleaColaTransportadora(cursor.getString(40));
                    bandaTransportadoraEntities.setEstadoRvtoPoleaColaTransportadora(cursor.getString(41));
                    bandaTransportadoraEntities.setTipoTransicionPoleaColaTransportadora(cursor.getString(42));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTransportadora(cursor.getString(43));
                    bandaTransportadoraEntities.setLongitudTensorTornilloPoleaColaTransportadora(cursor.getString(44));
                    bandaTransportadoraEntities.setLongitudRecorridoContrapesaPoleaColaTransportadora(cursor.getString(45));
                    bandaTransportadoraEntities.setGuardaPoleaColaTransportadora(cursor.getString(46));
                    bandaTransportadoraEntities.setHayDesviador(cursor.getString(47));
                    bandaTransportadoraEntities.setElDesviadorBascula(cursor.getString(48));
                    bandaTransportadoraEntities.setPresionUniformeALoAnchoDeLaBanda(cursor.getString(49));
                    bandaTransportadoraEntities.setCauchoVPlow(cursor.getString(50));
                    bandaTransportadoraEntities.setAnchoVPlow(cursor.getString(51));
                    bandaTransportadoraEntities.setEspesorVPlow(cursor.getString(52));
                    bandaTransportadoraEntities.setTipoRevestimientoTolvaCarga(cursor.getString(53));
                    bandaTransportadoraEntities.setEstadoRevestimientoTolvaCarga(cursor.getString(54));
                    bandaTransportadoraEntities.setDuracionPromedioRevestimiento(cursor.getString(55));
                    bandaTransportadoraEntities.setDeflectores(cursor.getString(56));
                    bandaTransportadoraEntities.setAltureCaida(cursor.getString(57));
                    bandaTransportadoraEntities.setLongitudImpacto(cursor.getString(58));
                    bandaTransportadoraEntities.setMaterial(cursor.getString(59));
                    bandaTransportadoraEntities.setAnguloSobreCarga(cursor.getString(60));
                    bandaTransportadoraEntities.setAtaqueQuimicoTransportadora(cursor.getString(61));
                    bandaTransportadoraEntities.setAtaqueTemperaturaTransportadora(cursor.getString(62));
                    bandaTransportadoraEntities.setAtaqueAceiteTransportadora(cursor.getString(63));
                    bandaTransportadoraEntities.setAtaqueImpactoTransportadora(cursor.getString(64));
                    bandaTransportadoraEntities.setCapacidadTransportadora(cursor.getString(65));
                    bandaTransportadoraEntities.setHorasTrabajoPorDiaTransportadora(cursor.getString(66));
                    bandaTransportadoraEntities.setDiasTrabajPorSemanaTransportadora(cursor.getString(67));
                    bandaTransportadoraEntities.setAlimentacionCentradaTransportadora(cursor.getString(68));
                    bandaTransportadoraEntities.setAbrasividadTransportadora(cursor.getString(69));
                    bandaTransportadoraEntities.setPorcentajeFinosTransportadora(cursor.getString(70));
                    bandaTransportadoraEntities.setMaxGranulometriaTransportadora(cursor.getString(71));
                    bandaTransportadoraEntities.setMaxPesoTransportadora(cursor.getString(72));
                    bandaTransportadoraEntities.setDensidadTransportadora(cursor.getString(73));
                    bandaTransportadoraEntities.setTempMaximaMaterialSobreBandaTransportadora(cursor.getString(74));
                    bandaTransportadoraEntities.setTempPromedioMaterialSobreBandaTransportadora(cursor.getString(75));
                    bandaTransportadoraEntities.setFugaDeMaterialesEnLaColaDelChute(cursor.getString(76));
                    bandaTransportadoraEntities.setFugaDeMaterialesPorLosCostados(cursor.getString(77));
                    bandaTransportadoraEntities.setFugaMateriales(cursor.getString(78));
                    bandaTransportadoraEntities.setCajaColaDeTolva(cursor.getString(79));
                    bandaTransportadoraEntities.setFugaDeMaterialParticulaALaSalidaDelChute(cursor.getString(80));
                    bandaTransportadoraEntities.setAnchoChute(cursor.getString(81));
                    bandaTransportadoraEntities.setLargoChute(cursor.getString(82));
                    bandaTransportadoraEntities.setAlturaChute(cursor.getString(83));
                    bandaTransportadoraEntities.setAbrazadera(cursor.getString(84));
                    bandaTransportadoraEntities.setCauchoGuardabandas(cursor.getString(85));
                    bandaTransportadoraEntities.setTriSealMultiSeal(cursor.getString(86));
                    bandaTransportadoraEntities.setEspesorGuardaBandas(cursor.getString(87));
                    bandaTransportadoraEntities.setAnchoGuardaBandas(cursor.getString(88));
                    bandaTransportadoraEntities.setLargoGuardaBandas(cursor.getString(89));
                    bandaTransportadoraEntities.setProtectorGuardaBandas(cursor.getString(90));
                    bandaTransportadoraEntities.setCortinaAntiPolvo1(cursor.getString(91));
                    bandaTransportadoraEntities.setCortinaAntiPolvo2(cursor.getString(92));
                    bandaTransportadoraEntities.setCortinaAntiPolvo3(cursor.getString(93));
                    bandaTransportadoraEntities.setBoquillasCanonesDeAire(cursor.getString(94));
                    bandaTransportadoraEntities.setTempAmbienteMaxTransportadora(cursor.getString(95));
                    bandaTransportadoraEntities.setTempAmbienteMinTransportadora(cursor.getString(96));
                    bandaTransportadoraEntities.setTieneRodillosImpacto(cursor.getString(97));
                    bandaTransportadoraEntities.setCamaImpacto(cursor.getString(98));
                    bandaTransportadoraEntities.setCamaSellado(cursor.getString(99));
                    bandaTransportadoraEntities.setBasculaPesaje(cursor.getString(100));
                    bandaTransportadoraEntities.setRodilloCarga(cursor.getString(101));
                    bandaTransportadoraEntities.setRodilloImpacto(cursor.getString(102));
                    bandaTransportadoraEntities.setBasculaASGCO(cursor.getString(103));
                    bandaTransportadoraEntities.setBarraImpacto(cursor.getString(104));
                    bandaTransportadoraEntities.setBarraDeslizamiento(cursor.getString(105));
                    bandaTransportadoraEntities.setEspesorUHMV(cursor.getString(106));
                    bandaTransportadoraEntities.setAnchoBarra(cursor.getString(107));
                    bandaTransportadoraEntities.setLargoBarra(cursor.getString(108));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1(cursor.getString(109));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2(cursor.getString(110));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3(cursor.getString(111));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa1AntesPoleaMotriz(cursor.getString(112));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa2AntesPoleaMotriz(cursor.getString(113));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesa3AntesPoleaMotriz(cursor.getString(114));
                    bandaTransportadoraEntities.setIntegridadSoportesRodilloImpacto(cursor.getString(115));
                    bandaTransportadoraEntities.setMaterialAtrapadoEntreCortinas(cursor.getString(116));
                    bandaTransportadoraEntities.setMaterialAtrapadoEntreGuardabandas(cursor.getString(117));
                    bandaTransportadoraEntities.setMaterialAtrapadoEnBanda(cursor.getString(118));
                    bandaTransportadoraEntities.setIntegridadSoportesCamaImpacto(cursor.getString(119));
                    bandaTransportadoraEntities.setInclinacionZonaCargue(cursor.getString(120));
                    bandaTransportadoraEntities.setSistemaAlineacionCarga(cursor.getString(121));
                    bandaTransportadoraEntities.setCantidadSistemaAlineacionEnCarga(cursor.getString(122));
                    bandaTransportadoraEntities.setSistemasAlineacionCargaFuncionando(cursor.getString(123));
                    bandaTransportadoraEntities.setSistemaAlineacionEnRetorno(cursor.getString(124));
                    bandaTransportadoraEntities.setCantidadSistemaAlineacionEnRetorno(cursor.getString(125));
                    bandaTransportadoraEntities.setSistemasAlineacionRetornoFuncionando(cursor.getString(126));
                    bandaTransportadoraEntities.setSistemaAlineacionRetornoPlano(cursor.getString(127));
                    bandaTransportadoraEntities.setSistemaAlineacionArtesaCarga(cursor.getString(128));
                    bandaTransportadoraEntities.setSistemaAlineacionRetornoEnV(cursor.getString(129));
                    bandaTransportadoraEntities.setLargoEjeRodilloCentralCarga(cursor.getString(130));
                    bandaTransportadoraEntities.setDiametroEjeRodilloCentralCarga(cursor.getString(131));
                    bandaTransportadoraEntities.setLargoTuboRodilloCentralCarga(cursor.getString(132));
                    bandaTransportadoraEntities.setLargoEjeRodilloLateralCarga(cursor.getString(133));
                    bandaTransportadoraEntities.setDiametroEjeRodilloLateralCarga(cursor.getString(134));
                    bandaTransportadoraEntities.setDiametroRodilloLateralCarga(cursor.getString(135));
                    bandaTransportadoraEntities.setLargoTuboRodilloLateralCarga(cursor.getString(136));
                    bandaTransportadoraEntities.setTipoRodilloCarga(cursor.getString(137));
                    bandaTransportadoraEntities.setDistanciaEntreArtesasCarga(cursor.getString(138));
                    bandaTransportadoraEntities.setAnchoInternoChasisRodilloCarga(cursor.getString(139));
                    bandaTransportadoraEntities.setAnchoExternoChasisRodilloCarga(cursor.getString(140));
                    bandaTransportadoraEntities.setAnguloAcanalamientoArtesaCArga(cursor.getString(141));
                    bandaTransportadoraEntities.setDetalleRodilloCentralCarga(cursor.getString(142));
                    bandaTransportadoraEntities.setDetalleRodilloLateralCarg(cursor.getString(143));
                    bandaTransportadoraEntities.setDiametroPoleaMotrizTransportadora(cursor.getString(144));
                    bandaTransportadoraEntities.setAnchoPoleaMotrizTransportadora(cursor.getString(145));
                    bandaTransportadoraEntities.setTipoPoleaMotrizTransportadora(cursor.getString(146));
                    bandaTransportadoraEntities.setLargoEjePoleaMotrizTransportadora(cursor.getString(147));
                    bandaTransportadoraEntities.setDiametroEjeMotrizTransportadora(cursor.getString(148));
                    bandaTransportadoraEntities.setIcobandasCentraEnPoleaMotrizTransportadora(cursor.getString(149));
                    bandaTransportadoraEntities.setAnguloAmarrePoleaMotrizTransportadora(cursor.getString(150));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaMotrizTransportadora(cursor.getString(151));
                    bandaTransportadoraEntities.setTipoTransicionPoleaMotrizTransportadora(cursor.getString(152));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaMotrizTransportadora(cursor.getString(153));
                    bandaTransportadoraEntities.setPotenciaMotorTransportadora(cursor.getString(154));
                    bandaTransportadoraEntities.setGuardaPoleaMotrizTransportadora(cursor.getString(155));
                    bandaTransportadoraEntities.setAnchoEstructura(cursor.getString(156));
                    bandaTransportadoraEntities.setAnchoTrayectoCarga(cursor.getString(157));
                    bandaTransportadoraEntities.setPasarelaRespectoAvanceBanda(cursor.getString(158));
                    bandaTransportadoraEntities.setMaterialAlimenticioTransportadora(cursor.getString(159));
                    bandaTransportadoraEntities.setMaterialAcidoTransportadora(cursor.getString(160));
                    bandaTransportadoraEntities.setMaterialTempEntre80y150Transportadora(cursor.getString(161));
                    bandaTransportadoraEntities.setMaterialSecoTransportadora(cursor.getString(162));
                    bandaTransportadoraEntities.setMaterialHumedoTransportadora(cursor.getString(163));
                    bandaTransportadoraEntities.setMaterialAbrasivoFinoTransportadora(cursor.getString(164));
                    bandaTransportadoraEntities.setMaterialPegajosoTransportadora(cursor.getString(165));
                    bandaTransportadoraEntities.setMaterialGrasosoAceitosoTransportadora(cursor.getString(166));
                    bandaTransportadoraEntities.setMarcaLimpiadorPrimario(cursor.getString(167));
                    bandaTransportadoraEntities.setReferenciaLimpiadorPrimario(cursor.getString(168));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorPrimario(cursor.getString(169));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorPrimario(cursor.getString(170));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorPrimario(cursor.getString(171));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorPrimario(cursor.getString(172));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorPrimario(cursor.getString(173));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla(cursor.getString(174));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda(cursor.getString(175));
                    bandaTransportadoraEntities.setMarcaLimpiadorSecundario(cursor.getString(176));
                    bandaTransportadoraEntities.setReferenciaLimpiadorSecundario(cursor.getString(177));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorSecundario(cursor.getString(178));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorSecundario(cursor.getString(179));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorSecundario(cursor.getString(180));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorSecundario(cursor.getString(181));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorSecundario(cursor.getString(182));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla1(cursor.getString(183));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda1(cursor.getString(184));
                    bandaTransportadoraEntities.setSistemaDribbleChute(cursor.getString(185));
                    bandaTransportadoraEntities.setMarcaLimpiadorTerciario(cursor.getString(186));
                    bandaTransportadoraEntities.setReferenciaLimpiadorTerciario(cursor.getString(187));
                    bandaTransportadoraEntities.setAnchoCuchillaLimpiadorTerciario(cursor.getString(188));
                    bandaTransportadoraEntities.setAltoCuchillaLimpiadorTerciario(cursor.getString(189));
                    bandaTransportadoraEntities.setEstadoCuchillaLimpiadorTerciario(cursor.getString(190));
                    bandaTransportadoraEntities.setEstadoTensorLimpiadorTerciario(cursor.getString(191));
                    bandaTransportadoraEntities.setEstadoTuboLimpiadorTerciario(cursor.getString(192));
                    bandaTransportadoraEntities.setFrecuenciaRevisionCuchilla2(cursor.getString(193));
                    bandaTransportadoraEntities.setCuchillaEnContactoConBanda2(cursor.getString(194));
                    bandaTransportadoraEntities.setEstadoRodilloRetorno(cursor.getString(195));
                    bandaTransportadoraEntities.setLargoEjeRodilloRetorno(cursor.getString(196));
                    bandaTransportadoraEntities.setDiametroEjeRodilloRetorno(cursor.getString(197));
                    bandaTransportadoraEntities.setDiametroRodilloRetorno(cursor.getString(198));
                    bandaTransportadoraEntities.setLargoTuboRodilloRetorno(cursor.getString(199));
                    bandaTransportadoraEntities.setTipoRodilloRetorno(cursor.getString(200));
                    bandaTransportadoraEntities.setDistanciaEntreRodillosRetorno(cursor.getString(201));
                    bandaTransportadoraEntities.setAnchoInternoChasisRetorno(cursor.getString(202));
                    bandaTransportadoraEntities.setAnchoExternoChasisRetorno(cursor.getString(203));
                    bandaTransportadoraEntities.setDetalleRodilloRetorno(cursor.getString(204));
                    bandaTransportadoraEntities.setDiametroPoleaAmarrePoleaMotriz(cursor.getString(205));
                    bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaMotriz(cursor.getString(206));
                    bandaTransportadoraEntities.setTipoPoleaAmarrePoleaMotriz(cursor.getString(207));
                    bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaMotriz(cursor.getString(208));
                    bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaMotriz(cursor.getString(209));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaMotriz(cursor.getString(210));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaMotriz(cursor.getString(211));
                    bandaTransportadoraEntities.setDimetroPoleaAmarrePoleaCola(cursor.getString(212));
                    bandaTransportadoraEntities.setAnchoPoleaAmarrePoleaCola(cursor.getString(213));
                    bandaTransportadoraEntities.setLargoEjePoleaAmarrePoleaCola(cursor.getString(214));
                    bandaTransportadoraEntities.setTipoPoleaAmarrePoleaCola(cursor.getString(215));
                    bandaTransportadoraEntities.setDiametroEjePoleaAmarrePoleaCola(cursor.getString(216));
                    bandaTransportadoraEntities.setIcobandasCentradaPoleaAmarrePoleaCola(cursor.getString(217));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaAmarrePoleaCola(cursor.getString(218));
                    bandaTransportadoraEntities.setDiametroPoleaTensora(cursor.getString(219));
                    bandaTransportadoraEntities.setAnchoPoleaTensora(cursor.getString(220));
                    bandaTransportadoraEntities.setTipoPoleaTensora(cursor.getString(221));
                    bandaTransportadoraEntities.setLargoEjePoleaTensora(cursor.getString(222));
                    bandaTransportadoraEntities.setDiametroEjePoleaTensora(cursor.getString(223));
                    bandaTransportadoraEntities.setIcobandasCentradaEnPoleaTensora(cursor.getString(224));
                    bandaTransportadoraEntities.setRecorridoPoleaTensora(cursor.getString(225));
                    bandaTransportadoraEntities.setEstadoRevestimientoPoleaTensora(cursor.getString(226));
                    bandaTransportadoraEntities.setTipoTransicionPoleaTensora(cursor.getString(227));
                    bandaTransportadoraEntities.setDistanciaTransicionPoleaColaTensora(cursor.getString(228));
                    bandaTransportadoraEntities.setPotenciaMotorPoleaTensora(cursor.getString(229));
                    bandaTransportadoraEntities.setGuardaPoleaTensora(cursor.getString(230));
                    bandaTransportadoraEntities.setPuertasInspeccion(cursor.getString(231));
                    bandaTransportadoraEntities.setGuardaRodilloRetornoPlano(cursor.getString(232));
                    bandaTransportadoraEntities.setGuardaTruTrainer(cursor.getString(233));
                    bandaTransportadoraEntities.setGuardaPoleaDeflectora(cursor.getString(234));
                    bandaTransportadoraEntities.setGuardaZonaDeTransito(cursor.getString(235));
                    bandaTransportadoraEntities.setGuardaMotores(cursor.getString(236));
                    bandaTransportadoraEntities.setGuardaCadenas(cursor.getString(237));
                    bandaTransportadoraEntities.setGuardaCorreas(cursor.getString(238));
                    bandaTransportadoraEntities.setInterruptoresDeSeguridad(cursor.getString(239));
                    bandaTransportadoraEntities.setSirenasDeSeguridad(cursor.getString(240));
                    bandaTransportadoraEntities.setGuardaRodilloRetornoV(cursor.getString(241));
                    bandaTransportadoraEntities.setDiametroRodilloCentralCarga(cursor.getString(242));
                    bandaTransportadoraEntities.setTipoRodilloImpacto(cursor.getString(243));
                    bandaTransportadoraEntities.setIntegridadSoporteCamaSellado(cursor.getString(244));
                    bandaTransportadoraEntities.setAtaqueAbrasivoTransportadora(cursor.getString(245));
                    bandaTransportadoraEntities.setObservacionRegistroTransportadora(cursor.getString(247));
                    bandaTransportadoraEntitiesArraylist.add(bandaTransportadoraEntities);
                }

                conteo=0;

                for (int i = 0; i < bandaTransportadoraEntitiesArraylist.size(); i++) {


                    String url = Constants.url + "actualizarHorizontal";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransportadora set  estadoRegistroTransportadora='Sincronizado' where idRegistro='" + bandaTransportadoraEntitiesArraylist.get(finalI).getIdRegistro() + "'");
                            conteo++;
                            if (conteo == bandaTransportadoraEntitiesArraylist.size()) {
                                recargarBD();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idRegistro", bandaTransportadoraEntitiesArraylist.get(finalI).getIdRegistro());
                            params.put("marcaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaBandaTransportadora());
                            params.put("anchoBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBandaTransportadora());
                            params.put("noLonasBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getNoLonasBandaTransportadora());
                            params.put("tipoLonaBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoLonaBandaTransportadora());
                            params.put("espesorTotalBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorTotalBandaTransportadora());
                            params.put("espesorCubiertaSuperiorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaSuperiorTransportadora());
                            params.put("espesorCojinTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCojinTransportadora());
                            params.put("espesorCubiertaInferiorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaInferiorTransportadora());
                            params.put("tipoCubiertaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoCubiertaTransportadora());
                            params.put("tipoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoEmpalmeTransportadora());
                            params.put("estadoEmpalmeTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoEmpalmeTransportadora());
                            params.put("distanciaEntrePoleasBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntrePoleasBandaHorizontal());
                            params.put("inclinacionBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getInclinacionBandaHorizontal());
                            params.put("recorridoUtilTensorBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getRecorridoUtilTensorBandaHorizontal());
                            params.put("longitudSinfinBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudSinfinBandaHorizontal());
                            params.put("resistenciaRoturaLonaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getResistenciaRoturaLonaTransportadora());
                            params.put("localizacionTensorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLocalizacionTensorTransportadora());
                            params.put("bandaReversible", bandaTransportadoraEntitiesArraylist.get(finalI).getBandaReversible());
                            params.put("bandaDeArrastre", bandaTransportadoraEntitiesArraylist.get(finalI).getBandaDeArrastre());
                            params.put("velocidadBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getVelocidadBandaHorizontal());
                            params.put("marcaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaBandaHorizontalAnterior());
                            params.put("anchoBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBandaHorizontalAnterior());
                            params.put("noLonasBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getNoLonasBandaHorizontalAnterior());
                            params.put("tipoLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoLonaBandaHorizontalAnterior());
                            params.put("espesorTotalBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorTotalBandaHorizontalAnterior());
                            params.put("espesorCubiertaSuperiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaSuperiorBandaHorizontalAnterior());
                            params.put("espesorCubiertaInferiorBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCubiertaInferiorBandaHorizontalAnterior());
                            params.put("espesorCojinBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorCojinBandaHorizontalAnterior());
                            params.put("tipoEmpalmeBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoEmpalmeBandaHorizontalAnterior());
                            params.put("resistenciaRoturaLonaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getResistenciaRoturaLonaBandaHorizontalAnterior());
                            params.put("tipoCubiertaBandaHorizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoCubiertaBandaHorizontalAnterior());
                            params.put("tonsTransportadasBandaHoizontalAnterior", bandaTransportadoraEntitiesArraylist.get(finalI).getTonsTransportadasBandaHoizontalAnterior());
                            params.put("causaFallaCambioBandaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getCausaFallaCambioBandaHorizontal());
                            params.put("diametroPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaColaTransportadora());
                            params.put("anchoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaColaTransportadora());
                            params.put("tipoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaColaTransportadora());
                            params.put("largoEjePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaColaTransportadora());
                            params.put("diametroEjePoleaColaHorizontal", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaColaHorizontal());
                            params.put("icobandasCentradaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaColaTransportadora());
                            params.put("anguloAmarrePoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAmarrePoleaColaTransportadora());
                            params.put("estadoRvtoPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRvtoPoleaColaTransportadora());
                            params.put("tipoTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaColaTransportadora());
                            params.put("distanciaTransicionPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaColaTransportadora());
                            params.put("longitudTensorTornilloPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudTensorTornilloPoleaColaTransportadora());
                            params.put("longitudRecorridoContrapesaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudRecorridoContrapesaPoleaColaTransportadora());
                            params.put("guardaPoleaColaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaColaTransportadora());
                            params.put("hayDesviador", bandaTransportadoraEntitiesArraylist.get(finalI).getHayDesviador());
                            params.put("elDesviadorBascula", bandaTransportadoraEntitiesArraylist.get(finalI).getElDesviadorBascula());
                            params.put("presionUniformeALoAnchoDeLaBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getPresionUniformeALoAnchoDeLaBanda());
                            params.put("cauchoVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getCauchoVPlow());
                            params.put("anchoVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoVPlow());
                            params.put("espesorVPlow", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorVPlow());
                            params.put("tipoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRevestimientoTolvaCarga());
                            params.put("estadoRevestimientoTolvaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoTolvaCarga());
                            params.put("duracionPromedioRevestimiento", bandaTransportadoraEntitiesArraylist.get(finalI).getDuracionPromedioRevestimiento());
                            params.put("deflectores", bandaTransportadoraEntitiesArraylist.get(finalI).getDeflectores());
                            params.put("altureCaida", bandaTransportadoraEntitiesArraylist.get(finalI).getAltureCaida());
                            params.put("longitudImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getLongitudImpacto());
                            params.put("material", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterial());
                            params.put("anguloSobreCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloSobreCarga());
                            params.put("ataqueQuimicoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueQuimicoTransportadora());
                            params.put("ataqueTemperaturaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueTemperaturaTransportadora());
                            params.put("ataqueAceiteTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueAceiteTransportadora());
                            params.put("ataqueImpactoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueImpactoTransportadora());
                            params.put("capacidadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getCapacidadTransportadora());
                            params.put("horasTrabajoPorDiaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getHorasTrabajoPorDiaTransportadora());
                            params.put("diasTrabajPorSemanaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiasTrabajPorSemanaTransportadora());
                            params.put("alimentacionCentradaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAlimentacionCentradaTransportadora());
                            params.put("abrasividadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAbrasividadTransportadora());
                            params.put("porcentajeFinosTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getPorcentajeFinosTransportadora());
                            params.put("maxGranulometriaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaxGranulometriaTransportadora());
                            params.put("maxPesoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaxPesoTransportadora());
                            params.put("densidadTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDensidadTransportadora());
                            params.put("tempMaximaMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempMaximaMaterialSobreBandaTransportadora());
                            params.put("tempPromedioMaterialSobreBandaTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempPromedioMaterialSobreBandaTransportadora());
                            params.put("fugaDeMaterialesEnLaColaDelChute", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialesEnLaColaDelChute());
                            params.put("fugaDeMaterialesPorLosCostados", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialesPorLosCostados());
                            params.put("fugaMateriales", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaMateriales());
                            params.put("cajaColaDeTolva", bandaTransportadoraEntitiesArraylist.get(finalI).getCajaColaDeTolva());
                            params.put("fugaDeMaterialParticulaALaSalidaDelChute", bandaTransportadoraEntitiesArraylist.get(finalI).getFugaDeMaterialParticulaALaSalidaDelChute());
                            params.put("anchoChute", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoChute());
                            params.put("largoChute", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoChute());
                            params.put("alturaChute", bandaTransportadoraEntitiesArraylist.get(finalI).getAlturaChute());
                            params.put("abrazadera", bandaTransportadoraEntitiesArraylist.get(finalI).getAbrazadera());
                            params.put("cauchoGuardabandas", bandaTransportadoraEntitiesArraylist.get(finalI).getCauchoGuardabandas());
                            params.put("triSealMultiSeal", bandaTransportadoraEntitiesArraylist.get(finalI).getTriSealMultiSeal());
                            params.put("espesorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorGuardaBandas());
                            params.put("anchoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoGuardaBandas());
                            params.put("largoGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoGuardaBandas());
                            params.put("protectorGuardaBandas", bandaTransportadoraEntitiesArraylist.get(finalI).getProtectorGuardaBandas());
                            params.put("cortinaAntiPolvo1", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo1());
                            params.put("cortinaAntiPolvo2", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo2());
                            params.put("cortinaAntiPolvo3", bandaTransportadoraEntitiesArraylist.get(finalI).getCortinaAntiPolvo3());
                            params.put("boquillasCanonesDeAire", bandaTransportadoraEntitiesArraylist.get(finalI).getBoquillasCanonesDeAire());
                            params.put("tempAmbienteMaxTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempAmbienteMaxTransportadora());
                            params.put("tempAmbienteMinTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTempAmbienteMinTransportadora());
                            params.put("tieneRodillosImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getTieneRodillosImpacto());
                            params.put("camaImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getCamaImpacto());
                            params.put("camaSellado", bandaTransportadoraEntitiesArraylist.get(finalI).getCamaSellado());
                            params.put("basculaPesaje", bandaTransportadoraEntitiesArraylist.get(finalI).getBasculaPesaje());
                            params.put("rodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getRodilloCarga());
                            params.put("rodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getRodilloImpacto());
                            params.put("basculaASGCO", bandaTransportadoraEntitiesArraylist.get(finalI).getBasculaASGCO());
                            params.put("barraImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getBarraImpacto());
                            params.put("barraDeslizamiento", bandaTransportadoraEntitiesArraylist.get(finalI).getBarraDeslizamiento());
                            params.put("espesorUHMV", bandaTransportadoraEntitiesArraylist.get(finalI).getEspesorUHMV());
                            params.put("anchoBarra", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoBarra());
                            params.put("largoBarra", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoBarra());
                            params.put("anguloAcanalamientoArtesa1", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa1());
                            params.put("anguloAcanalamientoArtesa2", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa2());
                            params.put("anguloAcanalamientoArtesa3", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa3());
                            params.put("anguloAcanalamientoArtesa1AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa1AntesPoleaMotriz());
                            params.put("anguloAcanalamientoArtesa2AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa2AntesPoleaMotriz());
                            params.put("anguloAcanalamientoArtesa3AntesPoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesa3AntesPoleaMotriz());
                            params.put("integridadSoportesRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoportesRodilloImpacto());
                            params.put("materialAtrapadoEntreCortinas", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEntreCortinas());
                            params.put("materialAtrapadoEntreGuardabandas", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEntreGuardabandas());
                            params.put("materialAtrapadoEnBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAtrapadoEnBanda());
                            params.put("integridadSoportesCamaImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoportesCamaImpacto());
                            params.put("inclinacionZonaCargue", bandaTransportadoraEntitiesArraylist.get(finalI).getInclinacionZonaCargue());
                            params.put("sistemaAlineacionCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionCarga());
                            params.put("cantidadSistemaAlineacionEnCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getCantidadSistemaAlineacionEnCarga());
                            params.put("sistemasAlineacionCargaFuncionando", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemasAlineacionCargaFuncionando());
                            params.put("sistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionEnRetorno());
                            params.put("cantidadSistemaAlineacionEnRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getCantidadSistemaAlineacionEnRetorno());
                            params.put("sistemasAlineacionRetornoFuncionando", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemasAlineacionRetornoFuncionando());
                            params.put("sistemaAlineacionRetornoPlano", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionRetornoPlano());
                            params.put("sistemaAlineacionArtesaCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionArtesaCarga());
                            params.put("sistemaAlineacionRetornoEnV", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaAlineacionRetornoEnV());
                            params.put("largoEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloCentralCarga());
                            params.put("diametroEjeRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloCentralCarga());
                            params.put("largoTuboRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloCentralCarga());
                            params.put("largoEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloLateralCarga());
                            params.put("diametroEjeRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloLateralCarga());
                            params.put("diametroRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloLateralCarga());
                            params.put("largoTuboRodilloLateralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloLateralCarga());
                            params.put("tipoRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloCarga());
                            params.put("distanciaEntreArtesasCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntreArtesasCarga());
                            params.put("anchoInternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoInternoChasisRodilloCarga());
                            params.put("anchoExternoChasisRodilloCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoExternoChasisRodilloCarga());
                            params.put("anguloAcanalamientoArtesaCArga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAcanalamientoArtesaCArga());
                            params.put("detalleRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloCentralCarga());
                            params.put("detalleRodilloLateralCarg", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloLateralCarg());
                            params.put("diametroPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaMotrizTransportadora());
                            params.put("anchoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaMotrizTransportadora());
                            params.put("tipoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaMotrizTransportadora());
                            params.put("largoEjePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaMotrizTransportadora());
                            params.put("diametroEjeMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeMotrizTransportadora());
                            params.put("icobandasCentraEnPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentraEnPoleaMotrizTransportadora());
                            params.put("anguloAmarrePoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnguloAmarrePoleaMotrizTransportadora());
                            params.put("estadoRevestimientoPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaMotrizTransportadora());
                            params.put("tipoTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaMotrizTransportadora());
                            params.put("distanciaTransicionPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaMotrizTransportadora());
                            params.put("potenciaMotorTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getPotenciaMotorTransportadora());
                            params.put("guardaPoleaMotrizTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaMotrizTransportadora());
                            params.put("anchoEstructura", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoEstructura());
                            params.put("anchoTrayectoCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoTrayectoCarga());
                            params.put("pasarelaRespectoAvanceBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getPasarelaRespectoAvanceBanda());
                            params.put("materialAlimenticioTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAlimenticioTransportadora());
                            params.put("materialAcidoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAcidoTransportadora());
                            params.put("materialTempEntre8finalIy15finalITransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialTempEntre80y150Transportadora());
                            params.put("materialSecoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialSecoTransportadora());
                            params.put("materialHumedoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialHumedoTransportadora());
                            params.put("materialAbrasivoFinoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialAbrasivoFinoTransportadora());
                            params.put("materialPegajosoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialPegajosoTransportadora());
                            params.put("materialGrasosoAceitosoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getMaterialGrasosoAceitosoTransportadora());
                            params.put("marcaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorPrimario());
                            params.put("referenciaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorPrimario());
                            params.put("anchoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorPrimario());
                            params.put("altoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorPrimario());
                            params.put("estadoCuchillaLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorPrimario());
                            params.put("estadoTensorLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorPrimario());
                            params.put("estadoTuboLimpiadorPrimario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorPrimario());
                            params.put("frecuenciaRevisionCuchilla", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla());
                            params.put("cuchillaEnContactoConBanda", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda());
                            params.put("marcaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorSecundario());
                            params.put("referenciaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorSecundario());
                            params.put("anchoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorSecundario());
                            params.put("altoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorSecundario());
                            params.put("estadoCuchillaLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorSecundario());
                            params.put("estadoTensorLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorSecundario());
                            params.put("estadoTuboLimpiadorSecundario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorSecundario());
                            params.put("frecuenciaRevisionCuchilla1", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla1());
                            params.put("cuchillaEnContactoConBanda1", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda1());
                            params.put("sistemaDribbleChute", bandaTransportadoraEntitiesArraylist.get(finalI).getSistemaDribbleChute());
                            params.put("marcaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getMarcaLimpiadorTerciario());
                            params.put("referenciaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getReferenciaLimpiadorTerciario());
                            params.put("anchoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoCuchillaLimpiadorTerciario());
                            params.put("altoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getAltoCuchillaLimpiadorTerciario());
                            params.put("estadoCuchillaLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoCuchillaLimpiadorTerciario());
                            params.put("estadoTensorLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTensorLimpiadorTerciario());
                            params.put("estadoTuboLimpiadorTerciario", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoTuboLimpiadorTerciario());
                            params.put("frecuenciaRevisionCuchilla2", bandaTransportadoraEntitiesArraylist.get(finalI).getFrecuenciaRevisionCuchilla2());
                            params.put("cuchillaEnContactoConBanda2", bandaTransportadoraEntitiesArraylist.get(finalI).getCuchillaEnContactoConBanda2());
                            params.put("estadoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRodilloRetorno());
                            params.put("largoEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjeRodilloRetorno());
                            params.put("diametroEjeRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjeRodilloRetorno());
                            params.put("diametroRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloRetorno());
                            params.put("largoTuboRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoTuboRodilloRetorno());
                            params.put("tipoRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloRetorno());
                            params.put("distanciaEntreRodillosRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaEntreRodillosRetorno());
                            params.put("anchoInternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoInternoChasisRetorno());
                            params.put("anchoExternoChasisRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoExternoChasisRetorno());
                            params.put("detalleRodilloRetorno", bandaTransportadoraEntitiesArraylist.get(finalI).getDetalleRodilloRetorno());
                            params.put("diametroPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaAmarrePoleaMotriz());
                            params.put("anchoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaAmarrePoleaMotriz());
                            params.put("tipoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaAmarrePoleaMotriz());
                            params.put("largoEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaAmarrePoleaMotriz());
                            params.put("diametroEjePoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaAmarrePoleaMotriz());
                            params.put("icobandasCentradaPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaAmarrePoleaMotriz());
                            params.put("estadoRevestimientoPoleaAmarrePoleaMotriz", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaAmarrePoleaMotriz());
                            params.put("dimetroPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getDimetroPoleaAmarrePoleaCola());
                            params.put("anchoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaAmarrePoleaCola());
                            params.put("largoEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaAmarrePoleaCola());
                            params.put("tipoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaAmarrePoleaCola());
                            params.put("diametroEjePoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaAmarrePoleaCola());
                            params.put("icobandasCentradaPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaPoleaAmarrePoleaCola());
                            params.put("estadoRevestimientoPoleaAmarrePoleaCola", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaAmarrePoleaCola());
                            params.put("diametroPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroPoleaTensora());
                            params.put("anchoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getAnchoPoleaTensora());
                            params.put("tipoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoPoleaTensora());
                            params.put("largoEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getLargoEjePoleaTensora());
                            params.put("diametroEjePoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroEjePoleaTensora());
                            params.put("icobandasCentradaEnPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getIcobandasCentradaEnPoleaTensora());
                            params.put("recorridoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getRecorridoPoleaTensora());
                            params.put("estadoRevestimientoPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getEstadoRevestimientoPoleaTensora());
                            params.put("tipoTransicionPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoTransicionPoleaTensora());
                            params.put("distanciaTransicionPoleaColaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getDistanciaTransicionPoleaColaTensora());
                            params.put("potenciaMotorPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getPotenciaMotorPoleaTensora());
                            params.put("guardaPoleaTensora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaTensora());
                            params.put("puertasInspeccion", bandaTransportadoraEntitiesArraylist.get(finalI).getPuertasInspeccion());
                            params.put("guardaRodilloRetornoPlano", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaRodilloRetornoPlano());
                            params.put("guardaTruTrainer", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaTruTrainer());
                            params.put("guardaPoleaDeflectora", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaPoleaDeflectora());
                            params.put("guardaZonaDeTransito", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaZonaDeTransito());
                            params.put("guardaMotores", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaMotores());
                            params.put("guardaCadenas", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaCadenas());
                            params.put("guardaCorreas", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaCorreas());
                            params.put("interruptoresDeSeguridad", bandaTransportadoraEntitiesArraylist.get(finalI).getInterruptoresDeSeguridad());
                            params.put("sirenasDeSeguridad", bandaTransportadoraEntitiesArraylist.get(finalI).getSirenasDeSeguridad());
                            params.put("guardaRodilloRetornoV", bandaTransportadoraEntitiesArraylist.get(finalI).getGuardaRodilloRetornoV());
                            params.put("diametroRodilloCentralCarga", bandaTransportadoraEntitiesArraylist.get(finalI).getDiametroRodilloCentralCarga());
                            params.put("tipoRodilloImpacto", bandaTransportadoraEntitiesArraylist.get(finalI).getTipoRodilloImpacto());
                            params.put("integridadSoporteCamaSellado", bandaTransportadoraEntitiesArraylist.get(finalI).getIntegridadSoporteCamaSellado());
                            params.put("ataqueAbrasivoTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getAtaqueAbrasivoTransportadora());
                            params.put("observacionRegistroTransportadora", bandaTransportadoraEntitiesArraylist.get(finalI).getObservacionRegistroTransportadora());


                            for(Map.Entry entry:params.entrySet()){
                                if(entry.getValue()==null)
                                {
                                    String campo=entry.getKey().toString();

                                    params.put(campo, "");
                                }
                            }

                            return params;
                        }

                    };
                    requestClientes.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(requestClientes);


                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void recargarBD()
    {
        SQLiteDatabase db= dbHelper.getReadableDatabase();
        Cursor cursorUsuario=db.rawQuery("SELECT * from usuario", null);
        cursorUsuario.moveToFirst();
        final String usuario=cursorUsuario.getString(0);
        final String contra=cursorUsuario.getString(2);
        final String[] nombreUsuario = new String[1];
        String rol;
        if(usuario.toString().equals("lepe") && contra.toString().equals("cac2cdfa95fb1ee32713f5870318c8b5") ||  usuario.toString().equals("leca") && contra.toString().equals("483b15a64101886f1e39808fcffd7562."))
        {
            rol="admin";
            String url = Constants.url + "loginAdmin";
            final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Type type = new TypeToken<List<LoginJson>>() {
                        }.getType();
                        loginJsons = gson.fromJson(response, type);
                        String nombreAdmin;

                        String estadoAdmin=null;
                        for (int i=0;i< loginJsons.size();i++)
                        {
                            if(loginJsons.get(i).getCodagente().equals(usuario.toString().toUpperCase()) || loginJsons.get(i).getCodagente().equals(contra.toString()))
                            {
                                estadoAdmin=loginJsons.get(i).getEstadoagte();
                                nombreUsuario[0] =loginJsons.get(i).getCodagente();

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

                            dialogCarga.cancel();
                            alerta.setTitle("ICOBANDAS S.A dice:");
                            alerta.setMessage("Usted no está autorizado para ingresar al sistema");
                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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

                            if(nombreUsuario[0].equals("LEPE"))
                            {
                                db.execSQL("insert into usuario values('LEPE','LEÓN PELÁEZ','cac2cdfa95fb1ee32713f5870318c8b5','admin')");

                            }
                            else
                            {
                                db.execSQL("insert into usuario values('LECA','LENIN RAUL CASTRO','483b15a64101886f1e39808fcffd7562','admin')");

                            }
                            Cursor cursor1=db.rawQuery("select * from usuario",null);
                            cursor1.moveToFirst();
                            nombreAdmin=cursor1.getString(1);

                            for (int i = 0; i < loginJsons.size(); i++) {
                                db.execSQL("INSERT INTO usuario values ('"+loginJsons.get(i).getCodagente()+"','"+loginJsons.get(i).getNombreagte()+"','"+loginJsons.get(i).getPwdagente()+"','agente')");
                                if (loginJsons.get(i).getNit() != null) {
                                    db.execSQL("INSERT INTO clientes values('" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameunido() + "','Sincronizado')");
                                }
                                if(loginJsons.get(i).getCodplanta()!=null)
                                {
                                    db.execSQL("INSERT INTO plantas values(" + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameplanta() + "',null,null,null)");

                                }

                                if (loginJsons.get(i).getIdRegistro() != null) {
                                    db.execSQL("INSERT INTO registro values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getFechaRegistro() + "'," + loginJsons.get(i).getIdTransportador() + "," + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','Sincronizado', '"+loginJsons.get(i).getEstadoRegistro()+"')");
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
                                                db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "','Sincronizado','"+loginJsons.get(i).getObservacionRegistro()+"')");
                                            }

                                            else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E"))
                                            {
                                                db.execSQL("INSERT INTO bandaElevadora values('"+loginJsons.get(i).getIdRegistro()+"', '"+loginJsons.get(i).getMarcaBandaElevadora()+"', '"+loginJsons.get(i).getAnchoBandaElevadora()+"', '"+loginJsons.get(i).getDistanciaEntrePoleasElevadora()+"', '"+loginJsons.get(i).getNoLonaBandaElevadora()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadora()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadora()+"', '"+loginJsons.get(i).getEspesorCojinActualElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorElevadora()+"', '"+loginJsons.get(i).getTipoCubiertaElevadora()+"', '"+loginJsons.get(i).getTipoEmpalmeElevadora()+"', '"+loginJsons.get(i).getEstadoEmpalmeElevadora()+"', '"+loginJsons.get(i).getResistenciaRoturaLonaElevadora()+"', '"+loginJsons.get(i).getVelocidadBandaElevadora()+"', '"+loginJsons.get(i).getMarcaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getAnchoBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getNoLonasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCojinElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getVelocidadBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getPesoMaterialEnCadaCangilon()+"', '"+loginJsons.get(i).getPesoCangilonVacio()+"', '"+loginJsons.get(i).getLongitudCangilon()+"', '"+loginJsons.get(i).getMaterialCangilon()+"', '"+loginJsons.get(i).getTipoCangilon()+"', '"+loginJsons.get(i).getProyeccionCangilon()+"','"+loginJsons.get(i).getProfundidadCangilon()+"' ,'"+loginJsons.get(i).getMarcaCangilon()+"', '"+loginJsons.get(i).getReferenciaCangilon()+"', '"+loginJsons.get(i).getCapacidadCangilon()+"', '"+loginJsons.get(i).getNoFilasCangilones()+"', '"+loginJsons.get(i).getSeparacionCangilones()+"', '"+loginJsons.get(i).getNoAgujeros()+"', '"+loginJsons.get(i).getDistanciaBordeBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaPosteriorBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura()+"', '"+loginJsons.get(i).getDistanciaBordesCangilonEstructura()+"', '"+loginJsons.get(i).getTipoVentilacion()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getLargoEjeMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getPotenciaMotorMotrizElevadora()+"', '"+loginJsons.get(i).getRpmSalidaReductorMotrizElevadora()+"', '"+loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroPoleaColaElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaColaElevadora()+"', '"+loginJsons.get(i).getTipoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLargoEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getDiametroEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora()+"', '"+loginJsons.get(i).getCargaTrabajoBandaElevadora()+"', '"+loginJsons.get(i).getTemperaturaMaterialElevadora()+"', '"+loginJsons.get(i).getEmpalmeMecanicoElevadora()+"', '"+loginJsons.get(i).getDiametroRoscaElevadora()+"', '"+loginJsons.get(i).getLargoTornilloElevadora()+"', '"+loginJsons.get(i).getMaterialTornilloElevadora()+"', '"+loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getMonitorPeligro()+"', '"+loginJsons.get(i).getRodamiento()+"', '"+loginJsons.get(i).getMonitorDesalineacion()+"', '"+loginJsons.get(i).getMonitorVelocidad()+"', '"+loginJsons.get(i).getSensorInductivo()+"', '"+loginJsons.get(i).getIndicadorNivel()+"', '"+loginJsons.get(i).getCajaUnion()+"', '"+loginJsons.get(i).getAlarmaYPantalla()+"', '"+loginJsons.get(i).getInterruptorSeguridad()+"', '"+loginJsons.get(i).getMaterialElevadora()+"', '"+loginJsons.get(i).getAtaqueQuimicoElevadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaElevadora()+"', '"+loginJsons.get(i).getAtaqueAceitesElevadora()+"', '"+loginJsons.get(i).getAtaqueAbrasivoElevadora()+"', '"+loginJsons.get(i).getCapacidadElevadora()+"', '"+loginJsons.get(i).getHorasTrabajoDiaElevadora()+"', '"+loginJsons.get(i).getDiasTrabajoSemanaElevadora()+"', '"+loginJsons.get(i).getAbrasividadElevadora()+"', '"+loginJsons.get(i).getPorcentajeFinosElevadora()+"', '"+loginJsons.get(i).getMaxGranulometriaElevadora()+"', '"+loginJsons.get(i).getDensidadMaterialElevadora()+"', '"+loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getVariosPuntosDeAlimentacion()+"', '"+loginJsons.get(i).getLluviaDeMaterial()+"', '"+loginJsons.get(i).getAnchoPiernaElevador()+"', '"+loginJsons.get(i).getProfundidadPiernaElevador()+"', '"+loginJsons.get(i).getTempAmbienteMin()+"', '"+loginJsons.get(i).getTempAmbienteMax()+"', '"+loginJsons.get(i).getTipoDescarga()+"', '"+loginJsons.get(i).getTipoCarga()+"','Sincronizado','"+loginJsons.get(i).getObservacionRegistroElevadora()+"')");

                                            }

                                            else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T"))
                                            {
                                                db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','"+loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda()+"' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "',"+
                                                        "'"+loginJsons.get(i).getMaterial()+"', '"+loginJsons.get(i).getAnguloSobreCarga()+"', '"+loginJsons.get(i).getAtaqueQuimicoTransportadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaTransportadora()+"', '"+loginJsons.get(i).getAtaqueAceiteTransportadora()+"', '"+loginJsons.get(i).getAtaqueImpactoTransportadora()+"', '"+loginJsons.get(i).getCapacidadTransportadora()+"', '"+loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()+"', '"+loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()+"', '"+loginJsons.get(i).getAlimentacionCentradaTransportadora()+"', '"+loginJsons.get(i).getAbrasividadTransportadora()+"', '"+loginJsons.get(i).getPorcentajeFinosTransportadora()+"', '"+loginJsons.get(i).getMaxGranulometriaTransportadora()+"', '"+loginJsons.get(i).getMaxPesoTransportadora()+"', '"+loginJsons.get(i).getDensidadTransportadora()+"', '"+loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getCajaColaDeTolva()+"', '"+loginJsons.get(i).getFugaMateriales()+"', '"+loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()+"', '"+loginJsons.get(i).getFugaDeMaterialesPorLosCostados()+"', '"+loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()+"', '"+loginJsons.get(i).getAnchoChute()+"', '"+loginJsons.get(i).getLargoChute()+"', '"+loginJsons.get(i).getAlturaChute()+"', '"+loginJsons.get(i).getCauchoGuardabandas()+"', '"+loginJsons.get(i).getTriSealMultiSeal()+"', '"+loginJsons.get(i).getEspesorGuardaBandas()+"', '"+loginJsons.get(i).getAnchoGuardaBandas()+"', '"+loginJsons.get(i).getLargoGuardaBandas()+"', '"+loginJsons.get(i).getProtectorGuardaBandas()+"', '"+loginJsons.get(i).getCortinaAntiPolvo1()+"', '"+loginJsons.get(i).getCortinaAntiPolvo2()+"', '"+loginJsons.get(i).getCortinaAntiPolvo3()+"', '"+loginJsons.get(i).getBoquillasCanonesDeAire()+"', '"+loginJsons.get(i).getTempAmbienteMaxTransportadora()+"', '"+loginJsons.get(i).getTempAmbienteMinTransportadora()+"', '"+loginJsons.get(i).getTieneRodillosImpacto()+"', '"+loginJsons.get(i).getCamaImpacto()+"', '"+loginJsons.get(i).getCamaSellado()+"', '"+loginJsons.get(i).getBasculaPesaje()+"', '"+loginJsons.get(i).getRodilloCarga()+"', '"+loginJsons.get(i).getRodilloImpacto()+"', '"+loginJsons.get(i).getBasculaASGCO()+"', '"+loginJsons.get(i).getBarraImpacto()+"', '"+loginJsons.get(i).getBarraDeslizamiento()+"', '"+loginJsons.get(i).getEspesorUHMV()+"', '"+loginJsons.get(i).getAnchoBarra()+"', '"+loginJsons.get(i).getLargoBarra()+"','"+loginJsons.get(i).getAnguloAcanalamientoArtesa1()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz()+"', '"+loginJsons.get(i).getIntegridadSoportesRodilloImpacto()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreCortinas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEnBanda()+"', '"+loginJsons.get(i).getIntegridadSoportesCamaImpacto()+"', '"+loginJsons.get(i).getInclinacionZonaCargue()+"', '"+loginJsons.get(i).getSistemaAlineacionCarga()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnCarga()+"', '"+loginJsons.get(i).getSistemasAlineacionCargFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getSistemasAlineacionRetornoFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoPlano()+"', '"+loginJsons.get(i).getSistemaAlineacionArtesaCarga()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoEnV()+"', '"+loginJsons.get(i).getLargoEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroRodilloLateralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloLateralCarga()+"', '"+loginJsons.get(i).getTipoRodilloCarga()+"', '"+loginJsons.get(i).getDistanciaEntreArtesasCarga()+"', '"+loginJsons.get(i).getAnchoInternoChasisRodilloCarga()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()+"', '"+loginJsons.get(i).getDetalleRodilloCentralCarga()+"', '"+loginJsons.get(i).getDetalleRodilloLateralCarg()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getLargoEjePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizTransportadora()+"', '"+loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getPotenciaMotorTransportadora()+"', '"+loginJsons.get(i).getGuardaPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoEstructura()+"', '"+loginJsons.get(i).getAnchoTrayectoCarga()+"', '"+loginJsons.get(i).getPasarelaRespectoAvanceBanda()+"', '"+loginJsons.get(i).getMaterialAlimenticioTransportadora()+"', '"+loginJsons.get(i).getMaterialAcidoTransportadora()+"', '"+loginJsons.get(i).getMaterialTempEntre80y150Transportadora()+"', '"+loginJsons.get(i).getMaterialSecoTransportadora()+"', '"+loginJsons.get(i).getMaterialHumedoTransportadora()+"', '"+loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()+"', '"+loginJsons.get(i).getMaterialPegajosoTransportadora()+"', '"+loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()+"', '"+loginJsons.get(i).getMarcaLimpiadorPrimario()+"'           ,'"+loginJsons.get(i).getReferenciaLimpiadorPrimario()+"' ,'"+loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getAltoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTensorLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorPrimario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla()+"','"+loginJsons.get(i).getCuchillaEnContactoConBanda()+"' , '"+loginJsons.get(i).getMarcaLimpiadorSecundario()+"','"+loginJsons.get(i).getReferenciaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorSecundario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorSecundario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla1()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda1()+"', '"+loginJsons.get(i).getSistemaDribbleChute()+"','"+loginJsons.get(i).getMarcaLimpiadorTerciario()+"', '"+loginJsons.get(i).getReferenciaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getFrecuenciaRevisionCuchilla2()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda2()+"', '"+loginJsons.get(i).getEstadoRodilloRetorno()+"', '"+loginJsons.get(i).getLargoEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroRodilloRetorno()+"', '"+loginJsons.get(i).getLargoTuboRodilloRetorno()+"', '"+loginJsons.get(i).getTipoRodilloRetorno()+"', '"+loginJsons.get(i).getDistanciaEntreRodillosRetorno()+"', '"+loginJsons.get(i).getAnchoInternoChasisRetorno()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getDetalleRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDimetroPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroPoleaTensora()+"', '"+loginJsons.get(i).getAnchoPoleaTensora()+"', '"+loginJsons.get(i).getTipoPoleaTensora()+"', '"+loginJsons.get(i).getLargoEjePoleaTensora()+"', '"+loginJsons.get(i).getDiametroEjePoleaTensora()+"', '"+loginJsons.get(i).getIcobandasCentradaEnPoleaTensora()+"', '"+loginJsons.get(i).getRecorridoPoleaTensora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaTensora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaTensora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaColaTensora()+"', '"+loginJsons.get(i).getPotenciaMotorPoleaTensora()+"', '"+loginJsons.get(i).getGuardaPoleaTensora()+"', '"+loginJsons.get(i).getPuertasInspeccion()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoPlano()+"', '"+loginJsons.get(i).getGuardaTruTrainer()+"', '"+loginJsons.get(i).getGuardaPoleaDeflectora()+"', '"+loginJsons.get(i).getGuardaZonaDeTransito()+"', '"+loginJsons.get(i).getGuardaMotores()+"', '"+loginJsons.get(i).getGuardaCadenas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getInterruptoresDeSeguridad()+"', '"+loginJsons.get(i).getSirenasDeSeguridad()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoV()+"', '"+loginJsons.get(i).getDiametroRodilloCentralCarga()+"', '"+loginJsons.get(i).getTipoRodilloImpacto()+"', '"+loginJsons.get(i).getIntegridadSoporteCamaSellado()+"', '"+loginJsons.get(i).getAtaqueAbrasivoTransportadora()+"','Sincronizado','"+loginJsons.get(i).getObservacionRegistroTransportadora()+"')");
                                            }
                                        }
                                    }
                                    db.close();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    MDToast.makeText(MainActivity.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                }
                            });
                            requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            queue.add(requestTransportadores);
                        }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {



                    MDToast.makeText(MainActivity.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                }
            });

            requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(requestLogin);
        }
        else
        {
            rol="agente";
            String url = Constants.url + "login/" + usuario + "&" + contra;
            final String[] nombreAdmin = new String[1];
            final StringRequest requestLogin = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {



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


                            alerta.setTitle("ICOBANDAS S.A dice:");
                            alerta.setMessage("Usted no está autorizado para ingresar al sistema");
                            alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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


                            db.execSQL("insert into usuario values('" + loginJsons.get(0).getCodagente() + "','" + loginJsons.get(0).getNombreagte() + "','" + loginJsons.get(0).getPwdagente() + "','agente')");
                            Cursor cursor1= db.rawQuery("SELECT * FROM usuario where nombreUsuario ='"+usuario.toString().toUpperCase()+"' and contraseñaUsuario='"+contra.toString()+"'", null);
                            cursor1.moveToFirst();
                            nombreAdmin[0] =cursor1.getString(1);
                            nombreUsuario[0] =cursor1.getString(0);

                            for (int i = 0; i < loginJsons.size(); i++) {
                                if (loginJsons.get(i).getNit() != null) {
                                    db.execSQL("INSERT INTO clientes values('" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameunido() + "','Sincronizado')");
                                }

                                db.execSQL("INSERT INTO plantas values(" + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','" + loginJsons.get(i).getNit() + "','" + loginJsons.get(i).getNameplanta() + "',null,null,null)");

                                if (loginJsons.get(i).getIdRegistro() != null) {
                                    db.execSQL("INSERT INTO registro values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getFechaRegistro() + "'," + loginJsons.get(i).getIdTransportador() + "," + loginJsons.get(i).getCodplanta() + ",'" + loginJsons.get(i).getCodagente() + "','Sincronizado', '"+loginJsons.get(i).getEstadoRegistro()+"')");
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

                                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.execSQL("DELETE FROM transportador");

                                    for (int i = 0; i < loginTransportadores.size(); i++) {
                                        db.execSQL("INSERT INTO transportador values(" + loginTransportadores.get(i).getIdTransportador() + ",'" + loginTransportadores.get(i).getTipoTransportador() + "','" + loginTransportadores.get(i).getNombreTransportador() + "','" + loginTransportadores.get(i).getCodplanta() + "','" + loginTransportadores.get(i).getCaracteristicaTransportador() + "', 'Sincronizado')");

                                    }
                                    for (int i = 0; i < loginJsons.size(); i++) {
                                        for (int x = 0; x < loginTransportadores.size(); x++) {
                                            if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.DSF"))
                                            {
                                                db.execSQL("INSERT INTO bandaTransmision values(" + loginJsons.get(i).getIdRegistro() + ",'" + loginJsons.get(i).getAnchoBandaTransmision() + "','" + loginJsons.get(i).getDistanciaEntreCentrosTransmision() + "','" + loginJsons.get(i).getPotenciaMotorTransmision() + "','" + loginJsons.get(i).getRpmSalidaReductorTransmision() + "','" + loginJsons.get(i).getDiametroPoleaConducidaTransmision() + "','" + loginJsons.get(i).getAnchoPoleaConducidaTransmision() + "','" + loginJsons.get(i).getDiametroPoleaMotrizTransmision() + "','" + loginJsons.get(i).getAnchoPoleaMotrizTransmision() + "','" + loginJsons.get(i).getTipoParteTransmision() + "','Sincronizado','"+loginJsons.get(i).getObservacionRegistro()+"')");
                                            }

                                            else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.E"))
                                            {
                                                db.execSQL("INSERT INTO bandaElevadora values('"+loginJsons.get(i).getIdRegistro()+"', '"+loginJsons.get(i).getMarcaBandaElevadora()+"', '"+loginJsons.get(i).getAnchoBandaElevadora()+"', '"+loginJsons.get(i).getDistanciaEntrePoleasElevadora()+"', '"+loginJsons.get(i).getNoLonaBandaElevadora()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadora()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadora()+"', '"+loginJsons.get(i).getEspesorCojinActualElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorElevadora()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorElevadora()+"', '"+loginJsons.get(i).getTipoCubiertaElevadora()+"', '"+loginJsons.get(i).getTipoEmpalmeElevadora()+"', '"+loginJsons.get(i).getEstadoEmpalmeElevadora()+"', '"+loginJsons.get(i).getResistenciaRoturaLonaElevadora()+"', '"+loginJsons.get(i).getVelocidadBandaElevadora()+"', '"+loginJsons.get(i).getMarcaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getAnchoBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getNoLonasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoLonaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorTotalBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaSuperiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCojinElevadoraAnterior()+"', '"+loginJsons.get(i).getEspesorCubiertaInferiorBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getTipoCubiertaElevadoraAnterior()+"', '"+loginJsons.get(i).getResistenciaRoturaBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getTonsTransportadasBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getVelocidadBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getCausaFallaCambioBandaElevadoraAnterior()+"', '"+loginJsons.get(i).getPesoMaterialEnCadaCangilon()+"', '"+loginJsons.get(i).getPesoCangilonVacio()+"', '"+loginJsons.get(i).getLongitudCangilon()+"', '"+loginJsons.get(i).getMaterialCangilon()+"', '"+loginJsons.get(i).getTipoCangilon()+"', '"+loginJsons.get(i).getProyeccionCangilon()+"','"+loginJsons.get(i).getProfundidadCangilon()+"' ,'"+loginJsons.get(i).getMarcaCangilon()+"', '"+loginJsons.get(i).getReferenciaCangilon()+"', '"+loginJsons.get(i).getCapacidadCangilon()+"', '"+loginJsons.get(i).getNoFilasCangilones()+"', '"+loginJsons.get(i).getSeparacionCangilones()+"', '"+loginJsons.get(i).getNoAgujeros()+"', '"+loginJsons.get(i).getDistanciaBordeBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaPosteriorBandaEstructura()+"', '"+loginJsons.get(i).getDistanciaLaboFrontalCangilonEstructura()+"', '"+loginJsons.get(i).getDistanciaBordesCangilonEstructura()+"', '"+loginJsons.get(i).getTipoVentilacion()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getLargoEjeMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getPotenciaMotorMotrizElevadora()+"', '"+loginJsons.get(i).getRpmSalidaReductorMotrizElevadora()+"', '"+loginJsons.get(i).getGuardaReductorPoleaMotrizElevadora()+"', '"+loginJsons.get(i).getDiametroPoleaColaElevadora()+"', '"+loginJsons.get(i).getAnchoPoleaColaElevadora()+"', '"+loginJsons.get(i).getTipoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLargoEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getDiametroEjePoleaColaElevadora()+"', '"+loginJsons.get(i).getBandaCentradaEnPoleaColaElevadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudTensorTornilloPoleaColaElevadora()+"', '"+loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaElevadora()+"', '"+loginJsons.get(i).getCargaTrabajoBandaElevadora()+"', '"+loginJsons.get(i).getTemperaturaMaterialElevadora()+"', '"+loginJsons.get(i).getEmpalmeMecanicoElevadora()+"', '"+loginJsons.get(i).getDiametroRoscaElevadora()+"', '"+loginJsons.get(i).getLargoTornilloElevadora()+"', '"+loginJsons.get(i).getMaterialTornilloElevadora()+"', '"+loginJsons.get(i).getAnchoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoCabezaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getAnchoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getLargoBotaElevadorPuertaInspeccion()+"', '"+loginJsons.get(i).getMonitorPeligro()+"', '"+loginJsons.get(i).getRodamiento()+"', '"+loginJsons.get(i).getMonitorDesalineacion()+"', '"+loginJsons.get(i).getMonitorVelocidad()+"', '"+loginJsons.get(i).getSensorInductivo()+"', '"+loginJsons.get(i).getIndicadorNivel()+"', '"+loginJsons.get(i).getCajaUnion()+"', '"+loginJsons.get(i).getAlarmaYPantalla()+"', '"+loginJsons.get(i).getInterruptorSeguridad()+"', '"+loginJsons.get(i).getMaterialElevadora()+"', '"+loginJsons.get(i).getAtaqueQuimicoElevadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaElevadora()+"', '"+loginJsons.get(i).getAtaqueAceitesElevadora()+"', '"+loginJsons.get(i).getAtaqueAbrasivoElevadora()+"', '"+loginJsons.get(i).getCapacidadElevadora()+"', '"+loginJsons.get(i).getHorasTrabajoDiaElevadora()+"', '"+loginJsons.get(i).getDiasTrabajoSemanaElevadora()+"', '"+loginJsons.get(i).getAbrasividadElevadora()+"', '"+loginJsons.get(i).getPorcentajeFinosElevadora()+"', '"+loginJsons.get(i).getMaxGranulometriaElevadora()+"', '"+loginJsons.get(i).getDensidadMaterialElevadora()+"', '"+loginJsons.get(i).getTempMaxMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaElevadora()+"', '"+loginJsons.get(i).getVariosPuntosDeAlimentacion()+"', '"+loginJsons.get(i).getLluviaDeMaterial()+"', '"+loginJsons.get(i).getAnchoPiernaElevador()+"', '"+loginJsons.get(i).getProfundidadPiernaElevador()+"', '"+loginJsons.get(i).getTempAmbienteMin()+"', '"+loginJsons.get(i).getTempAmbienteMax()+"', '"+loginJsons.get(i).getTipoDescarga()+"', '"+loginJsons.get(i).getTipoCarga()+"','Sincronizado','"+loginJsons.get(i).getObservacionRegistroElevadora()+"')");

                                            }

                                            else if (loginTransportadores.get(x).getIdTransportador().equals(loginJsons.get(i).getIdTransportador()) && loginTransportadores.get(x).getTipoTransportador().equals("B.T"))
                                            {
                                                db.execSQL("INSERT into bandaTransportadora VALUES (" + loginJsons.get(i).getIdRegistro() + ", '" + loginJsons.get(i).getMarcaBandaTransportadora() + "', '" + loginJsons.get(i).getAnchoBandaTransportadora() + "', '" + loginJsons.get(i).getNoLonasBandaTransportadora() + "', '" + loginJsons.get(i).getTipoLonaBandaTranportadora() + "', '" + loginJsons.get(i).getEspesorTotalBandaTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorTransportadora() + "', '" + loginJsons.get(i).getEspesorCojinTransportadora() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorTransportadora() + "', '" + loginJsons.get(i).getTipoCubiertaTransportadora() + "', '" + loginJsons.get(i).getTipoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getEstadoEmpalmeTransportadora() + "', '" + loginJsons.get(i).getDistanciaEntrePoleasBandaHorizontal() + "', '" + loginJsons.get(i).getInclinacionBandaHorizontal() + "', '" + loginJsons.get(i).getRecorridoUtilTensorBandaHorizontal() + "', '" + loginJsons.get(i).getLongitudSinfinBandaHorizontal() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaTransportadora() + "', '" + loginJsons.get(i).getLocalizacionTensorTransportadora() + "', '" + loginJsons.get(i).getBandaReversible() + "', '" + loginJsons.get(i).getBandaDeArrastre() + "', '" + loginJsons.get(i).getVelocidadBandaHorizontal() + "', '" + loginJsons.get(i).getMarcaBandaHorizontalAnterior() + "','" + loginJsons.get(i).getAnchoBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getNoLonasBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorTotalBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaSuperiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCubiertaInferiorBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getEspesorCojinBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoCubiertaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTipoEmpalmeBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getResistenciaRoturaLonaBandaHorizontalAnterior() + "', '" + loginJsons.get(i).getTonsTransportadasBandaHoizontalAnterior() + "', '" + loginJsons.get(i).getCausaFallaCambioBandaHorizontal() + "', '" + loginJsons.get(i).getDiametroPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnchoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLargoEjePoleaColaTransportadora() + "', '" + loginJsons.get(i).getDiametroEjePoleaColaHorizontal() + "', '" + loginJsons.get(i).getIcobandasCentradaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getAnguloAmarrePoleaColaTransportadora() + "', '" + loginJsons.get(i).getEstadoRvtoPoleaColaTransportadora() + "', '" + loginJsons.get(i).getTipoTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getDistanciaTransicionPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudTensorTornilloPoleaColaTransportadora() + "', '" + loginJsons.get(i).getLongitudRecorridoContrapesaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getGuardaPoleaColaTransportadora() + "', '" + loginJsons.get(i).getHayDesviador() + "', '" + loginJsons.get(i).getElDesviadorBascula() + "','"+loginJsons.get(i).getPresionUniformeALoAnchoDeLaBanda()+"' ,'" + loginJsons.get(i).getCauchoVPlow() + "', '" + loginJsons.get(i).getAnchoVPlow() + "', '" + loginJsons.get(i).getEspesorVPlow() + "', '" + loginJsons.get(i).getTipoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getEstadoRevestimientoTolvaCarga() + "', '" + loginJsons.get(i).getDuracionPromedioRevestimiento() + "', '" + loginJsons.get(i).getDeflectores() + "','" + loginJsons.get(i).getAltureCaida() + "', '" + loginJsons.get(i).getLongitudImpacto() + "',"+
                                                        "'"+loginJsons.get(i).getMaterial()+"', '"+loginJsons.get(i).getAnguloSobreCarga()+"', '"+loginJsons.get(i).getAtaqueQuimicoTransportadora()+"', '"+loginJsons.get(i).getAtaqueTemperaturaTransportadora()+"', '"+loginJsons.get(i).getAtaqueAceiteTransportadora()+"', '"+loginJsons.get(i).getAtaqueImpactoTransportadora()+"', '"+loginJsons.get(i).getCapacidadTransportadora()+"', '"+loginJsons.get(i).getHorasTrabajoPorDiaTransportadora()+"', '"+loginJsons.get(i).getDiasTrabajoPorSemanaTransportadora()+"', '"+loginJsons.get(i).getAlimentacionCentradaTransportadora()+"', '"+loginJsons.get(i).getAbrasividadTransportadora()+"', '"+loginJsons.get(i).getPorcentajeFinosTransportadora()+"', '"+loginJsons.get(i).getMaxGranulometriaTransportadora()+"', '"+loginJsons.get(i).getMaxPesoTransportadora()+"', '"+loginJsons.get(i).getDensidadTransportadora()+"', '"+loginJsons.get(i).getTempMaximaMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getTempPromedioMaterialSobreBandaTransportadora()+"', '"+loginJsons.get(i).getCajaColaDeTolva()+"', '"+loginJsons.get(i).getFugaMateriales()+"', '"+loginJsons.get(i).getFugaDeMaterialesEnLaColaDelChute()+"', '"+loginJsons.get(i).getFugaDeMaterialesPorLosCostados()+"', '"+loginJsons.get(i).getFugaDeMaterialParticuladoALaSalidaDelChute()+"', '"+loginJsons.get(i).getAnchoChute()+"', '"+loginJsons.get(i).getLargoChute()+"', '"+loginJsons.get(i).getAlturaChute()+"', '"+loginJsons.get(i).getCauchoGuardabandas()+"', '"+loginJsons.get(i).getTriSealMultiSeal()+"', '"+loginJsons.get(i).getEspesorGuardaBandas()+"', '"+loginJsons.get(i).getAnchoGuardaBandas()+"', '"+loginJsons.get(i).getLargoGuardaBandas()+"', '"+loginJsons.get(i).getProtectorGuardaBandas()+"', '"+loginJsons.get(i).getCortinaAntiPolvo1()+"', '"+loginJsons.get(i).getCortinaAntiPolvo2()+"', '"+loginJsons.get(i).getCortinaAntiPolvo3()+"', '"+loginJsons.get(i).getBoquillasCanonesDeAire()+"', '"+loginJsons.get(i).getTempAmbienteMaxTransportadora()+"', '"+loginJsons.get(i).getTempAmbienteMinTransportadora()+"', '"+loginJsons.get(i).getTieneRodillosImpacto()+"', '"+loginJsons.get(i).getCamaImpacto()+"', '"+loginJsons.get(i).getCamaSellado()+"', '"+loginJsons.get(i).getBasculaPesaje()+"', '"+loginJsons.get(i).getRodilloCarga()+"', '"+loginJsons.get(i).getRodilloImpacto()+"', '"+loginJsons.get(i).getBasculaASGCO()+"', '"+loginJsons.get(i).getBarraImpacto()+"', '"+loginJsons.get(i).getBarraDeslizamiento()+"', '"+loginJsons.get(i).getEspesorUHMV()+"', '"+loginJsons.get(i).getAnchoBarra()+"', '"+loginJsons.get(i).getLargoBarra()+"','"+loginJsons.get(i).getAnguloAcanalamientoArtesa1()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa1AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa2AntesPoleaMotriz()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesa3AntesPoleaMotriz()+"', '"+loginJsons.get(i).getIntegridadSoportesRodilloImpacto()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreCortinas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEntreGuardabandas()+"', '"+loginJsons.get(i).getMaterialAtrapadoEnBanda()+"', '"+loginJsons.get(i).getIntegridadSoportesCamaImpacto()+"', '"+loginJsons.get(i).getInclinacionZonaCargue()+"', '"+loginJsons.get(i).getSistemaAlineacionCarga()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnCarga()+"', '"+loginJsons.get(i).getSistemasAlineacionCargFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getCantidadSistemaAlineacionEnRetorno()+"', '"+loginJsons.get(i).getSistemasAlineacionRetornoFuncionando()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoPlano()+"', '"+loginJsons.get(i).getSistemaAlineacionArtesaCarga()+"', '"+loginJsons.get(i).getSistemaAlineacionRetornoEnV()+"', '"+loginJsons.get(i).getLargoEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloCentralCarga()+"', '"+loginJsons.get(i).getLargoEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroEjeRodilloLateralCarga()+"', '"+loginJsons.get(i).getDiametroRodilloLateralCarga()+"', '"+loginJsons.get(i).getLargoTuboRodilloLateralCarga()+"', '"+loginJsons.get(i).getTipoRodilloCarga()+"', '"+loginJsons.get(i).getDistanciaEntreArtesasCarga()+"', '"+loginJsons.get(i).getAnchoInternoChasisRodilloCarga()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getAnguloAcanalamientoArtesaCArga()+"', '"+loginJsons.get(i).getDetalleRodilloCentralCarga()+"', '"+loginJsons.get(i).getDetalleRodilloLateralCarg()+"', '"+loginJsons.get(i).getDiametroPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getLargoEjePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDiametroEjeMotrizTransportadora()+"', '"+loginJsons.get(i).getIcobandasCentraEnPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnguloAmarrePoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getPotenciaMotorTransportadora()+"', '"+loginJsons.get(i).getGuardaPoleaMotrizTransportadora()+"', '"+loginJsons.get(i).getAnchoEstructura()+"', '"+loginJsons.get(i).getAnchoTrayectoCarga()+"', '"+loginJsons.get(i).getPasarelaRespectoAvanceBanda()+"', '"+loginJsons.get(i).getMaterialAlimenticioTransportadora()+"', '"+loginJsons.get(i).getMaterialAcidoTransportadora()+"', '"+loginJsons.get(i).getMaterialTempEntre80y150Transportadora()+"', '"+loginJsons.get(i).getMaterialSecoTransportadora()+"', '"+loginJsons.get(i).getMaterialHumedoTransportadora()+"', '"+loginJsons.get(i).getMaterialAbrasivoFinoTransportadora()+"', '"+loginJsons.get(i).getMaterialPegajosoTransportadora()+"', '"+loginJsons.get(i).getMaterialGrasosoAceitosoTransportadora()+"', '"+loginJsons.get(i).getMarcaLimpiadorPrimario()+"'           ,'"+loginJsons.get(i).getReferenciaLimpiadorPrimario()+"' ,'"+loginJsons.get(i).getAnchoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getAltoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoCuchillaLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTensorLimpiadorPrimario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorPrimario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla()+"','"+loginJsons.get(i).getCuchillaEnContactoConBanda()+"' , '"+loginJsons.get(i).getMarcaLimpiadorSecundario()+"','"+loginJsons.get(i).getReferenciaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorSecundario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorSecundario()+"','"+loginJsons.get(i).getEstadoTuboLimpiadorSecundario()+"','"+loginJsons.get(i).getFrecuenciaRevisionCuchilla1()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda1()+"', '"+loginJsons.get(i).getSistemaDribbleChute()+"','"+loginJsons.get(i).getMarcaLimpiadorTerciario()+"', '"+loginJsons.get(i).getReferenciaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAnchoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getAltoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoCuchillaLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getEstadoTensorLimpiadorTerciario()+"', '"+loginJsons.get(i).getFrecuenciaRevisionCuchilla2()+"', '"+loginJsons.get(i).getCuchillaEnContactoConBanda2()+"', '"+loginJsons.get(i).getEstadoRodilloRetorno()+"', '"+loginJsons.get(i).getLargoEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroEjeRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroRodilloRetorno()+"', '"+loginJsons.get(i).getLargoTuboRodilloRetorno()+"', '"+loginJsons.get(i).getTipoRodilloRetorno()+"', '"+loginJsons.get(i).getDistanciaEntreRodillosRetorno()+"', '"+loginJsons.get(i).getAnchoInternoChasisRetorno()+"', '"+loginJsons.get(i).getAnchoExternoChasisRetorno()+"', '"+loginJsons.get(i).getDetalleRodilloRetorno()+"', '"+loginJsons.get(i).getDiametroPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaMotriz()+"', '"+loginJsons.get(i).getDimetroPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getAnchoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getTipoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getLargoEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroEjePoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getIcobandasCentradaPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaAmarrePoleaCola()+"', '"+loginJsons.get(i).getDiametroPoleaTensora()+"', '"+loginJsons.get(i).getAnchoPoleaTensora()+"', '"+loginJsons.get(i).getTipoPoleaTensora()+"', '"+loginJsons.get(i).getLargoEjePoleaTensora()+"', '"+loginJsons.get(i).getDiametroEjePoleaTensora()+"', '"+loginJsons.get(i).getIcobandasCentradaEnPoleaTensora()+"', '"+loginJsons.get(i).getRecorridoPoleaTensora()+"', '"+loginJsons.get(i).getEstadoRevestimientoPoleaTensora()+"', '"+loginJsons.get(i).getTipoTransicionPoleaTensora()+"', '"+loginJsons.get(i).getDistanciaTransicionPoleaColaTensora()+"', '"+loginJsons.get(i).getPotenciaMotorPoleaTensora()+"', '"+loginJsons.get(i).getGuardaPoleaTensora()+"', '"+loginJsons.get(i).getPuertasInspeccion()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoPlano()+"', '"+loginJsons.get(i).getGuardaTruTrainer()+"', '"+loginJsons.get(i).getGuardaPoleaDeflectora()+"', '"+loginJsons.get(i).getGuardaZonaDeTransito()+"', '"+loginJsons.get(i).getGuardaMotores()+"', '"+loginJsons.get(i).getGuardaCadenas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getGuardaCorreas()+"', '"+loginJsons.get(i).getInterruptoresDeSeguridad()+"', '"+loginJsons.get(i).getSirenasDeSeguridad()+"', '"+loginJsons.get(i).getGuardaRodilloRetornoV()+"', '"+loginJsons.get(i).getDiametroRodilloCentralCarga()+"', '"+loginJsons.get(i).getTipoRodilloImpacto()+"', '"+loginJsons.get(i).getIntegridadSoporteCamaSellado()+"', '"+loginJsons.get(i).getAtaqueAbrasivoTransportadora()+"','Sincronizado','"+loginJsons.get(i).getObservacionRegistroTransportadora()+"')");
                                            }
                                        }
                                    }
                                    dialogCarga.cancel();
                                    notificationManager.cancelAll();
                                    Service.verificacion=1;
                                    AlertDialog.Builder alerta= new AlertDialog.Builder(MainActivity.this);
                                    alerta.setTitle("ICOBANDAS S.A dice:");
                                    alerta.setMessage("Todos los datos han sido sincronizados exitosamente");
                                    alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Fragment f = new FragmentRegistrosRecientes();

                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.contenedor, f, "main").addToBackStack("main")
                                                    .commit();

                                            dialog.cancel();

                                        }
                                    });
                                    alerta.create();
                                    alerta.show();
                                    db.close();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    MDToast.makeText(MainActivity.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

                                }
                            });
                            requestTransportadores.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            queue.add(requestTransportadores);
                        }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    MDToast.makeText(MainActivity.this, error.toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                }
            });

            requestLogin.setRetryPolicy(new DefaultRetryPolicy(90000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(requestLogin);
        }
    }




}

