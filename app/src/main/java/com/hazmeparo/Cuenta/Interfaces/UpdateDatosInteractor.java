package com.hazmeparo.Cuenta.Interfaces;

public interface UpdateDatosInteractor {

    void getEstados();
    void getMunicipios(String estado);

    void updateDatos(String nombre, String apellido, String estado, String municipio, String direccion, String correo);

    void updatePass(String pass1, String pass2);
}
