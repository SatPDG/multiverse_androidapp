package multiverse.androidapp.multiverse.ui.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.NewUserRepositoryModel;
import multiverse.androidapp.multiverse.repository.callback.AuthenticationCallback;
import multiverse.androidapp.multiverse.repository.AuthenticationRepository;

public class RegisterViewModel extends AndroidViewModel implements AuthenticationCallback {

    private final AuthenticationRepository authenticationRepo;

    private MutableLiveData<AuthenticationResponseRepositoryModel.AuthenticationCode> authenticationCode;

    public RegisterViewModel(@NonNull Application application) {
        super(application);

        this.authenticationRepo = new AuthenticationRepository(((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).executorService);
        authenticationCode = new MutableLiveData<>(null);
    }

    public void register(String email, String password, String firstname, String lastname) {
        authenticationCode.setValue(null);

        NewUserRepositoryModel request = new NewUserRepositoryModel();
        request.email = email;
        request.password = password;
        request.firstname = firstname;
        request.lastname = lastname;

        authenticationRepo.register(request, this, getApplication().getApplicationContext() );
    }

    public LiveData<AuthenticationResponseRepositoryModel.AuthenticationCode> getAuthenticationCode() {
        return authenticationCode;
    }

    @Override
    public void loginCallback(AuthenticationResponseRepositoryModel response) {
        // Do nothing
    }

    @Override
    public void registerCallback(AuthenticationResponseRepositoryModel response) {
        authenticationCode.postValue(response.code);
    }
}
