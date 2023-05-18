package edu.ub.happyhound_app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> username, email;

    public ProfileViewModel() {
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void changeUsername(String user) {
        username.setValue(user);
    }

}
