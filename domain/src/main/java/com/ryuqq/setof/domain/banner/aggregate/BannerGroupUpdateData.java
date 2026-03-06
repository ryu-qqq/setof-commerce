package com.ryuqq.setof.domain.banner.aggregate;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;

/**
 * 배너 그룹 수정 데이터.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param title 배너 그룹명
 * @param bannerType 배너 타입
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param slides 슬라이드 목록
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerGroupUpdateData(
        String title,
        BannerType bannerType,
        DisplayPeriod displayPeriod,
        boolean active,
        List<BannerSlide> slides,
        Instant updatedAt) {}
