package com.hazmeparo.Inicio.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hazmeparo.Inicio.Interface.ServicioActivoInteractor;
import com.hazmeparo.Inicio.Interface.ServicioActivoPresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Calificacion;
import Models.Favor;
import Models.Servicio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicioActivoInteractorImpl implements ServicioActivoInteractor {

    ServicioActivoPresenter presenter;
    DatabaseReference dbReference;
    DatabaseReference dbReferencePropuestas;
    ValueEventListener listenerServicio;
    ChildEventListener listenerPropuestasChild;
    PrefsManager manager;

    private ApiInterface api;

    public ServicioActivoInteractorImpl(ServicioActivoPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        dbReference = FirebaseDatabase.getInstance().getReference();
        dbReferencePropuestas = FirebaseDatabase.getInstance().getReference();

        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void getServicio(String userId, String categoria) {

        Log.i("USERID", userId);
        Log.i("CATEGORIA", categoria);
        Query query = dbReference.child("Servicio").child(categoria).orderByChild("usuario").equalTo(userId);

        listenerServicio = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot servicio : dataSnapshot.getChildren()) {

                        if (servicio.child("usuario_cerrado").getValue().toString().equals("false")) {

                            if (servicio.hasChild("img")) {
                                presenter.setServicio(servicio.getKey(), servicio.child("status").getValue().toString(), servicio.child("titulo").getValue().toString(),
                                        servicio.child("descripcion").getValue().toString(), servicio.child("hora").getValue().toString(),
                                        servicio.child("direccion").getValue().toString(), servicio.child("img").getValue().toString(), servicio.child("motivo_cancelacion").getValue().toString(),
                                        servicio.child("codigo_finalizacion").getValue().toString());
                            } else {
                                presenter.setServicio(servicio.getKey(), servicio.child("status").getValue().toString(), servicio.child("titulo").getValue().toString(),
                                        servicio.child("descripcion").getValue().toString(), servicio.child("hora").getValue().toString(),
                                        servicio.child("direccion").getValue().toString(), "", servicio.child("motivo_cancelacion").getValue().toString(),
                                        servicio.child("codigo_finalizacion").getValue().toString());
                            }
                        }else{
                            dbReference.removeEventListener(this);
                        }

                        getPropuestas(servicio.getKey(), categoria);
                    }

                }else{
                    presenter.goToInicio();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(listenerServicio);

    }

    @Override
    public void getCompiElegido(String servicio) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(servicio).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot favor : dataSnapshot.getChildren()) {
                        presenter.setCompiElegido(favor.child("esp_id").getValue().toString(), favor.child("esp_nombre").getValue().toString(),  favor.child("calif_esp").getValue().toString(),
                                favor.child("numero_esp").getValue().toString(), favor.child("comentario").getValue().toString(),
                                favor.child("costo").getValue().toString());
                        dbReferencePropuestas.removeEventListener(listenerPropuestasChild);
                    }

                }else{
                    Log.i("Favor no xiste", servicio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    //Elimina el child Favor y en caso de que tenga una img asociada la elimina tmb
    public void finalizarServicio(String favor) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).removeValue();
                    presenter.goToInicio();

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

    @Override
    //Poner status 0 en firebase y django
    public void cancelarServicio(String favor) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        HashMap hashMap = new HashMap();
        hashMap.put("status", "0");

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Call<Servicio> call = api.updateStatusServicio("/a/servicio_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "0", "", "0.0");

                            call.enqueue(new Callback<Servicio>() {
                                @Override
                                public void onResponse(Call<Servicio> call, Response<Servicio> response) {

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
                                public void onFailure(Call<Servicio> call, Throwable t) {
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

    //Actualiza status a -1 y agrega child motivo para mostrar al compi
    @Override
    public void cancelarServicioActivo(String favor, String motivo) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        HashMap hashMap = new HashMap();
        hashMap.put("status", "-1");
        hashMap.put("motivo_cancelacion", motivo);

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            Call <Servicio> call = api.updateStatusServicio("/a/servicio_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "-1", motivo, "0.0");

                            call.enqueue(new Callback<Servicio>() {
                                @Override
                                public void onResponse(Call<Servicio> call, Response<Servicio> response) {

                                    if (response.isSuccessful()){
                                        presenter.showMsg("Servicio cancelado correctamente");

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
                                public void onFailure(Call<Servicio> call, Throwable t) {
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

    //El servicio se elimina de firebase si usuario y compi lo cerraron
    @Override
    public void finalizarServicioIniciado(String favor) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Actualizar campo usuario_cerrado para que al usuario ya no se le muestre el favor activo
                    HashMap hashMap = new HashMap();
                    hashMap.put("usuario_cerrado", "true");
                    dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            //Si el compi ya lo cerro entonces se elimina
                            String compi_cerrado = "false";
                            if (dataSnapshot.hasChild("compi_cerrado")){
                                compi_cerrado = dataSnapshot.child("compi_cerrado").getValue().toString();
                            }

                            if (compi_cerrado.equals("true")) {

                                Log.i("Compi lo cerro igual", "si");

                                dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).removeValue();

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

                            manager.guardarValorBoolean("servicioActivo", false);
                            manager.guardarValorString("servicioCategoria", "");
                            presenter.goToInicio();

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
    public void enviarCalificacionCompi(float calif, String comentario, String compiId) {
        Call<Calificacion> call = api.createCalifEsp("Token " + manager.obtenerValorString("token"), calif, comentario, compiId);

        call.enqueue(new Callback<Calificacion>() {
            @Override
            public void onResponse(Call<Calificacion> call, Response<Calificacion> response) {
                if (response.isSuccessful()){
                    Log.i("CALIF OK", "si");
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
    public void verificarCodigoFinalizacion(String favor, String codigo) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        Query query = dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).orderByChild("codigo_finalizacion_compi").equalTo(codigo);

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

    //Marca el child en status 3 (temrinado) y lo marca en el server tmb
    @Override
    public void terminarFavor(String favor) {

        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id_server = dataSnapshot.child("id_server").getValue().toString();

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("status", "3");
                    dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).updateChildren(datos).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            //Obtener el costo del favor (propuesta)
                            dbReference.child("Servicio").child(manager.obtenerValorString("servicioCategoria")).child(favor).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange( @NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot prop : dataSnapshot.getChildren()) {

                                            String total = prop.child("costo").getValue().toString();

                                            Call <Servicio> call = api.updateStatusServicio("/a/servicio_update/"+ id_server + "/", "Token " + manager.obtenerValorString("token"), "3", "",total);

                                            call.enqueue(new Callback<Servicio>() {
                                                @Override
                                                public void onResponse(Call<Servicio> call, Response<Servicio> response) {

                                                    if (response.isSuccessful()){
                                                        //presenter.showMsg("Servicio terminado correctamente");
                                                    }else{
                                                        presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<Servicio> call, Throwable t) {
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
            public void onCancelled( @NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });

    }

    public void getPropuestas(String key, String categoria) {

        listenerPropuestasChild = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("Nueva propuesta", "nueva");
                //presenter.crearNotificacion();
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

        dbReferencePropuestas.child("Servicio").child(categoria).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String status = dataSnapshot.child("status").getValue().toString();

                    if (status.equals("1")) {

                        dbReferencePropuestas.child("Servicio").child(categoria).child(key).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    dbReferencePropuestas.child("Servicio").child(categoria).child(key).child("Propuestas").addChildEventListener(listenerPropuestasChild);

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
}

