package com.ryuqq.setof.adapter.in.rest.admin.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** 날짜/시간 포맷 변환 유틸리티. */
public final class DateTimeFormatUtils {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private DateTimeFormatUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String format(Instant instant) {
        if (instant == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE_ID);
        return localDateTime.format(FORMATTER);
    }

    public static String formatIso8601(Instant instant) {
        if (instant == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = instant.atZone(ZONE_ID);
        return zonedDateTime.format(ISO_FORMATTER);
    }

    public static String nowIso8601() {
        return formatIso8601(Instant.now());
    }
}
