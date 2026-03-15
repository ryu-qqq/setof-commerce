package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 요약 도메인 VO.
 *
 * <p>현재 사용 가능 마일리지, 적립 예정, 소멸 예정 금액을 캡슐화합니다.
 *
 * @param currentMileage 현재 사용 가능 마일리지
 * @param expectedSaveMileage 적립 예정 마일리지
 * @param expectedExpireMileage 소멸 예정 마일리지
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MileageSummary(
        double currentMileage, double expectedSaveMileage, double expectedExpireMileage) {

    public static MileageSummary of(
            double currentMileage, double expectedSaveMileage, double expectedExpireMileage) {
        return new MileageSummary(currentMileage, expectedSaveMileage, expectedExpireMileage);
    }

    public static MileageSummary empty() {
        return new MileageSummary(0.0, 0.0, 0.0);
    }
}
