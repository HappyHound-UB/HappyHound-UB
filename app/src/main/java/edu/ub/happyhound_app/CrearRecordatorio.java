package edu.ub.happyhound_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CrearRecordatorio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recordatorio);

        Button btnComida = (Button) findViewById(R.id.ComidaButton);
        Button btnPaseo = (Button) findViewById(R.id.PaseoButton);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.CrearRecordatoriosConstraint);
        layout.setBackgroundColor(setDynamicLayout());

        btnComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmaComida.class);
                startActivity(intent);
            }
        });

        btnPaseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmaPaseo.class);
                startActivity(intent);
            }
        });


    }
    private int setDynamicLayout() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;

//
    }

}