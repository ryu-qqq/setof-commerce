package com.ryuqq.setof.application.discount.factory;

import com.ryuqq.setof.application.discount.internal.DiscountCalculator;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.DiscountedPrice;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품그룹 가격 갱신 데이터 팩토리.
 *
 * <p>할인 계산 결과를 가격 갱신 데이터로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceUpdateFactory {

    private final DiscountCalculator discountCalculator;

    public ProductGroupPriceUpdateFactory(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    /**
     * 상품그룹 가격 행 목록에 대해 할인을 적용하여 갱신 데이터 목록 생성.
     *
     * @param priceRows 상품그룹 가격 정보 목록
     * @param applicablePolicies 적용할 할인 정책 목록
     * @return 가격 갱신 데이터 목록
     */
    public List<ProductGroupPriceUpdateData> createAll(
            List<ProductGroupPriceRow> priceRows, List<DiscountPolicy> applicablePolicies) {
        return priceRows.stream().map(row -> create(row, applicablePolicies)).toList();
    }

    /**
     * 단일 상품그룹에 대한 가격 갱신 데이터 생성.
     *
     * @param row 상품그룹 가격 정보
     * @param applicablePolicies 적용할 할인 정책 목록
     * @return 가격 갱신 데이터
     */
    public ProductGroupPriceUpdateData create(
            ProductGroupPriceRow row, List<DiscountPolicy> applicablePolicies) {
        Money regularPrice = Money.of(row.regularPrice());
        Money currentPrice = Money.of(row.currentPrice());

        DiscountedPrice result =
                discountCalculator.calculate(regularPrice, currentPrice, applicablePolicies);

        return new ProductGroupPriceUpdateData(
                row.productGroupId(),
                result.salePrice().value(),
                result.totalDiscountRate(),
                result.directDiscountRate(currentPrice),
                result.directDiscountPrice(currentPrice).value(),
                result.appliedDiscounts());
    }
}
