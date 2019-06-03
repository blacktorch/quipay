package com.egrek.chidiebere.QuiPay;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

import java.util.ArrayList;

public class CreditCardActivity extends AppCompatActivity {

    ArrayList<CreditCard> creditCards;
    CoordinatorLayout mainLayout;
    Animation appbarHide, appbarShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        animateViews();
        mainLayout = (CoordinatorLayout)findViewById(R.id.main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        appbarHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appbar_hide);
        appbarShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appbar_show);
        //collapsingToolbarLayout.setTitle("Credit Cards");
        collapsingToolbarLayout.setExpandedTitleTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));

        final TextView cc_title = (TextView)findViewById(R.id.cc_title);
        cc_title.setTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));

        final TextView cc_mgs = (TextView)findViewById(R.id.cc_desc);
        cc_mgs.setTypeface(Typer.set(this).getFont(Font.ROBOTO_LIGHT));

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int centerX = (view.getLeft() + view.getRight())/2;
                int centerY = (view.getTop() + view.getBottom())/2;

                float radius = Math.max(mainLayout.getWidth(), mainLayout.getHeight()) * 2.0f;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewAnimationUtils.createCircularReveal(mainLayout, centerX, centerY, 0, radius).start();
                }

                Intent i = new Intent(view.getContext(), ChooseCardTypeActivity.class);
                startActivity(i);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    getSupportActionBar().setTitle("Credit Cards");

                }
                else if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() <= -100 &&
                         Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() >= -230)
                {
                    //Expanding
                    if (cc_title.getVisibility() == View.VISIBLE && cc_mgs.getVisibility() == View.VISIBLE){
                        cc_title.startAnimation(appbarHide);
                        cc_mgs.startAnimation(appbarHide);
                        cc_title.setVisibility(View.INVISIBLE);
                        cc_mgs.setVisibility(View.INVISIBLE);
                    }
                    getSupportActionBar().setTitle("");

                }
                else if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() <= 0 &&
                         Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() >= -99){
                    getSupportActionBar().setTitle("Credit Cards");

                }
                else {
                    //Expanded
                    if (cc_title.getVisibility() == View.INVISIBLE && cc_mgs.getVisibility() == View.INVISIBLE){
                        cc_title.startAnimation(appbarShow);
                        cc_mgs.startAnimation(appbarShow);
                        cc_title.setVisibility(View.VISIBLE);
                        cc_mgs.setVisibility(View.VISIBLE);
                    }
                    getSupportActionBar().setTitle("");
                }
            }
        });

        creditCards = CreditCard.defaultData();

        RecyclerView recyclerView =(RecyclerView)findViewById(R.id.credit_card_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CreditCardAdapter adapter = new CreditCardAdapter(getApplicationContext(), creditCards);

        recyclerView.setAdapter(adapter);

    }

    private void animateViews(){
        YoYo.with(Techniques.FadeInRight)
                .duration(1000)
                .playOn(findViewById(R.id.main_content));
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
