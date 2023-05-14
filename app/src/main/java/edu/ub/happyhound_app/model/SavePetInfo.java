package edu.ub.happyhound_app.model;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.ub.happyhound_app.view.ToastMessage;

public class SavePetInfo {
    FirebaseFirestore db;
    String accType;
    String[] collections;
    DocumentReference documentReference;
    private FirebaseAuthManager<SavePetInfo> authManager;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private Activity activity;

    public SavePetInfo(Activity activity) {
        this.activity = activity;
        authManager = new FirebaseAuthManager<>();
        db = FirebaseFirestore.getInstance();
        collections = new String[]{"New Account Users", "Google Users", "Facebook Users"};
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

    }

    public void saveDogs(String nombre, String raza, String edad, String sexo, Bitmap b) {
        String userID = authManager.getUser().getUid();

        for (int i = 0; i < collections.length; i++) {
            int finalI = i;
            db.collection(collections[i]).document(userID)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                accType = collections[finalI];
                                addDogs(userID, nombre, raza, edad, sexo, b);
                            }
                        } else
                            Log.d("Error", " Path not found");
                    });
        }
    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================

    private void addDogs(String userID, String nombre, String raza, String edad, String sexo, Bitmap b) {
        if (nombre.isEmpty() || raza.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
            return;
        }

        switch (accType) {
            case "New Account Users":
                documentReference = db.collection("New Account Users").document(userID)
                        .collection("Lista Perros").document(nombre);
                break;
            case "Google Users":
                documentReference = db.collection("Google Users").document(userID)
                        .collection("Lista Perros").document(nombre);
                break;
            case "Facebook Users":
                documentReference = db.collection("Facebook Users").document(userID)
                        .collection("Lista Perros").document(nombre);
                break;
            default:
                documentReference = db.collection("Other Users").document(userID)
                        .collection("Lista Perros").document(nombre);
                break;
        }

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
        }).addOnSuccessListener(taskSnapshot ->
                ToastMessage.displayToast(activity.getApplicationContext(), "Imagen subida con éxito"));

        documentReference.set(perros)
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error adding document", e));
    }
}
