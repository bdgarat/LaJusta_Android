package com.example.lajusta;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Image;
import com.example.lajusta.model.Product;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMostrarCarritoComprado extends AppCompatActivity {
    public ListView listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_carrito_comprado);

        ActivityMostrarCarritoComprado context = this;
        listado = findViewById(R.id.listadoCarrito);
        ImageButton botonS = findViewById(R.id.botonAtrasCarrito);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        //final Cart[] carrito = new Cart[1];

        botonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);

        service.getCart(getIntent().getIntExtra("cart_id",-1), "Bearer " + usuarioLogin.getValue()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.code()==200){
                    Cart carrito = response.body();

                    TextView textoTotal = findViewById(R.id.totalCarrito);
                    textoTotal.setText("Total $"+String.valueOf(carrito.calcularPrecio()));

                    ArrayList arrCarrito = new ArrayList();
                    for(int i = 0; i < carrito.getCartProducts().length; i++) {
                        arrCarrito.add(carrito.getCartProducts()[i]);
                    }

                    CustomAdapterCarrito adapter = new CustomAdapterCarrito(arrCarrito, carrito.calcularPrecio(), context,R.layout.producto_en_carrito, false);
                    listado.setAdapter(adapter);

                }
                else{
                    Toast.makeText(ActivityMostrarCarritoComprado.this,"No se pudo visualizar el producto",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(ActivityMostrarCarritoComprado.this,"No se pudo visualizar el producto",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });


    }
}