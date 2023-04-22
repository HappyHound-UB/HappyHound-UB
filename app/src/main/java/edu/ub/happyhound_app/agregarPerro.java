package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText nombrePerro, edadPerro;
    private Button btnCrearRecordatorio, btnAdd;

    private TextView btnCancelar;

    private Spinner spinnerRaza, spinnerSexo;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference = FirebaseFirestore.getInstance().document("Users/Verified Users");
    //private DocumentReference documentReference = FirebaseFirestore.getInstance().collection().document().collection().....
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

    protected void newDog(String nombre, String raza, String edad, String sexo){
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

    }


}


