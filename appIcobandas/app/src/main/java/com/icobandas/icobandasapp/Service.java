package com.icobandas.icobandasapp;


import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class Service extends android.app.Service {

    String respuesta;
    int verificacion=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

