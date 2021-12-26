package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;

public interface AuthenticationCallback {

    void loginCallback(AuthenticationResponseRepositoryModel response);

    void registerCallback(AuthenticationResponseRepositoryModel response);
}
