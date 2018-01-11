package com.aia.webservices;


public class ApiConstants
{

    //  public static final String DOMAIN_URL = "http://192.168.1.44:8006"; //  Priyanka Local
    public static final String DOMAIN_URL = "http://192.168.10.100:8000"; //  Vikas Local

    public static final String BASE_URL = DOMAIN_URL + "/api/";

//    TODO Please Make Sure that While creating signed APK make this value to "1"
//    TODO Check DOMAIN_URL, Check IMEI no in LoginActivity, Check All Log files(Log.d, Log.i, etc)

    public static final int LOG_STATUS = 0;

    // All URL's
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String GET_ASSIGNED_ASSET_CARD_URL = BASE_URL + "get-asset-card/";
    public static final String GET_DE_ASSIGNED_ASSET_CARD_URL = BASE_URL + "reassigned-deassigned-asset-card/";


    //All URL constants
    public static final String LOGIN = "1";
    public static final String GET_ASSIGNED_ASSET_CARD = "2";
    public static final String GET_DE_ASSIGNED_ASSET_CARD = "3";

}
