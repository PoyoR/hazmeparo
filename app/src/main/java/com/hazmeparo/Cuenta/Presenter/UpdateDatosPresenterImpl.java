package com.hazmeparo.Cuenta.Presenter;

import com.hazmeparo.Cuenta.Interactor.UpdateDatosInteractorImpl;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosInteractor;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosPresenter;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosView;

import java.util.List;

import Clases.PrefsManager;
import Models.Lugar;

public class UpdateDatosPresenterImpl implements UpdateDatosPresenter {

    UpdateDatosView view;
    PrefsManager manager;
    UpdateDatosInteractor interactor;

    public UpdateDatosPresenterImpl(UpdateDatosView view, PrefsManager manager) {
        this.view = view;
        this.manager = manager;
        interactor = new UpdateDatosInteractorImpl(this, manager);
    }

    @Override
    public void showMsg(String msg) {
        if (view != null){
            view.hideProgress();
            view.showMsg(msg);
        }
    }

    @Override
    public void setNombreError() {
        if (view != null){
            view.hideProgress();
            view.setNombreError();
        }
    }

    @Override
    public void setApellidosError() {
        if (view != null){
            view.hideProgress();
            view.setApellidosError();
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
    public void setNumeroError() {

    }

    @Override
    public void setPassError() {

    }

    @Override
    public void setCorreoError() {
        if (view != null){
            view.hideProgress();
            view.setCorreoError();
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

    @Override
    public void updateDatos(String nombre, String apellido, String estado, String municipio, String direccion, String correo) {
        if (view != null)
            view.showProgress();
        interactor.updateDatos(nombre, apellido, estado, municipio, direccion, correo);
    }

    @Override
    public void updatePass(String pass1, String pass2) {
        if (view != null)
            view.showProgress();

        interactor.updatePass(pass1, pass2);
    }
}
