package multiverse.androidapp.multiverse.database.webDatabase;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.ApiResponse;
import multiverse.androidapp.multiverse.model.webModel.util.ErrorWebModel;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {

    private HttpService() {

    }

    public static <T> ApiResponse<T> get(String route, Class<T> responseClass, Context context) {
        String token = SharedPreference.getAuthToken(context);

        Request request;
        if(token != null) {
            request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + token)
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .get()
                    .build();
        } else {
            request = new Request.Builder()
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .get()
                    .build();
        }
        ApiResponse<T> response = makeCall(request, responseClass);
        return response;
    }

    public static <B, T> ApiResponse<T> post(String route, B body, Class<T> responseClass, Context context) {
        String token = SharedPreference.getAuthToken(context);
        String bodyJson = "";
        if(body != null){
            bodyJson = new Gson().toJson(body);
        }

        Request request;
        if(token != null) {
            request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + token)
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .post(RequestBody.create(MediaType.parse("application/json"), bodyJson))
                    .build();
        } else {
            request = new Request.Builder()
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .post(RequestBody.create(MediaType.parse("application/json"), bodyJson))
                    .build();
        }
        ApiResponse<T> response = makeCall(request, responseClass);
        return response;
    }

    public static <B, T> ApiResponse<T> delete(String route, B body, Class<T> responseClass, Context context) {
        String token = SharedPreference.getAuthToken(context);
        String bodyJson = "";
        if(body != null){
            bodyJson = new Gson().toJson(body);
        }

        Request request;
        if(token != null) {
            request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + token)
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .delete(RequestBody.create(MediaType.parse("application/json"), bodyJson))
                    .build();
        } else {
            request = new Request.Builder()
                    .url("https://" + context.getString(R.string.network_multiverse_server_dns) + route)
                    .delete(RequestBody.create(MediaType.parse("application/json"), bodyJson))
                    .build();
        }
        ApiResponse<T> response = makeCall(request, responseClass);
        return response;
    }

    public static <T> ApiResponse<T> makeCall(Request request, Class<T> responseClass) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        OkHttpClient client = builder.build();
        ApiResponse<T> httpResponse = new ApiResponse<>();

        //client.hostnameVerifier();

        try {
            Response response = client.newCall(request).execute();

            int code = response.code();
            String responseStr = response.body().string();

            httpResponse.httpCode = code;
            if(code == 200){
                httpResponse.data = new Gson().fromJson(responseStr, responseClass);
                //httpResponse.token = response.header("Authorization");
                httpResponse.apiCode = 0;
            } else {
                if(responseStr == null && !responseStr.isEmpty()) {
                    ErrorWebModel data = new Gson().fromJson(responseStr, ErrorWebModel.class);
                    httpResponse.apiCode = data.errorType;
                } else {
                    httpResponse.apiCode = -1;
                }

                httpResponse.data = null;
            }

        } catch (IOException e) {
            httpResponse = null;
        }
        return httpResponse;
    }
}
