package com.egrek.chidiebere.QuiPay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

import java.util.ArrayList;

/**
 * Created by chidiebere on 3/29/17.
 */

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PaymentHistory> mData;

    public PaymentHistoryAdapter(Context context, ArrayList<PaymentHistory> data){
        mContext = context;
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView merchantTextView, amountTextView, descriptionTextView, dateTextView;

        public ViewHolder(View itemView){
            super(itemView);

            merchantTextView = (TextView)itemView.findViewById(R.id.merchant);
            merchantTextView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_REGULAR));
            amountTextView = (TextView)itemView.findViewById(R.id.amount);
            amountTextView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_REGULAR));
            descriptionTextView = (TextView)itemView.findViewById(R.id.payment_desc);
            descriptionTextView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_LIGHT));
            dateTextView = (TextView)itemView.findViewById(R.id.payment_date);
            dateTextView.setTypeface(Typer.set(mContext).getFont(Font.ROBOTO_LIGHT));

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
        View view = inflater.inflate(R.layout.recent_activity_row,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentHistory paymentHistory = mData.get(position);
        holder.merchantTextView.setText(paymentHistory.getMerchant());
        holder.amountTextView.setText(paymentHistory.getAmount());
        holder.descriptionTextView.setText(paymentHistory.getDescription());
        holder.dateTextView.setText(paymentHistory.getDate());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
