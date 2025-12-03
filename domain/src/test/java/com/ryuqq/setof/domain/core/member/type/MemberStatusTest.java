package com.ryuqq.setof.domain.core.member.type;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.vo.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemberStatus Enum")
class MemberStatusTest {

    @Test
    @DisplayName("모든 상태 값 존재 확인")
    void shouldHaveAllStatusValues() {
        assertNotNull(MemberStatus.ACTIVE);
        assertNotNull(MemberStatus.INACTIVE);
        assertNotNull(MemberStatus.SUSPENDED);
        assertNotNull(MemberStatus.WITHDRAWN);
        assertEquals(4, MemberStatus.values().length);
    }

    @Test
    @DisplayName("valueOf로 Enum 조회")
    void shouldGetEnumByValueOf() {
        assertEquals(MemberStatus.ACTIVE, MemberStatus.valueOf("ACTIVE"));
        assertEquals(MemberStatus.INACTIVE, MemberStatus.valueOf("INACTIVE"));
        assertEquals(MemberStatus.SUSPENDED, MemberStatus.valueOf("SUSPENDED"));
        assertEquals(MemberStatus.WITHDRAWN, MemberStatus.valueOf("WITHDRAWN"));
    }
}
