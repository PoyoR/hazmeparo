package com.hazmeparo.RegistroCompi.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Login.Views.LoginActivity;
import com.hazmeparo.Inicio.Views.MainActivity;
import com.hazmeparo.R;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiPresenter;
import com.hazmeparo.RegistroCompi.Interface.RegistroCompiView;
import com.hazmeparo.RegistroCompi.Presenter.RegistroCompiPresenterImpl;

import java.util.List;

import Clases.DialogCargando;
import Clases.DialogMessage;
import Clases.PrefsManager;
import Models.Lugar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistroCompiActivity extends AppCompatActivity implements RegistroCompiView {

    DialogCargando carga;
    DialogMessage dialog;

    String estado_seleccionado = "";
    String municipio_seleccionado = "";

    private RegistroCompiPresenter presenter;

    PrefsManager manager;

    @BindView(R.id.txtInputDireccionRegistroCompi) TextInputLayout txtDireccion;
    @BindView(R.id.txtInputNumeroRegistroCompi) TextInputLayout txtNumero;
    @BindView(R.id.txtInputPassRegistroCompi) TextInputLayout txtPass;

    @BindView(R.id.spinnerEstadoCompi) Spinner spinnerEstados;
    @BindView(R.id.spinnerMunicipioCompi) Spinner spinnerMunicipios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_compi);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRegistroCompi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        manager = new PrefsManager(this);
        presenter = new RegistroCompiPresenterImpl(this, manager);

        carga = new DialogCargando(this);
        dialog = new DialogMessage(this);

        presenter.getEstados();
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
    public void setDireccionError() {
        txtDireccion.setError("Por favor ingresa tu dirección");
    }

    @Override
    public void setNumeroError() {
        txtNumero.setError("Por favor ingresa tu número de teléfono");
    }

    @Override
    public void setPassError() {
        txtPass.setError("Por favor ingresa tu contraseña");
    }

    @Override
    public void registroError() {
        dialog.showDialog("Ocurrió un error, por favor intenta de nuevo");
    }

    @Override
    public void registroSuccess() {
        dialog.showDialog("Registro correcto");
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void goToLogin() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void setEstados(List<Lugar> estados) {
        ArrayAdapter<Lugar> adapter =
                new ArrayAdapter<Lugar>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spinnerEstados.setAdapter(adapter);
        spinnerEstados.setSelection(20);
        spinnerEstados.setEnabled(false);

        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Lugar estado = adapter.getItem(i);
                //estado_seleccionado = estado.getId();
                //presenter.getMunicipios(estado_seleccionado);
                presenter.getMunicipios("21");
                //Log.i("Estado", estado_seleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void setMunicipios(List<Lugar> municipios) {
        ArrayAdapter<Lugar> adapter =
                new ArrayAdapter<Lugar>(this, android.R.layout.simple_spinner_dropdown_item, municipios);
        spinnerMunicipios.setAdapter(adapter);
        spinnerMunicipios.setSelection(115);
        spinnerMunicipios.setEnabled(false);

        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Lugar municipio = adapter.getItem(i);
                //municipio_seleccionado = municipio.getId();
                //Log.i("Municipio", municipio_seleccionado);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.btnEnviarRegistroCompi) void registrarse(){
        String estado = "21";
        String municipio = "1905";
        String direccion = txtDireccion.getEditText().getText().toString();
        String numero = txtNumero.getEditText().getText().toString();
        String pass = txtPass.getEditText().getText().toString();

        presenter.registrarse(estado, municipio, "", direccion, numero, pass);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
