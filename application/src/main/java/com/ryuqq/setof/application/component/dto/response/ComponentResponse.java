package com.ryuqq.setof.application.component.dto.response;

import java.time.Instant;

/**
 * Component 응답 DTO
 *
 * @param componentId 컴포넌트 ID
 * @param contentId 소속 Content ID
 * @param componentType 컴포넌트 타입
 * @param componentName 컴포넌트 이름
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param exposedProducts 노출 상품 수
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param detail 타입별 상세 정보
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record ComponentResponse(
        Long componentId,
        Long contentId,
        String componentType,
        String componentName,
        int displayOrder,
        String status,
        int exposedProducts,
        Instant displayStartDate,
        Instant displayEndDate,
        ComponentDetailResponse detail,
        Instant createdAt,
        Instant updatedAt) {

    /** Static Factory Method */
    public static ComponentResponse of(
            Long componentId,
            Long contentId,
            String componentType,
            String componentName,
            int displayOrder,
            String status,
            int exposedProducts,
            Instant displayStartDate,
            Instant displayEndDate,
            ComponentDetailResponse detail,
            Instant createdAt,
            Instant updatedAt) {
        return new ComponentResponse(
                componentId,
                contentId,
                componentType,
                componentName,
                displayOrder,
                status,
                exposedProducts,
                displayStartDate,
                displayEndDate,
                detail,
                createdAt,
                updatedAt);
    }
}
