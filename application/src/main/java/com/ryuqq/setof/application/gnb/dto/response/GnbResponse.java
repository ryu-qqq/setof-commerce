package com.ryuqq.setof.application.gnb.dto.response;

import java.time.Instant;

/**
 * Gnb 응답 DTO
 *
 * @param gnbId GNB ID
 * @param title 제목
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record GnbResponse(
        Long gnbId,
        String title,
        String linkUrl,
        int displayOrder,
        String status,
        Instant displayStartDate,
        Instant displayEndDate,
        Instant createdAt,
        Instant updatedAt) {

    /** Static Factory Method */
    public static GnbResponse of(
            Long gnbId,
            String title,
            String linkUrl,
            int displayOrder,
            String status,
            Instant displayStartDate,
            Instant displayEndDate,
            Instant createdAt,
            Instant updatedAt) {
        return new GnbResponse(
                gnbId,
                title,
                linkUrl,
                displayOrder,
                status,
                displayStartDate,
                displayEndDate,
                createdAt,
                updatedAt);
    }
}
