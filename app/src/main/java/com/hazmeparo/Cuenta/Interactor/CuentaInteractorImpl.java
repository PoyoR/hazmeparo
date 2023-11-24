package com.hazmeparo.Cuenta.Interactor;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hazmeparo.Cuenta.Interfaces.CuentaInteractor;
import com.hazmeparo.Cuenta.Interfaces.CuentaPresenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Calificacion;
import Models.User;
import Models.Usuario;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuentaInteractorImpl implements CuentaInteractor {

    CuentaPresenter presenter;
    PrefsManager manager;
    private ApiInterface api;
    DatabaseReference dbReference;

    public CuentaInteractorImpl(CuentaPresenter presenter, PrefsManager manager) {
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
                            user.getUsuario().getCalificacion(), user.getUsername(), user.getEmail(),
                            user.getUsuario().getDireccion(),
                            user.getUsuario().getEstado_nombre() + ", " + user.getUsuario().getMunicipio_nombre(),
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
    public void updateFoto(String path, Uri uri) {

        RequestBody requestBody;
        MultipartBody.Part body;

        File img = new File(path);
        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), img);
        body = MultipartBody.Part.createFormData("foto", img.getName(), requestBody);
        
        Call<Usuario> call = api.updateFotoPerfil("/a/usuario_foto/" + manager.obtenerValorString("id_usuario"),
                "Token " + manager.obtenerValorString("token"), body);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){
                    presenter.hideProgress();
                    presenter.fotoActualizada(uri);
                }else{
                    /*try {
                        Log.i("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    presenter.showMsg("Ocurrió un error, por favor reintenta");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                //Log.i("Favores", t.getMessage());
                presenter.showMsg("Ocurrió un error, por favor reintenta");
            }
        });

    }

    @Override
    public void getOpiniones() {

        Call<List<Calificacion>> call = api.getOpinionesUsuario("/a/calif_usuario_list/?usuario="+manager.obtenerValorString("username")+"&compi=false&especialista=false", "Token " + manager.obtenerValorString("token"));

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
        Map<String, Object> datos = new HashMap<>();
        datos.put("status", "Libre");

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Usuarios_status").child(manager.obtenerValorString("username")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String status = dataSnapshot.child("status").getValue().toString();
                    presenter.setStatus(status);
                }else{
                    dbReference.child("Usuarios_status").child(manager.obtenerValorString("username")).setValue(datos);
                    presenter.setStatus("Libre");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
