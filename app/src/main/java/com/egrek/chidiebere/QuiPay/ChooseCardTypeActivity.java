package com.egrek.chidiebere.QuiPay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class ChooseCardTypeActivity extends AppCompatActivity {

    public static final String CARD_TYPE_CODE = "cardtype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.choose_card_type_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choose card type");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout masterCard = (RelativeLayout)findViewById(R.id.mastercard);
        masterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardDetails("MASTERCARD");
            }
        });

        RelativeLayout visaCard = (RelativeLayout)findViewById(R.id.visa);
        visaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardDetails("VISA");
            }
        });

        RelativeLayout verveCard = (RelativeLayout)findViewById(R.id.verve);
        verveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardDetails("VERVE");
            }
        });
    }

    private void getCardDetails(String cardType){

        Intent intent = new Intent(getApplicationContext(), AddCreditCardActivity.class);
        intent.putExtra(CARD_TYPE_CODE, cardType);
        startActivity(intent);

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
