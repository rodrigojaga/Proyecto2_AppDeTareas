package com.example.apptareas;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<viewHolder> {

    List<datos> ListaObjeto;

    public Adaptador(List<datos> listaObjeto){
        this.ListaObjeto = listaObjeto;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new viewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.titulo.setText(ListaObjeto.get(position).getTitulo());
        Bitmap imagen = ListaObjeto.get(position).getImagen1();
        holder.imagen.setImageBitmap(imagen);

        holder.btnUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Toast.makeText(v.getContext(),"Eliminar este item No."+(Integer.parseInt(String.valueOf(holder.getAdapterPosition()))+1),Toast.LENGTH_SHORT).show();
                    //mListener.onItemClick(holder.getAdapterPosition());
                   //ListaObjeto.remove(holder.getAdapterPosition());

                }
            }
        });

        holder.btnDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Editar este item No."+(Integer.parseInt(String.valueOf(holder.getAdapterPosition()))+1),Toast.LENGTH_SHORT).show();


            }
        });

        holder.btnTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Completado este item No."+(Integer.parseInt(String.valueOf(holder.getAdapterPosition()))+1),Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return ListaObjeto.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



}
