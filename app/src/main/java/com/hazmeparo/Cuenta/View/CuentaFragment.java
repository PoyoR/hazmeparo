package com.hazmeparo.Cuenta.View;


import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Cuenta.Interfaces.CuentaPresenter;
import com.hazmeparo.Cuenta.Interfaces.CuentaView;
import com.hazmeparo.Cuenta.Presenter.CuentaPresenterImpl;
import com.hazmeparo.Login.Views.LoginActivity;
import com.hazmeparo.R;
import com.hazmeparo.RegistroCompi.View.RegistroCompiActivity;
import com.hazmeparo.SettingsActivity;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import Adapters.FavorAdapter;
import Adapters.OpinionAdapter;
import Clases.DialogCargando;
import Clases.PrefsManager;
import Models.Calificacion;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuentaFragment extends Fragment implements CuentaView {


    public CuentaFragment() {
        // Required empty public constructor
    }

    CuentaPresenter presenter;
    PrefsManager manager;
    DialogCargando carga;
    private androidx.appcompat.app.AlertDialog dialog;

    @BindView(R.id.txtnombrePerfil) TextView txtNombre;
    @BindView(R.id.txtCalifUsuarioPerfil) TextView txtCalificacion;
    @BindView(R.id.txtTelefonoPerfil) TextView txtNumero;
    @BindView(R.id.txtCorreoPerfil) TextView txtCorreo;
    @BindView(R.id.txtDireccionPerfil) TextView txtDireccion;
    @BindView(R.id.txtEstadoCiudadPerfil) TextView txtEstadoCiudad;
    @BindView(R.id.imgFotoPerfil) ImageView imgFoto;

    //Status
    @BindView(R.id.imgStatusUsuarioPerfil) ImageView imgStatus;

    @BindView(R.id.recyclerOpinionesUsuario) RecyclerView listaOpiniones;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        ButterKnife.bind(this, view);
        manager = new PrefsManager(getActivity());
        carga = new DialogCargando(getActivity());
        presenter = new CuentaPresenterImpl(this, manager);

        presenter.getDatos(manager.obtenerValorString("username"));
        presenter.getOpiniones();
        presenter.getStatus();

        return view;
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
    public void setDatos(String nombre, String calif, String numero, String correo, String direccion, String estadoCIudad, String foto) {
        txtNombre.setText(nombre);
        txtCalificacion.setText(calif);
        txtNumero.setText(numero);
        txtCorreo.setText(correo);
        txtDireccion.setText(direccion);
        txtEstadoCiudad.setText(estadoCIudad);

        if (!foto.equals("")){
            if (isAdded()) {
                Glide.with(getActivity())
                        .load(foto)
                        .fitCenter()
                        .centerCrop()
                        .into(imgFoto);
            }
        }else{
            imgFoto.setImageResource(R.drawable.perfil_default);
        }
    }

    @Override
    public void fotoActualizada(Uri uri) {
        imgFoto.setImageURI(uri);
    }

    @Override
    public void showMsg(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
    public void setOpiniones(List<Calificacion> opiniones) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaOpiniones.setLayoutManager(layoutManager);

        OpinionAdapter adapter = new OpinionAdapter(opiniones);
        listaOpiniones.setAdapter(adapter);
    }

    @Override
    public void setStatus(String status) {
        switch (status) {
            case "Libre":
                //txtStatus.setText("Disponible");
                imgStatus.setImageResource(R.drawable.ic_status_disponible);
                break;
            case "Ocupado":
                //txtStatus.setText("Ocupado");
                imgStatus.setImageResource(R.drawable.ic_status_ocupado);
                break;
            case "Offline":
                //txtStatus.setText("Desconectado");
                imgStatus.setImageResource(R.drawable.ic_status_desconectado);
                break;
        }
    }

    @OnClick(R.id.imgEditFotoPerfil) void seleccionar(){
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 123);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == 123 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();

                //Comprimir
                if (selectedImage != null) {
                    File file  = new File(SiliCompressor.with(getActivity()).compress(FileUtils.getPath(getActivity(), selectedImage), new File(getActivity().getCacheDir(), "temp")));
                    Uri uri = Uri.fromFile(file);
                    String imgDecodableString = uri.getPath();

                    presenter.updateFoto(imgDecodableString, uri);
                }

            } else {
                Log.i("ERROR", "No elegiste una img");
            }
        } catch (Exception e) {
            Log.i("ERROR", "error inesperado");
            Log.i("error", e.getMessage());
        }
    }

    @OnClick(R.id.imgEditarInfoPerfil) void goToUpdateActivity(){
        startActivity(new Intent(getActivity(), UpdateDatosActivity.class));
    }

    @OnClick(R.id.imgLogOut) void cerrarSesion(){

        /*androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog3, null);
        builder.setView(v);
        //builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo3);
        txt.setText("¿Cerrar sesión?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo3);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo3);

        btnAceptar.setOnClickListener(
            v1 -> {
                manager.logout();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                dialog.dismiss();
            }
        );

        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();*/

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog_settings, null);
        builder.setView(v);

        MaterialButton btnConfiguracion = v.findViewById(R.id.btnSettings);
        MaterialButton btnCerrarSesion = v.findViewById(R.id.btnCerrarSesion);

        btnConfiguracion.setOnClickListener(
                v1 -> {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    dialog.dismiss();
                }
        );

        btnCerrarSesion.setOnClickListener(
                v1 -> {
                    manager.logout();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    dialog.dismiss();
                }
        );


        dialog = builder.create();
        dialog.show();

    }
}
