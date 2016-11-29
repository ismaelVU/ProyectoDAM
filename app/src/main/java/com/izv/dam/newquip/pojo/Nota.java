package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

public class Nota implements Parcelable {

    private long id;
    private String titulo, cuerpo;
    private String tipo;
    private String imagen;

    public Nota() {
        this(0, "", "", "", "");
    }

    public Nota(long id, String titulo, String cuerpo,String tipo,String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.tipo = tipo;
        this.imagen=imagen;
    }

    public Nota(String titulo,String cuerpo){
        this.titulo=titulo;
        this.cuerpo=cuerpo;
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        cuerpo = in.readString();
        tipo = in.readString();
        imagen = in.readString();
    }



    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Long.parseLong(id);
        } catch(NumberFormatException e){
            this.id = 0;
        }
    }


        //Metodos setter

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public void setTipo(String tipo) {
        this.tipo=tipo;
    }

    public void setImagen(String imagen){
        this.imagen=imagen;
    }

        //Metodos getter
    public String getTitulo() {
        return titulo;
    }

    public String getCuerpo() {

        return cuerpo;
    }

    public String getTipo(){
        return tipo;
    }

    public String getImagen(){
        return imagen;
    }

    public ContentValues getContentValues(){

        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId){

        ContentValues valores = new ContentValues();
        if(withId){
            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());
        }

        valores.put(ContratoBaseDatos.TablaNota.TITULO, this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.CUERPO, this.getCuerpo());
        valores.put(ContratoBaseDatos.TablaNota.TIPO, "nota");
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, this.getImagen());

        return valores;
    }

    public static Nota getNota(Cursor c){

        Nota objeto = new Nota();

        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setCuerpo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.CUERPO)));
        objeto.setTipo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TIPO)));
        objeto.setImagen(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.IMAGEN)));

        return objeto;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", tipo='"+ tipo + '\'' +
                ", imagen='" + imagen + '\''+
                '}';
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(cuerpo);
        dest.writeString(tipo);
        dest.writeString(imagen);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}