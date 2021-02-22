package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.gson.Gson;

public class ActivityBienvenida extends AppCompatActivity {

    private final int DURACION_SPLASH = 2000;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        pb = findViewById(R.id.progressBar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String usuarioToken = sharedPreferences.getString("usuarioToken", "");
        if(usuarioToken == "") {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("SignedIn", false);
            editor.apply();
        }
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(ActivityBienvenida.this, ActivityMain.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}