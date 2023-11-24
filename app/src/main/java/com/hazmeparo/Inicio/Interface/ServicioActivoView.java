package com.hazmeparo.Inicio.Interface;

public interface ServicioActivoView {

    void showMsg(String msg);
    void setServicio(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String motivo_cancelacion, String codigo);
    void setCompiElegido(String compiId, String nombre, String calif, String numero, String comentario, String precio);
    void setTotalPropuestas(String total, String status, boolean exist);
    void codigoFinalizacionIncorrecto();

    void goToInicio();
}
