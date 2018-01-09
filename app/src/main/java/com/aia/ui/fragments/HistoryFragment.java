package com.aia.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aia.R;
import com.aia.db.DatabaseManager;
import com.aia.models.TodayModel;
import com.aia.ui.activities.LandingActivity;
import com.aia.utility.App;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;

import java.util.ArrayList;

public class HistoryFragment extends ParentFragment
{
    private Context mContext;
    private TextView txtEmptyScreenMsg;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String userId = "";

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

        Typeface fontItalic = App.getFontItalic();
        txtEmptyScreenMsg = rootView.findViewById(R.id.txt_empty_screen_msg);
        txtEmptyScreenMsg.setTypeface(fontItalic);

        recyclerView = rootView.findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");

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

//        getHistoryCards();
        return rootView;
    }

    public void getHistoryCards()
    {
        ArrayList<TodayModel> historyDateList = DatabaseManager.getNSCJobCards(userId,AppConstants.CARD_STATUS_OPEN);

        /*if(historyDateList != null)
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

            TodayAdapter historyAdapter = new TodayAdapter(mContext, null);
            recyclerView.setAdapter(historyAdapter);
        }*/
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
