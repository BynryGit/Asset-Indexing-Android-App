package com.aia.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.models.AssetHistoryCardModel;
import com.aia.models.AssetJobCardModel;
import com.aia.ui.adapters.AssetHistoryCardAdapter;
import com.aia.ui.adapters.AssetJobCardAdapter;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;
import com.aia.utility.DialogCreator;
import com.aia.webservices.ApiConstants;
import com.aia.webservices.JsonResponse;
import com.aia.webservices.WebRequest;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class LandingActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{
    private Context mContext;
    private Toolbar toolbar;
    private TextView txtTitle, txtEmptyScreenMsg;
    private FloatingActionButton floatingSearch;
    private LinearLayout linearProfile;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Intent intent;
    private Typeface fontBold, fontRegular, fontItalic;
    public static Activity activity;
    private Button btnTotal, btnOpen, btnCompleted, btnHistory;
    private TextView txtTotal, txtOpen, txtCompleted, txtHistory;
    private boolean isOpen, isCompleted, isHistory;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mContext = this;
        activity = this;

        fontRegular = App.getFontRegular();
        fontBold = App.getFontBold();
        fontItalic = App.getFontItalic();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        floatingSearch = findViewById(R.id.fab);
        floatingSearch.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingSearch.getVisibility() == View.VISIBLE)
                {
                    floatingSearch.hide();
                }
                else if (dy < 0 && floatingSearch.getVisibility() != View.VISIBLE)
                {
                    floatingSearch.show();
                }
            }
        });

        isOpen = true; isCompleted = false; isHistory = false;

        txtTitle = findViewById(R.id.txt_name);
        txtTitle.setTypeface(fontItalic);
        txtTitle.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, AppConstants.BLANK_STRING));
        txtEmptyScreenMsg = findViewById(R.id.txt_empty_screen_msg);
        txtEmptyScreenMsg.setTypeface(fontBold);
        txtEmptyScreenMsg.setVisibility(View.GONE);

        txtTotal = findViewById(R.id.txt_total);
        txtTotal.setTypeface(fontRegular);
        txtOpen = findViewById(R.id.txt_open);
        txtOpen.setTypeface(fontRegular);
        txtCompleted = findViewById(R.id.txt_completed);
        txtCompleted.setTypeface(fontRegular);
        txtHistory = findViewById(R.id.txt_history);
        txtHistory.setTypeface(fontRegular);

        btnTotal = findViewById(R.id.btn_total);
        btnTotal.setTypeface(fontItalic);
        btnOpen = findViewById(R.id.btn_open);
        btnOpen.setTypeface(fontItalic);
        btnOpen.setOnClickListener(this);
        btnCompleted = findViewById(R.id.btn_completed);
        btnCompleted.setTypeface(fontItalic);
        btnCompleted.setOnClickListener(this);
        btnHistory = findViewById(R.id.btn_history);
        btnHistory.setTypeface(fontItalic);
        btnHistory.setOnClickListener(this);

        linearProfile = findViewById(R.id.linear_profile);
        linearProfile.setOnClickListener(this);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        refreshCount();
        updateDeviceToken();
    }

    private void refreshCount()
    {
        ArrayList<AssetJobCardModel> todayModels;
        ArrayList<AssetHistoryCardModel> assetHistoryTables;

        if(isOpen)
        {
            todayModels = DatabaseManager.getAssetJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            btnOpen.setBackgroundResource(R.drawable.ripple_oval_selected);
            btnCompleted.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            btnHistory.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            if(todayModels != null)
            {
                txtEmptyScreenMsg.setVisibility(View.GONE);
            }
            else
            {
                txtEmptyScreenMsg.setVisibility(View.VISIBLE);
                txtEmptyScreenMsg.setText(getString(R.string.consumer_info_not_assigned_to_you));
            }
            AssetJobCardAdapter assetJobCardAdapter = new AssetJobCardAdapter(mContext, todayModels, isOpen);
            recyclerView.setAdapter(assetJobCardAdapter);
        }
        else if(isCompleted)
        {
            todayModels = DatabaseManager.getAssetJobCards(userId, AppConstants.CARD_STATUS_COMPLETED);
            btnOpen.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            btnCompleted.setBackgroundResource(R.drawable.ripple_oval_selected);
            btnHistory.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            if(todayModels != null)
            {
                txtEmptyScreenMsg.setVisibility(View.GONE);
            }
            else
            {
                txtEmptyScreenMsg.setVisibility(View.VISIBLE);
                txtEmptyScreenMsg.setText(getString(R.string.reading_not_taken_yet));
            }
            AssetJobCardAdapter assetJobCardAdapter = new AssetJobCardAdapter(mContext, todayModels, isOpen);
            recyclerView.setAdapter(assetJobCardAdapter);
        }
        else if(isHistory)
        {
            DatabaseManager.deleteUploadsHistory(mContext);

            assetHistoryTables = DatabaseManager.getAssetHistoryCards(userId);
            btnOpen.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            btnCompleted.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            btnHistory.setBackgroundResource(R.drawable.ripple_oval_selected);
            if(assetHistoryTables != null)
            {
                txtEmptyScreenMsg.setVisibility(View.GONE);
            }
            else
            {
                txtEmptyScreenMsg.setVisibility(View.VISIBLE);
                txtEmptyScreenMsg.setText(getString(R.string.no_data_available));
            }
            AssetHistoryCardAdapter assetJobCardAdapter = new AssetHistoryCardAdapter(mContext, assetHistoryTables);
            recyclerView.setAdapter(assetJobCardAdapter);
        }

        int countOpen =  DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countCompleted = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_COMPLETED);
        int countHistory = 0;

        btnTotal.setText(""+(countOpen + countCompleted));
        btnOpen.setText(""+countOpen);
        btnCompleted.setText(""+countCompleted);

        boolean alreadyPresent = false;
        assetHistoryTables = DatabaseManager.getAssetHistoryCards(userId);

        if(assetHistoryTables != null)
        {
            for(int i = 0; i < assetHistoryTables.size(); i++)
            {
                if(assetHistoryTables.get(i).todayDate.equals(CommonUtility.getCurrentDate()))
                {
                    alreadyPresent = true;
                    break;
                }
            }

            if(!alreadyPresent)
            {
                AssetHistoryCardModel assetHistoryCardModel = new AssetHistoryCardModel();
                assetHistoryCardModel.todayDate = CommonUtility.getCurrentDate();
                assetHistoryCardModel.countOpen = ""+countOpen;
                assetHistoryCardModel.countCompleted = ""+countCompleted;

                DatabaseManager.saveAssetHistoryCardsInfo(mContext, assetHistoryCardModel);
            }
            else
            {
                DatabaseManager.updateAssetHistoryCard(CommonUtility.getCurrentDate(), ""+countOpen, ""+countCompleted);
            }

            assetHistoryTables = DatabaseManager.getAssetHistoryCards(userId);
            for(int j = 0; j < assetHistoryTables.size(); j++)
            {
                countHistory = countHistory + Integer.parseInt(assetHistoryTables.get(j).countCompleted);
            }
        }
        else
        {
            if(countOpen > 0)
            {
                AssetHistoryCardModel assetHistoryCardModel = new AssetHistoryCardModel();
                assetHistoryCardModel.todayDate = CommonUtility.getCurrentDate();
                assetHistoryCardModel.countOpen = ""+countOpen;
                assetHistoryCardModel.countCompleted = ""+countCompleted;

                DatabaseManager.saveAssetHistoryCardsInfo(mContext, assetHistoryCardModel);
            }
        }

        btnHistory.setText(""+countHistory);
    }

    @Override
    public void onClick(View v)
    {
        if(v == floatingSearch)
        {
            intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
        }

        if(v == linearProfile)
        {
            intent = new Intent(mContext, ProfileActivity.class);
            startActivity(intent);
        }

        if(v == btnOpen)
        {
            isOpen = true; isCompleted = false; isHistory = false;
            refreshCount();
        }

        if(v == btnCompleted)
        {
            isOpen = false; isCompleted = true; isHistory = false;
            refreshCount();
        }

        if(v == btnHistory)
        {
            isOpen = false; isCompleted = false; isHistory = true;
            refreshCount();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_notification:
                intent = new Intent(mContext, NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_download:
                getJobCards();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateDeviceToken()
    {
        String empId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        String deviceFcmToken = AppPreferences.getInstance(mContext).getString(AppConstants.DEVICE_FCM_TOKEN, AppConstants.BLANK_STRING);
        String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
        if (CommonUtility.checkConnectivity(mContext))
        {
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getString(R.string.user_id), empId);
                jsonObject.put(getString(R.string.fcm_token), deviceFcmToken);

                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.UPDATE_FCM_TOKEN_URL,
                        ApiConstants.UPDATE_FCM_TOKEN, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.UPDATE_FCM_TOKEN);

            } catch (Exception e) {e.printStackTrace();}
        }
        else{
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_LONG).show();
        }
    }

    private void getJobCards()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            showLoadingDialog();
            String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
            JsonObjectRequest request = WebRequest.callPostMethod(null, Request.Method.GET, ApiConstants.GET_ASSIGNED_ASSET_CARD_URL,
                    ApiConstants.GET_ASSIGNED_ASSET_CARD, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_ASSIGNED_ASSET_CARD);
        }
        else
        {
            dismissLoadingDialog();
            DialogCreator.showMessageDialog(mContext, getString(R.string.error_internet_not_connected), getString(R.string.error));
        }
    }

    private void removeJobCards()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            showLoadingDialog();
            String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
            JsonObjectRequest request = WebRequest.callPostMethod(null, Request.Method.GET, ApiConstants.GET_DE_ASSIGNED_ASSET_CARD_URL,
                    ApiConstants.GET_DE_ASSIGNED_ASSET_CARD, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_DE_ASSIGNED_ASSET_CARD);
        }
        else
        {
            dismissLoadingDialog();
            DialogCreator.showMessageDialog(mContext, getString(R.string.error_internet_not_connected), getString(R.string.error));
        }
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label)
    {
        switch (label)
        {
            case ApiConstants.GET_ASSIGNED_ASSET_CARD:
            {
                if (jsonResponse != null)
                {
                    dismissLoadingDialog();
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.asset_cards.size() > 0)
                        {
                            try
                            {
                                if(jsonResponse.asset_cards.get(0).assetCardId != null)
                                {
                                    DatabaseManager.saveAssetJobCards(mContext, jsonResponse.asset_cards);
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                    isOpen = true; isCompleted = false; isHistory = false;
                                    refreshCount();
                                }
                            } catch(Exception e) {e.printStackTrace();}
                        }
                        else
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                removeJobCards();
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD:
            {
                if (jsonResponse != null)
                {
                    dismissLoadingDialog();
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.deassign_request_list.size() > 0)
                        {
                            try
                            {
                                if(jsonResponse.deassign_request_list != null)
                                {
                                    DatabaseManager.handleDeAssignAssetJobCard(mContext, jsonResponse.deassign_request_list, userId);
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                    isOpen = true; isCompleted = false; isHistory = false;
                                    refreshCount();
                                }
                            } catch(Exception e) {e.printStackTrace();}
                        }
                        else
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
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
            case ApiConstants.GET_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                removeJobCards();
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD: {
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
            case ApiConstants.GET_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
                removeJobCards();
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, getString(R.string.api_fail_message), Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
