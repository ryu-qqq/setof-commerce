package com.ryuqq.setof.storage.legacy.composite.search.mapper;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.storage.legacy.composite.search.dto.LegacyWebSearchQueryDto;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSearchMapper - 레거시 Web 검색 Mapper.
 *
 * <p>QueryDto → Application Layer DTO 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSearchMapper {

    /** 검색 결과 QueryDto → ProductGroupListCompositeResult 변환. */
    public ProductGroupListCompositeResult toListCompositeResult(LegacyWebSearchQueryDto dto) {
        boolean soldOut = "Y".equals(dto.soldOutYn());
        boolean displayed = "Y".equals(dto.displayYn());
        String status = soldOut ? "SOLD_OUT" : (displayed ? "ACTIVE" : "INACTIVE");

        return new ProductGroupListCompositeResult(
                dto.productGroupId(),
                dto.sellerId(),
                null,
                dto.brandId(),
                dto.brandName(),
                dto.displayKoreanName(),
                dto.displayEnglishName(),
                dto.brandIconImageUrl(),
                0L,
                null,
                null,
                0,
                dto.productGroupName(),
                null,
                status,
                soldOut,
                displayed,
                dto.productImageUrl(),
                0,
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                dto.discountRate(),
                0,
                0,
                0,
                List.of(),
                dto.insertDate() != null
                        ? dto.insertDate().toInstant(ZoneOffset.of("+09:00"))
                        : null,
                dto.insertDate() != null
                        ? dto.insertDate().toInstant(ZoneOffset.of("+09:00"))
                        : null);
    }

    /** 검색 결과 QueryDto 목록 → ProductGroupListCompositeResult 목록 변환. */
    public List<ProductGroupListCompositeResult> toListCompositeResults(
            List<LegacyWebSearchQueryDto> dtos) {
        return dtos.stream().map(this::toListCompositeResult).toList();
    }
}
