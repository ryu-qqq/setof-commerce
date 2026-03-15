package com.ryuqq.setof.application.navigation.dto.command;

/**
 * RemoveNavigationMenuCommand - 네비게이션 메뉴 삭제 Command.
 *
 * <p>네비게이션 메뉴 논리 삭제 시 Adapter-In에서 Application Layer로 전달하는 Command DTO.
 *
 * @param id 삭제 대상 네비게이션 메뉴 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RemoveNavigationMenuCommand(long id) {}
