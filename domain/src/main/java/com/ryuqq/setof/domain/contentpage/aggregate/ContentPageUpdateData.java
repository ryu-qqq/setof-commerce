package com.ryuqq.setof.domain.contentpage.aggregate;

import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;

/**
 * ContentPage 수정 데이터.
 *
 * @param title 콘텐츠 제목
 * @param memo 메모
 * @param imageUrl 대표 이미지
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageUpdateData(
        String title,
        String memo,
        String imageUrl,
        DisplayPeriod displayPeriod,
        boolean active,
        Instant updatedAt) {}
