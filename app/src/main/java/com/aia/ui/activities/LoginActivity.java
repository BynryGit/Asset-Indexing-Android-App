package com.aia.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.models.UserProfileModel;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;
import com.aia.webservices.ApiConstants;
import com.aia.webservices.JsonResponse;
import com.aia.webservices.WebRequest;

import org.json.JSONObject;

public class LoginActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{
    private Context mContext;
    private EditText edtID, edtPassword;
    private Button btnLogin;
    private String userId, userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        edtID = findViewById(R.id.edt_id);
        edtID.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnLogin)
        {
            isValidate();
        }
    }

    public void doLogin()
    {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        String imeiNumber = telephonyManager.getDeviceId();
        if (CommonUtility.getInstance(this).checkConnectivity(mContext))
        {
            showLoadingDialog();
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", userId);
                jsonObject.put("password", userPass);
                jsonObject.put("imei_no", imeiNumber);
                JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.LOGIN_URL,
                        ApiConstants.LOGIN, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.LOGIN);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_LONG).show();
        }
    }

    public void isValidate()
    {
        userId = edtID.getText().toString().trim();
        userPass = edtPassword.getText().toString();
        if(!TextUtils.isEmpty(userId)){
            if(!TextUtils.isEmpty(userPass))
            {
                if(userPass.length()>=6)
                   doLogin();
                else
                   edtPassword.setError(getString(R.string.password_should_have_at_least_characters));
            }
            else
                edtPassword.setError(getString(R.string.please_enter_password));
        }else {
            edtID.setError(getString(R.string.please_enter_user_id));
        }

    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.LOGIN: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.responsedata != null) {
                            try {
                                dismissLoadingDialog();
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.user_id = jsonResponse.responsedata.getUser_id();
                                userProfileModel.emp_id = jsonResponse.responsedata.getEmp_id();
                                userProfileModel.user_name = jsonResponse.responsedata.getUser_name();
                                userProfileModel.city = jsonResponse.responsedata.getCity();
                                userProfileModel.contact_no = jsonResponse.responsedata.getContact_no();
                                userProfileModel.address = jsonResponse.responsedata.getAddress();
                                userProfileModel.emp_type = jsonResponse.responsedata.getEmp_type();
                                userProfileModel.email_id = jsonResponse.responsedata.getEmail_id();
                                DatabaseManager.saveLoginDetails(mContext, userProfileModel);
                                AppPreferences.getInstance(mContext).putString(AppConstants.AUTH_TOKEN, jsonResponse.responsedata.getToken());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_NAME, jsonResponse.responsedata.getUser_name());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_CITY, jsonResponse.responsedata.getCity());
                                AppPreferences.getInstance(mContext).putString(AppConstants.EMP_ID, jsonResponse.responsedata.getEmp_id());
                                AppPreferences.getInstance(mContext).putString(AppConstants.MOBILE_NO, jsonResponse.responsedata.getContact_no());
                                AppPreferences.getInstance(mContext).putString(AppConstants.PROFILE_IMAGE_URL, jsonResponse.responsedata.getProfile_image());

                                Intent intent = new Intent(mContext, LandingActivity.class);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {e.printStackTrace();}
                        }
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
            case ApiConstants.LOGIN: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
            case ApiConstants.LOGIN: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}
