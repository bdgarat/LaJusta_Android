package com.example.lajusta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Carrito{
    public User usuario;
    public ArrayList<ProductoEnCarrito> productos;

    public Carrito() {
        this.productos = new ArrayList<>();
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public void setProductos(ArrayList<ProductoEnCarrito> productos) {
        this.productos = productos;
    }

    public ArrayList<ProductoEnCarrito> getProductos(){
        return productos;
    }

    public ProductoEnCarrito agregarProducto(Product p){
        ProductoEnCarrito productoADevolver = new ProductoEnCarrito();
        boolean estaEnCarrito=false;
        for(ProductoEnCarrito product:productos){
            if(product.getProducto().equals(p)){
                productoADevolver=product;
                product.setCantidad(product.getCantidad()+1);
                estaEnCarrito=true;
            }
        }
        if(!estaEnCarrito){
            productoADevolver = new ProductoEnCarrito(p,1);
            productos.add(productoADevolver);
        }
        return productoADevolver;
    }

    public ProductoEnCarrito retirarProducto(Product p) {
        for (ProductoEnCarrito product : productos) {
            if (product.getProducto().equals(p)) {
                if (product.getCantidad() > 1) {
                    product.setCantidad(product.getCantidad() - 1);
                    return product;
                } else {
                    if (product.getCantidad() == 1) {
                        productos.remove(product);
                        product.setCantidad(0);
                        return product;
                    }
                }
            }
        }
        return null;
    }

       public double calcularPrecio(){
           double total=0;
           for(ProductoEnCarrito product: productos){
               total+=(product.getCantidad()*product.getProducto().getPrice());
           }
           return total;
       }
    }
