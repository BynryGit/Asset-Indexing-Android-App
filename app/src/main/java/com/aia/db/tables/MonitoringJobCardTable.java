package com.aia.db.tables;

import android.net.Uri;

import com.aia.db.ContentDescriptor;

/**
 * Created by Piyush on 04-01-2018.
 * Bynry
 */
public class MonitoringJobCardTable
{
    public static final String TABLE_NAME = "MonitoringJobCardTable";
    public static final String PATH = "MONITORING_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 30;
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
        public static final String MONITORING_ID = "monitoring_id";
        public static final String ASSET_ID = "asset_id";
        public static final String ASSET_NAME = "asset_name";
        public static final String ASSET_AREA = "asset_area";
        public static final String ASSET_LOCATION = "asset_location";
        public static final String ASSET_CATEGORY = "asset_category";
        public static final String ASSET_SUBCATEGORY = "asset_subcategory";
        public static final String CARD_STATUS = "card_status";
        public static final String ASSIGNED_DATE = "assigned_date";
    }
}