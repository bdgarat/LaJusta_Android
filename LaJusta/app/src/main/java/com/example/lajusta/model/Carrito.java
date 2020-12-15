package com.example.lajusta.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class Carrito{
    private HashMap<Product,Integer> productos;

    public Carrito(){
        productos = new HashMap();
    }

    public HashMap<Product, Integer> getProductos() {
        return productos;
    }

    public void agregarProducto(Product p){
        if (!productos.containsKey(p)) {
            productos.put(p,1);
        }
        else{
            productos.replace(p,productos.get(p),productos.get(p)+1);
        }
    }
    public boolean retirarProducto(Product p){
            if(productos.containsKey(p)) {

                if (productos.get(p) > 1) {
                    productos.replace(p,productos.get(p),productos.get(p)-1);
                    return true;
                }
                if(productos.get(p).intValue()==1){
                    productos.remove(p);
                }
                return true;
            }
            return false;
    }
       public double calcularPrecio(){
           double total=0;
           for(Map.Entry<Product,Integer> producto: productos.entrySet()){
               total+= (producto.getKey().getPrice()* producto.getValue().intValue());
           }
           return total;
       }
    }
