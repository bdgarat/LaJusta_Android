package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.General;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);

        TextView nombreUsuario = this.findViewById(R.id.textNombreUsuario);
        Button compra = this.findViewById(R.id.botonComprar);
        Button recetas = this.findViewById(R.id.botonRecetas);
        Button masInformacion = this.findViewById(R.id.botonMasInformacion);
        Button verPerfilUsuario = this.findViewById(R.id.botonVerPerfilUsuario);
        Button iniciarSesion = this.findViewById(R.id.botonIniciarSesion);

        if(json == "") {
            nombreUsuario.setText("No registrado");
            compra.setText("Ver Productos");
            verPerfilUsuario.setVisibility(View.GONE);
            iniciarSesion.setVisibility(View.VISIBLE);
        } else {
            compra.setText("Comprar");
            String nombreYApellidoUsuario = sharedPreferences.getString("nombreUsuario", "noNombre!")
                    .concat(" ")
                    .concat(sharedPreferences.getString("apellidoUsuario", "noApellido!"));
            nombreUsuario.setText(nombreYApellidoUsuario);
            verPerfilUsuario.setVisibility(View.VISIBLE);
            iniciarSesion.setVisibility(View.GONE);
        }

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        boolean signedIn = sharedPreferences.getBoolean("SignedIn",false);

        if(signedIn){
            service.getActive().enqueue(new Callback<General>() {
                @Override
                public void onResponse(Call<General> call, Response<General> response) {
                    if(response.code()==200){
                        General general = response.body();
                        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(general);
                        editor.putString("general", json);
                        editor.apply();
                    }
                    else{
                        Toast.makeText(ActivityMain.this,"Error en el servidor",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<General> call, Throwable t) {
                    System.out.println("Error del servidor");
                }
            });
        }
        compra.setOnClickListener(v -> {
            Intent i = new Intent(ActivityMain.this, ActivityCompra.class);
            startActivity(i);
        });

        recetas.setOnClickListener(v -> {
            Intent i = new Intent(ActivityMain.this, ActivityReceta.class);
            startActivity(i);
        });

        masInformacion.setOnClickListener(v -> startActivity(new Intent(ActivityMain.this,ActivityMasInfo.class)));

        iniciarSesion.setOnClickListener(v -> {
            Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
            startActivity(i);
        });

        verPerfilUsuario.setOnClickListener(v -> {
            if(signedIn) {
                Intent i = new Intent(ActivityMain.this, ActivityPerfil.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


