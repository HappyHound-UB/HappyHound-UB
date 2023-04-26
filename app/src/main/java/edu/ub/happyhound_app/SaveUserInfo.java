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
import java.util.Objects;

public class SaveUserInfo {


    public SaveUserInfo() {
    }

    /**
     * Función para guardar los usuarios registrados
     *
     * @param type  dependiendo del tipo (Registro a través de la app, Google o Facebook) creamos c
     *              colecciones para dividir los usuarios
     * @param name  nombre del usario
     * @param email email del registro del usuario
     */
    protected static void saveUsers(String type, String name, String email) {
        DocumentReference documentReference;
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        documentReference = FirebaseFirestore.getInstance().collection(type).document(userID);

        if (name.isEmpty() || email.isEmpty()) {
            return;
        }

        Map<String, Object> users = new HashMap<>();
        users.put("name", name);
        users.put("email", email);

        documentReference.set(users)
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
