package com.ryuqq.setof.adapter.out.persistence.displaycomponent.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DisplayComponentConditionBuilderTest - 디스플레이 컴포넌트 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DisplayComponentConditionBuilder 단위 테스트")
class DisplayComponentConditionBuilderTest {

    private DisplayComponentConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new DisplayComponentConditionBuilder();
    }

    @Nested
    @DisplayName("componentContentPageIdEq 메서드 테스트")
    class ComponentContentPageIdEqTest {

        @Test
        @DisplayName("유효한 contentPageId 입력 시 BooleanExpression을 반환합니다")
        void componentContentPageIdEq_WithValidId_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.componentContentPageIdEq(1L);
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("componentActiveEq 메서드 테스트")
    class ComponentActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void componentActiveEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.componentActiveEq(true);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void componentActiveEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.componentActiveEq(false);
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("componentNotDeleted 메서드 테스트")
    class ComponentNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void componentNotDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.componentNotDeleted();
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("displayTabComponentIdIn 메서드 테스트")
    class DisplayTabComponentIdInTest {

        @Test
        @DisplayName("유효한 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void displayTabComponentIdIn_WithValidIds_ReturnsBooleanExpression() {
            BooleanExpression result =
                    conditionBuilder.displayTabComponentIdIn(List.of(1L, 2L, 3L));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 목록 입력 시 null을 반환합니다")
        void displayTabComponentIdIn_WithNullList_ReturnsNull() {
            BooleanExpression result = conditionBuilder.displayTabComponentIdIn(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 목록 입력 시 null을 반환합니다")
        void displayTabComponentIdIn_WithEmptyList_ReturnsNull() {
            BooleanExpression result =
                    conditionBuilder.displayTabComponentIdIn(Collections.emptyList());
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("displayTabNotDeleted 메서드 테스트")
    class DisplayTabNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void displayTabNotDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.displayTabNotDeleted();
            assertThat(result).isNotNull();
        }
    }
}
