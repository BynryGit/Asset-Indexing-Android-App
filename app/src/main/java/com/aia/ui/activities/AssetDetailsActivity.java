package com.aia.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.aia.R;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.webservices.JsonResponse;
import com.android.volley.NetworkResponse;

public class AssetDetailsActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{
    private Context mContext;
    private Typeface fontRegular, fontBold, fontItalic;

    private String jobCardId = "";
    private TextView txtTitle, txtAssetNameNo;
    private ImageView imgBack, imgViewDetails, imgDefaultParameter, imgCamera, cameraImage;
    private Spinner spinnerSubDivision, spinnerArea, spinnerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_details);
        mContext = this;

        fontRegular = App.getFontRegular(); fontBold = App.getFontBold(); fontItalic = App.getFontItalic();

        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setTypeface(fontRegular);
        txtTitle.setText(getString(R.string.asset_details));
        txtAssetNameNo = findViewById(R.id.txt_asset_name_no);
        txtAssetNameNo.setTypeface(fontRegular);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgViewDetails = findViewById(R.id.img_view_more);
        imgViewDetails.setOnClickListener(this);
        imgDefaultParameter = findViewById(R.id.img_view_default_parameter);
        imgDefaultParameter.setOnClickListener(this);
        imgCamera = findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);
        cameraImage = findViewById(R.id.imageView);

        spinnerSubDivision = findViewById(R.id.spinner_sub_division);
        spinnerArea = findViewById(R.id.spinner_area);
        spinnerLocation = findViewById(R.id.spinner_location);

        Intent intent = getIntent();
        if(intent != null)
        {
            jobCardId = intent.getStringExtra(AppConstants.JOB_CARD_ID);
        }
        Log.d("sassasasasa","ss: "+jobCardId);
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label)
    {

    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response)
    {

    }

    @Override
    public void onAsyncCompletelyFail(String message, String label)
    {

    }
}
