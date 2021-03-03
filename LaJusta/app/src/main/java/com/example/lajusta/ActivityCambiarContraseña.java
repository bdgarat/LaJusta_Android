package com.example.lajusta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.ConfirmRecoveryPassword;
import com.example.lajusta.model.RecoveryPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCambiarContraseña extends AppCompatActivity {

    private RecoveryPassword recoveryPassword;
    private RecoveryPassword responseRecoveryPassword;
    private ConfirmRecoveryPassword confirmRecoveryPassword;
    private boolean confirm = false; //Provee el cambio entre ambas peticiones a la api


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        EditText editEmail = findViewById(R.id.editEmailCambiarContraseña);
        EditText editOldPass = findViewById(R.id.editOldPassword);
        EditText editNewPass = findViewById(R.id.editNewPassword);
        Button buttonRecoverPass = findViewById(R.id.buttonRecover);

        editOldPass.setVisibility(View.GONE);
        editNewPass.setVisibility(View.GONE);
        /*
        recoveryPassword.setEmail((editEmail.getText().toString().trim()));
        confirmRecoveryPassword.setEmail(editEmail.getText().toString().trim());
        confirmRecoveryPassword.setCode(responseRecoveryPassword.getCode());
        confirmRecoveryPassword.setOldPassword(editOldPass.getText().toString().trim());
        confirmRecoveryPassword.setNewPassword(editNewPass.getText().toString().trim());
        */

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        buttonRecoverPass.setOnClickListener(v -> {
            buttonRecoverPass.setVisibility(View.INVISIBLE);
            if(confirm == false) {
                if (TextUtils.isEmpty(editEmail.getText().toString())){
                    editEmail.setError("Por favor ingrese un email");
                    editEmail.requestFocus();
                    return;
                }
                recoveryPassword = new RecoveryPassword();
                recoveryPassword.setEmail((editEmail.getText().toString().trim()));
                //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
                // el onResponse()
                service.recoveryPassword(recoveryPassword).enqueue(new Callback<RecoveryPassword>() {
                    @Override
                    public void onResponse(Call<RecoveryPassword> call, Response<RecoveryPassword> response) {
                        responseRecoveryPassword = response.body();
                        editOldPass.setVisibility(View.VISIBLE);
                        editNewPass.setVisibility(View.VISIBLE);
                        buttonRecoverPass.setVisibility(View.VISIBLE);
                        confirm = true;
                    }

                    @Override
                    public void onFailure(Call<RecoveryPassword> call, Throwable t) {
                        Toast.makeText(ActivityCambiarContraseña.this,"Error del servidor",Toast.LENGTH_SHORT).show();
                        System.out.println("No se obtuvo respuesta al realizar la peticion de cambio");
                        System.out.println(t.getMessage());
                    }
                });
            } else {
                if (TextUtils.isEmpty(editEmail.getText().toString())){
                    editEmail.setError("Por favor ingrese un email");
                    editEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(editNewPass.getText().toString())){
                    editNewPass.setError("Por favor ingrese una contraseña nueva");
                    editNewPass.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(editOldPass.getText().toString())){
                    editOldPass.setError("Por favor ingrese la contraseña antigua (?)");
                    editOldPass.requestFocus();
                    return;
                }
                confirmRecoveryPassword = new ConfirmRecoveryPassword();
                confirmRecoveryPassword.setEmail(editEmail.getText().toString().trim());
                confirmRecoveryPassword.setOldPassword(editOldPass.getText().toString().trim());
                confirmRecoveryPassword.setNewPassword(editNewPass.getText().toString().trim());
                confirmRecoveryPassword.setCode(responseRecoveryPassword.getCode());
                //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
                // el onResponse()
                service.confirmRecoveryPassword(confirmRecoveryPassword).enqueue(new Callback<ConfirmRecoveryPassword>() {
                    @Override
                    public void onResponse(Call<ConfirmRecoveryPassword> call, Response<ConfirmRecoveryPassword> response) {
                        if(response.raw().code() == 200) {
                            Toast.makeText(ActivityCambiarContraseña.this,"La contraseña se ha reemplazado correctamente",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityCambiarContraseña.this, ActivityLogin.class);
                            startActivity(intent);
                        } else {
                            editEmail.setError("Por favor ingrese un email de una cuenta valida");
                            editEmail.requestFocus();
                            buttonRecoverPass.setVisibility(View.VISIBLE);
                            editOldPass.setVisibility(View.GONE);
                            editOldPass.setText("");
                            editNewPass.setVisibility(View.GONE);
                            editNewPass.setText("");
                            confirm = false;
                        }

                    }

                    @Override
                    public void onFailure(Call<ConfirmRecoveryPassword> call, Throwable t) {
                        Toast.makeText(ActivityCambiarContraseña.this,"Error del servidor",Toast.LENGTH_SHORT).show();
                        System.out.println("No se obtuvo respuesta al realizar la confirmacion de peticion de cambio");
                    }
                });
            }

        });
    }}
