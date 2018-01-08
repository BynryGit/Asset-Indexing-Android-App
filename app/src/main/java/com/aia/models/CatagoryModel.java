package com.aia.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CatagoryModel implements Serializable {
    @SerializedName("category")
    public String category;
    @SerializedName("id")
    public String id;
    @SerializedName("sub_category")
    public String sub_category;

    @SerializedName("sub_category_id")

    public String sub_category_id;

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    @SerializedName("asset_id")


    public String asset_id;
    @SerializedName("asset")

    public String asset;

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getsub_catagory_id() {
        return sub_category_id;
    }

    public void setsub_catagory_id(String sub_catagory_id) {
        this.sub_category_id = sub_catagory_id;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


