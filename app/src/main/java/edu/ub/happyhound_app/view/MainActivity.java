package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.databinding.ActivityMainBinding;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.model.NotificationManager;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuthManager<MainActivity> authManager;
    private NotificationManager notificationManager;
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
        notificationManager = new NotificationManager(this);
        notificationManager.reminderPassed();

        int targetFragment = getIntent().getIntExtra("targetFragment", 1); // Default to Fragment 1

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    tag = "home_fragment";
                    break;
                case R.id.agenda:
                    selectedFragment = new AgendaFragment();
                    tag = "agenda_fragment";
                    break;
                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    tag = "profile_fragment";
                    break;
            }

            //if (!isSameFragment(tag))
            changeFragments(selectedFragment, tag);

            return true;
        });

        // si queremos ir a otro fragmento desde otras actividades usamos este switch
        switch (targetFragment) {
            case 2:
                selectedFragment = new AgendaFragment();
                tag = "agenda_fragment";
                break;
            case 3:
                selectedFragment = new ProfileFragment();
                tag = "profile_fragment";
                break;
            default:
                selectedFragment = new HomeFragment();
                tag = "home_fragment";
                break;
        }

        changeFragments(selectedFragment, tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment editProfileFragment = getSupportFragmentManager().findFragmentByTag("editProfile_fragment");
        if (editProfileFragment != null) {
            editProfileFragment.onActivityResult(requestCode, resultCode, data);
        }

        Fragment infoPerrosFragment = getSupportFragmentManager().findFragmentByTag("infoPerros_fragment");
        if (infoPerrosFragment != null) {
            infoPerrosFragment.onActivityResult(requestCode, resultCode, data);
        }
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
        androidx.fragment.app.Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
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
                .replace(R.id.frameLayout , fragment, tag)
//                .setReorderingAllowed(true)
//                .addToBackStack("name")
                .commit();
        //fragmentTransaction.commit();
    }

}