package edu.ub.happyhound_app.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.ToastMessage;

public class SignUp extends AppCompatActivity {

    private EditText newName, newEmail, newPassword;
    private TextView btnSignIn;
    private Button btnContinue;
    private CheckBox conditions;
    private String name, email, password;
    private FirebaseAuthManager<SignUp> authManager;
    private ConstraintLayout layout;

    @Override
    public void onStart() {
        super.onStart();
        authManager.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        authManager = new FirebaseAuthManager<>(this, this);

        newName = findViewById(R.id.idNombreUsuario);
        newEmail = findViewById(R.id.idEmailAddressSU);
        newPassword = findViewById(R.id.idPasswordSU);
        btnContinue = findViewById(R.id.continueButtonSU);
        btnSignIn = findViewById(R.id.signInButtonSU);
        conditions = findViewById(R.id.checkBoxConditions);
        layout = findViewById(R.id.signInConstraint);

        int dynamic = DynamicLayout.setDynamicLayout(this);
        layout.setBackgroundColor(dynamic);
        btnSignIn.setTextColor(dynamic);

        // creación de nueva cuenta
        btnContinue.setOnClickListener(view -> {
            name = newName.getText().toString().trim();
            email = newEmail.getText().toString().trim();
            password = newPassword.getText().toString().trim();

            if (everythingOK(name, email, password) && conditions.isChecked())
                authManager.signUp(name, email, password);
            else {
                displayError();
                conditions.setTextColor(Color.RED);
            }
        });
        // si el usuario tiene cuenta puede volver a la pagina de log in
        btnSignIn.setOnClickListener(view -> finish());
        conditions.setOnCheckedChangeListener((compoundButton, b) -> conditions.setTextColor(Color.GRAY));
    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================

    /**
     * funcion para comprabar si han introducido datos y
     * en caso de email comprobamos si tiene el formato correcto
     *
     * @return true si hay datos introducidos y el email tiene el formato correcto
     * en caso contrario, retornamos false
     */
    private boolean everythingOK(String name, String email, String password) {
        return (!(name.isEmpty() || email.isEmpty() || password.isEmpty())) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * función para mostrar error, miramos que falta para completar y mostramos erroes con Toast
     */
    private void displayError() {
        if (name.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Escribe el nombre completo");
        if (email.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Introduzca un correo electrónico");
        if (password.isEmpty())
            ToastMessage.displayToast(getApplicationContext(), "Ingrese una contraseña válida y vuelva a intentarlo");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmail.setError("Introduzca un correo electrónico válido");
            newEmail.requestFocus();
        }
    }

    // ========================================
    //              GETTERS
    // ========================================

    public EditText getNewEmail() {
        return newEmail;
    }

    public EditText getNewPassword() {
        return newPassword;
    }

}