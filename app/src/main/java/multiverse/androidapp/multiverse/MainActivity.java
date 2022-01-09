package multiverse.androidapp.multiverse;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.ui.authentication.RegisterFragment;
import multiverse.androidapp.multiverse.ui.community.CommunityType;
import multiverse.androidapp.multiverse.ui.user.UserFragment;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class MainActivity extends AppCompatActivity {

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.LEFT));
        getWindow().setExitTransition(new Slide(Gravity.LEFT));

        // If their is no user authenticated, go back to authentication.
        if(SharedPreference.getAuthToken(getApplicationContext()) == null) {
            // Launch authentication activity
            Intent intent = AuthenticationActivity.newInstance(this);
            startActivity(intent);
            finish();
        }


        {
            // Reset the db
            MultiverseDbHelper dbHelper = new MultiverseDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.onDowngrade(db, 0, 0);
        }

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_users, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_main_action_bar);

        View customBarView = actionBar.getCustomView();

        // User button
        ImageView userImageView = customBarView.findViewById(R.id.main_actionbar_user);
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentUserID = SharedPreference.getUserID(getApplicationContext());
                Intent intent = GeneralActivity.getUserInstance(getApplicationContext(), currentUserID);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        // notification button
        ImageView notificationImageView = customBarView.findViewById(R.id.main_actionbar_notification);
        notificationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
