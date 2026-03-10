package com.ryuqq.setof.application.mileage.dto.response;

/**
 * MileageSummaryResult - 마일리지 요약 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param userId 사용자 ID
 * @param currentMileage 현재 사용 가능 마일리지
 * @param expectedSaveMileage 적립 예정 마일리지
 * @param expectedExpireMileage 소멸 예정 마일리지
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MileageSummaryResult(
        long userId,
        double currentMileage,
        double expectedSaveMileage,
        double expectedExpireMileage) {

    public static MileageSummaryResult of(
            long userId,
            double currentMileage,
            double expectedSaveMileage,
            double expectedExpireMileage) {
        return new MileageSummaryResult(
                userId, currentMileage, expectedSaveMileage, expectedExpireMileage);
    }

    public static MileageSummaryResult empty(long userId) {
        return new MileageSummaryResult(userId, 0.0, 0.0, 0.0);
    }
}
