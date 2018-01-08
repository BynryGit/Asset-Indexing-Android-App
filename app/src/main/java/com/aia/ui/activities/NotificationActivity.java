package com.aia.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aia.R;

public class NotificationActivity extends ParentActivity implements View.OnClickListener
{

    private Context mContext;
    private TextView txtTitle;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mContext = this;

        txtTitle = (TextView) findViewById(R.id.txt_name);
        txtTitle.setText(getString(R.string.notification));

        imgBack = (ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v == imgBack)
        {
            finish();
        }
    }
}
