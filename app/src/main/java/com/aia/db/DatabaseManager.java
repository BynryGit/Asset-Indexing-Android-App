package com.aia.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.aia.db.tables.BreakDownJobCardTable;
import com.aia.db.tables.DISCJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.MonitoringJobCardTable;
import com.aia.db.tables.NSCJobCardTable;
import com.aia.db.tables.PreventiveJobCardTable;
import com.aia.models.HistoryModel;
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

    public static String getDbPath(Context context) {
        return context.getDatabasePath("AIA.db").getAbsolutePath();
    }

    // LoginTable insertion
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
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
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


// NSC job card insertion
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

        } catch (Exception e) {
            e.printStackTrace();
        }
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



// DSC Job card Insertion
    public static void saveDSCJobCards(Context mContext, ArrayList<TodayModel> nsc_today) {
        for (TodayModel todayModel : nsc_today)
            DatabaseManager.savDSCJobCardsInfo(mContext, todayModel);
    }

    private static void savDSCJobCardsInfo(Context context, TodayModel nsc_today) {
        if (nsc_today != null) {
            ContentValues values = getContentValuesDSCJobCardTable(context, nsc_today);
            String condition = DISCJobCardTable.Cols.DECOMMISSION_ID + "='" + nsc_today.decommission_id + "'";
            saveValues(context, DISCJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesDSCJobCardTable(Context context, TodayModel nsc_today) {
        ContentValues values = new ContentValues();
        try {
            values.put(DISCJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(DISCJobCardTable.Cols.ASSET_ID, nsc_today.asset_id != null ? nsc_today.asset_id : "");
            values.put(DISCJobCardTable.Cols.ASSET_NAME,  nsc_today.asset_name != null ?  nsc_today.asset_name : "");
            values.put(DISCJobCardTable.Cols.DECOMMISSION_ID,  nsc_today.decommission_id != null ?  nsc_today.decommission_id : "");
            values.put(DISCJobCardTable.Cols.ASSET_AREA,  nsc_today.area != null ?  nsc_today.area : "");
            values.put(DISCJobCardTable.Cols.ASSET_LOCATION,  nsc_today.location != null ?  nsc_today.location : "");
            values.put(DISCJobCardTable.Cols.ASSET_CATEGORY,  nsc_today.category != null ? nsc_today.category : "");
            values.put(DISCJobCardTable.Cols.ASSET_SUBCATEGORY,  nsc_today.subcategory != null ? nsc_today.subcategory : "");
            values.put(DISCJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(DISCJobCardTable.Cols.ASSIGNED_DATE, CommonUtility.getCurrentDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getDISCJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DISCJobCardTable.TABLE_NAME + " where " + DISCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + DISCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getDSCJobCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getDSCJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getDSCJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getDSCJobCardFromCursor(Cursor cursor) {
        TodayModel dscTodayCards = new TodayModel();
        dscTodayCards.asset_id = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_ID)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_ID)) : "";
        dscTodayCards.decommission_id = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.DECOMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.DECOMMISSION_ID)) : "";
        dscTodayCards.asset_name = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_NAME)) : "";
        dscTodayCards.area = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_AREA)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_AREA)) : "";
        dscTodayCards.location = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_LOCATION)) : "";
        dscTodayCards.category = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_CATEGORY)) : "";
        dscTodayCards.subcategory = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSET_SUBCATEGORY)) : "";
        dscTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(DISCJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return dscTodayCards;
    }

    //  get total count of DISCToday
    public static int getDISCTodayCount(String userId,String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DISCJobCardTable.TABLE_NAME + " where " + DISCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + DISCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //     Monitoring job card insertion
    public static void saveMonitoringJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for(TodayModel todayModel : todayModels)
            DatabaseManager.saveMonitoringJobCardsInfo(mContext, todayModel);
    }

    private static void saveMonitoringJobCardsInfo(Context context, TodayModel nsc_today) {
        if (nsc_today != null) {
            ContentValues values = getContentValuesMonitoringJobCardTable(context, nsc_today);
            String condition = MonitoringJobCardTable.Cols.MONITORING_ID + "='" + nsc_today.monitoring_id + "'";
            saveValues(context, MonitoringJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesMonitoringJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();
        try {
            values.put(MonitoringJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(MonitoringJobCardTable.Cols.ASSET_ID, todayModels.asset_id != null ? todayModels.asset_id : "");
            values.put(MonitoringJobCardTable.Cols.ASSET_NAME,  todayModels.asset_name != null ?  todayModels.asset_name : "");
            values.put(MonitoringJobCardTable.Cols.MONITORING_ID,  todayModels.monitoring_id != null ?  todayModels.monitoring_id : "");
            values.put(MonitoringJobCardTable.Cols.ASSET_AREA,  todayModels.area != null ?  todayModels.area : "");
            values.put(MonitoringJobCardTable.Cols.ASSET_LOCATION,  todayModels.location != null ?  todayModels.location : "");
            values.put(MonitoringJobCardTable.Cols.ASSET_CATEGORY,  todayModels.category != null ? todayModels.category : "");
            values.put(MonitoringJobCardTable.Cols.ASSET_SUBCATEGORY,  todayModels.subcategory != null ? todayModels.subcategory : "");
            values.put(MonitoringJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(MonitoringJobCardTable.Cols.ASSIGNED_DATE, CommonUtility.getCurrentDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getMonitoringJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MonitoringJobCardTable.TABLE_NAME + " where " + MonitoringJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MonitoringJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getMonitoringJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getMonitoringJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getMonitoringJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getMonitoringJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.asset_id = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_ID)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_ID)) : "";
        todayModel.monitoring_id = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.MONITORING_ID)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.MONITORING_ID)) : "";
        todayModel.asset_name = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_NAME)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_AREA)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_AREA)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_LOCATION)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSET_SUBCATEGORY)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(MonitoringJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return todayModel;
    }

    //  get total count of Monitoring Today
    public static int getMonitoringTodayCount(String userId,String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MonitoringJobCardTable.TABLE_NAME + " where " + MonitoringJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MonitoringJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    // Preventive Card Insertion

    public static void savePreventiveJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        DatabaseManager.savePreventiveJobCardsInfo(mContext, todayModels);
    }

    private static void savePreventiveJobCardsInfo(Context context, ArrayList<TodayModel> preventive_today) {
        if (preventive_today != null) {
            ContentValues values = getContentValuesPreventiveJobCardTable(context, preventive_today);
            saveValues(context, PreventiveJobCardTable.CONTENT_URI, values, null);
        }
    }

    private static ContentValues getContentValuesPreventiveJobCardTable(Context context, ArrayList<TodayModel> todayModels) {
        ContentValues values = new ContentValues();
        String date = CommonUtility.getCurrentDate();
        try {
            for(int i = 0; i < todayModels.size(); i++) {
                values.put(PreventiveJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
                values.put(PreventiveJobCardTable.Cols.ASSET_ID, todayModels.get(i).asset_id != null ? todayModels.get(i).asset_id : "");
                values.put(PreventiveJobCardTable.Cols.ASSET_NAME,  todayModels.get(i).asset_name != null ?  todayModels.get(i).asset_name : "");
                values.put(PreventiveJobCardTable.Cols.PREVENTIVE_ID,  todayModels.get(i).preventive_id != null ?  todayModels.get(i).preventive_id : "");
                values.put(PreventiveJobCardTable.Cols.ASSET_AREA,  todayModels.get(i).area != null ?  todayModels.get(i).area : "");
                values.put(PreventiveJobCardTable.Cols.ASSET_LOCATION,  todayModels.get(i).location != null ?  todayModels.get(i).location : "");
                values.put(PreventiveJobCardTable.Cols.ASSET_CATEGORY,  todayModels.get(i).category != null ? todayModels.get(i).category : "");
                values.put(PreventiveJobCardTable.Cols.ASSET_SUBCATEGORY,  todayModels.get(i).subcategory != null ? todayModels.get(i).subcategory : "");
                values.put(PreventiveJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
                values.put(PreventiveJobCardTable.Cols.DATE, date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getPreventiveJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where " + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getPreventiveJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getPreventiveJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel preventive_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                preventive_today_cards = getPreventiveJobCardFromCursor(cursor);
                jobCards.add(preventive_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getPreventiveJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.asset_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_ID)) : "";
        todayModel.preventive_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) : "";
        todayModel.asset_name = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_NAME)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_AREA)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_AREA)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_LOCATION)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ASSET_SUBCATEGORY)) : "";
        return todayModel;
    }
//BreakDown Insertion

    public static void saveBreakdownJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        DatabaseManager.saveBreakdownJobCardsInfo(mContext, todayModels);
    }

    private static void saveBreakdownJobCardsInfo(Context context, ArrayList<TodayModel> breakdown_today) {
        if (breakdown_today != null) {
            ContentValues values = getContentValuesBreakdownJobCardTable(context, breakdown_today);
            saveValues(context, BreakDownJobCardTable.CONTENT_URI, values, null);
        }
    }

    private static ContentValues getContentValuesBreakdownJobCardTable(Context context, ArrayList<TodayModel> todayModels) {
        ContentValues values = new ContentValues();
        String date = CommonUtility.getCurrentDate();
        try {
            for(int i = 0; i < todayModels.size(); i++) {
                values.put(BreakDownJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
                values.put(BreakDownJobCardTable.Cols.ASSET_ID, todayModels.get(i).asset_id != null ? todayModels.get(i).asset_id : "");
                values.put(BreakDownJobCardTable.Cols.ASSET_NAME,  todayModels.get(i).asset_name != null ?  todayModels.get(i).asset_name : "");
                values.put(BreakDownJobCardTable.Cols.BREAKDOWN_ID,  todayModels.get(i).breakdown_id != null ?  todayModels.get(i).breakdown_id : "");
                values.put(BreakDownJobCardTable.Cols.ASSET_AREA,  todayModels.get(i).area != null ?  todayModels.get(i).area : "");
                values.put(BreakDownJobCardTable.Cols.ASSET_LOCATION,  todayModels.get(i).location != null ?  todayModels.get(i).location : "");
                values.put(BreakDownJobCardTable.Cols.ASSET_CATEGORY,  todayModels.get(i).category != null ? todayModels.get(i).category : "");
                values.put(BreakDownJobCardTable.Cols.ASSET_SUBCATEGORY,  todayModels.get(i).subcategory != null ? todayModels.get(i).subcategory : "");
                values.put(BreakDownJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
                values.put(BreakDownJobCardTable.Cols.DATE, date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getBreakdownJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where " + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getBreakdownJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getBreakdownJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel breakdown_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                breakdown_today_cards = getBreakdownJobCardFromCursor(cursor);
                jobCards.add(breakdown_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getBreakdownJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.asset_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_ID)) : "";
        todayModel.preventive_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) : "";
        todayModel.asset_name = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_NAME)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_AREA)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_AREA)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_LOCATION)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ASSET_SUBCATEGORY)) : "";
        return todayModel;
    }



// Get NSC HISTORY CARDS

    public static ArrayList<HistoryModel> getNSCHistoryJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " +NSCJobCardTable.TABLE_NAME + " where " + NSCJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + NSCJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' GROUP BY "
                + NSCJobCardTable.Cols.ASSIGNED_DATE +  "", null);
        ArrayList<HistoryModel> historyCards = getHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }
    private static ArrayList<HistoryModel> getHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel nsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_history_cards = getHistoryJobCardListCursor(cursor);
                jobCards.add(nsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }
    private static HistoryModel getHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(NSCJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return historyModel;
    }



}





