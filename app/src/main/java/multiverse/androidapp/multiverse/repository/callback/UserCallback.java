package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;

public interface UserCallback {

    void userInfoCallback(UserInfoRespositoryModel userInfo);

    void userOwnInfoCallback(UserOwnInfoRepositoryModel userInfo);

    void userErrorCallback(WebError webError);

}
