package edu.ub.happyhound_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import edu.ub.happyhound_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuthManager<MainActivity> authManager;
    private ActivityMainBinding binding;
    Fragment selectedFragment = null;
    String tag = "";

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new FirebaseAuthManager<>(this, this);

        changeFragments(new HomeFragment(), "home_fragment");
        //DynamicColors.applyToActivitiesIfAvailable(this.getApplication());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    tag = "home_fragment";
                    break;
                case R.id.add:
                    selectedFragment = new AddFragment();
                    tag = "add_fragment";
                    break;
                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    tag = "profile_fragment";
                    break;
            }

            if (!isSameFragment(tag)) {
                changeFragments(selectedFragment, tag);
            }
            return true;
        });
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================

    /**
     * Función para comprobar si cambiamos al mismo fragmento o no
     *
     * @param tag tag asociado al fragmento respectivo
     * @return retorna true si el fragmento es el mismo que anterior, false en caso contrario
     */
    private boolean isSameFragment(String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        return (currentFragment != null && Objects.equals(currentFragment.getTag(), tag));
    }

    /**
     * Función para cambiar entre los fragmentos disponibles
     *
     * @param fragment fragmento a que queremos cambiar
     * @param tag      associamos un tag al fragmento
     */
    private void changeFragments(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.frameLayout, fragment, tag)
//                .setReorderingAllowed(true)
//                .addToBackStack("name")
                .commit();
        //fragmentTransaction.commit();
    }

}