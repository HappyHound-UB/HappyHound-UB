package edu.ub.happyhound_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity {
    private EditText memail;
    private TextView btnReturn;
    private Button btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

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
            resetPassword(emailAddress);
        });

        btnReturn.setOnClickListener(view -> finish());
    }

    private void resetPassword(String emailAddress) {
        if (emailAddress.isEmpty()) {
            memail.setError("Introduzca un correo electrónico");
            memail.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            memail.setError("Introduzca un correo electrónico válido");
            memail.requestFocus();

        } else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Correo electrónico de restablecimiento de contraseña enviado con éxito
                                Toast.makeText(getApplicationContext(),
                                        "Correo electrónico de restablecimiento de contraseña enviado con éxito",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LogIn.class);

                                // Limpiar stack para evitar que el usuario regrese a esta actividad
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                // Error al enviar el correo electrónico de restablecimiento de contraseña
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    memail.setError("Usuario no existe");
                                    memail.requestFocus();

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Error al enviar el correo electrónico de restablecimiento de contraseña",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
        progressBar.setVisibility(View.GONE);
    }

}