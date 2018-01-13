package com.aia.utility;

public class AppConstants
{

    // All static app constants are here except String , array of strings, color, dimentions etc

    public static final String SHARED_PREF = "mAppNameHere";

    public static String DEVICE_FCM_TOKEN ="DEVICE_FCM_TOKEN";
    public static final String IS_PERMISSION_DLG_SHOWING = "IS_PERMISSION_DLG_SHOWING";

    //Constant Integer Values
    public static final int GET_ACCOUNTS_PERMISSION = 1;
    public static final int PERMISSION_WRITE_STORAGE = 2;
    public static final int PERMISSION_FINE_LOCATION = 3;
    public static final int PERMISSION_READ_STORAGE = 4;
    public static final int PERMISSION_CAMERA = 5;
    public static int ALL_PERMISSIONS_RESULT = 55;
    public static int IMAGE_WIDTH = 960;
    public static int IMAGE_HEIGHT = 720;
    public static int CAMERA_RESULT_CODE = 1000;
    public static final int RESULT_OK = -1;
    public static int ZERO = 0;
    public static int ONE = 1;

    //Constant for cropped image
    public static final String CROPPED_IMAGE = "croppedImage";
    public static final String UPLOAD_FOLDER = "/AIA/Upload";
    public static final String VIDEO_PATH = UPLOAD_FOLDER + "/Video";
    public static final String IMAGE_PATH = UPLOAD_FOLDER + "/Image";
    public static final String BLANK_STRING = "";


    //Constant String Values
    public static String CARD_STATUS_OPEN = "Open";
    public static String CARD_STATUS_COMPLETED = "Completed";

    //String Values
    public static String MOBILE_NO = "mobile_no";
    public static String USER_NAME = "user_name";
    public static String USER_CITY = "user_city";
    public static String EMP_ID = "emp_id";
    public static String PROFILE_IMAGE_URL = "image_url";
    public static String AUTH_TOKEN = "auth_token";

    public static String JOB_CARD_ID = "job_card_id";
}
