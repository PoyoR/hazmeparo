package com.hazmeparo.PerfilEsp.Presenter;

import com.hazmeparo.PerfilEsp.Interactor.PerfilEspInteractorImpl;
import com.hazmeparo.PerfilEsp.Interfaces.PerfilEspInteractor;
import com.hazmeparo.PerfilEsp.Interfaces.PerfilEspPresenter;
import com.hazmeparo.PerfilEsp.Interfaces.PerfilEspView;

import java.util.List;

import Clases.PrefsManager;
import Models.Calificacion;

public class PerfilEspPresenterImpl implements PerfilEspPresenter {

    PerfilEspView view;
    PerfilEspInteractor interactor;

    public PerfilEspPresenterImpl(PerfilEspView view, PrefsManager manager) {
        this.view = view;

        interactor = new PerfilEspInteractorImpl(this, manager);
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
    public void showMsg(String msg) {
        if (view != null){
            view.hideProgress();
            view.showMsg(msg);
        }
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
    public void getOpiniones(String username) {
        interactor.getOpiniones(username);
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
