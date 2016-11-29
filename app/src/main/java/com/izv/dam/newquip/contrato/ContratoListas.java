package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Lista;

/**
 * Created by dam on 20/10/2016.
 */

public interface ContratoListas {

    interface InterfaceModelo {

        void close();

        Lista getListas(long id);

        long saveListas(Lista n);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveListas(Lista n);

    }

    interface InterfaceVista {

        void mostrarListas(Lista lista);

    }

}
