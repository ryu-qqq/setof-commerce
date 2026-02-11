package com.ryuqq.setof.application.legacy.user.dto.response;

import java.util.List;

/**
 * 레거시 찜 목록 Slice 결과 DTO.
 *
 * <p>커서 기반 페이징 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param content 찜 목록
 * @param hasNext 다음 페이지 존재 여부
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyUserFavoriteSliceResult(
        List<LegacyUserFavoriteResult> content, boolean hasNext, long totalElements) {

    public static LegacyUserFavoriteSliceResult of(
            List<LegacyUserFavoriteResult> content, boolean hasNext, long totalElements) {
        return new LegacyUserFavoriteSliceResult(content, hasNext, totalElements);
    }

    public static LegacyUserFavoriteSliceResult empty() {
        return new LegacyUserFavoriteSliceResult(List.of(), false, 0L);
    }
}
