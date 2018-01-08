package com.aia.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class TodayModel implements Serializable
{

    public TodayModel()
    {}

    @SerializedName("asset_name")
    public String asset_name;

    @SerializedName("category")
    public String category;

    @SerializedName("subcategory")
    public String subcategory;

    @SerializedName("area")
    public String area;

    @SerializedName("location")
    public String location;

    @SerializedName("asset_id")
    public String asset_id;

    @SerializedName("commission_id")
    public String commission_id;

    @SerializedName("decommission_id")
    public String decommission_id;

    @SerializedName("parameter_name")
    public String parameter_name;

    @SerializedName("parameter_value")
    public String parameter_value;

    @SerializedName("parameter_unit")
    public String parameter_unit;

    @SerializedName("check_list")
    public ArrayList<String> check_list ;

    @SerializedName("deassign_request_list")
    public ArrayList<String> deassign_request_list ;

    @SerializedName("readingparameter_list")
    public ArrayList<String> readingparameter_list ;

    @SerializedName("monitoring_id")
public String monitoring_id;


    @SerializedName("preventive_id")
    public String preventive_id;

    public String getPreventive_id() {
        return preventive_id;
    }

    public void setPreventive_id(String preventive_id) {
        this.preventive_id = preventive_id;
    }

    public String getBreakdown_id() {
        return breakdown_id;
    }

    public void setBreakdown_id(String breakdown_id) {
        this.breakdown_id = breakdown_id;
    }

    @SerializedName("breakdown_id")
    public String breakdown_id;


    public String assignedDate;

    public String getAsset_name() {
        return asset_name;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getArea() {
        return area;
    }

    public String getLocation() {
        return location;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getCommission_id() {
        return commission_id;
    }

    public String getDecommission_id() {
        return decommission_id;
    }

    public String getParameter_name() {
        return parameter_name;
    }

    public String getParameter_value() {
        return parameter_value;
    }

    public String getParameter_unit() {
        return parameter_unit;
    }

    public ArrayList<String> getCheck_list() {
        return check_list;
    }

    public ArrayList<String> getReadingparameter_list() {
        return readingparameter_list;
    }

    public ArrayList<String> getDeassign_request_list() {
        return deassign_request_list;
    }

    public String getMonitoring_id() {
        return monitoring_id;
    }

    public String getAssignedDate() {
        return assignedDate;
    }
}
