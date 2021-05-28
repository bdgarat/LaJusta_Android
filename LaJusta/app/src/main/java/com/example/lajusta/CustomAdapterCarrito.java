package com.example.lajusta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.Image;
import com.example.lajusta.model.Product;

import java.util.ArrayList;

public class CustomAdapterCarrito extends BaseAdapter {

    private ArrayList<CartProduct> productosCarrito;
    private double totalCarrito;
    private Context context;
    private int mResource;
    private boolean mostrarImagen;

    public CustomAdapterCarrito(ArrayList<CartProduct> productosCarrito, double totalCarrito, Context context, int mResource, boolean mostrarImagen) {
        this.productosCarrito = productosCarrito;
        this.totalCarrito = totalCarrito;
        this.context = context;
        this.mResource= mResource;
        this.mostrarImagen = mostrarImagen;
    }

    @Override
    public int getCount() {
        return productosCarrito.size();
    }

    @Override
    public CartProduct getItem(int position) {
        return productosCarrito.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product producto = getItem(position).getProduct();
        int cantidad = getItem(position).getQuantity();

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.producto_en_carrito,null);

        TextView nombreP = (TextView) convertView.findViewById(R.id.nombreProducto);
        nombreP.setText(producto.getTitle());

        TextView cantidadP = (TextView) convertView.findViewById(R.id.cantidadProducto);
        cantidadP.setText("Unidad/es: "+String.valueOf(cantidad));

        TextView precioP = (TextView) convertView.findViewById(R.id.precioU);
        precioP.setText("Precio p/unidad $"+String.valueOf(producto.getPrice()));

        TextView total = (TextView) convertView.findViewById(R.id.subtotal);
        total.setText("Subtotal: $"+String.valueOf(producto.getPrice()*cantidad));

        if(mostrarImagen) {
            ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);
            //obtiene la imagen de producto guardada en base64

            if (producto.getImages().length > 0) {
                for (Image img : producto.getImages()) {
                    String base64Str = img.getValue();
                    if (base64Str != null) {
                        String base64Image = base64Str.split(",")[1];
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imagen.setImageBitmap(decoded);
                        break;
                    }
                }
            }
        }

        return convertView;

    }
}
