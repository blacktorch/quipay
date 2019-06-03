package com.egrek.chidiebere.QuiPay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

import java.util.ArrayList;

/**
 * Created by chidiebere on 4/1/17.
 */

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CreditCard> mData;

    public CreditCardAdapter(Context context, ArrayList<CreditCard> data){
        mContext = context;
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView cardTypeLogoView;
        public TextView cardTypeView;
        public TextView cardNumberView;

        public ViewHolder(View itemView){
            super(itemView);
            cardTypeLogoView = (ImageView)itemView.findViewById(R.id.card_type_logo);
            cardTypeView = (TextView)itemView.findViewById(R.id.card_type);
            cardTypeView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_LIGHT));
            cardNumberView = (TextView)itemView.findViewById(R.id.card_number);
            cardNumberView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_THIN));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.credit_card_row,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CreditCard creditCard = mData.get(position);
        int resId = -1;
        switch (creditCard.getCardType()){
            case MASTERCARD:
                resId = mContext.getResources().getIdentifier("mastercard_logo","drawable",mContext.getPackageName());
                holder.cardTypeView.setText("Mastercard");
                break;
            case VISA:
                resId = mContext.getResources().getIdentifier("visa_logo","drawable",mContext.getPackageName());
                holder.cardTypeView.setText("Visa");
                break;
            case VERVE:
                resId = mContext.getResources().getIdentifier("verve_logo","drawable",mContext.getPackageName());
                holder.cardTypeView.setText("Verve");
                break;
        }

        holder.cardTypeLogoView.setImageResource(resId);
        holder.cardNumberView.setText("Credit (*** " + retrieveCardLastFourDigits(creditCard.getCardNumber()) + ")");


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String retrieveCardLastFourDigits(String cardNumber){
        return cardNumber.substring(cardNumber.length() - 4);

    }
}
