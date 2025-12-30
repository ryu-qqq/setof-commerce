package com.connectly.partnerAdmin.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Getter
@AllArgsConstructor
public enum RedisKey implements EnumType{

    REFRESH_TOKEN("refresh_token", 1),

    USERS("users", 1),
    GNBS("gnbs", 1),
    SELLERS("sellers", 6),
    SELLER("seller", 1),


    CRAWL_PRODUCTS("item", 99),
    BANNERS ("banners", 1),
    DISCOUNT("discount",12),
    STOCK("stock", 3),
    EVENT("event", 1),
    INTERLOCKING("interlocking", 1),
    ;

    private final String key;
    private final int hour;
    private static final String TERM = ":";

    public String generateKey(String key){
        if(StringUtils.hasText(key)) return this.key+TERM+key;
        return generateKey();
    }

    private String generateKey(){
        return this.key;
    }

    public Duration getHourDuration(){
        return Duration.ofHours(this.hour);
    }

    public Duration getSecondDuration(){
        return Duration.ofSeconds(this.hour);
    }


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return String.format("REDIS KEY :  %s, '\n'REDIS HOUR TTL :  %s", key, hour);
    }
}
