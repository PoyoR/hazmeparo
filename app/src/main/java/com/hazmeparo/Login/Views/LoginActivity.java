package com.hazmeparo.Login.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Login.Interface.LoginPresenter;
import com.hazmeparo.Login.Interface.LoginView;
import com.hazmeparo.Login.Presenter.LoginPresenterImpl;
import com.hazmeparo.Inicio.Views.MainActivity;
import com.hazmeparo.R;
import com.hazmeparo.Registro.Views.RegistroActivity;

import Clases.DialogCargando;
import Clases.DialogMessage;
import Clases.PrefsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    DialogCargando carga;
    DialogMessage dialog;

    @BindView(R.id.txtInputUsuario) TextInputLayout txtUsuario;
    @BindView(R.id.txtInputPass) TextInputLayout txtPass;

    private LoginPresenter presenter;
    PrefsManager manager;

    private androidx.appcompat.app.AlertDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        manager = new PrefsManager(this);
        presenter = new LoginPresenterImpl(this, manager);
        carga = new DialogCargando(this);
        dialog = new DialogMessage(this);
        verificarSesion();

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
    public void setErrorUser() {
        txtUsuario.setError("Este campo es requerido");
    }

    @Override
    public void setErrorPassWord() {
        txtPass.setError("Este campo es requerido");
    }

    @Override
    public void loginSuccess() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginError() {
        dialog.showDialog("Usuario o contraseña incorrecto");

    }

    @Override
    public void verificarSesion() {
        Boolean logueado = manager.obtenerValorBoolean("logueado");

        if (logueado){
            loginSuccess();
        }
    }

    @Override
    public void showMsg(String msg) {

        dialog.showDialog(msg);
    }

    @OnClick(R.id.btnLogin) void loguearse() {
        String usuario = txtUsuario.getEditText().getText().toString();
        String pass = txtPass.getEditText().getText().toString();
        presenter.login(usuario, pass);
    }

    @OnClick(R.id.btnRegistro) void registrarse(){
        startActivity(new Intent(this, RegistroActivity.class));
    }

}
