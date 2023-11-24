package com.hazmeparo.Registro.Presenter;

import com.hazmeparo.Registro.Interactor.RegistroInteractorImpl;
import com.hazmeparo.Registro.Interface.RegistroInteractor;
import com.hazmeparo.Registro.Interface.RegistroPresenter;
import com.hazmeparo.Registro.Interface.RegistroView;

import java.util.List;

import Clases.PrefsManager;
import Models.Lugar;

public class RegistroPresenterImpl implements RegistroPresenter {

    private RegistroView view;
    private RegistroInteractor interactor;

    public RegistroPresenterImpl(RegistroView view, PrefsManager manager) {
        this.view = view;

        interactor = new RegistroInteractorImpl(this, manager);
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
