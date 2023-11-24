package com.hazmeparo.Propuestas.Presenter;

import com.google.firebase.database.DataSnapshot;
import com.hazmeparo.Propuestas.Interactor.PropuestasInteractorImpl;
import com.hazmeparo.Propuestas.Interactor.PropuestasServicioInteractorImpl;
import com.hazmeparo.Propuestas.Interface.PropuestasInteractor;
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioInteractor;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioPresenter;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioView;
import com.hazmeparo.Propuestas.Interface.PropuestasView;

import Clases.PrefsManager;

public class PropuestasServicioPresenterImpl implements PropuestasServicioPresenter {

    PropuestasServicioInteractor interactor;
    PropuestasServicioView view;

    public PropuestasServicioPresenterImpl(PropuestasServicioView view, PrefsManager manager) {
        this.view = view;

        interactor = new PropuestasServicioInteractorImpl(this, manager);
    }

    @Override
    public void showProgress() {
        if (view != null)
            view.showProgress();
    }

    @Override
    public void hideProgress() {
        if (view != null)
            view.hideProgress();
    }

    @Override
    public void getPropuestas(String favor, String categoria) {
        if (view != null){
            view.showProgress();
            interactor.getPropuestas(favor, categoria);
        }
    }

    @Override
    public void setPropuestas(DataSnapshot propuestas) {
        if (view != null){
            view.hideProgress();
            view.setPropuestas(propuestas);
        }
    }

    @Override
    public void aceptarPropuesta(String propuesta, String compi, String favor) {
        if (view != null)
            view.showProgress();
        interactor.aceptarPropuesta(propuesta, compi, favor);
    }


    @Override
    public void showDialog(String msg, String compi, String key) {
        if (view != null){
            view.showDialog(msg, compi, key);
        }
    }

    @Override
    public void showMsg(String msg) {
        if (view != null){
            view.hideProgress();
            view.showMsg(msg);
        }
    }
}
