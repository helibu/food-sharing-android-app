package com.example.helislaptop.foodsharing.setting;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helislaptop.foodsharing.MainActivity;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodBasicFragment;
import com.example.helislaptop.foodsharing.foodList.FoodItemAdapter;
import com.example.helislaptop.foodsharing.foodList.foodPost.FoodPostFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends FoodBasicFragment {
    private static final int RC_SIGN_IN = 1;
    public Boolean signUpModeActive = true;
    public static boolean isLogIn = false;
    private TextView changeSignupModeTextView;
    private EditText passwordText;
    private EditText usernameText;
    private Button signUpButton;
    private Button logOutButton;
    private View settingView;
    public LayoutInflater inflater;
    public ViewGroup container;
    public Bundle bundle;
    //private TextView welcomeMessage;
    private TextView userNameProfileWord;
    private TextView phoneNumberProfileWord;
    private TextView userNameProfile;
    private TextView phoneNumberProfile;

    SignInButton googleSignInButton;
    GoogleSignInClient mGoogleSignInClient;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOutInBackground();
        } else if (mGoogleSignInClient != null && account != null) {
            mGoogleSignInClient.signOut();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;
        this.bundle = savedInstanceState;
        //if (!isLogIn) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            settingView = inflater.inflate(R.layout.fragment_setting, container, false);
            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
            googleSignInButton = settingView.findViewById(R.id.google_sign_in_button);
            googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
            googleSignInButton.setOnClickListener(v -> {
                googleSignIn();
            });
            userNameProfile = settingView.findViewById(R.id.user_name_profile);
            userNameProfileWord = settingView.findViewById(R.id.profile_word1);

            userNameProfile.setVisibility(View.GONE);
            userNameProfileWord.setVisibility(View.GONE);
            phoneNumberProfile = settingView.findViewById(R.id.phone_number_profile);

            phoneNumberProfile.setVisibility(View.GONE);
            phoneNumberProfileWord = settingView.findViewById(R.id.profile_word2);

            phoneNumberProfileWord.setVisibility(View.GONE);
            logOutButton = settingView.findViewById(R.id.logoutButton);
            logOutButton.setVisibility(View.GONE);
            logOutButton.setOnClickListener(v -> {
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.logOut();
                } else {
                    googleSignOut();
                }
                showLogIn();
            });
            phoneNumberProfile.setVisibility(View.GONE);
            changeSignupModeTextView = (TextView) settingView.findViewById(R.id.changeSignupModeTextView);
            changeSignupModeTextView.setOnClickListener(this::onClick);
            RelativeLayout backgroundRelativeLayout = (RelativeLayout) settingView.findViewById(R.id.backgroundRelativeLayout);
            ImageView logoImageView =  settingView.findViewById(R.id.logoImageView);
            backgroundRelativeLayout.setOnClickListener(this::onClick);
            logoImageView.setOnClickListener(this::onClick);
            usernameText = (EditText) settingView.findViewById(R.id.usernameEditText);
            passwordText = (EditText) settingView.findViewById(R.id.passwordEditText);
            passwordText.setOnKeyListener(this::onKey);
            signUpButton = settingView.findViewById(R.id.signupButton);
            signUpButton.setOnClickListener(v -> signUp());

            //ParseUser.logOutInBackground();
            //ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());

        //}
        /*else {
            settingView = inflater.inflate(R.layout.user_info, container, false);
        }*/
        return settingView;
    }

    private void googleSignOut() {
        mGoogleSignInClient.signOut();
    }

    private void showLogIn() {
        ParseUser.logOutInBackground();
        isLogIn = false;
        userNameProfile.setVisibility(View.GONE);
        phoneNumberProfile.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
        userNameProfileWord.setVisibility(View.GONE);
        phoneNumberProfileWord.setVisibility(View.GONE);
        signUpButton.setVisibility(View.VISIBLE);
        googleSignInButton.setVisibility(View.VISIBLE);
        changeSignupModeTextView.setVisibility(View.VISIBLE);
        passwordText.setVisibility(View.VISIBLE);
        usernameText.setVisibility(View.VISIBLE);


    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            String userName = account.getDisplayName();
            String email = account.getEmail();
            isLogIn = true;
            signUpButton.setVisibility(View.GONE);
            googleSignInButton.setVisibility(View.GONE);
            changeSignupModeTextView.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            usernameText.setVisibility(View.GONE);
            userNameProfile.setText(userName);
            userNameProfile.setVisibility(View.VISIBLE);
            phoneNumberProfile.setText(email);
            phoneNumberProfile.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.VISIBLE);
            userNameProfileWord.setVisibility(View.VISIBLE);
            phoneNumberProfileWord.setVisibility(View.VISIBLE);
        }
    }




    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp();
        }
        return false;
    }
    public void signUp() {


        if (usernameText.getText().toString().matches("") || passwordText.getText().toString().matches("")) {
            Toast.makeText(getContext(), "A username and password are required.", Toast.LENGTH_SHORT).show();
        } else {

            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameText.getText().toString());
                user.setPassword(passwordText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup", "Successful");
                            showUserInfo();
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Signup", "Login successful");
                            showUserInfo();
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void showUserInfo() {
        isLogIn = true;
        signUpButton.setVisibility(View.GONE);
        googleSignInButton.setVisibility(View.GONE);
        changeSignupModeTextView.setVisibility(View.GONE);
        passwordText.setVisibility(View.GONE);
        usernameText.setVisibility(View.GONE);
        userNameProfile.setText(ParseUser.getCurrentUser().getUsername());
        userNameProfile.setVisibility(View.VISIBLE);
        phoneNumberProfile.setText(ParseUser.getCurrentUser().getEmail());
        phoneNumberProfile.setVisibility(View.VISIBLE);
        logOutButton.setVisibility(View.VISIBLE);
        userNameProfileWord.setVisibility(View.VISIBLE);
        phoneNumberProfileWord.setVisibility(View.VISIBLE);
        //settingView.setVisibility(View.VISIBLE);

    }
    public void onClick(View view) {

        if (view.getId() == R.id.changeSignupModeTextView) {
            Button signupButton = (Button) settingView.findViewById(R.id.signupButton);
            if (signUpModeActive) {
                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Or, Signup");
            } else {
                signUpModeActive = true;
                signupButton.setText("Signup");
                changeSignupModeTextView.setText("Or, Login");
            }
        } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView) {
            //locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            InputMethodManager inputMethodManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            }
            //inputMethodManager.hideSoftInputFromWindow(settingView.getWindowToken(), 0);


        }

    }
}
