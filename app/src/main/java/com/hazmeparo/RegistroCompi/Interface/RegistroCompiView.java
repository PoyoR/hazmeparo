package com.hazmeparo.RegistroCompi.Interface;

import java.util.List;

import Models.Lugar;

public interface RegistroCompiView {

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
