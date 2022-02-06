package multiverse.androidapp.multiverse.ui.conversation.conversationSettings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.ui.community.CommunityClickListener;

public class ConversationSettingsUserListAdapter extends RecyclerView.Adapter<ConversationSettingsUserListAdapter.ConversationSettingsUserViewHolder> {

    private Context context;
    private ConversationSettingsViewModel viewModel;
    private CommunityClickListener clickListener;

    public ConversationSettingsUserListAdapter(Context context, ConversationSettingsViewModel viewModel, CommunityClickListener clickListener) {
        this.context = context;
        this.viewModel = viewModel;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ConversationSettingsUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_community, parent, false);

        return new ConversationSettingsUserViewHolder(view, null);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationSettingsUserViewHolder holder, int position) {
        if (viewModel.getUsersList().size() > position && viewModel.getUsersList().get(position) != null) {
            UserModel user = viewModel.getUsersList().get(position);
            holder.mUser = user;
            holder.synch();
        } else {
            holder.mUser = null;
            holder.synch();
            viewModel.loadUser(position);
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getTotalSize().getValue();
    }

    public class ConversationSettingsUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public UserModel mUser;

        public ConstraintLayout container;
        public ProgressBar progressBar;
        public AppCompatTextView nameTextView;
        public AppCompatImageButton addBtn, removeBtn;

        public ConversationSettingsUserViewHolder(@NonNull View itemView, final UserModel user) {
            super(itemView);

            this.mUser = user;

            container = itemView.findViewById(R.id.entry_community_container);
            progressBar = itemView.findViewById(R.id.entry_community_loading);
            nameTextView = itemView.findViewById(R.id.entry_community_name);
            addBtn = itemView.findViewById(R.id.entry_community_accept);
            removeBtn = itemView.findViewById(R.id.entry_community_delete);

            itemView.setOnClickListener(this);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser != null)
                        clickListener.acceptUserClickListener(mUser);
                }
            });
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser != null)
                        clickListener.refuseUserClickListener(mUser);
                }
            });
        }

        public void synch() {
            if (mUser == null) {
                progressBar.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.INVISIBLE);
                addBtn.setVisibility(View.INVISIBLE);
                removeBtn.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                nameTextView.setVisibility(View.VISIBLE);

                if (viewModel.isAnAddedUser(mUser.userID)) {
                    addBtn.setVisibility(View.GONE);
                    removeBtn.setVisibility(View.VISIBLE);
                    container.setBackgroundColor(Color.rgb(175, 209, 115));
                } else if (viewModel.isARemovedUser(mUser.userID)) {
                    addBtn.setVisibility(View.VISIBLE);
                    removeBtn.setVisibility(View.GONE);
                    container.setBackgroundColor(Color.rgb(209, 134, 115));
                } else if (viewModel.isUserInConversation(mUser.userID)) {
                    addBtn.setVisibility(View.GONE);
                    removeBtn.setVisibility(View.VISIBLE);
                    container.setBackgroundColor(Color.rgb(255, 255, 255));
                } else {
                    addBtn.setVisibility(View.VISIBLE);
                    removeBtn.setVisibility(View.GONE);
                    container.setBackgroundColor(Color.rgb(255, 255, 255));
                }

                nameTextView.setText(mUser.firstname + " " + mUser.lastname);
            }
        }

        @Override
        public void onClick(View v) {
            if (mUser != null)
                clickListener.userClickListener(mUser);
        }
    }
}
