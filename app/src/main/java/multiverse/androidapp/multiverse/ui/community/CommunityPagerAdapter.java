package multiverse.androidapp.multiverse.ui.community;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import multiverse.androidapp.multiverse.R;

public class CommunityPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.community_follower,
            R.string.community_followed,
            R.string.community_follower_request,
            R.string.community_followed_request};

    private final Context mContext;

    public CommunityPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = CommunityFragment.newInstance(CommunityType.FOLLOWER_LIST);
        } else if (position == 1) {
            fragment = CommunityFragment.newInstance(CommunityType.FOLLOWED_LIST);
        } else if (position == 2) {
            fragment = CommunityFragment.newInstance(CommunityType.FOLLOWER_REQUEST_TYPE);
        } else if (position == 3) {
            fragment = CommunityFragment.newInstance(CommunityType.FOLLOWED_REQUEST_TYPE);
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}