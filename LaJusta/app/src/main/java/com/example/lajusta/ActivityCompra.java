package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCompra extends AppCompatActivity {
    private ExpandableListView listado;
    private SearchView searchView;
    private ArrayList<Category> categorias;
    private ArrayList<Product> productos;
    private CustomExpListViewAdapter adapter;
    private boolean signed_off;
    private LinearLayout layoutCarrito;
    private ArrayList<CartProduct> productosComprados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        listado = findViewById(R.id.listado);
        searchView = findViewById(R.id.searchView);
        layoutCarrito = findViewById(R.id.layoutCarrito);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        signed_off = !sharedPreferences.getBoolean("SignedIn", false);

        if(signed_off) {
            layoutCarrito.setVisibility(View.GONE);
        } else {
            layoutCarrito.setVisibility(View.VISIBLE);
        }

        TextView totalParcial = this.findViewById(R.id.totalParcial);
        ImageButton verCarrito = this.findViewById(R.id.carrito);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("compras",null);
        Type type = new TypeToken<ArrayList<CartProduct>>() {}.getType();
        ArrayList<CartProduct> recuperandoProductos = gson.fromJson(json,type);
        if(recuperandoProductos!=null){
            productosComprados=recuperandoProductos;
        }
        else{
            productosComprados = new ArrayList<>();
        }

        verCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productosComprados.isEmpty()) {
                    Toast.makeText(ActivityCompra.this, "El Carrito de Compras está vacio!", Toast.LENGTH_LONG).show();
                } else {
                    Gson gson = new Gson();
                    String json = gson.toJson(productosComprados);
                    editor.putString("compras",json);
                    editor.commit();
                    Intent i = new Intent(ActivityCompra.this, ActivityMostrarCarrito.class);
                    startActivity(i);
                }

            }
        });

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
        // el onResponse()
        service.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
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
                        adapter = new CustomExpListViewAdapter(categorias, prodCategorias, productosComprados, totalParcial, signed_off,ActivityCompra.this);
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
                        Toast.makeText(ActivityCompra.this,"Error del servidor",Toast.LENGTH_SHORT).show();
                        System.out.println("No se obtuvieron los productos");
                    }});
            }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Toast.makeText(ActivityCompra.this,"Error del servidor",Toast.LENGTH_SHORT).show();
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
















