package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;

public interface UserCallback extends WebErrorCallback<Void> {

    void userInfoCallback(UserInfoRespositoryModel userInfo);

    void userOwnInfoCallback(UserOwnInfoRepositoryModel userInfo);

}
