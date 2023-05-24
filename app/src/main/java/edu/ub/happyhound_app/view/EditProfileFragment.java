package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.model.FirebaseStorageManager;
import edu.ub.happyhound_app.model.SaveUserInfo;
import edu.ub.happyhound_app.model.SearchDatabase;
import edu.ub.happyhound_app.viewModels.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuthManager<EditProfileFragment> authManager;
    private EditText username, phoneNumber, address, city, zipCode;
    private Button modifyData, saveData;
    private ConstraintLayout layout;
    private String nombre, numero, direccion, ciudad, codigoPostal;
    private CircleImageView profilePic;
    private ImageView editIcon, returnB;
    private FirebaseStorageManager storageManager;
    private ProfileViewModel profileViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        authManager = new FirebaseAuthManager<>(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.editProfileConstraint);
        username = view.findViewById(R.id.editNameProfile);
        phoneNumber = view.findViewById(R.id.editNumberProfile);
        address = view.findViewById(R.id.editAddressProfile);
        city = view.findViewById(R.id.editCityProfile);
        zipCode = view.findViewById(R.id.editCPProfile);
        modifyData = view.findViewById(R.id.modifyData);
        saveData = view.findViewById(R.id.saveData);
        profilePic = view.findViewById(R.id.profile_image);
        editIcon = view.findViewById(R.id.edit_icon);
        returnB = view.findViewById(R.id.return_back);

        layout.setBackgroundColor(DynamicLayout.setDynamicLayout(requireActivity()));

        getUserData();
        changeState(false);

        storageManager = new FirebaseStorageManager(requireActivity(), profilePic);
        storageManager.displayImage("Profile Images/profile_Image.jpg");

        modifyData.setOnClickListener(view1 -> changeState(true));

        saveData.setOnClickListener(view1 -> {
            nombre = username.getText().toString().trim();
            numero = phoneNumber.getText().toString().trim();
            direccion = address.getText().toString().trim();
            ciudad = city.getText().toString().trim();
            codigoPostal = zipCode.getText().toString().trim();

            SaveUserInfo userInfo = new SaveUserInfo(getActivity());
            userInfo.saveUserInfo(nombre, numero, direccion, ciudad, codigoPostal);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();

            //profileViewModel.changeUsername(nombre);
        });

        editIcon.setOnClickListener(view12 -> storageManager.selectImage());
        returnB.setOnClickListener(view12 -> requireActivity().onBackPressed());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserData() {
        String uid = authManager.getUser().getUid();

        SearchDatabase database = new SearchDatabase();
        database.searchUserData("Users/" + uid, "Name", username);
        database.searchUserData("Users/" + uid, "Phone Number", phoneNumber);
        database.searchUserData("Users/" + uid, "Address", address);
        database.searchUserData("Users/" + uid, "City", city);
        database.searchUserData("Users/" + uid, "Postal Code", zipCode);
    }

    private void changeState(boolean state) {
        username.setEnabled(state);
        phoneNumber.setEnabled(state);
        address.setEnabled(state);
        city.setEnabled(state);
        zipCode.setEnabled(state);
    }

}


