package multiverse.androidapp.multiverse.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.UserLocalDbService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.UserWebService;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.UserWebModel;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserOwnResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserSearchRequestWebModel;
import multiverse.androidapp.multiverse.repository.callback.UserCallback;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class UserRepository {

    private final Executor executor;
    private final MultiverseDbHelper dbHelper;

    public UserRepository(Executor executor, MultiverseDbHelper dbHelper) {
        this.executor = executor;
        this.dbHelper = dbHelper;
    }

    public void getUserInfo(final int userID, final UserCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Check if we have some basic information in the local db
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                UserDbModel dbUser = UserLocalDbService.getUser(db, userID);
                if(dbUser != null) {
                    UserInfoRespositoryModel model = new UserInfoRespositoryModel();
                    model.firstname = dbUser.firstname;
                    model.lastname = dbUser.lastname;
                    model.userID = userID;
                    model.nbrOfFollower = -1;
                    model.nbrOfFollowed = -1;
                    model.isAFollower = false;
                    model.isFollowed = false;
                    model.isFollowerRequestReceived = false;
                    model.isFollowedRequestSend = false;
                    callback.userInfoCallback(model);
                }

                // Call the web api to get some updated informations
                WebServiceResponse<UserResponseWebModel> webResponse = UserWebService.getUserInfo(userID, context);
                if(webResponse.isResponseOK) {
                    UserInfoRespositoryModel model = new UserInfoRespositoryModel();
                    model.firstname = webResponse.data.firstname;
                    model.lastname = webResponse.data.lastname;
                    model.userID = webResponse.data.userID;
                    model.nbrOfFollower = webResponse.data.nbrOfFollower;
                    model.nbrOfFollowed = webResponse.data.nbrOfFollowed;
                    model.isAFollower = webResponse.data.isAFollower;
                    model.isFollowed = webResponse.data.isFollowed;
                    model.isFollowerRequestReceived = webResponse.data.isFollowerRequestPending;
                    model.isFollowedRequestSend = webResponse.data.isFollowedRequestPending;
                    callback.userInfoCallback(model);

                    // Update the local db
                    UserDbModel user = new UserDbModel();
                    user.userID = webResponse.data.userID;
                    user.firstname = webResponse.data.firstname;
                    user.lastname = webResponse.data.lastname;
                    user.lastLocation = "";
                    user.lastDataUpdate = Calendar.getInstance().getTime().getTime();
                    UserLocalDbService.addOrUpdateUser(db, user);
                } else {
                    WebError webError = new WebError(webResponse);
                    callback.userErrorCallback(webError);
                }
                db.close();
            }
        });
    }

//    public void getSimpleUserInfo(final int userID, final UserCallback callback, final Context context) {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                // Go fetch the user from the local db
//                SQLiteDatabase db = dbHelper.getReadableDatabase();
//                UserDbModel dbUser = UserLocalDbService.getUser(db, userID);
//                UserModel user = dbUser.toModel();
//
//                callback.userSimpleInfoCallback(user);
//            }
//        });
//    }

    public void getUserOwnInfo(final UserCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int userID = SharedPreference.getUserID(context);

                // Check if we have some basic information in the local database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                UserDbModel dbUser = UserLocalDbService.getUser(db, userID);
                if (dbUser != null) {
                    UserOwnInfoRepositoryModel model = new UserOwnInfoRepositoryModel();
                    model.firstname = dbUser.firstname;
                    model.lastname = dbUser.lastname;
                    model.userID = userID;
                    model.nbrOfFollower = -1;
                    model.nbrOfFollowed = -1;
                    model.nbrOfRequestFollower = -1;
                    model.nbrOfRequestFollowed = -1;
                    model.nbrOfConversation = -1;
                    callback.userOwnInfoCallback(model);
                }

                // Call the web api for more information
                WebServiceResponse<UserOwnResponseWebModel> webResponse = UserWebService.getUserOwnInfo(context);
                if (webResponse.isResponseOK) {
                    UserOwnInfoRepositoryModel model = new UserOwnInfoRepositoryModel();
                    model.firstname = webResponse.data.firstname;
                    model.lastname = webResponse.data.lastname;
                    model.userID = webResponse.data.userID;
                    model.nbrOfFollower = webResponse.data.nbrOfFollower;
                    model.nbrOfFollowed = webResponse.data.nbrOfFollowed;
                    model.nbrOfRequestFollower = webResponse.data.nbrOfRequestFollower;
                    model.nbrOfRequestFollowed = webResponse.data.nbrOfRequestFollowed;
                    model.nbrOfConversation = webResponse.data.nbrOfConversation;
                    callback.userOwnInfoCallback(model);

                    // Update the local db
                    UserDbModel user = new UserDbModel();
                    user.userID = webResponse.data.userID;
                    user.firstname = webResponse.data.firstname;
                    user.lastname = webResponse.data.lastname;
                    user.lastLocation = "";
                    user.lastDataUpdate = Calendar.getInstance().getTime().getTime();
                    UserLocalDbService.addOrUpdateUser(db, user);
                } else {
                    WebError webError = new WebError(webResponse);
                    callback.userErrorCallback(webError);
                }
                db.close();
            }
        });
    }

    public void getUserList(final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Go fetch some users from the db if we have enough users
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                int size = UserLocalDbService.getSize(db);
                if(size >= 10) {
                    List<UserModel> userList = new ArrayList<>();
                    List<UserDbModel> usersDbList = UserLocalDbService.getMostRecentUser(db, 10);
                    for(UserDbModel user : usersDbList) {
                        userList.add(user.toCommonModel());
                    }
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_LOCATION_SEARCH, userList, 10, 0, 10);
                } else {
                    // Fetch users from the api
                    WebServiceResponse<UserListResponseWebModel> webResponse = UserWebService.getUserList(context);
                    if(webResponse.isResponseOK) {
                        List<UserModel> modelList = new ArrayList<>();
                        for (UserWebModel webModel : webResponse.data.users) {
                            modelList.add(webModel.toCommonModel());
                        }
                        callback.userListCallback(UserListCallback.UserCallbackType.USER_LOCATION_SEARCH, modelList, 10, 0, 10);
                    } else {
                        callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_LOCATION_SEARCH, new WebError(webResponse));
                    }
                }
                db.close();
            }
        });
    }

    public void searchForUserByName(final String name, final int count, final int offset, final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Call the web for the search
                UserSearchRequestWebModel request = new UserSearchRequestWebModel();
                request.locationSearch = null;
                request.nameSearch = name;
                request.count = count;
                request.offset = offset;
                WebServiceResponse<UserListResponseWebModel> webResponse = UserWebService.searchForUsers(request, context);

                if(webResponse.isResponseOK) {
                    List<UserModel> modelList = new ArrayList<>();
                    for (UserWebModel webModel : webResponse.data.users) {
                        modelList.add(webModel.toCommonModel());
                    }
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_NAME_SEARCH, modelList, webResponse.data.count, webResponse.data.offset, webResponse.data.totalSize);
                } else {
                    callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_NAME_SEARCH, new WebError(webResponse));
                }
            }
        });
    }

    public void searchForUserByLocation(final int count, final int offset, final UserListCallback callback, final Context context) {
        // TODO - to implement

    }
}
