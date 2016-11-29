package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

import static android.R.attr.key;

public class Ayudante extends SQLiteOpenHelper {

    //sqlite
    //tipos de datos https://www.sqlite.org/datatype3.html
    //fechas https://www.sqlite.org/lang_datefunc.html
    //trigger https://www.sqlite.org/lang_createtrigger.html


    private static final int VERSION = 1;

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " ( " +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.CUERPO + " text, " +
                ContratoBaseDatos.TablaNota.TIPO+ " text," +
                ContratoBaseDatos.TablaNota.IMAGEN+ " text " +
                ")";

        db.execSQL(sql);
        Log.v("sql",sql);

        String tabla;
        tabla="create table if not exists " + ContratoBaseDatos.TablaLista.TABLA +
                " ( " +
                ContratoBaseDatos.TablaLista._ID + " integer primary key autoincrement, " +
                ContratoBaseDatos.TablaLista.TEXTO + " text, " +
                ContratoBaseDatos.TablaLista.CHECK + " integer, " +
                ContratoBaseDatos.TablaLista.IDNOTA + " integer " +
                ")";


        Log.v("sql1",tabla);
        db.execSQL(tabla);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        db.execSQL(sql);
        Log.v("sql",sql);

        String sql1="drop table if exists " + ContratoBaseDatos.TablaLista.TABLA;
        db.execSQL(sql1);
        Log.v("sql1",sql);

    }

}