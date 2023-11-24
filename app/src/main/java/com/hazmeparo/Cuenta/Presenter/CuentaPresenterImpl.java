package com.hazmeparo.Cuenta.Presenter;

import android.net.Uri;

import com.hazmeparo.Cuenta.Interactor.CuentaInteractorImpl;
import com.hazmeparo.Cuenta.Interfaces.CuentaInteractor;
import com.hazmeparo.Cuenta.Interfaces.CuentaPresenter;
import com.hazmeparo.Cuenta.Interfaces.CuentaView;

import java.util.List;

import Clases.PrefsManager;
import Models.Calificacion;

public class CuentaPresenterImpl implements CuentaPresenter {

    CuentaView view;
    CuentaInteractor interactor;

    public CuentaPresenterImpl(CuentaView view, PrefsManager manager) {
        this.view = view;
        interactor = new CuentaInteractorImpl(this, manager);
    }

    @Override
    public void showProgress() {
        if (view!= null)
            view.showProgress();
    }

    @Override
    public void hideProgress() {
        if (view != null)
            view.hideProgress();
    }

    @Override
    public void getDatos(String username) {
        if (view != null)
            view.showProgress();
        interactor.getDatos(username);
    }

    @Override
    public void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto) {
        if (view != null){
            view.hideProgress();
            view.setDatos(nombre, calif, numero, correo, direccion, estadoCIudad, foto);
        }
    }

    @Override
    public void updateFoto(String path, Uri uri) {
        if (view != null){
            view.showProgress();
        }
        interactor.updateFoto(path, uri);
    }

    @Override
    public void fotoActualizada(Uri uri) {
        if (view != null)
            view.fotoActualizada(uri);
    }

    @Override
    public void showMsg(String msg) {
        if (view != null){
            view.hideProgress();
            view.showMsg(msg);
        }
    }

    @Override
    public void getOpiniones() {
        interactor.getOpiniones();
    }

    @Override
    public void setOpiniones(List<Calificacion> opiniones) {
        if (view != null)
            view.setOpiniones(opiniones);
    }

    @Override
    public void getStatus() {
        interactor.getStatus();
    }

    @Override
    public void setStatus(String status) {
        if (view != null)
            view.setStatus(status);
    }
}
