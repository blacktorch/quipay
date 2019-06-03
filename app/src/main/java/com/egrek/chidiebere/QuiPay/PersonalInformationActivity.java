package com.egrek.chidiebere.QuiPay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.nectec.android.imagetaker.controller.CropImageTaker;
import th.or.nectec.android.imagetaker.model.getter.GalleryImageGetter;

import static com.egrek.chidiebere.QuiPay.MainActivity.GALLERY_REQUEST_READ;
import static com.egrek.chidiebere.QuiPay.MainActivity.GALLERY_REQUEST_WRITE;
import static com.egrek.chidiebere.QuiPay.MainActivity.PERMISSION_REQUEST;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetListener {

    private static final String PROFILE_IMAGE_URL = "URL"; //PersonalInformationActivity.class.getSimpleName();
    private static final String PROFILE_IMAGE_URL_PREF = "URL_PREF";


    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    boolean isUserLoggedIn = true;
    boolean isAddressEmpty;

    ProgressBar progressBarProfile;

    User user;
    PhoneNumber phoneNumber;
    Address address;

    SharedPreferences sharedPreferences;

    CropImageTaker cropImageTaker = new CropImageTaker(this, new CropImageListener(PersonalInformationActivity.this));
    CircleImageView profileImageView;
    TextView changeProfileImageText, emailText, phoneNumberText;
    RelativeLayout phoneNumberRow, emailAddressRow, addressRow;

    //Empty address container
    TextView addAddress;
    ImageView addAddressImage;
    View addAddressDivider;

    //Main address container...
    TextView addressLine1, addressCity, addressState;
    ImageView navAddressMain;
    View addressDividerMain;



    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        user = (User)getIntent().getSerializableExtra("USER");

        sharedPreferences = getSharedPreferences(PROFILE_IMAGE_URL_PREF, MODE_PRIVATE);

        profileImageView = (CircleImageView)findViewById(R.id.personal_info_profile_image);
        profileImageView.setOnClickListener(this);
        changeProfileImageText = (TextView)findViewById(R.id.change_picture_text);
        changeProfileImageText.setOnClickListener(this);
        progressBarProfile = (ProgressBar)findViewById(R.id.progressBar_profile);
        emailText = (TextView)findViewById(R.id.email_address);
        phoneNumberText = (TextView)findViewById(R.id.phone_number);

        //Information rows initialization
        phoneNumberRow = (RelativeLayout)findViewById(R.id.phone_number_row);
        emailAddressRow = (RelativeLayout)findViewById(R.id.email_address_row);
        addressRow = (RelativeLayout)findViewById(R.id.address_row);

        //Empty address initialization
        addAddress = (TextView)findViewById(R.id.add_address_text);
        addAddressImage = (ImageView)findViewById(R.id.add_address_img);
        addAddressDivider = findViewById(R.id.divider_address_empty);

        //Main address initialization
        addressLine1 = (TextView)findViewById(R.id.address_line1);
        addressCity = (TextView)findViewById(R.id.address_city);
        addressState = (TextView)findViewById(R.id.address_state);
        navAddressMain = (ImageView)findViewById(R.id.nav_address_main);
        addressDividerMain = findViewById(R.id.divider_address_main);

        //user authentication instance and listener
        mAuth = FirebaseAuth.getInstance();



        if (!user.getProfileImage().equals("null")){
            Picasso.with(PersonalInformationActivity.this)
                    .load(user.getProfileImage())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(profileImageView);
        }

        if (user != null){
            emailText.setText(user.getEmail());
        }

        if (phoneNumber != null){
            phoneNumberText.setText(Utilities.formatPhoneNumber(phoneNumber.getPhoneNumber()));
        }

        if (address != null){
            isAddressEmpty();
            //Toast.makeText(this,"address is null",Toast.LENGTH_LONG).show();
        }


        animateViews();

        Toolbar toolbar = (Toolbar)findViewById(R.id.personal_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Information");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    //Override Methods

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_info_profile_image:
                new BottomSheet.Builder(this)
                        .setSheet(R.menu.menu_photo)
                        .setListener(this)
                        .show();
                break;
            case R.id.change_picture_text:
                new BottomSheet.Builder(this)
                        .setSheet(R.menu.menu_photo)
                        .setListener(this)
                        .show();
                break;

        }
    }

    @Override
    public void onSheetShown(@NonNull BottomSheet bottomSheet) {

    }

    @Override
    public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.camera:
                cropImageTaker.takeImage();
                break;
            case R.id.image:
                cropImageTaker.takeImageWith(new GalleryImageGetter());
                break;
        }

    }

    @Override
    public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropImageTaker.onResult(requestCode, resultCode, data);

    }


    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);

        DatabaseReference phoneNumberRef = mRootRef.child("phonenumber").child(mAuth.getCurrentUser().getUid());
        DatabaseReference addressRef = mRootRef.child("address").child(mAuth.getCurrentUser().getUid());

        phoneNumberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phoneNumber = dataSnapshot.getValue(PhoneNumber.class);
                //Toast.makeText(PersonalInformationActivity.this, phoneNumber.getPhoneNumber(),Toast.LENGTH_LONG).show();
                phoneNumberText.setText(Utilities.formatPhoneNumber(phoneNumber.getPhoneNumber()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address = dataSnapshot.getValue(Address.class);
                if (address != null){
                    isAddressEmpty();
                    //Toast.makeText(this,"address is null",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //isUserInfoObtained = false;

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Helper Methods

    public void updateImage(final Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String path = "profileimages/" + mAuth.getCurrentUser().getUid() + ".png";

        StorageReference profileImageRef = storage.getReference(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("text", mAuth.getCurrentUser().getEmail())
                .build();

        progressBarProfile.setVisibility(View.VISIBLE);
        profileImageView.setClickable(false);
        changeProfileImageText.setClickable(false);
        UploadTask uploadTask = profileImageRef.putBytes(data,metadata);

        uploadTask.addOnSuccessListener(PersonalInformationActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                profileImageView.setClickable(true);
                changeProfileImageText.setClickable(true);
                progressBarProfile.setVisibility(View.GONE);

                Uri url = taskSnapshot.getUploadSessionUri();

                editor.putString(PROFILE_IMAGE_URL, url.toString());
                editor.putBoolean("IS_CHANGED", true);
                editor.commit();
                user.setProfileImage(url.toString());

                profileImageView = (CircleImageView)findViewById(R.id.personal_info_profile_image);
                profileImageView.setImageBitmap(bitmap);

                Toast.makeText(getApplicationContext(), "Profile Image updated", Toast.LENGTH_SHORT).show();

            }
        });

        uploadTask.addOnFailureListener(PersonalInformationActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                profileImageView.setClickable(true);
                changeProfileImageText.setClickable(true);
                progressBarProfile.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Failed! Unable to update Profile Image", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void animateViews(){

        YoYo.with(Techniques.SlideInDown)
                .duration(1000)
                .playOn(findViewById(R.id.personal_info_profile_image));

        YoYo.with(Techniques.SlideInDown)
                .duration(1100)
                .playOn(findViewById(R.id.change_picture_text));

        YoYo.with(Techniques.SlideInDown)
                .duration(1200)
                .playOn(findViewById(R.id.phone_number_row));

        YoYo.with(Techniques.SlideInDown)
                .duration(1300)
                .playOn(findViewById(R.id.email_address_row));

        YoYo.with(Techniques.SlideInDown)
                .duration(1400)
                .playOn(findViewById(R.id.address_row));
    }

    private boolean isAddressEmpty(){

        if (address.getStreetAddress1().equals("null")){
            addAddress.setVisibility(View.VISIBLE);
            addAddressImage.setVisibility(View.VISIBLE);
            addAddressDivider.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.GONE);
            addressCity.setVisibility(View.GONE);
            addressState.setVisibility(View.GONE);
            navAddressMain.setVisibility(View.GONE);
            addressDividerMain.setVisibility(View.GONE);

            isAddressEmpty = true;
            return true;

        }
        else {
            addAddress.setVisibility(View.INVISIBLE);
            addAddressImage.setVisibility(View.INVISIBLE);
            addAddressDivider.setVisibility(View.INVISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            addressCity.setVisibility(View.VISIBLE);
            addressState.setVisibility(View.VISIBLE);
            navAddressMain.setVisibility(View.VISIBLE);
            addressDividerMain.setVisibility(View.VISIBLE);

            if (address.getStreetAddress2().equals("null")){
                addressLine1.setText(address.getStreetAddress1());
            }
            else {
                addressLine1.setText(address.getStreetAddress1() + " " + address.getStreetAddress2());
            }
            addressCity.setText(address.getCity() + " " + address.getZip());
            addressState.setText(address.getState());

            isAddressEmpty = false;
            return false;
        }
    }
}
