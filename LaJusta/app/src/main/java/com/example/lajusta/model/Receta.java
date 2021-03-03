package com.example.lajusta.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;

public class Receta {
    public String nombre;
    public String tiempoPreparacion;
    public ArrayList<String> ingredientes;
    public String preparacion;
    public int imagen;

    public Receta(){
    }

    public Receta(String nombre, String tiempoPreparacion, ArrayList<String> ingredientes, String preparacion, int imagen) {
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.ingredientes = ingredientes;
        this.preparacion = preparacion;
        this.imagen=imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(String tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
