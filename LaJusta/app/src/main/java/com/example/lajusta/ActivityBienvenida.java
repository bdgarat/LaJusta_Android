package com.example.lajusta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.Timer;

public class ActivityBienvenida extends AppCompatActivity {

    private final int DURACION_SPLASH = 2000;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        pb = findViewById(R.id.progressBar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(ActivityBienvenida.this, ActivityMain.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}