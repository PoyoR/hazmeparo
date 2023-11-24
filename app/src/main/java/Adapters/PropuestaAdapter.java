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
import com.hazmeparo.Propuestas.Interface.PropuestasPresenter;
import com.hazmeparo.R;

import java.util.ArrayList;

import Models.Propuesta;

public class PropuestaAdapter extends RecyclerView.Adapter<PropuestaAdapter.PropuestaViewHolder>{

    private ArrayList<Propuesta> items;
    PropuestasPresenter presenter;
    Context ctx;

    public PropuestaAdapter(ArrayList<Propuesta> items, PropuestasPresenter presenter, Context ctx) {
        this.items = items;
        this.presenter = presenter;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PropuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lista_propuestas, parent, false);
        PropuestaViewHolder viewHolder = new PropuestaViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropuestaViewHolder holder, int position) {
        Propuesta propuesta = items.get(position);

        holder.txtCompiNombre.setText(propuesta.getCompi());
        holder.txtTransporte.setText(propuesta.getTransporte_compi());
        holder.txtCalif.setText(propuesta.getCalif_compi());
        holder.txtTiempo.setText("Tiempo estimado " + propuesta.getTiempo_estimado() + " min");
        holder.txtCosto.setText("$ " + propuesta.getCosto());


        if (propuesta.getPago_centrega().equals("true")){
            holder.imageView.setImageResource(R.drawable.ic_si);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_no);
        }

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
                    Intent i = new Intent(ctx, PerfilActivity.class);
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

        TextView txtCompiNombre, txtTransporte, txtCalif, txtTiempo, txtCosto;
        ImageView imageView, imgCompi;
        MaterialButton btnAceptar;

        public PropuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCompiNombre = itemView.findViewById(R.id.txtCompiPropuesta);
            txtTransporte = itemView.findViewById(R.id.txtTransporteCompiPropuesta);
            txtCalif = itemView.findViewById(R.id.txtCalifCompiPropuesta);
            txtTiempo = itemView.findViewById(R.id.txtTiempoEstimadoPropuesta);
            txtCosto = itemView.findViewById(R.id.txtPrecioPropuesta);
            imageView = itemView.findViewById(R.id.imgViewPagoEntregaPropuesta);
            btnAceptar = itemView.findViewById(R.id.btnAceptarPropuesta);
            imgCompi = itemView.findViewById(R.id.imgCompiPropuesta);
        }

        void bind(int index){

        }
    }


}
