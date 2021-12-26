package multiverse.androidapp.multiverse.ui.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import multiverse.androidapp.multiverse.AuthenticationActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.repository.callback.WebErrorCallback;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class UIWebErrorHandler {

    private UIWebErrorHandler() {

    }

    public static void handle(WebErrorCallback.WebError webError, Context context) {
        if(webError.authenticationError) {
            // Clear the credential in the memory
            SharedPreference.clearAuth(context);
            SharedPreference.clearUser(context);

            // Go back to the authentication activity
            Intent intent = new Intent(context.getApplicationContext(), AuthenticationActivity.class);
            context.getApplicationContext().startActivity(intent);
        } else if(webError.isOffline) {
            Toast.makeText(context, context.getString(R.string.general_device_offline), Toast.LENGTH_LONG).show();
        } else if(webError.ressourceNotAccessible) {
            Toast.makeText(context, context.getString(R.string.general_ressource_not_accessible), Toast.LENGTH_LONG).show();
        } else if(webError.serverError) {
            Toast.makeText(context, context.getString(R.string.general_serverError), Toast.LENGTH_LONG).show();
        }
    }
}
