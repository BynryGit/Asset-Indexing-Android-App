package com.aia.webservices;

import com.aia.models.AssetDetailModel;
import com.aia.models.AssetJobCardModel;
import com.aia.models.SpinnerModel;
import com.aia.models.UserProfileModel;

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
    public ArrayList<AssetJobCardModel> asset_cards;
    public ArrayList<String> deassign_request_list;
    public AssetDetailModel asset_detail;
    public ArrayList<SpinnerModel> sub_division_list;
    public ArrayList<SpinnerModel> area_list;
    public ArrayList<SpinnerModel> location_list;


}
