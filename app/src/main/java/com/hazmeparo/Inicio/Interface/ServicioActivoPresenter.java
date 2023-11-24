package com.hazmeparo.Inicio.Interface;

public interface ServicioActivoPresenter {

    void showMsg(String msg);

    void getServicio(String userId, String categoria);

    void getCompiElegido(String servicio);
    void setCompiElegido(String compiId, String nombre, String calif, String numero, String comentario, String precio);

    void setServicio(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String motivo_cancelacion, String codigo);

    void setTotalPropuestas(String total, String status, boolean exist);

    void finalizarServicio(String favor);
    void cancelarServicio(String favor);

    void cancelarServicioActivo(String favor, String motivo);

    void finalizarServicioIniciado(String favor);
    void enviarCalificacionCompi(float calif, String comentario, String compiId);

    void verificarCodigoFinalizacion(String favor, String codigo);
    void codigoFinalizacionIncorrecto();

    void terminarFavor(String favor);

    void goToInicio();


}
