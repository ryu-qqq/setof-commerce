package com.ryuqq.setof.storage.legacy.composite.web.mileage.dto;

import java.time.LocalDateTime;

/**
 * 레거시 Web UserMileage 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>사용자의 개별 마일리지 잔액 정보.
 *
 * @param userId 사용자 ID
 * @param mileageId 마일리지 ID
 * @param mileageAmount 적립 금액
 * @param usedMileageAmount 사용 금액
 * @param activeYn 활성 여부
 * @param issuedDate 발급 일시
 * @param expirationDate 만료 일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebUserMileageQueryDto(
        long userId,
        long mileageId,
        double mileageAmount,
        double usedMileageAmount,
        String activeYn,
        LocalDateTime issuedDate,
        LocalDateTime expirationDate) {

    /**
     * 현재 잔여 마일리지 계산.
     *
     * @return 잔여 마일리지 (적립 - 사용)
     */
    public double getCurrentMileage() {
        return mileageAmount - usedMileageAmount;
    }

    /**
     * 만료 여부 확인.
     *
     * @param now 기준 시각
     * @return 만료 여부
     */
    public boolean isExpired(LocalDateTime now) {
        return expirationDate != null && expirationDate.isBefore(now);
    }
}
