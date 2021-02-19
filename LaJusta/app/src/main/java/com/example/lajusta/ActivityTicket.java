package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityTicket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView total = (TextView) this.findViewById(R.id.totalCompra);
        double totalDelCarrito = this.getIntent().getDoubleExtra("total",0);
        int numero = (int)(Math.random()*999999999+100000);
        TextView codigo = this.findViewById(R.id.codigoCompra);
        codigo.setText("Su codigo de compra es #"+String.valueOf(numero));
        total.setText("$"+String.valueOf(totalDelCarrito));
        TextView lugarRetiro = findViewById(R.id.lugarRetiro);
        String nodoHarcodeado="Nodo harcodeado";
        lugarRetiro.setText("Retire su compra en el nodo: "+nodoHarcodeado);
        Button volver = (Button) this.findViewById(R.id.botonVolverInicio);
        volver.setOnClickListener(v -> {
            Toast.makeText(this.getApplicationContext(),"Compra realizada exitosamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityTicket.this, ActivityInicio.class);
            startActivity(i);
        });
    }
}