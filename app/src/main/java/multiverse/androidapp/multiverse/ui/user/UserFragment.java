package multiverse.androidapp.multiverse.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class UserFragment extends Fragment {

    public static final String USER_ID_KEY = "userID";

    private UserViewModel viewModel;

    private Guideline communityMiddleGuideline, request1Guideline, request2Guideline;
    private LinearLayout nbrOfFollowerContainer, nbrOfRequestFollowerContainer, nbrOfFollowedContainer, nbrOfRequestFollowedContainer, nbrOfPostContainer;
    private AppCompatButton followerButton, followedButton, messageButton;
    private ConstraintLayout requestContraintLayout;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(int userID) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(USER_ID_KEY, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        // Community section
        communityMiddleGuideline = root.findViewById(R.id.user_guideline_communityHorizMiddle);
        nbrOfFollowerContainer = root.findViewById(R.id.user_nbrFollower_container);
        nbrOfRequestFollowerContainer = root.findViewById(R.id.user_nbrRequestFollower_container);
        nbrOfFollowedContainer = root.findViewById(R.id.user_nbrFollowed_container);
        nbrOfRequestFollowedContainer = root.findViewById(R.id.user_nbrRequestFollowed_container);
        nbrOfPostContainer = root.findViewById(R.id.user_nbrPost_container);

        // Request section
        requestContraintLayout = root.findViewById(R.id.user_request_container);
        request1Guideline = root.findViewById(R.id.user_guideline_request1);
        request2Guideline = root.findViewById(R.id.user_guideline_request2);
        followerButton = root.findViewById(R.id.user_follower_btn);
        followedButton = root.findViewById(R.id.user_followed_btn);
        messageButton = root.findViewById(R.id.user_message_btn);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

//        int ownUserID = SharedPreference.getUserID(getContext());
//        Bundle bundle = getArguments();
//        int userID = bundle.getInt(USER_ID_KEY);
        boolean isOwnUser = false;//(ownUserID == userID)? true : false;

        if(isOwnUser) {
            // Community section
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) communityMiddleGuideline.getLayoutParams();
            params.guidePercent = 0.50f;
            communityMiddleGuideline.setLayoutParams(params);
            nbrOfRequestFollowerContainer.setVisibility(View.VISIBLE);
            nbrOfRequestFollowedContainer.setVisibility(View.VISIBLE);

            // Request section
            requestContraintLayout.setVisibility(View.GONE);


//            viewModel.getUserOwnInfo().observe(getViewLifecycleOwner(), new Observer<UserOwnInfoRepositoryModel>() {
//                @Override
//                public void onChanged(UserOwnInfoRepositoryModel userOwnInfoRepositoryModel) {
//
//                }
//            });
//            viewModel.loadUserOwnInfo();
        } else {
            // Community section
            nbrOfRequestFollowerContainer.setVisibility(View.GONE);
            nbrOfRequestFollowedContainer.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) communityMiddleGuideline.getLayoutParams();
            params.guidePercent = 0.95f;
            communityMiddleGuideline.setLayoutParams(params);

            // Request section
            requestContraintLayout.setVisibility(View.VISIBLE);

//            viewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInfoRespositoryModel>() {
//                @Override
//                public void onChanged(UserInfoRespositoryModel userInfoRespositoryModel) {
//
//                }
//            });
//            viewModel.loadUserInfo(userID);
        }
    }

    private void setRequestView(boolean isFollower, boolean isFollowed, boolean isRequestFollower, boolean isRequestFollowed) {
        if(isRequestFollower) {
            followerButton.setText(R.string.user_deleteRequest);
        } else if(isFollower) {
            followerButton.setText(R.string.user_removeFollower);
        } else {
            followerButton.setText(R.string.user_sendRequest);
        }

        if(isRequestFollowed) {
            followedButton.setText(R.string.user_deleteRequest);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.33f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.66f;
            request2Guideline.setLayoutParams(params);
            followedButton.setVisibility(View.VISIBLE);
        } else if(isFollowed) {
            followedButton.setText(R.string.user_removeFollowed);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.33f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.66f;
            request2Guideline.setLayoutParams(params);
            followedButton.setVisibility(View.VISIBLE);
        } else {
            followedButton.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.50f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.50f;
            request2Guideline.setLayoutParams(params);
        }
    }
}
