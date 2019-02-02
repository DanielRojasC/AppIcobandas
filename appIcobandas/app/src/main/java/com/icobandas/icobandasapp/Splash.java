package com.icobandas.icobandasapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pedirPermiso();


    }

    private void pedirPermiso() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

           /* // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                startActivity(new Intent(Splash.this, Login.class));
                finish();
            }*/


        }
        else
        {
            TimerTask tarea = new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(tarea,3000);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                startActivity(new Intent(Splash.this, Login.class));
                finish();
            }
            else
            {
                Toast.makeText(this, "Debe autorizar el uso del almacenamiento", Toast.LENGTH_LONG).show();
                pedirPermiso();

            }
        }

    }
}
