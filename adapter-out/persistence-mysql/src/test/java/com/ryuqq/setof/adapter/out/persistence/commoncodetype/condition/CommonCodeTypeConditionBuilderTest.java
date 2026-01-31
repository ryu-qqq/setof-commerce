package com.ryuqq.setof.adapter.out.persistence.commoncodetype.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSearchCriteria;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * CommonCodeTypeConditionBuilderTest - 공통 코드 타입 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("CommonCodeTypeConditionBuilder 단위 테스트")
class CommonCodeTypeConditionBuilderTest {

    private CommonCodeTypeConditionBuilder conditionBuilder;

    @Mock private CommonCodeTypeSearchCriteria criteria;

    @BeforeEach
    void setUp() {
        conditionBuilder = new CommonCodeTypeConditionBuilder();
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
    // 3. idNe 테스트
    // ========================================================================

    @Nested
    @DisplayName("idNe 메서드 테스트")
    class IdNeTest {

        @Test
        @DisplayName("유효한 ID 입력 시 BooleanExpression을 반환합니다")
        void idNe_WithValidId_ReturnsBooleanExpression() {
            // given
            Long excludeId = 1L;

            // when
            BooleanExpression result = conditionBuilder.idNe(excludeId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null ID 입력 시 null을 반환합니다")
        void idNe_WithNullId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.idNe(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. codeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("codeEq 메서드 테스트")
    class CodeEqTest {

        @Test
        @DisplayName("유효한 코드 입력 시 BooleanExpression을 반환합니다")
        void codeEq_WithValidCode_ReturnsBooleanExpression() {
            // given
            String code = "PAYMENT_METHOD";

            // when
            BooleanExpression result = conditionBuilder.codeEq(code);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 코드 입력 시 null을 반환합니다")
        void codeEq_WithNullCode_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.codeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. displayOrderEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("displayOrderEq 메서드 테스트")
    class DisplayOrderEqTest {

        @Test
        @DisplayName("유효한 순서 입력 시 BooleanExpression을 반환합니다")
        void displayOrderEq_WithValidOrder_ReturnsBooleanExpression() {
            // given
            Integer displayOrder = 1;

            // when
            BooleanExpression result = conditionBuilder.displayOrderEq(displayOrder);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 순서 입력 시 null을 반환합니다")
        void displayOrderEq_WithNullOrder_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.displayOrderEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 6. activeEq (Boolean) 테스트
    // ========================================================================

    @Nested
    @DisplayName("activeEq (Boolean) 메서드 테스트")
    class ActiveEqBooleanTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithTrue_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.activeEq(Boolean.TRUE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithFalse_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.activeEq(Boolean.FALSE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void activeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.activeEq((Boolean) null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 7. activeEq (Criteria) 테스트
    // ========================================================================

    @Nested
    @DisplayName("activeEq (Criteria) 메서드 테스트")
    class ActiveEqCriteriaTest {

        @Test
        @DisplayName("필터가 있으면 BooleanExpression을 반환합니다")
        void activeEq_WithActiveFilter_ReturnsBooleanExpression() {
            // given
            given(criteria.hasActiveFilter()).willReturn(true);
            given(criteria.active()).willReturn(true);

            // when
            BooleanExpression result = conditionBuilder.activeEq(criteria);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("필터가 없으면 null을 반환합니다")
        void activeEq_WithoutActiveFilter_ReturnsNull() {
            // given
            given(criteria.hasActiveFilter()).willReturn(false);

            // when
            BooleanExpression result = conditionBuilder.activeEq(criteria);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 8. keywordContains (String) 테스트
    // ========================================================================

    @Nested
    @DisplayName("keywordContains (String) 메서드 테스트")
    class KeywordContainsStringTest {

        @Test
        @DisplayName("유효한 키워드 입력 시 BooleanExpression을 반환합니다")
        void keywordContains_WithValidKeyword_ReturnsBooleanExpression() {
            // given
            String keyword = "결제";

            // when
            BooleanExpression result = conditionBuilder.keywordContains(keyword);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 키워드 입력 시 null을 반환합니다")
        void keywordContains_WithNullKeyword_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.keywordContains((String) null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 키워드 입력 시 null을 반환합니다")
        void keywordContains_WithBlankKeyword_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.keywordContains("   ");

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 9. keywordContains (Criteria) 테스트
    // ========================================================================

    @Nested
    @DisplayName("keywordContains (Criteria) 메서드 테스트")
    class KeywordContainsCriteriaTest {

        @Test
        @DisplayName("키워드가 있으면 BooleanExpression을 반환합니다")
        void keywordContains_WithKeyword_ReturnsBooleanExpression() {
            // given
            given(criteria.hasKeyword()).willReturn(true);
            given(criteria.keyword()).willReturn("결제");

            // when
            BooleanExpression result = conditionBuilder.keywordContains(criteria);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("키워드가 없으면 null을 반환합니다")
        void keywordContains_WithoutKeyword_ReturnsNull() {
            // given
            given(criteria.hasKeyword()).willReturn(false);

            // when
            BooleanExpression result = conditionBuilder.keywordContains(criteria);

            // then
            assertThat(result).isNull();
        }
    }
}
