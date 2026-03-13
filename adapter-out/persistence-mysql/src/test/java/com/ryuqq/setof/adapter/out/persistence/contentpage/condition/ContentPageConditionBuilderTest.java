package com.ryuqq.setof.adapter.out.persistence.contentpage.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentPageConditionBuilderTest - 콘텐츠 페이지 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentPageConditionBuilder 단위 테스트")
class ContentPageConditionBuilderTest {

    private ContentPageConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new ContentPageConditionBuilder();
    }

    @Nested
    @DisplayName("contentPageIdEq 메서드 테스트")
    class ContentPageIdEqTest {

        @Test
        @DisplayName("유효한 ID 입력 시 BooleanExpression을 반환합니다")
        void contentPageIdEq_WithValidId_ReturnsBooleanExpression() {
            Long id = 1L;
            BooleanExpression result = conditionBuilder.contentPageIdEq(id);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null ID 입력 시 null을 반환합니다")
        void contentPageIdEq_WithNullId_ReturnsNull() {
            BooleanExpression result = conditionBuilder.contentPageIdEq(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("contentPageActiveEq 메서드 테스트")
    class ContentPageActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void contentPageActiveEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.contentPageActiveEq(true);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void contentPageActiveEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.contentPageActiveEq(false);
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("contentPageNotDeleted 메서드 테스트")
    class ContentPageNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void contentPageNotDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.contentPageNotDeleted();
            assertThat(result).isNotNull();
        }
    }
}
