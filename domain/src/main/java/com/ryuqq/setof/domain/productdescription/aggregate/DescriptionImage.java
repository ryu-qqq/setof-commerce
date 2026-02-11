package com.ryuqq.setof.domain.productdescription.aggregate;

import com.ryuqq.setof.domain.productdescription.id.DescriptionImageId;

/**
 * DescriptionImage - 상세설명 이미지 Child Entity.
 *
 * <p>ProductGroupDescription Aggregate 내부에서 관리되는 이미지 정보입니다.
 */
public class DescriptionImage {

    private final DescriptionImageId id;
    private final String imageUrl;
    private final int sortOrder;

    private DescriptionImage(DescriptionImageId id, String imageUrl, int sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    /**
     * 새 상세설명 이미지 생성.
     *
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @return 새 DescriptionImage 인스턴스
     */
    public static DescriptionImage forNew(String imageUrl, int sortOrder) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다");
        }
        return new DescriptionImage(DescriptionImageId.forNew(), imageUrl.trim(), sortOrder);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @param id 식별자
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     * @return 복원된 DescriptionImage 인스턴스
     */
    public static DescriptionImage reconstitute(
            DescriptionImageId id, String imageUrl, int sortOrder) {
        return new DescriptionImage(id, imageUrl, sortOrder);
    }

    public boolean isNew() {
        return id.isNew();
    }

    // ========== Accessor Methods ==========

    public DescriptionImageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String imageUrl() {
        return imageUrl;
    }

    public int sortOrder() {
        return sortOrder;
    }
}
