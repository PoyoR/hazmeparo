package com.hazmeparo.Inicio.Presenter;

import com.hazmeparo.Inicio.Interactor.ServicioActivoInteractorImpl;
import com.hazmeparo.Inicio.Interface.ServicioActivoInteractor;
import com.hazmeparo.Inicio.Interface.ServicioActivoPresenter;
import com.hazmeparo.Inicio.Interface.ServicioActivoView;

import Clases.PrefsManager;

public class ServicioActivoPresenterImpl implements ServicioActivoPresenter {

    ServicioActivoView view;
    ServicioActivoInteractor interactor;

    public ServicioActivoPresenterImpl(ServicioActivoView view, PrefsManager manager) {
        this.view = view;
        interactor = new ServicioActivoInteractorImpl(this, manager);
    }

    @Override
    public void showMsg(String msg) {
        if (view != null)
            view.showMsg(msg);
    }

    @Override
    public void getServicio(String userId, String categoria) {
        interactor.getServicio(userId, categoria);
    }

    @Override
    public void getCompiElegido(String servicio) {
        interactor.getCompiElegido(servicio);
    }

    @Override
    public void setCompiElegido(String compiId, String nombre, String calif, String numero, String comentario, String precio) {
        if (view != null)
            view.setCompiElegido(compiId, nombre, calif, numero, comentario, precio);
    }

    @Override
    public void setServicio(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String motivo_cancelacion, String codigo) {
        if (view != null){
            view.setServicio(key, status, titulo, descripcion, hora, direccion, img, motivo_cancelacion, codigo);
        }
    }

    @Override
    public void setTotalPropuestas(String total, String status, boolean exist) {
        if (view != null){
            view.setTotalPropuestas(total, status, exist);
        }
    }

    @Override
    public void finalizarServicio(String favor) {
        interactor.finalizarServicio(favor);
    }

    @Override
    public void cancelarServicio(String favor) {
        interactor.cancelarServicio(favor);
    }

    @Override
    public void cancelarServicioActivo(String favor, String motivo) {
        interactor.cancelarServicioActivo(favor, motivo);
    }

    @Override
    public void finalizarServicioIniciado(String favor) {
        interactor.finalizarServicioIniciado(favor);
    }

    @Override
    public void enviarCalificacionCompi(float calif, String comentario, String compiId) {
        interactor.enviarCalificacionCompi(calif, comentario, compiId);
    }

    @Override
    public void verificarCodigoFinalizacion(String favor, String codigo) {
        interactor.verificarCodigoFinalizacion(favor, codigo);
    }

    @Override
    public void codigoFinalizacionIncorrecto() {
        if (view != null)
            view.codigoFinalizacionIncorrecto();
    }

    @Override
    public void terminarFavor(String favor) {
        interactor.terminarFavor(favor);
    }

    @Override
    public void goToInicio() {
        if (view != null)
            view.goToInicio();
    }
}
