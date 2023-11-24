package com.hazmeparo.Propuestas.Interface;

import com.google.firebase.database.DataSnapshot;

public interface PropuestasServicioPresenter {

    void showProgress();
    void hideProgress();
    void getPropuestas(String favor, String categoria);
    void setPropuestas(DataSnapshot propuestas);

    void aceptarPropuesta(String propuesta, String compi, String favor);

    void showDialog(String msg, String compi, String key);
    void showMsg(String msg);
}
