package com.hazmeparo.Login.Interface;

public interface LoginView {

    void showProgress();
    void hideProgress();

    void setErrorUser();
    void setErrorPassWord();

    void loginSuccess();
    void loginError();

    void verificarSesion();

    void showMsg(String msg);
}
