package com.ryuqq.setof.adapter.out.persistence.claim.condition;

import java.time.Instant;
import java.util.List;

/**
 * AdminClaimSearchCondition - Admin 클레임 검색 조건
 *
 * <p>QueryDSL 검색용 조건 객체입니다.
 *
 * @param sellerId 셀러 ID (선택)
 * @param memberId 회원 ID (선택)
 * @param statuses 상태 목록 (선택)
 * @param claimTypes 클레임 유형 목록 (선택)
 * @param searchKeyword 검색어 (선택)
 * @param startDate 시작 일시 (선택)
 * @param endDate 종료 일시 (선택)
 * @param lastClaimId 마지막 클레임 ID - 커서 (선택)
 * @param limit 조회 개수
 * @author development-team
 * @since 2.0.0
 */
public record AdminClaimSearchCondition(
        Long sellerId,
        Long memberId,
        List<String> statuses,
        List<String> claimTypes,
        String searchKeyword,
        Instant startDate,
        Instant endDate,
        String lastClaimId,
        int limit) {

    public static AdminClaimSearchCondition of(
            Long sellerId,
            Long memberId,
            List<String> statuses,
            List<String> claimTypes,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            String lastClaimId,
            int limit) {
        return new AdminClaimSearchCondition(
                sellerId,
                memberId,
                statuses,
                claimTypes,
                searchKeyword,
                startDate,
                endDate,
                lastClaimId,
                limit);
    }

    public boolean hasSellerId() {
        return sellerId != null;
    }

    public boolean hasMemberId() {
        return memberId != null;
    }

    public boolean hasStatuses() {
        return statuses != null && !statuses.isEmpty();
    }

    public boolean hasClaimTypes() {
        return claimTypes != null && !claimTypes.isEmpty();
    }

    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.isBlank();
    }

    public boolean hasStartDate() {
        return startDate != null;
    }

    public boolean hasEndDate() {
        return endDate != null;
    }

    public boolean hasCursor() {
        return lastClaimId != null && !lastClaimId.isBlank();
    }
}
