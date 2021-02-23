package com.example.lajusta.Interface;

import android.util.Log;

import com.example.lajusta.model.Address;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.General;
import com.example.lajusta.model.LoginUser;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.Product;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APICall {

    @GET("/api/product")
    Call<ArrayList<Product>> getProducts();

    @GET("/api/category")
    Call<ArrayList<Category>> getCategories();

    @POST("/api/token/generate-token")
    Call<Token> generateToken(@Body LoginUser body);

    @POST("/api/user/signup")
    Call<User> signup(@Body User body);

    @GET("/api/user/{id}")
    Call<User> getUser(@Path("id") int id, @Header("Authorization") String auth);

    @PUT("/api/user")
    Call<User> updateUser(@Body User body,@Header("Authorization") String auth);

    @GET("/api/node")
    Call<ArrayList<Nodo>> getNodes();

    @POST("/api/cart")
    Call<Cart> saveCart(@Body Cart cart, @Header("Authorization") String auth);

    @GET("/api/cart/{id}")
    Call<Cart> getCart(@Path("id") int id,@Header("Authorization") String auth);

    @POST("/api/cart/activate/{id}")
    Call<Cart> activateCart(@Path("id") int id,@Header("Authorization") String auth);

    @GET("/api/general/active")
    Call<General> getActive();
}
