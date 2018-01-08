package com.aia.db.tables;

import android.net.Uri;

import com.aia.db.ContentDescriptor;

/**
 * Created by Piyush on 04-01-2018.
 * Bynry
 */
public class PreventiveJobCardTable
{
    public static final String TABLE_NAME = "PreventiveJobCardTable";
    public static final String PATH = "PREVENTIVE_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 34;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();
    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols
    {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String PREVENTIVE_ID = "preventive_id";
        public static final String ASSET_ID = "preventive_asset_id";
        public static final String ASSET_NAME = "preventive_asset_name";
        public static final String ASSET_AREA = "preventive_asset_area";
        public static final String ASSET_LOCATION = "preventive_asset_location";
        public static final String ASSET_CATEGORY = "preventive_asset_category";
        public static final String ASSET_SUBCATEGORY = "preventive_asset_subcategory";
        public static final String CARD_STATUS = "preventive_card_status";
        public static final String DATE = "preventive_card_date";
    }
}
