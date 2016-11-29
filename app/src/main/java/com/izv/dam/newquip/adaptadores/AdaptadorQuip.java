package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

/**
 * Created by dam on 24/10/2016.
 */

public class AdaptadorQuip extends RecyclerView.Adapter<AdaptadorQuip.ViewHolder> implements View.OnLongClickListener,View.OnClickListener{

    private Cursor cursor;
    private Context c;

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    public AdaptadorQuip( Context c ) {

        this.c  = c;
        cursor  = null;
    }


    public void changeCursor( Cursor c ) {

        this.cursor = c;
        notifyDataSetChanged();
    }

    @Override
    public AdaptadorQuip.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(c);

        View v  = li.inflate(R.layout.item_nota_quip, parent, false );

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorQuip.ViewHolder holder, int position ) {

        if ( cursor.moveToPosition(position) ) {

            String titulo = cursor.getString(cursor.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO));
            holder.mostrarContenido(titulo);
        }
    }

    @Override
    public int getItemCount() {

        if ( cursor != null ) return cursor.getCount();

        return 0;
    }

    @Override
    public void onClick(View v) {

        if ( clickListener != null ) clickListener.onClick(v);
    }

    @Override
    public boolean onLongClick(View v) {

        if ( longClickListener != null ) longClickListener.onLongClick(v);

        return true;
    }

    public void setOnClickListener(View.OnClickListener listener ) {

        this.clickListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener){

        this.longClickListener = listener;
    }

    public int getType( int position ) {

        int tipoInt = 0;

        if ( cursor != null ) {

            if ( cursor.moveToPosition(position)) {

                String tipo = cursor.getString(cursor.getColumnIndex(ContratoBaseDatos.TablaNota.TIPO));

                if ( tipo.equals("lista") ) tipoInt = 1;
            }
        }

        return tipoInt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTitulo;

        public ViewHolder(View itemView) {

            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.tvTituloNota);

            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }

        public void mostrarContenido(String titulo) {

            tvTitulo.setText(titulo);
        }
    }
}
