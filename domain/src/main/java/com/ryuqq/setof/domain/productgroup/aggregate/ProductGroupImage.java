package com.ryuqq.setof.domain.productgroup.aggregate;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupImageId;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;

/**
 * ProductGroupImage - 상품그룹 이미지 Child Entity.
 *
 * <p>ProductGroup Aggregate 내부에서 관리되는 이미지 정보입니다.
 */
public class ProductGroupImage {

    private final ProductGroupImageId id;
    private final ImageType imageType;
    private final ImageUrl imageUrl;
    private final int sortOrder;

    private ProductGroupImage(
            ProductGroupImageId id, ImageType imageType, ImageUrl imageUrl, int sortOrder) {
        this.id = id;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    /**
     * 새 이미지 생성.
     *
     * @param imageType 이미지 유형
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @return 새 ProductGroupImage 인스턴스
     */
    public static ProductGroupImage forNew(ImageType imageType, ImageUrl imageUrl, int sortOrder) {
        return new ProductGroupImage(ProductGroupImageId.forNew(), imageType, imageUrl, sortOrder);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @param id 식별자
     * @param imageType 이미지 유형
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @return 복원된 ProductGroupImage 인스턴스
     */
    public static ProductGroupImage reconstitute(
            ProductGroupImageId id, ImageType imageType, ImageUrl imageUrl, int sortOrder) {
        return new ProductGroupImage(id, imageType, imageUrl, sortOrder);
    }

    public boolean isNew() {
        return id.isNew();
    }

    public boolean isThumbnail() {
        return imageType.isThumbnail();
    }

    // ========== Accessor Methods ==========

    public ProductGroupImageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ImageType imageType() {
        return imageType;
    }

    public ImageUrl imageUrl() {
        return imageUrl;
    }

    public String imageUrlValue() {
        return imageUrl.value();
    }

    public int sortOrder() {
        return sortOrder;
    }
}
