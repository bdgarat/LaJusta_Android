package com.example.lajusta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lajusta.model.Product;
import com.example.lajusta.model.ProductoEnCarrito;

import java.util.ArrayList;

public class CustomAdapterCarrito extends BaseAdapter {

    private ArrayList<ProductoEnCarrito> productosCarrito;
    private double totalCarrito;
    private Context context;
    private int mResource;
    private TextView nombre;
    private TextView precio;
    private TextView cantidad;
    private TextView subtotal;

    public CustomAdapterCarrito(ArrayList<ProductoEnCarrito> productosCarrito, double totalCarrito, Context context, int mResource) {
        this.productosCarrito = productosCarrito;
        this.totalCarrito = totalCarrito;
        this.context = context;
        this.mResource= mResource;
    }

    @Override
    public int getCount() {
        return productosCarrito.size();
    }

    @Override
    public ProductoEnCarrito getItem(int position) {
        return productosCarrito.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product producto = getItem(position).getProducto();
        int cantidad = getItem(position).getCantidad();

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.producto_en_carrito,null);

        TextView nombreP = (TextView) convertView.findViewById(R.id.nombreProducto);
        nombreP.setText(producto.getTitle());

        TextView cantidadP = (TextView) convertView.findViewById(R.id.cantidadProducto);
        cantidadP.setText("Unidades a comprar: "+String.valueOf(cantidad));

        TextView precioP = (TextView) convertView.findViewById(R.id.precioU);
        precioP.setText("Precio p/unidad $"+String.valueOf(producto.getPrice()));

        TextView total = (TextView) convertView.findViewById(R.id.subtotal);
        total.setText("Subtotal: $"+String.valueOf(producto.getPrice()*cantidad));

        return convertView;

    }
}