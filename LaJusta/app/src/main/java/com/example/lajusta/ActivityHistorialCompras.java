package com.example.lajusta;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.CartComplete;
import com.example.lajusta.model.CartsHistorial;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHistorialCompras  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);
        ImageButton atras = findViewById(R.id.botonAtras);
        ListView listado = findViewById(R.id.listadoPedidos);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView nombreUsuario = findViewById(R.id.nombreUsuario);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);
        if(usuarioLogin.getUser().getFirstName()!=null){
            nombreUsuario.setText("Compras realizadas por "+usuarioLogin.getUser().getFirstName()+" "+usuarioLogin.getUser().getLastName());
        }
        else{
            nombreUsuario.setText("Historial de compras");
        }
        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();
        Map<String,String> map = new HashMap<>();
        map.put("sort", "id,desc");
        map.put("range", "0,10");
        map.put("properties", "[{\"key\": \"history\", \"value\": true}]");
        service.getCarts(map,"Bearer " + usuarioLogin.getValue()).enqueue(new Callback<CartsHistorial>() {
            @Override
            public void onResponse(Call<CartsHistorial> call, Response<CartsHistorial> response) {
                if(response.isSuccessful()) {
                    ArrayList<CartComplete> carts = (ArrayList<CartComplete>) response.body().getPage();
                    CustomAdapterHistorialCarritos adapter = new CustomAdapterHistorialCarritos(carts, getApplicationContext(), R.layout.carrito_en_historial);
                    listado.setAdapter(adapter);
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<CartsHistorial> call, Throwable t) {

            }
        });



    }
}