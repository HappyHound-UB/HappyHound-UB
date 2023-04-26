package edu.ub.happyhound_app;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class GoogleLogIn extends LogIn {
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "SignIn";
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    public GoogleLogIn(GoogleSignInClient googleSignInClient, FirebaseAuth mAuth) {
        this.googleSignInClient = googleSignInClient;
        this.mAuth = mAuth;
    }

    protected void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google Sign In intent result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Inicializar inicio de sesión cuenta
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                firebaseAuthGoogle(googleSignInAccount);

            } catch (ApiException e) {
                displayToast(e.getMessage());
            }
        }
    }

    private void firebaseAuthGoogle(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "firebaseAuthgoogle: begin firebase auth with google");
        // When sign in account is not equal to null initialize auth credential
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        // Check credential
        mAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // usuario actual
                        FirebaseUser user = mAuth.getCurrentUser();
                        // info del usuario

                        if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                            String info = "Cuenta creada...\n" + Objects.requireNonNull(user).getEmail();

                            displayToast(info);
                            SaveUserInfo.saveUsers("Google Users", Objects.requireNonNull(user.getDisplayName()), user.getEmail());
                        } else
                            displayToast("Google Sign In successful");

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayToast("Error signing in  with Google");
                    }
                });
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}