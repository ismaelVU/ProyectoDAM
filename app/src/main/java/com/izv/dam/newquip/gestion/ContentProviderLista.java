package com.izv.dam.newquip.gestion;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.Lista;

public class ContentProviderLista extends ContentProvider {

    private static final UriMatcher URI_MATCHER;

    //TIPOS MYME
    public final static String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE + " /vnd. " + ContratoBaseDatos.autoridad1 + "." + ContratoBaseDatos.TablaLista.TABLA;
    public final static String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE + " /vnd. " + ContratoBaseDatos.autoridad1 + " . " + ContratoBaseDatos.TablaLista.TABLA  ;
    public GestionLista gestor;


    private static final int TODO=0;
    private static final int CONCRETO=1;

    static {

        URI_MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ContratoBaseDatos.autoridad1,ContratoBaseDatos.TablaLista.TABLA,TODO);
        URI_MATCHER.addURI(ContratoBaseDatos.autoridad1,ContratoBaseDatos.TablaLista.TABLA  + "/#",CONCRETO);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int borrados=0;
        int tipo=URI_MATCHER.match(uri);

        switch(tipo){
            case TODO:

                gestor.deleteAll();

                break;

            case CONCRETO: {

                long id = Long.parseLong(uri.getLastPathSegment());
                Lista lista = gestor.get(id);

                gestor.delete(lista);
                break;
            }
            default:
                throw new UnsupportedOperationException("Fallo en el borrado");

        }

        if(borrados > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return borrados;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.

        switch (URI_MATCHER.match(uri)){

            case TODO: return CONTENT_TYPE;

            case CONCRETO : return CONTENT_ITEM_TYPE;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        int tipo=URI_MATCHER.match(uri);

        long id;

        if(tipo==TODO ){

            id=gestor.insert(values);

            if(id>0){
                Uri uriGeller= ContentUris.withAppendedId(uri,id);
                getContext().getContentResolver().notifyChange(uriGeller,null);

                return uriGeller;
            }

        }

        throw new UnsupportedOperationException("Fallo en la inserccion");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.


        gestor = new GestionLista(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        int tipo=URI_MATCHER.match(uri);

        Cursor c = null;
        try {
            switch (tipo) {

                case CONCRETO : {

                    selection       = ContratoBaseDatos.TablaNota._ID + " = ?";
                    selectionArgs   = new String[]{uri.getLastPathSegment()};

                    c = gestor.getCursor(ContratoBaseDatos.TablaNota.TABLA, ContratoBaseDatos.TablaNota.PROJECTION_ALL,
                            selection, selectionArgs, null, null, sortOrder);

                    break;
                }

            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return c;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.

        int id = 0;
        int tipo = URI_MATCHER.match(uri);

        try {

            if (tipo == CONCRETO) {

                long id_lista = Long.parseLong(uri.getLastPathSegment());

                selection       = ContratoBaseDatos.TablaNota._ID + " = ?";
                selectionArgs   = new String[]{id_lista+""};

                gestor.update(values, selection, selectionArgs);
            }

        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
            throw new UnsupportedOperationException("Not yet implemented");

        }

        return id;

    }
}
