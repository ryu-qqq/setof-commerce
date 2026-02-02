package com.ryuqq.setof.adapter.out.persistence.category.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CategoryConditionBuilderTest - 카테고리 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryConditionBuilder 단위 테스트")
class CategoryConditionBuilderTest {

    private CategoryConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new CategoryConditionBuilder();
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
    // 3. categoryNameContains 테스트
    // ========================================================================

    @Nested
    @DisplayName("categoryNameContains 메서드 테스트")
    class CategoryNameContainsTest {

        @Test
        @DisplayName("유효한 카테고리명 입력 시 BooleanExpression을 반환합니다")
        void categoryNameContains_WithValidName_ReturnsBooleanExpression() {
            // given
            String categoryName = "테스트";

            // when
            BooleanExpression result = conditionBuilder.categoryNameContains(categoryName);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 카테고리명 입력 시 null을 반환합니다")
        void categoryNameContains_WithNullName_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryNameContains(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 카테고리명 입력 시 null을 반환합니다")
        void categoryNameContains_WithBlankName_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryNameContains("   ");

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. parentCategoryIdEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("parentCategoryIdEq 메서드 테스트")
    class ParentCategoryIdEqTest {

        @Test
        @DisplayName("유효한 부모 ID 입력 시 BooleanExpression을 반환합니다")
        void parentCategoryIdEq_WithValidId_ReturnsBooleanExpression() {
            // given
            Long parentId = 1L;

            // when
            BooleanExpression result = conditionBuilder.parentCategoryIdEq(parentId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 부모 ID 입력 시 null을 반환합니다")
        void parentCategoryIdEq_WithNullId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.parentCategoryIdEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. categoryDepthEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("categoryDepthEq 메서드 테스트")
    class CategoryDepthEqTest {

        @Test
        @DisplayName("유효한 깊이 입력 시 BooleanExpression을 반환합니다")
        void categoryDepthEq_WithValidDepth_ReturnsBooleanExpression() {
            // given
            Integer depth = 1;

            // when
            BooleanExpression result = conditionBuilder.categoryDepthEq(depth);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 깊이 입력 시 null을 반환합니다")
        void categoryDepthEq_WithNullDepth_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryDepthEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 6. displayedEq 테스트
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
    // 7. targetGroupEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("targetGroupEq 메서드 테스트")
    class TargetGroupEqTest {

        @Test
        @DisplayName("유효한 타겟 그룹 입력 시 BooleanExpression을 반환합니다")
        void targetGroupEq_WithValidTargetGroup_ReturnsBooleanExpression() {
            // given
            TargetGroup targetGroup = TargetGroup.MALE;

            // when
            BooleanExpression result = conditionBuilder.targetGroupEq(targetGroup);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 타겟 그룹 입력 시 null을 반환합니다")
        void targetGroupEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.targetGroupEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 8. categoryTypeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("categoryTypeEq 메서드 테스트")
    class CategoryTypeEqTest {

        @Test
        @DisplayName("유효한 카테고리 타입 입력 시 BooleanExpression을 반환합니다")
        void categoryTypeEq_WithValidType_ReturnsBooleanExpression() {
            // given
            CategoryType categoryType = CategoryType.CLOTHING;

            // when
            BooleanExpression result = conditionBuilder.categoryTypeEq(categoryType);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 카테고리 타입 입력 시 null을 반환합니다")
        void categoryTypeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.categoryTypeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 9. pathStartsWith 테스트
    // ========================================================================

    @Nested
    @DisplayName("pathStartsWith 메서드 테스트")
    class PathStartsWithTest {

        @Test
        @DisplayName("유효한 경로 접두사 입력 시 BooleanExpression을 반환합니다")
        void pathStartsWith_WithValidPrefix_ReturnsBooleanExpression() {
            // given
            String pathPrefix = "/1/";

            // when
            BooleanExpression result = conditionBuilder.pathStartsWith(pathPrefix);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 경로 접두사 입력 시 null을 반환합니다")
        void pathStartsWith_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.pathStartsWith(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 경로 접두사 입력 시 null을 반환합니다")
        void pathStartsWith_WithBlank_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.pathStartsWith("   ");

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 10. notDeleted 테스트
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
