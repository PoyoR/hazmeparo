package com.hazmeparo.Inicio.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Inicio.Interface.ServicioActivoPresenter;
import com.hazmeparo.Inicio.Interface.ServicioActivoView;
import com.hazmeparo.Inicio.Presenter.ServicioActivoPresenterImpl;
import com.hazmeparo.Propuestas.View.PropuestasActivity;
import com.hazmeparo.Propuestas.View.PropuestasServicioActivity;
import com.hazmeparo.R;

import Clases.PrefsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServicioActivoFragment extends Fragment implements ServicioActivoView {

    public ServicioActivoFragment() {
        // Required empty public constructor
    }

    ServicioActivoPresenter presenter;
    PrefsManager manager;

    private androidx.appcompat.app.AlertDialog dialog;

    //Favor activo
    @BindView(R.id.imgViewServicio) ImageView imgServicio;
    @BindView(R.id.txtStatusServicio) TextView txtStatus;
    @BindView(R.id.txtTituloServicio) TextView txtTitulo;
    @BindView(R.id.txtDescripcionServicio) TextView txtDescripcion;
    @BindView(R.id.txtHoraServicio) TextView txtHora;
    @BindView(R.id.txtDireccionServicio) TextView txtDireccion;
    @BindView(R.id.txtPropuestasServicio) TextView txtPropuestas;
    @BindView(R.id.layoutStatusServicio) LinearLayout layoutStatus;
    @BindView(R.id.btnverPropuestasServicio) MaterialButton btnVerPropuestas;

    @BindView(R.id.txtCancelacionServicioActivo) TextView txtCancelacion;

    //Layout btns servicio en curso
    @BindView(R.id.layoutBtnsServicioCurso) LinearLayout layoutBtnsEnCurso;

    @BindView(R.id.btnCerrarServicio) MaterialButton btnCerrarServicio;

    //Cancelar favor antes status 1
    @BindView(R.id.btnCancelarServicio) MaterialButton btnCancelarServicio;

    //Compi elegido
    @BindView(R.id.layoutCompiElegidoServicio) LinearLayout layouCompiElegido;
    @BindView(R.id.txtCompiElegidoServicio) TextView txtCompiElegido;
    @BindView(R.id.txtNumeroCompiElegidoServicio) TextView txtNumeroCompiElegido;
    @BindView(R.id.txtComentarioCompiElegidoServicio) TextView txtComentarioCompiElegido;
    @BindView(R.id.txtCalifCompiElegidoServicio) TextView txtCalifCompiElegido;
    @BindView(R.id.txtPrecioCompiElegidoServicio) TextView txtPrecioCompiElegido;

    @BindView(R.id.txtCodigoFinalizacion) TextView txtCodigo;

    private String keyServicio;
    private String compiId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicio_activo, container, false);
        ButterKnife.bind(this, view);

        manager = new PrefsManager(getActivity());
        presenter = new ServicioActivoPresenterImpl(this, manager);

        presenter.getServicio(manager.obtenerValorString("id_usuario"), manager.obtenerValorString("servicioCategoria"));

        return view;
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
    public void setServicio(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String motivo_cancelacion, String codigo) {

        keyServicio = key;

        if (status.equals("1")) {

            txtStatus.setText("Publicado");
            layoutStatus.setBackgroundColor(Color.parseColor("#FF00E676"));

            btnCancelarServicio.setVisibility(View.VISIBLE);
            layoutBtnsEnCurso.setVisibility(View.GONE);
            btnCerrarServicio.setVisibility(View.GONE);
            layouCompiElegido.setVisibility(View.GONE);
        }else if (status.equals("2")) {

            presenter.getCompiElegido(key);

            txtStatus.setText("En curso");
            layoutStatus.setBackgroundColor(Color.parseColor("#FF00B0FF"));

            btnCancelarServicio.setVisibility(View.GONE);
            layoutBtnsEnCurso.setVisibility(View.VISIBLE);
            btnCerrarServicio.setVisibility(View.GONE);
            layouCompiElegido.setVisibility(View.VISIBLE);
            txtCodigo.setText("Código finalización: "+ codigo);
        } else if (status.equals("3")) {

            presenter.getCompiElegido(key);

            txtStatus.setText("Finalizado");
            layoutStatus.setBackgroundColor(Color.parseColor("#FF0097A7"));


            btnCancelarServicio.setVisibility(View.GONE);
            layoutBtnsEnCurso.setVisibility(View.GONE);
            layouCompiElegido.setVisibility(View.GONE);
            btnCerrarServicio.setVisibility(View.VISIBLE);
            txtCodigo.setVisibility(View.GONE);

        } else{

            presenter.getCompiElegido(key);

            txtStatus.setText("Cancelado");
            layoutStatus.setBackgroundColor(Color.parseColor("#B90A0A"));

            btnCancelarServicio.setVisibility(View.GONE);
            layoutBtnsEnCurso.setVisibility(View.GONE);
            btnCerrarServicio.setVisibility(View.VISIBLE);
            txtCancelacion.setVisibility(View.VISIBLE);
            txtCancelacion.setText(motivo_cancelacion);
            txtCodigo.setVisibility(View.GONE);
        }

        txtTitulo.setText(titulo);
        txtDescripcion.setText(descripcion);
        txtHora.setText("Hora deseada: "+hora + " hrs.");
        txtDireccion.setText(direccion);

        if (!img.equals("")){
            if (isAdded()) {
                Glide.with(getActivity())
                        .load(img)
                        .override(500, 500)
                        .fitCenter()
                        //.centerCrop()
                        .into(imgServicio);
            }
        }
    }

    @Override
    public void setCompiElegido(String id, String nombre, String calif, String numero, String comentario, String precio) {
        compiId = id;
        Log.i("USUARIO ID", "es "+id);
        txtCompiElegido.setText(nombre);
        txtCalifCompiElegido.setText(calif);
        txtNumeroCompiElegido.setText(numero);
        txtComentarioCompiElegido.setText(comentario);
        txtPrecioCompiElegido.setText("$"+precio);
    }

    @Override
    public void setTotalPropuestas(String total, String status, boolean exist) {
        if (exist) {
            txtPropuestas.setText(total);
            txtPropuestas.setVisibility(View.VISIBLE);
            btnVerPropuestas.setVisibility(View.VISIBLE);
        }else {
            txtPropuestas.setVisibility(View.GONE);
            btnVerPropuestas.setVisibility(View.GONE);
        }
    }

    @Override
    public void codigoFinalizacionIncorrecto() {
        Toast.makeText(getActivity(),"Código incorrecto", Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToInicio() {
        manager.guardarValorBoolean("servicioActivo", false);
        manager.guardarValorString("servicioCategoria", "");

        if (getActivity() != null) {
            Fragment fragment = new SolicitarFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @OnClick(R.id.btnverPropuestasServicio) void verPropuestas(){
        Intent intent = new Intent(getActivity(), PropuestasServicioActivity.class);
        intent.putExtra("servicio", keyServicio);
        startActivity(intent);
    }


    @OnClick(R.id.btnCancelarServicio) void cancelarServicio(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog3, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo3);
        txt.setText("¿Seguro que desea cancelar este servicio?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo3);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo3);

        btnAceptar.setOnClickListener(
                v1 -> {
                    presenter.cancelarServicio(keyServicio); //Pone en status 0 el favor y actualiza en server
                    presenter.finalizarServicio(keyServicio); //Eliminar child e img asociada
                    dialog.dismiss();
                }
        );

        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.btnCancelarServicioActivo) void cancelarServicioActivo(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog2, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo2);
        TextInputLayout txtMotivo = v.findViewById(R.id.txtInputMotivoCancelacionFavor);

        txt.setText("¿Este favor aún no ha sido completado, seguro que desea cancelarlo?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo2);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo2);

        btnAceptar.setOnClickListener(
                v1 -> {
                    if (!txtMotivo.getEditText().getText().toString().equals("")){
                        presenter.cancelarServicioActivo(keyServicio, txtMotivo.getEditText().getText().toString());
                        dialog.dismiss();
                    }else{
                        txtMotivo.setError("Ingresa un motivo");
                    }
                }
        );

        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.btnServicioTerminado) void servicioTerminado(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog5, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo5);
        txt.setText("¿El servicio ha sido terminado?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo5);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo5);

        TextInputLayout txtCodigo = v.findViewById(R.id.txtInputCodigoFinalizacion);

        btnAceptar.setOnClickListener(
                v1 -> {
                    String codigo = txtCodigo.getEditText().getText().toString();
                    if (!codigo.equals("")){
                        presenter.verificarCodigoFinalizacion(keyServicio, codigo);
                        dialog.dismiss();
                    }else{
                        txtCodigo.setError("Ingresa un código");
                    }
                }
        );


        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();

    }

    @OnClick(R.id.btnCerrarServicio) void cerrarServicio(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.calif_dialog, null);
        builder.setView(v);
        builder.setCancelable(true);

        TextView txtTitulo = v.findViewById(R.id.txtTituloCalif);
        txtTitulo.setText("Por favor deja tu calificacion para este compi");
        MaterialButton btnAceptar = v.findViewById(R.id.btnEnviarCalificacion);
        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
        TextInputLayout txtComentario = v.findViewById(R.id.txtInputComentarioCalif);

        btnAceptar.setOnClickListener(v1 -> {

            if (!txtComentario.getEditText().getText().toString().equals("")){
                presenter.finalizarServicioIniciado(keyServicio); //Eliminar el favor de firebase
                presenter.enviarCalificacionCompi(ratingBar.getRating(), txtComentario.getEditText().getText().toString(), compiId);
                dialog.dismiss();
            }else{
                txtComentario.setError("Ingrese un comentario");
            }

        });

        dialog = builder.create();
        dialog.show();

    }
}