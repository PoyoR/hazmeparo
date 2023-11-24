package com.hazmeparo.RegistroCompi.Interactor;

import android.util.Log;

import com.hazmeparo.Registro.Interface.RegistroPresenter;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiInteractor;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiPresenter;

import java.util.List;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Lugar;
import Models.User;
import Models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroCompiInteractorImpl implements RegistroCompiInteractor {

    private RegistroCompiPresenter presenter;
    private ApiInterface api;
    private PrefsManager manager;

    public RegistroCompiInteractorImpl(RegistroCompiPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;

        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass) {

        if (!direccion.equals("") && !numero.equals("") && !pass.equals("")){

            User usuario = new User("", numero, "", "", "", pass, "",
                    new Usuario(estado, municipio, "", direccion,"","5", "", "", ""), "", "", "");

            Call<User> call = api.registrarse(usuario);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()){

                        Call<User> login = api.login(numero, pass);
                        login.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()){

                                    User usuario = response.body();

                                    manager.guardarValorBoolean("logueado", true);
                                    manager.guardarValorString("id_usuario", usuario.getId());
                                    manager.guardarValorString("nom_usuario", usuario.getFirstName());
                                    manager.guardarValorString("apellidos_usuario", usuario.getLastName());
                                    manager.guardarValorString("username", usuario.getUsername());
                                    manager.guardarValorString("token", usuario.getToken());

                                    presenter.registroSuccess();

                                }else{

                                }

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });


                    }else{
                        presenter.registroError();
                        /*try {
                            Log.i("ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    presenter.registroError();
                }
            });


        }else{

            if (direccion.equals("")){
                presenter.setDireccionError();
            }

            if (numero.equals("") || numero.length() < 10){
                presenter.setNumeroError();
            }

            if (pass.equals("") || pass.length() < 8){
                presenter.setPassError();
            }
        }
    }

    @Override
    public void getEstados() {
        Call<List<Lugar>> call = api.getEstados();

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                List<Lugar> estados = response.body();

                if (response.isSuccessful()){
                    presenter.setEstados(estados);
                }else{
                    Log.i("Error", "estados");
                }
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {
                Log.i("Error", "estados");
            }
        });

    }

    @Override
    public void getMunicipios(String estado) {
        Call<List<Lugar>> call = api.getMunicipios(estado);

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                List<Lugar> municipios = response.body();

                if (response.isSuccessful()){
                    presenter.setMunicipios(municipios);
                }else{
                    Log.i("Error", "municipios");
                }
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {

            }
        });
    }
}
