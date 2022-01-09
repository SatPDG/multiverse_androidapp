package multiverse.androidapp.multiverse.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import multiverse.androidapp.multiverse.CommunityActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.repositoryModel.user.UserOwnInfoRepositoryModel;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.ui.community.CommunityType;
import multiverse.androidapp.multiverse.ui.util.UIWebErrorHandler;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class UserFragment extends Fragment {

    public static final String USER_ID_KEY = "userID";

    private UserViewModel viewModel;

    private Guideline communityMiddleGuideline, request1Guideline, request2Guideline;
    private LinearLayout nbrOfFollowerContainer, nbrOfFollowerRequestContainer, nbrOfFollowedContainer, nbrOfFollowedRequestContainer, nbrOfPostContainer;
    private AppCompatButton followedButton, followerButton, messageButton;
    private ConstraintLayout requestContraintLayout;
    private AppCompatTextView usernameTextView, descriptionTextView;
    private AppCompatTextView nbrOfFollowerTextView, nbrOfFollowedTextView, nbrOfFollowerRequestTextView, nbrOfFollowedRequestTextView, nbrPostTextView;

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
        nbrOfFollowerRequestContainer = root.findViewById(R.id.user_nbrRequestFollower_container);
        nbrOfFollowedContainer = root.findViewById(R.id.user_nbrFollowed_container);
        nbrOfFollowedRequestContainer = root.findViewById(R.id.user_nbrRequestFollowed_container);
        nbrOfPostContainer = root.findViewById(R.id.user_nbrPost_container);
        nbrOfFollowerTextView = root.findViewById(R.id.user_nbrFollower_nbr);
        nbrOfFollowerRequestTextView = root.findViewById(R.id.user_nbrRequestFollower_nbr);
        nbrOfFollowedTextView = root.findViewById(R.id.user_nbrFollowed_nbr);
        nbrOfFollowedRequestTextView = root.findViewById(R.id.user_nbrRequestFollowed_nbr);
        nbrPostTextView = root.findViewById(R.id.user_nbrPost_nbr);

        // Request section
        requestContraintLayout = root.findViewById(R.id.user_request_container);
        request1Guideline = root.findViewById(R.id.user_guideline_request1);
        request2Guideline = root.findViewById(R.id.user_guideline_request2);
        followedButton = root.findViewById(R.id.user_followed_btn);
        followerButton = root.findViewById(R.id.user_follower_btn);
        messageButton = root.findViewById(R.id.user_message_btn);

        // User section
        usernameTextView = root.findViewById(R.id.user_username);
        descriptionTextView = root.findViewById(R.id.user_userDescription);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        int ownUserID = SharedPreference.getUserID(getContext());
        Bundle bundle = getArguments();
        final int userID = bundle.getInt(USER_ID_KEY);
        boolean isOwnUser = (ownUserID == userID) ? true : false;

        followedButton.setEnabled(false);
        followerButton.setEnabled(false);
        messageButton.setEnabled(false);

        followerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoRespositoryModel model = viewModel.getUserInfo().getValue();
                if (model != null) {
                    if (model.isAFollower) {
                        viewModel.removeFollower();
                    } else if (model.isFollowerRequestReceived) {
                        viewModel.deleteFollowerRequest();
                    } else {
                        viewModel.acceptFollowedRequest();
                    }
                }
            }
        });
        followedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoRespositoryModel model = viewModel.getUserInfo().getValue();
                if (model != null) {
                    if (model.isFollowed) {
                        viewModel.removeFollowed();
                    } else if (model.isFollowedRequestSend) {
                        viewModel.deleteFollowedRequest();
                    } else {
                        viewModel.sendFollowerRequest();
                    }
                }
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - implement link to message
            }
        });

        viewModel.getWebError().observe(getViewLifecycleOwner(), new Observer<WebError>() {
            @Override
            public void onChanged(WebError webError) {
                if (webError != null) {
                    UIWebErrorHandler.handle(webError, getContext());
                }
            }
        });

        if(isOwnUser) {
            // Community section
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) communityMiddleGuideline.getLayoutParams();
            params.guidePercent = 0.50f;
            communityMiddleGuideline.setLayoutParams(params);
            nbrOfFollowerRequestContainer.setVisibility(View.VISIBLE);
            nbrOfFollowedRequestContainer.setVisibility(View.VISIBLE);

            // Request section
            requestContraintLayout.setVisibility(View.GONE);

            viewModel.getUserOwnInfo().observe(getViewLifecycleOwner(), new Observer<UserOwnInfoRepositoryModel>() {
                @Override
                public void onChanged(UserOwnInfoRepositoryModel userInfo) {
                    if (userInfo != null) {
                        usernameTextView.setText(userInfo.firstname + " " + userInfo.lastname);
                        nbrOfFollowerTextView.setText(String.valueOf(userInfo.nbrOfFollower));
                        nbrOfFollowerRequestTextView.setText(String.valueOf(userInfo.nbrOfRequestFollower));
                        nbrOfFollowedTextView.setText(String.valueOf(userInfo.nbrOfFollowed));
                        nbrOfFollowedRequestTextView.setText(String.valueOf(userInfo.nbrOfRequestFollowed));
                        nbrPostTextView.setText(String.valueOf(0));
                    }
                }
            });
            viewModel.loadUserOwnInfo();

            // Add link to community activity
            nbrOfFollowerContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CommunityActivity.getInstance(getContext(), CommunityType.FOLLOWER_LIST);
                    startActivity(intent);
                }
            });
            nbrOfFollowedContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CommunityActivity.getInstance(getContext(), CommunityType.FOLLOWED_LIST);
                    startActivity(intent);
                }
            });
            nbrOfFollowerRequestContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CommunityActivity.getInstance(getContext(), CommunityType.FOLLOWER_REQUEST_TYPE);
                    startActivity(intent);
                }
            });
            nbrOfFollowedRequestContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CommunityActivity.getInstance(getContext(), CommunityType.FOLLOWED_REQUEST_TYPE);
                    startActivity(intent);
                }
            });

        } else {
            // Community section
            nbrOfFollowerRequestContainer.setVisibility(View.GONE);
            nbrOfFollowedRequestContainer.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) communityMiddleGuideline.getLayoutParams();
            params.guidePercent = 0.95f;
            communityMiddleGuideline.setLayoutParams(params);

            // Request section
            requestContraintLayout.setVisibility(View.VISIBLE);

            viewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInfoRespositoryModel>() {
                @Override
                public void onChanged(UserInfoRespositoryModel userInfo) {
                    if (userInfo != null) {
                        usernameTextView.setText(userInfo.firstname + " " + userInfo.lastname);
                        nbrOfFollowerTextView.setText(String.valueOf(userInfo.nbrOfFollower));
                        nbrOfFollowedTextView.setText(String.valueOf(userInfo.nbrOfFollowed));
                        nbrPostTextView.setText(String.valueOf(0));

                        followedButton.setEnabled(true);
                        followerButton.setEnabled(true);
                        messageButton.setEnabled(true);
                        setRequestView(userInfo.isAFollower, userInfo.isFollowed, userInfo.isFollowerRequestReceived, userInfo.isFollowedRequestSend);
                    }
                }
            });
            viewModel.loadUserInfo(userID);
        }
    }

    private void setRequestView(boolean isFollower, boolean isFollowed, boolean isRequestFollower, boolean isRequestFollowed) {
        if (isRequestFollowed) {
            followedButton.setText(R.string.user_deleteRequest);
        } else if (isFollower) {
            followedButton.setText(R.string.user_removeFollowed);
        } else {
            followedButton.setText(R.string.user_sendRequest);
        }

        if(isRequestFollower) {
            followerButton.setText(R.string.user_deleteRequest);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.33f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.66f;
            request2Guideline.setLayoutParams(params);
            followerButton.setVisibility(View.VISIBLE);
        } else if (isFollower) {
            followerButton.setText(R.string.user_removeFollower);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.33f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.66f;
            request2Guideline.setLayoutParams(params);
            followerButton.setVisibility(View.VISIBLE);
        } else {
            followerButton.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) request1Guideline.getLayoutParams();
            params.guidePercent = 0.50f;
            request1Guideline.setLayoutParams(params);
            params = (ConstraintLayout.LayoutParams) request2Guideline.getLayoutParams();
            params.guidePercent = 0.50f;
            request2Guideline.setLayoutParams(params);
        }
    }
}
