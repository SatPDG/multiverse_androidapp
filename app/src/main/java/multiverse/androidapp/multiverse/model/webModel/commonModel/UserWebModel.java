package multiverse.androidapp.multiverse.model.webModel.commonModel;

import multiverse.androidapp.multiverse.model.commonModel.UserModel;

public class UserWebModel {

    public int userID;
    public String firstname;
    public String lastname;
    public LocationWebModel lastLocation;

    public UserModel toCommonModel() {
        UserModel model = new UserModel();
        model.userID = userID;
        model.firstname = firstname;
        model.lastname = lastname;
        model.lastLocation = lastLocation.toCommonModel();
        return model;
    }
}
