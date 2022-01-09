package multiverse.androidapp.multiverse.ui.community;

import android.content.Context;
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

public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder> {

    private Context context;

    private CommunityType type;
    private CommunityViewModel viewModel;
    private CommunityClickListener clickListener;

    public CommunityListAdapter(Context context, CommunityType type, CommunityViewModel viewModel, CommunityClickListener clickListener) {
        this.context = context;

        this.type = type;
        this.viewModel = viewModel;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_community, parent, false);

        return new CommunityViewHolder(view, null);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        if (viewModel.getUsers().size() > position) {
            UserModel user = viewModel.getUsers().get(position);

            holder.mUser = user;
            holder.syncToUser();
        } else {
            viewModel.loadUser(position);
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getTotalSize().getValue();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public UserModel mUser;

        public ConstraintLayout container;
        public AppCompatTextView nameTextView;
        public ProgressBar progressBar;
        public AppCompatImageButton acceptImageView, refuseImageView;

        public CommunityViewHolder(@NonNull View itemView, UserModel user) {
            super(itemView);
            this.mUser = user;

            this.container = itemView.findViewById(R.id.entry_community_container);
            this.nameTextView = itemView.findViewById(R.id.entry_community_name);
            this.progressBar = itemView.findViewById(R.id.entry_community_loading);
            this.acceptImageView = itemView.findViewById(R.id.entry_community_accept);
            this.refuseImageView = itemView.findViewById(R.id.entry_community_delete);

            itemView.setOnClickListener(this);
            this.acceptImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.acceptUserClickListener(mUser);
                    }
                }
            });
            this.refuseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.refuseUserClickListener(mUser);
                    }
                }
            });
        }

        public void syncToUser() {
            if (mUser == null) {
                progressBar.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.INVISIBLE);
                acceptImageView.setVisibility(View.INVISIBLE);
                refuseImageView.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                nameTextView.setText(mUser.firstname + " " + mUser.lastname);

                if (type == CommunityType.FOLLOWER_LIST || type == CommunityType.FOLLOWED_LIST) {
                    acceptImageView.setVisibility(View.INVISIBLE);
                    refuseImageView.setVisibility(View.VISIBLE);
                } else if (type == CommunityType.FOLLOWER_REQUEST_TYPE) {
                    acceptImageView.setVisibility(View.VISIBLE);
                    refuseImageView.setVisibility(View.VISIBLE);
                } else if (type == CommunityType.FOLLOWED_REQUEST_TYPE) {
                    acceptImageView.setVisibility(View.INVISIBLE);
                    refuseImageView.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.userClickListener(mUser);
            }
        }
    }
}
