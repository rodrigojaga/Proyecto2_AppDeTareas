package com.example.apptareas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class agregarTarea extends AppCompatActivity implements View.OnClickListener {

    Button btnTomar, btnGuardar, btnAgregar;
    ImageView iVFoto;
    Bitmap bitmap, imgGeneral;

    Drawable drawable;

    boolean xd;
    String id,tituloAntiguo, descAntiguo, imgAntigua;

    EditText etTitulo, etDescripcion;

    TextView tvEncabezado;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private ArrayList<datos> listaTareas = new ArrayList<>();


    public interface DataListener{
        void onDataReceives(String a, Bitmap b);
    }

    private DataListener dataListener;

    public void setDataListener(DataListener listener){
        dataListener = listener;
    }

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int TAKE_PICTURE = 101;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);
        initUI();


        btnTomar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();

            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            xd = bundle.getBoolean("Message_Boolean_key");
            id = bundle.getString("Message_Id_key");
            tituloAntiguo = bundle.getString("Message_Title_key");
            descAntiguo = bundle.getString("Message_Description_key");
            imgAntigua = bundle.getString("Message_Image_key");
        }

        if(xd){

            btnAgregar.setText("Actualizar");
            tvEncabezado.setText("Actualizar Tarea");
            showPast();
        }

    }

    private void initUI(){
        btnTomar = findViewById(R.id.btnFotoTomar);
        btnGuardar = findViewById(R.id.btnFotoGuardar);
        iVFoto = findViewById(R.id.imgViewFoto);
        btnAgregar = findViewById(R.id.btnAgregarTarea);
        etTitulo = findViewById(R.id.etTituloTarea);
        etDescripcion = findViewById(R.id.etDescTarea);
        tvEncabezado = findViewById(R.id.tvEncabezado);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnFotoTomar){
            checkPermissionCamera();
        }else if(id == R.id.btnFotoGuardar){
            checkPermissionStorage();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TAKE_PICTURE){
            if(resultCode == Activity.RESULT_OK && data != null){

                bitmap = (Bitmap) data.getExtras().get("data");

                iVFoto.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }else if(requestCode == REQUEST_PERMISSION_WRITE_STORAGE){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissionCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else{

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION
                );

            }
        }else{

            takePicture();


        }
    }

    private void checkPermissionStorage() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else{
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION_WRITE_STORAGE
                    );
                }
            }else{
                saveImage();
            }
        }else{
            saveImage();
        }

    }

    private void takePicture(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,TAKE_PICTURE);
        //}

    }

    private void saveImage(){
        OutputStream fos = null;
        File file = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();

            String fileName = System.currentTimeMillis()+"image_example";
            values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");

            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
            values.put(MediaStore.Images.Media.IS_PENDING,1);

            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imageUri = resolver.insert(collection,values);


            try {
                fos = resolver.openOutputStream(imageUri);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values,null,null);
        }else{
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String FileName = System.currentTimeMillis() +".jpg";

            file = new File(imageDir,FileName);


            try {
                fos = new FileOutputStream(file);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        if(saved){
            Toast.makeText(this,"La imagen se guardo con exito", Toast.LENGTH_SHORT).show();
        }

        if(fos!=null){
            try {
                fos.flush();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        if(file != null){
            MediaScannerConnection.scanFile(this,new String[]{file.toString()},null,null);
        }

    }

    public void obtenerDatos(){
        //Toast.makeText(this,"Entrada obtenerDatos",Toast.LENGTH_SHORT).show();

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();


        String tituloTare = etTitulo.getText().toString().trim();
        String des = etDescripcion.getText().toString().trim();
        Intent intent = new Intent(this, MainActivity.class);

        if(xd && btnAgregar.getText().toString().equals("Actualizar")) {

            if (!tituloTare.isEmpty() && !des.isEmpty()) {
                Drawable drawable1 = iVFoto.getDrawable();
                Bitmap bm = ((BitmapDrawable) drawable1).getBitmap();
                String imgN = bitmapToBase64(bm);
                Toast.makeText(agregarTarea.this, id, Toast.LENGTH_SHORT).show();
                UpdateTask(tituloTare,des,imgN);
                Toast.makeText(agregarTarea.this, "Se Actualizo correctamente", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            } else {
                Toast.makeText(agregarTarea.this, "No se Actualizo", Toast.LENGTH_SHORT).show();
            }

        }else{
            if (!tituloTare.isEmpty() && !des.isEmpty()) {
                String imgN = bitmapToBase64(bitmap);
                insertTask(tituloTare, des, imgN);
                Toast.makeText(agregarTarea.this, "Se ingreso correctamente", Toast.LENGTH_SHORT).show();
                etTitulo.setText("");
                etDescripcion.setText("");
                startActivity(intent);
            } else {
                Toast.makeText(agregarTarea.this, "No se ingreso", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showPast(){

        etTitulo.setText(tituloAntiguo);
        etDescripcion.setText(descAntiguo);
        Bitmap imgTemp = base64ToBitmap(imgAntigua);
        iVFoto.setImageBitmap(imgTemp);
        imgGeneral = imgTemp;

    }

    private void UpdateTask(String tasktitle,String taskDesc, String taskImg){
        //Toast.makeText(agregarTarea.this, "Actualizacion", Toast.LENGTH_SHORT).show();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ColumnTask,tasktitle);
        values.put(DatabaseHelper.ColumnDesc,taskDesc);
        values.put(DatabaseHelper.ColumnImg,taskImg);
        String selection = DatabaseHelper.ColumnID+"=?";
        String[] selectionArgs = {id};
        database.update(databaseHelper.TableName, values, selection,selectionArgs);
    }


    private void insertTask(String taskTitle, String taskDes,String taskIMG){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ColumnTask,taskTitle);
        values.put(DatabaseHelper.ColumnDesc,taskDes);
        values.put(DatabaseHelper.ColumnImg,taskIMG);

        database.insert(DatabaseHelper.TableName,null,values);
        Toast.makeText(agregarTarea.this,taskTitle+" "+taskDes,Toast.LENGTH_SHORT).show();
        //readTask();
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
//    private void readTask(){
//        String[] projection = {DatabaseHelper.ColumnTask};
//        Cursor cursor = database.query(DatabaseHelper.TableName, projection,null,null,null,null,null);
//        if(cursor.moveToFirst()){
//            do{
//                String task = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ColumnTask));
//                Toast.makeText(agregarTarea.this,task,Toast.LENGTH_SHORT).show();
//            }while(cursor.moveToNext());
//        }
//        cursor.close();
//    }

    private void guardarDatos(Context context, String titulo, String descripcion, Bitmap imagen) {
        //SharedPreferences sharedPreferences = context.getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guardar el título y la descripción como cadenas de texto
        //editor.putString("titulo", titulo);
        //editor.putString("descripcion", descripcion);

        // Convertir el bitmap a una cadena Base64
        //String imagenString = bitmapToBase64(imagen);
        //editor.putString("imagen", imagenString);

        // Guardar los cambios
        //editor.apply();
    }

    private void agregarTarea(String titulo, String descripcion, Bitmap imagen) {
        //datos tarea = new datos(titulo,0,imagen);
        //listaTareas.add(tarea);
        //guardarTareas();
    }

    public void guardarTareas() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferencesJson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonTareas = gson.toJson(listaTareas);
        editor.putString("lista_tareas", jsonTareas);

        editor.apply();
    }





    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



    public void volver(View view){
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
    }



}

