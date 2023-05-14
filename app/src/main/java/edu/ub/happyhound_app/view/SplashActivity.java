package edu.ub.happyhound_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ub.happyhound_app.R;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_TIMEOUT = 3000;
    private ImageView logoApp;
    private TextView marcaApp;
    private Animation animTop, animBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        logoApp = findViewById(R.id.logoImage);
        marcaApp = findViewById(R.id.appName);

        animTop = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        animBottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        logoApp.setAnimation(animTop);
        marcaApp.setAnimation(animBottom);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LogIn.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIMEOUT); // Espera 3 segundos
    }
}