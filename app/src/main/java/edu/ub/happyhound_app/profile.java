package edu.ub.happyhound_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    private Button btn_settings;
    private Button btn_list;

    private  TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_list =(Button) findViewById(R.id.button_dog_list);
        btn_settings = (Button) findViewById(R.id.button_settings);

        name = (TextView) findViewById(R.id.textView_nombre2);
        email = (TextView) findViewById(R.id.textView_email2);

        name.setText("Martin");
        email.setText("malik129@gmail.com");

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, Lista_perros.class);
                startActivity(intent);            }
        });
    }


}