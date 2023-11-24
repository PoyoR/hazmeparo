package com.hazmeparo.RegistroCompi.Presenter;

import com.hazmeparo.Registro.Interactor.RegistroInteractorImpl;
import com.hazmeparo.Registro.Interface.RegistroInteractor;
import com.hazmeparo.Registro.Interface.RegistroView;
import com.hazmeparo.RegistroCompi.Interactor.RegistroCompiInteractorImpl;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiInteractor;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiPresenter;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiView;

import java.util.List;

import Clases.PrefsManager;
import Models.Lugar;

public class RegistroCompiPresenterImpl implements RegistroCompiPresenter {

    private RegistroCompiView view;
    private RegistroCompiInteractor interactor;

    public RegistroCompiPresenterImpl(RegistroCompiView view, PrefsManager manager) {
        this.view = view;

        interactor = new RegistroCompiInteractorImpl(this, manager);
    }

    @Override
    public void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass) {
        if (view != null)
            view.showProgress();

        interactor.registrarse(estado, municipio, correo, direccion, numero, pass);
    }

    public void setDireccionError() {
        if (view != null){
            view.hideProgress();
            view.setDireccionError();
        }
    }

    @Override
    public void setNumeroError() {
        if (view != null){
            view.hideProgress();
            view.setNumeroError();
        }

    }

    @Override
    public void setPassError() {
        if (view != null){
            view.hideProgress();
            view.setPassError();
        }
    }

    @Override
    public void registroError() {
        if (view != null){
            view.hideProgress();
            view.registroError();
        }
    }

    @Override
    public void registroSuccess() {
        if (view != null){
            view.hideProgress();
            view.registroSuccess();
        }

    }

    @Override
    public void goToLogin() {
        if (view != null){
            view.hideProgress();
            view.goToLogin();
        }
    }

    @Override
    public void getEstados() {
        interactor.getEstados();
    }

    @Override
    public void setEstados(List<Lugar> estados) {
        if (view != null){
            view.setEstados(estados);
        }
    }

    @Override
    public void getMunicipios(String estado) {
        interactor.getMunicipios(estado);
    }

    @Override
    public void setMunicipios(List<Lugar> municipios) {
        if (view != null)
            view.setMunicipios(municipios);
    }
}
