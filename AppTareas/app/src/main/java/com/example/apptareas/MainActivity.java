package com.example.apptareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adaptador.OnItemClickListener {



    static ArrayList<datos> lista = new ArrayList<>();;
    RecyclerView contenedor;
    LinearLayoutManager layout;
    Adaptador adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contenedor = findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);
        layout = new LinearLayoutManager(getApplicationContext());
        //layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedor.setLayoutManager(layout);

        obtenerDatos(this);
        adapter = new Adaptador(lista);

        //contenedor.setAdapter(new Adaptador(lista));
        contenedor.setAdapter(adapter);

        //contenedor.setLayoutManager(layout);

        //obtenerDatos(this);
        cargarTareas();
        //contenedor.setAdapter(new Adaptador(lista));

        //Adaptador adapter = new Adaptador(lista);
        adapter.setOnItemClickListener(this);
        //contenedor.setAdapter(adapter);



    }


    public void siguiente(View view){
        Intent siguiente = new Intent(this, agregarTarea.class);
        startActivity(siguiente);
    }


    public void lista(String titulo, Bitmap imagen){
        //datos dt = new datos(titulo,0,imagen);
        lista.add(new datos(titulo,0,imagen));
        //adapter.notifyItemInserted(lista.size()-1);


    }

    private void obtenerDatos(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);

        // Obtener los datos
        String titulo = sharedPreferences.getString("titulo", "");
        String descripcion = sharedPreferences.getString("descripcion", "");
        String imagenString = sharedPreferences.getString("imagen", "");


        Bitmap imagen = base64ToBitmap(imagenString);


        lista(titulo,imagen);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    private void cargarTareas() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferencesJson", Context.MODE_PRIVATE);

        String jsonTareas = sharedPreferences.getString("lista_tareas", "");
        if (!jsonTareas.isEmpty()) {
            Gson gson = new Gson();
            Type tipoListaTareas = new TypeToken<ArrayList<datos>>() {}.getType();

            for(int i=0;i>lista.size();i++){
                lista = gson.fromJson(jsonTareas, tipoListaTareas);
                contenedor.setAdapter(new Adaptador(lista));
            }

        }
    }






    @Override
    public void onItemClick(int position) {
        //lista.remove(position);

            if(!lista.isEmpty()){

                lista.remove(position);
                adapter.notifyItemRemoved(position);
            }

    }
}