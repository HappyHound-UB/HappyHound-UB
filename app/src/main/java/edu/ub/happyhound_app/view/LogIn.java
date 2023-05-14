package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.common.SignInButton;

import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.R;

public class LogIn extends AppCompatActivity {
    private EditText memail, mpassword;
    private TextView btnForgotPassword, btnSignUp;
    private Button btnLogIn, btnFacebook;
    private String email, password;
    private SignInButton googleButton;
    private GoogleLogIn googleLogIn;
    private FirebaseAuthManager<LogIn> authManager;
    private ConstraintLayout layout;

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

        memail = findViewById(R.id.idEmailAddress);
        mpassword = findViewById(R.id.idPassword);
        btnLogIn = findViewById(R.id.logInButton);
        btnForgotPassword = findViewById(R.id.forgotPasswordButton);
        btnSignUp = findViewById(R.id.SignUpButton);
        googleButton = findViewById(R.id.googleButton);
        btnFacebook = findViewById(R.id.facebookButton);
        layout = findViewById(R.id.logInConstraint);

        int dynamic = DynamicLayout.setDynamicLayout(this);
        layout.setBackgroundColor(dynamic);
        btnSignUp.setTextColor(dynamic);

        // iniciar sesion con email y contraseña ya creada
        btnLogIn.setOnClickListener(view -> {
            email = memail.getText().toString().trim();
            password = mpassword.getText().toString().trim();

            if (everythingOK(email, password))
                authManager.signIn(email, password);
            else {
                displayError();
                finish();
            }
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
        googleButton.setOnClickListener(view -> {
            googleLogIn.signInGoogle();
//            finish();
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