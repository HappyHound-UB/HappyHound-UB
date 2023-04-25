package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class agregarPerro extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    private EditText nombrePerro, edadPerro;
    private Button btnCrearRecordatorio, btnAdd, btnPicture;

    private TextView btnCancelar;

    private ImageView imageView;
    private Spinner spinnerRaza, spinnerSexo;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private String nombre, raza, edad, sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_perro);

        mAuth = FirebaseAuth.getInstance();

        nombrePerro = (EditText) findViewById(R.id.editTextNombre);
        edadPerro = (EditText) findViewById(R.id.editTextEdad);
        btnCrearRecordatorio = (Button) findViewById(R.id.buttonRecordatorio);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnPicture = (Button) findViewById(R.id.ButtonFoto);
        imageView = (ImageView) findViewById(R.id.imageView1);
        btnCancelar = (TextView) findViewById(R.id.cancelarButton);
        spinnerRaza = (Spinner) findViewById(R.id.spinnerRaza);
        spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);


        String [] Razas = getResources().getStringArray(R.array.Raza);
        ArrayAdapter<String> adapterRazas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Razas);

        String [] Sexo = getResources().getStringArray(R.array.Sexo);
        ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Sexo);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = nombrePerro.getText().toString();
                edad = edadPerro.getText().toString();
                raza = spinnerRaza.getSelectedItem().toString();
                sexo = spinnerSexo.getSelectedItem().toString();
                newDog(nombre, raza, edad, sexo, imageView);
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
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
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
        }
        else{
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void newDog(String nombre, String raza, String edad, String sexo, ImageView image){
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
            }
        }
        else{
            saveDog(nombre, raza, edad, sexo, image);
        }
    }
    protected void saveDog(String nombre, String raza, String edad, String sexo, ImageView image) {
        String userID = mAuth.getCurrentUser().getUid();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(userID)
                .collection("Lista Perros").document(nombre);

        if (nombre.isEmpty() || raza.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
            return;
        }


        Map<String, Object> perros = new HashMap<String, Object>();
        perros.put("name", nombre);
        perros.put("raza", raza);
        perros.put("edad", edad);
        perros.put("sexo", sexo);
        perros.put("imagen", image);

        documentReference.set(perros)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

}