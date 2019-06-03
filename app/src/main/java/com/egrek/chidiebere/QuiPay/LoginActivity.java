package com.egrek.chidiebere.QuiPay;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import static com.egrek.chidiebere.QuiPay.MainActivity.GALLERY_REQUEST_READ;
import static com.egrek.chidiebere.QuiPay.MainActivity.GALLERY_REQUEST_WRITE;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout loginButton;
    ProgressBar loginProgress;
    TextView loginText;
    EditText emailInput;
    EditText passwordInput;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("user");

    String userId;
    boolean isUserLoggedIn = false;
    boolean isUserInfoObtained = false;

    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //code to setup UI parameters

        emailInput = (EditText)findViewById(R.id.input_email);
        emailInput.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        passwordInput = (EditText)findViewById(R.id.input_password);
        passwordInput.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        TextView forgotPassword = (TextView)findViewById(R.id.forgot_password);
        forgotPassword.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        TextView signUpLink = (TextView)findViewById(R.id.link_sign_up);
        signUpLink.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        loginButton = (RelativeLayout)findViewById(R.id.btn_login);

        loginProgress = (ProgressBar)findViewById(R.id.progressBar_login);

        loginText = (TextView)findViewById(R.id.login_text);
        loginText.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        //Check user input email and password to activate the login button

        //Email check
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (emailInput.getText().length() > 0 && passwordInput.getText().length() > 0 && isLoginParametersValid()){
                    loginButtonActive(true);
                }
                else {
                    loginButtonActive(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Password check
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (emailInput.getText().length() > 0 && passwordInput.getText().length() > 0){
                    loginButtonActive(true);
                }
                else {
                    loginButtonActive(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //user authentication instance and listener
        mAuth = FirebaseAuth.getInstance();

        //check if user is logged in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    isUserLoggedIn = true;
                    userId = user.getUid();
                } else {
                    isUserLoggedIn = false;
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginButton.isClickable() && isLoginParametersValid()){
                    loginText.setVisibility(View.INVISIBLE);
                    loginProgress.setVisibility(View.VISIBLE);
                    loginButton.setClickable(false);
                    loginUser(emailInput.getText().toString(), passwordInput.getText().toString());
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });


    }

    private boolean loginButtonActive(boolean isButtonActive){

        if (isButtonActive){
            loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            loginButton.setClickable(true);
            loginText.setTextColor(0xFFFFFFFF);
        }
        else {
            loginButton.setBackgroundColor(0xFFBBBBBB);
            loginButton.setClickable(false);
            loginText.setTextColor(0xFFCCCCCC);
        }

        return isButtonActive;
    }

    private void loginUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            loginProgress.setVisibility(View.INVISIBLE);
                            loginText.setVisibility(View.VISIBLE);
                            loginButton.setClickable(true);
                            Toast.makeText(LoginActivity.this, "Log in failed " + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        } else {

                           getProfileInfo();

                        }

                    }
                });
    }

    public boolean isLoginParametersValid() {
        boolean valid = true;


        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("enter a valid email address");
            valid = false;
        } else {
            emailInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            passwordInput.setError("between 8 and 16 alphanumeric characters");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        return valid;
    }

    public void getProfileInfo(){
        DatabaseReference uidRef = userRef.child(mAuth.getCurrentUser().getUid());

        //get user
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
               Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("USER", user);
                isUserInfoObtained = true;
                startActivity(i);
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(LoginActivity.this, "Log in failed! Couldn't obtain user profile", Toast.LENGTH_LONG).show();
                mAuth.signOut();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
