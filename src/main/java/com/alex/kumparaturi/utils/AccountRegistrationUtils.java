package com.alex.kumparaturi.utils;

import java.util.UUID;

public class AccountRegistrationUtils {
    public static String generateActivationToken() {
        return UUID.randomUUID().toString();
    }
}
