package com.hazmeparo.Propuestas.Interface;

import com.google.firebase.database.DataSnapshot;

public interface PropuestasServicioView {

    void showProgress();
    void hideProgress();

    void showDialog(String msg, String compi, String key);
    void showMsg(String msg);

    void setPropuestas(DataSnapshot propuestas);
}
