package com.hazmeparo.Propuestas.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioPresenter;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioView;
import com.hazmeparo.Propuestas.Presenter.PropuestasPresenterImpl;
import com.hazmeparo.Propuestas.Presenter.PropuestasServicioPresenterImpl;
import com.hazmeparo.R;

import java.util.ArrayList;

import Adapters.PropuestaAdapter;
import Adapters.PropuestaServicioAdapter;
import Clases.DialogCargando;
import Clases.PrefsManager;
import Models.Propuesta;
import Models.PropuestaServicio;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PropuestasServicioActivity extends AppCompatActivity implements PropuestasServicioView {

    PropuestasServicioPresenter presenter;
    DialogCargando carga;
    String favor;
    private androidx.appcompat.app.AlertDialog dialog;
    PrefsManager manager;

    @BindView(R.id.recyclerPropuestasServicio) RecyclerView listaPropuestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propuestas_servicio);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPropuestasServicio);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        manager = new PrefsManager(this);

        presenter = new PropuestasServicioPresenterImpl(this, manager);
        carga = new DialogCargando(this);

        Intent intent = getIntent();
        favor = intent.getStringExtra("servicio");
        presenter.getPropuestas(favor, manager.obtenerValorString("servicioCategoria"));
    }

    @Override
    public void showProgress() {
        carga.showDialog();
    }

    @Override
    public void hideProgress() {
        carga.hideDialog();
    }

    @Override
    public void showDialog(String msg, String compi, String key) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog3, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo3);
        txt.setText(msg);
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo3);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo3);


        btnAceptar.setOnClickListener(
                v1 -> {
                    presenter.aceptarPropuesta(key, compi, favor);
                    dialog.dismiss();
                }
        );


        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showMsg(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsg);
        txt.setText(msg);
        MaterialButton btn = v.findViewById(R.id.btnCerrardialogo);


        btn.setOnClickListener(
                v1 -> {
                    finish();
                    dialog.dismiss();
                }
        );

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setPropuestas(DataSnapshot propuestas) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaPropuestas.setLayoutManager(layoutManager);
        ArrayList<PropuestaServicio> propuestasList = new ArrayList<>();
        propuestasList.clear();

        for (DataSnapshot propuesta : propuestas.getChildren()) {
            String key = propuesta.getKey();
            String compi_num = propuesta.child("numero_esp").getValue().toString();
            String compi = propuesta.child("esp_nombre").getValue().toString();
            String costo = propuesta.child("costo").getValue().toString();
            String comentario = propuesta.child("comentario").getValue().toString();
            String calif = propuesta.child("calif_esp").getValue().toString();
            String img = propuesta.child("img_esp").getValue().toString();
            String compi_id = propuesta.child("esp_id").getValue().toString();

            propuestasList.add(new PropuestaServicio(key, favor, compi, costo, comentario, calif, compi_num, img, compi_id));
        }

        PropuestaServicioAdapter adapter = new PropuestaServicioAdapter(propuestasList, presenter, this);
        listaPropuestas.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}