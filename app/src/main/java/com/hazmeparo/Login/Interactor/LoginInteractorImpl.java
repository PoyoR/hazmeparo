package com.hazmeparo.Login.Interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.hazmeparo.Login.Interface.LoginInteractor;
import com.hazmeparo.Login.Interface.LoginPresenter;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.User;
import Models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractorImpl implements LoginInteractor {

    private LoginPresenter presenter;
    private ApiInterface api;
    private PrefsManager manager;

    public LoginInteractorImpl(LoginPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void login(String usuario, String pass) {

        if (!usuario.equals("") && !pass.equals("")){

            Call<User> call = api.login(usuario, pass);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()){
                        Log.i("RESP", new Gson().toJson(response.body()));
                        User usuario = response.body();

                        if (usuario.isActivo()){
                            manager.guardarValorBoolean("logueado", true);
                            manager.guardarValorString("id_usuario", usuario.getId());
                            manager.guardarValorString("nom_usuario", usuario.getFirstName());
                            manager.guardarValorString("apellidos_usuario", usuario.getLastName());
                            manager.guardarValorString("correo_usuario", usuario.getEmail());
                            manager.guardarValorString("username", usuario.getUsername());
                            manager.guardarValorString("estado", usuario.getEstado());
                            manager.guardarValorString("municipio", usuario.getMunicipio());
                            manager.guardarValorString("direccion", usuario.getDireccion());
                            manager.guardarValorString("token", usuario.getToken());

                            presenter.loginSuccess();
                        }else{
                            presenter.showMsg("Usuario deshabilitado \n\n" + "\"" + usuario.getRazon() + "\"");
                        }

                    }else{
                        presenter.loginError();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("ERROR", "ocurrio un error");
                    Log.i("Error", t.getMessage());

                }
            });

        }else{

            if (usuario.equals("")){
                presenter.setErrorUsuario();
            }

            if (pass.equals("")){
                presenter.setErrorPass();
            }
        }
    }
}
