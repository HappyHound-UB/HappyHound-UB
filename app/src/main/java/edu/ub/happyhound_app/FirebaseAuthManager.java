package edu.ub.happyhound_app;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager<T> {
    private FirebaseAuth mAuth;
    private Activity activity;
    private T type;

    public FirebaseAuthManager(Activity activity, T classType) {
        this.activity = activity;
        this.type = classType;
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = getmAuth().getCurrentUser();
        if (currentUser != null) {
            //currentUser.reload();
            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * Funcion para iniciar sesión con el email y la contraseña dada
     *
     * @param email    email del usuario que se utilizara para iniciar la sesión
     * @param password contraseña creada para entrar
     */
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                // si cumple el sign in entramos en la aplicacion
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }
                    // si produce un fallo, mostramos el fallo
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Inicio de sesión fallido
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            // El usuario no existe y mosramos error
                            ((LogIn) type).getMemail().setError("Email no existe");
                            ((LogIn) type).getMemail().requestFocus();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Credenciales inválidas (por ejemplo, contraseña incorrecta)
                            ((LogIn) type).getMpassword().setError("Email y/o contraseña incorrecta");
                            ((LogIn) type).getMpassword().requestFocus();
                        } else
                            ToastMessage.displayToast(activity.getApplicationContext(), "No se ha podido iniciar sesión");
                    }
                });
    }

    /**
     * funcion para crear nueva cuenta de firebase
     *
     * @param name     nombre de usuario
     * @param email    email para acceder a la cuenta
     * @param password contraseña para acceder
     */
    public void signUp(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                // si el proceso de crear cuenta ha logrado con exito, guardamos datos del
                // del usuario en el firebase
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SaveUserInfo saveData = new SaveUserInfo(getmAuth());
                        saveData.saveUsers("New Account", name, email);
                    }
                })
                // si ha cumplido el proceso con exito entramos en la app
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                            ToastMessage.displayToast(activity.getApplicationContext(), name + " tu cuenta ha sido creado con éxito!");
                        }
                    }
                    // si produce un fallo, mostramos el fallo
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            // El usuario ya existe
                            ((SignUp) type).getNewEmail().setError("Ya existe un usuario con este email");
                            ((SignUp) type).getNewEmail().requestFocus();
                        } else if (e instanceof FirebaseAuthWeakPasswordException) {
                            // Contraseña no segura
                            ((SignUp) type).getNewPassword().setError("La contraseña debe tener al menos 6 caracteres y contener letras y números.");
                            ((SignUp) type).getNewPassword().requestFocus();
                        } else {
                            // Otro error
                            ToastMessage.displayToast(activity.getApplicationContext(), "No se ha podido crear la cuenta");
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(activity.getApplicationContext(), LogIn.class);
        activity.startActivity(intent);
        activity.finish();

    }

    // ========================================
    //              GETTERS
    // ========================================
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

}