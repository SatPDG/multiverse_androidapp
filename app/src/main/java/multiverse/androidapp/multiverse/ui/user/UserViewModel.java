package multiverse.androidapp.multiverse.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.repository.RelationshipRepository;
import multiverse.androidapp.multiverse.repository.UserRepository;
import multiverse.androidapp.multiverse.repository.callback.RelationshipCallback;
import multiverse.androidapp.multiverse.repository.callback.UserCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.repository.event.RelationshipEvent;

public class UserViewModel extends AndroidViewModel implements UserCallback, RelationshipCallback {

    private UserRepository userRepository;
    private RelationshipRepository relationshipRepository;

    private MutableLiveData<UserInfoRespositoryModel> userInfo;
    private MutableLiveData<UserOwnInfoRepositoryModel> userOwnInfo;
    private MutableLiveData<WebError> webError;

    public UserViewModel(@NonNull Application application) {
        super(application);

        this.userRepository = new UserRepository(((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).executorService,
                ((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).dbHelper);
        this.relationshipRepository = new RelationshipRepository(((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).executorService,
                ((MultiverseApplication) getApplication().getApplicationContext().getApplicationContext()).dbHelper);

        userInfo = new MutableLiveData<>(null);
        userOwnInfo = new MutableLiveData<>(null);
        webError = new MutableLiveData<>(null);

        // Register to bus
        EventBus.getDefault().register(this);
    }

    public void loadUserInfo(int userID) {
        userRepository.getUserInfo(userID, this, getApplication().getApplicationContext());
    }

    public void loadUserOwnInfo() {
        userRepository.getUserOwnInfo(this, getApplication().getApplicationContext());
    }

    public void sendFollowerRequest() {
        if (userInfo.getValue() != null)
            relationshipRepository.sendFollowerRequest(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public void deleteFollowerRequest() {
        if (userInfo.getValue() != null)
            relationshipRepository.deleteFollowerRequestUser(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public void acceptFollowedRequest() {
        if (userInfo.getValue() != null)
            relationshipRepository.acceptFollowedRequest(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public void deleteFollowedRequest() {
        if (userInfo.getValue() != null)
            relationshipRepository.deleteFollowedRequestUser(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public void removeFollower() {
        if (userInfo.getValue() != null)
            relationshipRepository.deleteFollowerUser(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public void removeFollowed() {
        if (userInfo.getValue() != null)
            relationshipRepository.deleteFollowedUser(userInfo.getValue().userID, this, getApplication().getApplicationContext());
    }

    public LiveData<UserInfoRespositoryModel> getUserInfo() {
        return userInfo;
    }

    public LiveData<UserOwnInfoRepositoryModel> getUserOwnInfo() {
        return userOwnInfo;
    }

    public LiveData<WebError> getWebError() {
        return webError;
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
    public void userErrorCallback(WebError webError) {
        this.webError.postValue(webError);
    }

    @Override
    public void relationshipActionCallback(RelationCallbackType type, int userID) {
        if (type != null && userInfo.getValue() != null) {
            UserInfoRespositoryModel userInfoLocal = userInfo.getValue();
            if (type == RelationCallbackType.SEND_FOLLOWED_REQ) {
                userInfoLocal.isFollowedRequestSend = true;
            } else if (type == RelationCallbackType.DELETE_FOLLOWER_REQ) {
                userInfoLocal.isFollowerRequestReceived = false;
            } else if (type == RelationCallbackType.DELETE_FOLLOWER) {
                userInfoLocal.isAFollower = false;
            } else if (type == RelationCallbackType.ACCEPT_FOLLOWER_REQ) {
                userInfoLocal.isFollowerRequestReceived = false;
                userInfoLocal.isAFollower = true;
            } else if (type == RelationCallbackType.DELETE_FOLLOWED_REQ) {
                userInfoLocal.isFollowedRequestSend = false;
            } else if (type == RelationCallbackType.DELETE_FOLLOWED) {
                userInfoLocal.isFollowed = false;
            }
            userInfo.postValue(userInfoLocal);
        }
    }

    @Override
    public void relationshipErrorCallback(RelationCallbackType type, WebError webError) {
        this.webError.postValue(webError);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRelationshipEvent(RelationshipEvent event) {
        if (event.type == RelationCallbackType.SEND_FOLLOWED_REQ) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfRequestFollowed++;
                userOwnInfo.postValue(user);
            }
        } else if (event.type == RelationCallbackType.ACCEPT_FOLLOWER_REQ) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfRequestFollower--;
                user.nbrOfFollower++;
                userOwnInfo.postValue(user);
            }
        } else if (event.type == RelationCallbackType.DELETE_FOLLOWER) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfFollower--;
                userOwnInfo.postValue(user);
            }
        } else if (event.type == RelationCallbackType.DELETE_FOLLOWED) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfFollowed--;
                userOwnInfo.postValue(user);
            }
        } else if (event.type == RelationCallbackType.DELETE_FOLLOWER_REQ) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfRequestFollower--;
                userOwnInfo.postValue(user);
            }
        } else if (event.type == RelationCallbackType.DELETE_FOLLOWED_REQ) {
            if (userOwnInfo.getValue() != null) {
                UserOwnInfoRepositoryModel user = userOwnInfo.getValue();
                user.nbrOfRequestFollowed--;
                userOwnInfo.postValue(user);
            }
        }
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }
}