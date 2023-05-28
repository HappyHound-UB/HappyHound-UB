package edu.ub.happyhound_app.view;

import android.app.AlertDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicLayout;
import edu.ub.happyhound_app.model.FirebaseAuthManager;
import edu.ub.happyhound_app.model.FirebaseListener;
import edu.ub.happyhound_app.model.FirebaseStorageManager;
import edu.ub.happyhound_app.model.SavePetInfo;
import edu.ub.happyhound_app.model.SearchDatabase;
import edu.ub.happyhound_app.model.ToastMessage;


public class infoPerros extends Fragment implements FirebaseListener {

    private static final String ARG_NAME = "name";
    private FirebaseAuthManager<infoPerros> authManager;
    private EditText nombrePerro, edadPerro, sexoPerro, razaPerro;
    private Button saveData;
    private TextView editDatos, eliminar;
    private ConstraintLayout layout;
    private String nombre, edad, sexo, raza, uid;
    private CircleImageView profilePic;
    private ImageView editIcon, flechaAtras;
    private FirebaseStorageManager storageManager;
    private String name;

    public infoPerros() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }

        authManager = new FirebaseAuthManager<>(getActivity(), this);
        uid = authManager.getUser().getUid();

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
        profilePic = view.findViewById(R.id.pet_image);
        editIcon = view.findViewById(R.id.edit_icon);
        flechaAtras = view.findViewById(R.id.return_back_infoPerro);
        eliminar = view.findViewById(R.id.textViewEliminar);

        layout.setBackgroundColor(DynamicLayout.setDynamicLayout(requireActivity()));

        getUserData();
        changeState(false);

        storageManager = new FirebaseStorageManager(requireActivity(), profilePic);
        storageManager.displayImage(name + ".jpg");

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

        editIcon.setOnClickListener(view13 -> storageManager.selectImage());
        flechaAtras.setOnClickListener(view13 -> requireActivity().onBackPressed());

        eliminar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Eliminar");
            builder.setMessage("¿Estás seguro de que quieres eliminar?");

            builder.setPositiveButton("Sí", (dialog, which) -> {
                storageManager.deletePet(this, "Users/" + uid + "/Lista Perros/", name);
                storageManager.deleteImage(this, authManager.getUser().getEmail() +
                        "/" + name + ".jpg");
            });

            builder.setNegativeButton("No", (dialog, which) ->
                    ToastMessage.displayToast(requireActivity(), "Eliminación cancelada"));

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageManager.onActivityResult(requestCode, resultCode, data, authManager.getUser().getEmail() +
                "/" + name + ".jpg");
    }

    private void getUserData() {

        SearchDatabase database = new SearchDatabase();
        database.searchUserData("Users/" + uid + "/Lista Perros/" + name, "name", nombrePerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/" + name, "edad", edadPerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/" + name, "sexo", sexoPerro);
        database.searchUserData("Users/" + uid + "/Lista Perros/" + name, "raza", razaPerro);
    }

    private void changeState(boolean state) {
        nombrePerro.setEnabled(false);
        edadPerro.setEnabled(state);
        sexoPerro.setEnabled(state);
        razaPerro.setEnabled(state);
    }

    @Override
    public void onSuccess() {
        ToastMessage.displayToast(requireActivity(), "Perro eliminado con éxito");
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onFailure() {
        ToastMessage.displayToast(requireActivity(), "No se ha podido eliminar");
    }
}
