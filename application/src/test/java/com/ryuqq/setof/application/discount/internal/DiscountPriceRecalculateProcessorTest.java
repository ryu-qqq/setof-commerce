package com.ryuqq.setof.application.discount.internal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.factory.ProductGroupPriceUpdateFactory;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.manager.ProductGroupPriceReadManager;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupPriceCommandManager;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountPriceRecalculateProcessor 단위 테스트")
class DiscountPriceRecalculateProcessorTest {

    @InjectMocks private DiscountPriceRecalculateProcessor sut;

    @Mock private ProductGroupPriceReadManager priceReadManager;
    @Mock private ProductGroupPriceCommandManager priceCommandManager;
    @Mock private DiscountPolicyReadManager policyReadManager;
    @Mock private ProductGroupPriceUpdateFactory priceUpdateFactory;

    @Nested
    @DisplayName("process() - 할인 가격 재계산")
    class ProcessTest {

        @Test
        @DisplayName("타겟에 해당하는 상품들의 가격을 재계산하여 일괄 갱신한다")
        void process_ValidTarget_RecalculatesAndPersistsAll() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            List<ProductGroupPriceRow> priceRows = DiscountDomainFixtures.priceRows();
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();
            List<ProductGroupPriceUpdateData> updates =
                    DiscountDomainFixtures.priceUpdateDataList();

            given(priceReadManager.findByTarget(targetType, targetId)).willReturn(priceRows);
            given(
                            policyReadManager.findApplicablePolicies(
                                    ArgumentMatchers.eq(targetType),
                                    ArgumentMatchers.eq(targetId),
                                    ArgumentMatchers.any(Instant.class)))
                    .willReturn(policies);
            given(priceUpdateFactory.createAll(priceRows, policies)).willReturn(updates);

            // when
            sut.process(targetType, targetId);

            // then
            then(priceReadManager).should().findByTarget(targetType, targetId);
            then(policyReadManager)
                    .should()
                    .findApplicablePolicies(
                            ArgumentMatchers.eq(targetType),
                            ArgumentMatchers.eq(targetId),
                            ArgumentMatchers.any(Instant.class));
            then(priceUpdateFactory).should().createAll(priceRows, policies);
            then(priceCommandManager).should().persistAll(updates);
        }

        @Test
        @DisplayName("타겟에 해당하는 상품이 없으면 이후 처리를 수행하지 않는다")
        void process_NoProductGroups_SkipsProcessing() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;

            given(priceReadManager.findByTarget(targetType, targetId)).willReturn(List.of());

            // when
            sut.process(targetType, targetId);

            // then
            then(priceReadManager).should().findByTarget(targetType, targetId);
            then(policyReadManager).shouldHaveNoInteractions();
            then(priceUpdateFactory).shouldHaveNoInteractions();
            then(priceCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("할인 정책이 없어도 가격 갱신 데이터를 생성하고 저장한다")
        void process_NoPolicies_StillCreatesAndPersistsUpdates() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;
            List<ProductGroupPriceRow> priceRows = DiscountDomainFixtures.priceRows();
            List<DiscountPolicy> emptyPolicies = DiscountDomainFixtures.emptyPolicies();
            List<ProductGroupPriceUpdateData> updates =
                    DiscountDomainFixtures.priceUpdateDataList();

            given(priceReadManager.findByTarget(targetType, targetId)).willReturn(priceRows);
            given(
                            policyReadManager.findApplicablePolicies(
                                    ArgumentMatchers.eq(targetType),
                                    ArgumentMatchers.eq(targetId),
                                    ArgumentMatchers.any(Instant.class)))
                    .willReturn(emptyPolicies);
            given(priceUpdateFactory.createAll(priceRows, emptyPolicies)).willReturn(updates);

            // when
            sut.process(targetType, targetId);

            // then
            then(priceCommandManager).should().persistAll(updates);
        }

        @Test
        @DisplayName("PRODUCT 타겟 유형으로도 정상 처리한다")
        void process_ProductTargetType_ProcessesCorrectly() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 200L;
            List<ProductGroupPriceRow> priceRows = List.of(DiscountDomainFixtures.priceRow());
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();
            List<ProductGroupPriceUpdateData> updates =
                    List.of(DiscountDomainFixtures.priceUpdateData());

            given(priceReadManager.findByTarget(targetType, targetId)).willReturn(priceRows);
            given(
                            policyReadManager.findApplicablePolicies(
                                    ArgumentMatchers.eq(targetType),
                                    ArgumentMatchers.eq(targetId),
                                    ArgumentMatchers.any(Instant.class)))
                    .willReturn(policies);
            given(priceUpdateFactory.createAll(priceRows, policies)).willReturn(updates);

            // when
            sut.process(targetType, targetId);

            // then
            then(priceCommandManager).should().persistAll(updates);
        }
    }
}
