package com.hazmeparo.Perfil.Interfaces;

import java.util.List;

import Models.Calificacion;

public interface PerfilView {

    void showProgress();
    void hideProgress();

    void showMsg(String msg);

    void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto);

    void setOpiniones(List<Calificacion> opiniones);

    void setStatus(String status);
}
