package com.ryuqq.setof.storage.legacy.seller.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacySellerConditionBuilder 단위 테스트.
 *
 * <p>레거시 셀러 QueryDSL 조건 빌더 로직을 검증합니다.
 */
@DisplayName("레거시 셀러 ConditionBuilder 테스트")
@Tag("unit")
@ExtendWith(MockitoExtension.class)
class LegacySellerConditionBuilderTest {

    @InjectMocks private LegacySellerConditionBuilder conditionBuilder;

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("ID가 주어지면 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenIdProvided() {
            // when
            BooleanExpression result = conditionBuilder.idEq(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("legacySellerEntity.id = 1");
        }

        @Test
        @DisplayName("ID가 null이면 null을 반환합니다")
        void shouldReturnNullWhenIdIsNull() {
            // when
            BooleanExpression result = conditionBuilder.idEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("idIn 메서드 테스트")
    class IdInTest {

        @Test
        @DisplayName("ID 목록이 주어지면 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenIdsProvided() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);

            // when
            BooleanExpression result = conditionBuilder.idIn(ids);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("legacySellerEntity.id in [1, 2, 3]");
        }

        @Test
        @DisplayName("ID 목록이 null이면 null을 반환합니다")
        void shouldReturnNullWhenIdsIsNull() {
            // when
            BooleanExpression result = conditionBuilder.idIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("ID 목록이 비어있으면 null을 반환합니다")
        void shouldReturnNullWhenIdsIsEmpty() {
            // when
            BooleanExpression result = conditionBuilder.idIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("idNe 메서드 테스트")
    class IdNeTest {

        @Test
        @DisplayName("제외 ID가 주어지면 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenExcludeIdProvided() {
            // when
            BooleanExpression result = conditionBuilder.idNe(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("legacySellerEntity.id != 1");
        }

        @Test
        @DisplayName("제외 ID가 null이면 null을 반환합니다")
        void shouldReturnNullWhenExcludeIdIsNull() {
            // when
            BooleanExpression result = conditionBuilder.idNe(null);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("sellerNameEq 메서드 테스트")
    class SellerNameEqTest {

        @Test
        @DisplayName("셀러명이 주어지면 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenSellerNameProvided() {
            // when
            BooleanExpression result = conditionBuilder.sellerNameEq("테스트 셀러");

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("legacySellerEntity.sellerName = 테스트 셀러");
        }

        @Test
        @DisplayName("셀러명이 null이면 null을 반환합니다")
        void shouldReturnNullWhenSellerNameIsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerNameEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("sellerNameContains 메서드 테스트")
    class SellerNameContainsTest {

        @Test
        @DisplayName("셀러명이 주어지면 LIKE 조건 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenSellerNameProvided() {
            // when
            BooleanExpression result = conditionBuilder.sellerNameContains("테스트");

            // then
            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("lower");
        }

        @Test
        @DisplayName("셀러명이 null이면 null을 반환합니다")
        void shouldReturnNullWhenSellerNameIsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerNameContains(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("셀러명이 공백이면 null을 반환합니다")
        void shouldReturnNullWhenSellerNameIsBlank() {
            // when
            BooleanExpression result = conditionBuilder.sellerNameContains("   ");

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("searchFieldContains 메서드 테스트")
    class SearchFieldContainsTest {

        @Test
        @DisplayName("SELLER_NAME 검색 필드는 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionForSellerNameField() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(SellerSearchField.SELLER_NAME, "테스트");

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("REGISTRATION_NUMBER 검색 필드는 null을 반환합니다 (다른 테이블)")
        void shouldReturnNullForRegistrationNumberField() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(
                            SellerSearchField.REGISTRATION_NUMBER, "123-45-67890");

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("COMPANY_NAME 검색 필드는 null을 반환합니다 (다른 테이블)")
        void shouldReturnNullForCompanyNameField() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(SellerSearchField.COMPANY_NAME, "테스트회사");

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("REPRESENTATIVE_NAME 검색 필드는 null을 반환합니다 (다른 테이블)")
        void shouldReturnNullForRepresentativeNameField() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(
                            SellerSearchField.REPRESENTATIVE_NAME, "홍길동");

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("검색어가 null이면 null을 반환합니다")
        void shouldReturnNullWhenSearchWordIsNull() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(SellerSearchField.SELLER_NAME, null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("검색어가 공백이면 null을 반환합니다")
        void shouldReturnNullWhenSearchWordIsBlank() {
            // when
            BooleanExpression result =
                    conditionBuilder.searchFieldContains(SellerSearchField.SELLER_NAME, "   ");

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("검색 필드가 null이면 sellerName으로 검색합니다")
        void shouldSearchSellerNameWhenFieldIsNull() {
            // when
            BooleanExpression result = conditionBuilder.searchFieldContains(null, "테스트");

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("searchCondition 메서드 테스트")
    class SearchConditionTest {

        @Test
        @DisplayName("검색 조건이 있으면 BooleanExpression을 반환합니다")
        void shouldReturnBooleanExpressionWhenSearchConditionExists() {
            // given
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(null, SellerSearchField.SELLER_NAME, "테스트", null);

            // when
            BooleanExpression result = conditionBuilder.searchCondition(criteria);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("검색 조건이 없으면 null을 반환합니다")
        void shouldReturnNullWhenNoSearchCondition() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.of(null, null, null, null);

            // when
            BooleanExpression result = conditionBuilder.searchCondition(criteria);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("검색어가 공백이면 null을 반환합니다")
        void shouldReturnNullWhenSearchWordIsBlank() {
            // given
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(null, SellerSearchField.SELLER_NAME, "   ", null);

            // when
            BooleanExpression result = conditionBuilder.searchCondition(criteria);

            // then
            assertThat(result).isNull();
        }
    }
}
