package com.aia.services;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;

public class FireBaseIDService extends FirebaseInstanceIdService
{
    private Context mContext;

    @Override
    public void onTokenRefresh()
    {
        mContext = this;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//       Log.d("---FCM KEY---", "RefreshedToken: " + refreshedToken);
        AppPreferences.getInstance(mContext).putString(AppConstants.DEVICE_FCM_TOKEN, refreshedToken);
    }

}
