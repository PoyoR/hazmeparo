package com.hazmeparo.Inicio.Interface;

import android.net.Uri;

public interface InicioView {

    void showProgress();
    void hideProgress();
    void showDialog(String msg, boolean correcto);

    void setTituloError();
    void setDescripcionError();
    void setHoraError();
    void setDireccionError();

    void setTotalPropuestas(String total,  String status, boolean exist);

    void favorPublicado();

    void setFavor(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String msg_compi, String motivo_cancelacion, String codigo);

    void mostrarLayout(String layout);

    void setCompiElegido(String compiId, String nombre, String calif, String numero, String transporte, String pago, String tiempo, String precio);

    void getDireccion();

    void showMsg(String msg);

    void crearNotificacion();

    void setPosicionCompi(String posicion);

    void codigoFinalizacionIncorrecto();

    void goToSolicitar();
}
