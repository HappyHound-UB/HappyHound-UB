package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SavePetInfo {
    private FirebaseAuth mAuth;

    public SavePetInfo(FirebaseAuth firebaseAuth) {
        this.mAuth = firebaseAuth;
    }

    protected void saveDogs(String nombre, String raza, String edad, String sexo) {
        DocumentReference documentReference;
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
        //perros.put("imagen", image);

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
