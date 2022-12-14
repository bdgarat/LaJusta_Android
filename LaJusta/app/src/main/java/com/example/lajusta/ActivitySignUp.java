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
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.User;
import com.example.lajusta.model.UserSinId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignUp extends AppCompatActivity {
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

        TextView text = findViewById(R.id.textRecuperarContrasena);
        name = findViewById(R.id.editName);
        lastname = findViewById(R.id.editLastName);
        phone = findViewById(R.id.editPhone);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPass);
        confirmPassword = findViewById(R.id.editConfirmPassword);
        Button btnlogin=findViewById(R.id.buttonSignOff);
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
            phone.setError("Ingresar Numero de Tel??fono");
            phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contra)){
            password.setError("Ingresar Contrase??a");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmarContra) || confirmarContra.compareTo(contra) != 0){
            confirmPassword.setError("Verifique que la contrase??a sea la misma en ambos campos");
            confirmPassword.requestFocus();
            return;
        }

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        UserSinId registrarUser = new UserSinId();
        registrarUser.setEmail(mail);
        registrarUser.setEncryptedPassword(contra);
        registrarUser.setFirstName(nombre);
        registrarUser.setLastName(apellido);
        registrarUser.setPhone(telefono);

        service.signup(registrarUser).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body()!=null) {
                    Toast.makeText(ActivitySignUp.this,"Se registro usuario correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivitySignUp.this, ActivityLogin.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ActivitySignUp.this,"Usuario no registrado, es probable que el usuario ya se encuentre en uso",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }
}
