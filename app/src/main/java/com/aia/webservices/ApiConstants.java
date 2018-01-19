package com.aia.webservices;


public class ApiConstants
{

    //  public static final String DOMAIN_URL = "http://192.168.1.44:8006"; //  Priyanka Local
    public static final String DOMAIN_URL = "http://192.168.10.103:8000"; //  Vikas Local

    public static final String BASE_URL = DOMAIN_URL + "/api/";


//    TODO Please Make Sure that While creating signed APK make this value to "1"
//    TODO Check DOMAIN_URL, Check IMEI no in LoginActivity, Check All Log files(Log.d, Log.i, etc)

    public static final int LOG_STATUS = 0;

    // All URL's
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String UPDATE_FCM_TOKEN_URL = BASE_URL + "fcm-token/";
    public static final String GET_ASSIGNED_ASSET_CARD_URL = BASE_URL + "get-asset-card/";
    public static final String GET_DE_ASSIGNED_ASSET_CARD_URL = BASE_URL + "reassigned-deassigned-asset-card/";
    public static final String GET_ASSET_DETAILS_URL = BASE_URL + "get-asset-details/";
    public static final String GET_SUB_DIVISION_URL = BASE_URL + "get-sub-division/";
    public static final String GET_AREA_URL = BASE_URL + "get-area/";
    public static final String GET_LOCATION_URL = BASE_URL + "get-location/";
    public static final String UPLOAD_ASSET_DETAILS_URL = BASE_URL + "upload-asset-details/";
    public static final String UPLOAD_ASSET_IMAGE_URL = BASE_URL + "save-asset-image/";


    //All URL constants
    public static final String LOGIN = "1";
    public static final String UPDATE_FCM_TOKEN = "2";
    public static final String GET_ASSIGNED_ASSET_CARD = "3";
    public static final String GET_DE_ASSIGNED_ASSET_CARD = "4";
    public static final String GET_ASSET_DETAILS = "5";
    public static final String GET_SUB_DIVISION = "6";
    public static final String GET_AREA = "7";
    public static final String GET_LOCATION = "8";
    public static final String UPLOAD_ASSET_DETAILS = "9";
    public static final String UPLOAD_ASSET_IMAGE = "10";

}
