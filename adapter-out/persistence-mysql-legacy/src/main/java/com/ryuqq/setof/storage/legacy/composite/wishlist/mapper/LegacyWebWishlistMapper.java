package com.ryuqq.setof.storage.legacy.composite.wishlist.mapper;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.storage.legacy.composite.wishlist.dto.LegacyWebWishlistQueryDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebWishlistMapper - 찜 목록 복합 조회 Mapper.
 *
 * <p>QueryDto -> Application Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebWishlistMapper {

    public WishlistItemResult toResult(LegacyWebWishlistQueryDto dto) {
        return WishlistItemResult.of(
                dto.userFavoriteId(),
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.imageUrl(),
                BigDecimal.valueOf(dto.regularPrice()),
                BigDecimal.valueOf(dto.currentPrice()),
                dto.discountRate(),
                dto.soldOutYn(),
                dto.displayYn(),
                dto.insertDate());
    }

    public List<WishlistItemResult> toResults(List<LegacyWebWishlistQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
