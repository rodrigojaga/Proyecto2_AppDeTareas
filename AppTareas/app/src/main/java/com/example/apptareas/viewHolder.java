package com.example.apptareas;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    Button btnUno,btnDos;
    ImageView imagen;
    TextView titulo;

    public List<datos> ListaObjeto;

    public viewHolder(@NonNull View itemView, List<datos> datos) {
        super(itemView);

        btnUno = (Button) itemView.findViewById(R.id.btn1);
        btnDos = (Button) itemView.findViewById(R.id.btn2);
        imagen = (ImageView) itemView.findViewById(R.id.imagen);
        titulo = (TextView) itemView.findViewById(R.id.texto);

        btnUno.setOnClickListener(this);

        btnDos.setOnClickListener(this);

        ListaObjeto = datos;

    }

    @Override
    public void onClick(View v) {

        int position = getAdapterPosition();
        datos objeto = ListaObjeto.get(position);
        if(v.getId() == btnUno.getId()){
            Toast.makeText(btnUno.getContext(),"Boton 1 del item "+position+" correspondiente al titulo "+objeto.getTitulo(),Toast.LENGTH_SHORT).show();

        }
        if(v.getId() == btnDos.getId()){
            Toast.makeText(btnDos.getContext(),"Boton 2",Toast.LENGTH_SHORT).show();
        }


    }
}
