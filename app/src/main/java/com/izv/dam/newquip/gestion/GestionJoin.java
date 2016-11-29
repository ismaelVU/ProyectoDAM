package com.izv.dam.newquip.gestion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.basedatos.Ayudante;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/**
 * Created by dam on 20/10/2016.
 */

public class GestionJoin {

    private Ayudante abd;
    private SQLiteDatabase db;

    public GestionJoin( Context c ) {

        abd = new Ayudante(c);
        db  = abd.getWritableDatabase();
    }

    public Cursor getCursor() {

       // Cursor N = gn.getCursor()
        //Cursor L = gl.getCursor();
        String columnas =   ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota._ID + ", " +
                            ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota.TITULO + ", " +
                            ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota.CUERPO + ", " +
                            ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota.TIPO + ", " +
                            ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota.IMAGEN + ", " +
                            ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista._ID + ", " +
                            ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista.TEXTO + ", " +
                            ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista.CHECK + ", " +
                            ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista.IDNOTA;


        Cursor cursor = db.rawQuery("select " + columnas + " from "+ ContratoBaseDatos.TablaNota.TABLA + " left join "+ ContratoBaseDatos.TablaLista.TABLA + " on " +
                ContratoBaseDatos.TablaNota.TABLA +"."+ ContratoBaseDatos.TablaNota._ID  + " = " + ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista.IDNOTA , null);

        return cursor;

    }


}
