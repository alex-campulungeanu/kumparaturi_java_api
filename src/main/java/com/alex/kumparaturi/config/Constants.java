package com.alex.kumparaturi.config;

/**
 * Application constants.
 */

public class Constants {

    //diverse
    public static final String TOKENAUTH_NAME = "TokenAuth";

    //notification
    public static final Long SEND_NOTIFICATION_SETTING_TYPE_ID = 1L;
    public static final Long SEND_NOTIFICATION_SETTING_VALUE_NUMBER = 1L;

    public static final Long ACTIVE_STATUS_ITEM = 0L;
    public static final Long INACTIVE = 1L;

    public static final Long SETTING_TYPE_SEND_NOTIFICATION = 1L;
    public static final Long SETTING_TYPE_RECEIVED_NOTIFICATION = 2L;

    //URL ENDPOINTS
    public static final String BASE_ACTIVATE_ACCOUNT_URL = "/api/auth";
    public static final String ACTIVATE_ACCOUNT_URL = "/activate_account";
    public static final String ALL_ACTIVATE_ACCOUNT_URL = BASE_ACTIVATE_ACCOUNT_URL + ACTIVATE_ACCOUNT_URL;

    public static final String BASE_RESET_PASSWORD_URL = "/api/rp";
    public static final String RESET_PASSWORD_URL = "/reset_password";
    public static final String ALL_RESET_PASSWORD_URL = BASE_RESET_PASSWORD_URL + RESET_PASSWORD_URL;

    private Constants() {
    }
}

