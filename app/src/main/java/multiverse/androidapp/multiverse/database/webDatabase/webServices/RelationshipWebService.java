package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;

import java.util.function.Supplier;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.HttpService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.model.webModel.user.UserListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;

public class RelationshipWebService {

    private RelationshipWebService() {

    }

    public static WebServiceResponse<UserListResponseWebModel> getFollowerList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_relationship_follower), request, UserListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserListResponseWebModel> getFollowedList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_relationship_followed), request, UserListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> deleteFollowerUser(final int followedID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_relationship_deleteFollower), followedID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> deleteFollowedUser(final int followerID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_relationship_deleteFollowed), followerID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserListResponseWebModel> getFollowerRequestList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_relationship_followerRequest), request, UserListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserListResponseWebModel> getFollowedRequestList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_relationship_followedRequest), request, UserListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> acceptFollowedRequest(final int followedID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.post(String.format(context.getString(R.string.network_relationship_acceptFollowedRequest), followedID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> deleteFollowerRequest(final int followedID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_relationship_deleteFollowerRequest), followedID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> sendFollowerRequest(final int followedID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.post(String.format(context.getString(R.string.network_relationship_sendFollowerRequest), followedID), null, Void.class, context);}
        }, context);
    }

    public static WebServiceResponse<Void> deleteFollowedRequest(final int followerID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_relationship_deleteFollowedRequest), followerID), null, Void.class, context);
            }
        }, context);
    }
}
