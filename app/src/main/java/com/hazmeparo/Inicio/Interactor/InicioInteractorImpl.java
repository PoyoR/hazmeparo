package com.hazmeparo.Inicio.Interactor;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hazmeparo.Inicio.Interface.InicioInteractor;
import com.hazmeparo.Inicio.Interface.InicioPresenter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Calificacion;
import Models.Favor;
import Models.Propuesta;
import Models.User;
import Models.Usuario;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioInteractorImpl implements InicioInteractor {

    private InicioPresenter presenter;
    private  PrefsManager manager;
    private ApiInterface api;
    DatabaseReference dbReferenceFavor;
    DatabaseReference dbReferencePropuestas;
    DatabaseReference dbReference;
    StorageReference storageReference;

    ValueEventListener listenerFavor;
    ValueEventListener listenerPropuestas;
    ChildEventListener listenerPropuestasChild;


    public InicioInteractorImpl(InicioPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        api = ApiClient.getClient().create(ApiInterface.class);

        dbReferenceFavor = FirebaseDatabase.getInstance().getReference();
        dbReferencePropuestas = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void getFavores(String userId) {

        Query query = dbReferenceFavor.child("Favor").orderByChild("usuario").equalTo(userId);

        listenerFavor = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot favor : dataSnapshot.getChildren()) {


                        if (favor.child("usuario_cerrado").getValue().toString().equals("false")) {
                            presenter.mostrarLayout("Activo");

                            if (favor.hasChild("img")) {
                                presenter.setFavor(favor.getKey(), favor.child("status").getValue().toString(), favor.child("titulo").getValue().toString(),
                                        favor.child("descripcion").getValue().toString(), favor.child("hora").getValue().toString(),
                                        favor.child("direccion").getValue().toString(), favor.child("img").getValue().toString(), favor.child("msg_status").getValue().toString(),
                                        favor.child("motivo_cancelacion").getValue().toString(), favor.child("codigo_finalizacion").getValue().toString());
                            } else {
                                presenter.setFavor(favor.getKey(), favor.child("status").getValue().toString(), favor.child("titulo").getValue().toString(),
                                        favor.child("descripcion").getValue().toString(), favor.child("hora").getValue().toString(),
                                        favor.child("direccion").getValue().toString(), "", favor.child("msg_status").getValue().toString(),
                                        favor.child("motivo_cancelacion").getValue().toString(), favor.child("codigo_finalizacion").getValue().toString());
                            }
                        }else{
                            presenter.mostrarLayout("Inactivo");
                            dbReferenceFavor.removeEventListener(this);
                        }

                        getPropuestas(favor.getKey());
                    }

                }else{
                    presenter.mostrarLayout("Inactivo");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(listenerFavor);

    }

    @Override
    public void getPropuestas(String key) {

        listenerPropuestasChild = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("Nueva propuesta", "nueva");
                presenter.crearNotificacion();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbReferencePropuestas.child("Favor").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String status = dataSnapshot.child("status").getValue().toString();

                    if (status.equals("1")) {

                        dbReferencePropuestas.child("Favor").child(key).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    dbReferencePropuestas.child("Favor").child(key).child("Propuestas").addChildEventListener(listenerPropuestasChild);

                                    int total = (int) dataSnapshot.getChildrenCount();

                                    if (total == 1)
                                        presenter.setTotalPropuestas(total + " propuesta", status, true);
                                    else
                                        presenter.setTotalPropuestas(total + " propuestas", status, true);

                                } else {
                                    presenter.setTotalPropuestas("0 propuestas", status, false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        dbReferencePropuestas.removeEventListener(this);
                        dbReferencePropuestas.removeEventListener(listenerPropuestasChild);
                        presenter.setTotalPropuestas("0", status, false);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void getCompiElegido(String favor) {

        dbReferenceFavor = FirebaseDatabase.getInstance().getReference();

        dbReferenceFavor.child("Favor").child(favor).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot favor : dataSnapshot.getChildren()) {
                        presenter.setCompiElegido(favor.child("compi_id").getValue().toString(), favor.child("compi_nombre").getValue().toString(),  favor.child("calif_compi").getValue().toString(),
                                favor.child("numero_compi").getValue().toString(),  favor.child("transporte_compi").getValue().toString(),
                                favor.child("pago_centrega").getValue().toString(), favor.child("tiempo_estimado").getValue().toString(),
                                favor.child("costo").getValue().toString());
                        dbReferencePropuestas.removeEventListener(listenerPropuestasChild);
                    }

                }else{
                    Log.i("Favor no xiste", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void publicarFavor(String usuario, String token, String nom_usuario, String titulo, String descripcion, String hora, String direccionCompra, String direccion,
                              String path, Uri uri, String ubicacion, String ubicacion2) {

        if (!titulo.equals("") && !descripcion.equals("") && !hora.equals("") && !direccion.equals("")){

            Log.i("UBICACION", ubicacion);

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

                        Call<Favor> call2 = api.postFavor("Token " + token, tit, desc, dir, hr, body);

                        call2.enqueue(new Callback<Favor>() {
                            @Override
                            public void onResponse(Call<Favor> call, Response<Favor> response) {

                                if (response.isSuccessful()){
                                    Favor favor = response.body();

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
                                            datos.put("direccion_compra", direccionCompra);
                                            datos.put("direccion", direccion);
                                            datos.put("propuesta", "");
                                            datos.put("status", "1");
                                            datos.put("usuario_nombre", nom_usuario + " " + manager.obtenerValorString("apellidos_usuario"));
                                            datos.put("usuario_img", img_usuario);
                                            datos.put("hora", hora);
                                            datos.put("id_server", favor.getId());
                                            datos.put("img", downloadURL);
                                            datos.put("ubicacion", ubicacion);
                                            datos.put("ubicacionOrigen", ubicacion2);
                                            datos.put("municipio", manager.obtenerValorString("municipio"));
                                            datos.put("calif_usuario", calif);
                                            datos.put("numero_usuario", manager.obtenerValorString("username"));
                                            datos.put("usuario_cerrado", "false");
                                            datos.put("compi_cerrado", "false");
                                            datos.put("motivo_cancelacion", "");
                                            datos.put("msg_status", "Confirmación enviada al Compi");
                                            //Codigo finalizacion
                                            Random random = new Random();
                                            IntStream intStream = random.ints(1, 1000, 10000);
                                            Iterator iterator = intStream.iterator();
                                            datos.put("codigo_finalizacion", String.valueOf(iterator.next()));
                                            //Codigo finalizacion compi
                                            IntStream intStream2 = random.ints(1, 1000, 10000);
                                            Iterator iterator2 = intStream.iterator();
                                            datos.put("codigo_finalizacion_compi", String.valueOf(iterator2.next()));
                                            dbReference.child("Favor").push().setValue(datos);

                                            presenter.showDialog("Favor publicado correctamente", true);
                                            manager.guardarValorBoolean("favorActivo", true);
                                            presenter.favorPublicado();
                                        });

                                    }else{
                                        Map<String, Object> datos = new HashMap<>();
                                        datos.put("id_server", favor.getId());
                                        datos.put("usuario", usuario);
                                        datos.put("titulo", titulo);
                                        datos.put("usuario_nombre", nom_usuario);
                                        datos.put("usuario_img", img_usuario);
                                        datos.put("direccion_compra", direccionCompra);
                                        datos.put("direccion", direccion);
                                        datos.put("propuesta", "");
                                        datos.put("status", "1");
                                        datos.put("descripcion", descripcion);
                                        datos.put("hora", hora);
                                        datos.put("ubicacion", ubicacion);
                                        datos.put("ubicacionOrigen", ubicacion2);
                                        datos.put("municipio", manager.obtenerValorString("municipio"));
                                        datos.put("calif_usuario", calif);
                                        datos.put("numero_usuario", manager.obtenerValorString("username"));
                                        datos.put("usuario_cerrado", "false");
                                        datos.put("compi_cerrado", "false");
                                        datos.put("motivo_cancelacion", "");
                                        datos.put("msg_status", "Confirmación enviada al Compi");
                                        //Codigo finalizacion
                                        Random random = new Random();
                                        IntStream intStream = random.ints(1, 1000, 10000);
                                        Iterator iterator = intStream.iterator();
                                        datos.put("codigo_finalizacion", String.valueOf(iterator.next()));
                                        //Codigo finalizacion compi
                                        IntStream intStream2 = random.ints(1, 1000, 10000);
                                        Iterator iterator2 = intStream2.iterator();
                                        datos.put("codigo_finalizacion_compi", String.valueOf(iterator2.next()));
                                        dbReference.child("Favor").push().setValue(datos);

                                        presenter.showDialog("Favor publicado correctamente", true);
                                        manager.guardarValorBoolean("favorActivo", true);
                                        presenter.favorPublicado();
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
                            public void onFailure(Call<Favor> call, Throwable t) {
                                presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                            }
                        });

                    }else{
                        presenter.showDialog("Ocurrió un error, por favor intenta de nuevo", false);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

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

    //Marca el child en status 3 (temrinado) y lo marca en el server tmb
    @Override
    public void terminarFavor(String favor) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Favor").child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("status", "3");
                    dbReference.child("Favor").child(favor).updateChildren(datos).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            //Obtener el costo del favor (propuesta)
                            dbReference.child("Favor").child(favor).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot prop : dataSnapshot.getChildren()) {

                                            String total = prop.child("costo").getValue().toString();

                                            Call <Favor> call = api.updateStatusFavor("/a/favor_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "3", "",total);

                                            call.enqueue(new Callback<Favor>() {
                                                @Override
                                                public void onResponse(Call<Favor> call, Response<Favor> response) {

                                                    if (response.isSuccessful()){
                                                        presenter.showMsg("Favor terminado correctamente");
                                                    }else{
                                                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<Favor> call, Throwable t) {
                                                    presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                                }
                                            });
                                        }



                                    }else{
                                        Log.i("Favor no xiste", favor);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }).addOnFailureListener(e -> {
                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                    });


                }else{
                    Log.i("Propuesta no existe", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });

    }

    @Override
    //Elimina el child Favor y en caso de que tenga una img asociada la elimina tmb
    public void finalizarFavor(String favor) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Favor").child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    dbReference.child("Favor").child(favor).removeValue();

                    manager.guardarValorBoolean("favorActivo", false);
                    presenter.goToSolicitar();

                    if (dataSnapshot.hasChild("img")){
                        String img = dataSnapshot.child("img").getValue().toString();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(img);

                        storageReference.delete().addOnSuccessListener(aVoid -> {
                            Log.i("ELiminada", "ok");
                        }).addOnFailureListener(e -> {
                            Log.i("No eliminada", "no" + e.getMessage());
                        });
                    }

                }else{
                    Log.i("Propuesta no existe", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });

    }

    //El favor se elimina de firebase si usuario y compi lo cerraron
    @Override
    public void finalizarFavorIniciado(String favor) {
        dbReference = FirebaseDatabase.getInstance().getReference();


        dbReference.child("Favor").child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Actualizar campo usuario_cerrado para que al usuario ya no se le muestre el favor activo
                    HashMap hashMap = new HashMap();
                    hashMap.put("usuario_cerrado", "true");
                    dbReference.child("Favor").child(favor).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            //Si el compi ya lo cerro entonces se elimina
                            String compi_cerrado = "false";
                            if (dataSnapshot.hasChild("compi_cerrado")){
                                compi_cerrado = dataSnapshot.child("compi_cerrado").getValue().toString();
                            }

                            if (compi_cerrado.equals("true")) {

                                if (dataSnapshot.hasChild("img")) {
                                    String img = dataSnapshot.child("img").getValue().toString();
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(img);

                                    storageReference.delete().addOnSuccessListener(aVoid -> {
                                        Log.i("ELiminada", "ok");
                                        dbReference.child("Posicion").child(favor).removeValue();
                                    }).addOnFailureListener(e -> {
                                        Log.i("No eliminada", "no" + e.getMessage());
                                    });
                                }

                            }

                            dbReference.child("Posicion").child(favor).removeValue();
                            dbReference.child("Favor").child(favor).removeValue();
                            manager.guardarValorBoolean("favorActivo", false);
                            presenter.goToSolicitar();

                        }

                    }).addOnFailureListener(e -> {
                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                    });


                }else{
                    Log.i("Propuesta no existe", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });
    }

    @Override
    public void cancelarFavor(String favor) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        HashMap hashMap = new HashMap();
        hashMap.put("status", "0");

        dbReference.child("Favor").child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    dbReference.child("Favor").child(favor).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Call <Favor> call = api.updateStatusFavor("/a/favor_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "0", "", "0.0");

                            call.enqueue(new Callback<Favor>() {
                                @Override
                                public void onResponse(Call<Favor> call, Response<Favor> response) {

                                    if (response.isSuccessful()) {
                                    }else{
                                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                        try {
                                            Log.i("ERROR", response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<Favor> call, Throwable t) {
                                    presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                    Log.i("ERROR", t.getMessage());
                                }
                            });
                        }

                    }).addOnFailureListener(e -> {
                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                    });


                }else{
                    Log.i("Propuesta no existe", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });
    }

    //Actualiza status a -1 y agrega child motivo
    @Override
    public void cancelarFavorActivo(String favor, String motivo) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> datos = new HashMap<>();
        datos.put("status", "-1");
        datos.put("motivo_cancelacion", motivo);

        dbReference.child("Favor").child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    dbReference.child("Favor").child(favor).updateChildren(datos).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Call <Favor> call = api.updateStatusFavor("/a/favor_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "-1", motivo, "0.0");

                            call.enqueue(new Callback<Favor>() {
                                @Override
                                public void onResponse(Call<Favor> call, Response<Favor> response) {

                                    if (response.isSuccessful()){
                                        presenter.showMsg("Favor cancelado correctamente");

                                    }else{
                                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                        try {
                                            Log.i("ERROR", response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<Favor> call, Throwable t) {
                                    presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                    Log.i("ERROR", t.getMessage());
                                }
                            });
                        }

                    }).addOnFailureListener(e -> {
                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                    });


                }else{
                    Log.i("Propuesta no existe", favor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });
    }

    @Override
    public void verificarCodigoFinalizacion(String favor, String codigo) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        Query query = dbReference.child("Favor").orderByChild("codigo_finalizacion_compi").equalTo(codigo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    presenter.terminarFavor(favor);
                }else{
                    presenter.codigoFinalizacionIncorrecto();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void enviarCalificacionCompi(float calif, String comentario, String compiId) {

        Call<Calificacion> call = api.createCalifCompi("Token " + manager.obtenerValorString("token"), calif, comentario, compiId);

        call.enqueue(new Callback<Calificacion>() {
            @Override
            public void onResponse(Call<Calificacion> call, Response<Calificacion> response) {
                if (response.isSuccessful()){
                    Log.i("CALIF OK", "si");
                    manager.guardarValorBoolean("favorActivo", false);
                    presenter.goToSolicitar();
                }else{
                    try {
                        Log.i("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Calificacion> call, Throwable t) {
                Log.i("ERROR CALIF", t.getMessage());
            }
        });
    }

    @Override
    public void getPosicionCompi(String favor) {

        dbReferenceFavor.child("Posicion").child(favor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.exists()) {
                        String coord = dataSnapshot.child("coordenadas").getValue().toString();
                        presenter.setPosicionCompi(coord);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });

    }
}
