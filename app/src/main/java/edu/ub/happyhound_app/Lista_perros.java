package edu.ub.happyhound_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Lista_perros extends AppCompatActivity {
    UserCardAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private String User;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Card_dog> fieldsList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_perros);
        firebaseAuth = FirebaseAuth.getInstance();
        User = firebaseAuth.getCurrentUser().getEmail();


        RecyclerView recyclerView = findViewById(R.id.recyclerView_dogList);
        ProgressBar p = findViewById(R.id.progressBar_dogList);
        p.setVisibility(View.VISIBLE);

        StorageReference storageRef = storage.getReference().child(User);

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
                                Card_dog i = new Card_dog(elementRef.getName().substring(0,elementRef.getName().lastIndexOf(".")), imageUrl);
                                fieldsList.add(i);
                                Log.d("Name", i.getDog_name());
                                p.setVisibility(View.GONE);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void volver(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
