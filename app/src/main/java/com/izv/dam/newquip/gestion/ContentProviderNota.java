package com.izv.dam.newquip.gestion;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.gestion.GestionNota;


public class ContentProviderNota extends ContentProvider {

    private static final UriMatcher URI_MATCHER;

    //TIPOS MYME
    public final static String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE + " /vnd. " + ContratoBaseDatos.autoridad + "." + ContratoBaseDatos.TablaNota.TABLA;
    public final static String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE + " /vnd. " + ContratoBaseDatos.autoridad + " . " + ContratoBaseDatos.TablaNota.TABLA  ;
    public GestionNota gestor;


    private static final int TODO=0;
    private static final int CONCRETO=1;

    static {

        URI_MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ContratoBaseDatos.autoridad,ContratoBaseDatos.TablaNota.TABLA,TODO);
        URI_MATCHER.addURI(ContratoBaseDatos.autoridad,ContratoBaseDatos.TablaNota.TABLA  + "/#",CONCRETO);
    }


    public ContentProviderNota() {
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

                selection       = ContratoBaseDatos.TablaNota._ID + " = ?";
                selectionArgs   = new String[]{uri.getLastPathSegment()};

                gestor.delete(ContratoBaseDatos.TablaNota.TABLA, selection, selectionArgs);
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


        gestor = new GestionNota(getContext());
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

                case TODO : {
                    c = gestor.getCursor();
                    break;
                }
                case CONCRETO : {

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
                    String[] parametros = {uri.getLastPathSegment()};

                    c = gestor.getCursor( arbol_elementos, columns, where, parametros, null, null, null);

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
        System.out.println(tipo);
        try {

            if (tipo == CONCRETO) {

                selection       = ContratoBaseDatos.TablaNota._ID + " = ?";
                selectionArgs   = new String[]{uri.getLastPathSegment()};

                id = gestor.update(values,selection,selectionArgs);
            }

        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
            throw new UnsupportedOperationException("Not yet implemented");

        }

        return id;

    }
}