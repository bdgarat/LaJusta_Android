package com.example.lajusta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.R;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.Image;
import com.example.lajusta.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivityVerProducto  extends AppCompatActivity {

    Product producto;
    TextView textProducto;
    TextView textDescripcion;
    TextView textPrecio;
    ImageView imageProducto;
    TextView textCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);

        String jsonProducto = getIntent().getStringExtra("JSON_PRODUCTO");
        Type type = new TypeToken<Product>() {}.getType();
        Gson gson = new Gson();
        producto = gson.fromJson(jsonProducto, type);

        textProducto = findViewById(R.id.textProducto);
        textDescripcion = findViewById(R.id.textDescripcionProducto);
        textPrecio = findViewById(R.id.textPrecioProducto);
        imageProducto = findViewById(R.id.imageProducto);
        textCategorias = findViewById(R.id.textCategoriasProducto);

        textProducto.setText(producto.getTitle());
        if(producto.getDescription() != null) {
            textDescripcion.setText(producto.getDescription());
        } else {
            textDescripcion.setText("<Sin descripcion de producto>");
        }

        textPrecio.setText("Precio: $" + String.valueOf(producto.getPrice()));

        //obtiene la imagen de producto guardada en base64
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

        //Obtiene los nombres de las categorias
        String strCategories = "Categorias: ";
        if(producto.getCategories().size() > 0) {
            for(int i = 0; i < producto.getCategories().size(); i++) {
                strCategories = strCategories.concat(producto.getCategories().get(i).getName() + ", ");
            }
        }
        textCategorias.setText(strCategories);



    }
}
