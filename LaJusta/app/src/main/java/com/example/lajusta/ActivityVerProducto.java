package com.example.lajusta;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Image;
import com.example.lajusta.model.Product;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityVerProducto  extends AppCompatActivity {
    TextView textProducto;
    TextView textDescripcion;
    TextView textPrecio;
    ImageView imageProducto;
    TextView textCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);

        textProducto = findViewById(R.id.textProducto);
        textDescripcion = findViewById(R.id.textDescripcionProducto);
        textPrecio = findViewById(R.id.textPrecioProducto);
        imageProducto = findViewById(R.id.imageProducto);
        textCategorias = findViewById(R.id.textCategoriasProducto);

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);
        service.getProduct(getIntent().getLongExtra("producto_id",-1),usuarioLogin.getValue()).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.code()==200){
                    Product producto = response.body();
                    textProducto.setText(producto.getTitle());
                    textProducto.setVisibility(View.VISIBLE);
                    if(producto.getDescription() != null) {
                        textDescripcion.setText(producto.getDescription());
                    } else {
                        textDescripcion.setText("<Sin descripcion de producto>");
                    }
                    textDescripcion.setVisibility(View.VISIBLE);
                    textPrecio.setText("Precio: $" + String.valueOf(producto.getPrice()));
                    textPrecio.setVisibility(View.VISIBLE);
                    if (producto.getImages().length > 0) {
                        for (Image img : producto.getImages()) {
                            String base64Str = img.getValue();
                            if (base64Str != null) {
                                String base64Image = base64Str.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imageProducto.setImageBitmap(decoded);
                                break;
                            }
                        }
                    }
                    imageProducto.setVisibility(View.VISIBLE);

                    //Obtiene los nombres de las categorias
                    String strCategories = "Categorias: ";
                    for(Category c:producto.getCategories()){
                        strCategories = strCategories.concat(c.getName())+", ";
                    }
                    strCategories = strCategories.substring(0, strCategories.length()-2);
                    textCategorias.setText(strCategories);
                    textCategorias.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(ActivityVerProducto.this,"No se pudo visualizar el producto",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ActivityVerProducto.this,"No se pudo visualizar el producto",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });



    }
}
