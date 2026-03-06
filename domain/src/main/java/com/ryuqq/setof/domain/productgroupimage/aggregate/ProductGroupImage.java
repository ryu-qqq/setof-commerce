package com.ryuqq.setof.domain.productgroupimage.aggregate;

import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import java.time.Instant;

/**
 * ProductGroupImage - 상품그룹 이미지 Child Entity.
 *
 * <p>ProductGroup Aggregate 내부에서 관리되는 이미지 정보입니다.
 */
public class ProductGroupImage {

    private final ProductGroupImageId id;
    private final ImageType imageType;
    private final ImageUrl imageUrl;
    private int sortOrder;
    private Instant deletedAt;

    private ProductGroupImage(
            ProductGroupImageId id,
            ImageType imageType,
            ImageUrl imageUrl,
            int sortOrder,
            Instant deletedAt) {
        this.id = id;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.deletedAt = deletedAt;
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
        return new ProductGroupImage(
                ProductGroupImageId.forNew(), imageType, imageUrl, sortOrder, null);
    }

    /**
     * 영속성 계층에서 복원 (하위호환).
     *
     * @param id 식별자
     * @param imageType 이미지 유형
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @return 복원된 ProductGroupImage 인스턴스
     */
    public static ProductGroupImage reconstitute(
            ProductGroupImageId id, ImageType imageType, ImageUrl imageUrl, int sortOrder) {
        return new ProductGroupImage(id, imageType, imageUrl, sortOrder, null);
    }

    /**
     * 영속성 계층에서 복원 (deletedAt 포함).
     *
     * @param id 식별자
     * @param imageType 이미지 유형
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @param deletedAt 삭제 일시
     * @return 복원된 ProductGroupImage 인스턴스
     */
    public static ProductGroupImage reconstitute(
            ProductGroupImageId id,
            ImageType imageType,
            ImageUrl imageUrl,
            int sortOrder,
            Instant deletedAt) {
        return new ProductGroupImage(id, imageType, imageUrl, sortOrder, deletedAt);
    }

    // ========== Business Methods ==========

    public boolean isNew() {
        return id.isNew();
    }

    public boolean isThumbnail() {
        return imageType == ImageType.THUMBNAIL;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 정렬 순서 변경.
     *
     * @param newSortOrder 새 정렬 순서
     */
    public void updateSortOrder(int newSortOrder) {
        this.sortOrder = newSortOrder;
    }

    /**
     * 소프트 삭제 처리.
     *
     * @param now 삭제 시각
     */
    public void delete(Instant now) {
        this.deletedAt = now;
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

    public Instant deletedAt() {
        return deletedAt;
    }
}
