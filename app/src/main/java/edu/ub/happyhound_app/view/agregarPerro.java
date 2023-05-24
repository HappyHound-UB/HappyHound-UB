package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.SavePetInfo;
import edu.ub.happyhound_app.model.ToastMessage;

public class agregarPerro extends AppCompatActivity implements SaveCallback {

    private static final int REQUEST_CODE = 22;
    private EditText nombrePerro, edadPerro;
    private Button btnAdd, btnPicture;
    private TextView btnCancelar;
    private ImageView imageView;
    private Spinner spinnerRaza, spinnerSexo;
    private String nombre, raza, edad, sexo;
    private boolean fotoTomada = false;
    private  Bitmap b;

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog.OnDateSetListener listener;

    Timer timer;


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

        edadPerro.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, listener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1; // Opcional si deseas mostrar el mes con valores del 1 al 12 en lugar del 0 al 11
                String date = dayOfMonth + "/" + month + "/" + year;
                edadPerro.setText(date);

                // Obtener la fecha de nacimiento seleccionada
                Calendar fechaNacimiento = Calendar.getInstance();
                fechaNacimiento.set(year, month - 1, dayOfMonth); // Resta 1 al mes

                // Calcular y mostrar la edad inicial
                actualizarEdadPerro(fechaNacimiento);

                // Iniciar el temporizador para actualizar la edad periódicamente
                iniciarTemporizador(fechaNacimiento);
            }
        };


    }

    private void iniciarTemporizador(final Calendar fechaNacimiento) {
        // Cancelar el temporizador si ya está en ejecución
        if (timer != null) {
            timer.cancel();
        }

        // Crear un nuevo temporizador
        timer = new Timer();

        // Programar una tarea periódica para actualizar la edad cada segundo
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Actualizar la edad actualizada en el hilo principal de la interfaz de usuario
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        actualizarEdadPerro(fechaNacimiento);
                    }
                });
            }
        }, 0, 1000); // Actualizar cada segundo (1000 ms)
    }
    private void actualizarEdadPerro(Calendar fechaNacimiento) {
        // Obtener la fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Calcular la diferencia de años
        int edad = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);

        // Verificar si aún no ha pasado el cumpleaños este año
        if (fechaActual.get(Calendar.MONTH) < fechaNacimiento.get(Calendar.MONTH) ||
                (fechaActual.get(Calendar.MONTH) == fechaNacimiento.get(Calendar.MONTH) &&
                        fechaActual.get(Calendar.DAY_OF_MONTH) < fechaNacimiento.get(Calendar.DAY_OF_MONTH))) {
            edad--;
        }

        String edadTexto;
        // Actualizar el texto con la edad calculada
        if (edad > 1) {
            edadTexto = edad + " años";
        } else if (edad == 1) {
            edadTexto = edad + " año";
        } else {
            int meses = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
            if (meses < 0) {
                meses = meses + 12; // Ajustar si el mes actual es menor al mes de nacimiento
            }
            if (meses == 1) {
                edadTexto = meses + " mes";
            } else {
                edadTexto = meses + " meses";
            }
        }

        edadPerro.setText(edadTexto);
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
            savePets.saveDogs(nombre, raza, edad, sexo,b, this);

        }
    }
    @Override
    public void onSaveComplete() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveFailure() {
        // Manejar el fallo en la carga de imagen o creación del documento
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}