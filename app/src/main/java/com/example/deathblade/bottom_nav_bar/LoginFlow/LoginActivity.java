package com.example.deathblade.bottom_nav_bar.LoginFlow;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deathblade.bottom_nav_bar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ImageView mProgressView;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextView mProgressTextView;
    private TextView mRemarksView;
    private View mLoginFormView;
    private AnimatedVectorDrawableCompat avd;


    private FirebaseAuth mAuth;

    private int RC_EMAIL_SIGN_IN = 2;

    private static final String EMAIL = "email";
    private static final String LOG_TAG = "LoginActivity";
    private static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private static final boolean ACTIVITY_EMAIL_CREATE = true;
    private Boolean isLoading = true;
    private static final boolean ACTIVITY_REGISTER = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeActionBar);
        setContentView(R.layout.activity_login);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (ImageView) findViewById(R.id.logo);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mRemarksView = (TextView) findViewById(R.id.remarks);

        Button mRegisterButton = (Button) findViewById(R.id.email_sign_up_btn);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_btn);

        mAuth = FirebaseAuth.getInstance();






        /**
         * Setting up new Email Registration.
         */
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra(ACTIVITY_MODE, ACTIVITY_EMAIL_CREATE);
                startActivityForResult(intent, RC_EMAIL_SIGN_IN);
            }
        });


        /**
         * Setting up Sign In with Email.
         */
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempSignIn();

            }
        });


    }


    private void attempSignIn() {
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        mRemarksView.setText("");


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (password.equals("")) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            showProgress(true);
            signInWithEmail(email, password);

        }

    }


    /**
     * Signs in with email
     *
     * @param email    Email ID
     * @param password Password
     */
    private void signInWithEmail(String email, String password) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showProgress(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String signInFailed = e.getLocalizedMessage();
                mRemarksView.setText(signInFailed);
            }
        });
    }


    /**
     * Shows the progress UI and hides the login form.
     *
     * @param show boolean dictating whether or not the progress animation should be shown.
     */
    private void showProgress(final boolean show) {
        avd = AnimatedVectorDrawableCompat.create(this, R.drawable.logo_loading_vector_white);
        mProgressView.setImageDrawable(avd);

        final Animatable animatable = (Animatable) mProgressView.getDrawable();
        if (show) {
            animatable.start();
            avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    if (show)
                        animatable.start();
                    else
                        animatable.stop();
                }
            });
        } else{
            animatable.stop();
            avd.clearAnimationCallbacks();

        }


    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /**
         *  For the email reg. result.
         */
        if (requestCode == RC_EMAIL_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();

            }

        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        avd.stop();
        avd.clearAnimationCallbacks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
