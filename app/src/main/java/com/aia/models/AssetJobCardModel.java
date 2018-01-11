package com.aia.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class AssetJobCardModel implements Serializable
{

    public AssetJobCardModel() {}

    @SerializedName("jc_id")
    public String assetCardId;

    @SerializedName("asset")
    public String assetName;

    @SerializedName("category")
    public String assetCategory;

    @SerializedName("make")
    public String assetMake;

    @SerializedName("make_no")
    public String assetMakeNo;

    @SerializedName("location")
    public String assetLocation;

    public String cardStatus;
    public String assignedDate;

    public String getAssetCardId() {
        return assetCardId;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public String getAssetMakeNo() {
        return assetMakeNo;
    }

    public String getAssetLocation() {
        return assetLocation;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getAssignedDate() {
        return assignedDate;
    }
}
