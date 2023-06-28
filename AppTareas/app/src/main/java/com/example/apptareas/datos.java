package com.example.apptareas;

import android.graphics.Bitmap;

public class datos {

    String titulo;
    //int imagen;
    int estado;

    Bitmap imagen1;

//    public datos(String titulo, int imagen, int estado) {
//        this.titulo = titulo;
//        this.imagen = imagen;
//        this.estado = estado;
//    }

    public datos(String titulo, int estado, Bitmap imagen1) {
        this.titulo = titulo;
        this.estado = estado;
        this.imagen1 = imagen1;
    }

    public datos(){

    }

    public Bitmap getImagen1() {
        return imagen1;
    }

    public void setImagen1(Bitmap imagen1) {
        this.imagen1 = imagen1;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //public int getImagen() {
      //  return imagen;
    //}

  //  public void setImagen(int imagen) {
  //      this.imagen = imagen;
  //  }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
