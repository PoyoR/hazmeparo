package com.hazmeparo.Cuenta.Interfaces;

import android.net.Uri;

import java.util.List;

import Models.Calificacion;

public interface CuentaPresenter {

    void showProgress();
    void hideProgress();

    void getDatos(String username);
    void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto);

    void updateFoto(String path, Uri uri);
    void fotoActualizada(Uri uri);

    void showMsg(String msg);

    void getOpiniones();
    void setOpiniones(List<Calificacion> opiniones);

    void getStatus();
    void setStatus(String status);
}
