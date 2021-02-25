package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.General;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.ProductoEnCarrito;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ActivityTicket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView total = (TextView) this.findViewById(R.id.totalCompra);
        Button volver = (Button) this.findViewById(R.id.botonVolverInicio);

        //Obtiene otra vez el total para mostrar, genera random simulando un codigo de compra
        double totalDelCarrito = this.getIntent().getDoubleExtra("total",0);
        int numero = (int)(Math.random()*999999999+100000);
        TextView codigo = this.findViewById(R.id.codigoCompra);
        codigo.setText("Su codigo de compra es #"+String.valueOf(numero));
        total.setText("$"+String.valueOf(totalDelCarrito));

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        /*service.generateToken(loginUser).enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.body()!=null) {
                    User user = response.body().getUser();
                    String value = response.body().getValue();
                    Token token = new Token();
                    token.setUser(user);
                    token.setValue(value);
        */


        //Aca se arma el carrito para guardarlo en la base de datos
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type typeProdsCarrito = new TypeToken<ArrayList<ProductoEnCarrito>>() {}.getType();
        ArrayList<ProductoEnCarrito> carrito = gson.fromJson(json,typeProdsCarrito);
        Cart cart = new Cart();
        //cart.convertirCartParaGuardar(carrito);
        cart.setTotal(cart.calcularPrecio());
        json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);
        cart.setUser(usuarioLogin.getUser());
        json = sharedPreferences.getString("general","");
        General general = gson.fromJson(json,General.class);
        cart.setGeneral(general);
        cart.setSaleDate(Calendar.getInstance().getTime().toString());

        //Braian, aca habria que recuperar el nodo seleccionado antes de la compra (por shared pref y mostrar algun
        // dato para que aparezca el nombre, o poner la direccion tambien.

        //cart.setNodeDate(AVAILABLE NODE RECUPERADO DE SHARED PREF);
        TextView lugarRetiro = findViewById(R.id.lugarRetiro);
        String nodoHarcodeado="Nodo harcodeado";
        lugarRetiro.setText("Retire su compra en el nodo: "+nodoHarcodeado);


        /*
        Spinner spinner = findViewById(R.id.spinnerNodoRetiro);
        String strNodos = this.getIntent().getStringExtra("nodos");
        Type typeNodos = new TypeToken<ArrayList<Nodo>>() {}.getType();
        ArrayList<Nodo> nodos= gson.fromJson(strNodos, typeNodos);
        ArrayList<String> nombreNodos = null;
        if(nombreNodos != null) {
            for (int i = 0; i < nodos.size(); i++) {
                nombreNodos.add(nodos.get(i).getName());
            }
            // Create an ArrayAdapter using the string array and a default spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombreNodos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        */









        service.saveCart(cart, "Bearer " + usuarioLogin.getValue()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                Toast.makeText(ActivityTicket.this,"El carrito se guardo exitosamente en la bd",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(ActivityTicket.this,"Malio sal :(",Toast.LENGTH_SHORT).show();
            }
        });

        volver.setOnClickListener(v -> {
            Toast.makeText(this.getApplicationContext(),"Compra realizada exitosamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityTicket.this, ActivityMain.class);
            startActivity(i);
        });
    }
}