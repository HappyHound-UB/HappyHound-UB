package edu.ub.happyhound_app.model;

import android.content.Context;
import android.widget.Toast;

public class ToastMessage {

    public static void displayToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

}
