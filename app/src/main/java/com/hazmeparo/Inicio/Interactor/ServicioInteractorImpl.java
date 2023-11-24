package com.hazmeparo.Inicio.Interactor;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hazmeparo.Inicio.Interface.ServicioInteractor;
import com.hazmeparo.Inicio.Interface.ServicioPresenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Favor;
import Models.Servicio;
import Models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicioInteractorImpl implements ServicioInteractor {

    ServicioPresenter presenter;
    private ApiInterface api;
    PrefsManager manager;
    DatabaseReference dbReference;
    StorageReference storageReference;

    public ServicioInteractorImpl(ServicioPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void solicitarFavor(String usuario, String token, String nom_usuario, String categoria, String titulo, String descripcion, String hora, String direccion, String path, Uri uri, String ubicacion) {

        if (!titulo.equals("") && !descripcion.equals("") && !hora.equals("") && !direccion.equals("")){

            dbReference = FirebaseDatabase.getInstance().getReference();

            RequestBody requestBody;
            MultipartBody.Part body;

            if (path != null){
                File img = new File(path);
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), img);
                body = MultipartBody.Part.createFormData("img_producto", img.getName(), requestBody);
            }else{
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body = MultipartBody.Part.createFormData("", "", requestBody);
            }

            RequestBody tit = RequestBody.create(MediaType.parse("multipart/form-data"), titulo);
            RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), descripcion);
            RequestBody hr = RequestBody.create(MediaType.parse("multipart/form-data"), hora);
            RequestBody dir = RequestBody.create(MediaType.parse("multipart/form-data"), direccion);

            //Obtener la calif del usuario
            Call<User> calif = api.getUserInfo("/a/usuario_get/"+manager.obtenerValorString("username")+"/", "Token " + manager.obtenerValorString("token"));

            calif.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()) {
                        User user = response.body();
                        String calif =  user.getUsuario().getCalificacion();
                        String img_usuario = user.getUsuario().getFoto();

                        Call<Servicio> call2 = api.postServicio("Token " + token, tit, desc, dir, hr, body);

                        call2.enqueue(new Callback<Servicio>() {
                            @Override
                            public void onResponse(Call<Servicio> call, Response<Servicio> response) {

                                if (response.isSuccessful()){
                                    Servicio servicio = response.body();

                                    //Firebase
                                    dbReference = FirebaseDatabase.getInstance().getReference();

                                    if (uri != null){
                                        storageReference = FirebaseStorage.getInstance().getReference();
                                        StorageReference path = storageReference.child("imgs").child(uri.getLastPathSegment());

                                        UploadTask uploadTask = path.putFile(uri);

                                        Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                                            if (!task.isSuccessful()) {
                                                throw Objects.requireNonNull(task.getException());
                                            }

                                            return path.getDownloadUrl();
                                        }).addOnCompleteListener(task -> {
                                            Uri uriDescargada = task.getResult();
                                            String downloadURL = uriDescargada.toString();
                                            Map<String, Object> datos = new HashMap<>();
                                            datos.put("usuario", usuario);
                                            datos.put("titulo", titulo);
                                            datos.put("descripcion", descripcion);
                                            datos.put("direccion", direccion);
                                            datos.put("propuesta", "");
                                            datos.put("status", "1");
                                            datos.put("usuario_nombre", nom_usuario + " " + manager.obtenerValorString("apellidos_usuario"));
                                            datos.put("usuario_img", img_usuario);
                                            datos.put("hora", hora);
                                            datos.put("id_server", servicio.getId());
                                            datos.put("img", downloadURL);
                                            datos.put("ubicacion", ubicacion);
                                            datos.put("municipio", manager.obtenerValorString("municipio"));
                                            datos.put("calif_usuario", calif);
                                            datos.put("motivo_cancelacion", "");
                                            datos.put("numero_usuario", manager.obtenerValorString("username"));
                                            datos.put("usuario_cerrado", "false");
                                            datos.put("prof_cerrado", "false");
                                            //Codigo finalizacion
                                            Random random = new Random();
                                            IntStream intStream = random.ints(1, 1000, 10000);
                                            Iterator iterator = intStream.iterator();
                                            datos.put("codigo_finalizacion", String.valueOf(iterator.next()));
                                            //Codigo finalizacion compi
                                            IntStream intStream2 = random.ints(1, 1000, 10000);
                                            Iterator iterator2 = intStream2.iterator();
                                            datos.put("codigo_finalizacion_compi", String.valueOf(iterator2.next()));
                                            dbReference.child("Servicio").child(categoria).push().setValue(datos);

                                            presenter.showDialog("Servicio solicitado correctamente", true);
                                            manager.guardarValorBoolean("servicioActivo", true);
                                            manager.guardarValorString("servicioCategoria", categoria);
                                            presenter.goToServicioActivo();
                                        });

                                    }else{
                                        Map<String, Object> datos = new HashMap<>();
                                        datos.put("usuario", usuario);
                                        //datos.put("categoria", categoria);
                                        datos.put("titulo", titulo);
                                        datos.put("descripcion", descripcion);
                                        datos.put("direccion", direccion);
                                        datos.put("propuesta", "");
                                        datos.put("status", "1");
                                        datos.put("usuario_nombre", nom_usuario + " " + manager.obtenerValorString("apellidos_usuario"));
                                        datos.put("usuario_img", img_usuario);
                                        datos.put("hora", hora);
                                        datos.put("id_server", servicio.getId());
                                        datos.put("ubicacion", ubicacion);
                                        datos.put("municipio", manager.obtenerValorString("municipio"));
                                        datos.put("calif_usuario", calif);
                                        datos.put("motivo_cancelacion", "");
                                        datos.put("numero_usuario", manager.obtenerValorString("username"));
                                        datos.put("usuario_cerrado", "false");
                                        datos.put("prof_cerrado", "false");
                                        //Codigo finalizacion
                                        Random random = new Random();
                                        IntStream intStream = random.ints(1, 1000, 10000);
                                        Iterator iterator = intStream.iterator();
                                        datos.put("codigo_finalizacion", String.valueOf(iterator.next()));
                                        //Codigo finalizacion compi
                                        IntStream intStream2 = random.ints(1, 1000, 10000);
                                        Iterator iterator2 = intStream2.iterator();
                                        datos.put("codigo_finalizacion_compi", String.valueOf(iterator2.next()));
                                        dbReference.child("Servicio").child(categoria).push().setValue(datos);

                                        presenter.showDialog("Servicio solicitado correctamente", true);
                                        manager.guardarValorBoolean("servicioActivo", true);
                                        manager.guardarValorString("servicioCategoria", categoria);
                                        presenter.goToServicioActivo();
                                    }

                                }else{
                                    try {

                                        Log.i("ERROR", response.errorBody().string());
                                        presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Servicio> call, Throwable t) {
                                presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                            }
                        });

                    }else{
                        presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                }
            });

        }else{
            if (titulo.equals("")){
                presenter.setTituloError();
            }

            if (descripcion.equals("")){
                presenter.setDescripcionError();
            }

            if (hora.equals("")){
                presenter.setHoraError();
            }

            if (direccion.equals("")){
                presenter.setDireccionError();
            }
        }
    }
}
