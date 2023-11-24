package com.hazmeparo.Inicio.Presenter;

import android.net.Uri;

import com.hazmeparo.Inicio.Interactor.ServicioInteractorImpl;
import com.hazmeparo.Inicio.Interface.ServicioInteractor;
import com.hazmeparo.Inicio.Interface.ServicioPresenter;
import com.hazmeparo.Inicio.Interface.ServicioView;

import Clases.PrefsManager;

public class ServicioPresenterImpl implements ServicioPresenter {

    ServicioView view;
    ServicioInteractor interactor;

    public ServicioPresenterImpl(ServicioView view, PrefsManager manager) {
        this.view = view;
        interactor = new ServicioInteractorImpl(this, manager);
    }

    @Override
    public void showDialog(String msg, boolean correcto) {
        if (view != null){
            view.hideProgress();
            view.showDialog(msg, correcto);
        }
    }

    @Override
    public void setTituloError() {
        if (view != null){
            view.hideProgress();
            view.setTituloError();
        }
    }

    @Override
    public void setDescripcionError() {
        if (view != null){
            view.hideProgress();
            view.setDescripcionError();
        }
    }

    @Override
    public void setHoraError() {
        if (view != null){
            view.hideProgress();
            view.setHoraError();
        }
    }

    @Override
    public void setDireccionError() {
        if (view != null){
            view.hideProgress();
            view.setDireccionError();
        }
    }

    @Override
    public void solicitarFavor(String usuario, String token, String nom_usuario, String categoria, String titulo, String descripcion, String hora, String direccion, String path, Uri uri, String ubicacion) {
        if (view != null)
            view.showProgress();

        interactor.solicitarFavor(usuario, token, nom_usuario, categoria, titulo, descripcion, hora, direccion, path, uri, ubicacion);
    }

    @Override
    public void goToServicioActivo() {
        if (view != null)
            view.goToServicioActivo();
    }
}
