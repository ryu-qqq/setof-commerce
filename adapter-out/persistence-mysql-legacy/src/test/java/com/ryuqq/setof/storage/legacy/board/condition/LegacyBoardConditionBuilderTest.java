package com.ryuqq.setof.storage.legacy.board.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * LegacyBoardConditionBuilder 단위 테스트.
 *
 * <p>QueryDSL 동적 쿼리 조건 빌더를 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("LegacyBoardConditionBuilder 테스트")
class LegacyBoardConditionBuilderTest {

    private final LegacyBoardConditionBuilder conditionBuilder = new LegacyBoardConditionBuilder();

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("ID 값이 있으면 BooleanExpression을 반환합니다")
        void shouldReturnExpressionWhenIdIsNotNull() {
            // when
            BooleanExpression result = conditionBuilder.idEq(1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("ID가 null이면 null을 반환합니다")
        void shouldReturnNullWhenIdIsNull() {
            // when
            BooleanExpression result = conditionBuilder.idEq(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("다양한 ID 값에 대해 정상 동작합니다")
        void shouldWorkWithVariousIds() {
            // when & then
            assertThat(conditionBuilder.idEq(1L)).isNotNull();
            assertThat(conditionBuilder.idEq(100L)).isNotNull();
            assertThat(conditionBuilder.idEq(999L)).isNotNull();
            assertThat(conditionBuilder.idEq(null)).isNull();
        }
    }
}
