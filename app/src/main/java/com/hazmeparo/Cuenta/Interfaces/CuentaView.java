package com.hazmeparo.Cuenta.Interfaces;

import android.net.Uri;

import java.util.List;

import Models.Calificacion;

public interface CuentaView {

    void showProgress();
    void hideProgress();

    void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto);

    void fotoActualizada(Uri uri);

    void showMsg(String msg);

    void setOpiniones(List<Calificacion> opiniones);

    void setStatus(String status);
}
