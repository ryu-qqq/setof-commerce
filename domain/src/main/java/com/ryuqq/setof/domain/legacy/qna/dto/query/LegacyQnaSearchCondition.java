package com.ryuqq.setof.domain.legacy.qna.dto.query;

import java.time.LocalDateTime;

/**
 * LegacyQnaSearchCondition - 레거시 Q&A 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param productGroupId 상품그룹 ID (fetchProductQnas용)
 * @param userId 사용자 ID (fetchMyQnas용)
 * @param qnaType Q&A 유형 (PRODUCT/ORDER)
 * @param lastDomainId 커서 기반 페이징용 마지막 ID
 * @param startDate 조회 시작일
 * @param endDate 조회 종료일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyQnaSearchCondition(
        Long productGroupId,
        Long userId,
        String qnaType,
        Long lastDomainId,
        LocalDateTime startDate,
        LocalDateTime endDate) {

    /**
     * 상품그룹 Q&A 조회용 조건 생성.
     *
     * @param productGroupId 상품그룹 ID
     * @return LegacyQnaSearchCondition
     */
    public static LegacyQnaSearchCondition ofProductGroupId(Long productGroupId) {
        return new LegacyQnaSearchCondition(productGroupId, null, null, null, null, null);
    }

    /**
     * 내 Q&A 조회용 조건 생성.
     *
     * @param userId 사용자 ID
     * @param qnaType Q&A 유형
     * @param lastDomainId 커서 기반 페이징용 마지막 ID
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return LegacyQnaSearchCondition
     */
    public static LegacyQnaSearchCondition ofMyQnas(
            Long userId,
            String qnaType,
            Long lastDomainId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return new LegacyQnaSearchCondition(
                null, userId, qnaType, lastDomainId, startDate, endDate);
    }

    /**
     * 전체 조회용 빈 조건.
     *
     * @return LegacyQnaSearchCondition
     */
    public static LegacyQnaSearchCondition empty() {
        return new LegacyQnaSearchCondition(null, null, null, null, null, null);
    }

    /**
     * 상품그룹 ID 존재 여부.
     *
     * @return productGroupId가 있으면 true
     */
    public boolean hasProductGroupId() {
        return productGroupId != null;
    }

    /**
     * 사용자 ID 존재 여부.
     *
     * @return userId가 있으면 true
     */
    public boolean hasUserId() {
        return userId != null;
    }

    /**
     * 커서 기반 페이징 여부.
     *
     * @return lastDomainId가 있으면 true
     */
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }

    /**
     * 상품 Q&A 여부.
     *
     * @return qnaType이 PRODUCT이면 true
     */
    public boolean isProductQna() {
        return "PRODUCT".equals(qnaType);
    }
}
