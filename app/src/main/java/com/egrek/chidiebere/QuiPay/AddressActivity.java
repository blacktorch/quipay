package com.egrek.chidiebere.QuiPay;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

public class AddressActivity extends AppCompatActivity {

    CoordinatorLayout mainLayout;
    Animation appbarHide, appbarShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mainLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        animateViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appbarHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appbar_hide);
        appbarShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appbar_show);
        //collapsingToolbarLayout.setTitle("Credit Cards");
        collapsingToolbarLayout.setExpandedTitleTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));

        final TextView addy_title = (TextView) findViewById(R.id.addy_title);
        addy_title.setTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));

        final TextView addy_mgs = (TextView) findViewById(R.id.addy_desc);
        addy_mgs.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    getSupportActionBar().setTitle("Login and Security");

                } else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() <= -100 &&
                        Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() >= -230) {
                    //Expanding
                    if (addy_title.getVisibility() == View.VISIBLE && addy_mgs.getVisibility() == View.VISIBLE) {
                        addy_title.startAnimation(appbarHide);
                        addy_mgs.startAnimation(appbarHide);
                        addy_title.setVisibility(View.INVISIBLE);
                        addy_mgs.setVisibility(View.INVISIBLE);
                    }
                    getSupportActionBar().setTitle("");

                } else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() <= 0 &&
                        Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() >= -99) {
                    getSupportActionBar().setTitle("Login and Security");

                } else {
                    //Expanded
                    if (addy_title.getVisibility() == View.INVISIBLE && addy_mgs.getVisibility() == View.INVISIBLE) {
                        addy_title.startAnimation(appbarShow);
                        addy_mgs.startAnimation(appbarShow);
                        addy_title.setVisibility(View.VISIBLE);
                        addy_mgs.setVisibility(View.VISIBLE);
                    }
                    getSupportActionBar().setTitle("");
                }
            }
        });
    }

    private void animateViews(){
        YoYo.with(Techniques.FadeInDown)
                .duration(1000)
                .playOn(findViewById(R.id.main_content));
    }
}



