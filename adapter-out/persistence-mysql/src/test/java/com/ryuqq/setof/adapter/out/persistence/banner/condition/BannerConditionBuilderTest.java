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

    @Nested
    @DisplayName("bannerGroupActiveEq(Boolean) nullable 메서드 테스트")
    class BannerGroupActiveEqNullableTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupActiveEq_WithTrue_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupActiveEq(Boolean.TRUE);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupActiveEq_WithFalse_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupActiveEq(Boolean.FALSE);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void bannerGroupActiveEq_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupActiveEq((Boolean) null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupDisplayStartAfter 메서드 테스트")
    class BannerGroupDisplayStartAfterTest {

        @Test
        @DisplayName("유효한 시작일 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupDisplayStartAfter_WithValidDate_ReturnsBooleanExpression() {
            BooleanExpression result =
                    conditionBuilder.bannerGroupDisplayStartAfter(
                            java.time.Instant.now().minusSeconds(3600));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void bannerGroupDisplayStartAfter_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupDisplayStartAfter(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupDisplayEndBefore 메서드 테스트")
    class BannerGroupDisplayEndBeforeTest {

        @Test
        @DisplayName("유효한 종료일 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupDisplayEndBefore_WithValidDate_ReturnsBooleanExpression() {
            BooleanExpression result =
                    conditionBuilder.bannerGroupDisplayEndBefore(
                            java.time.Instant.now().plusSeconds(86400));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void bannerGroupDisplayEndBefore_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupDisplayEndBefore(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupTitleContains 메서드 테스트")
    class BannerGroupTitleContainsTest {

        @Test
        @DisplayName("유효한 검색어 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupTitleContains_WithValidKeyword_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupTitleContains("테스트");
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void bannerGroupTitleContains_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupTitleContains(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 문자열 입력 시 null을 반환합니다")
        void bannerGroupTitleContains_WithBlank_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupTitleContains("   ");
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("bannerGroupIdLt 메서드 테스트")
    class BannerGroupIdLtTest {

        @Test
        @DisplayName("유효한 ID 입력 시 BooleanExpression을 반환합니다")
        void bannerGroupIdLt_WithValidId_ReturnsBooleanExpression() {
            BooleanExpression result = conditionBuilder.bannerGroupIdLt(100L);
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다 (No-Offset 미적용)")
        void bannerGroupIdLt_WithNull_ReturnsNull() {
            BooleanExpression result = conditionBuilder.bannerGroupIdLt(null);
            assertThat(result).isNull();
        }
    }
}
