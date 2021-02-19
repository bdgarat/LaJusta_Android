package com.example.lajusta.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ProductoEnCarrito implements Parcelable{

    public Product producto;
    public int cantidad;
    public Cart cart;
    public boolean isCanceled;
    public int id;

    public ProductoEnCarrito(Product producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    public ProductoEnCarrito() {
    }

    protected ProductoEnCarrito(Parcel in) {
        cantidad = in.readInt();
        producto = (Product) in.readSerializable();
    }

    public static final Parcelable.Creator<ProductoEnCarrito> CREATOR = new Parcelable.Creator<ProductoEnCarrito>() {
        @Override
        public ProductoEnCarrito createFromParcel(Parcel in) {
            return new ProductoEnCarrito(in);
        }

        @Override
        public ProductoEnCarrito[] newArray(int size) {
            return new ProductoEnCarrito[size];
        }
    };

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cantidad);
        Bundle bundle = new Bundle();
        bundle.putSerializable("producto",producto);
        dest.writeBundle(bundle);
    }
}
