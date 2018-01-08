package com.aia.db.tables;

import android.net.Uri;

import com.aia.db.ContentDescriptor;

/**
 * Created by Piyush on 04-01-2018.
 * Bynry
 */
public class BreakDownJobCardTable
{
    public static final String TABLE_NAME = "BreakDownJobCardTable";
    public static final String PATH = "BREAKDOWN_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 35;
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
        public static final String BREAKDOWN_ID = "breakdown_id";
        public static final String ASSET_ID = "breakdown_asset_id";
        public static final String ASSET_NAME = "breakdown_asset_name";
        public static final String ASSET_AREA = "breakdown_asset_area";
        public static final String ASSET_LOCATION = "breakdown_asset_location";
        public static final String ASSET_CATEGORY = "breakdown_asset_category";
        public static final String ASSET_SUBCATEGORY = "breakdown_asset_subcategory";
        public static final String CARD_STATUS = "breakdown_card_status";
        public static final String DATE = "breakdown_card_date";
    }
}
