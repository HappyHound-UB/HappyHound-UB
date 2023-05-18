package edu.ub.happyhound_app.model;

import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

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

}
