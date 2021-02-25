 package com.example.lajusta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lajusta.model.Carrito;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Image;
import com.example.lajusta.model.Product;
import com.example.lajusta.model.ProductoEnCarrito;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

 public class CustomExpListViewAdapter extends BaseExpandableListAdapter implements Filterable{

    private ArrayList<Category> listCategories;
    private ArrayList<Category> listFilter;
    private Context context;
    private HashMap<Integer,ArrayList<Product>> prod;
    private ArrayList<CartProduct> carrito;
    private TextView saldoGastado;
    private boolean signed_off;

    public CustomExpListViewAdapter(ArrayList<Category> listCategories, HashMap<Integer,ArrayList<Product>> prod, ArrayList<CartProduct> carrito, TextView totalParcial, boolean signed_off, Context context) {
        this.listCategories = listCategories;
        this.listFilter = listCategories;
        this.prod=prod;
        this.context = context;
        this.carrito= carrito;
        this.saldoGastado=totalParcial;
        this.signed_off=signed_off;
    }

    @Override
    public int getGroupCount() {
        return listFilter.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return prod.get(listFilter.get(groupPosition).getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
            return listFilter.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
            return prod.get(listFilter.get(groupPosition).getId()).get(childPosition);
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.categorias, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.categorias);
        text.setText(categoria.getName());

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //obtiene el producto
        Product producto = (Product) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.productos, null);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(producto.getDescription()!=null){
                    Toast.makeText(context.getApplicationContext(),producto.getDescription(),Toast.LENGTH_LONG).show();
                }*/
                Intent i = new Intent(context, ActivityVerProducto.class);
                Gson gson = new Gson();
                String jsonProducto = gson.toJson(producto);
                i.putExtra("JSON_PRODUCTO",jsonProducto);
                context.startActivity(i);
            }
        });

        //obtiene los elementos de la vista
        TextView nombre = (TextView) convertView.findViewById(R.id.nombreNodo);
        TextView descripcion = (TextView) convertView.findViewById(R.id.descripcionNodo);
        TextView precio = (TextView) convertView.findViewById(R.id.precio);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);
        ImageButton sumar = (ImageButton) convertView.findViewById(R.id.botonSumar);
        ImageButton restar = (ImageButton) convertView.findViewById(R.id.botonRestar);
        TextView cantidad = (TextView) convertView.findViewById(R.id.cantidadProducto);

        if(signed_off) {
            sumar.setVisibility(View.GONE);
            restar.setVisibility(View.GONE);
            cantidad.setVisibility(View.GONE);
        }

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

        //setea la cantidad seleccionada del producto
        boolean hayCantidad = false;
        for (CartProduct p : carrito) {
            if (p.getProduct().equals(producto)) {
                cantidad.setText(String.valueOf(p.getQuantity()));
                hayCantidad = true;
            }
        }
        if (hayCantidad == false) {
            cantidad.setText("0");
        }

        //onClick para cuando apreta en agregar producto
        sumar.setOnClickListener(v -> {
            CartProduct c = this.agregarProducto(producto);
            cantidad.setText(String.valueOf(c.getQuantity()));
            saldoGastado.setText("Total Parcial $" + String.valueOf(this.precioCarrito()));
            Toast.makeText(context.getApplicationContext(), producto.getTitle() + " agregado exitosamente", Toast.LENGTH_SHORT).show();
        });

        //onClick para cuando apreta en eliminar producto
        restar.setOnClickListener(v -> {
            CartProduct productoAEliminar = this.retirarProducto(producto);
            if (productoAEliminar != null) {
                Toast.makeText(context.getApplicationContext(), producto.getTitle() + " retirado exitosamente", Toast.LENGTH_SHORT).show();
                saldoGastado.setText("Total Parcial $" + String.valueOf(this.precioCarrito()));
                cantidad.setText(String.valueOf(productoAEliminar.getQuantity()));
            }
        });

        //Datos del producto Titulo,Precio y Descripcion
        nombre.setText(producto.getTitle());
        if(producto.getDescription()!=null){
            if(producto.getDescription().length()>40){
                descripcion.setText(producto.getDescription().substring(0,39));
            }
            else{
                descripcion.setText(producto.getDescription());
            }
        }
        String price = String.valueOf(producto.getPrice());
        precio.setText("Precio: $" + price);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = listCategories.size();
                    filterResults.values = listCategories;
                }
                else{
                    String searchStr = constraint.toString().toLowerCase();
                    ArrayList<Category> resultData = new ArrayList<>();
                    for(Category c:listCategories){
                        if(c.getName().toLowerCase().contains(searchStr)){
                            resultData.add(c);
                        }
                    }
                    filterResults.count= resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFilter= (ArrayList<Category>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

     public CartProduct agregarProducto(Product p){
         CartProduct productoADevolver= new CartProduct();
         boolean estaEnCarrito=false;
         for(CartProduct cp:carrito){
             if(cp.getProduct().equals(p)){
                 productoADevolver=cp;
                 cp.setQuantity(cp.getQuantity()+1);
                 estaEnCarrito=true;
                 break;
             }
         }
         if(!estaEnCarrito){
             productoADevolver.setProduct(p);
             productoADevolver.setQuantity(1);
             carrito.add(productoADevolver);
         }
         return productoADevolver;
     }

     public double precioCarrito(){
         double total=0;
         for(CartProduct c: carrito){
             total+=(c.getProduct().getPrice()*c.getQuantity());
         }
         return total;
     }

     public CartProduct retirarProducto(Product p){
         for(CartProduct c:carrito){
             if(c.getProduct()==p){
                 CartProduct retorno = c;
                 c.restar();
                 if(c.getQuantity()==0){
                     carrito.remove(c);
                 }
                 return retorno;
             }
         }
         return null;
     }
}

