package com.ryuqq.setof.application.legacy.order.dto.response;

import java.util.List;

/**
 * LegacyOrderSliceResult - 레거시 주문 목록 슬라이스 결과 DTO.
 *
 * <p>커서 기반 페이징을 위한 슬라이스 응답입니다.
 *
 * @param content 주문 목록
 * @param last 마지막 페이지 여부
 * @param first 첫 페이지 여부
 * @param number 현재 페이지 번호
 * @param size 페이지 크기
 * @param numberOfElements 현재 페이지 요소 수
 * @param empty 빈 결과 여부
 * @param lastDomainId 마지막 주문 ID (커서)
 * @param orderCounts 상태별 건수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderSliceResult(
        List<LegacyOrderResult> content,
        boolean last,
        boolean first,
        int number,
        int size,
        int numberOfElements,
        boolean empty,
        Long lastDomainId,
        List<LegacyOrderCountResult> orderCounts) {

    /** 정적 팩토리 메서드. */
    public static LegacyOrderSliceResult of(
            List<LegacyOrderResult> content,
            boolean last,
            boolean first,
            int number,
            int size,
            int numberOfElements,
            boolean empty,
            Long lastDomainId,
            List<LegacyOrderCountResult> orderCounts) {
        return new LegacyOrderSliceResult(
                content,
                last,
                first,
                number,
                size,
                numberOfElements,
                empty,
                lastDomainId,
                orderCounts);
    }

    /**
     * 간단한 슬라이스 생성.
     *
     * @param content 주문 목록
     * @param size 페이지 크기
     * @param lastDomainId 마지막 주문 ID
     * @param orderCounts 상태별 건수
     * @return LegacyOrderSliceResult
     */
    public static LegacyOrderSliceResult ofSimple(
            List<LegacyOrderResult> content,
            int size,
            Long lastDomainId,
            List<LegacyOrderCountResult> orderCounts) {
        boolean hasContent = content != null && !content.isEmpty();
        boolean isLast = content == null || content.size() <= size;
        return new LegacyOrderSliceResult(
                content,
                isLast,
                lastDomainId == null,
                0,
                size,
                hasContent ? content.size() : 0,
                !hasContent,
                lastDomainId,
                orderCounts);
    }
}
