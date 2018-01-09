package com.aia.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.NotificationTable;

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
        createNotificationTable(db);
        createNSCJobCardTable(db);
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
        String NSCJobCardTableFields = NSCJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
        createTable(db, NSCJobCardTable.TABLE_NAME, NSCJobCardTableFields);
    }

    private void createNotificationTable(SQLiteDatabase db)
    {
        String notificationTableFields = NotificationTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotificationTable.Cols.TITLE+ " VARCHAR, " +
                NotificationTable.Cols.MSG + " VARCHAR, " +
                NotificationTable.Cols.DATE + " VARCHAR, " +
                NotificationTable.Cols.IS_READ + " VARCHAR " ;

        createTable(db, NotificationTable.TABLE_NAME, notificationTableFields);
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