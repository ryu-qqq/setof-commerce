package com.ryuqq.setof.adapter.out.persistence.productgroup.condition;

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
 * ProductGroupConditionBuilderTest - 상품그룹 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ProductGroupConditionBuilder 단위 테스트")
class ProductGroupConditionBuilderTest {

    private ProductGroupConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new ProductGroupConditionBuilder();
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
    // 3. sellerIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("sellerIdIn 메서드 테스트")
    class SellerIdInTest {

        @Test
        @DisplayName("유효한 셀러 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void sellerIdIn_WithValidIds_ReturnsBooleanExpression() {
            // given
            List<Long> sellerIds = List.of(1L, 2L);

            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(sellerIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 셀러 ID 목록 입력 시 null을 반환합니다")
        void sellerIdIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 셀러 ID 목록 입력 시 null을 반환합니다")
        void sellerIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(Collections.emptyList());

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
        @DisplayName("유효한 브랜드 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void brandIdIn_WithValidIds_ReturnsBooleanExpression() {
            // given
            List<Long> brandIds = List.of(1L, 2L);

            // when
            BooleanExpression result = conditionBuilder.brandIdIn(brandIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 브랜드 ID 목록 입력 시 null을 반환합니다")
        void brandIdIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 브랜드 ID 목록 입력 시 null을 반환합니다")
        void brandIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.brandIdIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. categoryIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("categoryIdIn 메서드 테스트")
    class CategoryIdInTest {

        @Test
        @DisplayName("유효한 카테고리 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void categoryIdIn_WithValidIds_ReturnsBooleanExpression() {
            // given
            List<Long> categoryIds = List.of(1L, 2L, 3L);

            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(categoryIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 카테고리 ID 목록 입력 시 null을 반환합니다")
        void categoryIdIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 카테고리 ID 목록 입력 시 null을 반환합니다")
        void categoryIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryIdIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 6. statusNotDeleted 테스트
    // ========================================================================

    @Nested
    @DisplayName("statusNotDeleted 메서드 테스트")
    class StatusNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void statusNotDeleted_Always_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.statusNotDeleted();

            // then
            assertThat(result).isNotNull();
        }
    }
}
