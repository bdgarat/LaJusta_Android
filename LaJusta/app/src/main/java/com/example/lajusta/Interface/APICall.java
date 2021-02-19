package com.example.lajusta.Interface;

import com.example.lajusta.model.Category;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.Product;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APICall {

    @GET("/api/product")
    Call<ArrayList<Product>> getProducts();

    @GET("/api/category")
    Call<ArrayList<Category>> getCategories();

    @GET("/api/node")
    Call<ArrayList<Nodo>> getNodes();
}
