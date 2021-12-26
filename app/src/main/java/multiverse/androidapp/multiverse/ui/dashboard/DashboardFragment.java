package multiverse.androidapp.multiverse.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.UserLocalDbService;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    static int i = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        textView.setText("Initial text");
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        final Button addBtn = root.findViewById(R.id.addBtn);
        final Button showBtn = root.findViewById(R.id.showBtn);

//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserLocalDbService.Add(getContext(), i);
//                i++;
//            }
//        });
//        showBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> l = UserLocalDbService.Get(getContext());
//                String str = l.toString();
//
//                textView.setText(str);
//            }
//        });


        return root;
    }
}
