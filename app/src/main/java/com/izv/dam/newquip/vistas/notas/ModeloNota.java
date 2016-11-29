package com.izv.dam.newquip.vistas.notas;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloNota implements ContratoNota.InterfaceModelo {

    //private GestionNota gn = null;
    private ContentResolver cr = null;

    public ModeloNota(Context c) {

        //gn = new GestionNota(c);
        cr = c.getContentResolver();
    }

    @Override
    public void close() {
        //gn.close();
    }

    @Override
    public Nota getNota(long id) {
        //return gn.get(id);

        Cursor c = cr.query(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, id), null, null, null,null);

        return Nota.getNota(c);
    }

    @Override
    public long saveNota(Nota n) {
        long r;
        if(n.getId()==0) {
             r = this.insertNota(n);
        } else {
            r = this.updateNota(n);
        }
        return r;
    }

    private long deleteNota(Nota n) {

        System.out.println(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, n.getId()));
        return cr.delete(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, n.getId()), null, null);
    }

    private long insertNota(Nota n) {
        if(n.getCuerpo().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            return 0;
        }

        Uri uri = cr.insert(ContratoBaseDatos.URI_TABLA_NOTA, n.getContentValues());

        return Long.parseLong(uri.getLastPathSegment());
        //return gn.insert(n);
    }

    private long updateNota(Nota n) {
        if(n.getCuerpo().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            this.deleteNota(n);
            //gn.delete(n);
            return 0;
        }

        System.out.println(ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, n.getId()));
        return cr.update( ContentUris.withAppendedId(ContratoBaseDatos.URI_TABLA_NOTA, n.getId()), n.getContentValues(), null, null);
        //return gn.update(n);
    }
}