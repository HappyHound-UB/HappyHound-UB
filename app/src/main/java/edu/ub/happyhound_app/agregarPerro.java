package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private DocumentReference documentReference;
    private String nombre, raza, edad, sexo;

    private String user;

    private boolean fotoTomada = false;

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agregar_perro);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

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
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
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
            }
            if(fotoTomada == false){
                Toast.makeText(getApplicationContext(), "Porfavor haz una foto a tu perro", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            saveDog(nombre, raza, edad, sexo);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
    protected void saveDog(String nombre, String raza, String edad, String sexo){

     //   user = mAuth.getCurrentUser().getEmail();

        documentReference = FirebaseFirestore.getInstance().collection("Perros").document();

        Map<String, Object> perro = new HashMap<>();
        perro.put("Nombre", nombre);
        perro.put("Raza", raza);
        perro.put("Edad", edad);
        perro.put("Sexo", sexo);
        perro.put("Usuario", user);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child("malik129@gmail.com/"+nombre+".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Fallo al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(getApplicationContext(), "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
            }
        });

        documentReference.set(perro).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Perro agregado con éxito", Toast.LENGTH_SHORT).show();
                         }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Fallo al agregar el perro", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    /*
    protected void saveDog(String nombre, String raza, String edad, String sexo) {
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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataByteArray = baos.toByteArray();

        // Crea una referencia al archivo en el Firebase Storage
        StorageReference imageRef = mStorageRef.child(   "malik129@gmail.com/" + nombrePerro +".jpg");

        // Crea un UploadTask para subir la imagen al Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(dataByteArray);

        // Maneja la respuesta del UploadTask
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // La imagen se subió con éxito, obtén la URL de descarga
               Log.d(TAG,"Imagen guardada correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Hubo un error al subir la imagen
                Log.e(TAG, "Error al subir la imagen", e);
            }
        });
    }*/

}



