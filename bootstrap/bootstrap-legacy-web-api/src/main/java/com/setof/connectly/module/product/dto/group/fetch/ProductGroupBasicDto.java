package com.setof.connectly.module.product.dto.group.fetch;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.delivery.RefundNoticeDto;
import com.setof.connectly.module.product.dto.group.ClothesDetailDto;
import com.setof.connectly.module.product.dto.notice.ProductNoticeDto;
import com.setof.connectly.module.product.dto.review.ProductReviewDto;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.enums.option.OptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 그룹 기본 정보 DTO (1:1 관계 테이블들만 조회)
 * - 성능 최적화를 위해 13 테이블 JOIN에서 분리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupBasicDto {

    private long productGroupId;
    private String productGroupName;
    private long sellerId;
    private String sellerName;
    private BrandDto brand;
    private long categoryId;
    private String path;
    private Price price;
    private OptionType optionType;
    private ClothesDetailDto clothesDetail;
    private ProductStatus productStatus;
    private DeliveryNotice deliveryNotice;
    private RefundNoticeDto refundNotice;
    private String detailDescription;
    private ProductNoticeDto productNotice;
    private ProductReviewDto productReview;

    @QueryProjection
    public ProductGroupBasicDto(
            long productGroupId,
            String productGroupName,
            long sellerId,
            String sellerName,
            BrandDto brand,
            long categoryId,
            String path,
            Price price,
            OptionType optionType,
            ClothesDetailDto clothesDetail,
            ProductStatus productStatus,
            DeliveryNotice deliveryNotice,
            RefundNoticeDto refundNotice,
            String detailDescription,
            ProductNoticeDto productNotice,
            ProductReviewDto productReview) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.brand = brand;
        this.categoryId = categoryId;
        this.path = path;
        this.price = price;
        this.optionType = optionType;
        this.clothesDetail = clothesDetail;
        this.productStatus = productStatus;
        this.deliveryNotice = deliveryNotice;
        this.refundNotice = refundNotice;
        this.detailDescription = detailDescription;
        this.productNotice = productNotice;
        this.productReview = productReview;
    }
}
