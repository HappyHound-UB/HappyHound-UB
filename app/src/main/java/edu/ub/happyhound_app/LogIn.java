package edu.ub.happyhound_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    private EditText memail, mpassword;
    private TextView btnForgotPassword, btnSignUp;
    private Button btnLogIn, btnGoogle, btnFacebook;
    private FirebaseAuth mAuth;
    private String email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //currentUser.reload();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        memail = (EditText) findViewById(R.id.idEmailAddress);
        mpassword = (EditText) findViewById(R.id.idPassword);
        btnLogIn = (Button) findViewById(R.id.logInButton);
        btnForgotPassword = (TextView) findViewById(R.id.forgotPasswordButton);
        btnSignUp = (TextView) findViewById(R.id.SignUpButton);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = memail.getText().toString();
                password = mpassword.getText().toString();

                signIn(email, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Funcion para iniciar sesión con el email y la contraseña dada
     *
     * @param email
     * @param password
     */
    protected void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
            if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Please enter a valid password", Toast.LENGTH_SHORT).show();
            }
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Inicio de sesión fallido
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthInvalidUserException) {
                                    // El usuario no existe y mosramos error
                                    memail.setError("Usuario no existe");

                                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Credenciales inválidas (por ejemplo, contraseña incorrecta)
                                    mpassword.setError("Email y/o contraseña incorrecta");
                                    mpassword.requestFocus();
                                } else {
                                    // Otro error
                                }
                            }
                        }
                    });
        }


    }
}