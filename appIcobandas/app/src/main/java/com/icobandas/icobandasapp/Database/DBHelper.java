package com.icobandas.icobandasapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.icobandas.icobandasapp.Utilities.Utilities;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilities.CREAR_TABLA_CLIENTES);
        db.execSQL(Utilities.CREAR_TABLA_USUARIOS);
        db.execSQL(Utilities.CREAR_TABLA_PLANTAS);
        db.execSQL(Utilities.CREAR_TABLA_CIUDAD);
        db.execSQL(Utilities.CREAR_TABLA_TRANSPORTADOR);
        db.execSQL(Utilities.CREAR_TABLA_REGISTRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_PLANTAS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_CIUDAD);
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_TRANSPORTADOR);
        db.execSQL("DROP TABLE IF EXISTS "+Utilities.TABLA_REGISTRO);
        onCreate(db);

    }
}
