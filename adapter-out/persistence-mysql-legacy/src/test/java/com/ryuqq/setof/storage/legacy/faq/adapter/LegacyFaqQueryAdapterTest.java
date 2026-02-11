package com.ryuqq.setof.storage.legacy.faq.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import com.ryuqq.setof.storage.legacy.faq.LegacyFaqEntityFixtures;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import com.ryuqq.setof.storage.legacy.faq.mapper.LegacyFaqEntityMapper;
import com.ryuqq.setof.storage.legacy.faq.repository.LegacyFaqQueryDslRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyFaqQueryAdapter 단위 테스트.
 *
 * <p>어댑터 계층의 조회 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@DisplayName("LegacyFaqQueryAdapter 단위 테스트")
@ExtendWith(MockitoExtension.class)
class LegacyFaqQueryAdapterTest {

    @Mock private LegacyFaqQueryDslRepository queryDslRepository;

    private LegacyFaqQueryAdapter adapter;
    private LegacyFaqEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LegacyFaqEntityMapper();
        adapter = new LegacyFaqQueryAdapter(queryDslRepository, mapper);
    }

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 FAQ 목록을 조회하고 도메인으로 변환한다")
        void shouldFindByCriteriaAndConvertToDomain() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.MEMBER_LOGIN);

            LegacyFaqEntity entity1 =
                    LegacyFaqEntityFixtures.builder()
                            .id(1L)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("FAQ 1")
                            .build();

            LegacyFaqEntity entity2 =
                    LegacyFaqEntityFixtures.builder()
                            .id(2L)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("FAQ 2")
                            .build();

            given(queryDslRepository.findByCriteria(criteria))
                    .willReturn(List.of(entity1, entity2));

            // when
            List<Faq> results = adapter.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(2);
            assertThat(results.get(0).idValue()).isEqualTo(1L);
            assertThat(results.get(0).titleValue()).isEqualTo("FAQ 1");
            assertThat(results.get(1).idValue()).isEqualTo(2L);
            assertThat(results.get(1).titleValue()).isEqualTo("FAQ 2");

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoResults() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);

            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of());

            // when
            List<Faq> results = adapter.findByCriteria(criteria);

            // then
            assertThat(results).isEmpty();

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }

        @Test
        @DisplayName("TOP 유형 조회 시 정상적으로 변환한다")
        void shouldConvertTopFaqsCorrectly() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();

            LegacyFaqEntity topEntity =
                    LegacyFaqEntityFixtures.builder()
                            .id(10L)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.TOP)
                            .title("상단 고정 FAQ")
                            .topDisplayOrder(1)
                            .build();

            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of(topEntity));

            // when
            List<Faq> results = adapter.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).faqType()).isEqualTo(FaqType.TOP);
            assertThat(results.get(0).isTop()).isTrue();
            assertThat(results.get(0).topDisplayOrder()).isEqualTo(1);

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }

        @Test
        @DisplayName("여러 FAQ를 조회 시 모두 정상 변환한다")
        void shouldConvertMultipleFaqsCorrectly() {
            // given
            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.ORDER_PAYMENT);

            List<LegacyFaqEntity> entities =
                    List.of(
                            LegacyFaqEntityFixtures.builder()
                                    .id(1L)
                                    .faqType(
                                            com.ryuqq.setof.domain.legacy.faq.FaqType.ORDER_PAYMENT)
                                    .displayOrder(1)
                                    .build(),
                            LegacyFaqEntityFixtures.builder()
                                    .id(2L)
                                    .faqType(
                                            com.ryuqq.setof.domain.legacy.faq.FaqType.ORDER_PAYMENT)
                                    .displayOrder(2)
                                    .build(),
                            LegacyFaqEntityFixtures.builder()
                                    .id(3L)
                                    .faqType(
                                            com.ryuqq.setof.domain.legacy.faq.FaqType.ORDER_PAYMENT)
                                    .displayOrder(3)
                                    .build());

            given(queryDslRepository.findByCriteria(criteria)).willReturn(entities);

            // when
            List<Faq> results = adapter.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(3);
            assertThat(results).allMatch(faq -> faq.faqType() == FaqType.ORDER_PAYMENT);
            assertThat(results.get(0).displayOrderValue()).isEqualTo(1);
            assertThat(results.get(1).displayOrderValue()).isEqualTo(2);
            assertThat(results.get(2).displayOrderValue()).isEqualTo(3);

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }
    }
}
