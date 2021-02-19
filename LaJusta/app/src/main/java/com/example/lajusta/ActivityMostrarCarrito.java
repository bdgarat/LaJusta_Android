package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lajusta.model.Image;
import com.example.lajusta.model.ProductoEnCarrito;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivityMostrarCarrito extends AppCompatActivity {

    public ListView listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_carrito);

        listado = (ListView) findViewById(R.id.listadoCarrito);
        ImageButton botonS = (ImageButton) findViewById(R.id.botonAtrasCarrito);
        Button botonC = (Button) findViewById(R.id.botonConfirmarC);

        botonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        double totalDelCarrito = this.getIntent().getDoubleExtra("total",0);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type type = new TypeToken<ArrayList<ProductoEnCarrito>>() {}.getType();
        ArrayList<ProductoEnCarrito> carrito = gson.fromJson(json,type);
        TextView textoTotal = (TextView) findViewById(R.id.totalCarrito);
        textoTotal.setText("Total $"+String.valueOf(totalDelCarrito));

        CustomAdapterCarrito adapter = new CustomAdapterCarrito(carrito,totalDelCarrito,this,R.layout.producto_en_carrito);
        listado.setAdapter(adapter);

        botonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMostrarCarrito.this, ActivityTicket.class);
                i.putExtra("total",totalDelCarrito);
                startActivity(i);
            }
        });

    }
}