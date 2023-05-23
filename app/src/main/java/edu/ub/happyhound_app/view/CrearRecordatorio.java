package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicLayout;

public class CrearRecordatorio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recordatorio);

        Button btnComida = findViewById(R.id.ComidaButton);
        Button btnPaseo = findViewById(R.id.PaseoButton);
        Button btnOtros = findViewById(R.id.OtroButton);

        ConstraintLayout layout = findViewById(R.id.CrearRecordatoriosConstraint);
        layout.setBackgroundColor(DynamicLayout.setDynamicLayout(this));

        btnComida.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AlarmaComida.class);
            startActivity(intent);
        });

        btnPaseo.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AlarmaPaseo.class);
            startActivity(intent);
        });

        btnOtros.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SetReminder.class);
            startActivity(intent);
        });
    }

}