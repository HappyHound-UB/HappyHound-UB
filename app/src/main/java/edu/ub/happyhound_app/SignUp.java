package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText newName, newEmail, newPassword;
    private TextView  btnSignIn;
    private Button btnContinue;
    private CheckBox conditions;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference = FirebaseFirestore.getInstance().document("Users/Verified Users");
    //private DocumentReference documentReference = FirebaseFirestore.getInstance().collection().document().collection().....
    private String name, email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = newName.getText().toString();
                email = newEmail.getText().toString();
                password = newPassword.getText().toString();

                signUp(name, email, password);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

    }

    protected void signUp(String name, String email, String password){
        if(name.isEmpty()|| email.isEmpty() || password.isEmpty()) {
            if (name.isEmpty()) {
                Toast.makeText(SignUp.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            }
            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
            if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //falta classe home --> despues de iniciar sesion pasamos a la clase home

                        //FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    saveUsers();
                    finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUp.this, "Sign In failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    protected void saveUsers(){
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()){ return; }


        Map<String, Object> users = new HashMap<String, Object>();
        users.put("name", name);
        users.put("email", email);
        users.put("password", password);

        documentReference.set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

}