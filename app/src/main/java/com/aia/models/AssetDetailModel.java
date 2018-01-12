package com.aia.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Piyush on 12-01-2018.
 * Bynry
 */
public class AssetDetailModel
{

    @SerializedName("jc_id")
    public String assetJobCardId;

    @SerializedName("asset_id")
    public String assetId;

    @SerializedName("category")
    public String assetCategory;

    @SerializedName("subcategory")
    public String assetSubCategory;

    @SerializedName("make")
    public String assetMake;

    @SerializedName("make_no")
    public String assetMakeNo;

    @SerializedName("sub_division")
    public String assetSubDivision;

    @SerializedName("area")
    public String assetArea;

    @SerializedName("location")
    public String assetLocation;

    @SerializedName("asset")
    public String assetName;

    @SerializedName("latitude")
    public String assetLatitude;

    @SerializedName("longitude")
    public String assetLongitude;

    @SerializedName("address")
    public String assetAddress;

}
