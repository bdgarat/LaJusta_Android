package com.example.lajusta;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.example.lajusta.model.Receta;
import java.util.ArrayList;

public class ActivityReceta extends AppCompatActivity {
    private SearchView searchRecetas;
    private ArrayList<Receta> recetas = new ArrayList<>();
    private CustomAdapterRecetas adapterRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);

        ListView listRecetas = this.findViewById(R.id.listadoRecetas);
        searchRecetas = (SearchView) findViewById(R.id.searchViewRecetas);

        Toast.makeText(ActivityReceta.this,"Ingresar ingredientes separados por coma (,) y sin espacio. Ej: papa,huevo",Toast.LENGTH_LONG).show();

        ArrayList<String> ingredientes = new ArrayList<>();
        ingredientes.add("Papa");
        ingredientes.add("Huevo");
        ingredientes.add("Tomate");
        recetas.add(new Receta("Torreja de Papas","40 minutos",ingredientes,"Cortar papa huevo y tomate, y cocinar"));
        ingredientes = new ArrayList<>();
        ingredientes.add("Fideos Frescos");
        ingredientes.add("Tomate");
        recetas.add(new Receta("Fideos con salsa","20 minutos",ingredientes,"Hacer salsa con el tomate, cocinar los fideos"));
        ingredientes = new ArrayList<>();
        ingredientes.add("Pechito de Cerdo");
        ingredientes.add("Pescado");
        ingredientes.add("Pollo");
        recetas.add(new Receta("Todo Carne","55 minutos",ingredientes,"Cocinar todas las carnes y juntar en el plato"));

        adapterRecetas = new CustomAdapterRecetas(this,R.layout.receta,recetas);
        listRecetas.setAdapter(adapterRecetas);

        searchRecetas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterRecetas.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRecetas.getFilter().filter(newText);
                return false;
            }
        });




    }
}
