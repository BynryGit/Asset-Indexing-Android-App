package com.aia.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aia.db.tables.BreakDownJobCardTable;
import com.aia.db.tables.DISCJobCardTable;
import com.aia.db.tables.MonitoringJobCardTable;
import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.PreventiveJobCardTable;

import java.text.MessageFormat;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String KEY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {0} ({1})";
    public static final String KEY_DROP_TABLE = "DROP TABLE IF EXISTS {0}";
    public final static String SQL = "SELECT COUNT(*) FROM sqlite_master WHERE name=?";
    public static final String TAG = "DatabaseHelper";
    private static final int CURRENT_DB_VERSION = 1;
    private static final String DB_NAME = "AIA.db";
    private static final String DROP_RECORD_TRIGGER = "drop_records";

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createLoginTable(db);
        createNSCJobCardTable(db);
        createDISCJobCardTable(db);
        createMonitoringJobCardTable(db);
        createPreventiveJobCardTable(db);
        createBreakdownJobCardTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
//      dropTable(sqLiteDatabase, LoginTable.TABLE_NAME);
    }

    private void createLoginTable(SQLiteDatabase db)
    {
        String loginTableFields = LoginTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LoginTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                LoginTable.Cols.USER_ID + " VARCHAR, " +
                LoginTable.Cols.USER_NAME + " VARCHAR, " +
                LoginTable.Cols.USER_CITY + " VARCHAR, " +
                LoginTable.Cols.USER_CONTACT_NO + " VARCHAR, " +
                LoginTable.Cols.USER_EMAIL + " VARCHAR, " +
                LoginTable.Cols.EMP_TYPE + " VARCHAR, " +
                LoginTable.Cols.USER_ADDRESS + " VARCHAR, " +
                LoginTable.Cols.LOGIN_DATE + " VARCHAR, " +
                LoginTable.Cols.LOGIN_LAT + " VARCHAR, " +
                LoginTable.Cols.LOGIN_LNG + " VARCHAR";
        createTable(db, LoginTable.TABLE_NAME, loginTableFields);
    }

    private void createNSCJobCardTable(SQLiteDatabase db)
    {
        String NSCjobCardTableFields = NSCJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NSCJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_ID + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                NSCJobCardTable.Cols.COMMISSION_ID + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_SUBCATEGORY + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_AREA + " VARCHAR, " +
                NSCJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSET_LOCATION + " VARCHAR, " +
                NSCJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";
        createTable(db, NSCJobCardTable.TABLE_NAME, NSCjobCardTableFields);
    }

    private void createDISCJobCardTable(SQLiteDatabase db) {
        String DISCjobCardTableFields = DISCJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DISCJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_ID + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                DISCJobCardTable.Cols.DECOMMISSION_ID + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_SUBCATEGORY + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_AREA + " VARCHAR, " +
                DISCJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSET_LOCATION + " VARCHAR, " +
                DISCJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";

        createTable(db, DISCJobCardTable.TABLE_NAME, DISCjobCardTableFields);
    }

    private void createMonitoringJobCardTable(SQLiteDatabase db) {
        String MJobCardTableFields = MonitoringJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MonitoringJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_ID + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                MonitoringJobCardTable.Cols.MONITORING_ID + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_SUBCATEGORY + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_AREA + " VARCHAR, " +
                MonitoringJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSET_LOCATION + " VARCHAR, " +
                MonitoringJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";
        createTable(db, MonitoringJobCardTable.TABLE_NAME, MJobCardTableFields);
    }

    private void createPreventiveJobCardTable(SQLiteDatabase db) {
        String PrevJobCardTableFields = PreventiveJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PreventiveJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                PreventiveJobCardTable.Cols.PREVENTIVE_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_SUBCATEGORY + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_AREA + " VARCHAR, " +
                PreventiveJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                PreventiveJobCardTable.Cols.DATE + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ASSET_LOCATION + " VARCHAR";
        createTable(db, PreventiveJobCardTable.TABLE_NAME, PrevJobCardTableFields);
    }

    private void createBreakdownJobCardTable(SQLiteDatabase db) {
        String BreakdownJobCardTableFields = BreakDownJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BreakDownJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                BreakDownJobCardTable.Cols.BREAKDOWN_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_SUBCATEGORY + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_AREA + " VARCHAR, " +
                BreakDownJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                BreakDownJobCardTable.Cols.DATE + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ASSET_LOCATION + " VARCHAR";
        createTable(db, BreakDownJobCardTable.TABLE_NAME, BreakdownJobCardTableFields);
    }

    public void dropTable(SQLiteDatabase db, String name)
    {
        String query = MessageFormat.format(DatabaseHelper.KEY_DROP_TABLE, name);
        db.execSQL(query);
    }

    public static boolean exists(SQLiteDatabase db, String name)
    {
        Cursor cur = db.rawQuery(SQL, new String[]{name});
        cur.moveToFirst();
        int tables = cur.getInt(0);
        if (tables > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void createTable(SQLiteDatabase db, String name, String fields)
    {
        String query = MessageFormat.format(DatabaseHelper.KEY_CREATE_TABLE,
                name, fields);
        db.execSQL(query);
    }
}