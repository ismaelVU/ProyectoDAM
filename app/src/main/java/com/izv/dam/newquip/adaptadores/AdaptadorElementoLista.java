package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.ElementoLista;

import java.util.ArrayList;

/**
 * Created by dam on 24/10/2016.
 */

public class AdaptadorElementoLista extends RecyclerView.Adapter<AdaptadorElementoLista.ViewHolder> {

    private ArrayList<ElementoLista> items;
    private Context c;

    public AdaptadorElementoLista( Context c ) {

        this.c  = c;
        items   = new ArrayList<>();
    }

    @Override
    public AdaptadorElementoLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(c);

        View v = li.inflate(R.layout.item_elemento_lista, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorElementoLista.ViewHolder holder, int position) {

        ElementoLista elementoLista = items.get(position);
        holder.mostrarElemento(elementoLista);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(){

        items.add( new ElementoLista());
        notifyItemInserted(items.size());
    }

    public void deleteItem( int position ) {

        items.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<ElementoLista> getItems(){

        return this.items;
    }

    public void setItems( ArrayList<ElementoLista> items){

        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cb1;
        public Button cb2;
        public EditText edText;

        public ViewHolder( View v ){

            super(v);

            cb1     = (CheckBox) v.findViewById(R.id.cbCheck);
            edText  = (EditText) v.findViewById(R.id.tvTexto);
            cb2     = (Button) v.findViewById(R.id.cbDelete);

            cb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = ViewHolder.super.getAdapterPosition();

                    ElementoLista el = items.get(position);
                    el.setCheck(cb1.isChecked());



                }
            });

            cb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(cb1.isChecked()){
                        edText.setEnabled(false);
                    }else{
                        edText.setEnabled(true);
                    }
                }
            });




            edText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    int position = ViewHolder.super.getAdapterPosition();

                    ElementoLista el = items.get(position);
                    el.setTexto(s.toString());
                }
            });

            cb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = ViewHolder.super.getAdapterPosition();

                    deleteItem(position);
                }
            });
        }

        public void mostrarElemento(ElementoLista el ){

            cb1.setChecked(el.isCheck());
            edText.setText(el.getTexto());
            edText.requestFocus();


        }
    }
}
