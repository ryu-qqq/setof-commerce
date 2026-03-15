package com.ryuqq.setof.application.banner.dto.command;

/**
 * RemoveBannerGroupCommand - 배너 그룹 삭제 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. 소프트 삭제를 수행하기 위한 배너 그룹 ID를 담습니다.
 *
 * @param id 배너 그룹 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RemoveBannerGroupCommand(long id) {}
