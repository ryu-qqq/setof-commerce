package com.ryuqq.setof.application.member.dto.command;

/**
 * 동의 항목
 *
 * <p>회원 가입 및 OAuth 동의 항목을 표현하는 공통 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
public record ConsentItem(String type, boolean agreed) {}
