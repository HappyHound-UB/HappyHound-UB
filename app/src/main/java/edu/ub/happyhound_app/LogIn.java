package edu.ub.happyhound_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;

public class LogIn extends AppCompatActivity {
    private EditText memail, mpassword;
    private TextView btnForgotPassword, btnSignUp;
    private Button btnLogIn;
    private String email, password;
    private SignInButton googleButton;
    private GoogleLogIn googleLogIn;
    private FirebaseAuthManager<LogIn> authManager;

    @Override
    public void onStart() {
        super.onStart();
        authManager.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        authManager = new FirebaseAuthManager<>(this, this);

        memail = (EditText) findViewById(R.id.idEmailAddress);
        mpassword = (EditText) findViewById(R.id.idPassword);
        btnLogIn = (Button) findViewById(R.id.logInButton);
        btnForgotPassword = (TextView) findViewById(R.id.forgotPasswordButton);
        btnSignUp = (TextView) findViewById(R.id.SignUpButton);
        googleButton = (SignInButton) findViewById(R.id.googleButton);

        // iniciar sesion con email y contraseña ya creada
        btnLogIn.setOnClickListener(view -> {
            email = memail.getText().toString().trim();
            password = mpassword.getText().toString().trim();

            if (everythingOK(email, password))
                authManager.signIn(email, password);
            else
                displayError();
        });

        // crear nueva cuenta
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

        // iniciar sesion con la cuenta de google
        googleLogIn = new GoogleLogIn(this, authManager.getmAuth());
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogIn.signInGoogle();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleLogIn.onActivityResult(requestCode, resultCode, data);
    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================

    /**
     * funcion para comprabar si han introducido datos
     *
     * @return true si hay datos introducidos y false en caso contrario
     */
    private boolean everythingOK(String email, String password) {
        return (!(email.isEmpty() || password.isEmpty()));
    }

    /**
     * función para mostrar error, miramos que falta para completar y mostramos error con Toast
     */
    private void displayError() {
        if (email.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Introduzca un correo electrónico");

        if (password.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Introduce una contraseña");
    }

    // ========================================
    //              GETTERS
    // ========================================

    public EditText getMemail() {
        return memail;
    }

    public EditText getMpassword() {
        return mpassword;
    }
}