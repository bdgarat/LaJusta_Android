package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button compra = this.findViewById(R.id.botonComprar);
        Button recetas = this.findViewById(R.id.botonRecetas);
        Button masInformacion = this.findViewById(R.id.botonMasInformacion);

        compra.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ActivityCompra.class);
            startActivity(i);
        });

        recetas.setOnClickListener(v -> Toast.makeText(MainActivity.this,"Esta funcionalidad todavia no fue implementada",Toast.LENGTH_SHORT).show());

        masInformacion.setOnClickListener(v -> Toast.makeText(MainActivity.this,"Esta funcionalidad todavia no fue implementada",Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


