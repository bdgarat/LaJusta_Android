package com.example.lajusta.Interface;

import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartsHistorial;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.General;
import com.example.lajusta.model.LoginUser;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.Product;
import com.example.lajusta.model.RecoveryPassword;
import com.example.lajusta.model.ConfirmRecoveryPassword;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.example.lajusta.model.UserSinId;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APICall {

    @GET("/api/product")
    Call<ArrayList<Product>> getProducts();

    @GET("/api/product/{id}")
    Call<Product> getProduct(@Path("id") long id);

    @GET("/api/category")
    Call<ArrayList<Category>> getCategories();

    @POST("/api/token/generate-token")
    Call<Token> generateToken(@Body LoginUser body);

    @POST("/api/user/signup")
    Call<User> signup(@Body UserSinId body);

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

    @POST("/api/email/recovery")
    Call<RecoveryPassword> recoveryPassword(@Body RecoveryPassword email);

    @POST("/api/email/recovery/confirm")
    Call<ConfirmRecoveryPassword> confirmRecoveryPassword(@Body ConfirmRecoveryPassword cambiarContraseña);

    @GET("/api/cart")
    Call<CartsHistorial> getCarts(@QueryMap Map<String,String> options,@Header("Authorization") String auth);
}
