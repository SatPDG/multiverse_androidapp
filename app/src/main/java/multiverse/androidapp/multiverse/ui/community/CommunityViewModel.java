package multiverse.androidapp.multiverse.ui.community;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.UserWebModel;
import multiverse.androidapp.multiverse.repository.RelationshipRepository;
import multiverse.androidapp.multiverse.repository.UserRepository;
import multiverse.androidapp.multiverse.repository.callback.RelationshipCallback;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.repository.event.RelationshipEvent;
import multiverse.androidapp.multiverse.util.functions.ListUtil;

public class CommunityViewModel extends AndroidViewModel implements UserListCallback, RelationshipCallback {

    private final static int PAGE_SIZE = 10;

    private UserRepository userRepository;
    private RelationshipRepository relationshipRepository;
    private CommunityType type;

    private List<UserModel> users;
    private MutableLiveData<Integer> totalSize;
    private MutableLiveData<Integer> loadedSize;
    private MutableLiveData<WebError> lastWebError;

    private Set<Integer> loadingPage;

    public CommunityViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);
        relationshipRepository = new RelationshipRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);

        users = new ArrayList<>();
        totalSize = new MutableLiveData<>(-1);
        loadedSize = new MutableLiveData<>(0);
        lastWebError = new MutableLiveData<>(null);

        loadingPage = new HashSet<>();

        EventBus.getDefault().register(this);
    }

    public void setType(CommunityType type) {
        this.type = type;
    }

    public void loadUser(int offset) {
        // Make sure the requested user is loaded. Otherwise load it.
        if (users == null || users.size() <= offset || users.get(offset) == null) {
            int page = offset / PAGE_SIZE;
            // Make sure the page is not loading
            if (!loadingPage.contains(page)) {
                loadingPage.add(page);
                if (type == CommunityType.FOLLOWER_LIST) {
                    relationshipRepository.getFollowerList(PAGE_SIZE, page, this, getApplication().getApplicationContext());
                } else if (type == CommunityType.FOLLOWED_LIST) {
                    relationshipRepository.getFollowedList(PAGE_SIZE, page, this, getApplication().getApplicationContext());
                } else if (type == CommunityType.FOLLOWER_REQUEST_TYPE) {
                    relationshipRepository.getFollowerRequestList(PAGE_SIZE, page, this, getApplication().getApplicationContext());
                } else if (type == CommunityType.FOLLOWED_REQUEST_TYPE) {
                    relationshipRepository.getFollowedRequestList(PAGE_SIZE, page, this, getApplication().getApplicationContext());
                }
            }
        }
    }

    public void acceptUser(int userID) {
        if (type == CommunityType.FOLLOWER_REQUEST_TYPE) {
            // Accept the request
            relationshipRepository.acceptFollowedRequest(userID, this, getApplication().getApplicationContext());
        }
    }

    public void refuseUser(int userID) {
        if (type == CommunityType.FOLLOWER_LIST) {
            relationshipRepository.deleteFollowerUser(userID, this, getApplication().getApplicationContext());
        } else if (type == CommunityType.FOLLOWED_LIST) {
            relationshipRepository.deleteFollowedUser(userID, this, getApplication().getApplicationContext());
        } else if (type == CommunityType.FOLLOWER_REQUEST_TYPE) {
            relationshipRepository.deleteFollowerRequestUser(userID, this, getApplication().getApplicationContext());
        } else if (type == CommunityType.FOLLOWED_REQUEST_TYPE) {
            relationshipRepository.deleteFollowedRequestUser(userID, this, getApplication().getApplicationContext());
        }
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public LiveData<Integer> getTotalSize() {
        return totalSize;
    }

    public LiveData<Integer> getLoadedSize() {
        return loadedSize;
    }

    public LiveData<WebError> getLastWebError() {
        return lastWebError;
    }

    @Override
    public void userListCallback(UserCallbackType type, List<UserModel> users, int count, int offset, int totalSize) {
        // Make sure the callback fit with the view model state
        if ((type == UserCallbackType.USER_FOLLOWER && this.type == CommunityType.FOLLOWER_LIST) ||
                (type == UserCallbackType.USER_FOLLOWED && this.type == CommunityType.FOLLOWED_LIST) ||
                (type == UserCallbackType.USER_FOLLOWER_REQ && this.type == CommunityType.FOLLOWER_REQUEST_TYPE) ||
                (type == UserCallbackType.USER_FOLLOWED_REQ && this.type == CommunityType.FOLLOWED_REQUEST_TYPE)) {
            int pageNumber = offset / PAGE_SIZE;
            loadingPage.remove(pageNumber);

            // Add the result to the list
            ListUtil.mergeList(this.users, users, offset);

            if (totalSize == -1) {
                if (users.size() < count) {
                    this.totalSize.postValue(this.users.size());
                } else {
                    this.totalSize.postValue(Integer.MAX_VALUE);
                }
            } else {
                this.totalSize.postValue(totalSize);
            }
            this.loadedSize.postValue(this.users.size());
        }
    }

    @Override
    public void userListErrorCallback(UserCallbackType type, WebError webError) {
        if (webError != null) {
            lastWebError.postValue(webError);
        }
    }

    @Override
    public void relationshipActionCallback(RelationCallbackType type, int userID) {
        if (type == RelationCallbackType.ACCEPT_FOLLOWER_REQ ||
                type == RelationCallbackType.DELETE_FOLLOWER ||
                type == RelationCallbackType.DELETE_FOLLOWED ||
                type == RelationCallbackType.DELETE_FOLLOWER_REQ ||
                type == RelationCallbackType.DELETE_FOLLOWED_REQ) {
            // Remove the user from the list
            int i;
            for (i = 0; i < users.size(); i++) {
                if (users.get(i).userID == userID) {
                    break;
                }
            }
            users.remove(i);
            totalSize.postValue(totalSize.getValue() - 1);
            loadedSize.postValue(loadedSize.getValue() - 1);
        }
    }

    @Override
    public void relationshipErrorCallback(RelationCallbackType type, WebError webError) {
        lastWebError.postValue(webError);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRelationshipEvent(RelationshipEvent event) {
        if (type == CommunityType.FOLLOWER_LIST) {
            if (event.type == RelationCallbackType.ACCEPT_FOLLOWER_REQ) {
                // Make the list reloads itself
                users.clear();
                totalSize.postValue(-1);
                loadedSize.postValue(0);
            }
        }
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }
}
