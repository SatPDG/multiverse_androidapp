package multiverse.androidapp.multiverse.model.webModel.user;

import java.util.List;

import multiverse.androidapp.multiverse.model.webModel.commonModel.UserWebModel;

public class UserListResponseWebModel {

    public List<UserWebModel> users;
    public int count;
    public int offset;
    public int totalSize;

}
