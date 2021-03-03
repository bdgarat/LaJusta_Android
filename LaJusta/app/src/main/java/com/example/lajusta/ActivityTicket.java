package com.example.lajusta;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.AvailableNode;
import com.example.lajusta.model.Cart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class ActivityTicket extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView total = (TextView) this.findViewById(R.id.totalCompra);
        Button volver = (Button) this.findViewById(R.id.botonVolverInicio);

        //genera random simulando un codigo de compra
        int numero = (int)(Math.random()*999999999+100000);
        TextView codigo = this.findViewById(R.id.codigoCompra);
        codigo.setText("Su codigo de compra es #"+String.valueOf(numero));


        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        //Aca se arma el carrito para guardarlo en la base de datos
        String json = sharedPreferences.getString("carrito",null);
        Type typeProdsCarrito = new TypeToken<Cart>() {}.getType();
        Cart cart = gson.fromJson(json,typeProdsCarrito);
        AvailableNode nodoSeleccionado = cart.getNodeDate();
        TextView lugarRetiro = findViewById(R.id.lugarRetiro);
        ZonedDateTime zdtDia = ZonedDateTime.parse(nodoSeleccionado.getDay());
        String diaFormateado = (zdtDia.getDayOfMonth() + " de " + zdtDia.getMonth() + " del " + zdtDia.getYear());
        lugarRetiro.setText("Retire su compra en el nodo: " + nodoSeleccionado.getNode().getName() +" , que se ubica en la direccion: " + nodoSeleccionado.getNode().getAddress().getStreet() + " " + nodoSeleccionado.getNode().getAddress().getNumber() + ", Entre " + nodoSeleccionado.getNode().getAddress().getBetweenStreets()+", el dia: "+ diaFormateado +", Desde "+nodoSeleccionado.dateTimeFrom+"hs hasta "+nodoSeleccionado.dateTimeTo+"hs");
        total.setText("$"+String.valueOf(cart.calcularPrecio()));

        volver.setOnClickListener(v -> {
            Toast.makeText(this.getApplicationContext(),"Compra realizada exitosamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityTicket.this, ActivityMain.class);
            startActivity(i);
        });
    }
}