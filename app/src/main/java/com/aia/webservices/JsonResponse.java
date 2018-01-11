package com.aia.webservices;

import com.aia.models.AssetJobCardModel;
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


}
