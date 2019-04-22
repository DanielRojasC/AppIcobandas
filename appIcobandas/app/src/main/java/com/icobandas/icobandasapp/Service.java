package com.icobandas.icobandasapp;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.BandaElevadoraEntities;
import com.icobandas.icobandasapp.Entities.BandaPesada;
import com.icobandas.icobandasapp.Entities.ClientesEntities;
import com.icobandas.icobandasapp.Entities.PlantasEntities;
import com.icobandas.icobandasapp.Entities.RegistroEntities;
import com.icobandas.icobandasapp.Entities.TransportadorEntities;
import com.icobandas.icobandasapp.Modelos.IdMaximaRegistro;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.icobandas.icobandasapp.MainActivity.isOnline;

public class Service extends android.app.Service {

    String respuesta;
    public static int verificacion = 0;

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    boolean toast;

    RequestQueue queue;

    NotificationManager mNotificationManager;
    PendingIntent pendingIntent;

    private static final int NOTIFY_ID = 1;
    private static final String YES_ACTION = "Si";
    private static final String MAYBE_ACTION = "com.tinbytes.simplenotificationapp.MAYBE_ACTION";
    private static final String NO_ACTION = "com.tinbytes.simplenotificationapp.NO_ACTION";

    public static Context context;

    DbHelper dbHelper;
    Cursor cursor;
    Gson gson = new Gson();
    boolean sincronizacion = false;
    int conteo = 0;
    ArrayList<String> registros = new ArrayList<>();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        TareaAsincrona a = new TareaAsincrona();
        a.execute();


        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public String hilo() {
        try {

            if (isOnline(this)) {


                if (verificacion == 0) {
                    verificacion = 1;
                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                    notifcar();

                }


            } else {
                respuesta = "NO HAY CONEXION";
                notificationManager.cancelAll();
                verificacion = 0;
                //notification.shutdown();
                toast = false;


            }
            Thread.sleep(1000);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return respuesta;

    }

    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifcar() {

        Intent yesIntent = getNotificationIntent();
        yesIntent.setAction(YES_ACTION);


        Intent maybeIntent = getNotificationIntent();
        maybeIntent.setAction(MAYBE_ACTION);
        String CHANNEL_ID = "my_channel_01";
        Intent noIntent = getNotificationIntent();
        noIntent.setAction(NO_ACTION);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
             channel= new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }

        Notification notification = new NotificationCompat.Builder(this)
                .setColor(Color.argb(100, 156,38,46))
                .setContentIntent(PendingIntent.getActivity(this, 0, getNotificationIntent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.notificacion)

                .setTicker("Tiene registros por sincronizar")
                .setContentTitle("SINCRONIZAR REGISTROS")
                .setContentText("Presione para sincronizar los registros pendientes")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                //.setOngoing(true)
                .build();



        notificationManager.notify(NOTIFY_ID, notification);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Void... voids) {

            int i = 0;
            while (i < 2) {
                hilo();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {

        }

    }

    public void sincronizarClientes() {

        dbHelper = new DbHelper(this, "prueba", null, 1);

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("Select * from clientes where estadoRegistroCliente='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY CLIENTES PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            db.execSQL("Update clientes set estadoRegistroCliente='Sincronizado' where nit='" + cursor.getString(0) + "'");
            String url = Constants.url + "sincroClientes";

            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizacion = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nitCliente", cursor.getString(0));
                    params.put("nombreCliente", cursor.getString(1));

                    return params;
                }

            };
            queue.add(requestClientes);


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

                            return params;
                        }

                    };
                    queue.add(requestClientes);
                }
                if (conteo == clientesEntitiesArrayList.size()) {
                    conteo = 0;
                    sincronizacion = true;
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
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY PLANTAS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();

                    }
                });

            }
            sincronizacion = true;


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            final String idPlanta = cursor.getString(0);
            db.execSQL("Update plantas set estadoRegistroPlanta='Sincronizado' where codplanta='" + cursor.getString(0) + "'");
            String url = Constants.url + "crearPlanta";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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
                            sincronizacion = true;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(requestPlanta);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                                    conteo++;
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            queue.add(requestPlanta);
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

                    queue.add(requestClientes);
                    if (conteo == plantasEntitiesArrayList.size()) {
                        conteo = 0;
                        sincronizacion = true;
                    }


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
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY TRANSPORTADORES PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final String tipoTransportador = cursor.getString(1);
            final String nombreTranspotador = cursor.getString(2);
            final String idPlanta = cursor.getString(3);
            final String descripcionTransportador = cursor.getString(4);
            final String idTransportador1 = cursor.getString(0);
            db.execSQL("Update transportador set estadoRegistroTransportador='Sincronizado' where idTransportador='" + cursor.getString(0) + "'");
            String url = Constants.url + "crearTransportador";
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
                            //Log.e("SENTENCIA", "update transportador set idTransportador=" + idTransportador + " where idTransportador='" + idTransportador1 + "'");
                            db.execSQL("update transportador set idTransportador=" + idTransportador + " where idTransportador='" + idTransportador1 + "'");
                            sincronizacion = true;


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(request);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                                    conteo++;

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
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
                    queue.add(requestClientes);
                    if (conteo == transportadorEntitiesArrayList.size()) {
                        conteo = 0;
                        sincronizacion = true;
                    }
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
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY REGISTROS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;


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
                            Log.e("SENTENCIA", "update bandaTransmision set idRegistro='" + idTransportador + "' where idRegistro='" + idRegistro + "'");
                            Log.e("SENTENCIA1", String.valueOf(idTransportador));
                            sincronizacion = true;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    requestMaxRegistro.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(requestMaxRegistro);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

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
                    queue.add(requestClientes).wait(2000);

                    if (conteo == registroEntitiesArrayList.size()) {
                        sincronizacion = true;
                    }

                }


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarInsercionesPesada() {
        dbHelper = new DbHelper(this, "prueba", null, 1);



        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaTransmision where estadoRegistroPesada='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY REGISTROS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;

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
            bandaPesadaArrayList.add(bandaPesada);

            db.execSQL("Update bandaTransmision set estadoRegistroPesada='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "registroBandaPesada";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizacion = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                    Log.e("PARAMS", params.toString());


                    return params;
                }

            };
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

                for (int i = 0; i < bandaPesadaArrayList.size(); i++) {


                    String url = Constants.url + "registroBandaPesada";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransmision set  estadoRegistroPesada='Sincronizado' where idRegistro='" + bandaPesadaArrayList.get(finalI).getIdRegistro() + "'");

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


                            return params;
                        }

                    };
                    queue.add(requestClientes);
                    sincronizacion = true;

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
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY REGISTROS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;


        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final String idRegistro = cursor.getString(0);
            db.execSQL("Update registro set estadoRegistro='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarRegistro";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizacion = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                            if (response.equals("\ufeffok")) {
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
                    queue.add(requestClientes).wait(2000);

                    if (conteo == registroEntitiesArrayList.size()) {
                        sincronizacion = true;
                    }

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
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY REGISTROS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;

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
            bandaPesadaArrayList.add(bandaPesada);

            db.execSQL("Update bandaTransmision set estadoRegistroPesada='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "actualizarBandaPesada";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                    Log.e("PARAMS", params.toString());


                    return params;
                }

            };
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

                for (int i = 0; i < bandaPesadaArrayList.size(); i++) {


                    String url = Constants.url + "actualizarBandaPesada";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaTransmision set  estadoRegistroPesada='Sincronizado' where idRegistro='" + bandaPesadaArrayList.get(finalI).getIdRegistro() + "'");

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


                            return params;
                        }

                    };
                    queue.add(requestClientes);

                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }

    public void sincronizarInsercionesVertical()
    {
        dbHelper = new DbHelper(this, "prueba", null, 1);



        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from bandaElevadora where estadoRegistroElevadora='Pendiente INSERTAR BD'", null);
        if (cursor.getCount() == 0) {
            if (!toast) {
                toast = true;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "NO HAY REGISTROS PARA SINCRONIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            sincronizacion = true;

        } else if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            final ArrayList<BandaElevadoraEntities> bandaElevadoraEntitiesArrayList= new ArrayList<>();


            BandaElevadoraEntities bandaElevadoraEntities= new BandaElevadoraEntities();
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

            bandaElevadoraEntitiesArrayList.add(bandaElevadoraEntities);

            db.execSQL("Update bandaElevadora set estadoRegistroElevadora='Sincronizado' where idRegistro='" + cursor.getString(0) + "'");
            String url = Constants.url + "registroBandaPesada";
            StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sincronizacion = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                   /* params.put("idRegistro", bandaPesadaArrayList.get(0).getIdRegistro());
                    params.put("anchoBandaTransmision", bandaPesadaArrayList.get(0).getAnchoBandaTransmision());
                    params.put("distanciaEntreCentrosTransmision", bandaPesadaArrayList.get(0).getDistanciaEntreCentrosTransmision());
                    params.put("potenciaMotorTransmision", bandaPesadaArrayList.get(0).getPotenciaMotorTransmision());
                    params.put("rpmSalidaReductorTransmision", bandaPesadaArrayList.get(0).getRpmSalidaReductorTransmision());
                    params.put("diametroPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaConducidaTransmision());
                    params.put("anchoPoleaConducidaTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaConducidaTransmision());
                    params.put("diametroPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getDiametroPoleaMotrizTransmision());
                    params.put("anchoPoleaMotrizTransmision", bandaPesadaArrayList.get(0).getAnchoPoleaMotrizTransmision());
                    params.put("tipoParteTransmision", bandaPesadaArrayList.get(0).getTipoParteTransmision());*/



                    return params;
                }

            };
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

                for (int i = 0; i < bandaPesadaArrayList.size(); i++) {


                    String url = Constants.url + "registroBandaPesada";
                    final int finalI = i;
                    StringRequest requestClientes = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //int idTransportador=Integer.parseInt(registros.get(finalI));
                            db.execSQL("update bandaElevadora set  estadoRegistroElevadora='Sincronizado' where idRegistro='" + bandaPesadaArrayList.get(finalI).getIdRegistro() + "'");

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


                            return params;
                        }

                    };
                    queue.add(requestClientes);
                    sincronizacion = true;

                }
                registros.clear();


            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

        }
    }



}







