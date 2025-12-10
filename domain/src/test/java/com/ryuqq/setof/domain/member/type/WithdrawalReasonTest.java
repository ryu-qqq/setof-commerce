package com.ryuqq.setof.domain.member.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ryuqq.setof.domain.member.vo.WithdrawalReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("WithdrawalReason Enum")
class WithdrawalReasonTest {

    @Test
    @DisplayName("모든 탈퇴 사유 값 존재 확인")
    void shouldHaveAllReasonValues() {
        assertNotNull(WithdrawalReason.RARELY_USED);
        assertNotNull(WithdrawalReason.SERVICE_DISSATISFIED);
        assertNotNull(WithdrawalReason.PRIVACY_CONCERN);
        assertNotNull(WithdrawalReason.OTHER);
        assertEquals(4, WithdrawalReason.values().length);
    }

    @Test
    @DisplayName("valueOf로 Enum 조회")
    void shouldGetEnumByValueOf() {
        assertEquals(WithdrawalReason.RARELY_USED, WithdrawalReason.valueOf("RARELY_USED"));
        assertEquals(
                WithdrawalReason.SERVICE_DISSATISFIED,
                WithdrawalReason.valueOf("SERVICE_DISSATISFIED"));
        assertEquals(WithdrawalReason.PRIVACY_CONCERN, WithdrawalReason.valueOf("PRIVACY_CONCERN"));
        assertEquals(WithdrawalReason.OTHER, WithdrawalReason.valueOf("OTHER"));
    }

    @Test
    @DisplayName("displayName 메서드로 한글 표시명 반환")
    void shouldReturnDisplayName() {
        assertEquals("이용 빈도 낮음", WithdrawalReason.RARELY_USED.displayName());
        assertEquals("서비스 불만족", WithdrawalReason.SERVICE_DISSATISFIED.displayName());
        assertEquals("개인정보 우려", WithdrawalReason.PRIVACY_CONCERN.displayName());
        assertEquals("기타", WithdrawalReason.OTHER.displayName());
    }
}
