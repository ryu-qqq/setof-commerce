package com.ryuqq.setof.adapter.out.persistence.cart.mapper;

import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartOptionQueryDto;
import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartQueryDto;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartOptionResult;
import com.ryuqq.setof.application.cart.dto.response.CartPriceResult;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * CartCompositeMapper - 장바구니 Composite 조회 결과 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartCompositeMapper {

    public CartItemResult toResult(CartQueryDto dto) {
        if (dto == null) {
            return null;
        }

        Set<CartOptionResult> options = toOptionResults(dto.options());
        String optionValue = buildOptionValue(dto.options());

        return CartItemResult.of(
                dto.cartId(),
                dto.brandId(),
                dto.brandName(),
                dto.productGroupId(),
                dto.productGroupName(),
                dto.sellerId(),
                dto.sellerName(),
                dto.productId(),
                toPriceResult(dto),
                dto.quantity(),
                dto.stockQuantity(),
                optionValue,
                options,
                dto.imageUrl(),
                dto.productStatus(),
                dto.categoryPath());
    }

    public List<CartItemResult> toResults(List<CartQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    public CartCountResult toCountResult(long count) {
        return CartCountResult.of(count);
    }

    private CartPriceResult toPriceResult(CartQueryDto dto) {
        return CartPriceResult.of(
                dto.regularPrice(), dto.currentPrice(), dto.salePrice(), 0, 0, dto.discountRate());
    }

    private Set<CartOptionResult> toOptionResults(Set<CartOptionQueryDto> options) {
        if (options == null) {
            return Set.of();
        }
        return options.stream()
                .filter(opt -> opt.optionGroupId() > 0 && opt.optionValueId() > 0)
                .map(
                        opt ->
                                CartOptionResult.of(
                                        opt.optionGroupId(),
                                        opt.optionValueId(),
                                        opt.optionGroupName(),
                                        opt.getSafeOptionValue()))
                .collect(Collectors.toSet());
    }

    /**
     * 옵션값 조합 문자열 생성 (예: "블랙 270").
     *
     * <p>옵션 그룹 ID 순으로 정렬하여 옵션값을 공백으로 연결.
     */
    private String buildOptionValue(Set<CartOptionQueryDto> options) {
        if (options == null || options.isEmpty()) {
            return "";
        }
        return options.stream()
                .filter(opt -> opt.optionGroupId() > 0)
                .sorted(Comparator.comparing(CartOptionQueryDto::optionGroupId))
                .map(CartOptionQueryDto::getSafeOptionValue)
                .filter(v -> !v.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
