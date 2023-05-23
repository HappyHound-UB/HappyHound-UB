package edu.ub.happyhound_app.model;

import android.widget.EditText;

/**
 * Interfície para manejar Successes y Failures de los metodos de Firebase
 */
public interface FirebaseListener {
    static void onDataRetrieved(EditText editText, String data) {
        editText.setText(data);
    }


    void onSuccess();

    void onFailure();


}
