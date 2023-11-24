package com.hazmeparo.RegistroCompi.Interface;

public interface RegistroCompiInteractor {

    void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass);

    void getEstados();

    void getMunicipios(String estado);

}
