package com.ryuqq.setof.application.navigation.dto.command;

import java.time.Instant;

/**
 * UpdateNavigationMenuCommand - 네비게이션 메뉴 수정 Command.
 *
 * <p>네비게이션 메뉴 수정 시 Adapter-In에서 Application Layer로 전달하는 Command DTO.
 *
 * @param id 수정 대상 네비게이션 메뉴 ID
 * @param title 메뉴명
 * @param linkUrl 이동 URL
 * @param displayOrder 노출 순서
 * @param displayStartAt 노출 시작 시각
 * @param displayEndAt 노출 종료 시각
 * @param active 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateNavigationMenuCommand(
        long id,
        String title,
        String linkUrl,
        int displayOrder,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active) {}
