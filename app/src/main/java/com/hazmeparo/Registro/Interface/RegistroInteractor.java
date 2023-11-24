package com.hazmeparo.Registro.Interface;

import java.util.List;

import Models.Lugar;

public interface RegistroInteractor {

    void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass);

    void getEstados();

    void getMunicipios(String estado);
}
