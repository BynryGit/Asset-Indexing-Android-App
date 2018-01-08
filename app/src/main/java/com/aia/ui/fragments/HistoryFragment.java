package com.aia.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.models.HistoryModel;
import com.aia.ui.adapters.TodayAdapter;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends ParentFragment
{
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String screenName = "";
    private String userId;

    public HistoryFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        mContext = getActivity().getApplicationContext();
        screenName = AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, "");
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && DetailLandingActivity.floatingSearch.getVisibility() == View.VISIBLE)
                {
                    DetailLandingActivity.floatingSearch.hide();
                }
                else if (dy < 0 && DetailLandingActivity.floatingSearch.getVisibility() != View.VISIBLE)
                {
                    DetailLandingActivity.floatingSearch.show();
                }
            }
        });*/

        getHistoryCards();
        return rootView;
    }

    public void getHistoryCards()
    {
        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
        {

            ArrayList<HistoryModel> historyDateList = DatabaseManager.getNSCHistoryJobCard(userId,AppConstants.CARD_STATUS_OPEN);

            if(historyDateList != null)
            {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                HashMap<String, Integer> hashMapOpen = new HashMap<>();
                HashMap<String, Integer> hashMapClosed = new HashMap<>();
                for (int i = 0; i < historyDateList.size(); i++)
                {
                    countOpen =  DatabaseManager.getNSCTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed =  DatabaseManager.getNSCTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for(int i = 0; i < dateList.size(); i++)
                {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                TodayAdapter historyAdapter = new TodayAdapter(mContext, screenName, null);
                recyclerView.setAdapter(historyAdapter);
            }
        }
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

}
