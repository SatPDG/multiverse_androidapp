package multiverse.androidapp.multiverse.util.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    public final static String USER_SHARED_PREFERENCE = "user";
    public final static String USER_ID_SHARED_PREFERENCE = "userID";

    public final static String AUTH_SHARED_PREFERENCE = "auth";
    public final static String AUTH_TOKEN_SHARED_PREFERENCE = "token";
    public final static String AUTH_REFRESH_TOKEN_SHARED_PREFERENCE = "refreshToken";

    private SharedPreference(){

    }

    public static int getUserID(Context context) {
        return context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE).getInt(USER_ID_SHARED_PREFERENCE, -1);
    }

    public static void setUserID(Context context, int userID) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putInt(USER_ID_SHARED_PREFERENCE, userID);

        editor.commit();
    }

    public static void clearUser(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.clear();

        editor.commit();
    }

    public static String getAuthToken(Context context) {
        return context.getSharedPreferences(AUTH_SHARED_PREFERENCE, Context.MODE_PRIVATE).getString(AUTH_TOKEN_SHARED_PREFERENCE, null);
    }

    public static void setAuthToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AUTH_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(AUTH_TOKEN_SHARED_PREFERENCE, token);

        editor.commit();
    }

    public static String getAuthRefreshToken(Context context) {
        return context.getSharedPreferences(AUTH_SHARED_PREFERENCE, Context.MODE_PRIVATE).getString(AUTH_REFRESH_TOKEN_SHARED_PREFERENCE, null);
    }

    public static void setAuthRefreshToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AUTH_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(AUTH_REFRESH_TOKEN_SHARED_PREFERENCE, token);

        editor.commit();
    }

    public static void clearAuth(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AUTH_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.clear();

        editor.commit();
    }
}
