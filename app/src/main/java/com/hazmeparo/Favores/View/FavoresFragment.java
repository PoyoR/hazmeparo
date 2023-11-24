package com.hazmeparo.Favores.View;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hazmeparo.Favores.Interfaces.FavoresPresenter;
import com.hazmeparo.Favores.Interfaces.FavoresView;
import com.hazmeparo.Favores.Presenter.FavoresPresenterImpl;
import com.hazmeparo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.FavorAdapter;
import Adapters.PropuestaAdapter;
import Clases.DialogCargando;
import Clases.PrefsManager;
import Models.Favor;
import Models.Propuesta;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoresFragment extends Fragment implements FavoresView {


    public FavoresFragment() {
        // Required empty public constructor
    }
    DialogCargando carga;

    PrefsManager manager;
    FavoresPresenter presenter;
    private androidx.appcompat.app.AlertDialog dialog;

    @BindView(R.id.recyclerFavores) RecyclerView listaFavores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favores, container, false);
        ButterKnife.bind(this, view);

        manager = new PrefsManager(getActivity());
        carga = new DialogCargando(getActivity());
        presenter = new FavoresPresenterImpl(this, manager);

        presenter.getFavores(manager.obtenerValorString("token"));
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
    public void setFavores(List<Favor> favores) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaFavores.setLayoutManager(layoutManager);

        FavorAdapter adapter = new FavorAdapter(favores, presenter, getActivity());
        listaFavores.setAdapter(adapter);

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

}
