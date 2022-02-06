package multiverse.androidapp.multiverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import multiverse.androidapp.multiverse.ui.conversation.conversationMessage.ConversationMessageFragment;
import multiverse.androidapp.multiverse.ui.conversation.conversationSettings.ConversationSettingsFragment;

public class ConversationActivity extends AppCompatActivity {

    private static final String CONVERSATION_VIEW_TYPE_KEY = "viewType";
    private static final String CONVERSATION_ID_KEY = "conversationID";
    private static final int EDIT_CONVERSATION_SETTINGS = 0;
    private static final int CREATE_CONVERSATION = 1;
    private static final int OPEN_CONVERSATION_MESSAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Bundle args = getIntent().getExtras();
        int openMode = args.getInt(CONVERSATION_VIEW_TYPE_KEY);
        if (openMode == CREATE_CONVERSATION) {
            //getActionBar().setTitle(R.string.user_actionBar_title);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.conversation_activity_frag_container, ConversationSettingsFragment.createConversationNewInstance());
            transaction.commit();
        } else if (openMode == EDIT_CONVERSATION_SETTINGS) {

        } else if (openMode == OPEN_CONVERSATION_MESSAGE) {
            int conversationID = getIntent().getIntExtra(CONVERSATION_ID_KEY, -1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.conversation_activity_frag_container, ConversationMessageFragment.newInstance(conversationID));
            transaction.commit();
        }
    }

    public static Intent createConversationInstance(Context context) {
        Intent intent = new Intent(context, ConversationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CONVERSATION_VIEW_TYPE_KEY, CREATE_CONVERSATION);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent updateConversationInstance(Context context, int conversationID) {
        Intent intent = new Intent(context, ConversationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CONVERSATION_VIEW_TYPE_KEY, EDIT_CONVERSATION_SETTINGS);
        bundle.putInt(CONVERSATION_ID_KEY, conversationID);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent conversationMessageInstance(Context context, int conversationID) {
        Intent intent = new Intent(context, ConversationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CONVERSATION_VIEW_TYPE_KEY, OPEN_CONVERSATION_MESSAGE);
        bundle.putInt(CONVERSATION_ID_KEY, conversationID);
        intent.putExtras(bundle);
        return intent;
    }
}
