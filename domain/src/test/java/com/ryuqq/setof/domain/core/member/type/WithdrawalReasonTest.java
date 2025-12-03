package com.ryuqq.setof.domain.core.member.type;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
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
}
