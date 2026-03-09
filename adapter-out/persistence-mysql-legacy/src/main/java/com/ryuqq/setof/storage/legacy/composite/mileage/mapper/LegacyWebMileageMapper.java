package com.ryuqq.setof.storage.legacy.composite.mileage.mapper;

import com.ryuqq.setof.application.legacy.mileage.dto.response.LegacyMileageHistoryResult;
import com.ryuqq.setof.application.legacy.mileage.dto.response.LegacyUserMileageResult;
import com.ryuqq.setof.storage.legacy.composite.mileage.dto.LegacyWebMileageHistoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.mileage.dto.LegacyWebUserMileageQueryDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Mileage Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebMileageMapper {

    /**
     * MileageHistory QueryDto → Result 변환.
     *
     * @param dto 마일리지 이력 QueryDto
     * @return LegacyMileageHistoryResult
     */
    public LegacyMileageHistoryResult toHistoryResult(LegacyWebMileageHistoryQueryDto dto) {
        if (dto == null) {
            return null;
        }

        return new LegacyMileageHistoryResult(
                dto.mileageHistoryId(),
                dto.mileageId(),
                dto.title(),
                dto.paymentId(),
                dto.orderId(),
                dto.changeAmount(),
                dto.reason(),
                dto.insertDate(),
                dto.expirationDate());
    }

    /**
     * MileageHistory QueryDto 목록 → Result 목록 변환.
     *
     * <p>사유별 제목 자동 적용 (withReasonTitle).
     *
     * @param dtos 마일리지 이력 QueryDto 목록
     * @return LegacyMileageHistoryResult 목록
     */
    public List<LegacyMileageHistoryResult> toHistoryResults(
            List<LegacyWebMileageHistoryQueryDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toHistoryResult)
                .map(LegacyMileageHistoryResult::withReasonTitle)
                .toList();
    }

    /**
     * UserMileage QueryDto 목록 → UserMileageResult 변환.
     *
     * <p>레거시 toMyMileages 로직 대응:
     *
     * <ul>
     *   <li>만료 예정 마일리지 계산 (expirationDate < now)
     *   <li>현재 마일리지 합계
     *   <li>실제 사용 가능 금액 = 현재 - 만료 예정
     * </ul>
     *
     * @param userId 사용자 ID
     * @param dtos 사용자 마일리지 QueryDto 목록
     * @param pendingMileages 적립 예정 마일리지
     * @return LegacyUserMileageResult
     */
    public LegacyUserMileageResult toUserMileageResult(
            long userId, List<LegacyWebUserMileageQueryDto> dtos, double pendingMileages) {

        if (dtos == null || dtos.isEmpty()) {
            return LegacyUserMileageResult.of(userId, 0.0, pendingMileages, 0.0);
        }

        LocalDateTime now = LocalDateTime.now();

        double expiringMileageAmount = calculateExpiringMileage(dtos, now);
        double currentMileageAmount = calculateCurrentMileage(dtos);
        double availableMileage = Math.max(currentMileageAmount - expiringMileageAmount, 0.0);

        return LegacyUserMileageResult.of(
                userId, availableMileage, pendingMileages, expiringMileageAmount);
    }

    /**
     * 만료 예정 마일리지 계산.
     *
     * @param dtos 마일리지 목록
     * @param now 기준 시각
     * @return 만료 예정 마일리지 합계
     */
    private double calculateExpiringMileage(
            List<LegacyWebUserMileageQueryDto> dtos, LocalDateTime now) {
        return dtos.stream()
                .filter(dto -> dto.isExpired(now))
                .mapToDouble(LegacyWebUserMileageQueryDto::getCurrentMileage)
                .sum();
    }

    /**
     * 현재 마일리지 합계 계산.
     *
     * @param dtos 마일리지 목록
     * @return 현재 마일리지 합계
     */
    private double calculateCurrentMileage(List<LegacyWebUserMileageQueryDto> dtos) {
        return dtos.stream().mapToDouble(LegacyWebUserMileageQueryDto::getCurrentMileage).sum();
    }
}
