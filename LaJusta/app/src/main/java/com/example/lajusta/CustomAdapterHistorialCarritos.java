package com.example.lajusta;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.lajusta.model.CartComplete;

import java.time.ZonedDateTime;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartComplete cart = (CartComplete) getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.carrito_en_historial,null);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityMostrarCarritoComprado.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("cart_id",cart.getId());
                context.startActivity(i);
            }
        });

        ImageView imagen = convertView.findViewById(R.id.imagen);
        imagen.setImageResource(R.drawable.carrito2);
        TextView fecha = convertView.findViewById(R.id.fechaCompra);
        ZonedDateTime zdtSaleDate = ZonedDateTime.parse(cart.getSaleDate());
        fecha.setText("Fecha de Compra: "+zdtSaleDate.getDayOfMonth() + " de " + zdtSaleDate.getMonth() + " del " + zdtSaleDate.getYear() + ", a las " + zdtSaleDate.getHour() + ":" + zdtSaleDate.getMinute());
        TextView gasto = convertView.findViewById(R.id.precioCompra);
        gasto.setText(String.valueOf("Total Compra: $"+cart.getTotal()));
        TextView cantP = convertView.findViewById(R.id.cantidadArticulos);
        cantP.setText(String.valueOf("ID de compra: "+cart.getId()));
        TextView nodo = convertView.findViewById(R.id.nodoDondeRetiro);
        nodo.setText("Nodo retiro: "+cart.getNodeDate().getNode().getName());
        return convertView;
    }
}
