package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.HttpService;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.LoginRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.LoginResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RefreshRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RefreshResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RegisterRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RegisterResponseWebModel;

public class AuthenticationWebService {

    private AuthenticationWebService() {

    }

    public static ApiResponse<LoginResponseWebModel> login(LoginRequestWebModel request, Context context) {
        return HttpService.post(context.getString(R.string.network_auth_login), request, LoginResponseWebModel.class, context);
    }

    public static ApiResponse<RegisterResponseWebModel> register(RegisterRequestWebModel request, Context context) {
        return HttpService.post(context.getString(R.string.network_auth_register), request, RegisterResponseWebModel.class, context);
    }

    public static ApiResponse<RefreshResponseWebModel> refresh(RefreshRequestWebModel request, Context context) {
        return HttpService.post(context.getString(R.string.network_auth_refresh), request, RefreshResponseWebModel.class, context);
    }

    public static ApiResponse<UserModel> test(Context context) {
        return HttpService.get("/api/authentication/test", UserModel.class, context);
    }
}
