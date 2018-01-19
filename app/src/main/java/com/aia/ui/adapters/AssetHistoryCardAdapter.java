package com.aia.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aia.R;
import com.aia.models.AssetHistoryCardModel;
import com.aia.utility.CommonUtility;

import java.util.ArrayList;

/**
 * Created by Piyush on 16-01-2018.
 * Bynry
 */
public class AssetHistoryCardAdapter extends RecyclerView.Adapter<AssetHistoryCardAdapter.AssetHistoryCardHolder>
{

    private Context mContext;
    private ArrayList<AssetHistoryCardModel> historyCardModels = new ArrayList<>();

    public AssetHistoryCardAdapter(Context context, ArrayList<AssetHistoryCardModel>historyModelArrayList)
    {
        this.mContext = context;
        this.historyCardModels = historyModelArrayList;
    }

    @Override
    public AssetHistoryCardAdapter.AssetHistoryCardHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_history_card_layout, null);
        AssetHistoryCardAdapter.AssetHistoryCardHolder viewHolder = new AssetHistoryCardAdapter.AssetHistoryCardHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AssetHistoryCardAdapter.AssetHistoryCardHolder holder, int position)
    {
        CommonUtility.setAnimation(holder.itemView, position, -1, mContext);

        holder.txtFirst.setText(historyCardModels.get(position).todayDate);
        holder.txtSecond.setText(historyCardModels.get(position).countOpen);
        holder.txtThird.setText(historyCardModels.get(position).countCompleted);
    }

    @Override
    public int getItemCount()
    {
        if(historyCardModels != null && historyCardModels.size() > 0){
            return historyCardModels.size();
        }
        else{
            return 0;
        }
    }

    public class AssetHistoryCardHolder extends RecyclerView.ViewHolder
    {
        TextView lblFirst, lblSecond, lblThird,  txtFirst, txtSecond, txtThird;

        public AssetHistoryCardHolder(View itemView)
        {
            super(itemView);
            lblFirst = itemView.findViewById(R.id.lbl_date);
            lblSecond = itemView.findViewById(R.id.lbl_assigned);
            lblThird = itemView.findViewById(R.id.lbl_completed);

            txtFirst = itemView.findViewById(R.id.txt_date);
            txtSecond = itemView.findViewById(R.id.txt_assigned);
            txtThird = itemView.findViewById(R.id.txt_completed);

        }
    }
}