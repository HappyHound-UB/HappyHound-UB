package edu.ub.happyhound_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText newName, newEmail, newPassword;
    private TextView btnSignIn;
    private Button btnContinue;
    private CheckBox conditions;
    private FirebaseAuth mAuth;
    private String name, email, password;

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
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        newName = (EditText) findViewById(R.id.idNombreUsuario);
        newEmail = (EditText) findViewById(R.id.idEmailAddressSU);
        newPassword = (EditText) findViewById(R.id.idPasswordSU);
        btnContinue = (Button) findViewById(R.id.continueButtonSU);
        btnSignIn = (TextView) findViewById(R.id.signInButtonSU);
        conditions = (CheckBox) findViewById(R.id.checkBoxConditions);

        btnContinue.setOnClickListener(view -> {
            name = newName.getText().toString().trim();
            email = newEmail.getText().toString().trim();
            password = newPassword.getText().toString().trim();

            signUp(name, email, password);
        });

        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        });

    }

    protected void signUp(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            if (name.isEmpty()) displayToast("Escribe el nombre completo");
            if (email.isEmpty()) displayToast("Introduzca un correo electrónico");
            if (password.isEmpty())
                displayToast("Ingrese una contraseña válida y vuelva a intentarlo");

            //
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmail.setError("Introduzca un correo electrónico válido");
            newEmail.requestFocus();
        } else {
            if (conditions.isChecked()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    SaveUserInfo.saveUsers("New Account", name, email);
                                    finish();

                                } else {
                                    // Inicio de sesión fallido
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        // El usuario ya existe
                                        newEmail.setError("Ya existe un usuario con este email");
                                        newEmail.requestFocus();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        // Contraseña no segura
                                        newPassword.setError("La contraseña debe tener al menos 6 caracteres y contener letras y números.");
                                        newPassword.requestFocus();
                                    } catch (Exception e) {
                                        // Otro error
                                        Toast.makeText(SignUp.this,
                                                "No se ha podido crear la cuenta",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}