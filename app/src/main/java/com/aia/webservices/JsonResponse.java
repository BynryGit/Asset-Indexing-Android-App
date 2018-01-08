package com.aia.webservices;

import com.aia.models.TodayModel;
import com.aia.models.UserProfileModel;
import com.aia.models.CatagoryModel;

import java.util.ArrayList;

public class JsonResponse
{
    //Message and Success
    public String SUCCESS = "success";
    public String message ;
    public String result;
    public static String FAILURE = "failure";


    //ArrayList's Models
    public UserProfileModel responsedata;
    public ArrayList<CatagoryModel> categories;
    public ArrayList<CatagoryModel> sub_categories;
    public ArrayList<CatagoryModel> asset_data;
    public ArrayList<TodayModel> commissioning_cards;
    public ArrayList<TodayModel> decommissioning_cards;
    public ArrayList<TodayModel> commissioning_detail;
    public ArrayList<TodayModel> decommissioning_detail;
    public ArrayList<TodayModel> std_list;
    public ArrayList<TodayModel> monitoring_cards;
    public ArrayList<TodayModel> monitoring_detail;
    public ArrayList<TodayModel> reassigndeassign;
    public ArrayList<TodayModel> monitoring_details;
    public ArrayList<TodayModel> preventive_maintenance_cards;
    public ArrayList<TodayModel> breakdown_maintenance_cards;
    public ArrayList<TodayModel> preventive_maintenance_detail_cards;
    public ArrayList<TodayModel> breakdown_maintenance_detail_cards;


}
