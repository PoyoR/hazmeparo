package com.hazmeparo.Favores.Presenter;

import com.hazmeparo.Favores.Interactor.FavoresInteracotrImpl;
import com.hazmeparo.Favores.Interfaces.FavoresInteractor;
import com.hazmeparo.Favores.Interfaces.FavoresPresenter;
import com.hazmeparo.Favores.Interfaces.FavoresView;

import java.util.List;

import Clases.PrefsManager;
import Models.Favor;

public class FavoresPresenterImpl implements FavoresPresenter {

    FavoresView view;
    FavoresInteractor interactor;

    public FavoresPresenterImpl(FavoresView view, PrefsManager manager) {
        this.view = view;

        interactor = new FavoresInteracotrImpl(this, manager);
    }

    @Override
    public void getFavores(String usuario) {
        if (view != null)
            view.showProgress();
        interactor.getFavores(usuario);
    }

    @Override
    public void setFavores(List<Favor> favores) {
        if (view != null) {
            view.hideProgress();
            view.setFavores(favores);
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
