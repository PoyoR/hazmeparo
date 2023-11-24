package com.hazmeparo.Cuenta.Interactor;

import android.util.Log;

import com.hazmeparo.Cuenta.Interfaces.UpdateDatosInteractor;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosPresenter;

import java.io.IOException;
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

public class UpdateDatosInteractorImpl implements UpdateDatosInteractor {

    UpdateDatosPresenter presenter;
    PrefsManager manager;

    private ApiInterface api;

    public UpdateDatosInteractorImpl(UpdateDatosPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;

        api = ApiClient.getClient().create(ApiInterface.class);
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

    @Override
    public void updateDatos(String nombre, String apellido, String estado, String municipio, String direccion, String correo) {

        if (!nombre.equals("") && !apellido.equals("") && !direccion.equals("") && !correo.equals("")) {

            Usuario usuario = new Usuario(estado, municipio, "", direccion, "", "", "", "", "");
            User user = new User("", "", correo, nombre, apellido, "", "", usuario, "", "", "");

            Call<User> call = api.updateDatos("/a/usuarios/" + manager.obtenerValorString("username") + "/", "Token " + manager.obtenerValorString("token"), user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()){
                        manager.guardarValorString("nom_usuario", nombre);
                        manager.guardarValorString("apellidos_usuario", apellido);
                        manager.guardarValorString("correo_usuario", correo);
                        manager.guardarValorString("estado", estado);
                        manager.guardarValorString("municipio", municipio);
                        manager.guardarValorString("direccion", direccion);
                        presenter.showMsg("Datos actualizados correctamente");
                    }else{
                        presenter.showMsg("Ocurrió un error por favor reintenta");
                        /*try {
                            Log.i("ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    presenter.showMsg("Ocurrió un error por favor reintenta");
                }
            });

        }else{
            if (nombre.equals("")){
                presenter.setNombreError();
            }

            if (apellido.equals("")){
                presenter.setApellidosError();
            }

            if (direccion.equals("")){
                presenter.setDireccionError();
            }

            if (correo.equals("")){
                presenter.setCorreoError();
            }
        }
    }

    @Override
    public void updatePass(String pass1, String pass2) {

        if (!pass1.equals("") && !pass2.equals("")){
            if (pass1.length() >= 8){
                if (pass1.equals(pass2)){
                    Call<User> call = api.updatePass("/a/usuario_pass/" + manager.obtenerValorString("username") + "/", "Token "+ manager.obtenerValorString("token"),
                            pass1);

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            if (response.isSuccessful()){
                                presenter.showMsg("Contraseña actualizada correctamente");
                            }else{
                                try {
                                    Log.i("ERROR", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                presenter.showMsg("Ocurrió un error por favor reintenta");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i("ERROR", t.getMessage());
                            presenter.showMsg("Ocurrió un error por favor reintenta");
                        }
                    });
                }else{
                    presenter.showMsg("La contraseña no coincide");
                }
            }else{
                presenter.showMsg("La contraseña debe tener mínimo 8 caracteres");
            }


        }else{
            presenter.showMsg("Ingresa una contraseña");
        }
    }
}
