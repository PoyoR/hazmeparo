package com.hazmeparo.Inicio.Interface;

import android.net.Uri;

public interface ServicioPresenter {

    void showDialog(String msg, boolean correcto);

    void setTituloError();
    void setDescripcionError();
    void setHoraError();
    void setDireccionError();

    void solicitarFavor(String usuario, String token, String nom_usuario, String categoria, String titulo, String descripcion, String hora, String direccion, String path, Uri uri, String ubicacion);

    void goToServicioActivo();
}
