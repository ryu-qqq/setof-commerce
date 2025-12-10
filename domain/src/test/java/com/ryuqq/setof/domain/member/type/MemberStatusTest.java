package com.ryuqq.setof.domain.member.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ryuqq.setof.domain.member.vo.MemberStatus;
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

    @Test
    @DisplayName("displayName 메서드로 한글 표시명 반환")
    void shouldReturnDisplayName() {
        assertEquals("활동", MemberStatus.ACTIVE.displayName());
        assertEquals("휴면", MemberStatus.INACTIVE.displayName());
        assertEquals("정지", MemberStatus.SUSPENDED.displayName());
        assertEquals("탈퇴", MemberStatus.WITHDRAWN.displayName());
    }
}
