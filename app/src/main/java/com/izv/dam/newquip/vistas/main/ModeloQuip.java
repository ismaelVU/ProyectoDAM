package com.izv.dam.newquip.vistas.main;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloQuip implements ContratoMain.InterfaceModelo {

    private ContentResolver cr;
    /*private GestionNota gn = null;
    private GestionLista gl = null;*/

    private Cursor cursor;

    public ModeloQuip(Context c) {

        /*gn = new GestionNota(c);
        gl = new GestionLista(c);*/
        cr = c.getContentResolver();
    }

    @Override
    public void close() {

       // gn.close();
    }

    @Override
    public long deleteNota(Nota n) {

        return cr.delete(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, n.getId()), null, null);
    }

    @Override
    public long deleteNota(int position) {

        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return this.deleteNota(n);

    }

    @Override
    public Nota getNota(int position) {

        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return n;

    }

    @Override
    public void loadData(OnDataLoadListener listener) {

        //cursor = gn.getCursor();
        cursor = cr.query(ContratoBaseDatos.URI_TABLA_NOTA, null, null, null, null);
        listener.setCursor(cursor);
    }

    @Override
    public long deleteLista(int position) {

        cursor.moveToPosition(position);
        Lista l = Lista.getLista(cursor);

        return this.deleteLista(l);
        //return cr.delete(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_LISTA,  ,null,null);
    }

    @Override
    public long deleteLista(Lista lista) {

        //return gl.delete(lista);
        return cr.delete(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_LISTA, lista.getId()), null , null );
    }

    @Override
    public Lista getLista(int position) {

        cursor.moveToPosition(position);

        return Lista.getLista(cursor);

        //Buscamos la lista con el id asignado y sus elementos
        /*long id = cursor.getLong(cursor.getColumnIndex(ContratoBaseDatos.TablaNota._ID));

        Lista lista = gl.get(id);


        return lista;*/
    }
}