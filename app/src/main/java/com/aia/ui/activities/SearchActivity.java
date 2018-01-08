package com.aia.ui.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aia.R;

public class SearchActivity extends ParentActivity implements View.OnClickListener
{

    private SearchView searchView;
    private String searchQuery = "";
    private FloatingActionButton floatingActionButton;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        searchView.onActionViewExpanded();
//        searchView.setInputType(InputType.TYPE_CLASS_PHONE);
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
