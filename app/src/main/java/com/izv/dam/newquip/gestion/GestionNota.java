package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoListas;
import com.izv.dam.newquip.pojo.Nota;

public class GestionNota extends Gestion<Nota>{

    public GestionNota(Context c) {
        super(c);
    }

    public GestionNota(Context c, boolean write) {
        super(c, write);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaNota.TABLA);
    }

    public int delete(String condicion, String[] argumentos) {
        return this.delete(ContratoBaseDatos.TablaNota.TABLA, condicion, argumentos);
    }

    @Override
    public int delete(Nota objeto) {
        String condicion = ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        return this.delete(ContratoBaseDatos.TablaNota.TABLA, condicion, argumentos);
    }

    public Nota get(long id) {

        String arbol_elementos = ContratoBaseDatos.TablaNota.TABLA + " left join " +
                                 ContratoBaseDatos.TablaLista.TABLA + " on " +
                                 ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota._ID + " = " +
                                 ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista.IDNOTA;

        String[] columns = {
                ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota._ID,
                ContratoBaseDatos.TablaNota.TITULO,
                ContratoBaseDatos.TablaNota.CUERPO, ContratoBaseDatos.TablaNota.TIPO,
                ContratoBaseDatos.TablaNota.IMAGEN,
                ContratoBaseDatos.TablaLista.TABLA + "." + ContratoBaseDatos.TablaLista._ID,
                ContratoBaseDatos.TablaLista.TEXTO, ContratoBaseDatos.TablaLista.CHECK,
                ContratoBaseDatos.TablaLista.IDNOTA
        };

        String where = ContratoBaseDatos.TablaNota.TABLA + "." + ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] parametros = {id + ""};

        Cursor c = this.getCursor( arbol_elementos, columns, where, parametros, null, null, null);

        if(c.getCount() > 0) {

            c.moveToFirst();
            Nota nota = Nota.getNota(c);
            return nota;
        }

        return null;
    }

    @Override
    public Cursor getCursor() {

        return this.getCursor( ContratoBaseDatos.TablaNota.TABLA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, null, null, null, null, null);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(ContratoBaseDatos.TablaNota.TABLA, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaNota.TABLA, objeto);
    }

    @Override
    public long insert(Nota objeto) {
        return this.insert(ContratoBaseDatos.TablaNota.TABLA, objeto.getContentValues());
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos){
        return this.update(ContratoBaseDatos.TablaNota.TABLA, valores, condicion, argumentos);
    }

    @Override
    public int update(Nota objeto) {
        ContentValues valores = objeto.getContentValues();
        String condicion = ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };

        return this.update(ContratoBaseDatos.TablaNota.TABLA, valores, condicion, argumentos);
    }


}