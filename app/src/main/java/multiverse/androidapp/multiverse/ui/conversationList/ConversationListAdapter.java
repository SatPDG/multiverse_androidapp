package multiverse.androidapp.multiverse.ui.conversationList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;
import multiverse.androidapp.multiverse.util.functions.TimeUtil;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder> {

    private Context context;

    private ConversationListViewModel viewModel;
    private ConversationListClickListener clickListener;

    public ConversationListAdapter(Context context, ConversationListViewModel viewModel, ConversationListClickListener clickListener) {
        this.context = context;

        this.viewModel = viewModel;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_conversation, parent, false);

        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        if (viewModel.getConversationList().size() > position && viewModel.getConversationList().get(position) != null) {
            ConversationModel model = viewModel.getConversationList().get(position);
            holder.conversation = model;
            holder.synch();
        } else {
            holder.conversation = null;
            holder.synch();
            viewModel.loadUserConversation(position);
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getTotalSize().getValue();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public TextView lastUpdateTextView;
        public ProgressBar progressBar;

        public ConversationModel conversation;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.conversation = null;

            nameTextView = itemView.findViewById(R.id.entry_conversation_name);
            lastUpdateTextView = itemView.findViewById(R.id.entry_conversation_lastupdate);
            progressBar = itemView.findViewById(R.id.entry_conversation_progress);
            itemView.setOnClickListener(this);
        }

        public void synch() {
            if (conversation == null) {
                progressBar.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.INVISIBLE);
                lastUpdateTextView.setVisibility(View.INVISIBLE);
            } else {
                nameTextView.setText(conversation.name);
                TimeUtil.TimeDiff timeDiff = TimeUtil.getTimeDiffFromNow(conversation.lastUpdate);
                lastUpdateTextView.setText(timeDiff.toSimpleGraphicString(context));

                progressBar.setVisibility(View.INVISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                lastUpdateTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if (conversation != null) {
                clickListener.conversationClickListener(conversation);
            }
        }
    }
}
