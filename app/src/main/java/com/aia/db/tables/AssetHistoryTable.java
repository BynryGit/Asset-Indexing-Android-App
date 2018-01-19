package com.aia.db.tables;

import android.net.Uri;

import com.aia.db.ContentDescriptor;

/**
 * Created by Piyush on 16-01-2018.
 * Bynry
 */
public class AssetHistoryTable
{
    public static final String TABLE_NAME = "AssetHistoryTable";
    public static final String PATH = "ASSET_HISTORY_TABLE";
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
        public static final String TODAY_DATE = "today_date";
        public static final String OPEN_COUNT = "open_count";
        public static final String COMPLETED_COUNT = "completed_count";
    }
}
