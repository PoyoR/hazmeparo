package com.hazmeparo.Cuenta.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosPresenter;
import com.hazmeparo.Cuenta.Interfaces.UpdateDatosView;
import com.hazmeparo.Cuenta.Presenter.UpdateDatosPresenterImpl;
import com.hazmeparo.R;

import java.util.List;

import Clases.DialogCargando;
import Clases.DialogMessage;
import Clases.PrefsManager;
import Models.Lugar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateDatosActivity extends AppCompatActivity implements UpdateDatosView {

    DialogCargando carga;

    private androidx.appcompat.app.AlertDialog dialog;

    String estado_seleccionado = "";
    String municipio_seleccionado = "";

    PrefsManager manager;

    UpdateDatosPresenter presenter;

    @BindView(R.id.txtInputNombreUpdateDatos) TextInputLayout txtNombre;
    @BindView(R.id.txtInputApellidosUpdateDatos) TextInputLayout txtApellidos;
    @BindView(R.id.txtInputDireccionUpdateDatos) TextInputLayout txtDireccion;
    @BindView(R.id.txtInputCorreoUpdateDatos) TextInputLayout txtCorreo;
    //@BindView(R.id.txtInputPassUpdateDatos) TextInputLayout txtPass;

    @BindView(R.id.spinnerEstadoUpdateDatos) Spinner spinnerEstados;
    @BindView(R.id.spinnerMunicipioUpdateDatos) Spinner spinnerMunicipios;

    @BindView(R.id.layoutCambiarPass) LinearLayout layoutPass;
    @BindView(R.id.txtInputPassUpdateDatos) TextInputLayout txtPass;
    @BindView(R.id.txtInputPass2UpdateDatos) TextInputLayout txtPass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_datos);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUpdateDatos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        manager = new PrefsManager(this);
        presenter = new UpdateDatosPresenterImpl(this, manager);

        carga = new DialogCargando(this);

        getDatos();
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

    private void getDatos(){
        txtNombre.getEditText().setText(manager.obtenerValorString("nom_usuario"));
        txtApellidos.getEditText().setText(manager.obtenerValorString("apellidos_usuario"));
        txtCorreo.getEditText().setText(manager.obtenerValorString("correo_usuario"));
        txtDireccion.getEditText().setText(manager.obtenerValorString("direccion"));
    }

    @Override
    public void setNombreError() {
        txtNombre.setError("Por favor ingresa tu nombre");
    }

    @Override
    public void setApellidosError() {
        txtApellidos.setError("Por favor ingresa tus apellidos");
    }

    @Override
    public void setDireccionError() {
        txtDireccion.setError("Por favor ingresa tu dirección");
    }

    @Override
    public void setCorreoError() {
        txtCorreo.setError("Por favor ingresa un correo válido");
    }

    /*@Override
    public void setPassError() {
        txtPass.setError("Por favor ingresa tu contraseña");
    }*/

    @Override
    public void setEstados(List<Lugar> estados) {
        ArrayAdapter<Lugar> adapter =
                new ArrayAdapter<Lugar>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spinnerEstados.setAdapter(adapter);

        int index = 0;
        for (int i = 0; i < estados.size(); i++) {
            Lugar estado = estados.get(i);
            String estado_actual = manager.obtenerValorString("estado");

            if(estado_actual.equals(estado.getId())){
                index = i;
            }

        }
        spinnerEstados.setSelection(index);

        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Lugar estado = adapter.getItem(i);
                estado_seleccionado = estado.getId();
                presenter.getMunicipios(estado_seleccionado);
                Log.i("Estado", estado_seleccionado);
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

        int index = 0;
        for (int i = 0; i < municipios.size(); i++) {
            Lugar estado = municipios.get(i);
            String estado_actual = manager.obtenerValorString("municipio");

            if(estado_actual.equals(estado.getId())){
                index = i;
            }

        }
        spinnerMunicipios.setSelection(index);

        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Lugar municipio = adapter.getItem(i);
                municipio_seleccionado = municipio.getId();
                Log.i("Municipio", municipio_seleccionado);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.btnEnviarUpdateDatos) void updateDatos(){
        String nombre = txtNombre.getEditText().getText().toString();
        String apellidos = txtApellidos.getEditText().getText().toString();
        String direccion = txtDireccion.getEditText().getText().toString();
        String correo = txtCorreo.getEditText().getText().toString();

        presenter.updateDatos(nombre, apellidos, estado_seleccionado, municipio_seleccionado, direccion, correo);
    }

    @OnClick(R.id.btnUpdatePass) void showLayoutPass(){
        layoutPass.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnEnviarPassUpdateDatos) void updatePass(){
        presenter.updatePass(txtPass.getEditText().getText().toString(), txtPass2.getEditText().getText().toString());
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}