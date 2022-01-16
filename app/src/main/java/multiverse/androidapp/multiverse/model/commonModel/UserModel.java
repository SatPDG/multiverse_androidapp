package multiverse.androidapp.multiverse.model.commonModel;


import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.UserWebModel;

public class UserModel {
    public int userID;
    public String firstname;
    public String lastname;
    public LocationModel lastLocation;

    public UserModel() {

    }

    public UserWebModel toWebModel() {
        UserWebModel webModel = new UserWebModel();
        webModel.userID = userID;
        webModel.firstname = firstname;
        webModel.lastname = lastname;
        webModel.lastLocation = lastLocation.toWebModel();
        return webModel;
    }

    public UserDbModel toDbModel() {
        UserDbModel dbModel = new UserDbModel();
        dbModel.userID = userID;
        dbModel.firstname = firstname;
        dbModel.lastname = lastname;
        dbModel.lastLocation = "";
        return dbModel;
    }
}
