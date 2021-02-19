package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.lajusta.model.ProductoEnCarrito;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivityInicio extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);

        Button compra = this.findViewById(R.id.botonComprar);
        Button recetas = this.findViewById(R.id.botonRecetas);
        Button masInformacion = this.findViewById(R.id.botonMasInformacion);

        compra.setOnClickListener(v -> {
            Intent i = new Intent(ActivityInicio.this, ActivityCompra.class);
            startActivity(i);
        });

        recetas.setOnClickListener(v -> {
            Intent i = new Intent(ActivityInicio.this, ActivityReceta.class);
            startActivity(i);
        });

        masInformacion.setOnClickListener(v -> startActivity(new Intent(ActivityInicio.this,ActivityMasInfo.class)));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


