package multiverse.androidapp.multiverse.repository;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.RelationshipWebService;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;
import multiverse.androidapp.multiverse.repository.callback.RelationshipCallback;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.repository.event.RelationshipEvent;

public class RelationshipRepository {

    private final Executor executor;
    private final MultiverseDbHelper dbHelper;

    public RelationshipRepository(Executor executor, MultiverseDbHelper dbHelper) {
        this.executor = executor;
        this.dbHelper = dbHelper;
    }

    public void getFollowerList(final int count, final int offset, final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListRequestWebModel request = new ListRequestWebModel();
                request.count = count;
                request.offset = offset;
                WebServiceResponse<UserListResponseWebModel> webService = RelationshipWebService.getFollowerList(request, context);

                if(webService.isResponseOK) {
                    List<UserModel> list = new ArrayList<>();
                    list.addAll(webService.data.users);
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_FOLLOWER, list, webService.data.count, webService.data.offset, webService.data.totalSize);
                } else {
                    callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_FOLLOWER, new WebError(webService));
                }
            }
        });
    }

    public void getFollowedList(final int count, final int offset, final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListRequestWebModel request = new ListRequestWebModel();
                request.count = count;
                request.offset = offset;
                WebServiceResponse<UserListResponseWebModel> webService = RelationshipWebService.getFollowedList(request, context);

                if(webService.isResponseOK) {
                    List<UserModel> list = new ArrayList<>();
                    list.addAll(webService.data.users);
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_FOLLOWED, list, webService.data.count, webService.data.offset, webService.data.totalSize);
                } else {
                    callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_FOLLOWED, new WebError(webService));
                }
            }
        });
    }

    public void getFollowerRequestList(final int count, final int offset, final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListRequestWebModel request = new ListRequestWebModel();
                request.count = count;
                request.offset = offset;
                WebServiceResponse<UserListResponseWebModel> webService = RelationshipWebService.getFollowerRequestList(request, context);

                if(webService.isResponseOK) {
                    List<UserModel> list = new ArrayList<>();
                    list.addAll(webService.data.users);
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_FOLLOWER_REQ, list, webService.data.count, webService.data.offset, webService.data.totalSize);
                } else {
                    callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_FOLLOWER_REQ, new WebError(webService));
                }
            }
        });
    }

    public void getFollowedRequestList(final int count, final int offset, final UserListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListRequestWebModel request = new ListRequestWebModel();
                request.count = count;
                request.offset = offset;
                WebServiceResponse<UserListResponseWebModel> webService = RelationshipWebService.getFollowedRequestList(request, context);

                if(webService.isResponseOK) {
                    List<UserModel> list = new ArrayList<>();
                    list.addAll(webService.data.users);
                    callback.userListCallback(UserListCallback.UserCallbackType.USER_FOLLOWED_REQ, list, webService.data.count, webService.data.offset, webService.data.totalSize);
                } else {
                    callback.userListErrorCallback(UserListCallback.UserCallbackType.USER_FOLLOWED_REQ, new WebError(webService));
                }
            }
        });
    }

    public void sendFollowerRequest(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.sendFollowerRequest(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.SEND_FOLLOWED_REQ, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.SEND_FOLLOWED_REQ, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.SEND_FOLLOWED_REQ, new WebError(webService));
                }
            }
        });
    }

    public void acceptFollowedRequest(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.acceptFollowedRequest(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.ACCEPT_FOLLOWER_REQ, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.ACCEPT_FOLLOWER_REQ, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.ACCEPT_FOLLOWER_REQ, new WebError(webService));
                }
            }
        });
    }

    public void deleteFollowerUser(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.deleteFollowerUser(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER, new WebError(webService));
                }
            }
        });
    }

    public void deleteFollowedUser(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.deleteFollowedUser(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED, new WebError(webService));
                }
            }
        });
    }

    public void deleteFollowerRequestUser(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.deleteFollowerRequest(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER_REQ, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER_REQ, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWER_REQ, new WebError(webService));
                }
            }
        });
    }

    public void deleteFollowedRequestUser(final int userID, final RelationshipCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webService = RelationshipWebService.deleteFollowedRequest(userID, context);
                if(webService.isResponseOK) {
                    callback.relationshipActionCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED_REQ, userID);

                    // Send event to bus
                    EventBus.getDefault().post(new RelationshipEvent(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED_REQ, userID));
                } else {
                    callback.relationshipErrorCallback(RelationshipCallback.RelationCallbackType.DELETE_FOLLOWED_REQ, new WebError(webService));
                }
            }
        });
    }
}
