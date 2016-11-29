package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.icu.text.UnicodeSet;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dam on 17/10/2016.
 */

public class Lista implements Parcelable {
    private long id;
    private String titulo;
    private String tipo;
    private ArrayList<ElementoLista> items;

    public static final Gson GSON = new GsonBuilder().create();
    public static final Type TYPE = new TypeToken<ArrayList<ElementoLista>>(){}.getType();

    public Lista() {

        this(0,"","", new ArrayList<ElementoLista>());
    }

    public Lista(long id, String titulo, String tipo, ArrayList<ElementoLista>items ) {
        this.id     = id;
        this.titulo = titulo;
        this.tipo   = tipo;
        this.items  = items;

    }

    protected Lista(Parcel in) {

        id      = in.readLong();
        titulo  = in.readString();
        tipo    = in.readString();
        items   = in.createTypedArrayList(ElementoLista.CREATOR);
    }

    public static final Creator<Lista> CREATOR = new Creator<Lista>() {
        @Override
        public Lista createFromParcel(Parcel in) {
            return new Lista(in);
        }

        @Override
        public Lista[] newArray(int size) {
            return new Lista[size];
        }
    };

    public void setId(long id) {

        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Long.parseLong(id);
        } catch (NumberFormatException e) {
            this.id = 0;
        }
    }


    //Metodos getter

    public long getId() {

        return id;
    }

    public void setTitulo( String titulo) {

        this.titulo = titulo;
    }

    public String getTitulo(){return titulo;}

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<ElementoLista> getItems() {
        return items;
    }

    public void setItems(ArrayList<ElementoLista> items) {
        this.items = items;
    }

    public ContentValues getContentValues() {
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId) {

        ContentValues valores = new ContentValues();

        if (withId) {

            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());

        }

        valores.put(ContratoBaseDatos.TablaNota.TITULO , this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.CUERPO, GSON.toJson(items));
        valores.put(ContratoBaseDatos.TablaNota.TIPO, "lista");
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, "");

        return valores;
    }

    public static Lista getLista(Cursor c) {

        Lista objeto = new Lista();

        if ( c.moveToFirst() ) {

            //Tomamos el valor con la posicion ya que hay dos columnas que tienen el mismo nombre
            objeto.setId(c.getLong(0));
            objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
            objeto.setTipo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TIPO)));

            ArrayList<ElementoLista> itemsLista = new ArrayList();

            //Siempre le pasamos un cursor con sus datos no nos tenemos que preocupar por su tipo
            do
            {
                ElementoLista elementoLista = ElementoLista.getElementoLista(c);
                itemsLista.add(elementoLista);
            }
            while ( c.moveToNext()  );

            objeto.setItems(itemsLista);

        }

        return objeto;
    }

    public boolean tieneInformacion(){

        return !(titulo.isEmpty() && items.isEmpty());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(tipo);
        dest.writeTypedList(items);
    }

    @Override
    public String toString() {
        return "Lista{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", items=" + Arrays.toString( items.toArray( new ElementoLista[items.size()])) +
                '}';
    }
}
