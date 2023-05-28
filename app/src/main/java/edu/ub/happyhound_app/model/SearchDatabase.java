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
import java.util.Objects;

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

    public void getReminders(StorageListener listener, String alarmType) {
        String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        List<Reminder> reminderList = new ArrayList<>();

        CollectionReference listaPerrosCollectionRef = db.collection("Users")
                .document(uID).collection("Lista Perros");

        // Hacemos un query con todos los perros que el usuario tiene agregados
        listaPerrosCollectionRef.get().addOnSuccessListener(querySnapshot -> {
            List<Task<?>> tasks = new ArrayList<>(); // Store all subcollection queries

            for (QueryDocumentSnapshot petDoc : querySnapshot) {
                CollectionReference reference = petDoc.getReference().collection("Reminders");

                Task<QuerySnapshot> task = reference.whereEqualTo("type", alarmType)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot reminderDoc : queryDocumentSnapshots) {
                                String name = reminderDoc.getString("name");
                                String type = reminderDoc.getString("type");
                                String description = reminderDoc.getString("description");
                                String date = reminderDoc.getString("date");
                                String time = reminderDoc.getString("time");

                                Reminder reminder = new Reminder(name, type, description, date, time);
                                reminderList.add(reminder);

                            }
                        });
                tasks.add(task);
            }
            Tasks.whenAllComplete(tasks).addOnCompleteListener(taskList -> {
                // All reminders have been collected
                // Pass the reminderList to another method or do further processing
                if (alarmType.equals("Paseo")) {
                    ReminderManager.setPaseoReminderList(reminderList);
                    listener.onSuccessPaseoList();
                } else if (alarmType.equals("Comida")) {
                    ReminderManager.setComidaReminderList(reminderList);
                    listener.onSuccessComidaList();
                } else {
                    ReminderManager.setOtherReminderList(reminderList);
                    listener.onSuccessOtherList();
                }

            });
        });
    }

    public List<String> getAllPets() {
        String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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

    public void searchPetData(String docPath, UserCardAdapter.MyViewHolder holder) {
        db.document(docPath)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Si existe el documento con el path proporcionado buscamos el valor del parametro
                        // que queremos y pasamos la edad del perro

                        String age = documentSnapshot.getString("edad");
                        holder.ageView.setText(age);
                    }
                })
                .addOnFailureListener(e ->
                        // en caso que no path proporcionado no existe, no mostramos nada
                        holder.ageView.setText(""));
    }
}
