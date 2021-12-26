package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;

import java.util.function.Supplier;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.HttpService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.model.webModel.user.UserListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserOwnResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.user.UserSearchRequestWebModel;

public class UserWebService {

    private UserWebService() {

    }

    public static WebServiceResponse<UserResponseWebModel> getUserInfo(final int userID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserResponseWebModel>>() {
            @Override
            public ApiResponse<UserResponseWebModel> get() {
                return HttpService.get(String.format(context.getString(R.string.network_user_info), userID), UserResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserOwnResponseWebModel> getUserOwnInfo(final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserOwnResponseWebModel>>() {
            @Override
            public ApiResponse<UserOwnResponseWebModel> get() {
                return HttpService.get(context.getString(R.string.network_user_ownInfo), UserOwnResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserListResponseWebModel> getUserList(final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.get(context.getString(R.string.network_user_users), UserListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<UserListResponseWebModel> searchForUsers(final UserSearchRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<UserListResponseWebModel>>() {
            @Override
            public ApiResponse<UserListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_user_search), request, UserListResponseWebModel.class, context);
            }
        }, context);
    }
}
