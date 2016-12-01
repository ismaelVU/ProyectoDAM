package com.izv.dam.newquip.vistas.listas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorElementoLista;
import com.izv.dam.newquip.contrato.ContratoListas;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.vistas.notas.VistaNota;

//import org.antlr.v4.Tool;


/**
 * Created by dam on 20/10/2016.
 */

public class
VistaListas extends AppCompatActivity implements ContratoListas.InterfaceVista {

    private EditText editTextTitulo;
    private FloatingActionButton fab;
    private RecyclerView rv;
    private AdaptadorElementoLista adapter;
    private Toolbar toolbar_lista;
    private CheckBox controlar;

    private Lista lista = new Lista();
    private PresentadorLista presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);



        presentador     = new PresentadorLista(this);



        if(savedInstanceState != null){

            lista= savedInstanceState.getParcelable("lista");

        }
        else{
            Bundle b=getIntent().getExtras();

            if( b != null ) {

                lista=b.getParcelable("lista");
            }
        }

        init();

        mostrarListas(lista);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("lista", lista);
    }


    @Override
    public void mostrarListas(Lista n) {

        editTextTitulo.setText(lista.getTitulo());
        adapter.setItems(n.getItems());

    }



    private void saveLista(){

        lista.setTitulo(editTextTitulo.getText().toString());
        lista.setItems(adapter.getItems());

        long r = presentador.onSaveListas(lista);

        if(r>0 & lista.getId()==0){
            lista.setId(r);
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        saveLista();
        presentador.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presentador.onResume();
    }


    private void init() {

        editTextTitulo  = (EditText) findViewById(R.id.etTitutlo);
        fab             = (FloatingActionButton) findViewById(R.id.fabAddLista);
        rv              = (RecyclerView) findViewById(R.id.rvLista);
        adapter         = new AdaptadorElementoLista(this);
        toolbar_lista= (Toolbar) findViewById(R.id.toolbar_lista);


        adapter.setItems(lista.getItems());

        rv.setAdapter(adapter);
        rv.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false));

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                adapter.addItem();
            }
        });

        setSupportActionBar(toolbar_lista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar_lista.setNavigationIcon(R.drawable.ic_arrow_back_black_24px);


        toolbar_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}

