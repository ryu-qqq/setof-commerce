package com.ryuqq.setof.domain.displaycomponent.entity;

import com.ryuqq.setof.domain.displaycomponent.id.CuratedProductId;

/**
 * CuratedProduct - 큐레이팅된 상품 Entity (ProductCuration 소속).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class CuratedProduct {

    private final CuratedProductId id;
    private final long productGroupId;
    private String displayName;
    private String displayImage;
    private int displayOrder;

    private CuratedProduct(
            CuratedProductId id,
            long productGroupId,
            String displayName,
            String displayImage,
            int displayOrder) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.displayName = displayName;
        this.displayImage = displayImage;
        this.displayOrder = displayOrder;
    }

    public static CuratedProduct forNew(
            long productGroupId, String displayName, String displayImage, int displayOrder) {
        return new CuratedProduct(
                CuratedProductId.forNew(), productGroupId, displayName, displayImage, displayOrder);
    }

    public static CuratedProduct reconstitute(
            CuratedProductId id,
            long productGroupId,
            String displayName,
            String displayImage,
            int displayOrder) {
        return new CuratedProduct(id, productGroupId, displayName, displayImage, displayOrder);
    }

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public CuratedProductId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public long productGroupId() {
        return productGroupId;
    }

    public String displayName() {
        return displayName;
    }

    public String displayImage() {
        return displayImage;
    }

    public int displayOrder() {
        return displayOrder;
    }
}
