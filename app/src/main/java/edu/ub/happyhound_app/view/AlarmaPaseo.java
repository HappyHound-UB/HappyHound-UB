package edu.ub.happyhound_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ub.happyhound_app.R;

public class AlarmaPaseo extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private Button btnCancelar;
    private static final String CHANNEL_ID = "channel_id";
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_paseo);

        Button btnCancelar = findViewById(R.id.buttonCancelar);
        Button btnGuardar = findViewById(R.id.buttonGuardar);
        EditText mensajeNotificacion = (EditText) findViewById(R.id.editTextMensaje);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Inicializa el NotificationManagerCompat
        notificationManager = NotificationManagerCompat.from(this);

        // Crea el canal de notificación
        createNotificationChannel();

        //rellenamos los spinners de horas y minutos.
        List<String> hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(String.format("%02d", i)); // Agregar horas en formato 00 a 23
        }
        List<String> minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutesList.add(String.format("%02d", i)); // Agregar minutos en formato 00 a 59
        }
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hoursList);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner hourSpinner = findViewById(R.id.hour_spinner);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, minutesList);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner minuteSpinner = findViewById(R.id.minute_spinner);
        minuteSpinner.setAdapter(minuteAdapter);

        // Crear el Intent que se lanzará cuando se reciba la alarma
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //this.alarmIntent = pendingIntent;

        //hasta aqui es todo spinners.

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mensajeNotificacion.getText().toString();
                int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString());
                int minute = Integer.parseInt(minuteSpinner.getSelectedItem().toString());

                // Calcular la hora de la alarma
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.set(Calendar.HOUR_OF_DAY, hour);
                alarmTime.set(Calendar.MINUTE, minute);

                // Establecer la alarm
                setAlarm(alarmTime.getTimeInMillis());

                // Mostrar mensaje de confirmación
                Toast.makeText(getApplicationContext(), "Alarma establecida para las " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
            }

            private void setAlarm(long timeInMillis) {
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Volver a la actividad anterior
                Intent intent = new Intent(getApplicationContext(), CrearRecordatorio.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void sendNotification(Context context, String message) {
        // Crea el intent que abrirá la actividad cuando se toque la notificación
        Intent intent = new Intent(context, LogIn.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Crea la notificación
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Notificación Paseo!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Envía la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, notification);
    }


    public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Aquí es donde se ejecuta la acción cuando se recibe la alarma
            String message = intent.getStringExtra("message");
            sendNotification(context, message);
        }

        private void sendNotification(Context context, String message) {
            // Aquí se envía la notificación

        }
    }


}