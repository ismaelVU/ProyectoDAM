package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.ElementoLista;
import com.izv.dam.newquip.pojo.Lista;

import java.util.ArrayList;
import java.util.Arrays;

import static com.izv.dam.newquip.pojo.Lista.GSON;
import static com.izv.dam.newquip.pojo.Lista.TYPE;

/**
 * Created by dam on 19/10/2016.
 */

public class GestionLista extends Gestion<Lista> {


    public GestionLista(Context c) {
        super(c);

    }

    public GestionLista(Context c, boolean write) {
        super(c, write);
    }


    @Override
    public long insert(Lista objeto) {

        long idNota = this.insert(ContratoBaseDatos.TablaNota.TABLA, objeto.getContentValues());

        ArrayList<ElementoLista> items = objeto.getItems();

        for ( ElementoLista el : items ) {

            el.setIdNota(idNota);
            long idItem = this.insert(ContratoBaseDatos.TablaLista.TABLA, el.getContentValues());

            el.setId(idItem);
        }

        return idNota;
    }

    @Override
    public long insert(ContentValues objeto) {

        String cuerpo = objeto.getAsString(ContratoBaseDatos.TablaNota.CUERPO);
        ArrayList<ElementoLista> items = GSON.fromJson(cuerpo, TYPE);

        //TablaNota
        objeto.remove(ContratoBaseDatos.TablaNota.CUERPO);
        objeto.put(ContratoBaseDatos.TablaNota.CUERPO, "");

        long idNota = this.insert(ContratoBaseDatos.TablaNota.TABLA, objeto);

        for ( ElementoLista el : items ) {

            el.setIdNota(idNota);
            long idItem = this.insert(ContratoBaseDatos.TablaLista.TABLA, el.getContentValues());

            el.setId(idItem);
        }

        return idNota;
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaLista.TABLA);
    }

    @Override
    public int delete(Lista objeto) {

        String condicion = ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };

        int fila = this.delete(ContratoBaseDatos.TablaNota.TABLA, condicion, argumentos);

        String condicion2       = ContratoBaseDatos.TablaLista.IDNOTA + " = ?";
        String[] argumentos2    = new String[]{objeto.getId() + ""};

        this.delete(ContratoBaseDatos.TablaLista.TABLA, condicion2, argumentos2);

        return fila;
    }

    @Override
    public int update(Lista objeto) {

        ContentValues valores= objeto.getContentValues();

        valores.remove(ContratoBaseDatos.TablaNota.CUERPO);
        valores.put(ContratoBaseDatos.TablaNota.CUERPO, "");

        String condicion= ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] argumentos = { objeto.getId() + ""};

        int fila = this.update(ContratoBaseDatos.TablaNota.TABLA, valores, condicion, argumentos);

        //Actualizamos los items
        ArrayList<ElementoLista> items = objeto.getItems();

        String where    = ContratoBaseDatos.TablaLista.IDNOTA + " = ?";
        String[] args   = new String[]{objeto.getId() + ""};

        Cursor c        = this.getCursor(ContratoBaseDatos.TablaLista.TABLA, ContratoBaseDatos.TablaLista.PROJECTION_ALL,
                                         where, args, null, null, null);

        while ( c.moveToNext() )
        {
            boolean update          = false;

            long idItem             =  c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID));
            String whereItem        =  ContratoBaseDatos.TablaLista._ID + " = ?";
            String[] argsItem       =  new String[]{idItem + ""};

            for ( ElementoLista el : items ) {

                //Lo insertamos porque no existe en la tabla
                if ( el.getIdNota() == 0 ) {

                    el.setIdNota(objeto.getId());
                    long idItemInsertado = this.insert(ContratoBaseDatos.TablaLista.TABLA, el.getContentValues());

                    el.setId(idItemInsertado);
                    update = true;

                }

                //Si existe el id en la tabla hay que actualizarlo
                if ( idItem == el.getId() ) {

                    this.update( ContratoBaseDatos.TablaLista.TABLA, el.getContentValues(), whereItem, argsItem);
                    update = true;
                }

            }

            //No existe en los items por lo que se borra
            if ( !update ) {

                this.delete( ContratoBaseDatos.TablaLista.TABLA, whereItem, argsItem);
            }

        }

        return fila;
    }


    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {


        String cuerpo = valores.getAsString(ContratoBaseDatos.TablaNota.CUERPO);

        valores.remove(ContratoBaseDatos.TablaNota.CUERPO);
        valores.put(ContratoBaseDatos.TablaNota.CUERPO, "");

        int fila = this.update(ContratoBaseDatos.TablaNota.TABLA, valores, condicion, argumentos);

        //Actualizamos los items
        ArrayList<ElementoLista> items = GSON.fromJson(cuerpo, TYPE);

        String where    = ContratoBaseDatos.TablaLista.IDNOTA + " = ?";
        String[] args   = argumentos;

        Cursor c        = this.getCursor(ContratoBaseDatos.TablaLista.TABLA, ContratoBaseDatos.TablaLista.PROJECTION_ALL,
                                         where, args, null, null, null);

        while ( c.moveToNext() )
        {
            boolean update          = false;

            long idItem             =  c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID));
            String whereItem        =  ContratoBaseDatos.TablaLista._ID + " = ?";
            String[] argsItem       =  new String[]{idItem + ""};

            for ( ElementoLista el : items ) {

                //Lo insertamos porque no existe en la tabla
                if ( el.getIdNota() == 0 ) {

                    el.setIdNota(Long.parseLong(argumentos[0]));
                    long idItemInsertado = this.insert(ContratoBaseDatos.TablaLista.TABLA, el.getContentValues());

                    el.setId(idItemInsertado);
                    update = true;

                }

                //Si existe el id en la tabla hay que actualizarlo
                if ( idItem == el.getId() ) {

                    this.update( ContratoBaseDatos.TablaLista.TABLA, el.getContentValues(), whereItem, argsItem);
                    update = true;
                }

            }

            //No existe en los items por lo que se borra
            if ( !update ) {

                this.delete( ContratoBaseDatos.TablaLista.TABLA, whereItem, argsItem);
            }

        }

        return fila;
    }

    @Override
    public Cursor getCursor() {
        return getCursor(ContratoBaseDatos.TablaNota.TABLA,ContratoBaseDatos.TablaLista.PROJECTION_ALL,ContratoBaseDatos.TablaLista.SORT_ORDER_DEFAULT);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }

    public Lista get(long id) {

        String arbol_elementos =

                ContratoBaseDatos.TablaNota.TABLA + " left join " +
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
            Lista lista = Lista.getLista(c);

            return lista;
        }

        return null;
    }


}
