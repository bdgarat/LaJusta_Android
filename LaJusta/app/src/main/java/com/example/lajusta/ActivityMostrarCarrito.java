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

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.General;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.ProductoEnCarrito;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMostrarCarrito extends AppCompatActivity {

    public ListView listado;
    private ArrayList<Nodo> Nodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_carrito);

        listado = findViewById(R.id.listadoCarrito);
        ImageButton botonS = findViewById(R.id.botonAtrasCarrito);
        Button botonC = findViewById(R.id.botonConfirmarCarrito);
        botonC.setVisibility(View.GONE); //Se setea en gone la visibilidad hasta que termine de obtener los nodos
        Toast.makeText(this.getApplicationContext(),"Por favor, verifique que la lista de productos sea la correcta",Toast.LENGTH_SHORT).show();

        botonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Carga la informacion del carrito para poder guardarlo en la bd
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
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
        cart.setTotal(cart.calcularPrecio());

        json = gson.toJson(cart);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("carrito",json);
        editor.commit();
        TextView textoTotal = findViewById(R.id.totalCarrito);
        textoTotal.setText("Total $"+String.valueOf(cart.calcularPrecio()));

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
        // el onResponse()
        service.saveCart(cart,"Bearer "+token.getValue()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                Toast.makeText(ActivityMostrarCarrito.this,"El carrito fue guardado exitosamente, seleccione un nodo para retirar su compra",Toast.LENGTH_SHORT).show();
                botonC.setVisibility(View.VISIBLE);
                botonC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ActivityMostrarCarrito.this, ActivitySeleccionarNodo.class);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {

            }
        });
        CustomAdapterCarrito adapter = new CustomAdapterCarrito(carrito,cart.calcularPrecio(),this,R.layout.producto_en_carrito);
        listado.setAdapter(adapter);

    }
}