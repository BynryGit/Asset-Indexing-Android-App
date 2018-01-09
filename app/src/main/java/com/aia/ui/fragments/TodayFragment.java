package com.aia.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aia.ui.activities.LandingActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.interfaces.ApiServiceCaller;
import com.aia.models.TodayModel;
import com.aia.ui.adapters.TodayAdapter;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;
import com.aia.webservices.ApiConstants;
import com.aia.webservices.JsonResponse;
import com.aia.webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TodayFragment extends Fragment
{
    public Context mContext;
    private TextView txtEmptyScreenMsg;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    public String userId;

    public TodayFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        mContext = getActivity().getApplicationContext();

        Typeface fontItalic = App.getFontItalic();
        txtEmptyScreenMsg = rootView.findViewById(R.id.txt_empty_screen_msg);
        txtEmptyScreenMsg.setTypeface(fontItalic);

        recyclerView = rootView.findViewById(R.id.recycler_view_today);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && LandingActivity.floatingSearch.getVisibility() == View.VISIBLE)
                {
                    LandingActivity.floatingSearch.hide();
                }
                else if (dy < 0 && LandingActivity.floatingSearch.getVisibility() != View.VISIBLE)
                {
                    LandingActivity.floatingSearch.show();
                }
            }
        });
        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");
        getCardsFromDB();
        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public void getCardsFromDB()
    {
        ArrayList<TodayModel> nsc_todayArrayList = DatabaseManager.getNSCJobCards(userId, AppConstants.CARD_STATUS_OPEN);
        if(nsc_todayArrayList != null)
        {
            TodayAdapter todayAdapter = new TodayAdapter(mContext, nsc_todayArrayList);
            recyclerView.setAdapter(todayAdapter);
        }

        /*else if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            ArrayList<TodayModel> dsc_todayArrayList = DatabaseManager.getDISCJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            if(dsc_todayArrayList != null)
            {
                TodayAdapter todayAdapter = new TodayAdapter(mContext, dsc_todayArrayList);
                recyclerView.setAdapter(todayAdapter);
            }
        }*/
    }
}
