package com.setof.connectly.infra.config.redis;

public class CacheKey {

    public static final int DEFAULT_EXPIRE_HOUR = 24;

    public static final int TWO_EXPIRE_HOUR = 2;

    public static final int SIX_EXPIRE_HOUR = 6;

    public static final int DEFAULT_EXPIRE_MINUTE = 10;

    public static final int ONE_EXPIRE_MINUTE = 1;
    public static final String PRODUCTS = "products";
    public static final String CATEGORIES = "categories";
    public static final String GNBS = "gnbs";
    public static final String BANNERS = "banners";
    public static final String JOINED_USERS = "joinedUsers";

    public static final String USER_MILEAGE = "userMileage";
    public static final String PRODUCTS_BRAND = "productsBrand";
    public static final String PRODUCTS_SELLER = "productsSeller";
    public static final String CART_COUNT = "cartCount";
}
