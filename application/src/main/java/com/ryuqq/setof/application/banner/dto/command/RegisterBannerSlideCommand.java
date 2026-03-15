package com.ryuqq.setof.application.banner.dto.command;

import java.time.Instant;

/**
 * RegisterBannerSlideCommand - 배너 슬라이드 등록 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. UseCase 입력으로 사용되며, 비즈니스 로직을 포함하지 않습니다.
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
public record RegisterBannerSlideCommand(
        String title,
        String imageUrl,
        String linkUrl,
        int displayOrder,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active) {}
