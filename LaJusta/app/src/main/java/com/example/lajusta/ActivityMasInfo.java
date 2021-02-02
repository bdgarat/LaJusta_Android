package com.example.lajusta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ActivityMasInfo extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masinfo);

        Button verNodosEnMapa = this.findViewById(R.id.buttonVerNodos);

        verNodosEnMapa.setOnClickListener(v -> {
            Intent i = new Intent(ActivityMasInfo.this, ActivityMapaNodos.class);
            startActivity(i);
        });
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
