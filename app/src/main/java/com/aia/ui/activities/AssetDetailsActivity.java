package com.aia.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.models.AssetDetailModel;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;
import com.aia.utility.DialogCreator;
import com.aia.utility.MultipartUtility;
import com.aia.webservices.ApiConstants;
import com.aia.webservices.JsonResponse;
import com.aia.webservices.WebRequest;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class AssetDetailsActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static int UPDATE_INTERVAL = 500; // Location updates intervals in sec 5 sec
    private static int FASTEST_INTERVAL = 500; // Location updates intervals in sec 5 sec
    private static int DISPLACEMENT = 0; // 1 meters
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private String ASSET_IMAGE_DIRECTORY_NAME = "Asset-Image", assetImageName = "image";

    private Context mContext;
    private Typeface fontRegular, fontBold, fontItalic;
    private EditText edtAddress;
    private String userId = "", jobCardId = "", assetMake = "", assetMakeNo = "", assetAddress = "", previousLatitude = "", previousLongitude = "";
    private String subDivisionName = "", areaName = "", locationName = "", subDivisionId = "", areaId = "", locationId = "",
            assetId = "";
    private TextView txtTitle, txtAssetNameNo;
    private TextView lblSelectSubDivision, lblSelectArea, lblSelectLocation, lblAddress;
    private ImageView imgBack, imgViewDetails, imgDefaultParameter, imgCamera, cameraImage;
    private Button btnSubmit;
    private Spinner spinnerSubDivision, spinnerArea, spinnerLocation;
    private double currentLatitude = 0, currentLongitude = 0;
    private File finalFile = null;
    private AssetDetailModel assetDetailModel;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_details);
        mContext = this;
        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);

        fontRegular = App.getFontRegular();
        fontBold = App.getFontBold();
        fontItalic = App.getFontItalic();

        lblSelectSubDivision = findViewById(R.id.lbl_select_sub_division);
        lblSelectSubDivision.setTypeface(fontRegular);
        lblSelectArea = findViewById(R.id.lbl_select_area);
        lblSelectArea.setTypeface(fontRegular);
        lblSelectLocation = findViewById(R.id.lbl_select_location);
        lblSelectLocation.setTypeface(fontRegular);
        lblAddress = findViewById(R.id.lbl_address);
        lblAddress.setTypeface(fontRegular);

        edtAddress = findViewById(R.id.edt_address);
        edtAddress.setTypeface(fontBold);
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
        imgDefaultParameter.setVisibility(View.GONE);
        imgCamera = findViewById(R.id.img_capture);
        imgCamera.setOnClickListener(this);
        cameraImage = findViewById(R.id.imageView);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setTypeface(fontRegular);
        btnSubmit.setOnClickListener(this);

        spinnerSubDivision = findViewById(R.id.spinner_sub_division);
        spinnerArea = findViewById(R.id.spinner_area);
        spinnerLocation = findViewById(R.id.spinner_location);

        Intent intent = getIntent();
        if (intent != null) {
            jobCardId = intent.getStringExtra(AppConstants.JOB_CARD_ID);
        }

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        getAssetDetails();
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
        }

        if (v == imgDefaultParameter) {

        }

        if (v == imgViewDetails)
        {
            DialogCreator.showAssetDetailsDialog(mContext, assetDetailModel);
        }

        if (v == imgCamera)
        {
            boolean isGPSEnabled = CommonUtility.checkGPSConnectivity(mContext);
            if(isGPSEnabled)
            {
                createLocationRequest();
                startLocationUpdates();

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    currentLatitude = mLastLocation.getLatitude();
                    currentLongitude = mLastLocation.getLongitude();
                } else {
                    currentLatitude = 0;
                    currentLongitude = 0;
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = null;
                fileUri = getOutputMediaFileUri(ASSET_IMAGE_DIRECTORY_NAME, assetImageName);
                List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    mContext.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, AppConstants.CAMERA_RESULT_CODE);
            }
        }

        if(v == btnSubmit)
        {
            checkValidation();
        }
    }

    private void checkValidation()
    {
        if(finalFile == null)
            Toast.makeText(mContext, getString(R.string.please_provide_asset_image), Toast.LENGTH_SHORT).show();
        else
            uploadAssetDetails();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.CAMERA_RESULT_CODE && resultCode == AppConstants.RESULT_OK)
        {
            try
            {
                Bitmap bitmap = getBitmapScaled(ASSET_IMAGE_DIRECTORY_NAME, assetImageName);

                cameraImage.setImageBitmap(bitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(mContext, bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(mContext, Locale.getDefault());
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, AppConstants.ONE);
                String address = addresses.get(0).getAddressLine(AppConstants.ZERO);
                if(address != null)
                    edtAddress.setText(address);

            } catch (Exception e) {e.printStackTrace();}
        }
    }

    private void setParameters()
    {
        txtAssetNameNo.setText(assetMake + " | " + assetMakeNo);
        if(assetAddress.length() > 0)
        {
            edtAddress.setText(assetAddress);
        }
    }

    private void getAssetDetails()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                showLoadingDialog();
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getString(R.string.job_card_id), jobCardId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_ASSET_DETAILS_URL,
                        ApiConstants.GET_ASSET_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_ASSET_DETAILS);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAssetSubDivisions()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                JSONObject jsonObject = new JSONObject();
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.GET, ApiConstants.GET_SUB_DIVISION_URL,
                        ApiConstants.GET_SUB_DIVISION, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SUB_DIVISION);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAssetArea()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getString(R.string.sub_division_id), subDivisionId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_AREA_URL,
                        ApiConstants.GET_AREA, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_AREA);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAssetLocation()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getString(R.string.area_id), areaId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_LOCATION_URL,
                        ApiConstants.GET_LOCATION, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_LOCATION);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAssetDetails()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(getString(R.string.job_card_id), jobCardId);
                jsonObject.put(getString(R.string.asset_id_upload), assetId);
                jsonObject.put(getString(R.string.sub_division_upload), subDivisionId);
                jsonObject.put(getString(R.string.area_id_upload), areaId);
                jsonObject.put(getString(R.string.location_id_upload), locationId);
                jsonObject.put(getString(R.string.make_upload), assetMake);
                jsonObject.put(getString(R.string.make_no_upload), assetMakeNo);
                jsonObject.put(getString(R.string.address_upload), edtAddress.getText().toString().trim());
                jsonObject.put(getString(R.string.latitude_upload), String.valueOf(currentLatitude));
                jsonObject.put(getString(R.string.longitude_upload), String.valueOf(currentLongitude));

                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.UPLOAD_ASSET_DETAILS_URL,
                        ApiConstants.UPLOAD_ASSET_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_ASSET_DETAILS);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAsyncSuccess(final JsonResponse jsonResponse, String label)
    {
        switch (label)
        {
            case ApiConstants.GET_ASSET_DETAILS:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.asset_detail != null)
                        {
                            try
                            {
                                assetDetailModel = jsonResponse.asset_detail;

                                subDivisionName = assetDetailModel.assetSubDivision;
                                areaName = assetDetailModel.assetArea;
                                locationName = assetDetailModel.assetLocation;
                                assetMake = assetDetailModel.assetMake;
                                assetMakeNo = assetDetailModel.assetMakeNo;
                                assetAddress = assetDetailModel.assetAddress;
                                assetId = assetDetailModel.assetId;
                                previousLatitude = assetDetailModel.assetLatitude;
                                previousLongitude = assetDetailModel.assetLongitude;

                                assetImageName = "Asset_Image_" + assetDetailModel.assetJobCardId + "_"+assetDetailModel.assetId + "_" + userId;
                                getAssetSubDivisions();
                                setParameters();
                            } catch (Exception e) { finish(); e.printStackTrace();}
                        }else finish();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }else finish();
            }
            break;
            case ApiConstants.GET_SUB_DIVISION:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.sub_division_list != null)
                        {
                            try
                            {
                                if(jsonResponse.sub_division_list.size() > 0)
                                {
                                    List<String> subDivisionNameList = new ArrayList<>();
                                    int len = jsonResponse.sub_division_list.size();
                                    for(int i = 0; i < len; i++)
                                    {
                                        subDivisionNameList.add(jsonResponse.sub_division_list.get(i).getSubDivisionName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, subDivisionNameList)
                                    {
                                        public View getView(int position, View convertView, ViewGroup parent)
                                        {
                                            View v = super.getView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontBold);
                                            return v;
                                        }

                                        public View getDropDownView(int position,  View convertView,  ViewGroup parent)
                                        {
                                            View v =super.getDropDownView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontItalic);
                                            return v;
                                        }
                                    };
                                    spinnerSubDivision.setAdapter(adapter);

                                    for (int i = 0; i < adapter.getCount(); i++)
                                    {
                                        if (subDivisionName.trim().equals(adapter.getItem(i).toString()))
                                        {
                                            spinnerSubDivision.setSelection(adapter.getPosition(subDivisionName));
                                            break;
                                        }
                                    }

                                    spinnerSubDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                    {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                        {
                                            subDivisionId = jsonResponse.sub_division_list.get(position).getSubDivisionId();
                                            getAssetArea();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}

                                    });
                                }else finish();
                            } catch (Exception e) {finish(); e.printStackTrace();}
                        }else finish();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } else finish();
            }
            break;
            case ApiConstants.GET_AREA:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.area_list != null)
                        {
                            try
                            {
                                if(jsonResponse.area_list.size() > 0)
                                {
                                    List<String> areaNameList = new ArrayList<>();
                                    int len = jsonResponse.area_list.size();
                                    for(int i = 0; i < len; i++)
                                    {
                                        areaNameList.add(jsonResponse.area_list.get(i).getAreaName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, areaNameList)
                                    {
                                        public View getView(int position, View convertView, ViewGroup parent)
                                        {
                                            View v = super.getView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontBold);
                                            return v;
                                        }

                                        public View getDropDownView(int position,  View convertView,  ViewGroup parent)
                                        {
                                            View v =super.getDropDownView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontItalic);
                                            return v;
                                        }
                                    };
                                    spinnerArea.setAdapter(adapter);

                                    for (int i = 0; i < adapter.getCount(); i++)
                                    {
                                        if (areaName.trim().equals(adapter.getItem(i).toString()))
                                        {
                                            spinnerArea.setSelection(adapter.getPosition(areaName));
                                            break;
                                        }
                                    }

                                    spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                    {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                        {
                                            areaId = jsonResponse.area_list.get(position).getAreaId();
                                            getAssetLocation();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}

                                    });
                                }else finish();
                            } catch (Exception e) {finish(); e.printStackTrace();}
                        }else finish();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }else finish();
            }
            break;
            case ApiConstants.GET_LOCATION:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.location_list != null)
                        {
                            try
                            {
                                if(jsonResponse.location_list.size() > 0)
                                {
                                    dismissLoadingDialog();

                                    List<String> areaNameList = new ArrayList<>();
                                    int len = jsonResponse.location_list.size();
                                    for(int i = 0; i < len; i++)
                                    {
                                        areaNameList.add(jsonResponse.location_list.get(i).getLocationName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, areaNameList)
                                    {
                                        public View getView(int position, View convertView, ViewGroup parent)
                                        {
                                            View v = super.getView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontBold);
                                            return v;
                                        }

                                        public View getDropDownView(int position,  View convertView,  ViewGroup parent)
                                        {
                                            View v =super.getDropDownView(position, convertView, parent);
                                            ((TextView) v).setTypeface(fontItalic);
                                            return v;
                                        }
                                    };
                                    spinnerLocation.setAdapter(adapter);

                                    for (int i = 0; i < adapter.getCount(); i++)
                                    {
                                        if (locationName.trim().equals(adapter.getItem(i).toString()))
                                        {
                                            spinnerLocation.setSelection(adapter.getPosition(locationName));
                                            break;
                                        }
                                    }

                                    spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                    {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                        {
                                            locationId = jsonResponse.location_list.get(position).getLocationId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}

                                    });
                                }else finish();
                            } catch (Exception e) {finish(); e.printStackTrace();}
                        }else finish();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }else finish();
            }
            break;
            case ApiConstants.UPLOAD_ASSET_DETAILS:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        try
                        {
                            new UploadImage().execute();
                        } catch (Exception e) {e.printStackTrace();}
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response)
    {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_ASSET_DETAILS: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_SUB_DIVISION: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_AREA: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_LOCATION: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.UPLOAD_ASSET_DETAILS: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label)
    {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_ASSET_DETAILS: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_SUB_DIVISION: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_AREA: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.GET_LOCATION: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.UPLOAD_ASSET_DETAILS: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteImageFolder();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mContext, getString(R.string.this_device_is_not_support_gps), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
//        Log.d("Latitudeeeeee: "+mLastLocation.getLatitude(), "Longitude: "+mLastLocation.getLongitude());
    }

    public Uri getOutputMediaFileUri(String dirname, String filename)
    {
        File file = getFilePath(dirname, filename);
        return FileProvider.getUriForFile(mContext, "com.aia.file", file);
    }

    public File getFilePath(String dirname, String filename)
    {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), dirname);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs()) {return null;}
        }
        File createdFile = new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
        return createdFile;
    }

    private Bitmap getBitmapScaled(String dirname, String filename)
    {
        Bitmap compressedImage = null;
        try
        {
            File file = getFilePath(dirname, filename);
            compressedImage = new Compressor.Builder(mContext)
                    .setMaxWidth(AppConstants.IMAGE_WIDTH)
                    .setMaxHeight(AppConstants.IMAGE_HEIGHT)
                    .setQuality(AppConstants.ONE)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .build()
                    .compressToBitmap(file);
            compressedImage = Bitmap.createScaledBitmap(compressedImage, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT, false);
            if (compressedImage != null)
                compressedImage = CommonUtility.addWaterMarkDate(compressedImage, CommonUtility.getCurrentDateTime());
        } catch(Exception e) {}

        return compressedImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void deleteImage(File file) {
        File fileDelete = file;
        if (fileDelete.exists()) {
            if (fileDelete.delete()) {
//                callBroadCast();
            } else {
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaScannerConnection.scanFile(mContext, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    private void deleteImageFolder()
    {
        File folder1 = new File(Environment.getExternalStorageDirectory() + File.separator + ASSET_IMAGE_DIRECTORY_NAME);
        try {
            if(folder1.exists())
                CommonUtility.deleteDir(folder1);
        }catch(Exception e) {}
    }

    private class UploadImage extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, "");

                MultipartUtility multipartUtility = new MultipartUtility(ApiConstants.UPLOAD_ASSET_IMAGE_URL, "UTF-8");

                multipartUtility.addFormField(getString(R.string.asset_id_upload), assetId);
                multipartUtility.addFilePart(getString(R.string.file), finalFile);

                multipartUtility.addFormField("Authorization", token);
                String response = multipartUtility.finish(); // response from server.

                return response;

            } catch(IOException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(!TextUtils.isEmpty(s))
            {
                try {
                    JSONObject response = new JSONObject(s.toString());

                    if(response.getString(getString(R.string.result)).equalsIgnoreCase(getString(R.string.success)))
                    {
                        DatabaseManager.updateAssetJobCardStatus(jobCardId, AppConstants.CARD_STATUS_COMPLETED, CommonUtility.getCurrentDate());
                        deleteImage(finalFile);
                        finish();
                    }
                } catch(Exception e) {e.printStackTrace();}
            }
        }
    }
}
