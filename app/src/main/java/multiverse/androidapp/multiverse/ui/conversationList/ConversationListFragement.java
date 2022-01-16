package multiverse.androidapp.multiverse.ui.conversationList;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;

public class ConversationListFragement extends Fragment implements ConversationListClickListener {

    private ConversationListViewModel viewModel;

    private RecyclerView conversationRecylerView;
    private ConstraintLayout searchContraintLayout;
    private AppCompatEditText searchTextEdit;
    private AppCompatImageButton searchButton;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        conversationRecylerView = root.findViewById(R.id.conversation_list_list);
        searchContraintLayout = root.findViewById(R.id.conversation_list_search_container);
        searchButton = root.findViewById(R.id.conversation_list_search_btn);
        searchTextEdit = root.findViewById(R.id.conversation_list_search_text);

        viewModel = new ViewModelProvider(requireActivity()).get(ConversationListViewModel.class);

        // Set up the list adapter
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        conversationRecylerView.setLayoutManager(llm);
        ConversationListAdapter listAdapter = new ConversationListAdapter(getContext(), viewModel, this);
        conversationRecylerView.setAdapter(listAdapter);

        return root;
    }

    @Override
    public void conversationClickListener(ConversationWebModel conversation) {

    }
}
