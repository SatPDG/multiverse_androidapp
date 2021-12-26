package multiverse.androidapp.multiverse.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.repository.UserRepository;
import multiverse.androidapp.multiverse.repository.callback.UserCallback;
import multiverse.androidapp.multiverse.ui.util.UIWebErrorHandler;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class UserViewModel extends AndroidViewModel implements UserCallback {

    private UserRepository userRepository;

    private MutableLiveData<UserInfoRespositoryModel> userInfo;
    private MutableLiveData<UserOwnInfoRepositoryModel> userOwnInfo;

    public UserViewModel(@NonNull Application application) {
        super(application);

        this.userRepository = new UserRepository(((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).executorService,
                ((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).dbHelper);

        userInfo = new MutableLiveData<>(null);
        userOwnInfo = new MutableLiveData<>(null);
    }

    public void loadUserInfo(int userID) {
        userRepository.getUserInfo(userID, this, getApplication().getApplicationContext());
    }

    public void loadUserOwnInfo() {
        userRepository.getUserOwnInfo(this, getApplication().getApplicationContext());
    }

    public LiveData<UserInfoRespositoryModel> getUserInfo() {
        return userInfo;
    }

    public LiveData<UserOwnInfoRepositoryModel> getUserOwnInfo() {
        return userOwnInfo;
    }

    @Override
    public void userInfoCallback(UserInfoRespositoryModel userInfo) {
        this.userInfo.postValue(userInfo);
    }

    @Override
    public void userOwnInfoCallback(UserOwnInfoRepositoryModel userInfo) {
        this.userOwnInfo.postValue(userInfo);
    }

    @Override
    public void webErrorCallback(Void callbackType, WebError webError) {
        UIWebErrorHandler.handle(webError, getApplication().getApplicationContext());
    }
}