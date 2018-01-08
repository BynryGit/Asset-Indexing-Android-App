package com.aia.webservices;


public class ApiConstants
{

    public static final String DOMAIN_URL = "http://192.168.1.44:8006"; //  Priyanka Local

    public static final String BASE_URL = DOMAIN_URL + "/api/";

    public static final int LOG_STATUS = 0; // TODO Please Make Sure that While creating signed APK make this value to "1"

    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String LOGOUT_URL = BASE_URL + "logout";
    public static final String GET_CATEGORY_URL = BASE_URL + "categories/";
    public static final String GET_SUB_CATEGORY_URL = BASE_URL + "sub-categories/";
    public static final String GET_ASSETS_URL = BASE_URL + "asset/";
    public static final String FCM_DEVICE_TOKEN_URL = BASE_URL + "fcm-token/";
    public static final String NSC_TODAY_CARDS_URL = BASE_URL + "new-commissioning-card/";
    public static final String DISC_TODAY_CARDS_URL = BASE_URL + "new-decommissioning-card/";
    public static final String COMMISSION_ASSET_DETAILS_URL = BASE_URL + "commission_detail/";
    public static final String DECOMMISSION_ASSET_DETAILS_URL = BASE_URL + "decommission_detail/";
    public static final String UPLOAD_COMMISSION_ASSET_DETAILS_URL = BASE_URL + "upload_commissioning_asset_check/";
    public static final String UPLOAD_DECOMMISSION_ASSET_DETAILS_URL = BASE_URL + "upload_decommissioning_asset_check/";
    public static final String STD_PARAMETERS_COMMISSION_URL = BASE_URL + "standard-commission_detail/";
    public static final String STD_PARAMETERS_DECOMMISSION_URL = BASE_URL + "standard-decommission_detail/";

    public static final String GET_MONITORING_JOB_CARD_DETAILS_URL = BASE_URL + "get_asset_monitoring_card/";
    public static final String GET_MONITORING_DETAILS_URL = BASE_URL + "monitoring_detail/";
    public static final String STD_MONITORING_PARAMETERS_URL = BASE_URL + "monitoring_detail_standard/";
    public static final String REASSIGN_DEASSIGN_COMMISSION_CARD_URL = BASE_URL + "reassigned_deassigned_commissioning_card/";
    public static final String GET_PREVENTIVE_JOB_CARDS_URL = BASE_URL + "get_asset_preventive_maintenance_card/";
    public static final String GET_BREAKDOWN_JOB_CARDS_URL = BASE_URL + "get_asset_breakdown_maintenance_card/";
    public static final String GET_PREVENTIVE_DETAILS_URL = BASE_URL + "preventive_maintenance_detail/";
    public static final String GET_BREAKDOWN_DETAILS_URL = BASE_URL + "breakdown_maintenance_detail/";

    public static final String UPLOAD_PREVENTIVE_DETAILS_URL = BASE_URL + "Upload_preventive_maintenance_asset_check/";
    public static final String UPLOAD_BREAKDOWN_DETAILS_URL = BASE_URL + "Upload_breakdown_mainetenance_asset_check/";
    public static final String UPLOAD_MONITERING_DETAILS_URL = BASE_URL + "upload_decommissioning_asset_check/";



    //For Volley
    public static final String LOGIN = "1";
    public static final String LOGOUT = "2";
    public static final String GET_CATEGORY = "3";
    public static final String GET_SUB_CATEGORY = "4";
    public static final String GET_ASSETS = "5";
    public static final String FCM_DEVICE_TOKEN = "6";
    public static final String NSC_TODAY_CARDS = "7";
    public static final String DISC_TODAY_CARDS = "8";
    public static final String COMMISSION_ASSET_DETAILS = "9";
    public static final String STD_COMMISSION_PARAMETERS = "10";
    public static final String UPLOAD_COMMISSION_ASSET_DETAILS = "11";
    public static final String DECOMMISSION_ASSET_DETAILS = "12";
    public static final String UPLOAD_DECOMMISSION_ASSET_DETAILS = "13";
    public static final String STD_DECOMMISSION_PARAMETERS = "14";

    public static final String GET_MONITORING_JOB_CARD_DETAILS = "15";
    public static final String GET_MONITORING_DETAILS = "16";
    public static final String STD_MONITORING_PARAMETERS = "17";
    public static final String REASSIGN_DEASSIGN_COMMISSION_CARD = "18";

    public static final String GET_PREVENTIVE_JOB_CARDS = "19";
    public static final String GET_BREAKDOWN_JOB_CARDS = "20";
    public static final String GET_PREVENTIVE_DETAILS = "21";
    public static final String GET_BREAKDOWN_DETAILS = "22";

    public static final String UPLOAD_PREVENTIVE_DETAILS = "23";
    public static final String UPLOAD_BREAKDOWN_DETAILS = "24";
    public static final String UPLOAD_MONITERING_DETAILS = "25";

}
