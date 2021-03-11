package com.alex.kumparaturi.utils;

import java.util.Base64;

public class ImageToBase64 {

    public static String toBase64(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
