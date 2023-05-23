package edu.ub.happyhound_app.model;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class NotificationManager {

    private static final int REQUEST_ID = 0;
    private Activity activity;
    private FirebaseAuthManager<NotificationManager> authManager;

    public NotificationManager(Activity activity) {
        this.activity = activity;
        authManager = new FirebaseAuthManager<>();
    }

    public void reminderPassed() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uID = authManager.getUser().getUid();

        // Accedemos a la subcoleccion donde tenemos todos los perros guardados
        CollectionReference listaPerrosCollectionRef = db.collection("Users").document(uID).collection("Lista Perros");

        // Hacemos un query con todos los perros que el usuario tiene agregados
        listaPerrosCollectionRef.get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot petDoc : querySnapshot) {
                CollectionReference remindersCollectionRef = petDoc.getReference().collection("Reminders");
                // Miramos que documento tiene el reminder pasado
                Query expiredRemindersQuery = remindersCollectionRef.whereLessThan("timeInMillis", System.currentTimeMillis());

                expiredRemindersQuery.get().addOnSuccessListener(remindersSnapshot -> {
                    // Iteramos por cada documento del cual el reminder ha pasado y lo eliminamos
                    for (QueryDocumentSnapshot reminderDoc : remindersSnapshot) {
                        reminderDoc.getReference().delete();
                    }
                });
            }
        });
    }

    public void setReminder(Reminder reminder, long reminderTimestamp, String selectedPet) {

        if (timePassed(reminderTimestamp)) {
            ToastMessage.displayToast(activity, "El tiempo ha pasado");
            return;
        }

        // Create an intent to launch the notification
        Intent intent = new Intent(activity, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, REQUEST_ID, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager instance
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

        // Set the reminderTimestamp as the trigger time for the notification
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);

        saveReminder(reminder, reminderTimestamp, selectedPet);
    }

    private void saveReminder(Reminder reminder, long reminderTimestamp, String selectedPet) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uID = authManager.getUser().getUid();
        String day = reminder.getDate();
        String time = reminder.getTime();

        DocumentReference documentReference = db.collection("Users/" + uID + "/Lista Perros/" + selectedPet + "/Reminders")
                .document(String.valueOf(reminderTimestamp));

        documentReference
                .set(reminder)
                .addOnSuccessListener(unused -> ToastMessage.displayToast(activity,
                        "Recordatorio programado:\n " + day + " a las " + time))
                .addOnFailureListener(e -> ToastMessage.displayToast(activity, "Error"));
    }

    private boolean timePassed(long time) {
        return System.currentTimeMillis() >= time;
    }

}
