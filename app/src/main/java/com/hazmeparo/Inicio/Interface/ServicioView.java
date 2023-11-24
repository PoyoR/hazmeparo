package com.hazmeparo.Inicio.Interface;

public interface ServicioView {

    void showDialog(String msg, boolean correcto);

    void showProgress();
    void hideProgress();
    void showMsg(String msg);

    void setTituloError();
    void setDescripcionError();
    void setHoraError();
    void setDireccionError();

    void goToServicioActivo();
}
