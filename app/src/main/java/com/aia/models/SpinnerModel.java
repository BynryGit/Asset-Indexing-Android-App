package com.aia.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Piyush on 12-01-2018.
 * Bynry
 */
public class SpinnerModel
{
    @SerializedName("sub_division_id")
    public String subDivisionId;

    @SerializedName("sub_division")
    public String subDivisionName;

    @SerializedName("area_id")
    public String areaId;

    @SerializedName("area")
    public String areaName;

    @SerializedName("location_id")
    public String locationId;

    @SerializedName("location")
    public String locationName;

    public String getSubDivisionId() {
        return subDivisionId;
    }

    public String getSubDivisionName() {
        return subDivisionName;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }
}
