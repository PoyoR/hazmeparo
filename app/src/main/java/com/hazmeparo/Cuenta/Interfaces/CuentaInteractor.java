package com.hazmeparo.Cuenta.Interfaces;

import android.net.Uri;

public interface CuentaInteractor {

    void getDatos(String username);

    void updateFoto(String path, Uri uri);

    void getOpiniones();

    void getStatus();
}
