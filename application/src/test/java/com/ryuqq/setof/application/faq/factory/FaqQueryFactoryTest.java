package com.ryuqq.setof.application.faq.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("FaqQueryFactory 단위 테스트")
class FaqQueryFactoryTest {

    private final FaqQueryFactory sut = new FaqQueryFactory();

    @Nested
    @DisplayName("createCriteria() - SearchParams → SearchCriteria 변환")
    class CreateCriteriaTest {

        @Test
        @DisplayName("FaqSearchParams로부터 FaqSearchCriteria를 생성한다")
        void createCriteria_CreatesSearchCriteria() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.MEMBER_LOGIN);

            // when
            FaqSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.faqType()).isEqualTo(FaqType.MEMBER_LOGIN);
        }

        @Test
        @DisplayName("TOP 유형 파라미터로 TOP Criteria를 생성한다")
        void createCriteria_TopType_CreatesTopCriteria() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.TOP);

            // when
            FaqSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.faqType()).isEqualTo(FaqType.TOP);
            assertThat(result.isTop()).isTrue();
        }

        @Test
        @DisplayName("SHIPPING 유형 파라미터로 SHIPPING Criteria를 생성한다")
        void createCriteria_ShippingType_CreatesShippingCriteria() {
            // given
            FaqSearchParams params = FaqSearchParams.of(FaqType.SHIPPING);

            // when
            FaqSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.faqType()).isEqualTo(FaqType.SHIPPING);
            assertThat(result.isTop()).isFalse();
        }

        @Test
        @DisplayName("각 FaqType에 대해 올바른 Criteria를 생성한다")
        void createCriteria_EachFaqType_CreatesCorrectCriteria() {
            for (FaqType faqType : FaqType.values()) {
                // given
                FaqSearchParams params = FaqSearchParams.of(faqType);

                // when
                FaqSearchCriteria result = sut.createCriteria(params);

                // then
                assertThat(result.faqType()).isEqualTo(faqType);
            }
        }
    }
}
