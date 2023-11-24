package com.hazmeparo.Login.Presenter;

import com.hazmeparo.Login.Interactor.LoginInteractorImpl;
import com.hazmeparo.Login.Interface.LoginInteractor;
import com.hazmeparo.Login.Interface.LoginPresenter;
import com.hazmeparo.Login.Interface.LoginView;

import Clases.PrefsManager;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;
    private LoginInteractor interactor;

    public LoginPresenterImpl(LoginView view, PrefsManager manager) {
        this.view = view;
        interactor = new LoginInteractorImpl(this, manager);
    }

    @Override
    public void login(String usuario, String pass) {
        if (view != null)
            view.showProgress();

        interactor.login(usuario, pass);
    }

    @Override
    public void loginSuccess() {
        if (view != null){
            view.hideProgress();
            view.loginSuccess();
        }
    }

    @Override
    public void loginError() {
        if (view != null){
            view.hideProgress();
            view.loginError();
        }
    }

    @Override
    public void setErrorUsuario() {
        if (view != null){
            view.hideProgress();
            view.setErrorUser();
        }

    }

    @Override
    public void setErrorPass() {
        if (view != null){
            view.hideProgress();
            view.setErrorPassWord();
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
