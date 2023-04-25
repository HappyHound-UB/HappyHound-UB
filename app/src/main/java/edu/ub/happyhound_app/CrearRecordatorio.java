package edu.ub.happyhound_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CrearRecordatorio extends AppCompatActivity {

    private Button btnComida, btnPaseo, btnCalendario;
    private ImageView flecha_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recordatorio);

        Button btnComida = (Button) findViewById(R.id.ComidaButton);
        Button btnPaseo = (Button) findViewById(R.id.PaseoButton);
        Button btnCalendario = (Button) findViewById(R.id.CalendarioButton);
        ImageView flecha_return = (ImageView) findViewById(R.id.flecha_return);

        btnComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmaComida.class);
                startActivity(intent);
                finish();
            }
        });

        btnPaseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmaPaseo.class);
                startActivity(intent);
                finish();
            }
        });

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calendario.class);
                startActivity(intent);
                finish();
            }
        });

        flecha_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}