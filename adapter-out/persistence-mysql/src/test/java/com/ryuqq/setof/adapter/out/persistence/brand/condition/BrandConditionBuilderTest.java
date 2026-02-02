package com.ryuqq.setof.adapter.out.persistence.brand.condition;

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
 * BrandConditionBuilderTest - 브랜드 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandConditionBuilder 단위 테스트")
class BrandConditionBuilderTest {

    private BrandConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new BrandConditionBuilder();
    }

    // ========================================================================
    // 1. idEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("유효한 ID 입력 시 BooleanExpression을 반환합니다")
        void idEq_WithValidId_ReturnsBooleanExpression() {
            // given
            Long id = 1L;

            // when
            BooleanExpression result = conditionBuilder.idEq(id);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null ID 입력 시 null을 반환합니다")
        void idEq_WithNullId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.idEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 2. idIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("idIn 메서드 테스트")
    class IdInTest {

        @Test
        @DisplayName("유효한 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void idIn_WithValidIds_ReturnsBooleanExpression() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);

            // when
            BooleanExpression result = conditionBuilder.idIn(ids);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 목록 입력 시 null을 반환합니다")
        void idIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.idIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 목록 입력 시 null을 반환합니다")
        void idIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.idIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 3. brandNameContains 테스트
    // ========================================================================

    @Nested
    @DisplayName("brandNameContains 메서드 테스트")
    class BrandNameContainsTest {

        @Test
        @DisplayName("유효한 브랜드명 입력 시 BooleanExpression을 반환합니다")
        void brandNameContains_WithValidName_ReturnsBooleanExpression() {
            // given
            String brandName = "테스트";

            // when
            BooleanExpression result = conditionBuilder.brandNameContains(brandName);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 브랜드명 입력 시 null을 반환합니다")
        void brandNameContains_WithNullName_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandNameContains(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 브랜드명 입력 시 null을 반환합니다")
        void brandNameContains_WithBlankName_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandNameContains("   ");

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. displayedEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("displayedEq 메서드 테스트")
    class DisplayedEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void displayedEq_WithTrue_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.displayedEq(Boolean.TRUE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void displayedEq_WithFalse_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.displayedEq(Boolean.FALSE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void displayedEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.displayedEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. notDeleted 테스트
    // ========================================================================

    @Nested
    @DisplayName("notDeleted 메서드 테스트")
    class NotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void notDeleted_Always_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.notDeleted();

            // then
            assertThat(result).isNotNull();
        }
    }
}
