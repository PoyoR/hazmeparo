package com.hazmeparo.Inicio.Presenter;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.hazmeparo.Inicio.Interactor.InicioInteractorImpl;
import com.hazmeparo.Inicio.Interface.InicioInteractor;
import com.hazmeparo.Inicio.Interface.InicioPresenter;
import com.hazmeparo.Inicio.Interface.InicioView;

import Clases.PrefsManager;

public class InicioPresenterImpl implements InicioPresenter {

    private InicioView view;
    private InicioInteractor interactor;

    public InicioPresenterImpl(InicioView view, PrefsManager manager) {
        this.view = view;

        interactor = new InicioInteractorImpl(this, manager);
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
    public void favorPublicado() {
        if (view != null)
            view.favorPublicado();

    }

    @Override
    public void getFavores(String userId) {
        interactor.getFavores(userId);
    }

    @Override
    public void getCompiElegido(String favor) {
        interactor.getCompiElegido(favor);
    }

    @Override
    public void setCompiElegido(String compiId, String nombre, String calif, String numero, String transporte, String pago, String tiempo, String precio) {
        if (view != null)
            view.setCompiElegido(compiId, nombre, calif, numero, transporte, pago, tiempo, precio);
    }

    @Override
    public void setFavor(String key,String status, String titulo, String descripcion, String hora, String direccion, String img, String msg_compi, String motivo_cancelacion, String codigo) {
        if (view != null)
            view.setFavor(key, status, titulo, descripcion, hora, direccion, img, msg_compi, motivo_cancelacion, codigo);
    }

    @Override
    public void mostrarLayout(String layout) {
        if (view != null)
            view.mostrarLayout(layout);
    }

    @Override
    public void setTotalPropuestas(String total, String status, boolean exist) {
        if (view != null)
            view.setTotalPropuestas(total, status, exist);
    }

    @Override
    public void publicarFavor(String usuario, String token, String nom_usuario, String titulo, String descripcion, String hora, String direccionCompra, String direccion,  String path,
                              Uri uri, String ubicacion, String ubicacion2) {
        if (view != null)
            view.showProgress();

        interactor.publicarFavor(usuario, token, nom_usuario, titulo, descripcion, hora, direccionCompra, direccion, path, uri, ubicacion, ubicacion2);
    }

    @Override
    public void terminarFavor(String favor) {
        if (view != null)
            view.showProgress();
        interactor.terminarFavor(favor);
    }

    @Override
    public void showMsg(String msg) {
        if (view != null){
            view.hideProgress();
            view.showMsg(msg);
        }
    }

    @Override
    public void finalizarFavor(String favor) {
        interactor.finalizarFavor(favor);
    }

    @Override
    public void finalizarFavorIniciado(String favor) {
        interactor.finalizarFavorIniciado(favor);
    }

    @Override
    public void cancelarFavor(String favor) {
        if (view != null)
            view.showProgress();
        interactor.cancelarFavor(favor);
    }

    @Override
    public void cancelarFavorActivo(String favor, String motivo) {
        if (view != null)
            view.showProgress();
        interactor.cancelarFavorActivo(favor, motivo);
    }

    @Override
    public void enviarCalificacionCompi(float calif, String comentario, String compiId) {
        interactor.enviarCalificacionCompi(calif, comentario, compiId);
    }

    @Override
    public void crearNotificacion() {
        if (view != null)
            view.crearNotificacion();
    }

    @Override
    public void getPosicionCompi(String favor) {
        interactor.getPosicionCompi(favor);
    }

    @Override
    public void setPosicionCompi(String posicion) {
        if (view != null)
            view.setPosicionCompi(posicion);
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
    public void goToSolicitar() {
        if (view != null)
            view.goToSolicitar();
    }
}
