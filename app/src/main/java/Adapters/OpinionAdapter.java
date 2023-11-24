package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hazmeparo.R;

import java.util.List;

import Models.Calificacion;

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.OpinionViewHolder>{

    private List<Calificacion> items;

    public OpinionAdapter(List<Calificacion> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OpinionAdapter.OpinionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lista_opiniones, parent, false);
        OpinionAdapter.OpinionViewHolder viewHolder = new OpinionAdapter.OpinionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OpinionAdapter.OpinionViewHolder holder, int position) {
        Calificacion opinion = items.get(position);

        holder.txtCalif.setText(String.valueOf(opinion.getCalificacion()).substring(0,1) );
        holder.txtComentario.setText(opinion.getComentario());
        holder.txtFecha.setText(opinion.getFecha());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OpinionViewHolder extends RecyclerView.ViewHolder{

        TextView txtCalif, txtComentario, txtFecha;

        public OpinionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCalif = itemView.findViewById(R.id.txtCalifOpinion);
            txtComentario = itemView.findViewById(R.id.txtComentarioCalif);
            txtFecha = itemView.findViewById(R.id.txtFechaCalif);
        }
    }
}
