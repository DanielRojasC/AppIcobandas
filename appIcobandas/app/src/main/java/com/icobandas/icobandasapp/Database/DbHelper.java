package com.icobandas.icobandasapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.icobandas.icobandasapp.Utilities.Utilities;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utilities.CREAR_TABLA_USUARIOS);
        db.execSQL(Utilities.CREAR_TABLA_CLIENTES);
        db.execSQL(Utilities.CREAR_TABLA_CIUDAD);
        db.execSQL(Utilities.CREAR_TABLA_PLANTAS);
        db.execSQL(Utilities.CREAR_TABLA_TRANSPORTADOR);
        db.execSQL(Utilities.CREAR_TABLA_REGISTRO);
        db.execSQL(Utilities.CREAR_TABLA_ELEVADORA);
        db.execSQL(Utilities.CREAR_TABLA_PESADA);
        db.execSQL(Utilities.CREAR_TABLA_TRANSPORTADORA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS ciudades");
        db.execSQL("DROP TABLE IF EXISTS plantas");
        db.execSQL("DROP TABLE IF EXISTS transportador");
        db.execSQL("DROP TABLE IF EXISTS registro");
        db.execSQL("DROP TABLE IF EXISTS bandaTransportadora");
        db.execSQL("DROP TABLE IF EXISTS bandaElevadora");
        db.execSQL("DROP TABLE IF EXISTS bandaTransmision");
        onCreate(db);

    }
}
