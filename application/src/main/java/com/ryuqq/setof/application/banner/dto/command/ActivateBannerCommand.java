package com.ryuqq.setof.application.banner.dto.command;

/**
 * 배너 활성화 Command
 *
 * @param bannerId 활성화할 배너 ID
 * @author development-team
 * @since 2.0.0
 */
public record ActivateBannerCommand(Long bannerId) {}
