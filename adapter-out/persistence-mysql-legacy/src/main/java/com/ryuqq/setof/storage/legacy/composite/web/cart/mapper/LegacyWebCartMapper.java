package com.ryuqq.setof.storage.legacy.composite.web.cart.mapper;

import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartCountResult;
import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartOptionResult;
import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartPriceResult;
import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartResult;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartQueryDto;
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
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCartMapper {

    /**
     * QueryDto → LegacyCartResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyCartResult
     */
    public LegacyCartResult toResult(LegacyWebCartQueryDto dto) {
        if (dto == null) {
            return null;
        }

        Set<LegacyCartOptionResult> options = toOptionResults(dto.options());
        String optionValue = buildOptionValue(dto.options());

        return LegacyCartResult.of(
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

    /**
     * QueryDto 목록 → LegacyCartResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyCartResult 목록
     */
    public List<LegacyCartResult> toResults(List<LegacyWebCartQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    /**
     * 카운트 결과 생성.
     *
     * @param userId 사용자 ID
     * @param count 장바구니 개수
     * @return LegacyCartCountResult
     */
    public LegacyCartCountResult toCountResult(long userId, long count) {
        return LegacyCartCountResult.of(userId, count);
    }

    /** 가격 정보 변환. */
    private LegacyCartPriceResult toPriceResult(LegacyWebCartQueryDto dto) {
        return LegacyCartPriceResult.of(dto.regularPrice(), dto.currentPrice(), dto.salePrice());
    }

    /** 옵션 QueryDto Set → OptionResult Set 변환. */
    private Set<LegacyCartOptionResult> toOptionResults(Set<LegacyWebCartOptionQueryDto> options) {
        if (options == null) {
            return Set.of();
        }
        return options.stream()
                .filter(opt -> opt.optionGroupId() != null && opt.optionDetailId() != null)
                .map(
                        opt ->
                                LegacyCartOptionResult.of(
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
