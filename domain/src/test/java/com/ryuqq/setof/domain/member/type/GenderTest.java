package com.ryuqq.setof.domain.member.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ryuqq.setof.domain.member.vo.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("displayName 메서드로 한글 표시명 반환")
    void shouldReturnDisplayName() {
        assertEquals("남성", Gender.M.displayName());
        assertEquals("여성", Gender.W.displayName());
        assertEquals("미지정", Gender.N.displayName());
    }
}
