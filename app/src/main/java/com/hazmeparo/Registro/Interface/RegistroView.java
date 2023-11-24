package com.hazmeparo.Registro.Interface;

import java.util.List;

import Models.Lugar;

public interface RegistroView {

    void showProgress();
    void hideProgress();

    void setDireccionError();
    void setNumeroError();
    void setPassError();

    void registroError();
    void registroSuccess();

    void goToLogin();

    void setEstados(List<Lugar> estados);
    void setMunicipios(List<Lugar> municipios);
}
