package com.ryuqq.setof.adapter.out.persistence.composite.content.mapper;

import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.AutoProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.FixedProductThumbnailDto;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import org.springframework.stereotype.Component;

/**
 * ContentCompositeMapper - 콘텐츠 Composite DTO → Domain 변환 Mapper.
 *
 * <p>FixedProductThumbnailDto, AutoProductThumbnailDto를 ProductThumbnailSnapshot으로 변환한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentCompositeMapper {

    public ProductThumbnailSnapshot toSnapshot(FixedProductThumbnailDto dto) {
        int effectivePrice = effectivePrice(dto.currentPrice(), dto.salePrice());
        return new ProductThumbnailSnapshot(
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.imageUrl(),
                dto.displayName(),
                dto.displayImageUrl(),
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                calcDirectDiscountRate(dto.currentPrice(), dto.salePrice()),
                calcDirectDiscountPrice(dto.currentPrice(), dto.salePrice()),
                calcDiscountRate(dto.regularPrice(), effectivePrice),
                dto.createdAt(),
                true,
                false);
    }

    public ProductThumbnailSnapshot toSnapshot(AutoProductThumbnailDto dto) {
        int effectivePrice = effectivePrice(dto.currentPrice(), dto.salePrice());
        return new ProductThumbnailSnapshot(
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.imageUrl(),
                null,
                null,
                dto.regularPrice(),
                dto.currentPrice(),
                dto.salePrice(),
                calcDirectDiscountRate(dto.currentPrice(), dto.salePrice()),
                calcDirectDiscountPrice(dto.currentPrice(), dto.salePrice()),
                calcDiscountRate(dto.regularPrice(), effectivePrice),
                dto.createdAt(),
                true,
                false);
    }

    private int effectivePrice(int currentPrice, int salePrice) {
        return (salePrice > 0 && salePrice < currentPrice) ? salePrice : currentPrice;
    }

    private int calcDiscountRate(int regularPrice, int effectivePrice) {
        if (regularPrice <= 0 || effectivePrice >= regularPrice) {
            return 0;
        }
        return (regularPrice - effectivePrice) * 100 / regularPrice;
    }

    private int calcDirectDiscountRate(int currentPrice, int salePrice) {
        if (currentPrice <= 0 || salePrice <= 0 || salePrice >= currentPrice) {
            return 0;
        }
        return (currentPrice - salePrice) * 100 / currentPrice;
    }

    private int calcDirectDiscountPrice(int currentPrice, int salePrice) {
        if (currentPrice <= 0 || salePrice <= 0 || salePrice >= currentPrice) {
            return 0;
        }
        return currentPrice - salePrice;
    }
}
