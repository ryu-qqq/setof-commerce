package com.ryuqq.setof.application.member.port.out.client;

/**
 * Password Encoder Port
 *
 * <p>비밀번호 암호화/검증을 위한 Outbound Port
 *
 * <p>구현체는 Adapter 레이어에서 BCrypt 등으로 구현
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PasswordEncoderPort {

    /**
     * 비밀번호 암호화
     *
     * @param rawPassword 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    String encode(String rawPassword);

    /**
     * 비밀번호 검증
     *
     * @param rawPassword 평문 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return 일치 여부
     */
    boolean matches(String rawPassword, String encodedPassword);
}
