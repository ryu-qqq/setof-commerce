package com.ryuqq.setof.adapter.out.persistence.navigation.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * NavigationMenuConditionBuilderTest - 네비게이션 메뉴 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("NavigationMenuConditionBuilder 단위 테스트")
class NavigationMenuConditionBuilderTest {

    private NavigationMenuConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new NavigationMenuConditionBuilder();
    }

    @Nested
    @DisplayName("activeEq 메서드 테스트")
    class ActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.activeEq(Boolean.TRUE);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.activeEq(Boolean.FALSE);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void activeEq_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.activeEq(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("notDeleted 메서드 테스트")
    class NotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void notDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.notDeleted();
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("displayStartAfter 메서드 테스트")
    class DisplayStartAfterTest {

        @Test
        @DisplayName("유효한 시작일 입력 시 BooleanExpression을 반환합니다")
        void displayStartAfter_WithValidDate_ReturnsBooleanExpression() {
            BooleanExpression result =
                    conditionBuilder.displayStartAfter(java.time.Instant.now().minusSeconds(3600));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void displayStartAfter_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.displayStartAfter(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("displayEndBefore 메서드 테스트")
    class DisplayEndBeforeTest {

        @Test
        @DisplayName("유효한 종료일 입력 시 BooleanExpression을 반환합니다")
        void displayEndBefore_WithValidDate_ReturnsBooleanExpression() {
            BooleanExpression result =
                    conditionBuilder.displayEndBefore(java.time.Instant.now().plusSeconds(86400));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void displayEndBefore_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.displayEndBefore(null);
            assertThat(result).isNull();
        }
    }
}
