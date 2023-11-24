package com.hazmeparo.Login.Interface;

public interface LoginPresenter {

    void login(String usuario, String pass);

    void loginSuccess();
    void loginError();
    void setErrorUsuario();
    void setErrorPass();

    void showMsg(String msg);
}
