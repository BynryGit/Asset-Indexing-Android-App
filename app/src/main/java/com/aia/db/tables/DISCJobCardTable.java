package com.aia.db.tables;
import android.net.Uri;

import com.aia.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the NSCJobCardTable of device database
 *
 * @author Bynry01
 */
public class DISCJobCardTable
{

    public static final String TABLE_NAME = "DISCJobCardTable";
    public static final String PATH = "DISC_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 10;
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
        public static final String DECOMMISSION_ID = "dsc_commission_id";
        public static final String ASSET_ID = "dsc_asset_id";
        public static final String ASSET_NAME = "dsc_asset_name";
        public static final String ASSET_AREA = "dsc_asset_area";
        public static final String ASSET_LOCATION = "dsc_asset_location";
        public static final String ASSET_CATEGORY = "dsc_asset_category";
        public static final String ASSET_SUBCATEGORY = "dsc_asset_subcategory";
        public static final String CARD_STATUS = "dsc_card_status";
        public static final String ASSIGNED_DATE = "assigned_date";
    }
}