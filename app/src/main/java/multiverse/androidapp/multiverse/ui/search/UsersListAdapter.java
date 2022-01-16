package multiverse.androidapp.multiverse.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.UserWebModel;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> {

    private Context context;
    private int layoutRessourceID;

    private SearchViewModel viewModel;
    private UsersClickListener clickListener;

    public UsersListAdapter(Context context, int layoutRessourceID, SearchViewModel viewModel, UsersClickListener clickListener) {
        this.context = context;
        this.layoutRessourceID = layoutRessourceID;

        this.viewModel = viewModel;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutRessourceID, parent, false);

        return new UserViewHolder(view, null);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if(viewModel.getUsers().getValue().size() > position) {
            UserModel user = viewModel.getUsers().getValue().get(position);

            holder.user = user;
            holder.syncToUser();
        } else {
            // Ask for more user
            viewModel.fetchMoreNameSearchResult(viewModel.getPageNumber(position));
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getTotalSize().getValue();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public UserModel user;

        public AppCompatTextView nameTextView;
        public ProgressBar progressBar;

        public UserViewHolder(@NonNull View itemView, UserModel user) {
            super(itemView);
            this.user = user;

            this.nameTextView = itemView.findViewById(R.id.entry_user_name);
            this.progressBar = itemView.findViewById(R.id.entry_user_loading);

            itemView.setOnClickListener(this);
        }

        public void syncToUser() {
            if(user == null) {
                progressBar.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                nameTextView.setText(user.firstname + " " + user.lastname);
            }
        }

        @Override
        public void onClick(View v) {
            if(user != null && clickListener != null) {
                clickListener.userClickListener(user);
            }
        }
    }
}
