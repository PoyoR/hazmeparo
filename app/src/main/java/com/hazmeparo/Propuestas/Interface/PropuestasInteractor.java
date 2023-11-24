package com.hazmeparo.Propuestas.Interface;

public interface PropuestasInteractor {

    void getPropuestas(String favor);

    void aceptarPropuesta(String propuesta, String compi, String favor);
}
