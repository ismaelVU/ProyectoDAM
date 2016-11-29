package com.izv.dam.newquip.vistas.listas;

import android.content.Context;

import com.izv.dam.newquip.contrato.ContratoListas;
import com.izv.dam.newquip.pojo.Lista;

/**
 * Created by dam on 20/10/2016.
 */

public class PresentadorLista implements ContratoListas.InterfacePresentador {
    private ContratoListas.InterfaceModelo modelo;
    private ContratoListas.InterfaceVista vista;

    public PresentadorLista(ContratoListas.InterfaceVista vista) {
        this.vista = vista;
        this.modelo = new ModeloLista((Context)vista);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public long onSaveListas(Lista n) {

         return this.modelo.saveListas(n);
    }
}
