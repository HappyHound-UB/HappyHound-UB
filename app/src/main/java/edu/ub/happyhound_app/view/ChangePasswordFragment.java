package edu.ub.happyhound_app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.model.ToastMessage;

public class ChangePasswordFragment extends Fragment implements AuthenticationListener {

    private FirebaseAuthManager<ChangePasswordFragment> authManager;
    private TextInputLayout oldPassword, newPassword, confirmPassword;
    private String oldpass, newpass, confpass;

    public ChangePasswordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authManager = new FirebaseAuthManager<>(getActivity(), this);

        oldPassword = requireActivity().findViewById(R.id.old_password);
        newPassword = requireActivity().findViewById(R.id.new_password);
        confirmPassword = requireActivity().findViewById(R.id.confirm_password);
        Button update = requireActivity().findViewById(R.id.update_password);
        ImageView back = requireActivity().findViewById(R.id.return_back);

        back.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });

        update.setOnClickListener(view1 -> {
            oldpass = Objects.requireNonNull(oldPassword.getEditText()).getText().toString().trim();
            newpass = Objects.requireNonNull(newPassword.getEditText()).getText().toString().trim();
            confpass = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString().trim();

            if (oldpass.isEmpty() || newpass.isEmpty() || confpass.isEmpty())
                displayError();
            else authManager.reAuthenticate(this, oldpass);
        });
    }


    @Override
    public void onAuthenticationSuccess() {
        oldPassword.setError(null);

        if (newPasswordMatches(newpass, confpass)) {
            newPassword.setError(null);
            confirmPassword.setError(null);
            authManager.changePassword(newpass);
        } else displayError();
    }

    @Override
    public void onAuthenticationFailure() {
        if (newPasswordMatches(newpass, confpass)) {
            newPassword.setError(null);
            confirmPassword.setError(null);
        }
        displayError();
    }

    // ========================================
    //          METODOS PRIVADOS
    // ========================================
    private void displayError() {
        if (oldpass.isEmpty() || newpass.isEmpty() || confpass.isEmpty()) {
            if (oldpass.isEmpty())
                ToastMessage.displayToast(getContext(), "Introduce tu contraseña actual");
            if (newpass.isEmpty())
                ToastMessage.displayToast(getContext(), "Introduce nueva contraseña");
            if (confpass.isEmpty())
                ToastMessage.displayToast(getContext(), "Confirma tu nueva contraseña");

        } else if (!newPasswordMatches(newpass, confpass)) {
            confirmPassword.setError("Las contaseñas no coincidan");

        } else {
            ToastMessage.displayToast(getContext(), "La contraseña no está bien.\n " +
                    "Vuelve a intentar.");
            oldPassword.setError("Constraseña inválida");
            oldPassword.requestFocus();
        }
    }

    private boolean newPasswordMatches(String newpass, String confpass) {
        return newpass.equals(confpass);
    }

    // ========================================
    //              GETTERS
    // ========================================
    public TextInputLayout getNewPassword() {
        return newPassword;
    }
}