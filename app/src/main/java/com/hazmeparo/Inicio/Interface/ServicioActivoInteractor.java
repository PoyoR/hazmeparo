package com.hazmeparo.Inicio.Interface;

public interface ServicioActivoInteractor {

    void getServicio(String userId, String categoria);

    void getCompiElegido(String servicio);

    void finalizarServicio(String favor);
    void cancelarServicio(String favor);

    void cancelarServicioActivo(String favor, String motivo);

    void finalizarServicioIniciado(String favor);

    void enviarCalificacionCompi(float calif, String comentario, String compiId);

    void verificarCodigoFinalizacion(String favor, String codigo);
    void terminarFavor(String favor);
}
