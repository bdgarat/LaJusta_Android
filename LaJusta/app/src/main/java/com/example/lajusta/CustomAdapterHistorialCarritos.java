package com.example.lajusta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lajusta.model.CartComplete;

import java.util.ArrayList;

public class CustomAdapterHistorialCarritos extends BaseAdapter {
    private ArrayList<CartComplete> carritos;
    private Context context;
    private int mResource;

    public CustomAdapterHistorialCarritos(ArrayList<CartComplete> carritos, Context context, int mResource){
        this.carritos=carritos;
        this.context=context;
        this.mResource=mResource;
    }

    @Override
    public int getCount() {
        return carritos.size();
    }

    @Override
    public Object getItem(int position) {
        return carritos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartComplete cart = (CartComplete) getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.carrito_en_historial,null);
        ImageView imagen = convertView.findViewById(R.id.imagen);
        imagen.setImageResource(R.drawable.carrito2);
        TextView fecha = convertView.findViewById(R.id.fechaCompra);
        fecha.setText("Fecha de Compra: "+cart.getSaleDate());
        TextView gasto = convertView.findViewById(R.id.precioCompra);
        gasto.setText(String.valueOf("Total Compra: $"+cart.getTotal()));
        TextView cantP = convertView.findViewById(R.id.cantidadArticulos);
        cantP.setText(String.valueOf("Cantidad de Productos Distintos: "+cart.getCartProducts().length));
        TextView nodo = convertView.findViewById(R.id.nodoDondeRetiro);
        nodo.setText("Nodo retiro: "+cart.getNodeDate().getNode().getName());
        return convertView;
    }
}
