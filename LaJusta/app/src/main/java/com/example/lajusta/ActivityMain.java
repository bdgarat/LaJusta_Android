package com.example.lajusta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.Category;
import com.example.lajusta.model.LoginUser;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ActivityMain extends AppCompatActivity {

        EditText email;
        EditText password;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button registrar=findViewById(R.id.registrar);
            Button sobreNosotros=findViewById(R.id.btnmasinfo);
            email=findViewById(R.id.username);
            password=findViewById(R.id.password);
            email.setText("");
            password.setText("");
            Button btnlogin=findViewById(R.id.btnlogin);
            btnlogin.setOnClickListener(v -> logIn());

            registrar.setOnClickListener(v -> startActivity(new Intent(ActivityMain.this,SignUpActivity.class)));
            sobreNosotros.setOnClickListener(v -> startActivity(new Intent(ActivityMain.this,ActivityMasInfo.class)));
    }

    private void logIn() {
        String name=email.getText().toString().trim();
        String contra=password.getText().toString();
        if (TextUtils.isEmpty(name)){
            email.setError("Please Enter Email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contra)){
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }
        //Creacion del objeto mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //instanciacion del retrofit con los parametros correspondientes
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-235-40-183.compute-1.amazonaws.com/swagger-ui/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        //inicia el servicio, ya se puede consumir
        APICall service = retrofit.create(APICall.class);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserName(name);
        loginUser.setUserPassword(contra);
        service.generateToken(loginUser).enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.body()!=null) {
                    User user = response.body().getUser();
                    String value = response.body().getValue();

                    Intent intent = new Intent(ActivityMain.this, ActivityInicio.class);
                    Token token = new Token();
                    token.setUser(user);
                    token.setValue(value);
                    SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(token);
                    editor.putString("usuarioToken", json);
                    editor.apply();
                    intent.putExtra("valorToken",token.getValue());
                    startActivity(intent);
                    Toast.makeText(ActivityMain.this,"Bienvenido "+user.getFirstName(),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ActivityMain.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(ActivityMain.this,"Fallo del servidor",Toast.LENGTH_SHORT).show();
            }
        });

        }

}
