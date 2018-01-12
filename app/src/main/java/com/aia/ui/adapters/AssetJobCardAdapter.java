package com.aia.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aia.R;
import com.aia.models.AssetJobCardModel;
import com.aia.ui.activities.AssetDetailsActivity;
import com.aia.utility.AppConstants;
import com.aia.utility.CommonUtility;

import java.util.ArrayList;

/**
 * Created by Piyush on 31-08-2017.
 * Bynry
 */
public class AssetJobCardAdapter extends RecyclerView.Adapter<AssetJobCardAdapter.TodayHolder>
{

    private Context mContext;
    private ArrayList<AssetJobCardModel> todayModelArrayList;

    public AssetJobCardAdapter(Context context, ArrayList<AssetJobCardModel> todayModelCards)
    {
        this.mContext = context;
        this.todayModelArrayList = todayModelCards;
    }

    @Override
    public TodayHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_today_card_layout, null);
        AssetJobCardAdapter.TodayHolder viewHolder = new AssetJobCardAdapter.TodayHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodayHolder holder, final int position)
    {
        CommonUtility.setAnimation(holder.itemView, position, AppConstants.RESULT_OK, mContext);

        /*holder.lblFirst.setText(R.string.asset_name);
        holder.lblSecond.setText(R.string.category);
        holder.lblThird.setText(R.string.make_no);
        holder.lblFourth.setText(R.string.location);*/

        holder.txtFirst.setText(todayModelArrayList.get(position).getAssetName());
        holder.txtSecond.setText(todayModelArrayList.get(position).getAssetCategory());
        holder.txtThird.setText(todayModelArrayList.get(position).getAssetMakeNo());
        holder.txtFourth.setText(todayModelArrayList.get(position).getAssetLocation());

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, AssetDetailsActivity.class);
                intent.putExtra(AppConstants.JOB_CARD_ID, todayModelArrayList.get(position).assetCardId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(todayModelArrayList != null && todayModelArrayList.size() > 0){
            return todayModelArrayList.size();
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
        }
    }
}
