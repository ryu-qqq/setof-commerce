package com.setof.connectly.module.common.enums;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public enum RedisKey {
    USERS("users", 1),
    JOINED_USERS("joinedUsers", 1),
    REFRESH_TOKEN("refreshToken", 1),
    STOCK("stock", 3),
    SOLD_OUT("soldOut", 3),
    CART_COUNT("cartCount", 1),
    CATEGORIES("categories", 3),
    DISCOUNT("discount", 12),
    PRODUCT_SELLER("productsSeller", 1),
    PRODUCT_BRAND("productsBrand", 1),
    GNBS("gnbs", 1),
    BANNERS("banners", 3),
    PAYMENT_LOCK("paymentLock", 1),
    SELLER("seller", 1),
    EVENT("event", 1),
    INTERLOCKING("interlocking", 1),
    ;

    private final String key;
    private final int hour;
    private static final String TERM = "::";

    public String generateKey(String key) {
        if (StringUtils.hasText(key)) return this.key + TERM + key;
        return generateKey();
    }

    private String generateKey() {
        return this.key;
    }

    public Duration getHourDuration() {
        return Duration.ofHours(this.hour);
    }
}
