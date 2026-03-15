package com.ryuqq.setof.application.discount.dto.response;

import java.util.List;

/**
 * 할인 정책 페이지 조회 결과 DTO.
 *
 * <p>할인 정책 목록 페이지네이션 응답을 표현합니다.
 *
 * @param items 현재 페이지의 할인 정책 결과 목록
 * @param totalCount 전체 항목 수
 * @param page 현재 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountPolicyPageResult(
        List<DiscountPolicyResult> items, long totalCount, int page, int size) {

    /**
     * 페이지 결과 DTO를 생성합니다.
     *
     * @param items 현재 페이지의 결과 목록
     * @param totalCount 전체 항목 수
     * @param page 현재 페이지 번호
     * @param size 페이지 크기
     * @return 생성된 페이지 결과 DTO
     */
    public static DiscountPolicyPageResult of(
            List<DiscountPolicyResult> items, long totalCount, int page, int size) {
        return new DiscountPolicyPageResult(items, totalCount, page, size);
    }
}
