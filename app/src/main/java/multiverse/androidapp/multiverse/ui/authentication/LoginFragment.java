package multiverse.androidapp.multiverse.ui.authentication;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import multiverse.androidapp.multiverse.MainActivity;
import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;

public class LoginFragment extends Fragment {

    public static final String LOGIN_FRAGMENT_TAG = "login_fragment_tag";

    private LoginViewModel viewModel;

    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatButton submitButton;
    private AppCompatTextView registerTextView;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = root.findViewById(R.id.login_email);
        passwordEditText = root.findViewById(R.id.login_password);
        submitButton = root.findViewById(R.id.login_submit);
        registerTextView = root.findViewById(R.id.login_register);

        registerTextView.setPaintFlags(registerTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.auth_frag_container, RegisterFragment.newInstance(), RegisterFragment.REGISTER_FRAGMENT_TAG);
                transaction.commit();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the informations
                boolean isRegisterValid = true;

                if(emailEditText.getText().toString().isEmpty()) {
                    isRegisterValid = false;
                    emailEditText.setError(getContext().getString(R.string.register_emptyField));
                } else if(!emailEditText.getText().toString().contains("@")) {
                    isRegisterValid = false;
                    emailEditText.setError(getContext().getString(R.string.register_badEmail));
                } else {
                    emailEditText.setError(null);
                }
                if(passwordEditText.getText().toString().isEmpty()) {
                    isRegisterValid = false;
                    passwordEditText.setError(getContext().getString(R.string.register_emptyField));
                } else {
                    passwordEditText.setError(null);
                }

                if(isRegisterValid) {
                    // Send the form
                    viewModel.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        viewModel.getAuthenticationCode().observe(getViewLifecycleOwner(), new Observer<AuthenticationResponseRepositoryModel.AuthenticationCode>() {
            @Override
            public void onChanged(AuthenticationResponseRepositoryModel.AuthenticationCode authenticationCode) {
                if(authenticationCode != null) {
                    if(authenticationCode == AuthenticationResponseRepositoryModel.AuthenticationCode.OK) {
                        // Go back to main activity
                        Intent intent = MainActivity.newInstance(getContext());
                        startActivity(intent);
                        getActivity().finish();
                    } else if(authenticationCode == AuthenticationResponseRepositoryModel.AuthenticationCode.BAD_CREDENTIAL) {
                        Toast.makeText(getContext(), getString(R.string.login_badCredential), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.general_serverError), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
}
