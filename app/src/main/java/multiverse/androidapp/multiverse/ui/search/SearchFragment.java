package multiverse.androidapp.multiverse.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import multiverse.androidapp.multiverse.GeneralActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.ui.util.UIWebErrorHandler;

public class SearchFragment extends Fragment implements UsersClickListener {

    private SearchViewModel viewModel;

    private View mainView;
    private AppCompatEditText searchEditText;
    private AppCompatImageButton searchButton;
    private RecyclerView usersRecyclerView;

    private UsersListAdapter listAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        mainView = root;
        searchEditText = root.findViewById(R.id.users_search_text);
        searchButton = root.findViewById(R.id.users_search_button);
        usersRecyclerView = root.findViewById(R.id.users_list);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply the search
                String nameSearch = searchEditText.getText().toString();
                viewModel.searchForUserByName(nameSearch);

                // Hide the keyboard
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        // Set up the list adapter
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        usersRecyclerView.setLayoutManager(llm);
        listAdapter = new UsersListAdapter(getContext(), R.layout.entry_user, viewModel, this);
        usersRecyclerView.setAdapter(listAdapter);

        // Set up the observer
        viewModel.getLoadedSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                listAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getLastError().observe(getViewLifecycleOwner(), new Observer<UserListCallback.UserCallbackType>() {
            @Override
            public void onChanged(UserListCallback.UserCallbackType userCallbackType) {
                WebError webError = viewModel.getLastWebError();
                UIWebErrorHandler.handle(webError, getContext());
            }
        });

        viewModel.getRecentUserList();
    }

    @Override
    public void userClickListener(UserModel user) {
        // Open the user fragment
        if (user.userID > 0) {
            Intent intent = GeneralActivity.getUserInstance(getContext(), user.userID);
            startActivity(intent);
        }
    }
}
