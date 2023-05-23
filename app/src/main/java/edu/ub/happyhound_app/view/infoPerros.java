package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import edu.ub.happyhound_app.model.SavePetInfo;
import edu.ub.happyhound_app.model.SearchDatabase;
import edu.ub.happyhound_app.viewModels.ProfileViewModel;


public class infoPerros extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuthManager<infoPerros> authManager;
    private EditText nombrePerro, edadPerro, sexoPerro, razaPerro;
    private Button  saveData;

    private TextView editDatos;
    private ConstraintLayout layout;
    private String nombre, edad, sexo, raza;
    private CircleImageView profilePic;
    private ImageView editIcon;
    private FirebaseStorageManager storageManager;
    private ProfileViewModel profileViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public infoPerros() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infoPerros.
     */
    // TODO: Rename and change types and number of parameters
    public static infoPerros newInstance(String param1, String param2) {
        infoPerros fragment = new infoPerros();
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
        return inflater.inflate(R.layout.fragment_info_perro, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.editProfileConstraint);
        nombrePerro = view.findViewById(R.id.editDogName);
        edadPerro = view.findViewById(R.id.editAge);
        sexoPerro = view.findViewById(R.id.editSexo);
        razaPerro = view.findViewById(R.id.editRaza);
        editDatos = view.findViewById(R.id.editarDatos);
        saveData = view.findViewById(R.id.guardarDatos);
        profilePic = view.findViewById(R.id.profile_image);
        editIcon = view.findViewById(R.id.edit_icon);

        layout.setBackgroundColor(DynamicLayout.setDynamicLayout(requireActivity()));

        getUserData();
        changeState(false);

        storageManager = new FirebaseStorageManager(requireActivity(), profilePic);
        //malik cambialoooooooo, a la imagen de los perretes ^^
        storageManager.displayImage("/Profile Images/profile_Image.jpg");

        editDatos.setOnClickListener(view1 -> changeState(true));

        saveData.setOnClickListener(view1 -> {
            nombre = nombrePerro.getText().toString().trim();
            edad = edadPerro.getText().toString().trim();
            sexo = sexoPerro.getText().toString().trim();
            raza = razaPerro.getText().toString().trim();


            SavePetInfo petInfo = new SavePetInfo(getActivity());
            petInfo.saveDogs(nombre, raza, edad, sexo, null, null);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();

            //profileViewModel.changeUsername(nombre);
        });

        editIcon.setOnClickListener(view12 -> storageManager.selectImage());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserData() {
        String uid = authManager.getUser().getUid();

        SearchDatabase database = new SearchDatabase();
        database.searchUserData("Users/" + uid + "/Lista Perros/", "name", nombrePerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/", "edad", edadPerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/", "sexo", sexoPerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/", "raza", razaPerro);
    }

    private void changeState(boolean state) {
        nombrePerro.setEnabled(state);
        edadPerro.setEnabled(state);
        sexoPerro.setEnabled(state);
        razaPerro.setEnabled(state);
    }

}
