package com.egrek.chidiebere.QuiPay;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chidiebere.medusa.CardActivity;
import com.chidiebere.medusa.views.CreditCardView;

import io.card.payment.*;
import io.card.payment.CreditCard;

import static com.chidiebere.medusa.constants.CardType.MASTERCARD;
import static com.chidiebere.medusa.constants.CardType.VERVE;
import static com.chidiebere.medusa.constants.CardType.VISA;

public class AddCreditCardActivity extends AppCompatActivity {

    public static final int MY_SCAN_REQUEST_CODE = 5;
    CreditCardView creditCardView;
    CardType cardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);

        Toolbar toolbar = findViewById(R.id.add_card_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add your card");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        creditCardView = findViewById(R.id.card_view);

        String extraCardType = getIntent().getStringExtra("cardtype");

        if (extraCardType != null){
            if (extraCardType.equals("MASTERCARD")){
                cardType = CardType.MASTERCARD;
                creditCardView.setType(MASTERCARD);
            }
            else if (extraCardType.equals("VISA")){
                cardType = CardType.VISA;
                creditCardView.setType(VISA);
            }
            else if (extraCardType.equals("VERVE")){
                cardType = CardType.VERVE;
                creditCardView.setType(VERVE);
            }
        }

        ImageView cardLogo = findViewById(R.id.card_logo);

        switch (cardType){
            case MASTERCARD:
                cardLogo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),com.chidiebere.medusa.R.drawable.mastercard_logo));
                break;
            case VISA:
                cardLogo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),com.chidiebere.medusa.R.drawable.visa_logo));
                break;
            case VERVE:
                cardLogo.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),com.chidiebere.medusa.R.drawable.verve_logo));
                break;
        }



        LinearLayout captureCard = findViewById(R.id.capture_card);
        captureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(view.getContext(), CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON,false);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);


                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {

            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                creditCardView.setCardNumber(scanResult.getFormattedCardNumber());

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    creditCardView.setExpiryDate(scanResult.expiryMonth + "/" + scanResult.expiryYear);
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    creditCardView.setCvv(scanResult.cvv);

                }

                if (scanResult.postalCode != null) {

                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Scan was cancelled",Toast.LENGTH_SHORT).show();
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
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
