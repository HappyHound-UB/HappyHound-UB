package edu.ub.happyhound_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SavePetInfo {
    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    public SavePetInfo(FirebaseAuth firebaseAuth) {
        this.mAuth = firebaseAuth;
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
    }

    protected void saveDogs(String nombre, String raza, String edad, String sexo, Context c, Bitmap b) {
        DocumentReference documentReference;
        String userID = mAuth.getCurrentUser().getUid();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(userID)
                .collection("Lista Perros").document(nombre);

        Map<String, Object> perros = new HashMap<String, Object>();
        perros.put("name", nombre);
        perros.put("raza", raza);
        perros.put("edad", edad);
        perros.put("sexo", sexo);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = mStorageRef.child( mAuth.getCurrentUser().getEmail()+"/"+nombre+".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(c , "Fallo al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(c , "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
            }
        });

        documentReference.set(perros)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}
