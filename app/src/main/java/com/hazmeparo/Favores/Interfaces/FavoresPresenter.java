package com.hazmeparo.Favores.Interfaces;

import java.util.List;

import Models.Favor;

public interface FavoresPresenter {


    void getFavores(String usuario);
    void setFavores(List<Favor> favores);

    void showMsg(String msg);
}
