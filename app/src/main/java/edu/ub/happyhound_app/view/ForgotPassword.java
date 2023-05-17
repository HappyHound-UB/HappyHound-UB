package edu.ub.happyhound_app.view;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.model.ToastMessage;

public class ForgotPassword extends AppCompatActivity {
    private EditText memail;
    private TextView btnReturn;
    private Button btnResetPassword;
    private ProgressBar progressBar;
    private ConstraintLayout layout;
    private FirebaseAuthManager<ForgotPassword> authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authManager = new FirebaseAuthManager<>(this, this);

        memail = findViewById(R.id.edit_text_email);
        btnResetPassword = findViewById(R.id.button_reset_password);
        btnReturn = findViewById(R.id.btn_return);
        progressBar = findViewById(R.id.progress_bar);
        layout = findViewById(R.id.forgotPasswordConstraint);

        int dynamic = DynamicLayout.setDynamicLayout(this);
        layout.setBackgroundColor(dynamic);
        btnReturn.setTextColor(dynamic);

        btnResetPassword.setOnClickListener(view -> {
            String emailAddress = memail.getText().toString().trim();
            if (isEmailCorrect(emailAddress)) {
                progressBar.setVisibility(View.VISIBLE);
                authManager.resetPassword(emailAddress);
                progressBar.setVisibility(View.GONE);
            } else displayError(emailAddress);
        });

        btnReturn.setOnClickListener(view -> finish());
    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================

    /**
     * Funcion para comprabar si ha introducido email y comprobamos si tiene el formato correcto
     *
     * @return true si hay datos introducidos y el email tiene el formato correcto
     * en caso contrario, retornamos false
     */
    private boolean isEmailCorrect(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Función para mostrar error
     *
     * @param email email introducido por usuarios
     */
    private void displayError(String email) {
        if (email.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Introduzca un correo electrónico");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getMemail().setError("Introduzca un correo electrónico válido");
            getMemail().requestFocus();
        }
    }

    // ========================================
    //              GETTERS
    // ========================================

    public EditText getMemail() {
        return memail;
    }
}