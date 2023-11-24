package com.hazmeparo.Perfil.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hazmeparo.Perfil.Interfaces.PerfilPresenter;
import com.hazmeparo.Perfil.Interfaces.PerfilView;
import com.hazmeparo.Perfil.Presenter.PerfilPresenterImpl;
import com.hazmeparo.R;

import java.util.List;

import Adapters.OpinionAdapter;
import Clases.DialogCargando;
import Clases.PrefsManager;
import Models.Calificacion;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PerfilActivity extends AppCompatActivity implements PerfilView {

    PerfilPresenter presenter;
    @BindView(R.id.imgFotoPerfilUser) ImageView imgPerfil;
    @BindView(R.id.imgStatusUsuarioPerfilUser) ImageView imgStatus;
    @BindView(R.id.txtnombrePerfilUser) TextView txtNombre;
    @BindView(R.id.txtCalifPerfilUser) TextView txtCalificacion;

    @BindView(R.id.recyclerOpinionesPerfilUser) RecyclerView listaOpiniones;

    DialogCargando carga;
    private androidx.appcompat.app.AlertDialog dialog;
    PrefsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        manager = new PrefsManager(this);
        presenter = new PerfilPresenterImpl(this, manager);
        carga = new DialogCargando(this);

        String compi = getIntent().getStringExtra("compi");
        Log.i("COMPI ID", compi);
        presenter.getDatos(compi);
        presenter.getOpiniones(compi);
        presenter.getStatus();
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
                    dialog.dismiss();
                }
        );

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto) {
        txtNombre.setText(nombre);
        txtCalificacion.setText(calif);

        if (!foto.equals("")){

            Glide.with(this)
                    .load(foto)
                    .fitCenter()
                    .centerCrop()
                    .into(imgPerfil);

        }else{
            imgPerfil.setImageResource(R.drawable.perfil_default);
        }
    }

    @Override
    public void setOpiniones(List<Calificacion> opiniones) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaOpiniones.setLayoutManager(layoutManager);

        OpinionAdapter adapter = new OpinionAdapter(opiniones);
        listaOpiniones.setAdapter(adapter);
    }

    @Override
    public void setStatus(String status) {
        switch (status) {
            case "Libre":
                imgStatus.setImageResource(R.drawable.ic_status_disponible);
                break;
            case "Ocupado":
                imgStatus.setImageResource(R.drawable.ic_status_ocupado);
                break;
            case "Offline":
                imgStatus.setImageResource(R.drawable.ic_status_desconectado);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}