package com.hazmeparo.Favores.Interfaces;

import java.util.List;

import Models.Favor;

public interface FavoresView {

    void showProgress();
    void hideProgress();

    void setFavores(List<Favor> favores);

    void showMsg(String msg);
}
