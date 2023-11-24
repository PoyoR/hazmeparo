package com.hazmeparo.Inicio.Interface;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public interface InicioInteractor {

    void getFavores(String userId);

    void getPropuestas(String key);
    void getCompiElegido(String favor);

    void publicarFavor(String usuario, String token, String nom_usuario, String titulo, String descripcion, String hora, String direccionCompra, String direccion,  String path, Uri uri, String ubicacion, String ubicacion2);

    void terminarFavor(String favor);

    void finalizarFavor(String favor);
    void finalizarFavorIniciado(String favor);
    void cancelarFavor(String favor);
    void cancelarFavorActivo(String favor, String motivo);

    void verificarCodigoFinalizacion(String favor, String codigo);

    void enviarCalificacionCompi(float calif, String comentario, String compiId);

    void getPosicionCompi(String favor);
}
