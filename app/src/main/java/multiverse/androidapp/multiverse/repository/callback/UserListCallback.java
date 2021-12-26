package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.UserModel;

public interface UserListCallback extends WebErrorCallback<UserListCallback.UserCallbackType> {

    void userListCallback(UserCallbackType type, List<UserModel> users, int count, int offset, int totalSize);

    enum UserCallbackType {
        USER_LOCATION_SEARCH, USER_NAME_SEARCH, USER_FOLLOWER, USER_FOLLOWED, USER_FOLLOWER_REQ, USER_FOLLOWED_REQ
    }
}
