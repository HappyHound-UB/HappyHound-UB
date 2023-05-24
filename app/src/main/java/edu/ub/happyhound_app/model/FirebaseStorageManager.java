package edu.ub.happyhound_app.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Clase para buscar y guardar imagenes en Firebase Storage
 */
public class FirebaseStorageManager {
    private static final int EDIT_PIC = 1000;
    private final FirebaseAuthManager<FirebaseStorageManager> authManager;
    private final Activity activity;
    private StorageReference storageReference;
    private CircleImageView profilePic;
    FirebaseFirestore db;


    public FirebaseStorageManager(Activity activity, CircleImageView profilePic) {
        this.activity = activity;
        this.profilePic = profilePic;
        storageReference = FirebaseStorage.getInstance().getReference();
        authManager = new FirebaseAuthManager<>();
        db = FirebaseFirestore.getInstance();
    }

    public void selectImage() {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(openGallery, EDIT_PIC);
    }

    public void displayImage(String path) {
        StorageReference profileRef = storageReference
                .child(authManager.getUser().getEmail() + "/" + path);
        profileRef.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(profilePic));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = Objects.requireNonNull(data).getData();
                profilePic.setImageURI(imageUri);
                uploadImageFirebase(imageUri, authManager.getUser().getEmail() + "/Profile Images/profile_Image.jpg");
            }
        }
    }

    protected void uploadImageFirebase(Uri imageUri, String path) {
        StorageReference fileReference = storageReference.child(path);

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    ToastMessage.displayToast(activity.getApplicationContext(), "Image uploaded");
                    fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                //Picasso.get().load(uri).into(profilePic))
                            })
                            .addOnFailureListener(e -> {

                            });
                })
                .addOnFailureListener(e -> ToastMessage.displayToast(activity.getApplicationContext(), "Image not uploaded"));
    }

    public void deletePet(FirebaseListener listener, String collectionPath, String petToDelete) {
        db.collection(collectionPath)
                .whereEqualTo("name", petToDelete)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot petDoc : querySnapshot) {
                        DocumentReference petRef = petDoc.getReference();

                        // Delete the Reminders subcollection
                        petRef.collection("Reminders")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        document.getReference().delete();
                                    }
                                    // After deleting the subcollection, delete the main pet document
                                    petRef.delete();
                                    listener.onSuccess();
                                })
                                .addOnFailureListener(e -> listener.onFailure());
                    }
                })
                .addOnFailureListener(e -> listener.onFailure());
    }

    public void deleteImage(FirebaseListener listener, String imagePath) {
        StorageReference imageRef = storageReference.child(imagePath);
        imageRef.delete()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure());
    }
}
