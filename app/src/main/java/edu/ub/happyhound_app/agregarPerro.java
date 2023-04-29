package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class agregarPerro extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    private EditText nombrePerro, edadPerro;
    private Button btnAdd, btnPicture;

    private TextView btnCancelar;

    private ImageView imageView;
    private Spinner spinnerRaza, spinnerSexo;
    private FirebaseAuth mAuth;
    private String nombre, raza, edad, sexo;

    private boolean fotoTomada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_perro);

        mAuth = FirebaseAuth.getInstance();

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            fotoTomada = true;
        }
        else{
            Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void newDog(String nombre, String Raza, String edad, String sexo){
        if(nombre.isEmpty()|| raza.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
            if (nombre.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Porfavor introduzca el nombre de tu perro", Toast.LENGTH_SHORT).show();
            }
            if (raza.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Porfavor introduzca la raza de tu perro", Toast.LENGTH_SHORT).show();
            }
            if (edad.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Porfavor introduzca la edad de tu perro", Toast.LENGTH_SHORT).show();
            }
            if (sexo.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Porfavor introduzca el sexo de tu perro", Toast.LENGTH_SHORT).show();
            }/*
            if(fotoTomada == false){
                Toast.makeText(getApplicationContext(), "Porfavor haz una foto a tu perro", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            SavePetInfo savePets = new SavePetInfo(getmAuth());
            savePets.saveDogs(nombre, raza, edad, sexo);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }
}