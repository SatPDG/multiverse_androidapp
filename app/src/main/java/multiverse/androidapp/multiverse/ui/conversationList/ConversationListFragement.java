package multiverse.androidapp.multiverse.ui.conversationList;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import multiverse.androidapp.multiverse.ConversationActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;

public class ConversationListFragement extends Fragment implements ConversationListClickListener {

    private ConversationListViewModel viewModel;

    private RecyclerView conversationRecylerView;
    private ConstraintLayout searchContraintLayout;
    private AppCompatEditText searchTextEdit;
    private AppCompatImageButton searchButton, addButton;

    public ConversationListFragement() {
        // Required empty public constructor
    }

    public static ConversationListFragement newInstance() {
        ConversationListFragement fragment = new ConversationListFragement();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        conversationRecylerView = root.findViewById(R.id.conversation_list_list);
        searchContraintLayout = root.findViewById(R.id.conversation_list_search_container);
        searchButton = root.findViewById(R.id.conversation_list_search_btn);
        addButton = root.findViewById(R.id.conversation_list_add);
        searchTextEdit = root.findViewById(R.id.conversation_list_search_text);

        viewModel = new ViewModelProvider(requireActivity()).get(ConversationListViewModel.class);

        // Set up the list adapter
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        conversationRecylerView.setLayoutManager(llm);
        final ConversationListAdapter listAdapter = new ConversationListAdapter(getContext(), viewModel, this);
        conversationRecylerView.setAdapter(listAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the create conversation view
                Intent intent = ConversationActivity.createConversationInstance(getContext());
                startActivity(intent);
            }
        });

        viewModel.getLoadedSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                listAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    @Override
    public void conversationClickListener(ConversationModel conversation) {

    }
}
