package com.izv.dam.newquip.contrato;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContratoBaseDatos {

    public final static String BASEDATOS    = "quiip.sqlite";
    public final static  String autoridad   = "com.izv.dam.newquip";
    public final static  String autoridad1   = "com.izv.dam.newquip1";

    public final static Uri CONTENT_URI     = Uri.parse("content:// "+ autoridad);
    public final static Uri URI_TABLA_NOTA  = Uri.parse("content://" + autoridad + "/" + TablaNota.TABLA);
    public final static Uri URI_TABLA_LISTA = Uri.parse("content://" + autoridad1 + "/" + TablaLista.TABLA);


    public static abstract class TablaNota implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota";
        public static final String TITULO = "titulo";
        public static final String CUERPO = "cuerpo";
        public static final String TIPO = "tipo";
        public static final String IMAGEN= "imagen";
        public static final String[] PROJECTION_ALL = {_ID, TITULO, CUERPO,TIPO,IMAGEN};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

    }

    public static abstract class TablaLista implements BaseColumns{
        public static final String TABLA= "lista";
        public static final String TEXTO= "texto";
        public static final String CHECK= "marcado";
        public static final String IDNOTA= "idnota";
        public static final String[] PROJECTION_ALL  = {_ID,TEXTO,CHECK,IDNOTA};
        public static final String SORT_ORDER_DEFAULT=_ID+ " desc";

    }

}