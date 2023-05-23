package edu.ub.happyhound_app.model;

import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase model para buscar datos del Firebase database
 */
public class SearchDatabase {
    private FirebaseFirestore db;

    public SearchDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Funcion para buscar datos guardados del usuario y mostrarle
     * Con esta funcion no vulneraremos el principio Open Close del SOLID, ya que si en futuro
     * si guardamos más datos del usuario no tendremos que modificar mucho el codigo sino solo
     * cambiando el camino de documento podremos buscar lo que queremos y mostrarle al usuario
     *
     * @param docPath  path del documento que queremos
     * @param field    parametro que buscamos
     * @param editText editText donde queremos mostrar el valor buscado de database
     */
    public void searchUserData(String docPath, String field, EditText editText) {
        db.document(docPath)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Si existe el documento con el path proporcionado buscamos el valor del parametro
                        // que queremos y pasamos el valor al metodo statico del listener para que actualize
                        // el valor obtenido en el campo de editText dado

                        String data = documentSnapshot.getString(field);
                        FirebaseListener.onDataRetrieved(editText, data);
                    }
                })
                .addOnFailureListener(e ->
                        // en caso que no path proporcionado no existe, pasamos null
                        FirebaseListener.onDataRetrieved(editText, null));
    }

    public void getAllReminders(FirebaseListener listener) {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Reminder> reminderList = new ArrayList<>();

        CollectionReference listaPerrosCollectionRef = db.collection("Users")
                .document(uID).collection("Lista Perros");

        // Hacemos un query con todos los perros que el usuario tiene agregados
        listaPerrosCollectionRef.get().addOnSuccessListener(querySnapshot -> {
            List<Task<?>> tasks = new ArrayList<>(); // Store all subcollection queries

            for (QueryDocumentSnapshot petDoc : querySnapshot) {
                CollectionReference reference = petDoc.getReference().collection("Reminders");

                Task<QuerySnapshot> task = reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot reminderDoc : queryDocumentSnapshots) {
                        String title = reminderDoc.getString("title");
                        String description = reminderDoc.getString("description");
                        String date = reminderDoc.getString("date");
                        String time = reminderDoc.getString("time");

                        Reminder reminder = new Reminder(title, description, date, time);
                        reminderList.add(reminder);


                    }
                });
                tasks.add(task);
            }
            Tasks.whenAllComplete(tasks).addOnCompleteListener(taskList -> {
                // All reminders have been collected
                // Pass the reminderList to another method or do further processing
                ReminderManager.setReminderList(reminderList);
                listener.onSuccess();

            });
        });
    }

    public List<String> getAllPets() {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<String> petList = new ArrayList<>();
        petList.add("Elige un perro");

        CollectionReference listaPerrosCollectionRef = db.collection("Users")
                .document(uID).collection("Lista Perros");

        // Hacemos un query con todos los perros que el usuario tiene agregados
        listaPerrosCollectionRef.get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot petDoc : querySnapshot) {
                String petDocId = petDoc.getId();
                petList.add(petDocId);
            }
        });

        return petList;
    }

}
