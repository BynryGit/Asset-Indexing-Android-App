package com.aia.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aia.R;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.ui.adapters.ViewPagerAdapter;
import com.aia.ui.fragments.HistoryFragment;
import com.aia.ui.fragments.TodayFragment;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.DialogCreator;
import com.aia.webservices.JsonResponse;
import com.android.volley.NetworkResponse;

public class LandingActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller
{

    private Context mContext;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextView txtTitle;
    private ViewPagerAdapter adapter;
    private FrameLayout layoutToday, layoutHistory;
    public static FloatingActionButton floatingSearch;
    private LinearLayout linearProfile;
    private Intent intent;
    private Typeface fontBold, fontRegular, fontItalic;

    private Button btnOpenToday, btnCompletedToday, btnTotalHistory, btnCompletedHistory;
    private TextView txtOpenToday, txtCompletedToday, txtTotalHistory, txtCompletedHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mContext = this;

        fontRegular = App.getFontRegular();
        fontBold = App.getFontBold();
        fontItalic = App.getFontItalic();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        txtTitle = findViewById(R.id.txt_name);
        txtTitle.setTypeface(fontBold);
        txtTitle.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, AppConstants.BLANK_STRING));

        txtOpenToday = findViewById(R.id.txt_open_today);
        txtOpenToday.setTypeface(fontRegular);
        txtCompletedToday = findViewById(R.id.txt_completed_today);
        txtCompletedToday.setTypeface(fontRegular);
        txtTotalHistory = findViewById(R.id.txt_total_history);
        txtTotalHistory.setTypeface(fontRegular);
        txtCompletedHistory = findViewById(R.id.txt_completed_history);
        txtCompletedHistory.setTypeface(fontRegular);

        btnOpenToday = findViewById(R.id.btn_open_today);
        btnOpenToday.setTypeface(fontItalic);
        btnCompletedToday = findViewById(R.id.btn_completed_today);
        btnCompletedToday.setTypeface(fontItalic);
        btnTotalHistory = findViewById(R.id.btn_total_history);
        btnTotalHistory.setTypeface(fontItalic);
        btnCompletedHistory = findViewById(R.id.btn_completed_history);
        btnCompletedHistory.setTypeface(fontItalic);

        layoutToday = findViewById(R.id.today_layout);
        layoutHistory = findViewById(R.id.history_layout);

        linearProfile = findViewById(R.id.linear_profile);
        linearProfile.setOnClickListener(this);

        floatingSearch = findViewById(R.id.fab);
        floatingSearch.setOnClickListener(this);
    }

    private void setupViewPager(final ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayFragment(), getString(R.string.today));
        adapter.addFragment(new HistoryFragment(), getString(R.string.history));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position)
            {
                slideFloatingBtn(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void slideFloatingBtn(int var)
    {
        if(var == 0)
        {
            layoutToday.setVisibility(View.VISIBLE);
            layoutHistory.setVisibility(View.GONE);

        }
        else if(var == 1)
        {
            layoutToday.setVisibility(View.GONE);
            layoutHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        DialogCreator.showExitDialog(this, getString(R.string.exit_app), getString(R.string.do_you_want_to_exit), AppConstants.BLANK_STRING);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(v == floatingSearch)
        {
            intent = new Intent(mContext, SearchActivity.class);
        }
        else if(v == linearProfile)
        {
            intent = new Intent(mContext, ProfileActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
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
