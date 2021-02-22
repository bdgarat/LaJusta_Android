package com.example.lajusta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.Token;
import com.example.lajusta.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText lastname;
    EditText phone;
    ImageButton atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView text = findViewById(R.id.textRegistrar);
        name = findViewById(R.id.editName);
        lastname = findViewById(R.id.editLastName);
        phone = findViewById(R.id.editPhone);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPass);
        confirmPassword = findViewById(R.id.editConfirmPassword);
        Button btnlogin=findViewById(R.id.buttonAccount);
        atras = findViewById(R.id.botonAtras);
        btnlogin.setOnClickListener(v -> logIn());

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void logIn() {
        String nombre=name.getText().toString();
        String mail=email.getText().toString().trim();
        String contra=password.getText().toString();
        String confirmarContra=confirmPassword.getText().toString();
        String apellido=lastname.getText().toString();
        String telefono=phone.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            name.setError("Ingresar Nombre");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(apellido)){
            lastname.setError("Ingresar Apellido");
            lastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail)){
            email.setError("Ingresar Email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telefono)){
            phone.setError("Ingresar Numero de Teléfono");
            phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contra)){
            password.setError("Ingresar Contraseña");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmarContra) || confirmarContra.compareTo(contra) != 0){
            confirmPassword.setError("Verifique que la contraseña sea la misma en ambos campos");
            confirmPassword.requestFocus();
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

        User registrarUser = new User();
        registrarUser.setEmail(mail);
        registrarUser.setEncryptedPassword(contra);
        registrarUser.setFirstName(nombre);
        registrarUser.setLastName(apellido);
        registrarUser.setPhone(telefono);

        service.signup(registrarUser).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body()!=null) {
                    Toast.makeText(SignUpActivity.this,"Se registro usuario correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, ActivityMain.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Usuario no registrado, es probable que el usuario ya se encuentre en uso",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }
}
