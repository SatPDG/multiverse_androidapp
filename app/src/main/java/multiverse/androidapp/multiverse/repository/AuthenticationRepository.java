package multiverse.androidapp.multiverse.repository;

import android.content.Context;

import java.util.concurrent.Executor;

import multiverse.androidapp.multiverse.database.webDatabase.webServices.ApiResponse;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.AuthenticationWebService;
import multiverse.androidapp.multiverse.model.commonModel.LocationModel;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.NewUserRepositoryModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.LoginRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.LoginResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RegisterRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RegisterResponseWebModel;
import multiverse.androidapp.multiverse.repository.callback.AuthenticationCallback;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class AuthenticationRepository {

    private final Executor executor;

    public AuthenticationRepository(Executor executor) {
        this.executor = executor;
    }

    public void login(final String email, final String password, final AuthenticationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                LoginRequestWebModel request = new LoginRequestWebModel();
                request.email = email;
                request.password = password;
                ApiResponse<LoginResponseWebModel> apiResponse = AuthenticationWebService.login(request, context);

                AuthenticationResponseRepositoryModel response = new AuthenticationResponseRepositoryModel();
                if (apiResponse != null && apiResponse.httpCode == 200) {
                    response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.OK;
                    response.userID = apiResponse.data.userID;
                    response.token = apiResponse.data.token;
                    response.refreshToken = apiResponse.data.refreshToken;

                    // Save the token and current user ID informations
                    SharedPreference.setAuthToken(context, apiResponse.data.token);
                    SharedPreference.setAuthRefreshToken(context, apiResponse.data.refreshToken);
                    SharedPreference.setUserID(context, apiResponse.data.userID);
                } else {
                    if (apiResponse != null && apiResponse.apiCode == 1) {
                        response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.BAD_CREDENTIAL;
                    }else{
                        response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.SERVER_ERROR;
                    }
                }
                callback.loginCallback(response);
            }
        });
    }

    public void register(final NewUserRepositoryModel newUser, final AuthenticationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RegisterRequestWebModel webRequest = new RegisterRequestWebModel();
                webRequest.email = newUser.email;
                webRequest.password = newUser.password;
                webRequest.firstname = newUser.firstname;
                webRequest.lastname = newUser.lastname;
                webRequest.lastLocation = new LocationModel(newUser.longitude, newUser.latitude);

                ApiResponse<RegisterResponseWebModel> webResponse = AuthenticationWebService.register(webRequest, context);

                AuthenticationResponseRepositoryModel response = new AuthenticationResponseRepositoryModel();
                if (webResponse != null && webResponse.httpCode == 200) {
                    response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.OK;
                    response.userID = webResponse.data.userID;
                    response.token = webResponse.data.token;
                    response.refreshToken = webResponse.data.refreshToken;

                    // Save the token and current user ID informations
                    SharedPreference.setAuthToken(context, webResponse.data.token);
                    SharedPreference.setAuthRefreshToken(context, webResponse.data.refreshToken);
                    SharedPreference.setUserID(context, webResponse.data.userID);
                } else {
                    if (webResponse != null && webResponse.apiCode == 50) {
                        response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.EMAIL_ALREADY_IN_USE;
                    } else {
                        response.code = AuthenticationResponseRepositoryModel.AuthenticationCode.SERVER_ERROR;
                    }
                }
                callback.registerCallback(response);
            }
        });
    }
}