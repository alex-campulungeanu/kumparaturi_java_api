package com.alex.kumparaturi.utils;

import javax.servlet.http.HttpServletRequest;

public class AdministrationUtils {

    public static String getAppBaseUrl(HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(),  request.getServerName(), request.getServerPort());
    }

    public static void addDelay() {
        try {
            Thread.sleep(2000);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
