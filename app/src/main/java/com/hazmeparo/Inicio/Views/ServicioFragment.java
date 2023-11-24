package com.hazmeparo.Inicio.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hazmeparo.Inicio.Interface.ServicioPresenter;
import com.hazmeparo.Inicio.Interface.ServicioView;
import com.hazmeparo.Inicio.Presenter.ServicioPresenterImpl;
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

public class ServicioFragment extends Fragment implements ServicioView {

    public ServicioFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.scrollViewServicio) NestedScrollView mainScrollView;
    @BindView(R.id.transparent_imageServicio) ImageView transparentImageView;

    private androidx.appcompat.app.AlertDialog dialog;

    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;

    AnstronCoreHelper anstronCoreHelper;
    DialogCargando carga;

    //Calendario para obtener fecha & hora
    private final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @BindView(R.id.layoutSolicitarServicio) LinearLayout layoutSolicitarServicio;
    @BindView(R.id.spinnerCategoriaServicio) Spinner spinnerServicio;
    @BindView(R.id.txtInputTitutoServicio) TextInputLayout txtTitulo;
    @BindView(R.id.txtInputDescripcionServicio) TextInputLayout  txtDescripcion;
    @BindView(R.id.edtInputHoraServicio) TextInputEditText txtHora;
    @BindView(R.id.txtInputDireccionServicio) TextInputLayout  txtDireccion;
    @BindView(R.id.imgServicio) ImageView imagenServicio;
    Uri imagen_producto;

    PrefsManager manager;
    File file;
    Uri img_seleccionada;
    String imgDecodableString;

    GoogleMap map;
    LatLng ubicacionMapa;

    ServicioPresenter presenter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicio, container, false);
        ButterKnife.bind(this, view);

        manager = new PrefsManager(getActivity());

        if (manager.obtenerValorBoolean("servicioActivo")){
            goToServicioActivo();
        }

        carga = new DialogCargando(getActivity());

        getDireccion();

        presenter = new ServicioPresenterImpl(this, manager);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapSolicitarServicio);

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

    private void getDireccion() {
        String direccion = manager.obtenerValorString("direccion");
        txtDireccion.getEditText().setText(direccion);
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
    public void showProgress() {
        carga.showDialog();
    }

    @Override
    public void hideProgress() {
        carga.hideDialog();
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
    public void goToServicioActivo() {
        Fragment fragment = new ServicioActivoFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @OnClick(R.id.btnImgServicio) void seleccionar(){
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
                    file = new File(SiliCompressor.with(getActivity()).compress(FileUtils.getPath(getActivity(), selectedImage), new File(getActivity().getCacheDir(), "temp")));
                    Uri uri = Uri.fromFile(file);
                    imgDecodableString = uri.getPath();

                    img_seleccionada = uri;
                    imagenServicio.setImageURI(uri);
                }

            } else {
                Log.i("ERROR", "No elegiste una img");
            }
        } catch (Exception e) {
            Log.i("ERROR", "error inesperado");
            Log.i("error", e.getMessage());
        }
    }

    @OnClick(R.id.edtInputHoraServicio) void obtener(){

        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {

            String horaFormateada =  (hourOfDay < 10) ? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);

            String minutoFormateado = (minute < 10) ? String.valueOf("0" + minute):String.valueOf(minute);

            txtHora.setText(horaFormateada + ":" + minutoFormateado);
        }, hora, minuto, true);

        recogerHora.show();
    }

    @OnClick(R.id.btnSolicitarServicio) void solicitar(){
        String titulo = txtTitulo.getEditText().getText().toString();
        String descripcion = txtDescripcion.getEditText().getText().toString();
        String hora = txtHora.getText().toString();
        String direccion = txtDireccion.getEditText().getText().toString();
        String path = imgDecodableString;
        String token = manager.obtenerValorString("token");
        String idUser = manager.obtenerValorString("id_usuario");
        String nom_usuario = manager.obtenerValorString("nom_usuario");
        String ubicacion = "0,0";

        String categoria = spinnerServicio.getSelectedItem().toString();
        if (ubicacionMapa != null)
            ubicacion = ubicacionMapa.latitude + "," + ubicacionMapa.longitude;

        presenter.solicitarFavor(idUser, token, nom_usuario, categoria, titulo, descripcion, hora, direccion, path, img_seleccionada, ubicacion);

        /*//Limpiar edits
        txtTitulo.getEditText().setText("");
        txtDescripcion.getEditText().setText("");
        txtHora.setText("");
        imagenServicio.setImageResource(0);
        path = "";*/
    }
}