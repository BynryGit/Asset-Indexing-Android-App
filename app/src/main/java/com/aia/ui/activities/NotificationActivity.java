package com.aia.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ItemTouchHelperAdapter;
import com.aia.interfaces.OnStartDragListener;
import com.aia.interfaces.SimpleItemTouchHelperCallback;
import com.aia.ui.adapters.NotificationCardAdapter;

public class NotificationActivity extends Activity implements View.OnClickListener, OnStartDragListener
{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private TextView title;
    private ImageView imgBack;
    private ItemTouchHelper mItemTouchHelper;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = this;
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        title = findViewById(R.id.txt_title);
        title.setText(getString(R.string.notification));
        title.setOnClickListener(this);

        loadRecyclerView();
    }

    private void loadRecyclerView()
    {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        NotificationCardAdapter adapter = new NotificationCardAdapter(mContext, DatabaseManager.getAllNotification(mContext));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v)
    {
        if (v == imgBack)
        {
            finish();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder)
    {
        mItemTouchHelper.startDrag(viewHolder);
    }
}