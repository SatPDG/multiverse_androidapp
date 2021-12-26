package multiverse.androidapp.multiverse.ui.users;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.repository.callback.UserCallback;
import multiverse.androidapp.multiverse.repository.UserRepository;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.util.functions.ListUtil;

public class UsersViewModel extends AndroidViewModel implements UserListCallback {

    private final static int PAGE_SIZE = 10;

    private UserRepository userRepository;

    private MutableLiveData<List<UserModel>> users;
    private MutableLiveData<Integer> loadedSize;
    private MutableLiveData<Integer> totalSize;
    private MutableLiveData<UserListCallback.UserCallbackType> lastError;

    private WebError lastWebError;

    private boolean isInNameSearch;
    private String currentNameSearch;
    private Set<Integer> loadingPages;

    public UsersViewModel(@NonNull Application application) {
        super(application);

        this.userRepository = new UserRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);

        List<UserModel> list = new ArrayList<>();
        users = new MutableLiveData<>(list);
        loadedSize = new MutableLiveData<>(0);
        totalSize = new MutableLiveData<>(0);
        lastError = new MutableLiveData<>(null);

        isInNameSearch = false;
        currentNameSearch = "";
        loadingPages = new HashSet<>();
    }

    public void getRecentUserList() {
        if(!loadingPages.contains(0)){
            loadingPages.add(0);
            userRepository.getUserList(this, getApplication().getApplicationContext());
        }
    }

    public void searchForUserByName(String name) {
        if(name.isEmpty()) {
            currentNameSearch = "";

            // Fluch all the user in the list
            users.getValue().clear();
            loadedSize.setValue(0);
            totalSize.setValue(0);
        } else {
            currentNameSearch = name;
            isInNameSearch = true;

            loadingPages.add(0);
            userRepository.searchForUserByName(currentNameSearch, PAGE_SIZE, 0, this, getApplication().getApplicationContext());
        }
    }

    public void fetchMoreNameSearchResult(int pageNbr) {
        if(!loadingPages.contains(pageNbr)) {
            int offset = pageNbr * PAGE_SIZE;
            loadingPages.add(pageNbr);

            userRepository.searchForUserByName(currentNameSearch, PAGE_SIZE, offset, this, getApplication().getApplicationContext());
        }
    }

    public int getPageNumber(int offset) {
        return offset / PAGE_SIZE;
    }

    @Override
    public void userListCallback(UserCallbackType type, List<UserModel> users, int count, int offset, int totalSize) {
        if(type == UserCallbackType.USER_LOCATION_SEARCH) {
            if(!isInNameSearch) {
                loadingPages.remove(0);

                this.users.postValue(users);
                this.loadedSize.postValue(users.size());
                this.totalSize.postValue(totalSize);
            }
        } else if(type == UserCallbackType.USER_NAME_SEARCH) {
            if(isInNameSearch) {
                int pageNumber = offset / PAGE_SIZE;
                loadingPages.remove(pageNumber);

                List<UserModel> userList;
                // If it is the first name search result, flush the location result
                if(offset == 0) {
                    userList = new ArrayList<>();
                } else {
                    userList = this.users.getValue();
                }

                userList = ListUtil.mergeList(userList, users, offset);
                this.users.postValue(userList);

                if(totalSize == -1) {
                    if(users.size() < count) {
                        this.totalSize.postValue(userList.size());
                    } else {
                        this.totalSize.postValue(Integer.MAX_VALUE);
                    }
                } else {
                    this.totalSize.postValue(totalSize);
                }
                this.loadedSize.postValue(userList.size());
            }
        }
    }

    @Override
    public void webErrorCallback(UserCallbackType callbackType, WebError webError) {
        lastWebError = webError;
        lastError.postValue(callbackType);
    }

    public LiveData<List<UserModel>> getUsers() {
        return users;
    }

    public LiveData<Integer> getLoadedSize() {
        return loadedSize;
    }

    public LiveData<Integer> getTotalSize() {
        return totalSize;
    }

    public LiveData<UserListCallback.UserCallbackType> getLastError() {
        return lastError;
    }

    public WebError getLastWebError() {
        return lastWebError;
    }
}
