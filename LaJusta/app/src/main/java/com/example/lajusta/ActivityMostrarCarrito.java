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
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.ProductoEnCarrito;
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

        listado = (ListView) findViewById(R.id.listadoCarrito);
        ImageButton botonS = (ImageButton) findViewById(R.id.botonAtrasCarrito);
        Button botonC = (Button) findViewById(R.id.botonConfirmarCarrito);
        botonC.setVisibility(View.GONE); //Se setea en gone la visibilidad hasta que termine de obtener los nodos
        Toast.makeText(this.getApplicationContext(),"Por favor, verifique que la lista de productos sea la correcta",Toast.LENGTH_SHORT).show();

        botonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        double totalDelCarrito = this.getIntent().getDoubleExtra("total",0);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type type = new TypeToken<ArrayList<ProductoEnCarrito>>() {}.getType();
        ArrayList<ProductoEnCarrito> carrito = gson.fromJson(json,type);
        TextView textoTotal = (TextView) findViewById(R.id.totalCarrito);
        textoTotal.setText("Total $"+String.valueOf(totalDelCarrito));

        CustomAdapterCarrito adapter = new CustomAdapterCarrito(carrito,totalDelCarrito,this,R.layout.producto_en_carrito);
        listado.setAdapter(adapter);

        botonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMostrarCarrito.this, ActivityTicket.class);
                //Gson gson = new Gson();
                //String strNodos = gson.toJson(Nodos);
                //i.putExtra("nodos", strNodos);
                i.putExtra("total",totalDelCarrito);
                startActivity(i);
            }
        });

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
        // el onResponse()
        service.getNodes().enqueue(new Callback<ArrayList<Nodo>>() {
            @Override
            public void onResponse(Call<ArrayList<Nodo>> call, Response<ArrayList<Nodo>> response) {
                //Crea la vista del listado de productos
                Nodos = response.body();
                botonC.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ArrayList<Nodo>> call, Throwable t) {

            }
        });}}