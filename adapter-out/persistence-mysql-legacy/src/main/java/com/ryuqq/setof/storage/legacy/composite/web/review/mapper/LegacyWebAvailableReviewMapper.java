package com.ryuqq.setof.storage.legacy.composite.web.review.mapper;

import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebAvailableReviewQueryDto;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web 작성 가능한 리뷰 주문 Mapper.
 *
 * <p>QueryDto → Domain VO 변환.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebAvailableReviewMapper {

    private static final ZoneId LEGACY_ZONE = ZoneId.of("Asia/Seoul");

    public ReviewableOrder toDomain(LegacyWebAvailableReviewQueryDto dto) {
        if (dto == null) {
            return null;
        }

        return new ReviewableOrder(
                dto.orderId(),
                null,
                dto.paymentId(),
                null,
                new ReviewableOrder.SellerSnapshot(dto.sellerId(), dto.sellerName()),
                new ReviewableOrder.ProductSnapshot(
                        dto.productId(),
                        dto.productGroupId(),
                        dto.productGroupName(),
                        dto.productGroupMainImageUrl()),
                new ReviewableOrder.BrandSnapshot(dto.brandId(), dto.brandName()),
                dto.productQuantity(),
                dto.orderStatus(),
                dto.regularPrice(),
                dto.currentPrice(),
                dto.orderAmount(),
                List.of(),
                dto.paymentDate() != null
                        ? dto.paymentDate().atZone(LEGACY_ZONE).toInstant()
                        : null);
    }

    public List<ReviewableOrder> toDomainList(List<LegacyWebAvailableReviewQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toDomain).toList();
    }
}
