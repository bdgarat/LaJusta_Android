package com.example.lajusta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartComplete;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.CartsHistorial;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPerfil  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        TextView nombreUsuario = findViewById(R.id.textDescripcionProducto);
        TextView apellidoUsuario = findViewById(R.id.textPrecioProducto);
        TextView emailUsuario = findViewById(R.id.textPerfilEmail);
        TextView telefonoUsuario = findViewById(R.id.textPerfilTelefono);
        Button cerrarSesion = findViewById(R.id.buttonSignOff);
        Button historialCarrito = findViewById(R.id.historialPedidos);
        Button recuperarContra = findViewById(R.id.buttonRecuperarContrase├▒a);
        ImageButton atras = findViewById(R.id.botonAtras);

        boolean errorPipe = getIntent().getBooleanExtra("errorPipe",false);
        if(errorPipe){

        }

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioToken = gson.fromJson(json,Token.class);

        nombreUsuario.setText(usuarioToken.user.getFirstName());
        apellidoUsuario.setText(usuarioToken.user.getLastName());
        emailUsuario.setText(usuarioToken.user.getEmail());
        telefonoUsuario.setText(usuarioToken.user.getPhone());

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cerrarSesion.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("SignedIn", false);
            editor.putString("nombreUsuario", "");
            editor.putString("apellidoUsuario", "");
            editor.putString("usuarioToken", "");
            String vaciarCarro = gson.toJson(new ArrayList<CartProduct>());
            editor.putString("compras",vaciarCarro);
            editor.apply();
            Intent i = new Intent(ActivityPerfil.this, ActivityMain.class);
            startActivity(i);
            Toast.makeText(ActivityPerfil.this,"Ha cerrado su sesion correctamente",Toast.LENGTH_SHORT).show();
        });

        historialCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityPerfil.this,ActivityHistorialCompras.class);
                startActivity(i);
            }
        });

        recuperarContra.setOnClickListener(v -> startActivity(new Intent(ActivityPerfil.this, ActivityCambiarContrase├▒a.class)));

    }
}
