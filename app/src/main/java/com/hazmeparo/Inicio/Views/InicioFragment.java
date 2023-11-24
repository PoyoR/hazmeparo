package com.hazmeparo.Inicio.Views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Inicio.Interface.InicioPresenter;
import com.hazmeparo.Inicio.Interface.InicioView;
import com.hazmeparo.Inicio.Presenter.InicioPresenterImpl;
import com.hazmeparo.Propuestas.View.PropuestasActivity;
import com.hazmeparo.R;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.Calendar;

import Clases.DialogCargando;
import Clases.PrefsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class InicioFragment extends Fragment implements InicioView {


    public InicioFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.scrollViewInicio) NestedScrollView mainScrollView;
    @BindView(R.id.transparent_image) ImageView transparentImageView;
    @BindView(R.id.transparent_image1) ImageView transparentImageView1;
    @BindView(R.id.transparent_image2) ImageView transparentImageView2;

    private androidx.appcompat.app.AlertDialog dialog;

    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment, supportMapFragment2;

    AnstronCoreHelper anstronCoreHelper;
    DialogCargando carga;

    //Calendario para obtener fecha & hora
    private final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @BindView(R.id.layoutPedirFavor) LinearLayout layoutPedirFavor;
    @BindView(R.id.txtInputTitulo) TextInputLayout  txtTitulo;
    @BindView(R.id.txtInputDescripcion) TextInputLayout  txtDescripcion;
    @BindView(R.id.edtInputHora) TextInputEditText txtHora;
    @BindView(R.id.txtInputDireccionCompra) TextInputLayout txtDireccionCompra;
    @BindView(R.id.txtInputDireccionDestino) TextInputLayout  txtDireccion;
    @BindView(R.id.imgProducto) ImageView imagenProducto;
    Uri imagen_producto;

    //Favor activo
    @BindView(R.id.layoutFavorActivo) LinearLayout layoutFavorActivo;
    @BindView(R.id.imgViewFavorActivo) ImageView imgFavorActivo;
    @BindView(R.id.txtStatusFavorActivo) TextView txtStatusFavorActivo;
    @BindView(R.id.txtTituloFavorActivo) TextView txtTituloFavorActivo;
    @BindView(R.id.txtDescripcionFavorActivo) TextView txtDescripcionFavorActivo;
    @BindView(R.id.txtHoraFavorActivo) TextView txtHoraFavorActivo;
    @BindView(R.id.txtDireccionFavorActivo) TextView txtDirFavorActivo;
    @BindView(R.id.txtPropuestasFavorActivo) TextView txtPropuestasFavorActivo;
    @BindView(R.id.layoutStatusFavor) LinearLayout layoutStatus;
    @BindView(R.id.btnverPropuestasFavorActivo) MaterialButton btnVerPropuestas;
    @BindView(R.id.imgFlechaAbajo) ImageView imgFlecha;
    @BindView(R.id.txtMsgStatusFavorActivo) TextView txtMsgCompi;
    @BindView(R.id.txtCancelacionFavorActivo) TextView txtCancelacion;
    @BindView(R.id.txtCodigoFinalizacionFavor) TextView txtCodigo;

    //Compi elegido
    @BindView(R.id.layoutCompiElegido) LinearLayout layouCompiElegido;
    @BindView(R.id.txtCompiElegido) TextView txtCompiElegido;
    @BindView(R.id.txtNumeroCompiElegido) TextView txtNumeroCompiElegido;
    @BindView(R.id.txtTransporteCompiElegido) TextView txtTransporteCompiElegido;
    @BindView(R.id.txtCalifCompiElegido) TextView txtCalifCompiElegido;
    @BindView(R.id.txtTiempoEstimadoCompiElegido) TextView txtTiempoCompiElegido;
    @BindView(R.id.txtPrecioCompiElegido) TextView txtPrecioCompiElegido;
    @BindView(R.id.imgViewPagoEntregaCompiElegido) ImageView imgPagoEntregaCompiElegido;

    @BindView(R.id.layoutExtraFavor) LinearLayout layoutExtraFavor;

    @BindView(R.id.btnFinalizarFavor) MaterialButton btnTerminar;
    @BindView(R.id.btnCancelarFavor) MaterialButton btnCancelar;
    @BindView(R.id.btnCancelarFavor2) MaterialButton btnCancelar2;

    @BindView(R.id.txtRecuerda) TextView txtRecuerda;
    @BindView(R.id.btnFinalizarFavor2) MaterialButton btnCerrar;

    String compiId;

    private InicioPresenter presenter;
    PrefsManager manager;
    String favorActivoKey = "";
    String imgDecodableString;
    File file;
    Uri img_seleccionada;

    GoogleMap map, map2;
    LatLng ubicacionMapa, ubicacionMapa2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, view);
        manager = new PrefsManager(getActivity());
        presenter = new InicioPresenterImpl(this, manager);
        carga = new DialogCargando(getActivity());
        anstronCoreHelper = new AnstronCoreHelper(getActivity());

        getDireccion();

        presenter.getFavores(manager.obtenerValorString("id_usuario"));

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapSolicitarFavor);
        supportMapFragment2 = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapSolicitarFavor2);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        getUbicacion();

        transparentImageView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        });

        transparentImageView1.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        });

        transparentImageView2.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        });

        return view;
    }

    private void getUbicacion(){
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    ubicacionMapa = new LatLng(location.getLatitude(), location.getLongitude());
                                    Log.i("POSICION", "es: "+ubicacionMapa);
                                }else{
                                    Log.i("NO POSICION", "nmo hay");
                                }

                                if (location != null){
                                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @SuppressLint("MissingPermission")
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            map = googleMap;
                                            map.getUiSettings().setZoomControlsEnabled(true);
                                            map.getUiSettings().setZoomGesturesEnabled(true);
                                            ubicacionMapa = new LatLng(location.getLatitude(), location.getLongitude());
                                            MarkerOptions markerOptions = new MarkerOptions().position(ubicacionMapa);
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionMapa, 15));
                                            map.addMarker(markerOptions);
                                            map.setMyLocationEnabled(true);

                                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                @Override
                                                public void onMapClick(LatLng point) {
                                                    map.clear();
                                                    map.addMarker(new MarkerOptions().position(point));
                                                    ubicacionMapa = point;
                                                }
                                            });
                                        }
                                    });

                                    supportMapFragment2.getMapAsync(new OnMapReadyCallback() {
                                        @SuppressLint("MissingPermission")
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            map2 = googleMap;
                                            map2.getUiSettings().setZoomControlsEnabled(true);
                                            map2.getUiSettings().setZoomGesturesEnabled(true);
                                            ubicacionMapa2 = new LatLng(location.getLatitude(), location.getLongitude());
                                            MarkerOptions markerOptions = new MarkerOptions().position(ubicacionMapa2);
                                            map2.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionMapa2, 15));
                                            map2.addMarker(markerOptions);
                                            map2.setMyLocationEnabled(true);

                                            map2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                @Override
                                                public void onMapClick(LatLng point) {
                                                    map2.clear();
                                                    map2.addMarker(new MarkerOptions().position(point));
                                                    ubicacionMapa2 = point;
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showMsg("Este permiso es necesario para continuar");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.edtInputHora) void obtener(){

        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {

            String horaFormateada =  (hourOfDay < 10) ? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);

            String minutoFormateado = (minute < 10) ? String.valueOf("0" + minute):String.valueOf(minute);

            txtHora.setText(horaFormateada + ":" + minutoFormateado);
        }, hora, minuto, true);

        recogerHora.show();
    }

    @OnClick(R.id.btnImgProducto) void seleccionar(){
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

    @OnClick(R.id.btnSolicitarFavor) void enviarFavor(){
        String titulo = txtTitulo.getEditText().getText().toString();
        String descripcion = txtDescripcion.getEditText().getText().toString();
        String hora = txtHora.getText().toString();
        String direccionCompra = txtDireccionCompra.getEditText().getText().toString();
        String direccion = txtDireccion.getEditText().getText().toString();
        String path = imgDecodableString;
        String token = manager.obtenerValorString("token");
        String idUser = manager.obtenerValorString("id_usuario");
        String nom_usuario = manager.obtenerValorString("nom_usuario");
        String ubicacion = "0,0";
        String ubicacion2 = "0,0";
        if (ubicacionMapa != null)
            ubicacion = ubicacionMapa.latitude + "," + ubicacionMapa.longitude;

        if (ubicacionMapa2 != null)
            ubicacion2 = ubicacionMapa2.latitude + "," + ubicacionMapa2.longitude;

        presenter.publicarFavor(idUser, token, nom_usuario, titulo, descripcion, hora, direccionCompra, direccion, path, img_seleccionada, ubicacion, ubicacion2);

        //Limpiar edits
        txtTitulo.getEditText().setText("");
        txtDescripcion.getEditText().setText("");
        txtHora.setText("");
        txtDireccionCompra.getEditText().setText("");
        imagenProducto.setImageResource(0);
        path = "";


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
                    file = new File(SiliCompressor.with(getActivity()).compress(FileUtils.getPath(getActivity(), selectedImage), new File(getActivity().getCacheDir(), "temp")));
                    Uri uri = Uri.fromFile(file);
                    imgDecodableString = uri.getPath();

                    /*String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();*/

                    img_seleccionada = uri;
                    imagenProducto.setImageURI(uri);
                }

            } else {
                Log.i("ERROR", "No elegiste una img");
            }
        } catch (Exception e) {
            Log.i("ERROR", "error inesperado");
            Log.i("error", e.getMessage());
        }
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
    public void showDialog(String msg, boolean correcto) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsg);
        txt.setText(msg);
        MaterialButton btn = v.findViewById(R.id.btnCerrardialogo);

        if (correcto) {
            btn.setOnClickListener(
                v1 -> {
                    dialog.dismiss();
                }
            );

        }else{
            btn.setOnClickListener(
                v12 -> dialog.dismiss()
            );
        }

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setTituloError() {
        txtTitulo.setError("Por favor ingresa el objetivo del favor");
    }

    @Override
    public void setDescripcionError() {
        txtDescripcion.setError("Por favor ingresa una descripción del favor");
    }

    @Override
    public void setHoraError() {
        txtHora.setError("Por favor selecciona una hora deseada");
    }

    @Override
    public void setDireccionError() {
        txtDireccion.setError("Por favor ingresa una dirección");
    }

    @Override
    public void setTotalPropuestas(String total,  String status, boolean exist) {
        if (exist) {
            txtPropuestasFavorActivo.setText(total);
            txtPropuestasFavorActivo.setVisibility(View.VISIBLE);
            btnVerPropuestas.setVisibility(View.VISIBLE);
        }else {
            txtPropuestasFavorActivo.setVisibility(View.GONE);
            btnVerPropuestas.setVisibility(View.GONE);
        }

    }

    @Override
    public void favorPublicado() {

        try {
            file.delete();
        }
        catch(Exception e) {
        }
    }

    @Override
    public void setFavor(String key, String status, String titulo, String descripcion, String hora, String direccion, String img, String msg_compi, String motivo_cancelacion, String codigo) {
        favorActivoKey = key;

        if (status.equals("1")){
            txtStatusFavorActivo.setText("Publicado");
            layoutStatus.setBackgroundColor(Color.parseColor("#FF00E676"));
            imgFlecha.setVisibility(View.GONE);
            layouCompiElegido.setVisibility(View.GONE);
            txtRecuerda.setVisibility(View.GONE);
            btnCerrar.setVisibility(View.GONE);
            btnCancelar2.setVisibility(View.VISIBLE);
        }else if (status.equals("2")){
            presenter.getCompiElegido(key);
            txtStatusFavorActivo.setText("En curso");
            layoutStatus.setBackgroundColor(Color.parseColor("#FF00B0FF"));
            imgFlecha.setVisibility(View.VISIBLE);
            layouCompiElegido.setVisibility(View.VISIBLE);
            btnTerminar.setVisibility(View.VISIBLE);
            btnCancelar.setVisibility(View.VISIBLE);
            txtRecuerda.setVisibility(View.GONE);
            btnCerrar.setVisibility(View.GONE);
            btnCancelar2.setVisibility(View.GONE);
            presenter.getPosicionCompi(favorActivoKey);
            layoutExtraFavor.setVisibility(View.VISIBLE);
            txtMsgCompi.setText(msg_compi);
            if (isAdded())
                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapPosicionCompi);

            txtPropuestasFavorActivo.setVisibility(View.GONE);
            btnVerPropuestas.setVisibility(View.GONE);
            txtCodigo.setText("Código de finalización: "+codigo);
        }else if (status.equals("3")){
            txtStatusFavorActivo.setText("Finalizado");
            presenter.getCompiElegido(key);
            layoutStatus.setBackgroundColor(Color.parseColor("#FF0097A7"));
            imgFlecha.setVisibility(View.VISIBLE);
            layouCompiElegido.setVisibility(View.VISIBLE);
            btnTerminar.setVisibility(View.GONE);
            btnCancelar.setVisibility(View.GONE);
            txtRecuerda.setVisibility(View.VISIBLE);
            btnCerrar.setVisibility(View.VISIBLE);
            btnCancelar2.setVisibility(View.GONE);
            layoutExtraFavor.setVisibility(View.GONE);
            txtPropuestasFavorActivo.setVisibility(View.GONE);
            btnVerPropuestas.setVisibility(View.GONE);
            txtCodigo.setVisibility(View.GONE);
        }else{
            txtStatusFavorActivo.setText("Cancelado");
            presenter.getCompiElegido(key);
            layoutStatus.setBackgroundColor(Color.parseColor("#B90A0A"));
            imgFlecha.setVisibility(View.VISIBLE);
            layouCompiElegido.setVisibility(View.VISIBLE);
            btnTerminar.setVisibility(View.GONE);
            btnCancelar.setVisibility(View.GONE);
            btnCerrar.setVisibility(View.VISIBLE);
            txtRecuerda.setVisibility(View.VISIBLE);
            btnCancelar2.setVisibility(View.GONE);
            layoutExtraFavor.setVisibility(View.GONE);

            txtCancelacion.setVisibility(View.VISIBLE);
            txtCancelacion.setText(motivo_cancelacion);

            txtPropuestasFavorActivo.setVisibility(View.GONE);
            btnVerPropuestas.setVisibility(View.GONE);
            txtCodigo.setVisibility(View.GONE);
        }

        txtTituloFavorActivo.setText(titulo);
        txtDescripcionFavorActivo.setText(descripcion);
        txtHoraFavorActivo.setText("Hora deseada: "+hora + " hrs.");
        txtDirFavorActivo.setText(direccion);

        if (!img.equals("")){
            if (isAdded()) {
                Glide.with(getActivity())
                        .load(img)
                        .override(500, 500)
                        .fitCenter()
                        //.centerCrop()
                        .into(imgFavorActivo);
            }
        }

    }

    @Override
    public void mostrarLayout(String layout) {
        if (layout.equals("Activo")){
            layoutPedirFavor.setVisibility(View.GONE);
            layoutFavorActivo.setVisibility(View.VISIBLE);
        }else{
            layoutFavorActivo.setVisibility(View.GONE);
            layoutPedirFavor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCompiElegido(String id, String nombre, String calif, String numero, String transporte, String pago, String tiempo, String precio) {
        compiId = id;
        txtCompiElegido.setText(nombre);
        txtCalifCompiElegido.setText(calif);
        txtNumeroCompiElegido.setText(numero);
        txtTransporteCompiElegido.setText(transporte);
        txtTiempoCompiElegido.setText("Tiempo estimado: " +tiempo+ " min.");
        txtPrecioCompiElegido.setText("$"+precio);

        if (pago.equals("true")){
            imgPagoEntregaCompiElegido.setImageResource(R.drawable.ic_si);
        }else{
            imgPagoEntregaCompiElegido.setImageResource(R.drawable.ic_no);
        }
    }

    @Override
    public void getDireccion() {
        String direccion = manager.obtenerValorString("direccion");
        txtDireccion.getEditText().setText(direccion);
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
    public void crearNotificacion() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean notifs = preferences.getBoolean("notificaciones", true);
        Log.i("NOTIFICACIONES ACTIVAS", "" + notifs);

        if (preferences.getBoolean("notificaciones", true)) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "1907")
                    .setSmallIcon(R.drawable.hazmelogo)
                    .setContentTitle("Popuestas activas")
                    .setContentText("Tienes propuestas activas para tu favor solicitado")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (isAdded()) {
                Intent notifyIntent = new Intent(getActivity(), MainActivity.class);
                // Set the Activity to start in a new, empty task
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Create the PendingIntent
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        getActivity(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                builder.setContentIntent(notifyPendingIntent);
                builder.setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                notificationManager.notify(1, builder.build());
            }

        }
    }

    @Override
    public void setPosicionCompi(String posicion) {

        String[] parts = posicion.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lng = Double.parseDouble(parts[1]);

        if (isAdded()) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.clear();
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);
                    LatLng ubicacion = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions().position(ubicacion);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
                    googleMap.addMarker(markerOptions);
                }
            });
        }


    }

    @Override
    public void codigoFinalizacionIncorrecto() {
        Toast.makeText(getActivity(),"Código incorrecto", Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToSolicitar() {
        try {
            hideProgress();
        }catch (Exception e){}

        if (isAdded()) {
            Fragment fragment = new SolicitarFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    @OnClick(R.id.btnverPropuestasFavorActivo) void verPropuestas(){
        Intent intent = new Intent(getActivity(), PropuestasActivity.class);
        intent.putExtra("favor", favorActivoKey);
        startActivity(intent);
    }

    @OnClick(R.id.btnFinalizarFavor) void terminarFavor(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog5, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo5);
        txt.setText("¿El favor ha sido terminado?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo5);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo5);

        TextInputLayout txtCodigo = v.findViewById(R.id.txtInputCodigoFinalizacion);

        btnAceptar.setOnClickListener(
                v1 -> {
                    String codigo = txtCodigo.getEditText().getText().toString();
                    if (!codigo.equals("")){
                        presenter.verificarCodigoFinalizacion(favorActivoKey, codigo);
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

    @OnClick(R.id.btnCancelarFavor) void cancelarFavor(){
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
                        presenter.cancelarFavorActivo(favorActivoKey, txtMotivo.getEditText().getText().toString());
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

    @OnClick(R.id.btnCancelarFavor2) void cancelarFavor2(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog2, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsgDialogo2);
        txt.setText("¿Seguro que desea cancelarlo?");
        MaterialButton btnAceptar = v.findViewById(R.id.btnAceptarDialogo2);
        MaterialButton btnCancelar = v.findViewById(R.id.btnCancelarDialogo2);

        btnAceptar.setOnClickListener(
                v1 -> {
                    presenter.cancelarFavor(favorActivoKey); //Pone en status 0 el favor y actualiza en server
                    presenter.finalizarFavor(favorActivoKey); //Eliminar child e img asociada
                    dialog.dismiss();
                }
        );

        btnCancelar.setOnClickListener(
                v12 -> dialog.dismiss()
        );


        dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.btnFinalizarFavor2) void cerrarFavor(){

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
                presenter.finalizarFavorIniciado(favorActivoKey); //Eliminar el favor de firebase
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
