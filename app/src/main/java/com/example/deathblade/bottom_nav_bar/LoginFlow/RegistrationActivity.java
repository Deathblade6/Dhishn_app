package com.example.deathblade.bottom_nav_bar.LoginFlow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deathblade.bottom_nav_bar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mNameView;
    private EditText mPhoneView;
    private EditText mPasswordView;
    private EditText mInstituteView;
    private View mLoginFormView;
    private ImageView mProgressView;
    private TextView mProgressTextView;

    private FirebaseUser firebaseUser;

    private static final String LOG_TAG = "RegistrationActivity";
    private static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private RadioGroup mGenderGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeActionBar);
        setContentView(R.layout.activity_registration);

        // Set up the UI elements.
        mNameView = (EditText) findViewById(R.id.name);
        mPhoneView = (EditText) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (EditText) findViewById(R.id.email);
        mInstituteView = (EditText) findViewById(R.id.institute);
        mGenderGroup = (RadioGroup) findViewById(R.id.gender_group);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (ImageView) findViewById(R.id.login_progress);
        mProgressTextView = (TextView) findViewById(R.id.text_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegistration() {

        // Reset errors.
        mEmailView.setError(null);
        mNameView.setError(null);
        mPhoneView.setError(null);
        mInstituteView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        final String name = mNameView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String institute = mInstituteView.getText().toString();
        String password = mPasswordView.getText().toString();
        String gender = ((RadioButton) findViewById(mGenderGroup.getCheckedRadioButtonId())).getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //Check if name and phone number entered.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(institute)) {
            mInstituteView.setError(getString(R.string.error_field_required));
            focusView = mInstituteView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            final DhishnaUser dhishnaUser = new DhishnaUser(name, email, phone, gender, institute);


            createEmailAccount(password, dhishnaUser);

        }
    }




    /**
     * Function which handles creating a new account with emailID and password.
     *
     * @param password    Password.
     * @param dhishnaUser A user object which contains the details of the user.
     */
    private void createEmailAccount(String password, final DhishnaUser dhishnaUser) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();



        /**
         * A listener which listens for when the Firebase account has been created.
         * Pushes upon successful completion.
         */
        OnCompleteListener<AuthResult> accountCreationListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Firebase account has been successfully created.
                    firebaseUser = mAuth.getCurrentUser();

                    //Pushing to DB
                    mProgressTextView.setText("Creating account.");
                    pushToDBandExit(dhishnaUser);
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        };


        mAuth.createUserWithEmailAndPassword(dhishnaUser.getEmail(), password)
                .addOnCompleteListener(this, accountCreationListener);
        showProgress(true);
        mProgressTextView.setText("Creating account");

    }

    private void pushToDBandExit(DhishnaUser user) {
        FirebaseUser updatedUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("users");
        reference.child(updatedUser.getUid()).setValue(user);
        setResult(RESULT_OK);
        finish();
    }




    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressTextView.setVisibility(show ? View.VISIBLE : View.GONE);

        AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(this, R.drawable.logo_loading_vector_white);
        mProgressView.setImageDrawable(avd);
        final Animatable animatable = (Animatable) mProgressView.getDrawable();
        animatable.start();
        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (show)
                    animatable.start();

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }





}
