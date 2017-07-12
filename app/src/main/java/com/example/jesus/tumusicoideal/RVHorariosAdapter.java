package com.example.jesus.tumusicoideal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jesus on 11/07/2017.
 */

public class RVHorariosAdapter extends RecyclerView.Adapter<RVHorariosAdapter.HorarioViewHolder>
{

    List<Horario_data> horarios;

    RVHorariosAdapter(List<Horario_data> horarios)
    {
        this.horarios = horarios;
    }

    @Override
    public HorarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horario_wrapper, parent, false);
        HorarioViewHolder hvh = new HorarioViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(HorarioViewHolder holder, int position) {
        holder.dia.setText(horarios.get(position).getDia());
        holder.horario_inicial.setText("De "+horarios.get(position).getHora_inicial());
        holder.horario_final.setText("Hasta "+horarios.get(position).getHora_final());
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder
    {
        CardView carta;
        TextView dia;
        TextView horario_inicial;
        TextView horario_final;

        public HorarioViewHolder(View itemView)
        {
            super(itemView);

            carta = (CardView) itemView.findViewById(R.id.horario_wrapper);
            dia = (TextView) itemView.findViewById(R.id.dia);
            horario_inicial = (TextView) itemView.findViewById(R.id.horarioInicial);
            horario_final = (TextView) itemView.findViewById(R.id.horarioFinal);
        }
    }
}
