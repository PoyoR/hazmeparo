package com.hazmeparo.Cuenta.Interfaces;

import java.util.List;

import Models.Lugar;

public interface UpdateDatosPresenter {

    void showMsg(String msg);

    void setNombreError();
    void setApellidosError();
    void setDireccionError();
    void setNumeroError();
    void setPassError();
    void setCorreoError();

    void getEstados();
    void setEstados(List<Lugar> estados);

    void getMunicipios(String estado);
    void setMunicipios(List<Lugar> municipios);

    void updateDatos(String nombre, String apellido, String estado, String municipio, String direccion, String correo);

    void updatePass(String pass1, String pass2);
}
