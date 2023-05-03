package edu.ub.happyhound_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.DynamicColors;

import edu.ub.happyhound_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuthManager<MainActivity> authManager;
    private ActivityMainBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        // si aun no se ha iniciado la sesion volvemos al login para inicar sesion
        if (authManager.getUser() == null) {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new FirebaseAuthManager<>(this, this);

        changeFragments(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    changeFragments(new HomeFragment());
                    break;
                case R.id.add:
                    changeFragments(new AddFragment());
                    break;
                case R.id.profile:
                    changeFragments(new ProfileFragment());
                    break;
            }

            return true;
        });
    }

    private void changeFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.frameLayout, fragment.getClass(), null)
//                .setReorderingAllowed(true)
//                .addToBackStack("name")
                .commit();
        //fragmentTransaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }
}