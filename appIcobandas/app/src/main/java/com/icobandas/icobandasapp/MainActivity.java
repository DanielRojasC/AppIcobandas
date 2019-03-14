
package com.icobandas.icobandasapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Sincronizacion.Clientes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String respuesta;
    RequestQueue queue;

    public static Context context;
    int verificacion = 0;
    DbHelper dbHelper;
    Cursor cursor;
    Gson gson = new Gson();
    ArrayList<Clientes> clientesArrayList = new ArrayList<>();

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //inicializar();
        Intent intent=new Intent(this,IntentService.class);
        startService(intent);
        if(isMyServiceRunning(IntentService.class))
        {
            Log.e("SERVICIO", "EL SERVICIO ESTÁ CORRIENDO");
        }
        else
        {
            startService(intent);
            Log.e("SERVICIO", "EL SERVICIO NO ESTÁ CORRIENDO");

        }
        context = this.getApplicationContext();
        //Intent intent = new Intent(this, Service.class);
        //startService(intent);

        /*if (!isMyServiceRunning(Service.class)) {//método que determina si el servicio ya está corriendo o no

            TareaAsincrona a = new TareaAsincrona();
            a.execute();

            Intent intent = new Intent(getApplicationContext(), com.icobandas.icobandasapp.Service.class); //serv de tipo Intent
            getApplicationContext().startService(intent); //ctx de tipo Context


        } else {
            Log.e("App", "Service already running");
        }*/


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
            if(toolbar.getChildAt(i) instanceof ImageButton){
                toolbar.getChildAt(i).setScaleX(0.6f);
                toolbar.getChildAt(i).setScaleY(0.6f);
            }
        }


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.txtNombreUsuario);

        if (isOnline(getApplicationContext())) {
            if(Login.rol.equals("admin"))
            {
                navUsername.setText("AGENTE:\n" + Login.nombreAdmin+"\n"+Login.nombreUsuario);
            }
            else
            {
                navUsername.setText("AGENTE:\n" + Login.loginJsons.get(0).getNombreagte()+"\n"+Login.loginJsons.get(0).getCodagente());

            }
        }
        else
        {
            navUsername.setText("AGENTE:\n" + Login.cursor.getString(1));

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new FragmentRegistrosRecientes()).commit();



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.contenedor);

            if(currentFragment instanceof FragmentRegistrosRecientes)
            {

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

            }
            else
            {
                super.onBackPressed();

            }

        }
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



    /*private class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {


            for (int i = 0; i < 3; i++) {
                hilo();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            ejecutar();
            Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
        }

    }

    public String hilo() {
        try {

            if (isOnline(this)) {
                respuesta = "SI HAY CONEXION";
                if (verificacion == 0) {
                    notificationManager.notify("ALGO", 1, mBuilder.build());
                    verificacion = 1;

                }


            } else {
                respuesta = "NO HAY CONEXION";
                notificationManager.cancelAll();
                verificacion = 0;
            }

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return respuesta;

    }

    public void ejecutar() {
        TareaAsincrona a = new TareaAsincrona();
        a.execute();

    }

    private void inicializar() {
        int color = Color.argb(255, 156, 38, 46);
        Intent intent = new Intent();
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notificacion)
                .setContentTitle("SINCRONIZANDO DATOS")
                .setContentText("Espere...")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColorized(true)
                .setColor(color)
                .setOngoing(true);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setProgress(100
                , 1, true);


    }

    public void sincronizarTransportadores() {
        queue = Volley.newRequestQueue(this);
        dbHelper = new DbHelper(getApplicationContext(), "prueba", null, 1);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        String url = Constants.url + "verificarCliente";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean validacion=false;
                clientesArrayList.clear();
                Type type = new TypeToken<List<Clientes>>() {
                }.getType();
                clientesArrayList = gson.fromJson(response, type);

                cursor = db.rawQuery("SELECT * from clientes", null);
                while (cursor.moveToNext()) {
                    for (int i = 0; i < clientesArrayList.size(); i++)
                    {

                        if (cursor.getString(0).equals(clientesArrayList.get(i).getNit())) {
                            Log.e("CLIENTE", "YA EXISTE");
                            validacion=true;
                            i=clientesArrayList.size()+1;
                        }
                        else
                        {
                            Log.e("CLIENTE", "NO EXISTE");
                            validacion=false;
                        }

                    }
                    Log.e("RESULTADO VALIDACION", String.valueOf(validacion));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);

    }*/



}
