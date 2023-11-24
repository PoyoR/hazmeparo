package com.hazmeparo.Registro.Interface;

import java.util.List;

import Models.Lugar;

public interface RegistroPresenter {

    void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass);

    void setDireccionError();
    void setNumeroError();
    void setPassError();

    void registroError();
    void registroSuccess();

    void goToLogin();

    void getEstados();
    void setEstados(List<Lugar> estados);

    void getMunicipios(String estado);
    void setMunicipios(List<Lugar> municipios);
}
