package com.egrek.chidiebere.QuiPay;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.internal.zzbne;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText phoneNumber;
    TextView loginLink;
    RelativeLayout signUpButton;
    ProgressBar progressBarSignUp;
    TextView signUpText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String phoneNumberText;
    String userId;
    boolean isUserLoggedIn = false;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("user");
    //DatabaseReference addressRef = mRootRef.child("address");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //user authentication instance and listener
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar)findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hello!");

        firstName = (EditText)findViewById(R.id.input_first_name);
        lastName  = (EditText)findViewById(R.id.input_last_name);
        email = (EditText)findViewById(R.id.input_sign_up_email);
        password = (EditText)findViewById(R.id.input_sign_up_password);
        phoneNumber = (EditText)findViewById(R.id.input_phone_number);
        loginLink = (TextView)findViewById(R.id.link_login);
        signUpButton = (RelativeLayout)findViewById(R.id.btn_sign_up);
        progressBarSignUp = (ProgressBar)findViewById(R.id.progressBar_sign_up);
        signUpText = (TextView)findViewById(R.id.sign_up_text);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpText.setVisibility(View.INVISIBLE);
                progressBarSignUp.setVisibility(View.VISIBLE);
                try {
                    if (isSignUpParametersValid()){
                        signUpUser(email.getText().toString(), password.getText().toString());
                    }else{
                        signUpText.setVisibility(View.VISIBLE);
                        progressBarSignUp.setVisibility(View.INVISIBLE);
                    }
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public boolean isSignUpParametersValid() throws NumberParseException {
        boolean valid = true;

        String firstNameText = firstName.getText().toString();
        String lastNameText = lastName.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        phoneNumberText = phoneNumber.getText().toString();

        if (firstNameText.isEmpty() || firstNameText.length() < 3) {
            firstName.setError("at least 3 characters");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (lastNameText.isEmpty() || lastNameText.length() < 3) {
            lastName.setError("at least 3 characters");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (emailText.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordText.isEmpty() || passwordText.length() < 8 || passwordText.length() > 16) {
            password.setError("between 8 and 16 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (phoneNumberText.isEmpty() || !Patterns.PHONE.matcher(phoneNumberText).matches()) {
            phoneNumber.setError("enter a valid phone number");
            valid = false;
        } else {
            phoneNumber.setError(null);
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumberText,"NG");
            String pnE164 = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);
            phoneNumberText = pnE164;
            //Toast.makeText(this, formatPhoneNumber(phoneNumberText),Toast.LENGTH_LONG ).show();

        }

        return valid;
    }

    private void signUpUser(String mEmail, final String mPassword){

        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign up failed, please check credentials",
                                    Toast.LENGTH_LONG).show();
                        }else {

                            // create reference to database user and phone number
                            DatabaseReference uidRef = userRef.child(mAuth.getCurrentUser().getUid());
                            DatabaseReference phoneNumberRef = mRootRef.child("phonenumber").child(mAuth.getCurrentUser().getUid());
                            DatabaseReference addressRef = mRootRef.child("address").child(mAuth.getCurrentUser().getUid());

                            //create new user
                            User user = new User(firstName.getText().toString(), lastName.getText().toString(),
                                    email.getText().toString(), password.getText().toString(), mAuth.getCurrentUser().getUid(),
                                    mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid(), "null");

                            PhoneNumber number = new PhoneNumber(phoneNumberText,
                                    0, 1, mAuth.getCurrentUser().getUid());

                            Address address = new Address("null","null","null","null","null","null",0);

                            //save user data to database
                            uidRef.setValue(user);
                            phoneNumberRef.setValue(number);
                            addressRef.setValue(address);

                            //Start Main Activity
                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            i.putExtra("USER", user);
                            i.putExtra("PHONE_NUMBER", number);
                            i.putExtra("ADDRESS", address);
                            startActivity(i);
                            finish();

                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }
}
