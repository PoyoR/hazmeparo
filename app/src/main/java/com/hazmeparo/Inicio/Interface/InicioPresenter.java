package com.hazmeparo.Inicio.Interface;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public interface InicioPresenter {

    void showProgress();
    void hideProgress();
    void showDialog(String msg, boolean correcto);

    void setTituloError();
    void setDescripcionError();
    void setHoraError();
    void setDireccionError();

    void favorPublicado();

    void getFavores(String userId);
    void getCompiElegido(String favor);
    void setCompiElegido(String compiId, String nombre, String calif, String numero, String transporte, String pago, String tiempo, String precio);
    void setFavor(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String msg_compi, String motivo_cancelacion, String codigo);

    void mostrarLayout(String layout);

    void setTotalPropuestas(String total, String status, boolean exist);

    void publicarFavor(String usuario, String token, String nom_usuario, String titulo, String descripcion, String hora, String direccionCompra, String direccion, String path, Uri uri,
                       String ubicacion, String ubicacion2);

    void terminarFavor(String favor);

    void showMsg(String msg);

    void finalizarFavor(String favor);
    void finalizarFavorIniciado(String favor);
    void cancelarFavor(String favor);
    void cancelarFavorActivo(String favor, String motivo);

    void enviarCalificacionCompi(float calif, String comentario, String compiId);

    void crearNotificacion();

    void getPosicionCompi(String favor);
    void setPosicionCompi(String posicion);

    void verificarCodigoFinalizacion(String favor, String codigo);
    void codigoFinalizacionIncorrecto();

    void goToSolicitar();
}
