package com.example.apptareas;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<viewHolder> {

    List<datos> ListaObjeto;
    boolean variablePer = false;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    String task;
    
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
        holder.id1.setText(String.valueOf(ListaObjeto.get(position).getEstado()));
        Bitmap imagen = ListaObjeto.get(position).getImagen1();
        holder.imagen.setImageBitmap(imagen);

        holder.btnUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Toast.makeText(v.getContext(), "Eliminar este item No." + (Integer.parseInt(String.valueOf(holder.getAdapterPosition())) + 1), Toast.LENGTH_SHORT).show();
                    databaseHelper = new DatabaseHelper(v.getContext());
                    database = databaseHelper.getWritableDatabase();
                    ListaObjeto.remove(holder.getAdapterPosition());
                    //Toast.makeText(v.getContext(), holder.id1.getText().toString(), Toast.LENGTH_SHORT).show();
                    deleteTask(Integer.parseInt(holder.id1.getText().toString()));


                    Toast.makeText(v.getContext(), "Eliminado correctamente" + (Integer.parseInt(String.valueOf(holder.getAdapterPosition())) + 1), Toast.LENGTH_SHORT).show();
                    variablePer = true;
                }catch(Exception e){
                    Log.d("a",e.toString());
                    Toast.makeText(v.getContext(),e.toString(),Toast.LENGTH_SHORT).show();}
            }
        });

        holder.btnDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, agregarTarea.class);
                String id = holder.id1.getText().toString().trim();
                String titulo = holder.titulo.getText().toString().trim();
                String desc = ListaObjeto.get(position).getDescripcion();
                String img = bitmapToBase64(ListaObjeto.get(position).getImagen1());
                intent.putExtra("Message_Id_key",id);
                intent.putExtra("Message_Boolean_key",true);
                intent.putExtra("Message_Title_key",titulo);
                intent.putExtra("Message_Description_key",desc);
                intent.putExtra("Message_Image_key",img);

                context.startActivity(intent);


            }
        });

        holder.btnTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Toast.makeText(v.getContext(), "Eliminar este item No." + (Integer.parseInt(String.valueOf(holder.getAdapterPosition())) + 1), Toast.LENGTH_SHORT).show();
                    databaseHelper = new DatabaseHelper(v.getContext());
                    database = databaseHelper.getWritableDatabase();
                    ListaObjeto.remove(holder.getAdapterPosition());
                    //Toast.makeText(v.getContext(), holder.id1.getText().toString(), Toast.LENGTH_SHORT).show();
                    deleteTask(Integer.parseInt(holder.id1.getText().toString()));
                    Toast.makeText(v.getContext(), "Completado correctamente" + (Integer.parseInt(String.valueOf(holder.getAdapterPosition())) + 1), Toast.LENGTH_SHORT).show();
                    variablePer = true;
                }catch(Exception e){
                    Log.d("a",e.toString());
                    Toast.makeText(v.getContext(),e.toString(),Toast.LENGTH_SHORT).show();}

            }
        });

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),ListaObjeto.get(position).getDescripcion(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void deleteTask(int id){

        String selection = DatabaseHelper.ColumnID +" = ?";
        String[] selectionArguments = {String.valueOf(id)};
        database.delete(DatabaseHelper.TableName,selection,selectionArguments);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
