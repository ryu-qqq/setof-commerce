package com.ryuqq.setof.adapter.out.client.portone.dto;

/**
 * PortOne Token Response
 *
 * <p>포트원 인증 토큰 응답 DTO
 *
 * @param accessToken 액세스 토큰
 * @param expiredAt 만료 시간 (Unix timestamp)
 * @param now 현재 시간 (Unix timestamp)
 * @author development-team
 * @since 1.0.0
 */
public record PortOneTokenResponse(String accessToken, long expiredAt, long now) {}
