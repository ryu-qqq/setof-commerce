package com.ryuqq.setof.domain.core.member.type;

import com.ryuqq.setof.domain.core.member.vo.Gender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gender Enum")
class GenderTest {

    @Test
    @DisplayName("모든 성별 값 존재 확인")
    void shouldHaveAllGenderValues() {
        assertNotNull(Gender.M);
        assertNotNull(Gender.W);
        assertNotNull(Gender.N);
        assertEquals(3, Gender.values().length);
    }

    @Test
    @DisplayName("valueOf로 Enum 조회")
    void shouldGetEnumByValueOf() {
        assertEquals(Gender.M, Gender.valueOf("M"));
        assertEquals(Gender.W, Gender.valueOf("W"));
        assertEquals(Gender.N, Gender.valueOf("N"));
    }
}
