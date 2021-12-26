package multiverse.androidapp.multiverse.ui.authentication;

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

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.model.repositoryModel.authentication.AuthenticationResponseRepositoryModel;

public class RegisterFragment extends Fragment {

    private RegisterViewModel viewModel;

    private AppCompatEditText firstnameEditText;
    private AppCompatEditText lastnameEditText;
    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatEditText passwordRepeatEditText;
    private AppCompatButton submitButton;
    private AppCompatTextView loginTextView;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        firstnameEditText = root.findViewById(R.id.register_firstname);
        lastnameEditText = root.findViewById(R.id.register_lastname);
        emailEditText = root.findViewById(R.id.register_email);
        passwordEditText = root.findViewById(R.id.register_password);
        passwordRepeatEditText = root.findViewById(R.id.register_passwordRepeat);
        submitButton = root.findViewById(R.id.register_submit);
        loginTextView = root.findViewById(R.id.register_login);

        loginTextView.setPaintFlags(loginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.auth_frag_container, LoginFragment.newInstance());
                transaction.commit();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the informations
                boolean isRegisterValid = true;

                if(firstnameEditText.getText().toString().isEmpty()) {
                    isRegisterValid = false;
                    firstnameEditText.setError(getContext().getString(R.string.register_emptyField));
                } else {
                    firstnameEditText.setError(null);
                }
                if(lastnameEditText.getText().toString().isEmpty()) {
                    isRegisterValid = false;
                    lastnameEditText.setError(getContext().getString(R.string.register_emptyField));
                } else {
                    lastnameEditText.setError(null);
                }
                if(emailEditText.getText().toString().isEmpty()) {
                    isRegisterValid = false;
                    emailEditText.setError(getContext().getString(R.string.register_emptyField));
                } else if(!emailEditText.getText().toString().contains("@")) {
                    isRegisterValid = false;
                    emailEditText.setError(getContext().getString(R.string.register_badEmail));
                } else {
                    emailEditText.setError(null);
                }
                boolean isPassword1Empty = passwordEditText.getText().toString().isEmpty();
                boolean isPassword2Empty = passwordRepeatEditText.getText().toString().isEmpty();
                if(isPassword1Empty) {
                    isRegisterValid = false;
                    passwordEditText.setError(getContext().getString(R.string.register_emptyField));
                } else {
                    passwordEditText.setError(null);
                }
                if(isPassword2Empty) {
                    isRegisterValid = false;
                    passwordRepeatEditText.setError(getContext().getString(R.string.register_emptyField));
                } else {
                    passwordRepeatEditText.setError(null);
                }
                if(!isPassword1Empty && !isPassword2Empty) {
                    if(!passwordEditText.getText().toString().equals(passwordRepeatEditText.getText().toString())) {
                        isRegisterValid = false;
                        passwordRepeatEditText.setError(getContext().getString(R.string.register_badRepeatPassword));
                    }
                }

                if(isRegisterValid) {
                    // Send register form
                    viewModel.register(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            firstnameEditText.getText().toString(),
                            lastnameEditText.getText().toString());
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        viewModel.getAuthenticationCode().observe(getViewLifecycleOwner(), new Observer<AuthenticationResponseRepositoryModel.AuthenticationCode>() {
            @Override
            public void onChanged(AuthenticationResponseRepositoryModel.AuthenticationCode authenticationCode) {
                if(authenticationCode != null){
                    if(authenticationCode == AuthenticationResponseRepositoryModel.AuthenticationCode.OK) {
                        // Go to main app
                    } else if(authenticationCode == AuthenticationResponseRepositoryModel.AuthenticationCode.EMAIL_ALREADY_IN_USE) {
                        Toast.makeText(getContext(), getString(R.string.register_emailAlreadyExists), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.general_serverError), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
