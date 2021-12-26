package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.util.function.Supplier;

import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.model.webModel.authentication.RefreshRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.authentication.RefreshResponseWebModel;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class ApiCaller {

    private static final int API_CALLER_NUMBER_OF_RETRY = 3;

    private ApiCaller() {

    }

    public static <T> WebServiceResponse<T> callApi(Supplier<ApiResponse<T>> apiCall, Context context) {
        ApiResponse<T> response = null;
        boolean isResponseOK = false;
        boolean isRessourceNotAccessible = false;
        boolean isBadAuthentication = false;
        boolean serverFailed = false;
        boolean clientFailed = false;
        boolean isOffline = false;

        // Check if the device is connected to a network
        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if(capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            isOffline = true;
        }

        int nbrOfRetry = 0;
        while(nbrOfRetry < API_CALLER_NUMBER_OF_RETRY && !isOffline) {
            response = apiCall.get();

            if(response == null) {
                serverFailed = true;
                break;
            } else  if(response.httpCode <= 300) {
                // Teh request was successful
                serverFailed = false;
                isResponseOK = true;
                break;
            } else if (response.httpCode == 401) {
                // The token is expire or the credentiel are not entered...
                RefreshRequestWebModel request = new RefreshRequestWebModel();
                request.token = SharedPreference.getAuthToken(context);
                request.refreshToken = SharedPreference.getAuthRefreshToken(context);

                ApiResponse<RefreshResponseWebModel> refreshResponse = AuthenticationWebService.refresh(request, context);
                if(refreshResponse.httpCode == 200) {
                    // The refresh has succeded.
                    SharedPreference.setAuthToken(context, refreshResponse.data.token);
                    SharedPreference.setAuthRefreshToken(context, refreshResponse.data.refreshToken);
                } else {
                    isBadAuthentication = true;
                    break;
                }
            } else if(response.httpCode == 403 || response.httpCode == 404) {
                isRessourceNotAccessible = true;
                break;
            } else if(response.httpCode >= 500) {
                // Just retry
                serverFailed = true;
                nbrOfRetry++;
            } else {
               break;
            }
        }

        WebServiceResponse<T> callerResponse = new WebServiceResponse<>();
        callerResponse.httpCode = response.httpCode;
        callerResponse.apiCode = response.apiCode;
        callerResponse.data = response.data;
        callerResponse.isResponseOK = isResponseOK;
        callerResponse.authenticationError = isBadAuthentication;
        callerResponse.serverError = serverFailed;
        callerResponse.isRessourceNotAccessible = isRessourceNotAccessible;
        callerResponse.isOffline = isOffline;

        return callerResponse;
    }
}
