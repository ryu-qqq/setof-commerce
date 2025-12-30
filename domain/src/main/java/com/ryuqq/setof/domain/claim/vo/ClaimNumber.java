package com.ryuqq.setof.domain.claim.vo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClaimNumber - 클레임 번호 VO
 *
 * <p>사용자에게 표시되는 클레임 번호입니다. 형식: CLM-yyyyMMdd-NNNNNN
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ClaimNumber {

    private static final String PREFIX = "CLM";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private final String value;

    private ClaimNumber(String value) {
        this.value = value;
    }

    /**
     * 새로운 ClaimNumber 생성
     *
     * @return 오늘 날짜 기반 클레임 번호
     */
    public static ClaimNumber generate() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        int seq = SEQUENCE.incrementAndGet() % 1000000;
        return new ClaimNumber(String.format("%s-%s-%06d", PREFIX, dateStr, seq));
    }

    /**
     * 시퀀스를 지정하여 ClaimNumber 생성
     *
     * @param sequence 일련번호
     * @return 클레임 번호
     */
    public static ClaimNumber generate(int sequence) {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        return new ClaimNumber(String.format("%s-%s-%06d", PREFIX, dateStr, sequence));
    }

    /**
     * 문자열로부터 ClaimNumber 생성 (복원용)
     *
     * @param value 클레임 번호 문자열
     * @return ClaimNumber
     * @throws IllegalArgumentException value가 null이거나 형식이 맞지 않으면
     */
    public static ClaimNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ClaimNumber must not be null or blank");
        }
        if (!value.startsWith(PREFIX + "-")) {
            throw new IllegalArgumentException("ClaimNumber must start with '" + PREFIX + "-'");
        }
        return new ClaimNumber(value);
    }

    /**
     * 클레임 번호 값 반환
     *
     * @return 클레임 번호 문자열
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimNumber that = (ClaimNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
