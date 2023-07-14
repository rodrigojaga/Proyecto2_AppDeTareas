package com.example.apptareas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "AppTareas.db";

    public static final String TableName = "tareas";
    public static final String ColumnID = "_id";
    public static final String ColumnTask = "tareaTitulo";
    public static final String ColumnDesc = "descripcion";
    public static final String ColumnImg = "imagen";


    public DatabaseHelper(Context context){
        super(context,DatabaseName,null,DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create table "+TableName+"("
                +ColumnID+" INTEGER Primary Key Autoincrement,"
                +ColumnTask+" Text,"
                +ColumnImg+" Text,"
                +ColumnDesc+" TEXT) ";

//        String query = "Create table "+TableName+"("
//                +ColumnID+" INTEGER Primary Key Autoincrement,"
//                +ColumnTask+" Text) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TableName);
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+TableName, null);
    }



}
