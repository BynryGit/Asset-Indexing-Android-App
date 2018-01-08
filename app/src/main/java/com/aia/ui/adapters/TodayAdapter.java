package com.aia.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aia.R;
import com.aia.models.TodayModel;
import com.aia.utility.CommonUtility;

import java.util.ArrayList;

/**
 * Created by Piyush on 31-08-2017.
 * Bynry
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.TodayHolder>
{

    private Context mContext;
    private String screenName = "";
    private ArrayList<TodayModel> mTodayModelCards;

    public TodayAdapter(Context context, String screenName, ArrayList<TodayModel> todayModelCards)
    {
        this.mContext = context;
        this.screenName = screenName;
        this.mTodayModelCards = todayModelCards;
    }

    public TodayAdapter(){

    }

    @Override
    public TodayHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_today_card_layout, null);
        TodayAdapter.TodayHolder viewHolder = new TodayAdapter.TodayHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodayHolder holder, final int position)
    {
        CommonUtility.setAnimation(holder.itemView, position, -1, mContext);

        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
        {
            holder.txtFirst.setText(mTodayModelCards.get(position).getAsset_name());
            holder.txtSecond.setText(mTodayModelCards.get(position).getCategory());
            holder.txtThird.setText(mTodayModelCards.get(position).getArea());
            holder.txtFourth.setText(mTodayModelCards.get(position).getLocation());
        }
        else if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            holder.lblFirst.setText(R.string.asset_name);
            holder.lblSecond.setText(R.string.category);
            holder.lblThird.setText(R.string.area);
            holder.lblFourth.setText(R.string.location);

            holder.txtFirst.setText(mTodayModelCards.get(position).getAsset_name());
            holder.txtSecond.setText(mTodayModelCards.get(position).getCategory());
            holder.txtThird.setText(mTodayModelCards.get(position).getArea());
            holder.txtFourth.setText(mTodayModelCards.get(position).getLocation());
        }

        else if(screenName.equals(CommonUtility.getString(mContext, R.string.monitoring)))
        {
            holder.lblFirst.setText(R.string.asset_name);
            holder.lblSecond.setText(R.string.category);
            holder.lblThird.setText(R.string.area);
            holder.lblFourth.setText(R.string.location);

            holder.txtFirst.setText(mTodayModelCards.get(position).getAsset_name());
            holder.txtSecond.setText(mTodayModelCards.get(position).getCategory());
            holder.txtThird.setText(mTodayModelCards.get(position).getArea());
            holder.txtFourth.setText(mTodayModelCards.get(position).getLocation());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(mTodayModelCards != null && mTodayModelCards.size() > 0){
            return mTodayModelCards.size();
        }
        else{
            return 0;
        }
    }

    public class TodayHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView lblFirst, lblSecond, lblThird, lblFourth, txtFirst, txtSecond, txtThird, txtFourth;

        public TodayHolder(View itemView)
        {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);

            lblFirst = itemView.findViewById(R.id.lbl_first);
            lblSecond = itemView.findViewById(R.id.lbl_second);
            lblThird = itemView.findViewById(R.id.lbl_third);
            lblFourth = itemView.findViewById(R.id.lbl_fourth);

            txtFirst = itemView.findViewById(R.id.txt_first);
            txtSecond = itemView.findViewById(R.id.txt_second);
            txtThird = itemView.findViewById(R.id.txt_third);
            txtFourth = itemView.findViewById(R.id.txt_fourth);

            if(screenName.equals(CommonUtility.getString(mContext, R.string.reconnection)) ||
                    screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
            {
                lblFirst.setText(CommonUtility.getString(mContext, R.string.consumer_name));
                lblSecond.setText(CommonUtility.getString(mContext, R.string.binder));
                lblThird.setText(CommonUtility.getString(mContext, R.string.acc_no));
            }
            else if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
            {
                lblFirst.setText(R.string.asset_name);
                 lblSecond.setText(R.string.category);
                lblThird.setText(R.string.area);
                lblFourth.setText(R.string.location);

            }
            else if(screenName.equals(CommonUtility.getString(mContext, R.string.monitoring)))
            {
                lblThird.setText(CommonUtility.getString(mContext, R.string.monitoring_type));
            }
        }
    }
}
