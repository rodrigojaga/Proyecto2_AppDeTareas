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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    Button btn1;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        contenedor = findViewById(R.id.contenedor);
        contenedor.setHasFixedSize(true);
        layout = new LinearLayoutManager(getApplicationContext());
        //layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedor.setLayoutManager(layout);

        obtenerDatos(this);
        readTask();
        adapter = new Adaptador(lista);

        //contenedor.setAdapter(new Adaptador(lista));
        contenedor.setAdapter(adapter);

        //contenedor.setLayoutManager(layout);

        //obtenerDatos(this);
        cargarTareas();
        //contenedor.setAdapter(new Adaptador(lista));
        boolean variable = adapter.variablePer;
        //Adaptador adapter = new Adaptador(lista);
        adapter.setOnItemClickListener(this);
        //contenedor.setAdapter(adapter);
        btn1 = findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashPoint();
            }
        });


    }

    public void flashPoint(){
        Intent siguiente = new Intent(this, MainActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(siguiente);
        overridePendingTransition(0,0);

    }

    public void siguiente(View view){
        Intent siguiente = new Intent(this, agregarTarea.class);

        startActivity(siguiente);
    }


    public void lista(String titulo, Bitmap imagen,int id,String desc){
        lista.add(new datos(titulo,desc,id,imagen));
    }

    private void obtenerDatos(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);

        // Obtener los datos
        String titulo = sharedPreferences.getString("titulo", "");
        String descripcion = sharedPreferences.getString("descripcion", "");
        String imagenString = sharedPreferences.getString("imagen", "");


        imagen = base64ToBitmap(imagenString);


        //lista(titulo,imagen);
    }


    private void readTask(){

        String[] projection = {DatabaseHelper.ColumnID,DatabaseHelper.ColumnTask,DatabaseHelper.ColumnImg,DatabaseHelper.ColumnDesc};

        Cursor cursor = database.query(DatabaseHelper.TableName, projection,null,null,null,null,null);

        String name = "";
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ColumnID));
                name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ColumnTask));
                String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ColumnDesc));
                String img = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ColumnImg));
                Bitmap imgHD = base64ToBitmap(img);
                lista(name,imgHD,id,desc);

            }while(cursor.moveToNext());

        }

        cursor.close();
    }

    private void readTask1(){
        String[] projection = {DatabaseHelper.ColumnID};
        Cursor cursor = databaseHelper.getAllData();
        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ColumnID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ColumnTask));
            // Puedes obtener otros valores de columnas aquí

            stringBuilder.append("ID: ").append(id).append(", Name: ").append(name).append("\n");
            Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
        }





        cursor.close();
    }


    public void deleteTask(){
        String selection = DatabaseHelper.ColumnID +" = ?";
        String[] selectionArguments = {"1"};
        database.delete(DatabaseHelper.TableName,selection,selectionArguments);
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
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
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