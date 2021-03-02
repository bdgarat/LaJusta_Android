package com.example.lajusta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.model.CartProduct;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ActivityFaltaStock extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falta_stock);
        Button button = findViewById(R.id.buttonVolverACompra);
        Button button2 = findViewById(R.id.buttonNuevoNodo);
        Button button3 = findViewById(R.id.botonCancelarCompra);

        //Esto no devuelve el mensaje de que falta stock, habria que ver como obtener ese mensaje para ponerlo
        String msg = getIntent().getStringExtra("errorBody");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityFaltaStock.this,ActivityCompra.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityFaltaStock.this,ActivitySeleccionarNodo.class);
                startActivity(i);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(new ArrayList<CartProduct>());
                editor.putString("compras",json);
                editor.commit();
                Intent i = new Intent(ActivityFaltaStock.this,ActivityCompra.class);
                startActivity(i);
            }
        });
    }
}
