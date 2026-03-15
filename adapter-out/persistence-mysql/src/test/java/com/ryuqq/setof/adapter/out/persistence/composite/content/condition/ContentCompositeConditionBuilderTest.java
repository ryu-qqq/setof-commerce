package com.ryuqq.setof.adapter.out.persistence.composite.content.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentCompositeConditionBuilderTest - 콘텐츠 Composite 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null/빈 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentCompositeConditionBuilder 단위 테스트")
class ContentCompositeConditionBuilderTest {

    private ContentCompositeConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new ContentCompositeConditionBuilder();
    }

    // ========================================================================
    // 1. componentIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("componentIdIn 메서드 테스트")
    class ComponentIdInTest {

        @Test
        @DisplayName("componentIds가 유효하면 BooleanExpression을 반환합니다")
        void componentIdIn_WithValidIds_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.componentIdIn(List.of(1L, 2L, 3L));

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("componentIds가 null이면 null을 반환합니다")
        void componentIdIn_WithNullIds_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.componentIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("componentIds가 빈 리스트이면 null을 반환합니다")
        void componentIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.componentIdIn(List.of());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 2. fixedProductNotDeleted 테스트
    // ========================================================================

    @Nested
    @DisplayName("fixedProductNotDeleted 메서드 테스트")
    class FixedProductNotDeletedTest {

        @Test
        @DisplayName("삭제되지 않은 조건을 반환합니다")
        void fixedProductNotDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.fixedProductNotDeleted();

            // then
            assertThat(result).isNotNull();
        }
    }

    // ========================================================================
    // 3. categoryIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("categoryIdIn 메서드 테스트")
    class CategoryIdInTest {

        @Test
        @DisplayName("categoryIds가 유효하면 BooleanExpression을 반환합니다")
        void categoryIdIn_WithValidIds_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(List.of(10L, 20L));

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("categoryIds가 null이면 null을 반환합니다")
        void categoryIdIn_WithNullIds_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("categoryIds가 빈 리스트이면 null을 반환합니다")
        void categoryIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(List.of());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. brandIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("brandIdIn 메서드 테스트")
    class BrandIdInTest {

        @Test
        @DisplayName("brandIds가 유효하면 BooleanExpression을 반환합니다")
        void brandIdIn_WithValidIds_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.brandIdIn(List.of(5L, 6L));

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("brandIds가 null이면 null을 반환합니다")
        void brandIdIn_WithNullIds_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("brandIds가 빈 리스트이면 null을 반환합니다")
        void brandIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandIdIn(List.of());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. productGroupStatusNotDeleted 테스트
    // ========================================================================

    @Nested
    @DisplayName("productGroupStatusNotDeleted 메서드 테스트")
    class ProductGroupStatusNotDeletedTest {

        @Test
        @DisplayName("상품 그룹 삭제되지 않은 조건을 반환합니다")
        void productGroupStatusNotDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.productGroupStatusNotDeleted();

            // then
            assertThat(result).isNotNull();
        }
    }
}
