package com.aia.db;

import android.content.UriMatcher;
import android.net.Uri;

import com.aia.db.tables.BreakDownJobCardTable;
import com.aia.db.tables.DISCJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.MonitoringJobCardTable;
import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.PreventiveJobCardTable;


/**
 * This class contains description about
 * application database content providers
 *
 * @author Bynry01
 */
public class ContentDescriptor
{

    public static final String AUTHORITY = "com.aia";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();


    /**
     * @return UriMatcher for database table Uris
     */
    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, LoginTable.PATH, LoginTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, NSCJobCardTable.PATH, NSCJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, DISCJobCardTable.PATH, DISCJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, MonitoringJobCardTable.PATH, MonitoringJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, PreventiveJobCardTable.PATH, PreventiveJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, BreakDownJobCardTable.PATH, BreakDownJobCardTable.PATH_TOKEN);
        return matcher;
    }
}