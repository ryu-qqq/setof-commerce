package com.ryuqq.setof.domain.contentpage.aggregate;

import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;

/**
 * 콘텐츠 페이지 수정 데이터.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param title 페이지 제목
 * @param memo 메모
 * @param coverImageUrl 커버 이미지 URL
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageUpdateData(
        String title,
        String memo,
        String coverImageUrl,
        DisplayPeriod displayPeriod,
        boolean active,
        Instant updatedAt) {}
