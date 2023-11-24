package Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.hazmeparo.Perfil.View.PerfilActivity;
import com.hazmeparo.PerfilEsp.View.PerfilEspActivity;
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;
import com.hazmeparo.Propuestas.Interface.PropuestasServicioPresenter;
import com.hazmeparo.R;

import java.util.ArrayList;

import Models.Propuesta;
import Models.PropuestaServicio;

public class PropuestaServicioAdapter extends RecyclerView.Adapter<PropuestaServicioAdapter.PropuestaViewHolder>{

    private ArrayList<PropuestaServicio> items;
    PropuestasServicioPresenter presenter;
    Context ctx;

    public PropuestaServicioAdapter(ArrayList<PropuestaServicio> items, PropuestasServicioPresenter presenter, Context ctx) {
        this.items = items;
        this.presenter = presenter;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PropuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lista_propuestas_servicio, parent, false);
        PropuestaViewHolder viewHolder = new PropuestaViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropuestaViewHolder holder, int position) {
        PropuestaServicio propuesta = items.get(position);

        holder.txtCompiNombre.setText(propuesta.getCompi());
        holder.txtCalif.setText(propuesta.getCalif_compi());
        holder.txtComentario.setText(propuesta.getComentario());
        holder.txtCosto.setText("$ " + propuesta.getCosto());


        if (propuesta.getImg() != null) {
            Glide.with(ctx)
                    .load(propuesta.getImg())
                    .apply(RequestOptions.circleCropTransform())
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imgCompi);

            holder.imgCompi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ctx, PerfilEspActivity.class);
                    i.putExtra("compi", propuesta.getNumero_compi());

                    ctx.startActivity(i);
                }
            });

        }else{
            Glide.with(ctx).clear(holder.imgCompi);
            holder.imgCompi.setImageDrawable(null);
        }

        holder.btnAceptar.setOnClickListener(v -> {
            presenter.showDialog("¿Aceptar esta propuesta?", propuesta.getNumero_compi(), propuesta.getKey());
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PropuestaViewHolder extends RecyclerView.ViewHolder{

        TextView txtCompiNombre, txtCalif, txtComentario, txtCosto;
        ImageView imgCompi;
        MaterialButton btnAceptar;

        public PropuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCompiNombre = itemView.findViewById(R.id.txtCompiPropuestaServicio);

            txtCalif = itemView.findViewById(R.id.txtCalifCompiPropuestaServicio);
            txtComentario = itemView.findViewById(R.id.txtComentarioPropuestaServicio);
            txtCosto = itemView.findViewById(R.id.txtPrecioPropuestaServicio);
            btnAceptar = itemView.findViewById(R.id.btnAceptarPropuestaServicio);
            imgCompi = itemView.findViewById(R.id.imgCompiPropuestaServicio);
        }

        void bind(int index){

        }
    }


}
