package com.ryuqq.setof.application.discount.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.internal.DiscountCalculator;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.DiscountedPrice;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupPriceUpdateFactory 단위 테스트")
class ProductGroupPriceUpdateFactoryTest {

    @InjectMocks private ProductGroupPriceUpdateFactory sut;

    @Mock private DiscountCalculator discountCalculator;

    @Nested
    @DisplayName("create() - 단건 가격 갱신 데이터 생성")
    class CreateTest {

        @Test
        @DisplayName("단일 ProductGroupPriceRow에 대한 가격 갱신 데이터를 생성한다")
        void create_ValidRow_ReturnsUpdateData() {
            // given
            ProductGroupPriceRow row = DiscountDomainFixtures.priceRow(100L, 100000, 90000);
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();
            DiscountedPrice discountedPrice = DiscountedPrice.of(Money.of(85000), 15, List.of());

            given(discountCalculator.calculate(Money.of(100000), Money.of(90000), policies))
                    .willReturn(discountedPrice);

            // when
            ProductGroupPriceUpdateData result = sut.create(row, policies);

            // then
            assertThat(result.productGroupId()).isEqualTo(100L);
            assertThat(result.salePrice()).isEqualTo(85000);
            assertThat(result.discountRate()).isEqualTo(15);
            then(discountCalculator)
                    .should()
                    .calculate(Money.of(100000), Money.of(90000), policies);
        }

        @Test
        @DisplayName("할인 정책이 없으면 현재가를 그대로 반환한다")
        void create_NoPolicies_ReturnsSamePriceAsCurrentPrice() {
            // given
            ProductGroupPriceRow row = DiscountDomainFixtures.priceRow(100L, 100000, 90000);
            List<DiscountPolicy> emptyPolicies = DiscountDomainFixtures.emptyPolicies();
            DiscountedPrice noDiscount = DiscountedPrice.noDiscount(Money.of(90000));

            given(discountCalculator.calculate(Money.of(100000), Money.of(90000), emptyPolicies))
                    .willReturn(noDiscount);

            // when
            ProductGroupPriceUpdateData result = sut.create(row, emptyPolicies);

            // then
            assertThat(result.salePrice()).isEqualTo(90000);
            assertThat(result.discountRate()).isZero();
        }
    }

    @Nested
    @DisplayName("createAll() - 복수 가격 갱신 데이터 생성")
    class CreateAllTest {

        @Test
        @DisplayName("여러 ProductGroupPriceRow에 대한 가격 갱신 데이터 목록을 생성한다")
        void createAll_MultipleRows_ReturnsUpdateDataList() {
            // given
            List<ProductGroupPriceRow> rows =
                    List.of(
                            DiscountDomainFixtures.priceRow(100L, 100000, 90000),
                            DiscountDomainFixtures.priceRow(101L, 80000, 80000));
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();
            DiscountedPrice discountedPrice1 = DiscountedPrice.of(Money.of(85000), 15, List.of());
            DiscountedPrice discountedPrice2 = DiscountedPrice.of(Money.of(75000), 6, List.of());

            given(discountCalculator.calculate(Money.of(100000), Money.of(90000), policies))
                    .willReturn(discountedPrice1);
            given(discountCalculator.calculate(Money.of(80000), Money.of(80000), policies))
                    .willReturn(discountedPrice2);

            // when
            List<ProductGroupPriceUpdateData> results = sut.createAll(rows, policies);

            // then
            assertThat(results).hasSize(2);
            assertThat(results.get(0).productGroupId()).isEqualTo(100L);
            assertThat(results.get(1).productGroupId()).isEqualTo(101L);
            then(discountCalculator)
                    .should()
                    .calculate(Money.of(100000), Money.of(90000), policies);
            then(discountCalculator).should().calculate(Money.of(80000), Money.of(80000), policies);
        }

        @Test
        @DisplayName("빈 목록이면 빈 결과를 반환한다")
        void createAll_EmptyRows_ReturnsEmptyList() {
            // given
            List<ProductGroupPriceRow> emptyRows = List.of();
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();

            // when
            List<ProductGroupPriceUpdateData> results = sut.createAll(emptyRows, policies);

            // then
            assertThat(results).isEmpty();
            then(discountCalculator).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("단건 목록도 정상적으로 처리한다")
        void createAll_SingleRow_ReturnsSingleUpdateData() {
            // given
            List<ProductGroupPriceRow> singleRow =
                    List.of(DiscountDomainFixtures.priceRow(100L, 100000, 90000));
            List<DiscountPolicy> policies = DiscountDomainFixtures.applicablePolicies();
            DiscountedPrice discountedPrice = DiscountedPrice.of(Money.of(85000), 15, List.of());

            given(discountCalculator.calculate(Money.of(100000), Money.of(90000), policies))
                    .willReturn(discountedPrice);

            // when
            List<ProductGroupPriceUpdateData> results = sut.createAll(singleRow, policies);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).productGroupId()).isEqualTo(100L);
        }
    }

    @Nested
    @DisplayName("ProductGroupPriceUpdateFactory (실제 DiscountCalculator 사용)")
    class IntegrationWithRealCalculatorTest {

        private ProductGroupPriceUpdateFactory realSut;

        @BeforeEach
        void setUp() {
            realSut = new ProductGroupPriceUpdateFactory(new DiscountCalculator());
        }

        @Test
        @DisplayName("실제 DiscountCalculator로 FIXED_AMOUNT 할인을 적용한다")
        void create_WithRealCalculator_AppliesDiscount() {
            // given
            ProductGroupPriceRow row = new ProductGroupPriceRow(100L, 100000, 100000);
            List<DiscountPolicy> policies =
                    List.of(
                            DiscountFixtures.fixedAmountPolicy(
                                    1L, 5000, StackingGroup.SELLER_INSTANT));

            // when
            ProductGroupPriceUpdateData result = realSut.create(row, policies);

            // then
            assertThat(result.productGroupId()).isEqualTo(100L);
            assertThat(result.salePrice()).isEqualTo(95000);
            assertThat(result.discountRate()).isEqualTo(5);
        }

        @Test
        @DisplayName("할인 정책이 없으면 현재가를 그대로 사용한다")
        void create_WithRealCalculatorNoPolicies_ReturnsSamePriceAsCurrentPrice() {
            // given
            ProductGroupPriceRow row = new ProductGroupPriceRow(100L, 100000, 90000);
            List<DiscountPolicy> emptyPolicies = List.of();

            // when
            ProductGroupPriceUpdateData result = realSut.create(row, emptyPolicies);

            // then
            assertThat(result.salePrice()).isEqualTo(90000);
            assertThat(result.discountRate()).isZero();
        }
    }
}
