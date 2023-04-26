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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
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
    private Button btnLogIn;
    private FirebaseAuth mAuth;
    private String email, password;
    private SignInButton googleLogIn;
    private GoogleSignInClient googleSignInClient;

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        mAuth = FirebaseAuth.getInstance();
        memail = (EditText) findViewById(R.id.idEmailAddress);
        mpassword = (EditText) findViewById(R.id.idPassword);
        btnLogIn = (Button) findViewById(R.id.logInButton);
        btnForgotPassword = (TextView) findViewById(R.id.forgotPasswordButton);
        btnSignUp = (TextView) findViewById(R.id.SignUpButton);
        googleLogIn = (SignInButton) findViewById(R.id.googleButton);

        btnLogIn.setOnClickListener(view -> {
            email = memail.getText().toString().trim();
            password = mpassword.getText().toString().trim();

            signIn(email, password);
        });

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        });

        //cambiar la contraseña
        btnForgotPassword.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),
                    "Ya puedes cambiar la contraseña", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(intent);

        });

        googleLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLogIn googleLogIn = new GoogleLogIn(getGoogleSignInClient(), getmAuth());
                googleLogIn.signInGoogle();
            }
        });
    }


    /**
     * Funcion para iniciar sesión con el email y la contraseña dada
     *
     * @param email    email del usuario que se utilizara para iniciar la sesión
     * @param password contraseña creada para entrar
     */
    protected void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty())
                displayToast("Introduzca un correo electrónico");

            if (password.isEmpty())
                displayToast("Introduce una contraseña");

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
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    // El usuario no existe y mosramos error
                                    memail.setError("Email no existe");
                                    memail.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    // Credenciales inválidas (por ejemplo, contraseña incorrecta)
                                    mpassword.setError("Email y/o contraseña incorrecta");
                                    mpassword.requestFocus();
                                } catch (Exception e) {
                                    displayToast("No se ha podido iniciar sesión");
                                }
                            }
                        }
                    });
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }
}