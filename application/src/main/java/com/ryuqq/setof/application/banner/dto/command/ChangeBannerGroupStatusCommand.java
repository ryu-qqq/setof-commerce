package com.ryuqq.setof.application.banner.dto.command;

/**
 * ChangeBannerGroupStatusCommand - 배너 그룹 노출 상태 변경 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. 배너 그룹의 노출(active) 상태를 토글하는 입력 데이터입니다.
 *
 * @param id 배너 그룹 ID
 * @param active 변경할 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ChangeBannerGroupStatusCommand(long id, boolean active) {}
