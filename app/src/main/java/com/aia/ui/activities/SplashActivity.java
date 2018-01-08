package com.aia.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aia.R;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;

public class SplashActivity extends ParentActivity
{

    private Context mContext;
    private final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                callNextScreen();
            }
        }, SPLASH_TIME_OUT);
    }

    private void callNextScreen()
    {
        String empId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");
        Intent intent = null;
        if(empId.equals("") || empId == null)
        {
            intent = new Intent(mContext, LoginActivity.class);
        }
        else
        {
            intent = new Intent(mContext, LandingActivity.class);
        }

        startActivity(intent);
        this.finish();
    }
}