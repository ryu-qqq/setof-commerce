package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.order.exception.InvalidOrderNumberException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * OrderNumber Value Object
 *
 * <p>고객에게 노출되는 주문 번호입니다. 형식: ORD-YYYYMMDD-XXXX (예: ORD-20251216-A7K9)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 주문 번호 문자열
 */
public record OrderNumber(String value) {

    private static final String PREFIX = "ORD";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 혼동 방지 (0,O,I,1 제외)

    /** Compact Constructor - 검증 로직 */
    public OrderNumber {
        if (value == null || value.isBlank()) {
            throw new InvalidOrderNumberException("주문 번호는 필수입니다");
        }
    }

    /**
     * Static Factory Method - 신규 주문 번호 생성
     *
     * @param orderTime 주문 시각
     * @return OrderNumber 인스턴스
     */
    public static OrderNumber generate(Instant orderTime) {
        LocalDate date = LocalDate.ofInstant(orderTime, ZoneId.of("Asia/Seoul"));
        String dateStr = date.format(DATE_FORMATTER);
        String random = generateRandomCode(4);
        return new OrderNumber(PREFIX + "-" + dateStr + "-" + random);
    }

    /**
     * Static Factory Method - 기존 주문 번호 복원
     *
     * @param value 주문 번호 문자열
     * @return OrderNumber 인스턴스
     */
    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    /**
     * 주문 날짜 추출
     *
     * @return 주문 날짜 문자열 (YYYYMMDD)
     */
    public String extractDate() {
        String[] parts = value.split("-");
        return parts.length >= 2 ? parts[1] : "";
    }

    private static String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(idx));
        }
        return sb.toString();
    }
}
