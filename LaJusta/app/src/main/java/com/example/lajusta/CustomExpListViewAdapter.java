 package com.example.lajusta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lajusta.model.Carrito;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Product;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomExpListViewAdapter extends BaseExpandableListAdapter {

    private ArrayList<Category> listCategories;
    private Context context;
    private HashMap<Integer,ArrayList<Product>> prod;
    private Carrito carrito;
    private TextView saldoGastado;

    public CustomExpListViewAdapter(ArrayList<Category> listCategories, HashMap<Integer,ArrayList<Product>> prod, Carrito carrito, TextView totalParcial, Context context) {
        this.listCategories = listCategories;
        this.prod=prod;
        this.context = context;
        this.carrito= carrito;
        this.saldoGastado=totalParcial;
    }

    @Override
    public int getGroupCount() {
        return listCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.prod.get(this.listCategories.get(groupPosition).getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return prod.get(listCategories.get(groupPosition).getId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category categoria = (Category) getGroup(groupPosition);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.categorias,null);

        TextView text = (TextView) convertView.findViewById(R.id.categorias);
        text.setText(categoria.getName());
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            //obtiene el producto
            Product producto = (Product) getChild(groupPosition,childPosition);

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.productos,null);

            //obtiene los elementos de la vista
            TextView nombre= (TextView) convertView.findViewById(R.id.nombre);
            TextView descripcion = (TextView) convertView.findViewById(R.id.descripcion);
            TextView precio = (TextView) convertView.findViewById(R.id.precio);
            ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);
            Button sumar = (Button) convertView.findViewById(R.id.botonSumar);
            Button restar = (Button) convertView.findViewById(R.id.botonRestar);
            TextView cantidad = (TextView) convertView.findViewById(R.id.cantidadProducto);

            //obtiene la imagen a partir de base64
            if(producto.getImages().length>0) {
            String base64Str = producto.getImage(0).getValue();
            String base64Image = base64Str.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagen.setImageBitmap(decoded);
            }

            //setea la cantidad seleccionada del producto
            if(carrito.getProductos().containsKey(producto)==true){
                cantidad.setText(String.valueOf(carrito.getProductos().get(producto)));
            }
            else{
                cantidad.setText("0");
            }

        sumar.setOnClickListener(v -> {
            carrito.agregarProducto(producto);
            cantidad.setText(String.valueOf(carrito.getProductos().get(producto)));
            saldoGastado.setText("Total Parcial $"+String.valueOf(carrito.calcularPrecio()));
            Toast.makeText(context.getApplicationContext(),producto.getTitle()+" agregado exitosamente",Toast.LENGTH_SHORT).show();
        });

        restar.setOnClickListener(v -> {
            if (carrito.retirarProducto(producto)) {
                if (carrito.getProductos().containsKey(producto)) {
                    cantidad.setText(String.valueOf(carrito.getProductos().get(producto)));
                } else {
                    cantidad.setText("0");
                }
                Toast.makeText(context.getApplicationContext(),producto.getTitle()+" retirado exitosamente",Toast.LENGTH_SHORT).show();
                saldoGastado.setText("Total Parcial $" + String.valueOf(carrito.calcularPrecio()));
            }
        });

        //Datos del producto Titulo,Precio y Descripcion
        nombre.setText(producto.getTitle());
        descripcion.setText(producto.getDescription());
        String price = String.valueOf(producto.getPrice());
        precio.setText("Precio: $"+price);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

