package com.example.jesus.tumusicoideal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Jesus on 10/07/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MusicoViewHolder>
{
    List<Musico> musicos;

    RVAdapter(List<Musico> musicos){
        this.musicos = musicos;
    }

    @Override
    public MusicoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.musico_wrapper, parent, false);
        MusicoViewHolder mvh = new MusicoViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MusicoViewHolder holder, int position)
    {
        holder.nombre.setText(musicos.get(position).getNombreCompleto());
        holder.telefono.setText(musicos.get(position).getTelefono());

    }

    @Override
    public int getItemCount()
    {
        return musicos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class MusicoViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView nombre;
        TextView telefono;

        public MusicoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.musicoWrapper);
            nombre = (TextView) itemView.findViewById(R.id.nombreCompleto);
            telefono = (TextView) itemView.findViewById(R.id.telefonoMusico);
        }

    }

}
