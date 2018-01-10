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
import com.aia.models.TodayModel;
import com.aia.ui.adapters.TodayAdapter;
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

import java.util.ArrayList;

public class LandingActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{

    private Context mContext;
    private Toolbar toolbar;
    private TextView txtTitle;
    private FloatingActionButton floatingSearch;
    private LinearLayout linearProfile;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Intent intent;
    private Typeface fontBold, fontRegular, fontItalic;
    public static Activity activity;
    private Button btnOpen, btnCompleted, btnTotal;
    private TextView txtOpenToday, txtCompletedToday, txtTotalHistory;
    private boolean isOpen, isCompleted;
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
        
        recyclerView = findViewById(R.id.recycler_view_today);
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

        isOpen = true; isCompleted = false;

        txtTitle = findViewById(R.id.txt_name);
        txtTitle.setTypeface(fontBold);
        txtTitle.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, AppConstants.BLANK_STRING));

        txtOpenToday = findViewById(R.id.txt_open_today);
        txtOpenToday.setTypeface(fontRegular);
        txtCompletedToday = findViewById(R.id.txt_completed_today);
        txtCompletedToday.setTypeface(fontRegular);
        txtTotalHistory = findViewById(R.id.txt_total);
        txtTotalHistory.setTypeface(fontRegular);

        btnOpen = findViewById(R.id.btn_open_today);
        btnOpen.setTypeface(fontItalic);
        btnOpen.setOnClickListener(this);
        btnCompleted = findViewById(R.id.btn_completed_today);
        btnCompleted.setTypeface(fontItalic);
        btnCompleted.setOnClickListener(this);
        btnTotal = findViewById(R.id.btn_total);
        btnTotal.setTypeface(fontItalic);
        btnTotal.setOnClickListener(this);

        linearProfile = findViewById(R.id.linear_profile);
        linearProfile.setOnClickListener(this);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        refreshCount();
    }

    private void refreshCount()
    {
        ArrayList<TodayModel> todayModels = DatabaseManager.getAssetJobCards(userId, AppConstants.CARD_STATUS_OPEN);
        TodayAdapter todayAdapter = new TodayAdapter(mContext, todayModels);
        recyclerView.setAdapter(todayAdapter);

        int todayOpenCount =  DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
        int todayCompletedCount = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_CLOSED);
        int historyCompletedCount = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_CLOSED);

        btnOpen.setText(""+todayOpenCount);
        btnCompleted.setText(""+todayCompletedCount);
        btnTotal.setText(""+(todayOpenCount + historyCompletedCount));
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
            isOpen = true; isCompleted = false;
            btnOpen.setBackgroundResource(R.drawable.ripple_oval_selected);
            btnCompleted.setBackgroundResource(R.drawable.ripple_oval_un_selected);
        }

        if(v == btnCompleted)
        {
            isOpen = false; isCompleted = true;
            btnOpen.setBackgroundResource(R.drawable.ripple_oval_un_selected);
            btnCompleted.setBackgroundResource(R.drawable.ripple_oval_selected);
        }

        if(v == btnTotal)
        {
            isOpen = false; isCompleted = false;
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

    private void getJobCards()
    {
        if (CommonUtility.checkConnectivity(mContext))
        {
            showLoadingDialog();
            String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
            JsonObjectRequest request = WebRequest.callPostMethod(null, Request.Method.GET, ApiConstants.GET_ASSET_CARD_URL,
                    ApiConstants.GET_ASSET_CARD, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_ASSET_CARD);
        }
        else
        {
            dismissLoadingDialog();
            DialogCreator.showMessageDialog(this, getString(R.string.error_internet_not_connected), getString(R.string.error));
        }

    }

    private void removeJobCards()
    {

    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label)
    {
        switch (label)
        {
            case ApiConstants.GET_ASSET_CARD:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.asset_cards.size() > 0)
                        {
                            try
                            {
                                if(jsonResponse.asset_cards.get(0).assetCardId != null)
                                {
                                    dismissLoadingDialog();
                                    DatabaseManager.saveAssetJobCards(mContext, jsonResponse.asset_cards);
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                                    isOpen = true; isCompleted = false;
                                }
                            } catch(Exception e) {e.printStackTrace();}
                        }
                        else
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                removeJobCards();
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response)
    {
        switch (label) {
            case ApiConstants.GET_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
                removeJobCards();
            }
            break;
            /*case ApiConstants.DISC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
            }
            break;*/
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label)
    {
        switch (label) {
            case ApiConstants.GET_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
                removeJobCards();
            }
            break;
            /*case ApiConstants.DISC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
            }
            break;*/
        }
    }
}
