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

    Button btnUno,btnDos,btnTres;
    ImageView imagen;
    TextView titulo, id1;

    public List<datos> ListaObjeto;

    public viewHolder(@NonNull View itemView) {
        super(itemView);

        btnUno = itemView.findViewById(R.id.btnDelete);
        btnDos = itemView.findViewById(R.id.btnEditar);
        btnTres = itemView.findViewById(R.id.btnCompletado);
        imagen = itemView.findViewById(R.id.imagen);
        titulo = itemView.findViewById(R.id.texto);
        id1 = itemView.findViewById(R.id.idTarea111);


    }

    @Override
    public void onClick(View v) {

        int position = getAdapterPosition();
        datos objeto = ListaObjeto.get(position);
        if(v.getId() == btnUno.getId()){
            Toast.makeText(btnUno.getContext(),"Remover este item No. "+position,Toast.LENGTH_SHORT).show();

        }
        if(v.getId() == btnDos.getId()){
            Toast.makeText(btnDos.getContext(),"Editar este item No."+position,Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == btnTres.getId()){
            Toast.makeText(btnDos.getContext(),"Completado este item No."+position,Toast.LENGTH_SHORT).show();
        }


    }
}
