package com.example.jesus.tumusicoideal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesus on 11/07/2017.
 */

public class RVInstrumentosAdapter extends RecyclerView.Adapter<RVInstrumentosAdapter.InstrumentoViewHolder> {

    List<String> instrumentos;

    RVInstrumentosAdapter(List<String> intrumentos){
        this.instrumentos = intrumentos;
    }

    @Override
    public InstrumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.instrumento_wrapper, parent, false);
        InstrumentoViewHolder ivh = new InstrumentoViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(InstrumentoViewHolder holder, int position) {
        holder.nombre.setText(instrumentos.get(position));
    }

    @Override
    public int getItemCount() {
        return instrumentos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class InstrumentoViewHolder extends RecyclerView.ViewHolder{
        CardView cartita;
        TextView nombre;

        public InstrumentoViewHolder(View itemView) {
            super(itemView);
            cartita = (CardView) itemView.findViewById(R.id.cartitaCV);
            nombre = (TextView) itemView.findViewById(R.id.nombreInstrumento);

        }
    }
}
