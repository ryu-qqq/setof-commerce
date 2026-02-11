package com.ryuqq.setof.application.legacy.mileage.dto.response;

/**
 * 레거시 UserMileage 조회 결과 DTO.
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
public record LegacyUserMileageResult(
        long userId,
        double currentMileage,
        double expectedSaveMileage,
        double expectedExpireMileage) {

    public static LegacyUserMileageResult of(
            long userId,
            double currentMileage,
            double expectedSaveMileage,
            double expectedExpireMileage) {
        return new LegacyUserMileageResult(
                userId, currentMileage, expectedSaveMileage, expectedExpireMileage);
    }

    public static LegacyUserMileageResult empty(long userId) {
        return new LegacyUserMileageResult(userId, 0.0, 0.0, 0.0);
    }
}
