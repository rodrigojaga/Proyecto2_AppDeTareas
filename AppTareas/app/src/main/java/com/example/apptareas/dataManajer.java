package com.example.apptareas;

public class dataManajer {

    private static dataManajer instance;

    private String tituloTrans;

    private datos dt;

    private dataManajer(){

    }

    public static dataManajer getInstance(){
        if(instance == null){
            instance = new dataManajer();
        }
        return instance;
    }

    public datos getDatos(){
        return dt;
    }

    public void setDatos(datos da){
        dt = da;
    }

    public void setTituloTrans(String data){
        tituloTrans = data;
    }

}
