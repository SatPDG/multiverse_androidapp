package multiverse.androidapp.multiverse.ui.conversation.conversationMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import multiverse.androidapp.multiverse.R;

public class ConversationMessageFragment extends Fragment {

    public static final String CONVERSATION_ID_KEY = "conversationID";

    public ConversationMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation_message, container, false);
    }

    public static ConversationMessageFragment newInstance(int conversationID) {
        ConversationMessageFragment fragment = new ConversationMessageFragment();
        Bundle args = new Bundle();
        args.putInt(CONVERSATION_ID_KEY, conversationID);
        fragment.setArguments(args);
        return fragment;
    }
}
