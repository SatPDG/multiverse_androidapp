package multiverse.androidapp.multiverse.ui.community;

import multiverse.androidapp.multiverse.model.commonModel.UserModel;

public interface CommunityClickListener {

    void userClickListener(UserModel user);

    void acceptUserClickListener(UserModel user);

    void refuseUserClickListener(UserModel user);
}
