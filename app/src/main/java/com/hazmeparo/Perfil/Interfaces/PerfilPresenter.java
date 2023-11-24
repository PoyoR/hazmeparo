package com.hazmeparo.Perfil.Interfaces;

import java.util.List;

import Models.Calificacion;

public interface PerfilPresenter {

    void showProgress();
    void hideProgress();

    void showMsg(String msg);

    void getDatos(String username);
    void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto);

    void getOpiniones(String username);
    void setOpiniones(List<Calificacion> opiniones);

    void getStatus();
    void setStatus(String status);
}
