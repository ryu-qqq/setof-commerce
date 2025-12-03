package com.ryuqq.setof.domain.core.member.type;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthProvider Enum")
class AuthProviderTest {

    @Test
    @DisplayName("LOCAL과 KAKAO 값 존재 확인")
    void shouldHaveLocalAndKakaoValues() {
        assertNotNull(AuthProvider.LOCAL);
        assertNotNull(AuthProvider.KAKAO);
        assertEquals(2, AuthProvider.values().length);
    }

    @Test
    @DisplayName("valueOf로 Enum 조회")
    void shouldGetEnumByValueOf() {
        assertEquals(AuthProvider.LOCAL, AuthProvider.valueOf("LOCAL"));
        assertEquals(AuthProvider.KAKAO, AuthProvider.valueOf("KAKAO"));
    }
}
