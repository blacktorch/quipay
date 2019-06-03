package com.egrek.chidiebere.QuiPay;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView profileFirstName;
    TextView profileLastName;
    TextView profileEmail;

    User user;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    boolean isUserLoggedIn = true;

    final Context dialogContext = this;

    //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private static final String PROFILE_IMAGE_URL = "URL";
    private static final String PROFILE_IMAGE_URL_PREF = "URL_PREF";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupWindowAnimations();

        sharedPreferences = getSharedPreferences(PROFILE_IMAGE_URL_PREF, MODE_PRIVATE);
        String imageURL = sharedPreferences.getString(PROFILE_IMAGE_URL, null);
        boolean urlChanged = sharedPreferences.getBoolean("IS_CHANGED", false);

        //user authentication instance and listener
        mAuth = FirebaseAuth.getInstance();

        user = (User) getIntent().getSerializableExtra("USER");

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar()

        profileFirstName = (TextView)findViewById(R.id.profile_first_name);
        profileLastName = (TextView)findViewById(R.id.profile_last_name);
        profileEmail = (TextView)findViewById(R.id.profile_email);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);

        profileFirstName.setTypeface(Typer.set(getApplicationContext()).getFont(Font.ROBOTO_LIGHT));
        profileLastName.setTypeface(Typer.set(getApplicationContext()).getFont(Font.ROBOTO_LIGHT));
        profileEmail.setTypeface(Typer.set(getApplicationContext()).getFont(Font.ROBOTO_THIN));

        //load profile information

        if (imageURL != null && urlChanged){
            Picasso.with(SettingsActivity.this)
                    .load(user.getProfileImage())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImage);
        }
        else if (!user.getProfileImage().equals("null")){
            Picasso.with(SettingsActivity.this)
                    .load(user.getProfileImage())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImage);
        }
        profileFirstName.setText(user.getFirstName());
        profileLastName.setText(user.getLastName());
        profileEmail.setText(user.getEmail());


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    getSupportActionBar().setTitle(profileFirstName.getText() + " " + profileLastName.getText());
                }
                else
                {
                    //Expanded
                    getSupportActionBar().setTitle("");
                }
            }
        });

        //Settings Item selections

        RelativeLayout creditCard = (RelativeLayout)findViewById(R.id.credit_card_row);
        RelativeLayout loginAndSecurity = (RelativeLayout)findViewById(R.id.login_security_row);
        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), CreditCardActivity.class);
                startActivity(i);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPersonalInfoActivity(v);
            }
        });

        loginAndSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, LoginAndSecurityActivity.class);
                startActivity(i);
            }
        });

        //check if user is logged in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    isUserLoggedIn = true;

                } else {
                    isUserLoggedIn = false;
                    finish();

                }
            }
        };

    }

    public void openPersonalInfoActivity(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View, String>(profileImage, "profile_image");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this, pair);
            Intent i = new Intent(SettingsActivity.this, PersonalInformationActivity.class);
            i.putExtra("USER", user);
            startActivity(i, options.toBundle());
        }else {
            Intent i = new Intent(view.getContext(), PersonalInformationActivity.class);
            i.putExtra("USER", user);
            startActivity(i);
        }
    }

    public void logOutAlert(){

        final Dialog dialog = new Dialog(dialogContext);
        dialog.setContentView(R.layout.alert_dialog);

        LinearLayout dialogButtonOK = (LinearLayout)dialog.findViewById(R.id.dialog_ok);
        LinearLayout dialogButtonCancel = (LinearLayout)dialog.findViewById(R.id.dialog_cancel);
        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_main_text);
        dialogTitle.setTypeface(Typer.set(dialogContext).getFont(Font.ROBOTO_REGULAR));
        TextView dialogSubTitle = (TextView)dialog.findViewById(R.id.dialog_sub_text);
        dialogSubTitle.setTypeface(Typer.set(dialogContext).getFont(Font.ROBOTO_LIGHT));

        TextView dialogOkText = (TextView)dialog.findViewById(R.id.dialog_ok_text);
        dialogOkText.setTypeface(Typer.set(dialogContext).getFont(Font.ROBOTO_LIGHT));

        TextView dialogCancelText = (TextView)dialog.findViewById(R.id.dialog_cancel_text);
        dialogCancelText.setTypeface(Typer.set(dialogContext).getFont(Font.ROBOTO_LIGHT));

        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PROFILE_IMAGE_URL, null);
                editor.putBoolean("IS_CHANGED", false);
                editor.commit();
                mAuth.signOut();
                finish();
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        //MenuItem searchItem = menu.findItem(R.id.main_search_item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        } else if(id == R.id.log_out_item){
            logOutAlert();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
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
    protected void onRestart() {
        super.onRestart();

        //Check if profile image was changed
        String imageURL = sharedPreferences.getString(PROFILE_IMAGE_URL, null);
        boolean urlChanged = sharedPreferences.getBoolean("IS_CHANGED", false);

        // if it was changed update image view
        if (imageURL != null && urlChanged){
            user.setProfileImage(imageURL);
            Picasso.with(SettingsActivity.this)
                    .load(imageURL)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImage);
        }

    }

    private void setupWindowAnimations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Slide enterTransition = new Slide();
            enterTransition.setDuration(400);
            getWindow().setEnterTransition(enterTransition);
            getWindow().setAllowEnterTransitionOverlap(false);
        }

    }

}
