package com.ryuqq.setof.application.banner.dto.command;

import java.time.Instant;

/**
 * UpdateBannerGroupCommand - 배너 그룹 수정 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. UseCase 입력으로 사용되며, 비즈니스 로직을 포함하지 않습니다.
 *
 * <p>슬라이드 수정은 {@link UpdateBannerSlidesCommand}를 통해 별도 처리합니다.
 *
 * @param id 배너 그룹 ID
 * @param title 배너 그룹명
 * @param bannerType 배너 타입 (BannerType enum 이름)
 * @param displayStartAt 노출 시작일
 * @param displayEndAt 노출 종료일
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateBannerGroupCommand(
        long id,
        String title,
        String bannerType,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active) {}
