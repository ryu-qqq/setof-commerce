package com.ryuqq.setof.integration.test.repository.legacy.faq;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.faq.LegacyFaqEntityFixtures;
import com.ryuqq.setof.storage.legacy.faq.condition.LegacyFaqConditionBuilder;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import com.ryuqq.setof.storage.legacy.faq.repository.LegacyFaqQueryDslRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LegacyFaqQueryDslRepository 통합 테스트.
 *
 * <p>레거시 FAQ QueryDSL 레포지토리의 DB 연동을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.LEGACY)
@DisplayName("LegacyFaqQueryDslRepository 통합 테스트")
class LegacyFaqQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacyFaqQueryDslRepository repository;

    @Autowired private LegacyFaqConditionBuilder conditionBuilder;

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("FaqType으로 FAQ를 조회한다")
        void shouldFindByFaqType() {
            // given
            LegacyFaqEntity entity1 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("회원 FAQ 1")
                            .displayOrder(1)
                            .build();

            LegacyFaqEntity entity2 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("회원 FAQ 2")
                            .displayOrder(2)
                            .build();

            LegacyFaqEntity entity3 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .title("배송 FAQ")
                            .displayOrder(1)
                            .build();

            persist(entity1);
            persist(entity2);
            persist(entity3);
            flushAndClear();

            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.MEMBER_LOGIN);

            // when
            List<LegacyFaqEntity> results = repository.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(2);
            assertThat(results)
                    .allMatch(
                            e ->
                                    e.getFaqType()
                                            == com.ryuqq.setof.domain.legacy.faq.FaqType
                                                    .MEMBER_LOGIN);
            assertThat(results.get(0).getTitle()).isEqualTo("회원 FAQ 1");
            assertThat(results.get(1).getTitle()).isEqualTo("회원 FAQ 2");
        }

        @Test
        @DisplayName("TOP 유형은 topDisplayOrder로 정렬한다")
        void shouldOrderByTopDisplayOrderForTopType() {
            // given
            LegacyFaqEntity top1 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.TOP)
                            .title("TOP FAQ 3")
                            .topDisplayOrder(3)
                            .displayOrder(100)
                            .build();

            LegacyFaqEntity top2 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.TOP)
                            .title("TOP FAQ 1")
                            .topDisplayOrder(1)
                            .displayOrder(200)
                            .build();

            LegacyFaqEntity top3 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.TOP)
                            .title("TOP FAQ 2")
                            .topDisplayOrder(2)
                            .displayOrder(50)
                            .build();

            persist(top1);
            persist(top2);
            persist(top3);
            flushAndClear();

            FaqSearchCriteria criteria = FaqSearchCriteria.ofTop();

            // when
            List<LegacyFaqEntity> results = repository.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(3);
            assertThat(results.get(0).getTitle()).isEqualTo("TOP FAQ 1");
            assertThat(results.get(0).getTopDisplayOrder()).isEqualTo(1);
            assertThat(results.get(1).getTitle()).isEqualTo("TOP FAQ 2");
            assertThat(results.get(1).getTopDisplayOrder()).isEqualTo(2);
            assertThat(results.get(2).getTitle()).isEqualTo("TOP FAQ 3");
            assertThat(results.get(2).getTopDisplayOrder()).isEqualTo(3);
        }

        @Test
        @DisplayName("일반 유형은 displayOrder로 정렬한다")
        void shouldOrderByDisplayOrderForNormalType() {
            // given
            LegacyFaqEntity entity1 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .title("배송 FAQ C")
                            .displayOrder(30)
                            .build();

            LegacyFaqEntity entity2 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .title("배송 FAQ A")
                            .displayOrder(10)
                            .build();

            LegacyFaqEntity entity3 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .title("배송 FAQ B")
                            .displayOrder(20)
                            .build();

            persist(entity1);
            persist(entity2);
            persist(entity3);
            flushAndClear();

            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);

            // when
            List<LegacyFaqEntity> results = repository.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(3);
            assertThat(results.get(0).getTitle()).isEqualTo("배송 FAQ A");
            assertThat(results.get(0).getDisplayOrder()).isEqualTo(10);
            assertThat(results.get(1).getTitle()).isEqualTo("배송 FAQ B");
            assertThat(results.get(1).getDisplayOrder()).isEqualTo(20);
            assertThat(results.get(2).getTitle()).isEqualTo("배송 FAQ C");
            assertThat(results.get(2).getDisplayOrder()).isEqualTo(30);
        }

        @Test
        @DisplayName("조건에 맞는 FAQ가 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoMatch() {
            // given
            LegacyFaqEntity entity =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .build();

            persist(entity);
            flushAndClear();

            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.SHIPPING);

            // when
            List<LegacyFaqEntity> results = repository.findByCriteria(criteria);

            // then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("여러 FaqType에 대해 각각 조회한다")
        void shouldFindByDifferentFaqTypes() {
            // given
            persist(
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("회원 FAQ")
                            .build());
            persist(
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .title("배송 FAQ")
                            .build());
            persist(
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.ORDER_PAYMENT)
                            .title("주문 FAQ")
                            .build());
            flushAndClear();

            // when
            List<LegacyFaqEntity> memberResults =
                    repository.findByCriteria(FaqSearchCriteria.of(FaqType.MEMBER_LOGIN));
            List<LegacyFaqEntity> shippingResults =
                    repository.findByCriteria(FaqSearchCriteria.of(FaqType.SHIPPING));
            List<LegacyFaqEntity> orderResults =
                    repository.findByCriteria(FaqSearchCriteria.of(FaqType.ORDER_PAYMENT));

            // then
            assertThat(memberResults).hasSize(1);
            assertThat(memberResults.get(0).getTitle()).isEqualTo("회원 FAQ");

            assertThat(shippingResults).hasSize(1);
            assertThat(shippingResults.get(0).getTitle()).isEqualTo("배송 FAQ");

            assertThat(orderResults).hasSize(1);
            assertThat(orderResults.get(0).getTitle()).isEqualTo("주문 FAQ");
        }

        @Test
        @DisplayName("동일 displayOrder인 경우에도 안정적으로 조회한다")
        void shouldHandleSameDisplayOrder() {
            // given
            LegacyFaqEntity entity1 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.CANCEL_REFUND)
                            .title("취소 FAQ 1")
                            .displayOrder(10)
                            .build();

            LegacyFaqEntity entity2 =
                    LegacyFaqEntityFixtures.builder()
                            .id(null)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.CANCEL_REFUND)
                            .title("취소 FAQ 2")
                            .displayOrder(10)
                            .build();

            persist(entity1);
            persist(entity2);
            flushAndClear();

            FaqSearchCriteria criteria = FaqSearchCriteria.of(FaqType.CANCEL_REFUND);

            // when
            List<LegacyFaqEntity> results = repository.findByCriteria(criteria);

            // then
            assertThat(results).hasSize(2);
            assertThat(results).allMatch(e -> e.getDisplayOrder() == 10);
        }
    }
}
