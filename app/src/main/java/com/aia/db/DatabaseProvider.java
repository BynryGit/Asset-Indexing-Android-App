package com.aia.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.aia.db.tables.BreakDownJobCardTable;
import com.aia.db.tables.DISCJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.MonitoringJobCardTable;
import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.PreventiveJobCardTable;


/**
 * This class provides Content Provider for application database
 *
 * @author Bynry01
 */
public class DatabaseProvider extends ContentProvider
{

    private static final String UNKNOWN_URI = "Unknown URI";

    public static DatabaseHelper dbHelper;


    @Override
    public boolean onCreate()
    {
        dbHelper = new DatabaseHelper(getContext());
        dbHelper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int token = ContentDescriptor.URI_MATCHER.match(uri);

        Cursor result = null;

        switch (token)
        {
            case LoginTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, LoginTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

            case NSCJobCardTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, NSCJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

            case DISCJobCardTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, DISCJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

            case MonitoringJobCardTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, MonitoringJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

            case PreventiveJobCardTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, PreventiveJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

            case BreakDownJobCardTable.PATH_TOKEN:
            {
                result = doQuery(db, uri, BreakDownJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }



        }

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        Uri result = null;

        switch (token)
        {
            case LoginTable.PATH_TOKEN:
            {
                result = doInsert(db, LoginTable.TABLE_NAME,
                        LoginTable.CONTENT_URI, uri, values);
                break;
            }

            case NSCJobCardTable.PATH_TOKEN:
            {
                result = doInsert(db, NSCJobCardTable.TABLE_NAME,
                        NSCJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case DISCJobCardTable.PATH_TOKEN:
            {
                result = doInsert(db, DISCJobCardTable.TABLE_NAME,
                        DISCJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case MonitoringJobCardTable.PATH_TOKEN:
            {
                result = doInsert(db, MonitoringJobCardTable.TABLE_NAME,
                        MonitoringJobCardTable.CONTENT_URI, uri, values);
                break;
            }

            case PreventiveJobCardTable.PATH_TOKEN:
            {
                result = doInsert(db, PreventiveJobCardTable.TABLE_NAME,
                        PreventiveJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN:
            {
                result = doInsert(db, BreakDownJobCardTable.TABLE_NAME,
                        BreakDownJobCardTable.CONTENT_URI, uri, values);
                break;
            }
        }

        if (result == null)
        {
            throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        String table = null;
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        switch (token)
        {
            case LoginTable.PATH_TOKEN:
            {
                table = LoginTable.TABLE_NAME;
                break;
            }
            case NSCJobCardTable.PATH_TOKEN:
            {
                table = NSCJobCardTable.TABLE_NAME;
                break;
            }
            case DISCJobCardTable.PATH_TOKEN:
            {
                table = DISCJobCardTable.TABLE_NAME;
                break;
            }
            case MonitoringJobCardTable.PATH_TOKEN:
            {
                table = MonitoringJobCardTable.TABLE_NAME;
                break;
            }

            case PreventiveJobCardTable.PATH_TOKEN:
            {
                table = PreventiveJobCardTable.TABLE_NAME;
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN:
            {
                table = BreakDownJobCardTable.TABLE_NAME;
                break;
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        for (ContentValues cv : values)
        {
            db.insert(table, null, cv);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return values.length;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token)
        {
            case LoginTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, LoginTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case NSCJobCardTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, NSCJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case DISCJobCardTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, DISCJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case MonitoringJobCardTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, MonitoringJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }

            case PreventiveJobCardTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, PreventiveJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN:
            {
                result = doDelete(db, uri, BreakDownJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token)
        {
            case LoginTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, LoginTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case NSCJobCardTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, NSCJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case DISCJobCardTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, DISCJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case MonitoringJobCardTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, MonitoringJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }

            case PreventiveJobCardTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, PreventiveJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN:
            {
                result = doUpdate(db, uri, BreakDownJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
        }

        return result;
    }

    /**
     * Performs query to specified table using the projection, selection and
     * sortOrder
     *
     * @param db            SQLiteDatabase instance
     * @param uri           ContentUri for watch
     * @param tableName     Name of table on which query has to perform
     * @param projection    needed projection
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param sortOrder     sort order if necessary
     * @return Cursor cursor from the table tableName matching all the criterion
     */
    private Cursor doQuery(SQLiteDatabase db, Uri uri, String tableName,
                           String[] projection, String selection, String[] selectionArgs,
                           String sortOrder)
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tableName);
        Cursor result = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    /**
     * performs update to the specified table row or rows
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param values        content values to update
     * @return success or failure
     */
    private int doUpdate(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs, ContentValues values)
    {
        int result = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * deletes the row/rows from the table
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @return success or failure
     */
    private int doDelete(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs)
    {
        int result = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * insert rows to the specified table
     *
     * @param db         SQLiteDatabase instance
     * @param tableName  Name of table on which query has to perform
     * @param contentUri ContentUri to build the path
     * @param uri        uri of the content that was changed
     * @param values     content values to update
     * @return success or failure
     */
    private Uri doInsert(SQLiteDatabase db, String tableName, Uri contentUri,
                         Uri uri, ContentValues values)
    {
        long id = db.insert(tableName, null, values);
        Uri result = contentUri.buildUpon().appendPath(String.valueOf(id))
                .build();
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}