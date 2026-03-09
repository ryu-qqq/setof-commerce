package com.ryuqq.setof.application.discount.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
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
@DisplayName("LegacyProductGroupPriceReadManager 단위 테스트")
class LegacyProductGroupPriceReadManagerTest {

    @InjectMocks private LegacyProductGroupPriceReadManager sut;

    @Mock private LegacyProductGroupPriceQueryPort priceQueryPort;

    @Nested
    @DisplayName("findByTarget() - 타겟의 상품그룹 가격 조회")
    class FindByTargetTest {

        @Test
        @DisplayName("SELLER 타겟에 해당하는 상품그룹 가격 정보를 반환한다")
        void findByTarget_SellerTarget_ReturnsPriceRows() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            List<ProductGroupPriceRow> priceRows = DiscountDomainFixtures.priceRows();

            given(priceQueryPort.findByTarget(targetType, targetId)).willReturn(priceRows);

            // when
            List<ProductGroupPriceRow> result = sut.findByTarget(targetType, targetId);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(priceRows);
            then(priceQueryPort).should().findByTarget(targetType, targetId);
        }

        @Test
        @DisplayName("BRAND 타겟에 해당하는 상품그룹 가격 정보를 반환한다")
        void findByTarget_BrandTarget_ReturnsPriceRows() {
            // given
            DiscountTargetType targetType = DiscountTargetType.BRAND;
            long targetId = 5L;
            List<ProductGroupPriceRow> priceRows = List.of(DiscountDomainFixtures.priceRow());

            given(priceQueryPort.findByTarget(targetType, targetId)).willReturn(priceRows);

            // when
            List<ProductGroupPriceRow> result = sut.findByTarget(targetType, targetId);

            // then
            assertThat(result).hasSize(1);
            then(priceQueryPort).should().findByTarget(targetType, targetId);
        }

        @Test
        @DisplayName("타겟에 해당하는 상품그룹이 없으면 빈 목록을 반환한다")
        void findByTarget_NoProductGroups_ReturnsEmptyList() {
            // given
            DiscountTargetType targetType = DiscountTargetType.PRODUCT;
            long targetId = 999L;

            given(priceQueryPort.findByTarget(targetType, targetId)).willReturn(List.of());

            // when
            List<ProductGroupPriceRow> result = sut.findByTarget(targetType, targetId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("조회된 가격 정보에 productGroupId, regularPrice, currentPrice가 포함된다")
        void findByTarget_ValidRow_ContainsCorrectFields() {
            // given
            DiscountTargetType targetType = DiscountTargetType.SELLER;
            long targetId = 1L;
            ProductGroupPriceRow expectedRow =
                    DiscountDomainFixtures.priceRow(
                            DiscountDomainFixtures.DEFAULT_PRODUCT_GROUP_ID,
                            DiscountDomainFixtures.DEFAULT_REGULAR_PRICE,
                            DiscountDomainFixtures.DEFAULT_CURRENT_PRICE);

            given(priceQueryPort.findByTarget(targetType, targetId))
                    .willReturn(List.of(expectedRow));

            // when
            List<ProductGroupPriceRow> result = sut.findByTarget(targetType, targetId);

            // then
            assertThat(result.get(0).productGroupId())
                    .isEqualTo(DiscountDomainFixtures.DEFAULT_PRODUCT_GROUP_ID);
            assertThat(result.get(0).regularPrice())
                    .isEqualTo(DiscountDomainFixtures.DEFAULT_REGULAR_PRICE);
            assertThat(result.get(0).currentPrice())
                    .isEqualTo(DiscountDomainFixtures.DEFAULT_CURRENT_PRICE);
        }
    }
}
