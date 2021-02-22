package com.example.lajusta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.Carrito;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ActivityCompra extends AppCompatActivity {
    private ExpandableListView listado;
    private SearchView searchView;
    private ArrayList<Category> categorias;
    private ArrayList<Product> productos;
    private Carrito carrito;
    private CustomExpListViewAdapter adapter;
    private boolean signed_off;
    private LinearLayout layoutCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        listado = (ExpandableListView) findViewById(R.id.listado);
        searchView = (SearchView) findViewById(R.id.searchView);
        layoutCarrito = findViewById(R.id.layoutCarrito);

        //Creacion del objeto mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //instanciacion del retrofit con los parametros correspondientes
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-235-40-183.compute-1.amazonaws.com/swagger-ui/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        //inicia el servicio, ya se puede consumir
        APICall service = retrofit.create(APICall.class);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        signed_off = !sharedPreferences.getBoolean("SignedIn", false);

        if(signed_off) {
            layoutCarrito.setVisibility(View.GONE);
        } else {
            layoutCarrito.setVisibility(View.VISIBLE);
        }

        TextView totalParcial = (TextView) this.findViewById(R.id.totalParcial);
        ImageButton verCarrito = this.findViewById(R.id.carrito);
        carrito = new Carrito();

        verCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito.getProductos().isEmpty()) {
                    Toast.makeText(ActivityCompra.this, "El Carrito de Compras está vacio!", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(ActivityCompra.this, ActivityMostrarCarrito.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(carrito.getProductos());
                    editor.putString("task list", json);
                    editor.apply();
                    i.putExtra("total",carrito.calcularPrecio());
                    startActivity(i);
                }

            }
        });
        //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
        // el onResponse()
        service.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                //Crea la vista del listado de productos
                categorias = response.body();

                //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
                // el onResponse()
                service.getProducts().enqueue(new Callback<ArrayList<Product>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                        productos = response.body();

                        //Genera un hashmap, cada categoria contiene sus productos
                        HashMap<Integer, ArrayList<Product>> prodCategorias = obtenerProductosPorCategoria(categorias,productos);
                        //ExpAdapter maneja la vista de la Expandable List view y las interacciones
                        adapter = new CustomExpListViewAdapter(categorias, prodCategorias, carrito, totalParcial, signed_off,ActivityCompra.this);
                        listado.setAdapter(adapter);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                adapter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                        System.out.println("No se obtuvieron los productos");
                    }}); }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                System.out.println("No se obtuvieron las categorias");
            }
        });



    }

        public HashMap<Integer,ArrayList<Product>> obtenerProductosPorCategoria(ArrayList<Category> categorias, ArrayList<Product> productos){
            HashMap<Integer, ArrayList<Product>> prodCategorias = new HashMap();
            for (Category c : categorias) {
                ArrayList<Product> prodsParaCategoria = new ArrayList<>();
                for (Product p : productos) {
                    ArrayList<Category> lista = p.getCategories();
                    for (Category category : lista) {
                        if (category.getId() == c.getId()) {
                            prodsParaCategoria.add(p);
                        }
                    }

                }
                prodCategorias.put(c.getId(), prodsParaCategoria);
            }
            return prodCategorias;
    }
}
















