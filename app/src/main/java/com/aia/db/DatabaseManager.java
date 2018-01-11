package com.aia.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.aia.db.tables.AssetJobCardTable;
import com.aia.db.tables.LoginTable;
import com.aia.db.tables.NotificationTable;
import com.aia.models.NotificationCard;
import com.aia.models.AssetJobCardModel;
import com.aia.models.UserProfileModel;
import com.aia.utility.AppConstants;
import com.aia.utility.AppPreferences;
import com.aia.utility.CommonUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseManager
{

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
    public static void saveAssetJobCards(Context mContext, ArrayList<AssetJobCardModel> todayModels) {
        for(AssetJobCardModel todayModel : todayModels)
        {
            DatabaseManager.saveAssetJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveAssetJobCardsInfo(Context context, AssetJobCardModel todayModel) {
        if (todayModel != null) {
            ContentValues values = getContentValuesNSCJobCardTable(context, todayModel);
            String condition = AssetJobCardTable.Cols.ASSET_CARD_ID + "='" + todayModel.assetCardId + "'";
            saveValues(context, AssetJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesNSCJobCardTable(Context context, AssetJobCardModel todayModel) {
        ContentValues values = new ContentValues();
        try {
            values.put(AssetJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AssetJobCardTable.Cols.ASSET_CARD_ID, todayModel.assetCardId != null ? todayModel.assetCardId : "");
            values.put(AssetJobCardTable.Cols.ASSET_NAME, todayModel.assetName != null ? todayModel.assetName : "");
            values.put(AssetJobCardTable.Cols.ASSET_CATEGORY,  todayModel.assetCategory != null ? todayModel.assetCategory : "");
            values.put(AssetJobCardTable.Cols.ASSET_MAKE,  todayModel.assetMake != null ? todayModel.assetMake : "");
            values.put(AssetJobCardTable.Cols.ASSET_MAKE_NO,  todayModel.assetMakeNo != null ? todayModel.assetMakeNo : "");
            values.put(AssetJobCardTable.Cols.ASSET_LOCATION,  todayModel.assetLocation != null ?  todayModel.assetLocation : "");
            values.put(AssetJobCardTable.Cols.ASSET_CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE, CommonUtility.getCurrentDate());

        } catch (Exception e) {e.printStackTrace();}

        return values;
    }

    public static ArrayList<AssetJobCardModel> getAssetJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where " + AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<AssetJobCardModel> jobCards = getAssetJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<AssetJobCardModel> getAssetJobCardListFromCursor(Cursor cursor) {
        ArrayList<AssetJobCardModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            AssetJobCardModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getAssetJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static AssetJobCardModel getAssetJobCardFromCursor(Cursor cursor) {
        AssetJobCardModel todayModel = new AssetJobCardModel();
        todayModel.assetCardId = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_ID)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_ID)) : "";
        todayModel.assetName = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_NAME)) : "";
        todayModel.assetCategory = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CATEGORY)) : "";
        todayModel.assetMake = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE)) : "";
        todayModel.assetMakeNo = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE_NO)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE_NO)) : "";
        todayModel.assetLocation = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_LOCATION)) : "";
        todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_STATUS)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) : "";
        return todayModel;
    }

    //  get total count of NSCToday
    public static int getAssetJobCardCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where " +
                AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " +
                AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of NSCToday
    public static int getAssetJobCardCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where " +
                AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " +
                AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus + "' AND " +
                AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // Update NscTable
    public static void updateAssetJobCardStatus(String assetCardId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Update  " + AssetJobCardTable.TABLE_NAME + " set " + AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus +  " where " + AssetJobCardTable.Cols.ASSET_CARD_ID + "='" + assetCardId + "'", null);
        cursor.close();
    }

    // Nsc Deassign
    public static void handleDeassignAssetJobCard(Context mContext, ArrayList<String> deAssignedJobCards, String userId) {
        for (String card_id : deAssignedJobCards) {
            deleteAssetJobCard(mContext, card_id, userId);
        }
    }

    public static void deleteAssetJobCard(Context context, String assetCardId, String userId ) {
        try {
            String condition = AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " + AssetJobCardTable.Cols.ASSET_CARD_ID + "='" + assetCardId + "'";
            context.getContentResolver().delete(AssetJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //     AssetTable Related Methods Ends
}