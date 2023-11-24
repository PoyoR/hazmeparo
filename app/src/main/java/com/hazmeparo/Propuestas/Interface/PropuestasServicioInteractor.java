package com.hazmeparo.Propuestas.Interface;

public interface PropuestasServicioInteractor {

    void getPropuestas(String favor, String categoria);

    void aceptarPropuesta(String propuesta, String compi, String favor);
}
