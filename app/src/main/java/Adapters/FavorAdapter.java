package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.hazmeparo.Cuenta.View.UpdateDatosActivity;
import com.hazmeparo.Favores.Interfaces.FavoresPresenter;
import com.hazmeparo.Perfil.View.PerfilActivity;
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;
import com.hazmeparo.R;

import java.util.ArrayList;
import java.util.List;

import Models.Favor;
import Models.Propuesta;

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.PropuestaViewHolder>{

    private List<Favor> items;
    FavoresPresenter presenter;
    Context ctx;

    public FavorAdapter(List<Favor> items, FavoresPresenter presenter, Context ctx) {
        this.items = items;
        this.presenter = presenter;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PropuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lista_favores, parent, false);
        PropuestaViewHolder viewHolder = new PropuestaViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropuestaViewHolder holder, int position) {
        Favor favor = items.get(position);

        holder.txtFecha.setText(favor.getFecha());
        holder.txtTitulo.setText(favor.getTitulo());
        holder.txtDescripcion.setText(favor.getDescripcion());
        holder.txtDireccion.setText(favor.getDestino());
        holder.txtCosto.setText("$ " + favor.getTotal());

        if (favor.getCompi() != null) {
            holder.txtcompi.setText(favor.getCompi().getFirstName() + " " + favor.getCompi().getLastName());

            if (favor.getCompi().getUsuario().getFoto() != null){
                Glide.with(ctx)
                        .load(favor.getCompi().getUsuario().getFoto())
                        .apply(RequestOptions.circleCropTransform())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.imgCompi);

                holder.imgCompi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ctx, PerfilActivity.class);
                        i.putExtra("compi", favor.getCompi().getUsername());

                        ctx.startActivity(i);
                    }
                });
            }

        }else {
            Glide.with(ctx).clear(holder.imgCompi);
            holder.imgCompi.setImageDrawable(null);
            holder.txtcompi.setText("");
        }

        if (favor.getStatus() == 0 || favor.getStatus() == -1) {
            holder.txtStatus.setText("Cancelado");
            holder.txtStatus.setBackgroundColor(Color.parseColor("#B90A0A"));
        }else if (favor.getStatus() == 1){
            holder.txtStatus.setText("Publicado");
            holder.txtStatus.setBackgroundColor(Color.parseColor("#FF00E676"));
        }else if (favor.getStatus() == 2){
            holder.txtStatus.setText("En curso");
            holder.txtStatus.setBackgroundColor(Color.parseColor("#FF00B0FF"));
        }
        else if (favor.getStatus() == 3){
            holder.txtStatus.setText("Finalizado");
            holder.txtStatus.setBackgroundColor(Color.parseColor("#FF0097A7"));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class PropuestaViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitulo, txtDescripcion, txtFecha, txtDireccion, txtCosto, txtStatus, txtcompi;
        ImageView imgCompi;

        public PropuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloFavor);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionFavor);
            txtFecha = itemView.findViewById(R.id.txtFechaFavor);
            txtDireccion = itemView.findViewById(R.id.txtDireccionFavor);
            txtCosto = itemView.findViewById(R.id.txtPrecioFavor);
            txtStatus = itemView.findViewById(R.id.txtStatusFavor);
            txtcompi = itemView.findViewById(R.id.txtCompiFavor);
            imgCompi = itemView.findViewById(R.id.imgCompiFavor);
        }
    }


}
