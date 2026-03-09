package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderClient;
import org.springframework.stereotype.Component;

/**
 * PasswordManager - 비밀번호 인코딩/검증 Manager.
 *
 * <p>PasswordEncoderClient를 래핑합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class PasswordManager {

    private final PasswordEncoderClient passwordEncoderClient;

    public PasswordManager(PasswordEncoderClient passwordEncoderClient) {
        this.passwordEncoderClient = passwordEncoderClient;
    }

    /**
     * 원본 비밀번호를 인코딩합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @return 인코딩된 비밀번호
     */
    public String encode(String rawPassword) {
        return passwordEncoderClient.encode(rawPassword);
    }

    /**
     * 원본 비밀번호와 인코딩된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @param encodedPassword 인코딩된 비밀번호
     * @return 일치 여부
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoderClient.matches(rawPassword, encodedPassword);
    }
}
