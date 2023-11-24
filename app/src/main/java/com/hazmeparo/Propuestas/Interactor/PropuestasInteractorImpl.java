package com.hazmeparo.Propuestas.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hazmeparo.Propuestas.Interface.PropuestasInteractor;
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Propuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropuestasInteractorImpl implements PropuestasInteractor {

    PropuestasPresenter presenter;
    DatabaseReference dbReference;
    private ApiInterface api;
    private PrefsManager manager;

    public PropuestasInteractorImpl(PropuestasPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;
        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void getPropuestas(String favor) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        dbReference.child("Favor").child(favor).child("Propuestas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    presenter.setPropuestas(dataSnapshot);
                }else{
                    Log.i("NO HAY PROPUESTAS", "NEL");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("NO HAY PROPUESTAS 2", "NEL");
            }
        });

    }

    @Override
    public void aceptarPropuesta(String propuesta, String compi, String favor) {
        dbReference = FirebaseDatabase.getInstance().getReference();

        //Verificar que el usuario no le hayan aceptado otro favor
        dbReference.child("Compis_status").child(compi).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String status = dataSnapshot.child("status").getValue().toString();
                    if (!status.equals("Ocupado")){
                        aceptar(propuesta, compi, favor);
                    }else{
                        presenter.showMsg("Este Compi no se encuentra disponible");
                        //Eliminar propuesta
                        dbReference.child("Propuesta").child(favor).child(propuesta).removeValue();
                    }

                }else{
                    aceptar(propuesta, compi, favor);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
            }
        });




    }

    private void aceptar(String propuesta, String compi, String favor){
        dbReference = FirebaseDatabase.getInstance().getReference();

        //Eliminar todas las demas propuestas
        dbReference.child("Favor").child(favor).child("Propuestas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot prop : dataSnapshot.getChildren()) {

                        if (!prop.getKey().equals(propuesta)){
                            prop.getRef().removeValue();
                        }else{
                            //Datos a modificar a la propuesta
                            HashMap hashMap2 = new HashMap();
                            hashMap2.put("aceptada", "true");

                            dbReference.child("Propuestas_usuarios").child(compi).child(propuesta).updateChildren(hashMap2).addOnSuccessListener(o2 -> {
                            }).addOnFailureListener(e -> {
                                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                            });


                            //Agregar key de propuesta a favor
                            HashMap hashMap = new HashMap();
                            hashMap.put("propuesta", prop.getKey());
                            hashMap.put("status", "2");

                            dbReference.child("Favor").child(favor).updateChildren(hashMap).addOnSuccessListener(o2 -> {

                                        presenter.showMsg("Propuesta aceptada correctamente");
                                        dbReference.child("Propuestas_usuarios").child(compi).child(propuesta).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()) {

                                                    String compi = dataSnapshot.child("compi_id").getValue().toString();
                                                    String favor = dataSnapshot.child("favor_id_server").getValue().toString();
                                                    String tiempo = dataSnapshot.child("tiempo_estimado").getValue().toString();
                                                    String pago = dataSnapshot.child("pago_centrega").getValue().toString();
                                                    String costo = dataSnapshot.child("costo").getValue().toString();

                                                    Map<String, Object> datos = new HashMap<>();
                                                    datos.put("compi_id", compi);
                                                    datos.put("coordenadas", "0,0");
                                                    dbReference.child("Posicion").child(dataSnapshot.child("favor").getValue().toString()).setValue(datos);

                                                    registrarPropuestaDjango(compi, favor, tiempo, pago, costo);

                                                }else{
                                                    Log.i("Favor no existe", favor);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                            ).addOnFailureListener(e -> {
                                presenter.showMsg("Ocurrió un error por favor intenta de nuevo");
                            });
                            break;


                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void registrarPropuestaDjango(String compi, String favor, String tiempo, String pagoentrega, String costo){

        Call<Propuesta> call = api.guardarPropuesta("Token "+manager.obtenerValorString("token"), compi, favor, tiempo, Boolean.parseBoolean(pagoentrega), costo);

        call.enqueue(new Callback<Propuesta>() {
            @Override
            public void onResponse(@NonNull Call<Propuesta> call, @NonNull Response<Propuesta> response) {
                if (response.isSuccessful()){
                }else{
                    /*try {
                        Log.i("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propuesta> call, @NonNull Throwable t) {
                //Log.i("Error", t.getMessage());
            }
        });
    }
}