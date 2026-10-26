package com.example.listaandres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listaandres.Motos;
import com.example.listaandres.R;

import java.util.ArrayList;
public class AdaptadorMotos
        extends RecyclerView.Adapter<AdaptadorMotos.ViewHolder> {
    // La lista de datos que va a pintar
    private final ArrayList<Motos> motos;
    public AdaptadorMotos(ArrayList<Motos> motos) {
        this.motos = motos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fila = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_moto, parent, false);
        return new ViewHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Motos moto = motos.get(position);
        holder.tvTitulo.setText(moto.getMarca());
        holder.tvSubtitulo.setText(moto.getModelo());
        holder.ivImagen.setImageResource(moto.getImagen());
    }

    @Override
    public int getItemCount() {
        return motos.size();
    }

    // El ViewHolder guarda las vistas de UNA fila
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvTitulo;
        TextView tvSubtitulo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
        }
    }
}