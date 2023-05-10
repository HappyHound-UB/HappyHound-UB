package edu.ub.happyhound_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class agregarPerro extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    private EditText nombrePerro, edadPerro;
    private Button btnAdd, btnPicture;
    private TextView btnCancelar;
    private ImageView imageView;
    private Spinner spinnerRaza, spinnerSexo;
    private String nombre, raza, edad, sexo;
    private boolean fotoTomada = false;
    private  Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_perro);

        nombrePerro = (EditText) findViewById(R.id.editTextNombre);
        edadPerro = (EditText) findViewById(R.id.editTextEdad);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnPicture = (Button) findViewById(R.id.ButtonFoto);
        imageView = (ImageView) findViewById(R.id.imageView1);
        btnCancelar = (TextView) findViewById(R.id.cancelarButton);
        spinnerRaza = (Spinner) findViewById(R.id.spinnerRaza);
        spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);


        String [] Razas = getResources().getStringArray(R.array.Raza);
        ArrayAdapter<String> adapterRazas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Razas);
        spinnerRaza.setAdapter(adapterRazas);

        String [] Sexo = getResources().getStringArray(R.array.Sexo);
        ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Sexo);
        spinnerSexo.setAdapter(adapterSexo);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = nombrePerro.getText().toString();
                edad = edadPerro.getText().toString();
                raza = spinnerRaza.getSelectedItem().toString();
                sexo = spinnerSexo.getSelectedItem().toString();
                newDog(nombre,raza, edad, sexo);
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,REQUEST_CODE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            b = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(b);
            fotoTomada = true;
        } else {
            ToastMessage.displayToast(getApplicationContext(), "cancelled");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void newDog(String nombre, String Raza, String edad, String sexo) {
        if (nombre.isEmpty() || raza.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
            if (nombre.isEmpty()) {
                ToastMessage.displayToast(getApplicationContext(), "Porfavor introduzca el nombre de tu perro");
            }
            if (raza.isEmpty()) {
                ToastMessage.displayToast(getApplicationContext(), "Porfavor introduzca la raza de tu perro");
            }
            if (edad.isEmpty()) {
                ToastMessage.displayToast(getApplicationContext(), "Porfavor introduzca la edad de tu perro");
            }
            if (sexo.isEmpty()) {
                ToastMessage.displayToast(getApplicationContext(), "Porfavor introduzca el sexo de tu perro");
            }/*
            if(fotoTomada == false){
                Toast.makeText(getApplicationContext(), "Porfavor haz una foto a tu perro", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            SavePetInfo savePets = new SavePetInfo(this);
            savePets.saveDogs(nombre, raza, edad, sexo, b);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}