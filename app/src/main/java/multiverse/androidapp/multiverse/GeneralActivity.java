package multiverse.androidapp.multiverse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import multiverse.androidapp.multiverse.ui.user.UserFragment;

public class GeneralActivity extends AppCompatActivity {

    private final static String GENERAL_ACTIVITY_INTENT_KEY = "activityIntent";
    private final static String USER_ID_KEY = "userID";

    private final static String USER_INTENT = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String intent = bundle.getString(GENERAL_ACTIVITY_INTENT_KEY);
        if(intent.equals(USER_INTENT)) {
            actionBar.setTitle(R.string.user_actionBar_title);
            
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            int userID = bundle.getInt(USER_ID_KEY);
            transaction.replace(R.id.general_activity_frag_container, UserFragment.newInstance(userID));
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getUserInstance(Context context, int userID) {
        Intent intent = new Intent(context, GeneralActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GENERAL_ACTIVITY_INTENT_KEY, USER_INTENT);
        bundle.putInt(USER_ID_KEY, userID);
        intent.putExtras(bundle);
        return intent;
    }
}
