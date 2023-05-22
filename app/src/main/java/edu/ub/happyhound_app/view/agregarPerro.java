package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.SavePetInfo;
import edu.ub.happyhound_app.model.ToastMessage;

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

        nombrePerro = findViewById(R.id.editTextNombre);
        edadPerro = findViewById(R.id.editTextEdad);
        btnAdd = findViewById(R.id.buttonAdd);
        btnPicture = findViewById(R.id.ButtonFoto);
        imageView = findViewById(R.id.imageView1);
        btnCancelar = findViewById(R.id.cancelarButton);
        spinnerRaza = findViewById(R.id.spinnerRaza);
        spinnerSexo = findViewById(R.id.spinnerSexo);


        String[] Razas = getResources().getStringArray(R.array.Raza);
        ArrayAdapter<String> adapterRazas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Razas);
        spinnerRaza.setAdapter(adapterRazas);

        String[] Sexo = getResources().getStringArray(R.array.Sexo);
        ArrayAdapter<String> adapterSexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Sexo);
        spinnerSexo.setAdapter(adapterSexo);

        btnAdd.setOnClickListener(view -> {
            nombre = nombrePerro.getText().toString();
            edad = edadPerro.getText().toString();
            raza = spinnerRaza.getSelectedItem().toString();
            sexo = spinnerSexo.getSelectedItem().toString();
            newDog(nombre, raza, edad, sexo);
        });

        btnPicture.setOnClickListener(view -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE);
        });
        btnCancelar.setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            b = (Bitmap) Objects.requireNonNull(data).getExtras().get("data");
            imageView.setImageBitmap(b);
            fotoTomada = true;
        } else {
            ToastMessage.displayToast(getApplicationContext(), "cancelled");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void newDog(String nombre, String raza, String edad, String sexo) {
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
            }
            if (!fotoTomada) {
                ToastMessage.displayToast(getApplicationContext(), "Porfavor haz una foto a tu perro");
            }
        } else {
            SavePetInfo savePets = new SavePetInfo(this);
            savePets.saveDogs(nombre, raza, edad, sexo, b);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}