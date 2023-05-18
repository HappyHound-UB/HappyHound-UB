package edu.ub.happyhound_app.model;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SavePetInfo {
    FirebaseFirestore db;
    DocumentReference documentReference;
    private FirebaseAuthManager<SavePetInfo> authManager;
    private StorageReference mStorageRef;
    private Activity activity;

    public SavePetInfo(Activity activity) {
        this.activity = activity;
        authManager = new FirebaseAuthManager<>();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    /**
     * Funcion para guardar datos del perro en el firebase
     *
     * @param nombre nombre del perro a guardar
     * @param raza   raza del perro
     * @param edad   edad del perro
     * @param sexo   sexo del perro
     * @param b      bitmap
     */
    public void saveDogs(String nombre, String raza, String edad, String sexo, Bitmap b) {
        if (nombre.isEmpty() || raza.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
            return;
        }

        String userID = authManager.getUser().getUid();

        documentReference = db.collection("Users").document(userID)
                .collection("Lista Perros").document(nombre);

        Map<String, Object> perros = new HashMap<>();
        perros.put("name", nombre);
        perros.put("raza", raza);
        perros.put("edad", edad);
        perros.put("sexo", sexo);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child(authManager.getUser().getEmail() + "/" + nombre + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            ToastMessage.displayToast(activity.getApplicationContext(), "\"Fallo al subir la imagen\"");
        }).addOnSuccessListener(taskSnapshot -> {
            // Handle successful uploads
            // ToastMessage.displayToast(activity.getApplicationContext(), "Imagen subida con éxito");
        });

        documentReference.set(perros)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    ToastMessage.displayToast(activity.getApplicationContext(), "Perro guardado con éxito");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    ToastMessage.displayToast(activity.getApplicationContext(), "No se ha podido guardar el `perro");
                });
    }
}
