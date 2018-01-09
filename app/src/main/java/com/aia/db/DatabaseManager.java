package com.aia.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.aia.db.tables.LoginTable;
import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.NotificationTable;
import com.aia.models.NotificationCard;
import com.aia.models.TodayModel;
import com.aia.models.UserProfileModel;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class acts as an interface between database and UI. It contains all the
 * methods to interact with device database.
 *
 * @author Bynry01
 */
public class DatabaseManager {
    /**
     * Save User to Login table
     *
     * @param context Context

     */
    public static int  cnt;
    public static String pattern = "EEE, d MMM yyyy, HH:mm";

    public static String getDbPath(Context context) {
        return context.getDatabasePath("AIA.db").getAbsolutePath();
    }

    //     LoginTable Related Methods Starts
    public static void saveLoginDetails(Context loginActivity, UserProfileModel user_info) {
        DatabaseManager.saveUserInfo(loginActivity, user_info);
    }

    private static void saveUserInfo(Context context, UserProfileModel userProfilesModel) {
        if (userProfilesModel != null) {
            ContentValues values = getContentValuesUserInfoTable(userProfilesModel);
            saveValues(context, LoginTable.CONTENT_URI, values, null);
        }
    }

    private static void saveValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private static ContentValues getContentValuesUserInfoTable(UserProfileModel userProfileModel) {
        ContentValues values = new ContentValues();
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(new Date());
            values.put(LoginTable.Cols.USER_ID, userProfileModel.user_id != null ? userProfileModel.user_id : "");
            values.put(LoginTable.Cols.USER_NAME, userProfileModel.user_name != null ? userProfileModel.user_name : "");
            values.put(LoginTable.Cols.USER_LOGIN_ID, userProfileModel.emp_id != null ? userProfileModel.emp_id : "");
            values.put(LoginTable.Cols.USER_CITY, userProfileModel.city != null ? userProfileModel.city : "");
            values.put(LoginTable.Cols.USER_EMAIL, userProfileModel.email_id != null ? userProfileModel.email_id : "");
            values.put(LoginTable.Cols.USER_CONTACT_NO, userProfileModel.contact_no != null ? userProfileModel.contact_no : "");
            values.put(LoginTable.Cols.USER_ADDRESS, userProfileModel.address != null ? userProfileModel.address : "");
            values.put(LoginTable.Cols.EMP_TYPE, userProfileModel.emp_type != null ? userProfileModel.emp_type : "");
            values.put(LoginTable.Cols.LOGIN_DATE,  date);
            values.put(LoginTable.Cols.LOGIN_LAT,  "");
            values.put(LoginTable.Cols.LOGIN_LNG,  "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    //     LoginTable Related Methods Ends

    //     NotificationTable Related Methods Starts
    public static void saveNotification(Context context, NotificationCard noti) {
        if (noti != null) {
            ContentValues values = new ContentValues();
            try {
                values.put(NotificationTable.Cols.TITLE, noti.title);
                values.put(NotificationTable.Cols.MSG, noti.message);
                values.put(NotificationTable.Cols.DATE, noti.date);
                values.put(NotificationTable.Cols.IS_READ, "false");
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long newRowId = db.insert(NotificationTable.TABLE_NAME, null, values);
        }
    }

    public static void setReadNotification(Context context, String title) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationTable.Cols.IS_READ, "true");
        String[] args = new String[]{title};
        db.update(NotificationTable.TABLE_NAME, values, "title=?", args);
    }

    public static int getCount(Context context, String flag) {
        int i = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + NotificationTable.TABLE_NAME + " where  " + NotificationTable.Cols.IS_READ + " = '" + flag + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<NotificationCard> noti = new ArrayList<NotificationCard>();

        while (c.moveToNext()) {
            i++;
            NotificationCard notiCard = new NotificationCard();
            notiCard.title = c.getString(c.getColumnIndex("title"));
            notiCard.message = c.getString(c.getColumnIndex("msg"));
            notiCard.date = c.getString(c.getColumnIndex("date"));
            notiCard.is_read = c.getString(c.getColumnIndex("is_read"));

            noti.add(notiCard);
        }
        db.close();
        return i;
    }

    public static ArrayList<NotificationCard> getAllNotification(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + NotificationTable.TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<NotificationCard> noti = new ArrayList<NotificationCard>();

        while (c.moveToNext()) {
            NotificationCard notiCard = new NotificationCard();
            notiCard.title = c.getString(c.getColumnIndex("title"));
            notiCard.message = c.getString(c.getColumnIndex("msg"));
            notiCard.date = c.getString(c.getColumnIndex("date"));
            notiCard.is_read = c.getString(c.getColumnIndex("is_read"));
            noti.add(notiCard);
        }
        db.close();
        return noti;
    }

    public static void deleteAccount(Context context, String messageBody) {
        try {
            String condition = NotificationTable.Cols.MSG + "='" + messageBody + "'";
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("Notification", condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //     NotificationTable Related Methods Ends

    //     AssetTable Related Methods Starts
    public static void saveNSCJobCards(Context mContext, ArrayList<TodayModel> nsc_today) {
        for(TodayModel todayModel : nsc_today)
        {
            DatabaseManager.saveNSCJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveNSCJobCardsInfo(Context context, TodayModel nsc_today) {
        if (nsc_today != null) {
            ContentValues values = getContentValuesNSCJobCardTable(context, nsc_today);
            String condition = NSCJobCardTable.Cols.COMMISSION_ID + "='" + nsc_today.commission_id + "'";
            saveValues(context, NSCJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesNSCJobCardTable(Context context, TodayModel nsc_today) {
        ContentValues values = new ContentValues();
        try {
            values.put(NSCJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(NSCJobCardTable.Cols.ASSET_ID, nsc_today.asset_id != null ? nsc_today.asset_id : "");
            values.put(NSCJobCardTable.Cols.ASSET_NAME,  nsc_today.asset_name != null ?  nsc_today.asset_name : "");
            values.put(NSCJobCardTable.Cols.COMMISSION_ID,  nsc_today.commission_id != null ?  nsc_today.commission_id : "");
            values.put(NSCJobCardTable.Cols.ASSET_AREA,  nsc_today.area != null ?  nsc_today.area : "");
            values.put(NSCJobCardTable.Cols.ASSET_LOCATION,  nsc_today.location != null ?  nsc_today.location : "");
            values.put(NSCJobCardTable.Cols.ASSET_CATEGORY,  nsc_today.category != null ? nsc_today.category : "");
            values.put(NSCJobCardTable.Cols.ASSET_SUBCATEGORY,  nsc_today.subcategory != null ? nsc_today.subcategory : "");
            values.put(NSCJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(NSCJobCardTable.Cols.ASSIGNED_DATE, CommonUtility.getCurrentDate());

        } catch (Exception e) {e.printStackTrace();}

        return values;
    }

    public static ArrayList<TodayModel> getNSCJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + NSCJobCardTable.TABLE_NAME + " where " + NSCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + NSCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getJobCardFromCursor(Cursor cursor) {
        TodayModel nscTodayCards = new TodayModel();
        nscTodayCards.asset_id = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_ID)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_ID)) : "";
        nscTodayCards.commission_id = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.COMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.COMMISSION_ID)) : "";
        nscTodayCards.asset_name = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_NAME)) : "";
        nscTodayCards.area = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_AREA)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_AREA)) : "";
        nscTodayCards.location = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_LOCATION)) : "";
        nscTodayCards.category = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_CATEGORY)) : "";
        nscTodayCards.subcategory = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSET_SUBCATEGORY)) : "";
        nscTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return nscTodayCards;
    }

    //  get total count of NSCToday
    public static int getNSCTodayCount(String userId,String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + NSCJobCardTable.TABLE_NAME + " where " + NSCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + NSCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of NSCToday
    public static int getNSCTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + NSCJobCardTable.TABLE_NAME + " where " + NSCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + NSCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + NSCJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // Update NscTable
    public static void updateNSCCardStatus(String commissionid,String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Update  " + NSCJobCardTable.TABLE_NAME + " set " + NSCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus +  " where " + NSCJobCardTable.Cols.COMMISSION_ID + "='" + commissionid + "'", null);
        cursor.close();
    }

    // Nsc Deassign
    public static void handleReassignDeassignNSC(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardNSC(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardNSC(Context context, String card_id, String user_id ) {
        try {
            String condition = NSCJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND " + NSCJobCardTable.Cols.COMMISSION_ID + "='" + card_id + "'";
            context.getContentResolver().delete(NSCJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //     AssetTable Related Methods Ends
}