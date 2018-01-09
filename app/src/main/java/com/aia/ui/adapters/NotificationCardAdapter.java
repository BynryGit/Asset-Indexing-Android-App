package com.aia.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ItemTouchHelperAdapter;
import com.aia.models.NotificationCard;
import com.aia.utility.App;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Piyush on 09-01-2018.
 * Bynry
 */
public class NotificationCardAdapter extends RecyclerView.Adapter<NotificationCardAdapter.NotificationCardHolder>
        implements ItemTouchHelperAdapter
{

    public Context mContext;
    private ArrayList<NotificationCard> mNotificationCard;
    private int lastPosition = -1;
    private Typeface bold, regular;

    public NotificationCardAdapter(Context context, ArrayList<NotificationCard> NotificationCards)
    {
        this.mContext = context;
        this.mNotificationCard = NotificationCards;
    }

    @Override
    public NotificationCardHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_notification_card, null);
        NotificationCardHolder viewHolder = new NotificationCardHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotificationCardHolder holder, final int position)
    {
        setAnimation(holder.itemView, position);
        final NotificationCard item = mNotificationCard.get(position);
        regular = App.getFontRegular();
        bold = App.getFontBold();

        holder.notificationTitle.setText(String.valueOf(item.title));
        holder.notificationMessage.setText(String.valueOf(item.message));
        holder.notificationDate.setText(String.valueOf(item.date));
    }

    @Override
    public int getItemCount()
    {
        if(mNotificationCard != null && mNotificationCard.size() > 0)
            return mNotificationCard.size();
        else
            return 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        Collections.swap(mNotificationCard, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position)
    {
        DatabaseManager.deleteAccount(mContext, mNotificationCard.get(position).message);
        mNotificationCard.remove(position);
        notifyItemRemoved(position);
    }

    public class NotificationCardHolder extends RecyclerView.ViewHolder
    {
        public TextView notificationMessage, notificationDate, notificationTitle;
        public LinearLayout card;

        public NotificationCardHolder(View itemView)
        {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.txt_notifications_title);
            notificationTitle.setTypeface(bold);
            notificationMessage = itemView.findViewById(R.id.txt_notifications);
            notificationMessage.setTypeface(regular);
            notificationDate = itemView.findViewById(R.id.txt_date);
            notificationDate.setTypeface(bold);
            card = itemView.findViewById(R.id.card);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}