package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.ToastMessage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseAuthManager<ProfileFragment> authManager;
    private TextView username, email;
    private Button editProfile, changePassword, about, signOut;
    private ConstraintLayout layout;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        authManager = new FirebaseAuthManager<>(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.profileConstraint);
        username = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.emailID);
        editProfile = view.findViewById(R.id.button_editProfile);
        changePassword = view.findViewById(R.id.button_updatePassword);
        about = view.findViewById(R.id.button_Acerca);
        signOut = view.findViewById(R.id.button_log_out);

        layout.setBackgroundColor(DynamicLayout.setDynamicLayout(requireActivity()));
        username.setText(Objects.requireNonNull(authManager.getUser().getDisplayName()));
        email.setText(Objects.requireNonNull(authManager.getUser().getEmail()));

        editProfile.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.frameLayout, new EditProfileFragment())
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
//                .addToBackStack("name")
                    .commit();
        });

        changePassword.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.frameLayout, new ChangePasswordFragment())
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
//                .addToBackStack("name")
                    .commit();
        });

        signOut.setOnClickListener(view1 -> {
            authManager.signOut();
            ToastMessage.displayToast(getActivity(), "Cesión cerrado con éxito");
            Intent intent = new Intent(requireActivity().getApplicationContext(), LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}