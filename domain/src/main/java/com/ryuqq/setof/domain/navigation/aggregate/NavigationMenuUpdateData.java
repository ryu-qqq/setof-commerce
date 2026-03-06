package com.ryuqq.setof.domain.navigation.aggregate;

import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;

/**
 * NavigationMenu 수정 데이터.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param title 메뉴명
 * @param linkUrl 이동 URL
 * @param displayOrder 노출 순서
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NavigationMenuUpdateData(
        String title,
        String linkUrl,
        int displayOrder,
        DisplayPeriod displayPeriod,
        boolean active,
        Instant updatedAt) {}
