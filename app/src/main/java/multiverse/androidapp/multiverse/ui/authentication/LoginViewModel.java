package multiverse.androidapp.multiverse.ui.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;
import multiverse.androidapp.multiverse.repository.callback.AuthenticationCallback;
import multiverse.androidapp.multiverse.repository.AuthenticationRepository;

public class LoginViewModel extends AndroidViewModel implements AuthenticationCallback {

    private final AuthenticationRepository authenticationRepo;

    private MutableLiveData<AuthenticationResponseRepositoryModel.AuthenticationCode> authenticationCode;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        this.authenticationRepo = new AuthenticationRepository(((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).executorService);
        authenticationCode = new MutableLiveData<>(null);
    }

    public void login(String email, String password) {
        authenticationCode.setValue(null);

        authenticationRepo.login(email, password, this, getApplication().getApplicationContext());
    }

    public LiveData<AuthenticationResponseRepositoryModel.AuthenticationCode> getAuthenticationCode() {
        return authenticationCode;
    }

    @Override
    public void loginCallback(AuthenticationResponseRepositoryModel response) {
        authenticationCode.postValue(response.code);
    }

    @Override
    public void registerCallback(AuthenticationResponseRepositoryModel response) {
        // Do nothing
    }
}
