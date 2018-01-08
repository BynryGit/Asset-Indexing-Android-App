package com.aia.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aia.R;
import com.aia.ui.adapters.ViewPagerAdapter;
import com.aia.ui.fragments.HistoryFragment;
import com.aia.ui.fragments.TodayFragment;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;

public class LandingActivity extends ParentActivity implements View.OnClickListener
{

    private Context mContext;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TextView txtTitle;
    private ViewPagerAdapter adapter;
    private Menu menu;
    private FrameLayout layoutToday, layoutHistory;
    public static FloatingActionButton floatingSearch;
    private LinearLayout linearProfile;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mContext = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        txtTitle = findViewById(R.id.txt_name);
        txtTitle.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, AppConstants.BLANK_STRING));

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
            menu.getItem(AppConstants.ZERO).setIcon(getResources().getDrawable(R.drawable.ic_action_download));
            menu.getItem(AppConstants.ZERO).setVisible(true);
            layoutToday.setVisibility(View.VISIBLE);
            layoutHistory.setVisibility(View.GONE);

        }
        else if(var == 1)
        {
            menu.getItem(AppConstants.ZERO).setVisible(false);
            layoutToday.setVisibility(View.GONE);
            layoutHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {

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
        this.menu = menu;
        return true;
    }
}
