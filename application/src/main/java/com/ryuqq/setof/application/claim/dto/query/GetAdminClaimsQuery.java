package com.ryuqq.setof.application.claim.dto.query;

import java.time.Instant;
import java.util.List;

/**
 * GetAdminClaimsQuery - Admin 클레임 목록 조회 Query DTO
 *
 * <p>Admin에서 클레임 목록 조회 시 사용되는 검색 조건을 담는 record입니다.
 *
 * @param sellerId 셀러 ID (선택)
 * @param memberId 회원 ID (선택)
 * @param claimStatuses 클레임 상태 목록 (선택)
 * @param claimTypes 클레임 유형 목록 (선택)
 * @param searchKeyword 검색어 - 클레임번호, 주문ID (선택)
 * @param startDate 시작 일시 (선택)
 * @param endDate 종료 일시 (선택)
 * @param lastClaimId 마지막 조회 클레임 ID - 커서 페이징용 (선택)
 * @param pageSize 페이지 크기
 * @author development-team
 * @since 2.0.0
 */
public record GetAdminClaimsQuery(
        Long sellerId,
        Long memberId,
        List<String> claimStatuses,
        List<String> claimTypes,
        String searchKeyword,
        Instant startDate,
        Instant endDate,
        String lastClaimId,
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    public GetAdminClaimsQuery {
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
    }

    public static GetAdminClaimsQuery of(
            Long sellerId,
            Long memberId,
            List<String> claimStatuses,
            List<String> claimTypes,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            String lastClaimId,
            int pageSize) {
        return new GetAdminClaimsQuery(
                sellerId,
                memberId,
                claimStatuses,
                claimTypes,
                searchKeyword,
                startDate,
                endDate,
                lastClaimId,
                pageSize);
    }

    public boolean hasSellerId() {
        return sellerId != null;
    }

    public boolean hasMemberId() {
        return memberId != null;
    }

    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.isBlank();
    }

    public boolean hasCursor() {
        return lastClaimId != null && !lastClaimId.isBlank();
    }

    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }

    public boolean hasStatuses() {
        return claimStatuses != null && !claimStatuses.isEmpty();
    }

    public boolean hasTypes() {
        return claimTypes != null && !claimTypes.isEmpty();
    }
}
