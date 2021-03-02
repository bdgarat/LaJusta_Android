package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.General;
import com.example.lajusta.model.Token;
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

        listado = findViewById(R.id.listadoCarrito);
        ImageButton botonS = findViewById(R.id.botonAtrasCarrito);
        ImageButton botonVaciar = findViewById(R.id.botonVaciarCarrito);
        Button botonC = findViewById(R.id.botonConfirmarCarrito);
        Toast.makeText(this.getApplicationContext(),"Por favor, verifique que la lista de productos sea la correcta",Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        botonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        botonVaciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = gson.toJson(new ArrayList<CartProduct>());
                editor.putString("compras",json);
                editor.commit();
                Toast.makeText(ActivityMostrarCarrito.this,"Vaciando Carrito...",Toast.LENGTH_LONG).show();

                Intent i = new Intent(ActivityMostrarCarrito.this,ActivityCompra.class);
                startActivity(i);
            }
        });

        //Carga la informacion del carrito para poder guardarlo en la bd
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String json = sharedPreferences.getString("compras",null);
        Type type = new TypeToken<ArrayList<CartProduct>>() {}.getType();
        ArrayList<CartProduct> carrito = gson.fromJson(json,type);
        json = sharedPreferences.getString("general",null);
        type = new TypeToken<General>() {}.getType();
        General general = gson.fromJson(json,type);
        json = sharedPreferences.getString("usuarioToken",null);
        type = new TypeToken<Token>() {}.getType();
        Token token = gson.fromJson(json,type);
        Cart cart = new Cart();
        cart.setCartProducts(carrito.toArray(new CartProduct[carrito.size()]));
        cart.setGeneral(general);
        cart.setUser(token.getUser());

        json = gson.toJson(cart);
        editor.putString("carrito",json);
        editor.commit();
        TextView textoTotal = findViewById(R.id.totalCarrito);
        textoTotal.setText("Total $"+String.valueOf(cart.calcularPrecio()));
        botonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMostrarCarrito.this, ActivitySeleccionarNodo.class);
                startActivity(i);
            }
        });
        CustomAdapterCarrito adapter = new CustomAdapterCarrito(carrito,cart.calcularPrecio(),this,R.layout.producto_en_carrito);
        listado.setAdapter(adapter);
    }
}