package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCarrito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        TextView total = (TextView) this.findViewById(R.id.totalCompra);
        double precioTotal = this.getIntent().getDoubleExtra("total",0);
        total.setText("$"+String.valueOf(precioTotal));
        Button volver = (Button) this.findViewById(R.id.botonVolverInicio);
        volver.setOnClickListener(v -> {
            Toast.makeText(this.getApplicationContext(),"Compra realizada exitosamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityCarrito.this,MainActivity.class);
            startActivity(i);
        });
    }
}