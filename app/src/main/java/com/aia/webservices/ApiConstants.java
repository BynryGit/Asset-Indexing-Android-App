package com.aia.webservices;


public class ApiConstants
{

//    public static final String DOMAIN_URL = "http://192.168.1.44:8006"; //  Priyanka Local
    public static final String DOMAIN_URL = "http://192.168.10.100:8000"; //  Vikas Local

    public static final String BASE_URL = DOMAIN_URL + "/api/";

    public static final int LOG_STATUS = 0; // TODO Please Make Sure that While creating signed APK make this value to "1"

    // All URL's
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String GET_ASSET_CARD_URL = BASE_URL + "get-asset-card/";


    //All URL constants
    public static final String LOGIN = "1";
    public static final String GET_ASSET_CARD = "2";

}
