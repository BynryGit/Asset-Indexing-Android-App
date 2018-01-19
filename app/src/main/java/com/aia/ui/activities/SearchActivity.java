package com.aia.ui.activities;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.models.AssetJobCardModel;
import com.aia.ui.adapters.AssetJobCardAdapter;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;

import java.util.ArrayList;

public class SearchActivity extends ParentActivity implements View.OnClickListener
{
    private Context mContext;
    private SearchView searchView;
    private String searchQuery = "";
    private FloatingActionButton floatingActionButton;
    private ImageView imgBack;
    private TextView txtResult;
    private RecyclerView recyclerView;
    private AssetJobCardAdapter assetJobCardAdapter;
    private ArrayList<AssetJobCardModel> mJobCards = new ArrayList<>();
    private String userId = "";
    private boolean jobCardClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        Typeface fontBold = App.getFontBold();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        txtResult = findViewById(R.id.txt_result);
        txtResult.setTypeface(fontBold);
        txtResult.setGravity(Gravity.CENTER);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        assetJobCardAdapter = new AssetJobCardAdapter(mContext, mJobCards, jobCardClickable);
        recyclerView.setAdapter(assetJobCardAdapter);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        searchView.onActionViewExpanded();
        searchView.setQuery(searchQuery, false);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(final String newText)
            {
                assetJobCardAdapter = null;
                if(newText.length() >= 1)
                {
                    if(mJobCards != null)
                    {
                        mJobCards.clear();
                    }

                    mJobCards = DatabaseManager.getJobCardsBySearch(mContext, newText, userId, AppConstants.CARD_STATUS_OPEN);

                    if(mJobCards != null)
                    {
                        assetJobCardAdapter = new AssetJobCardAdapter(mContext, mJobCards, jobCardClickable);
                        recyclerView.setAdapter(assetJobCardAdapter);
                        if(mJobCards.size() == 1)
                            txtResult.setText(mJobCards.size() + " " + getString(R.string.record_found));
                        else
                            txtResult.setText(mJobCards.size() + " " + getString(R.string.record_found));
                    }
                    else
                    {
                        assetJobCardAdapter = new AssetJobCardAdapter(mContext, mJobCards, jobCardClickable);
                        recyclerView.setAdapter(assetJobCardAdapter);
                        txtResult.setText(getString(R.string.no_asset_found));
                    }

                }
                else
                {
                    assetJobCardAdapter = new AssetJobCardAdapter(mContext, mJobCards, jobCardClickable);
                    recyclerView.setAdapter(assetJobCardAdapter);
                    txtResult.setText(getString(R.string.search_text_criteria));
                }
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return true;
            }
        });
        return true;
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
