package edu.ub.happyhound_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseStorage storage;
    private FirebaseAuthManager<HomeFragment> authManager;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private UserCardAdapter adapter;
    private StorageReference storageRef;
    private FloatingActionButton addDogs, addNotification;
    private FloatingActionsMenu menu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_dogList);
        progressBar = view.findViewById(R.id.progressBar_dogList);
        menu = view.findViewById(R.id.floatingMenu);
        addDogs = view.findViewById(R.id.floatingAddPets);
        addNotification = view.findViewById(R.id.floatingAddNotification);

        progressBar.setVisibility(View.VISIBLE);
        addDogs.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), agregarPerro.class);
            startActivity(intent);
            menu.collapse();
        });

        addNotification.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), CrearRecordatorio.class);
            startActivity(intent);
            menu.collapse();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authManager.getUser() == null) {
            Intent intent = new Intent(getActivity(), LogIn.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            storageRef = storage.getReference().child(Objects.requireNonNull(authManager.getUser().getEmail()));
            load_list();
        }
    }

    private void load_list() {
        List<Card_dog> fieldsList = new ArrayList<>();
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Obtener la lista de referencias de elementos en la carpeta
                List<StorageReference> elementsRefs = listResult.getItems();

                // Recorrer la lista de referencias de elementos y hacer algo con cada una
                for (StorageReference elementRef : elementsRefs) {
                    // Hacer algo con cada referencia de elemento, por ejemplo:
                    elementRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            Card_dog i = new Card_dog(elementRef.getName().substring(0, elementRef.getName().lastIndexOf(".")), imageUrl);
                            fieldsList.add(i);
                            Log.d("Name", i.getDog_name());
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Manejar el error en caso de que no se pueda descargar la imagen
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Manejar el error en caso de que no se pueda descargar la imagen
            }
        });


        adapter = new UserCardAdapter(fieldsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}