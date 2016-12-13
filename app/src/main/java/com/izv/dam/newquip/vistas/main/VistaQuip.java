package com.izv.dam.newquip.vistas.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.service.dreams.DreamService;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.text.List;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorElementoLista;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.adaptadores.AdaptadorQuip;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.dialogo.DialogoBorrarLista;
import com.izv.dam.newquip.dialogo.OnBorrarDialogListener;
import com.izv.dam.newquip.gestion.ContentProviderNota;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrarNota;
import com.izv.dam.newquip.vistas.listas.VistaListas;
import com.izv.dam.newquip.vistas.notas.VistaNota;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista , OnBorrarDialogListener , LoaderManager.LoaderCallbacks<Cursor> {

    private AdaptadorQuip adaptador;
    private PresentadorQuip presentador;
    private DrawerLayout drawerLayout;
    private NavigationView navegador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_quip_2);
        setContentView(R.layout.navigation_drawer);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_listas);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.mipmap.ic_navigation_drawer);

        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navegador = (NavigationView) findViewById(R.id.nav_view);

        presentador = new PresentadorQuip(this);

        final RecyclerView lv = (RecyclerView) findViewById(R.id.lvListaNotas);
        adaptador = new AdaptadorQuip(this);
        lv.setAdapter(adaptador);
        lv.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false));


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = lv.getChildAdapterPosition(v);

                int tipo = adaptador.getType(position);

                if ( tipo == 0 ) {

                    presentador.onEditNota(position);
                }
                else
                {
                    presentador.onEditLista(position);
                }
            }
        });

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int position = lv.getChildAdapterPosition(v);

                int tipo = adaptador.getType(position);

                if ( tipo == 0 ) {

                    presentador.onShowBorrarNota(position);
                }
                else
                {
                    presentador.onShowBorrarLista(position);
                }

                return true;
            }
        });

        navegador.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id= item.getItemId();

                switch(id){
                    case R.id.item_notas:{
                        presentador.onAddNota();
                    }
                    break;

                    case R.id.item_listas:{
                        presentador.onAddLista();
                    }

                }
                return false;
            }
        });






        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
            }
        });*/
    }







    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }



    @Override
    public void mostrarAgregarNota() {
        Toast.makeText(VistaQuip.this, "add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        adaptador.changeCursor(c);
    }

    @Override
    public void mostrarEditarNota(Nota n) {
        Toast.makeText(VistaQuip.this, "edit", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable("nota", n);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void mostrarConfirmarBorrarNota(Nota n) {
        DialogoBorrarNota fragmentBorrar = DialogoBorrarNota.newInstance(n);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");

    }

    @Override
    public void mostrarAgregarLista() {

        Toast.makeText(VistaQuip.this, "add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaListas.class);
        startActivity(i);
    }

    @Override
    public void mostrarEditarLista(Lista l) {

        Toast.makeText(VistaQuip.this, "edit", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaListas.class);

        Bundle b = new Bundle();
        b.putParcelable("lista", l);

        i.putExtras(b);
        startActivity(i);

    }

    @Override
    public void mostrarConfirmarBorrarLista(Lista l) {

        DialogoBorrarLista fragment = DialogoBorrarLista.newInstance(l);
        fragment.show( getSupportFragmentManager(), "Dialogo borrar");
    }


    @Override
    public void onBorrarPossitiveButtonClick(Nota n) {
        presentador.onDeleteNota(n);
    }

    @Override
    public void onBorrarPossitiveButtonClick(Lista l) {
        presentador.onDeleteLista(l);
    }


    @Override
    public void onBorrarNegativeButtonClick() {

    }

    @Override
    //metodo  para inflar el menu
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        //getMenuInflater().inflate(R.menu.menu_drawer , menu);


        return true;
    }

    @Override
    //menu para controlar e insertar codigo a los items
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id) {

            case R.id.action_camara : {
                return true;
            }

            case R.id.hacer_nota : {

                presentador.onAddNota();
                return true;
            }

            case R.id.hacer_lista:{
                presentador.onAddLista();

                return true;
            }

            case android.R.id.home: {
                if (drawerLayout.isDrawerOpen(navegador)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer( navegador);
                }
                return true;
            }


    }

        return false;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader c = new CursorLoader( this, ContratoBaseDatos.URI_TABLA_NOTA, null, null, null, null);

        return c;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adaptador.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adaptador.changeCursor(null);
    }





}//fin class