package com.egrek.chidiebere.QuiPay;

import android.*;
import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    User user;
    PhoneNumber phoneNumber;
    Address address;

    final Context dialogContext = this;

    CircleImageView profileImage;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("user");

    boolean isUserLoggedIn = true;

    ArrayList<PaymentHistory> paymentHistories;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    public static final int GALLERY_REQUEST_READ = 300;
    public static final int GALLERY_REQUEST_WRITE = 400;

    private static final String PROFILE_IMAGE_URL = "URL";
    private static final String PROFILE_IMAGE_URL_PREF = "URL_PREF";

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET}, PERMISSION_REQUEST);
            }
        }

        sharedPreferences = getSharedPreferences(PROFILE_IMAGE_URL_PREF, MODE_PRIVATE);

        //get user Id from intent
        user = (User) getIntent().getExtras().getSerializable("USER");

        //user authentication instance and listener
        mAuth = FirebaseAuth.getInstance();

        setupWindowAnimations();

        //load profile image
        profileImage = (CircleImageView)findViewById(R.id.main_profile_image);
        if (!user.getProfileImage().equals("null")){
            Picasso.with(MainActivity.this)
                    .load(user.getProfileImage())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImage);
        }


        RelativeLayout settingsToolbar = (RelativeLayout)findViewById(R.id.toolbar);


        TextView viewAllText = (TextView)findViewById(R.id.view_all);
        viewAllText.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        paymentHistories = PaymentHistory.defaultData();

        RecyclerView recyclerView =(RecyclerView)findViewById(R.id.recent_activity_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(getApplicationContext(), paymentHistories);

        recyclerView.setAdapter(adapter);

        ImageView scanQR = (ImageView)findViewById(R.id.scan_image);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        settingsToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                    Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                    i.putExtra("USER", user);
                    startActivity(i, options.toBundle());
                    //finish();
                }else{
                    Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                    i.putExtra("USER", user);
                    startActivity(i);
                    //finish();
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                Toast.makeText(getApplicationContext(), barcode.displayValue,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void logOutAlert(){
        final Dialog dialog = new Dialog(dialogContext);
        dialog.setContentView(R.layout.alert_dialog);

        LinearLayout dialogButtonOK =  dialog.findViewById(R.id.dialog_ok);
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


    private void setupWindowAnimations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                // Re-enter transition is executed when returning back to this activity
                Slide slideTransition = new Slide();
                slideTransition.setSlideEdge(Gravity.LEFT); // Use START if using right - to - left locale
                slideTransition.setDuration(400);

                getWindow().setReenterTransition(slideTransition);  // When MainActivity Re-enter the Screen
                getWindow().setExitTransition(slideTransition);     // When MainActivity Exits the Screen

                // For overlap of Re Entering Activity - MainActivity.java and Exiting TransitionActivity.java
                getWindow().setAllowReturnTransitionOverlap(false);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        String imageURL = sharedPreferences.getString(PROFILE_IMAGE_URL, null);
        boolean urlChanged = sharedPreferences.getBoolean("IS_CHANGED", false);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (imageURL != null && urlChanged){
            userRef.child(mAuth.getCurrentUser().getUid()).child("profileImage").setValue(imageURL);
            user.setProfileImage(imageURL);
            Picasso.with(MainActivity.this)
                    .load(imageURL)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImage);
            editor.putString(PROFILE_IMAGE_URL, null);
            editor.putBoolean("IS_CHANGED", false);
            editor.commit();
        }

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
        logOutAlert();

    }
}
