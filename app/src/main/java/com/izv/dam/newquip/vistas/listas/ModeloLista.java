package com.izv.dam.newquip.vistas.listas;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoListas;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.pojo.Lista;

/**
 * Created by dam on 20/10/2016.
 */

public class ModeloLista implements ContratoListas.InterfaceModelo {
    //private GestionLista gl;
    private ContentResolver cr;



    public ModeloLista(Context c){

        //gl=new GestionLista(c);
        cr= c.getContentResolver();
    }




    @Override
    public void close() {
      //  gl.close();

    }

    @Override
    public Lista getListas(long id) {

        //return gl.get(id);
        Cursor c= cr.query(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_LISTA,id),null,null,null,null);

        return Lista.getLista(c);
    }


    @Override
    public long saveListas(Lista l) {

        long i;

        if(l.getId()==0){

            i = this.insertLista(l);
        }
        else{

            i = this.updateLista(l);
        }

        return i;
    }

    private long deleteLista(Lista l){

        //return gl.delete(l);
        return cr.delete(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_LISTA, l.getId()),null, null);
    }

    private long insertLista(Lista l ){

        if( l.getItems().size() == 0 && l.getTitulo().trim().compareTo("")==0){
            return 0;
        }
        //return gl.insert(l);
        Uri uri= cr.insert(ContratoBaseDatos.URI_TABLA_LISTA, l.getContentValues());

        return Long.parseLong(uri.getLastPathSegment());
    }

    private long updateLista(Lista lista){

        if( lista.getItems().size() == 0 && lista.getTitulo().compareTo("")==0){

            this.deleteLista(lista);
            //gl.delete(lista);
            return 0;
        }

        //return gl.update(lista);
        return cr.update(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_LISTA, lista.getId()), lista.getContentValues(), null,null);
    }


}