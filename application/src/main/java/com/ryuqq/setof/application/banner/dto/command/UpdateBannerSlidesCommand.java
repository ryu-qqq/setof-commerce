package com.ryuqq.setof.application.banner.dto.command;

import java.util.List;

/**
 * UpdateBannerSlidesCommand - 배너 슬라이드 일괄 수정 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다.
 *
 * <p>slideId가 null이면 신규, 값이 있으면 기존 슬라이드 수정, 요청에 미포함이면 삭제로 처리됩니다.
 *
 * @param bannerGroupId 배너 그룹 ID
 * @param slides 슬라이드 수정 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateBannerSlidesCommand(
        long bannerGroupId, List<UpdateBannerSlideCommand> slides) {}
