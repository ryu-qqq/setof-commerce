package com.ryuqq.setof.application.member.port.out.client;

/**
 * Password Encoder Port
 *
 * <p>비밀번호 인코딩 및 검증을 위한 아웃바운드 포트입니다.
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>BCryptPasswordEncoderAdapter - BCrypt 기반 비밀번호 인코더
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PasswordEncoderPort {

    /**
     * 원본 비밀번호를 인코딩합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @return 인코딩된 비밀번호
     * @throws IllegalArgumentException 비밀번호가 null이거나 빈 문자열인 경우
     */
    String encode(String rawPassword);

    /**
     * 원본 비밀번호와 인코딩된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @param encodedPassword 인코딩된 비밀번호
     * @return 일치 여부
     */
    boolean matches(String rawPassword, String encodedPassword);
}
