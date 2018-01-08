package com.aia.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class TodayFragment extends Fragment implements ApiServiceCaller
{
    public static Context mContext;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static String screenName = "";
    public static TodayFragment instance;
    public static String userId;
    private int totalCount, completedCount;

    public TodayFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        mContext = getActivity().getApplicationContext();
        screenName = AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, "");
        recyclerView = rootView.findViewById(R.id.recycler_view_today);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        instance = this;
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

    public  void getCardsFromDB()
    {
        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
        {
            ArrayList<TodayModel> nsc_todayArrayList = DatabaseManager.getNSCJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            if(nsc_todayArrayList != null)
            {
                TodayAdapter todayAdapter = new TodayAdapter(mContext, screenName,nsc_todayArrayList);
                recyclerView.setAdapter(todayAdapter);
            }
            refreshUI(screenName);
        }
        else if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            ArrayList<TodayModel> dsc_todayArrayList = DatabaseManager.getDISCJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            if(dsc_todayArrayList != null)
            {
                TodayAdapter todayAdapter = new TodayAdapter(mContext, screenName,dsc_todayArrayList);
                recyclerView.setAdapter(todayAdapter);
            }
            refreshUI(screenName);
        }
    }

    public static void getTodayCards()
    {
        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.NSC_TODAY_CARDS_URL,
                        ApiConstants.NSC_TODAY_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.NSC_TODAY_CARDS);


            } catch (JSONException e) {e.printStackTrace();}
        }
        if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            JSONObject jsonObject = new JSONObject();
            String userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");
            try
            {
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.DISC_TODAY_CARDS_URL,
                        ApiConstants.DISC_TODAY_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.DISC_TODAY_CARDS);
            } catch (JSONException e) {e.printStackTrace();}
        }
    }

    public void refreshUI(String screenName)
    {
        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc))) {
            totalCount =  DatabaseManager.getNSCTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            completedCount = DatabaseManager.getNSCTodayCount(userId, AppConstants.CARD_STATUS_CLOSED);
        }
        else if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            totalCount =  DatabaseManager.getDISCTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            completedCount = DatabaseManager.getDISCTodayCount(userId, AppConstants.CARD_STATUS_CLOSED);
        }
    }

    public void deassignTodayCards(){

        if(screenName.equals(CommonUtility.getString(mContext, R.string.short_nsc)))
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD);

            } catch (JSONException e) {e.printStackTrace();}
        }

        if(screenName.equals(CommonUtility.getString(mContext, R.string.disconnection)))
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD);

            } catch (JSONException e) {e.printStackTrace();}
        }
    }


    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label)
    {
        switch (label)
        {
            case ApiConstants.NSC_TODAY_CARDS:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.commissioning_cards.size()>0)
                        {
                            try
                            {
                                if(jsonResponse.commissioning_cards.get(0).commission_id != null) {
                                    ArrayList<TodayModel> todayModelCardArrayList = new ArrayList<>();
                                    todayModelCardArrayList.addAll(jsonResponse.commissioning_cards);
                                    DatabaseManager.saveNSCJobCards(mContext, todayModelCardArrayList);
                                    TodayAdapter todayAdapter = new TodayAdapter(mContext, screenName, todayModelCardArrayList);
                                    recyclerView.setAdapter(todayAdapter);
                                    refreshUI(screenName);
                                }
                                else
                                {
                                    Toast.makeText(mContext, "No Job Cards Assign to you!", Toast.LENGTH_SHORT).show();
                                }
                            } catch(Exception e) {e.printStackTrace();}
                        }
                        else
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }

                }
                deassignTodayCards();
            }
            break;
            case ApiConstants.DISC_TODAY_CARDS:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.decommissioning_cards.size()>0)
                        {
                            try
                            {
                                if(jsonResponse.decommissioning_cards.get(0).decommission_id != null) {
                                    ArrayList<TodayModel> discTodayCardArrayList = new ArrayList<>();
                                    discTodayCardArrayList.addAll(jsonResponse.decommissioning_cards);
                                    DatabaseManager.saveDSCJobCards(mContext, discTodayCardArrayList);
                                    TodayAdapter todayAdapter = new TodayAdapter(mContext, screenName, discTodayCardArrayList);
                                    recyclerView.setAdapter(todayAdapter);
                                }
                                else
                                {
                                    Toast.makeText(mContext, "No Job Cards Assign to you!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {e.printStackTrace();}
                        }
                        else
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD:
            {
                if (jsonResponse != null)
                {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS))
                    {
                        if (jsonResponse.reassigndeassign.size()>0)
                        {
                            try
                            {
                                DatabaseManager.handleReassignDeassignNSC(mContext, jsonResponse.reassigndeassign.get(0).deassign_request_list, userId);
                                getCardsFromDB();
                                refreshUI(screenName);
                            } catch (Exception e) {e.printStackTrace();}
                        }
                        else
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE))
                        {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
            case ApiConstants.NSC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
                deassignTodayCards();
            }
            break;
            case ApiConstants.DISC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
            case ApiConstants.NSC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
                deassignTodayCards();
            }
            break;
            case ApiConstants.DISC_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_LONG).show();
            }
            break;
        }
    }
}
