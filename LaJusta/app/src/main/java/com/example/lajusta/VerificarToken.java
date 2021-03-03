package com.example.lajusta;

import android.content.SharedPreferences;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificarToken {

    public boolean verificarToken(SharedPreferences sharedPref) {
        final boolean[] tokenValidado = {false};
        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();
        String json = sharedPref.getString("usuarioToken","");
        Gson gson = new Gson();
        Token usuarioLogin= gson.fromJson(json,Token.class);

        service.getUser(1, "Bearer " + usuarioLogin.getValue()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    tokenValidado[0] = true;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Error validando el token. " + t.getMessage());
            }
        });
        return tokenValidado[0];
    }
}

