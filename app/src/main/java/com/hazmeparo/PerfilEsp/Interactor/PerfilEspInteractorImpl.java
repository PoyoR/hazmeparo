package com.hazmeparo.PerfilEsp.Interactor;

import com.google.firebase.database.DatabaseReference;
import com.hazmeparo.PerfilEsp.Interfaces.PerfilEspInteractor;
import com.hazmeparo.PerfilEsp.Interfaces.PerfilEspPresenter;

import java.util.List;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Calificacion;
import Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilEspInteractorImpl implements PerfilEspInteractor {

    PerfilEspPresenter presenter;
    private ApiInterface api;
    DatabaseReference dbReference;
    PrefsManager manager;

    public PerfilEspInteractorImpl(PerfilEspPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;

        api = ApiClient.getClient().create(ApiInterface.class);
    }


    @Override
    public void getDatos(String username) {
        Call<User> call = api.getUserInfo("/a/usuario_get/"+username+"/", "Token " + manager.obtenerValorString("token"));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    User user = response.body();
                    presenter.setDatos(user.getFirstName() + " " + user.getLastName(),
                            user.getUsuario().getCalificacion_especialista(), user.getUsername(), user.getEmail(),
                            user.getUsuario().getDireccion(),
                            user.getUsuario().getEstado() + ", " + user.getUsuario().getCiudad(),
                            user.getUsuario().getFoto());
                }else{
                    presenter.showMsg("Ocurrió un error al recuperar datos del usuario, por favor reintenta");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                presenter.showMsg("Ocurrió un error al recuperar datos del usuario, por favor reintenta");
            }
        });
    }

    @Override
    public void getOpiniones(String username) {
        Call<List<Calificacion>> call = api.getOpinionesUsuario("/a/calif_usuario_list/?usuario="+username+"&especialista=true", "Token " + manager.obtenerValorString("token"));

        call.enqueue(new Callback<List<Calificacion>>() {
            @Override
            public void onResponse(Call<List<Calificacion>> call, Response<List<Calificacion>> response) {

                if (response.isSuccessful()){
                    presenter.setOpiniones(response.body());
                }else{
                    /*try {
                        Log.i("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }

            @Override
            public void onFailure(Call<List<Calificacion>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getStatus() {

    }
}
