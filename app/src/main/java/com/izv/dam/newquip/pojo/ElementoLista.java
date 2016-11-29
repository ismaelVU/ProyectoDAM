package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/**
 * Created by dam on 19/10/2016.
 */

public class ElementoLista implements Parcelable {

    private long id;
    private String texto;
    private boolean check;
    private long idNota;

    public ElementoLista(){

        this(0,"", false, 0);
    }

    public ElementoLista( long id, String texto, boolean check, long idNota ) {

        this.id     = id;
        this.texto  = texto;
        this.check  = check;
        this.idNota = idNota;
    }


    protected ElementoLista(Parcel in) {
        id = in.readLong();
        texto = in.readString();
        check = in.readByte() != 0;
        idNota = in.readLong();
    }

    public static final Creator<ElementoLista> CREATOR = new Creator<ElementoLista>() {
        @Override
        public ElementoLista createFromParcel(Parcel in) {
            return new ElementoLista(in);
        }

        @Override
        public ElementoLista[] newArray(int size) {
            return new ElementoLista[size];
        }
    };

    public ContentValues getContentValues() {

        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId) {

        ContentValues valores = new ContentValues();
        if (withId) {
            valores.put(ContratoBaseDatos.TablaLista._ID, this.getId());
        }

        valores.put(ContratoBaseDatos.TablaLista.TEXTO, this.getTexto());

        if ( this.isCheck() ) {

            valores.put(ContratoBaseDatos.TablaLista.CHECK, 1);
        }
        else
        {
            valores.put(ContratoBaseDatos.TablaLista.CHECK, 0);
        }

        valores.put(ContratoBaseDatos.TablaLista.IDNOTA, this.getIdNota());

        return valores;
    }

    public static ElementoLista getElementoLista( Cursor c ) {

        ElementoLista el = new ElementoLista();

        el.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID)));
        el.setTexto(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaLista.TEXTO)));
        el.setCheck(c.getInt(c.getColumnIndex(ContratoBaseDatos.TablaLista.CHECK)) == 1);
        el.setIdNota(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista.IDNOTA)));

        return el;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ElementoLista{" +
                "check=" + check +
                ", id=" + id +
                ", texto='" + texto + '\'' +
                ", idNota=" + idNota +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(texto);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeLong(idNota);
    }
}
