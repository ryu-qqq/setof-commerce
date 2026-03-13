package com.ryuqq.setof.adapter.out.persistence.banner.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BannerConditionBuilderTest - 배너 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerConditionBuilder 단위 테스트")
class BannerConditionBuilderTest {

    private BannerConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new BannerConditionBuilder();
    }

    @Nested
    @DisplayName("bannerGroupTypeEq 메서드 테스트")
    class BannerGroupTypeEqTest {

        @Test
        @DisplayName("유효한 배너 타입 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupTypeEq_WithValidType_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupTypeEq("RECOMMEND");
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 배너 타입 입력 시 null을 반환합니다")
        void bannerGroupTypeEq_WithNullType_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupTypeEq(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 배너 타입 입력 시 null을 반환합니다")
        void bannerGroupTypeEq_WithBlankType_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupTypeEq("   ");
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupActiveEq 메서드 테스트")
    class BannerGroupActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupActiveEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupActiveEq(true);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupActiveEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupActiveEq(false);
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupNotDeleted 메서드 테스트")
    class BannerGroupNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void bannerGroupNotDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupNotDeleted();
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("bannerSlideActiveEq 메서드 테스트")
    class BannerSlideActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void bannerSlideActiveEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerSlideActiveEq(true);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void bannerSlideActiveEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerSlideActiveEq(false);
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("bannerSlideNotDeleted 메서드 테스트")
    class BannerSlideNotDeletedTest {

        @Test
        @DisplayName("항상 BooleanExpression을 반환합니다")
        void bannerSlideNotDeleted_Always_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerSlideNotDeleted();
            assertThat(result).isNotNull();
        }
    }
}
