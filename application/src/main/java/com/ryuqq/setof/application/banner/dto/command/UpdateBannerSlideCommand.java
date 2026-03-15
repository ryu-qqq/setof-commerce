package com.ryuqq.setof.application.banner.dto.command;

import java.time.Instant;

/**
 * UpdateBannerSlideCommand - 배너 슬라이드 수정 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. 슬라이드 전체 교체 시 각 슬라이드의 수정 데이터를 담습니다.
 *
 * @param title 슬라이드 제목
 * @param imageUrl 이미지 URL
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param displayStartAt 노출 시작일
 * @param displayEndAt 노출 종료일
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateBannerSlideCommand(
        Long slideId,
        String title,
        String imageUrl,
        String linkUrl,
        int displayOrder,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active) {}
