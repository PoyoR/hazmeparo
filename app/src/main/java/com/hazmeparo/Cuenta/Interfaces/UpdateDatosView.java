package com.hazmeparo.Cuenta.Interfaces;

import java.util.List;

import Models.Lugar;

public interface UpdateDatosView {

    void showProgress();
    void hideProgress();

    void showMsg(String msg);

    void setNombreError();
    void setApellidosError();
    void setDireccionError();
    //void setNumeroError();
    void setCorreoError();

    void setEstados(List<Lugar> estados);
    void setMunicipios(List<Lugar> municipios);
}
