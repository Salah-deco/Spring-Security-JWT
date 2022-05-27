package org.sid.authjwt.security;

public class JWTUtil {
    public static final String SECRET = "mySecret@key";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final long EXPIRE_ACCESS_TOKEN = 120000;
    public static final long EXPIRE_REFRESH_TOKEN = 1200000;
}
