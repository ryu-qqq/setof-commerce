package com.setof.connectly.module.product.dto.group.fetch;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.delivery.RefundNoticeDto;
import com.setof.connectly.module.product.dto.group.ClothesDetailDto;
import com.setof.connectly.module.product.dto.image.ProductImageDto;
import com.setof.connectly.module.product.dto.notice.ProductNoticeDto;
import com.setof.connectly.module.product.dto.review.ProductReviewDto;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.product.enums.option.OptionType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupFetchDto implements DiscountOffer {

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
    private ProductNoticeDto productNotices;
    private ProductReviewDto productReview;

    private Set<ProductFetchDto> products = new HashSet<>();
    private Set<ProductImageDto> productImages = new HashSet<>();
    private Set<ProductCategoryDto> categories = new HashSet<>();

    public void setCategories(Set<ProductCategoryDto> categories) {
        this.categories = categories;
    }

    public List<ProductFetchDto> getProducts() {
        return new ArrayList<>(products);
    }

    public String getMainImageUrl() {
        return productImages.stream()
                .filter(img -> img.getProductGroupImageType() == ProductGroupImageType.MAIN)
                .findFirst()
                .map(ProductImageDto::getImageUrl)
                .orElseThrow(() -> new RuntimeException("Main image not found for product group"));
    }

    @QueryProjection
    public ProductGroupFetchDto(
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
            ProductReviewDto productReview,
            String detailDescription,
            ProductNoticeDto productNotices,
            Set<ProductFetchDto> products,
            Set<ProductImageDto> productImages) {
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
        this.productReview = productReview;
        this.detailDescription = detailDescription;
        this.productNotices = productNotices;
        this.products = products;
        this.productImages = productImages;
    }

    @Override
    public long getSellerId() {
        return sellerId;
    }

    @Override
    public long getProductGroupId() {
        return productGroupId;
    }

    @Override
    public Price getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public void setShareRatio(double shareRatio) {}

    /**
     * 분리된 쿼리 결과를 조합하기 위한 생성자 (쿼리 최적화용)
     */
    public ProductGroupFetchDto(
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
            ProductReviewDto productReview,
            String detailDescription,
            ProductNoticeDto productNotices) {
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
        this.productReview = productReview;
        this.detailDescription = detailDescription;
        this.productNotices = productNotices;
        this.products = new HashSet<>();
        this.productImages = new HashSet<>();
    }

    public void setProducts(Set<ProductFetchDto> products) {
        this.products = products;
    }

    public void setProductImages(Set<ProductImageDto> productImages) {
        this.productImages = productImages;
    }
}
