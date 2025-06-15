package com.mcnz.spring.soaprest.Security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    public static final long JWT_Expiration = TimeUnit.HOURS.toMillis(6);
    public static final String JWT_Secret = "secret";
}
