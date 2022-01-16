package multiverse.androidapp.multiverse.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import multiverse.androidapp.multiverse.GeneralActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.ui.util.UIWebErrorHandler;

public class CommunityFragment extends Fragment implements CommunityClickListener {

    private static final String COMMUNITY_TYPE_KEY = "community_type";

    private CommunityViewModel viewModel;

    private CommunityType type;
    private RecyclerView userList;

    private CommunityListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int noType = getArguments().getInt(COMMUNITY_TYPE_KEY);
            type = CommunityType.getType(noType);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_community, container, false);

        userList = root.findViewById(R.id.community_recyclerlist);

        viewModel = new ViewModelProvider(requireActivity()).get(type.toString(), CommunityViewModel.class);
        viewModel.setType(type);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new CommunityListAdapter(getContext(), type, viewModel, this);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(listAdapter);

        viewModel.getLoadedSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                listAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getLastWebError().observe(getViewLifecycleOwner(), new Observer<WebError>() {
            @Override
            public void onChanged(WebError webError) {
                UIWebErrorHandler.handle(webError, getContext());
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.loadUser(0);
    }

    @Override
    public void userClickListener(UserModel user) {
        // Open user
        Intent intent = GeneralActivity.getUserInstance(getContext(), user.userID);
        startActivity(intent);
    }

    @Override
    public void acceptUserClickListener(UserModel user) {
        viewModel.acceptUser(user.userID);
    }

    @Override
    public void refuseUserClickListener(UserModel user) {
        viewModel.refuseUser(user.userID);
    }

    public static CommunityFragment newInstance(CommunityType type) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COMMUNITY_TYPE_KEY, type.no);
        fragment.setArguments(bundle);
        return fragment;
    }
}