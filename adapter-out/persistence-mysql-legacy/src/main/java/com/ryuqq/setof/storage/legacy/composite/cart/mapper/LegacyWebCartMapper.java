package com.ryuqq.setof.storage.legacy.composite.cart.mapper;

import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartOptionResult;
import com.ryuqq.setof.application.cart.dto.response.CartPriceResult;
import com.ryuqq.setof.storage.legacy.composite.cart.dto.LegacyWebCartOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.cart.dto.LegacyWebCartQueryDto;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCartMapper - 레거시 장바구니 Mapper.
 *
 * <p>QueryDto → Application Result 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCartMapper {

    public CartItemResult toResult(LegacyWebCartQueryDto dto) {
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
                dto.getProductStatus(),
                dto.categoryPath());
    }

    public List<CartItemResult> toResults(List<LegacyWebCartQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    public CartCountResult toCountResult(long count) {
        return CartCountResult.of(count);
    }

    private CartPriceResult toPriceResult(LegacyWebCartQueryDto dto) {
        return CartPriceResult.of(
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                dto.directDiscountRate(),
                dto.directDiscountPrice(),
                dto.discountRate());
    }

    private Set<CartOptionResult> toOptionResults(Set<LegacyWebCartOptionQueryDto> options) {
        if (options == null) {
            return Set.of();
        }
        return options.stream()
                .filter(opt -> opt.optionGroupId() != null && opt.optionDetailId() != null)
                .map(
                        opt ->
                                CartOptionResult.of(
                                        opt.optionGroupId(),
                                        opt.optionDetailId(),
                                        opt.optionName(),
                                        opt.getSafeOptionValue()))
                .collect(Collectors.toSet());
    }

    /**
     * 옵션값 조합 문자열 생성 (예: "블랙 270").
     *
     * <p>옵션 그룹 ID 순으로 정렬하여 옵션값을 공백으로 연결.
     */
    private String buildOptionValue(Set<LegacyWebCartOptionQueryDto> options) {
        if (options == null || options.isEmpty()) {
            return "";
        }
        return options.stream()
                .filter(opt -> opt.optionGroupId() != null)
                .sorted(Comparator.comparing(LegacyWebCartOptionQueryDto::optionGroupId))
                .map(LegacyWebCartOptionQueryDto::getSafeOptionValue)
                .filter(v -> !v.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
