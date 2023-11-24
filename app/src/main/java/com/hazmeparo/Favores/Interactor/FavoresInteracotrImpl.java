package com.hazmeparo.Favores.Interactor;

import android.util.Log;

import com.hazmeparo.Favores.Interfaces.FavoresInteractor;
import com.hazmeparo.Favores.Interfaces.FavoresPresenter;

import java.io.IOException;
import java.util.List;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Favor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoresInteracotrImpl implements FavoresInteractor {

    FavoresPresenter presenter;
    PrefsManager manager;
    private ApiInterface api;

    public FavoresInteracotrImpl(FavoresPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        api = ApiClient.getClient().create(ApiInterface.class);
    }


    @Override
    public void getFavores(String usuario) {

        Call<List<Favor>> call = api.getFavores("Token "+usuario);

        call.enqueue(new Callback<List<Favor>>() {
            @Override
            public void onResponse(Call<List<Favor>> call, Response<List<Favor>> response) {
                if (response.isSuccessful()){
                    presenter.setFavores(response.body());
                }else{
                    presenter.showMsg("Ocurrió un error al recuperar los datos, por favor reintenta");
                    /*try {
                        Log.i("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }

            @Override
            public void onFailure(Call<List<Favor>> call, Throwable t) {
                //Log.i("Favores", t.getMessage());
                presenter.showMsg("Ocurrió un error al recuperar los datos, por favor reintenta");
            }
        });
    }
}
