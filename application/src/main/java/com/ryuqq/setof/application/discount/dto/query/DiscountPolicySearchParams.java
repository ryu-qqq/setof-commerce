package com.ryuqq.setof.application.discount.dto.query;

/**
 * 할인 정책 검색 파라미터 DTO.
 *
 * <p>할인 정책 목록 조회 시 외부에서 전달되는 검색 조건과 페이징 정보를 담습니다. Factory를 통해 {@code DiscountPolicySearchCriteria}로
 * 변환됩니다.
 *
 * @param applicationType 적용 방식 필터 (nullable, IMMEDIATE / COUPON)
 * @param publisherType 발행 주체 필터 (nullable, ADMIN / SELLER)
 * @param stackingGroup 스태킹 그룹 필터 (nullable)
 * @param sellerId 판매자 ID 필터 (nullable)
 * @param active 활성 정책만 조회 여부 (nullable, null이면 전체)
 * @param sortKey 정렬 기준 필드명 (nullable)
 * @param sortDirection 정렬 방향 (nullable, ASC / DESC)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountPolicySearchParams(
        String applicationType,
        String publisherType,
        String stackingGroup,
        Long sellerId,
        Boolean active,
        String sortKey,
        String sortDirection,
        int page,
        int size) {

    /**
     * 활성 정책만 조회 여부를 boolean으로 반환합니다.
     *
     * @return active 가 non-null true 인 경우에만 true
     */
    public boolean activeOnly() {
        return active != null && active;
    }
}
