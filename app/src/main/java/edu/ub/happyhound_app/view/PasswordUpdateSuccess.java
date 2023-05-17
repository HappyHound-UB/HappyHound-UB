package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.ub.happyhound_app.R;

public class PasswordUpdateSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update_success);

        Button logIn = findViewById(R.id.success_logIn_btn);

        logIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

}