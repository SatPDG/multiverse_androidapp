package multiverse.androidapp.multiverse.ui.conversation.conversationMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.MessageModel;
import multiverse.androidapp.multiverse.util.functions.TimeUtil;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class ConversationMessageListAdapter extends RecyclerView.Adapter<ConversationMessageListAdapter.ConversationMessageViewHolder> {

    private Context context;

    private ConversationMessageViewModel viewModel;

    @NonNull
    @Override
    public ConversationMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_message, parent, false);

        return new ConversationMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationMessageViewHolder holder, int position) {
        if (viewModel.getMessageList().size() > position && viewModel.getMessageList().get(position) != null) {
            MessageModel model = viewModel.getMessageList().get(position);
            holder.message = model;
            holder.synch();
        } else {
            holder.message = null;
            holder.synch();
            viewModel.loadMessage(position);
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getTotalSize().getValue();
    }

    public class ConversationMessageViewHolder extends RecyclerView.ViewHolder {

        public MessageModel message;

        public ConstraintLayout mainContainer, contentContainer;
        public AppCompatTextView dateTextView, contentTextView;
        public ProgressBar progressBar;
        public Guideline leftGuideline, rightGuideline, p75Guideline, p25Guideline;


        public ConversationMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            mainContainer = itemView.findViewById(R.id.entry_message_container);
            contentContainer = itemView.findViewById(R.id.entry_message_content_container);
            dateTextView = itemView.findViewById(R.id.entry_message_date);
            contentTextView = itemView.findViewById(R.id.entry_message_content_text);
            progressBar = itemView.findViewById(R.id.entry_message_content_progress);
            leftGuideline = itemView.findViewById(R.id.entry_message_guideline_left);
            rightGuideline = itemView.findViewById(R.id.entry_message_guideline_right);
            p75Guideline = itemView.findViewById(R.id.entry_message_guideline_75);
            p25Guideline = itemView.findViewById(R.id.entry_message_guideline_25);

            message = null;
        }

        public void synch() {
            if (message == null) {
                dateTextView.setVisibility(View.GONE);
                contentTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                dateTextView.setVisibility(View.VISIBLE);
                contentTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                boolean isFromOwner = message.authorID == SharedPreference.getUserID(context);
                if (isFromOwner) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.connect(R.id.entry_message_content_container, ConstraintSet.LEFT, R.id.entry_message_guideline_25, ConstraintSet.START, 0);
                    constraintSet.applyTo(mainContainer);
                    constraintSet = new ConstraintSet();
                    constraintSet.connect(R.id.entry_message_content_container, ConstraintSet.RIGHT, R.id.entry_message_guideline_right, ConstraintSet.START, 0);
                    constraintSet.applyTo(mainContainer);
                    constraintSet = new ConstraintSet();
                    constraintSet.setHorizontalBias(R.id.entry_message_content_text, 1);
                    constraintSet.applyTo(contentContainer);
                } else {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.connect(R.id.entry_message_content_container, ConstraintSet.LEFT, R.id.entry_message_guideline_left, ConstraintSet.START, 0);
                    constraintSet.applyTo(mainContainer);
                    constraintSet = new ConstraintSet();
                    constraintSet.connect(R.id.entry_message_content_container, ConstraintSet.RIGHT, R.id.entry_message_guideline_75, ConstraintSet.START, 0);
                    constraintSet.applyTo(mainContainer);
                    constraintSet.setHorizontalBias(R.id.entry_message_content_text, 0);
                    constraintSet.applyTo(contentContainer);
                }
                contentTextView.setText(message.message);
                dateTextView.setText(TimeUtil.getDateString(message.publishedTime));
            }
        }
    }
}
