package edu.ub.happyhound_app.model;

import android.app.Activity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SaveUserInfo {
    private final Activity activity;
    private FirebaseAuthManager<SaveUserInfo> authManager;
    private FirebaseFirestore db;


    public SaveUserInfo(Activity activity) {
        this.activity = activity;
        authManager = new FirebaseAuthManager<>();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Función para guardar datos del perfil actualizado del usuario
     *
     * @param nombre       nombre del usuario
     * @param numero       numero de telefono
     * @param direccion    direccion donde vive
     * @param ciudad       ciudad donde vive
     * @param codigoPostal y el codigo postal
     */
    public void saveUserInfo(String nombre, String numero, String direccion, String ciudad, String codigoPostal) {
        DocumentReference documentReference;

        String userID = authManager.getUser().getUid();
        String email = authManager.getUser().getEmail();

        documentReference = db.collection("Users").document(userID);

        Map<String, Object> users = new HashMap<>();
        users.put("Email", Objects.requireNonNull(email));
        users.put("Name", nombre);
        users.put("Phone Number", numero);
        users.put("Address", direccion);
        users.put("City", ciudad);
        users.put("Postal Code", codigoPostal);

        documentReference.set(users)
                .addOnSuccessListener(documentReference1 -> {
                    ToastMessage.displayToast(activity, "Perfil actualizado con éxito");
                    // si se ha añadido correctamente los datos, modificamos el DisplayName también del usuario
                    authManager.changeUsername(nombre);
                })
                .addOnFailureListener(e ->
                        ToastMessage.displayToast(activity, "No se ha podido actualizar el perfil"));
    }
}
