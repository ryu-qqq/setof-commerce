package com.ryuqq.setof.domain.banner.aggregate;

import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;

/**
 * 배너 그룹 수정 데이터.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * <p>slideEntries의 SlideEntry.slideId가 null이면 신규, 값이 있으면 기존 슬라이드 수정, 기존 슬라이드가 요청에 없으면 삭제로 분류됩니다.
 *
 * @param title 배너 그룹명
 * @param bannerType 배너 타입
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param slideEntries 슬라이드 수정 엔트리 목록
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerGroupUpdateData(
        String title,
        BannerType bannerType,
        DisplayPeriod displayPeriod,
        boolean active,
        List<SlideEntry> slideEntries,
        Instant updatedAt) {

    /**
     * 슬라이드 수정 엔트리.
     *
     * @param slideId 슬라이드 ID (null이면 신규 생성, 값이 있으면 기존 수정)
     * @param title 슬라이드 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     */
    public record SlideEntry(
            Long slideId,
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active) {}
}
