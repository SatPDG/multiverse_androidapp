package multiverse.androidapp.multiverse.ui.conversation.conversationSettings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.Toast;

import multiverse.androidapp.multiverse.ConversationActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.repository.callback.ConversationCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.ui.community.CommunityClickListener;
import multiverse.androidapp.multiverse.ui.util.UIWebErrorHandler;

public class ConversationSettingsFragment extends Fragment implements CommunityClickListener {

    private final static String OPEN_MODE_KEY = "openMode";
    private final static String CONVERSATION_ID_KEY = "conversationID";
    private final static int CREATE_CONV_MODE = 0;
    private final static int UPDATE_CONV_MODE = 1;

    private ConversationSettingsViewModel viewModel;
    private ConversationSettingsUserListAdapter listAdapter;
    private boolean isCreatingNewConversation;
    private int conversationID;

    private AppCompatEditText nameEditText, searchEditText;
    private AppCompatImageButton searchBtn, closeBtn;
    private AppCompatButton saveBtn;
    private RecyclerView userList;

    public ConversationSettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            int openMode = args.getInt(OPEN_MODE_KEY);
            if (openMode == CREATE_CONV_MODE) {
                isCreatingNewConversation = true;
            } else if (openMode == UPDATE_CONV_MODE) {
                isCreatingNewConversation = false;
                conversationID = args.getInt(CONVERSATION_ID_KEY);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation_settings, container, false);

        nameEditText = root.findViewById(R.id.conversation_settings_conversation_name);
        searchEditText = root.findViewById(R.id.conversation_settings_user_search);
        searchBtn = root.findViewById(R.id.conversation_settings_search_btn);
        closeBtn = root.findViewById(R.id.conversation_settings_close_btn);
        saveBtn = root.findViewById(R.id.conversation_settings_save_btn);
        userList = root.findViewById(R.id.conversation_settings_user_list);

        viewModel = new ViewModelProvider(requireActivity()).get(ConversationSettingsViewModel.class);
        viewModel.setSettingsMode(isCreatingNewConversation, conversationID);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new ConversationSettingsUserListAdapter(getContext(), viewModel, this);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(listAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.searchForUser(searchEditText.getText().toString());
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.closeSearch();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.saveConversation(nameEditText.getText().toString());
            }
        });

        viewModel.getLoadedSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                listAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getConversationName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nameEditText.setText(s);
            }
        });
        viewModel.getLastWebError().observe(getViewLifecycleOwner(), new Observer<WebError>() {
            @Override
            public void onChanged(WebError webError) {
                if (webError != null)
                    UIWebErrorHandler.handle(webError, getContext());
            }
        });
        viewModel.getLastApiCallback().observe(getViewLifecycleOwner(), new Observer<ConversationCallback.ConversationCallbackType>() {
            @Override
            public void onChanged(ConversationCallback.ConversationCallbackType conversationCallbackType) {
                if (conversationCallbackType == ConversationCallback.ConversationCallbackType.CREATE_CONVERSATION) {
                    // Go to the new conversation page
                    Toast.makeText(getContext(), R.string.conversation_settings_conv_created, Toast.LENGTH_SHORT).show();

                    conversationID = viewModel.getConversationID();
                    // TODO - open the conversation message activity
                    Intent intent = ConversationActivity.conversationMessageInstance(getContext(), conversationID);
                    intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    startActivity(intent);
                    getActivity().finish();
                } else if (conversationCallbackType == ConversationCallback.ConversationCallbackType.SET_CONVERSATION_INFO) {
                    Toast.makeText(getContext(), R.string.conversation_settings_conv_updated, Toast.LENGTH_SHORT).show();
                } else if (conversationCallbackType == ConversationCallback.ConversationCallbackType.ADD_USER_TO_CONV) {
                    Toast.makeText(getContext(), R.string.conversation_settings_user_added, Toast.LENGTH_SHORT).show();
                } else if (conversationCallbackType == ConversationCallback.ConversationCallbackType.REMOVE_USER_FROM_CONV) {
                    Toast.makeText(getContext(), R.string.conversation_settings_user_removed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void userClickListener(UserModel user) {

    }

    @Override
    public void acceptUserClickListener(UserModel user) {
        viewModel.addUser(user.userID);
    }

    @Override
    public void refuseUserClickListener(UserModel user) {
        viewModel.removeUser(user.userID);
    }

    public static ConversationSettingsFragment createConversationNewInstance() {
        ConversationSettingsFragment fragment = new ConversationSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(OPEN_MODE_KEY, CREATE_CONV_MODE);
        fragment.setArguments(args);
        return fragment;
    }

    public static ConversationSettingsFragment updateConversationNewInstance(int conversationID) {
        ConversationSettingsFragment fragment = new ConversationSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(OPEN_MODE_KEY, UPDATE_CONV_MODE);
        args.putInt(CONVERSATION_ID_KEY, conversationID);
        fragment.setArguments(args);
        return fragment;
    }
}
