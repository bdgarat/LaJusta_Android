package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.Carrito;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ActivityCompra extends AppCompatActivity {
    private ExpandableListView listado;
    private ArrayList<Category> categorias;
    private ArrayList<Product> productos;
    private Carrito carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        listado = (ExpandableListView) findViewById(R.id.listado);

        //Creacion del objeto mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //instanciacion del retrofit con los parametros correspondientes
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-235-40-183.compute-1.amazonaws.com")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        //inicia el servicio, ya se puede consumir
        APICall service = retrofit.create(APICall.class);

        TextView totalParcial = (TextView) this.findViewById(R.id.totalParcial);
        Button verCarrito = (Button) this.findViewById(R.id.carrito);
        carrito = new Carrito();
        verCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito.getProductos().isEmpty()) {
                    Toast.makeText(ActivityCompra.this, "El Carrito de Compras está vacio!", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(ActivityCompra.this, ActivityCarrito.class);
                    i.putExtra("total", carrito.calcularPrecio());
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
                        listado.setAdapter(new CustomExpListViewAdapter(categorias, prodCategorias, carrito, totalParcial, ActivityCompra.this));
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
















