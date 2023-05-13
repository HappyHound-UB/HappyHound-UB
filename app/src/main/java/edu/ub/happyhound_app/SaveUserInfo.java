package edu.ub.happyhound_app;

import android.app.Activity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SaveUserInfo {
    private final Activity activity;
    private FirebaseAuthManager<SaveUserInfo> authManager;
    private FirebaseFirestore db;

    public SaveUserInfo(Activity activity) {
        this.activity = activity;
        authManager = new FirebaseAuthManager<>();
        db = FirebaseFirestore.getInstance();
    }

    public void saveUserInfo(String nombre, String numero, String direccion, String ciudad, String codigoPostal) {
        DocumentReference documentReference;

        String userID = authManager.getUser().getUid();
        String email = authManager.getUser().getEmail();
        String doc;

        if (email != null) doc = authManager.getUser().getEmail();
        else doc = nombre;

        documentReference = db.collection("Google Users").document(userID)
                .collection("User Info").document(doc);

        Map<String, Object> users = new HashMap<>();
        users.put("Name", nombre);
        users.put("Phone Number", numero);
        users.put("Address", direccion);
        users.put("City", ciudad);
        users.put("Postal Code", codigoPostal);

        documentReference.set(users)
                .addOnSuccessListener(documentReference1 ->
                        ToastMessage.displayToast(activity, "Perfil actualizado con éxito"))
                .addOnFailureListener(e ->
                        ToastMessage.displayToast(activity, "No se ha podido actualizar el perfil"));
    }
}
