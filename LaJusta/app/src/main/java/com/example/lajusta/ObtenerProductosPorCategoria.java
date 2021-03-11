package com.example.lajusta;

import com.example.lajusta.model.Category;
import com.example.lajusta.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class ObtenerProductosPorCategoria {

    public HashMap<Integer, ArrayList<Product>> obtenerProductosPorCategoria(ArrayList<Category> categorias, ArrayList<Product> productos){
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
