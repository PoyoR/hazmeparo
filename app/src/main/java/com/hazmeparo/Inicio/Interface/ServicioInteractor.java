package com.hazmeparo.Inicio.Interface;

import android.net.Uri;

public interface ServicioInteractor {

    void solicitarFavor(String usuario, String token, String nom_usuario, String categoria, String titulo, String descripcion, String hora, String direccion, String path, Uri uri, String ubicacion);
}
