package com.example.lajusta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.LoginUser;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

        EditText email;
        EditText password;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            Button registrar=findViewById(R.id.registrar);
            Button recuperarContra = findViewById(R.id.buttonCambiarContraseña);
            email=findViewById(R.id.username);
            password=findViewById(R.id.password);
            email.setText("");
            password.setText("");
            Button btnlogin=findViewById(R.id.btnlogin);


            btnlogin.setOnClickListener(v -> logIn());

            registrar.setOnClickListener(v -> startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class)));

            recuperarContra.setOnClickListener(v -> startActivity(new Intent(ActivityLogin.this, ActivityCambiarContraseña.class)));
    }

    private void logIn() {
        String name=email.getText().toString().trim();
        String contra=password.getText().toString();
        if (TextUtils.isEmpty(name)){
            email.setError("Por favor ingrese un email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contra)){
            password.setError("Por favor ingrese una contraseña");
            password.requestFocus();
            return;
        }
        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        LoginUser loginUser = new LoginUser();
        loginUser.setUserName(name);
        loginUser.setUserPassword(contra);
        service.generateToken(loginUser).enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.body()!=null) {
                    User user = response.body().getUser();
                    String value = response.body().getValue();

                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                    Token token = new Token();
                    token.setUser(user);
                    token.setValue(value);
                    SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("SignedIn", true);
                    editor.putString("nombreUsuario", user.getFirstName());
                    editor.putString("apellidoUsuario", user.getLastName());
                    Gson gson = new Gson();
                    String json = gson.toJson(token);
                    editor.putString("usuarioToken", json);
                    editor.apply();
                    startActivity(intent);
                    Toast.makeText(ActivityLogin.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ActivityLogin.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(ActivityLogin.this,"Fallo del servidor",Toast.LENGTH_SHORT).show();
            }
        });

        }

}
