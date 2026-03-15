package com.ryuqq.setof.application.banner.dto.command;

import java.time.Instant;
import java.util.List;

/**
 * RegisterBannerGroupCommand - 배너 그룹 등록 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. UseCase 입력으로 사용되며, 비즈니스 로직을 포함하지 않습니다.
 *
 * @param title 배너 그룹명
 * @param bannerType 배너 타입 (BannerType enum 이름)
 * @param displayStartAt 노출 시작일
 * @param displayEndAt 노출 종료일
 * @param active 노출 여부
 * @param slides 슬라이드 등록 Command 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterBannerGroupCommand(
        String title,
        String bannerType,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active,
        List<RegisterBannerSlideCommand> slides) {}
