package multiverse.androidapp.multiverse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import multiverse.androidapp.multiverse.ui.community.CommunityPagerAdapter;
import multiverse.androidapp.multiverse.ui.community.CommunityType;

public class CommunityActivity extends AppCompatActivity {

    private static final String COMMUNITY_TYPE_OPEN_KEY = "community_open_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_community);
        CommunityPagerAdapter sectionsPagerAdapter = new CommunityPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.community_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.community_tabs);
        tabs.setupWithViewPager(viewPager);

        Bundle bundle = getIntent().getExtras();

        // Open the right page
        int page = bundle.getInt(COMMUNITY_TYPE_OPEN_KEY);
        viewPager.setCurrentItem(page);

        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.community_actionbar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    public static Intent getInstance(Context context, CommunityType openType) {
        Intent intent = new Intent(context, CommunityActivity.class);
        Bundle bundle = new Bundle();

        int no = 0;
        if (openType != null)
            no = openType.no;

        bundle.putInt(COMMUNITY_TYPE_OPEN_KEY, no);
        intent.putExtras(bundle);
        return intent;
    }
}